package com.example.specification;

import com.example.entity.Event;
import com.example.entity.Event.Category;
import org.springframework.data.jpa.domain.Specification;

import java.time.LocalDate;

public class EventSpecification {

    public static Specification<Event> hasNameLike(String name) {
        return (root, query, builder) -> {
            if (name == null || name.isEmpty()) return null;
            return builder.like(builder.lower(root.get("name")), "%" + name.toLowerCase() + "%");
        };
    }

    public static Specification<Event> hasCategory(Category category) {
        return (root, query, builder) -> {
            if (category == null) return null;
            return builder.equal(root.get("category"), category);
        };
    }

    public static Specification<Event> hasDate(LocalDate date) {
        return (root, query, builder) -> {
            if (date == null) return null;
            return builder.equal(root.get("date"), date);
        };
    }

    public static Specification<Event> hasCity(String city) {
        return (root, query, builder) -> {
            if (city == null || city.isEmpty()) return null;
            return builder.equal(root.get("city"), city);
        };
    }
}
