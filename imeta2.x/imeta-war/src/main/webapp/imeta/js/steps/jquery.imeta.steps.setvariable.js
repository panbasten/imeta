jQuery.imeta.steps.setvariable = {
	btn : {
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
				id : 'variableName',
				type : 'input',
				text : ''
			}, {
				id : 'variableType',
				type : 'select',
				text : ''
			}, {
				id : 'defaultValue',
				type : 'input',
				text : ''
			} ];
			jQuery.imeta.parameter.getfields(e, v, r);
		},
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
				id : rootId + '.variableName',
				type : 'input',
				text : ''
			}, {
				id : rootId + '.variableType',
				type : 'select',
				text : ''
			}, {
				id : rootId + '.defaultValue',
				type : 'input',
				text : ''
			} ];

			jQuery.imetabar.createRowByHeader(r, rootId);
		}
	},
	listeners : {}
};