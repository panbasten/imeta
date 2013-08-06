jQuery.imeta.steps.synchronizeaftermerge = {
	btn : {
		getFields : function(e, v) {
			var r = [ {
				id : 'keyId',
				type : 'number',
				text : ''
			}, {
				id : 'keyLookup',
				type : 'input',
				text : 'fieldName'
			}, {
				id : 'keyCondition',
				type : 'select',
				text : ''
			}, {
				id : 'keyStream',
				type : 'input',
				text : 'fieldName'
			}, {
				id : 'keyStream2',
				type : 'input',
				text : ''
			} ];
			jQuery.imeta.parameter.getfields(e, v, r, "keys");
		},
		getUpdateFields : function(e, v) {
			var r = [ {
				id : 'valueId',
				type : 'number',
				text : ''
			}, {
				id : 'updateLookup',
				type : 'input',
				text : 'fieldName'
			}, {
				id : 'updateStream',
				type : 'input',
				text : 'fieldName'
			}, {
				id : 'update',
				type : 'select',
				text : ''
			} ];
			jQuery.imeta.parameter.getfields(e, v, r, "values");
		},
		keysAdd : function(c) {
			var rootId = c.getAttribute("rootId");
			var r = [ {
				id : rootId + '.keyId',
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
		valuesAdd : function(c) {
			var rootId = c.getAttribute("rootId");
			var r = [ {
				id : rootId + '.valueId',
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
		}
	},
	listeners : {
		tablenameInFieldListeners : function(e, v) {
			var elId = e.target.id;
			var id = elId.split(".")[0];
			if (e.target.checked) {
				$("[id=" + id + ".tablenameField]").attr("disabled", false);
				$("[id=" + id + ".tableName]").attr("disabled", true);
			} else {
				$("[id=" + id + ".tablenameField]").attr("disabled", true);
				$("[id=" + id + ".tableName]").attr("disabled", false);
			}
		}
	}
};