package steps;

import io.cucumber.java.pt.Dado;
import io.cucumber.java.pt.E;
import io.cucumber.java.pt.Quando;
import org.junit.Assert;
import services.RegistroESGService;

import java.util.List;
import java.util.Map;

public class RegistroESGSteps {
    RegistroESGService registroESGService = new RegistroESGService();

    @Dado("que eu tenha os seguintes dados do registro ESG:")
    public void queEuTenhaOsSeguintesDados(List<Map<String, String>> rows) {
        for (Map<String, String> columns : rows) {
            registroESGService.setFieldsRegistroESG(columns.get("campo"), columns.get("valor"));
        }
    }

    @Dado("que eu altere os seguintes dados do registro ESG:")
    public void queEuAltereOsSeguintesDados(List<Map<String, String>> rows) {
        for (Map<String, String> columns : rows) {
            registroESGService.setFieldsRegistroESG(columns.get("campo"), columns.get("valor"));
        }
    }

    @Quando("eu enviar a requisição para o endpoint {string} de cadastro de registros ESG")
    public void euEnviarARequisicaoDeCadastro(String endPoint) {
        registroESGService.createRegistroESG(endPoint);
    }

    @Dado("que eu recupere o ID do registro ESG criado")
    public void queEuRecupereOID() {
        registroESGService.retrieveIdRegistroESG();
    }

    @Quando("eu enviar a requisição para o endpoint {string} de listagem de registros ESG")
    public void euEnviarARequisicaoDeListagem(String endPoint) {
        registroESGService.listRegistroESG(endPoint);
    }

    @Quando("eu enviar a requisição com o ID para o endpoint {string} de consulta de registro ESG")
    public void euEnviarARequisicaoDeConsulta(String endPoint) {
        registroESGService.getRegistroESG(endPoint);
    }

    @Quando("eu enviar a requisição com o ID {int} para o endpoint {string} de consulta de registro ESG")
    public void euEnviarARequisicaoDeConsultaComId(int id, String endPoint) {
        registroESGService.getRegistroESG(endPoint, id);
    }

    @Quando("eu enviar a requisição com o ID para o endpoint {string} de atualização de registro ESG")
    public void euEnviarARequisicaoDeAtualizacao(String endPoint) {
        registroESGService.updateRegistroESG(endPoint);
    }

    @Quando("eu enviar a requisição com o ID {int} para o endpoint {string} de atualização de registro ESG")
    public void euEnviarARequisicaoDeAtualizacaoComId(int id, String endPoint) {
        registroESGService.updateRegistroESG(endPoint, id);
    }

    @Quando("eu enviar a requisição com o ID para o endpoint {string} de deleção de registro ESG")
    public void euEnviarARequisicaoDeDelecao(String endPoint) {
        registroESGService.deleteRegistroESG(endPoint);
    }

    @Quando("eu enviar a requisição com o ID {int} para o endpoint {string} de deleção de registro ESG")
    public void euEnviarARequisicaoDeDelecaoComId(int id, String endPoint) {
        registroESGService.deleteRegistroESG(endPoint, id);
    }

    @E("o registro ESG retornado deve conter os dados:")
    public void oCorpoDaRespostaDeveConterOsDados(List<Map<String, String>> rows) {
        for (Map<String, String> columns : rows) {
            Assert.assertEquals(columns.get("valor"), registroESGService.getFieldResponse(columns.get("campo")));
        }
    }

    @E("o registro ESG deletado não deve mais ser encontrado no endpoint {string}")
    public void oRegistroESGDeletadoNaoDeveMaisSerEncontrado(String endPoint) {
        registroESGService.getRegistroESG(endPoint);
        Assert.assertEquals(404, registroESGService.response.statusCode());
    }
}
