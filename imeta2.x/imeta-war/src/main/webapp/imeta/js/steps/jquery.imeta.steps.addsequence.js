jQuery.imeta.steps.addsequence = {
	btn : {},
	listeners : {
		useDatabase : function(e, v) {
			var elId = e.target.id;
			var id = elId.split(".")[0];
			if (e.target.checked) {
				$("[id=" + id + ".connectionId]").attr("disabled", false);
				$("[id=" + id + ".schemaName]").attr("disabled", false);
				$("[id=" + id + ".sequenceName]").attr("disabled", false);
				$("[id=" + id + ".counterName]").attr("disabled", true);
				$("[id=" + id + ".startAt]").attr("disabled", true);
				$("[id=" + id + ".incrementBy]").attr("disabled", true);
				$("[id=" + id + ".maxValue]").attr("disabled", true);
				$("[id=" + id + ".useCounter]").attr("checked", false);

			} else {
				$("[id=" + id + ".connectionId]").attr("disabled", true);
				$("[id=" + id + ".schemaName]").attr("disabled", true);
				$("[id=" + id + ".sequenceName]").attr("disabled", true);
				$("[id=" + id + ".counterName]").attr("disabled", false);
				$("[id=" + id + ".startAt]").attr("disabled", false);
				$("[id=" + id + ".incrementBy]").attr("disabled", false);
				$("[id=" + id + ".maxValue]").attr("disabled", false);
				$("[id=" + id + ".useCounter]").attr("checked", true);
			}
		},
		useCounter : function(e, v) {
			var elId = e.target.id;
			var id = elId.split(".")[0];
			if (e.target.checked) {
				$("[id=" + id + ".connectionId]").attr("disabled", true);
				$("[id=" + id + ".schemaName]").attr("disabled", true);
				$("[id=" + id + ".sequenceName]").attr("disabled", true);
				$("[id=" + id + ".counterName]").attr("disabled", false);
				$("[id=" + id + ".startAt]").attr("disabled", false);
				$("[id=" + id + ".incrementBy]").attr("disabled", false);
				$("[id=" + id + ".maxValue]").attr("disabled", false);
				$("[id=" + id + ".useCounter]").attr("checked", true);
				$("[id=" + id + ".useDatabase]").attr("checked", false);

			} else {
				$("[id=" + id + ".connectionId]").attr("disabled", false);
				$("[id=" + id + ".schemaName]").attr("disabled", false);
				$("[id=" + id + ".sequenceName]").attr("disabled", false);
				$("[id=" + id + ".counterName]").attr("disabled", true);
				$("[id=" + id + ".startAt]").attr("disabled", true);
				$("[id=" + id + ".incrementBy]").attr("disabled", true);
				$("[id=" + id + ".maxValue]").attr("disabled", true);
				$("[id=" + id + ".useCounter]").attr("checked", false);
				$("[id=" + id + ".useDatabase]").attr("checked", true);
			}
		}
	}
};