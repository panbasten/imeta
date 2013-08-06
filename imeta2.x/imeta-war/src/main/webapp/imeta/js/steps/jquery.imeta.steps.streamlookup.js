jQuery.imeta.steps.streamlookup = {
	btn : {
		getfields : function(e, v) {
			var r = [ {
				id : 'keysId',
				type : 'number',
				text : ''
			}, {
				id : 'keystream',
				type : 'input',
				text : 'fieldName'
			}, {
				id : 'keylookup',
				type : 'input',
				text : 'fieldName'
			} ];
			jQuery.imeta.parameter.getfields(e, v, r, "keys");
		},
		getkeywords : function(e, v) {
			var r = [ {
				id : 'valuesId',
				type : 'number',
				text : ''
			}, {
				id : 'value',
				type : 'input',
				text : 'fieldName'
			}, {
				id : 'valueName',
				type : 'input',
				text : ''
			}, {
				id : 'valueDefault',
				type : 'input',
				text : ''
			}, {
				id : 'valueDefaultType',
				type : 'select',
				text : 'type'
			} ];
			jQuery.imeta.parameter.getfields(e, v, r, "values");
		},
		keysAdd : function(c) {
			var rootId = c.getAttribute("rootId");
			var r = [ {
				id : rootId + '.keysId',
				type : 'number',
				text : ''
			}, {
				id : rootId + '.keystream',
				type : 'input',
				text : ''
			}, {
				id : rootId + '.keylookup',
				type : 'input',
				text : ''
			} ];

			jQuery.imetabar.createRowByHeader(r, rootId);
		},
		valuesAdd : function(c) {
			var rootId = c.getAttribute("rootId");
			var r = [ {
				id : rootId + '.valuesId',
				type : 'number',
				text : ''
			}, {
				id : rootId + '.value',
				type : 'input',
				text : ''
			}, {
				id : rootId + '.valueName',
				type : 'input',
				text : ''
			}, {
				id : rootId + '.valueDefault',
				type : 'input',
				text : ''
			}, {
				id : rootId + '.valueDefaultType',
				type : 'select',
				text : ''
			} ];

			jQuery.imetabar.createRowByHeader(r, rootId);
		}
	}
};