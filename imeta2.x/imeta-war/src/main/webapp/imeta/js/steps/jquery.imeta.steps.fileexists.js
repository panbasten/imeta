jQuery.imeta.steps.fileexists = {
	listeners : {
		includefiletype : function(e, v) {
			var elId = e.target.id;
			var id = elId.split(".")[0];
			if (!e.target.checked) {
				$("[id=" + id + ".filetypefieldname]").attr("disabled", true);
			} else {
				$("[id=" + id + ".filetypefieldname]").attr("disabled", false);
			}
		}
	}
};