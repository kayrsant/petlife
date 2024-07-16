package br.ufsm.csi.pi_petshop.entity.cliente.dtos;

import br.ufsm.csi.pi_petshop.entity.user.enums.UserRole;

public record ClienteDTO(Long id, String nome, String email, String telefone, String cep, String cpf) {
}
