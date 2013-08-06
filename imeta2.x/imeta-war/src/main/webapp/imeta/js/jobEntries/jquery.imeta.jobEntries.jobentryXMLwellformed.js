jQuery.imeta.jobEntries.jobentryXMLwellformed = {
	listeners : {
		successOnChange : function(e) {
			// alert(this.value);
			var elId = e.target.id;
			var id = elId.split(".")[0];
			if (this.value == 'success_if_no_errors') { // 移动文件
				$("[id=" + id + ".nr_errors_less_than]").attr("disabled", true);
			} else {
				$("[id=" + id + ".nr_errors_less_than]").attr("disabled", false);
			}
		}
	},
	btn : {
		fieldAdd : function(c) {
			var rootId = c.getAttribute("rootId");
			var r = [ {
				id : rootId + '.fieldId',
				type : 'number',
				text : ''
			}, {
				id : rootId + '.source_filefolder',
				type : 'input',
				text : ''
			}, {
				id : rootId + '.wildcard',
				type : 'input',
				text : ''
			} ];
			jQuery.imetabar.createRowByHeader(r, rootId);
		}
	}
};