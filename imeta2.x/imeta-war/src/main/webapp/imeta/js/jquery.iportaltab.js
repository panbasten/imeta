jQuery.iPortalTab = {
	
	TYPE_ACTION_NONE : 0,
	TYPE_ACTION_CHANGE_STEP : 1,
	TYPE_ACTION_CHANGE_CONNECTION : 2,
	TYPE_ACTION_CHANGE_HOP : 3,
	TYPE_ACTION_CHANGE_NOTE : 4,
	TYPE_ACTION_NEW_STEP : 5,
	TYPE_ACTION_NEW_CONNECTION : 6,
	TYPE_ACTION_NEW_HOP : 7,
	TYPE_ACTION_NEW_NOTE : 8,
	TYPE_ACTION_DELETE_STEP : 9,
	TYPE_ACTION_DELETE_CONNECTION : 10,
	TYPE_ACTION_DELETE_HOP : 11,
	TYPE_ACTION_DELETE_NOTE : 12,
	TYPE_ACTION_POSITION_STEP : 13,
	TYPE_ACTION_POSITION_NOTE : 14,
	
	TYPE_ACTION_CHANGE_JOB_ENTRY : 15,
	TYPE_ACTION_CHANGE_JOB_HOP : 16,
	TYPE_ACTION_NEW_JOB_ENTRY : 17,
	TYPE_ACTION_NEW_JOB_HOP : 18,
	TYPE_ACTION_DELETE_JOB_ENTRY : 19,
	TYPE_ACTION_DELETE_JOB_HOP : 20,
	TYPE_ACTION_POSITION_JOB_ENTRY : 21,
	
	TYPE_ACTION_CHANGE_TABLEITEM : 22,
	TYPE_ACTION_NEW_TABLEITEM : 23,
	TYPE_ACTION_DELETE_TABLEITEM : 24,
	TYPE_ACTION_POSITION_TABLEITEM : 25,
	
	TYPE_ACTION_CHANGE_TABLE : 26,
	TYPE_ACTION_CHANGE_RELATIONSHIP : 27,
	TYPE_ACTION_NEW_TABLE : 28,
	TYPE_ACTION_NEW_RELATIONSHIP : 29,
	TYPE_ACTION_DELETE_TABLE : 30,
	TYPE_ACTION_DELETE_RELATIONSHIP : 31,
	TYPE_ACTION_POSITION_TABLE : 32,
	
	TYPE_ACTION_NEW_SLAVE : 33,
	TYPE_ACTION_CHANGE_SLAVE : 34,
	TYPE_ACTION_DELETE_SLAVE : 35,
	
	TYPE_ACTION_NEW_CLUSTER : 36,
	TYPE_ACTION_CHANGE_CLUSTER : 37,
	TYPE_ACTION_DELETE_CLUSTER : 38,
	
	TYPE_ACTION_NEW_PARTITION : 39,
	TYPE_ACTION_CHANGE_PARTITION : 40,
	TYPE_ACTION_DELETE_PARTITION : 41,
	
	TYPE_ACTION_HUNDRED_PERCENT : 42,
	TYPE_ACTION_FIX_SCALE : 43,
	TYPE_ACTION_PART_SCALE : 44,
	TYPE_ACTION_MAGNIFY_SCALE : 45,
	TYPE_ACTION_LESSEN_SCALE : 46,
	TYPE_ACTION_SCREEN_MOVE : 47,
	
	I_PORTAL_TAB_ID_PRE : 'portal_tab_',
	I_PORTAL_TAB : 'portal_tab',
	I_PORTAL_TAB_SCROLLER_RIGHT : 'portalTabScrollerRight',
	I_PORTAL_TAB_SCROLLER_LEFT : 'portalTabScrollerLeft',
	I_TAB_EDGE : 'portalTabEdge',
	I_TAB_POOL : {},
	I_TAB_POOL_MAX :20,
	
	OBJECT_TYPE_TRANS : 'Transformation',
	OBJECT_TYPE_JOB : 'Job',
	OBJECT_TYPE_DOMAIN : 'Domain',
	
	OBJECT_TYPES : ['Transformation','Job','Domain'],
	
	OBJECT_TYPE_CLASS : ['toolbar-null','toolbar-trans','toolbar-job','toolbar-domain'],
	
	
	activeTabId : null,
	
	newTabIndex : 0,
	
	hasCreateTab : function(id){
		var tabId = this.I_PORTAL_TAB_ID_PRE+id;
		// 是否已存在
		var theTab = this.I_TAB_POOL[tabId];
		if(theTab&&theTab!=null){
			return true;
		}
		return false;
	},
	
	createTab : function(c){
		if(c&&c.id){
			var tabId = this.I_PORTAL_TAB_ID_PRE+c.id;
			if(this.hasCreateTab(c.id)){
				this.activeTab(tabId,c.objectType);
			}else{
				var newTab = this.createTabBody(c);
				if(this.activeTabId==null){
					newTab.insertBefore("#"+this.I_TAB_EDGE);
				}else{
					newTab.insertAfter("#"+this.activeTabId);
				}

				// 加入tab池
				var tab = {
					oriId : c.id,
					tab: newTab,
					objectType:c.objectType,
					objectName:c.objectName,
					directoryId:c.directoryId,
					directoryName:c.directoryName,
					directoryPath:c.directoryPath,
					isNew:(c.isNew)?c.isNew:false,
					isUpdate: false,
					ori : c.ori,
					editable: c.editable,
					newNum : 0,
					track : [c.ori],
					trackHistoryIndex : 0,
					trackOriIndex : 0
				};
				eval("$.extend(this.I_TAB_POOL,{"+tabId+":tab});");
				
				// 选中tab
				this.activeTab(tabId,c.objectType);
				
				this.modifyTabText(tab.isNew);
				
				//增加鼠标事件
				newTab.mouseover(function(e){
					newTab.addClass("x-tab-strip-over");
				});
				newTab.mouseout(function(e){
					newTab.removeClass("x-tab-strip-over");
				});
				newTab.click(function(e){
					jQuery.iPortalTab.activeTab(tabId,c.objectType);
				});
			}
		}
	},
	
	createTabBody : function(c){
		var tabId = this. I_PORTAL_TAB_ID_PRE+c.id;
		var b = $("<li class='x-tab-strip-closable x-tab-with-icon'></li>");
		b.attr("id",tabId);
		this.I_PORTAL_TAB_ID_INDEX++;
		var b_close = $("<a class='x-tab-strip-close' onclick='javascript:jQuery.iPortalTab.closeTab(\""+tabId+"\");'></a>").appendTo(b);
		var b_content = $("<a class='x-tab-right' href='#' title='"+c.title+"'></a>").appendTo(b);
		var b_content_em = $("<em class='x-tab-left'></em>").appendTo(b_content);
		var b_content_em_span = $("<span class='x-tab-strip-inner'></span>").appendTo(b_content_em);
		var b_content_em_span_span = $("<span class='x-tab-strip-text "+((c.objectType)?c.objectType:"tabs")+"'></span>").appendTo(b_content_em_span);
		b_content_em_span_span.html("<font id='"+tabId+"-modified' style='color:blue;'></font>"+c.title);
		
		return b;
	},
	
	/**
	 * 得到当前canvas
	 */
	getCanvas : function(id){
		var tab = this.I_TAB_POOL[id];
		return tab.track[tab.trackHistoryIndex];
	},
	
	getCanvasInfo : function(id){
		var tab = this.I_TAB_POOL[id];
		if(tab!=undefined){
			return {
				objectType:tab.objectType,
				objectName:tab.objectName,
				isNew:tab.isNew,
				isUpdate:tab.isUpdate,
				canvas:this.getCanvas(id)
			};
		}
		return null;
	},
	
	isUpdateById : function(id){
		var tab = this.I_TAB_POOL[id];
		if(tab!=undefined){
			return tab.isUpdate;
		}
		return false;
	},
	
	undoResentCanvas : function(){
		if(this.activeTabId!=null){
			var tab = this.I_TAB_POOL[this.activeTabId];
			var trackHistoryIndex = tab.trackHistoryIndex-1;
			if(trackHistoryIndex>=0){
				jQuery.imenu.iContent.setCanvasEls(tab.track[trackHistoryIndex]);
				tab.trackHistoryIndex=trackHistoryIndex;
				this.modifyTabText(trackHistoryIndex!=tab.trackOriIndex || tab.isNew);
			}
			// 重载页面
			jQuery.imenu.iContent.redraw();
			
			//前进按钮true，后退按钮判断
			var canUndo = false;
			if(trackHistoryIndex>0){
				canUndo = true;
			}
			jQuery.imenu.batchControlMenus({
				undo : canUndo,
				redo : true
			});
		}
	},
	
	redoResentCanvas : function(){
		if(this.activeTabId!=null){
			var tab = this.I_TAB_POOL[this.activeTabId];
			var trackHistoryIndex = tab.trackHistoryIndex+1;
			var trackNum = tab.track.length;
			if(trackNum>0){
				if(trackHistoryIndex<=trackNum-1){
					jQuery.imenu.iContent.setCanvasEls(tab.track[trackHistoryIndex]);
					tab.trackHistoryIndex=trackHistoryIndex;
					// 重载页面
					jQuery.imenu.iContent.redraw();
					this.modifyTabText(trackHistoryIndex!=tab.trackOriIndex || tab.isNew);
				}
			}
			
			//前进按钮true，后退按钮判断
			var canRedo = false;
			if(trackHistoryIndex+1<=trackNum-1){
				canRedo = true;
			}
			jQuery.imenu.batchControlMenus({
				undo : true,
				redo : canRedo
			});
		}
	},
	
	/**
	 * 激活tab
	 */
	activeTab : function(id, type){
		// 如果id==activeTabId，不做操作
		if(id==this.activeTabId){
			return;
		}
		// 取消选定
		if(this.activeTabId!=null){
			$("#"+this.activeTabId).removeClass("x-tab-strip-active");
		}
		// 选定对象
		$("#"+ id).addClass("x-tab-strip-active");
		
		// 重载页面
		jQuery.imenu.iContent.setCanvasEls(this.getCanvas(id));
		
		// 重置工具箱和菜单
		if(type == jQuery.iPortalTab.OBJECT_TYPE_TRANS){
			jQuery.iPortalTab.dealToolbarClass("toolbar-trans");
			$("#transTools").removeClass("x-element-not-display");
			$("#jobTools").addClass("x-element-not-display");
			$("#domainTools").addClass("x-element-not-display");
		}else if(type == jQuery.iPortalTab.OBJECT_TYPE_JOB){
			jQuery.iPortalTab.dealToolbarClass("toolbar-job");
			$("#transTools").addClass("x-element-not-display");
			$("#jobTools").removeClass("x-element-not-display");
			$("#domainTools").addClass("x-element-not-display");
		}else if(type == jQuery.iPortalTab.OBJECT_TYPE_DOMAIN){
			jQuery.iPortalTab.dealToolbarClass("toolbar-domain");
			$("#transTools").addClass("x-element-not-display");
			$("#jobTools").addClass("x-element-not-display");
			$("#domainTools").removeClass("x-element-not-display");
		}
		
		 // 设置可编辑状态
		 var theTab = this.I_TAB_POOL[id];
		 jQuery.imenu.selectedMenu('editObject',theTab.editable);
		
		// 设置活动tab
		this.activeTabId=id;
	},
	
	/**
	 * 保存活动标签
	 */
	saveActiveTab : function(c){
		var tab = this.I_TAB_POOL[this.activeTabId];
		tab.trackOriIndex=tab.trackHistoryIndex;
		this.modifyTabText(false);
		tab.track[tab.trackHistoryIndex]=c;
		jQuery.imenu.iContent.setCanvasEls(tab.track[tab.trackHistoryIndex]);
		jQuery.imenu.iContent.redraw();
	},
	
	updateActiveTabBy :  function(c,modify){
		var tab = this.I_TAB_POOL[this.activeTabId];
		tab.trackOriIndex=tab.trackHistoryIndex;
		tab.track[tab.trackHistoryIndex]=c;
		jQuery.imenu.iContent.setCanvasEls(tab.track[tab.trackHistoryIndex]);
		jQuery.imenu.iContent.redraw();
		if(modify && modify!=null){
		   this.modifyTabText(modify);
		}
	},
	
	/**
	 * 更新活动标签
	 */
	updateActiveTab : function(t){
		var tab = this.I_TAB_POOL[this.activeTabId];
		
		// 如果是添加元素，修改newNum
		if(t == jQuery.iPortalTab.TYPE_ACTION_NEW_STEP || t == jQuery.iPortalTab.TYPE_ACTION_NEW_JOB_ENTRY){
		    tab.newNum = (tab.newNum +1);
		}
		
		// 如果当前历史指针记录不为最大位置，出栈后面的历史记录
		if(tab.trackHistoryIndex>-1){
			if(tab.trackHistoryIndex<tab.track.length-1){
				var popNum = tab.track.length-tab.trackHistoryIndex-1;
				tab.track.splice(tab.trackHistoryIndex+1,popNum);
			}
		}else{
			tab.track.length=0;
		}
		tab.track.push(jQuery.imenu.iContent.cloneCanvasEls());
		// 前面的历史记录溢出
		if(tab.track.length>this.I_TAB_POOL_MAX){
			tab.track.shift();
			if(tab.trackOriIndex>-1){
				tab.trackOriIndex=tab.trackOriIndex-1;
			}
		}
		// 当前历史记录指针
		tab.trackHistoryIndex=tab.track.length-1;
		
		this.modifyTabText(true);
		
		// 后退按钮true，前进按钮false
		jQuery.imenu.batchControlMenus({
			undo : true,
			redo : false
		});
		
		
		// 更新服务器
		var canvas = this.getCanvasInfo(this.activeTabId);
		var canvasStr = $.toJSON(canvas);
		// 删除对象
		if(t == this.TYPE_ACTION_DELETE_STEP
			|| t == this.TYPE_ACTION_DELETE_HOP
			|| t == this.TYPE_ACTION_DELETE_JOB_ENTRY
			|| t == this.TYPE_ACTION_DELETE_JOB_HOP){
			$.ajax({
		    	 type: "POST",
		    	 url: "ImetaAction!deleteElement.action",
				 dataType:"json",
				 data: jQuery.cutil.objectToUrl({
				 	roName : tab.objectName,
		        	roType : tab.objectType,
		        	directoryId : tab.directoryId,
		        	canvas : canvasStr
				 }),
				 error : globalError
		    });
		}
	},
	
	/**
	 * 得到激活的tab
	 */
	getActiveTab : function(){
		if(this.activeTabId==null)return null;
		return this.I_TAB_POOL[this.activeTabId];
	},
	
	closeFn : function(id){
		// 从对象池中删除
		this.I_TAB_POOL[id]=null;
		// 删除对象
		$("#"+id).remove();
		
		// 如果是选定的对象
		if(this.activeTabId == id){
			for (var p in this.I_TAB_POOL) {
				if(this.I_TAB_POOL[p]!=null){
					this.activeTab(p,this.I_TAB_POOL[p].objectType);
					return;	
				}
			}
			this.activeTabId = null;
			// 设置工具箱
			this.dealToolbarClass("toolbar-null");
			// 设置选定
			jQuery.imenu.selectedMenu('editObject',false);
			jQuery.imenu.iContent.reinitCanvas();
		}
	},
	
	closeTab : function(id){
		// 判断是否更新
		var thetab = this.I_TAB_POOL[id];
		if(thetab.isUpdate){
			$.imessagebox("#ibody",{
				title : "提示",
				type : "ync",
				marded : true,
				message : "对象已经修改，是否关闭并保存修改？",
				fn : function(m){
				    var tab = jQuery.iPortalTab.I_TAB_POOL[id];
					// 更新对象
					if(m == "yes"){
						jQuery.imenu.programLoading(true,"program");
						var canvas = jQuery.iPortalTab.getCanvasInfo(id);
						var canvasStr = $.toJSON(canvas);
						$.ajax({
					    	 type: "POST",
					    	 url: "ImetaAction!saveAndCloseFile.action",
							 dataType:"json",
							 data: jQuery.cutil.objectToUrl({
							 	roName : tab.objectName,
					        	roType : tab.objectType,
					        	directoryId : tab.directoryId,
					        	canvas : canvasStr
							 }),
							 success : function(json){
			            		// 删除页面元素
			            		jQuery.iPortalTab.closeFn(id);
			            		jQuery.imenu.programLoading(true,null);
			            		$.imessagebox("#ibody",json);
			            		jQuery.imetabar.createDraggableObjectViewFlush();
			            	 }
					    });
					    
					}
					// 关闭不更新
					else if(m == "no"){
						$.ajax({
					    	 type: "POST",
					    	 url: "ImetaAction!closeFile.action",
							 data: jQuery.cutil.objectToUrl({
							 	roName : tab.objectName,
					        	roType : tab.objectType,
					        	directoryId : tab.directoryId
							 })
					    });
					    
					    // 删除页面元素
						jQuery.iPortalTab.closeFn(id);
						
					}
					// 取消
					else {
						return;
					}
				}
			});
		}
		// 没有更新
		else{
			$.ajax({
		    	 type: "POST",
		    	 url: "ImetaAction!closeFile.action",
				 data: jQuery.cutil.objectToUrl({
				 	roName : thetab.objectName,
		        	roType : thetab.objectType,
		        	directoryId : thetab.directoryId
				 })
		    });
			// 删除页面元素
			this.closeFn(id);
		}
                
	},
	
	//关闭其他
	closeOtherTab : function(id){
		for(var p in this.I_TAB_POOL) {
            if(this.activeTabId ==p){
				continue;
			 }
			if(this.I_TAB_POOL[p]!=null){
				this.closeTab(p);
			}
		}
	}, 
	
	dealToolbarClass : function(cls){
		for(var i in jQuery.iPortalTab.OBJECT_TYPE_CLASS){
			$("#imb").removeClass(jQuery.iPortalTab.OBJECT_TYPE_CLASS[i]);
		}
		$("#imb").addClass(cls);
	},

	//关闭全部
	closeAllTab : function(){
		for(var p in this.I_TAB_POOL){
			if(this.I_TAB_POOL[p]!=null){
				this.closeTab(p);
			}
		} 
		jQuery.imenu.iContent.reinitCanvas();
	},
	
	
	modifyTabText : function(b){
		var tab = this.I_TAB_POOL[this.activeTabId];
		if(b){
			tab.isUpdate=true;
			$("#"+this.activeTabId+"-modified").html("*");
		}else{
			tab.isUpdate=false;
			$("#"+this.activeTabId+"-modified").html("");
		}
	}
};