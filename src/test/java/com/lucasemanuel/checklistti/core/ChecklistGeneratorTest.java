package com.lucasemanuel.checklistti.core;

import org.junit.jupiter.api.Test;

import java.util.HashSet;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

class ChecklistGeneratorTest {
    private final ChecklistGenerator generator = new ChecklistGenerator();

    @Test
    void onboardingDeAnalistaUsaSistemasEGruposDoPerfil() {
        var employee = new Employee("Maria Silva", "Analista de TI", "TI", "2026-12-01");
        var checklist = generator.onboarding(employee);

        assertItemContem(checklist, "ACC-02", "TI-Analistas");
        assertItemContem(checklist, "ACC-03", "Sistema de chamados");
        assertItemContem(checklist, "EQP-01", "Notebook");
        assertTrue(checklist.title().contains("Maria Silva"));
    }

    @Test
    void cargoDesconhecidoUsaPerfilGeral() {
        var employee = new Employee("João Rocha", "Estagiário", "Administrativo", "2026-12-01");
        var checklist = generator.onboarding(employee);

        assertItemContem(checklist, "ACC-02", "Usuarios");
        assertItemContem(checklist, "ACC-03", "E-mail corporativo");
        assertItemContem(checklist, "EQP-01", "Computador");
    }

    @Test
    void offboardingIncluiDesativacaoERecolhimento() {
        var employee = new Employee("Ana Lima", "Desenvolvedor", "TI", "2026-11-30");
        var checklist = generator.offboarding(employee);

        assertItemContem(checklist, "SEC-01", "2026-11-30");
        assertItemContem(checklist, "ACC-01", "Dev-Team");
        assertItemContem(checklist, "ACC-02", "Banco de homologação");
        assertItemContem(checklist, "EQP-01", "Licença de IDE");
    }

    @Test
    void idsSaoUnicosETodosItensTemCategoria() {
        var employee = new Employee("Pedro Alves", "Administrativo", "Financeiro", "2026-12-01");

        for (var checklist : List.of(generator.onboarding(employee), generator.offboarding(employee))) {
            var ids = new HashSet<String>();
            checklist.items().forEach(item -> {
                assertTrue(ids.add(item.id()), "ID repetido: " + item.id());
                assertFalse(item.title().isBlank());
                assertNotNull(item.category());
            });
            assertTrue(checklist.requiredCount() > 0);
        }
    }

    @Test
    void normalizacaoDeCargoIgnoraAcentosECaixa() {
        assertEquals("analista de ti", ProfileCatalog.normalize("  ANALISTA DE TI  "));
        assertEquals("administrativo", ProfileCatalog.normalize("Administratívo"));
        assertEquals("analista de ti", new ProfileCatalog().forRole("Analista de TI").key());
        assertEquals("geral", new ProfileCatalog().forRole("Técnico de TI").key());
    }

    private static void assertItemContem(Checklist checklist, String id, String trecho) {
        var item = checklist.items().stream()
                .filter(candidate -> candidate.id().equals(id))
                .findFirst()
                .orElseThrow(() -> new AssertionError("Item não encontrado: " + id));
        assertTrue(item.details().contains(trecho),
                "Esperava '" + trecho + "' em " + id + ", mas encontrou: " + item.details());
    }
}
