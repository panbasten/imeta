jQuery.imeta.steps.encrypt = {
	btn : {
		encryptAdd : function(c) {
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
				id : rootId + '.newFieldName',
				type : 'input',
				text : ''
			}, {
				id : rootId + '.optionType',
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
				id : 'newFieldName',
				type : 'input',
				text : ''
			}, {
				id : 'optionType',
				type : 'select',
				text : ''
			} ];
			jQuery.imeta.parameter.getfields(e, v, r);
		}
	}
};