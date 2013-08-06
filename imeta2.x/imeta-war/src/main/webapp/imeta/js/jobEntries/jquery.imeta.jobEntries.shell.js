jQuery.imeta.jobEntries.shell = {
	btn : {
		wordsAdd : function(c) {
			var rootId = c.getAttribute("rootId");

			var r = [ {
				id : rootId + '.fieldId',
				type : 'number',
				text : ''
			}, {
				id : rootId + '.arguments',
				type : 'input',
				text : ''
			} ];

			jQuery.imetabar.createRowByHeader(r, rootId);

		}
	},
	listeners : {
		designatedFile : function(e, v) {
			var elId = e.target.id;
			var id = elId.split(".")[0];
			if (e.target.checked) {
				$("[id=" + id + ".addFile]").attr("disabled", true);
				$("[id=" + id + ".fileName]").attr("disabled", true);
				$("[id=" + id + ".extension]").attr("disabled", true);
				$("[id=" + id + ".containsData]").attr("disabled", true);
				$("[id=" + id + ".containsTime]").attr("disabled", true);
				$("[id=" + id + ".logLevel]").attr("disabled", true);

			} else {
				$("[id=" + id + ".addFile]").attr("disabled", false);
				$("[id=" + id + ".fileName]").attr("disabled", false);
				$("[id=" + id + ".extension]").attr("disabled", false);
				$("[id=" + id + ".containsData]").attr("disabled", false);
				$("[id=" + id + ".containsTime]").attr("disabled", false);
				$("[id=" + id + ".logLevel]").attr("disabled", false);

			}
		},
		clusterMode : function(e, v) {
			var elId = e.target.id;
			var id = elId.split(".")[0];
			if (e.target.checked) {
				$("[id=" + id + ".slaveServer]").attr("disabled", true);
				$("[id=" + id + ".waitingToFinish]").attr("disabled", true);
				$("[id=" + id + ".followingAbortRemotely]").attr("disabled",
						true);
			} else {
				$("[id=" + id + ".slaveServer]").attr("disabled", false);
				$("[id=" + id + ".waitingToFinish]").attr("disabled", false);
				$("[id=" + id + ".followingAbortRemotely]").attr("disabled",
						false);
			}
		}
	}
};