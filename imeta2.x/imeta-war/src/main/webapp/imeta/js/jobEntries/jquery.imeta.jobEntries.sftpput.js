jQuery.imeta.jobEntries.sftpput = {
	btn : {},
	listeners : {
		copyprevious : function(e, v) {
			var elId = e.target.id;
			var id = elId.split(".")[0];
			if (e.target.checked) {
				$("[id=" + id + ".localDirectory]").attr("disabled", false);
				$("[id=" + id + ".wildcard]").attr("disabled", false);
			} else {
				$("[id=" + id + ".localDirectory]").attr("disabled", true);
				$("[id=" + id + ".wildcard]").attr("disabled", true);
			}
		}
	}
};