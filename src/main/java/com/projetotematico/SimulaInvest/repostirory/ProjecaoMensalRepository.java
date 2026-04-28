package com.projetotematico.SimulaInvest.repostirory;

import com.projetotematico.SimulaInvest.domain.entity.ProjecaoMensal;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ProjecaoMensalRepository extends JpaRepository<ProjecaoMensal, Long> {}