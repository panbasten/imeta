jQuery.imeta.steps.replacestring = {
	btn : {
		getfields : function(e, v) {
			var r = [ {
				id : 'fieldId',
				type : 'number',
				text : ''
			}, {
				id : 'fieldInStream',
				type : 'input',
				text : 'fieldName'
			}, {
				id : 'fieldOutStream',
				type : 'input',
				text : ''
			}, {
				id : 'useRegEx',
				type : 'select',
				text : ''
			}, {
				id : 'replaceString',
				type : 'input',
				text : ''
			}, {
				id : 'replaceByString',
				type : 'input',
				text : ''
			}, {
				id : 'wholeWord',
				type : 'select',
				text : ''
			}, {
				id : 'caseSensitive',
				type : 'select',
				text : ''
			} ];
			jQuery.imeta.parameter.getfields(e, v, r);
		},
		replacestringAdd : function(c) {
			var rootId = c.getAttribute("rootId");
			var r = [ {
				id : rootId + '.fieldId',
				type : 'number',
				text : ''
			}, {
				id : rootId + '.fieldInStream',
				type : 'input',
				text : ''
			}, {
				id : rootId + '.fieldOutStream',
				type : 'input',
				text : ''
			}, {
				id : rootId + '.useRegEx',
				type : 'select',
				text : ''
			}, {
				id : rootId + '.replaceString',
				type : 'input',
				text : ''
			}, {
				id : rootId + '.replaceByString',
				type : 'input',
				text : ''
			}, {
				id : rootId + '.wholeWord',
				type : 'select',
				text : ''
			}, {
				id : rootId + '.caseSensitive',
				type : 'select',
				text : ''
			} ];

			jQuery.imetabar.createRowByHeader(r, rootId);
		}
	}
};