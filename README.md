# Checklist de onboarding/offboarding de TI

CLI em **Java 17** que gera checklists padronizados de entrada e saída de colaboradores a partir de **perfis de acesso fictícios**. Nasceu de uma rotina real de analista de TI: transformar “o que tem que ser feito” em uma lista objetiva, com categorias, itens obrigatórios e passos de documentação.

> **Nenhum dado de ambiente corporativo real é usado.** Sistemas, grupos, cargos e equipamentos são inventados para o projeto.

## O que o checklist cobre

- **Onboarding:** criação de conta, grupos de acesso, sistemas, entrega de equipamentos, registro no inventário, validação com o gestor, aceite da política de segurança e comunicação ao time.
- **Offboarding:** desativação da conta, remoção de grupos, revogação de licenças, recolhimento de equipamentos, backup/transferência de arquivos, baixa no inventário e comunicação da saída.
- **Saída:** Markdown (com `- [ ]`) ou HTML para imprimir e salvar como PDF pelo navegador.

## Perfis disponíveis

| Cargo informado | Perfil usado |
| --- | --- |
| `analista de ti`, `analista`, `ti` | E-mail, diretório, chamados, VPN, pasta de TI · grupos `TI-Analistas`, `Acesso-Chamados`, `VPN-Usuarios` · notebook, monitor, crachá |
| `desenvolvedor`, `dev` | Git, CI/CD, homologação, VPN · grupos `Dev-Team`, `CI-Deploy`, `VPN-Usuarios` · notebook, monitor duplo, IDE |
| `administrativo`, `faturamento` | Faturamento e pasta compartilhada · grupos `Administrativo`, `Faturamento` · computador, crachá |
| qualquer outro cargo | Perfil **geral** (e-mail, pasta compartilhada, grupo `Usuarios`) |

A comparação ignora acentos e caixa: `Analista de TI`, `analista de ti` e `ANALISTA DE TI` chegam ao mesmo perfil.

## Como rodar

Requisito: **JDK 17**.

```bash
./gradlew jar                       # no Windows: gradlew.bat jar
java -jar build/libs/ChecklistTI-1.0.0.jar --help

java -jar build/libs/ChecklistTI-1.0.0.jar onboard \
  --name "Maria Silva" --role "Analista de TI" \
  --department TI --date 2026-12-01

java -jar build/libs/ChecklistTI-1.0.0.jar offboard \
  --name "Ana Lima" --role "Desenvolvedor" --date 2026-11-30 \
  --format html --output checklist.html
```

Sem `--output`, o resultado vai para o terminal. Com `--output`, o arquivo é gravado em UTF-8.

### Exemplo de saída (Markdown)

```markdown
# Onboarding — Maria Silva

**Colaborador:** Maria Silva
**Cargo:** Analista de TI
**Departamento:** TI
**Data:** 2026-12-01

## Itens

- [ ] **ACC-01 · Conta** Criar conta corporativa *(obrigatório)*
  - E-mail corporativo para Maria Silva.
- [ ] **ACC-02 · Acesso** Adicionar aos grupos de acesso *(obrigatório)*
  - TI-Analistas, Acesso-Chamados, VPN-Usuarios
...
```

## Decisões

- **Perfis centralizados em `ProfileCatalog`:** adicionar um cargo não exige mudar o gerador nem os renderizadores.
- **Regra de negócio separada da interface:** `core/` não sabe nada sobre terminal ou arquivo, por isso é fácil de testar.
- **Escape nas saídas:** um nome com `|`, `*` ou `<script>` não quebra o Markdown nem injeta HTML. Isso está coberto por testes.
- **IDs estáveis:** cada item tem um código (`ACC-01`, `EQP-01` etc.), o que facilita referenciar o passo em uma documentação de processo.

## Testes e CI

**14 testes JUnit 5** cobrem perfis, normalização de cargo, geração dos dois fluxos, IDs únicos, escape nas saídas, parsing da CLI e gravação de arquivo.

O workflow em `.github/workflows/ci.yml` roda `./gradlew build` em cada push e PR e anexa o relatório de testes.

## Estrutura

```
src/main/java/com/lucasemanuel/checklistti/
├── Main.java
├── cli/Cli.java
├── core/
│   ├── AccessProfile.java   Checklist.java       ChecklistGenerator.java
│   ├── ChecklistItem.java   Employee.java        ProfileCatalog.java
│   └── Category.java
└── render/
    ├── Renderer.java        MarkdownRenderer.java  HtmlRenderer.java
```

## Licença

[Unlicense](LICENSE) — uso livre, sem restrições.
