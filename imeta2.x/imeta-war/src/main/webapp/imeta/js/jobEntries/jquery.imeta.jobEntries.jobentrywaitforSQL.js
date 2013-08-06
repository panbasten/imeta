jQuery.imeta.jobEntries.jobentrywaitforSQL = {
	listeners : {
		iscustomSQLListeners : function(e, v) {
			var elId = e.target.id;
			var id = elId.split(".")[0];
			if (e.target.checked) {
				$("[id=" + id + ".schemaname]").attr("disabled", true);
				$("[id=" + id + ".tablename]").attr("disabled", true);
				$("[id=" + id + ".isUseVars]").attr("disabled", false);
				$("[id=" + id + ".isClearResultList]").attr("disabled", false);
				$("[id=" + id + ".isAddRowsResult]").attr("disabled", false);
				// $("[id="+id+".getSqlSelect]").attr("disabled",false);
				$("[id=" + id + ".customSQL]").attr("disabled", false);

			} else {
				$("[id=" + id + ".schemaname]").attr("disabled", false);
				$("[id=" + id + ".tablename]").attr("disabled", false);
				$("[id=" + id + ".isUseVars]").attr("disabled", true);
				$("[id=" + id + ".isClearResultList]").attr("disabled", true);
				$("[id=" + id + ".isAddRowsResult]").attr("disabled", true);
				// $("[id="+id+".getSqlSelect]").attr("disabled",true);
				$("[id=" + id + ".customSQL]").attr("disabled", true);
			}
		}
	}
};