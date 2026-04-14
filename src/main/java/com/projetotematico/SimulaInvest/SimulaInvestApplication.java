package com.projetotematico.SimulaInvest;

import javafx.application.Application;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class SimulaInvestApplication {

    public static void main(String[] args) {
        // Esta linha é crucial: Desativa o modo headless antes de lançar o JavaFX
        System.setProperty("java.awt.headless", "false");

        // Inicia a aplicação JavaFX (que por sua vez inicia o Spring no init())
        Application.launch(JavaFxApplication.class, args);
    }
}