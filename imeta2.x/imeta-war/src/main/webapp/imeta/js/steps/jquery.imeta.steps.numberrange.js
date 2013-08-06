jQuery.imeta.steps.numberrange = {
	btn : {
		numberrangeAdd : function(c) {
			var rootId = c.getAttribute("rootId");
			var r = [ {
				id : rootId + '.fieldId',
				type : 'number',
				text : ''
			}, {
				id : rootId + '.lowerBoundStr',
				type : 'input',
				text : ''
			}, {
				id : rootId + '.upperBoundStr',
				type : 'input',
				text : ''
			}, {
				id : rootId + '.value',
				type : 'input',
				text : ''
			} ];

			jQuery.imetabar.createRowByHeader(r, rootId);
		}
	}
};