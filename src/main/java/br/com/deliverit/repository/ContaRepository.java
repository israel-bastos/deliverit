package br.com.deliverit.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import br.com.deliverit.model.Conta;

@Repository
public interface ContaRepository extends JpaRepository<Conta, Long> {
	
	List<Conta> findByNumeroDaConta(String numeroDaConta);

}
