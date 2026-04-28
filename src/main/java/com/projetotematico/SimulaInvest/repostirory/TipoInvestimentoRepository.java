package com.projetotematico.SimulaInvest.repostirory;

import com.projetotematico.SimulaInvest.domain.entity.TipoInvestimento;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TipoInvestimentoRepository extends JpaRepository<TipoInvestimento, Long> {}