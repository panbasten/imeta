jQuery.imeta.jobEntries.trans = {
	btn : {
		fieldAdd : function(c) {
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
		},
		parameterAdd : function(c) {
			var rootId = c.getAttribute("rootId");
			var r = [ {
				id : rootId + '.parameterId',
				type : 'number',
				text : ''
			}, {
				id : rootId + '.parameterNames',
				type : 'input',
				text : ''
			}, {
				id : rootId + '.valuesFromResult',
				type : 'input',
				text : ''
			}, {
				id : rootId + '.values',
				type : 'input',
				text : ''
			} ];

			jQuery.imetabar.createRowByHeader(r, rootId);
		},
		transnameBtn : function(e, v) {
			var elId = e.target.id;
			var id = elId.split(".")[0];
			var win = $.iformwindow('#ibody', {
				id : id + "_transnameBtn",
				title : '选择转换',
				showLoading : true
			});

			$
					.ajax( {
						type : "POST",
						url : "ImetaAction!detectRep.action",
						dataType : "json",
						data : jQuery.cutil
								.objectToUrl( {
									id : id + "_transnameBtn",
									parentId : id,
									// 显示的元素类型
									showTypes : "trans",
									// 点击确定后调用的方法，有两个参数输入，1 类型，2 ID
									customOkFn : "jQuery.imeta.jobEntries.trans.btn.transnameBtnRtn"
								}),
						success : function(json) {
							win.appendContent(json);
						}
					});
		},
		transnameBtnRtn : function(t, st) {
			var transname = t.attr("elName")
			if (transname && transname != "") {
				$("[id=" + st + ".transname]").val(transname);
				$("[id=" + st + ".directory]").val(t.attr("directoryPath"));
			} else {
				$("[id=" + st + ".transname]").val("");
				$("[id=" + st + ".directory]").val("");
			}
		}
	},
	listeners : {
		setLogfile : function(e, v) {
			var elId = e.target.id;
			var id = elId.split(".")[0];
			if (e.target.checked) {
				$("[id=" + id + ".setAppendLogfile]").attr("disabled", false);
				$("[id=" + id + ".logfile]").attr("disabled", false);
				$("[id=" + id + ".logext]").attr("disabled", false);
				$("[id=" + id + ".addDate]").attr("disabled", false);
				$("[id=" + id + ".addTime]").attr("disabled", false);
				$("[id=" + id + ".loglevel]").attr("disabled", false);
			} else {
				$("[id=" + id + ".setAppendLogfile]").attr("disabled", true);
				$("[id=" + id + ".logfile]").attr("disabled", true);
				$("[id=" + id + ".logext]").attr("disabled", true);
				$("[id=" + id + ".addDate]").attr("disabled", true);
				$("[id=" + id + ".addTime]").attr("disabled", true);
				$("[id=" + id + ".loglevel]").attr("disabled", true);
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