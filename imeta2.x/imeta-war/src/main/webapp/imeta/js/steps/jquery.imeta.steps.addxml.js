jQuery.imeta.steps.addxml = {
	btn : {
		getfields : function(e, v) {
			var r = [ {
				id : 'fieldId',
				type : 'number',
				text : ''
			}, {
				id : 'fieldName',
				type : 'input',
				text : 'fieldName'
			}, {
				id : 'fieldElement',
				type : 'input',
				text : 'fieldName'
			}, {
				id : 'fieldType',
				type : 'select',
				text : 'typeDesc'
			}, {
				id : 'fieldFormat',
				type : 'input',
				text : ''
			}, {
				id : 'fieldLength',
				type : 'input',
				text : 'length'
			}, {
				id : 'fieldPrecision',
				type : 'input',
				text : 'precision'
			}, {
				id : 'fieldCurrency',
				type : 'input',
				text : ''
			}, {
				id : 'fieldDecimal',
				type : 'input',
				text : 'decimal'
			}, {
				id : 'fieldGroup',
				type : 'input',
				text : 'group'
			}, {
				id : 'fieldNullif',
				type : 'input',
				text : ''
			}, {
				id : 'fieldAttribute',
				type : 'select',
				text : ''
			}, {
				id : 'fieldAttributeParentName',
				type : 'input',
				text : ''
			} ];
			jQuery.imeta.parameter.getfields(e, v, r);
		},
		fieldsAdd : function(c) {
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
				id : rootId + '.fieldElement',
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
			}, {
				id : rootId + '.fieldAttribute',
				type : 'select',
				text : ''
			}, {
				id : rootId + '.fieldAttributeParentName',
				type : 'input',
				text : ''
			} ];

			jQuery.imetabar.createRowByHeader(r, rootId);
		}
	}
};