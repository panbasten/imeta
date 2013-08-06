jQuery.imeta.steps.dimensionlookup = {
	btn : {
		getfields : function(e, v) {
			var r = [ {
				id : rootId + '.keysId',
				type : 'number',
				text : ''
			}, {
				id : rootId + '.keyStream',
				type : 'select',
				text : ''
			}, {
				id : rootId + '.keyLookup',
				type : 'select',
				text : ''
			} ];
			jQuery.imetabar.createRowByHeader(e, v, r);
		},
		keyWordsAdd : function(c) {
			var rootId = c.getAttribute("rootId");
			var r = [ {
				id : rootId + '.keysId',
				type : 'number',
				text : ''
			}, {
				id : rootId + '.keyStream',
				type : 'select',
				text : ''
			}, {
				id : rootId + '.keyLookup',
				type : 'select',
				text : ''
			} ];

			jQuery.imetabar.createRowByHeader(r, rootId);
		},
		queryUpdateFieldAdd : function(c) {
			var rootId = c.getAttribute("rootId");
			var r = [ {
				id : rootId + '.fieldsId',
				type : 'number',
				text : ''
			}, {
				id : rootId + '.fieldStream',
				type : 'input',
				text : ''
			}, {
				id : rootId + '.fieldLookup',
				type : 'input',
				text : ''
			}, {
				id : rootId + '.fieldUpdate',
				type : 'select',
				text : ''
			} ];

			jQuery.imetabar.createRowByHeader(r, rootId);
		}
	}
};