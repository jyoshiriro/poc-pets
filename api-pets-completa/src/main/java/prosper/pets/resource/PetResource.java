package prosper.pets.resource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.annotation.Secured;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import prosper.pets.domain.Pet;
import prosper.pets.service.PetService;

import javax.annotation.security.RolesAllowed;
import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/pets")
public class PetResource {

    @Autowired
    private PetService petService;

    @GetMapping
    public ResponseEntity<List<Pet>> get(Pet petPesquisa) {
        return ResponseEntity.ok(petService.getLista(petPesquisa));
    }

    @GetMapping("/busca-rapida")
    public ResponseEntity<List<Pet>> get(@RequestParam String nome,
                                         @RequestParam String nomeDono) {
        return ResponseEntity.ok(petService.getLista(nome, nomeDono));
    }

    @GetMapping("/{id}")
    @RolesAllowed("ROLE_admin")
    public ResponseEntity<Pet> get(@PathVariable Long id) {
        return ResponseEntity.ok(petService.recuperar(id));
    }

    @PostMapping
    @Secured("ROLE_admin")
    public ResponseEntity<Pet> post(@RequestBody @Valid Pet novoPet,
                                    Authentication authentication) {
        return ResponseEntity.status(HttpStatus.CREATED).body(petService.criar(novoPet, authentication));
    }

    @PutMapping("/{id}")
    @Secured("ROLE_admin")
    public ResponseEntity<Pet> put(@PathVariable Long id,
                                   @RequestBody @Valid Pet pet,
                                   Authentication authentication) {
        petService.atualizar(id, pet, authentication);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }

    @DeleteMapping("/{id}")
    @Secured("ROLE_admin")
    public ResponseEntity<Pet> delete(@PathVariable Long id,
                                      Authentication authentication) {
        petService.excluir(id, authentication);
        return ResponseEntity.noContent().build();
    }

    @PatchMapping("/{id}/peso/{novoPeso}")
    @Secured("ROLE_admin")
    public ResponseEntity<Void> patch(@PathVariable Long id,
                                      @PathVariable Double novoPeso,
                                      Authentication authentication) {
        petService.atualizarPeso(id, novoPeso, authentication);
        return ResponseEntity.noContent().build();
    }

}
