package services;

import model.DoacaoModel;

public class DoacaoService extends BaseService {
    final DoacaoModel doacaoModel = new DoacaoModel();
    String idDoacao;

    public void setFieldsDoacao(String field, String value) {
        switch (field) {
            case "participantId" -> doacaoModel.setParticipantId(Long.parseLong(value));
            case "nomeItem" -> doacaoModel.setNomeItem(value);
            case "descricao" -> doacaoModel.setDescricao(value);
            case "tamanho" -> doacaoModel.setTamanho(value);
            case "localRetirada" -> doacaoModel.setLocalRetirada(value);
            case "status" -> doacaoModel.setStatus(value);
            default -> throw new IllegalStateException("Campo não esperado: " + field);
        }
    }

    public void createDoacao(String endPoint) {
        post(baseUrl + endPoint, gson.toJson(doacaoModel));
    }

    public void retrieveIdDoacao() {
        idDoacao = String.valueOf(gson.fromJson(response.jsonPath().prettify(), DoacaoModel.class).getDonationId());
    }

    public void listDoacao(String endPoint) {
        get(baseUrl + endPoint);
    }

    public void getDoacao(String endPoint) {
        get(urlComId(endPoint, idDoacao));
    }

    public void getDoacao(String endPoint, long id) {
        get(urlComId(endPoint, id));
    }

    public void updateDoacao(String endPoint) {
        put(urlComId(endPoint, idDoacao), gson.toJson(doacaoModel));
    }

    public void updateDoacao(String endPoint, long id) {
        put(urlComId(endPoint, id), gson.toJson(doacaoModel));
    }

    public void deleteDoacao(String endPoint) {
        delete(urlComId(endPoint, idDoacao));
    }

    public void deleteDoacao(String endPoint, long id) {
        delete(urlComId(endPoint, id));
    }

    // Lê o corpo da resposta com o Gson e devolve o valor do campo pedido
    public String getFieldResponse(String field) {
        DoacaoModel retorno = gson.fromJson(response.jsonPath().prettify(), DoacaoModel.class);
        return switch (field) {
            case "donationId" -> String.valueOf(retorno.getDonationId());
            case "participantId" -> String.valueOf(retorno.getParticipantId());
            case "nomeItem" -> String.valueOf(retorno.getNomeItem());
            case "descricao" -> String.valueOf(retorno.getDescricao());
            case "tamanho" -> String.valueOf(retorno.getTamanho());
            case "localRetirada" -> String.valueOf(retorno.getLocalRetirada());
            case "status" -> String.valueOf(retorno.getStatus());
            default -> throw new IllegalStateException("Campo não esperado: " + field);
        };
    }
}
