package br.com.deliverit.controller;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Collections;
import java.util.List;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentMatchers;
import org.mockito.BDDMockito;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import br.com.deliverit.model.Conta;
import br.com.deliverit.service.ContaService;

@ExtendWith(SpringExtension.class)
class ContaControllerTest {
    
	@InjectMocks
    private ContaController contaController;
    
	@Mock
    private ContaService contaServiceMock;

    @BeforeEach
    void setUp(){
        BDDMockito.when(contaServiceMock.findAll(ArgumentMatchers.any())).thenReturn(new PageImpl<>(List.of(
        		Conta.builder()
        		.nomeDaConta("Conta de Internet")
        		.valorDaContaOriginal(new BigDecimal(100.0))
        		.valorDaContaCorrigido(new BigDecimal(0.0))
        		.dataVencimento(LocalDate.now().plusDays(30))
        		.dataPagamento(LocalDate.now())
        		.build()
        )));
        
        BDDMockito.when(contaServiceMock.findAllNonPageable()).thenReturn(List.of(Conta.builder()
				.nomeDaConta("Conta de Luz")
				.valorDaContaOriginal(new BigDecimal(100.0))
        		.valorDaContaCorrigido(new BigDecimal(0.0))
        		.dataVencimento(LocalDate.now().plusDays(30))
        		.dataPagamento(LocalDate.now())
				.build()));
        
        BDDMockito.when(contaServiceMock.findById(ArgumentMatchers.anyLong())).thenReturn(Conta.builder()
				.id(1L)
				.nomeDaConta("Conta de Luz")
				.valorDaContaOriginal(new BigDecimal(100.0))
        		.valorDaContaCorrigido(new BigDecimal(0.0))
        		.dataVencimento(LocalDate.now().plusDays(30))
        		.dataPagamento(LocalDate.now())
				.build());
        
        BDDMockito.when(contaServiceMock.findByNomeDaConta(ArgumentMatchers.anyString())).thenReturn(List.of(Conta.builder()
        		.nomeDaConta("Conta de Luz")
				.valorDaContaOriginal(new BigDecimal(100.0))
        		.valorDaContaCorrigido(new BigDecimal(0.0))
        		.dataVencimento(LocalDate.now().plusDays(30))
        		.dataPagamento(LocalDate.now())
				.build()));

        BDDMockito.when(contaServiceMock.save(ArgumentMatchers.any(Conta.class))).thenReturn(Conta.builder()
				.id(1L)
				.nomeDaConta("Conta de Luz")
				.valorDaContaOriginal(new BigDecimal(100.0))
        		.valorDaContaCorrigido(new BigDecimal(0.0))
        		.dataVencimento(LocalDate.now().plusDays(30))
        		.dataPagamento(LocalDate.now())
				.build());

		BDDMockito.doNothing().when(contaServiceMock).update(ArgumentMatchers.any(Conta.class));

		BDDMockito.doNothing().when(contaServiceMock).delete(ArgumentMatchers.anyLong());
    }
    
    @Test
    void findAllWithPageableContasTest(){
		var created = Conta.builder()
				.nomeDaConta("Conta de Internet")
				.valorDaContaOriginal(new BigDecimal(100.0))
        		.valorDaContaCorrigido(new BigDecimal(0.0))
        		.dataVencimento(LocalDate.now().plusDays(30))
        		.dataPagamento(LocalDate.now())
				.build();
    	
        String nomeDaConta = created.getNomeDaConta();

        Page<Conta> contaPage = contaController.findAll(null).getBody();
        
        Assertions.assertThat(contaPage).isNotNull();
        Assertions.assertThat(contaPage.toList()).isNotEmpty().hasSize(1);
        Assertions.assertThat(contaPage.toList().get(0).getNomeDaConta()).isEqualTo(nomeDaConta);

    }
    
    @Test
    void findAllFullListOfContasTest(){
    	List<Conta> contas = contaController.findAllNonPageable().getBody();
        
        Assertions.assertThat(contas).isNotNull().isNotEmpty().hasSize(1);
        Assertions.assertThat(contas.get(0).getNomeDaConta()).isNotNull();
		
    }
    
    @Test
    void findByIdTest(){
        Long findedId = Conta.builder()
        		.id(1L)
        		.nomeDaConta("Conta de Luz")
				.valorDaContaOriginal(new BigDecimal(100.0))
        		.valorDaContaCorrigido(new BigDecimal(0.0))
        		.dataVencimento(LocalDate.now().plusDays(30))
        		.dataPagamento(LocalDate.now())
				.build().getId();

        Conta conta = contaController.findById(1).getBody();
        
        Assertions.assertThat(findedId).isNotNull();
        Assertions.assertThat(findedId).isEqualTo(conta.getId());
    }

    @Test
    void findByNumeroDaContaTest(){
        String findedNomeDaConta = Conta.builder()
        		.nomeDaConta("Conta de Luz")
				.valorDaContaOriginal(new BigDecimal(100.0))
        		.valorDaContaCorrigido(new BigDecimal(0.0))
        		.dataVencimento(LocalDate.now().plusDays(30))
        		.dataPagamento(LocalDate.now())
				.build().getNomeDaConta();

        List<Conta> contas = contaController.findByNomeDaConta("conta").getBody();
        
        Assertions.assertThat(findedNomeDaConta).isNotNull().isNotEmpty();
        Assertions.assertThat(findedNomeDaConta).isEqualTo(contas.get(0).getNomeDaConta());
    }

    @Test
    void findByNumeroDaContaWhenNotFoundTest(){
        BDDMockito.when(contaServiceMock.findByNomeDaConta(ArgumentMatchers.anyString()))
                .thenReturn(Collections.emptyList());

        List<Conta> contas = contaController.findByNomeDaConta("Conta de celular").getBody();
        
        Assertions.assertThat(contas).isNotNull().isEmpty();
    }

    @Test
    void saveTest(){
        var saved = contaController.save(Conta.builder()
        		.id(1L)
        		.nomeDaConta("Conta de Luz")
				.valorDaContaOriginal(new BigDecimal(100.0))
        		.valorDaContaCorrigido(new BigDecimal(0.0))
        		.dataVencimento(LocalDate.now().plusDays(30))
        		.dataPagamento(LocalDate.now())
				.build());
        
        Conta conta = contaController.findById(1).getBody();
        
        Assertions.assertThat(saved.getBody()).isNotNull().isEqualTo(conta);
        Assertions.assertThat(saved.getStatusCode()).isEqualTo(HttpStatus.CREATED);
    }

    @Test
    void updateTest(){
        var created = contaController.save(Conta.builder()
        		.id(1L)
        		.nomeDaConta("Conta de Luz")
				.valorDaContaOriginal(new BigDecimal(100.0))
        		.valorDaContaCorrigido(new BigDecimal(0.0))
        		.dataVencimento(LocalDate.now().plusDays(30))
        		.dataPagamento(LocalDate.now())
				.build());
        
        
        var findedConta = contaController.findById(created.getBody().getId()).getBody();
        findedConta.setNomeDaConta("Conta De Cartão de Crédito");
        
        ResponseEntity<Void> updatedConta = contaController.update(findedConta);
        
        Assertions.assertThat(created.getBody().getId()).isEqualByComparingTo(findedConta.getId());
        Assertions.assertThat(updatedConta).isNotNull();
        Assertions.assertThat(updatedConta.getStatusCode()).isEqualTo(HttpStatus.NO_CONTENT);
    }

    @Test
    void deleteTest(){
    	ResponseEntity<Void> deletedConta = contaController.delete(1);
    	
    	Assertions.assertThat(deletedConta).isNotNull();
        Assertions.assertThat(deletedConta.getStatusCode()).isEqualTo(HttpStatus.NO_CONTENT);
    }
}