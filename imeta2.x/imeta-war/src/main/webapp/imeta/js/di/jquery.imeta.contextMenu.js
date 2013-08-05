jQuery.imeta.contextMenu = {
	edit : function(el,mc){
		var activeTab = jQuery.iPortalTab.getActiveTab();
		if(activeTab!=null){
            if($.iformwindow.activeWindow(el.id)){
				return;
			}
			
			var win = $.iformwindow('#ibody',{
                id: el.id,
                title: el.stepName,
                showLoading : true
            });
            
			$.ajax({
                type: "POST",
                url: "ImetaAction!editElement.action",
                data: jQuery.cutil.objectToUrl({
                	id : el.id,
                	roName : activeTab.objectName,
                	roType : activeTab.objectType,
                	directoryId : activeTab.directoryId,
                	stepName : el.stepName,
                	stepType : el.stepType
                }),
                dataType: "json",
                success : function(json){
	                win.appendContent(json);
                }
            });
		}
	},
	cut : function(el,mc){
		alert(0);
	},
	copy : function(el,mc){
		alert(0);
	},
	paste : function(el,mc){
		alert(0);
	},
	delete_el : function(el,mc){
		jQuery.imenu.iContent.disabledEl(el);
		jQuery.imenu.iContent.deleteHopWithEl(el);
		jQuery.imenu.iContent.cleanSelectedEl();
		jQuery.imenu.iContent.redraw();
		var activeTab = jQuery.iPortalTab.getActiveTab();
		if(activeTab.objectType == jQuery.iPortalTab.OBJECT_TYPE_TRANS){
			jQuery.iPortalTab.updateActiveTab(jQuery.iPortalTab.TYPE_ACTION_DELETE_STEP);
		}else if(activeTab.objectType == jQuery.iPortalTab.OBJECT_TYPE_JOB){
			jQuery.iPortalTab.updateActiveTab(jQuery.iPortalTab.TYPE_ACTION_DELETE_JOB_ENTRY);
		}
	},
	data_distribute : function(el,mc){
	    // 通知后台
	    var activeTab = jQuery.iPortalTab.getActiveTab();
	    var canvas = jQuery.iPortalTab.getCanvasInfo(jQuery.iPortalTab.activeTabId);
	    var canvasStr = $.toJSON(canvas);
	    $.ajax({
	    	 type: "POST",
	    	 url: "ImetaAction!modifyElement.action",
			 dataType:"json",
			 data: jQuery.cutil.objectToUrl({
			 	roName : activeTab.objectName,
	        	roType : activeTab.objectType,
	        	directoryId : activeTab.directoryId,
	        	elId : el.id,
	        	modifyType : "dataMoveType",
	        	modifyValue : "distribute",
	        	canvas : canvasStr
			 }),
			 success : function(json){
			    // 刷新前台
	            jQuery.iPortalTab.updateActiveTabBy(json,true);
			 },
			 error : globalError
	    });
	},
	data_copy : function(el,mc){
	    // 通知后台
	    var activeTab = jQuery.iPortalTab.getActiveTab();
	    var canvas = jQuery.iPortalTab.getCanvasInfo(jQuery.iPortalTab.activeTabId);
	    var canvasStr = $.toJSON(canvas);
	    $.ajax({
	    	 type: "POST",
	    	 url: "ImetaAction!modifyElement.action",
			 dataType:"json",
			 data: jQuery.cutil.objectToUrl({
			 	roName : activeTab.objectName,
	        	roType : activeTab.objectType,
	        	directoryId : activeTab.directoryId,
	        	elId : el.id,
	        	modifyType : "dataMoveType",
	        	modifyValue : "copy",
	        	canvas : canvasStr
			 }),
			 success : function(json){
			    // 刷新前台
	            jQuery.iPortalTab.updateActiveTabBy(json,true);
			 },
			 error : globalError
	    });
	},
	data_copies : function(el,mc){
	    // 通知后台
	    $.imessagebox("#ibody",{
			title : "输入",
			type : "prompt",
			marded : true,
			defaultValue : el.copies,
			message : "复制的数量（1或更多）",
			fn : function(m,n){
			    if(m == 'ok'){
				    var activeTab = jQuery.iPortalTab.getActiveTab();
        		    var canvas = jQuery.iPortalTab.getCanvasInfo(jQuery.iPortalTab.activeTabId);
		            var canvasStr = $.toJSON(canvas);
        		    $.ajax({
        		    	 type: "POST",
        		    	 url: "ImetaAction!modifyElement.action",
        				 dataType:"json",
        				 data: jQuery.cutil.objectToUrl({
        				 	roName : activeTab.objectName,
        		        	roType : activeTab.objectType,
        		        	directoryId : activeTab.directoryId,
        		        	elId : el.id,
        		        	modifyType : "dataCopies",
        		        	modifyValue : n,
        		        	canvas : canvasStr
        				 }),
        				 success : function(json){
        				    // 刷新前台
        		            jQuery.iPortalTab.updateActiveTabBy(json,true);
        				 },
        				 error : globalError
        		    });
			    }
			}
		});
	},
	exchange : function(el,mc){
		var fromEl = el.fromEl;
		var fromElType = el.fromElType;
		var fromElWidth = el.fromElWidth;
		var fromElHeight = el.fromElHeight;
		var nx = [],ny = [];
		for(var i=el.x.length-1;i>=0;i--){
			nx.push(el.x[i]);
			ny.push(el.y[i]);
		}
		jQuery.imenu.iContent.updateEl(el,{
			fromEl : el.toEl,
			fromElType : el.toElType,
			fromElWidth : el.toElWidth,
			fromElHeight : el.toElHeight,
			toEl : fromEl,
			toElType : fromElType,
			toElWidth : fromElWidth,
			toElHeight : fromElHeight,
			x : nx,
			y : ny
		});
		jQuery.imenu.iContent.redraw();
		var activeTab = jQuery.iPortalTab.getActiveTab();
		if(activeTab.objectType == jQuery.iPortalTab.OBJECT_TYPE_TRANS){
			jQuery.iPortalTab.updateActiveTab(jQuery.iPortalTab.TYPE_ACTION_CHANGE_HOP);
		}else if(activeTab.objectType == jQuery.iPortalTab.OBJECT_TYPE_JOB){
			jQuery.iPortalTab.updateActiveTab(jQuery.iPortalTab.TYPE_ACTION_CHANGE_JOB_HOP);
		}
	},
	evaluation_unconditional : function(el,mc){
		jQuery.imenu.iContent.updateEl(el,{
			customType : "unconditional",
			style : "blue"
		});
		jQuery.imenu.iContent.redraw();
		jQuery.iPortalTab.updateActiveTab(jQuery.iPortalTab.TYPE_ACTION_CHANGE_JOB_HOP);
	},
	evaluation_success : function(el,mc){
		jQuery.imenu.iContent.updateEl(el,{
			customType : "success",
			style : "green"
		});
		jQuery.imenu.iContent.redraw();
		jQuery.iPortalTab.updateActiveTab(jQuery.iPortalTab.TYPE_ACTION_CHANGE_JOB_HOP);
	},
	evaluation_failure : function(el,mc){
		jQuery.imenu.iContent.updateEl(el,{
			customType : "failure",
			style : "red"
		});
		jQuery.imenu.iContent.redraw();
		jQuery.iPortalTab.updateActiveTab(jQuery.iPortalTab.TYPE_ACTION_CHANGE_JOB_HOP);
	},
	delete_hop : function(el,mc){
		jQuery.imenu.iContent.disabledEl(el);
		jQuery.imenu.iContent.cleanSelectedEl();
		jQuery.imenu.iContent.redraw();
		var activeTab = jQuery.iPortalTab.getActiveTab();
		if(activeTab.objectType == jQuery.iPortalTab.OBJECT_TYPE_TRANS){
			jQuery.iPortalTab.updateActiveTab(jQuery.iPortalTab.TYPE_ACTION_DELETE_HOP);
		}else if(activeTab.objectType == jQuery.iPortalTab.OBJECT_TYPE_JOB){
			jQuery.iPortalTab.updateActiveTab(jQuery.iPortalTab.TYPE_ACTION_DELETE_JOB_HOP);
		}
	}
};