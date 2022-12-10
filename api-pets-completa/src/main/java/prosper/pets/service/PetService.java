package prosper.pets.service;

import feign.FeignException;
import org.springframework.data.domain.Example;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;
import prosper.pets.config.apiclient.RacasApi;
import prosper.pets.domain.Pet;
import prosper.pets.domain.racas.RacaPet;
import prosper.pets.domain.racas.TipoRaca;
import prosper.pets.exception.PetNaoEncontradoException;
import prosper.pets.respository.PetRepository;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class PetService {

    private RacasApi racasApi;

    private RegistroLogService registroLogService;

    private PetRepository petRepository;

    public PetService(RacasApi racasApi, RegistroLogService registroLogService, PetRepository petRepository) {
        this.racasApi = racasApi;
        this.registroLogService = registroLogService;
        this.petRepository = petRepository;
    }

    public List<Pet> getLista(Pet petPesquisa) {
        return getListaOuStatus204(petRepository.findAll(Example.of(petPesquisa)));
    }

    public List<Pet> getLista(String nome, String nomeDono) {
        return getListaOuStatus204(petRepository.findByNomeAndNomeDonoContains(nome, nomeDono));
    }

    public Pet criar(Pet novoPet, Authentication authentication) {
        setRaca(novoPet);
        Pet pet = petRepository.save(novoPet);
        registrarLog(authentication, "Pet criado: %d", pet.getId());
        return pet;
    }

    public Pet atualizar(Long idPet, Pet pet, Authentication authentication) {
        pet.setId(idPet);
        setRaca(pet);
        petRepository.save(pet);
        registrarLog(authentication, "Pet %d atualizado", pet.getId());
        return pet;
    }

    public Pet recuperar(Long idPet) {
        validarId(idPet);
        return petRepository.getReferenceById(idPet);
    }

    public void excluir(Long idPet, Authentication authentication) {
        validarId(idPet);
        petRepository.deleteById(idPet);
        registrarLog(authentication, "Pet %d excluido", idPet);
    }

    protected void setRaca(Pet novoPet) {
        RacaPet raca = getRaca(novoPet.getTipo(), novoPet.getRaca());
        novoPet.setIdRaca(raca.getId());
    }

    public void atualizarPeso(Long idPet, Double novoPeso, Authentication authentication) {
        validarId(idPet);
        petRepository.atualizarPeso(idPet, novoPeso);
        registrarLog(authentication, "Pet %d teve o peso atualizado para %.3f", idPet, novoPeso);
    }

    protected List<Pet> getListaOuStatus204(List<Pet> pets) {
        if (pets.isEmpty()) {
            throw new ResponseStatusException(HttpStatus.NO_CONTENT);
        }
        return pets;
    }

    protected void registrarLog(Authentication authentication, String descricao, Object... parametros) {
        registroLogService.registrarLog(authentication.getName(), String.format(descricao, parametros));
    }

    protected void validarId(Long idPet) {
        if (!petRepository.existsById(idPet)) {
            throw new PetNaoEncontradoException();
        }
    }

    public RacaPet getRaca(TipoRaca tipo, String raca) {
        List<RacaPet> racas = null;

        try {
            racas = racasApi.get(tipo.getUri(), raca);
        } catch (FeignException ex) {
            if (ex.status() == -1) {
                throw new ResponseStatusException(
                        HttpStatus.SERVICE_UNAVAILABLE,
                        "API de Raças indisponível"
                );
            }
            throw new ResponseStatusException(
                HttpStatus.INTERNAL_SERVER_ERROR,
                    String.format("Erro na chamada à API de raças: %s", ex.getMessage()),
                ex
            );
        }

        if (racas.isEmpty()) {
            throw new ResponseStatusException(
                    HttpStatus.NOT_FOUND,
                    String.format("Raça '%s' não encontrada na API de raças", raca)
            );
        }

        if (racas.size() > 1) {
            throw new ResponseStatusException(
                    HttpStatus.CONFLICT,
                    String.format(
                        "Mais de um raça encontrada para '%s': %s",
                        raca, racas.stream().map(RacaPet::getRaca).collect(Collectors.joining(", "))
                    )
            );
        }

        return racas.get(0);
    }

}
