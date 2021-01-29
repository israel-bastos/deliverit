package br.com.deliverit.controller;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

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
        PageImpl<Conta> contaPage = new PageImpl<>(List.of(
        		Conta.builder()
				.numeroDaConta("01")
        		.nomeDaConta("Conta de Luz")
        		.valorDaConta(new BigDecimal(100.0))
        		.dataVencimento(LocalDate.now().plusDays(30))
        		.dataPagamento(LocalDate.now())
        		.build()
        ));
        
        BDDMockito.when(contaServiceMock.findAll(ArgumentMatchers.any())).thenReturn(contaPage);
    }
    
    @Test
    void listAllPageableContas(){
		var created = Conta.builder()
				.numeroDaConta("01")
				.nomeDaConta("Conta de Luz")
				.valorDaConta(new BigDecimal(100.0))
				.dataVencimento(LocalDate.now().plusDays(30))
				.dataPagamento(LocalDate.now()).build();
    	
        String numeroDaConta = created.getNumeroDaConta();

        Page<Conta> contaPage = contaController.findAll(null).getBody();

        Assertions.assertTrue(contaPage != null);
        Assertions.assertTrue(contaPage.toList() != null || contaPage.toList().isEmpty());
        Assertions.assertTrue(contaPage.toList().size() == 1);
        Assertions.assertTrue(contaPage.toList().get(0).getNumeroDaConta().equals(numeroDaConta));
    }
}
