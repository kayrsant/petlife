package br.ufsm.csi.pi_petshop.cliente.repositories;

import br.ufsm.csi.pi_petshop.cliente.models.ClienteModel;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ClienteRepository extends JpaRepository<ClienteModel, Long> {
    ClienteModel findByEmail(String email);
}
