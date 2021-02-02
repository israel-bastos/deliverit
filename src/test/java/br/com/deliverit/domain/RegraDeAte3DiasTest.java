package br.com.deliverit.domain;

import java.math.BigDecimal;
import java.time.LocalDate;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
public class RegraDeAte3DiasTest {
	
	@Autowired
	private Multas multas;
	
	@Autowired
	private Juros juros;
	
	@Autowired
	private RegraDeAtraso regra;
	
	private Conta create() {
		var created = Conta.builder()
				.nomeDaConta("Conta de Internet")
				.valorDaContaOriginal(new BigDecimal(100.0))
        		.valorDaContaCorrigido(new BigDecimal(0.0))
        		.dataVencimento(LocalDate.now().plusDays(30))
        		.dataPagamento(LocalDate.now())
				.build();
		
		return created;
	}
	
	@Test
	public void verficarSeEstaEmAtrasoTest(Conta conta) {
		
		
	}
}
