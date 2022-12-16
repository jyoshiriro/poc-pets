package prosper.pets.resttest;

import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import io.restassured.path.json.JsonPath;
import io.restassured.specification.RequestSpecification;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.Map;

import static io.restassured.RestAssured.given;

public class CachorrosApiPostV3Test {

    @BeforeAll
    static void setup() {
        RestAssured.baseURI = "http://api.petsperto.com";
        RestAssured.port = 9000;
        RestAssured.basePath = "/pets";
    }

    RequestSpecification getRequisicao() {
        return given().auth().basic("admin", "admin").contentType(ContentType.JSON);
    }

    @Test
    @DisplayName("POST /pets sem autenticação status 401")
    void testPostSemAutenticacao() {
        Map corpoValido = new JsonPath(getClass().getResource("/pet-valido.json")).getMap("");

        getRequisicao().body(corpoValido)
                .post().then()
                .statusCode(401);
    }

    @Test
    @DisplayName("POST /pets sem autorização status 403")
    void testPostSemAutorizacao() {
        Map corpoValido = new JsonPath(getClass().getResource("/pet-valido.json")).getMap("");

        getRequisicao().auth().basic("usuario1", "s1").body(corpoValido)
                .post().then()
                .statusCode(403);
    }

}
