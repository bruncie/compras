package com.sicredi.api.mapper;

import com.sicredi.api.dto.CompraDto;
import com.sicredi.api.entity.CompraEntity;
import com.sicredi.api.utils.Utils;

import java.util.List;
import java.util.stream.Collectors;

public class Mapper {
    public static CompraEntity parseToEntity(CompraDto dto) {
        return CompraEntity.builder()
                .nomeProduto(dto.getNomeProduto())
                .idProduto(dto.getIdProduto())
                .quantidade(dto.getQuantidade())
                .cpfComprador(dto.getCpfComprador())
                .valorUnitario(dto.getValorUnitario())
                .dataHoraCompra(Utils.stringToLocalDateTime(dto.getDataHoraCompra()))
                .build();
    }

    public static CompraDto parseToDto(CompraEntity entity) {
        return CompraDto.builder()
                .nomeProduto(entity.getNomeProduto())
                .idProduto(entity.getIdProduto())
                .quantidade(entity.getQuantidade())
                .cpfComprador(Utils.mascararCpf(entity.getCpfComprador()))
                .valorUnitario(entity.getValorUnitario())
                .dataHoraCompra(Utils.localDateTimeToString(entity.getDataHoraCompra()))
                .build();
    }

    public static List<CompraDto> parseEntityListToDto(List<CompraEntity> compras) {
        return compras.stream()
                .map(compraEntity -> new CompraDto(
                        compraEntity.getNomeProduto(),
                        compraEntity.getIdProduto(),
                        compraEntity.getQuantidade(),
                        compraEntity.getCpfComprador(),
                        compraEntity.getValorUnitario(),
                        compraEntity.getDataHoraCompra().toString()))
                .collect(Collectors.toList());
    }
}
