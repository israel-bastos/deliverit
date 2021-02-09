package br.com.deliverit.domain;

import javax.persistence.Entity;
import javax.persistence.Id;

import lombok.AllArgsConstructor;

@Entity
@AllArgsConstructor
public class Multas {
	
	@Id
	private long id;
	
	public final double DE_2_PORCENTOS = 0.01;
	public final double DE_3_PORCENTOS = 0.02;
	public final double DE_5_PORCENTO = 0.03;
}
