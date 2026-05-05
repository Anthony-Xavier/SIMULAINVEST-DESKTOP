package com.projetotematico.SimulaInvest.controller;

import com.projetotematico.SimulaInvest.domain.entity.Simulacao;
import com.projetotematico.SimulaInvest.service.SimulacaoService;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import org.springframework.stereotype.Controller;

@Controller
public class SimulacaoController {

    private final SimulacaoService simulacaoService;

    public SimulacaoController(SimulacaoService simulacaoService) {
        this.simulacaoService = simulacaoService;
    }

    // --- CAMPOS DE ENTRADA ---
    @FXML private TextField txtCapitalInicial;
    @FXML private TextField txtAporteMensal;
    @FXML
    private TextField txtPrazo;
    @FXML private TextField txtRentabilidade;

    // --- CAMPOS DE SAÍDA ---
    @FXML private Label lblValorTotalBruto;
    @FXML private Label lblValorInvestido;
    @FXML private Label lblValorTotalJuros;
    @FXML private Label lblValorPagoIR;
    @FXML private Label lblValorTotalLiquido;

    // --- MÉTODO DO BOTÃO "CALCULAR" ---
    @FXML
    public void onCalcularRendimentoClicado() {
        try {
            //Cria uma nova simulação e preenche com os textos da tela
            Simulacao novaSimulacao = new Simulacao();
            novaSimulacao.setCapitalInicial(txtCapitalInicial.getText().replace(",", "."));
            novaSimulacao.setAporteMensal(txtAporteMensal.getText().replace(",", "."));
            novaSimulacao.setPrazoMeses(Integer.parseInt(txtPrazo.getText()));
            novaSimulacao.setTaxaRentabilidade(Double.parseDouble(txtRentabilidade.getText().replace(",", ".")));

            //gravar na no db
            Simulacao simulacaoCalculada = simulacaoService.calcularEGravarSimulacao(novaSimulacao);

            // Atualiza os cards coloridos da tela com as respostas
            lblValorTotalBruto.setText("R$ " + simulacaoCalculada.getResumoResultado().getValorTotalBruto());
            lblValorInvestido.setText("R$ " + simulacaoCalculada.getResumoResultado().getValorInvestido());
            lblValorTotalJuros.setText("R$ " + simulacaoCalculada.getResumoResultado().getValorTotalJuros());

            // Tratamento temporário para o IR
            String ir = simulacaoCalculada.getResumoResultado().getValorPagoIR() != null
                    ? simulacaoCalculada.getResumoResultado().getValorPagoIR() : "0.00";
            lblValorPagoIR.setText("R$ " + ir);

            lblValorTotalLiquido.setText("R$ " + simulacaoCalculada.getResumoResultado().getValorTotalLiquido());

        } catch (NumberFormatException e) {
            System.out.println("ERRO: Formatação incorreta. Digita apenas números!");
        } catch (Exception e) {
            System.out.println("Ocorreu um erro ao calcular: " + e.getMessage());
        }
    }
}