package br.com.deliverit.integration;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.annotation.DirtiesContext;

import br.com.deliverit.controller.PageableResponse;
import br.com.deliverit.model.Conta;
import br.com.deliverit.repository.ContaRepository;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureTestDatabase
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
public class ContaControllerIntegrationTest {
	@Autowired
    private TestRestTemplate testRestTemplate;
    
	@Autowired
    private ContaRepository contaRepository;

	@LocalServerPort
    private int port;
    
    @Test
    void findAllWithPageableContasTest() {
        Conta saved = Conta.builder()
				.numeroDaConta("01")
        		.nomeDaConta("Conta de Internet")
        		.valorDaConta(new BigDecimal(100.0))
        		.dataVencimento(LocalDate.now().plusDays(30))
        		.dataPagamento(LocalDate.now())
        		.build();

        String numeroDaConta = saved.getNumeroDaConta();

        PageableResponse<Conta> contaPage = testRestTemplate.exchange("/api/v1/contas/pageable", HttpMethod.GET, null,
                new ParameterizedTypeReference<PageableResponse<Conta>>() {}).getBody();

        Assertions.assertNotNull(contaPage);
        Assertions.assertTrue(!contaPage.isEmpty());
        Assertions.assertTrue(contaPage.toList().get(0).getNumeroDaConta().equals(numeroDaConta));
    }

    @Test
    void findAllFullListOfContasTest() {
        List<Conta> contas = testRestTemplate.exchange("/api/v1/contas", HttpMethod.GET, null,
                new ParameterizedTypeReference<List<Conta>>() {}).getBody();

        Assertions.assertNotNull(contas);
        Assertions.assertTrue(!contas.isEmpty());
        Assertions.assertEquals(contas.size(), 1);
    }

    @Test
    void findByIdTest() {
        Conta saved = Conta.builder()
				.numeroDaConta("01")
        		.nomeDaConta("Conta de Internet")
        		.valorDaConta(new BigDecimal(100.0))
        		.dataVencimento(LocalDate.now().plusDays(30))
        		.dataPagamento(LocalDate.now())
        		.build();

        Long findedId = saved.getId();

        Conta conta = testRestTemplate.getForObject("/api/v1/contas/{id}", Conta.class, findedId);
        
        Assertions.assertNotNull(conta);
        Assertions.assertEquals(conta.getId(), findedId);
    }

    @Test
    void findByNumeroDaContaTest(){
        Conta saved = Conta.builder()
				.numeroDaConta("01")
        		.nomeDaConta("Conta de Internet")
        		.valorDaConta(new BigDecimal(100.0))
        		.dataVencimento(LocalDate.now().plusDays(30))
        		.dataPagamento(LocalDate.now())
        		.build();

        String findedNumeroDaConta = saved.getNumeroDaConta();

        String url = String.format("/api/v1/contas/search?numeroDaConta=%s", findedNumeroDaConta);

        List<Conta> contas = testRestTemplate.exchange(url, HttpMethod.GET, null,
                new ParameterizedTypeReference<List<Conta>>() {}).getBody();
        
        Assertions.assertNotNull(contas);
        Assertions.assertTrue(!contas.isEmpty());
        Assertions.assertEquals(contas.get(0).getNumeroDaConta(), findedNumeroDaConta);
    }

    @Test
    void findByNumeroDaContaWhenNotFoundTest(){
        List<Conta> contas = testRestTemplate.exchange("/api/v1/contas/search?numeroDaConta=02", HttpMethod.GET, null,
                new ParameterizedTypeReference<List<Conta>>() {}).getBody();
        
        Assertions.assertNotNull(contas);
        Assertions.assertTrue(contas.isEmpty());

    }

    @Test
    void saveTest(){
        Conta saved = Conta.builder()
				.numeroDaConta("01")
        		.nomeDaConta("Conta de Internet")
        		.valorDaConta(new BigDecimal(100.0))
        		.dataVencimento(LocalDate.now().plusDays(30))
        		.dataPagamento(LocalDate.now())
        		.build();

        ResponseEntity<Conta> contaResponse = testRestTemplate.postForEntity("/api/v1/contas", saved, Conta.class);

        Assertions.assertNotNull(contaResponse);
        Assertions.assertEquals(contaResponse.getBody().getId(), saved.getId());
        Assertions.assertEquals(contaResponse.getStatusCode(), HttpStatus.CREATED);
    }

    @Test
    void updateTest(){
        Conta saved = Conta.builder()
				.numeroDaConta("01")
        		.nomeDaConta("Conta de Internet")
        		.valorDaConta(new BigDecimal(100.0))
        		.dataVencimento(LocalDate.now().plusDays(30))
        		.dataPagamento(LocalDate.now())
        		.build();
        
        Optional<Conta> findedConta = contaRepository.findById(saved.getId());
        findedConta.get().setNomeDaConta("02");

        ResponseEntity<Void> contaResponse = testRestTemplate.exchange("api/v1/contas",
                HttpMethod.PUT,new HttpEntity<>(saved), Void.class);
        
        Assertions.assertNotNull(findedConta);
        Assertions.assertEquals(contaResponse.getStatusCode(), HttpStatus.NO_CONTENT);
    }

    @Test
    void deleteTest(){
        Conta saved = Conta.builder()
				.numeroDaConta("01")
        		.nomeDaConta("Conta de Internet")
        		.valorDaConta(new BigDecimal(100.0))
        		.dataVencimento(LocalDate.now().plusDays(30))
        		.dataPagamento(LocalDate.now())
        		.build();

        ResponseEntity<Void> contaResponse = testRestTemplate.exchange("/api/v1/contas/{id}",
                HttpMethod.DELETE,null, Void.class, saved.getId());
        
        Assertions.assertNotNull(saved);
        Assertions.assertEquals(contaResponse.getStatusCode(), HttpStatus.NO_CONTENT);
    }
}