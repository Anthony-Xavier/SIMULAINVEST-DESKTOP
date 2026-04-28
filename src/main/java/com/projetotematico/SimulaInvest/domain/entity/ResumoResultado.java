package com.projetotematico.SimulaInvest.domain.entity;

import jakarta.persistence.*;

@Entity
@Table(name = "resumos_resultado")
public class ResumoResultado {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // Relacionamento 1 para 1: Cada simulação tem exatamente 1 resumo
    @OneToOne
    @JoinColumn(name = "simulacao_id")
    private Simulacao simulacao;

    private String valorTotalBruto;

    private String valorInvestido;

    private String valorTotalJuros;

    private String valorPagoIR;

    private String valorTotalLiquido;

}