package br.ufsm.csi.pi_petshop.entity.user.repositories;

import br.ufsm.csi.pi_petshop.entity.user.models.UserModel;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.security.core.userdetails.UserDetails;

public interface UserRepository extends JpaRepository<UserModel, Long> {
    UserDetails findByEmail(String email);
    UserModel findModelByEmail(String email);
}
