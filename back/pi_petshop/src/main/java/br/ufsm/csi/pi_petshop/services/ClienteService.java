package br.ufsm.csi.pi_petshop.services;

import br.ufsm.csi.pi_petshop.entity.cliente.dtos.ClienteDTO;
import br.ufsm.csi.pi_petshop.entity.cliente.dtos.ClienteUpdateDTO;
import br.ufsm.csi.pi_petshop.entity.cliente.models.ClienteModel;
import br.ufsm.csi.pi_petshop.entity.cliente.repositories.ClienteRepository;
import br.ufsm.csi.pi_petshop.controllers.ClienteController;
import br.ufsm.csi.pi_petshop.entity.user.models.UserModel;
import br.ufsm.csi.pi_petshop.entity.user.repositories.UserRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.sql.Timestamp;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class ClienteService {

    private static final Logger logger = LoggerFactory.getLogger(ClienteController.class);

    @Autowired
    ClienteRepository clienteRepository;

    @Autowired
    UserService userService;

    @Autowired
    private UserRepository userRepository;

    public ResponseEntity<?> saveCliente(ClienteDTO clienteDTO) {
        if (clienteRepository.findByEmail(clienteDTO.email()) != null) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Cliente com este e-mail já existe.");
        }

        ClienteModel clienteModel = new ClienteModel(clienteDTO.nome(), clienteDTO.email(), clienteDTO.telefone(), clienteDTO.cep(), clienteDTO.cpf());
        clienteRepository.save(clienteModel);

        if (userRepository.findByEmail(clienteDTO.email()) == null) {
            userService.createUserForClient(clienteDTO.email(), clienteDTO.cpf());
        }

        return ResponseEntity.status(HttpStatus.CREATED).body(clienteModel);
    }

    public List<ClienteDTO> getAllClientes() {
        return clienteRepository.findAll().stream()
                .map(this::convertToClienteDTO)
                .collect(Collectors.toList());
    }

    public Optional<ClienteModel> getClienteById(Long id) {
        return clienteRepository.findById(id);
    }

    public ClienteUpdateDTO getClienteWithRoleById(Long id){
        ClienteModel cliente = clienteRepository.findById(id).orElse(null);
        if (cliente != null) {
            UserModel user = userRepository.findModelByEmail(cliente.getEmail());
            return new ClienteUpdateDTO(
                    cliente.getId(),
                    cliente.getNome(),
                    cliente.getEmail(),
                    cliente.getTelefone(),
                    cliente.getCep(),
                    cliente.getCpf(),
                    user.getUserRole()
            );
        }
        return null;
    }

    public ClienteModel getClienteByEmail(String email) {
        return clienteRepository.findByEmail(email);
    }

    public ResponseEntity<?> updateClienteWithId(Long id, ClienteUpdateDTO clienteDTO) {
        Optional<ClienteModel> optionalCliente = clienteRepository.findById(id);
        if (optionalCliente.isPresent()) {
            ClienteModel clienteModel = optionalCliente.get();
            clienteModel.setNome(clienteDTO.nome());
            clienteModel.setEmail(clienteDTO.email());
            clienteModel.setTelefone(clienteDTO.telefone());
            clienteModel.setCep(clienteDTO.cep());
            clienteModel.setCpf(clienteDTO.cpf());
            clienteRepository.save(clienteModel);

            // Atualiza a role do usuário
            UserModel userModel = userRepository.findModelByEmail(clienteDTO.email());
            if (userModel != null) {
                userService.updateRole(userModel.getId(), clienteDTO.userRole());
            }

            return ResponseEntity.status(HttpStatus.OK).body(clienteModel);
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Cliente não encontrado.");
        }
    }

    public ResponseEntity<?> updateCliente(ClienteUpdateDTO clienteDTO, Long id) {
        try {
            Optional<ClienteModel> existingCliente = clienteRepository.findById(id);

            if(existingCliente.isPresent()){
                ClienteModel clienteModel = existingCliente.get();
                clienteModel.setId(existingCliente.get().getId());
                clienteModel.setNome(clienteDTO.nome());
                clienteModel.setEmail(clienteDTO.email());
                clienteModel.setTelefone(clienteDTO.telefone());
                clienteModel.setCep(clienteDTO.cep());
                clienteModel.setCpf(clienteDTO.cpf());
                clienteRepository.save(clienteModel);
                return ResponseEntity.status(HttpStatus.OK).body(clienteModel);
            } else {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Cliente não encontrado.");
            }
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Erro ao atualizar cliente.");
        }
    }


    public void deleteCliente(Long id) {
        clienteRepository.deleteById(id);
    }

    private ClienteDTO convertToClienteDTO(ClienteModel clienteModel) {
        return new ClienteDTO(
                clienteModel.getId(),
                clienteModel.getNome(),
                clienteModel.getEmail(),
                clienteModel.getTelefone(),
                clienteModel.getCep(),
                clienteModel.getCpf()
        );
    }
}
