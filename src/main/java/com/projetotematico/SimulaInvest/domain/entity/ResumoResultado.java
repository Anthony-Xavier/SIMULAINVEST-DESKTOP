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
@Table(name = "resumos_resultado")
public class ResumoResultado {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // Relacionamento 1 para 1: cada simulação tem exatamente 1 resumo
    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "simulacao_id", unique = true)
    private Simulacao simulacao;

    @Convert(converter = BigDecimalCryptoConverter.class)
    private BigDecimal valorTotalBruto;

    @Convert(converter = BigDecimalCryptoConverter.class)
    private BigDecimal valorInvestido;

    @Convert(converter = BigDecimalCryptoConverter.class)
    private BigDecimal valorTotalJuros;

    @Convert(converter = BigDecimalCryptoConverter.class)
    private BigDecimal valorPagoIR;

    @Convert(converter = BigDecimalCryptoConverter.class)
    private BigDecimal valorTotalLiquido;
}
