jQuery.imeta.steps.deleteDialog = {
	btn : {
		deleteAdd : function(c) {
			var rootId = c.getAttribute("rootId");
			var r = [ {
				id : rootId + '.fieldId',
				type : 'number',
				text : ''
			}, {
				id : rootId + '.keyLookup',
				type : 'input',
				text : ''
			}, {
				id : rootId + '.keyCondition',
				type : 'input',
				text : ''
			}, {
				id : rootId + '.keyStream',
				type : 'input',
				text : ''
			}, {
				id : rootId + '.keyStream2',
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
				id : 'keyLookup',
				type : 'input',
				text : 'fieldName'
			}, {
				id : 'keyCondition',
				type : 'input',
				text : ''
			}, {
				id : 'keyStream',
				type : 'input',
				text : ''
			}, {
				id : 'keyStream2',
				type : 'input',
				text : ''
			} ];
			jQuery.imeta.parameter.getfields(e, v, r);
		}
	},
	listeners : {

	}
};