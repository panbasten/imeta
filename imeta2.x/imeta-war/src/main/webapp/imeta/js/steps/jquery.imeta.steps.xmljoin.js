jQuery.imeta.steps.xmljoin = {
	btn : {

	},
	listeners : {
		isComplexJoinClick : function(e, v) {
			var elId = e.target.id;
			var id = elId.split(".")[0];
			if (e.target.checked) {
				$("[id=" + id + ".joinCompareField]").attr("disabled", false);
			} else {
				$("[id=" + id + ".joinCompareField]").attr("disabled", true);
			}
		}
	}
};