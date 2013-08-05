jQuery.imeta.steps.tableinput = {
	listeners : {

	},
	btn : {
		parametersAdd : function(c) {
			var rootId = c.getAttribute("rootId");

			var r = [ {
				id : rootId + '.parameterId',
				type : 'number',
				text : ''
			}, {
				id : rootId + '.parameterName',
				type : 'input',
				text : ''
			} ];

			jQuery.imetabar.createRowByHeader(r, rootId);
		}
	}
};