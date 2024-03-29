(function($) {
	var apresenteMensagem = function(objeto){
		var mensagemSucesso = "";
		var linkParaVoltarLogin = "";
		var container = "";
		
		if(objeto.cadastrado)
		{
			mensagemSucesso = "<div id='mensagem' class='alert alert-info'>Usuário cadastrado com sucesso!</div>";
			linkParaVoltarLogin = "<a href='/login' class='badge badge-info'>Voltar</a>";
			container = "<div class='text-center'>" + "<br>" + mensagemSucesso + linkParaVoltarLogin + "</div>";
		}
		else
		{
			mensagemSucesso = "<div id='mensagem' class='alert alert-danger'>Usuário não cadastrado!</div>";
			linkParaVoltarLogin = "<a href='/login' class='badge badge-info'>Voltar</a>";
			container = "<div class='text-center'>" + "<br>" + mensagemSucesso + linkParaVoltarLogin + "</div>";
		}
		
		$("#salvar").after(container);
		
		if ($("#mensagem").is(":visible")) {
			setTimeout(function() {
				$("#mensagem").hide();
			}, 10000);
		}
	}
	
	
	$("#salvar").on("click", function() {
		var nome = $("[name='nome']").val();
		var login = $("[name='login']").val();
		var senha = $("[name='senha']").val();

		if (nome === "" || login === "" || senha === "") {
			return;
		}
		
		var objeto = {
			id : 0,
			nome : nome,
			login : login,
			senha : senha,
			ativo : 1
		};
		
		$.ajax({
			url : "cadastreusuario",
			method : "POST",
			dataType: "json",
			contentType : "application/json",
			processData : false,
			data : JSON.stringify(objeto),
			success : function(retorno) {
				var objeto = JSON.parse(retorno);
				
				apresenteMensagem(objeto);
			},
			error : function(erro) {
			}
		});
	});
})(jQuery)