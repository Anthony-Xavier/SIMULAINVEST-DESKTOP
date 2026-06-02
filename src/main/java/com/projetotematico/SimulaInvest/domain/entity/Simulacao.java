package com.projetotematico.SimulaInvest.domain.entity;

import com.projetotematico.SimulaInvest.security.BigDecimalCryptoConverter;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "simulacoes")
public class Simulacao {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Convert(converter = BigDecimalCryptoConverter.class)
    @Column(name = "capital_inicial")
    private BigDecimal capitalInicial;

    @Convert(converter = BigDecimalCryptoConverter.class)
    @Column(name = "aporte_mensal")
    private BigDecimal aporteMensal;

    @Column(name = "prazo_meses", nullable = false)
    private Integer prazoMeses;

    @Column(name = "taxa_rentabilidade", nullable = false)
    private Double taxaRentabilidade;

    @Column(name = "tipo_rentabilidade")
    private String tipoRentabilidade;

    @Column(name = "data_simulacao", nullable = false)
    private LocalDateTime dataSimulacao;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "usuario_id")
    private Usuario usuario;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "tipo_investimento_id")
    private TipoInvestimento tipoInvestimento;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "indexador_id")
    private Indexador indexador;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "meta_id")
    private Meta meta;

    @OneToOne(mappedBy = "simulacao", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.EAGER)
    private ResumoResultado resumoResultado;

    @OneToMany(mappedBy = "simulacao", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.EAGER)
    private List<ProjecaoMensal> projecoesMensais = new ArrayList<>();

    @OneToMany(mappedBy = "simulacao", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
    private List<Aporte> aportes = new ArrayList<>();
}
