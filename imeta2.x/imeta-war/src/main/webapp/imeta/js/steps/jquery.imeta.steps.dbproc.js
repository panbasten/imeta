jQuery.imeta.steps.dbproc = {
	btn : {
		getfields : function(e, v) {
			var r = [ {
				id : 'keywordsId',
				type : 'number',
				text : ''
			}, {
				id : 'argument',
				type : 'input',
				text : 'fieldName'
			}, {
				id : 'argumentDirection',
				type : 'select',
				text : ''
			}, {
				id : 'argumentType',
				type : 'select',
				text : 'type'
			} ];
			jQuery.imeta.parameter.getfields(e, v, r, 'keywords');
		},
		dbprocAdd : function(c) {
			var rootId = c.getAttribute("rootId");
			var r = [ {
				id : rootId + '.keywordsId',
				type : 'number',
				text : ''
			}, {
				id : rootId + '.argument',
				type : 'input',
				text : ''
			}, {
				id : rootId + '.argumentDirection',
				type : 'select',
				text : ''
			}, {
				id : rootId + '.argumentType',
				type : 'select',
				text : ''
			} ];
			jQuery.imetabar.createRowByHeader(r, rootId);
		}
	}
};