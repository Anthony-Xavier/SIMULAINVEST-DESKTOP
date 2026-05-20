package com.projetotematico.SimulaInvest.domain.entity;

import com.projetotematico.SimulaInvest.security.BigDecimalCryptoConverter;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;

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

    @Column(name = "mes_referencia", nullable = false)
    private Integer mesReferencia;

    @Convert(converter = BigDecimalCryptoConverter.class)
    private BigDecimal valorJurosDoMes;

    @Convert(converter = BigDecimalCryptoConverter.class)
    private BigDecimal totalInvestido;

    @Convert(converter = BigDecimalCryptoConverter.class)
    private BigDecimal totalJurosAcumulado;

    @Convert(converter = BigDecimalCryptoConverter.class)
    private BigDecimal totalAcumulado;

    @ManyToOne
    @JoinColumn(name = "simulacao_id")
    private Simulacao simulacao;
}
