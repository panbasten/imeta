jQuery.imeta.steps.xslt = {
	btn : {},
	listeners : {
		xslFileFieldUse : function(e, v) {
			var elId = e.target.id;
			var id = elId.split(".")[0];
			if (e.target.checked) {
				$("[id=" + id + ".xslFileField]").attr("disabled", false);
				$("[id=" + id + ".xslFilename]").attr("disabled", true);
			} else {
				$("[id=" + id + ".xslFileField]").attr("disabled", true);
				$("[id=" + id + ".xslFilename]").attr("disabled", false);
			}
		}
	}
};