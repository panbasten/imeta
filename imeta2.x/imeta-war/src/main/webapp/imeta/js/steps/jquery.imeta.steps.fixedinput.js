jQuery.imeta.steps.fixedinput = {
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
				id : rootId + '.fieldFormat',
				type : 'input',
				text : ''
			}, {
				id : rootId + '.fieldWidth',
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
				id : 'fieldWidth',
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
				text : ''
			}, {
				id : 'fieldGroup',
				type : 'input',
				text : 'group'
			}, {
				id : 'fieldTrimType',
				type : 'select',
				text : ''
			} ];
			jQuery.imeta.parameter.getfields(e, v, r);
		}
	},
	listeners : {
		runningListeners : function(e, v) {
			var elId = e.target.id;
			var id = elId.split(".")[0];
			if (e.target.checked) {
				$("[id=" + id + ".fileType]").attr("disabled", false);
			} else {
				$("[id=" + id + ".fileType]").attr("disabled", true);
			}
		}
	}
};