package com.srnjak.jpa.pagination;

import com.srnjak.sortbox.bean.BeanSortBox;

import javax.persistence.EntityManager;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

/**
 * Page request for keyset pagination.
 */
public interface KeysetPageRequest extends PageRequest {

    /**
     * Gets query predicate to filter data.
     *
     * @param root The criteria query root
     * @param em The entity manager
     * @return The predicate
     */
    Predicate getQuery(Root<?> root, EntityManager em);

    /**
     * Gets {@link BeanSortBox} instance to sort the data.
     *
     * @param <O> Type of object for sorting
     * @return The {@link BeanSortBox} instance
     */
    <O> BeanSortBox<O> getSort();

    /**
     * Gets an identity object after which data should be returned.
     *
     * @return The identity object
     */
    Object getAfter();

    /**
     * Gets a limit of how many data should be return the most.
     *
     * @return The limit number
     */
    Integer getLimit();

    /**
     * Gets a direction to search of data after a
     * {@link KeysetPageRequest#getAfter} value.
     *
     * @return The direction
     */
    Direction getDirection();

    /**
     * Whether direction is backward;
     *
     * @return {@literal TRUE} if backward, {@literal FALSE} otherwise.
     */
    boolean isDirectionBackward();

    /**
     * Whether direction is forward;
     *
     * @return {@literal TRUE} if forward, {@literal FALSE} otherwise.
     */
    boolean isDirectionForward();
}
