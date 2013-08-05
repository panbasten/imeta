jQuery.imeta.steps.textfileoutput = {
	listeners : {
		fileNameInFieldListener : function(e, v) {
			var elId = e.target.id;
			var id = elId.split(".")[0];
			if (e.target.checked) {
				$("[id=" + id + ".fileName]").attr("disabled", true);
				$("[id=" + id + ".btn.showFieldName]").attr("disabled", true);
				$("[id=" + id + ".fileAsCommand]").attr("disabled", true);
				$("[id=" + id + ".fileNameField]").attr("disabled", false);
			} else {
				$("[id=" + id + ".fileName]").attr("disabled", false);
				$("[id=" + id + ".btn.showFieldName]").attr("disabled", false);
				$("[id=" + id + ".fileAsCommand]").attr("disabled", false);
				$("[id=" + id + ".fileNameField]").attr("disabled", true);
			}
		},
		specifyingFormatListener : function(e, v) {
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
		}
	},
	btn : {
		fieldsAdd : function(c) {
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
			}, {
				id : rootId + '.fieldLength',
				type : 'input',
				text : ''
			}, {
				id : rootId + '.fieldPrecision',
				type : 'input',
				text : ''
			}, {
				id : rootId + '.fieldCurrency',
				type : 'input',
				text : ''
			}, {
				id : rootId + '.fieldDecimal',
				type : 'input',
				text : ''
			}, {
				id : rootId + '.fieldGroup',
				type : 'input',
				text : ''
			}, {
				id : rootId + '.fieldTrimType',
				type : 'select',
				text : ''
			}, {
				id : rootId + '.fieldNullif',
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
				text : 'type'
			}, {
				id : 'fieldFormat',
				type : 'input',
				text : ''
			}, {
				id : 'fieldLength',
				type : 'input',
				text : 'length'
			}, {
				id : 'fieldPrecision',
				type : 'input',
				text : 'precision'
			}, {
				id : 'fieldCurrency',
				type : 'input',
				text : ''
			}, {
				id : 'fieldDecimal',
				type : 'input',
				text : 'decimal'
			}, {
				id : 'fieldGroup',
				type : 'input',
				text : 'group'
			}, {
				id : 'fieldTrimType',
				type : 'select',
				text : 'trimType'
			}, {
				id : 'fieldNullif',
				type : 'input',
				text : ''
			} ];
			jQuery.imeta.parameter.getfields(e, v, r);
		}
	}
};