package model;

import com.google.gson.annotations.Expose;
import lombok.Data;

// Erro padrão do middleware da API (ex.: recurso não encontrado)
@Data
public class ErroDetalheModel {
    @Expose
    private String title;
    @Expose
    private int status;
    @Expose
    private String detail;
}
