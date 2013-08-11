jQuery.imeta.jobEntries.ftp = {
	btn : {
		dbTest : function(e, v) {
			var elId = e.target.id;
			var id = elId.split(".")[0];
			var dbId = 0;
			try {
				dbId = id.split("_")[1];
			} catch (e) {
			}

			$("#" + id).ajaxSubmit( {
				type : "POST",
				url : "ImetaDBAction!testDatabase.action",
				dataType : "json",
				data : {
					id : id,
					databaseId : dbId
				},
				success : function(json) {
					$.imessagebox("#ibody", json);
				}
			});

			e.stopPropagation();
		}

	},
	listeners : {
		ftpChange : function(e) {
			var elId = e.target.id;
			var id = elId.split(".")[0];
			if (this.value == '0') {
				$("[id=" + id + ".nr_limit]").attr("disabled", true);
			} else {
				$("[id=" + id + ".nr_limit]").attr("disabled", false);
			}
		},
		movefiles : function(e, v) {
			var elId = e.target.id;
			var id = elId.split(".")[0];
			if (e.target.checked) {
				$("[id=" + id + ".movetodirectory]").attr("disabled", false);
				$("[id=" + id + ".createmovefolder]").attr("disabled", false);
			} else {
				$("[id=" + id + ".movetodirectory]").attr("disabled", true);
				$("[id=" + id + ".createmovefolder]").attr("disabled", true);
			}
		},
		SpecifyFormat : function(e, v) {
			var elId = e.target.id;
			var id = elId.split(".")[0];
			if (e.target.checked) {
				$("[id=" + id + ".adddate]").attr("disabled", true);
				$("[id=" + id + ".addtime]").attr("disabled", true);
				$("[id=" + id + ".date_time_format]").attr("disabled", false);
				$("[id=" + id + ".AddDateBeforeExtension]").attr("disabled",
						false);
			} else {
				$("[id=" + id + ".adddate]").attr("disabled", false);
				$("[id=" + id + ".addtime]").attr("disabled", false);
				$("[id=" + id + ".date_time_format]").attr("disabled", true);
				$("[id=" + id + ".AddDateBeforeExtension]").attr("disabled",
						true);
			}
		},
		onlyGettingNewFiles : function(e, v) {
			var elId = e.target.id;
			var id = elId.split(".")[0];
			if (e.target.checked) {
				$("[id=" + id + ".SifFileExists]").attr("disabled", false);
			} else {
				$("[id=" + id + ".SifFileExists]").attr("disabled", true);
			}
		}
	}
};