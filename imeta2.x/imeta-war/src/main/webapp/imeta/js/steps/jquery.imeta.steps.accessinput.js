jQuery.imeta.steps.accessinput = {
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
				id : 'fieldColumn',
				type : 'input',
				text : ''
			}, {
				id : 'fieldType',
				type : 'select',
				text : 'type'
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
	selectedfiles : {
		btn : {
			fieldAdd : function(c) {
				var rootId = c.getAttribute("rootId");
				var r = [ {
					id : rootId + '.fileId',
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
					id : rootId + '.fieldColumn',
					type : 'input',
					text : ''
				}, {
					id : rootId + '.fieldType',
					type : 'select',
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
			}
		}
	},
	listeners : {
		isFileFieldListeners : function(e, v) {
			var elId = e.target.id;
			var id = elId.split(".")[0];
			if (e.target.checked) {
				$("[id=" + id + ".dynamicFilenameField]").attr("disabled",
						false);
				$("[id=" + id + ".fileOrpath]").attr("disabled", true);
				$("[id=" + id + ".regularExpression]").attr("disabled", true);
				$("[id=" + id + ".includeFilename]").attr("disabled", true);
				$("[id=" + id + ".rowLimit]").attr("disabled", true);
				$("[id=" + id + ".filenameField]").attr("disabled", true);
				$("[id=" + id + ".includeFilename]").attr("checked", false);

				$("[id=" + id + "_selectedFiles.btn.add]").attr("disabled",
						true);
				$("[id=" + id + "_selectedFiles.btn.delete]").attr("disabled",
						true);
				$("[id=" + id + "_selectedFiles.btn.add.root]").addClass(
						"x-item-disabled");
				$("[id=" + id + "_selectedFiles.btn.delete.root]").addClass(
						"x-item-disabled");
				$("#" + id + "_selectedFiles_gRoot input").attr("disabled",
						true);
			} else {
				$("[id=" + id + ".dynamicFilenameField]")
						.attr("disabled", true);
				$("[id=" + id + ".fileOrpath]").attr("disabled", false);
				$("[id=" + id + ".regularExpression]").attr("disabled", false);
				$("[id=" + id + ".includeFilename]").attr("disabled", false);
				$("[id=" + id + ".rowLimit]").attr("disabled", false);
				// $("[id="+id+".filenameField]").attr("disabled",false);

				$("[id=" + id + "_selectedFiles.btn.add]").attr("disabled",
						false);
				$("[id=" + id + "_selectedFiles.btn.delete]").attr("disabled",
						false);
				$("[id=" + id + "_selectedFiles.btn.add.root]").removeClass(
						"x-item-disabled");
				$("[id=" + id + "_selectedFiles.btn.delete.root]").removeClass(
						"x-item-disabled");
				$("#" + id + "_selectedFiles_gRoot input").attr("disabled",
						false);
			}
		},
		includefilenameListeners : function(e, v) {
			var elId = e.target.id;
			var id = elId.split(".")[0];
			if (e.target.checked) {
				$("[id=" + id + ".filenameField]").attr("disabled", false);
			} else {
				$("[id=" + id + ".filenameField]").attr("disabled", true);
			}
		},
		includeTablenameListeners : function(e, v) {
			var elId = e.target.id;
			var id = elId.split(".")[0];
			if (e.target.checked) {
				$("[id=" + id + ".tablenameField]").attr("disabled", false);
			} else {
				$("[id=" + id + ".tablenameField]").attr("disabled", true);
			}
		},
		includeRowNumberListeners : function(e, v) {
			var elId = e.target.id;
			var id = elId.split(".")[0];
			if (e.target.checked) {
				$("[id=" + id + ".rowNumberField]").attr("disabled", false);
				$("[id=" + id + ".resetRowNumber]").attr("disabled", false);
			} else {
				$("[id=" + id + ".rowNumberField]").attr("disabled", true);
				$("[id=" + id + ".resetRowNumber]").attr("disabled", true);
			}
		}
	}
};