package br.com.deliverit.repository;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
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
		Assertions.assertThrows(InvalidDataAccessApiUsageException.class, () -> this.repository.save(null));
	}
	
	@Test
	void saveWithNullNomeDaContaTest() {
		var created = Conta.builder()
				.numeroDaConta("01")
        		.nomeDaConta(null)
        		.valorDaConta(new BigDecimal(100.0))
        		.dataVencimento(LocalDate.now().plusDays(30))
        		.dataPagamento(LocalDate.now())
        		.build();
		
		ConstraintViolationException exception = Assertions.assertThrows(
				ConstraintViolationException.class,
		           () -> this.repository.save(created));

		Assertions.assertTrue(exception.getConstraintViolations().toString().contains("Campo nome da conta é obrigatório."));
	}
	
	@Test
	void saveWithEmptyNumeroDaContaTest() {
		var created = Conta.builder()
				.numeroDaConta("")
        		.nomeDaConta("Conta de Internet")
        		.valorDaConta(new BigDecimal(100.0))
        		.dataVencimento(LocalDate.now().plusDays(30))
        		.dataPagamento(LocalDate.now())
        		.build();
		
		ConstraintViolationException thrown = Assertions.assertThrows(
				ConstraintViolationException.class,
		           () -> this.repository.save(created));

		Assertions.assertTrue(thrown.getConstraintViolations().toString().contains("Campo número da conta é obrigatório."));
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
}