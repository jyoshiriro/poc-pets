package prosper.pets.resttest.post;

import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import io.restassured.path.json.JsonPath;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;
import java.util.Map;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.*;

public class CachorrosApiPostV4Test {

    @BeforeAll
    static void setup() {
        RestAssured.baseURI = "http://api.petsperto.com";
        RestAssured.port = 9000;
        RestAssured.basePath = "/pets";
    }

    Map getMapaDeJson(String arquivoJson) {
        return new JsonPath(getClass().getResource(arquivoJson)).getMap("$");
    }

    @Test
    @DisplayName("POST /pets sem autenticação status 401")
    void testPostSemAutenticacao() {
        Map corpoValido = getMapaDeJson("/pet-valido.json");

        given().contentType(ContentType.JSON).body(corpoValido)
                .post().then()
                .statusCode(401);
    }

    @Test
    @DisplayName("POST /pets sem autorização status 403")
    void testPostSemAutorizacao() {
        Map corpoValido = getMapaDeJson("/pet-valido.json");

        given().auth().basic("usuario1", "s1").contentType(ContentType.JSON).body(corpoValido)
                .post().then()
                .statusCode(403);
    }

    @Test
    @DisplayName("POST /pets sem erros status 201 e c/ novos campos no corpo")
    void testPostSemErros() {
        Map corpoValido = getMapaDeJson("/pet-valido.json");

        given().auth().basic("admin", "admin").contentType(ContentType.JSON).body(corpoValido)
                .post().then()
                .statusCode(201)
                .assertThat()
                .body("id", isA(Integer.class))
                .body("idade", stringContainsInOrder("anos","meses","dias"))
                .body("idRaca", isA(Integer.class))
                .body("criacao", stringContainsInOrder("-","-","T",":",":","."))
                .body("atualizacao", resposta ->
                        equalTo(LocalDateTime.parse(resposta.path("atualizacao")).toString()));

    }

}
