(function($){
	var data = document.getElementById("data");
	var botaoAdicionar = $("#adicionar");
	var botaoRemover = $("#remover");
	var botaoSalvar = $("#salvar");
	var tabela = $("#tabela tbody");
	
	var adicioneItemNaTabela = function(objeto){
		debugger;
		var dataEHora = objeto.DataEHorario.replace("T"," ").split(" ");
		
		var colunaId = "<th scope='row'>" + objeto.Id + "</th>";
		
		var dataFormatada = dataEHora[0].split("-");
		
		dataFormatada = dataFormatada[2] + "/" + dataFormatada[1] + "/" + dataFormatada[0];
		
		var horaFormatada = dataEHora[1].substr(0, 5);
		
		var colunaData = "<td>" + dataFormatada + "</td>";
		
		var colunaHora = "<td>" + horaFormatada + "</td>";
		
		var colunaFuncionario = "<td>" + $("#funcionario option[value='" + objeto.Funcionario + "']").html() + "</td>";
		
		var colunaQtd = "<td><span class='badge badge-secondary'>0</span></td>";
		
		var linha = "<tr name='"+ "linha_" + objeto.Id +"'>" + colunaId + colunaData + colunaHora + colunaFuncionario + colunaQtd +"</tr>";
		
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
		
		$("#funcionario").append(opcaoNome);
	}
	
	var removaItemDaTabela = function(objetos){
		$("#tabela-horarios").removeData();
		
		$("#tabela-horarios").data(objetos);
		
		$(".selecionado").remove();
	}
	
	$(botaoRemover).on("click", function(){
		var dados = $("#tabela-horarios").data();
		
		var indiceRemover = parseInt($(".selecionado [scope='row']").html());
		
		var objetos = Object.values(dados).filter(x => x.Id !== indiceRemover);
		
		var objeto = { 
				Id : indiceRemover, 
				DataEHorario : new Date(data.value),
				Funcionario: $("#funcionario").children("option:selected").val()
		};
		debugger;
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
		if(data.value === ""){
			return;
		}
		
		var objeto = { 
				Id : 0, 
				DataEHorario : new Date(data.value),
				Funcionario: $("#funcionario").children("option:selected").val()
		};
		
		var dados = JSON.stringify(objeto);
		
		$.ajax({
			url: "cadastreatendimento",
			dataType: "json",
			method: "POST",
			contentType: "application/json",
			processData: false,
			data: dados,
			success: function(resultado){
				adicioneItemNaTabela(resultado);
			},
			error: function(erro){
				
			}
		});
	});
	
	$(document).ready(function(){
		$.get("consulteafuncionarios", function(dados, status){
			var objetos = JSON.parse(dados);
			
			objetos.forEach(funcionario => adicioneNaComboBox(funcionario));
		});
		
		$.get("consulteatendimentos", function(dados, status){
			var objetos = JSON.parse(dados);
			
			objetos.forEach(atendimento => adicioneItemNaTabela(atendimento));
		});
	});
})(jQuery)