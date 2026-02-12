package hr.abysalto.hiring.mid.dto;

public record LoginRequest(
        String email,
        String password
) {}
