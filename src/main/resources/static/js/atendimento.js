(function($){
	var data= document.getElementById("data");
	var botaoAdicionar = $("#adicionar");
	var botaoRemover = $("#remover");
	var botaoSalvar = $("#salvar");
	var tabela = $("#tabela tbody");
	
	var adicioneItemNaTabela = function(objeto){
		var dataEHora = objeto.DataEHorario.split("T");
		
		var colunaId = "<th scope='row'>" + objeto.Id + "</th>";
		
		var dataFormatada = dataEHora[0].split("-");
		
		dataFormatada = dataFormatada[2] + "/" + dataFormatada[1] + "/" + dataFormatada[0];
		
		var colunaData = "<td>" + dataFormatada + "</td>";
		
		var colunaHora = "<td>" + dataEHora[1] + "</td>";
		
		var colunaQtd = "<td><span class='badge badge-secondary'>0</span></td>";
		
		var linha = "<tr name='"+ "linha_" + objeto.Id +"'>" + colunaId + colunaData + colunaHora + colunaQtd + "</tr>"
		
		$("#tabela-horarios > tbody").append(linha);
		
		var selectorLinha = "[name='" + "linha_" + objeto.Id + "']";
		
		$(selectorLinha).on("click", function(){
			$(".selecionado").removeClass("selecionado bg-info");
			
			$(this).addClass("selecionado")
			$(this).addClass("bg-info");
		});
		
		var dados = $("#tabela-horarios").data();
		
		var objetos = Object.values(dados); 
		
		objetos.push(objeto);
		
		$("#tabela-horarios").data(objetos);
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
		
		var objeto = { Id : indiceRemover, DataEHorario : new Date(data.value)};
		
		$.ajax({
			url: "exclua",
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
		
		var objeto = { Id : 0, DataEHorario : new Date(data.value)};
		
		var dados = JSON.stringify(objeto);
		
		$.ajax({
			url: "cadastre",
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
})(jQuery)