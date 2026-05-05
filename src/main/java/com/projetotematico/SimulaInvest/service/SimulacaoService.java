package com.projetotematico.SimulaInvest.service;

import com.projetotematico.SimulaInvest.domain.entity.ProjecaoMensal;
import com.projetotematico.SimulaInvest.domain.entity.ResumoResultado;
import com.projetotematico.SimulaInvest.domain.entity.Simulacao;
import com.projetotematico.SimulaInvest.repostirory.SimulacaoRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
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

        //Extrair os valores da simulacao
        double capital = Double.parseDouble(simulacao.getCapitalInicial());
        double aporteMensal = Double.parseDouble(simulacao.getAporteMensal());
        double taxa = simulacao.getTaxaRentabilidade() / 100.0;
        int meses = simulacao.getPrazoMeses();

        // Variáveis de controle para o cálculo
        double totalAcumulado = capital;
        double totalInvestido = capital;
        List<ProjecaoMensal> projecoes = new ArrayList<>();

        // Matemática: Loop de Juros Compostos mês a mês
        for (int i = 1; i <= meses; i++) {
            // Calcula o rendimento do mês e soma ao acumulado
            double jurosDoMes = totalAcumulado * taxa;
            totalAcumulado += jurosDoMes;

            // Adiciona o aporte mensal que o usuário informou
            totalAcumulado += aporteMensal;
            totalInvestido += aporteMensal;

            // Cria a "linha" da tabela para este mês
            ProjecaoMensal ponto = new ProjecaoMensal();
            ponto.setMesReferencia(i);
            // O String.format("%.2f", valor) garante que salvemos com apenas 2 casas decimais (ex: 1500,50)
            ponto.setTotalAcumulado(String.format("%.2f", totalAcumulado));
            ponto.setTotalInvestido(String.format("%.2f", totalInvestido));
            ponto.setValorJurosDoMes(String.format("%.2f", jurosDoMes));

            // Relaciona com a simulação principal e guarda na lista
            ponto.setSimulacao(simulacao);
            projecoes.add(ponto);
        }

        // Atribui a lista de meses calculada à simulação
        simulacao.setProjecoesMensais(projecoes);

        // Preencher o Resumo
        ResumoResultado resumo = new ResumoResultado();
        resumo.setValorTotalBruto(String.format("%.2f", totalAcumulado));
        resumo.setValorInvestido(String.format("%.2f", totalInvestido));
        resumo.setValorTotalJuros(String.format("%.2f", totalAcumulado - totalInvestido));

        //Relaciona o resumo com a simulação principal
        resumo.setSimulacao(simulacao);
        simulacao.setResumoResultado(resumo);

        //Salvar no banco
        return simulacaoRepository.save(simulacao);
    }

    // Método para listar o histórico na tela inicial
    public List<Simulacao> listarHistorico() {
        return simulacaoRepository.findAll();
    }
}