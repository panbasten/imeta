jQuery.imeta.steps.sqlfileoutput = {
	listeners : {
		addCreattable : function(e, v) {
			var elId = e.target.id;
			var id = elId.split(".")[0];
			if (e.target.checked) {
				$("[id=" + id + ".addTruncatetable]").attr("disabled", true);
			} else {
				$("[id=" + id + ".addTruncatetable]").attr("disabled", false);
			}
		}
	}
};