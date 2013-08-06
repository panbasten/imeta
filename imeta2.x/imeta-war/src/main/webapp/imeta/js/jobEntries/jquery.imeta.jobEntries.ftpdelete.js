jQuery.imeta.jobEntries.ftpdelete = {
	btn : {},
	listeners : {
		ftpdeleteChange : function(e) {
			var elId = e.target.id;
			var id = elId.split(".")[0];
			if (this.value == '0') {
				$("[id=" + id + ".nr_limit_success]").attr("disabled", true);
			} else {
				$("[id=" + id + ".nr_limit_success]").attr("disabled", false);
			}
		},
		protocolChange : function(e) {
			var elId = e.target.id;
			var id = elId.split(".")[0];
			if (this.value == '2') {
				$("[id=" + id + ".publicpublickey]").attr("disabled", true);
				$("[id=" + id + ".keyFilename]").attr("disabled", true);
				$("[id=" + id + ".keyFilePass]").attr("disabled", true);
			} else {
				$("[id=" + id + ".publicpublickey]").attr("disabled", false);
				$("[id=" + id + ".keyFilename]").attr("disabled", false);
				$("[id=" + id + ".keyFilePass]").attr("disabled", false);
			}
		},
		useproxy : function(e, v) {
			var elId = e.target.id;
			var id = elId.split(".")[0];
			if (e.target.checked) {
				$("[id=" + id + ".proxyHost]").attr("disabled", false);
				$("[id=" + id + ".proxyPort]").attr("disabled", false);
				$("[id=" + id + ".proxyUsername]").attr("disabled", false);
				$("[id=" + id + ".proxyPassword]").attr("disabled", false);
			} else {
				$("[id=" + id + ".proxyHost]").attr("disabled", true);
				$("[id=" + id + ".proxyPort]").attr("disabled", true);
				$("[id=" + id + ".proxyUsername]").attr("disabled", true);
				$("[id=" + id + ".proxyPassword]").attr("disabled", true);
			}
		},
		publicpublickey : function(e, v) {
			var elId = e.target.id;
			var id = elId.split(".")[0];
			if (e.target.checked) {
				$("[id=" + id + ".keyFilename]").attr("disabled", false);
				$("[id=" + id + ".keyFilePass]").attr("disabled", false);
			} else {
				$("[id=" + id + ".keyFilename]").attr("disabled", true);
				$("[id=" + id + ".keyFilePass]").attr("disabled", true);
			}
		},
		copyprevious : function(e, v) {
			var elId = e.target.id;
			var id = elId.split(".")[0];
			if (e.target.checked) {
				$("[id=" + id + ".ftpDirectory]").attr("disabled", false);
				$("[id=" + id + ".wildcard]").attr("disabled", false);
			} else {
				$("[id=" + id + ".ftpDirectory]").attr("disabled", true);
				$("[id=" + id + ".wildcard]").attr("disabled", true);
			}
		}
	}
};