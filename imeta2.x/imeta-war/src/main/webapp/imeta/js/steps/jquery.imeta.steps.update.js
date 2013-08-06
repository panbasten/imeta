jQuery.imeta.steps.update = {
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
				text : ''
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
				text : ''
			}, {
				id : 'updateStream',
				type : 'input',
				text : ''
			} ];
			jQuery.imeta.parameter.getfields(e, v, r, 'refreshwords');
		}
	},
	listeners : {
		skipLookup : function(e, v) {
			var elId = e.target.id;
			var id = elId.split(".")[0];
			if (e.target.checked) {
				$("[id=" + id + ".errorIgnored]").attr("disabled", true);
			} else {
				$("[id=" + id + ".errorIgnored]").attr("disabled", false);

			}
		},
		errorIgnored : function(e, v) {
			var elId = e.target.id;
			var id = elId.split(".")[0];
			if (e.target.checked) {
				$("[id=" + id + ".ignoreFlagField]").attr("disabled", false);
			} else {
				$("[id=" + id + ".ignoreFlagField]").attr("disabled", true);

			}
		}
	}
};