package com.projetotematico.SimulaInvest.domain.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "simulaceoes")
public class Simulacao {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long Id;

    private String capitalInicial;

    private String aporteMensal;

    private Integer prazoMeses;
    private Double taxaRentabilidade;
    private String tipoRentabilidade;
    private LocalDateTime dataSimulacao;

    @ManyToOne
    @JoinColumn(name = "tipo_investimento_id")
    private TipoInvestimento tipoInvestimento;

    @ManyToOne
    @JoinColumn(name = "indexador_id")
    private Indexador indexador;

    // Ligação com o Resumo Final (Cards)
    @OneToOne(mappedBy = "simulacao", cascade = CascadeType.ALL)
    private ResumoResultado resumoResultado;

    // Ligação com a Tabela de Projeção Mês a Mês
    @OneToMany(mappedBy = "simulacao", cascade = CascadeType.ALL)
    private List<ProjecaoMensal> projecoesMensais;
}
