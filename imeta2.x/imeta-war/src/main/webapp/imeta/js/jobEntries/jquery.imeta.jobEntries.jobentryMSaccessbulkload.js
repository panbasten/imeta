jQuery.imeta.jobEntries.jobentryMSaccessbulkload = {
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
				id : rootId + '.source_wildcard',
				type : 'input',
				text : ''
			}, {
				id : rootId + '.delimiter',
				type : 'input',
				text : ''
			}, {
				id : rootId + '.target_Db',
				type : 'input',
				text : ''
			}, {
				id : rootId + '.target_table',
				type : 'input',
				text : ''
			} ];
			jQuery.imetabar.createRowByHeader(r, rootId);
		}
	},
	listeners : {
		successOnChange : function(e) {
			// alert(this.value);
			var elId = e.target.id;
			var id = elId.split(".")[0];
			if (this.value == 'success_if_no_errors') { // 移动文件
				$("[id=" + id + ".limit]").attr("disabled", true);
			} else {
				$("[id=" + id + ".limit]").attr("disabled", false);
			}
		}
	}
};