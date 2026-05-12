package com.projetotematico.SimulaInvest.controller;

import com.projetotematico.SimulaInvest.domain.entity.ProjecaoMensal;
import com.projetotematico.SimulaInvest.domain.entity.Simulacao;
import com.projetotematico.SimulaInvest.service.SimulacaoService;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import org.springframework.stereotype.Controller;

@Controller
public class SimulacaoController {

    private final SimulacaoService simulacaoService;

    public SimulacaoController(SimulacaoService simulacaoService) {
        this.simulacaoService = simulacaoService;
    }


    @FXML private TextField txtCapitalInicial;
    @FXML private TextField txtAporteMensal;
    @FXML private TextField txtPrazo;
    @FXML private TextField txtRentabilidade;


    @FXML private Label lblValorTotalBruto;
    @FXML private Label lblValorInvestido;
    @FXML private Label lblValorTotalJuros;
    @FXML private Label lblValorPagoIR;
    @FXML private Label lblValorTotalLiquido;

    @FXML private TableView<ProjecaoMensal> tabelaProjecoes;
    @FXML private TableColumn<ProjecaoMensal, Integer> colMes;
    @FXML private TableColumn<ProjecaoMensal, String> colJuros;
    @FXML private TableColumn<ProjecaoMensal, String> colInvestido;
    @FXML private TableColumn<ProjecaoMensal, String> colAcumulado;

    @FXML
    public void initialize() {
        colMes.setCellValueFactory(new PropertyValueFactory<>("mesReferencia"));
        colJuros.setCellValueFactory(new PropertyValueFactory<>("valorJurosDoMes"));
        colInvestido.setCellValueFactory(new PropertyValueFactory<>("totalInvestido"));
        colAcumulado.setCellValueFactory(new PropertyValueFactory<>("totalAcumulado"));
    }

    @FXML
    public void onCalcularRendimentoClicado() {
        try {
            Simulacao novaSimulacao = new Simulacao();
            novaSimulacao.setCapitalInicial(txtCapitalInicial.getText().replace(",", "."));
            novaSimulacao.setAporteMensal(txtAporteMensal.getText().replace(",", "."));
            novaSimulacao.setPrazoMeses(Integer.parseInt(txtPrazo.getText()));
            novaSimulacao.setTaxaRentabilidade(Double.parseDouble(txtRentabilidade.getText().replace(",", ".")));

            Simulacao simulacaoCalculada = simulacaoService.calcularEGravarSimulacao(novaSimulacao);

            // Atualiza os Cards
            lblValorTotalBruto.setText("R$ " + simulacaoCalculada.getResumoResultado().getValorTotalBruto());
            lblValorInvestido.setText("R$ " + simulacaoCalculada.getResumoResultado().getValorInvestido());
            lblValorTotalJuros.setText("R$ " + simulacaoCalculada.getResumoResultado().getValorTotalJuros());

            String ir = simulacaoCalculada.getResumoResultado().getValorPagoIR() != null
                    ? simulacaoCalculada.getResumoResultado().getValorPagoIR() : "0.00";
            lblValorPagoIR.setText("R$ " + ir);
            lblValorTotalLiquido.setText("R$ " + simulacaoCalculada.getResumoResultado().getValorTotalLiquido());

            ObservableList<ProjecaoMensal> dadosTabela = FXCollections.observableArrayList(simulacaoCalculada.getProjecoesMensais());
            tabelaProjecoes.setItems(dadosTabela);

        } catch (Exception e) {
            System.out.println("Ocorreu um erro ao calcular: " + e.getMessage());
        }
    }
}