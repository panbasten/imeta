jQuery.imeta.steps.excelinput = {
	btn : {
		getsheetsname : function(e, v) {
			var elId = e.target.id;
			var id = elId.split(".")[0];
			if ($.iformwindow.activeWindow(id + "_explorer")) {
				return;
			}

			var win = $.iformwindow('#ibody', {
				id : id + "_explorer",
				title : "输入列表",
				showLoading : true
			});
		},
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
				id : 'fieldLength',
				type : 'input',
				text : 'length'
			}, {
				id : 'fieldPrecision',
				type : 'input',
				text : 'precision'
			}, {
				id : 'fieldTrimType',
				type : 'select',
				text : ''
			}, {
				id : 'fieldRepeat',
				type : 'select',
				text : ''
			}, {
				id : 'fieldFormat',
				type : 'input',
				text : ''
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
				}, {
					id : rootId + '.fileRequired',
					type : 'input',
					text : ''
				} ];
				jQuery.imetabar.createRowByHeader(r, rootId);
			}
		}
	},
	selectedsheets : {
		btn : {
			fieldAdd : function(c) {
				var rootId = c.getAttribute("rootId");
				var r = [ {
					id : rootId + '.sheetId',
					type : 'number',
					text : ''
				}, {
					id : rootId + '.sheetName',
					type : 'input',
					text : ''
				}, {
					id : rootId + '.startRow',
					type : 'input',
					text : ''
				}, {
					id : rootId + '.startColumn',
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
					id : rootId + '.fieldLength',
					type : 'input',
					text : ''
				}, {
					id : rootId + '.fieldPrecision',
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
					id : rootId + '.fieldFormat',
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
				} ];

				jQuery.imetabar.createRowByHeader(r, rootId);
			}
		}
	},
	listeners : {
		getFilesNameListeners : function(e, v) {
			var elId = e.target.id;
			var id = elId.split(".")[0];
			if (e.target.checked) {
				$("[id=" + id + ".stepFilesName]").attr("disabled", false);
				$("[id=" + id + ".stepFieldFilesName]").attr("disabled", false);
				$("[id=" + id + ".fileOrDir]").attr("disabled", true);
				$("[id=" + id + ".ruleExpression]").attr("disabled", true);
				// 点击后，表格屏蔽选中文件的按钮和包含文件的字段名
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
				$("[id=" + id + ".stepFilesName]").attr("disabled", true);
				$("[id=" + id + ".stepFieldFilesName]").attr("disabled", true);
				$("[id=" + id + ".fileOrDir]").attr("disabled", false);
				$("[id=" + id + ".ruleExpression]").attr("disabled", false);
				// 点击不选中后，表格显示选中文件的按钮和包含文件的字段名
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
		errorIgnoredListeners : function(e, v) {
			var elId = e.target.id;
			var id = elId.split(".")[0];
			if (e.target.checked) {
				$("[id=" + id + ".errorLineSkipped]").attr("disabled", false);
				$("[id=" + id + ".warningFilesDestinationDirectory]").attr(
						"disabled", false);
				$("[id=" + id + ".warningFilesExtension]").attr("disabled",
						false);
				$("[id=" + id + ".errorFilesDestinationDirectory]").attr(
						"disabled", false);
				$("[id=" + id + ".errorFilesExtension]")
						.attr("disabled", false);
				$("[id=" + id + ".lineNumberFilesDestinationDirectory]").attr(
						"disabled", false);
				$("[id=" + id + ".lineNumberFilesExtension]").attr("disabled",
						false);
			} else {
				$("[id=" + id + ".errorLineSkipped]").attr("disabled", true);
				$("[id=" + id + ".warningFilesDestinationDirectory]").attr(
						"disabled", true);
				$("[id=" + id + ".warningFilesExtension]").attr("disabled",
						true);
				$("[id=" + id + ".errorFilesDestinationDirectory]").attr(
						"disabled", true);
				$("[id=" + id + ".errorFilesExtension]").attr("disabled", true);
				$("[id=" + id + ".lineNumberFilesDestinationDirectory]").attr(
						"disabled", true);
				$("[id=" + id + ".lineNumberFilesExtension]").attr("disabled",
						true);
			}
		}
	}
};