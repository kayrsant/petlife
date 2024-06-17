package br.ufsm.csi.pi_petshop.controllers;

import br.ufsm.csi.pi_petshop.funcionario.dtos.FuncionarioDTO;
import br.ufsm.csi.pi_petshop.funcionario.models.FuncionarioModel;
import br.ufsm.csi.pi_petshop.services.FuncionarioService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/funcionario")
@CrossOrigin(origins = "http://localhost:5173")
public class FuncionarioController {

    @Autowired
    FuncionarioService funcionarioService;

    @PostMapping("/create")
    public ResponseEntity<?> create(@RequestBody FuncionarioDTO funcionarioDTO) {
        return funcionarioService.saveFuncionario(funcionarioDTO);
    }

    @GetMapping
    public List<FuncionarioDTO> getAll() {
        return funcionarioService.getAllFuncionarios();
    }

    @GetMapping("/{id}")
    public Optional<FuncionarioModel> getById(@PathVariable Long id) {
        return funcionarioService.getFuncionarioById(id);
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> updateById(@PathVariable Long id, @RequestBody FuncionarioDTO funcionarioDTO) {
        return funcionarioService.updateFuncionario(id, funcionarioDTO);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteById(@PathVariable Long id) {
        funcionarioService.deleteFuncionario(id);
        return ResponseEntity.ok().build();
    }
}
