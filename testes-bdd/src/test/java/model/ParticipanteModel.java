package model;

import com.google.gson.annotations.Expose;
import lombok.Data;

@Data
public class ParticipanteModel {
    @Expose(serialize = false)
    private long participantId;
    @Expose
    private String nome;
    @Expose
    private String email;
    // A senha só é enviada, a API não devolve esse campo
    @Expose(deserialize = false)
    private String senha;
    @Expose
    private String tipoPerfil;
    @Expose(serialize = false)
    private String createdAt;
}
