package br.ufsm.csi.pi_petshop.user.enums;

public enum UserRole {
    ADMIN("admin"),
    FUNCIONARIO("funcionario"),
    CLIENTE("user");

    private String role;

    UserRole (String role){
        this.role = role;
    }

    public String getRole(){
        return role;
    }
}
