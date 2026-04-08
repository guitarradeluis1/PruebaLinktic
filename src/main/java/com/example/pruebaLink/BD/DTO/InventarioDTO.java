package com.example.pruebaLink.BD.DTO;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;

@Schema(description = "DTO para crear inventario")
public record InventarioDTO(
        @Schema(description = "ID", example = "1", required = true)
        @NotNull @Min(0)
        Integer id,

        @Schema(description = "Cantidad", example = "10")
        Integer cantidad
) {}