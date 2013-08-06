jQuery.imeta.steps.databasejoin = {
	btn : {
		getparam : function(e, v) {
			var r = [ {
				id : 'paramId',
				type : 'number',
				text : ''
			}, {
				id : 'parameterField',
				type : 'input',
				text : 'fieldName'
			}, {
				id : 'parameterType',
				type : 'select',
				text : 'type'
			} ];
			jQuery.imeta.parameter.getfields(e, v, r, 'param');
		},
		fieldAdd : function(c) {
			var rootId = c.getAttribute("rootId");
			var r = [ {
				id : rootId + '.paramId',
				type : 'number',
				text : ''
			}, {
				id : rootId + '.parameterField',
				type : 'input',
				text : ''
			}, {
				id : rootId + '.parameterType',
				type : 'select',
				text : ''
			} ];

			jQuery.imetabar.createRowByHeader(r, rootId);
		}
	}
};