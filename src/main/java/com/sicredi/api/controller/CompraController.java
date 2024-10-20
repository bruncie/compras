package com.sicredi.api.controller;

import com.sicredi.api.dto.CompraDto;
import com.sicredi.api.dto.RelatorioCompraDto;
import com.sicredi.api.service.CompraService;
import com.sicredi.api.validator.Validator;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequestMapping("/api/compras")
@RequiredArgsConstructor
public class CompraController {

    static final Logger LOG = LoggerFactory.getLogger(CompraController.class);

    private final CompraService compraService;

    @PostMapping
    public ResponseEntity<CompraDto> cadastrarCompra(@RequestBody @Valid CompraDto compraDto) {
        Validator.validaCompraDto(compraDto);
        CompraDto novaCompra = compraService.cadastrarCompra(compraDto);
        return ResponseEntity.status(HttpStatus.CREATED).body(novaCompra);
    }

    @GetMapping
    public ResponseEntity<List<CompraDto>> buscarCompras(@RequestParam String cpfComprador,
                                                         @RequestParam String nomeProduto,
                                                         @RequestParam @DateTimeFormat(pattern = "dd-MM-yyyy HH:mm:ss") String dataInicio,
                                                         @RequestParam @DateTimeFormat(pattern = "dd-MM-yyyy HH:mm:ss") String dataFim) {
        Validator.validaDataHora(dataInicio);
        Validator.validaDataHora(dataFim);
        return ResponseEntity.ok(compraService.buscarCompras(cpfComprador, nomeProduto, dataInicio, dataFim));
    }

    @GetMapping("/relatorio")
    public ResponseEntity<List<RelatorioCompraDto>> buscarComprasPorPeriodo(
            @RequestParam("dataInicio") @DateTimeFormat(pattern = "dd-MM-yyyy HH:mm:ss") LocalDateTime dataInicio,
            @RequestParam("dataFim") @DateTimeFormat(pattern = "dd-MM-yyyy HH:mm:ss") LocalDateTime dataFim) {

        List<RelatorioCompraDto> resultado = compraService.buscarRelatorioComprasPorPeriodo(dataInicio, dataFim);
        return ResponseEntity.ok(resultado);
    }
}
