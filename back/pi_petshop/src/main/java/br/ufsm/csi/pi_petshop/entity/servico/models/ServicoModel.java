package br.ufsm.csi.pi_petshop.entity.servico.models;

import br.ufsm.csi.pi_petshop.entity.agendamento.models.AgendamentoModel;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "servicos")
public class ServicoModel {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "descricao", nullable = false)
    private String descricao;

    @Column(name = "valor", nullable = false)
    private Double valor;

    @ManyToMany(mappedBy = "servicos")
    private List<AgendamentoModel> agendamentos;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getDescricao() {
        return descricao;
    }

    public void setDescricao(String descricao) {
        this.descricao = descricao;
    }

    public Double getValor() {
        return valor;
    }

    public void setValor(Double valor) {
        this.valor = valor;
    }

    public List<AgendamentoModel> getAgendamentos() {
        return agendamentos;
    }

    public void setAgendamentos(List<AgendamentoModel> agendamentos) {
        this.agendamentos = agendamentos;
    }
}
