package hr.abysalto.hiring.mid.dto;

public record ProductDto(
        Long id,
        String title,
        String description,
        Double price,
        String thumbnail
) {}
