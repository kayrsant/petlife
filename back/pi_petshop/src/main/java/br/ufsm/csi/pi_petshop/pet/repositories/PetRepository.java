package br.ufsm.csi.pi_petshop.pet.repositories;

import br.ufsm.csi.pi_petshop.pet.models.PetModel;
import org.springframework.data.jpa.repository.JpaRepository;


public interface PetRepository extends JpaRepository<PetModel, Long> {

}
