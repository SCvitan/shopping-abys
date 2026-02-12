package hr.abysalto.hiring.mid.dto;

public record AddToCartRequest(
        Long productId,
        int quantity
) {}
