package br.ufsm.csi.pi_petshop.entity.agendamento.models;

import br.ufsm.csi.pi_petshop.entity.agendamento.enums.StatusAgendamento;
import br.ufsm.csi.pi_petshop.entity.cliente.models.ClienteModel;
import br.ufsm.csi.pi_petshop.entity.funcionario.models.FuncionarioModel;
import br.ufsm.csi.pi_petshop.entity.pet.models.PetModel;
import br.ufsm.csi.pi_petshop.entity.servico.models.ServicoModel;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.sql.Timestamp;
import java.util.List;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "agendamentos")
public class AgendamentoModel {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @OneToOne
    @JoinColumn(name = "id_cliente", nullable = false)
    private ClienteModel cliente;
    @OneToOne
    @JoinColumn(name = "id_pet", nullable = false)
    private PetModel pet;
    @OneToOne
    @JoinColumn(name = "id_funcionario", nullable = false)
    private FuncionarioModel funcionario;
    @Temporal(TemporalType.TIMESTAMP)
    @Column(name="data", nullable = false)
    private Timestamp data;
    @Column(name = "valor", nullable = false)
    private Double valor;
    @Column(name = "status", nullable = false)
    private StatusAgendamento status;
    @ManyToMany
    @JoinTable(
            name = "agendamentos_servicos",
            joinColumns = @JoinColumn(name = "id_agendamento"),
            inverseJoinColumns = @JoinColumn(name = "id_servico")
    )
    private List<ServicoModel> servicos;

    public AgendamentoModel(ClienteModel cliente, PetModel pet, FuncionarioModel funcionario, Timestamp data, Double valor, StatusAgendamento status) {
        this.cliente = cliente;
        this.pet = pet;
        this.funcionario = funcionario;
        this.data = data;
        this.valor = valor;
        this.status = status;
    }

    public AgendamentoModel(ClienteModel cliente, PetModel pet, FuncionarioModel funcionario, Timestamp data, Double valor, StatusAgendamento status, List<ServicoModel> servicos) {
        this.cliente = cliente;
        this.pet = pet;
        this.funcionario = funcionario;
        this.data = data;
        this.valor = valor;
        this.status = status;
        this.servicos = servicos;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public ClienteModel getCliente() {
        return cliente;
    }

    public void setCliente(ClienteModel cliente) {
        this.cliente = cliente;
    }

    public PetModel getPet() {
        return pet;
    }

    public void setPet(PetModel pet) {
        this.pet = pet;
    }

    public FuncionarioModel getFuncionario() {
        return funcionario;
    }

    public void setFuncionario(FuncionarioModel funcionario) {
        this.funcionario = funcionario;
    }

    public Timestamp getData() {
        return data;
    }

    public void setData(Timestamp data) {
        this.data = data;
    }

    public Double getValor() {
        return valor;
    }

    public void setValor(Double valor) {
        this.valor = valor;
    }

    public StatusAgendamento getStatus() {
        return status;
    }

    public void setStatus(StatusAgendamento status) {
        this.status = status;
    }
}
