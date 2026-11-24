package com.lucasemanuel.checklistti.core;

import java.text.Normalizer;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

/**
 * Perfis fictícios de acesso. Nenhum dado aqui vem de um ambiente real de trabalho.
 */
public final class ProfileCatalog {
    public static final AccessProfile GERAL = new AccessProfile(
            "geral",
            List.of("E-mail corporativo", "Pasta compartilhada"),
            List.of("Usuarios"),
            List.of("Computador", "Crachá"),
            List.of("Confirmar as permissões com o gestor antes da criação.")
    );

    private static final AccessProfile ANALISTA_TI = new AccessProfile(
            "analista de ti",
            List.of("E-mail corporativo", "Diretório corporativo", "Sistema de chamados", "VPN", "Pasta da equipe de TI"),
            List.of("TI-Analistas", "Acesso-Chamados", "VPN-Usuarios"),
            List.of("Notebook", "Monitor", "Crachá"),
            List.of("Validar o nível de acesso ao sistema de chamados com o gestor.")
    );

    private static final AccessProfile DESENVOLVEDOR = new AccessProfile(
            "desenvolvedor",
            List.of("E-mail corporativo", "Git", "CI/CD", "Banco de homologação", "VPN"),
            List.of("Dev-Team", "CI-Deploy", "VPN-Usuarios"),
            List.of("Notebook", "Monitor duplo", "Licença de IDE"),
            List.of("Não conceder acesso a produção sem autorização formal.")
    );

    private static final AccessProfile ADMINISTRATIVO = new AccessProfile(
            "administrativo",
            List.of("E-mail corporativo", "Sistema de faturamento", "Pasta compartilhada"),
            List.of("Administrativo", "Faturamento"),
            List.of("Computador", "Crachá"),
            List.of("Conferir a permissão de emissão de documentos com o gestor.")
    );

    private static final Map<String, AccessProfile> PROFILES = new HashMap<>();

    static {
        PROFILES.put("analista de ti", ANALISTA_TI);
        PROFILES.put("analista", ANALISTA_TI);
        PROFILES.put("ti", ANALISTA_TI);
        PROFILES.put("desenvolvedor", DESENVOLVEDOR);
        PROFILES.put("dev", DESENVOLVEDOR);
        PROFILES.put("administrativo", ADMINISTRATIVO);
        PROFILES.put("faturamento", ADMINISTRATIVO);
    }

    public AccessProfile forRole(String role) {
        return PROFILES.getOrDefault(normalize(role), GERAL);
    }

    /** Remove acentos, caixa e espaços repetidos para comparar cargos escritos de formas diferentes. */
    public static String normalize(String value) {
        String decomposed = Normalizer.normalize(value == null ? "" : value, Normalizer.Form.NFD);
        return decomposed
                .replaceAll("\\p{M}", "")
                .toLowerCase(Locale.ROOT)
                .replaceAll("\\s+", " ")
                .trim();
    }
}
