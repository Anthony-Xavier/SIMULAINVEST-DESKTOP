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
import javafx.stage.FileChooser;
import java.io.File;
import java.io.PrintWriter;

@Controller
public class SimulacaoController {

    private final SimulacaoService simulacaoService;

    private Simulacao simulacaoAtual;

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

            // --- LÓGICA COLOCADA DENTRO DO MÉTODO APÓS O CÁLCULO ---
            this.simulacaoAtual = simulacaoCalculada;

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

    @FXML
    public void onExportarDadosClicado() {
        if (simulacaoAtual == null || simulacaoAtual.getProjecoesMensais() == null) {
            System.out.println("Nenhuma simulação para exportar!");
            return;
        }

        // Abre a janela para o utilizador escolher onde guardar o ficheiro
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Guardar Exportação");
        fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("Ficheiro CSV", "*.csv"));
        fileChooser.setInitialFileName("SimulaInvest_Exportacao.csv");

        File file = fileChooser.showSaveDialog(null);

        if (file != null) {
            try (PrintWriter writer = new PrintWriter(file, "UTF-8")) {
                // Escreve o cabeçalho
                writer.println("Mês,Juros do Mês,Total Investido,Total Acumulado");

                // Escreve as linhas da tabela
                for (ProjecaoMensal p : simulacaoAtual.getProjecoesMensais()) {
                    writer.println(
                            p.getMesReferencia() + "," +
                                    p.getValorJurosDoMes().replace(",", ".") + "," +
                                    p.getTotalInvestido().replace(",", ".") + "," +
                                    p.getTotalAcumulado().replace(",", ".")
                    );
                }
                System.out.println("Ficheiro exportado com sucesso para: " + file.getAbsolutePath());
            } catch (Exception e) {
                System.out.println("Erro ao exportar: " + e.getMessage());
            }
        }
    }
}