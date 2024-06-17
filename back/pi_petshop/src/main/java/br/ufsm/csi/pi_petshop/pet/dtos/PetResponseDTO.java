package br.ufsm.csi.pi_petshop.pet.dtos;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record PetResponseDTO(
        @NotNull Long id,
        @NotBlank String nome,
        @NotNull Integer idade,
        @NotBlank String raca,
        @NotBlank String tipo,
        String peso,
        @NotBlank String genero,
        String alergias,
        String emTratamento,
        br.ufsm.csi.pi_petshop.cliente.models.@NotNull ClienteModel cliente
){

}
