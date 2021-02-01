package br.com.deliverit.controller;

import java.net.URI;
import java.util.List;

import javax.validation.Valid;

import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import br.com.deliverit.model.Conta;
import br.com.deliverit.service.ContaService;

@RestController
@RequestMapping(path = "api/v1/contas")
public class ContaController {
	
	private ContaService contaService;
	
	public ContaController(ContaService contaService) {
		this.contaService = contaService;
	}
	
	@GetMapping
	public ResponseEntity<List<Conta>> findAllNonPageable(){
		return ResponseEntity.ok(contaService.findAllNonPageable());
	}
	
	@GetMapping(path = "/pageable")
	public ResponseEntity<Page<Conta>> findAll(Pageable pageable){
		return ResponseEntity.ok(contaService.findAll(pageable));
	}
	
	@GetMapping(path = "/{id}")
	public ResponseEntity<Conta> findById(@PathVariable long id){
		return ResponseEntity.ok(contaService.findById(id));
	}
	
	@GetMapping(path = "/search")
	public ResponseEntity<List<Conta>> findByNumeroDaConta(@RequestParam String numeroDaConta){
		return ResponseEntity.ok(contaService.findByNumeroDaConta(numeroDaConta));
	}
	
	@PostMapping
	public ResponseEntity<Conta> save(@RequestBody @Valid Conta conta) {
		contaService.save(conta);
		
		return ResponseEntity.created(URI.create("/api/v1/contas/" + conta.getId())).body(conta);
	}
	
	@PutMapping
	public ResponseEntity<Void> update(@RequestBody Conta conta){
		contaService.update(conta);
		
		return ResponseEntity.noContent().build();
	}
	
	@DeleteMapping(path = "/{id}")
	public ResponseEntity<Void> delete(@PathVariable long id){
		contaService.delete(id);
		
		return ResponseEntity.noContent().build();
	}
}