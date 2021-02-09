package br.com.deliverit.service;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentMatchers;
import org.mockito.BDDMockito;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import br.com.deliverit.handler.exception.NotFoundException;
import br.com.deliverit.model.Conta;
import br.com.deliverit.repository.ContaRepository;

@ExtendWith(SpringExtension.class)
public class ContaServiceTest {
	
	@InjectMocks
    private ContaService contaService;
    
	@Mock
    private ContaRepository contaRepositoryMock;

    @BeforeEach
    void setUp(){
        BDDMockito.when(contaRepositoryMock.findAll(ArgumentMatchers.any(PageRequest.class)))
        .thenReturn(new PageImpl<>(List.of(
        		Conta.builder()
        		.nomeDaConta("Conta de Internet")
        		.valorDaContaOriginal(new BigDecimal(100.0))
        		.valorDaContaCorrigido(null)
        		.dataVencimento(LocalDate.now().plusDays(30))
        		.dataPagamento(LocalDate.now())
        		.build()
        )));
        
		BDDMockito.when(contaRepositoryMock.findAll())
		.thenReturn(List.of(Conta.builder()
				.nomeDaConta("Conta de Luz")
				.valorDaContaOriginal(new BigDecimal(100.0))
        		.valorDaContaCorrigido(null)
        		.dataVencimento(LocalDate.now().plusDays(30))
        		.dataPagamento(LocalDate.now())
				.build()));
        
        BDDMockito.when(contaRepositoryMock.findById(ArgumentMatchers.anyLong()))
        .thenReturn(Optional.of(Conta.builder()
				.id(1L)
				.nomeDaConta("Conta de Luz")
				.valorDaContaOriginal(new BigDecimal(100.0))
        		.valorDaContaCorrigido(null)
        		.dataVencimento(LocalDate.now().plusDays(30))
        		.dataPagamento(LocalDate.now())
				.build()));
        
        BDDMockito.when(contaRepositoryMock.findByNomeDaConta(ArgumentMatchers.anyString()))
        .thenReturn(List.of(Conta.builder()
        		.nomeDaConta("Conta de Luz")
				.valorDaContaOriginal(new BigDecimal(100.0))
        		.valorDaContaCorrigido(null)
        		.dataVencimento(LocalDate.now().plusDays(30))
        		.dataPagamento(LocalDate.now())
				.build()));

        BDDMockito.when(contaRepositoryMock.save(ArgumentMatchers.any(Conta.class)))
        .thenReturn(Conta.builder()
				.id(1L)
				.nomeDaConta("Conta de Luz")
				.valorDaContaOriginal(new BigDecimal(100.0))
        		.valorDaContaCorrigido(null)
        		.dataVencimento(LocalDate.now().plusDays(30))
        		.dataPagamento(LocalDate.now())
				.build());

		BDDMockito.doNothing().when(contaRepositoryMock).delete(ArgumentMatchers.any(Conta.class));
    }
    
    @Test
    void findAllWithPageableContasTest(){
    	var created = Conta.builder()
				.nomeDaConta("Conta de Internet")
				.valorDaContaOriginal(new BigDecimal(100.0))
        		.valorDaContaCorrigido(null)
        		.dataVencimento(LocalDate.now().plusDays(30))
        		.dataPagamento(LocalDate.now())
				.build();
    	
        String nomeDaConta = created.getNomeDaConta();

        Page<Conta> contaPage = contaService.findAll(PageRequest.of(1, 1));

        Assertions.assertTrue(!contaPage.isEmpty() || contaPage != null );
        Assertions.assertEquals(contaPage.toList().get(0).getNomeDaConta(), nomeDaConta);
    }
    
    @Test
    void findAllFullListOfContasTest(){
    	var created = Conta.builder()
				.nomeDaConta("Conta de Luz")
				.valorDaContaOriginal(new BigDecimal(100.0))
        		.valorDaContaCorrigido(null)
        		.dataVencimento(LocalDate.now().plusDays(30))
        		.dataPagamento(LocalDate.now())
				.build();
    	
		String nomeDaConta = created.getNomeDaConta();
        List<Conta> contas = contaService.findAllNonPageable();
		
        Assertions.assertTrue(!contas.isEmpty() || contas != null);
        Assertions.assertEquals(contas.get(0).getNomeDaConta(), nomeDaConta);
    }
    
    @Test
    void findByIdTest(){
        Long findedId = Conta.builder()
        		.id(1L)
        		.nomeDaConta("Conta de Luz")
				.valorDaContaOriginal(new BigDecimal(100.0))
        		.valorDaContaCorrigido(null)
        		.dataVencimento(LocalDate.now().plusDays(30))
        		.dataPagamento(LocalDate.now())
				.build().getId();

        Conta conta = contaService.findById(1);

        Assertions.assertNotEquals(null, conta);
        Assertions.assertEquals(conta.getId(), findedId);
    }
    
    @Test
    void findByIdWhenNotFoundTest(){
    	 BDDMockito.when(contaRepositoryMock.findById(ArgumentMatchers.anyLong()))
         .thenReturn(Optional.empty());
        		 
        Assertions.assertThrows(NotFoundException.class, () -> contaService.findById(1), "Conta não encontrada.");
    }

    @Test
    void findByNumeroDaContaTest(){
        String findedNomeDaConta = Conta.builder()
        		.nomeDaConta("Conta de Luz")
				.valorDaContaOriginal(new BigDecimal(100.0))
        		.valorDaContaCorrigido(null)
        		.dataVencimento(LocalDate.now().plusDays(30))
        		.dataPagamento(LocalDate.now())
				.build().getNomeDaConta();

        List<Conta> contas = contaService.findByNomeDaConta("03");

        Assertions.assertNotEquals(null, contas);
        Assertions.assertTrue(!contas.isEmpty());
        Assertions.assertEquals(contas.get(0).getNomeDaConta(), findedNomeDaConta);
    }

    @Test
    void findByNumeroDaContaWhenNotFoundTest(){
        BDDMockito.when(contaRepositoryMock.findByNomeDaConta(ArgumentMatchers.anyString()))
                .thenReturn(Collections.emptyList());

        List<Conta> contas = contaService.findByNomeDaConta("Conta de Cartão de Crédito");

        Assertions.assertNotEquals(null, contas);
        Assertions.assertTrue(contas.isEmpty());
    }

    @Test
    void saveTest(){
        var saved = contaService.save(Conta.builder()
        		.id(1L)
        		.nomeDaConta("Conta de Luz")
				.valorDaContaOriginal(new BigDecimal(100.0))
        		.valorDaContaCorrigido(null)
        		.dataVencimento(LocalDate.now().plusDays(30))
        		.dataPagamento(LocalDate.now())
				.build());
        
        var conta = contaService.findById(1);
        
        Assertions.assertNotEquals(null, conta);
        Assertions.assertEquals(conta.getId(), saved.getId());
    }

    @Test
    void updateTest(){
        var created = contaService.save(Conta.builder()
        		.id(1L)
        		.nomeDaConta("Conta de Luz")
				.valorDaContaOriginal(new BigDecimal(100.0))
        		.valorDaContaCorrigido(null)
        		.dataVencimento(LocalDate.now().plusDays(30))
        		.dataPagamento(LocalDate.now())
				.build());
        
        var findedConta = contaService.findById(created.getId());
        findedConta.setNomeDaConta("Conta cartão de crédito");
        
        contaService.update(findedConta);
        
        Assertions.assertEquals(created.getId(), findedConta.getId());
        Assertions.assertNotEquals(created.getNomeDaConta(), findedConta.getNomeDaConta());
    }

    @Test
    void deleteTest(){
    	Assertions.assertDoesNotThrow(() -> {
    		contaService.delete(1L);
    	});
    }
}