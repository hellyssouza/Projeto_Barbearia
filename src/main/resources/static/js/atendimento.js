(function($){
	var data = document.getElementById("data");
	var botaoAdicionar = $("#adicionar");
	var botaoRemover = $("#remover");
	var botaoSalvar = $("#salvar");
	var tabela = $("#tabela tbody");
	
	var adicioneItemNaTabela = function(objeto){
		var dataEHora = objeto.DataEHorario.replace("T"," ").split(" ");
		
		var funcionarios = Object.values(JSON.parse($("#tabela-horarios").data("FUNCIONARIOS")));
		
		var colunaId = "<th scope='row'>" + objeto.Id + "</th>";
		
		var dataFormatada = dataEHora[0].split("-");
		
		dataFormatada = dataFormatada[2] + "/" + dataFormatada[1] + "/" + dataFormatada[0];
		
		var horaFormatada = dataEHora[1].substr(0, 5);
		
		var colunaData = "<td>" + dataFormatada + "</td>";
		
		var colunaHora = "<td>" + horaFormatada + "</td>";
		
		var colunaValor = "<td>" + accounting.formatMoney(objeto.Valor) + "</td>";
		
		var colunaFuncionario = "<td>" + funcionarios.filter(x=> x.Id == objeto.Funcionario)[0].Nome + "</td>";
		
		var colunaStatus = "";
		
		if(objeto.Usuario === null)
		{
			colunaStatus = "<td><span class='badge badge-info'>Livre</span></td>";
		}
		else
		{
			colunaStatus = "<td><span class='badge badge-warning'>Agendado</span></td>";
		}
		
		var linha = "<tr name='"+ "linha_" + objeto.Id +"'>" + colunaId + colunaData + colunaHora + colunaFuncionario + colunaValor + colunaStatus +"</tr>";
		
		$("#tabela-horarios > tbody").append(linha);
		
		var selectorLinha = "[name='" + "linha_" + objeto.Id + "']";
		
		$(selectorLinha).on("click", function(){
			$(".selecionado").removeClass("selecionado bg-light");
			
			$(this).addClass("selecionado")
			$(this).addClass("bg-light");
		});
		
		var dados = $("#tabela-horarios").data();
		
		var objetos = Object.values(dados); 
		
		objetos.push(objeto);
		
		$("#tabela-horarios").data(objetos);
	}
	
	var adicioneNaComboBox = function(objeto){
		var opcaoNome = "<option id='funcionario_" + objeto.Id + "' value='" + objeto.Id + "'>" + objeto.Nome + "</option>";
		
		$("#funcionarios").append(opcaoNome);
	}
	
	var removaItemDaTabela = function(objetos){
		$("#tabela-horarios").removeData();
		
		$("#tabela-horarios").data(objetos);
		
		$(".selecionado").remove();
	}
	
	var limpaCampos = function(){
		$("#data").val("");
		$("#horario-inicio").val("");
		$("#horario-fim").val("");
		$("#periodo").val("");
		$("#funcionarios option:first").attr("selected","selected");
	}
	
	var valideCampos = function(){
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
	
	$(botaoRemover).on("click", function(){
		var dados = $("#tabela-horarios").data();
		
		var indiceRemover = parseInt($(".selecionado [scope='row']").html());
		
		var objetos = Object.values(dados).filter(x => x.Id !== indiceRemover);
		
		var objeto = { 
				Id : indiceRemover, 
				DataEHorario : new Date(data.value),
				Funcionario: $("#funcionarios").children("option:selected").val()
		};
		
		$.ajax({
			url: "excluaatendimento",
			method: "POST",
			contentType: "application/json",
			processData: false,
			data: JSON.stringify(objeto),
			success: function(){
				removaItemDaTabela(objetos);
			},
			error: function(erro){
			}
		});
	});
	
	$(botaoSalvar).on("click", function(){
		if(valideCampos())
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
				Funcionario: $("#funcionarios").children("option:selected").val()
		};
		
		var dados = JSON.stringify(objeto);
		
		$.ajax({
			url: "cadastreatendimento",
			dataType: "json",
			method: "POST",
			contentType: "application/json",
			processData: false,
			data: dados,
			success: function(dados){
				dados.forEach(atendimento => adicioneItemNaTabela(atendimento));
				limpaCampos();
			},
			error: function(erro){
				$("#mensagem").html(erro.responseText);
				$("#modal-alerta").modal("show");
			}
		});
	});
	
	$(document).ready(function(){
		$.get("consultefuncionarios", function(dados, status){
			var objetos = JSON.parse(dados);
			
			$("#tabela-horarios").data("FUNCIONARIOS", JSON.stringify(objetos));
			
			objetos.forEach(funcionario => adicioneNaComboBox(funcionario));
			
			$.get("consulteatendimentos", function(dados, status){
				var objetos = JSON.parse(dados);
				
				objetos.forEach(atendimento => adicioneItemNaTabela(atendimento));
			});
		});
	});
})(jQuery)