package services;

import model.EntregaModel;

public class EntregaService extends BaseService {
    final EntregaModel entregaModel = new EntregaModel();
    String idEntrega;

    public void setFieldsEntrega(String field, String value) {
        switch (field) {
            case "solicitacaoId" -> entregaModel.setSolicitacaoId(Long.parseLong(value));
            case "participantId" -> entregaModel.setParticipantId(Long.parseLong(value));
            case "dataAceite" -> entregaModel.setDataAceite(value);
            case "dataEntrega" -> entregaModel.setDataEntrega(value);
            case "status" -> entregaModel.setStatus(value);
            default -> throw new IllegalStateException("Campo não esperado: " + field);
        }
    }

    public void createEntrega(String endPoint) {
        post(baseUrl + endPoint, gson.toJson(entregaModel));
    }

    public void retrieveIdEntrega() {
        idEntrega = String.valueOf(gson.fromJson(response.jsonPath().prettify(), EntregaModel.class).getEntregaId());
    }

    public void listEntrega(String endPoint) {
        get(baseUrl + endPoint);
    }

    public void getEntrega(String endPoint) {
        get(urlComId(endPoint, idEntrega));
    }

    public void getEntrega(String endPoint, long id) {
        get(urlComId(endPoint, id));
    }

    public void updateEntrega(String endPoint) {
        put(urlComId(endPoint, idEntrega), gson.toJson(entregaModel));
    }

    public void updateEntrega(String endPoint, long id) {
        put(urlComId(endPoint, id), gson.toJson(entregaModel));
    }

    public void deleteEntrega(String endPoint) {
        delete(urlComId(endPoint, idEntrega));
    }

    public void deleteEntrega(String endPoint, long id) {
        delete(urlComId(endPoint, id));
    }

    // Lê o corpo da resposta com o Gson e devolve o valor do campo pedido
    public String getFieldResponse(String field) {
        EntregaModel retorno = gson.fromJson(response.jsonPath().prettify(), EntregaModel.class);
        return switch (field) {
            case "entregaId" -> String.valueOf(retorno.getEntregaId());
            case "solicitacaoId" -> String.valueOf(retorno.getSolicitacaoId());
            case "participantId" -> String.valueOf(retorno.getParticipantId());
            case "dataAceite" -> String.valueOf(retorno.getDataAceite());
            case "dataEntrega" -> String.valueOf(retorno.getDataEntrega());
            case "status" -> String.valueOf(retorno.getStatus());
            default -> throw new IllegalStateException("Campo não esperado: " + field);
        };
    }
}
