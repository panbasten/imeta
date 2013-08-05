jQuery.imeta.steps.xsd = {
	btn : {

	},
	listeners : {
		outStringFieldClick : function(e, v) {
			var elId = e.target.id;
			var id = elId.split(".")[0];
			if (e.target.checked) {
				$("[id=" + id + ".ifXmlValid]").attr("disabled", false);
				$("[id=" + id + ".ifXmlInvalid]").attr("disabled", false);

			} else {
				$("[id=" + id + ".ifXmlValid]").attr("disabled", true);
				$("[id=" + id + ".ifXmlInvalid]").attr("disabled", true);
			}
		},
		addMsgValidationClick : function(e, v) {
			var elId = e.target.id;
			var id = elId.split(".")[0];
			if (e.target.checked) {
				$("[id=" + id + ".validationMessageField]").attr("disabled",
						false);
			} else {
				$("[id=" + id + ".validationMessageField]").attr("disabled",
						true);
			}
		},
		xsdSourceChange : function(e) {
			// alert(this.value);
			var elId = e.target.id;
			var id = elId.split(".")[0];
			if (this.value == 'filename') {
				$("[id=" + id + ".xsdFileName]").attr("disabled", false);
				$("[id=" + id + ".xsdDefinedField]").attr("disabled", true);
			} else if (this.value == 'fieldname') {
				$("[id=" + id + ".xsdFileName]").attr("disabled", true);
				$("[id=" + id + ".xsdDefinedField]").attr("disabled", false);
			} else if (this.value == 'noneed') {
				$("[id=" + id + ".xsdFileName]").attr("disabled", true);
				$("[id=" + id + ".xsdDefinedField]").attr("disabled", true);
			}
		}
	}
};