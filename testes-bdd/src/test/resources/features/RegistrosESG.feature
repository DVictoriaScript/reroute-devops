# language: pt
@regressivo
Funcionalidade: Gerenciamento de registros ESG
  Como participante do ReRoute
  Quero registrar o impacto ambiental das doações
  Para que a plataforma mostre o resíduo e o CO2 evitados

  Contexto: Participante autenticado
    Dado que eu esteja autenticado como "DOADOR"

  Cenário: Cadastro bem-sucedido de registro ESG
    Dado que eu tenha os seguintes dados do registro ESG:
      | campo          | valor |
      | donationId     | 1     |
      | residuoEvitado | 12.5  |
      | co2Evitado     | 6.3   |
    Quando eu enviar a requisição para o endpoint "/api/registros" de cadastro de registros ESG
    Então o status code da resposta deve ser 201
    E o registro ESG retornado deve conter os dados:
      | campo          | valor |
      | donationId     | 1     |
      | residuoEvitado | 12.5  |
      | co2Evitado     | 6.3   |
    E que o arquivo de contrato esperado é o "Registro ESG"
    Então a resposta da requisição deve estar em conformidade com o contrato selecionado

  Cenário: Cadastro de registro ESG sem sucesso com resíduo acima do limite
    Dado que eu tenha os seguintes dados do registro ESG:
      | campo          | valor |
      | donationId     | 1     |
      | residuoEvitado | 1000  |
      | co2Evitado     | 6.3   |
    Quando eu enviar a requisição para o endpoint "/api/registros" de cadastro de registros ESG
    Então o status code da resposta deve ser 400
    E o corpo de resposta de erro de validação deve retornar a mensagem "The field ResiduoEvitado must be between 0 and 999.99." para o campo "ResiduoEvitado"
    E que o arquivo de contrato esperado é o "Erro de validação"
    Então a resposta da requisição deve estar em conformidade com o contrato selecionado

  Cenário: Cadastro de registro ESG sem token
    Dado que eu não esteja autenticado
    E que eu tenha os seguintes dados do registro ESG:
      | campo          | valor |
      | donationId     | 1     |
      | residuoEvitado | 12.5  |
      | co2Evitado     | 6.3   |
    Quando eu enviar a requisição para o endpoint "/api/registros" de cadastro de registros ESG
    Então o status code da resposta deve ser 401

  Cenário: Listagem de registros ESG com token
    Quando eu enviar a requisição para o endpoint "/api/registros?page=1&pageSize=10" de listagem de registros ESG
    Então o status code da resposta deve ser 200
    E a listagem deve retornar a página 1 com pelo menos 5 registros
    E que o arquivo de contrato esperado é o "Lista de registros ESG"
    Então a resposta da requisição deve estar em conformidade com o contrato selecionado

  Cenário: Listagem de registros ESG sem token
    Dado que eu não esteja autenticado
    Quando eu enviar a requisição para o endpoint "/api/registros" de listagem de registros ESG
    Então o status code da resposta deve ser 401

  Cenário: Consulta de registro ESG por ID
    Dado que eu tenha os seguintes dados do registro ESG:
      | campo          | valor |
      | donationId     | 2     |
      | residuoEvitado | 40    |
      | co2Evitado     | 18.75 |
    E eu enviar a requisição para o endpoint "/api/registros" de cadastro de registros ESG
    E que eu recupere o ID do registro ESG criado
    Quando eu enviar a requisição com o ID para o endpoint "/api/registros" de consulta de registro ESG
    Então o status code da resposta deve ser 200
    E o registro ESG retornado deve conter os dados:
      | campo          | valor |
      | donationId     | 2     |
      | residuoEvitado | 40.0  |
      | co2Evitado     | 18.75 |
    E que o arquivo de contrato esperado é o "Registro ESG"
    Então a resposta da requisição deve estar em conformidade com o contrato selecionado

  Cenário: Consulta de registro ESG com ID inexistente
    Quando eu enviar a requisição com o ID 999999 para o endpoint "/api/registros" de consulta de registro ESG
    Então o status code da resposta deve ser 404
    E o corpo de resposta de erro da api deve retornar a mensagem "Registro ESG 999999 não encontrado."
    E que o arquivo de contrato esperado é o "Erro de recurso não encontrado"
    Então a resposta da requisição deve estar em conformidade com o contrato selecionado

  Cenário: Atualização bem-sucedida de registro ESG
    Dado que eu tenha os seguintes dados do registro ESG:
      | campo          | valor |
      | donationId     | 3     |
      | residuoEvitado | 10    |
      | co2Evitado     | 5     |
    E eu enviar a requisição para o endpoint "/api/registros" de cadastro de registros ESG
    E que eu recupere o ID do registro ESG criado
    E que eu altere os seguintes dados do registro ESG:
      | campo          | valor |
      | residuoEvitado | 22.4  |
      | co2Evitado     | 11.2  |
    Quando eu enviar a requisição com o ID para o endpoint "/api/registros" de atualização de registro ESG
    Então o status code da resposta deve ser 200
    E o registro ESG retornado deve conter os dados:
      | campo          | valor |
      | residuoEvitado | 22.4  |
      | co2Evitado     | 11.2  |
    E que o arquivo de contrato esperado é o "Registro ESG"
    Então a resposta da requisição deve estar em conformidade com o contrato selecionado

  Cenário: Atualização de registro ESG com ID inexistente
    Dado que eu tenha os seguintes dados do registro ESG:
      | campo          | valor |
      | donationId     | 3     |
      | residuoEvitado | 10    |
      | co2Evitado     | 5     |
    Quando eu enviar a requisição com o ID 999999 para o endpoint "/api/registros" de atualização de registro ESG
    Então o status code da resposta deve ser 404
    E o corpo de resposta de erro da api deve retornar a mensagem "Registro ESG 999999 não encontrado."

  Cenário: Deve ser possível deletar um registro ESG
    Dado que eu tenha os seguintes dados do registro ESG:
      | campo          | valor |
      | donationId     | 4     |
      | residuoEvitado | 8     |
      | co2Evitado     | 4     |
    E eu enviar a requisição para o endpoint "/api/registros" de cadastro de registros ESG
    E que eu recupere o ID do registro ESG criado
    Quando eu enviar a requisição com o ID para o endpoint "/api/registros" de deleção de registro ESG
    Então o status code da resposta deve ser 204
    E o corpo da resposta deve estar vazio
    E o registro ESG deletado não deve mais ser encontrado no endpoint "/api/registros"

  Cenário: Deleção de registro ESG com ID inexistente
    Quando eu enviar a requisição com o ID 999999 para o endpoint "/api/registros" de deleção de registro ESG
    Então o status code da resposta deve ser 404
    E o corpo de resposta de erro da api deve retornar a mensagem "Registro ESG 999999 não encontrado."
