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
        springContext.publishEvent(new StageReadyEvent(primaryStage));
    }

    @Override
    public void stop() {
        springContext.close();
        Platform.exit();
    }
}