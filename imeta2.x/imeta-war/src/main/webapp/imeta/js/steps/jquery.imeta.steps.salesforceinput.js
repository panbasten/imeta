jQuery.imeta.steps.salesforceinput = {
	btn : {
		getfields : function(e, v) {
			var r = [ {
				id : 'fieldId',
				type : 'number',
				text : ''
			}, {
				id : 'name',
				type : 'input',
				text : ''
			}, {
				id : 'field',
				type : 'input',
				text : 'fieldName'
			}, {
				id : 'type',
				type : 'select',
				text : 'type'
			}, {
				id : 'format',
				type : 'input',
				text : ''
			}, {
				id : 'length',
				type : 'input',
				text : 'length'
			}, {
				id : 'precision',
				type : 'input',
				text : 'precision'
			}, {
				id : 'currencySymbol',
				type : 'input',
				text : ''
			}, {
				id : 'decimalSymbol',
				type : 'input',
				text : 'decimal'
			}, {
				id : 'groupSymbol',
				type : 'input',
				text : 'group'
			}, {
				id : 'trimType',
				type : 'select',
				text : ''
			}, {
				id : 'repeated',
				type : 'select',
				text : ''
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
				id : rootId + '.name',
				type : 'input',
				text : ''
			}, {
				id : rootId + '.field',
				type : 'input',
				text : ''
			}, {
				id : rootId + '.type',
				type : 'select',
				text : ''
			}, {
				id : rootId + '.format',
				type : 'input',
				text : ''
			}, {
				id : rootId + '.length',
				type : 'input',
				text : ''
			}, {
				id : rootId + '.precision',
				type : 'input',
				text : ''
			}, {
				id : rootId + '.currencySymbol',
				type : 'input',
				text : ''
			}, {
				id : rootId + '.decimalSymbol',
				type : 'input',
				text : ''
			}, {
				id : rootId + '.groupSymbol',
				type : 'input',
				text : ''
			}, {
				id : rootId + '.trimType',
				type : 'select',
				text : ''
			}, {
				id : rootId + '.repeated',
				type : 'select',
				text : ''
			} ];
			jQuery.imetabar.createRowByHeader(r, rootId);
		}
	},
	listeners : {
		includeTargetURLListeners : function(e, v) {
			var elId = e.target.id;
			var id = elId.split(".")[0];
			if (e.target.checked) {
				$("[id=" + id + ".targetURLField]").attr("disabled", false);
			} else {
				$("[id=" + id + ".targetURLField]").attr("disabled", true);
			}
		},
		includeModuleListeners : function(e, v) {
			var elId = e.target.id;
			var id = elId.split(".")[0];
			if (e.target.checked) {
				$("[id=" + id + ".moduleField]").attr("disabled", false);
			} else {
				$("[id=" + id + ".moduleField]").attr("disabled", true);
			}
		},
		includeSQLListeners : function(e, v) {
			var elId = e.target.id;
			var id = elId.split(".")[0];
			if (e.target.checked) {
				$("[id=" + id + ".sqlField]").attr("disabled", false);
			} else {
				$("[id=" + id + ".sqlField]").attr("disabled", true);
			}
		},
		includeTimestampListeners : function(e, v) {
			var elId = e.target.id;
			var id = elId.split(".")[0];
			if (e.target.checked) {
				$("[id=" + id + ".timestampField]").attr("disabled", false);
			} else {
				$("[id=" + id + ".timestampField]").attr("disabled", true);
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