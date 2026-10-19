# language: pt
@regressivo
Funcionalidade: Gerenciamento de entregas
  Como transportador do ReRoute
  Quero registrar e atualizar as entregas das doações
  Para que o receptor acompanhe a logística até o recebimento

  Contexto: Transportador autenticado
    Dado que eu esteja autenticado como "TRANSPORTADOR"

  Cenário: Cadastro bem-sucedido de entrega
    Dado que eu tenha os seguintes dados da entrega:
      | campo         | valor               |
      | solicitacaoId | 1                   |
      | participantId | 3                   |
      | dataAceite    | 2026-11-01T09:00:00 |
    Quando eu enviar a requisição para o endpoint "/api/entregas" de cadastro de entregas
    Então o status code da resposta deve ser 201
    E a entrega retornada deve conter os dados:
      | campo         | valor               |
      | solicitacaoId | 1                   |
      | participantId | 3                   |
      | dataAceite    | 2026-11-01T09:00:00 |
      | status        | PENDENTE            |
    E que o arquivo de contrato esperado é o "Entrega"
    Então a resposta da requisição deve estar em conformidade com o contrato selecionado

  Cenário: Cadastro de entrega sem sucesso com status maior que o permitido
    Dado que eu tenha os seguintes dados da entrega:
      | campo         | valor                       |
      | solicitacaoId | 1                           |
      | status        | SAIU_PARA_ENTREGA_NO_CENTRO |
    Quando eu enviar a requisição para o endpoint "/api/entregas" de cadastro de entregas
    Então o status code da resposta deve ser 400
    E o corpo de resposta de erro de validação deve retornar a mensagem "The field Status must be a string or array type with a maximum length of '20'." para o campo "Status"
    E que o arquivo de contrato esperado é o "Erro de validação"
    Então a resposta da requisição deve estar em conformidade com o contrato selecionado

  Cenário: Cadastro de entrega com perfil sem permissão
    Dado que eu esteja autenticado como "DOADOR"
    E que eu tenha os seguintes dados da entrega:
      | campo         | valor |
      | solicitacaoId | 1     |
    Quando eu enviar a requisição para o endpoint "/api/entregas" de cadastro de entregas
    Então o status code da resposta deve ser 403

  Cenário: Cadastro de entrega sem token
    Dado que eu não esteja autenticado
    E que eu tenha os seguintes dados da entrega:
      | campo         | valor |
      | solicitacaoId | 1     |
    Quando eu enviar a requisição para o endpoint "/api/entregas" de cadastro de entregas
    Então o status code da resposta deve ser 401

  Cenário: Listagem de entregas sem precisar de token
    Dado que eu não esteja autenticado
    Quando eu enviar a requisição para o endpoint "/api/entregas?page=1&pageSize=10" de listagem de entregas
    Então o status code da resposta deve ser 200
    E a listagem deve retornar a página 1 com pelo menos 5 registros
    E que o arquivo de contrato esperado é o "Lista de entregas"
    Então a resposta da requisição deve estar em conformidade com o contrato selecionado

  Cenário: Consulta de entrega por ID
    Dado que eu tenha os seguintes dados da entrega:
      | campo         | valor |
      | solicitacaoId | 2     |
      | participantId | 3     |
    E eu enviar a requisição para o endpoint "/api/entregas" de cadastro de entregas
    E que eu recupere o ID da entrega criada
    Quando eu enviar a requisição com o ID para o endpoint "/api/entregas" de consulta de entrega
    Então o status code da resposta deve ser 200
    E a entrega retornada deve conter os dados:
      | campo         | valor    |
      | solicitacaoId | 2        |
      | status        | PENDENTE |
    E que o arquivo de contrato esperado é o "Entrega"
    Então a resposta da requisição deve estar em conformidade com o contrato selecionado

  Cenário: Consulta de entrega com ID inexistente
    Quando eu enviar a requisição com o ID 999999 para o endpoint "/api/entregas" de consulta de entrega
    Então o status code da resposta deve ser 404
    E o corpo de resposta de erro da api deve retornar a mensagem "Entrega 999999 não encontrada."
    E que o arquivo de contrato esperado é o "Erro de recurso não encontrado"
    Então a resposta da requisição deve estar em conformidade com o contrato selecionado

  Cenário: Atualização bem-sucedida de entrega
    Dado que eu tenha os seguintes dados da entrega:
      | campo         | valor               |
      | solicitacaoId | 1                   |
      | participantId | 3                   |
      | dataAceite    | 2026-11-01T09:00:00 |
    E eu enviar a requisição para o endpoint "/api/entregas" de cadastro de entregas
    E que eu recupere o ID da entrega criada
    E que eu altere os seguintes dados da entrega:
      | campo       | valor               |
      | dataEntrega | 2026-11-02T15:30:00 |
      | status      | ENTREGUE            |
    Quando eu enviar a requisição com o ID para o endpoint "/api/entregas" de atualização de entrega
    Então o status code da resposta deve ser 200
    E a entrega retornada deve conter os dados:
      | campo       | valor               |
      | dataEntrega | 2026-11-02T15:30:00 |
      | status      | ENTREGUE            |
    E que o arquivo de contrato esperado é o "Entrega"
    Então a resposta da requisição deve estar em conformidade com o contrato selecionado

  Cenário: Atualização de entrega com ID inexistente
    Dado que eu tenha os seguintes dados da entrega:
      | campo         | valor    |
      | solicitacaoId | 1        |
      | status        | ENTREGUE |
    Quando eu enviar a requisição com o ID 999999 para o endpoint "/api/entregas" de atualização de entrega
    Então o status code da resposta deve ser 404
    E o corpo de resposta de erro da api deve retornar a mensagem "Entrega 999999 não encontrada."

  Cenário: Deve ser possível deletar uma entrega
    Dado que eu tenha os seguintes dados da entrega:
      | campo         | valor |
      | solicitacaoId | 1     |
    E eu enviar a requisição para o endpoint "/api/entregas" de cadastro de entregas
    E que eu recupere o ID da entrega criada
    Quando eu enviar a requisição com o ID para o endpoint "/api/entregas" de deleção de entrega
    Então o status code da resposta deve ser 204
    E o corpo da resposta deve estar vazio
    E a entrega deletada não deve mais ser encontrada no endpoint "/api/entregas"

  Cenário: Deleção de entrega com perfil sem permissão
    Dado que eu esteja autenticado como "RECEPTOR"
    Quando eu enviar a requisição com o ID 1 para o endpoint "/api/entregas" de deleção de entrega
    Então o status code da resposta deve ser 403
