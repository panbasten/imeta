jQuery.imeta.steps.btn = {
	ok : function(e, v) {
		var elId = e.target.id;
		var id = elId.split(".")[0];
		$("#" + id).ajaxSubmit(
			{
				type : "POST",
				url : "ImetaAction!editElementSubmit.action",
				dataType : "json",
				data : {
					id : id,
					roType : jQuery.iPortalTab.OBJECT_TYPE_TRANS,
					roName : e.target.getAttribute("transName"),
					directoryId : e.target
							.getAttribute("directoryId"),
					elementName : e.target.getAttribute("stepName")
				},
				success : function(json) {
					if (json.oldName != json.newName) {
						var el = jQuery.imenu.iContent
								.getCanvasElByText(json.oldName,
										'step');
						jQuery.imenu.iContent.updateEl(el, {
							stepName : json.newName,
							bText : [ json.newName ]
						});
						jQuery.imenu.iContent.redraw();
					}
					jQuery.iPortalTab
							.updateActiveTab(jQuery.iPortalTab.TYPE_ACTION_CHANGE_STEP);
					$("#window-" + id).remove();
					$.imessagebox("#ibody", json);
				}
			});
	},
	cancel : function(e, v) {
		var elId = e.target.id;
		var id = elId.split(".")[0];
		$("#window-" + id).remove();
	}
};