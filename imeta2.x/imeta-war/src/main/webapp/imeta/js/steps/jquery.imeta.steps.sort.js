jQuery.imeta.steps.sort = {
	btn : {
		getwords : function(e, v) {
			var r = [ {
				id : 'fieldId',
				type : 'number',
				text : ''
			}, {
				id : 'fieldName',
				type : 'input',
				text : 'fieldName'
			}, {
				id : 'ascending',
				type : 'select',
				text : 'ascending'
			}, {
				id : 'caseSensitive',
				type : 'select',
				text : ''
			} ];
			jQuery.imeta.parameter.getfields(e, v, r);
		},
		sortAdd : function(c) {
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
				id : rootId + '.ascending',
				type : 'select',
				text : ''
			}, {
				id : rootId + '.caseSensitive',
				type : 'select',
				text : ''
			} ];

			jQuery.imetabar.createRowByHeader(r, rootId);
		}
	},
	listeners : {}
};