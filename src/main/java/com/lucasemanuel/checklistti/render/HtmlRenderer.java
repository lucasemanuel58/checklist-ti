package com.lucasemanuel.checklistti.render;

import com.lucasemanuel.checklistti.core.Checklist;
import com.lucasemanuel.checklistti.core.ChecklistItem;

public final class HtmlRenderer implements Renderer {

    private static final String CSS = """
            body { font-family: system-ui, sans-serif; color: #0f172a; max-width: 760px;
                   margin: 40px auto; padding: 0 20px; line-height: 1.6; }
            h1 { font-size: 1.8rem; margin-bottom: 4px; }
            h2 { font-size: 1.1rem; margin-top: 28px; color: #0e7490; }
            table { border-collapse: collapse; margin-bottom: 24px; }
            th, td { text-align: left; padding: 6px 14px 6px 0; }
            th { color: #64748b; font-size: .85rem; }
            ul { list-style: none; padding: 0; }
            li { padding: 12px 14px; margin-bottom: 8px; border: 1px solid #e2e8f0;
                 border-radius: 10px; break-inside: avoid; }
            li.obrigatorio { border-left: 3px solid #0e7490; }
            strong { font-size: .95rem; }
            .tag { color: #0e7490; font-size: .8rem; font-weight: 600; }
            .detalhes { color: #475569; font-size: .9rem; margin-top: 4px; }
            footer { border-top: 1px solid #e2e8f0; margin-top: 32px; padding-top: 12px;
                     color: #64748b; font-size: .82rem; }
            @media print { body { margin: 0; } li { break-inside: avoid; } }
            """;

    @Override
    public String render(Checklist checklist) {
        var employee = checklist.employee();
        var out = new StringBuilder();

        out.append("<!doctype html>\n<html lang=\"pt-BR\">\n<head>\n")
                .append("<meta charset=\"utf-8\">\n")
                .append("<meta name=\"viewport\" content=\"width=device-width, initial-scale=1\">\n")
                .append("<title>").append(escape(checklist.title())).append("</title>\n")
                .append("<style>\n").append(CSS).append("\n</style>\n")
                .append("</head>\n<body>\n");

        out.append("<h1>").append(escape(checklist.title())).append("</h1>\n");
        out.append("<table>\n")
                .append("<tr><th>Colaborador</th><td>").append(escape(employee.name())).append("</td></tr>\n")
                .append("<tr><th>Cargo</th><td>").append(escape(employee.role())).append("</td></tr>\n")
                .append("<tr><th>Departamento</th><td>").append(escape(employee.department())).append("</td></tr>\n")
                .append("<tr><th>Data</th><td>").append(escape(employee.date())).append("</td></tr>\n")
                .append("</table>\n");

        out.append("<h2>Itens (").append(checklist.items().size()).append(")</h2>\n<ul>\n");
        for (ChecklistItem item : checklist.items()) {
            out.append("<li class=\"").append(item.required() ? "obrigatorio" : "").append("\">\n")
                    .append("<label><input type=\"checkbox\"> ")
                    .append("<strong>").append(escape(item.id())).append(" · ")
                    .append(escape(item.title())).append("</strong></label>\n")
                    .append("<div class=\"tag\">").append(item.category().label()).append("</div>\n")
                    .append("<div class=\"detalhes\">").append(escape(item.details())).append("</div>\n")
                    .append("</li>\n");
        }
        out.append("</ul>\n<footer>")
                .append(checklist.requiredCount())
                .append(" itens obrigatórios · Perfis e dados fictícios · Imprima ou salve como PDF pelo navegador.")
                .append("</footer>\n</body>\n</html>\n");

        return out.toString();
    }

    static String escape(String value) {
        if (value == null) {
            return "";
        }
        return value
                .replace("&", "&amp;")
                .replace("<", "&lt;")
                .replace(">", "&gt;")
                .replace("\"", "&quot;")
                .replace("'", "&#39;");
    }
}
