package br.com.deliverit.domain.regra;

import java.math.BigDecimal;
import java.time.Period;

import br.com.deliverit.domain.Conta;
import br.com.deliverit.domain.Juros;
import br.com.deliverit.domain.Multas;
import br.com.deliverit.domain.RegraDeAtraso;

public class RegraDeAte3Dias implements RegraDeAtraso {

	private Multas multas;
	private Juros juros;
	
	public RegraDeAte3Dias(Multas multas, Juros juros) {
		this.multas = multas;
		this.juros = juros;
	}

	@Override
	public boolean verficarSeEstaEmAtraso(Conta conta) {
		if (conta.getDataVencimento().isAfter(conta.getDataPagamento())) {
			int diasEmAtraso = Period.between(conta.getDataVencimento(), conta.getDataPagamento()).getDays();
			
			if (diasEmAtraso > 0 && diasEmAtraso <= 3) {
				BigDecimal valorDosJuros = conta.getValorDaContaOriginal().multiply(BigDecimal.valueOf(juros.REGRA_DE_JUROS_1));
				BigDecimal valorCorrigdo = valorDosJuros.add(BigDecimal.valueOf(multas.DE_2_PORCENTOS));
				
				conta.setValorDaContaCorrigido(valorCorrigdo);
				
				return true;
			}
		}
		
		return false;
	}
}
