package prosper.pets.resttest.get;

import io.restassured.http.ContentType;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static io.restassured.RestAssured.given;

public class CachorrosApiGetV1Test {

    @Test
    @DisplayName("GET /pets sem autenticação status 401")
    void testGetSemAutenticacao() {
        given().get("/pets").then()
                .statusCode(401);
    }

    @Test
    @DisplayName("GET /pets com conteúdo status 200 e application/json")
    void testGetComConteudo() {
        given().auth().basic("usuario1","s1").get("/pets").then()
                .statusCode(200)
                .contentType(ContentType.JSON);
    }

}
