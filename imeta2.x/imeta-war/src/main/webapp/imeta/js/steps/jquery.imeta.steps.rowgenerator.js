jQuery.imeta.steps.rowgenerator = {
	btn : {
		fieldAdd : function(c) {
			var rootId = c.getAttribute("rootId");
			var r = [ {
				id : rootId + '.fieldId',
				type : 'number',
				text : ''
			}, {
				id : rootId + '.fieldName',
				type : 'input',
				text : ''
			}, {
				id : rootId + '.fieldType',
				type : 'select',
				text : ''
			}, {
				id : rootId + '.fieldFormat',
				type : 'input',
				text : ''
			}, {
				id : rootId + '.fieldLength',
				type : 'input',
				text : ''
			}, {
				id : rootId + '.fieldPrecision',
				type : 'input',
				text : ''
			}, {
				id : rootId + '.fieldCurrency',
				type : 'input',
				text : ''
			}, {
				id : rootId + '.fieldDecimal',
				type : 'input',
				text : ''
			}, {
				id : rootId + '.fieldGroup',
				type : 'input',
				text : ''
			}, {
				id : rootId + '.fieldNullif',
				type : 'input',
				text : ''
			} ];
			jQuery.imetabar.createRowByHeader(r, rootId);
		}
	}
};