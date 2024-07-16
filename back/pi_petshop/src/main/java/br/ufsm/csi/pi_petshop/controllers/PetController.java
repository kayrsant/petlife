package br.ufsm.csi.pi_petshop.controllers;

import br.ufsm.csi.pi_petshop.entity.cliente.models.ClienteModel;
import br.ufsm.csi.pi_petshop.entity.funcionario.models.FuncionarioModel;
import br.ufsm.csi.pi_petshop.entity.funcionario.repositories.FuncionarioRepository;
import br.ufsm.csi.pi_petshop.entity.pet.dtos.PetDTO;
import br.ufsm.csi.pi_petshop.entity.pet.dtos.PetEmailDTO;
import br.ufsm.csi.pi_petshop.entity.pet.dtos.PetResponseDTO;
import br.ufsm.csi.pi_petshop.entity.pet.models.PetModel;
import br.ufsm.csi.pi_petshop.entity.pet.repositories.PetRepository;
import br.ufsm.csi.pi_petshop.entity.user.enums.UserRole;
import br.ufsm.csi.pi_petshop.entity.user.models.UserModel;
import br.ufsm.csi.pi_petshop.entity.user.repositories.UserRepository;
import br.ufsm.csi.pi_petshop.security.TokenService;
import br.ufsm.csi.pi_petshop.services.ClienteService;
import br.ufsm.csi.pi_petshop.services.PetService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/pet")
@CrossOrigin(origins = "http://localhost:5173")
public class PetController {

    private static final Logger logger = LoggerFactory.getLogger(ClienteController.class);

    @Autowired
    PetService petService;

    @Autowired
    TokenService tokenService;
    @Autowired
    private PetRepository petRepository;
    @Autowired
    private ClienteService clienteService;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private FuncionarioRepository funcionarioRepository;

    @PostMapping("/create")
    public ResponseEntity<?> create(@RequestBody @Valid PetEmailDTO petDTO){
        return petService.savePet(petDTO);
    }

    @GetMapping
    public List<PetResponseDTO> getAll(){
        return petService.getAllPets();
    }

    @GetMapping("/{id}")
    public PetResponseDTO getById(@PathVariable Long id){
        PetResponseDTO petResponseDTO = null;
        if(petRepository.existsById(id)){
            Optional<PetModel> petOptional = petRepository.findById(id);
            petResponseDTO = new PetResponseDTO(
                    petOptional.get().getId(),
                    petOptional.get().getNome(),
                    petOptional.get().getIdade(),
                    petOptional.get().getRaca(),
                    petOptional.get().getTipo(),
                    petOptional.get().getPeso(),
                    petOptional.get().getGenero(),
                    petOptional.get().getAlergias(),
                    petOptional.get().getEmTratamento(),
                    petOptional.get().getCliente()
            );
        }
        return petResponseDTO;
    }

    @GetMapping("/cliente")
    public ResponseEntity<?> getAllPetsByClientId(HttpServletRequest request) {
        String token = extractToken(request);
        String username = tokenService.getSubject(token);

        Long idCliente = petService.getUserIdByEmail(username);

        if (idCliente != null) {
            List<PetResponseDTO> pets = petService.getAllPetsByClientId(idCliente);
            return ResponseEntity.ok(pets);
        } else {
            System.out.println("não encontrei nada");
            return ResponseEntity.notFound().build();
        }
    }

    @GetMapping("/cliente/{id}")
    public ResponseEntity<?> getPetsByClientId(@PathVariable Long id) {
        List<PetResponseDTO> pets = petService.getAllPetsByClientId(id);
        if (pets.isEmpty()) {
            System.out.println("Não encontrei pets para o cliente com ID " + id);
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(pets);
    }



    private String extractToken(HttpServletRequest request) {
        String authHeader = request.getHeader("Authorization");
        if (authHeader != null && authHeader.startsWith("Bearer ")) {
            return authHeader.substring(7);
        }
        return null;
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteById(@PathVariable Long id) {
        Optional<PetModel> petModelOptional = petRepository.findById(id);
        Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();

        if (petModelOptional.isPresent()) {
            ClienteModel clienteModel = clienteService.getClienteByEmail(((UserDetails) principal).getUsername());
            FuncionarioModel funcionarioModel = funcionarioRepository.findByEmail(((UserDetails) principal).getUsername());
            if (funcionarioModel != null || petModelOptional.get().getCliente().getId().equals(clienteModel.getId())) {
                petService.deletePet(id);
                return ResponseEntity.status(HttpStatus.OK).body("Pet deletado com sucesso.");
            }
        }
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Pet não encontrado.");
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> updateById(@PathVariable Long id, @RequestBody @Valid PetDTO petDTO) {
        return petService.updatePet(id, petDTO);
    }
}
