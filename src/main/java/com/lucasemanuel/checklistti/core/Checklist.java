package com.lucasemanuel.checklistti.core;

import java.util.List;

public final class Checklist {
    private final String title;
    private final Employee employee;
    private final List<ChecklistItem> items;

    public Checklist(String title, Employee employee, List<ChecklistItem> items) {
        this.title = title;
        this.employee = employee;
        this.items = List.copyOf(items);
    }

    public String title() {
        return title;
    }

    public Employee employee() {
        return employee;
    }

    public List<ChecklistItem> items() {
        return items;
    }

    public long requiredCount() {
        return items.stream().filter(ChecklistItem::required).count();
    }
}
