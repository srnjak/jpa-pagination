package com.srnjak.jpa.pagination;

import com.srnjak.sortbox.PropertySortElement;
import com.srnjak.sortbox.SortOrder;
import com.srnjak.sortbox.bean.BeanSortBox;
import com.srnjak.sortbox.bean.plugins.jpa.CriteriaOrderWriter;
import lombok.Setter;
import lombok.experimental.Accessors;
import org.apache.commons.beanutils.PropertyUtils;
import org.apache.commons.lang3.StringUtils;

import javax.persistence.EntityManager;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.*;
import java.lang.reflect.InvocationTargetException;
import java.util.*;
import java.util.function.BiFunction;
import java.util.stream.Collectors;

/**
 * Helper class for build and execute query for a keyset pagination.
 *
 * @param <Y> The type of the entity
 */
@Accessors(fluent = true)
public class KeysetPagination<Y> {

    /**
     * The default identity field.
     */
    public static final String DEFAULT_ID_FIELD = "id";

    /**
     * Provides an instance of this class.
     *
     * @param aClass The entity class
     * @param em The entity manager
     * @param <Y> The type of the entity
     * @return Instance of the {@link KeysetPagination}.
     */
    public static <Y> KeysetPagination<Y> of(
            Class<Y> aClass, EntityManager em) {
        return new KeysetPagination<>(aClass, em);
    }

    /**
     * The entity manager
     */
    private final EntityManager em;

    /**
     * The criteria builder
     */
    private final CriteriaBuilder cb;

    /**
     * The entity class
     */
    private final Class<Y> aClass;

    /**
     * The criteria query
     */
    private final CriteriaQuery<Y> criteria;

    /**
     * The root for search
     */
    private final Root<Y> root;

    /**
     * The page request
     */
    @Setter
    private KeysetPageRequest pageRequest;

    /**
     * Supplier for additional filter
     */
    @Setter
    private BiFunction<CriteriaBuilder, Root<Y>, Predicate> additionalFilter;

    /**
     * The field for identity
     */
    @Setter
    private String idField = DEFAULT_ID_FIELD;

    /**
     * Constructor.
     *
     * @param aClass Entity class
     * @param em Entity manager
     */
    public KeysetPagination(Class<Y> aClass, EntityManager em) {
        this.em = em;
        this.cb = em.getCriteriaBuilder();

        this.aClass = aClass;

        this.criteria = cb.createQuery(aClass);
        this.root = criteria.from(aClass);
        this.additionalFilter = (cb, root) -> cb.conjunction();
    }

    /**
     * Search for the records in database for a desired page depending on
     * filter, sort order, where to start and limit.
     *
     * @return The list of results.
     */
    public List<Y> getResultList() {

        // Query predicate
        Predicate queryPredicate = pageRequest.getQuery(this.root, this.em);

        // Sorting
        BeanSortBox<Y> sortBoxInput =
                new BeanSortBox<>(pageRequest.<Y>getSort().getSortBox());
        sortBoxInput.addSortElement(this.idField, SortOrder.ASCENDING);

        BeanSortBox<Y> beanSortBox = Optional.of(pageRequest)
                .filter(KeysetPageRequest::isDirectionBackward)
                .map(p -> sortBoxInput.reverse())
                .orElse(sortBoxInput);

        // Get predicate for search the start of the result set depends on sort.
        Predicate startPredicate = Optional.ofNullable(pageRequest.getAfter())
                .map(s -> {
                    Y after = em.find(aClass, s);

                    return buildAfterPredicate(
                            beanSortBox,
                            after);
                })
                .orElse(cb.conjunction());

        // Make conjunction of all the predicates
        Predicate combinedPredicate = cb.and(
                additionalFilter.apply(cb, root),
                queryPredicate,
                startPredicate);

        // Build query
        TypedQuery<Y> query = em.createQuery(
                criteria.where(combinedPredicate)
                        .orderBy(beanSortBox.export(
                                CriteriaOrderWriter.forRoot(root, em))));

        Optional.ofNullable(pageRequest.getLimit())
                .ifPresent(query::setMaxResults);

        // Retrieve the results
        List<Y> results = query.getResultList();

        if (pageRequest.isDirectionBackward()) {
            Collections.reverse(results);
        }

        return results;
    }

    /**
     * Builds predicate for WHERE statement, considering after which
     * value to search.
     *
     * @param beanSortBox The {@link BeanSortBox} instance
     * @param after The after id
     * @return The predicate
     */
    private Predicate buildAfterPredicate(
            BeanSortBox<Y> beanSortBox,
            Y after) {

        Map<String, ?> valueMap = getBeanValues(beanSortBox, after);

        List<Predicate> eqPredicateList = buildEqPredicates(
                beanSortBox,
                valueMap);

        List<Predicate> gtLtPredicateList = buildGtLtPredicates(
                beanSortBox,
                valueMap);

        List<Predicate> orPredicateList = buildOrPredicates(
                beanSortBox,
                eqPredicateList,
                gtLtPredicateList);

        return cb.or(orPredicateList.toArray(new Predicate[]{}));
    }

    /**
     * Builds 'or' predicates combining given 'equal', 'greater than'
     * and 'less than' predicates based on a given {@link BeanSortBox} instance.
     *
     * @param beanSortBox The {@link BeanSortBox} instance
     * @param eqPredicateList The list of 'equal' predicates
     * @param gtLtPredicateList The list of 'greater than' and 'less than'
     *                          predicates.
     * @return The list of predicates
     */
    private List<Predicate> buildOrPredicates(
            BeanSortBox<Y> beanSortBox,
            List<Predicate> eqPredicateList,
            List<Predicate> gtLtPredicateList) {

        List<Predicate> orPredicateList = new ArrayList<>();
        int l = beanSortBox.size() -1;
        for (int i = 0; i < beanSortBox.size(); i++) {

            List<Predicate> andPredicateList = new ArrayList<>();
            for (int j = 0; j < l; j++) {
                andPredicateList.add(eqPredicateList.get(j));
            }
            andPredicateList.add(gtLtPredicateList.get(l));

            orPredicateList.add(
                    cb.and(andPredicateList.toArray(new Predicate[]{})));

            l--;
        }

        return orPredicateList;
    }

    /**
     * Builds predicates 'greater than' or 'less than' given values.
     *
     * @param beanSortBox The {@link BeanSortBox} instance
     * @param valueMap The map of values
     * @return The list of predicates
     */
    private List<Predicate> buildGtLtPredicates(
            BeanSortBox<Y> beanSortBox,
            Map<String, ?> valueMap) {

        return beanSortBox.stream()
                .map(el -> orderedPredicate(el, valueMap))
                .collect(Collectors.toList());
    }

    /**
     * Builds a 'greater than' or 'less than' predicate for a single
     * {@link PropertySortElement} instance, based on a given map of values.
     *
     * @param el The {@link PropertySortElement} instance
     * @param valueMap The map of values
     * @return The predicate
     */
    @SuppressWarnings("unchecked")
    private Predicate orderedPredicate(
            PropertySortElement<Y> el,
            Map<String, ?> valueMap) {

        Predicate p;

        if (el.isAscending()) {
            p = cb.greaterThan(
                    toPath(el.getSortBy()),
                    (Comparable<Object>) valueMap.get(el.getSortBy()));
        } else {
            p = cb.lessThan(
                    toPath(el.getSortBy()),
                    (Comparable<Object>) valueMap.get(el.getSortBy()));
        }

        return p;
    }

    /**
     * Builds predicates for equality with given values.
     *
     * @param beanSortBox The {@link BeanSortBox} instance
     * @param valueMap The map of values
     * @return The list of predicates
     */
    private List<Predicate> buildEqPredicates(
            BeanSortBox<Y> beanSortBox,
            Map<String, ?> valueMap) {

        return beanSortBox.stream()
                .map(el -> cb.equal(
                        root.get(el.getSortBy()),
                        valueMap.get(el.getSortBy())))
                .collect(Collectors.toList());
    }

    /**
     * Get values where {@link BeanSortBox} instance is pointing to from a bean.
     *
     * @param beanSortBox The {@link BeanSortBox} instance.
     * @param bean The bean instance.
     * @return The map of values.
     */
    private Map<String, ?> getBeanValues(
            BeanSortBox<Y> beanSortBox,
            Y bean) {

        return beanSortBox.stream()
                .map(el -> {
                    Object value;
                    try {
                        value = PropertyUtils.getProperty(
                                bean, el.getSortBy());
                    } catch (IllegalAccessException
                            | InvocationTargetException
                            | NoSuchMethodException e) {
                        throw new RuntimeException(e);
                    }

                    return new AbstractMap.SimpleImmutableEntry<>(
                            el.getSortBy(), value);
                })
                .collect(Collectors.toMap(
                        Map.Entry::getKey, Map.Entry::getValue));
    }

    /**
     * Creates a {@link Path} expression from a string.
     *
     * @param pathStr The path string.
     * @param <Z> The type referenced by the path
     * @return The {@link Path} expression
     */
    private <Z> Path<Z> toPath(String pathStr) {

        return Optional.of(pathStr)
                .filter(p -> p.contains("."))
                .map(p -> StringUtils.split(pathStr, "."))
                .map(f -> {
                    Path<Z> expression = root.get(f[0]);
                    for (int i = 1; i < f.length; i++) {
                        expression = expression.get(f[i]);
                    }
                    return expression;
                })
                .orElse(root.get(pathStr));
    }
}
