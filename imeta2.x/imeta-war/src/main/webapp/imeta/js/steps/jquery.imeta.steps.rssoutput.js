jQuery.imeta.steps.rssoutput = {
	btn : {
		channelFields1Add : function(c) {
			var rootId = c.getAttribute("rootId");

			var r = [ {
				id : rootId + '.fieldId',
				type : 'number',
				text : ''
			}, {
				id : rootId + '.ChannelCustomTags',
				type : 'input',
				text : ''
			}, {
				id : rootId + '.ChannelCustomFields',
				type : 'input',
				text : ''
			} ];

			jQuery.imetabar.createRowByHeader(r, rootId);
		},
		itemFields1Add : function(c) {
			var rootId = c.getAttribute("rootId");

			var r = [ {
				id : rootId + '.fieldId',
				type : 'number',
				text : ''
			}, {
				id : rootId + '.ItemCustomTags',
				type : 'input',
				text : ''
			}, {
				id : rootId + '.ItemCustomFields',
				type : 'input',
				text : ''
			} ]

			jQuery.imetabar.createRowByHeader(r, rootId);

		},
		namespacesAdd : function(c) {
			var rootId = c.getAttribute("rootId");

			var r = [ {
				id : rootId + '.fieldId',
				type : 'number',
				text : ''
			}, {
				id : rootId + '.NameSpacesTitle',
				type : 'input',
				text : ''
			}, {
				id : rootId + '.NameSpaces',
				type : 'input',
				text : ''
			} ];

			jQuery.imetabar.createRowByHeader(r, rootId);
		}
	}
};