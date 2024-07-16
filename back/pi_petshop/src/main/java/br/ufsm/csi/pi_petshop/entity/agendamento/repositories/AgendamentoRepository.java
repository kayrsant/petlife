package br.ufsm.csi.pi_petshop.entity.agendamento.repositories;

import br.ufsm.csi.pi_petshop.entity.agendamento.models.AgendamentoModel;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface AgendamentoRepository extends JpaRepository<AgendamentoModel, Long> {
    List<AgendamentoModel> findAllByClienteId(Long clienteId);
}
