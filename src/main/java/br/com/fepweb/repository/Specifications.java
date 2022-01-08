package br.com.fepweb.repository;

import org.springframework.data.jpa.domain.Specification;

import javax.persistence.criteria.Predicate;

public class Specifications {

    public static <T> Specification<T> allColumnsLike(String text) {
        if (!text.contains("%")) {
            text = "%" + text + "%";
        }
        final String finalText = text.toUpperCase();

        return (root, cq, builder) ->
                builder.or(root.getModel()
                        .getDeclaredSingularAttributes()
                        .stream()
                        .filter(a -> a.getJavaType()
                                .getSimpleName().equalsIgnoreCase("string"))
                        .map(a -> builder.like(builder.upper(root.get(a.getName())), finalText)
                        ).toArray(Predicate[]::new));
    }
}
