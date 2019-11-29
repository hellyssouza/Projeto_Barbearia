var MODULO = (function(modulo, $){
	modulo.relatorioatendimento = {};
	modulo.relatorioatendimento.funcionarios = {};
	modulo.relatorioatendimento.comboFuncionarios = $("#funcionarios");
	modulo.relatorioatendimento.comboPagamento = $("#pagamento");
	modulo.relatorioatendimento.comboStatus = $("#status");
	modulo.relatorioatendimento.botaoSalvar = $("#salvar");
	modulo.relatorioatendimento.usuarioDoContexto = {};
	
	modulo.relatorioatendimento.adicioneNaComboBox = function(objeto)
	{
		var opcaoNome = "<option id='funcionario_" + objeto.Id + "' value='" + objeto.Id + "'>" + objeto.Nome + "</option>";
		
		$(modulo.relatorioatendimento.comboFuncionarios).append(opcaoNome);
	}
	
	modulo.relatorioatendimento.camposNaoEstaoPreenchidos = function()
	{
		if(document.getElementById("dataDeInicio").value === "" ||
		  document.getElementById("dataDeFim").value === "" ||
		  $(modulo.relatorioatendimento.comboFuncionarios).children("option:selected").val() === "" || 
		  $(modulo.relatorioatendimento.comboStatus).children("option:selected").val() === "" ||
		  ($(".agrupador-pagamento").is(":visible") && $(modulo.relatorioatendimento.comboPagamento).children("option:selected").val() === ""))
		{
			return true;
		}
		
		return false;
	}
	
	$(document).ready(function(){
		$(".agrupador-pagamento").hide();
		
		$.get("consulteusuariocontexto", function(usuario, status){
			modulo.relatorioatendimento.usuarioDoContexto = JSON.parse(usuario);
			
			$.get("consultefuncionarios", function(dados, status){
				if(modulo.relatorioatendimento.usuarioDoContexto.funcionario)
				{
					modulo.relatorioatendimento.funcionarios = Object.values(JSON.parse(dados)).filter(x => x.Id == modulo.relatorioatendimento.usuarioDoContexto.funcionario);
				}
				else
				{
					modulo.relatorioatendimento.funcionarios = Object.values(JSON.parse(dados));
				}
				
				modulo.relatorioatendimento.funcionarios.forEach(funcionario => {
					modulo.relatorioatendimento.adicioneNaComboBox(funcionario);
				});
			});
		});
		
		$(modulo.relatorioatendimento.comboStatus).on("change", function(){
			if($(this).val() === '3')
			{
				$(".agrupador-pagamento").show();
			}
			else
			{
				$(".agrupador-pagamento").hide();
			}
		});
		
		$(modulo.relatorioatendimento.botaoSalvar).on("click", function() {
			if(modulo.relatorioatendimento.camposNaoEstaoPreenchidos())
			{
				$.notify("Existem campos que não foram preenchidos!", { className: 'error', position: "top lefth" });
				
				return;
			}
			
			var xhr = new XMLHttpRequest();
			xhr.open('POST', 'gererelatorio', true);
			xhr.setRequestHeader('Content-Type', 'application/json');
		    xhr.responseType = 'arraybuffer';
		    
		    xhr.onload = function(e) {
		       if (this.status == 200) {
		          var blob = new Blob([this.response], {type:"application/pdf"});
		          var link = document.createElement('a');
		          link.href = window.URL.createObjectURL(blob);
		          link.download = "atendimentos.pdf";
		          link.click();
		       }
		    };
		    
		    var filtros = {
		    		dataDeInicio : document.getElementById("dataDeInicio").value,
		    		dataDeFim: document.getElementById("dataDeFim").value,
		    		funcionario: $(modulo.relatorioatendimento.comboFuncionarios).children("option:selected").val(),
		    		status: $(modulo.relatorioatendimento.comboStatus).children("option:selected").val(),
		    		pagamento: $(".agrupador-pagamento").is(":visible") ? $(modulo.relatorioatendimento.comboPagamento).children("option:selected").val() : 0
		    };
		    
		    xhr.send(JSON.stringify(filtros));
		});
	});
})(MODULO || {}, jQuery)