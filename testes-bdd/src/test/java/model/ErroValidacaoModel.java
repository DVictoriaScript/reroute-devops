package model;

import com.google.gson.annotations.Expose;
import lombok.Data;

import java.util.List;
import java.util.Map;

// Erro de validação devolvido pelo ASP.NET quando o corpo enviado é inválido
@Data
public class ErroValidacaoModel {
    @Expose
    private String title;
    @Expose
    private int status;
    @Expose
    private Map<String, List<String>> errors;
}
