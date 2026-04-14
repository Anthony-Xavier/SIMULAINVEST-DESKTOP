package com.projetotematico.SimulaInvest;

import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationListener;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Component // Diz ao Spring para gerir esta classe
public class StageInitializer implements ApplicationListener<StageReadyEvent> {

    private final ApplicationContext applicationContext;

    // Injeta o contexto do Spring para podermos ligá-lo ao JavaFX
    public StageInitializer(ApplicationContext applicationContext) {
        this.applicationContext = applicationContext;
    }

    @Override
    public void onApplicationEvent(StageReadyEvent event) {
        try {
            // Aponta para o ficheiro FXML que a Eduarda vai desenhar
            ClassPathResource fxml = new ClassPathResource("/fxml/tela.fxml");
            FXMLLoader fxmlLoader = new FXMLLoader(fxml.getURL());

            // O SEGredo: Diz ao JavaFX para usar o Spring para criar os Controllers!
            fxmlLoader.setControllerFactory(applicationContext::getBean);

            Parent parent = fxmlLoader.load();
            Stage stage = event.getStage();
            stage.setScene(new Scene(parent, 800, 600)); // Tamanho inicial da janela
            stage.setTitle("SimulaInvest Desktop - MVP");
            stage.show(); // Exibe a janela!

        } catch (IOException e) {
            throw new RuntimeException("Erro ao carregar o ecrã inicial", e);
        }
    }
}