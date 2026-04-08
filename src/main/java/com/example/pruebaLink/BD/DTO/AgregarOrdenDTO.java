package com.example.pruebaLink.BD.DTO;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;

@Schema(description = "DTO para crear productos en una orde de servicio")
public record  AgregarOrdenDTO(

        @Schema(description = "ordenId", example = "1", required = true)
        @NotNull
        Integer ordenId,

        @Schema(description = "productoId", example = "1", required = true)
        @NotNull
        Integer productoId,

        @Schema(description = "Cantidad", example = "2", required = true)
        @NotNull @Min(1)
        Integer cantidad,

        @Schema(description = "Costo", example = "20.0", required = true)
        @NotNull @Min(1)
        Double costo
) {}