jQuery.imeta.jobEntries.ping = {
	pingChange : function(e) {
		var elId = e.target.id;
		var id = elId.split(".")[0];
		if (this.value == 'classicPing') {
			$("[id=" + id + ".timeout]").attr("disabled", true);
			$("[id=" + id + ".nbrPackets]").attr("disabled", false);
		} else if (this.value == 'systemPing') {
			$("[id=" + id + ".timeout]").attr("disabled", false);
			$("[id=" + id + ".nbrPackets]").attr("disabled", true);
		} else if (this.value == 'bothPings') {
			$("[id=" + id + ".timeout]").attr("disabled", false);
			$("[id=" + id + ".nbrPackets]").attr("disabled", false);
		}
	}
};