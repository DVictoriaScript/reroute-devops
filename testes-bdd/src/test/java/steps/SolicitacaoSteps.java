package steps;

import io.cucumber.java.pt.Dado;
import io.cucumber.java.pt.E;
import io.cucumber.java.pt.Quando;
import org.junit.Assert;
import services.SolicitacaoService;

import java.util.List;
import java.util.Map;

public class SolicitacaoSteps {
    SolicitacaoService solicitacaoService = new SolicitacaoService();

    @Dado("que eu tenha os seguintes dados da solicitação:")
    public void queEuTenhaOsSeguintesDados(List<Map<String, String>> rows) {
        for (Map<String, String> columns : rows) {
            solicitacaoService.setFieldsSolicitacao(columns.get("campo"), columns.get("valor"));
        }
    }

    @Dado("que eu altere os seguintes dados da solicitação:")
    public void queEuAltereOsSeguintesDados(List<Map<String, String>> rows) {
        for (Map<String, String> columns : rows) {
            solicitacaoService.setFieldsSolicitacao(columns.get("campo"), columns.get("valor"));
        }
    }

    @Quando("eu enviar a requisição para o endpoint {string} de cadastro de solicitações")
    public void euEnviarARequisicaoDeCadastro(String endPoint) {
        solicitacaoService.createSolicitacao(endPoint);
    }

    @Dado("que eu recupere o ID da solicitação criada")
    public void queEuRecupereOID() {
        solicitacaoService.retrieveIdSolicitacao();
    }

    @Quando("eu enviar a requisição para o endpoint {string} de listagem de solicitações")
    public void euEnviarARequisicaoDeListagem(String endPoint) {
        solicitacaoService.listSolicitacao(endPoint);
    }

    @Quando("eu enviar a requisição com o ID para o endpoint {string} de consulta de solicitação")
    public void euEnviarARequisicaoDeConsulta(String endPoint) {
        solicitacaoService.getSolicitacao(endPoint);
    }

    @Quando("eu enviar a requisição com o ID {int} para o endpoint {string} de consulta de solicitação")
    public void euEnviarARequisicaoDeConsultaComId(int id, String endPoint) {
        solicitacaoService.getSolicitacao(endPoint, id);
    }

    @Quando("eu enviar a requisição com o ID para o endpoint {string} de atualização de solicitação")
    public void euEnviarARequisicaoDeAtualizacao(String endPoint) {
        solicitacaoService.updateSolicitacao(endPoint);
    }

    @Quando("eu enviar a requisição com o ID {int} para o endpoint {string} de atualização de solicitação")
    public void euEnviarARequisicaoDeAtualizacaoComId(int id, String endPoint) {
        solicitacaoService.updateSolicitacao(endPoint, id);
    }

    @Quando("eu enviar a requisição com o ID para o endpoint {string} de deleção de solicitação")
    public void euEnviarARequisicaoDeDelecao(String endPoint) {
        solicitacaoService.deleteSolicitacao(endPoint);
    }

    @Quando("eu enviar a requisição com o ID {int} para o endpoint {string} de deleção de solicitação")
    public void euEnviarARequisicaoDeDelecaoComId(int id, String endPoint) {
        solicitacaoService.deleteSolicitacao(endPoint, id);
    }

    @E("a solicitação retornada deve conter os dados:")
    public void oCorpoDaRespostaDeveConterOsDados(List<Map<String, String>> rows) {
        for (Map<String, String> columns : rows) {
            Assert.assertEquals(columns.get("valor"), solicitacaoService.getFieldResponse(columns.get("campo")));
        }
    }

    @E("a solicitação deletada não deve mais ser encontrada no endpoint {string}")
    public void aSolicitacaoDeletadaNaoDeveMaisSerEncontrada(String endPoint) {
        solicitacaoService.getSolicitacao(endPoint);
        Assert.assertEquals(404, solicitacaoService.response.statusCode());
    }
}
