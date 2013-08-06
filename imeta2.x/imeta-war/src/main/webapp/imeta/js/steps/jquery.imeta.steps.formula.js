jQuery.imeta.steps.formula = {
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
				id : rootId + '.formula',
				type : 'input',
				text : ''
			}, {
				id : rootId + '.valueType',
				type : 'select',
				text : ''
			}, {
				id : rootId + '.valueLength',
				type : 'input',
				text : ''
			}, {
				id : rootId + '.valuePrecision',
				type : 'input',
				text : ''
			}, {
				id : rootId + '.replaceField',
				type : 'input',
				text : ''
			} ];

			jQuery.imetabar.createRowByHeader(r, rootId);
		}
	}
};