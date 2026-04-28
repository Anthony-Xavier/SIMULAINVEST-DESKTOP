package com.projetotematico.SimulaInvest.domain.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "projecoes_mensais")
public class ProjecaoMensal {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Integer mesReferencia; // 1, 2, 3...

    private String valorJurosDoMes;

    private String totalInvestido; // Aportes somados

    private String totalJurosAcumulado;

    private String totalAcumulado; // Montante final do mês

    @ManyToOne
    @JoinColumn(name = "simulacao_id")
    private Simulacao simulacao;

}