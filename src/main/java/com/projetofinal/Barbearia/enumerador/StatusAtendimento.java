package com.projetofinal.Barbearia.enumerador;

import javax.persistence.AttributeConverter;

public enum StatusAtendimento implements AttributeConverter<StatusAtendimento, Integer> {
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

	@Override
	public Integer convertToDatabaseColumn(final StatusAtendimento rating) {
		return rating.getCodigo();
	}

	@Override
	public StatusAtendimento convertToEntityAttribute(Integer dbData) {
		StatusAtendimento status = null;
		
		if (dbData == 1) {
			status = StatusAtendimento.LIVRE;
		} else if (dbData == 2) {
			status = StatusAtendimento.AGENDADO;
		} else if (dbData == 3) {
			status = StatusAtendimento.ATENDIDO;
		}

		return status;
	}
}
