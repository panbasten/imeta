jQuery.imeta.steps.uniquerowsbyhashset = {
	btn : {
		getfields : function(e, v) {
			var r = [ {
				id : 'fieldId',
				type : 'number',
				text : ''
			}, {
				id : 'compareFields',
				type : 'input',
				text : 'fieldName'
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
				id : rootId + '.compareFields',
				type : 'input',
				text : ''
			} ];

			jQuery.imetabar.createRowByHeader(r, rootId);
		}
	}
};