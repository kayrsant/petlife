package br.ufsm.csi.pi_petshop.controllers;

import br.ufsm.csi.pi_petshop.services.UserService;
import br.ufsm.csi.pi_petshop.user.models.UserModel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("user")
public class UserController {

    @Autowired
    UserService userService;

    @GetMapping
    public ResponseEntity<List<UserModel>> getAll() {
        List<UserModel> users = userService.getAllUsers();
        if (users.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(users);
        }
        return ResponseEntity.ok(users);
    }

}
