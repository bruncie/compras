package com.sicredi.api.entity;

import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;


@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class CompraEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    private String nomeProduto;
    private Long idProduto;
    private Integer quantidade;
    private String cpfComprador;
    private BigDecimal valorUnitario;
    private LocalDateTime dataHoraCompra;
}
