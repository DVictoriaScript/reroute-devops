package services;

import model.ParticipanteModel;

public class ParticipanteService extends BaseService {
    final ParticipanteModel participanteModel = new ParticipanteModel();
    String idParticipante;

    public void setFieldsParticipante(String field, String value) {
        switch (field) {
            case "nome" -> participanteModel.setNome(value);
            case "email" -> participanteModel.setEmail(value);
            case "senha" -> participanteModel.setSenha(value);
            case "tipoPerfil" -> participanteModel.setTipoPerfil(value);
            default -> throw new IllegalStateException("Campo não esperado: " + field);
        }
    }

    public void createParticipante(String endPoint) {
        post(baseUrl + endPoint, gson.toJson(participanteModel));
    }

    public void retrieveIdParticipante() {
        idParticipante = String.valueOf(gson.fromJson(response.jsonPath().prettify(), ParticipanteModel.class).getParticipantId());
    }

    public void listParticipante(String endPoint) {
        get(baseUrl + endPoint);
    }

    public void getParticipante(String endPoint) {
        get(urlComId(endPoint, idParticipante));
    }

    public void getParticipante(String endPoint, long id) {
        get(urlComId(endPoint, id));
    }

    public void updateParticipante(String endPoint) {
        put(urlComId(endPoint, idParticipante), gson.toJson(participanteModel));
    }

    public void updateParticipante(String endPoint, long id) {
        put(urlComId(endPoint, id), gson.toJson(participanteModel));
    }

    public void deleteParticipante(String endPoint) {
        delete(urlComId(endPoint, idParticipante));
    }

    public void deleteParticipante(String endPoint, long id) {
        delete(urlComId(endPoint, id));
    }

    // Lê o corpo da resposta com o Gson e devolve o valor do campo pedido
    public String getFieldResponse(String field) {
        ParticipanteModel retorno = gson.fromJson(response.jsonPath().prettify(), ParticipanteModel.class);
        return switch (field) {
            case "participantId" -> String.valueOf(retorno.getParticipantId());
            case "nome" -> String.valueOf(retorno.getNome());
            case "email" -> String.valueOf(retorno.getEmail());
            case "tipoPerfil" -> String.valueOf(retorno.getTipoPerfil());
            default -> throw new IllegalStateException("Campo não esperado: " + field);
        };
    }
}
