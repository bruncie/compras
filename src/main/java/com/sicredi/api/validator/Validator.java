package com.sicredi.api.validator;

import com.sicredi.api.dto.CompraDto;
import com.sicredi.api.exception.CompraException;
import com.sicredi.api.utils.Utils;

public class Validator {
    public static void validaCompraDto(CompraDto dto) {
        validaDataHora(dto.getDataHoraCompra());
        validaQuantidade(dto.getQuantidade());
    }

    public static void validaDataHora(String dataHora) {
        if (!Utils.validarDataHora(dataHora)) {
            throw new CompraException("dataHora fora do padrão 'dd-MM-aa HH:mm:ss'");
        }
    }

    private static void validaQuantidade(Integer quantidade) {
        if (quantidade > 3) {
            throw new CompraException("O mesmo comprador nao pode comprar mais de 3 itens do mesmo produto");
        }
    }
}
