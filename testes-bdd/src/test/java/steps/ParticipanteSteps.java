package steps;

import io.cucumber.java.pt.Dado;
import io.cucumber.java.pt.E;
import io.cucumber.java.pt.Quando;
import org.junit.Assert;
import services.ParticipanteService;

import java.util.List;
import java.util.Map;

public class ParticipanteSteps {
    ParticipanteService participanteService = new ParticipanteService();

    @Dado("que eu tenha os seguintes dados do participante:")
    public void queEuTenhaOsSeguintesDados(List<Map<String, String>> rows) {
        for (Map<String, String> columns : rows) {
            participanteService.setFieldsParticipante(columns.get("campo"), columns.get("valor"));
        }
    }

    @Dado("que eu altere os seguintes dados do participante:")
    public void queEuAltereOsSeguintesDados(List<Map<String, String>> rows) {
        for (Map<String, String> columns : rows) {
            participanteService.setFieldsParticipante(columns.get("campo"), columns.get("valor"));
        }
    }

    @Quando("eu enviar a requisição para o endpoint {string} de cadastro de participantes")
    public void euEnviarARequisicaoDeCadastro(String endPoint) {
        participanteService.createParticipante(endPoint);
    }

    @Dado("que eu recupere o ID do participante criado")
    public void queEuRecupereOID() {
        participanteService.retrieveIdParticipante();
    }

    @Quando("eu enviar a requisição para o endpoint {string} de listagem de participantes")
    public void euEnviarARequisicaoDeListagem(String endPoint) {
        participanteService.listParticipante(endPoint);
    }

    @Quando("eu enviar a requisição com o ID para o endpoint {string} de consulta de participante")
    public void euEnviarARequisicaoDeConsulta(String endPoint) {
        participanteService.getParticipante(endPoint);
    }

    @Quando("eu enviar a requisição com o ID {int} para o endpoint {string} de consulta de participante")
    public void euEnviarARequisicaoDeConsultaComId(int id, String endPoint) {
        participanteService.getParticipante(endPoint, id);
    }

    @Quando("eu enviar a requisição com o ID para o endpoint {string} de atualização de participante")
    public void euEnviarARequisicaoDeAtualizacao(String endPoint) {
        participanteService.updateParticipante(endPoint);
    }

    @Quando("eu enviar a requisição com o ID {int} para o endpoint {string} de atualização de participante")
    public void euEnviarARequisicaoDeAtualizacaoComId(int id, String endPoint) {
        participanteService.updateParticipante(endPoint, id);
    }

    @Quando("eu enviar a requisição com o ID para o endpoint {string} de deleção de participante")
    public void euEnviarARequisicaoDeDelecao(String endPoint) {
        participanteService.deleteParticipante(endPoint);
    }

    @Quando("eu enviar a requisição com o ID {int} para o endpoint {string} de deleção de participante")
    public void euEnviarARequisicaoDeDelecaoComId(int id, String endPoint) {
        participanteService.deleteParticipante(endPoint, id);
    }

    @E("o participante retornado deve conter os dados:")
    public void oCorpoDaRespostaDeveConterOsDados(List<Map<String, String>> rows) {
        for (Map<String, String> columns : rows) {
            Assert.assertEquals(columns.get("valor"), participanteService.getFieldResponse(columns.get("campo")));
        }
    }

    @E("o participante deletado não deve mais ser encontrado no endpoint {string}")
    public void oParticipanteDeletadoNaoDeveMaisSerEncontrado(String endPoint) {
        participanteService.getParticipante(endPoint);
        Assert.assertEquals(404, participanteService.response.statusCode());
    }
}
