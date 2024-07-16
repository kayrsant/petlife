package br.ufsm.csi.pi_petshop.controllers;

import br.ufsm.csi.pi_petshop.entity.cliente.dtos.ClienteDTO;
import br.ufsm.csi.pi_petshop.entity.cliente.dtos.ClienteUpdateDTO;
import br.ufsm.csi.pi_petshop.entity.cliente.models.ClienteModel;
import br.ufsm.csi.pi_petshop.entity.user.enums.UserRole;
import br.ufsm.csi.pi_petshop.entity.user.models.UserModel;
import br.ufsm.csi.pi_petshop.entity.user.repositories.UserRepository;
import br.ufsm.csi.pi_petshop.security.TokenService;
import br.ufsm.csi.pi_petshop.services.ClienteService;
import br.ufsm.csi.pi_petshop.services.UserService;
import jakarta.validation.Valid;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import java.util.Date;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/cliente")
@CrossOrigin(origins = "http://localhost:5173")
public class ClienteController {

    private static final Logger logger = LoggerFactory.getLogger(ClienteController.class);

    @Autowired
    ClienteService clienteService;

    @Autowired
    TokenService tokenService;

    @Autowired
    private UserService userService;

    @Autowired
    private UserRepository userRepository;

    @PostMapping("/create")
    public ResponseEntity<?> create(@RequestBody @Valid ClienteDTO clienteDTO) {
        return clienteService.saveCliente(clienteDTO);
    }

    @GetMapping("/todos")
    public ResponseEntity<List<ClienteDTO>> getAll() {
        List<ClienteDTO> clientes = clienteService.getAllClientes();
        if (clientes.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(clientes);
        }
        return ResponseEntity.ok(clientes);
    }

    @GetMapping
    public ResponseEntity<?> getCliente() {
        Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();

        try {
            if (principal instanceof UserDetails) {
                ClienteModel clienteModel = clienteService.getClienteByEmail(((UserDetails) principal).getUsername());
                if (clienteModel != null) {
                    ClienteDTO clienteDTO = new ClienteDTO(clienteModel.getId(), clienteModel.getNome(), clienteModel.getEmail(), clienteModel.getTelefone(), clienteModel.getCep(), clienteModel.getCpf());
                    return ResponseEntity.status(HttpStatus.OK).body(clienteDTO);
                } else {
                    logger.info("Cliente não encontrado.");
                }
            }
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Cliente não encontrado.");
        }
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Cliente não encontrado.");
    }

    @GetMapping("/cliente")
    public ResponseEntity<?> getClienteByEmail(@RequestParam String email) {
        if (email == null || email.isEmpty()) {
            return ResponseEntity.badRequest().body("Email não pode ser vazio.");
        }

        Optional<ClienteModel> clienteModel = Optional.ofNullable(clienteService.getClienteByEmail(email));
        if (clienteModel.isPresent()) {
            return ResponseEntity.ok(clienteModel.get());
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Cliente não encontrado");
        }
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> getClienteById(@PathVariable Long id) {
        if (id == null) {
            return ResponseEntity.badRequest().body("ID não pode ser vazio.");
        }

        Optional<ClienteModel> clienteModel = clienteService.getClienteById(id);
        if (clienteModel.isPresent()) {
            return ResponseEntity.ok(clienteModel.get());
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Cliente não encontrado");
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> updateById(@PathVariable Long id, @RequestBody @Valid ClienteUpdateDTO clienteUpdateDTO) {
        return clienteService.updateCliente(clienteUpdateDTO, id);
    }

    @PutMapping
    public ResponseEntity<?> updateByEmail(@RequestBody @Valid ClienteUpdateDTO clienteUpdateDTO) {
        try {
            ClienteModel clienteModel = clienteService.getClienteByEmail(clienteUpdateDTO.email());
            if (clienteModel != null) {
                clienteModel.setNome(clienteUpdateDTO.nome());
                clienteModel.setEmail(clienteUpdateDTO.email());
                clienteModel.setTelefone(clienteUpdateDTO.telefone());
                clienteModel.setCep(clienteUpdateDTO.cep());
                clienteModel.setCpf(clienteUpdateDTO.cpf());

                ClienteDTO clienteDTO = new ClienteDTO(clienteUpdateDTO.id(), clienteUpdateDTO.nome(), clienteUpdateDTO.email(), clienteUpdateDTO.telefone(), clienteUpdateDTO.cep(), clienteUpdateDTO.cpf());
                clienteService.updateCliente(clienteUpdateDTO, clienteModel.getId());

                return ResponseEntity.status(HttpStatus.OK).body(clienteDTO);
            }
        }  catch (Exception e){
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Erro ao atualizar cliente.");
        }
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Erro ao atualizar cliente.");
    }

    @PutMapping("/{id}/role")
    public ResponseEntity<?> updateRole(@PathVariable Long id, @RequestParam UserRole role) {
        try {
            ClienteModel clienteModel = clienteService.getClienteById(id).orElse(null);
            if (clienteModel != null) {
                UserModel userModel = userRepository.findModelByEmail(clienteModel.getEmail());
                if (userModel != null) {
                    userService.updateRole(userModel.getId(), role);
                    return ResponseEntity.ok().body("Role atualizada com sucesso!");
                } else {
                    return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Usuário não encontrado.");
                }
            } else {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Cliente não encontrado.");
            }
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Erro ao atualizar a role do cliente.");
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteById(@PathVariable Long id) {
        Optional<ClienteModel> cliente = clienteService.getClienteById(id);
        if (cliente.isPresent()) {
            clienteService.deleteCliente(id);
            return ResponseEntity.ok().build();
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Cliente não encontrado.");
        }
    }
}
