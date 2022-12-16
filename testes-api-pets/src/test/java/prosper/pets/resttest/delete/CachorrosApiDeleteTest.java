package prosper.pets.resttest.delete;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import prosper.pets.resttest.AbstractCachorrosApiTest;

import java.util.Map;

import static org.hamcrest.Matchers.blankOrNullString;

public class CachorrosApiDeleteTest extends AbstractCachorrosApiTest {

    @Test
    @DisplayName("DELETE /pets com id inválido status 404")
    void testDeleteInexistente() {
        getRequisicaoAdmin().delete("/-100").then()
                .statusCode(404);
    }

    @Test
    @DisplayName("DELETE /pets com id válido status 204 e sem corpo")
    void testDeleteExistente() {
        Map corpoValido = getMapaDeJson("/pet-valido.json");

        Map petCriado = getRequisicaoAdmin().body(corpoValido).post().then().extract().as(Map.class);

        getRequisicaoAdmin().delete("/"+petCriado.get("id")).then()
                .statusCode(204)
                .body(blankOrNullString());
    }

    @Test
    @DisplayName("DELETE /pets com id válido exclui mesmo")
    void testDeleteExcluiMesmo() {
        Map corpoValido = getMapaDeJson("/pet-valido.json");

        Map petCriado = getRequisicaoAdmin().body(corpoValido).post().then().extract().as(Map.class);
        String uriIdExcluido = "/"+petCriado.get("id");

        getRequisicaoAdmin().delete(uriIdExcluido).then().statusCode(204);

        getRequisicaoUsuario().get(uriIdExcluido).then().statusCode(404);
    }
}
