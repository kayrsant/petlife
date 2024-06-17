package br.ufsm.csi.pi_petshop.cliente.dtos;

public record ClienteDTO(Long id, String nome, String email, String telefone, String cep, String cpf) {
}
