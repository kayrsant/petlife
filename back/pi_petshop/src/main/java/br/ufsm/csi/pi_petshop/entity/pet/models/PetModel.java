package br.ufsm.csi.pi_petshop.entity.pet.models;

import br.ufsm.csi.pi_petshop.entity.cliente.models.ClienteModel;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Table(name="pets")
public class PetModel {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, length = 100)
    private String nome;

    @Column(nullable = false)
    private Integer idade;

    @Column(nullable = false, length = 100)
    private String raca;

    @Column(nullable = false, length = 100)
    private String tipo;

    @Column(length = 100)
    private String peso;

    @Column(nullable = false, length = 100)
    private String genero;

    @Column(length = 100)
    private String alergias;

    @Column(name = "em_tratamento", length = 100)
    private String emTratamento;

    @ManyToOne
    @JoinColumn(name = "id_cliente", nullable = false)
    private @NotNull ClienteModel cliente;

    public PetModel(String nome, Integer idade, String raca, String tipo, String peso, String genero, String alergias, String emTratamento, ClienteModel cliente) {
        this.nome = nome;
        this.idade = idade;
        this.raca = raca;
        this.tipo = tipo;
        this.peso = peso;
        this.genero = genero;
        this.alergias = alergias;
        this.emTratamento = emTratamento;
        this.cliente = cliente;
    }

    public PetModel(String nome, String tipo, Integer idade, ClienteModel cliente){
        this.nome = nome;
        this.tipo = tipo;
        this.idade = idade;
        this.cliente = cliente;
    }

    public PetModel(String nome, String raca, Integer idade, String tipo, String peso, String genero, String alergias, String emTratamento, ClienteModel cliente) {
        this.nome = nome;
        this.raca = raca;
        this.idade = idade;
        this.tipo = tipo;
        this.peso = peso;
        this.genero = genero;
        this.alergias = alergias;
        this.emTratamento = emTratamento;
        this.cliente = cliente;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public Integer getIdade() {
        return idade;
    }

    public void setIdade(Integer idade) {
        this.idade = idade;
    }

    public String getRaca() {
        return raca;
    }

    public void setRaca(String raca) {
        this.raca = raca;
    }

    public String getTipo() {
        return tipo;
    }

    public void setTipo(String tipo) {
        this.tipo = tipo;
    }

    public String getPeso() {
        return peso;
    }

    public void setPeso(String peso) {
        this.peso = peso;
    }

    public String getGenero() {
        return genero;
    }

    public void setGenero(String genero) {
        this.genero = genero;
    }

    public String getAlergias() {
        return alergias;
    }

    public void setAlergias(String alergias) {
        this.alergias = alergias;
    }

    public String getEmTratamento() {
        return emTratamento;
    }

    public void setEmTratamento(String emTratamento) {
        this.emTratamento = emTratamento;
    }

    public @NotNull ClienteModel getCliente() {
        return cliente;
    }

    public void setCliente(ClienteModel cliente) {
        this.cliente = cliente;
    }
}
