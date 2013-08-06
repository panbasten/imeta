jQuery.imeta.jobEntries.getpop = {
	btn : {},
	listeners : {
		getpopChange : function(e) {
			var elId = e.target.id;
			var id = elId.split(".")[0];
			if (this.value == '0') {
				$("[id=" + id + ".firstmails]").attr("disabled", true);
			} else if (this.value == '1') {
				$("[id=" + id + ".firstmails]").attr("disabled", true);
			} else if (this.value == '2') {
				$("[id=" + id + ".firstmails]").attr("disabled", false);
			}
		},
		usessl : function(e, v) {
			var elId = e.target.id;
			var id = elId.split(".")[0];
			if (e.target.checked) {
				$("[id=" + id + ".sslport]").attr("disabled", false);
			} else {
				$("[id=" + id + ".sslport]").attr("disabled", true);
			}
		}
	}
};