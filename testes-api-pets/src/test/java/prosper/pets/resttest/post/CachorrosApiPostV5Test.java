package prosper.pets.resttest.post;

import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import io.restassured.path.json.JsonPath;
import io.restassured.specification.RequestSpecification;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;
import java.util.Map;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.*;

public class CachorrosApiPostV5Test {

    @BeforeAll
    static void setup() {
        RestAssured.baseURI = "http://api.petsperto.com";
        RestAssured.port = 9000;
        RestAssured.basePath = "/pets";
    }

    Map getMapaDeJson(String arquivoJson) {
        return new JsonPath(getClass().getResource(arquivoJson)).getMap("$");
    }

    RequestSpecification getRequisicao() {
        return given().auth().basic("admin", "admin").contentType(ContentType.JSON);
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

        getRequisicao().body(corpoValido)
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

    @Test
    @DisplayName("POST /pets com tipo inválido status 400")
    void testPostTipoInvalido() {
        Map corpoInvalido = getMapaDeJson("/pet-tipo-invalido.json");

        getRequisicao().body(corpoInvalido)
                .post().then().log().ifError()
                .statusCode(400)
                .assertThat()
                .body(containsString(corpoInvalido.get("tipo").toString()));
    }

    @Test
    @DisplayName("POST /pets com nome do pet e CPF do dono inválidos status 400")
    void testPostNomeCpfInvalidos() {
        Map corpoInvalido = getMapaDeJson("/pet-nome-cpfdono-invalidos.json");

        getRequisicao().body(corpoInvalido)
                .post().then()
                .statusCode(400)
                .assertThat()
                .body("message", containsString("2"))
                .body(containsString("pet.nome"))
                .body(containsString(corpoInvalido.get("cpfDono").toString()))
                .extract().asString();
    }


    @Test
    @DisplayName("POST /pets sem erros realmente criou Pet")
    void testPostRealmenteCriou() {
        Map corpoValido = getMapaDeJson("/pet-valido.json");

        corpoValido.put("nascimento", LocalDateTime.now().minusDays(3).minusMonths(2).minusYears(1).toString());

        Map corpoResposta = getRequisicao().body(corpoValido).post().then().extract().as(Map.class);

        getRequisicao().get("/"+corpoResposta.get("id")).then()
                .body("id", equalTo(corpoResposta.get("id")))
                .body("nome", equalTo(corpoResposta.get("nome")))
                .body("nomeDono", equalTo(corpoResposta.get("nomeDono")))
                .body("telefoneDono", equalTo(corpoResposta.get("telefoneDono")))
                .body("idade", equalTo("1 ano, 2 meses e 3 dias"));
    }
}
