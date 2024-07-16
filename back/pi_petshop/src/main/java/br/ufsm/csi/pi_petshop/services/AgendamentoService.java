package br.ufsm.csi.pi_petshop.services;

import br.ufsm.csi.pi_petshop.entity.agendamento.dtos.AgendamentoDTO;
import br.ufsm.csi.pi_petshop.entity.agendamento.dtos.AgendamentoResponseDTO;
import br.ufsm.csi.pi_petshop.entity.agendamento.models.AgendamentoModel;
import br.ufsm.csi.pi_petshop.entity.agendamento.repositories.AgendamentoRepository;
import br.ufsm.csi.pi_petshop.entity.cliente.dtos.ClienteAgendamentoDTO;
import br.ufsm.csi.pi_petshop.entity.cliente.models.ClienteModel;
import br.ufsm.csi.pi_petshop.entity.cliente.repositories.ClienteRepository;
import br.ufsm.csi.pi_petshop.entity.funcionario.dtos.FuncionarioAgendamentoDTO;
import br.ufsm.csi.pi_petshop.entity.funcionario.models.FuncionarioModel;
import br.ufsm.csi.pi_petshop.entity.funcionario.repositories.FuncionarioRepository;
import br.ufsm.csi.pi_petshop.entity.pet.dtos.PetAgendamentoDTO;
import br.ufsm.csi.pi_petshop.entity.pet.models.PetModel;
import br.ufsm.csi.pi_petshop.entity.pet.repositories.PetRepository;
import br.ufsm.csi.pi_petshop.entity.servico.dtos.ServicoDTO;
import br.ufsm.csi.pi_petshop.entity.servico.models.ServicoModel;
import br.ufsm.csi.pi_petshop.entity.servico.repositories.ServicoRepository;
import br.ufsm.csi.pi_petshop.entity.user.models.UserModel;
import br.ufsm.csi.pi_petshop.entity.user.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class AgendamentoService {

    @Autowired
    private AgendamentoRepository agendamentoRepository;
    @Autowired
    private ClienteRepository clienteRepository;
    @Autowired
    private PetRepository petRepository;
    @Autowired
    private FuncionarioRepository funcionarioRepository;
    @Autowired
    private ServicoRepository servicoRepository;
    @Autowired
    private UserRepository userRepository;

    public ResponseEntity<?> saveAgendamento(AgendamentoDTO agendamentoDTO) {
        ClienteModel cm = clienteRepository.findById(agendamentoDTO.cliente().id()).orElse(null);
        if (cm == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Cliente não encontrado.");
        }

        PetModel pm = petRepository.findById(agendamentoDTO.pet().id()).orElse(null);
        if (pm == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Pet não encontrado.");
        }

        FuncionarioModel fm = funcionarioRepository.findById(agendamentoDTO.funcionario().id()).orElse(null);
        if (fm == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Funcionário não encontrado.");
        }

        List<ServicoModel> servicos = agendamentoDTO.servicos().stream()
                .map(servicoDTO -> servicoRepository.findById(servicoDTO.id()).orElse(null))
                .collect(Collectors.toList());

        if (servicos.contains(null)) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Um ou mais serviços não encontrados.");
        }

        AgendamentoModel agendamento = new AgendamentoModel(
                cm,
                pm,
                fm,
                agendamentoDTO.data(),
                agendamentoDTO.valor(),
                agendamentoDTO.status(),
                servicos
        );

        agendamentoRepository.save(agendamento);
        return ResponseEntity.status(HttpStatus.CREATED).body(agendamentoDTO);
    }

    public List<AgendamentoDTO> getAllAgendamentos() {
        return agendamentoRepository.findAll().stream()
                .map(this::convertToAgendamentoDTO)
                .collect(Collectors.toList());
    }

    public ResponseEntity<?> getAgendamentoById(Long id) {
        Optional<AgendamentoModel> agendamentoOptional = agendamentoRepository.findById(id);
        if (agendamentoOptional.isPresent()) {
            return ResponseEntity.status(HttpStatus.OK).body(convertToAgendamentoDTO(agendamentoOptional.get()));
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Agendamento não encontrado.");
        }
    }

    public ResponseEntity<?> updateAgendamento(Long id, AgendamentoDTO agendamentoDTO) {
        Optional<AgendamentoModel> agendamentoOptional = agendamentoRepository.findById(id);
        if (agendamentoOptional.isPresent()) {
            AgendamentoModel agendamento = agendamentoOptional.get();
            agendamento.setCliente(clienteRepository.findById(agendamentoDTO.cliente().id()).orElse(null));
            agendamento.setPet(petRepository.findById(agendamentoDTO.pet().id()).orElse(null));
            agendamento.setFuncionario(funcionarioRepository.findById(agendamentoDTO.funcionario().id()).orElse(null));
            agendamento.setData(agendamentoDTO.data());
            agendamento.setValor(agendamentoDTO.valor());
            agendamento.setStatus(agendamentoDTO.status());

            List<ServicoModel> servicos = agendamentoDTO.servicos().stream()
                    .map(servicoDTO -> servicoRepository.findById(servicoDTO.id()).orElse(null))
                    .collect(Collectors.toList());

            if (servicos.contains(null)) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Um ou mais serviços não encontrados.");
            }

            agendamento.setServicos(servicos);
            agendamentoRepository.save(agendamento);
            return ResponseEntity.status(HttpStatus.OK).body(agendamentoDTO);
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Agendamento não encontrado.");
        }
    }

    public ResponseEntity<?> deleteAgendamento(Long id) {
        if (agendamentoRepository.existsById(id)) {
            agendamentoRepository.deleteById(id);
            return ResponseEntity.status(HttpStatus.OK).body("Agendamento deletado com sucesso.");
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Agendamento não encontrado.");
        }
    }

    private AgendamentoDTO convertToAgendamentoDTO(AgendamentoModel agendamento) {
        List<ServicoDTO> servicos = agendamento.getServicos().stream()
                .map(servico -> new ServicoDTO(servico.getId(), servico.getDescricao(), servico.getValor()))
                .collect(Collectors.toList());

        return new AgendamentoDTO(
                agendamento.getId(),
                new ClienteAgendamentoDTO(agendamento.getCliente().getId(), agendamento.getCliente().getNome()),
                new PetAgendamentoDTO(
                        agendamento.getPet().getId(),
                        agendamento.getPet().getNome(),
                        agendamento.getPet().getIdade(),
                        agendamento.getPet().getRaca(),
                        agendamento.getPet().getTipo(),
                        agendamento.getPet().getGenero(),
                        agendamento.getPet().getPeso(),
                        agendamento.getPet().getAlergias(),
                        agendamento.getPet().getEmTratamento(),
                        agendamento.getCliente().getId()
                ),
                new FuncionarioAgendamentoDTO(
                        agendamento.getFuncionario().getId(),
                        agendamento.getFuncionario().getNome()),
                agendamento.getData(),
                agendamento.getValor(),
                agendamento.getStatus(),
                servicos
        );
    }

    public List<AgendamentoResponseDTO> getAllAgendamentosByClientId(Long clientId) {
        return agendamentoRepository.findAllByClienteId(clientId).stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    public Long getUserIdByEmail(String email) {
        ClienteModel cliente = clienteRepository.findByEmail(email);
        return cliente != null ? cliente.getId() : null;
    }

    private AgendamentoResponseDTO convertToDTO(AgendamentoModel agendamento) {
        PetModel pet = petRepository.findById(agendamento.getPet().getId()).orElse(null);
        return new AgendamentoResponseDTO(
                agendamento.getId(),
                new ClienteAgendamentoDTO(agendamento.getCliente().getId(), agendamento.getCliente().getNome()),
                new PetAgendamentoDTO(pet.getId(), pet.getNome(), pet.getIdade(), pet.getRaca(), pet.getTipo(), pet.getGenero(), pet.getPeso(), pet.getAlergias(), pet.getEmTratamento(), pet.getCliente().getId()),
                new FuncionarioAgendamentoDTO(agendamento.getFuncionario().getId(), agendamento.getFuncionario().getNome()),
                agendamento.getData(),
                agendamento.getValor(),
                agendamento.getStatus(),
                agendamento.getServicos().stream()
                        .map(servico -> new ServicoDTO(servico.getId(), servico.getDescricao(), servico.getValor()))
                        .collect(Collectors.toList())
        );
    }

}
