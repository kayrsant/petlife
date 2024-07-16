package br.ufsm.csi.pi_petshop.entity.agendamento.dtos;

import br.ufsm.csi.pi_petshop.entity.agendamento.enums.StatusAgendamento;
import br.ufsm.csi.pi_petshop.entity.cliente.dtos.ClienteAgendamentoDTO;
import br.ufsm.csi.pi_petshop.entity.funcionario.dtos.FuncionarioAgendamentoDTO;
import br.ufsm.csi.pi_petshop.entity.pet.dtos.PetAgendamentoDTO;
import br.ufsm.csi.pi_petshop.entity.servico.dtos.ServicoDTO;
import jakarta.validation.constraints.NotNull;

import java.sql.Timestamp;
import java.util.List;

public record AgendamentoResponseDTO (
        @NotNull Long id,
        @NotNull ClienteAgendamentoDTO cliente,
        @NotNull PetAgendamentoDTO pet,
        @NotNull FuncionarioAgendamentoDTO funcionario,
        @NotNull Timestamp data,
        @NotNull Double valor,
        @NotNull StatusAgendamento status,
        @NotNull List<ServicoDTO> servicos
){
}
