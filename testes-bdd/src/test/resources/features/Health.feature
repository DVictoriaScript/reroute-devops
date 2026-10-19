# language: pt
@regressivo
Funcionalidade: Verificação de saúde da API
  Como responsável pela infraestrutura do ReRoute
  Quero consultar o endpoint de health
  Para que eu saiba se a API está no ar antes de rodar os testes

  Cenário: API respondendo no endpoint de health
    Quando eu enviar a requisição para o endpoint "/health" de verificação de saúde
    Então o status code da resposta deve ser 200
    E o corpo da resposta deve ser o texto "Healthy"
