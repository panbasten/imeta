jQuery.imeta.steps.combinationlookup = {
	btn : {
		keyWordsAdd : function(c) {
			var rootId = c.getAttribute("rootId");
			var r = [ {
				id : rootId + '.keysId',
				type : 'number',
				text : ''
			}, {
				id : rootId + '.keyField',
				type : 'select',
				text : ''
			}, {
				id : rootId + '.keyLookup',
				type : 'select',
				text : ''
			} ];

			jQuery.imetabar.createRowByHeader(r, rootId);
		},
		getfields : function(e, v) {
			var elId = e.target.id;
			var roType = e.target.getAttribute("roType");
			var roName = e.target.getAttribute("roName");
			var elementName = e.target.getAttribute("elementName");
			var id = elId.split(".")[0];
			var rowNr = $("#" + id + "_keyWords_gList").attr("rowNr");
			$.imessagebox("#ibody", {
				title : "问题",
				marded : true,
				icon : "question",
				type : "custom",
				message : "表中已经有" + rowNr + "行记录，如何处理这" + rowNr + "行数据？",
				btns : [ {
					key : "addNew",
					text : "增加新的",
					btnWidth : 80
				}, {
					key : "addAll",
					text : "增加所有",
					btnWidth : 80
				}, {
					key : "clearAndAddNew",
					text : "清除并增加新的",
					btnWidth : 80
				} ],
				fn : function(m) {
					switch (m) {
					case "addNew":
						$("#" + id).ajaxSubmit( {
							type : "POST",
							url : "ImetaAction!addAndUpdateParameters.action",
							dataType : "json",
							data : {
								roType : roType,
								roName : roName,
								elementName : elementName
							},
							success : function(json) {

							},
							error : globalError
						});
						break;
					case "clearAndAddNew":
						var listId = id + "_keyWords_gList";
						$("#" + listId + " .x-grid-row").remove();
						var index = 0;
						$("#" + listId).attr("rowNr", index);
					case "addAll":
						$.ajax( {
							type : "POST",
							url : "ImetaAction!addAndUpdateParameters.action",
							data : jQuery.cutil.objectToUrl( {
								roType : roType,
								roName : roName,
								elementName : elementName
							}),
							dataType : "json",
							success : function(json) {

								var rootId = id + "_keyWords";

								$.each(json.fields, function(e, v) {
									var r = [ {
										id : rootId + '.keysId',
										type : 'number',
										text : ''
									}, {
										id : rootId + '.keyField',
										type : 'select',
										text : v.fieldName
									}, {
										id : rootId + '.keyLookup',
										type : 'select',
										text : v.fieldName
									} ];

									jQuery.imetabar
											.createRowByHeader(r, rootId);
								});
							},
							error : globalError
						});
						break;
					}
				}

			});
		}
	},
	listeners : {
		useHash : function(e, v) {
			var elId = e.target.id;
			var id = elId.split(".")[0];
			if (e.target.checked) {
				$("[id=" + id + ".hashField]").attr("disabled", false);
			} else {
				$("[id=" + id + ".hashField]").attr("disabled", true);
			}
		}
	}
};