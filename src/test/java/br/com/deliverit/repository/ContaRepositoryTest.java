package br.com.deliverit.repository;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Optional;

import javax.validation.ConstraintViolationException;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

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
		
		Assertions.assertThat(created).isNotNull();
        Assertions.assertThat(created.getId()).isNotNull();
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
		
		Assertions.assertThatExceptionOfType(ConstraintViolationException.class)
				.isThrownBy(() -> this.repository.save(created))
				.withMessageContaining("Campo nome da conta é obrigatório.");
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
		
		Assertions.assertThatExceptionOfType(ConstraintViolationException.class)
				.isThrownBy(() -> this.repository.save(created))
				.withMessageContaining("Campo nome da conta é obrigatório.");
	}
	
	@Test
	void updateTest() {
		var created = create();
		String tempNomeDaConta = created.getNomeDaConta();
		
		created.setNomeDaConta("Conta de internet");
		
		var updated = this.repository.save(created);
		
		Assertions.assertThat(updated).isNotNull();
        Assertions.assertThat(updated.getId()).isNotNull();
        Assertions.assertThat(updated.getNomeDaConta()).isNotEqualTo(tempNomeDaConta);
	}
	
	@Test
	void deleteTest() {
		var created = create();
		this.repository.delete(created);
		
		Optional<Conta> contaDeleted = this.repository.findById(created.getId());
        Assertions.assertThat(contaDeleted).isEmpty();
	}
	
	@Test
	void findByIdTest() {
		var created = create();
		var finded = this.repository.findById(created.getId());
		
		Assertions.assertThat(finded).isNotNull();
		Assertions.assertThat(created).isNotNull().isEqualTo(finded.get());
	}
	
	@Test
	void findByIdUnknownTest() {
		long idUnknown = 1234L;
		Optional<Conta> unknowns = this.repository.findById(idUnknown);
		
		 Assertions.assertThat(unknowns).isEmpty();
	}
	
	@Test
	void findAllTest() {
		var createdList = new ArrayList<>();
		
		for (int i = 0; i < 3; i++) {
			Conta created = create();
			createdList.add(created);
		}
		
		Assertions.assertThat(createdList).isNotNull().size().isEqualTo(this.repository.findAll().size());
	}
}