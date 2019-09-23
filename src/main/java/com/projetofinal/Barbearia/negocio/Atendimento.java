package com.projetofinal.Barbearia.negocio;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "ATENDIMENTO")
public class Atendimento {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "ID")
	private Long Id;
	
	@Column(name = "DATAHORARIO")
	private String DataEHorario;
	
	public String getDataEHorario() {
		return DataEHorario;
	}
	public void setDataEHorario(String dataEHorario) {
		DataEHorario = dataEHorario;
	}
	public Long getId() {
		return Id;
	}
	public void setId(Long id) {
		Id = id;
	}
}
