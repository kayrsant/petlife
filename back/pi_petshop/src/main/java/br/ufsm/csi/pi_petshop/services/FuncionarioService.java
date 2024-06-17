package br.ufsm.csi.pi_petshop.services;

import br.ufsm.csi.pi_petshop.funcionario.dtos.FuncionarioDTO;
import br.ufsm.csi.pi_petshop.funcionario.models.FuncionarioModel;
import br.ufsm.csi.pi_petshop.funcionario.repositories.FuncionarioRepository;
import br.ufsm.csi.pi_petshop.user.enums.UserRole;
import br.ufsm.csi.pi_petshop.user.models.UserModel;
import br.ufsm.csi.pi_petshop.user.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class FuncionarioService {

    @Autowired
    FuncionarioRepository funcionarioRepository;
    @Autowired
    private UserService userService;


    public ResponseEntity<?> saveFuncionario(FuncionarioDTO funcionarioDTO) {
        FuncionarioModel funcionarioModel = new FuncionarioModel(
                funcionarioDTO.cargo(),
                funcionarioDTO.nome(),
                funcionarioDTO.email(),
                funcionarioDTO.telefone()
        );
        funcionarioRepository.save(funcionarioModel);

        userService.createUserForFuncionario(funcionarioModel.getEmail(), funcionarioModel.getNome(), funcionarioModel.getCargo());
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
