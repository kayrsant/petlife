package br.ufsm.csi.pi_petshop.services;

import br.ufsm.csi.pi_petshop.cliente.models.ClienteModel;
import br.ufsm.csi.pi_petshop.cliente.repositories.ClienteRepository;
import br.ufsm.csi.pi_petshop.pet.dtos.PetDTO;
import br.ufsm.csi.pi_petshop.pet.dtos.PetEmailDTO;
import br.ufsm.csi.pi_petshop.pet.dtos.PetResponseDTO;
import br.ufsm.csi.pi_petshop.pet.models.PetModel;
import br.ufsm.csi.pi_petshop.pet.repositories.PetRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class PetService {

    @Autowired
    PetRepository petRepository;
    @Autowired
    private ClienteService clienteService;
    @Autowired
    private ClienteRepository clienteRepository;

    public ResponseEntity<?> savePet(PetEmailDTO petDTO) {
        String clienteEmail = petDTO.clienteEmail();

        if (clienteEmail == null) {
            System.out.println("CLIENTE E-mail NULL");
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Cliente não encontrado.");
        }

        Optional<ClienteModel> clienteOpt = Optional.ofNullable(clienteRepository.findByEmail(clienteEmail));
        if (!clienteOpt.isPresent()) {
            System.out.println("CLIENTE NÃO ENCONTRADO.");
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Cliente não encontrado.");
        }
        ClienteModel cliente = clienteOpt.get();

        PetModel petModel = new PetModel(
                petDTO.nome(),
                petDTO.idade(),
                petDTO.raca(),
                petDTO.tipo(),
                petDTO.peso(),
                petDTO.genero(),
                petDTO.alergias(),
                petDTO.emTratamento(),
                cliente
        );

        petRepository.save(petModel);

        return ResponseEntity.status(HttpStatus.CREATED).body(petModel);
    }

    public Long getUserIdByEmail(String email) {
        ClienteModel cliente = clienteService.getClienteByEmail(email);
        if (cliente != null) {
            return cliente.getId();
        } else {
            throw new IllegalArgumentException("Cliente não encontrado para o e-mail fornecido");
        }
    }

    public List<PetResponseDTO> getAllPets() {
        return petRepository.findAll().stream()
                .map(this::convertToPetResponseDTO)
                .collect(Collectors.toList());
    }

    public List<PetResponseDTO> getAllPetsByClientId(Long id) {
        Optional<ClienteModel> optionalClienteModel = clienteRepository.findById(id);

        if (optionalClienteModel.isPresent()) {
            Long clientId = optionalClienteModel.get().getId();
            System.out.println(petRepository.findAll().stream()
                    .filter(pet -> pet.getCliente().getId().equals(clientId))
                    .map(this::convertToPetResponseDTO)
                    .collect(Collectors.toList()));
            return petRepository.findAll().stream()
                    .filter(pet -> pet.getCliente().getId().equals(clientId))
                    .map(this::convertToPetResponseDTO)
                    .collect(Collectors.toList());
        } else {
            throw new IllegalArgumentException("Cliente com ID " + id + " não encontrado.");
        }
    }

    public ResponseEntity<?> updatePet(Long id, PetDTO petDTO) {
        Optional<PetModel> optionalPet = petRepository.findById(id);
        if (optionalPet.isPresent()) {
            Optional<ClienteModel> clienteOpt = clienteRepository.findById(petDTO.cliente().id());
            if (!clienteOpt.isPresent()) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Cliente não encontrado.");
            }
            ClienteModel cliente = clienteOpt.get();
            PetModel petModel = optionalPet.get();
            petModel.setNome(petDTO.nome());
            petModel.setTipo(petDTO.tipo());
            petModel.setPeso(petDTO.peso());
            petModel.setGenero(petDTO.genero());
            petModel.setIdade(petDTO.idade());
            petModel.setCliente(cliente);
            petRepository.save(petModel);
            return ResponseEntity.status(HttpStatus.OK).body(petModel);
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Pet não encontrado.");
        }
    }

    public void deletePet(Long id) {
        petRepository.deleteById(id);
    }

    private PetResponseDTO convertToPetResponseDTO(PetModel petModel) {
        return new PetResponseDTO(
                petModel.getId(),
                petModel.getNome(),
                petModel.getIdade(),
                petModel.getRaca(),
                petModel.getTipo(),
                petModel.getPeso(),
                petModel.getGenero(),
                petModel.getAlergias(),
                petModel.getEmTratamento(),
                petModel.getCliente());
    }
}
