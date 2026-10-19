# ReRoute ESG API - Testes Automatizados (BDD)

Projeto de testes do **Desafio de Testes Automatizados** (FIAP) para a API ReRoute ESG (ASP.NET Core 8).
Segue a estrutura ensinada no capítulo "É hora de testar": projeto Maven separado da API, cenários em
Gherkin (português) com Cucumber, requisições com Rest Assured, leitura do corpo com Gson e teste de
contrato com JSON Schema.

## Tecnologias

| Ferramenta | Versão |
|---|---|
| Java (JDK) | 21 |
| Maven + maven-surefire-plugin | 3.x / 3.0.0-M5 |
| cucumber-java e cucumber-junit (JUnit 4) | 7.18.1 |
| rest-assured | 5.5.0 |
| gson | 2.11.0 |
| lombok | 1.18.34 |
| networknt json-schema-validator | 1.5.1 |
| org.json | 20240303 |
| Docker / Docker Compose | para subir a API |

## Pré-requisitos

- JDK 21 e Maven instalados (`java -version` e `mvn -v`)
- Docker com o Docker Compose
- Nenhum banco é necessário: para os testes a API sobe com **EF Core InMemory** (variável `USE_INMEMORY_DB=true`)
  e carrega o seed dela mesma (João/DOADOR, Maria/RECEPTOR, Carlos/TRANSPORTADOR etc.). O Oracle não é usado.

## Onde cada arquivo fica no repositório

```
reroute-devops/                      (raiz do repositório da API)
├── Dockerfile                       (já existia)
├── docker-compose.testes.yml        sobe a API em modo de teste na porta 8080
├── .github/workflows/testes-bdd.yml pipeline dos testes
├── ReRoute.Api/                     API (Program.cs com a chave USE_INMEMORY_DB)
└── testes-bdd/                      este projeto
```

## Como rodar localmente

1. Na raiz do repositório da API, suba a API em modo de teste:

```bash
docker compose -f docker-compose.testes.yml up -d --build
```

2. Confira se a API está no ar (deve responder `Healthy`):

```bash
curl http://localhost:8080/health
```

Com o compose de testes o Swagger também fica disponível em `http://localhost:8080/swagger`.

3. Rode os testes:

```bash
cd testes-bdd
mvn clean test
```

4. Para derrubar a API:

```bash
docker compose -f docker-compose.testes.yml down
```

Sem Docker Compose, dá para subir só com `docker build` e `docker run`:

```bash
docker build -t reroute-api-testes .
docker run -d --name reroute-api-testes -p 8080:8080 \
  -e USE_INMEMORY_DB=true \
  -e Jwt__SecretKey=chave-de-teste-reroute-com-mais-de-32-caracteres \
  -e Jwt__Issuer=ReRoute.Api -e Jwt__Audience=ReRoute.Client \
  reroute-api-testes
```

Se a API estiver em outra URL, use `mvn clean test -Dapi.url=http://localhost:5080` (ou a variável de ambiente `API_URL`).
O padrão é `http://localhost:8080`.

Como o banco é em memória, ele volta ao seed sempre que o container é reiniciado. Os cenários criam os
próprios registros antes de consultar, atualizar ou deletar, então também podem rodar várias vezes seguidas
com o mesmo container.

## Como rodar no CI (GitHub Actions)

O workflow `.github/workflows/testes-bdd.yml` roda em todo push e pull request:

1. faz o checkout;
2. sobe a API com `docker compose -f docker-compose.testes.yml up -d --build` (build da imagem pelo Dockerfile da API);
3. espera o `/health` responder;
4. configura o JDK 21;
5. roda `mvn clean test` dentro de `testes-bdd`;
6. publica o `cucumber-reports.html` como artefato **relatorio-cucumber** (aba Actions > execução > Artifacts);
7. derruba a API.

Se algum cenário falhar, o job fica vermelho e os logs da API aparecem no próprio job.

## Relatório

Depois do `mvn clean test` o relatório HTML fica em `target/cucumber-reports.html`. No CI ele é baixado pelo artefato.

## Estrutura do projeto

```
testes-bdd/
├── pom.xml
└── src/test/
    ├── java/
    │   ├── model/      DTOs usados com Gson e Lombok (DoacaoModel, LoginModel, ErroDetalheModel...)
    │   ├── services/   chamadas Rest Assured e validação de contrato (BaseService + um service por recurso)
    │   ├── steps/      step definitions (@Dado, @Quando, @Então, @E), um por funcionalidade + ComumSteps
    │   ├── runner/     TestRunner (@RunWith(Cucumber.class), tag @regressivo, relatório HTML)
    │   └── hook/       Hook (limpa token e resposta antes de cada cenário)
    └── resources/
        ├── features/   um .feature por recurso da API
        └── schemas/    um JSON Schema por tipo de resposta
```

Algumas decisões:

- **Autenticação**: o passo `Dado que eu esteja autenticado como "DOADOR"` faz o `POST /api/auth/login` com o
  usuário do seed daquele perfil, guarda o token e as próximas requisições vão com
  `.header("Authorization", "Bearer " + token)`. O passo `Dado que eu não esteja autenticado` remove o token.
- **Contexto**: nas funcionalidades com rotas protegidas o `Contexto` faz o login com o perfil que tem permissão
  (DOADOR em doações, RECEPTOR em solicitações, TRANSPORTADOR em entregas).
- **Resposta compartilhada**: a última resposta e o token ficam no `BaseService`, assim os passos comuns
  (status code, contrato, mensagens de erro) servem para todas as funcionalidades. O `Hook` zera tudo antes de cada cenário.
- **Status code**: `Assert.assertEquals(statusCode, response.statusCode())`.
- **Corpo**: a resposta é convertida com Gson para o model e comparada campo a campo com a tabela do cenário.
- **Contrato**: `JsonSchemaFactory.getInstance(SpecVersion.VersionFlag.V4)` + `schema.validate(...)` e
  `assertTrue(errors.isEmpty())`. Os schemas usam `additionalProperties: false` nas entidades, então um campo novo
  ou removido (por exemplo, a senha aparecendo no participante) quebra o teste.

## Endpoints cobertos (27)

| Método | Rota | Autenticação | Status testados | Contrato |
|---|---|---|---|---|
| GET | /health | pública | 200 (corpo `Healthy`, texto) | não se aplica (texto puro) |
| POST | /api/auth/login | pública | 200, 401 | login-bem-sucedido, erro-de-login |
| GET | /api/participantes | qualquer perfil | 200, 401 | lista-de-participantes |
| GET | /api/participantes/{id} | qualquer perfil | 200, 404, 401 | participante, erro-recurso-nao-encontrado |
| POST | /api/participantes | pública | 201, 400 | participante, erro-de-validacao |
| PUT | /api/participantes/{id} | qualquer perfil | 200, 404 | participante |
| DELETE | /api/participantes/{id} | qualquer perfil | 204, 404 | sem corpo no 204 |
| GET | /api/doacoes | pública | 200 | lista-de-doacoes |
| GET | /api/doacoes/{id} | pública | 200, 404 | doacao, erro-recurso-nao-encontrado |
| POST | /api/doacoes | DOADOR | 201, 400, 401, 403 | doacao, erro-de-validacao |
| PUT | /api/doacoes/{id} | DOADOR | 200, 404 | doacao |
| DELETE | /api/doacoes/{id} | DOADOR | 204, 401 | sem corpo no 204 |
| GET | /api/solicitacoes | pública | 200 | lista-de-solicitacoes |
| GET | /api/solicitacoes/{id} | pública | 200, 404 | solicitacao, erro-recurso-nao-encontrado |
| POST | /api/solicitacoes | RECEPTOR | 201, 400, 401, 403 | solicitacao, erro-de-validacao |
| PUT | /api/solicitacoes/{id} | RECEPTOR | 200, 404 | solicitacao |
| DELETE | /api/solicitacoes/{id} | RECEPTOR | 204, 404 | sem corpo no 204 |
| GET | /api/entregas | pública | 200 | lista-de-entregas |
| GET | /api/entregas/{id} | pública | 200, 404 | entrega, erro-recurso-nao-encontrado |
| POST | /api/entregas | TRANSPORTADOR | 201, 400, 401, 403 | entrega, erro-de-validacao |
| PUT | /api/entregas/{id} | TRANSPORTADOR | 200, 404 | entrega |
| DELETE | /api/entregas/{id} | TRANSPORTADOR | 204, 403 | sem corpo no 204 |
| GET | /api/registros | qualquer perfil | 200, 401 | lista-de-registros-esg |
| GET | /api/registros/{id} | qualquer perfil | 200, 404 | registro-esg, erro-recurso-nao-encontrado |
| POST | /api/registros | qualquer perfil | 201, 400, 401 | registro-esg, erro-de-validacao |
| PUT | /api/registros/{id} | qualquer perfil | 200, 404 | registro-esg |
| DELETE | /api/registros/{id} | qualquer perfil | 204, 404 | sem corpo no 204 |

Os 401 e 403 gerados pelo JWT do ASP.NET voltam sem corpo, então nesses casos o teste valida o status
(e, em alguns, que o corpo está vazio). O 401 do login tem corpo (`{"mensagem": ...}`) e tem contrato próprio.
Depois de cada DELETE o cenário consulta o mesmo ID e espera 404.

## Cenários (62)

**Health.feature** (1)
- API respondendo no endpoint de health (positivo)

**Autenticacao.feature** (5)
- Login bem-sucedido de doador (positivo)
- Login bem-sucedido de receptor (positivo)
- Login bem-sucedido de transportador (positivo)
- Login sem sucesso com senha incorreta (negativo)
- Login sem sucesso com e-mail não cadastrado (negativo)

**Participantes.feature** (12)
- Cadastro bem-sucedido de participante sem precisar de token (positivo)
- Cadastro de participante sem sucesso ao passar um e-mail inválido (negativo)
- Cadastro de participante sem sucesso ao passar um perfil inexistente (negativo)
- Listagem de participantes com token (positivo)
- Listagem de participantes sem token (negativo)
- Consulta de participante por ID (positivo)
- Consulta de participante com ID inexistente (negativo)
- Consulta de participante sem token (negativo)
- Atualização bem-sucedida de participante (positivo)
- Atualização de participante com ID inexistente (negativo)
- Deve ser possível deletar um participante (positivo)
- Deleção de participante com ID inexistente (negativo)

**Doacoes.feature** (11)
- Cadastro bem-sucedido de doação (positivo)
- Cadastro de doação sem sucesso ao não informar o nome do item (negativo)
- Cadastro de doação sem token (negativo)
- Cadastro de doação com perfil sem permissão (negativo)
- Listagem de doações sem precisar de token (positivo)
- Consulta de doação por ID (positivo)
- Consulta de doação com ID inexistente (negativo)
- Atualização bem-sucedida de doação (positivo)
- Atualização de doação com ID inexistente (negativo)
- Deve ser possível deletar uma doação (positivo)
- Deleção de doação sem token (negativo)

**Solicitacoes.feature** (11)
- Cadastro bem-sucedido de solicitação (positivo)
- Cadastro de solicitação sem sucesso com status maior que o permitido (negativo)
- Cadastro de solicitação com perfil sem permissão (negativo)
- Cadastro de solicitação sem token (negativo)
- Listagem de solicitações sem precisar de token (positivo)
- Consulta de solicitação por ID (positivo)
- Consulta de solicitação com ID inexistente (negativo)
- Atualização bem-sucedida de solicitação (positivo)
- Atualização de solicitação com ID inexistente (negativo)
- Deve ser possível deletar uma solicitação (positivo)
- Deleção de solicitação com ID inexistente (negativo)

**Entregas.feature** (11)
- Cadastro bem-sucedido de entrega (positivo)
- Cadastro de entrega sem sucesso com status maior que o permitido (negativo)
- Cadastro de entrega com perfil sem permissão (negativo)
- Cadastro de entrega sem token (negativo)
- Listagem de entregas sem precisar de token (positivo)
- Consulta de entrega por ID (positivo)
- Consulta de entrega com ID inexistente (negativo)
- Atualização bem-sucedida de entrega (positivo)
- Atualização de entrega com ID inexistente (negativo)
- Deve ser possível deletar uma entrega (positivo)
- Deleção de entrega com perfil sem permissão (negativo)

**RegistrosESG.feature** (11)
- Cadastro bem-sucedido de registro ESG (positivo)
- Cadastro de registro ESG sem sucesso com resíduo acima do limite (negativo)
- Cadastro de registro ESG sem token (negativo)
- Listagem de registros ESG com token (positivo)
- Listagem de registros ESG sem token (negativo)
- Consulta de registro ESG por ID (positivo)
- Consulta de registro ESG com ID inexistente (negativo)
- Atualização bem-sucedida de registro ESG (positivo)
- Atualização de registro ESG com ID inexistente (negativo)
- Deve ser possível deletar um registro ESG (positivo)
- Deleção de registro ESG com ID inexistente (negativo)

Total: 29 positivos e 33 negativos. Todos com a tag `@regressivo`, que é a tag filtrada no `TestRunner`.

## Alteração feita na API

Para a API subir sem Oracle foi incluída no `Program.cs` a variável `USE_INMEMORY_DB`. Quando ela vale `true`,
o `DbContext` usa `UseInMemoryDatabase`. Sem ela, o caminho do Oracle continua igual (inclusive o erro quando a
connection string não está configurada). O pacote `Microsoft.EntityFrameworkCore.InMemory` (8.0.11, a mesma versão
do projeto de testes xUnit) foi adicionado no `ReRoute.Api.csproj`. Os testes xUnit da API continuam passando.

## Observações sobre o comportamento da API

- As mensagens de validação dos DTOs de doação, solicitação, entrega e registro ESG vêm no padrão do ASP.NET, em
  inglês (ex.: `The NomeItem field is required.`). Só o DTO de participante tem mensagens em português.
- O banco em memória não valida chave estrangeira, então uma doação com `participantId` inexistente é aceita nos
  testes. No Oracle esse mesmo caso daria erro do banco (500), por isso não foi criado cenário para ele.
- Os IDs enviados no corpo do PUT (`donationId`, `solicitacaoId`...) não são usados pela API, só o ID da rota.
