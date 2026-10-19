package steps;

import io.cucumber.java.pt.Dado;
import io.cucumber.java.pt.E;
import io.cucumber.java.pt.Quando;
import model.ErrorMessageModel;
import model.LoginResponseModel;
import org.junit.Assert;
import services.AutenticacaoService;

import java.util.List;
import java.util.Map;

public class AutenticacaoSteps {
    AutenticacaoService autenticacaoService = new AutenticacaoService();

    @Dado("que eu tenha as seguintes credenciais de login:")
    public void queEuTenhaAsSeguintesCredenciaisDeLogin(List<Map<String, String>> rows) {
        for (Map<String, String> columns : rows) {
            autenticacaoService.setFieldsLogin(columns.get("campo"), columns.get("valor"));
        }
    }

    @Quando("eu enviar a requisição para o endpoint {string} de login")
    public void euEnviarARequisicaoParaOEndpointDeLogin(String endPoint) {
        autenticacaoService.login(endPoint);
    }

    @E("a resposta deve trazer um token JWT para o usuário {string} com o perfil {string}")
    public void aRespostaDeveTrazerUmTokenJWT(String nome, String perfil) {
        LoginResponseModel login = autenticacaoService.gson.fromJson(
                autenticacaoService.response.jsonPath().prettify(), LoginResponseModel.class);
        // Um JWT tem três partes separadas por ponto: header.payload.assinatura
        Assert.assertEquals(3, login.getToken().split("\\.").length);
        Assert.assertEquals(nome, login.getNome());
        Assert.assertEquals(perfil, login.getTipoPerfil());
    }

    @E("o corpo de resposta de erro de login deve retornar a mensagem {string}")
    public void oCorpoDeRespostaDeErroDeLoginDeveRetornarAMensagem(String message) {
        ErrorMessageModel errorMessageModel = autenticacaoService.gson.fromJson(
                autenticacaoService.response.jsonPath().prettify(), ErrorMessageModel.class);
        Assert.assertEquals(message, errorMessageModel.getMensagem());
    }
}
