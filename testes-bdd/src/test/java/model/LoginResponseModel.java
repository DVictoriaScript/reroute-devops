package model;

import com.google.gson.annotations.Expose;
import lombok.Data;

@Data
public class LoginResponseModel {
    @Expose
    private String token;
    @Expose
    private String nome;
    @Expose
    private String email;
    @Expose
    private String tipoPerfil;
    @Expose
    private String expiraEm;
}
