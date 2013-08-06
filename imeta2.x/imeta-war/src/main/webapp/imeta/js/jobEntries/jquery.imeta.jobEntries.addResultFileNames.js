jQuery.imeta.jobEntries.addResultFileNames = {
	btn : {
		fieldAdd : function(c) {
			var rootId = c.getAttribute("rootId");
			var r = [ {
				id : rootId + '.fieldId',
				type : 'number',
				text : ''
			}, {
				id : rootId + '.fileFolder',
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
		isArgFromPreviousClick : function(e, v) {
			var elId = e.target.id;
			var id = elId.split(".")[0];
			if (!e.target.checked) {
				$("[id=" + id + "_multiFileFolderTable.btn.add]").attr(
						"disabled", false);
				$("[id=" + id + "_multiFileFolderTable.btn.delete]").attr(
						"disabled", false);
				$("[id=" + id + "_multiFileFolderTable.btn.add.root]")
						.removeClass("x-item-disabled");
				$("[id=" + id + "_multiFileFolderTable.btn.delete.root]")
						.removeClass("x-item-disabled");
				$("#" + id + "_multiFileFolderTable_gRoot input").attr(
						"disabled", false);
			} else {
				$("[id=" + id + "_multiFileFolderTable.btn.add]").attr(
						"disabled", true);
				$("[id=" + id + "_multiFileFolderTable.btn.delete]").attr(
						"disabled", true);
				$("[id=" + id + "_multiFileFolderTable.btn.add.root]")
						.addClass("x-item-disabled");
				$("[id=" + id + "_multiFileFolderTable.btn.delete.root]")
						.addClass("x-item-disabled");
				$("#" + id + "_multiFileFolderTable_gRoot input").attr(
						"disabled", true);
			}
		}
	}
};