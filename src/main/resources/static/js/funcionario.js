(function($){
	var botaoAdicionar = $("#adicionar");
	var botaoRemover = $("#remover");
	var botaoSalvar = $("#salvar");
	var tabela = $("#tabela tbody");
	
	var adicioneItemNaTabela = function(objeto){
		var colunaId = "<th scope='row'>" + objeto.Id + "</th>";
		
		var colunaNome = "<td>" + objeto.Nome + "</td>";
		
		var colunaCargo = "<td>" + objeto.Cargo + "</td>";
		
		var linha = "<tr name='"+ "linha_" + objeto.Id +"'>" + colunaId + colunaNome + colunaCargo + "</tr>"
		
		$("#tabela-funcionarios > tbody").append(linha);
		
		var selectorLinha = "[name='" + "linha_" + objeto.Id + "']";
		
		$(selectorLinha).on("click", function(){
			$(".selecionado").removeClass("selecionado bg-light");
			
			$(this).addClass("selecionado")
			$(this).addClass("bg-light");
		});
		
		var dados = $("#tabela-funcionarios").data();
		
		var objetos = Object.values(dados); 
		
		objetos.push(objeto);
		
		$("#tabela-funcionarios").data(objetos);
	}
	
	var adicioneNaLista = function(objeto){
		var id = objeto[0];
		
		var opcao = "<option id='opcao_" + id + "'" + "value='" + id + "'>" + objeto[1] + "</option>";
		
		$("#funcionarios").append(opcao);
	}
	
	var removaItemDaTabela = function(objetos){
		$("#tabela-funcionarios").removeData();
		
		$("#tabela-funcionarios").data(objetos);
		
		$(".selecionado").remove();
	}
	
	$(botaoRemover).on("click", function(){
		var dados = $("#tabela-funcionarios").data();
		
		var indiceRemover = parseInt($(".selecionado [scope='row']").html());
		
		var objetos = Object.values(dados).filter(x => x.Id !== indiceRemover);
		
		var objeto = { 
				Id : indiceRemover, 
				Nome : $("#nome").val(), 
				Cargo: $("#cargo").val()
		};
		
		$.ajax({
			url: "excluafuncionario",
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
		if($("#nome").val() === ""){
			return;
		}
		
		var objeto = { 
				Id : 0, 
				Nome : $('#funcionarios').find(":selected").text(), 
				Cargo: $("#cargo").val(),
				IdUsuario: $('#funcionarios').find(":selected").val()
		};
		
		var dados = JSON.stringify(objeto);
		
		$.ajax({
			url: "cadastrefuncionario",
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
		$.get("consultefuncionarios", function(dados, status){
			var objetos = JSON.parse(dados);
			
			objetos.forEach(funcionario => adicioneItemNaTabela(funcionario));
		});
		
		$.get("consulteusuarios", function(dados, status){
			var objetos = JSON.parse(dados);
			
			objetos.forEach(usuario => adicioneNaLista(usuario));
		});
	});
})(jQuery)