package br.ufsm.csi.pi_petshop.entity.pet.repositories;

import br.ufsm.csi.pi_petshop.entity.cliente.models.ClienteModel;
import br.ufsm.csi.pi_petshop.entity.pet.models.PetModel;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;


public interface PetRepository extends JpaRepository<PetModel, Long> {

    PetModel findByCliente(ClienteModel c);
    List<PetModel> findByClienteId(Long clienteId);

}
