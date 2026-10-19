package services;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.networknt.schema.JsonSchema;
import com.networknt.schema.JsonSchemaFactory;
import com.networknt.schema.SpecVersion;
import com.networknt.schema.ValidationMessage;
import io.restassured.http.ContentType;
import io.restassured.response.Response;
import org.json.JSONObject;
import org.json.JSONTokener;

import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import static io.restassured.RestAssured.given;

// Parte comum de todos os services: requisições, token JWT e validação de contrato.
// A resposta e o token ficam estáticos para que os steps de recursos diferentes
// enxerguem a mesma requisição dentro do cenário. O Hook limpa tudo antes de cada cenário.
public class BaseService {
    public static final Gson gson = new GsonBuilder()
            .excludeFieldsWithoutExposeAnnotation()
            .create();
    public static Response response;
    public static String token;
    static String baseUrl = System.getProperty("api.url",
            System.getenv().getOrDefault("API_URL", "http://localhost:8080"));
    static String schemasPath = "src/test/resources/schemas/";
    static JSONObject jsonSchema;
    private static final ObjectMapper mapper = new ObjectMapper();

    public static void limparContexto() {
        response = null;
        token = null;
        jsonSchema = null;
    }

    // Só envia o header Authorization quando existe um token do login
    private Map<String, String> headers() {
        Map<String, String> headers = new HashMap<>();
        if (token != null) {
            headers.put("Authorization", "Bearer " + token);
        }
        return headers;
    }

    protected void post(String url, String bodyToSend) {
        response = given()
                .contentType(ContentType.JSON)
                .accept(ContentType.JSON)
                .headers(headers())
                .body(bodyToSend)
                .when()
                .post(url)
                .then()
                .extract()
                .response();
    }

    protected void get(String url) {
        response = given()
                .accept(ContentType.JSON)
                .headers(headers())
                .when()
                .get(url)
                .then()
                .extract()
                .response();
    }

    protected void put(String url, String bodyToSend) {
        response = given()
                .contentType(ContentType.JSON)
                .accept(ContentType.JSON)
                .headers(headers())
                .body(bodyToSend)
                .when()
                .put(url)
                .then()
                .extract()
                .response();
    }

    protected void delete(String url) {
        response = given()
                .accept(ContentType.JSON)
                .headers(headers())
                .when()
                .delete(url)
                .then()
                .extract()
                .response();
    }

    protected String urlComId(String endPoint, Object id) {
        return String.format("%s%s/%s", baseUrl, endPoint, id);
    }

    private JSONObject loadJsonFromFile(String filePath) throws IOException {
        try (InputStream inputStream = Files.newInputStream(Paths.get(filePath))) {
            JSONTokener tokener = new JSONTokener(inputStream);
            return new JSONObject(tokener);
        }
    }

    public void setContract(String contract) throws IOException {
        switch (contract) {
            case "Login bem-sucedido" -> jsonSchema = loadJsonFromFile(schemasPath + "login-bem-sucedido.json");
            case "Erro de login" -> jsonSchema = loadJsonFromFile(schemasPath + "erro-de-login.json");
            case "Erro de recurso não encontrado" -> jsonSchema = loadJsonFromFile(schemasPath + "erro-recurso-nao-encontrado.json");
            case "Erro de validação" -> jsonSchema = loadJsonFromFile(schemasPath + "erro-de-validacao.json");
            case "Participante" -> jsonSchema = loadJsonFromFile(schemasPath + "participante.json");
            case "Lista de participantes" -> jsonSchema = loadJsonFromFile(schemasPath + "lista-de-participantes.json");
            case "Doação" -> jsonSchema = loadJsonFromFile(schemasPath + "doacao.json");
            case "Lista de doações" -> jsonSchema = loadJsonFromFile(schemasPath + "lista-de-doacoes.json");
            case "Solicitação" -> jsonSchema = loadJsonFromFile(schemasPath + "solicitacao.json");
            case "Lista de solicitações" -> jsonSchema = loadJsonFromFile(schemasPath + "lista-de-solicitacoes.json");
            case "Entrega" -> jsonSchema = loadJsonFromFile(schemasPath + "entrega.json");
            case "Lista de entregas" -> jsonSchema = loadJsonFromFile(schemasPath + "lista-de-entregas.json");
            case "Registro ESG" -> jsonSchema = loadJsonFromFile(schemasPath + "registro-esg.json");
            case "Lista de registros ESG" -> jsonSchema = loadJsonFromFile(schemasPath + "lista-de-registros-esg.json");
            default -> throw new IllegalStateException("Contrato não esperado: " + contract);
        }
    }

    // Mesmo código da aula (fábrica V4). Os schemas declaram draft-07 igual ao gerado no extendsclass e o
    // networknt 1.5.1 usa o "$schema" do arquivo, então o V4 só valeria para um schema sem "$schema".
    public Set<ValidationMessage> validateResponseAgainstSchema() throws IOException {
        JSONObject jsonResponse = new JSONObject(response.getBody().asString());
        JsonSchemaFactory schemaFactory = JsonSchemaFactory.getInstance(SpecVersion.VersionFlag.V4);
        JsonSchema schema = schemaFactory.getSchema(jsonSchema.toString());
        JsonNode jsonResponseNode = mapper.readTree(jsonResponse.toString());
        Set<ValidationMessage> schemaValidationErrors = schema.validate(jsonResponseNode);
        return schemaValidationErrors;
    }
}
