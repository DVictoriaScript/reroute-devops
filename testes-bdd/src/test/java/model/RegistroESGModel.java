package model;

import com.google.gson.annotations.Expose;
import lombok.Data;

@Data
public class RegistroESGModel {
    @Expose(serialize = false)
    private long registroId;
    @Expose
    private Long donationId;
    @Expose
    private Double residuoEvitado;
    @Expose
    private Double co2Evitado;
    @Expose(serialize = false)
    private String dataRegistro;
}
