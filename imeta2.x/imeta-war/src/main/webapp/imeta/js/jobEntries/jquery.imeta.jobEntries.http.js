jQuery.imeta.jobEntries.http = {
	listeners : {
		isRunForEveryRowClick : function(e, v) {
			var elId = e.target.id;
			var id = elId.split(".")[0];
			if (e.target.checked) {
				$("[id=" + id + ".url]").attr("disabled", true);
				$("[id=" + id + ".urlFieldname]").attr("disabled", false);
			} else {
				$("[id=" + id + ".url]").attr("disabled", false);
				$("[id=" + id + ".urlFieldname]").attr("disabled", true);
			}
		},
		isDateTimeAddedClick : function(e, v) {
			var elId = e.target.id;
			var id = elId.split(".")[0];
			if (e.target.checked) {
				$("[id=" + id + ".fileAppended]").attr("disabled", true);
				$("[id=" + id + ".targetFilenameExtention]").attr("disabled",
						false);
			} else {
				$("[id=" + id + ".fileAppended]").attr("disabled", false);
				$("[id=" + id + ".targetFilenameExtention]").attr("disabled",
						true);
			}
		}
	},
	btn : {

	}
};