jQuery.imeta.jobEntries.folderisempty = {
	btn : {},
	listeners : {
		specifywildcard : function(e, v) {
			var elId = e.target.id;
			var id = elId.split(".")[0];
			if (e.target.checked) {
				$("[id=" + id + ".wildcard]").attr("disabled", false);
			} else {
				$("[id=" + id + ".wildcard]").attr("disabled", true);
			}
		}
	}
};