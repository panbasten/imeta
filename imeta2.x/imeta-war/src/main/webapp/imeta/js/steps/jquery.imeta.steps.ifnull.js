jQuery.imeta.steps.ifnull = {
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
				id : 'replaceValue',
				type : 'input',
				text : ''
			}, {
				id : 'replaceMask',
				type : 'input',
				text : ''
			} ];
			jQuery.imeta.parameter.getfields(e, v, r);
		},
		ifnullTypeAdd : function(c) {
			var rootId = c.getAttribute("rootId");
			var r = [ {
				id : rootId + '.typeId',
				type : 'number',
				text : ''
			}, {
				id : rootId + '.typeName',
				type : 'input',
				text : ''
			}, {
				id : rootId + '.typereplaceValue',
				type : 'input',
				text : ''
			}, {
				id : rootId + '.typereplaceMask',
				type : 'input',
				text : ''
			} ];

			jQuery.imetabar.createRowByHeader(r, rootId);
		},
		ifnullFieldsAdd : function(c) {
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
				id : rootId + '.replaceValue',
				type : 'input',
				text : ''
			}, {
				id : rootId + '.replaceMask',
				type : 'input',
				text : ''
			} ];

			jQuery.imetabar.createRowByHeader(r, rootId);
		}
	},
	listeners : {
		selectFields : function(e, v) {
			var elId = e.target.id;
			var id = elId.split(".")[0];
			if (e.target.checked) {

				$("[id=" + id + "_fields.btn.add]").attr("disabled", false);
				$("[id=" + id + "_fields.btn.delete]").attr("disabled", false);
				$("[id=" + id + "_fields.btn.add.root]").removeClass(
						"x-item-disabled");
				$("[id=" + id + "_fields.btn.delete.root]").removeClass(
						"x-item-disabled");
				$("#" + id + "_fields_gRoot input").attr("disabled", false);

				$("[id=" + id + "_types.btn.add]").attr("disabled", true);
				$("[id=" + id + "_types.btn.delete]").attr("disabled", true);
				$("[id=" + id + "_types.btn.add.root]").addClass(
						"x-item-disabled");
				$("[id=" + id + "_types.btn.delete.root]").addClass(
						"x-item-disabled");
				$("#" + id + "_types_gRoot input").attr("disabled", true);

				$("[id=" + id + ".replaceAllByValue]").attr("disabled", true);
				$("[id=" + id + ".replaceAllMask]").attr("disabled", true);
				$("[id=" + id + ".getfields]").attr("disabled", false);
				$("[id=" + id + ".selectValuesType]").attr("checked", false);

			} else {

				$("[id=" + id + "_fields.btn.add]").attr("disabled", true);
				$("[id=" + id + "_fields.btn.delete]").attr("disabled", true);
				$("[id=" + id + "_fields.btn.add.root]").addClass(
						"x-item-disabled");
				$("[id=" + id + "_fields.btn.delete.root]").addClass(
						"x-item-disabled");
				$("#" + id + "_fields_gRoot input").attr("disabled", true);

				$("[id=" + id + "_types.btn.add]").attr("disabled", false);
				$("[id=" + id + "_types.btn.delete]").attr("disabled", false);
				$("[id=" + id + "_types.btn.add.root]").addClass(
						"x-item-disabled");
				$("[id=" + id + "_types.btn.delete.root]").addClass(
						"x-item-disabled");
				$("#" + id + "_types_gRoot input").attr("disabled", false);

				$("[id=" + id + ".replaceAllByValue]").attr("disabled", false);
				$("[id=" + id + ".replaceAllMask]").attr("disabled", false);
				$("[id=" + id + ".getfields]").attr("disabled", true);

			}
		},
		selectValuesType : function(e, v) {
			var elId = e.target.id;
			var id = elId.split(".")[0];
			if (e.target.checked) {

				$("[id=" + id + "_fields.btn.add]").attr("disabled", true);
				$("[id=" + id + "_fields.btn.delete]").attr("disabled", true);
				$("[id=" + id + "_fields.btn.add.root]").addClass(
						"x-item-disabled");
				$("[id=" + id + "_fields.btn.delete.root]").addClass(
						"x-item-disabled");
				$("#" + id + "_fields_gRoot input").attr("disabled", true);

				$("[id=" + id + "_types.btn.add]").attr("disabled", false);
				$("[id=" + id + "_types.btn.delete]").attr("disabled", false);
				$("[id=" + id + "_types.btn.add.root]").removeClass(
						"x-item-disabled");
				$("[id=" + id + "_types.btn.delete.root]").removeClass(
						"x-item-disabled");
				$("#" + id + "_types_gRoot input").attr("disabled", false);
				$("[id=" + id + ".replaceAllByValue]").attr("disabled", true);
				$("[id=" + id + ".replaceAllMask]").attr("disabled", true);
				$("[id=" + id + ".selectFields]").attr("checked", false);

				$("[id=" + id + ".getfields]").attr("disabled", true);
			} else {

				$("[id=" + id + "_fields.btn.add]").attr("disabled", false);
				$("[id=" + id + "_fields.btn.delete]").attr("disabled", false);
				$("[id=" + id + "_fields.btn.add.root]").addClass(
						"x-item-disabled");
				$("[id=" + id + "_fields.btn.delete.root]").addClass(
						"x-item-disabled");
				$("#" + id + "_fields_gRoot input").attr("disabled", false);

				$("[id=" + id + "_types.btn.add]").attr("disabled", true);
				$("[id=" + id + "_types.btn.delete]").attr("disabled", true);
				$("[id=" + id + "_types.btn.add.root]").addClass(
						"x-item-disabled");
				$("[id=" + id + "_types.btn.delete.root]").addClass(
						"x-item-disabled");
				$("#" + id + "_types_gRoot input").attr("disabled", true);

				$("[id=" + id + ".replaceAllByValue]").attr("disabled", false);
				$("[id=" + id + ".replaceAllMask]").attr("disabled", false);

				$("[id=" + id + ".getfields]").attr("disabled", true);

			}
		}
	}
};