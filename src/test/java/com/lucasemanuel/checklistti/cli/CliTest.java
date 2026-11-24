package com.lucasemanuel.checklistti.cli;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

class CliTest {
    @TempDir
    Path temporario;

    @Test
    void helpRetornaZeroEExibeUso() {
        var out = new ByteArrayOutputStream();
        var err = new ByteArrayOutputStream();

        int code = Cli.run(new String[]{"--help"}, print(out), print(err));

        assertEquals(0, code);
        assertTrue(out.toString(StandardCharsets.UTF_8).contains("Uso:"));
    }

    @Test
    void semNomeRetornaErro() {
        var out = new ByteArrayOutputStream();
        var err = new ByteArrayOutputStream();

        int code = Cli.run(new String[]{"onboard", "--role", "TI"}, print(out), print(err));

        assertEquals(2, code);
        assertTrue(err.toString(StandardCharsets.UTF_8).contains("obrigatórios"));
    }

    @Test
    void comandoDesconhecidoRetornaErro() {
        var out = new ByteArrayOutputStream();
        var err = new ByteArrayOutputStream();

        int code = Cli.run(new String[]{"reiniciar"}, print(out), print(err));

        assertEquals(2, code);
        assertTrue(err.toString(StandardCharsets.UTF_8).contains("Comando desconhecido"));
    }

    @Test
    void formatoInvalidoRetornaErro() {
        var out = new ByteArrayOutputStream();
        var err = new ByteArrayOutputStream();

        int code = Cli.run(new String[]{
                "onboard", "--name", "Maria", "--role", "TI", "--format", "pdf"
        }, print(out), print(err));

        assertEquals(2, code);
        assertTrue(err.toString(StandardCharsets.UTF_8).contains("Formato inválido"));
    }

    @Test
    void imprimeMarkdownNoTerminal() {
        var out = new ByteArrayOutputStream();
        var err = new ByteArrayOutputStream();

        int code = Cli.run(new String[]{
                "onboard", "--name", "Maria Silva", "--role", "Analista de TI"
        }, print(out), print(err));

        assertEquals(0, code);
        assertTrue(out.toString(StandardCharsets.UTF_8).contains("# Onboarding — Maria Silva"));
        assertTrue(out.toString(StandardCharsets.UTF_8).contains("ACC-01"));
    }

    @Test
    void gravaHtmlEmArquivo() throws Exception {
        var out = new ByteArrayOutputStream();
        var err = new ByteArrayOutputStream();
        Path arquivo = temporario.resolve("offboarding.html");

        int code = Cli.run(new String[]{
                "offboard", "--name", "Ana Lima", "--role", "Desenvolvedor",
                "--date", "2026-11-30", "--format", "html", "--output", arquivo.toString()
        }, print(out), print(err));

        assertEquals(0, code);
        String conteudo = Files.readString(arquivo, StandardCharsets.UTF_8);
        assertTrue(conteudo.contains("<!doctype html>"));
        assertTrue(conteudo.contains("SEC-01"));
        assertFalse(conteudo.contains("<script>"));
    }

    private static PrintStream print(ByteArrayOutputStream stream) {
        return new PrintStream(stream, true, StandardCharsets.UTF_8);
    }
}
