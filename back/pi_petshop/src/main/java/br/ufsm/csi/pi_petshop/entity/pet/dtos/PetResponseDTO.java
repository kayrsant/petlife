package br.ufsm.csi.pi_petshop.entity.pet.dtos;

import br.ufsm.csi.pi_petshop.entity.cliente.models.ClienteModel;
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
        @NotNull ClienteModel cliente
){

}
