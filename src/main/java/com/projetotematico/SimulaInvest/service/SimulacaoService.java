package com.projetotematico.SimulaInvest.service;

import com.projetotematico.SimulaInvest.domain.entity.Simulacao;
import com.projetotematico.SimulaInvest.repostirory.SimulacaoRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class SimulacaoService {

    private final SimulacaoRepository simulacaoRepository;

    // Injeção de dependência do repositório
    public SimulacaoService(SimulacaoRepository simulacaoRepository) {
        this.simulacaoRepository = simulacaoRepository;
    }

    @Transactional
    public Simulacao calcularEGravarSimulacao(Simulacao simulacao) {

        // extrair os valores da simulacao (Capital, Aporte, Taxa, Prazo)
        double capital = Double.parseDouble(simulacao.getCapitalInicial());
        double aporteMensal = Double.parseDouble(simulacao.getAporteMensal());
        double taxa = simulacao.getTaxaRentabilidade() / 100.0;
        int meses = simulacao.getPrazoMeses();

        return simulacaoRepository.save(simulacao);
    }

    // Método para listar o histórico na tela inicial
    public List<Simulacao> listarHistorico() {
        return simulacaoRepository.findAll();
    }
}