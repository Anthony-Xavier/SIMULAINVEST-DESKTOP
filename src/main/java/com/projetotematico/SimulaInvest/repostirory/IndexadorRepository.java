package com.projetotematico.SimulaInvest.repostirory;

import com.projetotematico.SimulaInvest.domain.entity.Indexador;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface IndexadorRepository extends JpaRepository<Indexador, Long> {}