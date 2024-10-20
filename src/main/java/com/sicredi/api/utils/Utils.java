package com.sicredi.api.utils;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;

public class Utils {

    private static final DateTimeFormatter FORMATTER = DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm:ss");

    public static String mascararCpf(String cpf) {
        String cpfNumeros = cpf.replaceAll("[^\\d]", "");
        StringBuilder cpfMascarado = new StringBuilder(cpfNumeros);
        cpfMascarado.replace(3, 9, "******");

        return cpfMascarado.insert(3, ".").insert(7, ".").insert(11, "-").toString();
    }

    public static boolean validarDataHora(String dataHoraString) {
        try {
            LocalDateTime.parse(dataHoraString, FORMATTER);
            return true;
        } catch (DateTimeParseException e) {
            return false;
        }
    }

    public static LocalDateTime stringToLocalDateTime(String dateTimeString) {
        return LocalDateTime.parse(dateTimeString, FORMATTER);
    }

    public static String localDateTimeToString(LocalDateTime dateTime) {
        return dateTime.format(FORMATTER);
    }
}
