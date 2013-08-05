jQuery.imeta.steps.getPreviousRowField = {
	btn : {
		fieldAdd : function(c) {
			var rootId = c.getAttribute("rootId");
			var r = [ {
				id : rootId + '.fieldId',
				type : 'number',
				text : ''
			}, {
				id : rootId + '.input',
				type : 'input',
				text : ''
			}, {
				id : rootId + '.output',
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
				id : 'input',
				type : 'input',
				text : 'fieldName'
			}, {
				id : 'output',
				type : 'input',
				text : ''
			}

			];
			jQuery.imeta.parameter.getfields(e, v, r);

		}
	}
};