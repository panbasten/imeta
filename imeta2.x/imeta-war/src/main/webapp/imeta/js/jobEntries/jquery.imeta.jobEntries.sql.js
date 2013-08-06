jQuery.imeta.jobEntries.sql = {
	btn : {
		getfields : function(e, v) {
			var r = [ {
				id : 'fieldId',
				type : 'number',
				text : ''
			}, {
				id : 'arguments',
				type : 'input',
				text : 'fieldName'
			} ];
			jQuery.imeta.parameter.getfields(e, v, r);
		},
		argsAdd : function(c) {
			var rootId = c.getAttribute("rootId");
			var r = [ {
				id : rootId + '.fieldId',
				type : 'number',
				text : ''
			}, {
				id : rootId + '.arguments',
				type : 'input',
				text : ''
			} ];

			jQuery.imetabar.createRowByHeader(r, rootId);
		}
	},
	listeners : {
		sqlfromfile : function(e, v) {
			var elId = e.target.id;
			var id = elId.split(".")[0];
			if (e.target.checked) {
				$("[id=" + id + ".sqlfilename]").attr("disabled", false);
				$("[id=" + id + ".useVariableSubstitution]").attr("disabled",
						true);
				$("[id=" + id + ".sql]").attr("disabled", true);
			} else {
				$("[id=" + id + ".sqlfilename]").attr("disabled", true);
				$("[id=" + id + ".useVariableSubstitution]").attr("disabled",
						false);
				$("[id=" + id + ".sql]").attr("disabled", false);
			}
		}
	}
};