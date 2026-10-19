package hook;

import io.cucumber.java.After;
import io.cucumber.java.AfterAll;
import io.cucumber.java.Before;
import io.cucumber.java.BeforeAll;
import io.cucumber.java.Scenario;
import services.BaseService;

public class Hook {

    @BeforeAll
    public static void setUpBeforeClass() {
        System.out.println("Iniciando os testes da API ReRoute em "
                + System.getProperty("api.url", System.getenv().getOrDefault("API_URL", "http://localhost:8080")));
    }

    @AfterAll
    public static void tearDownAfterClass() {
        System.out.println("Fim da execução. Relatório em target/cucumber-reports.html");
    }

    @Before
    public void setUp(Scenario scenario) {
        // Cada cenário começa sem token e sem resposta anterior, para ficar independente
        BaseService.limparContexto();
        System.out.println("Iniciando o cenário: " + scenario.getName());
    }

    @After
    public void tearDown(Scenario scenario) {
        System.out.println("Cenário finalizado com status " + scenario.getStatus());
        BaseService.limparContexto();
    }
}
