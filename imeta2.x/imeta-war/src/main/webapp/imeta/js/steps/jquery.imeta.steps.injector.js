jQuery.imeta.steps.injector = {
	btn : {
		injectorAdd : function(c) {
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
			} ];

			jQuery.imetabar.createRowByHeader(r, rootId);
		}
	}
};