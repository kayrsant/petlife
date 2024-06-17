package br.ufsm.csi.pi_petshop.controllers;

import br.ufsm.csi.pi_petshop.cliente.dtos.ClienteDTO;
import br.ufsm.csi.pi_petshop.cliente.dtos.ClienteUpdateDTO;
import br.ufsm.csi.pi_petshop.cliente.models.ClienteModel;
import br.ufsm.csi.pi_petshop.security.TokenService;
import br.ufsm.csi.pi_petshop.services.ClienteService;
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

        if (principal instanceof UserDetails) {
            ClienteModel clienteModel = clienteService.getClienteByEmail(((UserDetails) principal).getUsername());
            if (clienteModel != null) {
                ClienteDTO clienteDTO = new ClienteDTO(clienteModel.getId(), clienteModel.getNome(), clienteModel.getEmail(), clienteModel.getTelefone(), clienteModel.getCep(), clienteModel.getCpf());
                return ResponseEntity.status(HttpStatus.OK).body(clienteDTO);
            } else {
                logger.info("Cliente não encontrado.");
            }
        }
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Cliente não encontrado.");
    }

    @GetMapping("/cliente")
    public ResponseEntity<?> getClienteByEmail(@PathVariable String email) {
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

    @PutMapping("/{id}")
    public ResponseEntity<?> updateById(@PathVariable Long id, @RequestBody @Valid ClienteDTO clienteDTO) {
        return clienteService.updateClienteWithId(id, clienteDTO);
    }

    @PutMapping
    public ResponseEntity<?> updateCliente(@RequestBody @Valid ClienteDTO clienteDTO) {
        ClienteModel cliente = clienteService.getClienteByEmail(clienteDTO.email());
        if (cliente != null) {
            ClienteUpdateDTO novoCliente = new ClienteUpdateDTO(cliente.getId(), clienteDTO.nome(), clienteDTO.email(), clienteDTO.telefone(), clienteDTO.cep(), clienteDTO.cpf(), new Date(System.currentTimeMillis()));
            return clienteService.updateCliente(novoCliente);
        }
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Cliente Não Encontrado.");
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
