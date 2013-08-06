jQuery.imeta.steps.blockUntilStepsFinish = {
	btn : {
		fieldsAdd : function(c) {
			var rootId = c.getAttribute("rootId");

			var r = [ {
				id : rootId + '.fieldId',
				type : 'number',
				text : ''
			}, {
				id : rootId + '.stepName',
				type : 'input',
				text : ''
			}, {
				id : rootId + '.stepCopyNr',
				type : 'input',
				text : '0'
			} ];

			jQuery.imetabar.createRowByHeader(r, rootId);
		}
	}
};