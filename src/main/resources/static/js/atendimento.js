var MODULO = (function(modulo, $){
	modulo.atendimento = {};

	modulo.atendimento.containeratendido = "<td style='width:400px'><div>{Status} {Pagamento} {Valor}</div></td>";
	modulo.atendimento.colunaatendido = "<td><span class='badge badge-success'>{Descricao}</span></td>";
	modulo.atendimento.colunalivre = "<td><span class='badge badge-info'>Livre</span></td>";
	modulo.atendimento.modalatendimento = $("#modal-info-agendamento");
	modulo.atendimento.status = { LIVRE: 1, AGENDADO: 2, ATENDIDO: 3};
	modulo.atendimento.pagamento = { DINHEIRO: 1, CARTAO: 2};
	modulo.atendimento.comboFuncionarios = $("#funcionarios");
	modulo.atendimento.data = document.getElementById("data");
	modulo.atendimento.botaoCancelar = $(".btn-cancelar");
	modulo.atendimento.botaoAdicionar = $("#adicionar");
	modulo.atendimento.botaoAtender = $(".btn-atender");
	modulo.atendimento.botaoRemover = $("#remover");
	modulo.atendimento.botaoSalvar = $("#salvar");
	modulo.atendimento.tabela = $("#tabela tbody");
	
	modulo.atendimento.adicioneItemNaTabela = function(objeto){
		var dataEHora = objeto.DataEHorario.replace("T"," ").split(" ");
		
		var colunaId = "<th scope='row'>" + objeto.Id + "</th>";
		
		var dataFormatada = dataEHora[0].split("-");
		
		dataFormatada = dataFormatada[2] + "/" + dataFormatada[1] + "/" + dataFormatada[0];
		
		var horaFormatada = dataEHora[1].substr(0, 5);
		
		var colunaData = "<td>" + dataFormatada + "</td>";
		
		var colunaHora = "<td>" + horaFormatada + "</td>";
		
		var colunaFuncionario = "<td>" + modulo.atendimento.funcionarios.filter(x => x.Id == objeto.Funcionario)[0].Nome + "</td>";
		
		var linha = "<tr name='"+ "linha_" + objeto.Id +"'>" + colunaId + colunaData + colunaHora + colunaFuncionario +"</tr>";
		
		$("#tabela-horarios > tbody").append(linha);
		
		var selectorLinha = "[name='" + "linha_" + objeto.Id + "']";
		
		modulo.atendimento.adicioneColunaStatus(selectorLinha, objeto);
		
		$(selectorLinha).on("click", function(){
			$(".selecionado").removeClass("selecionado bg-light");
			
			$(this).addClass("selecionado")
			$(this).addClass("bg-light");
		});
	}
	
	modulo.atendimento.gereGuid = function(){  
		   function s4() {  
		      return Math.floor((1 + Math.random()) * 0x10000).toString(16).substring(1);  
		   }  
		   return s4() + s4() + '-' + s4() + '-' + s4() + '-' + s4() + '-' + s4() + s4() + s4();  
	}
	
	modulo.atendimento.crieColunaEAdicioneNaTabela = function(selectorLinha, objeto, usuario, servicos){
		var classe = modulo.atendimento.gereGuid();
		var colunaStatus = "<td class='coluna_" + classe +"'><span class='badge badge-warning " + classe + "'>Agendado</span></td>";
		
		$(selectorLinha).append(colunaStatus);
		
		$("." + classe).on("click", function(){
			$(".valor-total-agendamento").val(accounting.formatMoney(objeto.Valor));
			
			$(selectorLinha).click();
			
			$(".lista-de-servicos").empty();
			
			$(".nome-cliente").html("Cliente: " + usuario.nome);
			
			servicos.forEach(servico => {
				$(".lista-de-servicos").append("<li class='list-group-item d-flex justify-content-between align-items-center'>" 
										+ servico.nome + "<span class='badge badge-primary badge-pill'> " 
										+ accounting.formatMoney(objeto.Valor) + "</span>" +"</li>");
			});
			
			modulo.atendimento.seletorcolunacontexto = ".coluna_" + classe;
			modulo.atendimento.modalatendimento.modal("show");			
			modulo.atendimento.atendimentocontexto = objeto;
			modulo.atendimento.colunacontexto = classe;
		});
	}
	
	modulo.atendimento.adicioneColunaStatus = function(selectorLinha, objeto){
		switch(objeto.Status)
		{
			case modulo.atendimento.status.LIVRE: 
				$(selectorLinha).append(modulo.atendimento.colunalivre);
				break;
			case modulo.atendimento.status.ATENDIDO:
				var coluna = modulo.atendimento.containeratendido;
				var porcentagem = modulo.atendimento.funcionarios.filter(x => x.Id == objeto.Funcionario)[0].Porcentagem / 100;
				coluna = coluna.replace("{Status}", modulo.atendimento.colunaatendido.replace("{Descricao}", "Atendido").replace("<td>", "").replace("</td>", ""))
				var pagamento = objeto.Pagamento == modulo.atendimento.pagamento.DINHEIRO ? "DINHEIRO" : "CARTAO";
				coluna = coluna.replace("{Pagamento}", modulo.atendimento.colunaatendido.replace("{Descricao}", "Pagamento: " + pagamento).replace("<td>", "").replace("</td>", ""));
				coluna = coluna.replace("{Valor}", modulo.atendimento.colunaatendido.replace("{Descricao}", "Porcentagem: " + accounting.formatMoney(objeto.Valor * porcentagem)).replace("<td>", "").replace("</td>", ""));
				
				$(selectorLinha).append(coluna);
				break;
			case modulo.atendimento.status.AGENDADO:
				$.ajax({
					url: "consulteservicosdoatendimento",
					dataType: "json",
					method: "POST",
					contentType: "application/json",
					processData: false,
					async: false,
					data: JSON.stringify({idAgendamento: objeto.Id }),
					success: function(servicos){
						$.ajax({
							url: "consulteusuario",
							dataType: "json",
							method: "POST",
							contentType: "application/json",
							processData: false,
							data: JSON.stringify({id: objeto.Usuario }),
							success: function(usuario){
								modulo.atendimento.crieColunaEAdicioneNaTabela(selectorLinha, objeto, usuario, servicos);
							},
							error: function(erro){
							}
						});
					},
					error: function(erro){
					}
				});
				break;
		}
	}
	
	modulo.atendimento.adicioneNaComboBox = function(objeto){
		var opcaoNome = "<option id='funcionario_" + objeto.Id + "' value='" + objeto.Id + "'>" + objeto.Nome + "</option>";
		
		$(modulo.atendimento.comboFuncionarios).append(opcaoNome);
	}
	
	modulo.atendimento.removaItemSelecionadoDaTabela = function(){
		var indiceRemover = parseInt($(".selecionado [scope='row']").html());
		
		modulo.atendimento.atendimentos = Object.values(modulo.atendimento.atendimentos).filter(x => x.Id !== indiceRemover);
		
		$(".selecionado").remove();
	}
	
	modulo.atendimento.limpaCampos = function(){
		$("#data").val("");
		$("#horario-inicio").val("");
		$("#horario-fim").val("");
		$("#periodo").val("");
		$("#funcionarios option:first").attr("selected","selected");
		$(".forma-de-pagamento option:first").attr("selected","selected");
	}
	
	modulo.atendimento.valideCampos = function(){
		if(data.value == "" 
		|| $("#horario-inicio").val() == "" 
		|| $("#horario-fim").val() == ""
		|| $("#periodo").val() == "")
		{
			$.notify("Existem campos que não foram preenchidos!", { className: 'error', position: "top lefth" });
			
			return true;
		}
		
		return false;
	}
	
	modulo.atendimento.associeAcoes = function(){
		$(modulo.atendimento.botaoAtender).on("click", function(){
			if($(".forma-de-pagamento").children("option:selected").val() == "")
			{
				$.notify("O tipo de pagamento não foi selecionado!", { className: 'error', position: "top lefth" });
				
				return;
			}
			
			var objeto = { 
					idAgendamento: modulo.atendimento.atendimentocontexto.Id,
					statusAgendamento: modulo.atendimento.status.ATENDIDO,
					pagamento: $(".forma-de-pagamento").children("option:selected").val()
			};
			
			$.ajax({
				url: "atulizestatus",
				method: "POST",
				contentType: "application/json",
				processData: false,
				data: JSON.stringify(objeto),
				success: function(){
					var coluna = modulo.atendimento.containeratendido;
					modulo.atendimento.atendimentocontexto.Pagamento = $(".forma-de-pagamento").children("option:selected").val();
					var porcentagem = modulo.atendimento.funcionarios.filter(x => x.Id == modulo.atendimento.atendimentocontexto.Funcionario)[0].Porcentagem / 100;
					coluna = coluna.replace("{Status}", modulo.atendimento.colunaatendido.replace("{Descricao}", "Atendido").replace("<td>", "").replace("</td>", ""))
					var pagamento = modulo.atendimento.atendimentocontexto.Pagamento == modulo.atendimento.pagamento.DINHEIRO ? "DINHEIRO" : "CARTAO";
					coluna = coluna.replace("{Pagamento}", modulo.atendimento.colunaatendido.replace("{Descricao}", "Pagamento: " + pagamento).replace("<td>", "").replace("</td>", ""));
					coluna = coluna.replace("{Valor}", modulo.atendimento.colunaatendido.replace("{Descricao}", "Porcentagem: " + accounting.formatMoney(modulo.atendimento.atendimentocontexto.Valor * porcentagem)).replace("<td>", "").replace("</td>", ""))
					$(modulo.atendimento.seletorcolunacontexto).children().remove();
					$(modulo.atendimento.seletorcolunacontexto).append(coluna);
					modulo.atendimento.modalatendimento.modal("hide");
				},
				error: function(erro){
				}
			});
		});
		
		$(modulo.atendimento.botaoCancelar).on("click", function(){
			$.ajax({
				url: "excluaagendamento",
				method: "POST",
				contentType: "application/json",
				processData: false,
				data: JSON.stringify({idAgendamento: modulo.atendimento.atendimentocontexto.Id }),
				success: function(){
					var seletorColuna = ".coluna_" + modulo.atendimento.colunacontexto;
					$(seletorColuna).children().remove();
					$(seletorColuna).append(modulo.atendimento.colunalivre.replace("<td>", "").replace("</td>", ""));
					modulo.atendimento.modalatendimento.modal("hide");
				},
				error: function(erro){
				}
			});
		});
		
		$(modulo.atendimento.botaoRemover).on("click", function(){
			var indiceRemover = parseInt($(".selecionado [scope='row']").html());
			
			var objeto = { 
					Id : indiceRemover, 
					DataEHorario : new Date(data.value),
					Funcionario: $(modulo.atendimento.comboFuncionarios).children("option:selected").val()
			};
			
			$.ajax({
				url: "excluaatendimento",
				method: "POST",
				contentType: "application/json",
				processData: false,
				data: JSON.stringify(objeto),
				success: function(){
					modulo.atendimento.removaItemSelecionadoDaTabela();
				},
				error: function(erro){
				}
			});
		});
		
		$(modulo.atendimento.botaoSalvar).on("click", function(){
			if(modulo.atendimento.valideCampos())
			{
				return;
			}
			
			var objeto = { 
					Id : 0, 
					DataEHorario : new Date(data.value),
					HorarioInicio: $("#horario-inicio").val(),
					HorarioFim: $("#horario-fim").val(),
					Periodo: $("#periodo").val(),
					Valor: 0,
					Funcionario: $(modulo.atendimento.comboFuncionarios).children("option:selected").val()
			};
			
			$.ajax({
				url: "cadastreatendimento",
				dataType: "json",
				method: "POST",
				contentType: "application/json",
				processData: false,
				data: JSON.stringify(objeto),
				success: function(dados){
					dados.forEach(atendimento => {
						modulo.atendimento.adicioneItemNaTabela(atendimento);
						
						modulo.atendimento.atendimentos.push(atendimento);
					});
					limpaCampos();
				},
				error: function(erro){
					$("#mensagem").html(erro.responseText);
					$("#modal-alerta").modal("show");
				}
			});
		});
	}
	
	modulo.atendimento.inicialize = function(){
		modulo.atendimento.associeAcoes();
		
		$.get("consultefuncionarios", function(dados, status){
			modulo.atendimento.funcionarios = Object.values(JSON.parse(dados));
			
			modulo.atendimento.funcionarios.forEach(funcionario => {
				modulo.atendimento.adicioneNaComboBox(funcionario);
			});
			
			$.get("consulteatendimentos", function(dados, status){
				modulo.atendimento.atendimentos = JSON.parse(dados);
				
				modulo.atendimento.atendimentos.forEach(atendimento => { 
					modulo.atendimento.adicioneItemNaTabela(atendimento);
				});
			});
		});
		
		$("#filtro").on("keyup", function() {
		    var value = $(this).val().toLowerCase();
		    
		    $("#tabela-horarios tbody tr").filter(function() {
		      $(this).toggle($(this).text().toLowerCase().indexOf(value) > -1)
		    });
		});
	}
	
	$(document).ready(function(){
		modulo.atendimento.inicialize();
	});
	
	return modulo;
})(MODULO || {}, jQuery)