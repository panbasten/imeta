jQuery.imeta.steps.uniquerows = {
	btn : {
		getfields : function(e, v) {
			var r = [ {
				id : 'fieldId',
				type : 'number',
				text : ''
			}, {
				id : 'compareFields',
				type : 'input',
				text : 'fieldName'
			}, {
				id : 'caseInsensitive',
				type : 'select',
				text : ''
			} ];
			jQuery.imeta.parameter.getfields(e, v, r);
		},
		uniquerowsAdd : function(c) {
			var rootId = c.getAttribute("rootId");
			;
			var r = [ {
				id : rootId + '.fieldId',
				type : 'number',
				text : ''
			}, {
				id : rootId + '.compareFields',
				type : 'input',
				text : ''
			}, {
				id : rootId + '.caseInsensitive',
				type : 'select',
				text : ''
			} ];

			jQuery.imetabar.createRowByHeader(r, rootId);
		}
	},
	listeners : {
		countRows : function(e, v) {
			var elId = e.target.id;
			var id = elId.split(".")[0];
			if (e.target.checked) {
				$("[id=" + id + ".countField]").attr("disabled", false);
			} else {
				$("[id=" + id + ".countField]").attr("disabled", true);
			}
		}
	}
};