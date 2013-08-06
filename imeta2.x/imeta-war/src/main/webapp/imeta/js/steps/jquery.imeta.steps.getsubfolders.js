jQuery.imeta.steps.getsubfolders = {
	btn : {
		filesAdd : function(c) {
			var rootId = c.getAttribute("rootId");
			var r = [ {
				id : rootId + '.folderId',
				type : 'number',
				text : ''
			}, {
				id : rootId + '.folderName',
				type : 'input',
				text : ''
			}, {
				id : rootId + '.folderRequired',
				type : 'input',
				text : ''
			} ];

			jQuery.imetabar.createRowByHeader(r, rootId);
		}
	},
	listeners : {
		isFoldernameDynamic : function(e, v) {
			var elId = e.target.id;
			var id = elId.split(".")[0];
			if (e.target.checked) {
				$("[id=" + id + ".dynamicFoldernameField]").attr("disabled",
						false);
				// $("[id="+id+".directory]").attr("disabled",true);

				// 点击后，表格屏蔽选中文件的按钮和包含文件的字段名
				$("[id=" + id + "_files.btn.add]").attr("disabled", true);
				$("[id=" + id + "_files.btn.delete]").attr("disabled", true);
				$("[id=" + id + "_files.btn.add.root]").addClass(
						"x-item-disabled");
				$("[id=" + id + "_files.btn.delete.root]").addClass(
						"x-item-disabled");
				$("#" + id + "_files_gRoot input").attr("disabled", true);
			} else {
				$("[id=" + id + ".dynamicFoldernameField]").attr("disabled",
						true);
				// $("[id="+id+".directory]").attr("disabled",false);

				// 点击不选中后，表格显示选中文件的按钮和包含文件的字段名
				$("[id=" + id + "_files.btn.add]").attr("disabled", false);
				$("[id=" + id + "_files.btn.delete]").attr("disabled", false);
				$("[id=" + id + "_files.btn.add.root]").removeClass(
						"x-item-disabled");
				$("[id=" + id + "_files.btn.delete.root]").removeClass(
						"x-item-disabled");
				$("#" + id + "_files_gRoot input").attr("disabled", false);
			}
		},
		includeRowNumber : function(e, v) {
			var elId = e.target.id;
			var id = elId.split(".")[0];
			if (e.target.checked) {
				$("[id=" + id + ".rowNumberField]").attr("disabled", false);
			} else {
				$("[id=" + id + ".rowNumberField]").attr("disabled", true);
			}
		}
	}
};