package com.sicredi.api.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.*;

import java.math.BigDecimal;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class CompraDto {

    @NotBlank
    private String nomeProduto;

    @NotNull
    private Long idProduto;

    @NotNull
    private Integer quantidade;

    @NotBlank
    private String cpfComprador;

    @NotNull
    private BigDecimal valorUnitario;

    @NotBlank
    private String dataHoraCompra;
}
