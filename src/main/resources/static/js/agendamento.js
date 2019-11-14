(function($){
	var data = document.getElementById("data");
	var botaoSalvar = $("#salvar");
	
	var crieColunasDaTabela = function(objeto)
	{
		var colunaatendido = "<td><span class='badge badge-success'>{Descricao}</span></td>";
		
		var status = { LIVRE: 1, AGENDADO: 2, ATENDIDO: 3};
		
		var pagamento = { DINHEIRO: 1, CARTAO: 2};
		
		var dadosDosFuncionarios = JSON.parse($("#tabela-horarios").data("FUNCIONARIOS"));
		
		var usuarioLogado = JSON.parse($("#tabela-horarios").data("USUARIOLOGADO"));
		
		var funcionarios = Object.values(dadosDosFuncionarios);
		
		var dataEHora = objeto.DataEHorario.replace("T"," ").split(" ");
		
		var colunaId = "<th scope='row'>" + objeto.Id + "</th>";
		
		var dataFormatada = dataEHora[0].split("-");
		
		dataFormatada = dataFormatada[2] + "/" + dataFormatada[1] + "/" + dataFormatada[0];
		
		var horaFormatada = dataEHora[1].substr(0, 5);
		
		var colunaData = "<td>" + dataFormatada + "</td>";
		
		var colunaHora = "<td>" + horaFormatada + "</td>";
		
		var nomeFuncionario = funcionarios.filter(x => x.Id == objeto.Funcionario)[0].Nome;
		
		var colunaFuncionario = "<td>" + nomeFuncionario + "</td>";
		
		var colunaBotao = ""
		
		switch(objeto.Status)
		{
			case status.LIVRE: 
				colunaBotao = "<td><a id='agendar_"+ objeto.Id +"' class='badge badge-info'>Agendar</a></td>";
				break;
			case status.AGENDADO:
				colunaBotao = "<td><span id='cancelar_"+ objeto.Id +"' class='badge badge-danger'>Cancelar</span></td>";
				break;
			case status.ATENDIDO: 
				var coluna = "<td><div><div>{Status}</div><div>{Pagamento}</div><div>{Valor}</div></div></td>";
				coluna = coluna.replace("{Status}", colunaatendido.replace("{Descricao}", "Atendido").replace("<td>", "").replace("</td>", ""))
				var pagamento = objeto.Pagamento == pagamento.DINHEIRO ? "DINHEIRO" : "CARTAO";
				coluna = coluna.replace("{Pagamento}", colunaatendido.replace("{Descricao}", pagamento).replace("<td>", "").replace("</td>", ""));
				coluna = coluna.replace("{Valor}", colunaatendido.replace("{Descricao}", accounting.formatMoney(objeto.Valor)).replace("<td>", "").replace("</td>", ""));
				colunaBotao = coluna;
				break;
		}
		
		var colunas = [colunaId, colunaData, colunaHora, colunaFuncionario, colunaBotao];
		
		return colunas;
	}
	
	var adicioneEventosNosBotoes = function(objeto)
	{
		$("[id='agendar_" + objeto.Id + "']").on("click", function(){
			$('#confirme-agendamento').modal('show');
			
			$("#tabela-horarios").data("EMAGENDAMENTO", objeto.Id);
		});
		
		$("[id='cancelar_" + objeto.Id + "']").on("click", function(){
			var dados = { idAgendamento: objeto.Id, idUsuario: null };
			
			$.ajax({
				url: "excluaagendamento",
				method: "POST",
				contentType: "application/json",
				processData: false,
				data: JSON.stringify(dados),
				success: function(){
					$("#tabela-horarios tbody").html("");
					carregueDadosDaTabela();
				},
				error: function(erro){
				}
			});
		});
	}
	
	var adicioneItemNaTabela = function(objeto)
	{
		var colunas = crieColunasDaTabela(objeto);
		
		var linha = "<tr name='" + "linha_" + objeto.Id +"'>" + colunas[0] + colunas[1] + colunas[2] + colunas[3] + colunas[4] + "</tr>";
		
		$("#tabela-horarios > tbody").append(linha);
		
		var selectorLinha = "[name='" + "linha_" + objeto.Id + "']";
		
		adicioneEventosNosBotoes(objeto);
	}
	
	var carregueDadosDaTabela = function()
	{
		$.get("consulteusuariocontexto", function(dados, status){
			$("#tabela-horarios").data("USUARIOLOGADO", dados);
		});
		
		$.get("consulteservicos", function(dados, status){
			$("#tabela-horarios").data("SERVICOS", dados);
		});
		
		$.get("consultefuncionarios", function(dados, status){
			$("#tabela-horarios").data("FUNCIONARIOS", dados);
			
			$.get("consulteatendimentosparausuario", function(dados, status){
				var objetos = JSON.parse(dados);
				
				objetos.forEach(atendimento => adicioneItemNaTabela(atendimento));
			});
		});
	}
	
	$(document).ready(function(){
		carregueDadosDaTabela();
				
		$('#confirme-agendamento').on('shown.bs.modal', function () {
			var servicos = JSON.parse($("#tabela-horarios").data("SERVICOS"));
			
			$("#servicos").html("");
			
			servicos.forEach(servico => {
				var caixaServico = "<div class='col-lg-2 text-white caixa-agendamento' id='caixa-servico-" + servico.id + "'>";
				caixaServico += "<div hidden>" + servico.id + "</div>";
				caixaServico += "<div>" + servico.nome + "</div>";
				caixaServico += "<div>" + accounting.formatMoney(servico.valor) +"</div>";
				caixaServico += "</div>";
				
				$("#servicos").append(caixaServico);
				
				$("[id='caixa-servico-" + servico.id + "']").on("click", function(){
					if($(this).hasClass("selecionado"))
					{
						var valor = accounting.unformat($("#valor").val()) - servico.valor;
						
						$("#valor").val(accounting.formatMoney(valor));
						
						$(this).removeClass("selecionado");
						
						$(this).css("background-color", "#DC7633");
					}
					else
					{
						var valor = accounting.unformat($("#valor").val()) + servico.valor;
						
						$("#valor").val(accounting.formatMoney(valor));
						
						$(this).addClass("selecionado");
						
						$(this).css("background-color", "#2980B9");
					}
				});
			});
		});
		
		$('#confirme-agendamento').on('hide.bs.modal', function(){
			$("#valor").val(accounting.formatMoney(0));
			
			$("#tabela-horarios").removeData("EMAGENDAMENTO");
		});
		
		$("#btnAgendar").on("click", function(){
			var usuarioLogado = JSON.parse($("#tabela-horarios").data("USUARIOLOGADO"));
			var valor = accounting.unformat($("#valor").val());
			
			if(valor == "0" || valor == "")
			{
				$("#valor").notify("Não foi selecionado nenum serviço!", { className: 'error', position: "bottom lefth" });
				return;
			}
			
			var dados = {
					idAgendamento: $("#tabela-horarios").data("EMAGENDAMENTO"),
					idUsuario: usuarioLogado.id,
					servicos: Array.from($(".selecionado div:hidden").map(function(){ return parseInt(this.innerText); })),
					valor: valor
				};
			
			$.ajax({
				url: "efetueagendamento",
				method: "POST",
				contentType: "application/json",
				processData: false,
				data: JSON.stringify(dados),
				success: function(){
					$('#confirme-agendamento').modal('hide');
					$("#tabela-horarios tbody").html("");
					carregueDadosDaTabela();
				},
				error: function(erro){
				}
			});
		});
		
		$("#filtro").on("keyup", function() {
		    var value = $(this).val().toLowerCase();
		    
		    $("#tabela-horarios tbody tr").filter(function() {
		      $(this).toggle($(this).text().toLowerCase().indexOf(value) > -1)
		    });
		});
	});
})(jQuery)