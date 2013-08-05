jQuery.imeta.steps.xmlinput = {
	btn : {
		getfields : function(e, v) {
			var r = [ {
				id : 'fieldId',
				type : 'number',
				text : ''
			}, {
				id : 'fieldName',
				type : 'input',
				text : 'fieldName'
			}, {
				id : 'fieldType',
				type : 'select',
				text : 'type'
			}, {
				id : 'fieldFormat',
				type : 'select',
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
			}, {
				id : 'fieldPosition',
				type : 'input',
				text : ''
			} ];
			jQuery.imeta.parameter.getfields(e, v, r);
		}
	},
	listeners : {
		includeFilenameListeners : function(e, v) {
			var elId = e.target.id;
			var id = elId.split(".")[0];
			if (e.target.checked) {
				$("[id=" + id + ".filenameField]").attr("disabled", false);
			} else {
				$("[id=" + id + ".filenameField]").attr("disabled", true);
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
	},
	selectedfiles : {
		btn : {
			fieldAdd : function(c) {
				var rootId = c.getAttribute("rootId");
				var r = [ {
					id : rootId + '.fieldId',
					type : 'number',
					text : ''
				}, {
					id : rootId + '.fileName',
					type : 'input',
					text : ''
				}, {
					id : rootId + '.fileMask',
					type : 'input',
					text : ''
				} ];
				jQuery.imetabar.createRowByHeader(r, rootId);
			}
		}
	},
	location : {
		btn : {
			fieldAdd : function(c) {
				var rootId = c.getAttribute("rootId");
				var r = [ {
					id : rootId + '.fieldId',
					type : 'number',
					text : ''
				}, {
					id : rootId + '.element',
					type : 'input',
					text : ''
				} ];
				jQuery.imetabar.createRowByHeader(r, rootId);
			}
		}
	},
	fields : {
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
					id : rootId + '.fieldFormat',
					type : 'select',
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
				}, {
					id : rootId + '.fieldPosition',
					type : 'input',
					text : ''
				} ];
				jQuery.imetabar.createRowByHeader(r, rootId);
			}
		}
	}
};