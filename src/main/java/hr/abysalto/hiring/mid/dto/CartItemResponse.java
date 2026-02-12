package hr.abysalto.hiring.mid.dto;

public record CartItemResponse(
        Long productId,
        String title,
        Double price,
        int quantity
) {}
