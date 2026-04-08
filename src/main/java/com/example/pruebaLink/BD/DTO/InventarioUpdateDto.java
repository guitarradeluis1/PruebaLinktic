package com.example.pruebaLink.BD.DTO;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;

@Schema(description = "DTO para actualizar inventario")
public record InventarioUpdateDto(
        @Schema(description = "Nueva cantidad", example = "100")
        @NotNull(message = "La cantidad es obligatoria")
        @Min(value = 0, message = "La cantidad no puede ser negativa")
        Integer cantidad
) {}