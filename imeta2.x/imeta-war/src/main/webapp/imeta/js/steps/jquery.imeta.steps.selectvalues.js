jQuery.imeta.steps.selectvalues = {
	btn : {
		getchoosefields : function(e, v) {
			var r = [ {
				id : 'fieldId',
				type : 'number',
				text : ''
			}, {
				id : 'selectName',
				type : 'input',
				text : 'fieldName'
			}, {
				id : 'selectRename',
				type : 'input',
				text : ''
			}, {
				id : 'selectLength',
				type : 'input',
				text : 'length'
			}, {
				id : 'selectPrecision',
				type : 'input',
				text : 'precision'
			} ];
			jQuery.imeta.parameter.getfields(e, v, r, 'fields');
		},
		getremovefields : function(e, v) {
			var r = [ {
				id : 'fieldId',
				type : 'number',
				text : ''
			}, {
				id : 'deleteName',
				type : 'input',
				text : 'fieldName'
			} ];
			jQuery.imeta.parameter.getfields(e, v, r, 'removefields');
		},
		getmetadata : function(e, v) {
			var r = [ {
				id : 'fieldId',
				type : 'number',
				text : ''
			}, {
				id : 'name',
				type : 'input',
				text : 'fieldName'
			}, {
				id : 'rename',
				type : 'input',
				text : ''
			}, {
				id : 'type',
				type : 'select',
				text : 'type'
			}, {
				id : 'length',
				type : 'input',
				text : 'length'
			}, {
				id : 'precision',
				type : 'input',
				text : 'precision'
			}, {
				id : 'storageType',
				type : 'select',
				text : ''
			}, {
				id : 'conversionMask',
				type : 'input',
				text : ''
			}, {
				id : 'decimalSymbol',
				type : 'input',
				text : 'decimal'
			}, {
				id : 'groupingSymbol',
				type : 'input',
				text : 'group'
			}, {
				id : 'currencySymbol',
				type : 'input',
				text : ''
			} ];
			jQuery.imeta.parameter.getfields(e, v, r, 'metadata');
		},
		fieldAdd : function(c) {
			var rootId = c.getAttribute("rootId");
			var r = [ {
				id : rootId + '.fieldId',
				type : 'number',
				text : ''
			}, {
				id : rootId + '.selectName',
				type : 'input',
				text : ''
			}, {
				id : rootId + '.selectRename',
				type : 'input',
				text : ''
			}, {
				id : rootId + '.selectLength',
				type : 'input',
				text : ''
			}, {
				id : rootId + '.selectPrecision',
				type : 'input',
				text : ''
			}

			];

			jQuery.imetabar.createRowByHeader(r, rootId);
		},
		removeAdd : function(c) {
			var rootId = c.getAttribute("rootId");
			var r = [ {
				id : rootId + '.fieldId',
				type : 'number',
				text : ''
			}, {
				id : rootId + '.deleteName',
				type : 'input',
				text : ''
			} ];

			jQuery.imetabar.createRowByHeader(r, rootId);
		},
		metaAdd : function(c) {
			var rootId = c.getAttribute("rootId");
			var r = [ {
				id : rootId + '.fieldId',
				type : 'number',
				text : ''
			}, {
				id : rootId + '.name',
				type : 'input',
				text : ''
			}, {
				id : rootId + '.rename',
				type : 'input',
				text : ''
			}, {
				id : rootId + '.type',
				type : 'select',
				text : ''
			}, {
				id : rootId + '.length',
				type : 'input',
				text : ''
			}, {
				id : rootId + '.precision',
				type : 'input',
				text : ''
			}, {
				id : rootId + '.storageType',
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
			}

			];

			jQuery.imetabar.createRowByHeader(r, rootId);
		}
	}
};