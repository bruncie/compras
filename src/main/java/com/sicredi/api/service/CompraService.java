package com.sicredi.api.service;

import com.sicredi.api.dto.CompraDto;
import com.sicredi.api.dto.RelatorioCompraDto;
import com.sicredi.api.entity.CompraEntity;
import com.sicredi.api.exception.CompraException;
import com.sicredi.api.mapper.Mapper;
import com.sicredi.api.repository.CompraRepository;
import com.sicredi.api.utils.Utils;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RequiredArgsConstructor
@Service
public class CompraService {
    private final CompraRepository compraRepository;

    public CompraDto cadastrarCompra(CompraDto compraDto) {
        this.validaQuantidades(compraDto.getCpfComprador(), compraDto.getNomeProduto());
        CompraEntity entity = compraRepository.save(Mapper.parseToEntity(compraDto));
        return Mapper.parseToDto(entity);
    }

    public List<CompraDto> buscarCompras(String cpfComprador, String nomeProduto, String dataInicio, String dataFim) {
        if (nomeProduto != null && nomeProduto.length() < 3) {
            throw new CompraException("Nome do produto deve ter no mínimo 3 caracteres.");
        }
        var entityList = compraRepository.findByCpfNomeProdutoAndDateRange(cpfComprador, nomeProduto, Utils.stringToLocalDateTime(dataInicio), Utils.stringToLocalDateTime(dataFim));
        return Mapper.parseEntityListToDto(entityList);
    }

    public List<RelatorioCompraDto> buscarRelatorioComprasPorPeriodo(LocalDateTime dataInicio, LocalDateTime dataFim) {
        List<CompraEntity> compras = compraRepository.findAllByPeriodo(dataInicio, dataFim);

        Map<String, RelatorioCompraDto> agrupado = new HashMap<>();

        for (CompraEntity compra : compras) {
            String nomeProduto = compra.getNomeProduto();
            BigDecimal valorUnitario = compra.getValorUnitario();

            RelatorioCompraDto dto = agrupado.getOrDefault(nomeProduto, new RelatorioCompraDto());
            dto.setNomeProduto(nomeProduto);
            dto.setValorUnitario(valorUnitario);

            int quantidadeAtual = dto.getQuantidadeProdutos() != null ? dto.getQuantidadeProdutos() : 0;
            dto.setQuantidadeProdutos(quantidadeAtual + compra.getQuantidade());

            dto.setValorTotal(dto.getValorUnitario().multiply(BigDecimal.valueOf(dto.getQuantidadeProdutos())));
            agrupado.put(nomeProduto, dto);
        }

        return new ArrayList<>(agrupado.values());
    }

    private void validaQuantidades(String cpf, String nomeProduto) {
        var entity = compraRepository.findByNomeProdutoContainingIgnoreCaseAndCpfComprador(nomeProduto, cpf);
        int quantidade = entity.stream()
                .mapToInt(CompraEntity::getQuantidade)  // Extrai o campo 'quantidade'
                .sum();
        if (quantidade >= 3)
            throw new CompraException("O mesmo comprador nao pode comprar mais de 3 itens do mesmo produto");
    }
}
