package steps;

import io.cucumber.java.pt.Dado;
import io.cucumber.java.pt.E;
import io.cucumber.java.pt.Quando;
import org.junit.Assert;
import services.DoacaoService;

import java.util.List;
import java.util.Map;

public class DoacaoSteps {
    DoacaoService doacaoService = new DoacaoService();

    @Dado("que eu tenha os seguintes dados da doação:")
    public void queEuTenhaOsSeguintesDados(List<Map<String, String>> rows) {
        for (Map<String, String> columns : rows) {
            doacaoService.setFieldsDoacao(columns.get("campo"), columns.get("valor"));
        }
    }

    @Dado("que eu altere os seguintes dados da doação:")
    public void queEuAltereOsSeguintesDados(List<Map<String, String>> rows) {
        for (Map<String, String> columns : rows) {
            doacaoService.setFieldsDoacao(columns.get("campo"), columns.get("valor"));
        }
    }

    @Quando("eu enviar a requisição para o endpoint {string} de cadastro de doações")
    public void euEnviarARequisicaoDeCadastro(String endPoint) {
        doacaoService.createDoacao(endPoint);
    }

    @Dado("que eu recupere o ID da doação criada")
    public void queEuRecupereOID() {
        doacaoService.retrieveIdDoacao();
    }

    @Quando("eu enviar a requisição para o endpoint {string} de listagem de doações")
    public void euEnviarARequisicaoDeListagem(String endPoint) {
        doacaoService.listDoacao(endPoint);
    }

    @Quando("eu enviar a requisição com o ID para o endpoint {string} de consulta de doação")
    public void euEnviarARequisicaoDeConsulta(String endPoint) {
        doacaoService.getDoacao(endPoint);
    }

    @Quando("eu enviar a requisição com o ID {int} para o endpoint {string} de consulta de doação")
    public void euEnviarARequisicaoDeConsultaComId(int id, String endPoint) {
        doacaoService.getDoacao(endPoint, id);
    }

    @Quando("eu enviar a requisição com o ID para o endpoint {string} de atualização de doação")
    public void euEnviarARequisicaoDeAtualizacao(String endPoint) {
        doacaoService.updateDoacao(endPoint);
    }

    @Quando("eu enviar a requisição com o ID {int} para o endpoint {string} de atualização de doação")
    public void euEnviarARequisicaoDeAtualizacaoComId(int id, String endPoint) {
        doacaoService.updateDoacao(endPoint, id);
    }

    @Quando("eu enviar a requisição com o ID para o endpoint {string} de deleção de doação")
    public void euEnviarARequisicaoDeDelecao(String endPoint) {
        doacaoService.deleteDoacao(endPoint);
    }

    @Quando("eu enviar a requisição com o ID {int} para o endpoint {string} de deleção de doação")
    public void euEnviarARequisicaoDeDelecaoComId(int id, String endPoint) {
        doacaoService.deleteDoacao(endPoint, id);
    }

    @E("a doação retornada deve conter os dados:")
    public void oCorpoDaRespostaDeveConterOsDados(List<Map<String, String>> rows) {
        for (Map<String, String> columns : rows) {
            Assert.assertEquals(columns.get("valor"), doacaoService.getFieldResponse(columns.get("campo")));
        }
    }

    @E("a doação deletada não deve mais ser encontrada no endpoint {string}")
    public void aDoacaoDeletadaNaoDeveMaisSerEncontrada(String endPoint) {
        doacaoService.getDoacao(endPoint);
        Assert.assertEquals(404, doacaoService.response.statusCode());
    }
}
