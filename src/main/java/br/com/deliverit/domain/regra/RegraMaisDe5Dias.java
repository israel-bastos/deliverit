package br.com.deliverit.domain.regra;

import java.math.BigDecimal;
import java.time.Period;

import br.com.deliverit.domain.Conta;
import br.com.deliverit.domain.Juros;
import br.com.deliverit.domain.Multas;
import br.com.deliverit.domain.RegraDeAtraso;

public class RegraMaisDe5Dias implements RegraDeAtraso {
	
	private Multas multas;
	private Juros juros;
	
	public RegraMaisDe5Dias(Multas multas, Juros juros) {
		this.multas = multas;
		this.juros = juros;
	}
	
	@Override
	public boolean verficarSeEstaEmAtraso(Conta conta) {
		if (conta.getDataVencimento().isAfter(conta.getDataPagamento())) {
			int diasEmAtraso = Period.between(conta.getDataVencimento(), conta.getDataPagamento()).getDays();
			
			if (diasEmAtraso > 3 && diasEmAtraso <= 5) {
				BigDecimal valorDosJuros = conta.getValorDaContaOriginal().multiply(BigDecimal.valueOf(juros.REGRA_DE_JUROS_3));
				BigDecimal valorCorrigdo = valorDosJuros.add(BigDecimal.valueOf(multas.DE_5_PORCENTO));
				
				conta.setValorDaContaCorrigido(valorCorrigdo);
				
				return true;
			}
		}
		
		return false;
	}
}
