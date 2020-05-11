package com.srnjak.jpa.pagination;

import com.github.tennaito.rsql.jpa.JpaPredicateVisitor;
import com.srnjak.sortbox.bean.BeanSortBox;
import com.srnjak.sortbox.bean.plugins.CompactSort;
import cz.jirutka.rsql.parser.RSQLParser;
import cz.jirutka.rsql.parser.ast.Node;
import cz.jirutka.rsql.parser.ast.RSQLVisitor;
import lombok.*;

import javax.persistence.EntityManager;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import java.util.Optional;

/**
 * Implementation for keyset pagination page request -
 * {@link KeysetPageRequest}.
 *
 * <br>
 * <br>
 * It uses RSQL notation for filter input and compact sort string for sorting.
 */
@AllArgsConstructor
@Getter
@EqualsAndHashCode
@ToString
@Builder
public class RsqlKeysetPageRequest implements KeysetPageRequest {

    /**
     * Filter written in RSQL
     */
    String filter;

    /**
     * Sort written in compact sort string (e.g. 'name,-lastname')
     */
    String sort;

    /**
     * Identity object after which data should be searched for
     */
    Object after;

    /**
     * Maximum number of results to be returned
     */
    @Builder.Default
    @NonNull
    Integer limit = DEFAULT_LIMIT;

    /**
     * Direction to search of data after
     * the {@link RsqlKeysetPageRequest#after} value.
     */
    @Builder.Default
    @NonNull
    Direction direction = Direction.FORWARD;

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean isDirectionBackward() {
        return Direction.BACKWARD.equals(this.direction);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean isDirectionForward() {
        return Direction.FORWARD.equals(this.direction);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Predicate getQuery(Root<?> root, EntityManager em) {

        CriteriaBuilder cb = em.getCriteriaBuilder();

        return Optional.ofNullable(this.filter)
                .map(q -> {
                    RSQLVisitor<Predicate, EntityManager> visitor =
                            new JpaPredicateVisitor<>().defineRoot(root);

                    Node rootNode = new RSQLParser().parse(this.filter);
                    return rootNode.accept(visitor, em);
                })
                .orElse(cb.conjunction());
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public <O> BeanSortBox<O> getSort() {
        return BeanSortBox.from(this.sort, new CompactSort<>());
    }
}
