package br.ufsm.csi.pi_petshop.controllers;

import br.ufsm.csi.pi_petshop.entity.agendamento.dtos.AgendamentoDTO;
import br.ufsm.csi.pi_petshop.entity.agendamento.dtos.AgendamentoResponseDTO;
import br.ufsm.csi.pi_petshop.services.AgendamentoService;
import br.ufsm.csi.pi_petshop.security.TokenService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/agendamento")
@CrossOrigin(origins = "http://localhost:5173")
public class AgendamentoController {

    @Autowired
    private AgendamentoService agendamentoService;

    @Autowired
    private TokenService tokenService;

    @PostMapping("/create")
    public ResponseEntity<?> create(@RequestBody @Valid AgendamentoDTO agendamentoDTO) {
        return agendamentoService.saveAgendamento(agendamentoDTO);
    }

    @GetMapping
    public List<AgendamentoDTO> getAll() {
        return agendamentoService.getAllAgendamentos();
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> getById(@PathVariable Long id) {
        return agendamentoService.getAgendamentoById(id);
    }

    @GetMapping("/cliente")
    public ResponseEntity<?> getAllAgendamentosByClientId(HttpServletRequest request) {
        String token = extractToken(request);
        String username = tokenService.getSubject(token);

        Long idCliente = agendamentoService.getUserIdByEmail(username);

        if (idCliente != null) {
            List<AgendamentoResponseDTO> agendamentos = agendamentoService.getAllAgendamentosByClientId(idCliente);
            return ResponseEntity.status(HttpStatus.OK).body(agendamentos);
        } else {
            System.out.println("não encontrei nada");
            return ResponseEntity.notFound().build();
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> updateById(@PathVariable Long id, @RequestBody @Valid AgendamentoDTO agendamentoDTO) {
        return agendamentoService.updateAgendamento(id, agendamentoDTO);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteById(@PathVariable Long id) {
        return agendamentoService.deleteAgendamento(id);
    }

    private String extractToken(HttpServletRequest request) {
        String bearerToken = request.getHeader("Authorization");
        if (bearerToken != null && bearerToken.startsWith("Bearer ")) {
            return bearerToken.substring(7);
        }
        return null;
    }
}
