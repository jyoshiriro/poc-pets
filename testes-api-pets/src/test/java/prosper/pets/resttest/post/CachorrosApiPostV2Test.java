package prosper.pets.resttest.post;

import io.restassured.http.ContentType;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import prosper.pets.resttest.AbstractCachorrosApiTest;

import java.time.LocalDateTime;
import java.util.Map;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.*;

/*
Aqui fizemos a classe de teste ser subclasse da AbstractCachorrosApiTest
para reduzir o volume de código repetido aqui e para não termo que definir as configurações da API a ser testada aqui
 */
public class CachorrosApiPostV2Test extends AbstractCachorrosApiTest {

    @Test
    @DisplayName("POST /pets sem autenticação status 401")
    void testPostSemAutenticacao() {
        Map corpoValido = getMapaDeJson("/pet-valido.json");
        // getMapaDeJson() veio da AbstractCachorrosApiTest e cria um JSON a partir do arquivo /src/test/resource/pet-valido.json

        given().contentType(ContentType.JSON).body(corpoValido)
                .post().then()
                .statusCode(401);
        /*
        No código acima, pedimos para o REST Assured:
        - efetuar um post /pets passando no corpo da requisição um JSON criado a partir do Map "corpoValido"
        - verificar se o status da resposta é 401 (Unauthorized),
          afinal não enviamos nenhum usuário
         */
    }

    @Test
    @DisplayName("POST /pets sem autorização status 403")
    void testPostSemAutorizacao() {
        Map corpoValido = getMapaDeJson("/pet-valido.json");

        getRequisicaoUsuario().body(corpoValido)
                .post().then()
                .statusCode(403);

        /*
        No código acima, pedimos para o REST Assured:
        - efetuar um post /pets passando no corpo da requisição um JSON criado a partir do Map "corpoValido"
        - verificar se o status da resposta é 403 (Forbidden),
          afinal nos autenticams com um usuário de perfil "usuario"
         */
    }

    @Test
    @DisplayName("POST /pets sem erros status 201 e c/ novos campos no corpo")
    void testPostSemErros() {
        Map corpoValido = getMapaDeJson("/pet-valido.json");

        getRequisicaoAdmin().body(corpoValido)
                .post().then()
                .statusCode(201)
                .assertThat()
                .body("$", hasKey("id"))
                .body("$", hasKey("idade"))
                .body("$", hasKey("idRaca"))
                .body("$", hasKey("criacao"))
                .body("$", hasKey("atualizacao"));
        /*
        No código acima, pedimos para o REST Assured:
        - efetuar um post /pets passando no corpo da requisição um JSON criado a partir do Map "corpoValido"
        - verificar se o status da resposta é 201 (Created),
          afinal nos autenticams com um usuário de perfil "admin"
        - verificar se o JSON raiz do corpo possui os campos "id", "idade", "idRaca"m "criacao" e "atualizacao"
         */

    }


    @Test
    @DisplayName("POST /pets sem erros realmente criou Pet")
    void testPostRealmenteCriou() {
        Map corpoValido = getMapaDeJson("/pet-valido.json");

        corpoValido.put("nascimento", LocalDateTime.now().minusDays(3).minusMonths(2).minusYears(1).toString());

        Map corpoResposta = getRequisicaoAdmin().body(corpoValido).post().then().extract().as(Map.class);
        /*
        No código acima, pedimos para o REST Assured
        efetuar um post /pets passando no corpo da requisição um JSON criado a partir do Map "corpoValido"
        */

        getRequisicaoAdmin().get("/" + corpoResposta.get("id")).then()
                .body("id", equalTo(corpoResposta.get("id")))
                .body("nome", equalTo(corpoResposta.get("nome")))
                .body("nomeDono", equalTo(corpoResposta.get("nomeDono")))
                .body("telefoneDono", equalTo(corpoResposta.get("telefoneDono")))
                .body("idade", equalTo("1 ano, 2 meses e 3 dias"));
        /*
        No código acima, pedimos para o REST Assured:
        - efetuar um get /pets/{id} passando como "id" o id obtido no corpo da resposta da requisição anterior
        - verificar se o JSON raiz:
          - tem o campo "id" igual ao "id" que veio no corpo da resposta da requisição anterior
          - tem o campo "nome" igual ao "nome" que veio no corpo da resposta da requisição anterior
          - tem o campo "nomeDono" igual ao "nomeDono" que veio no corpo da resposta da requisição anterior
          - tem o campo "telefoneDono" igual ao "telefoneDono" que veio no corpo da resposta da requisição anterior
          - tem o campo "idade" com valor correto, considerando o valor de "nascimento" enciado no POST anterior
         */

    }


    @Test
    @DisplayName("POST /pets com nome do pet e CPF do dono inválidos status 400")
    void testPostNomeCpfInvalidos() {
        Map corpoInvalido = getMapaDeJson("/pet-nascimento-cpfdono-invalidos.json");

        getRequisicaoAdmin().body(corpoInvalido)
                .post().then()
                .statusCode(400)
                .assertThat()
                .body(containsString(corpoInvalido.get("nascimento").toString()))
                .body(containsString(corpoInvalido.get("cpfDono").toString()));
        /*
        No código acima, pedimos para o REST Assured:
        - efetuar um post /pets passando no corpo da requisição um JSON criado a partir do Map "corpoInvalido"
        - verificar se o status da resposta é 400 (Bed Request),
        - verificar se o JSON do corpo da resposta contém o valor do "nascimento" enviado na requisição
        - verificar se o JSON do corpo da resposta contém o valor do "cpfDono" enviado na requisição
         */
    }
}
