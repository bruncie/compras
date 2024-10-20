package com.sicredi.api.repository;

import com.sicredi.api.entity.CompraEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface CompraRepository extends JpaRepository<CompraEntity, Long> {

    List<CompraEntity> findByNomeProdutoContainingIgnoreCaseAndCpfComprador(String nomeProduto, String cpfComprador);

    @Query("SELECT c FROM CompraEntity c WHERE " +
            "c.cpfComprador = :cpf AND " +
            "LOWER(c.nomeProduto) LIKE LOWER(CONCAT('%', :nomeProduto, '%')) AND " +
            "c.dataHoraCompra BETWEEN :dataInicio AND :dataFim")
    List<CompraEntity> findByCpfNomeProdutoAndDateRange(
            @Param("cpf") String cpf,
            @Param("nomeProduto") String nomeProduto,
            @Param("dataInicio") LocalDateTime dataInicio,
            @Param("dataFim") LocalDateTime dataFim);

    @Query("SELECT c FROM CompraEntity c WHERE c.dataHoraCompra BETWEEN :dataInicio AND :dataFim")
    List<CompraEntity> findAllByPeriodo(@Param("dataInicio") LocalDateTime dataInicio, @Param("dataFim") LocalDateTime dataFim);
}
