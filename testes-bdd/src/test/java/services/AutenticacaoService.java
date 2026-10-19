package services;

import model.LoginModel;
import model.LoginResponseModel;

public class AutenticacaoService extends BaseService {
    final LoginModel loginModel = new LoginModel();

    public void setFieldsLogin(String field, String value) {
        switch (field) {
            case "email" -> loginModel.setEmail(value);
            case "senha" -> loginModel.setSenha(value);
            default -> throw new IllegalStateException("Campo não esperado: " + field);
        }
    }

    public void login(String endPoint) {
        post(baseUrl + endPoint, gson.toJson(loginModel));
    }

    // Usuários do seed da API, um para cada perfil
    public void autenticarComPerfil(String perfil) {
        switch (perfil) {
            case "DOADOR" -> setCredenciais("joao@email.com", "senha1");
            case "RECEPTOR" -> setCredenciais("maria@email.com", "senha2");
            case "TRANSPORTADOR" -> setCredenciais("carlos@email.com", "senha3");
            default -> throw new IllegalStateException("Perfil não esperado: " + perfil);
        }
        token = null;
        login("/api/auth/login");
        if (response.statusCode() != 200) {
            throw new IllegalStateException("Falha no login do perfil " + perfil + ": " + response.statusCode());
        }
        token = gson.fromJson(response.jsonPath().prettify(), LoginResponseModel.class).getToken();
    }

    private void setCredenciais(String email, String senha) {
        loginModel.setEmail(email);
        loginModel.setSenha(senha);
    }
}
