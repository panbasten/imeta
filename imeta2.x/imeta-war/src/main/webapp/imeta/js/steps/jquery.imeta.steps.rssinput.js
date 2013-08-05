jQuery.imeta.steps.rssinput = {
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
				type : 'select',
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
	urlList : {
		btn : {
			fieldAdd : function(c) {
				var rootId = c.getAttribute("rootId");
				var r = [ {
					id : rootId + '.urlId',
					type : 'number',
					text : ''
				}, {
					id : rootId + '.url',
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
					type : 'select',
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
		urlInField : function(e, v) {
			var elId = e.target.id;
			var id = elId.split(".")[0];
			if (e.target.checked) {
				$("[id=" + id + ".urlField]").attr("disabled", false);
				$("[id=" + id + ".includeUrl]").attr("disabled", true);
				$("[id=" + id + ".urlFieldname]").attr("disabled", true);
				$("[id=" + id + ".includeUrl]").attr("checked", false);

				// 点击后，表格屏蔽选中文件的按钮和包含文件的字段名
				$("[id=" + id + "_urlList.btn.add]").attr("disabled", true);
				$("[id=" + id + "_urlList.btn.delete]").attr("disabled", true);
				$("[id=" + id + "_urlList.btn.add.root]").addClass("x-item-disabled");
				$("[id=" + id + "_urlList.btn.delete.root]")
						.addClass("x-item-disabled");
				$("#" + id + "_urlList_gRoot input").attr("disabled", true);
			} else {
				$("[id=" + id + ".urlField]").attr("disabled", true);
				$("[id=" + id + ".includeUrl]").attr("disabled", false);
				// $("[id="+id+".urlFieldname]").attr("disabled",false);
		
				// 点击不选中后，表格显示选中文件的按钮和包含文件的字段名
				$("[id=" + id + "_urlList.btn.add]").attr("disabled", false);
				$("[id=" + id + "_urlList.btn.delete]").attr("disabled", false);
				$("[id=" + id + "_urlList.btn.add.root]")
						.removeClass("x-item-disabled");
				$("[id=" + id + "_urlList.btn.delete.root]").removeClass(
						"x-item-disabled");
				$("#" + id + "_urlList_gRoot input").attr("disabled", false);
			}
		},
		includeUrlInOutput : function(e, v) {
			var elId = e.target.id;
			var id = elId.split(".")[0];
			if (e.target.checked) {
				$("[id=" + id + ".urlFieldname]").attr("disabled", false);
			} else {
				$("[id=" + id + ".urlFieldname]").attr("disabled", true);
			}
		},
		includeRownumInOutput : function(e, v) {
			var elId = e.target.id;
			var id = elId.split(".")[0];
			if (e.target.checked) {
				$("[id=" + id + ".rownumFieldname]").attr("disabled", false);
			} else {
				$("[id=" + id + ".rownumFieldname]").attr("disabled", true);
			}
		}
	}
};