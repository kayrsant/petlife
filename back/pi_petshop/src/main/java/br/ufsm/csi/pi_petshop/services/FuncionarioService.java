package br.ufsm.csi.pi_petshop.services;

import br.ufsm.csi.pi_petshop.entity.cliente.dtos.ClienteDTO;
import br.ufsm.csi.pi_petshop.entity.cliente.models.ClienteModel;
import br.ufsm.csi.pi_petshop.entity.funcionario.dtos.FuncionarioDTO;
import br.ufsm.csi.pi_petshop.entity.funcionario.models.FuncionarioModel;
import br.ufsm.csi.pi_petshop.entity.funcionario.repositories.FuncionarioRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class FuncionarioService {

    @Autowired
    FuncionarioRepository funcionarioRepository;
    @Autowired
    private UserService userService;
    @Autowired
    private ClienteService clienteService;


    public ResponseEntity<?> saveFuncionario(FuncionarioDTO funcionarioDTO) {
        FuncionarioModel funcionarioModel = new FuncionarioModel(
                funcionarioDTO.cargo(),
                funcionarioDTO.nome(),
                funcionarioDTO.email(),
                funcionarioDTO.telefone()
        );
        funcionarioRepository.save(funcionarioModel);

        ClienteModel clienteModel = new ClienteModel(funcionarioModel.getNome(), funcionarioModel.getEmail(), funcionarioModel.getTelefone(), null, null);

        ClienteDTO clienteDTO = new ClienteDTO(clienteModel.getId(), clienteModel.getNome(), clienteModel.getEmail(), clienteModel.getTelefone(), "12345678", "12345678901");
        userService.createUserForFuncionario(funcionarioModel.getEmail(), funcionarioModel.getNome(), funcionarioModel.getCargo());
        clienteService.saveCliente(clienteDTO);
        return ResponseEntity.status(HttpStatus.CREATED).body(funcionarioModel);
    }



    public List<FuncionarioDTO> getAllFuncionarios() {
        return funcionarioRepository.findAll().stream()
                .map(this::convertToFuncionarioDTO)
                .collect(Collectors.toList());
    }

    public Optional<FuncionarioModel> getFuncionarioById(Long id) {
        return funcionarioRepository.findById(id);
    }

    public ResponseEntity<?> getFuncionarioByToken() {
        Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        try {
            if (principal instanceof UserDetails) {
                String email = ((UserDetails) principal).getUsername();
                FuncionarioModel funcionarioModel = funcionarioRepository.findByEmail(email);
                if (funcionarioModel != null) {
                    FuncionarioDTO funcionarioDTO = new FuncionarioDTO(funcionarioModel.getId(), funcionarioModel.getCargo(), funcionarioModel.getNome(), funcionarioModel.getEmail(), funcionarioModel.getTelefone());
                    return ResponseEntity.status(HttpStatus.OK).body(funcionarioDTO);
                } else {
                    return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Funcionário não encontrado.");
                }
            }
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Funcionário não encontrado.");
        }
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Funcionário não encontrado.");

    }

    public ResponseEntity<?> updateFuncionario(Long id, FuncionarioDTO funcionarioDTO) {
        Optional<FuncionarioModel> optionalFuncionario = funcionarioRepository.findById(id);
        if (optionalFuncionario.isPresent()) {
            FuncionarioModel funcionarioModel = optionalFuncionario.get();
            funcionarioModel.setCargo(funcionarioDTO.cargo());
            funcionarioModel.setNome(funcionarioDTO.nome());
            funcionarioModel.setEmail(funcionarioDTO.email());
            funcionarioModel.setTelefone(funcionarioDTO.telefone());
            funcionarioRepository.save(funcionarioModel);
            return ResponseEntity.status(HttpStatus.OK).body(funcionarioModel);
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Funcionário não encontrado.");
        }
    }

    public void deleteFuncionario(Long id) {
        funcionarioRepository.deleteById(id);
    }

    private FuncionarioDTO convertToFuncionarioDTO(FuncionarioModel funcionarioModel) {
        return new FuncionarioDTO(
                funcionarioModel.getId(),
                funcionarioModel.getCargo(),
                funcionarioModel.getNome(),
                funcionarioModel.getEmail(),
                funcionarioModel.getTelefone()
        );
    }
}
