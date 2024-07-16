package br.ufsm.csi.pi_petshop.controllers;

import br.ufsm.csi.pi_petshop.entity.user.dtos.UserDTO;
import br.ufsm.csi.pi_petshop.entity.user.dtos.UserProfileDTO;
import br.ufsm.csi.pi_petshop.entity.user.enums.UserRole;
import br.ufsm.csi.pi_petshop.entity.user.repositories.UserRepository;
import br.ufsm.csi.pi_petshop.security.TokenService;
import br.ufsm.csi.pi_petshop.services.UserService;
import br.ufsm.csi.pi_petshop.entity.user.models.UserModel;
import jakarta.servlet.http.HttpServletRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.Collections;
import java.util.List;
import java.util.NoSuchElementException;

@RestController
@RequestMapping("user")
public class UserController {

    private static final Logger logger = LoggerFactory.getLogger(ClienteController.class);

    @Autowired
    UserService userService;
    @Autowired
    private UserRepository userRepository;

    TokenService tokenService;

    @GetMapping
    public ResponseEntity<List<UserModel>> getAll() {
        List<UserModel> users = userService.getAllUsers();
        if (users.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(users);
        }
        return ResponseEntity.ok(users);
    }

    @GetMapping("/profile")
    public ResponseEntity<UserProfileDTO> getUserProfile() {
        Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();

        if (principal instanceof UserDetails) {
            String email = ((UserDetails) principal).getUsername();
            var user = userService.getUserByEmail(email);

            if (user != null) {
                UserProfileDTO userProfileDTO = new UserProfileDTO(user.getUsername(), null);
                return ResponseEntity.ok(userProfileDTO);
            } else {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
            }
        }
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(null);
    }

    @PostMapping("/role")
    public ResponseEntity<?> updateRole(@RequestParam Long id, @RequestParam UserRole role) {
        try {
            userService.updateRole(id, role);
            return ResponseEntity.ok().body("Role atualizada com sucesso!");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Erro ao atualizar a role do usuário.");
        }
    }

    @PutMapping("/user/{id}/role")
    public ResponseEntity<String> updateUserRole(
            @PathVariable Long id,
            @RequestBody UserRole newRole
    ) {
        try {
            userService.updateRole(id, newRole);
            return ResponseEntity.ok("Role atualizada com sucesso");
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        } catch (NoSuchElementException e) {
            return ResponseEntity.notFound().build();
        } catch (Exception e) {
            logger.error("Erro ao atualizar role", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Erro ao atualizar role");
        }
    }

    @PutMapping("/user/{id}/remove-role")
    public ResponseEntity<String> removeUserRole(@PathVariable Long id) {
        try {
            userService.updateRole(id, UserRole.CLIENTE);
            return ResponseEntity.ok("Permissão removida com sucesso");
        } catch (Exception e) {
            logger.error("Erro ao remover permissão", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Erro ao remover permissão");
        }
    }

    @GetMapping("/me")
    public ResponseEntity<UserDTO> getCurrentUser() {
        Principal principal = SecurityContextHolder.getContext().getAuthentication();

        String email = principal.getName();

        UserModel user = userRepository.findModelByEmail(email);
        if (user == null) {
            return ResponseEntity.notFound().build();
        }
        UserDTO userDTO = new UserDTO(user.getId(), user.getEmail(), user.getUserRole());
        return ResponseEntity.ok(userDTO);
    }


    @GetMapping("/user-role")
        public ResponseEntity<?> getUserRole(@RequestParam String email) {
            UserModel user = userRepository.findModelByEmail(email);
            if (user != null) {
                return ResponseEntity.ok(Collections.singletonMap("role", user.getUserRole()));
            } else {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body("User not found");
            }
        }

    private String extractToken(HttpServletRequest request) {
        String authHeader = request.getHeader("Authorization");
        if (authHeader != null && authHeader.startsWith("Bearer ")) {
            return authHeader.substring(7);
        }
        return null;
    }
    }

