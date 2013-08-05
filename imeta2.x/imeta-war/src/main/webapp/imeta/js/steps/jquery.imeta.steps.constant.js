jQuery.imeta.steps.constant = {
	btn : {
		constantAdd : function(c) {
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
				id : rootId + '.currency',
				type : 'input',
				text : ''
			}, {
				id : rootId + '.decimal',
				type : 'input',
				text : ''
			}, {
				id : rootId + '.group',
				type : 'input',
				text : ''
			}, {
				id : rootId + '.value',
				type : 'input',
				text : ''
			} ];

			jQuery.imetabar.createRowByHeader(r, rootId);
		}
	}
};