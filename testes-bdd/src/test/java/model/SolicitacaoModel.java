package model;

import com.google.gson.annotations.Expose;
import lombok.Data;

@Data
public class SolicitacaoModel {
    @Expose(serialize = false)
    private long solicitacaoId;
    @Expose
    private Long donationId;
    @Expose
    private Long participantId;
    @Expose(serialize = false)
    private String dataSolicitacao;
    @Expose
    private String status;
}
