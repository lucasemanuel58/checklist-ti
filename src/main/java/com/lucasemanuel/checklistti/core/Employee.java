package com.lucasemanuel.checklistti.core;

public record Employee(String name, String role, String department, String date) {

    public Employee {
        name = required(name, "name");
        role = required(role, "role");
        department = department == null || department.isBlank() ? "Não informado" : department.trim();
        date = date == null || date.isBlank() ? "Não informada" : date.trim();
    }

    private static String required(String value, String field) {
        if (value == null || value.isBlank()) {
            throw new IllegalArgumentException("Campo obrigatório: " + field);
        }
        return value.trim();
    }
}
