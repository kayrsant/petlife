package br.ufsm.csi.pi_petshop.cliente.dtos;

import java.util.Date;

public record ClienteUpdateDTO(Long id, String nome, String email, String telefone, String cep, String cpf, Date data) {
}
