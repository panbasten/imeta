jQuery.imeta.steps.ldapinput = {
	btn : {
		fieldAdd : function(c) {
			var rootId = c.getAttribute("rootId");
			var r = [ {
				id : rootId + '.fieldId',
				type : 'number',
				text : ''
			}, {
				id : rootId + '.fieldName',
				type : 'input',
				text : ''
			}, {
				id : rootId + '.fieldType',
				type : 'select',
				text : ''
			}, {
				id : rootId + '.fieldAttribute',
				type : 'input',
				text : ''
			}, {
				id : rootId + '.fieldFormat',
				type : 'input',
				text : ''
			}, {
				id : rootId + '.fieldLength',
				type : 'input',
				text : ''
			}, {
				id : rootId + '.fieldPrecision',
				type : 'input',
				text : ''
			}, {
				id : rootId + '.fieldCurrency',
				type : 'input',
				text : ''
			}, {
				id : rootId + '.fieldDecimal',
				type : 'input',
				text : ''
			}, {
				id : rootId + '.fieldGroup',
				type : 'input',
				text : ''
			}, {
				id : rootId + '.fieldTrimType',
				type : 'select',
				text : ''
			}, {
				id : rootId + '.fieldRepeat',
				type : 'select',
				text : ''
			} ];
			jQuery.imetabar.createRowByHeader(r, rootId);
		},
		getfields : function(e, v) {
			var r = [ {
				id : 'fieldId',
				type : 'number',
				text : ''
			}, {
				id : 'fieldName',
				type : 'input',
				text : 'fieldName '
			}, {
				id : 'fieldType',
				type : 'select',
				text : 'type'
			}, {
				id : 'fieldAttribute',
				type : 'input',
				text : ''
			}, {
				id : 'fieldFormat',
				type : 'input',
				text : ''
			}, {
				id : 'fieldLength',
				type : 'input',
				text : 'length'
			}, {
				id : 'fieldPrecision',
				type : 'input',
				text : 'precision'
			}, {
				id : 'fieldCurrency',
				type : 'input',
				text : ''
			}, {
				id : 'fieldDecimal',
				type : 'input',
				text : 'decimal'
			}, {
				id : 'fieldGroup',
				type : 'input',
				text : 'group'
			}, {
				id : 'fieldTrimType',
				type : 'select',
				text : ''
			}, {
				id : 'fieldRepeat',
				type : 'select',
				text : ''
			} ];
			jQuery.imeta.parameter.getfields(e, v, r);
		}
	},
	listeners : {
		useAuthenticationListeners : function(e, v) {
			var elId = e.target.id;
			var id = elId.split(".")[0];
			if (e.target.checked) {
				$("[id=" + id + ".userName]").attr("disabled", false);
				$("[id=" + id + ".password]").attr("disabled", false);
			} else {
				$("[id=" + id + ".userName]").attr("disabled", true);
				$("[id=" + id + ".password]").attr("disabled", true);
			}
		},
		includeRowNumberListeners : function(e, v) {
			var elId = e.target.id;
			var id = elId.split(".")[0];
			if (e.target.checked) {
				$("[id=" + id + ".rowNumberField]").attr("disabled", false);
			} else {
				$("[id=" + id + ".rowNumberField]").attr("disabled", true);
			}
		}
	}
};