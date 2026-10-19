# language: pt
@regressivo
Funcionalidade: Autenticação de participantes
  Como participante do ReRoute
  Quero fazer login com e-mail e senha
  Para que eu receba um token JWT e consiga usar as rotas protegidas

  Cenário: Login bem-sucedido de doador
    Dado que eu tenha as seguintes credenciais de login:
      | campo | valor          |
      | email | joao@email.com |
      | senha | senha1         |
    Quando eu enviar a requisição para o endpoint "/api/auth/login" de login
    Então o status code da resposta deve ser 200
    E a resposta deve trazer um token JWT para o usuário "João Silva" com o perfil "DOADOR"
    E que o arquivo de contrato esperado é o "Login bem-sucedido"
    Então a resposta da requisição deve estar em conformidade com o contrato selecionado

  Cenário: Login bem-sucedido de receptor
    Dado que eu tenha as seguintes credenciais de login:
      | campo | valor           |
      | email | maria@email.com |
      | senha | senha2          |
    Quando eu enviar a requisição para o endpoint "/api/auth/login" de login
    Então o status code da resposta deve ser 200
    E a resposta deve trazer um token JWT para o usuário "Maria Souza" com o perfil "RECEPTOR"

  Cenário: Login bem-sucedido de transportador
    Dado que eu tenha as seguintes credenciais de login:
      | campo | valor            |
      | email | carlos@email.com |
      | senha | senha3           |
    Quando eu enviar a requisição para o endpoint "/api/auth/login" de login
    Então o status code da resposta deve ser 200
    E a resposta deve trazer um token JWT para o usuário "Carlos Lima" com o perfil "TRANSPORTADOR"

  Cenário: Login sem sucesso com senha incorreta
    Dado que eu tenha as seguintes credenciais de login:
      | campo | valor          |
      | email | joao@email.com |
      | senha | senhaerrada    |
    Quando eu enviar a requisição para o endpoint "/api/auth/login" de login
    Então o status code da resposta deve ser 401
    E o corpo de resposta de erro de login deve retornar a mensagem "Email ou senha inválidos."
    E que o arquivo de contrato esperado é o "Erro de login"
    Então a resposta da requisição deve estar em conformidade com o contrato selecionado

  Cenário: Login sem sucesso com e-mail não cadastrado
    Dado que eu tenha as seguintes credenciais de login:
      | campo | valor               |
      | email | naoexiste@email.com |
      | senha | senha1              |
    Quando eu enviar a requisição para o endpoint "/api/auth/login" de login
    Então o status code da resposta deve ser 401
    E o corpo de resposta de erro de login deve retornar a mensagem "Email ou senha inválidos."
