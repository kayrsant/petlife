package br.ufsm.csi.pi_petshop.user.dtos;

import br.ufsm.csi.pi_petshop.user.enums.UserRole;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record RegisterDTO(
        @NotBlank String nome,
        @NotBlank @Email String email,
        @NotBlank String senha,
        @NotBlank String telefone,
        @NotBlank String cep,
        @NotBlank String cpf,
        @NotNull(message = "User role is mandatory") UserRole userRole
) {}
