jQuery.imeta.jobEntries.special = {
	schedulerTypeChange : function(e, v) {
		var elId = e.target.id;
		var id = elId.split(".")[0];
		var value = e.target.value;
		switch (value) {
		case "1":
			changeField(false, false, true, true, true);
			break;
		case "2":
			changeField(true, true, false, true, true);
			break;
		case "3":
			changeField(true, true, true, false, true);
			break;
		case "4":
			changeField(true, true, true, true, false);
			break;
		default:
			changeField(true, true, true, true, true);
			break;
		}
		function changeField(intervalSecond, intervalMinute, timeOfDay,
				dayOfWeek, dayOfMonth) {
			$("[id=" + id + ".intervalSecond]")
					.attr("disabled", intervalSecond);
			$("[id=" + id + ".intervalMinute]")
					.attr("disabled", intervalMinute);
			$("[id=" + id + ".timeOfDay]").attr("disabled", timeOfDay);
			$("[id=" + id + ".dayOfWeek]").attr("disabled", dayOfWeek);
			$("[id=" + id + ".dayOfMonth]").attr("disabled", dayOfMonth);
		}
	}
};