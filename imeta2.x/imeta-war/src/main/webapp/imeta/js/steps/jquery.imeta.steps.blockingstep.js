jQuery.imeta.steps.blockingstep = {
	btn : {},
	listeners : {
		passAllRows : function(e, v) {
			var elId = e.target.id;
			var id = elId.split(".")[0];
			if (e.target.checked) {
				$("[id=" + id + ".directory]").attr("disabled", false);
				$("[id=" + id + ".prefix]").attr("disabled", false);
				$("[id=" + id + ".cacheSize]").attr("disabled", false);
				$("[id=" + id + ".compressFiles]").attr("disabled", false);
			} else {
				$("[id=" + id + ".directory]").attr("disabled", true);
				$("[id=" + id + ".prefix]").attr("disabled", true);
				$("[id=" + id + ".cacheSize]").attr("disabled", true);
				$("[id=" + id + ".compressFiles]").attr("disabled", true);
			}
		}
	}
};