jQuery.imeta.steps.tableoutput = {
	btn : {
		fieldAdd : function(c) {
			var rootId = c.getAttribute("rootId");
			var r = [ {
				id : rootId + '.fieldId',
				type : 'number',
				text : ''
			}, {
				id : rootId + '.fieldDatabase',
				type : 'input',
				text : ''
			}, {
				id : rootId + '.fieldStream',
				type : 'input',
				text : ''
			}

			];

			jQuery.imetabar.createRowByHeader(r, rootId);
		}
	},
	listeners : {
		specifyFieldsListener : function(e, v) {
			var elId = e.target.id;
			var id = elId.split(".")[0];
			if (e.target.checked) {
				$("[id=" + id + ".tableNameInTable]").attr("disabled", true);

			} else {
				$("[id=" + id + ".tableNameInTable]").attr("disabled", false);
			}
		},
		tableZoningListener : function(e, v) {
			var elId = e.target.id;
			var id = elId.split(".")[0];
			if (e.target.checked) {
				$("[id=" + id + ".truncateTable]").attr("disabled", true);
				$("[id=" + id + ".partitioningField]").attr("disabled", false);
				$("[id=" + id + ".partitioningMonthly]")
						.attr("disabled", false);
				$("[id=" + id + ".partitioningDaily]").attr("disabled", false);
			} else {
				$("[id=" + id + ".truncateTable]").attr("disabled", false);
				$("[id=" + id + ".partitioningField]").attr("disabled", true);
				$("[id=" + id + ".partitioningMonthly]").attr("disabled", true);
				$("[id=" + id + ".partitioningDaily]").attr("disabled", true);
			}
		},
		bulkInsertListener : function(e, v) {
			var elId = e.target.id;
			var id = elId.split(".")[0];
			if (e.target.checked) {
				$("[id=" + id + ".ignoreErrors]").attr("disabled", true);

			} else {
				$("[id=" + id + ".ignoreErrors]").attr("disabled", false);

			}
		},
		inOneFieldListener : function(e, v) {
			var elId = e.target.id;
			var id = elId.split(".")[0];
			if (e.target.checked) {
				$("[id=" + id + ".tablename]").attr("disabled", true);
				$("[id=" + id + ".truncateTable]").attr("disabled", true);
				$("[id=" + id + ".tableNameField]").attr("disabled", false);

			} else {
				$("[id=" + id + ".tablename]").attr("disabled", false);
				$("[id=" + id + ".truncateTable]").attr("disabled", false);
				$("[id=" + id + ".tableNameField]").attr("disabled", true);

			}
		},
		returnKeyListener : function(e, v) {
			var elId = e.target.id;
			var id = elId.split(".")[0];
			if (e.target.checked) {
				$("[id=" + id + ".useBatchUpdate]").attr("disabled", true);
				$("[id=" + id + ".generatedKeyField]").attr("disabled", false);

			} else {
				$("[id=" + id + ".useBatchUpdate]").attr("disabled", false);
				$("[id=" + id + ".generatedKeyField]").attr("disabled", true);

			}
		}

	}
};