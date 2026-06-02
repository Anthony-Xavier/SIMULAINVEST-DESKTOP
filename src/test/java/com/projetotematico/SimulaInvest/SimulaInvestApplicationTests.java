package com.projetotematico.SimulaInvest;

import org.junit.jupiter.api.Test;

/**
 * Teste smoke. Não usamos @SpringBootTest aqui para evitar
 * subir o JavaFX em ambiente headless de CI.
 * Os testes reais ficam em /service e /security.
 */
class SimulaInvestApplicationTests {

    @Test
    void smoke() {
        // garante que o pacote compila
    }
}
