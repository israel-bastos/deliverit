package br.com.deliverit.service;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import br.com.deliverit.handler.exception.NotFoundException;
import br.com.deliverit.model.Conta;
import br.com.deliverit.repository.ContaRepository;

@Service
public class ContaService {
	
	private final ContaRepository contaRepository;
	
	public ContaService(ContaRepository contaRepository) {
		this.contaRepository = contaRepository;
	}
	
	public Page<Conta> findAll(Pageable pageable){
		return contaRepository.findAll(pageable);
	}
	
	public List<Conta> findAllNonPageable(){
		return contaRepository.findAll();
	}
	
	public Conta findById(long id) {
		return contaRepository.findById(id)
				.orElseThrow(() -> new NotFoundException("Conta não encontrada."));
	}
	
	public List<Conta> findByNumeroDaConta(String numeroDaConta) {
		List<Conta> listaDeContas = contaRepository.findByNumeroDaConta(numeroDaConta);
		
		return listaDeContas;
	}
	
	@Transactional
	public Conta save(Conta conta) {
        return contaRepository.save(conta);
    }
	
	@Transactional
	public void delete(long id) {
		contaRepository.delete(findById(id));
	}
	
	public void update(Conta conta) {
		Conta save = findById(conta.getId());

		Conta saved = Conta.builder()
				.id(save.getId())
				.numeroDaConta(conta.getNumeroDaConta())
        		.nomeDaConta(conta.getNomeDaConta())
        		.valorDaConta(conta.getValorDaConta())
        		.dataVencimento(conta.getDataVencimento())
        		.dataPagamento(conta.getDataPagamento())
        		.build();
		
		contaRepository.save(saved);
	}
}
