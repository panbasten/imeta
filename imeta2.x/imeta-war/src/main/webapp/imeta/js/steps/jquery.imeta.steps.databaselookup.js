jQuery.imeta.steps.databaselookup = {
	btn : {
		getkeywords : function(e, v) {
			var r = [ {
				id : 'keywordsId',
				type : 'number',
				text : ''
			}, {
				id : 'tableKeyField',
				type : 'input',
				text : 'fieldName'
			}, {
				id : 'keyCondition',
				type : 'select',
				text : ''
			}, {
				id : 'streamKeyField1',
				type : 'input',
				text : 'fieldName'
			}, {
				id : 'streamKeyField2',
				type : 'input',
				text : ''
			} ];
			jQuery.imeta.parameter.getfields(e, v, r, "keywords");
		},
		getqueryvalue : function(e, v) {
			var r = [ {
				id : 'queryvalueId',
				type : 'number',
				text : ''
			}, {
				id : 'returnValueField',
				type : 'input',
				text : 'fieldName'
			}, {
				id : 'returnValueNewName',
				type : 'input',
				text : ''
			}, {
				id : 'returnValueDefault',
				type : 'input',
				text : ''
			}, {
				id : 'returnValueDefaultType',
				type : 'select',
				text : 'type'
			} ];
			jQuery.imeta.parameter.getfields(e, v, r, "queryvalue");
		}
	},
	listeners : {
		cachedListeners : function(e, v) {
			var elId = e.target.id;
			var id = elId.split(".")[0];
			if (e.target.checked) {
				$("[id=" + id + ".cacheSize]").attr("disabled", false);
				$("[id=" + id + ".loadingAllDataInCache]").attr("disabled",
						false);
				$("[id=" + id + ".failingOnMultipleResults]").attr("disabled",
						true);
			} else {
				$("[id=" + id + ".cacheSize]").attr("disabled", true);
				$("[id=" + id + ".loadingAllDataInCache]").attr("disabled",
						true);
				$("[id=" + id + ".failingOnMultipleResults]").attr("disabled",
						false);
			}
		},
		resultsListeners : function(e, v) {
			var elId = e.target.id;
			var id = elId.split(".")[0];
			if (e.target.checked) {
				$("[id=" + id + ".orderByClause]").attr("disabled", true);

			} else {
				$("[id=" + id + ".orderByClause]").attr("disabled", false);
			}
		}
	},
	keywords : {
		btn : {
			fieldAdd : function(c) {
				var rootId = c.getAttribute("rootId");
				var r = [ {
					id : rootId + '.keywordsId',
					type : 'number',
					text : ''
				}, {
					id : rootId + '.tableKeyField',
					type : 'input',
					text : ''
				}, {
					id : rootId + '.keyCondition',
					type : 'select',
					text : ''
				}, {
					id : rootId + '.streamKeyField1',
					type : 'input',
					text : ''
				}, {
					id : rootId + '.streamKeyField2',
					type : 'input',
					text : ''
				} ];
				jQuery.imetabar.createRowByHeader(r, rootId);
			}
		}
	},
	queryvalue : {
		btn : {
			fieldAdd : function(c) {
				var rootId = c.getAttribute("rootId");
				var r = [ {
					id : rootId + '.queryvalueId',
					type : 'number',
					text : ''
				}, {
					id : rootId + '.returnValueField',
					type : 'input',
					text : ''
				}, {
					id : rootId + '.returnValueNewName',
					type : 'input',
					text : ''
				}, {
					id : rootId + '.returnValueDefault',
					type : 'input',
					text : ''
				}, {
					id : rootId + '.returnValueDefaultType',
					type : 'select',
					text : ''
				} ];
				jQuery.imetabar.createRowByHeader(r, rootId);
			}
		}
	}
};