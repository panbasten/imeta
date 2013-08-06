jQuery.imeta.jobEntries.columnsexist = {
	btn : {
		lineAdd : function(c) {
			var rootId = c.getAttribute("rootId");
			var r = [ {
				id : rootId + '.lineId',
				type : 'number',
				text : ''
			}, {
				id : rootId + '.arguments',
				type : 'input',
				text : ''
			} ];

			jQuery.imetabar.createRowByHeader(r, rootId);
		}
	}
};