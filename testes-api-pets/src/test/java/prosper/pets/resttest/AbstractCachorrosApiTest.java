package prosper.pets.resttest;

import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import io.restassured.path.json.JsonPath;
import io.restassured.specification.RequestSpecification;
import org.junit.jupiter.api.BeforeAll;

import java.util.Map;

import static io.restassured.RestAssured.given;

/*
Essa classe abstrata contém configurações e métodos que podemos usar em vários cenários de testes neste projeto
 */
public abstract class AbstractCachorrosApiTest {

    @BeforeAll // acontecerá apenas 1x e antes de qualquer método de teste da classe ser executado
    static void setup() {
        RestAssured.baseURI = "http://api.petsperto.com"; // definindo o "endereço" API que será testada
        RestAssured.port = 9000; // definindo a porta da API que será testada
        RestAssured.basePath = "/pets";  // definindo o caminho base da API que será testada
    }

    // cria e retorna uma requisição do tipo "basic" com login "usuario1" e senha "s1" e com cabeçalho "content-type":application/json"
    protected RequestSpecification getRequisicaoUsuario() {
        return given().auth().basic("usuario1", "s1").contentType(ContentType.JSON);
    }

    // cria e retorna uma requisição do tipo "basic" com login "admin" e senha "admin" e com cabeçalho "content-type":application/json"
    protected RequestSpecification getRequisicaoAdmin() {
        return given().auth().basic("admin", "admin").contentType(ContentType.JSON);
    }

    // cria e retorna um Map criado a partir de um arquivo JSON válido em /src/test/resources
    protected Map getMapaDeJson(String arquivoJson) {
        return new JsonPath(getClass().getResource(arquivoJson)).getMap("$");
        // getMap("$") -> indica que usaremos o JSON raiz para criar o Map
    }

}
