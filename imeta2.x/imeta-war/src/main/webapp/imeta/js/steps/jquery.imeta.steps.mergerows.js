jQuery.imeta.steps.mergerows = {

	btn : {
		matchkeywordsAdd : function(c) {
			var rootId = c.getAttribute("rootId");
			var r = [ {
				id : rootId + '.fieldId',
				type : 'number',
				text : ''
			}, {
				id : rootId + '.fieldName',
				type : 'input',
				text : ''
			} ];
			jQuery.imetabar.createRowByHeader(r, rootId);
		},
		dateFieldAdd : function(c) {
			var rootId = c.getAttribute("rootId");
			var r = [ {
				id : rootId + '.fieldId',
				type : 'number',
				text : ''
			}, {
				id : rootId + '.valueFields',
				type : 'input',
				text : ''
			} ];
			jQuery.imetabar.createRowByHeader(r, rootId);
		},
		getKeywords : function(e, v) {

			var r = [ {
				id : 'fieldId',
				type : 'number',
				text : ''
			}, {
				id : 'fieldName',
				type : 'input',
				text : 'fieldName'
			} ];

			jQuery.imeta.parameter.getfields(e, v, r, 'matchkeywords', 'info');

		},
		getValue : function(e, v) {

			var r = [ {
				id : 'fieldId',
				type : 'number',
				text : ''
			}, {
				id : 'valueFields',
				type : 'input',
				text : 'fieldName'
			} ];

			jQuery.imeta.parameter.getfields(e, v, r, 'dateField', 'info');

		}// end getField
	}
};