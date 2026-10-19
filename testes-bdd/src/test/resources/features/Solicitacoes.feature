# language: pt
@regressivo
Funcionalidade: Gerenciamento de solicitações
  Como receptor do ReRoute
  Quero solicitar uma doação e acompanhar a solicitação
  Para que eu consiga receber os itens de que preciso

  Contexto: Receptor autenticado
    Dado que eu esteja autenticado como "RECEPTOR"

  Cenário: Cadastro bem-sucedido de solicitação
    Dado que eu tenha os seguintes dados da solicitação:
      | campo         | valor |
      | donationId    | 1     |
      | participantId | 2     |
    Quando eu enviar a requisição para o endpoint "/api/solicitacoes" de cadastro de solicitações
    Então o status code da resposta deve ser 201
    E a solicitação retornada deve conter os dados:
      | campo         | valor    |
      | donationId    | 1        |
      | participantId | 2        |
      | status        | PENDENTE |
    E que o arquivo de contrato esperado é o "Solicitação"
    Então a resposta da requisição deve estar em conformidade com o contrato selecionado

  Cenário: Cadastro de solicitação sem sucesso com status maior que o permitido
    Dado que eu tenha os seguintes dados da solicitação:
      | campo         | valor                          |
      | donationId    | 1                              |
      | participantId | 2                              |
      | status        | AGUARDANDO_APROVACAO_DO_DOADOR |
    Quando eu enviar a requisição para o endpoint "/api/solicitacoes" de cadastro de solicitações
    Então o status code da resposta deve ser 400
    E o corpo de resposta de erro de validação deve retornar a mensagem "The field Status must be a string or array type with a maximum length of '20'." para o campo "Status"
    E que o arquivo de contrato esperado é o "Erro de validação"
    Então a resposta da requisição deve estar em conformidade com o contrato selecionado

  Cenário: Cadastro de solicitação com perfil sem permissão
    Dado que eu esteja autenticado como "DOADOR"
    E que eu tenha os seguintes dados da solicitação:
      | campo         | valor |
      | donationId    | 1     |
      | participantId | 1     |
    Quando eu enviar a requisição para o endpoint "/api/solicitacoes" de cadastro de solicitações
    Então o status code da resposta deve ser 403

  Cenário: Cadastro de solicitação sem token
    Dado que eu não esteja autenticado
    E que eu tenha os seguintes dados da solicitação:
      | campo         | valor |
      | donationId    | 1     |
      | participantId | 2     |
    Quando eu enviar a requisição para o endpoint "/api/solicitacoes" de cadastro de solicitações
    Então o status code da resposta deve ser 401

  Cenário: Listagem de solicitações sem precisar de token
    Dado que eu não esteja autenticado
    Quando eu enviar a requisição para o endpoint "/api/solicitacoes?page=1&pageSize=10" de listagem de solicitações
    Então o status code da resposta deve ser 200
    E a listagem deve retornar a página 1 com pelo menos 5 registros
    E que o arquivo de contrato esperado é o "Lista de solicitações"
    Então a resposta da requisição deve estar em conformidade com o contrato selecionado

  Cenário: Consulta de solicitação por ID
    Dado que eu tenha os seguintes dados da solicitação:
      | campo         | valor |
      | donationId    | 4     |
      | participantId | 2     |
    E eu enviar a requisição para o endpoint "/api/solicitacoes" de cadastro de solicitações
    E que eu recupere o ID da solicitação criada
    Quando eu enviar a requisição com o ID para o endpoint "/api/solicitacoes" de consulta de solicitação
    Então o status code da resposta deve ser 200
    E a solicitação retornada deve conter os dados:
      | campo      | valor    |
      | donationId | 4        |
      | status     | PENDENTE |
    E que o arquivo de contrato esperado é o "Solicitação"
    Então a resposta da requisição deve estar em conformidade com o contrato selecionado

  Cenário: Consulta de solicitação com ID inexistente
    Quando eu enviar a requisição com o ID 999999 para o endpoint "/api/solicitacoes" de consulta de solicitação
    Então o status code da resposta deve ser 404
    E o corpo de resposta de erro da api deve retornar a mensagem "Solicitação 999999 não encontrada."
    E que o arquivo de contrato esperado é o "Erro de recurso não encontrado"
    Então a resposta da requisição deve estar em conformidade com o contrato selecionado

  Cenário: Atualização bem-sucedida de solicitação
    Dado que eu tenha os seguintes dados da solicitação:
      | campo         | valor |
      | donationId    | 1     |
      | participantId | 2     |
    E eu enviar a requisição para o endpoint "/api/solicitacoes" de cadastro de solicitações
    E que eu recupere o ID da solicitação criada
    E que eu altere os seguintes dados da solicitação:
      | campo  | valor    |
      | status | APROVADA |
    Quando eu enviar a requisição com o ID para o endpoint "/api/solicitacoes" de atualização de solicitação
    Então o status code da resposta deve ser 200
    E a solicitação retornada deve conter os dados:
      | campo  | valor    |
      | status | APROVADA |
    E que o arquivo de contrato esperado é o "Solicitação"
    Então a resposta da requisição deve estar em conformidade com o contrato selecionado

  Cenário: Atualização de solicitação com ID inexistente
    Dado que eu tenha os seguintes dados da solicitação:
      | campo         | valor    |
      | donationId    | 1        |
      | participantId | 2        |
      | status        | APROVADA |
    Quando eu enviar a requisição com o ID 999999 para o endpoint "/api/solicitacoes" de atualização de solicitação
    Então o status code da resposta deve ser 404
    E o corpo de resposta de erro da api deve retornar a mensagem "Solicitação 999999 não encontrada."

  Cenário: Deve ser possível deletar uma solicitação
    Dado que eu tenha os seguintes dados da solicitação:
      | campo         | valor |
      | donationId    | 1     |
      | participantId | 2     |
    E eu enviar a requisição para o endpoint "/api/solicitacoes" de cadastro de solicitações
    E que eu recupere o ID da solicitação criada
    Quando eu enviar a requisição com o ID para o endpoint "/api/solicitacoes" de deleção de solicitação
    Então o status code da resposta deve ser 204
    E o corpo da resposta deve estar vazio
    E a solicitação deletada não deve mais ser encontrada no endpoint "/api/solicitacoes"

  Cenário: Deleção de solicitação com ID inexistente
    Quando eu enviar a requisição com o ID 999999 para o endpoint "/api/solicitacoes" de deleção de solicitação
    Então o status code da resposta deve ser 404
    E o corpo de resposta de erro da api deve retornar a mensagem "Solicitação 999999 não encontrada."
