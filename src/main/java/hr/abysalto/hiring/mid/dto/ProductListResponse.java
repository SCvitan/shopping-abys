package hr.abysalto.hiring.mid.dto;

import java.util.List;

public record ProductListResponse(
        List<ProductDto> products,
        int total,
        int skip,
        int limit
) {}
