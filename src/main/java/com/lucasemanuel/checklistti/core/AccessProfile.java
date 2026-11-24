package com.lucasemanuel.checklistti.core;

import java.util.List;

public record AccessProfile(
        String key,
        List<String> systems,
        List<String> groups,
        List<String> equipment,
        List<String> notes
) {
    public AccessProfile {
        systems = List.copyOf(systems);
        groups = List.copyOf(groups);
        equipment = List.copyOf(equipment);
        notes = List.copyOf(notes);
    }
}
