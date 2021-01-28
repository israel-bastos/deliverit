package br.com.deliverit.repository;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import br.com.deliverit.model.Conta;

@DataJpaTest
class ContaRepositoryTest {
	
	@Autowired
	private ContaRepository repository;
	
	private Conta create() {
		var created = Conta.builder()
				.numeroDaConta("01")
        		.nomeDaConta("Conta de Luz")
        		.valorDaConta(new BigDecimal(100.0))
        		.dataVencimento(LocalDate.now().plusDays(30))
        		.dataPagamento(LocalDate.now())
        		.build();
		
		return this.repository.save(created);
	}
	
	@Test
	void saveTest() {
		var created = create();
		
		Assertions.assertNotNull(created);
		Assertions.assertNotNull(created.getId());
	}
	
	@Test
	void updateTest() {
		var created = create();
		var whenCreated = LocalDateTime.now();
		
		created.setNomeDaConta("Conta de internet");
		
		var saved = this.repository.save(created);
		var whenUpdated = LocalDateTime.now();
		
		Assertions.assertEquals(saved.getId(), saved.getId());
		Assertions.assertEquals(created.getNomeDaConta(), saved.getNomeDaConta());
		Assertions.assertNotEquals(whenCreated, whenUpdated);
	}
	
	@Test
	void deleteTest() {
		var created = create();
		this.repository.delete(created);
		
		var deleted = this.repository.findById(created.getId());
		Assertions.assertTrue(deleted.isEmpty());
	}
	
	@Test
	void findByIdTest() {
		var created = create();
		var finded = this.repository.findById(created.getId());
		
		Assertions.assertNotNull(finded);
	}
	
	@Test
	void findAllTest() {
		var createdList = new ArrayList<>();
		
		for (int i = 0; i < 3; i++) {
			Conta created = create();
			createdList.add(created);
		}
		
		Assertions.assertEquals(3, createdList.size());
	}
	
	@Test
	void findByNumeroDaContaTest() {
		List<Conta> createdList = new ArrayList<>();
		
		for (int i = 0; i < 3; i++) {
			var created = Conta.builder()
					.numeroDaConta(String.valueOf(i))
	        		.nomeDaConta("Conta de Luz")
	        		.valorDaConta(new BigDecimal(100.0))
	        		.dataVencimento(LocalDate.now().plusDays(30))
	        		.dataPagamento(LocalDate.now())
	        		.build();
			
			this.repository.save(created);
			
			createdList.add(created);
		}
		
		var finded = this.repository.findByNumeroDaConta(createdList.get(0).getNumeroDaConta()).stream()
				.map(conta -> conta.getNumeroDaConta()).collect(Collectors.toList());
		
		Assertions.assertNotNull(finded);
		Assertions.assertEquals(createdList.get(0).getNumeroDaConta(), finded.iterator().next());
	}
	
	// Se quiser amanhã incluir testes para gerar erros
}