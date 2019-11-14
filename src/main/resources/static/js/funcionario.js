(function($){
	var botaoAdicionar = $("#adicionar");
	var botaoRemover = $("#remover");
	var botaoSalvar = $("#salvar");
	var tabela = $("#tabela tbody");
	
	var adicioneItemNaTabela = function(objeto){
		debugger;
		var colunaId = "<th scope='row'>" + objeto.Id + "</th>";
		
		var colunaNome = "<td>" + objeto.Nome + "</td>";
		
		var colunaCargo = "<td>" + objeto.Cargo + "</td>";
		
		var colunaPorcentagem = "<td>" + objeto.Porcentagem + "%" + "</td>";
		
		var linha = "<tr name='"+ "linha_" + objeto.Id +"'>" + colunaId + colunaNome + colunaCargo + colunaPorcentagem + "</tr>"
		
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
	
	var preenchaListaDeUsuarios = function(){
		$.get("consulteusuarios", function(dados, status){
			var objetos = JSON.parse(dados);
			
			objetos.forEach(usuario => adicioneNaLista(usuario));
		});
	}
	
	var removaItemDaTabela = function(objetos){
		$("#tabela-funcionarios").removeData();
		
		$("#tabela-funcionarios").data(objetos);
		
		$(".selecionado").remove();
	}
	
	var removaTodosUsuariosEAdicioneNovamente = function(){
		$("#funcionarios").empty();
		preenchaListaDeUsuarios();
	}
	
	var limpeCampos = function(){
		$("#funcionarios option:first").attr("selected","selected");
		$("#cargo").val("");
	}
	
	var valideCampos = function(){
		if($("#funcionarios").children("option:selected").val() === "" || 
		   $("#cargo").val() === "" || $("#porcentagem").val() === "")
		{
			$.notify("Existem campos que não foram preenchidos!", { className: 'error', position: "top lefth" });
			
			return true;
		}
		
		return false;
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
				removaTodosUsuariosEAdicioneNovamente();
			},
			error: function(erro){
			}
		});
	});
	
	$(botaoSalvar).on("click", function(){
		if(valideCampos()){
			return;
		}
		
		var objeto = { 
				Id : 0, 
				Nome : $('#funcionarios').find(":selected").text(), 
				Cargo: $("#cargo").val(),
				IdUsuario: $('#funcionarios').find(":selected").val(),
				Porcentagem: parseInt($('#porcentagem').val())
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
				removaTodosUsuariosEAdicioneNovamente();
				limpeCampos();
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
		
		preenchaListaDeUsuarios();
		
		$("#filtro").on("keyup", function() {
		    var value = $(this).val().toLowerCase();
		    
		    $("#tabela-funcionarios tbody tr").filter(function() {
		      $(this).toggle($(this).text().toLowerCase().indexOf(value) > -1)
		    });
		});
	});
})(jQuery)