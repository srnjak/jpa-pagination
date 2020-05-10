package com.srnjak.jpa.pagination;

import com.srnjak.sortbox.bean.BeanSortBox;

import javax.persistence.EntityManager;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

public interface KeysetPageRequest extends PageRequest {

    Predicate getQuery(Root<?> root, EntityManager em);

    <O> BeanSortBox<O> getSort();

    Object getAfter();

    Integer getLimit();

    Direction getDirection();

    boolean isDirectionBackward();

    boolean isDirectionForward();
}
