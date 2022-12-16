package prosper.pets.resttest.buscarapida;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import prosper.pets.resttest.AbstractCachorrosApiTest;

import java.util.Map;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.is;
import static org.junit.jupiter.api.Assertions.assertEquals;

public class CachorrosApiGetBuscaRapidaTest extends AbstractCachorrosApiTest {

    @Test
    @DisplayName("GET /pets/busca-rapida sem autenticação status 401")
    void testGetSemAutenticacao() {
        given().get("/pets/busca-rapida").then()
                .statusCode(401);
    }

    @Test
    @DisplayName("GET /pets/busca-rapida qualquer usuário status 204")
    void testGetComAutenticacao() {
        Map<String, String> parametros = Map.of(
            "nome", getTextoAleatorio(),
            "nomeDono", getTextoAleatorio()
        );

        getRequisicaoUsuario()
                .params(parametros).get("/busca-rapida").then()
                .statusCode(204);

        getRequisicaoAdmin()
                .params(parametros).get("/busca-rapida").then()
                .statusCode(204);
    }

    @Test
    @DisplayName("GET /pets/busca-rapida se encontrar status 200 com conteúdo correto")
    void testGetAposCadastro() {
        Map corpoValido = getMapaDeJson("/pet-valido.json");

        corpoValido.put("nome", getTextoAleatorio());
        corpoValido.put("nomeDono", getTextoAleatorio());

        getRequisicaoAdmin().body(corpoValido).post().then().statusCode(201);

        Map parametros = Map.of(
            "nome", corpoValido.get("nome"),
            "nomeDono", corpoValido.get("nomeDono")
        );

        getRequisicaoAdmin()
                .params(parametros).get("/busca-rapida").then()
                .statusCode(200)
                .body("[0].nome", equalTo(corpoValido.get("nome")))
                .body("[0].nomeDono", equalTo(corpoValido.get("nomeDono")))
                .body("size()", is(1));

        getRequisicaoAdmin().body(corpoValido).post().then().statusCode(201);
        getRequisicaoAdmin().body(corpoValido).post().then().statusCode(201);
        
        getRequisicaoAdmin()
                .params(parametros).get("/busca-rapida").then()
                .statusCode(200)
                .body("size()", is(3));
    }

}
