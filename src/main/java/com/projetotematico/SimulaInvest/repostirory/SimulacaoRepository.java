package com.projetotematico.SimulaInvest.repostirory;


import com.projetotematico.SimulaInvest.domain.entity.Simulacao;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * Interface de repositório para a entidade Simulacao.
 * O JpaRepository já fornece métodos como save(), findAll(), findById() e delete().
 */
@Repository
public interface SimulacaoRepository extends JpaRepository<Simulacao, Long> {
}