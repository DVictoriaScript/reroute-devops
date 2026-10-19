package model;

import com.google.gson.annotations.Expose;
import lombok.Data;

import java.util.List;

// Resposta paginada usada em todas as listagens da API
@Data
public class ListaPaginadaModel {
    @Expose
    private List<Object> items;
    @Expose
    private int page;
    @Expose
    private int pageSize;
    @Expose
    private int totalItems;
    @Expose
    private int totalPages;
    @Expose
    private boolean hasPrevious;
    @Expose
    private boolean hasNext;
}
