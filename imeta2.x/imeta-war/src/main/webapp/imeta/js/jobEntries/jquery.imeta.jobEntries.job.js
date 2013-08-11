jQuery.imeta.jobEntries.job = {
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
		jobnameBtn : function(e, v) {
			var elId = e.target.id;
			var id = elId.split(".")[0];
			var win = $.iformwindow('#ibody', {
				id : id + "_jobnameBtn",
				title : '选择作业',
				showLoading : true
			});

			$.ajax( {
				type : "POST",
				url : "ImetaDBAction!detectRep.action",
				dataType : "json",
				data : jQuery.cutil.objectToUrl( {
							id : id + "_jobnameBtn",
							parentId : id,
							// 显示的元素类型
							showTypes : "job",
							// 点击确定后调用的方法，有两个参数输入，1 类型，2 ID
							customOkFn : "jQuery.imeta.jobEntries.job.btn.jobnameBtnRtn"
						}),
				success : function(json) {
					win.appendContent(json);
				}
			});
		},
		jobnameBtnRtn : function(t, st) {
			var jobname = t.attr("elName")
			if (jobname && jobname != "") {
				$("[id=" + st + ".jobname]").val(jobname);
				$("[id=" + st + ".directory]").val(t.attr("directoryPath"));
			} else {
				$("[id=" + st + ".jobname]").val("");
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
		}
	}

};