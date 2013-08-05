jQuery.imeta.steps.calculator = {
	btn : {
		calculatorAdd : function(c) {
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
				id : rootId + '.calcType',
				type : 'select',
				text : ''
			}, {
				id : rootId + '.fieldA',
				type : 'input',
				text : ''
			}, {
				id : rootId + '.fieldB',
				type : 'input',
				text : ''
			}, {
				id : rootId + '.fieldC',
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
				id : rootId + '.removedFromResult',
				type : 'select',
				text : ''
			}, {
				id : rootId + '.conversionMask',
				type : 'input',
				text : ''
			}, {
				id : rootId + '.decimalSymbol',
				type : 'input',
				text : ''
			}, {
				id : rootId + '.groupingSymbol',
				type : 'input',
				text : ''
			}, {
				id : rootId + '.currencySymbol',
				type : 'input',
				text : ''
			} ];

			jQuery.imetabar.createRowByHeader(r, rootId);
		}
	}
};