jQuery.imeta.steps.OraBulkLoader = {
	btn : {
		fieldAdd : function(c) {
			var rootId = c.getAttribute("rootId");
			var r = [ {
				id : rootId + '.fieldId',
				type : 'number',
				text : ''
			}, {
				id : rootId + '.fieldTable',
				type : 'input',
				text : ''
			}, {
				id : rootId + '.fieldStream',
				type : 'select',
				text : ''
			}, {
				id : rootId + '.dateMask',
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
				id : 'fieldTable',
				type : 'input',
				text : 'fieldName'
			}, {
				id : 'fieldStream',
				type : 'select',
				text : 'fieldName'
			}, {
				id : 'dateMask',
				type : 'select',
				text : ''
			} ];
			jQuery.imeta.parameter.getfields(e, v, r);
		}
	}
};