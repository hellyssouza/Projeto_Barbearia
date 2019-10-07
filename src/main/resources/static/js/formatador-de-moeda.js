var formatador = (function($){
	var formateParaMoeda = function formateParaMoeda(numero) {
		var numeroAuxiliar = numero + '';
		var neg = false;
		
		if (numeroAuxiliar - (Math.round(numero)) == 0) {
			numeroAuxiliar = numeroAuxiliar + '00';        
	    }
		
		if (numeroAuxiliar.indexOf(".")) {
			numeroAuxiliar = numeroAuxiliar.replace(".", "");
		}

		if (numeroAuxiliar.indexOf("-") == 0) {
			neg = true;
			numeroAuxiliar = numeroAuxiliar.replace("-", "");
		}

		if (numeroAuxiliar.length == 1) numeroAuxiliar = "0" + numeroAuxiliar

		numeroAuxiliar = numeroAuxiliar.replace(/([0-9]{2})$/g, ",$1");
		
		if (numeroAuxiliar.length > 6)
			numeroAuxiliar = numeroAuxiliar.replace(/([0-9]{3}),([0-9]{2}$)/g, ".$1,$2");

		if (numeroAuxiliar.length > 9)
			numeroAuxiliar = numeroAuxiliar.replace(/([0-9]{3}).([0-9]{3}),([0-9]{2}$)/g, ".$1.$2,$3");

		if (numeroAuxiliar.length = 12)
			numeroAuxiliar = numeroAuxiliar.replace(/([0-9]{3}).([0-9]{3}).([0-9]{3}),([0-9]{2}$)/g, ".$1.$2.$3,$4");

		if (numeroAuxiliar.length > 12)
			numeroAuxiliar = numeroAuxiliar.replace(/([0-9]{3}).([0-9]{3}).([0-9]{3}).([0-9]{3}),([0-9]{2}$)/g, ".$1.$2.$3.$4,$5");

		if (numeroAuxiliar.indexOf(".") == 0) numeroAuxiliar = numeroAuxiliar.replace(".", "");
		if (numeroAuxiliar.indexOf(",") == 0) numeroAuxiliar = numeroAuxiliar.replace(",", "0,");

		return (neg ? '-' + numeroAuxiliar : numeroAuxiliar);
	}
	
	$.fn.moeda = function(){
		$(this).focusout(function(){
			$(this).val(formateParaMoeda($(this).val()));
		});
		
		return this;
	};
	
	return { formateParaMoeda : formateParaMoeda };
})(jQuery)
