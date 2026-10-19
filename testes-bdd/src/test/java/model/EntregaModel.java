package model;

import com.google.gson.annotations.Expose;
import lombok.Data;

@Data
public class EntregaModel {
    @Expose(serialize = false)
    private long entregaId;
    @Expose
    private Long solicitacaoId;
    @Expose
    private Long participantId;
    @Expose
    private String dataAceite;
    @Expose
    private String dataEntrega;
    @Expose
    private String status;
}
