package steps;

import com.networknt.schema.ValidationMessage;
import io.cucumber.java.pt.Dado;
import io.cucumber.java.pt.E;
import io.cucumber.java.pt.Então;
import model.ErroDetalheModel;
import model.ErroValidacaoModel;
import model.ListaPaginadaModel;
import org.junit.Assert;
import services.AutenticacaoService;
import services.BaseService;

import java.io.IOException;
import java.util.List;
import java.util.Set;

// Passos usados por todas as funcionalidades (status code, contrato, erros e token)
public class ComumSteps {
    AutenticacaoService autenticacaoService = new AutenticacaoService();

    @Dado("que eu esteja autenticado como {string}")
    public void queEuEstejaAutenticadoComo(String perfil) {
        autenticacaoService.autenticarComPerfil(perfil);
    }

    @Dado("que eu não esteja autenticado")
    public void queEuNaoEstejaAutenticado() {
        BaseService.token = null;
    }

    @Então("o status code da resposta deve ser {int}")
    public void oStatusDaRespostaDeveSer(int statusCode) {
        Assert.assertEquals(statusCode, BaseService.response.statusCode());
    }

    @E("que o arquivo de contrato esperado é o {string}")
    public void queOArquivoDeContratoEsperadoEO(String contract) throws IOException {
        autenticacaoService.setContract(contract);
    }

    @Então("a resposta da requisição deve estar em conformidade com o contrato selecionado")
    public void aRespostaDaRequisicaoDeveEstarEmConformidadeComOContratoSelecionado() throws IOException {
        Set<ValidationMessage> validateResponse = autenticacaoService.validateResponseAgainstSchema();
        Assert.assertTrue("O contrato está inválido. Erros encontrados: " + validateResponse, validateResponse.isEmpty());
    }

    @E("o corpo de resposta de erro da api deve retornar a mensagem {string}")
    public void oCorpoDeRespostaDeErroDaApiDeveRetornarAMensagem(String message) {
        ErroDetalheModel erro = BaseService.gson.fromJson(BaseService.response.jsonPath().prettify(), ErroDetalheModel.class);
        Assert.assertEquals(message, erro.getDetail());
    }

    @E("o corpo de resposta de erro de validação deve retornar a mensagem {string} para o campo {string}")
    public void oCorpoDeRespostaDeErroDeValidacaoDeveRetornarAMensagem(String message, String campo) {
        ErroValidacaoModel erro = BaseService.gson.fromJson(BaseService.response.jsonPath().prettify(), ErroValidacaoModel.class);
        List<String> mensagens = erro.getErrors().get(campo);
        Assert.assertNotNull("O campo " + campo + " não apareceu nos erros: " + erro.getErrors(), mensagens);
        Assert.assertTrue("Mensagens do campo " + campo + ": " + mensagens, mensagens.contains(message));
    }

    @E("a listagem deve retornar a página {int} com pelo menos {int} registro(s)")
    public void aListagemDeveRetornarAPagina(int pagina, int quantidadeMinima) {
        ListaPaginadaModel lista = BaseService.gson.fromJson(BaseService.response.jsonPath().prettify(), ListaPaginadaModel.class);
        Assert.assertEquals(pagina, lista.getPage());
        Assert.assertTrue("A listagem retornou " + lista.getItems().size() + " registros",
                lista.getItems().size() >= quantidadeMinima);
        Assert.assertTrue(lista.getTotalItems() >= lista.getItems().size());
    }

    @E("o corpo da resposta deve estar vazio")
    public void oCorpoDaRespostaDeveEstarVazio() {
        Assert.assertEquals("", BaseService.response.getBody().asString());
    }
}
