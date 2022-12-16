package prosper.pets.resource;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import prosper.pets.domain.Pet;
import prosper.pets.domain.racas.TipoRaca;

import java.time.LocalDate;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringRunner.class)
@SpringBootTest
@AutoConfigureMockMvc
class PetResourceSegurancaTest {

    private static final String URI_BASE = "/pets";

    @Autowired
    MockMvc mockMvc;

    @Autowired
    ObjectMapper mapper;
    

    @Test
    @DisplayName("post de Pet deve retornar status 401 se nenhum usuário")
    void post401() throws Exception {
        Pet requisicao = new Pet();

        mockMvc.perform(
                post(URI_BASE).contentType(MediaType.APPLICATION_JSON)
                .content(mapper.writeValueAsString(requisicao)))
                .andExpect(status().isUnauthorized());
    }

    @Test
    @DisplayName("post de Pet deve retornar status 403 se usuário sem permissão")
    @WithMockUser(roles = "hacker")
    void post403() throws Exception {
        Pet requisicao = new Pet();
        requisicao.setCpfDono("56254606046");
        requisicao.setFilhotes(0);
        requisicao.setNascimento(LocalDate.now().minusYears(2));
        requisicao.setNome("Fofura");
        requisicao.setEmailDono("eee@ggg.com");
        requisicao.setNomeDono("Zé Ruela");
        requisicao.setTelefoneDono("11 22222-3333");
        requisicao.setPeso(1.99);
        requisicao.setRaca("bulldog");
        requisicao.setTipo(TipoRaca.CACHORRO);

        mockMvc.perform(
                post(URI_BASE).contentType(MediaType.APPLICATION_JSON)
                .content(mapper.writeValueAsString(requisicao)))
                .andExpect(status().isForbidden());
    }

    @Test
    @DisplayName("post de Pet deve retornar status 201 se usuário autorizado")
    @WithMockUser(roles = "admin")
    void post201() throws Exception {
        Pet requisicao = new Pet();
        requisicao.setCpfDono("56254606046");
        requisicao.setFilhotes(0);
        requisicao.setNascimento(LocalDate.now().minusYears(2));
        requisicao.setNome("Fofura");
        requisicao.setEmailDono("eee@ggg.com");
        requisicao.setNomeDono("Zé Ruela");
        requisicao.setTelefoneDono("11 22222-3333");
        requisicao.setPeso(1.99);
        requisicao.setRaca("bulldog");
        requisicao.setTipo(TipoRaca.CACHORRO);

        mockMvc.perform(
                post(URI_BASE).contentType(MediaType.APPLICATION_JSON)
                .content(mapper.writeValueAsString(requisicao)))
                .andExpect(status().isCreated());
    }

    @Test
    @DisplayName("get deve retornar status 401 se nenhum usuário")
    void get401() throws Exception {
        mockMvc.perform(
                 get(URI_BASE))
                .andExpect(status().isUnauthorized());
    }

    @Test
    @DisplayName("get deve retornar status 403 se usuário sem permissão")
    @WithMockUser(roles = "hacker")
    void get403() throws Exception {
        mockMvc.perform(
                get(URI_BASE))
                .andExpect(status().isForbidden());
    }

    @Test
    @DisplayName("get deve retornar status 200 se usuário autorizado")
    @WithMockUser(roles = "usuario")
    void get204() throws Exception {
        mockMvc.perform(
                get(URI_BASE))
                .andExpect(status().isOk());
    }

}