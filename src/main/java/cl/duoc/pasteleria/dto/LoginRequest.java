package cl.duoc.pasteleria.dto;

import jakarta.validation.constraints.NotBlank;

public record LoginRequest(@NotBlank String correo, @NotBlank String clave) {
}
