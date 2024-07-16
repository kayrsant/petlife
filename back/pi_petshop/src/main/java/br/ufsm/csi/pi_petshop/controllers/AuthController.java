package br.ufsm.csi.pi_petshop.controllers;

import br.ufsm.csi.pi_petshop.entity.cliente.models.ClienteModel;
import br.ufsm.csi.pi_petshop.entity.cliente.repositories.ClienteRepository;
import br.ufsm.csi.pi_petshop.security.TokenService;
import br.ufsm.csi.pi_petshop.services.AuthorizationService;
import br.ufsm.csi.pi_petshop.services.UserService;
import br.ufsm.csi.pi_petshop.entity.user.dtos.AuthenticationDTO;
import br.ufsm.csi.pi_petshop.entity.user.dtos.LoginResponseDTO;
import br.ufsm.csi.pi_petshop.entity.user.dtos.RegisterDTO;
import br.ufsm.csi.pi_petshop.entity.user.dtos.UserDTO;
import br.ufsm.csi.pi_petshop.entity.user.enums.UserRole;
import br.ufsm.csi.pi_petshop.entity.user.models.UserModel;
import br.ufsm.csi.pi_petshop.entity.user.repositories.UserRepository;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.util.Date;

@RestController
@RequestMapping("auth")
@CrossOrigin(origins = "http://localhost:5173")
public class AuthController {

    @Autowired
    private AuthorizationService authorizationService;

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private TokenService tokenService;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private ClienteRepository clienteRepository;
    @Autowired
    private UserService userService;

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody @Valid AuthenticationDTO authetinticationDto){
        try {
            Authentication authentication = new UsernamePasswordAuthenticationToken(authetinticationDto.email(), authetinticationDto.senha());
            Authentication at = authenticationManager.authenticate(authentication);
            var token = tokenService.gerarToken((UserModel) at.getPrincipal());
            return ResponseEntity.status(HttpStatus.OK).body(new LoginResponseDTO(token));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @PostMapping("/register")
    public ResponseEntity<Object> register(@RequestBody RegisterDTO registerDto){
        try{
            String encryptedPassword = new BCryptPasswordEncoder().encode(registerDto.senha());
            UserModel userModel;

            if(registerDto.userRole() == null){
                UserRole userRole = UserRole.CLIENTE;
                userModel = new UserModel(registerDto.email(), encryptedPassword, userRole);
            } else{
                userModel = new UserModel(registerDto.email(), encryptedPassword, registerDto.userRole());
            }
            userModel.setCriadoEm(new Date(System.currentTimeMillis()));

            if(registerDto.telefone() == null){
                return ResponseEntity.status(HttpStatus.CREATED).body(new UserDTO(userModel.getId(), userModel.getEmail(), userModel.getUserRole()));
            } else{
                ClienteModel newCliente = new ClienteModel(
                        registerDto.nome(),
                        registerDto.email(),
                        registerDto.telefone(),
                        registerDto.cep(),
                        registerDto.cpf()
                );

                this.clienteRepository.save(newCliente);
                this.userRepository.save(userModel);

                return ResponseEntity.status(HttpStatus.CREATED).body(newCliente);
            }
        } catch (Exception e){
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        }

    }

    @GetMapping("/check-email")
    public ResponseEntity<Boolean> checkEmail(@RequestParam String email) {
        try {
            UserDetails user = authorizationService.loadUserByUsername(email);
            return ResponseEntity.ok(true);
        } catch (UsernameNotFoundException e) {
            return ResponseEntity.ok(false);
        }
    }
}