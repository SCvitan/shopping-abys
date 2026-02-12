package hr.abysalto.hiring.mid.dto;

import java.util.List;

public record CartResponse(
        List<CartItemResponse> items,
        Double totalPrice
) {}
