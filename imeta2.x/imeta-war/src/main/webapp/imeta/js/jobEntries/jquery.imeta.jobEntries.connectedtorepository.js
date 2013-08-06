jQuery.imeta.jobEntries.connectedtorepository = {
	listeners : {
		isspecificrep : function(e, v) {
			var elId = e.target.id;
			var id = elId.split(".")[0];
			if (e.target.checked) {
				$("[id=" + id + ".repname]").attr("disabled", false);

			} else {
				$("[id=" + id + ".repname]").attr("disabled", true);

			}
		},
		isspecificuser : function(e, v) {
			var elId = e.target.id;
			var id = elId.split(".")[0];
			if (e.target.checked) {

				$("[id=" + id + ".username]").attr("disabled", false);

			} else {
				$("[id=" + id + ".username]").attr("disabled", true);

			}
		}
	}
};