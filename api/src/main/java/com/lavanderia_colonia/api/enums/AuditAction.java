package com.lavanderia_colonia.api.enums;

public enum AuditAction {
    ORDER_CREATED("Pedido criado"),
    ORDER_UPDATED("Pedido atualizado"),
    ORDER_FINISHED("Pedido finalizado"),
    ORDER_CANCELLED("Pedido cancelado"),
    ORDER_DELETED("Pedido excluído"),
    CLIENT_CREATED("Cliente criado"),
    CLIENT_UPDATED("Cliente atualizado"),
    CLIENT_DELETED("Cliente deletado"),
    PRODUCT_CREATED("Produto criado"),
    PRODUCT_UPDATED("Produto atualizado"),
    USER_LOGIN("Usuário logado"),
    USER_LOGOUT("Usuário deslogado"),
    PAYMENT_RECEIVED("Pagamento recebido"),
    STATUS_CHANGED("Status alterado");

    private final String description;

    AuditAction(String description) {
        this.description = description;
    }

    public String getDescription() {
        return description;
    }
}
