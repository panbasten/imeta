jQuery.imeta.steps.exceloutput = {
	btn : {
		outputFieldsAdd : function(c) {
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
				id : rootId + '.fieldFormat',
				type : 'input',
				text : ''
			} ];

			jQuery.imetabar.createRowByHeader(r, rootId);

		},

		getfields : function(e, v) {
			var r = [ {
				id : 'fieldId',
				type : 'number',
				text : ''
			}, {
				id : 'fieldName',
				type : 'input',
				text : 'fieldName'
			}, {
				id : 'fieldType',
				type : 'select',
				text : 'type '
			}, {
				id : 'fieldFormat',
				type : 'input',
				text : ''
			} ];
			jQuery.imeta.parameter.getfields(e, v, r);

		}
	},
	listeners : {
		specifyFormat : function(e, v) {
			var elId = e.target.id;
			var id = elId.split(".")[0];
			if (e.target.checked) {
				$("[id=" + id + ".dateInFilename]").attr("disabled", true);
				$("[id=" + id + ".timeInFilename]").attr("disabled", true);
				$("[id=" + id + ".dateTimeFormat]").attr("disabled", false);
			} else {
				$("[id=" + id + ".dateInFilename]").attr("disabled", false);
				$("[id=" + id + ".timeInFilename]").attr("disabled", false);
				$("[id=" + id + ".dateTimeFormat]").attr("disabled", true);
			}
		},
		protectsheet : function(e, v) {
			var elId = e.target.id;
			var id = elId.split(".")[0];
			if (e.target.checked) {
				$("[id=" + id + ".password]").attr("disabled", false);
			} else {
				$("[id=" + id + ".password]").attr("disabled", true);
			}
		},
		templateEnabled : function(e, v) {
			var elId = e.target.id;
			var id = elId.split(".")[0];
			if (e.target.checked) {
				$("[id=" + id + ".templateFileName]").attr("disabled", false);
			} else {
				$("[id=" + id + ".templateFileName]").attr("disabled", true);
			}
		}
	}
};