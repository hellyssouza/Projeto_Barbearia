package com.projetofinal.Barbearia.negocio;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;

public class GeradorDeHorarios {
	private SimpleDateFormat formatador = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS", Locale.US);
	private List<String> horarios = new ArrayList<String>();

	public List<String> gere(String data, String horarioInicio, String horarioFim, String periodo)
			throws ParseException {
		Calendar dataAuxiliar = obtenhaDataComHorario(data, horarioInicio);
		LocalTime horarioAuxiliar = LocalTime.parse(horarioInicio);
		LocalTime horarioDeFim = LocalTime.parse(horarioFim);
		LocalTime horarioPeriodo = LocalTime.parse(periodo);

		do {
			horarios.add(formatador.format(dataAuxiliar.getTime()));
			
			horarioAuxiliar = horarioAuxiliar.plusHours(horarioPeriodo.getHour());

			horarioAuxiliar = horarioAuxiliar.plusMinutes(horarioPeriodo.getMinute());

			dataAuxiliar.set(Calendar.HOUR_OF_DAY, horarioAuxiliar.getHour());

			dataAuxiliar.set(Calendar.MINUTE, horarioAuxiliar.getMinute());
		} while (horarioAuxiliar.compareTo(horarioDeFim) < 0 || horarioAuxiliar.compareTo(horarioDeFim) == 0);

		return horarios;
	}

	private Calendar obtenhaDataComHorario(String data, String horario) throws ParseException {
		Calendar dataAuxiliar = new Calendar.Builder().setInstant(formatador.parse(data)).build();

		LocalTime horarioAuxiliar = LocalTime.parse(horario);

		dataAuxiliar.set(Calendar.HOUR_OF_DAY, horarioAuxiliar.getHour());

		dataAuxiliar.set(Calendar.MINUTE, horarioAuxiliar.getMinute());

		return dataAuxiliar;
	}
}
