# language: pt
@regressivo
Funcionalidade: Gerenciamento de doações
  Como doador do ReRoute
  Quero cadastrar, consultar, atualizar e remover doações
  Para que os itens fiquem disponíveis para os receptores

  Contexto: Doador autenticado
    Dado que eu esteja autenticado como "DOADOR"

  Cenário: Cadastro bem-sucedido de doação
    Dado que eu tenha os seguintes dados da doação:
      | campo         | valor               |
      | participantId | 1                   |
      | nomeItem      | Cobertores          |
      | descricao     | Cobertores de casal |
      | tamanho       | MEDIO               |
      | localRetirada | Av. Paulista, 1106  |
    Quando eu enviar a requisição para o endpoint "/api/doacoes" de cadastro de doações
    Então o status code da resposta deve ser 201
    E a doação retornada deve conter os dados:
      | campo         | valor              |
      | participantId | 1                  |
      | nomeItem      | Cobertores         |
      | localRetirada | Av. Paulista, 1106 |
      | status        | DISPONIVEL         |
    E que o arquivo de contrato esperado é o "Doação"
    Então a resposta da requisição deve estar em conformidade com o contrato selecionado

  Cenário: Cadastro de doação sem sucesso ao não informar o nome do item
    Dado que eu tenha os seguintes dados da doação:
      | campo         | valor               |
      | participantId | 1                   |
      | descricao     | Cobertores de casal |
      | tamanho       | MEDIO               |
      | localRetirada | Av. Paulista, 1106  |
    Quando eu enviar a requisição para o endpoint "/api/doacoes" de cadastro de doações
    Então o status code da resposta deve ser 400
    E o corpo de resposta de erro de validação deve retornar a mensagem "The NomeItem field is required." para o campo "NomeItem"
    E que o arquivo de contrato esperado é o "Erro de validação"
    Então a resposta da requisição deve estar em conformidade com o contrato selecionado

  Cenário: Cadastro de doação sem token
    Dado que eu não esteja autenticado
    E que eu tenha os seguintes dados da doação:
      | campo         | valor               |
      | participantId | 1                   |
      | nomeItem      | Cobertores          |
      | descricao     | Cobertores de casal |
      | tamanho       | MEDIO               |
      | localRetirada | Av. Paulista, 1106  |
    Quando eu enviar a requisição para o endpoint "/api/doacoes" de cadastro de doações
    Então o status code da resposta deve ser 401
    E o corpo da resposta deve estar vazio

  Cenário: Cadastro de doação com perfil sem permissão
    Dado que eu esteja autenticado como "RECEPTOR"
    E que eu tenha os seguintes dados da doação:
      | campo         | valor            |
      | participantId | 2                |
      | nomeItem      | Sapatos          |
      | descricao     | Par de sapatos   |
      | tamanho       | PEQUENO          |
      | localRetirada | Rua Augusta, 500 |
    Quando eu enviar a requisição para o endpoint "/api/doacoes" de cadastro de doações
    Então o status code da resposta deve ser 403

  Cenário: Listagem de doações sem precisar de token
    Dado que eu não esteja autenticado
    Quando eu enviar a requisição para o endpoint "/api/doacoes?page=1&pageSize=10" de listagem de doações
    Então o status code da resposta deve ser 200
    E a listagem deve retornar a página 1 com pelo menos 5 registros
    E que o arquivo de contrato esperado é o "Lista de doações"
    Então a resposta da requisição deve estar em conformidade com o contrato selecionado

  Cenário: Consulta de doação por ID
    Dado que eu tenha os seguintes dados da doação:
      | campo         | valor                   |
      | participantId | 1                       |
      | nomeItem      | Livros didáticos        |
      | descricao     | Coleção do ensino médio |
      | tamanho       | GRANDE                  |
      | localRetirada | Rua Vergueiro, 300      |
      | status        | DISPONIVEL              |
    E eu enviar a requisição para o endpoint "/api/doacoes" de cadastro de doações
    E que eu recupere o ID da doação criada
    Quando eu enviar a requisição com o ID para o endpoint "/api/doacoes" de consulta de doação
    Então o status code da resposta deve ser 200
    E a doação retornada deve conter os dados:
      | campo     | valor                   |
      | nomeItem  | Livros didáticos        |
      | descricao | Coleção do ensino médio |
      | tamanho   | GRANDE                  |
    E que o arquivo de contrato esperado é o "Doação"
    Então a resposta da requisição deve estar em conformidade com o contrato selecionado

  Cenário: Consulta de doação com ID inexistente
    Quando eu enviar a requisição com o ID 999999 para o endpoint "/api/doacoes" de consulta de doação
    Então o status code da resposta deve ser 404
    E o corpo de resposta de erro da api deve retornar a mensagem "Doação 999999 não encontrada."
    E que o arquivo de contrato esperado é o "Erro de recurso não encontrado"
    Então a resposta da requisição deve estar em conformidade com o contrato selecionado

  Cenário: Atualização bem-sucedida de doação
    Dado que eu tenha os seguintes dados da doação:
      | campo         | valor                 |
      | participantId | 1                     |
      | nomeItem      | Geladeira             |
      | descricao     | Geladeira 300 litros  |
      | tamanho       | GRANDE                |
      | localRetirada | Rua da Consolação, 80 |
    E eu enviar a requisição para o endpoint "/api/doacoes" de cadastro de doações
    E que eu recupere o ID da doação criada
    E que eu altere os seguintes dados da doação:
      | campo  | valor      |
      | status | SOLICITADA |
    Quando eu enviar a requisição com o ID para o endpoint "/api/doacoes" de atualização de doação
    Então o status code da resposta deve ser 200
    E a doação retornada deve conter os dados:
      | campo    | valor      |
      | nomeItem | Geladeira  |
      | status   | SOLICITADA |
    E que o arquivo de contrato esperado é o "Doação"
    Então a resposta da requisição deve estar em conformidade com o contrato selecionado

  Cenário: Atualização de doação com ID inexistente
    Dado que eu tenha os seguintes dados da doação:
      | campo         | valor                 |
      | participantId | 1                     |
      | nomeItem      | Geladeira             |
      | descricao     | Geladeira 300 litros  |
      | tamanho       | GRANDE                |
      | localRetirada | Rua da Consolação, 80 |
    Quando eu enviar a requisição com o ID 999999 para o endpoint "/api/doacoes" de atualização de doação
    Então o status code da resposta deve ser 404
    E o corpo de resposta de erro da api deve retornar a mensagem "Doação 999999 não encontrada."

  Cenário: Deve ser possível deletar uma doação
    Dado que eu tenha os seguintes dados da doação:
      | campo         | valor           |
      | participantId | 1               |
      | nomeItem      | Mesa de estudo  |
      | descricao     | Mesa de madeira |
      | tamanho       | GRANDE          |
      | localRetirada | Rua Tabapuã, 45 |
    E eu enviar a requisição para o endpoint "/api/doacoes" de cadastro de doações
    E que eu recupere o ID da doação criada
    Quando eu enviar a requisição com o ID para o endpoint "/api/doacoes" de deleção de doação
    Então o status code da resposta deve ser 204
    E o corpo da resposta deve estar vazio
    E a doação deletada não deve mais ser encontrada no endpoint "/api/doacoes"

  Cenário: Deleção de doação sem token
    Dado que eu não esteja autenticado
    Quando eu enviar a requisição com o ID 1 para o endpoint "/api/doacoes" de deleção de doação
    Então o status code da resposta deve ser 401
