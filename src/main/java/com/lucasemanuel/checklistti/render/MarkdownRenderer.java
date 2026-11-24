package com.lucasemanuel.checklistti.render;

import com.lucasemanuel.checklistti.core.Checklist;
import com.lucasemanuel.checklistti.core.ChecklistItem;

public final class MarkdownRenderer implements Renderer {

    @Override
    public String render(Checklist checklist) {
        var employee = checklist.employee();
        var out = new StringBuilder();

        out.append("# ").append(escape(checklist.title())).append("\n\n");
        out.append("**Colaborador:** ").append(escape(employee.name())).append("  \n");
        out.append("**Cargo:** ").append(escape(employee.role())).append("  \n");
        out.append("**Departamento:** ").append(escape(employee.department())).append("  \n");
        out.append("**Data:** ").append(escape(employee.date())).append("\n\n");

        out.append("## Itens\n\n");
        for (ChecklistItem item : checklist.items()) {
            out.append("- [ ] **")
                    .append(escape(item.id()))
                    .append(" · ")
                    .append(item.category().label())
                    .append("** ")
                    .append(escape(item.title()));
            if (item.required()) {
                out.append(" *(obrigatório)*");
            }
            out.append("\n  - ").append(escape(item.details())).append("\n");
        }

        out.append("\n---\n\n")
                .append(checklist.items().size())
                .append(" itens · ")
                .append(checklist.requiredCount())
                .append(" obrigatórios · Perfis e dados fictícios.\n");

        return out.toString();
    }

    /** Evita que texto digitado pelo usuário mude a formatação do Markdown. */
    static String escape(String value) {
        return value == null ? "" : value
                .replace("\\", "\\\\")
                .replace("|", "\\|")
                .replace("*", "\\*")
                .replace("_", "\\_")
                .replace("`", "\\`")
                .replace("\r", " ")
                .replace("\n", " ");
    }
}
