package prosper.pets.resttest.post;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import prosper.pets.resttest.AbstractCachorrosApiTest;

import java.util.Map;

/*
Aqui fizemos a classe de teste ser subclasse da AbstractCachorrosApiTest
para reduzir o volume de código repetido aqui e para não termo que definir as configurações da API a ser testada aqui
 */
public class CachorrosApiPostV1Test extends AbstractCachorrosApiTest {

    @Test
    @DisplayName("POST /pets com corpo válido status 201")
    void testPostSemAutenticacao() {
        Map corpoValido = getMapaDeJson("/pet-valido.json");
        // getMapaDeJson() veio da AbstractCachorrosApiTest e cria um JSON a partir do arquivo /src/test/resource/pet-valido.json

        // getRequisicaoAdmin() veio da AbstractCachorrosApiTest
        getRequisicaoAdmin().body(corpoValido)
                            .post().then()
                            .statusCode(201);

        /*
        No código acima, pedimos para o REST Assured:
        - efetuar um post /pets passando no corpo da requisição um JSON criado a partir do Map "corpoValido"
        - verificar se o status da resposta é 201 (Created),
          afinal usamos o usuário "admin" e enviamos um JSON válido
         */
    }

}
