package br.ufsm.csi.pi_petshop.entity.cliente.dtos;

import br.ufsm.csi.pi_petshop.entity.user.enums.UserRole;

import java.util.Date;

public record ClienteUpdateDTO(Long id, String nome, String email, String telefone, String cep, String cpf, UserRole userRole) {
}
