jQuery.imeta.jobEntries.exportrepository = {
	listeners : {
		SpecifyFormat : function(e, v) {
			var elId = e.target.id;
			var id = elId.split(".")[0];
			if (e.target.checked) {
				$("[id=" + id + ".add_date]").attr("disabled", false);
				$("[id=" + id + ".add_time]").attr("disabled", true);
				$("[id=" + id + ".date_time_format]").attr("disabled", true);
			} else {
				$("[id=" + id + ".add_date]").attr("disabled", true);
				$("[id=" + id + ".add_time]").attr("disabled", false);
				$("[id=" + id + ".date_time_format]").attr("disabled", false);
			}
		}
	}
};