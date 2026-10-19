package steps;

import io.cucumber.java.pt.Dado;
import io.cucumber.java.pt.E;
import io.cucumber.java.pt.Quando;
import org.junit.Assert;
import services.EntregaService;

import java.util.List;
import java.util.Map;

public class EntregaSteps {
    EntregaService entregaService = new EntregaService();

    @Dado("que eu tenha os seguintes dados da entrega:")
    public void queEuTenhaOsSeguintesDados(List<Map<String, String>> rows) {
        for (Map<String, String> columns : rows) {
            entregaService.setFieldsEntrega(columns.get("campo"), columns.get("valor"));
        }
    }

    @Dado("que eu altere os seguintes dados da entrega:")
    public void queEuAltereOsSeguintesDados(List<Map<String, String>> rows) {
        for (Map<String, String> columns : rows) {
            entregaService.setFieldsEntrega(columns.get("campo"), columns.get("valor"));
        }
    }

    @Quando("eu enviar a requisição para o endpoint {string} de cadastro de entregas")
    public void euEnviarARequisicaoDeCadastro(String endPoint) {
        entregaService.createEntrega(endPoint);
    }

    @Dado("que eu recupere o ID da entrega criada")
    public void queEuRecupereOID() {
        entregaService.retrieveIdEntrega();
    }

    @Quando("eu enviar a requisição para o endpoint {string} de listagem de entregas")
    public void euEnviarARequisicaoDeListagem(String endPoint) {
        entregaService.listEntrega(endPoint);
    }

    @Quando("eu enviar a requisição com o ID para o endpoint {string} de consulta de entrega")
    public void euEnviarARequisicaoDeConsulta(String endPoint) {
        entregaService.getEntrega(endPoint);
    }

    @Quando("eu enviar a requisição com o ID {int} para o endpoint {string} de consulta de entrega")
    public void euEnviarARequisicaoDeConsultaComId(int id, String endPoint) {
        entregaService.getEntrega(endPoint, id);
    }

    @Quando("eu enviar a requisição com o ID para o endpoint {string} de atualização de entrega")
    public void euEnviarARequisicaoDeAtualizacao(String endPoint) {
        entregaService.updateEntrega(endPoint);
    }

    @Quando("eu enviar a requisição com o ID {int} para o endpoint {string} de atualização de entrega")
    public void euEnviarARequisicaoDeAtualizacaoComId(int id, String endPoint) {
        entregaService.updateEntrega(endPoint, id);
    }

    @Quando("eu enviar a requisição com o ID para o endpoint {string} de deleção de entrega")
    public void euEnviarARequisicaoDeDelecao(String endPoint) {
        entregaService.deleteEntrega(endPoint);
    }

    @Quando("eu enviar a requisição com o ID {int} para o endpoint {string} de deleção de entrega")
    public void euEnviarARequisicaoDeDelecaoComId(int id, String endPoint) {
        entregaService.deleteEntrega(endPoint, id);
    }

    @E("a entrega retornada deve conter os dados:")
    public void oCorpoDaRespostaDeveConterOsDados(List<Map<String, String>> rows) {
        for (Map<String, String> columns : rows) {
            Assert.assertEquals(columns.get("valor"), entregaService.getFieldResponse(columns.get("campo")));
        }
    }

    @E("a entrega deletada não deve mais ser encontrada no endpoint {string}")
    public void aEntregaDeletadaNaoDeveMaisSerEncontrada(String endPoint) {
        entregaService.getEntrega(endPoint);
        Assert.assertEquals(404, entregaService.response.statusCode());
    }
}
