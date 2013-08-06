jQuery.imeta.steps.mail = {
	btn : {},
	listeners : {
		usingAuthentication : function(e, v) {
			var elId = e.target.id;
			var id = elId.split(".")[0];
			if (e.target.checked) {
				$("[id=" + id + ".authenticationUser]").attr("disabled", false);
				$("[id=" + id + ".authenticationPassword]").attr("disabled",
						false);
				$("[id=" + id + ".usingSecureAuthentication]").attr("disabled",
						false);
				// $("[id="+id+".secureconnectiontype]").attr("disabled",false);
			} else {
				$("[id=" + id + ".authenticationUser]").attr("disabled", true);
				$("[id=" + id + ".authenticationPassword]").attr("disabled",
						true);
				$("[id=" + id + ".usingSecureAuthentication]").attr("disabled",
						true);
				// $("[id="+id+".secureconnectiontype]").attr("disabled",true);
			}
		},
		usingSecureAuthentication : function(e, v) {
			var elId = e.target.id;
			var id = elId.split(".")[0];
			if (e.target.checked) {
				$("[id=" + id + ".secureconnectiontype]").attr("disabled",
						false);
			} else {
				$("[id=" + id + ".secureconnectiontype]")
						.attr("disabled", true);
			}
		},
		useHTML : function(e, v) {
			var elId = e.target.id;
			var id = elId.split(".")[0];
			if (e.target.checked) {
				$("[id=" + id + ".encoding]").attr("disabled", false);
			} else {
				$("[id=" + id + ".encoding]").attr("disabled", true);
			}
		},
		manage : function(e, v) {
			var elId = e.target.id;
			var id = elId.split(".")[0];
			if (e.target.checked) {
				$("[id=" + id + ".priority]").attr("disabled", false);
				$("[id=" + id + ".importance]").attr("disabled", false);
			} else {
				$("[id=" + id + ".priority]").attr("disabled", true);
				$("[id=" + id + ".importance]").attr("disabled", true);
			}
		},
		isFilenameDynamic : function(e, v) {
			var elId = e.target.id;
			var id = elId.split(".")[0];
			if (e.target.checked) {
				$("[id=" + id + ".dynamicFieldname]").attr("disabled", false);
				$("[id=" + id + ".dynamicWildcard]").attr("disabled", false);
				$("[id=" + id + ".sourcefilefoldername]")
						.attr("disabled", true);
				$("[id=" + id + ".sourcewildcard]").attr("disabled", true);
			} else {
				$("[id=" + id + ".dynamicFieldname]").attr("disabled", true);
				$("[id=" + id + ".dynamicWildcard]").attr("disabled", true);
				$("[id=" + id + ".sourcefilefoldername]").attr("disabled",
						false);
				$("[id=" + id + ".sourcewildcard]").attr("disabled", false);
			}
		},
		zipFiles : function(e, v) {
			var elId = e.target.id;
			var id = elId.split(".")[0];
			if (e.target.checked) {
				$("[id=" + id + ".zipFilenameDynamic]").attr("disabled", false);
				// $("[id="+id+".dynamicZipFilename]").attr("disabled",false);
				$("[id=" + id + ".zipFilename]").attr("disabled", false);
				$("[id=" + id + ".ziplimitsize]").attr("disabled", false);
			} else {
				$("[id=" + id + ".zipFilenameDynamic]").attr("disabled", true);
				// $("[id="+id+".dynamicZipFilename]").attr("disabled",true);
				$("[id=" + id + ".zipFilename]").attr("disabled", true);
				$("[id=" + id + ".ziplimitsize]").attr("disabled", true);
			}
		},
		zipFilenameDynamic : function(e, v) {
			var elId = e.target.id;
			var id = elId.split(".")[0];
			if (e.target.checked) {
				$("[id=" + id + ".dynamicZipFilename]").attr("disabled", false);
			} else {
				$("[id=" + id + ".dynamicZipFilename]").attr("disabled", true);
			}
		}
	}
};