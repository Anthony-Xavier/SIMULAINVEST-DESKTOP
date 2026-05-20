package com.projetotematico.SimulaInvest.service;

import com.projetotematico.SimulaInvest.domain.entity.ProjecaoMensal;
import com.projetotematico.SimulaInvest.domain.entity.ResumoResultado;
import com.projetotematico.SimulaInvest.domain.entity.Simulacao;
import com.projetotematico.SimulaInvest.domain.entity.Usuario;
import com.projetotematico.SimulaInvest.repository.SimulacaoRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

/**
 * Lógica de cálculo de juros compostos e persistência da simulação.
 * Usa BigDecimal para garantir precisão (RNF 3 - Confiabilidade).
 */
@Service
public class SimulacaoService {

    private static final int ESCALA = 8;

    private final SimulacaoRepository simulacaoRepository;

    public SimulacaoService(SimulacaoRepository simulacaoRepository) {
        this.simulacaoRepository = simulacaoRepository;
    }

    @Transactional
    public Simulacao calcularEGravarSimulacao(Simulacao simulacao, Usuario usuarioLogado) {
        if (simulacao.getCapitalInicial() == null
                || simulacao.getAporteMensal() == null
                || simulacao.getPrazoMeses() == null
                || simulacao.getTaxaRentabilidade() == null) {
            throw new IllegalArgumentException("Parâmetros da simulação incompletos.");
        }

        BigDecimal capital = simulacao.getCapitalInicial();
        BigDecimal aporteMensal = simulacao.getAporteMensal();
        BigDecimal taxa = BigDecimal.valueOf(simulacao.getTaxaRentabilidade())
                .divide(BigDecimal.valueOf(100), ESCALA, RoundingMode.HALF_UP);
        int meses = simulacao.getPrazoMeses();

        BigDecimal totalAcumulado = capital;
        BigDecimal totalInvestido = capital;
        BigDecimal totalJurosAcumulado = BigDecimal.ZERO;
        List<ProjecaoMensal> projecoes = new ArrayList<>();

        for (int i = 1; i <= meses; i++) {
            BigDecimal jurosDoMes = totalAcumulado.multiply(taxa)
                    .setScale(ESCALA, RoundingMode.HALF_UP);

            totalAcumulado = totalAcumulado.add(jurosDoMes).add(aporteMensal);
            totalInvestido = totalInvestido.add(aporteMensal);
            totalJurosAcumulado = totalJurosAcumulado.add(jurosDoMes);

            ProjecaoMensal ponto = new ProjecaoMensal();
            ponto.setMesReferencia(i);
            ponto.setValorJurosDoMes(jurosDoMes.setScale(2, RoundingMode.HALF_UP));
            ponto.setTotalInvestido(totalInvestido.setScale(2, RoundingMode.HALF_UP));
            ponto.setTotalJurosAcumulado(totalJurosAcumulado.setScale(2, RoundingMode.HALF_UP));
            ponto.setTotalAcumulado(totalAcumulado.setScale(2, RoundingMode.HALF_UP));
            ponto.setSimulacao(simulacao);
            projecoes.add(ponto);
        }

        simulacao.setProjecoesMensais(projecoes);
        simulacao.setUsuario(usuarioLogado);
        if (simulacao.getDataSimulacao() == null) {
            simulacao.setDataSimulacao(LocalDateTime.now());
        }

        BigDecimal totalJuros = totalAcumulado.subtract(totalInvestido);
        BigDecimal aliquota = calcularAliquotaIR(meses);
        BigDecimal valorImposto = totalJuros.multiply(aliquota);
        BigDecimal valorLiquido = totalAcumulado.subtract(valorImposto);

        ResumoResultado resumo = new ResumoResultado();
        resumo.setValorTotalBruto(totalAcumulado.setScale(2, RoundingMode.HALF_UP));
        resumo.setValorInvestido(totalInvestido.setScale(2, RoundingMode.HALF_UP));
        resumo.setValorTotalJuros(totalJuros.setScale(2, RoundingMode.HALF_UP));
        resumo.setValorPagoIR(valorImposto.setScale(2, RoundingMode.HALF_UP));
        resumo.setValorTotalLiquido(valorLiquido.setScale(2, RoundingMode.HALF_UP));
        resumo.setSimulacao(simulacao);
        simulacao.setResumoResultado(resumo);

        return simulacaoRepository.save(simulacao);
    }

    public List<Simulacao> listarHistorico() {
        return simulacaoRepository.findAll();
    }

    private BigDecimal calcularAliquotaIR(int prazoMeses) {
        if (prazoMeses <= 6) return new BigDecimal("0.225");
        if (prazoMeses <= 12) return new BigDecimal("0.20");
        if (prazoMeses <= 24) return new BigDecimal("0.175");
        return new BigDecimal("0.15");
    }
}
