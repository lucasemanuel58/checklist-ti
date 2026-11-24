package com.lucasemanuel.checklistti.core;

import java.util.ArrayList;
import java.util.List;

public final class ChecklistGenerator {
    private final ProfileCatalog catalog;

    public ChecklistGenerator() {
        this(new ProfileCatalog());
    }

    public ChecklistGenerator(ProfileCatalog catalog) {
        this.catalog = catalog;
    }

    public Checklist onboarding(Employee employee) {
        AccessProfile profile = catalog.forRole(employee.role());
        List<ChecklistItem> items = new ArrayList<>();

        add(items, "ACC-01", Category.ACCOUNT, "Criar conta corporativa",
                "E-mail corporativo para " + employee.name() + ".", true);
        add(items, "ACC-02", Category.ACCESS, "Adicionar aos grupos de acesso",
                String.join(", ", profile.groups()), true);
        add(items, "ACC-03", Category.ACCESS, "Liberar sistemas",
                String.join(", ", profile.systems()), true);
        add(items, "EQP-01", Category.EQUIPMENT, "Entregar equipamentos",
                String.join(", ", profile.equipment()), true);
        add(items, "PRC-01", Category.PROCESS, "Registrar os equipamentos no inventário",
                "Incluir usuário, número de série e data de entrega.", false);

        for (int i = 0; i < profile.notes().size(); i++) {
            add(items, "PRC-0" + (i + 2), Category.PROCESS, "Validar permissões com o gestor",
                    profile.notes().get(i), false);
        }

        add(items, "SEG-01", Category.SECURITY, "Confirmar aceite da política de segurança",
                "Senha, autenticação em dois fatores e regras de uso aceitas por " + employee.name() + ".", true);
        add(items, "COM-01", Category.COMMUNICATION, "Apresentar ao time",
                "Enviar instruções iniciais e informar o gestor da equipe.", false);

        return new Checklist("Onboarding — " + employee.name(), employee, items);
    }

    public Checklist offboarding(Employee employee) {
        AccessProfile profile = catalog.forRole(employee.role());
        List<ChecklistItem> items = new ArrayList<>();

        add(items, "SEC-01", Category.SECURITY, "Desativar a conta corporativa",
                "Último dia: " + employee.date() + ". Confirmar com o gestor antes de desativar.", true);
        add(items, "ACC-01", Category.ACCESS, "Remover dos grupos de acesso",
                String.join(", ", profile.groups()), true);
        add(items, "ACC-02", Category.ACCESS, "Revogar licenças e acessos aos sistemas",
                String.join(", ", profile.systems()), true);
        add(items, "EQP-01", Category.EQUIPMENT, "Recolher equipamentos",
                String.join(", ", profile.equipment()), true);
        add(items, "PRC-01", Category.PROCESS, "Fazer backup e transferir arquivos",
                "Salvar os arquivos de " + employee.name() + " em local definido pelo gestor.", true);
        add(items, "PRC-02", Category.PROCESS, "Atualizar o inventário",
                "Dar baixa dos equipamentos e registrar a destinação de cada um.", false);
        add(items, "COM-01", Category.COMMUNICATION, "Comunicar a saída ao time",
                "Avisar a equipe e registrar a confirmação do desligamento.", false);

        return new Checklist("Offboarding — " + employee.name(), employee, items);
    }

    private static void add(List<ChecklistItem> items, String id, Category category,
                            String title, String details, boolean required) {
        items.add(new ChecklistItem(id, category, title, details, required));
    }
}
