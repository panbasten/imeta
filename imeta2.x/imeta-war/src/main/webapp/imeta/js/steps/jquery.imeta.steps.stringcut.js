jQuery.imeta.steps.stringcut = {
	btn : {
		getfields : function(e, v) {
			var r = [ {
				id : 'fieldId',
				type : 'number',
				text : ''
			}, {
				id : 'fieldInStream',
				type : 'input',
				text : 'fieldName'
			}, {
				id : 'fieldOutStream',
				type : 'input',
				text : ''
			}, {
				id : 'cutFrom',
				type : 'input',
				text : ''
			}, {
				id : 'cutTo',
				type : 'input',
				text : ''
			} ];
			jQuery.imeta.parameter.getfields(e, v, r);
		},
		stringcutAdd : function(c) {
			var rootId = c.getAttribute("rootId");
			var r = [ {
				id : rootId + '.fieldId',
				type : 'number',
				text : ''
			}, {
				id : rootId + '.fieldInStream',
				type : 'input',
				text : ''
			}, {
				id : rootId + '.fieldOutStream',
				type : 'input',
				text : ''
			}, {
				id : rootId + '.cutFrom',
				type : 'input',
				text : ''
			}, {
				id : rootId + '.cutTo',
				type : 'input',
				text : ''
			} ];

			jQuery.imetabar.createRowByHeader(r, rootId);
		}
	}
};