package br.com.deliverit.domain;

import javax.persistence.Entity;
import javax.persistence.Id;

import lombok.AllArgsConstructor;

@Entity
@AllArgsConstructor
public class Juros {
	
	@Id
	private long id;
	
	public final double REGRA_DE_JUROS_1 = 0.01;
	public final double REGRA_DE_JUROS_2 = 0.02;
	public final double REGRA_DE_JUROS_3 = 0.03;
}
