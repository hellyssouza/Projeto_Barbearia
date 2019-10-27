(function($){
	var botaoAdicionar = $("#adicionar");
	var botaoRemover = $("#remover");
	var botaoSalvar = $("#salvar");
	var tabela = $("#tabela tbody");
	
	var adicioneItemNaTabela = function(objeto){
		var colunaId = "<th scope='row'>" + objeto.id + "</th>";
		
		var colunaNome = "<td>" + objeto.nome + "</td>";
		
		var colunaValor = "<td>" + accounting.formatMoney(objeto.valor) + "</td>";
		
		var linha = "<tr name='"+ "linha_" + objeto.id +"'>" + colunaId + colunaNome + colunaValor + "</tr>"
		
		$("#tabela-servicos > tbody").append(linha);
		
		var selectorLinha = "[name='" + "linha_" + objeto.id + "']";
		
		$(selectorLinha).on("click", function(){
			$(".selecionado").removeClass("selecionado bg-light");
			
			$(this).addClass("selecionado")
			$(this).addClass("bg-light");
		});
		
		var dados = $("#tabela-servicos").data();
		
		var objetos = Object.values(dados); 
		
		objetos.push(objeto);
		
		$("#tabela-servicos").data(objetos);
	}
	
	var removaItemDaTabela = function(objetos){
		$("#tabela-servicos").removeData();
		
		$("#tabela-servicos").data(objetos);
		
		$(".selecionado").remove();
	}
	
	var limpeCampos = function(){
		$("#nome").val("");
		$("#valor").val("");
	}
	
	var valideCampos = function(){
		if($("#nome").val() === "" || 
		   $("#valor").val() === "")
		{
			$.notify("Existem campos que não foram preenchidos!", { className: 'error', position: "top lefth" });
			
			return true;
		}
		
		return false;
	}
	
	$(botaoRemover).on("click", function(){
		var dados = $("#tabela-servicos").data();
		
		var indiceRemover = parseInt($(".selecionado [scope='row']").html());
		
		var objetos = Object.values(dados).filter(x => x.Id !== indiceRemover);
		
		var objeto = { 
				id : indiceRemover
		};
		
		$.ajax({
			url: "excluaservico",
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
		if(valideCampos()){
			return;
		}
		
		var objeto = { 
				id : 0, 
				nome : $('#nome').val(), 
				valor: accounting.unformat($("#valor").val())
		};
		
		var dados = JSON.stringify(objeto);
		
		$.ajax({
			url: "cadastreservico",
			dataType: "json",
			method: "POST",
			contentType: "application/json",
			processData: false,
			data: dados,
			success: function(resultado){
				adicioneItemNaTabela(resultado);
				limpeCampos();
			},
			error: function(erro){
				
			}
		});
	});
	
	$(document).ready(function(){
		$("#valor").focusout(function(){
			$("#valor").val(accounting.formatMoney($("#valor").val()));
		});
		
		$.get("consulteservicos", function(dados, status){
			var objetos = JSON.parse(dados);
			
			objetos.forEach(servico => adicioneItemNaTabela(servico));
		});
		
		$("#filtro").on("keyup", function() {
		    var value = $(this).val().toLowerCase();
		    
		    $("#tabela-servicos tbody tr").filter(function() {
		      $(this).toggle($(this).text().toLowerCase().indexOf(value) > -1)
		    });
		});
	});
})(jQuery)