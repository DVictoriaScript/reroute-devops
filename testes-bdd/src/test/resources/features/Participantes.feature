# language: pt
@regressivo
Funcionalidade: Gerenciamento de participantes
  Como usuário da API ReRoute
  Quero cadastrar, consultar, atualizar e remover participantes
  Para que doadores, receptores e transportadores fiquem registrados no sistema

  Cenário: Cadastro bem-sucedido de participante sem precisar de token
    Dado que eu não esteja autenticado
    E que eu tenha os seguintes dados do participante:
      | campo      | valor             |
      | nome       | Beatriz Andrade   |
      | email      | beatriz@email.com |
      | senha      | senha123          |
      | tipoPerfil | TRANSPORTADOR     |
    Quando eu enviar a requisição para o endpoint "/api/participantes" de cadastro de participantes
    Então o status code da resposta deve ser 201
    E o participante retornado deve conter os dados:
      | campo      | valor             |
      | nome       | Beatriz Andrade   |
      | email      | beatriz@email.com |
      | tipoPerfil | TRANSPORTADOR     |
    E que o arquivo de contrato esperado é o "Participante"
    Então a resposta da requisição deve estar em conformidade com o contrato selecionado

  Cenário: Cadastro de participante sem sucesso ao passar um e-mail inválido
    Dado que eu tenha os seguintes dados do participante:
      | campo      | valor           |
      | nome       | Beatriz Andrade |
      | email      | beatriz.email   |
      | senha      | senha123        |
      | tipoPerfil | DOADOR          |
    Quando eu enviar a requisição para o endpoint "/api/participantes" de cadastro de participantes
    Então o status code da resposta deve ser 400
    E o corpo de resposta de erro de validação deve retornar a mensagem "E-mail inválido." para o campo "Email"
    E que o arquivo de contrato esperado é o "Erro de validação"
    Então a resposta da requisição deve estar em conformidade com o contrato selecionado

  Cenário: Cadastro de participante sem sucesso ao passar um perfil inexistente
    Dado que eu tenha os seguintes dados do participante:
      | campo      | valor             |
      | nome       | Beatriz Andrade   |
      | email      | beatriz@email.com |
      | senha      | senha123          |
      | tipoPerfil | ADMIN             |
    Quando eu enviar a requisição para o endpoint "/api/participantes" de cadastro de participantes
    Então o status code da resposta deve ser 400
    E o corpo de resposta de erro de validação deve retornar a mensagem "Perfil deve ser DOADOR, RECEPTOR ou TRANSPORTADOR." para o campo "TipoPerfil"

  Cenário: Listagem de participantes com token
    Dado que eu esteja autenticado como "DOADOR"
    Quando eu enviar a requisição para o endpoint "/api/participantes?page=1&pageSize=10" de listagem de participantes
    Então o status code da resposta deve ser 200
    E a listagem deve retornar a página 1 com pelo menos 5 registros
    E que o arquivo de contrato esperado é o "Lista de participantes"
    Então a resposta da requisição deve estar em conformidade com o contrato selecionado

  Cenário: Listagem de participantes sem token
    Dado que eu não esteja autenticado
    Quando eu enviar a requisição para o endpoint "/api/participantes" de listagem de participantes
    Então o status code da resposta deve ser 401
    E o corpo da resposta deve estar vazio

  Cenário: Consulta de participante por ID
    Dado que eu tenha os seguintes dados do participante:
      | campo      | valor            |
      | nome       | Rafael Mendes    |
      | email      | rafael@email.com |
      | senha      | senha123         |
      | tipoPerfil | RECEPTOR         |
    E eu enviar a requisição para o endpoint "/api/participantes" de cadastro de participantes
    E que eu recupere o ID do participante criado
    E que eu esteja autenticado como "RECEPTOR"
    Quando eu enviar a requisição com o ID para o endpoint "/api/participantes" de consulta de participante
    Então o status code da resposta deve ser 200
    E o participante retornado deve conter os dados:
      | campo      | valor            |
      | nome       | Rafael Mendes    |
      | email      | rafael@email.com |
      | tipoPerfil | RECEPTOR         |
    E que o arquivo de contrato esperado é o "Participante"
    Então a resposta da requisição deve estar em conformidade com o contrato selecionado

  Cenário: Consulta de participante com ID inexistente
    Dado que eu esteja autenticado como "DOADOR"
    Quando eu enviar a requisição com o ID 999999 para o endpoint "/api/participantes" de consulta de participante
    Então o status code da resposta deve ser 404
    E o corpo de resposta de erro da api deve retornar a mensagem "Participante 999999 não encontrado."
    E que o arquivo de contrato esperado é o "Erro de recurso não encontrado"
    Então a resposta da requisição deve estar em conformidade com o contrato selecionado

  Cenário: Consulta de participante sem token
    Dado que eu não esteja autenticado
    Quando eu enviar a requisição com o ID 1 para o endpoint "/api/participantes" de consulta de participante
    Então o status code da resposta deve ser 401

  Cenário: Atualização bem-sucedida de participante
    Dado que eu tenha os seguintes dados do participante:
      | campo      | valor              |
      | nome       | Fernanda Costa     |
      | email      | fernanda@email.com |
      | senha      | senha123           |
      | tipoPerfil | DOADOR             |
    E eu enviar a requisição para o endpoint "/api/participantes" de cadastro de participantes
    E que eu recupere o ID do participante criado
    E que eu esteja autenticado como "DOADOR"
    E que eu altere os seguintes dados do participante:
      | campo      | valor               |
      | nome       | Fernanda Costa Lima |
      | tipoPerfil | TRANSPORTADOR       |
    Quando eu enviar a requisição com o ID para o endpoint "/api/participantes" de atualização de participante
    Então o status code da resposta deve ser 200
    E o participante retornado deve conter os dados:
      | campo      | valor               |
      | nome       | Fernanda Costa Lima |
      | tipoPerfil | TRANSPORTADOR       |
    E que o arquivo de contrato esperado é o "Participante"
    Então a resposta da requisição deve estar em conformidade com o contrato selecionado

  Cenário: Atualização de participante com ID inexistente
    Dado que eu esteja autenticado como "DOADOR"
    E que eu tenha os seguintes dados do participante:
      | campo      | valor             |
      | nome       | Ninguém           |
      | email      | ninguem@email.com |
      | senha      | senha123          |
      | tipoPerfil | DOADOR            |
    Quando eu enviar a requisição com o ID 999999 para o endpoint "/api/participantes" de atualização de participante
    Então o status code da resposta deve ser 404
    E o corpo de resposta de erro da api deve retornar a mensagem "Participante 999999 não encontrado."

  Cenário: Deve ser possível deletar um participante
    Dado que eu tenha os seguintes dados do participante:
      | campo      | valor             |
      | nome       | Gustavo Pereira   |
      | email      | gustavo@email.com |
      | senha      | senha123          |
      | tipoPerfil | RECEPTOR          |
    E eu enviar a requisição para o endpoint "/api/participantes" de cadastro de participantes
    E que eu recupere o ID do participante criado
    E que eu esteja autenticado como "DOADOR"
    Quando eu enviar a requisição com o ID para o endpoint "/api/participantes" de deleção de participante
    Então o status code da resposta deve ser 204
    E o corpo da resposta deve estar vazio
    E o participante deletado não deve mais ser encontrado no endpoint "/api/participantes"

  Cenário: Deleção de participante com ID inexistente
    Dado que eu esteja autenticado como "DOADOR"
    Quando eu enviar a requisição com o ID 999999 para o endpoint "/api/participantes" de deleção de participante
    Então o status code da resposta deve ser 404
    E o corpo de resposta de erro da api deve retornar a mensagem "Participante 999999 não encontrado."
