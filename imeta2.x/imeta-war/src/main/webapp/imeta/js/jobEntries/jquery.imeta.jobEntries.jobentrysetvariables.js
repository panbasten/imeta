jQuery.imeta.jobEntries.jobentrysetvariables = {
	btn : {
		fieldAdd : function(c) {
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
				id : rootId + '.value',
				type : 'input',
				text : ''
			}, {
				id : rootId + '.type',
				type : 'select',
				text : ''
			} ];
			jQuery.imetabar.createRowByHeader(r, rootId);
		}
	}
};