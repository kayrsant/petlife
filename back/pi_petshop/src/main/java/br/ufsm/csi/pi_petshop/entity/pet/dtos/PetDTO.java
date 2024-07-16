package br.ufsm.csi.pi_petshop.entity.pet.dtos;

import br.ufsm.csi.pi_petshop.entity.cliente.dtos.ClienteDTO;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record PetDTO(
        @NotBlank String nome,
        @NotNull Integer idade,
        @NotBlank String raca,
        @NotBlank String tipo,
        String peso,
        @NotBlank String genero,
        String alergias,
        String emTratamento,
        @NotNull ClienteDTO cliente
) {}

