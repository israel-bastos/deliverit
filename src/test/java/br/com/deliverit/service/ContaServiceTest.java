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
				.numeroDaConta("01")
        		.nomeDaConta("Conta de Internet")
        		.valorDaConta(new BigDecimal(100.0))
        		.dataVencimento(LocalDate.now().plusDays(30))
        		.dataPagamento(LocalDate.now())
        		.build()
        )));
        
		BDDMockito.when(contaRepositoryMock.findAll())
		.thenReturn(List.of(Conta.builder()
				.numeroDaConta("01")
				.nomeDaConta("Conta de Luz")
				.valorDaConta(new BigDecimal(100.0))
				.dataVencimento(LocalDate.now().plusDays(30))
				.dataPagamento(LocalDate.now())
				.build()));
        
        BDDMockito.when(contaRepositoryMock.findById(ArgumentMatchers.anyLong()))
        .thenReturn(Optional.of(Conta.builder()
				.id(1L)
        		.numeroDaConta("01")
				.nomeDaConta("Conta de Luz")
				.valorDaConta(new BigDecimal(100.0))
				.dataVencimento(LocalDate.now().plusDays(30))
				.dataPagamento(LocalDate.now())
				.build()));
        
        BDDMockito.when(contaRepositoryMock.findByNumeroDaConta(ArgumentMatchers.anyString()))
        .thenReturn(List.of(Conta.builder()
				.numeroDaConta("01")
				.nomeDaConta("Conta de Luz")
				.valorDaConta(new BigDecimal(100.0))
				.dataVencimento(LocalDate.now().plusDays(30))
				.dataPagamento(LocalDate.now())
				.build()));

        BDDMockito.when(contaRepositoryMock.save(ArgumentMatchers.any(Conta.class)))
        .thenReturn(Conta.builder()
				.id(1L)
        		.numeroDaConta("01")
				.nomeDaConta("Conta de Luz")
				.valorDaConta(new BigDecimal(100.0))
				.dataVencimento(LocalDate.now().plusDays(30))
				.dataPagamento(LocalDate.now())
				.build());

		BDDMockito.doNothing().when(contaRepositoryMock).delete(ArgumentMatchers.any(Conta.class));
    }
    
    @Test
    void findAllWithPageableContasTest(){
		var created = Conta.builder()
				.numeroDaConta("01")
				.nomeDaConta("Conta de Luz")
				.valorDaConta(new BigDecimal(100.0))
				.dataVencimento(LocalDate.now().plusDays(30))
				.dataPagamento(LocalDate.now())
				.build();
    	
        String numeroDaConta = created.getNumeroDaConta();

        Page<Conta> contaPage = contaService.findAll(PageRequest.of(1, 1));

        Assertions.assertTrue(!contaPage.isEmpty() || contaPage != null );
        Assertions.assertTrue(contaPage.toList().get(0).getNumeroDaConta().equals(numeroDaConta));
    }
    
    @Test
    void findAllFullListOfContasTest(){
		var created = Conta.builder()
				.numeroDaConta("01")
				.nomeDaConta("Conta de Luz")
				.valorDaConta(new BigDecimal(100.0))
				.dataVencimento(LocalDate.now().plusDays(30))
				.dataPagamento(LocalDate.now())
				.build();
    	
		String numeroDaConta = created.getNumeroDaConta();
        List<Conta> contas = contaService.findAllNonPageable();
		
        Assertions.assertTrue(!contas.isEmpty() || contas != null);
        Assertions.assertTrue(contas.get(0).getNumeroDaConta().equals(numeroDaConta));
    }
    
    @Test
    void findByIdTest(){
        Long findedId = Conta.builder()
        		.id(1L)
        		.numeroDaConta("01")
				.nomeDaConta("Conta de Luz")
				.valorDaConta(new BigDecimal(100.0))
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
        String findedNumeroDaConta = Conta.builder()
				.numeroDaConta("01")
				.nomeDaConta("Conta de Luz")
				.valorDaConta(new BigDecimal(100.0))
				.dataVencimento(LocalDate.now().plusDays(30))
				.dataPagamento(LocalDate.now())
				.build().getNumeroDaConta();

        List<Conta> contas = contaService.findByNumeroDaConta("03");

        Assertions.assertNotEquals(null, contas);
        Assertions.assertTrue(!contas.isEmpty());
        Assertions.assertEquals(contas.get(0).getNumeroDaConta(), findedNumeroDaConta);
    }

    @Test
    void findByNumeroDaContaWhenNotFoundTest(){
        BDDMockito.when(contaRepositoryMock.findByNumeroDaConta(ArgumentMatchers.anyString()))
                .thenReturn(Collections.emptyList());

        List<Conta> contas = contaService.findByNumeroDaConta("01");

        Assertions.assertNotEquals(null, contas);
        Assertions.assertTrue(contas.isEmpty());
    }

    @Test
    void saveTest(){
        var saved = contaService.save(Conta.builder()
        		.id(1L)
				.numeroDaConta("01")
				.nomeDaConta("Conta de Luz")
				.valorDaConta(new BigDecimal(100.0))
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
				.numeroDaConta("01")
				.nomeDaConta("Conta de Luz")
				.valorDaConta(new BigDecimal(100.0))
				.dataVencimento(LocalDate.now().plusDays(30))
				.dataPagamento(LocalDate.now())
				.build());
        
        var findedConta = contaService.findById(created.getId());
        findedConta.setNumeroDaConta("02");
        
        contaService.update(findedConta);
        
        Assertions.assertEquals(created.getId(), findedConta.getId());
        Assertions.assertNotEquals(created.getNumeroDaConta(), findedConta.getNumeroDaConta());
    }

    @Test
    void deleteTest(){
    	Assertions.assertDoesNotThrow(() -> {
    		contaService.delete(1L);
    	});
    }
}