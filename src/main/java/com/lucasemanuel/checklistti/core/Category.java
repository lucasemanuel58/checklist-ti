package com.lucasemanuel.checklistti.core;

public enum Category {
    ACCOUNT("Conta"),
    ACCESS("Acesso"),
    EQUIPMENT("Equipamento"),
    PROCESS("Processo"),
    SECURITY("Segurança"),
    COMMUNICATION("Comunicação");

    private final String label;

    Category(String label) {
        this.label = label;
    }

    public String label() {
        return label;
    }
}
