jQuery.imeta.jobEntries.mail = {
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
				// $("[id="+id+".secureConnectionType]").attr("disabled",false);
			} else {
				$("[id=" + id + ".authenticationUser]").attr("disabled", true);
				$("[id=" + id + ".authenticationPassword]").attr("disabled",
						true);
				$("[id=" + id + ".usingSecureAuthentication]").attr("disabled",
						true);
				// $("[id="+id+".secureConnectionType]").attr("disabled",true);
			}
		},
		usingSecureAuthentication : function(e, v) {
			var elId = e.target.id;
			var id = elId.split(".")[0];
			if (e.target.checked) {
				$("[id=" + id + ".secureConnectionType]").attr("disabled",
						false);
			} else {
				$("[id=" + id + ".secureConnectionType]")
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
		usePriority : function(e, v) {
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
		includingFiles : function(e, v) {
			var elId = e.target.id;
			var id = elId.split(".")[0];
			if (e.target.checked) {
				$("[id=" + id + ".fileType]").attr("disabled", false);
				$("[id=" + id + ".zipFiles]").attr("disabled", false);
				// $("[id="+id+".zipFilename]").attr("disabled",false);
			} else {
				$("[id=" + id + ".fileType]").attr("disabled", true);
				$("[id=" + id + ".zipFiles]").attr("disabled", true);
				// $("[id="+id+".zipFilename]").attr("disabled",true);
			}
		},
		zipFiles : function(e, v) {
			var elId = e.target.id;
			var id = elId.split(".")[0];
			if (e.target.checked) {
				$("[id=" + id + ".zipFilename]").attr("disabled", false);
		
			} else {
				$("[id=" + id + ".zipFilename]").attr("disabled", true);
		
			}
		}
	}
};