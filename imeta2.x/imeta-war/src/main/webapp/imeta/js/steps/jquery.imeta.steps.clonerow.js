jQuery.imeta.steps.clonerow = {
	btn : {},
	listeners : {
		nrcloneinfield : function(e, v) {
			var elId = e.target.id;
			var id = elId.split(".")[0];
			if (!e.target.checked) {
				$("[id=" + id + ".nrclones]").attr("disabled", false);
				$("[id=" + id + ".nrclonefield]").attr("disabled", true);
			} else {
				$("[id=" + id + ".nrclones]").attr("disabled", true);
				$("[id=" + id + ".nrclonefield]").attr("disabled", false);
			}
		},
		addcloneflag : function(e, v) {
			var elId = e.target.id;
			var id = elId.split(".")[0];
			if (e.target.checked) {
				$("[id=" + id + ".cloneflagfield]").attr("disabled", false);
			} else {
				$("[id=" + id + ".cloneflagfield]").attr("disabled", true);
			}
		}
	}
};