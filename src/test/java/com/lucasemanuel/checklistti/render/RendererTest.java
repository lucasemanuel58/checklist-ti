package com.lucasemanuel.checklistti.render;

import com.lucasemanuel.checklistti.core.ChecklistGenerator;
import com.lucasemanuel.checklistti.core.Employee;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

class RendererTest {
    private final ChecklistGenerator generator = new ChecklistGenerator();

    @Test
    void markdownEscapaPipesENovasLinhas() {
        var employee = new Employee("Maria | Silva\nConsultora", "Analista de TI", "TI", "2026-12-01");
        String markdown = new MarkdownRenderer().render(generator.onboarding(employee));

        assertTrue(markdown.contains("Maria \\| Silva Consultora"));
        assertFalse(markdown.contains("Maria | Silva"));
    }

    @Test
    void htmlEscapaTagsEAspas() {
        var employee = new Employee("<script>alert(\"x\")</script>", "Analista de TI", "TI", "2026-12-01");
        String html = new HtmlRenderer().render(generator.onboarding(employee));

        assertTrue(html.contains("&lt;script&gt;"));
        assertFalse(html.contains("<script>"));
        assertTrue(html.contains("&quot;x&quot;"));
    }

    @Test
    void htmlMarcaItensObrigatorios() {
        var employee = new Employee("Ana Lima", "Desenvolvedor", "TI", "2026-11-30");
        String html = new HtmlRenderer().render(generator.offboarding(employee));

        assertTrue(html.contains("class=\"obrigatorio\""));
        assertTrue(html.contains("type=\"checkbox\""));
        assertTrue(html.contains("SEC-01"));
    }
}
