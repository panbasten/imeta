jQuery.imeta.steps.valuemapper = {
	btn : {
		fieldAdd : function(c) {
			var rootId = c.getAttribute("rootId");
			var r = [ {
				id : rootId + '.field',
				type : 'number',
				text : ''
			}, {
				id : rootId + '.sourceValue',
				type : 'input',
				text : ''
			}, {
				id : rootId + '.targetValue',
				type : 'input',
				text : ''
			}

			];

			jQuery.imetabar.createRowByHeader(r, rootId);
		}
	}
};