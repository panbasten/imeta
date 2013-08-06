jQuery.imeta.jobEntries.foldersCompare = {
	btn : {

	},
	listeners : {
		compareonlyChange : function(e) {
			// alert(this.value);
			var elId = e.target.id;
			var id = elId.split(".")[0];
			if (this.value == '0' || this.value == '1') {
				$("[id=" + id + ".wildcard]").attr("disabled", true);
				$("[id=" + id + ".comparefilesize]").attr("disabled", false);
				$("[id=" + id + ".comparefilecontent]").attr("disabled", false);
			} else if (this.value == '2') {
				$("[id=" + id + ".wildcard]").attr("disabled", true);
				$("[id=" + id + ".comparefilesize]").attr("disabled", true);
				$("[id=" + id + ".comparefilecontent]").attr("disabled", true);
			} else if (this.value == '3') {
				$("[id=" + id + ".wildcard]").attr("disabled", false);
				$("[id=" + id + ".comparefilesize]").attr("disabled", false);
				$("[id=" + id + ".comparefilecontent]").attr("disabled", false);
			}
		}
	}
};