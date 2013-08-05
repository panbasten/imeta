jQuery.imeta.steps.getfilenames = {
	btn : {
		fieldAdd : function(c) {
			var rootId = c.getAttribute("rootId");
			var r = [ {
				id : rootId + '.fileId',
				type : 'number',
				text : ''
			}, {
				id : rootId + '.fileName',
				type : 'input',
				text : ''
			}, {
				id : rootId + '.fileMask',
				type : 'input',
				text : ''
			}, {
				id : rootId + '.fileRequired',
				type : 'input',
				text : ''
			} ];
			jQuery.imetabar.createRowByHeader(r, rootId);
		}
	},
	listeners : {
		filenameDefinedListeners : function(e, v) {
			var elId = e.target.id;
			var id = elId.split(".")[0];
			if (e.target.checked) {
				$("[id=" + id + ".dynamicFilenameField]").attr("disabled",
						false);
				$("[id=" + id + ".dynamicWildcardField]").attr("disabled",
						false);
				$("[id=" + id + ".fileOrcontents]").attr("disabled", true);
				// $("[id="+id+".btn.fileOrcontentsAdd]").attr("disabled",true);
				// $("[id="+id+".btn.fileOrcontentsSelecter]").attr("disabled",true);
				$("[id=" + id + ".regularExpression]").attr("disabled", true);
				// $("[id="+id+".showFilesName]").attr("disabled",true);
				// $("[id="+id+".deleteBtn]").attr("disabled",true);
				// $("[id="+id+".editBtn]").attr("disabled",true);
				$("[id=" + id + ".rowLimit]").attr("disabled", true);

				// 点击后，表格屏蔽选中文件的按钮和包含文件的字段名
				$("[id=" + id + "_selectedFiles.btn.add]").attr("disabled",
						true);
				$("[id=" + id + "_selectedFiles.btn.delete]").attr("disabled",
						true);
				$("[id=" + id + "_selectedFiles.btn.add.root]").addClass(
						"x-item-disabled");
				$("[id=" + id + "_selectedFiles.btn.delete.root]").addClass(
						"x-item-disabled");
				$("#" + id + "_selectedFiles_gRoot input").attr("disabled",
						true);
			} else {
				$("[id=" + id + ".dynamicFilenameField]")
						.attr("disabled", true);
				$("[id=" + id + ".dynamicWildcardField]")
						.attr("disabled", true);
				$("[id=" + id + ".fileOrcontents]").attr("disabled", false);
				// $("[id="+id+".btn.fileOrcontentsAdd]").attr("disabled",false);
				// $("[id="+id+".btn.fileOrcontentsSelecter]").attr("disabled",false);
				$("[id=" + id + ".regularExpression]").attr("disabled", false);
				// $("[id="+id+".showFilesName]").attr("disabled",false);
				// $("[id="+id+".deleteBtn]").attr("disabled",false);
				// $("[id="+id+".editBtn]").attr("disabled",false);
				$("[id=" + id + ".rowLimit]").attr("disabled", false);

				// 点击不选中后，表格显示选中文件的按钮和包含文件的字段名
				$("[id=" + id + "_selectedFiles.btn.add]").attr("disabled",
						false);
				$("[id=" + id + "_selectedFiles.btn.delete]").attr("disabled",
						false);
				$("[id=" + id + "_selectedFiles.btn.add.root]").removeClass(
						"x-item-disabled");
				$("[id=" + id + "_selectedFiles.btn.delete.root]").removeClass(
						"x-item-disabled");
				$("#" + id + "_selectedFiles_gRoot input").attr("disabled",
						false);
			}
		}
	}
};