package br.ufsm.csi.pi_petshop.entity.agendamento.enums;

public enum StatusAgendamento {
    ABERTO("aberto"),
    FINALIZADO("fechado"),
    EM_ANDAMENTO("em andamento");

    private String status;

    StatusAgendamento (String status){
        this.status = status;
    }

    public String getStatus(){
        return status;
    }
}
