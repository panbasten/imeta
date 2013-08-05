jQuery.imeta.execute = {
	run : {
		btn : {
			parametersAdd : function(c){
				var rootId = c.getAttribute("rootId");
				var r = [
					{ id : rootId+'.parameterId', type : 'number' , text : '' },
					{ id : rootId+'.parameter', type : 'input' , text : '' },
					{ id : rootId+'.value', type : 'input' , text : '' }
				];
				jQuery.imetabar.createRowByHeader(r,rootId);
			},
			variablesAdd : function(c){
				var rootId = c.getAttribute("rootId");
				var r = [
					{ id : rootId+'.parameterId', type : 'number' , text : '' },
					{ id : rootId+'.parameter', type : 'input' , text : '' },
					{ id : rootId+'.value', type : 'input' , text : '' }
				];
				jQuery.imetabar.createRowByHeader(r,rootId);
			},
			runTrans : function(e){
				jQuery.imenu.programLoading(true,"program");
				var elId = e.target.id;
				var id = elId.split(".")[0];
				
				var activeTabId = jQuery.iPortalTab.activeTabId;
				var canvas = jQuery.iPortalTab.getCanvasInfo(activeTabId);
				var tab = jQuery.iPortalTab.I_TAB_POOL[activeTabId];
				$.imessagebox("#ibody",{
					title : "提示",
					type : "ync",
					marded : true,
					message : "是否等待运行完成？",
					fn : function(m){
						// 等待完成
						var wait = true;
						if(m == "yes"){
							wait = true;
						}else if(m == "no"){
							wait = false;
						}else {
							e.stopPropagation();
							return;
						}
						jQuery.imenu.programLoading(true,"program");
						$("#"+id).ajaxSubmit({
							type: "post",
							url:"ImetaAction!start.action",
							dataType:"json",
							data : {
								id : id,
								roType: jQuery.iPortalTab.OBJECT_TYPE_TRANS,
								directoryId : tab.directoryId,
								objectName : canvas.objectName,
								isWait : wait
							},
							success : function(json){
								jQuery.imenu.programLoading(true,null);
								$("#window-"+e.target.getAttribute("rootId")).remove();
								$.imessagebox("#ibody",json);
							}
						});
						
						e.stopPropagation();
					}
				});
				
				e.stopPropagation();
			},
			cancel : function(e){
				$("#window-"+e.target.getAttribute("rootId")).remove();
			},
			runJob : function(e){
				var elId = e.target.id;
				var id = elId.split(".")[0];
				var activeTabId = jQuery.iPortalTab.activeTabId;
				var canvas = jQuery.iPortalTab.getCanvasInfo(activeTabId);
				var tab = jQuery.iPortalTab.I_TAB_POOL[activeTabId];
				jQuery.imenu.programLoading(true,"program");
				$("#"+id).ajaxSubmit({
					type: "post",
					url:"ImetaAction!start.action",
					dataType:"json",
					data : {
						id : id,
						roType: jQuery.iPortalTab.OBJECT_TYPE_JOB,
						directoryId : tab.directoryId,
						objectName : canvas.objectName
					},
					success : function(json){
						jQuery.imenu.programLoading(true,null);
						$("#window-"+e.target.getAttribute("rootId")).remove();
						$.imessagebox("#ibody",json);
					}
				});
				
				e.stopPropagation();
			}
		}
	}
};