package br.ufsm.csi.pi_petshop.user.dtos;

import br.ufsm.csi.pi_petshop.user.enums.UserRole;
import jakarta.validation.constraints.NotNull;

public record UserDTO(
        @NotNull Long id,
        @NotNull String email,
        @NotNull UserRole userRole
)
{}
