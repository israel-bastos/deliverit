package br.com.deliverit.repository;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import javax.validation.ConstraintViolationException;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.dao.InvalidDataAccessApiUsageException;

import br.com.deliverit.domain.Conta;

@DataJpaTest
class ContaRepositoryTest {
	
	@Autowired
	private ContaRepository repository;
	
	private Conta create() {
		var created = Conta.builder()
				.nomeDaConta("Conta de Internet")
				.valorDaContaOriginal(new BigDecimal(100.0))
        		.valorDaContaCorrigido(new BigDecimal(0.0))
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
		Assertions.assertThrows(InvalidDataAccessApiUsageException.class, () -> this.repository.save(null));
	}
	
	@Test
	void saveWithNullNomeDaContaTest() {
		var created = Conta.builder()
				.nomeDaConta(null)
				.valorDaContaOriginal(new BigDecimal(100.0))
        		.valorDaContaCorrigido(new BigDecimal(0.0))
        		.dataVencimento(LocalDate.now().plusDays(30))
        		.dataPagamento(LocalDate.now())
				.build();
		
		ConstraintViolationException exception = Assertions.assertThrows(
				ConstraintViolationException.class,
		           () -> this.repository.save(created));

		Assertions.assertTrue(exception.getConstraintViolations().toString().contains("Campo nome da conta é obrigatório."));
	}
	
	@Test
	void saveWithEmptyNomeDaContaTest() {
		var created = Conta.builder()
				.nomeDaConta("")
				.valorDaContaOriginal(new BigDecimal(100.0))
        		.valorDaContaCorrigido(new BigDecimal(0.0))
        		.dataVencimento(LocalDate.now().plusDays(30))
        		.dataPagamento(LocalDate.now())
				.build();
		
		ConstraintViolationException thrown = Assertions.assertThrows(
				ConstraintViolationException.class,
		           () -> this.repository.save(created));

		Assertions.assertTrue(thrown.getConstraintViolations().toString().contains("Campo nome da conta é obrigatório."));
	}
	
	@Test
	void updateTest() {
		var created = create();
		String tempNomeDaConta = created.getNomeDaConta();
		
		created.setNomeDaConta("Conta de internet");
		
		var updated = this.repository.save(created);
		
		Assertions.assertNotNull(updated.getId());
		Assertions.assertEquals(created.getId(), updated.getId());
		Assertions.assertNotEquals(tempNomeDaConta, updated.getNomeDaConta());
	}
	
	@Test
	void deleteTest() {
		var created = create();
		this.repository.delete(created);
		
		var deleted = this.repository.findById(created.getId());
		Assertions.assertTrue(deleted.isEmpty());
		Assertions.assertThrows(InvalidDataAccessApiUsageException.class, () -> this.repository.delete(null));
	}
	
	@Test
	void findByIdTest() {
		var created = create();
		var finded = this.repository.findById(created.getId());
		
		Assertions.assertNotNull(finded);
		Assertions.assertThrows(InvalidDataAccessApiUsageException.class, () -> this.repository.findById(null));
	}
	
	@Test
	void findByIdWithNullIdTest() {
		Assertions.assertThrows(InvalidDataAccessApiUsageException.class, () -> this.repository.findById(null));
	}
	
	@Test
	void findByIdUnknownTest() {
		long idUnknown = 1234L;
		Optional<Conta> unknown = this.repository.findById(idUnknown);
		
		Assertions.assertTrue(unknown.isEmpty());
	}
	
	@Test
	void findAllTest() {
		var createdList = new ArrayList<>();
		
		for (int i = 0; i < 3; i++) {
			Conta created = create();
			createdList.add(created);
		}
		
		Assertions.assertEquals(createdList.size(), this.repository.findAll().size());
	}
	
	@Test
	void findByNumeroDaContaTest() {
		List<Conta> createdList = new ArrayList<>();
		
		for (int i = 0; i < 3; i++) {
			var created = Conta.builder()
					.nomeDaConta("Conta de Internet")
					.valorDaContaOriginal(new BigDecimal(100.0))
	        		.valorDaContaCorrigido(new BigDecimal(0.0))
	        		.dataVencimento(LocalDate.now().plusDays(30))
	        		.dataPagamento(LocalDate.now())
					.build();
			
			this.repository.save(created);
			
			createdList.add(created);
		}
		
		var finded = this.repository.findByNomeDaConta(createdList.get(0).getNomeDaConta()).stream()
				.map(conta -> conta.getNomeDaConta()).collect(Collectors.toList());
		
		Assertions.assertNotNull(finded);
		Assertions.assertEquals(createdList.get(0).getNomeDaConta(), finded.iterator().next());
	}
}