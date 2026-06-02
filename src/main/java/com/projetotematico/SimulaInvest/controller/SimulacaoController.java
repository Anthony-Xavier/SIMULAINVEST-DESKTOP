package com.projetotematico.SimulaInvest.controller;

import com.projetotematico.SimulaInvest.domain.entity.ProjecaoMensal;
import com.projetotematico.SimulaInvest.domain.entity.Simulacao;
import com.projetotematico.SimulaInvest.service.AuthService;
import com.projetotematico.SimulaInvest.service.SimulacaoService;
import com.projetotematico.SimulaInvest.ui.TelaNavigator;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.FileChooser;
import org.springframework.stereotype.Controller;

import java.io.File;
import java.io.PrintWriter;
import java.math.BigDecimal;
import java.math.RoundingMode;

@Controller
public class SimulacaoController {

    private final SimulacaoService simulacaoService;
    private final AuthService authService;
    private final TelaNavigator telaNavigator;

    private Simulacao simulacaoAtual;

    public SimulacaoController(SimulacaoService simulacaoService,
                               AuthService authService,
                               TelaNavigator telaNavigator) {
        this.simulacaoService = simulacaoService;
        this.authService = authService;
        this.telaNavigator = telaNavigator;
    }

    @FXML
    public void onLogoutClicado() {
        authService.logout();
        telaNavigator.abrirLogin();
    }

    @FXML private Label lblUsuarioLogado;

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
        if (authService.getUsuarioLogado() != null) {
            lblUsuarioLogado.setText("Olá, " + authService.getUsuarioLogado().getNome());
        }
        colMes.setCellValueFactory(new PropertyValueFactory<>("mesReferencia"));
        // Como os valores são BigDecimal, usamos lambdas para formatar em texto BR.
        colJuros.setCellValueFactory(c ->
                new javafx.beans.property.SimpleStringProperty(formatBR(c.getValue().getValorJurosDoMes())));
        colInvestido.setCellValueFactory(c ->
                new javafx.beans.property.SimpleStringProperty(formatBR(c.getValue().getTotalInvestido())));
        colAcumulado.setCellValueFactory(c ->
                new javafx.beans.property.SimpleStringProperty(formatBR(c.getValue().getTotalAcumulado())));
    }

    @FXML
    public void onCalcularRendimentoClicado() {
        // Validações de formato (RNF 3) — bloqueia o cálculo e exibe alerta.
        BigDecimal capitalInicial = parseValorMonetario(txtCapitalInicial, "Capital Inicial");
        if (capitalInicial == null) return;

        BigDecimal aporteMensal = parseValorMonetario(txtAporteMensal, "Aporte Mensal");
        if (aporteMensal == null) return;

        Integer prazo = parseInteiro(txtPrazo, "Prazo (Meses)");
        if (prazo == null) return;
        if (prazo <= 0) {
            mostrarAlerta(AlertType.WARNING, "Validação", "O prazo deve ser maior que zero.");
            return;
        }

        BigDecimal rentabilidade = parseValorMonetario(txtRentabilidade, "Rentabilidade Mensal (%)");
        if (rentabilidade == null) return;

        try {
            Simulacao novaSimulacao = new Simulacao();
            novaSimulacao.setCapitalInicial(capitalInicial);
            novaSimulacao.setAporteMensal(aporteMensal);
            novaSimulacao.setPrazoMeses(prazo);
            novaSimulacao.setTaxaRentabilidade(rentabilidade.doubleValue());

            Simulacao simulacaoCalculada = simulacaoService.calcularEGravarSimulacao(
                    novaSimulacao, authService.getUsuarioLogado());

            this.simulacaoAtual = simulacaoCalculada;

            lblValorTotalBruto.setText(formatBR(simulacaoCalculada.getResumoResultado().getValorTotalBruto()));
            lblValorInvestido.setText(formatBR(simulacaoCalculada.getResumoResultado().getValorInvestido()));
            lblValorTotalJuros.setText(formatBR(simulacaoCalculada.getResumoResultado().getValorTotalJuros()));
            lblValorPagoIR.setText(formatBR(simulacaoCalculada.getResumoResultado().getValorPagoIR()));
            lblValorTotalLiquido.setText(formatBR(simulacaoCalculada.getResumoResultado().getValorTotalLiquido()));

            ObservableList<ProjecaoMensal> dadosTabela =
                    FXCollections.observableArrayList(simulacaoCalculada.getProjecoesMensais());
            tabelaProjecoes.setItems(dadosTabela);

        } catch (Exception e) {
            mostrarAlerta(AlertType.ERROR, "Erro no cálculo",
                    "Ocorreu um erro ao calcular a simulação: " + e.getMessage());
        }
    }

    @FXML
    public void onExportarDadosClicado() {
        if (simulacaoAtual == null || simulacaoAtual.getProjecoesMensais() == null
                || simulacaoAtual.getProjecoesMensais().isEmpty()) {
            mostrarAlerta(AlertType.INFORMATION, "Exportar",
                    "Nenhuma simulação para exportar. Execute um cálculo primeiro.");
            return;
        }

        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Guardar Exportação");
        fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("Ficheiro CSV", "*.csv"));
        fileChooser.setInitialFileName("SimulaInvest_Exportacao.csv");

        File file = fileChooser.showSaveDialog(null);
        if (file == null) return;

        try (PrintWriter writer = new PrintWriter(file, "UTF-8")) {
            writer.println("Mês,Juros do Mês,Total Investido,Total Acumulado");
            for (ProjecaoMensal p : simulacaoAtual.getProjecoesMensais()) {
                writer.println(p.getMesReferencia() + "," +
                        p.getValorJurosDoMes().toPlainString() + "," +
                        p.getTotalInvestido().toPlainString() + "," +
                        p.getTotalAcumulado().toPlainString());
            }
            mostrarAlerta(AlertType.INFORMATION, "Exportar",
                    "Ficheiro exportado com sucesso para:\n" + file.getAbsolutePath());
        } catch (Exception e) {
            mostrarAlerta(AlertType.ERROR, "Erro ao exportar", e.getMessage());
        }
    }

    // ----- utilitários -----

    private BigDecimal parseValorMonetario(TextField campo, String nome) {
        String texto = campo.getText();
        if (texto == null || texto.isBlank()) {
            mostrarAlerta(AlertType.WARNING, "Validação", "Preencha o campo: " + nome);
            campo.requestFocus();
            return null;
        }
        try {
            return new BigDecimal(texto.replace(".", "").replace(",", "."));
        } catch (NumberFormatException ex) {
            mostrarAlerta(AlertType.ERROR, "Formatação incorreta",
                    "O campo \"" + nome + "\" deve conter apenas números. Valor recebido: " + texto);
            campo.requestFocus();
            return null;
        }
    }

    private Integer parseInteiro(TextField campo, String nome) {
        String texto = campo.getText();
        if (texto == null || texto.isBlank()) {
            mostrarAlerta(AlertType.WARNING, "Validação", "Preencha o campo: " + nome);
            campo.requestFocus();
            return null;
        }
        try {
            return Integer.parseInt(texto.trim());
        } catch (NumberFormatException ex) {
            mostrarAlerta(AlertType.ERROR, "Formatação incorreta",
                    "O campo \"" + nome + "\" deve conter um número inteiro. Valor recebido: " + texto);
            campo.requestFocus();
            return null;
        }
    }

    private String formatBR(BigDecimal valor) {
        if (valor == null) return "R$ 0,00";
        return "R$ " + valor.setScale(2, RoundingMode.HALF_UP)
                .toPlainString().replace(".", ",");
    }

    private void mostrarAlerta(AlertType tipo, String titulo, String mensagem) {
        Alert alert = new Alert(tipo);
        alert.setTitle(titulo);
        alert.setHeaderText(null);
        alert.setContentText(mensagem);
        alert.showAndWait();
    }
}
