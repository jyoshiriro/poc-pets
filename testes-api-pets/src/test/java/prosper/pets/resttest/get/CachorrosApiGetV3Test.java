package prosper.pets.resttest.get;

import io.restassured.http.ContentType;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import prosper.pets.resttest.AbstractCachorrosApiTest;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.blankOrNullString;

/*
Aqui fizemos a classe de teste ser subclasse da AbstractCachorrosApiTest
para reduzir o volume de código repetido aqui e para não termo que definir as configurações da API a ser testada aqui
 */
public class CachorrosApiGetV3Test extends AbstractCachorrosApiTest {

    @Test
    @DisplayName("GET /pets sem autenticação status 401")
    void testGetSemAutenticacao() {
        given().get().then()
                .statusCode(401);
    }

    @Test
    @DisplayName("GET /pets com conteúdo status 200 e application/json")
    void testGetComConteudo() {
        getRequisicaoUsuario().get().then()
                .statusCode(200)
                .contentType(ContentType.JSON);
        // getRequisicaoUsuario() veio da AbstractCachorrosApiTest
    }

    @Test
    @DisplayName("GET /pets sem conteúdo status 204 e sem corpo")
    void testGetSemConteudo() {
        getRequisicaoUsuario().param("nome", "miojo")
                .get().then()
                .statusCode(204)
                .body(blankOrNullString());
        // getRequisicaoUsuario() veio da AbstractCachorrosApiTest
    }

}
