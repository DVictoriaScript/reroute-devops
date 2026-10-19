package steps;

import io.cucumber.java.pt.E;
import io.cucumber.java.pt.Quando;
import org.junit.Assert;
import services.HealthService;

public class HealthSteps {
    HealthService healthService = new HealthService();

    @Quando("eu enviar a requisição para o endpoint {string} de verificação de saúde")
    public void euEnviarARequisicaoParaOEndpointDeVerificacaoDeSaude(String endPoint) {
        healthService.verificarSaude(endPoint);
    }

    @E("o corpo da resposta deve ser o texto {string}")
    public void oCorpoDaRespostaDeveSerOTexto(String texto) {
        Assert.assertEquals(texto, healthService.response.getBody().asString());
    }
}
