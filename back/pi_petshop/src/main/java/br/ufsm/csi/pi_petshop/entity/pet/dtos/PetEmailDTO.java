package br.ufsm.csi.pi_petshop.entity.pet.dtos;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record PetEmailDTO(
        @NotBlank String nome,
        @NotNull Integer idade,
        @NotBlank String raca,
        @NotBlank String tipo,
        String peso,
        @NotBlank String genero,
        String alergias,
        String emTratamento,
        @NotNull String clienteEmail
) {}
