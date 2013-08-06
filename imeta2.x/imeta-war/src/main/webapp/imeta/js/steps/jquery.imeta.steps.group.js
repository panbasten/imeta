jQuery.imeta.steps.group = {
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
				text : 'fieldName'
			} ];
			jQuery.imeta.parameter.getfields(e, v, r, 'fields');

		},// end getFields

		aggregationAdd : function(c) {
			var rootId = c.getAttribute("rootId");
			var r = [ {
				id : rootId + '.fieldId',
				type : 'number',
				text : ''
			}, {
				id : rootId + '.aggregateField',
				type : 'input',
				text : ''
			}, {
				id : rootId + '.subjectField',
				type : 'input',
				text : ''
			}, {
				id : rootId + '.aggregateType',
				type : 'select',
				text : ''
			}, {
				id : rootId + '.valueField',
				type : 'input',
				text : ''
			} ];
			jQuery.imetabar.createRowByHeader(r, rootId);
		},

		getAggregationfields : function(e, v) {

			var r = [ {
				id : 'fieldId',
				type : 'number',
				text : ''
			}, {
				id : 'aggregateField',
				type : 'input',
				text : 'fieldName'
			}, {
				id : 'subjectField',
				type : 'input',
				text : 'fieldName'
			}, {
				id : 'aggregateType',
				type : 'select',
				text : ''
			}, {
				id : 'valueField',
				type : 'input',
				text : ''
			} ];
			jQuery.imeta.parameter.getfields(e, v, r, 'aggregation');

		},// end getFields
		analyticFieldAdd : function(c) {
			var rootId = c.getAttribute("rootId");
			var r = [ {
				id : rootId + '.fieldId',
				type : 'number',
				text : ''
			}, {
				id : rootId + '.groupField',
				type : 'input',
				text : ''
			} ];
			jQuery.imetabar.createRowByHeader(r, rootId);
		},
		getAnalyticFields : function(e, v) {

			var r = [ {
				id : 'fieldId',
				type : 'number',
				text : ''
			}, {
				id : 'groupField',
				type : 'input',
				text : 'fieldName'
			} ];
			jQuery.imeta.parameter.getfields(e, v, r, 'group');

		},// end getFields
		analyticFunctionsAdd : function(c) {
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
				id : rootId + '.subject',
				type : 'select',
				text : ''
			}, {
				id : rootId + '.type',
				type : 'select',
				text : ''
			}, {
				id : rootId + '.value',
				type : 'select',
				text : ''
			} ];
			jQuery.imetabar.createRowByHeader(r, rootId);
		},
		getAnalyticFunctions : function(e, v) {

			var r = [ {
				id : 'fieldId',
				type : 'number',
				text : ''
			}, {
				id : 'name',
				type : 'input',
				text : 'fieldName'
			}, {
				id : 'subject',
				type : 'select',
				text : 'fieldName'
			}, {
				id : 'type',
				type : 'select',
				text : ''
			}, {
				id : 'value',
				type : 'input',
				text : ''
			} ];
			jQuery.imeta.parameter.getfields(e, v, r, 'analyticFunctions');

		},// end getFields
		statsAdd : function(c) {
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
				id : rootId + '.n',
				type : 'select',
				text : ''
			}, {
				id : rootId + '.mean',
				type : 'select',
				text : ''
			}, {
				id : rootId + '.stdDev',
				type : 'select',
				text : ''
			}, {
				id : rootId + '.min',
				type : 'select',
				text : ''
			}, {
				id : rootId + '.max',
				type : 'select',
				text : ''
			}, {
				id : rootId + '.median',
				type : 'select',
				text : ''
			}, {
				id : rootId + '.arbitraryPercentile',
				type : 'input',
				text : ''
			}, {
				id : rootId + '.interpolatePercentile',
				type : 'select',
				text : ''
			} ];
			jQuery.imetabar.createRowByHeader(r, rootId);
		}
	},
	listeners : {
		isPassAllRowsClick : function(e, v) {
			var elId = e.target.id;
			var id = elId.split(".")[0];
			if (e.target.checked) {
				$("[id=" + id + ".directory]").attr("disabled", false);
				$("[id=" + id + ".prefix]").attr("disabled", false);
				$("[id=" + id + ".addingLineNrInGroup]")
						.attr("disabled", false);
			} else {
				$("[id=" + id + ".directory]").attr("disabled", true);
				$("[id=" + id + ".prefix]").attr("disabled", true);
				$("[id=" + id + ".addingLineNrInGroup]").attr("disabled", true);
			}
		},
		isAddingLineNrInGroupClick : function(e, v) {
			var elId = e.target.id;
			var id = elId.split(".")[0];
			if (e.target.checked) {
				$("[id=" + id + ".lineNrInGroupField]").attr("disabled", false);
			} else {
				$("[id=" + id + ".lineNrInGroupField]").attr("disabled", true);
			}
		}
	}
};