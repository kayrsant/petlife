package br.ufsm.csi.pi_petshop.services;

import br.ufsm.csi.pi_petshop.controllers.ClienteController;
import br.ufsm.csi.pi_petshop.entity.cliente.models.ClienteModel;
import br.ufsm.csi.pi_petshop.entity.cliente.repositories.ClienteRepository;
import br.ufsm.csi.pi_petshop.entity.user.dtos.UserDTO;
import br.ufsm.csi.pi_petshop.entity.user.enums.UserRole;
import br.ufsm.csi.pi_petshop.entity.user.models.UserModel;
import br.ufsm.csi.pi_petshop.entity.user.repositories.UserRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class UserService {

    private static final Logger logger = LoggerFactory.getLogger(ClienteController.class);

    @Autowired
    UserRepository userRepository;

    @Autowired
    ClienteRepository clienteRepository;

    public void createUserForClient(String email, String cpf) {
        String encryptedPassword = new BCryptPasswordEncoder().encode(cpf);

        UserModel newUser = new UserModel(email, encryptedPassword, UserRole.CLIENTE);
        newUser.setCriadoEm(new Date(System.currentTimeMillis()));
        this.userRepository.save(newUser);
    }

    public void createUserForFuncionario(String email, String nome, String cargo) {
        String encryptedPassword = new BCryptPasswordEncoder().encode(nome);

        UserModel newUser = new UserModel(email, encryptedPassword, UserRole.FUNCIONARIO);
        newUser.setCriadoEm(new Date(System.currentTimeMillis()));
        this.userRepository.save(newUser);
    }

    public List<UserModel> getAllUsers() {
        return userRepository.findAll();
    }

    public void updateRole(Long id, UserRole newRole) {
        UserModel user = userRepository.findById(id).orElse(null);
        if (user != null) {
            if (newRole != null) {
                user.setUserRole(newRole);  // Atualiza o campo de role
                userRepository.save(user);
                logger.info("Role atualizada com sucesso: " + user.getUsername() + " para " + newRole);
            } else {
                throw new IllegalArgumentException("Role não pode ser nulo");
            }
        } else {
            throw new NoSuchElementException("Usuário não encontrado");
        }
    }



    public UserDetails getUserByEmail(String email) {
        return userRepository.findByEmail(email);
    }

    public UserModel getUserModelByEmail(String email){
        return userRepository.findModelByEmail(email);
    }



}
