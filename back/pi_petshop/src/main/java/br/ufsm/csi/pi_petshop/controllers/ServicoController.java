package br.ufsm.csi.pi_petshop.controllers;

import br.ufsm.csi.pi_petshop.entity.servico.dtos.ServicoDTO;
import br.ufsm.csi.pi_petshop.services.ServicoService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/servico")
@CrossOrigin(origins = "http://localhost:5173")
public class ServicoController {

    @Autowired
    private ServicoService servicoService;

    @PostMapping("/create")
    public ResponseEntity<?> create(@RequestBody @Valid ServicoDTO servicoDTO) {
        return servicoService.saveServico(servicoDTO);
    }

    @GetMapping
    public List<ServicoDTO> getAll() {
        return servicoService.getAll();
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> getById(@PathVariable Long id) {
        return servicoService.getServicoById(id);
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> updateById(@PathVariable Long id, @RequestBody @Valid ServicoDTO servicoDTO) {
        return servicoService.updateServico(id, servicoDTO);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteById(@PathVariable Long id) {
        return servicoService.deleteServico(id);
    }
}
