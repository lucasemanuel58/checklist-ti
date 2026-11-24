package com.lucasemanuel.checklistti.cli;

import com.lucasemanuel.checklistti.core.Checklist;
import com.lucasemanuel.checklistti.core.ChecklistGenerator;
import com.lucasemanuel.checklistti.core.Employee;
import com.lucasemanuel.checklistti.render.HtmlRenderer;
import com.lucasemanuel.checklistti.render.MarkdownRenderer;
import com.lucasemanuel.checklistti.render.Renderer;

import java.io.IOException;
import java.io.PrintStream;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

public final class Cli {
    public static final String USAGE = """
            Uso:
              checklist onboard  --name "Maria Silva" --role "Analista de TI" --department TI --date 2026-12-01 [opções]
              checklist offboard --name "Maria Silva" --role "Analista de TI" --department TI --date 2026-12-01 [opções]

            Opções:
              --name        Nome do colaborador (obrigatório)
              --role        Cargo (obrigatório): analista de ti, desenvolvedor, administrativo ou outro
              --department  Departamento (opcional)
              --date        Data de início ou último dia (opcional)
              --format      markdown (padrão) ou html
              --output      Arquivo de saída (opcional; padrão: terminal)
              -h, --help    Mostra esta ajuda

            Cargos não reconhecidos usam o perfil geral. Todos os dados são fictícios.
            """;

    private static final Set<String> OPTIONS = Set.of(
            "--name", "--role", "--department", "--date", "--format", "--output");

    private Cli() {
    }

    public static int run(String[] args, PrintStream out, PrintStream err) {
        if (args.length == 0 || isHelp(args[0])) {
            out.print(USAGE);
            return 0;
        }

        String command = args[0];
        if (!command.equals("onboard") && !command.equals("offboard")) {
            err.println("Comando desconhecido: " + command);
            err.print(USAGE);
            return 2;
        }

        Map<String, String> values = new HashMap<>();
        for (int i = 1; i < args.length; i++) {
            String option = args[i];
            if (isHelp(option)) {
                out.print(USAGE);
                return 0;
            }
            if (!OPTIONS.contains(option)) {
                err.println("Opção desconhecida: " + option);
                return 2;
            }
            if (i + 1 >= args.length) {
                err.println("Falta valor para " + option);
                return 2;
            }
            values.put(option, args[++i]);
        }

        if (!values.containsKey("--name") || !values.containsKey("--role")) {
            err.println("--name e --role são obrigatórios.");
            return 2;
        }

        String format = values.getOrDefault("--format", "markdown");
        if (!format.equals("markdown") && !format.equals("html")) {
            err.println("Formato inválido: " + format + ". Use markdown ou html.");
            return 2;
        }

        var employee = new Employee(
                values.get("--name"),
                values.get("--role"),
                values.get("--department"),
                values.get("--date")
        );

        var generator = new ChecklistGenerator();
        Checklist checklist = command.equals("onboard")
                ? generator.onboarding(employee)
                : generator.offboarding(employee);

        Renderer renderer = format.equals("html") ? new HtmlRenderer() : new MarkdownRenderer();
        String content = renderer.render(checklist);

        String output = values.get("--output");
        if (output == null) {
            out.println(content);
            return 0;
        }

        try {
            Files.writeString(Path.of(output), content, StandardCharsets.UTF_8);
            out.println("Checklist gravado em " + output);
            return 0;
        } catch (IOException | RuntimeException exception) {
            err.println("Não foi possível gravar " + output + ": " + exception.getMessage());
            return 1;
        }
    }

    private static boolean isHelp(String value) {
        return value.equals("-h") || value.equals("--help");
    }
}
