jQuery.imeta.steps.denormaliser = {
	btn : {
		getwords : function(e, v) {
			var r = [ {
				id : 'fieldId',
				type : 'number',
				text : ''
			}, {
				id : 'groupField',
				type : 'input',
				text : 'fieldName'
			} ];
			jQuery.imeta.parameter.getfields(e, v, r, 'consistwords');
		},
		getdemandwords : function(e, v) {
			var r = [ {
				id : 'fieldId',
				type : 'number',
				text : ''
			}, {
				id : 'targetName',
				type : 'input',
				text : ''
			}, {
				id : 'fieldName',
				type : 'input',
				text : 'fieldName'
			}, {
				id : 'keyValue',
				type : 'input',
				text : ''
			}, {
				id : 'targetType',
				type : 'select',
				text : 'type'
			}, {
				id : 'targetFormat',
				type : 'input',
				text : ''
			}, {
				id : 'targetLength',
				type : 'input',
				text : 'length'
			}, {
				id : 'targetPrecision',
				type : 'input',
				text : 'precision'
			}, {
				id : 'targetCurrencySymbol',
				type : 'input',
				text : ''
			}, {
				id : 'targetDecimalSymbol',
				type : 'input',
				text : 'decimal'
			}, {
				id : 'targetGroupingSymbol',
				type : 'input',
				text : 'group'
			}, {
				id : 'targetNullString',
				type : 'input',
				text : ''
			}, {
				id : 'targetAggregationType',
				type : 'select',
				text : ''
			} ];

			jQuery.imeta.parameter.getfields(e, v, r, 'obwords');

		},
		consistwordsAdd : function(c) {
			var rootId = c.getAttribute("rootId");
			var r = [ {
				id : rootId + '.fieldId',
				type : 'number',
				text : ''
			}, {
				id : rootId + '.groupField',
				type : 'input',
				text : ''
			} ];

			jQuery.imetabar.createRowByHeader(r, rootId);
		},
		obwordsAdd : function(c) {
			var rootId = c.getAttribute("rootId");

			var r = [ {
				id : rootId + '.fieldId',
				type : 'number',
				text : ''
			}, {
				id : rootId + '.targetName',
				type : 'input',
				text : ''
			}, {
				id : rootId + '.fieldName',
				type : 'input',
				text : ''
			}, {
				id : rootId + '.keyValue',
				type : 'input',
				text : ''
			}, {
				id : rootId + '.targetType',
				type : 'select',
				text : ''
			}, {
				id : rootId + '.targetFormat',
				type : 'input',
				text : ''
			}, {
				id : rootId + '.targetLength',
				type : 'input',
				text : ''
			}, {
				id : rootId + '.targetPrecision',
				type : 'input',
				text : ''
			}, {
				id : rootId + '.targetCurrencySymbol',
				type : 'input',
				text : ''
			}, {
				id : rootId + '.targetDecimalSymbol',
				type : 'input',
				text : ''
			}, {
				id : rootId + '.targetGroupingSymbol',
				type : 'input',
				text : ''
			}, {
				id : rootId + '.targetNullString',
				type : 'input',
				text : ''
			}, {
				id : rootId + '.targetAggregationType',
				type : 'select',
				text : ''
			} ];

			jQuery.imetabar.createRowByHeader(r, rootId);
		}
	}
};