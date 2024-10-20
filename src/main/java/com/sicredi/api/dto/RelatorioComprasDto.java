package com.sicredi.api.dto;

import lombok.*;

import java.math.BigDecimal;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class RelatorioComprasDto {
    private String nomeProduto;
    private BigDecimal valorUnitario;
    private Long quantidadeCompras;
    private BigDecimal totalVendido;
}
