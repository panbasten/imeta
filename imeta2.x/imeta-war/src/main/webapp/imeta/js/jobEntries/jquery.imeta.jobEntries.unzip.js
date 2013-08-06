jQuery.imeta.jobEntries.unzip = {
	btn : {

	},
	listeners : {
		isfrompreviousClick : function(e, v) {
			var elId = e.target.id;
			var id = elId.split(".")[0];
			if (e.target.checked) {
				$("[id=" + id + ".zipFilename]").attr("disabled", true);
				$("[id=" + id + ".wildcardSource]").attr("disabled", true);
			} else {
				$("[id=" + id + ".zipFilename]").attr("disabled", false);
				$("[id=" + id + ".wildcardSource]").attr("disabled", false);
			}
		},
		isSpecifyFormatClick : function(e, v) {
			var elId = e.target.id;
			var id = elId.split(".")[0];
			if (e.target.checked) {
				$("[id=" + id + ".adddate]").attr("disabled", true);
				$("[id=" + id + ".addtime]").attr("disabled", true);
				$("[id=" + id + ".date_time_format]").attr("disabled", false);
			} else {
				$("[id=" + id + ".adddate]").attr("disabled", false);
				$("[id=" + id + ".addtime]").attr("disabled", false);
				$("[id=" + id + ".date_time_format]").attr("disabled", true);
			}
		},
		afterunzipChange : function(e) {
			// alert(this.value);
			var elId = e.target.id;
			var id = elId.split(".")[0];
			if (this.value == '2') { // 移动文件
				$("[id=" + id + ".movetodirectory]").attr("disabled", false);
				$("[id=" + id + ".createMoveToDirectory]").attr("disabled", false);
			} else {
				$("[id=" + id + ".movetodirectory]").attr("disabled", true);
				$("[id=" + id + ".createMoveToDirectory]").attr("disabled", true);
			}
		},
		successOnChange : function(e) {
			// alert(this.value);
			var elId = e.target.id;
			var id = elId.split(".")[0];
			if (this.value == '0') { // 移动文件
				$("[id=" + id + ".nr_limit]").attr("disabled", true);
			} else {
				$("[id=" + id + ".nr_limit]").attr("disabled", false);
			}
		}
	}
};