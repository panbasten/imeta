jQuery.imeta.steps.textfileinput = {
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
				type : 'input',
				text : ''
			}, {
				id : 'fieldPosition',
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
				id : 'nullIf',
				type : 'input',
				text : ''
			}, {
				id : 'ifNull',
				type : 'input',
				text : ''
			}, {
				id : 'fieldTrimType',
				type : 'select',
				text : 'trimType'
			}, {
				id : 'fieldRepeat',
				type : 'select',
				text : ''
			} ];
			jQuery.imeta.parameter.getfields(e, v, r);
		}
	},
	pitch : {
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
	sift : {
		btn : {
			fieldAdd : function(c) {
				var rootId = c.getAttribute("rootId");
				var r = [ {
					id : rootId + '.siftId',
					type : 'number',
					text : ''
				}, {
					id : rootId + '.filterString',
					type : 'input',
					text : ''
				}, {
					id : rootId + '.locationFilter',
					type : 'input',
					text : ''
				}, {
					id : rootId + '.stopFilter',
					type : 'select',
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
					type : 'input',
					text : ''
				}, {
					id : rootId + '.fieldPosition',
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
					id : rootId + '.nullIf',
					type : 'input',
					text : ''
				}, {
					id : rootId + '.ifNull',
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
		acceptingFilenamesListeners : function(e, v) {
			var elId = e.target.id;
			var id = elId.split(".")[0];
			if (e.target.checked) {
				$("[id=" + id + ".fieldsorcatalog]").attr("disabled", true);
				// $("[id="+id+".btn.add]").attr("disabled",true);
				// $("[id="+id+".btn.browse]").attr("disabled",true);
				$("[id=" + id + ".codexexpression]").attr("disabled", true);
				// $("[id="+id+".btn.delete]").attr("disabled",true);
				// $("[id="+id+".btn.edit]").attr("disabled",true);
				$("[id=" + id + ".passingThruFields]").attr("disabled", false);
				$("[id=" + id + ".acceptingStepName]").attr("disabled", false);
				$("[id=" + id + ".acceptingField]").attr("disabled", false);
				// $("[id="+id+".btn.showname]").attr("disabled",true);
				// $("[id="+id+".btn.showcontent]").attr("disabled",true);
				// $("[id="+id+".btn.showfirstlinecontent]").attr("disabled",true);

				// 点击后，表格屏蔽选中文件的按钮和包含文件的字段名
				$("[id=" + id + "_pitch.btn.add]").attr("disabled", true);
				$("[id=" + id + "_pitch.btn.delete]").attr("disabled", true);
				$("[id=" + id + "_pitch.btn.add.root]").addClass(
						"x-item-disabled");
				$("[id=" + id + "_pitch.btn.delete.root]").addClass(
						"x-item-disabled");
				$("#" + id + "_pitch_gRoot input").attr("disabled", true);
			} else {
				$("[id=" + id + ".fieldsorcatalog]").attr("disabled", false);
				// $("[id="+id+".btn.add]").attr("disabled",false);
				// $("[id="+id+".btn.browse]").attr("disabled",false);
				$("[id=" + id + ".codexexpression]").attr("disabled", false);
				// $("[id="+id+".btn.delete]").attr("disabled",false);
				// $("[id="+id+".btn.edit]").attr("disabled",false);
				$("[id=" + id + ".passingThruFields]").attr("disabled", true);
				$("[id=" + id + ".acceptingStepName]").attr("disabled", true);
				$("[id=" + id + ".acceptingField]").attr("disabled", true);
				// $("[id="+id+".btn.showname]").attr("disabled",false);
				// $("[id="+id+".btn.showcontent]").attr("disabled",false);
				// $("[id="+id+".btn.showfirstlinecontent]").attr("disabled",false);

				// 点击不选中后，表格显示选中文件的按钮和包含文件的字段名
				$("[id=" + id + "_pitch.btn.add]").attr("disabled", false);
				$("[id=" + id + "_pitch.btn.delete]").attr("disabled", false);
				$("[id=" + id + "_pitch.btn.add.root]").removeClass(
						"x-item-disabled");
				$("[id=" + id + "_pitch.btn.delete.root]").removeClass(
						"x-item-disabled");
				$("#" + id + "_pitch_gRoot input").attr("disabled", false);
			}
		},
		header : function(e, v) {
			var elId = e.target.id;
			var id = elId.split(".")[0];
			if (e.target.checked) {
				$("[id=" + id + ".nrHeaderLines]").attr("disabled", false);
			} else {
				$("[id=" + id + ".nrHeaderLines]").attr("disabled", true);
			}
		},
		footer : function(e, v) {
			var elId = e.target.id;
			var id = elId.split(".")[0];
			if (e.target.checked) {
				$("[id=" + id + ".nrFooterLines]").attr("disabled", false);
			} else {
				$("[id=" + id + ".nrFooterLines]").attr("disabled", true);
			}
		},
		lineWrapped : function(e, v) {
			var elId = e.target.id;
			var id = elId.split(".")[0];
			if (e.target.checked) {
				$("[id=" + id + ".nrWraps]").attr("disabled", false);
			} else {
				$("[id=" + id + ".nrWraps]").attr("disabled", true);
			}
		},
		layoutPaged : function(e, v) {
			var elId = e.target.id;
			var id = elId.split(".")[0];
			if (e.target.checked) {
				$("[id=" + id + ".nrLinesPerPage]").attr("disabled", false);
				$("[id=" + id + ".nrLinesDocHeader]").attr("disabled", false);
			} else {
				$("[id=" + id + ".nrLinesPerPage]").attr("disabled", true);
				$("[id=" + id + ".nrLinesDocHeader]").attr("disabled", true);
			}
		},
		includeFilename : function(e, v) {
			var elId = e.target.id;
			var id = elId.split(".")[0];
			if (e.target.checked) {
				$("[id=" + id + ".filenameField]").attr("disabled", false);
			} else {
				$("[id=" + id + ".filenameField]").attr("disabled", true);
			}
		},
		includeRowNumber : function(e, v) {
			var elId = e.target.id;
			var id = elId.split(".")[0];
			if (e.target.checked) {
				$("[id=" + id + ".rowNumberField]").attr("disabled", false);
				$("[id=" + id + ".rowNumberByFile]").attr("disabled", false);
			} else {
				$("[id=" + id + ".rowNumberField]").attr("disabled", true);
				$("[id=" + id + ".rowNumberByFile]").attr("disabled", true);
			}
		},
		errorIgnored : function(e, v) {
			var elId = e.target.id;
			var id = elId.split(".")[0];
			if (e.target.checked) {
				$("[id=" + id + ".errorLineSkipped]").attr("disabled", false);
				$("[id=" + id + ".errorCountField]").attr("disabled", false);
				$("[id=" + id + ".errorFieldsField]").attr("disabled", false);
				$("[id=" + id + ".errorTextField]").attr("disabled", false);
				$("[id=" + id + ".warningFilesDestinationDirectory]").attr(
						"disabled", false);
				$("[id=" + id + ".warningFilesExtension]").attr("disabled",
						false);
				$("[id=" + id + ".variable]").attr("disabled", false);
				$("[id=" + id + ".browse]").attr("disabled", false);
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
				$("[id=" + id + ".errorCountField]").attr("disabled", true);
				$("[id=" + id + ".errorFieldsField]").attr("disabled", true);
				$("[id=" + id + ".errorTextField]").attr("disabled", true);
				$("[id=" + id + ".warningFilesDestinationDirectory]").attr(
						"disabled", true);
				$("[id=" + id + ".warningFilesExtension]").attr("disabled",
						true);
				$("[id=" + id + ".variable]").attr("disabled", true);
				$("[id=" + id + ".browse]").attr("disabled", true);
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