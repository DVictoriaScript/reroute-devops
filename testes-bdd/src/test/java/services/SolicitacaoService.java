package services;

import model.SolicitacaoModel;

public class SolicitacaoService extends BaseService {
    final SolicitacaoModel solicitacaoModel = new SolicitacaoModel();
    String idSolicitacao;

    public void setFieldsSolicitacao(String field, String value) {
        switch (field) {
            case "donationId" -> solicitacaoModel.setDonationId(Long.parseLong(value));
            case "participantId" -> solicitacaoModel.setParticipantId(Long.parseLong(value));
            case "status" -> solicitacaoModel.setStatus(value);
            default -> throw new IllegalStateException("Campo não esperado: " + field);
        }
    }

    public void createSolicitacao(String endPoint) {
        post(baseUrl + endPoint, gson.toJson(solicitacaoModel));
    }

    public void retrieveIdSolicitacao() {
        idSolicitacao = String.valueOf(gson.fromJson(response.jsonPath().prettify(), SolicitacaoModel.class).getSolicitacaoId());
    }

    public void listSolicitacao(String endPoint) {
        get(baseUrl + endPoint);
    }

    public void getSolicitacao(String endPoint) {
        get(urlComId(endPoint, idSolicitacao));
    }

    public void getSolicitacao(String endPoint, long id) {
        get(urlComId(endPoint, id));
    }

    public void updateSolicitacao(String endPoint) {
        put(urlComId(endPoint, idSolicitacao), gson.toJson(solicitacaoModel));
    }

    public void updateSolicitacao(String endPoint, long id) {
        put(urlComId(endPoint, id), gson.toJson(solicitacaoModel));
    }

    public void deleteSolicitacao(String endPoint) {
        delete(urlComId(endPoint, idSolicitacao));
    }

    public void deleteSolicitacao(String endPoint, long id) {
        delete(urlComId(endPoint, id));
    }

    // Lê o corpo da resposta com o Gson e devolve o valor do campo pedido
    public String getFieldResponse(String field) {
        SolicitacaoModel retorno = gson.fromJson(response.jsonPath().prettify(), SolicitacaoModel.class);
        return switch (field) {
            case "solicitacaoId" -> String.valueOf(retorno.getSolicitacaoId());
            case "donationId" -> String.valueOf(retorno.getDonationId());
            case "participantId" -> String.valueOf(retorno.getParticipantId());
            case "status" -> String.valueOf(retorno.getStatus());
            default -> throw new IllegalStateException("Campo não esperado: " + field);
        };
    }
}
