package br.ufsm.csi.pi_petshop.services;

import br.ufsm.csi.pi_petshop.cliente.dtos.ClienteDTO;
import br.ufsm.csi.pi_petshop.cliente.dtos.ClienteUpdateDTO;
import br.ufsm.csi.pi_petshop.cliente.models.ClienteModel;
import br.ufsm.csi.pi_petshop.cliente.repositories.ClienteRepository;
import br.ufsm.csi.pi_petshop.controllers.ClienteController;
import br.ufsm.csi.pi_petshop.user.repositories.UserRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

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

    public ClienteModel getClienteByEmail(String email) {
        return clienteRepository.findByEmail(email);
    }

    public ResponseEntity<?> updateClienteWithId(Long id, ClienteDTO clienteDTO) {
        Optional<ClienteModel> optionalCliente = clienteRepository.findById(id);
        if (optionalCliente.isPresent()) {
            ClienteModel clienteModel = optionalCliente.get();
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
    }

    public ResponseEntity<?> updateCliente(ClienteUpdateDTO clienteDTO) {
        ClienteModel clienteModel = clienteRepository.findByEmail(clienteDTO.email());
        if (clienteModel.getId() != null) {
            try{
                logger.info("Cliente encontrado: " + clienteModel.getNome());;
                clienteModel.setNome(clienteDTO.nome());
                clienteModel.setCep(clienteDTO.cep());
                clienteModel.setCpf(clienteDTO.cpf());
                clienteModel.setEmail(clienteDTO.email());
                clienteModel.setTelefone(clienteDTO.telefone());
                clienteRepository.save(clienteModel);
                return ResponseEntity.status(HttpStatus.OK).body(clienteModel);
            } catch (Exception e){
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Erro ao atualizar cliente.");
            }
        } else {
            logger.info("Cliente não encontrado.");
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Cliente não encontrado.");
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
