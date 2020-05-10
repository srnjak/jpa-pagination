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

@AllArgsConstructor
@Getter
@EqualsAndHashCode
@ToString
@Builder
public class RsqlKeysetPageRequest implements KeysetPageRequest {

    String filter;

    String sort;

    Object after;

    @Builder.Default
    @NonNull
    Integer limit = 20;

    @Builder.Default
    @NonNull
    Direction direction = Direction.FORWARD;

    @Override
    public boolean isDirectionBackward() {
        return Direction.BACKWARD.equals(this.direction);
    }

    @Override
    public boolean isDirectionForward() {
        return Direction.FORWARD.equals(this.direction);
    }

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

    @Override
    public <O> BeanSortBox<O> getSort() {
        return BeanSortBox.from(this.sort, new CompactSort<>());
    }
}
