jQuery.imeta.jobEntries.deleteResultFileNames = {
	btn : {

	},
	listeners : {
		isSpecifywildcardClick : function(e, v) {
			var elId = e.target.id;
			var id = elId.split(".")[0];
			if (e.target.checked) {
				$("[id=" + id + ".wildcard]").attr("disabled", false);
				$("[id=" + id + ".wildcardexclude]").attr("disabled", false);
			} else {
				$("[id=" + id + ".wildcard]").attr("disabled", true);
				$("[id=" + id + ".wildcardexclude]").attr("disabled", true);
			}
		}
	}
};