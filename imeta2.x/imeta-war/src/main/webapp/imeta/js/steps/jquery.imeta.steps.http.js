jQuery.imeta.steps.http = {
	listeners : {
		urlInField : function(e, v) {
			var elId = e.target.id;
			var id = elId.split(".")[0];
			if (e.target.checked) {
				$("[id=" + id + ".url]").attr("disabled", true);
				$("[id=" + id + ".urlField]").attr("disabled", false);

			} else {
				$("[id=" + id + ".url]").attr("disabled", false);
				$("[id=" + id + ".urlField]").attr("disabled", true);

			}
		}
	},
	btn : {
		getfields : function(e, v) {
			var r = [ {
				id : 'fieldId',
				type : 'number',
				text : ''
			}, {
				id : 'argumentField',
				type : 'input',
				text : 'fieldName'
			}, {
				id : 'argumentParameter',
				type : 'input',
				text : 'fieldName'
			} ];
			jQuery.imeta.parameter.getfields(e, v, r);
		},
		fieldAdd : function(c) {
			var rootId = c.getAttribute("rootId");
			var r = [ {
				id : rootId + '.fieldId',
				type : 'number',
				text : ''
			}, {
				id : rootId + '.argumentField',
				type : 'input',
				text : ''
			}, {
				id : rootId + '.argumentParameter',
				type : 'input',
				text : ''
			} ];
			jQuery.imetabar.createRowByHeader(r, rootId);
		}
	}
};