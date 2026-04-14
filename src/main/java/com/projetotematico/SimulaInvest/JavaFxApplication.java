package com.projetotematico.SimulaInvest;

import javafx.application.Application;
import javafx.application.Platform;
import javafx.stage.Stage;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.context.ConfigurableApplicationContext;

public class JavaFxApplication extends Application {

    private ConfigurableApplicationContext springContext;

    @Override
    public void init() {
        // Inicia o contexto do Spring Boot em background
        springContext = new SpringApplicationBuilder(SimulaInvestApplication.class).run();
    }

    @Override
    public void start(Stage primaryStage) throws Exception {
        // Aqui você carregará a sua primeira tela FXML (ex: Tela de Login do MVP)
        // E passará o springContext para o FXMLLoader, permitindo a injeção de dependências.

        // Exemplo simplificado de emissão de evento de inicialização de UI:
        springContext.publishEvent(new StageReadyEvent(primaryStage));
    }

    @Override
    public void stop() {
        // Garante que o Spring feche corretamente quando a janela do JavaFX for fechada
        springContext.close();
        Platform.exit();
    }
}