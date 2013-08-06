jQuery.imeta.steps.splitfieldtorows = {
	btn : {},
	listeners : {
		includeRowNumber : function(e, v) {
			var elId = e.target.id;
			var id = elId.split(".")[0];
			if (e.target.checked) {
				$("[id=" + id + ".rowNumberField]").attr("disabled", false);
				$("[id=" + id + ".resetRowNumber]").attr("disabled", false);
			} else {
				$("[id=" + id + ".rowNumberField]").attr("disabled", true);
				$("[id=" + id + ".resetRowNumber]").attr("disabled", true);
			}
		}
	}
};