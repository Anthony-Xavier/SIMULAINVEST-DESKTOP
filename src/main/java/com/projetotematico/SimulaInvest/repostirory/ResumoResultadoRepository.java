package com.projetotematico.SimulaInvest.repostirory;

import com.projetotematico.SimulaInvest.domain.entity.ResumoResultado;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ResumoResultadoRepository extends JpaRepository<ResumoResultado, Long> {}