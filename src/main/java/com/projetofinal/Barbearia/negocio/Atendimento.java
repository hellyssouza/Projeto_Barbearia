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

	@Column(name = "FUNCIONARIO")
	private Long Funcionario;

	@Column(name = "USUARIO")
	private Long Usuario;

	@Column(name = "VALOR")
	private Float Valor;

	public Atendimento() {
	}

	public Atendimento(String dataEHorarario, Long funcionario, Float valor) {
		this.DataEHorario = dataEHorarario;
		this.Funcionario = funcionario;
		this.Valor = valor;
	}

	public Float getValor() {
		return Valor;
	}

	public void setValor(Float valor) {
		Valor = valor;
	}

	public Long getUsuario() {
		return Usuario;
	}

	public void setUsuario(Long usuario) {
		Usuario = usuario;
	}

	public Long getFuncionario() {
		return Funcionario;
	}

	public void setFuncionario(Long funcionario) {
		Funcionario = funcionario;
	}

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

	public Atendimento clone() {
		return new Atendimento(this.DataEHorario, this.Funcionario, this.Valor);
	}
}
