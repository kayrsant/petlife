package br.ufsm.csi.pi_petshop.services;

import br.ufsm.csi.pi_petshop.entity.servico.dtos.ServicoDTO;
import br.ufsm.csi.pi_petshop.entity.servico.models.ServicoModel;
import br.ufsm.csi.pi_petshop.entity.servico.repositories.ServicoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class ServicoService {

    @Autowired
    private ServicoRepository servicoRepository;

    public ResponseEntity<?> saveServico(ServicoDTO servicoDTO) {
        ServicoModel servico = new ServicoModel();
        servico.setDescricao(servicoDTO.descricao());
        servico.setValor(servicoDTO.valor());

        servicoRepository.save(servico);
        return ResponseEntity.status(HttpStatus.CREATED).body(servicoDTO);
    }

    public List<ServicoDTO> getAll() {
        return servicoRepository.findAll().stream()
                .map(this::convertToServicoDTO)
                .collect(Collectors.toList());
    }

    public ResponseEntity<?> getServicoById(Long id) {
        Optional<ServicoModel> servicoOptional = servicoRepository.findById(id);
        if (servicoOptional.isPresent()) {
            return ResponseEntity.status(HttpStatus.OK).body(convertToServicoDTO(servicoOptional.get()));
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Serviço não encontrado.");
        }
    }

    public ResponseEntity<?> updateServico(Long id, ServicoDTO servicoDTO) {
        Optional<ServicoModel> servicoOptional = servicoRepository.findById(id);
        if (servicoOptional.isPresent()) {
            ServicoModel servico = servicoOptional.get();
            servico.setDescricao(servicoDTO.descricao());
            servico.setValor(servicoDTO.valor());
            servicoRepository.save(servico);
            return ResponseEntity.status(HttpStatus.OK).body(servicoDTO);
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Serviço não encontrado.");
        }
    }

    public ResponseEntity<?> deleteServico(Long id) {
        if (servicoRepository.existsById(id)) {
            servicoRepository.deleteById(id);
            return ResponseEntity.status(HttpStatus.OK).body("Serviço deletado com sucesso.");
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Serviço não encontrado.");
        }
    }

    private ServicoDTO convertToServicoDTO(ServicoModel servico) {
        return new ServicoDTO(servico.getId(), servico.getDescricao(), servico.getValor());
    }
}
