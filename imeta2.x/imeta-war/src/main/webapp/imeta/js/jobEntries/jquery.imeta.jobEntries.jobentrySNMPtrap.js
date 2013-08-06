jQuery.imeta.jobEntries.jobentrySNMPtrap = {
	listeners : {
		targettypeChange : function(e) {
			// alert(this.value);
			var elId = e.target.id;
			var id = elId.split(".")[0];
			if (this.value == 'user') {
				$("[id=" + id + ".comString]").attr("disabled", false);
				$("[id=" + id + ".user]").attr("disabled", true);
				$("[id=" + id + ".passphrase]").attr("disabled", true);
				$("[id=" + id + ".engineid]").attr("disabled", true);
			} else {
				$("[id=" + id + ".comString]").attr("disabled", true);
				$("[id=" + id + ".user]").attr("disabled", false);
				$("[id=" + id + ".passphrase]").attr("disabled", false);
				$("[id=" + id + ".engineid]").attr("disabled", false);
			}
		}
	}
};