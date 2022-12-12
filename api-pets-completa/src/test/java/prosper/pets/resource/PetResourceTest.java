package prosper.pets.resource;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.security.core.Authentication;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.web.server.ResponseStatusException;
import prosper.pets.domain.Pet;
import prosper.pets.domain.racas.TipoRaca;
import prosper.pets.service.PetService;

import java.security.Principal;
import java.time.LocalDate;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(controllers = PetResource.class)
@AutoConfigureMockMvc(addFilters = false)
class PetResourceTest {

    private static final String URI_BASE = "/pets";

    @MockBean
    PetService service;

    @Autowired
    MockMvc mockMvc;

    @Autowired
    ObjectMapper mapper;

    Principal principal;

    @BeforeEach
    void setup() {
        principal = mock(Authentication.class);

        when(principal.getName()).thenReturn("usuario-teste");
    }

    @Test
    @DisplayName("post de Pet deve retornar status 400 em caso de erro de validação")
    void postErroValidacao() throws Exception {
        Pet requisicao = new Pet();

        mockMvc.perform(
                    post(URI_BASE).contentType(MediaType.APPLICATION_JSON).principal(principal)
                    .content(mapper.writeValueAsString(requisicao)))
                    .andExpect(status().isBadRequest());
    }

    @Test
    @DisplayName("post de Pet deve retornar status 201 se tudo der certo")
    void postSemErro() throws Exception {
        Pet requisicao = new Pet();
        requisicao.setCpfDono("56254606046");
        requisicao.setFilhotes(0);
        requisicao.setNascimento(LocalDate.now().minusYears(2));
        requisicao.setEmailDono("eee@ggg.com");
        requisicao.setNome("Fofura");
        requisicao.setNomeDono("Zé Ruela");
        requisicao.setPeso(1.99);
        requisicao.setRaca("bulldog");
        requisicao.setTipo(TipoRaca.CACHORRO);

        mockMvc.perform(
                    post(URI_BASE).contentType(MediaType.APPLICATION_JSON).principal(principal)
                    .content(mapper.writeValueAsString(requisicao)))
                    .andExpect(status().isCreated());
    }

    @Test
    @DisplayName("get deve retornar 204 e sem corpo caso não existam dados")
    void getSemDados() throws Exception {
        when(service.getLista(any(Pet.class))).then(invocation -> {
            throw new ResponseStatusException(HttpStatus.NO_CONTENT);
        });

        MvcResult resposta = mockMvc.perform(
                        get(URI_BASE).principal(principal))
                .andExpect(status().isNoContent())
                .andReturn();

        assertEquals(0, resposta.getResponse().getContentLength());
    }

    @Test
    @DisplayName("get deve retornar 200 e com corpo caso existam dados")
    void getComDados() throws Exception {
        Pet pet1 = new Pet();
        pet1.setNome("p1");
        pet1.setPeso(11.1);
        Pet pet2 = new Pet();
        pet1.setNome("p22");
        pet1.setPeso(22.22);
        List<Pet> lista = List.of(pet1, pet2);

        when(service.getLista(any(Pet.class))).thenReturn(lista);

        MvcResult resposta = mockMvc.perform(
                        get(URI_BASE).principal(principal))
                .andExpect(status().isOk())
                .andReturn();

        List<Map<String, Object>> corpoResposta = mapper.readValue(
                resposta.getResponse().getContentAsString(),
                List.class
        );

        assertEquals(2, corpoResposta.size());
        assertEquals(lista.get(0).getNome(), corpoResposta.get(0).get("nome"));
        assertEquals(lista.get(0).getPeso(), corpoResposta.get(0).get("peso"));
        assertEquals(lista.get(1).getNome(), corpoResposta.get(1).get("nome"));
        assertEquals(lista.get(1).getPeso(), corpoResposta.get(1).get("peso"));
    }
}