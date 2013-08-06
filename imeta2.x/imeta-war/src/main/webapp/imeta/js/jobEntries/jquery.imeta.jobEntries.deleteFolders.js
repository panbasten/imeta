jQuery.imeta.jobEntries.deleteFolders = {
	btn : {
		fieldAdd : function(c) {
			var rootId = c.getAttribute("rootId");
			var r = [ {
				id : rootId + '.fieldId',
				type : 'number',
				text : ''
			}, {
				id : rootId + '.folderName',
				type : 'input',
				text : ''
			} ];
			jQuery.imetabar.createRowByHeader(r, rootId);
		}
	},
	listeners : {
		success_conditionChange : function(e) {
			// alert(this.value);
			var elId = e.target.id;
			var id = elId.split(".")[0];
			if (this.value == 'success_if_no_errors') {
				$("[id=" + id + ".limit_folders]").attr("disabled", true);
			} else {
				$("[id=" + id + ".limit_folders]").attr("disabled", false);
			}
		},
		isArgFromPreviousClick : function(e, v) {
			var elId = e.target.id;
			var id = elId.split(".")[0];
			if (!e.target.checked) {
				$("[id=" + id + "_foldersToDeleteTable.btn.add]").attr(
						"disabled", false);
				$("[id=" + id + "_foldersToDeleteTable.btn.delete]").attr(
						"disabled", false);
				$("[id=" + id + "_foldersToDeleteTable.btn.add.root]")
						.removeClass("x-item-disabled");
				$("[id=" + id + "_foldersToDeleteTable.btn.delete.root]")
						.removeClass("x-item-disabled");
				$("#" + id + "_foldersToDeleteTable_gRoot input").attr(
						"disabled", false);
			} else {
				$("[id=" + id + "_foldersToDeleteTable.btn.add]").attr(
						"disabled", true);
				$("[id=" + id + "_foldersToDeleteTable.btn.delete]").attr(
						"disabled", true);
				$("[id=" + id + "_foldersToDeleteTable.btn.add.root]")
						.addClass("x-item-disabled");
				$("[id=" + id + "_foldersToDeleteTable.btn.delete.root]")
						.addClass("x-item-disabled");
				$("#" + id + "_foldersToDeleteTable_gRoot input").attr(
						"disabled", true);
			}
		}
	}
};