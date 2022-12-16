package prosper.pets.resttest.get;

import io.restassured.http.ContentType;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static io.restassured.RestAssured.given;
import static org.junit.jupiter.api.Assertions.assertEquals;

public class CachorrosApiGetTest {

    @BeforeAll
    static void setup() {
//        RestAssured.baseURI = "http://60b6deef17d1dc0017b886b3.mockapi.io/api/v1";
//        RestAssured.port = 80;
//        RestAssured.basePath = "/pets";
    }

    @Test
    @DisplayName("GET /pets com conteúdo status 200 e application/json")
    void testGetComConteudo() {
        given().auth().basic("usuario1", "s1").get("/pets").then()
                .statusCode(200)
                .contentType(ContentType.JSON);
    }

    @Test
    @DisplayName("GET /pets/busca-rapida som conteúdo status 204 e sem corpo")
    void testGetBuscaRapidaSemConteudo() {
        int tamanhoResposta = given().auth().basic("usuario1", "s1").get("/pets/busca-rapida?nome=xyz&nomeDono=yxz").then()
                    .statusCode(204)
                    .extract().asByteArray().length;

        assertEquals(0, tamanhoResposta);
    }

}
