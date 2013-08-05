jQuery.imeta.steps.aggregateRows = {
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
				id : rootId + '.fieldNewName',
				type : 'input',
				text : ''
			}, {
				id : rootId + '.aggregateType',
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
				id : 'fieldNewName',
				type : 'input',
				text : 'fieldName'
			}, {
				id : 'aggregateType',
				type : 'select',
				text : 'aggregateType'
			} ];

			jQuery.imeta.parameter.getfields(e, v, r);

		}
	}
};