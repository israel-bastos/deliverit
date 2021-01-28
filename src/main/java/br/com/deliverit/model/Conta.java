package br.com.deliverit.model;

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

@Data
@AllArgsConstructor
@Builder
@Entity
public class Conta {
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	
	@NotEmpty(message = "Campo número da conta é obrigatório.")
	private String numeroDaConta;
	
	@NotEmpty(message = "Campo nome da conta é obrigatório.")
	private String nomeDaConta;
	
	@NotNull(message = "Campo valor da conta é obrigatório.")
	private BigDecimal valorDaConta;
	
	@NotNull(message = "Campo data de vencimento é obrigatório.")
	private LocalDate dataVencimento;
	
	@NotNull(message = "Campo data de pagamento é obrigatório.")
	private LocalDate dataPagamento;
	
}