package cl.duoc.pasteleria.dto;

import jakarta.validation.Valid;
import jakarta.validation.constraints.*;
import java.util.List;

public record CompraRequest(
        @NotBlank String direccion,
        String codigo,
        @NotEmpty List<@Valid Item> items) {

    public record Item(@NotNull Long productoId, @Min(1) int cantidad) {
    }
}
