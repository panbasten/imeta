jQuery.imeta.steps.xbaseinput = {
	listeners : {
		acceptingFilenamesListeners : function(e, v) {
			var elId = e.target.id;
			var id = elId.split(".")[0];
			if (e.target.checked) {
				$("[id=" + id + ".dbfFileName]").attr("disabled", true);
			} else {
				$("[id=" + id + ".dbfFileName]").attr("disabled", false);
			}
		},
		rowNrAddedListeners : function(e, v) {
			var elId = e.target.id;
			var id = elId.split(".")[0];
			if (e.target.checked) {
				$("[id=" + id + ".rowNrField]").attr("disabled", false);
			} else {
				$("[id=" + id + ".rowNrField]").attr("disabled", true);
			}
		},
		IncludeFilenameListeners : function(e, v) {
			var elId = e.target.id;
			var id = elId.split(".")[0];
			if (e.target.checked) {
				$("[id=" + id + ".filenameField]").attr("disabled", false);
			} else {
				$("[id=" + id + ".filenameField]").attr("disabled", true);
			}
		}
	}
};