package com.projetofinal.Barbearia.relatorio;

import java.io.InputStream;
import java.io.Serializable;
import java.sql.Connection;
import java.sql.DriverManager;
import java.util.HashMap;
import java.util.Map;

import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JasperCompileManager;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.JasperReport;

public class GeradorDeRelatorioAtendimentos implements Serializable {
	private static final long serialVersionUID = 1L;

	public JasperPrint gereRelatorio(String dataDeInicio, String dataDeFim, Integer idFuncionario, Integer status, Integer pagamento)
			throws JRException {
		final InputStream jasperTemplate = GeradorDeRelatorioAtendimentos.class
				.getResourceAsStream("relatorioatendimento.jrxml");

		JasperReport relatorio = JasperCompileManager.compileReport(jasperTemplate);

		Map<String, Object> parametros = new HashMap<String, Object>();

		parametros.put("PR_DATA_INICIO", dataDeInicio);
		
		parametros.put("PR_DATA_FIM", dataDeFim);
		
		parametros.put("PR_FUNCIONARIO", idFuncionario);

		parametros.put("PR_STATUS", status);

		parametros.put("PR_PAGAMENTO", pagamento);

		Connection conexao = null;

		try {
			Class.forName("com.microsoft.sqlserver.jdbc.SQLServerDriver");
			conexao = DriverManager.getConnection("jdbc:sqlserver://localhost:1433;databaseName=BARBEARIA", "sa", "sa");
		} catch (Exception e) {
		}

		JasperPrint print = JasperFillManager.fillReport(relatorio, parametros, conexao);

		return print;
	}
}
