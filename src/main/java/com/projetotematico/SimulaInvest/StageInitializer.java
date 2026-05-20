package com.projetotematico.SimulaInvest;

import com.projetotematico.SimulaInvest.ui.TelaNavigator;
import org.springframework.context.ApplicationListener;
import org.springframework.stereotype.Component;

@Component
public class StageInitializer implements ApplicationListener<StageReadyEvent> {

    private final TelaNavigator telaNavigator;

    public StageInitializer(TelaNavigator telaNavigator) {
        this.telaNavigator = telaNavigator;
    }

    @Override
    public void onApplicationEvent(StageReadyEvent event) {
        telaNavigator.setStagePrincipal(event.getStage());
        // Inicia pela tela de login (MVP — fluxo de autenticação)
        telaNavigator.abrirLogin();
    }
}
