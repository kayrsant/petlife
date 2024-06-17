package br.ufsm.csi.pi_petshop.services;

import br.ufsm.csi.pi_petshop.cliente.models.ClienteModel;
import br.ufsm.csi.pi_petshop.cliente.repositories.ClienteRepository;
import br.ufsm.csi.pi_petshop.user.dtos.UserDTO;
import br.ufsm.csi.pi_petshop.user.enums.UserRole;
import br.ufsm.csi.pi_petshop.user.models.UserModel;
import br.ufsm.csi.pi_petshop.user.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;

@Service
public class UserService {

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

    public void createUser(String email, String password, UserRole role) {
        String encryptedPassword = new BCryptPasswordEncoder().encode(password);

        UserModel newUser = new UserModel(email, encryptedPassword, role);
        newUser.setCriadoEm(new Date(System.currentTimeMillis()));
        this.userRepository.save(newUser);

        if (role == UserRole.CLIENTE) {
            ClienteModel clienteModel = new ClienteModel(null, email, null, null, null);
            clienteRepository.save(clienteModel);
        }
    }

    public void createAdminUser(String email, String password) {
        createUser(email, password, UserRole.ADMIN);
    }

    public void createEmployeeUser(String email, String password) {
        createUser(email, password, UserRole.FUNCIONARIO);
    }

    public List<UserModel> getAllUsers() {
        return userRepository.findAll();
    }

    private UserDTO convertToUserDTO(UserModel userDTO) {
        return new UserDTO(
                userDTO.getId(),
                userDTO.getEmail(),
                userDTO.getUserRole()
        );
    }
}
