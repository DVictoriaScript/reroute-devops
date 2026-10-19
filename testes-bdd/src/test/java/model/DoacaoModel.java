package model;

import com.google.gson.annotations.Expose;
import lombok.Data;

@Data
public class DoacaoModel {
    @Expose(serialize = false)
    private long donationId;
    @Expose
    private Long participantId;
    @Expose
    private String nomeItem;
    @Expose
    private String descricao;
    @Expose
    private String tamanho;
    @Expose
    private String localRetirada;
    @Expose
    private String status;
    @Expose(serialize = false)
    private String createdAt;
}
