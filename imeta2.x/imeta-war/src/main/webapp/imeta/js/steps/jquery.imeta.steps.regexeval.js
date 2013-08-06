jQuery.imeta.steps.regexeval = {
	listeners : {
		createFileListener : function(e, v) {
			var elId = e.target.id;
			var id = elId.split(".")[0];
			if (e.target.checked) {
				$("[id=" + id + "_fields.btn.add]").attr("disabled", false);
				$("[id=" + id + "_fields.btn.delete]").attr("disabled", false);
				$("[id=" + id + "_fields.btn.add.root]").removeClass(
						"x-item-disabled");
				$("[id=" + id + "_fields.btn.delete.root]").removeClass(
						"x-item-disabled");
				$("#" + id + "_fields_gRoot input").attr("disabled", false);
			} else {
				$("[id=" + id + "_fields.btn.add]").attr("disabled", true);
				$("[id=" + id + "_fields.btn.delete]").attr("disabled", true);
				$("[id=" + id + "_fields.btn.add.root]").addClass(
						"x-item-disabled");
				$("[id=" + id + "_fields.btn.delete.root]").addClass(
						"x-item-disabled");
				$("#" + id + "_fields_gRoot input").attr("disabled", true);
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
				id : rootId + '.fieldName',
				type : 'input',
				text : ''
			}, {
				id : rootId + '.fieldType',
				type : 'select',
				text : ''
			}, {
				id : rootId + '.fieldLength',
				type : 'input',
				text : ''
			}, {
				id : rootId + '.fieldPrecision',
				type : 'input',
				text : ''
			}, {
				id : rootId + '.fieldFormat',
				type : 'input',
				text : ''
			}, {
				id : rootId + '.fieldGroup',
				type : 'input',
				text : ''
			}, {
				id : rootId + '.fieldDecimal',
				type : 'input',
				text : ''
			}, {
				id : rootId + '.fieldNullIf',
				type : 'input',
				text : ''
			}, {
				id : rootId + '.fieldIfNull',
				type : 'input',
				text : ''
			}, {
				id : rootId + '.fieldTrimType',
				type : 'select',
				text : ''
			} ];

			jQuery.imetabar.createRowByHeader(r, rootId);
		}
	}
};