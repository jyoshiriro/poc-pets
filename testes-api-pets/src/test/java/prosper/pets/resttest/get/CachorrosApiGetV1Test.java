package prosper.pets.resttest.get;

import io.restassured.http.ContentType;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.blankOrNullString;
import static org.hamcrest.Matchers.greaterThan;

public class CachorrosApiGetV1Test {
    /*
    OBS: como não configuramos nada no REST Assured, ele vai tentar usar a URL
    http://localhost:8080
    para testar os EndPoints
     */
    @Test
    @DisplayName("GET /pets sem autenticação status 401")
    void testGetSemAutenticacao() {
        given().get("/pets").then()
                .statusCode(401);
        /*
        No código acima, pedimos para o REST Assured:
        - efetuar um get /pets
        - verificar se o status da resposta é 401 (Unauthorized),
          afinal não passamos nenhum usuário
         */
    }

    @Test
    @DisplayName("GET /pets sem autorização status 403")
    void testGetSemAutorizacao() {
        given().auth().basic("hacker","hacker").get("/pets").then()
                .statusCode(403)
                .contentType(ContentType.JSON);
        /*
        No código acima, pedimos para o REST Assured:
        - efetuar um get /pets
        - verificar se o status da resposta é 403 (Forbidden),
          afinal o usuário "hacker" não deveria ter permissão nenhuma na API
         */
    }

    @Test
    @DisplayName("GET /pets com conteúdo status 200 e application/json")
    void testGetComConteudo() {
        given().auth().basic("usuario1","s1").get("/pets").then()
                .statusCode(200)
                .contentType(ContentType.JSON)
                .body("size()", greaterThan(0));
        /*
        No código acima, pedimos para o REST Assured:
        - efetuar um get /pets com o usuário "usuario1"
        - verificar se o status da resposta é 200 (Ok)
        - verificar se o corpo da resposta tem tamanho maior 0 (se NÃO é vazio mesmo)
         */
    }

    @Test
    @DisplayName("GET /pets sem conteúdo status 204 e sem corpo")
    void testGetSemConteudo() {
        given().auth().basic("usuario1", "s1").param("nome", "miojo")
                .get("/pets").then()
                .statusCode(204)
                .body(blankOrNullString());
        /*
        No código acima, pedimos para o REST Assured:
        - efetuar um get /pets?nome=miojo com o usuário "usuario1"
        - verificar se o status da resposta é 204 (No Content)
        - verificar se o corpo da resposta está vazio mesmo
         */
    }

}
