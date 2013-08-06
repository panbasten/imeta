jQuery.imeta.jobEntries.copyFiles = {
	btn : {
		fieldAdd : function(c) {
			var rootId = c.getAttribute("rootId");
			var r = [ {
				id : rootId + '.fieldId',
				type : 'number',
				text : ''
			}, {
				id : rootId + '.fileFolderSource',
				type : 'input',
				text : ''
			}, {
				id : rootId + '.fileFolderDest',
				type : 'input',
				text : ''
			}, {
				id : rootId + '.wildcard',
				type : 'input',
				text : ''
			} ];
			jQuery.imetabar.createRowByHeader(r, rootId);
		}
	},
	listeners : {
		isIncludeSubfoldersClick : function(e, v) {
			var elId = e.target.id;
			var id = elId.split(".")[0];
			if (e.target.checked) {
				$("[id=" + id + ".copy_empty_folders]").attr("disabled", false);
			} else {
				$("[id=" + id + ".copy_empty_folders]").attr("disabled", true);
			}
		},
		is_arg_from_previous_click : function(e, v) {
			var elId = e.target.id;
			var id = elId.split(".")[0];
			if (!e.target.checked) {
				$("[id=" + id + "_fileFolderTable.btn.add]").attr("disabled",
						false);
				$("[id=" + id + "_fileFolderTable.btn.delete]").attr(
						"disabled", false);
				$("[id=" + id + "_fileFolderTable.btn.add.root]").removeClass(
						"x-item-disabled");
				$("[id=" + id + "_fileFolderTable.btn.delete.root]")
						.removeClass("x-item-disabled");
				$("#" + id + "_fileFolderTable_gRoot input").attr("disabled",
						false);
			} else {
				$("[id=" + id + "_fileFolderTable.btn.add]").attr("disabled",
						true);
				$("[id=" + id + "_fileFolderTable.btn.delete]").attr(
						"disabled", true);
				$("[id=" + id + "_fileFolderTable.btn.add.root]").addClass(
						"x-item-disabled");
				$("[id=" + id + "_fileFolderTable.btn.delete.root]").addClass(
						"x-item-disabled");
				$("#" + id + "_fileFolderTable_gRoot input").attr("disabled",
						true);
			}
		}
	}
};