package br.ufsm.csi.pi_petshop.entity.pet.dtos;

public record PetAgendamentoDTO(
        Long id,
        String nome,
        Integer idade,
        String raca,
        String tipo,
        String genero,
        String peso,
        String alergias,
        String emTratamento,
        Long clienteId
) {
}
