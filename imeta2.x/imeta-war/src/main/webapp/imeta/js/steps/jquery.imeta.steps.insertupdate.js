jQuery.imeta.steps.insertupdate = {
	btn : {
		keywordsAdd : function(c) {
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
				type : 'select',
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
		refreshwordsAdd : function(c) {
			var rootId = c.getAttribute("rootId");

			var r = [ {
				id : rootId + '.fieldId',
				type : 'number',
				text : ''
			}, {
				id : rootId + '.updateLookup',
				type : 'input',
				text : ''
			}, {
				id : rootId + '.updateStream',
				type : 'input',
				text : ''
			}, {
				id : rootId + '.update',
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
				id : 'keyLookup',
				type : 'input',
				text : 'fieldName'
			}, {
				id : 'keyStream',
				type : 'input',
				text : ''
			}, {
				id : 'keyCondition',
				type : 'input',
				text : ''
			}, {
				id : 'keyStream2',
				type : 'input',
				text : ''
			} ];
			jQuery.imeta.parameter.getfields(e, v, r, 'keywords');
		},
		getRefreshFields : function(e, v) {
			var r = [ {
				id : 'fieldId',
				type : 'number',
				text : ''
			}, {
				id : 'updateLookup',
				type : 'input',
				text : 'fieldName'
			}, {
				id : 'updateStream',
				type : 'input',
				text : ''
			}, {
				id : 'update',
				type : 'select',
				text : ''
			} ];
			jQuery.imeta.parameter.getfields(e, v, r, 'refreshwords');
		}
	},
	listeners : {

	}
};