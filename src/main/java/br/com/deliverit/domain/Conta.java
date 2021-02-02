package br.com.deliverit.domain;

import java.math.BigDecimal;
import java.time.LocalDate;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Entity
public class Conta {
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	
	@NotEmpty(message = "Campo nome da conta é obrigatório.")
	private String nomeDaConta;
	
	@NotNull(message = "Campo valor da conta original é obrigatório.")
	private BigDecimal valorDaContaOriginal;
	
	@NotNull(message = "Campo valor da conta corrigido é obrigatório.")
	private BigDecimal valorDaContaCorrigido;
	
	@NotNull(message = "Campo data de vencimento é obrigatório.")
	private LocalDate dataVencimento;
	
	@NotNull(message = "Campo data de pagamento é obrigatório.")
	private LocalDate dataPagamento;
	
	@Data
	@Entity
	class DadosDeRegra {
		
		@Id
		private long id;
		
		private int quantidadeDeDiasEmAtraso;
		private String regraAplicada;
		
	}
}