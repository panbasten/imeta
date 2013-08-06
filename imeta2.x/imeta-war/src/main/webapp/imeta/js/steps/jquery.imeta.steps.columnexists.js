jQuery.imeta.steps.columnexists = {
	listeners : {
		istablenameInfield : function(e, v) {
			var elId = e.target.id;
			var id = elId.split(".")[0];
			if (e.target.checked) {
				$("[id=" + id + ".tablename]").attr("disabled", true);
				$("[id=" + id + ".tablenamefield]").attr("disabled", false);

			} else {
				$("[id=" + id + ".tablename]").attr("disabled", false);
				$("[id=" + id + ".tablenamefield]").attr("disabled", true);

			}
		}
	}

};