package br.ufsm.csi.pi_petshop.entity.funcionario.repositories;

import br.ufsm.csi.pi_petshop.entity.funcionario.models.FuncionarioModel;
import org.springframework.data.jpa.repository.JpaRepository;

public interface FuncionarioRepository extends JpaRepository<FuncionarioModel, Long> {
    FuncionarioModel findByEmail(String email);
}
