jQuery.imenu = {
	portalLoading : {},
	
	programLoading : function(display,type){
		// 显示
		if(display){
			if(type == "program"){
				$("#portalLoading").css("display","block");
				$("#portalLoading-msg").html("处理中，请稍等...");
			}else{
				$("#portalLoading").css("display","none");
				$("#portalLoading-msg").html("");
			}
			
			$("#programLoading-mask").css("display","block");
		}
		// 不显示
		else{
			$("#portalLoading").css("display","none");
			$("#programLoading-mask").css("display","none");
		}
	},
	
	menuCursorPath : $path+"/imeta/images/imeta/cursor/",
	/**
	 * 操作台对象句柄
	 */
	iContent : null,
	/**
	 * 操作台剪切板对象句柄
	 * 
	 * originTabId 表示来源tab的id
	 * cutEls 表示剪切的对象
	 */
	iContentClipboard : {
		originTabId : null,
		cutEls : null
	},
	/**
	 * 菜单的所有元素
	 */
	iMenuBtns : {},
	
	userType : null,
	
	initMenuBtns : {
		operator : ['editObject','startCut','startCopy','startAffix','createUser','deleteUser','detectRep','fileNewTrans','fileNewJob','startAddDatabase','fileOpen','fromXMLToOpen','fileSave','fileSaveAs','fileSavedAsXML','imbMenu','commonNew','commonCopy','commonExportPNG','commonExportXML'],
		editor : ['createUser','deleteUser','detectRep','startAddDatabase']
	},
	
	customState : {
		menuCollapse : false,// 菜单
		iovCollapse : false,// 对象视图
		logCollapse : true,// 日志视图
		imbPoint : {
			x : 0,
			y : 0
		},
		iowPoint : {
			x : 0,
			y : 0
		}
	},
	
	pushPortalLoading : function(id, msg){
		eval("$.extend(jQuery.imenu.portalLoading,{"+id+":msg});");
		$("#portalLoading-msg").html(msg);
		
	},
	
	popPortalLoading : function(id){
		eval("jQuery.imenu.portalLoading."+id+" = undefined;");
		var hasLoading = false;
		$.each(jQuery.imenu.portalLoading,function(e,v){
			if(v!=undefined){
				$("#portalLoading-msg").html(v);
				hasLoading = true;
			}
		});
		if(!hasLoading){
			jQuery.imenu.changePortalSize();
			setTimeout(function() {
				$("#portalLoading").css("display","none");
				$("#portalLoading-mask").fadeOut("slow");
			},250);
		}
	},
	
	createPortal : function(path){
		// 产生可拖拽缩略图
		jQuery.imenu.createDraggableOverviewBar($path+"/imeta/js/jquery.ioverview.json","#ibody",path);
	},
	
	// 产生log
	createLog : function(path){
		jQuery.imenu.pushPortalLoading("logBar", "创建日志...");
		$.getJSON(path, function(d){
			var log = $.createObjFromJson(d, "#log", "jQuery.imenu.appendContentForILog");
			jQuery.imenu.popPortalLoading("logBar");
		});
		$.ajax({
            type: "POST",
            url: "ImetaLogAction!loadLogDialog.action",
            dataType: "json",
            success : function(json){
                $.createObjFromJson(json, "#logMain", "jQuery.iformwindow.extendContentFn");
            }
        });
	},
	
	appendContentForILog : function(url, parentDiv){
		if (url.loadUrl) {
            $.ajax({
                url: url.loadUrl,
                async: false,
                dataType : "json",
                success : function(json){
                	var trees = {
		                items: json
		            };
		            $.createObjFromJson(trees, parentDiv, "jQuery.imenu.appendContentForILogEvent");
                },
                error : globalError
            });
        }
	},
	
	appendContentForILogEvent : function(url, parentDiv){
		$("#logCollapse").click(function(e){
			if($("#logCollapse").attr("closed") == 'on'){
				$("#log").animate({ bottom: 0 }, "normal");
				$("#logCollapse").attr("closed", "off");
				$("#logCollapse").attr("class","logCollapse2");
			}else{
				$("#log").animate({ bottom: -182 }, "normal");
				$("#logCollapse").attr("closed", "on");
				$("#logCollapse").attr("class","logCollapse");
			}
		});
	},
	
	/**
     * 通过url创建可拖拽的缩略图
     *
     * @param {}
     *            url json对象的路径
     * @param {}
     *            parentDiv 添加到的父对象名称
     */
    createDraggableOverviewBar: function(url, parentDiv, path){
    	jQuery.imenu.pushPortalLoading("overviewBar", "创建缩略图...");
    	$.getJSON(url, function(d){
            // 创建缩略视图
            var toolbar = $.createObjFromJson(d, parentDiv, "jQuery.imenu.appendContentForOverviewBar");

            $("#iow").draggable({
                containment: parentDiv,
                scroll: false,
                handle: "#iow-tl"
            });
            // 工具箱最小化
            $("#iow-min").bind("click", function(e){
                var is = ($("#iow").css("display") != "none");
            	jQuery.imetabar.changeBarView("iow",!is);
            });
            
            // 创建菜单
            jQuery.imenu.createMenu(path,"#portal");
        });
    },
    
    appendContentForOverviewBar: function(url, parentDiv){
    	
    },
    
  	/**
	 * 改变portal的高度
	 * 
	 * @param c 是否合上
	 */
	changePortalSize : function (){
		var oraHeight = $.getWindowScroll().height;
		var oraWidth = $.getWindowScroll().width;
		var left = "0px";
		if(!jQuery.imenu.customState.iovCollapse){
			left = "200px";
			oraWidth = oraWidth - 200;
		}
		$("#portal_content").css("left",left);
		if(jQuery.imenu.customState.menuCollapse){
			$("#portal_content").height(oraHeight-28);
			jQuery.imenu.iContent.reinitContent(oraHeight-28,oraWidth);
			$("#iov-by").height(oraHeight-101);
			$("#iov").css("top", "28px");
		}else{
			$("#portal_content").height(oraHeight-133);
			jQuery.imenu.iContent.reinitContent(oraHeight-133,oraWidth);
			$("#iov-by").height(oraHeight-206);
			$("#iov").css("top", "133px");
		}
	},
	
	createMenu : function(url, parentDiv) {
		jQuery.imenu.pushPortalLoading("menu", "创建菜单...");
        jQuery.imenu.popPortalLoading("overviewBar");
		$.getJSON(url, function(d) {
			// 创建菜单
			var imenudiv = $.createObjFromJson(d, parentDiv, 'jQuery.imenu.drawMenu');
			$('#imenutab').menutab();
			$("#portal_content").height($.getWindowScroll().height-133);
		    jQuery.imenu.iContent = $.canvasObj($("#portal_content"),$("#portal_overview"));
		    
			jQuery.imenu.initMenu();
			jQuery.imenu.popPortalLoading("menu");
			
		});
	},
	
	iMenuFn : {
		top : {
            logout : function(e){
				$.ajax({
	                type: "POST",
	                url: "ImetaScurityAction!logout.action",
	                dataType: "json",
	                success : globalError
	            });
			},
			undo : function(e){
				jQuery.iPortalTab.undoResentCanvas();
			},
			redo : function(e){
				jQuery.iPortalTab.redoResentCanvas();
			},
			commonNew : function(e) {
			    $.imessagebox("#ibody",{
					title : "提示",
					marded : true,
					type : "custom",
					message : "请选择新建对象的类型",
					btns : [{
							key : "ok",
							text : "确定",
							btnWidth : 50
						}],
					content : $("<select></select>").html("<option value='trans'>转换</option><option value='job'>作业</option>"),
					fn : function(m,n){
					    if(m == 'ok'){
					        var elType = n.find('option:selected').val();
					        switch(elType){
					            case 'trans':
					                jQuery.imenu.iMenuFn.file.fileNewTrans();
                                    break;
                                case 'job':
                                    jQuery.imenu.iMenuFn.file.fileNewJob();
                                    break;
					        }
					    }
					}
			    });
			},
			commonExportPNG : function(e){
			    var activeTabId = jQuery.iPortalTab.activeTabId;
				if(activeTabId==null){
					$.imessagebox.showAlert("请先选择一个编辑对象！");
					return;
				}
				
				var canvas = jQuery.imenu.iContent.getCanvas();
				var idata = canvas.toDataURL();
        		if (!idata || idata.toLowerCase() == 'data:') {
        			return false;
        		}
        		var imgwin = window.open();
        		if (!imgwin) {
        			return false;
        		}
        		imgwin.location = idata;
        		idata = null;
        		return true;
			},
			commonMin : function(e) {
				// 如果缩略视图显示
				var d = ($("#iow").css("display") != "none");
				
				jQuery.imenu.selectedMenu("iovMenu",!d);
				jQuery.imenu.customState.iovCollapse=d;
				var left = (!d)?0:$("#iov").width() * (-1);
				$("#iov").animate({left:left}, "slow");
				
				$.each(jQuery.imetabar.allBars,function(e,v){
					if(jQuery.imenu.userType=='operator'&&v=='imb'){
					}else{
						jQuery.imetabar.changeBarView(v,!d);
					}
				});
				jQuery.imenu.iMenuFn.top.commonCollapse(e);
			},
			commonCollapse : function(e) {
				jQuery.imenu.customState.menuCollapse = false;
				if($.browser.msie){
					if($("#menu_root").css('display')=='none'){
						$("#menu_root").css('display','block');
						jQuery.imenu.customState.menuCollapse = false;
					}else{
						$("#menu_root").css('display','none');
						jQuery.imenu.customState.menuCollapse = true;
					}
		  		}else{
		  			if($("#menu_root").height()!=0){
				  		$("#menu_root").animate({ height: 0 },   "normal"); 
				  		jQuery.imenu.customState.menuCollapse = true;
					}else{
						$("#menu_root").animate({ height: 105 },   "normal"); 
						jQuery.imenu.customState.menuCollapse = false;
					}
		  		}
		  		changeCollapseImg(jQuery.imenu.customState.menuCollapse);
				jQuery.imenu.changePortalSize();
		  		
		  		/**
		  		 * @param c 是否合上
		  		 */
		  		function changeCollapseImg(c){
		  			// 如果是IE
					if ($.browser.msie){
						var img = "commonCollapse";
			  			if(c){
			  				img = "commonCollapse2";
			  			}
						$('#commonCollapse').css({
							"filter":"progid:DXImageTransform.Microsoft.AlphaImageLoader(enabled=true, sizingMethod=scale, src='"+$path+"/imeta/images/imeta/menuBtn/"+img+".png') !important;",
							"width" : "16px",
							"height" : "16px",
							"background":"none"
						});
					}else{
						if(c){
							$('#commonCollapse').removeClass('commonCollapse');
							$('#commonCollapse').addClass('commonCollapse2');
						}else{
							$('#commonCollapse').addClass('commonCollapse');
							$('#commonCollapse').removeClass('commonCollapse2');
						}
					}
		  		}
			}
		},
		start : {
			/**
			 * 剪切操作
			 * 
			 * 首先clone选定的内容，保存到jQuery.imenu.iContentClipboard
			 * 然后删除选定的内容
			 */
			startCut : function(e){
				jQuery.imenu.batchControlMenus({
					startCut : false,
					startCopy : false,
					startAffix : true
				});
				jQuery.imenu.iContent.getCutSelectedEls();
				
			},	
			startCopy : function(c){				
				jQuery.imenu.batchControlMenus({
					startCut : false,
					startCopy : false,
					startAffix : true
				});
				jQuery.imenu.iContent.getCopySelectedEls();
			},	
			startAffix : function(c){
				// TODO
				alert("ok");
			},	
			startRun : function(c){
				var activeTabId = jQuery.iPortalTab.activeTabId;
				if(!activeTabId){
					$.imessagebox.showAlert("请先选择一个转换对象！");
					return;
				}
				if($.iformwindow.activeWindow(activeTabId)){
					return;
				}
				var canvas = jQuery.iPortalTab.getCanvasInfo(activeTabId);
				var tab = jQuery.iPortalTab.I_TAB_POOL[activeTabId];
				var jsonStr = $.toJSON(canvas);
				
				var win = $.iformwindow('#ibody',{
				                    id: activeTabId,
				                    title: '执行转换',
				                    showLoading : true
				                });
				
				$.ajax({
	                type: 'post',
			        url: 'ImetaAction!run.action',
			        dataType: "json",
			        success: function(json){ 
			        	win.appendContent(json);
			        },
			        data:  jQuery.cutil.objectToUrl({
			        	taskId : activeTabId,
			        	roType : jQuery.iPortalTab.OBJECT_TYPE_TRANS,
			        	directoryId : tab.directoryId,
						canvas: jsonStr
			        })
	            });
			},	
			startSuspend : function(c){
				var activeTabId = jQuery.iPortalTab.activeTabId;
				if(!activeTabId){
					$.imessagebox.showAlert("请先选择一个作业对象！");
					return;
				}
				jQuery.imenu.programLoading(true,"program");
				var canvas = jQuery.iPortalTab.getCanvasInfo(activeTabId);
				var tab = jQuery.iPortalTab.I_TAB_POOL[activeTabId];
				$.ajax({
	                type: 'post',
			        url: 'ImetaAction!pause.action',
			        dataType: "json",
			        success: function(json){ 
			        	jQuery.imenu.programLoading(true,null);
						$.imessagebox("#ibody",json);
			        },
			        data:  jQuery.cutil.objectToUrl({
			        	roType : jQuery.iPortalTab.OBJECT_TYPE_TRANS,
						objectName: canvas.objectName,
						directoryId : tab.directoryId
			        })
	            });
			},	
			startStop : function(c){
				var activeTabId = jQuery.iPortalTab.activeTabId;
				if(!activeTabId){
					$.imessagebox.showAlert("请先选择一个作业对象！");
					return;
				}
				jQuery.imenu.programLoading(true,"program");
				var canvas = jQuery.iPortalTab.getCanvasInfo(activeTabId);
				var tab = jQuery.iPortalTab.I_TAB_POOL[activeTabId];
				$.ajax({
	                type: 'post',
			        url: 'ImetaAction!stop.action',
			        dataType: "json",
			        success: function(json){ 
			        	jQuery.imenu.programLoading(true,null);
						$.imessagebox("#ibody",json);
			        },
			        data:  jQuery.cutil.objectToUrl({
			        	roType : jQuery.iPortalTab.OBJECT_TYPE_TRANS,
			        	directoryId : tab.directoryId,
						objectName: canvas.objectName
			        })
	            });
			},	
			startValidate : function(c){
				var activeTabId = jQuery.iPortalTab.activeTabId;
				if(!activeTabId){
					$.imessagebox.showAlert("请先选择一个转换对象！");
					return;
				}
				if($.iformwindow.activeWindow("check_" + activeTabId)){
					return;
				}
				var canvas = jQuery.iPortalTab.getCanvasInfo(activeTabId);
				var tab = jQuery.iPortalTab.I_TAB_POOL[activeTabId];
				
				var win = $.iformwindow('#ibody',{
				                    id: "check_" + activeTabId,
				                    title: '验证转换',
				                    showLoading : true
				                });
				
				$.ajax({
	                type: 'post',
			        url: 'ImetaTransAction!checkTrans.action',
			        dataType: "json",
			        success: function(json){ 
			        	win.appendContent(json);
			        },
			        data:  jQuery.cutil.objectToUrl({
			        	taskId : activeTabId,
						objectName: canvas.objectName,
						directoryId : tab.directoryId
			        }),
			        error: function(e){
			        	$.imessagebox("#ibody",{
							title : "错误",
							type : "alert",
							icon : "error",
							marded : true,
							message : "验证转换出现错误！",
							fn : function(m){
								$("#window-check_" + activeTabId).remove();
							}
			        	});
			        }
	            });
			},	
			startAnalysis : function(c){
				var activeTabId = jQuery.iPortalTab.activeTabId;
				if(!activeTabId){
					$.imessagebox.showAlert("请先选择一个转换对象！");
					return;
				}
				if($.iformwindow.activeWindow("ai_" + activeTabId)){
					return;
				}
				var canvas = jQuery.iPortalTab.getCanvasInfo(activeTabId);
				var tab = jQuery.iPortalTab.I_TAB_POOL[activeTabId];
				var win = $.iformwindow('#ibody',{
				                    id: "ai_" + activeTabId,
				                    title: '分析对数据库的影响',
				                    showLoading : true
				                });
				
				$.ajax({
	                type: 'post',
			        url: 'ImetaTransAction!analyseImpactTrans.action',
			        dataType: "json",
			        success: function(json){ 
			        	win.appendContent(json);
			        },
			        data:  jQuery.cutil.objectToUrl({
			        	taskId : activeTabId,
			        	directoryId : tab.directoryId,
						objectName: canvas.objectName
			        }),
			        error: function(e){
			        	$.imessagebox("#ibody",{
							title : "错误",
							type : "alert",
							icon : "error",
							marded : true,
							message : "分析对数据库的影响出现错误！",
							fn : function(m){
								$("#window-ai_" + activeTabId).remove();
							}
			        	});
			        }
	            });
			},
			jobRun : function(c){
				var activeTabId = jQuery.iPortalTab.activeTabId;
				if(!activeTabId){
					$.imessagebox.showAlert("请先选择一个作业对象！");
					return;
				}
				if($.iformwindow.activeWindow(activeTabId)){
					return;
				}
				var canvas = jQuery.iPortalTab.getCanvasInfo(activeTabId);
				var tab = jQuery.iPortalTab.I_TAB_POOL[activeTabId];
				var jsonStr = $.toJSON(canvas);
				
				var win = $.iformwindow('#ibody',{
				                    id: activeTabId,
				                    title: '执行作业',
				                    showLoading : true
				                });
				
				$.ajax({
	                type: 'post',
			        url: 'ImetaAction!run.action',
			        dataType: "json",
			        success: function(json){ 
			        	win.appendContent(json);
			        },
			        data:  jQuery.cutil.objectToUrl({
			        	taskId : activeTabId,
			        	roType : jQuery.iPortalTab.OBJECT_TYPE_JOB,
			        	directoryId : tab.directoryId,
						canvas: jsonStr
			        })
	            });
			},	
			jobStop : function(c){
				var activeTabId = jQuery.iPortalTab.activeTabId;
				if(!activeTabId){
					$.imessagebox.showAlert("请先选择一个作业对象！");
					return;
				}
				jQuery.imenu.programLoading(true,"program");
				var canvas = jQuery.iPortalTab.getCanvasInfo(activeTabId);
				var tab = jQuery.iPortalTab.I_TAB_POOL[activeTabId];
				$.ajax({
	                type: 'post',
			        url: 'ImetaAction!stop.action',
			        dataType: "json",
			        success: function(json){ 
			        	jQuery.imenu.programLoading(true,null);
						$.imessagebox("#ibody",json);
			        },
			        data:  jQuery.cutil.objectToUrl({
			        	roType : jQuery.iPortalTab.OBJECT_TYPE_JOB,
			        	directoryId : tab.directoryId,
						objectName: canvas.objectName
			        })
	            });
			},
			jobList : function(c){
				if($.iformwindow.activeWindow('jobList')){
					return;
				}
				
				var win = $.iformwindow('#ibody',{
				                    id: 'jobList',
				                    title: '作业列表',
				                    showLoading : false
				                });
				
				$.ajax({
	                type: "POST",
	                url: "ImetaBaseAction!jobList.action",
	                dataType: "json",
	                success : function(json){
		               	win.appendContent(json);
	                }
	            });
			},
			jobListStop : function(dirId,jobName){
				jQuery.imenu.programLoading(true,"program");
				$.ajax({
					type: "post",
					url:"ImetaBaseAction!jobListStop.action",
					dataType:"json",
					data : {
						dirId : dirId,
						jobName : jobName
					},
					success : function(json){
						jQuery.imenu.programLoading(true,null);
						$.imessagebox("#ibody",json);
					}
				});
			},
			editObject : function(c){
			    var isSelected = jQuery.imenu.iMenuBtns['editObject'].selected;
			    var activeTabId = jQuery.iPortalTab.activeTabId;
				if(activeTabId==null){
					$.imessagebox.showAlert("请先选择一个编辑对象！");
					return;
				}
				var tab = jQuery.iPortalTab.I_TAB_POOL[activeTabId];
				var canvas = jQuery.iPortalTab.getCanvasInfo(activeTabId);
				var canvasStr = $.toJSON(canvas);
				
				// 如果是选中编辑
				if(!isSelected){
				    
				    jQuery.imenu.programLoading(true,"program");
					$.ajax({
		                type: 'post',
		                url: 'ImetaAction!editFile.action',
		                data: {
		                	roName : tab.objectName,
				        	roType : tab.objectType,
				        	directoryId : tab.directoryId,
				        	edit : 'on'
		                },
		            	dataType:"json",
		            	success : function(json){
		            		// 修改保存状态
		            		if(json.success){
		            			 jQuery.imenu.selectedMenu('editObject',!isSelected);
		            			 tab.editable=!isSelected;
		            		}
		            		jQuery.imenu.programLoading(true,null);
		            		$.imessagebox("#ibody",json);
		            	},
					    error : globalError
		            });
				    
				}else{
				    // 如果是修改过的提示保存或关闭
				    // 判断是否更新
            		if(tab.isUpdate){
            			$.imessagebox("#ibody",{
            				title : "提示",
            				type : "ync",
            				marded : true,
            				message : "对象已经修改，是否保存修改并取消编辑？",
            				fn : function(m){
            					// 更新对象
            					if(m == "yes"){
            						jQuery.imenu.programLoading(true,"program");
            						$.ajax({
            					    	 type: "POST",
            					    	 url: "ImetaAction!saveAndNotEditFile.action",
            							 dataType:"json",
            							 data: jQuery.cutil.objectToUrl({
            							 	roName : tab.objectName,
            					        	roType : tab.objectType,
            					        	directoryId : tab.directoryId,
            					        	canvas : canvasStr,
            					        	edit : 'off'
            							 }),
            							 success : function(json){
            			            		// 修改保存状态
                		            		if(json.success){
                		            			//修改对象树
                		            			jQuery.imetabar.closeObjectViewFileChildren(tab.oriId);
                			            		//修改tab
                			            		jQuery.iPortalTab.saveActiveTab(json.canvas);
                			            		// 修改保存状态
    		            		                jQuery.imenu.selectedMenu('editObject',!isSelected);
    		            		                tab.editable=!isSelected;
                		            		}
                		            		jQuery.imenu.programLoading(true,null);
                		            		$.imessagebox("#ibody",json);
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
            					        	roType :tab.objectType,
            					        	directoryId : tab.directoryId
            							 })
            					    });
            					    
            					    // 删除页面元素
            						jQuery.iPortalTab.closeFn(activeTabId);
            						
            					}
            					// 取消
            					else {
            						return;
            					}
            				}
            			});
            		}
				    // 如果是未修改过的，从编辑对象组中清除
				    else{
				        jQuery.imenu.programLoading(true,"program");
    					$.ajax({
    		                type: 'post',
    		                url: 'ImetaAction!editFile.action',
    		                data: {
    		                	roName : tab.objectName,
    				        	roType : tab.objectType,
    				        	directoryId : tab.directoryId,
    				        	edit : 'off'
    		                },
    		            	dataType:"json",
    		            	success : function(json){
    		            		// 修改保存状态
    		            		if(json.success){
    		            			 jQuery.imenu.selectedMenu('editObject',!isSelected);
    		            			 tab.editable=!isSelected;
    		            		}
    		            		jQuery.imenu.programLoading(true,null);
    		            		$.imessagebox("#ibody",json);
    		            	},
    					    error : globalError
    		            });
				    }
				}
			},
			magnify : function(c){
				var isSelected = jQuery.imenu.iMenuBtns['magnify'].selected;
				if(!isSelected && jQuery.iPortalTab.activeTabId==null){
					$.imessagebox.showAlert("请先选择一个编辑对象！");
					return;
				}
				
				// 放大按钮选择
				jQuery.imenu.selectedMenu('magnify',!isSelected);
				// 操作content
				jQuery.imenu.iContent.setOuterControl({
					isControl : !isSelected,
					controlType : (!isSelected)?'magnify':null,
					cursorImg : jQuery.imenu.menuCursorPath+"magnify.ico"
				});
				// 如果是选定则清楚其他缩放按钮选定
				if(!isSelected){
					jQuery.imenu.selectedMenu('lessen',false);
					jQuery.imenu.selectedMenu('partMagnify',false);
					jQuery.imenu.selectedMenu('screenMove',false);
				}
			},	
			lessen : function(c){
				var isSelected = jQuery.imenu.iMenuBtns['lessen'].selected;
				if(!isSelected && jQuery.iPortalTab.activeTabId==null){
					$.imessagebox.showAlert("请先选择一个编辑对象！");
					return;
				}
				// 放大按钮选择
				jQuery.imenu.selectedMenu('lessen',!isSelected);
				// 操作content
				jQuery.imenu.iContent.setOuterControl({
					isControl : !isSelected,
					controlType : (!isSelected)?'lessen':null,
					cursorImg : jQuery.imenu.menuCursorPath+"lessen.ico"
				});
				// 如果是选定则清楚其他缩放按钮选定
				if(!isSelected){
					jQuery.imenu.selectedMenu('magnify',false);
					jQuery.imenu.selectedMenu('partMagnify',false);
					jQuery.imenu.selectedMenu('screenMove',false);
				}
			},	
			hundred : function(c){
				if(jQuery.iPortalTab.activeTabId==null){
					$.imessagebox.showAlert("请先选择一个编辑对象！");
					return;
				}
				// 取消其他缩放按钮
				jQuery.imenu.selectedMenu('magnify',false);
				jQuery.imenu.selectedMenu('lessen',false);
				jQuery.imenu.selectedMenu('partMagnify',false);
				jQuery.imenu.selectedMenu('screenMove',false);
				
				// 操作content
				jQuery.imenu.iContent.setOuterControl({
					isControl : false,
					controlType : null,
					cursorImg : null
				});
				
				// 修改缩放
				jQuery.imenu.iContent.setHundredPercent();
				
			},
			partMagnify : function(c){
				var isSelected = jQuery.imenu.iMenuBtns['partMagnify'].selected;
				if(!isSelected&&jQuery.iPortalTab.activeTabId==null){
					$.imessagebox.showAlert("请先选择一个编辑对象！");
					return;
				}
				
				// 放大按钮选择
				jQuery.imenu.selectedMenu('partMagnify',!isSelected);
				// 操作content
				jQuery.imenu.iContent.setOuterControl({
					isControl : !isSelected,
					controlType : (!isSelected)?'partMagnify':null,
					cursorImg : jQuery.imenu.menuCursorPath+"partMagnify.ico"
				});
				// 如果是选定则清楚其他缩放按钮选定
				if(!isSelected){
					jQuery.imenu.selectedMenu('magnify',false);
					jQuery.imenu.selectedMenu('lessen',false);
					jQuery.imenu.selectedMenu('screenMove',false);
				}
			},
			proportion : function(c){
				if(jQuery.iPortalTab.activeTabId==null){
					$.imessagebox.showAlert("请先选择一个编辑对象！");
					return;
				}
				// 取消其他缩放按钮
				jQuery.imenu.selectedMenu('magnify',false);
				jQuery.imenu.selectedMenu('lessen',false);
				jQuery.imenu.selectedMenu('partMagnify',false);
				jQuery.imenu.selectedMenu('screenMove',false);
				
				// 操作content
				jQuery.imenu.iContent.setOuterControl({
					isControl : false,
					controlType : null,
					cursorImg : null
				});
				
				// 修改缩放
				jQuery.imenu.iContent.setFixScale();
			},	
			screenMove : function(c){
				var isSelected = jQuery.imenu.iMenuBtns['screenMove'].selected;
				if(!isSelected&&jQuery.iPortalTab.activeTabId==null){
					$.imessagebox.showAlert("请先选择一个编辑对象！");
					return;
				}
				// 放大按钮选择
				jQuery.imenu.selectedMenu('screenMove',!isSelected);
				// 操作content
				jQuery.imenu.iContent.setOuterControl({
					isControl : !isSelected,
					controlType : (!isSelected)?'screenMove':null,
					cursorImg : jQuery.imenu.menuCursorPath+"screenMove.ico"
				});
				// 如果是选定则清楚其他缩放按钮选定
				if(!isSelected){
					jQuery.imenu.selectedMenu('magnify',false);
					jQuery.imenu.selectedMenu('lessen',false);
					jQuery.imenu.selectedMenu('partMagnify',false);
				}
			},	
			showGrid : function(c){
				if(jQuery.imenu.iContent!=null){
					var isSelected = jQuery.imenu.iMenuBtns['showGrid'].selected;
					jQuery.imenu.selectedMenu('showGrid',!isSelected);
					jQuery.imenu.iContent.setShowGrid(!isSelected);
					jQuery.imenu.iContent.redraw();
					
					// 当选定时，贴近网格可用
					jQuery.imenu.batchControlMenus({
						closeTOGrid : !isSelected
					});
				}
			},	
			closeTOGrid : function(c){
		     	if(jQuery.imenu.iContent!=null){
					var isSelected = jQuery.imenu.iMenuBtns['closeTOGrid'].selected;
					jQuery.imenu.selectedMenu('closeTOGrid',!isSelected);
					jQuery.imenu.iContent.setCloseGrid(!isSelected);
					jQuery.imenu.iContent.redraw();
				}
			}
		},
		file : {
			createUser : function(e){
				if($.iformwindow.activeWindow('createUser')){
					return;
				}
				
				var win = $.iformwindow('#ibody',{
				                    id: 'createUser',
				                    title: '创建用户',
				                    showLoading : false
				                });
				
				$.ajax({
	                type: "POST",
	                url: "ImetaScurityAction!createUser.action",
	                dataType: "json",
	                success : function(json){
		               	win.appendContent(json);
	                },
	                error : function(e){
	                  $("#window-createUser").remove();
	                  $.imessagebox.showAlert("无法创建用户，请先连接资源库！");
	                }
	            });
			},
			createUserBtn : {
				ok : function(e,v){
					var elId = e.target.id;
					var id = elId.split(".")[0];
					$("#"+id).ajaxSubmit({
                        type: "POST",
						url:"ImetaScurityAction!saveUser.action",
						dataType:"json",
						data : {
							id : id
						},
						success : function(json){
							if(json.success){
								$("#window-createUser").remove();
							}
							$.imessagebox("#ibody",json);
						}
					});
				},
				cancel : function(e,v){
					$("#window-createUser").remove();
				}
			},
			deleteUser : function(e){
				if($.iformwindow.activeWindow('toDeleteUser')){
					return;
				}
				
				var win = $.iformwindow('#ibody',{
				                    id: 'toDeleteUser',
				                    title: '删除用户',
				                    showLoading : true
				                });
				
				$.ajax({
	                type: "POST",
	                url: "ImetaScurityAction!toDeleteUser.action",
	                dataType: "json",
	                success : function(json){
		               	win.appendContent(json);
	                },
	                error : function(e){
	                  $("#window-toDeleteUser").remove();
	                  $.imessagebox.showAlert("无法获取用户列表，请先连接资源库！");
	                }
	            });
			},
			deleteUserBtn : {
				ok : function(e,v){
					var elId = e.target.id;
					var id = elId.split(".")[0];
					$("#"+id).ajaxSubmit({
                        type: "POST",
						url:"ImetaScurityAction!deleteUser.action",
						dataType:"json",
						data : {
							id : id
						},
						success : function(json){
						if(json.success){
							 $("#window-toDeleteUser").remove();
							 }
							$.imessagebox("#ibody",json);
						},
	                 error : function(e){
	                  $.imessagebox.showAlert("请选择要删除的用户！");
	                }
					});
				},
				cancel : function(e,v){
					$("#window-toDeleteUser").remove();
				}
			},
			editCurrentUser : function(e){
				if($.iformwindow.activeWindow('toEditCurrentUser')){
					return;
				}
				
				var win = $.iformwindow('#ibody',{
				                    id: 'toEditCurrentUser',
				                    title: '编辑当前用户',
				                    showLoading : true
				                });
				
				$.ajax({
	                type: "POST",
	                url: "ImetaScurityAction!toEditCurrentUser.action",
	                dataType: "json",
	                success : function(json){
		               	win.appendContent(json);
	                },
	                error : function(e){
	                  $("#window-toEditCurrentUser").remove();
	                  $.imessagebox.showAlert("无法获取用户信息，请先连接资源库！");
	                }
	            });
			},
			editUserBtn : {
				ok : function(e,v){
					var elId = e.target.id;
					var id = elId.split(".")[0];
					$("#"+id).ajaxSubmit({
                        type: "POST",
						url:"ImetaScurityAction!editCurrentUser.action",
						dataType:"json",
						data : {
							id : id
						},
						success : function(json){
						     if(json.success) {
							 $("#window-toEditCurrentUser").remove();
							 }
							$.imessagebox("#ibody",json);
						}
					});
				},
				cancel : function(e,v){
					$("#window-toEditCurrentUser").remove();
				}
			},
			connectRep : function(e){
				$.ajax({
	                type: "POST",
	                url: "ImetaScurityAction!logout.action",
	                dataType: "json",
	                success : globalError
	            });
			},
			connectRep1 : function(e){
				if($.iformwindow.activeWindow('connection_rep')){
					return;
				}
				
				var win = $.iformwindow('#ibody',{
				                    id: 'connection_rep',
				                    title: '连接资源库',
				                    showLoading : true
				                });
				
				$.ajax({
	                type: "POST",
	                url: "ImetaDBAction!connectRep.action",
	                dataType: "json",
	                success : function(json){
		               	win.appendContent(json);
	                }
	            });
			},
			connectRepBtn : {
				ok : function(e,v){
					$("#connection_rep").ajaxSubmit({
                        type: "POST",
						url:"ImetaDBAction!connectRepSubmit.action",
						dataType:"json",
						success : function(json){
								$("#window-connection_rep").remove();

							$.imessagebox("#ibody",json);
						}
					});
				},
				cancel : function(e,v){
					$("#window-connection_rep").remove();
				}
			},
			disconnectRep : function(e){
				if($.iformwindow.activeWindow('disConnectRep')){
					return;
				}
				$.ajax({
	                type: "POST",
	                url: "ImetaDBAction!disConnectRep.action",
	                dataType: "json",
	                success : function(json){
	                    $.imessagebox("#ibody",json);
	                }
	            });
			},
			detectRep : function(e){
				if($.iformwindow.activeWindow('recent_rep_explorer')){
					return;
				}
				
				var id = "recent_rep_explorer";
				
				var win = $.iformwindow('#ibody',{
                    id: id,
                    title: '探测资源库',
                    showLoading : true
                });
				
				$.ajax({
	                type: "POST",
	                url: "ImetaDBAction!detectRep.action",
	                dataType: "json",
	                data : jQuery.cutil.objectToUrl({
	                	id : id,
	                	// 显示的元素类型
	                	showTypes:"all",
	                	modify:"true"
	                	// 点击确定后调用的方法，有两个参数输入，1 类型，2 ID
	                	// customOkFn : "jQuery.imenu.iMenuFn.file.detectRepBtn.test"
	                }),
	                success : function(json){
		               	win.appendContent(json);
	                },
	                error : function(e){
	                $("#window-recent_rep_explorer").remove();
	                  $.imessagebox.showAlert("请先连接资源库！");
	                }
	            });
			},
			detectRepBtn : {
			    deleteObject : function(e){
			       e.stopPropagation();
			       t=e.target;
			       $.imessagebox("#ibody",{
    					title : "确认",
    					marded : true,
    					type : "confirm",
    					message : "是否要删除【"+t.getAttribute('elName')+"】对象",
    					fn : function(m,n){
    					    if(m=='yes'){
    					        jQuery.imenu.programLoading(true,"program");
    					        $.ajax({
                	                type: "POST",
                	                url: "ImetaAction!deleteFile.action",
                	                dataType: "json",
                	                data : jQuery.cutil.objectToUrl({
                	                	elType : t.getAttribute('elType'),
                	                	directoryId : t.getAttribute('directoryId'),
                	                	elName : t.getAttribute('elName'),
                	                	elId : t.getAttribute('elId')
                	                }),
                	                success : function(json){
                		                $.imessagebox("#ibody",json);
                		                jQuery.imenu.iMenuFn.file.detectRepBtn.flush(e);
                	                },
                	                error : globalError
                	            });
    					    }
    					}
			       });
			    },
			    addFolder : function(e){
			        e.stopPropagation();
			        t=e.target;
			        $.imessagebox("#ibody",{
    					title : "输入",
    					marded : true,
    					type : "prompt",
    					message : "请输入要创建的目录名称",
    					fn : function(m,n){
    					    if(m=='ok'){
    					         jQuery.imenu.programLoading(true,"program");
    					         $.ajax({
                	                type: "POST",
                	                url: "ImetaBaseAction!addDirectory.action",
                	                dataType: "json",
                	                data : jQuery.cutil.objectToUrl({
                	                	directoryId : t.getAttribute('directoryId'),
                	                	directoryName : n
                	                }),
                	                success : function(json){
                		                $.imessagebox("#ibody",json);
                		                jQuery.imenu.iMenuFn.file.detectRepBtn.flush(e);
                	                },
                	                error : globalError
                	            });
    					    }
    					}
			        });
			    },
			    deleteFolder : function(e){
			        e.stopPropagation();
			        t=e.target;
			        $.imessagebox("#ibody",{
    					title : "确认",
    					marded : true,
    					type : "confirm",
    					message : "是否要删除目录【"+t.getAttribute('directoryName')+"】，以及该目录下面所有对象",
    					fn : function(m,n){
    					    if(m=='yes'){
    					         jQuery.imenu.programLoading(true,"program");
    					         $.ajax({
                	                type: "POST",
                	                url: "ImetaBaseAction!deleteDirectory.action",
                	                dataType: "json",
                	                data : jQuery.cutil.objectToUrl({
                	                	directoryId : t.getAttribute('directoryId')
                	                }),
                	                success : function(json){
                		                $.imessagebox("#ibody",json);
                		                jQuery.imenu.iMenuFn.file.detectRepBtn.flush(e);
                	                },
                	                error : globalError
                	            });
    					    }
    					}
			        });
			    },
			    editFolder : function(e){
			        e.stopPropagation();
			        t=e.target;
			        $.imessagebox("#ibody",{
    					title : "输入",
    					marded : true,
    					type : "prompt",
    					defaultValue : t.getAttribute('directoryName'),
    					message : "请输入目录【"+t.getAttribute('directoryName')+"】的新名称",
    					fn : function(m,n){
    					    if(m=='ok'){
    					         jQuery.imenu.programLoading(true,"program");
    					         $.ajax({
                	                type: "POST",
                	                url: "ImetaBaseAction!editDirectory.action",
                	                dataType: "json",
                	                data : jQuery.cutil.objectToUrl({
                	                	directoryId : t.getAttribute('directoryId'),
                	                	directoryName : t.getAttribute('directoryName'),
                	                	newDirectoryName : n
                	                }),
                	                success : function(json){
                		                $.imessagebox("#ibody",json);
                		                jQuery.imenu.iMenuFn.file.detectRepBtn.flush(e);
                	                },
                	                error : globalError
                	            });
    					    }
    					}
			        });
			    },
				test : function(t,id){
					alert("type:"+t+";id="+id);
				},
				ok : function(e,v){
					var t = e.target;
					var id = t.id.split(".")[0];
					var customOkFn = t.getAttribute("customOkFn");
					var parentId = t.getAttribute("parentId");
					var st = $(".tree-item-select");
					if(customOkFn && customOkFn!=null){
						try{
							eval(customOkFn+"(st,parentId)");
						}catch(ex){
							console.log("error fn from detectRepBtn:"+customOkFn);
						}
					}
					$("#window-"+id).remove();
					
				},
				flush : function(e,v){
					$("#repExplorerTreeDiv").html("重新加载中...");
					var t = e.target;
					var id = t.id.split(".")[0];
					var prop = $("[id="+id+".btn.flush]");
					$.ajax({
		                type: "GET",
		                url: "ImetaDBAction!loadRepExplorerObjects.action",
		                data : jQuery.cutil.objectToUrl({
		                	id : id,
		                	// 显示的元素类型
		                	customOkFn : prop.attr("customOkFn"),
		                	parentId : prop.attr("parentId"),
		                	showTypes : prop.attr("showTypes"),
		                	modify : prop.attr("modify")
		                }),
		                dataType: "json",
		                success : function(json){
		                    $("#repExplorerTreeDiv").empty();
			                var trees = {
			                   items: json
	    		            };
	    		            $.createObjFromJson(trees, $("#repExplorerTreeDiv"), "jQuery.imetabar.appendContentForObjectView");
		                },
		                error : globalError
		            });
				},
				flushDirectory : function(e,v){
					$("#repDirectoryTreeDiv").html("重新加载中...");
					var t = e.target;
					var id = t.id.split(".")[0];
					$.ajax({
		                type: "GET",
		                url: "ImetaDBAction!loadRepDirectoryObjects.action",
		                data : jQuery.cutil.objectToUrl({
		                	id : id,
		                	// 显示的元素类型
		                	customOkFn : t.getAttribute("customOkFn"),
		                	parentId : t.getAttribute("parentId"),
		                	showTypes : t.getAttribute("showTypes"),
		                	modify : t.getAttribute("modify")
		                }),
		                dataType: "json",
		                success : function(json){
		                    $("#repDirectoryTreeDiv").empty();
			                var trees = {
			                   items: json
	    		            };
	    		            $.createObjFromJson(trees, $("#repDirectoryTreeDiv"), "jQuery.imetabar.appendContentForObjectView");
		                },
		                error : globalError
		            });
				},
				itemMouseOver : function(e){
					e.target.style.cursor = 'pointer';
					e.target.style.color = 'red';
				},
				itemMouseOut : function(e){
					e.target.style.cursor = 'auto';
					e.target.style.color = 'black';
				},
				itemClick : function(e){
					$(".tree-item-select").removeClass("tree-item-select");
					e.target.className = e.target.className + " tree-item-select";
					var id = e.target.id;
				}
			},
			getDirectory : function(st,parentId){
			    var directoryPath = st.attr("directoryPath");
			    if(directoryPath != undefined && directoryPath != "/"){
			        $("[id=messageInput-"+parentId+"]").val(directoryPath+"/");
			    }else{
			        $("[id=messageInput-"+parentId+"]").val("/");
			    }
			},
			fileNewTrans : function(c){
			    $.imessagebox("#ibody",{
			        id : "fileNewTrans",
					title : "输入",
					type : "prompt",
					marded : true,
					btns : [{
					    key : "getDirectory",
					    text : "获得目录",
					    btnWidth : "100",
					    removeMessage : false
					}],
					message : "请输入新转换的名字",
					fn : function(m,n){
					    if(m == 'getDirectory'){
					        var win = $.iformwindow('#ibody',{
			                    id: 'getDirectory',
			                    parentId : 'fileNewTrans',
			                    title: '请选择目录',
			                    showLoading : true,
			                    zindex : 400010
			                });
					        $.ajax({
            	                type: "POST",
            	                url: "ImetaBaseAction!getDirectory.action",
            	                data: jQuery.cutil.objectToUrl({
            	                    id : "getDirectory",
            	                    customOkFn : "jQuery.imenu.iMenuFn.file.getDirectory",
            	                    parentId : 'fileNewTrans'
            	                }),
            	                dataType: "json",
            	                success : function(json){
            		                win.appendContent(json);
            	                },
            	                error : globalError
            	            });
					    }else if(m == 'ok'){
    					    jQuery.imenu.programLoading(true,"program");
            				$.ajax({
            	                type: "POST",
            	                url: "ImetaAction!newTransFile.action",
            	                data: "newTransName="+n,
            	                dataType: "json",
            	                success : function(json){
            	                	var objectName = json.objectName;
            		                var frameConfig = {
            		                    id: json.objectType+"_new_"+(jQuery.iPortalTab.newTabIndex++),
            		                    objectType:json.objectType,
            		                    objectName:objectName,
            		                    title: objectName,
            		                    isNew: true,
            		                    editable : json.editable,
            		                    directoryId : json.directoryId,
            		                    directoryName : json.directoryName,
            		                    directoryPath : json.directoryPath,
            		                    ori: json
            		                };
            		                
            		                jQuery.iPortalTab.createTab(frameConfig);
            		                jQuery.imenu.programLoading(false);
            	                },
            	                error : globalError
            	            });
					    }
					}
			    });
			},
			fileNewJob : function(c){
				$.imessagebox("#ibody",{
				    id : "fileNewJob",
					title : "输入",
					type : "prompt",
					marded : true,
					btns : [{
					    key : "getDirectory",
					    text : "获得目录",
					    btnWidth : "100",
					    removeMessage : false
					}],
					message : "请输入新作业的名字",
					fn : function(m,n){
					    if(m == 'getDirectory'){
					        var win = $.iformwindow('#ibody',{
			                    id: 'getDirectory',
			                    parentId : 'fileNewJob',
			                    title: '请选择目录',
			                    showLoading : true,
			                    zindex : 400010
			                });
					        $.ajax({
            	                type: "POST",
            	                url: "ImetaBaseAction!getDirectory.action",
            	                data: jQuery.cutil.objectToUrl({
            	                    id : "getDirectory",
            	                    customOkFn : "jQuery.imenu.iMenuFn.file.getDirectory",
            	                    parentId : 'fileNewJob'
            	                }),
            	                dataType: "json",
            	                success : function(json){
            		                win.appendContent(json);
            	                },
            	                error : globalError
            	            });
					    }else if(m == 'ok'){
    						jQuery.imenu.programLoading(true,"program");
            				$.ajax({
            	                type: "POST",
            	                url: "ImetaAction!newJobFile.action",
            	                dataType: "json",
            	                data:"newJobName="+n,
            	                success : function(json){
            	                	var objectName = json.objectName;
            		                var frameConfig = {
            		                    id: json.objectType+"_new_"+(jQuery.iPortalTab.newTabIndex++),
            		                    objectType:json.objectType,
            		                    objectName:objectName,
            		                    title: objectName,
            		                    editable : json.editable,
            		                    isNew: true,
            		                    directoryId : json.directoryId,
            		                    directoryName : json.directoryName,
            		                    directoryPath : json.directoryPath,
            		                    ori: json
            		                };
            		                
            		                jQuery.iPortalTab.createTab(frameConfig);
            		                jQuery.imenu.programLoading(false);
            	                },
            	                error : globalError
            	            });
					    }
					}
	        	});
			},
			startAddDatabase : function(c){
				if($.iformwindow.activeWindow("new_database")){
					return;
				}
				
				var win = $.iformwindow('#ibody',{
		                    id: "new_database",
		                    title: "创建数据库",
		                    showLoading : true
		                });
				
                $.ajax({
	                type: "POST",
	                url: "ImetaDBAction!createDatabase.action",
	                dataType: "json",
	                success : function(json){
	                	win.appendContent(json);
	                }
	          	});
			},
			fileOpen : function(c){
				if($.iformwindow.activeWindow('recent_rep_explorer')){
					return;
				}
				
				var id = "recent_rep_explorer";
				
				var win = $.iformwindow('#ibody',{
                    id: id,
                    title: '打开文件',
                    showLoading : true
                });
				
				$.ajax({
	                type: "POST",
	                url: "ImetaAction!fileOpen.action",
	                dataType: "json",
	                data : jQuery.cutil.objectToUrl({
	                	id : id,
	                	// 显示的元素类型
	                	showTypes:"trans,job",
	                	// 点击确定后调用的方法，有两个参数输入，1 类型，2 ID
	                	customOkFn : "jQuery.imenu.iMenuFn.file.fileOpenOk"
	                }),
	                success : function(json){
		               	win.appendContent(json);
	                },
	                error : function(e){
	                $("#window-recent_rep_explorer").remove();
	                  $.imessagebox.showAlert("请先连接资源库！");
	                }
	            });
			},	
			fileOpenOk : function(st){
				// 判断是否打开，如果打开提示先关闭
	            var id = st.attr("repId");
	            if(jQuery.iPortalTab.hasCreateTab(id)){
	            	var tabId = jQuery.iPortalTab.I_PORTAL_TAB_ID_PRE+id;
	            	jQuery.iPortalTab.activeTab(tabId,st.attr("elType"));
	            	return;
	            }
	            jQuery.imenu.programLoading(true,"program");
	            // 读取页面信息
                $.ajax({
	                type: "POST",
	                url: "ImetaAction!openFile.action",
	                data: jQuery.cutil.objectToUrl({
	                	roName : st.attr("elName"),
	                	roType : st.attr("elType"),
	                	directoryId : st.attr("directoryId")
	                }),
	                dataType: "json",
	                success : function(json){
		                var frameConfig = {
		                    id: st.attr("repId"),
		                    objectType: st.attr("elType"),
		                    objectName: st.attr("elName"),
		                    directoryId : st.attr("directoryId"),
		                    title: st.attr("elName"),
		                    editable: json.editable,
		                    ori: json
		                };
		                jQuery.iPortalTab.createTab(frameConfig);
		               
		                jQuery.imenu.programLoading(false);
	                },
	                error : globalError
	            });
            },
			fromXMLToOpen : function(c){
				if($.iformwindow.activeWindow('importFile')){
					return;
				}
				
				var win = $.iformwindow('#ibody',{
                    id: 'importFile',
                    title: '从XML文件打开对象',
                    showLoading : false
                });
				
				$.ajax({
	                type: "POST",
	                url: "ImetaAction!importFile.action",
	                dataType: "json",
	                success : function(json){
		               	win.appendContent(json);
	                },
	                error : globalError
	            });
			},
			fromXMLToOpenBtn : {
			    ok : function(e,v){
			        var elId = e.target.id;
    				var id = elId.split(".")[0];
    				jQuery.imenu.programLoading(true,"program");
    				$("#"+id).ajaxSubmit({
    					type:"POST",
    					url:"FileUploadAction!uploadXMLFile.action",
    					dataType:"json",
    					data : {
    						id : id
    					},
    					success : function(json){
    						$("#window-"+id).remove();
    						var objectName = json.objectName;
    		                var frameConfig = {
    		                    id: json.objectType+"_new_"+(jQuery.iPortalTab.newTabIndex++),
    		                    objectType:json.objectType,
    		                    objectName:objectName,
    		                    title: objectName,
    		                    isNew: true,
    		                    editable : json.editable,
    		                    directoryId : json.directoryId,
    		                    directoryName : json.directoryName,
    		                    directoryPath : json.directoryPath,
    		                    ori: json
    		                };
    		                
    		                jQuery.iPortalTab.createTab(frameConfig);
    		                jQuery.imenu.programLoading(false);
    					},
    					error : function(){
    						$.imessagebox.showAlert("上传文件格式不正确，请检查后再上传！");
    					}
    				});
			    },
			    cancel : function(e,v){
			        var elId = e.target.id;
    				var id = elId.split(".")[0];
    				$("#window-"+id).remove();
			    },
			    pathBtn : function(e,v){
			        var win = $.iformwindow('#ibody',{
	                    id: 'getDirectory',
	                    parentId : 'importFile',
	                    title: '请选择目录',
	                    showLoading : true
	                });
			        $.ajax({
    	                type: "POST",
    	                url: "ImetaBaseAction!getDirectory.action",
    	                data: jQuery.cutil.objectToUrl({
    	                    id : "getDirectory",
    	                    customOkFn : "jQuery.imenu.iMenuFn.file.fromXMLToOpenBtn.pathBtnOk",
    	                    parentId : 'importFile'
    	                }),
    	                dataType: "json",
    	                success : function(json){
    		                win.appendContent(json);
    	                },
    	                error : globalError
    	            });
			    },
			    pathBtnOk : function(st,parentId){
			        var directoryPath = st.attr("directoryPath");
    			    if(directoryPath != undefined && directoryPath!=""){
    			        $("[id="+parentId+".path]").val(directoryPath);
    			    }else{
    			        $("[id="+parentId+".path]").val("/");
    			    }
			    }
			},
			fileSave : function(e){
			    var activeTabId = jQuery.iPortalTab.activeTabId;
				if(activeTabId==null){
					$.imessagebox.showAlert("请先选择一个编辑对象！");
					return;
				}
				
				if(jQuery.iPortalTab.isUpdateById(activeTabId)){
					jQuery.imenu.programLoading(true,"program");
					var canvas = jQuery.iPortalTab.getCanvasInfo(activeTabId);
					var canvasStr = $.toJSON(canvas);
					var tab = jQuery.iPortalTab.I_TAB_POOL[activeTabId];
					$.ajax({
		                type: 'post',
		                url: 'ImetaAction!saveFile.action',
		                data: {
		                	roName : tab.objectName,
				        	roType : tab.objectType,
				        	directoryId : tab.directoryId,
		                	canvas : canvasStr
		                },
		            	dataType:"json",
		            	success : function(json){
		            		// 修改保存状态
		            		if(json.success){
		            			//修改对象树
		            			jQuery.imetabar.closeObjectViewFileChildren(tab.oriId);
			            		//修改tab
			            		jQuery.iPortalTab.saveActiveTab(json.canvas);
		            		}
		            		jQuery.imenu.programLoading(true,null);
		            		$.imessagebox("#ibody",json);
		            		jQuery.imetabar.createDraggableObjectViewFlush();
		            	},
					    error : globalError
		            });
				}
			},
			fileSaveAs : function(c){
			    var activeTabId = jQuery.iPortalTab.activeTabId;
				if(activeTabId==null){
					$.imessagebox.showAlert("请先选择一个编辑对象！");
					return;
				}
				
				var tab = jQuery.iPortalTab.I_TAB_POOL[activeTabId];
				
				var fullName = tab.directoryPath;
				if(fullName!="/"){
				    fullName = fullName + "/";
				}
				fullName = fullName + tab.objectName;
				
				$.imessagebox("#ibody",{
				    id : "fileSaveAs",
					title : "输入",
					type : "prompt",
					marded : true,
					defaultValue : fullName,
					message : "请输入另存为对象的名字",
					btns : [{
					    key : "getDirectory",
					    text : "获得目录",
					    btnWidth : "100",
					    removeMessage : false
					}],
					fn : function(m,n){
					    if(m == 'getDirectory'){
					        var win = $.iformwindow('#ibody',{
			                    id: 'getDirectory',
			                    parentId : 'fileSaveAs',
			                    title: '请选择目录',
			                    showLoading : true,
			                    zindex : 400010
			                });
					        $.ajax({
            	                type: "POST",
            	                url: "ImetaBaseAction!getDirectory.action",
            	                data: jQuery.cutil.objectToUrl({
            	                    id : "getDirectory",
            	                    customOkFn : "jQuery.imenu.iMenuFn.file.getDirectory",
            	                    parentId : 'fileSaveAs'
            	                }),
            	                dataType: "json",
            	                success : function(json){
            		                win.appendContent(json);
            	                },
            	                error : globalError
            	            });
					    }else if(m == 'ok'){
    						jQuery.imenu.programLoading(true,"program");
    						var canvas = jQuery.iPortalTab.getCanvasInfo(activeTabId);
    						var canvasStr = $.toJSON(canvas);
    						
    						$.ajax({
    			                type: 'post',
    			                url: 'ImetaAction!saveFileAs.action',
    			                data: {
    			                    newName : n,
    			                	roName : tab.objectName,
    					        	roType : tab.objectType,
    					        	directoryId : tab.directoryId,
    			                	canvas : canvasStr
    			                },
    			            	dataType:"json",
    			            	success : function(json){
    			            		if(json.success){
    			            			var frameConfig = {
                		                    id: "n_" + tab.objectType + "_" + (jQuery.iPortalTab.newTabIndex++),
                		                    objectType:  tab.objectType,
                		                    objectName: json.canvas.objectName,
                		                    title: json.canvas.objectName,
                		                    editable: true,
                		                    directoryId : json.canvas.directoryId,
                		                    directoryName : json.canvas.directoryName,
                		                    directoryPath : json.canvas.directoryPath,
                		                    ori: json.canvas
                		                };
                		                jQuery.iPortalTab.createTab(frameConfig);
                		                jQuery.imetabar.createDraggableObjectViewFlush();
    			            		}
    			            		jQuery.imenu.programLoading(true,null);
    			            		$.imessagebox("#ibody",json);
    			            	},
    						    error : globalError
    			            });
        					
					    }
					}
				});
			},	
			fileSavedAsXML : function(c){
				var activeTabId = jQuery.iPortalTab.activeTabId;
				if(activeTabId==null){
					$.imessagebox.showAlert("请先选择一个编辑对象！");
					return;
				}
				
				jQuery.imenu.programLoading(true,"program");
				var canvas = jQuery.iPortalTab.getCanvasInfo(activeTabId);
				var canvasStr = $.toJSON(canvas);
				var tab = jQuery.iPortalTab.I_TAB_POOL[activeTabId];
				
				$.ajax({
	                type: 'post',
	                url: 'ImetaAction!saveFileAsXML.action',
	                data: {
	                	roName : tab.objectName,
			        	roType : tab.objectType,
			        	directoryId : tab.directoryId,
	                	canvas : canvasStr
	                },
	            	dataType:"json",
	            	success : function(json){
	            		if(json.success){
	            			//打开下载页面
	            			$("#downloadFile").attr("src","ImetaAction!saveFileAsXMLDownload.action?xmlFileKey="+json.xmlFileKey);
	            		    jQuery.imenu.programLoading(false,null);
	            		}else{
	            		    $.imessagebox("#ibody",json);
	            		}
	            	},
				    error : globalError
	            });
			},	
			fileClose : function(c){
				if(jQuery.iPortalTab.activeTabId!=null){
					var activeTabId=jQuery.iPortalTab.activeTabId;
					jQuery.iPortalTab.closeTab(activeTabId);
				}
			},	
			turnOffOther : function(c){
			   jQuery.iPortalTab.closeOtherTab();
			},	
			turnOffAll : function(c){
				jQuery.iPortalTab.closeAllTab();
			}
		},
		windows : {
			imbMenu : function(c){
				var isSelected = jQuery.imenu.iMenuBtns['imbMenu'].selected;
				if(isSelected==undefined){
					isSelected=false;
				}
				jQuery.imetabar.changeBarView("imb",!isSelected);
			},	
			 ietMenu : function(c){
			   	var isSelected = jQuery.imenu.iMenuBtns['ietMenu'].selected;
				if(isSelected==undefined){
					isSelected=false;
				}
				jQuery.imetabar.changeBarView("iet",!isSelected);
			},
			icaMenu : function(c){
				var isSelected = jQuery.imenu.iMenuBtns['icaMenu'].selected;
				if(isSelected==undefined){
					isSelected=false;
				}
				jQuery.imetabar.changeBarView("ica",!isSelected);
			},
			iowMenu : function(c){
				var isSelected = jQuery.imenu.iMenuBtns['iowMenu'].selected;
				if(isSelected==undefined){
					isSelected=false;
				}
				jQuery.imetabar.changeBarView("iow",!isSelected);
			},
			iovMenu : function(c){
			   	var isSelected = jQuery.imenu.iMenuBtns['iovMenu'].selected;
				if(isSelected==undefined){
					isSelected=false;
				}
				jQuery.imenu.selectedMenu("iovMenu",!isSelected);
				jQuery.imenu.customState.iovCollapse=isSelected;
				var left = (!isSelected)?0:$("#iov").width() * (-1);
				$("#iov").animate({left:left}, "slow");
				jQuery.imenu.changePortalSize();
			}	
		},
		wizard : {
			createDatabase : function(c){
				alert("createDatabase");
			},
			copyTable : function(c) {
				alert("copyTable");
			},
			jobStart : function(c) {
				if($.iformwindow.activeWindow('jobStart')){
					return;
				}
				
				var win = $.iformwindow('#ibody',{
                    id: 'jobStart',
                    title: '作业启动管理',
                    showLoading : false
                });
				
				$.ajax({
	                type: "POST",
	                url: "ImetaBaseAction!jobStart.action",
	                dataType: "json",
	                success : function(json){
		               	win.appendContent(json);
	                }
	            });
			},
			jobStartFn : {
				btn : {
					ok : function(e,v){
						var elId = e.target.id;
						var id = elId.split(".")[0];
						jQuery.imenu.programLoading(true,"program");
						$("#"+id).ajaxSubmit({
							type:"POST",
							url:"ImetaBaseAction!settingJobStart.action",
							dataType:"json",
							data : {
								id : id
							},
							success : function(json){
								if(json.success){
									jQuery.imetabar.createDraggableObjectViewFlush();
									$("#window-jobStart").remove();
								}
								$.imessagebox("#ibody",json);
							}
						});
					},
					cancel : function(e,v){
						$("#window-jobStart").remove();
					},
					
					settingOpen : function(dirId,jobId,jobName){
						var win = $.iformwindow('#ibody',{
		                    id: 'auto_run_job_setting_'+jobId,
		                    title: '设置作业参数',
		                    showLoading : false
		                });
						
						$.ajax({
			                type: "POST",
			                url: "ImetaBaseAction!openAutoJobSetting.action",
			                dataType: "json",
			                data : {
								dirId : dirId,
								jobName : jobName
							},
			                success : function(json){
				               	win.appendContent(json);
			                }
			            });
					},
					
					settingSubmit : function(e){
						var elId = e.target.id;
						var id = elId.split(".")[0];
						var jobId = id.substring(8);
						jQuery.imenu.programLoading(true,"program");
						$("#"+id).ajaxSubmit({
							type: "post",
							url:"ImetaBaseAction!submitAutoJobSetting.action",
							dataType:"json",
							data : {
								id : id,
								jobId : jobId
							},
							success : function(json){
								jQuery.imenu.programLoading(true,null);
								$("#window-auto_run_job_setting_"+jobId).remove();
								$.imessagebox("#ibody",json);
							}
						});
						
						e.stopPropagation();
					}
				}
			}
		},
		help : {
			showStepPlugin : function(c){
				if($.iformwindow.activeWindow('stepPluginList')){
					return;
				}
				
				var win = $.iformwindow('#ibody',{
				                    id: 'stepPluginList',
				                    title: '步骤插件列表',
				                    showLoading : true
				                });
				
				$.ajax({
	                type: "POST",
	                url: "ImetaBaseAction!stepPluginList.action",
	                dataType: "json",
	                success : function(json){
		               	win.appendContent(json);
	                }
	            });
			},
			showEntryPlugin : function(c){
				if($.iformwindow.activeWindow('entryPluginList')){
					return;
				}
				
				var win = $.iformwindow('#ibody',{
				                    id: 'entryPluginList',
				                    title: '作业项插件列表',
				                    showLoading : true
				                });
				
				$.ajax({
	                type: "POST",
	                url: "ImetaBaseAction!entryPluginList.action",
	                dataType: "json",
	                success : function(json){
		               	win.appendContent(json);
	                }
	            });
			},
			about : function(c){
				if($.iformwindow.activeWindow('entryPluginList')){
					return;
				}
				$.ajax({
	                type: "POST",
	                url: "ImetaBaseAction!helpAbout.action",
	                success : function(data){
		               	$.imessagebox.showAlert(data);
	                }
	            });
			}
		}
	},

	//单击事件
	initMenu : function(){
		$.ajax({
            type: "POST",
            url: "ImetaScurityAction!toCurrentUserLevel.action",
            success: function(text){
				
				jQuery.imenu.userType = text;
			
				if(text == "operator"){
					$.each(jQuery.imenu.initMenuBtns.operator,function(e,v){
						jQuery.imenu.iMenuBtns[v].enabled=false;
					});
					jQuery.imenu.iMenuBtns['imbMenu'].selected=false;
					jQuery.imetabar.changeBarView("imb",false);
				}else if(text == "editor"){
					$.each(jQuery.imenu.initMenuBtns.editor,function(e,v){
						jQuery.imenu.iMenuBtns[v].enabled=false;
					});
				}
				
	           	//e---value
	            //v---button 
	       	    $.each(jQuery.imenu.iMenuBtns,function(e,v){
	       	     	eval("$('#"+e+"').unbind();");
	       			if(v.enabled!=false){
	       				jQuery.imenu.enabledMenu(e);
	                    	if(v&&v.btnFn){
	                            try{
	                           	 eval("$('#"+e+"').bind('click',"+v.btnFn+")");
	                           	 eval("$('#"+e+"').mouseover(jQuery.imenu.mouseoverFn);");
	                           	 eval("$('#"+e+"').mouseout(jQuery.imenu.mouseoutFn);");
	                            }catch(ex){
	                            	 eval("$('#"+e+"').unbind();");
	                            	 jQuery.imenu.disabledMenu(e);
	                            }
	                       }
	       			} else {
	       				jQuery.imenu.disabledMenu(e);
	       			}
	       			jQuery.imenu.selectedMenu(v.value,v.selected);
	       	    });
            }
        });
		
         
	},
	
	/**
	 * 批量改变菜单状态
	 */
	batchControlMenus : function(el){
		$.each(el,function(e,v){
			jQuery.imenu.iMenuBtns[e].enabled=v;
			var menu = jQuery.imenu.iMenuBtns[e];
			eval("$('#"+e+"').unbind();");
			if(v){
				jQuery.imenu.enabledMenu(e);
             	if(menu&&menu.btnFn){
                     try{
                    	 eval("$('#"+e+"').bind('click',"+menu.btnFn+");");
                    	 eval("$('#"+e+"').mouseover(jQuery.imenu.mouseoverFn);");
                    	 eval("$('#"+e+"').mouseout(jQuery.imenu.mouseoutFn);");
                     }catch(ex){
                     	 eval("$('#"+e+"').unbind();");
                     	 jQuery.imenu.disabledMenu(e);
                     }
                }
			} else {
				jQuery.imenu.disabledMenu(e);
			}
		});
		$(".x-btn-over").removeClass("x-btn-over");
	},
	
	mouseoverFn : function(e){
		if(e.target.id!=null&&e.target.id!=''){
			$("#"+e.target.id+"-table").addClass("x-btn-over");
		}
	},
	
	mouseoutFn : function(e){
		if(e.target.id!=null&&e.target.id!=''){
			$("#"+e.target.id+"-table").removeClass("x-btn-over");
		}
	},
	
	selectedMenu : function(e,v) {
		try{
			jQuery.imenu.iMenuBtns[e].selected=v;
			if(v){
				$("#"+e+"-table").addClass("x-btn-click");
			}else{
				$("#"+e+"-table").removeClass("x-btn-click");
			}
		}catch(ex){
			
		}
	},
	
	disabledMenu : function(id){
		var m = $('#'+id);
		m.attr("disabled","true"); 
		m.css("cursor","auto");
		m.css("opacity","0.4");
	},
	
	enabledMenu : function(id){
		var m = $('#'+id);
		m.attr("disabled",""); 
		m.css("cursor","pointer");
		m.css("opacity","1");
	},
	
	
	/**
	 * 组元素以正常表格显示
	 */
	appendContentTableNormal : function(c, i, areaId){
		var cTable = $("<table cellspacing='0' class='x-table-layout'></table>").addClass(areaId);
		var cTr = $("<tr></tr>").appendTo(cTable).addClass(areaId);
		
		$.each(i, function(k,v){
			var btnId = v.value;
			var btnLabel = v.label;
			var btnImgCls = v.imgCls;
			var btnWidth = v.width;
			var cTd = $("<td></td>").appendTo(cTr).addClass(areaId);
			var mTable = $("<table id='"+btnId+"-table' class='x-btn' cellspacing='0' style='width:auto;'></table>").appendTo(cTd).addClass(areaId);
			// 第一行
			var mTr1 = $("<tr></tr>").appendTo(mTable);
			var mTr1Td1 = $("<td class='x-btn-tl'></td>").appendTo(mTr1).addClass(areaId);
			$("<i></i>").appendTo(mTr1Td1).addClass(areaId);
			var mTr1Td2 = $("<td class='x-btn-tc'></td>").appendTo(mTr1).addClass(areaId);
			var mTr1Td3 = $("<td class='x-btn-tr'></td>").appendTo(mTr1).addClass(areaId);
			$("<i></i>").appendTo(mTr1Td3).addClass(areaId);
			// 第二行
			var mTr2 = $("<tr></tr>").appendTo(mTable).addClass(areaId);
			var mTr2Td1 = $("<td class='x-btn-ml'></td>").appendTo(mTr2).addClass(areaId);
			$("<i></i>").appendTo(mTr2Td1);
			var mTr2Td2 = $("<td class='x-btn-mc'></td>").appendTo(mTr2).addClass(areaId);
			
			var em = $("<em></em>").appendTo(mTr2Td2).addClass(areaId);
			var btn = $("<button class='x-btn-text' id='"+btnId+"-btn'></button>").appendTo(em).addClass(areaId);
			
			// 如果是IE
			if ($.browser.msie){
				btn.css({
					"filter":"progid:DXImageTransform.Microsoft.AlphaImageLoader(enabled=true, sizingMethod=crop, src='"+$path+"/imeta/images/imeta/menuBtn/"+btnImgCls+".png') !important",
					'width':'32px',
					'height':'32px',
					'background':'#eee'
				});
			}else{
				btn.addClass(btnImgCls);
			}
			btn.attr({
				id : btnId
			});
			btn.css("width", btnWidth);
			btn.html(btnLabel);
			// TODO 处理当disabled为false时不可操作
			
			var mTr2Td3 = $("<td class='x-btn-mr'></td>").appendTo(mTr2).addClass(areaId);
			$("<i></i>").appendTo(mTr2Td3).addClass(areaId);
			// 第三行
			var mTr3 = $("<tr></tr>").appendTo(mTable);
			var mTr3Td1 = $("<td class='x-btn-bl'></td>").appendTo(mTr3).addClass(areaId);
			$("<i></i>").appendTo(mTr3Td1).addClass(areaId);
			var mTr3Td2 = $("<td class='x-btn-bc'></td>").appendTo(mTr3).addClass(areaId);
			var mTr3Td3 = $("<td class='x-btn-br'></td>").appendTo(mTr3).addClass(areaId);
			$("<i></i>").appendTo(mTr3Td3).addClass(areaId);
			
			
			// 将按钮加入iMenuBtns
			eval("$.extend(jQuery.imenu.iMenuBtns, {"+btnId+":v});");
		});
		cTable.appendTo(c);
	},
	
	drawMenu : function(item, theObj){
		if(item.type == 'menu'){
			if(item.attr!=null && item.attr.label!=null){
				theObj.html(item.attr.label);
			}
		}
		if(item.topMenus){
			// 如果是IE
			if ($.browser.msie){
				theObj.css("top","2px");
			}
			$.each(item.topMenus, function(k, v){
				var span = $("<span style='margin-left:5px;'></span>").appendTo(theObj);
				var icon = $("<button></button>").appendTo(span);
				icon.attr({
					id : v.value,
					title : v.title
				});
				// 如果是IE
				if ($.browser.msie){
					icon.css({
						"filter":"progid:DXImageTransform.Microsoft.AlphaImageLoader(enabled=true, sizingMethod=scale, src='"+$path+"/imeta/images/imeta/menuBtn/"+v.imgCls+".png') !important;",
						"width" : "16px",
						"height" : "16px",
						"background":"none"
					});
				}else{
					icon.addClass(v.imgCls);
				}
				
				// 将按钮加入iMenuBtns
				eval("$.extend(jQuery.imenu.iMenuBtns, {"+v.value+":v});");
			});
		}
		if(item.menuArea){
			var areaId = item.menuArea.attr.id;
			var g = item.menuArea.menuGroups;
			if(g && g.length>0){
				// 建立组之前
				var menuAreaDiv = $("<div></div>").addClass(areaId);
				var menuGroupsTable = $("<table cellspacing='0'></table>").appendTo(menuAreaDiv);
				var menuGroupsTr = $("<tr></tr>").appendTo(menuGroupsTable);
				
				// 遍历组
				$.each(g, function(k, v){
					var gId = v.attr.id;
					var gName = v.attr.label;
					var gWidth = v.attr.width;
					var menuGroupsTd = $("<td></td>").appendTo(menuGroupsTr);
					
					// layer 1
					var gDiv = $("<div class='x-btn-group'></div>").appendTo(menuGroupsTd).addClass(areaId);
					gDiv.attr({
						id : gId
					});
					
					// layer 1-1
					var gTlDiv = $("<div class='x-btn-group-tl'></div>").appendTo(gDiv).addClass(areaId);
					var gTrDiv = $("<div class='x-btn-group-tr'></div>").appendTo(gTlDiv).addClass(areaId);
					var gTcDiv = $("<div class='x-btn-group-tc'></div>").appendTo(gTrDiv).addClass(areaId);
					var gHeaderDiv = $("<div class='x-btn-group-header x-unselectable'></div>").appendTo(gTcDiv).addClass(areaId);
					var gHeaderSpan = $("<span class='x-btn-group-header-text'></span>").appendTo(gHeaderDiv);
					gHeaderSpan.attr({
						id : gId+"-header"
					});
					gHeaderSpan.html(gName);
					
					// layer 1-2
					var gWrapDiv = $("<div class='x-btn-group-bwrap'></div>").appendTo(gDiv).addClass(areaId);
					// layer 1-2-1
					var gMlDiv = $("<div class='x-btn-group-ml'></div>").appendTo(gWrapDiv).addClass(areaId);
					var gMrDiv = $("<div class='x-btn-group-mr'></div>").appendTo(gMlDiv).addClass(areaId);
					var gMcDiv = $("<div class='x-btn-group-mc'></div>").appendTo(gMrDiv).addClass(areaId);
					var gBodyDiv = $("<div class='x-btn-group-body x-table-layout-ct'></div>").appendTo(gMcDiv).addClass(areaId);
					gBodyDiv.attr({
						id : gId+"-body"
					});
					gBodyDiv.css("width",gWidth);
					// 加入实际内容的table
					jQuery.imenu.appendContentTableNormal(gBodyDiv,v.menuItems, areaId);
					
					// layer 1-2-2
					var gBlDiv = $("<div class='x-btn-group-bl'></div>").appendTo(gWrapDiv).addClass(areaId);
					var gBrDiv = $("<div class='x-btn-group-br'></div>").appendTo(gBlDiv).addClass(areaId);
					var gBcDiv = $("<div class='x-btn-group-bc'></div>").appendTo(gBrDiv).addClass(areaId);
				});
				
				menuAreaDiv.appendTo(theObj);
			}
		}
	}
};

(function($) {
	$.fn.extend({

				// menutab function
				menutab : function(options) {

					// Clean up peoples goddam crappy code
					// Get the user extensions and defaults
					var opts = $.extend({}, $.fn.menutab.defaults, options);
					// Content location
					if (opts.contloc == "none") {
						contloc = $(this).parent();
					} else {
						contloc = opts.contloc;
					}
					// Content location
					if (opts.tabloc == "none") {
						tabloc = $(this).parent();
					} else {
						tabloc = opts.tabloc;
					}
					// better define some stuff for safety
					var newli = "", newdiv = "";

					// Start Building Tabs
					return this.each(function(i) {
								// start developing basis
								now = $(this);
								nowid = now.attr("id");
								now.addClass("menu_tab");

								// tab maker function
								$("#" + nowid + " li").each(function(i) {
									taba = $('#' + nowid + " li q");
									$(this).addClass("removeme");
									tabcont = taba.html();
									$(".removeme q").remove();
									tabli = $('#' + nowid + " li");
									licont = tabli.html();
									$(this).remove();

									newli += "<li rel='" + nowid + "-" + i
											+ "'><a>" + licont + "</a></li>";
									newdiv += "<div id='" + nowid + "-" + i
											+  "' class='menu_space_tab'>" + tabcont + "</div>";

								});

								// Something weird to gain the location
								now.remove();
								$(tabloc).append("<ul id='" + nowid
										+ "' class='" + opts.color + "'>"
										+ newli + "</ul>");
								// Fix the ul
								// $("#"+nowid).append(newli);
								// Find the Parent then append the divs
								// var parent = $("#"+nowid).parent();

								newdiv = "<div id='"
										+ nowid
										+ "content' class='tab_cont'><div class='"
										+ opts.area + "'>" + newdiv
										+ "</div>"
										+ "<div id='"+ nowid + "-menuTabScrollerLeft' class='menu-tab-scroller-left x-unselectable'></div>"
										+ "<div id='"+ nowid + "-menuTabScrollerRight' class='menu-tab-scroller-right x-unselectable'></div>"
										+ "</div>";
								newdiv = newdiv.replace(/\/>/, ">");
								$(contloc).append(newdiv);

								// Find the default
								ndef = nowid + "-" + opts.dopen;
								ncon = nowid + "content ." + opts.area + " div";
								$('#' + ncon + "").hide();
								$('#' + ndef).show();
								// @self
								$('.' + ndef).show();
								$('#' + ndef).children("div").show();

								deftab = $('li[rel*=' + ndef + "]");

								deftab.addClass(opts.tabactive);
								deftab.children("a").addClass(opts.tabactive);

								// Seperate function to start the tabbing
								$("#" + nowid + " li").click(function() {

									here = $(this);
									nbound = here.attr("rel");

									// Make the active class / remove it from
									// others
									$("#" + nowid + " li")
											.removeClass(opts.tabactive);
									$("#" + nowid + " li a")
											.removeClass(opts.tabactive);
									here.addClass(opts.tabactive);
									here.children("a").addClass(opts.tabactive);

									// The real action! Also detirmine
									// transition
									$('#' + ncon + ':visible').hide();
									// @self
									$('.' + nbound).show();
									$('#' + nbound)
											.children("div")
											.show();
									$('#' + nbound)
											.show();

								});
								
								function menuTabScrollerLeftClick() {
									$.each($(".menu_space_tab"), function(e,v){
										if(v.style.display != 'none'){
											try{
												var left = v.style.left.split("px")[0];
												left=left-100;
												$("#"+v.id).animate({left:left}, "slow");
											}catch(e){}
										}
										return;
									});
								};
								
								function menuTabScrollerRightClick() {
									$.each($(".menu_space_tab"), function(e,v){
										if(v.style.display != 'none'){
											try{
												var left = v.style.left.split("px")[0];
												if(left==''){
													return;
												}
												if(parseInt(left)>=0){
													return;
												}
												left=(parseInt(left)+100);
												left=(left>0)?0:left;
												$("#"+v.id).animate({left:left}, "slow");
											}catch(e){}
										}
										return;
									});
								}
								
								$("#" + nowid + "-menuTabScrollerLeft").click(menuTabScrollerLeftClick);
								
								$("#" + nowid + "-menuTabScrollerRight").click(menuTabScrollerRightClick);

							});// end return each (i)
				}// end menutab
			});// end $.fn.extend

	// Defaults
	$.fn.menutab.defaults = {
		mislpace : 'none',
		color : 'menu_tab',
		area : 'menu_space',
		tabloc : 'none',
		contloc : 'none',
		dopen : 0,
		transition : 'fade',
		tabactive : 'tabactive'
	}; // end defaults

})(jQuery);// end function($)
