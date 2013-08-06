jQuery.imeta.jobEntries.copyMoveResultFilename = {
	btn : {

	},
	listeners : {
		isAddDateTimeClick : function(e, v) {
			var elId = e.target.id;
			var id = elId.split(".")[0];
			if (e.target.checked) {
				$("[id=" + id + ".AddDateBeforeExtension]").attr("disabled",
						false);
			} else {
				if (!$("[id=" + id + ".add_date]").attr("checked")
						&& !$("[id=" + id + ".add_time]").attr("checked"))
					$("[id=" + id + ".AddDateBeforeExtension]").attr(
							"disabled", true);
			}
		},
		isSpecifyFormatClick : function(e, v) {
			var elId = e.target.id;
			var id = elId.split(".")[0];
			if (e.target.checked) {
				$("[id=" + id + ".add_date]").attr("disabled", true);
				$("[id=" + id + ".add_date]").attr("checked", false);
				$("[id=" + id + ".add_time]").attr("disabled", true);
				$("[id=" + id + ".add_time]").attr("checked", false);
				$("[id=" + id + ".date_time_format]").attr("disabled", false);
				$("[id=" + id + ".AddDateBeforeExtension]").attr("disabled",
						false);
			} else {
				$("[id=" + id + ".add_date]").attr("disabled", false);
				$("[id=" + id + ".add_time]").attr("disabled", false);
				$("[id=" + id + ".date_time_format]").attr("disabled", true);
				$("[id=" + id + ".AddDateBeforeExtension]").attr("disabled",
						true);
			}
		},
		isSpecifywildcardClick : function(e, v) {
			var elId = e.target.id;
			var id = elId.split(".")[0];
			if (e.target.checked) {
				$("[id=" + id + ".wildcard]").attr("disabled", false);
				$("[id=" + id + ".wildcardexclude]").attr("disabled", false);
			} else {
				$("[id=" + id + ".wildcard]").attr("disabled", true);
				$("[id=" + id + ".wildcardexclude]").attr("disabled", true);
			}
		},
		success_conditionChange : function(e) {
			// alert(this.value);
			var elId = e.target.id;
			var id = elId.split(".")[0];
			if (this.value == 'success_if_no_errors') {
				$("[id=" + id + ".nr_errors_less_than]").attr("disabled", true);
			} else {
				$("[id=" + id + ".nr_errors_less_than]").attr("disabled", false);
			}
		}
	}
};