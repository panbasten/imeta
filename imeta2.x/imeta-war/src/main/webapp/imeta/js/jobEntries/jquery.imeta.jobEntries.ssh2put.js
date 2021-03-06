jQuery.imeta.jobEntries.ssh2put = {
	btn : {},
	listeners : {
		ssh2putChange : function(e) {
			var elId = e.target.id;
			var id = elId.split(".")[0];
			if (this.value == '2') {
				$("[id=" + id + ".destinationfolder]").attr("disabled", false);
				$("[id=" + id + ".createDestinationFolder]").attr("disabled",
						false);
			} else {
				$("[id=" + id + ".destinationfolder]").attr("disabled", true);
				$("[id=" + id + ".createDestinationFolder]").attr("disabled",
						true);
			}
		},
		usehttpproxy : function(e, v) {
			var elId = e.target.id;
			var id = elId.split(".")[0];
			if (e.target.checked) {
				$("[id=" + id + ".httpproxyhost]").attr("disabled", false);
				$("[id=" + id + ".httpproxyport]").attr("disabled", false);
				$("[id=" + id + ".useBasicAuthentication]").attr("disabled",
						false);
			} else {
				$("[id=" + id + ".httpproxyhost]").attr("disabled", true);
				$("[id=" + id + ".httpproxyport]").attr("disabled", true);
				$("[id=" + id + ".useBasicAuthentication]").attr("disabled",
						true);
				$("[id=" + id + ".httpproxyusername]").attr("disabled", true);
				$("[id=" + id + ".httpProxyPassword]").attr("disabled", true);
			}
		},
		useBasicAuthentication : function(e, v) {
			var elId = e.target.id;
			var id = elId.split(".")[0];
			if (e.target.checked) {
				$("[id=" + id + ".httpproxyusername]").attr("disabled", false);
				$("[id=" + id + ".httpProxyPassword]").attr("disabled", false);
			} else {
				$("[id=" + id + ".httpproxyusername]").attr("disabled", true);
				$("[id=" + id + ".httpProxyPassword]").attr("disabled", true);
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
		}
	}
};