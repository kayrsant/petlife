package br.ufsm.csi.pi_petshop.entity.servico.repositories;

import br.ufsm.csi.pi_petshop.entity.servico.models.ServicoModel;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ServicoRepository extends JpaRepository<ServicoModel, Long> {
}
