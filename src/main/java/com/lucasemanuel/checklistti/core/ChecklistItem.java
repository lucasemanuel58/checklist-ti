package com.lucasemanuel.checklistti.core;

public record ChecklistItem(String id, Category category, String title, String details, boolean required) {

    public ChecklistItem {
        if (id == null || id.isBlank()) {
            throw new IllegalArgumentException("Item sem id");
        }
        if (title == null || title.isBlank()) {
            throw new IllegalArgumentException("Item sem título: " + id);
        }
        if (category == null) {
            throw new IllegalArgumentException("Item sem categoria: " + id);
        }
    }
}
