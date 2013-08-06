jQuery.imeta.jobEntries.jobentrytruncatetables = {
	btn : {
		fieldAdd : function(c) {
			var rootId = c.getAttribute("rootId");
			var r = [ {
				id : rootId + '.fieldId',
				type : 'number',
				text : ''
			}, {
				id : rootId + '.arguments',
				type : 'input',
				text : ''
			}, {
				id : rootId + '.schemaname',
				type : 'input',
				text : ''
			} ];
			jQuery.imetabar.createRowByHeader(r, rootId);
		}
	}
};