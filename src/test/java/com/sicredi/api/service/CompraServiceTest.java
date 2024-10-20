package com.sicredi.api.service;

import com.sicredi.api.dto.CompraDto;
import com.sicredi.api.dto.RelatorioCompraDto;
import com.sicredi.api.entity.CompraEntity;
import com.sicredi.api.exception.CompraException;
import com.sicredi.api.repository.CompraRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class CompraServiceTest {

    @Mock
    private CompraRepository compraRepository;

    @InjectMocks
    private CompraService compraService;

    private CompraDto compraDto;
    private CompraEntity compraEntity;

    @BeforeEach
    void setup() {
        compraDto = new CompraDto();
        compraDto.setCpfComprador("12345678900");
        compraDto.setNomeProduto("Produto1");
        compraDto.setQuantidade(1);
        compraDto.setValorUnitario(BigDecimal.valueOf(100.0));

        compraEntity = new CompraEntity();
        compraEntity.setCpfComprador("12345678900");
        compraEntity.setNomeProduto("Produto1");
        compraEntity.setQuantidade(1);
        compraEntity.setValorUnitario(BigDecimal.valueOf(100.0));
    }

    @Test
    void testCadastrarCompra() {
        when(compraRepository.save(any(CompraEntity.class))).thenReturn(compraEntity);
        CompraDto resultado = compraService.cadastrarCompra(compraDto);

        assertNotNull(resultado);
        assertEquals(compraDto.getCpfComprador(), resultado.getCpfComprador());
        verify(compraRepository, times(1)).save(any(CompraEntity.class));
    }

    @Test
    void testBuscarCompras() {
        when(compraRepository.findByCpfNomeProdutoAndDateRange(anyString(), anyString(), any(), any()))
            .thenReturn(List.of(compraEntity));

        List<CompraDto> resultado = compraService.buscarCompras("12345678900", "Produto1", "17-10-2024 14:02:00", "18-10-2024 15:13:00");

        assertNotNull(resultado);
        assertEquals(1, resultado.size());
        assertEquals(compraDto.getNomeProduto(), resultado.get(0).getNomeProduto());
    }

    @Test
    void testBuscarComprasComNomeProdutoInvalido() {
        CompraException exception = assertThrows(CompraException.class, () ->
            compraService.buscarCompras("12345678900", "Pr", "2023-01-01T00:00:00", "2023-12-31T23:59:59")
        );

        assertEquals("Nome do produto deve ter no mínimo 3 caracteres.", exception.getMessage());
    }

    @Test
    void testBuscarRelatorioComprasPorPeriodo() {
        when(compraRepository.findAllByPeriodo(any(LocalDateTime.class), any(LocalDateTime.class)))
            .thenReturn(List.of(compraEntity));

        List<RelatorioCompraDto> resultado = compraService.buscarRelatorioComprasPorPeriodo(
            LocalDateTime.of(2023, 1, 1, 0, 0),
            LocalDateTime.of(2023, 12, 31, 23, 59)
        );

        assertNotNull(resultado);
        assertEquals(1, resultado.size());
        assertEquals("Produto1", resultado.get(0).getNomeProduto());
        assertEquals(1, resultado.get(0).getQuantidadeProdutos());
        assertEquals(BigDecimal.valueOf(100.0), resultado.get(0).getValorTotal());
    }

    @Test
    void testValidaQuantidadesComMaisDeTresItens() {
        CompraEntity compra1 = new CompraEntity();
        compra1.setQuantidade(2);
        CompraEntity compra2 = new CompraEntity();
        compra2.setQuantidade(2);

        when(compraRepository.findByNomeProdutoContainingIgnoreCaseAndCpfComprador(anyString(), anyString()))
            .thenReturn(Arrays.asList(compra1, compra2));

        CompraException exception = assertThrows(CompraException.class, () ->
            compraService.cadastrarCompra(compraDto)
        );

        assertEquals("O mesmo comprador nao pode comprar mais de 3 itens do mesmo produto", exception.getMessage());
    }

    @Test
    void testValidaQuantidadesMenosDeTresItens() {
        CompraEntity compra1 = new CompraEntity();
        compra1.setQuantidade(1);

        when(compraRepository.findByNomeProdutoContainingIgnoreCaseAndCpfComprador(anyString(), anyString()))
            .thenReturn(Arrays.asList(compra1));

        when(compraRepository.save(any(CompraEntity.class))).thenReturn(compraEntity);

        CompraDto resultado = compraService.cadastrarCompra(compraDto);

        assertNotNull(resultado);
        verify(compraRepository, times(1)).save(any(CompraEntity.class));
    }
}