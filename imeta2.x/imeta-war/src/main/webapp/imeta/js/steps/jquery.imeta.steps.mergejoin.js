jQuery.imeta.steps.mergejoin = {
	btn : {
		stepAGridAdd : function(c) {
			var rootId = c.getAttribute("rootId");
			var r = [ {
				id : rootId + '.fieldId',
				type : 'number',
				text : ''
			}, {
				id : rootId + '.value',
				type : 'input',
				text : ''
			} ];
			jQuery.imetabar.createRowByHeader(r, rootId);
		},
		stepBGridAdd : function(c) {
			var rootId = c.getAttribute("rootId");
			var r = [ {
				id : rootId + '.fieldId',
				type : 'number',
				text : ''
			}, {
				id : rootId + '.value',
				type : 'input',
				text : ''
			} ];
			jQuery.imetabar.createRowByHeader(r, rootId);
		},
		getStep1Field : function(e, v) {

			var r = [ {
				id : 'fieldId',
				type : 'number',
				text : ''
			}, {
				id : 'value',
				type : 'input',
				text : 'fieldName'
			} ];

			jQuery.imeta.parameter.getfields(e, v, r, 'stepAGrid', 'info');

		},// end getField
		getStep2Field : function(e, v) {

			var r = [ {
				id : 'fieldId',
				type : 'number',
				text : ''
			}, {
				id : 'value',
				type : 'input',
				text : 'fieldName'
			} ];

			jQuery.imeta.parameter.getfields(e, v, r, 'stepBGrid', 'info');

		}// end getField
	}
};