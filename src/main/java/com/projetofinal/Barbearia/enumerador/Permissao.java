package com.projetofinal.Barbearia.enumerador;

public enum Permissao {
	TOTAL(1), FUNCIONARIO(2), CLIENTE(3);

	private int codigo;

	private Permissao(int codigo) {
		this.codigo = codigo;
	}

	public int getCodigo() {
		return this.codigo;
	}
}
