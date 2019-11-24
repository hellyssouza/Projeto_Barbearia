package com.projetofinal.Barbearia.enumerador;

public enum TipoDePagamento {
	DINHEIRO(1), CARTAO(2);

	private int codigo;

	private TipoDePagamento(int codigo) {
		this.codigo = codigo;
	}

	public int getCodigo() {
		return this.codigo;
	}

	public void setCodigo(int codigo) {
		this.codigo = codigo;
	}
}
