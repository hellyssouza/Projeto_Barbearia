var login = (function($) {
	return {
		inicialize : function() {
			if ($("#alertaErro").is(":visible")) {
				setTimeout(function() {
					$("#alertaErro").hide();
				}, 10000);
			}

			if ($("#alertaLogaut").is(":visible")) {
				setTimeout(function() {
					$("#alertaLogaut").hide();
				}, 10000);
			}
		}
	}
})($);

login.inicialize();