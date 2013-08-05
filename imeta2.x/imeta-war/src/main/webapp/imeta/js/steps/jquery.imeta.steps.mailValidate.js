jQuery.imeta.steps.mailValidate = {
	btn : {

	},
	listeners : {
		smtpCheckClick : function(e, v) {
			var elId = e.target.id;
			var id = elId.split(".")[0];
			if (e.target.checked) {
				$("[id=" + id + ".timeout]").attr("disabled", false);
				$("[id=" + id + ".emailSender]").attr("disabled", false);
				$("[id=" + id + ".defaultSMTP]").attr("disabled", false);
				$("[id=" + id + ".isdynamicDefaultSMTP]").attr("disabled",
						false);
			} else {
				$("[id=" + id + ".timeout]").attr("disabled", true);
				$("[id=" + id + ".emailSender]").attr("disabled", true);
				$("[id=" + id + ".defaultSMTP]").attr("disabled", true);
				$("[id=" + id + ".isdynamicDefaultSMTP]")
						.attr("disabled", true);
			}
		},
		isDynamicSMTPClick : function(e, v) {
			var elId = e.target.id;
			var id = elId.split(".")[0];
			if (e.target.checked) {
				$("[id=" + id + ".defaultSMTPField]").attr("disabled", false);
			} else {
				$("[id=" + id + ".defaultSMTPField]").attr("disabled", true);
			}
		},
		isResultAsStringClick : function(e, v) {
			var elId = e.target.id;
			var id = elId.split(".")[0];
			if (e.target.checked) {
				$("[id=" + id + ".emailValideMsg]").attr("disabled", false);
				$("[id=" + id + ".emailNotValideMsg]").attr("disabled", false);
			} else {
				$("[id=" + id + ".emailValideMsg]").attr("disabled", true);
				$("[id=" + id + ".emailNotValideMsg]").attr("disabled", true);
			}
		}
	}
};