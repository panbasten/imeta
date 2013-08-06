jQuery.imeta.steps.switchcase = {
	btn : {
		caseValuesAdd : function(c) {
			var rootId = c.getAttribute("rootId");
			var r = [ {
				id : rootId + '.caseId',
				type : 'number',
				text : ''
			}, {
				id : rootId + '.caseValues',
				type : 'input',
				text : ''
			}, {
				id : rootId + '.caseTargetStepnames',
				type : 'select',
				text : ''
			} ];

			jQuery.imetabar.createRowByHeader(r, rootId);
		}
	}
};