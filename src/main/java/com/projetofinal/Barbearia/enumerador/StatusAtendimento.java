package com.projetofinal.Barbearia.enumerador;

public enum StatusAtendimento {
	LIVRE(1), AGENDADO(2), ATENDIDO(3);

	private int codigo;

	private StatusAtendimento(int codigo) {
		this.codigo = codigo;
	}

	public int getCodigo() {
		return this.codigo;
	}

	public void setCodigo(int codigo) {
		this.codigo = codigo;
	}
}
