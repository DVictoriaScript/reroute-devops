package model;

import com.google.gson.annotations.Expose;
import lombok.Data;

// Erro devolvido pelo login quando o e-mail ou a senha estão errados
@Data
public class ErrorMessageModel {
    @Expose
    private String mensagem;
}
