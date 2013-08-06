jQuery.imeta.steps.httppost = {
	btn : {
		getbodyfields : function(e, v) {
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
			jQuery.imeta.parameter.getfields(e, v, r, 'bodyParameters');
		},
		getqueryfields : function(e, v) {
			var r = [ {
				id : 'fieldId',
				type : 'number',
				text : ''
			}, {
				id : 'queryField',
				type : 'input',
				text : 'fieldName'
			}, {
				id : 'queryParameter',
				type : 'input',
				text : 'fieldName'
			} ];
			jQuery.imeta.parameter.getfields(e, v, r, 'queryParameters');
		}
	},
	bodyfield : {
		btn : {
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
	},
	queryfield : {
		btn : {
			fieldAdd : function(c) {
				var rootId = c.getAttribute("rootId");
				var r = [ {
					id : rootId + '.fieldId',
					type : 'number',
					text : ''
				}, {
					id : rootId + '.queryField',
					type : 'input',
					text : ''
				}, {
					id : rootId + '.queryParameter',
					type : 'input',
					text : ''
				} ];
				jQuery.imetabar.createRowByHeader(r, rootId);
			}
		}
	},
	listeners : {
		urlInFieldListeners : function(e, v) {
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
	}
};