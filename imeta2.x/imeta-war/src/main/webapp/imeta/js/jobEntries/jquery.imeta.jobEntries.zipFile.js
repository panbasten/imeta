jQuery.imeta.jobEntries.zipFile = {
	btn : {

	},
	listeners : {
		isfrompreviousClick : function(e, v) {
			var elId = e.target.id;
			var id = elId.split(".")[0];
			if (e.target.checked) {
				$("[id=" + id + ".sourcedirectory]").attr("disabled", true);
				$("[id=" + id + ".wildcard]").attr("disabled", true);
				$("[id=" + id + ".wildcardexclude]").attr("disabled", true);
				$("[id=" + id + ".zipFilename]").attr("disabled", true);
				$("[id=" + id + ".adddate]").attr("disabled", true);
				$("[id=" + id + ".addtime]").attr("disabled", true);
			} else {
				$("[id=" + id + ".sourcedirectory]").attr("disabled", false);
				$("[id=" + id + ".wildcard]").attr("disabled", false);
				$("[id=" + id + ".wildcardexclude]").attr("disabled", false);
				$("[id=" + id + ".zipFilename]").attr("disabled", false);
				$("[id=" + id + ".adddate]").attr("disabled", false);
				$("[id=" + id + ".addtime]").attr("disabled", false);
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
		afterzipChange : function(e) {
			// alert(this.value);
			var elId = e.target.id;
			var id = elId.split(".")[0];
			if (this.value == '2') { // 移动文件
				$("[id=" + id + ".movetodirectory]").attr("disabled", false);
			} else {
				$("[id=" + id + ".movetodirectory]").attr("disabled", true);
			}
		}
	}
};