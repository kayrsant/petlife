package br.ufsm.csi.pi_petshop.entity.user.enums;

public enum UserRole {
    ADMIN("admin"),
    FUNCIONARIO("funcionario"),
    CLIENTE("user"),
    USER("cliente");

    private String role;

    UserRole (String role){
        this.role = role;
    }

    public String getRole(){
        return role;
    }
}
