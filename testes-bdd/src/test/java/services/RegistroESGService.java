package services;

import model.RegistroESGModel;

public class RegistroESGService extends BaseService {
    final RegistroESGModel registroESGModel = new RegistroESGModel();
    String idRegistroESG;

    public void setFieldsRegistroESG(String field, String value) {
        switch (field) {
            case "donationId" -> registroESGModel.setDonationId(Long.parseLong(value));
            case "residuoEvitado" -> registroESGModel.setResiduoEvitado(Double.parseDouble(value));
            case "co2Evitado" -> registroESGModel.setCo2Evitado(Double.parseDouble(value));
            default -> throw new IllegalStateException("Campo não esperado: " + field);
        }
    }

    public void createRegistroESG(String endPoint) {
        post(baseUrl + endPoint, gson.toJson(registroESGModel));
    }

    public void retrieveIdRegistroESG() {
        idRegistroESG = String.valueOf(gson.fromJson(response.jsonPath().prettify(), RegistroESGModel.class).getRegistroId());
    }

    public void listRegistroESG(String endPoint) {
        get(baseUrl + endPoint);
    }

    public void getRegistroESG(String endPoint) {
        get(urlComId(endPoint, idRegistroESG));
    }

    public void getRegistroESG(String endPoint, long id) {
        get(urlComId(endPoint, id));
    }

    public void updateRegistroESG(String endPoint) {
        put(urlComId(endPoint, idRegistroESG), gson.toJson(registroESGModel));
    }

    public void updateRegistroESG(String endPoint, long id) {
        put(urlComId(endPoint, id), gson.toJson(registroESGModel));
    }

    public void deleteRegistroESG(String endPoint) {
        delete(urlComId(endPoint, idRegistroESG));
    }

    public void deleteRegistroESG(String endPoint, long id) {
        delete(urlComId(endPoint, id));
    }

    // Lê o corpo da resposta com o Gson e devolve o valor do campo pedido
    public String getFieldResponse(String field) {
        RegistroESGModel retorno = gson.fromJson(response.jsonPath().prettify(), RegistroESGModel.class);
        return switch (field) {
            case "registroId" -> String.valueOf(retorno.getRegistroId());
            case "donationId" -> String.valueOf(retorno.getDonationId());
            case "residuoEvitado" -> String.valueOf(retorno.getResiduoEvitado());
            case "co2Evitado" -> String.valueOf(retorno.getCo2Evitado());
            default -> throw new IllegalStateException("Campo não esperado: " + field);
        };
    }
}
