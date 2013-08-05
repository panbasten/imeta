jQuery.imeta = {
    parameter : {
        getfields : function(e,v,r,fname,getfieldsType){
			var elId = e.target.id;
			var roType = e.target.getAttribute("roType");
			var roName = e.target.getAttribute("roName");
			var elementName = e.target.getAttribute("elementName");
			var directoryId = e.target.getAttribute("directoryId");
			var id = elId.split(".")[0];
			if($.isNull(fname)){
			    fname = "fields";
			}
			var rowNr = $("#"+id+"_"+fname+"_gList").attr("rowNr");
			$.imessagebox("#ibody",{
				title : "问题",
				marded : true,
				icon : "question",
				type : "custom",
				message : "表中已经有"+rowNr+"行记录，如何处理这"+rowNr+"行数据？",
				btns : [{
						key : "addNew",
						text : "增加新的",
						btnWidth : 80
					},{
						key : "addAll",
						text : "增加所有",
						btnWidth : 80
					},{
						key : "clearAndAddNew",
						text : "清除并增加新的",
						btnWidth : 80
					}],
				fn : function(m){
					switch(m){
						case "addNew" :
							$.ajax({
								type : "POST",
								url: "ImetaAction!addAndUpdateParameters.action",
				                data: jQuery.cutil.objectToUrl({
								 	roType : roType,
								 	roName : roName,
								 	directoryId : directoryId,
								 	elementName : elementName,
								 	getfieldsType : (($.isNull(getfieldsType))?"":getfieldsType)
								}),
				                dataType: "json",
				                success : function(json){
				                    
				                    var rootId = id+"_"+fname;
				                    
									$.each(json.fields,function(e,v){
										var ther = [];
                						$.each(r,function(re,rv){
                						    var thef = $.extend({},rv);
                						    thef.id=rootId+"."+thef.id;
                						    if(!$.isNull(thef.text)){
                						        thef.text = v[thef.text];
                						    }
                						    ther.push(thef);
                						});
                						
                						jQuery.imetabar.createRowByHeaderWithCheck(ther,rootId);
									});
				                },
				                error : globalError
							});
							break;
						case "clearAndAddNew" : 
							var listId = id + "_"+fname+"_gList";
							$("#"+listId+" .x-grid-row").remove();
							var index=0;
							$("#"+listId).attr("rowNr",index);
						case "addAll" :
							$.ajax({
								type : "POST",
								url: "ImetaAction!addAndUpdateParameters.action",
				                data: jQuery.cutil.objectToUrl({
								 	roType : roType,
								 	roName : roName,
								 	directoryId : directoryId,
								 	elementName : elementName,
								 	getfieldsType : (($.isNull(getfieldsType))?"":getfieldsType)
								}),
				                dataType: "json",
				                success : function(json){
				                    
				                    var rootId = id+"_"+fname;
				                    
									$.each(json.fields,function(e,v){
										var ther = [];
                						$.each(r,function(re,rv){
                						    var thef = $.extend({},rv);
                						    thef.id=rootId+"."+thef.id;
                						    if(!$.isNull(thef.text)){
                						        thef.text = v[thef.text];
                						    }
                						    ther.push(thef);
                						});
                						
                						jQuery.imetabar.createRowByHeader(ther,rootId);
									});
				                },
				                error : globalError
							});
							break;
					}
				}
			});
		},
		fieldsDelete : function(c){
            var rootId = c.getAttribute("rootId");
			jQuery.imetabar.deleteRowByHeader(rootId);
	    }
    },
	job : {
		setting : {
			btn : {
    			pathBtn : function(e,v){
			    	var elId = e.target.id;
					var id = elId.split(".")[0];
					var win = $.iformwindow('#ibody',{
			            id: 'getDirectory',
			            parentId : id,
			            title: '请选择目录',
			            showLoading : true
			        });
			        $.ajax({
			            type: "POST",
			            url: "ImetaAction!getDirectory.action",
			            data: jQuery.cutil.objectToUrl({
			                id : "getDirectory",
			                customOkFn : "jQuery.imeta.job.setting.btn.pathBtnOk",
			                parentId : id
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
    			        $("[id="+parentId+".directory]").val(directoryPath);
    			    }else{
    			        $("[id="+parentId+".directory]").val("/");
    			    }
				},
				ok : function(e,v){
					var elId = e.target.id;
					var id = elId.split(".")[0];
					var winId = e.target.getAttribute("winId");
					jQuery.imenu.programLoading(true,"program");
					$("#"+id).ajaxSubmit({
						type:"POST",
						url:"ImetaAction!settingFileSubmit.action",
						dataType:"json",
						data : {
							id : id,
							roType : jQuery.iPortalTab.OBJECT_TYPE_JOB,
							roName : e.target.getAttribute("roName"),
							directoryId : e.target.getAttribute("directoryId")
						},
						success : function(json){
							if(json.success){
								jQuery.imetabar.createDraggableObjectViewFlush();
								$("#window-"+winId).remove();
							}
							$.imessagebox("#ibody",json);
						},
						error : globalError
					});
				},
				cancel : function(e,v){
					var elId = e.target.id;
					var id = elId.split(".")[0];
					var winId = e.target.getAttribute("winId");
					$("#window-"+winId).remove();
				}
			}
		}
	},
	trans : {
		analyseImpact : {
			btn : {
				closeWin : function(e,v){
					$("#window-ai_"+e.target.getAttribute("rootId")).remove();
				}
			}
		},
		check : {
			listener : {
				showSuccess : function(e,v){
					var elId = e.target.id;
					var id = elId.split(".")[0];
					if(e.target.checked){
						$("#"+id+"_warnsAndErrors_gList .x-grid-row-hidden").css("display","block");
						var index=0;
						$.each($("#"+id+"_warnsAndErrors_gList .x-grid-row .x-grid-td-numberer div.x-grid-cell-inner"),function(e,v){
							$(v).html(++index);
						});
					}else{
						$("#"+id+"_warnsAndErrors_gList .x-grid-row-hidden").css("display","none");
						var index=0;
						$.each($("#"+id+"_warnsAndErrors_gList .x-grid-row-show .x-grid-td-numberer div.x-grid-cell-inner"),function(e,v){
							$(v).html(++index);
						});
					}
				}
			},
			btn : {
				closeWin : function(e,v){
					$("#window-check_"+e.target.getAttribute("rootId")).remove();
				}
			}
		},
		setting : {
			btn : {
				ok : function(e,v){
					var elId = e.target.id;
					var id = elId.split(".")[0];
					var winId = e.target.getAttribute("winId");
					jQuery.imenu.programLoading(true,"program");
					$("#"+id).ajaxSubmit({
						type:"POST",
						url:"ImetaAction!settingFileSubmit.action",
						dataType:"json",
						data : {
							id : id,
							roType : jQuery.iPortalTab.OBJECT_TYPE_TRANS,
							roName : e.target.getAttribute("roName"),
							directoryId : e.target.getAttribute("directoryId")
						},
						success : function(json){
							if(json.success){
								jQuery.imetabar.createDraggableObjectViewFlush();
								$("#window-"+winId).remove();
							}
							$.imessagebox("#ibody",json);
						}
					});
				},
				pathBtn : function(e,v){
					var elId = e.target.id;
					var id = elId.split(".")[0];
					var win = $.iformwindow('#ibody',{
	                    id: 'getDirectory',
	                    parentId : id,
	                    title: '请选择目录',
	                    showLoading : true
	                });
			        $.ajax({
    	                type: "POST",
    	                url: "ImetaAction!getDirectory.action",
    	                data: jQuery.cutil.objectToUrl({
    	                    id : "getDirectory",
    	                    customOkFn : "jQuery.imeta.trans.setting.btn.pathBtnOk",
    	                    parentId : id
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
    			        $("[id="+parentId+".directory]").val(directoryPath);
    			    }else{
    			        $("[id="+parentId+".directory]").val("/");
    			    }
				},
				cancel : function(e,v){
					var elId = e.target.id;
					var id = elId.split(".")[0];
					var winId = e.target.getAttribute("winId");
					$("#window-"+winId).remove();
				},
				parametersAdd : function(c){
					var rootId = c.getAttribute("rootId");
					var r = [
						{ id : rootId+'.parameterId', type : 'number' , text : '' },
						{ id : rootId+'.parameter', type : 'input' , text : '' },
						{ id : rootId+'.description', type : 'input' , text : '' }
					];
					jQuery.imetabar.createRowByHeader(r,rootId);
				},
				dependenciesAdd : function(c){
					var rootId = c.getAttribute("rootId");
					var r = [
						{ id : rootId+'.dependencyId', type : 'number' , text : '' },
						{ id : rootId+'.dbconnection', type : 'input' , text : '' },
						{ id : rootId+'.dbtable', type : 'input' , text : '' },
						{ id : rootId+'.dbfield', type : 'input' , text : '' }
					];
					jQuery.imetabar.createRowByHeader(r,rootId);
				}
			}
		}
	},
	contextMenu : {
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
	},
	execute : {
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
	},
	db : {
		dbexplorer : {
			btn : {
				ok : function(e){
					alert(1);
				},
				flush : function(e){
					alert(2);
				},
				previewFirst100 : function(e){
					alert(3);
				},
				previewFirstN : function(e){
					alert(4);
				}
			},
			listeners : {
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
					var rootId = e.target.getAttribute("rootId");
					var itemValue = e.target.innerHTML;
					$("[id="+rootId+".btn.previewFirst100]").removeAttr("disabled");
					$("[id="+rootId+".btn.previewFirst100]").attr("itemValue",e.target.innerHTML);
					$("[id="+rootId+".btn.previewFirst100]").val("预览前100行["+itemValue+"]");
					
					$("[id="+rootId+".btn.previewFirstN]").removeAttr("disabled");
					$("[id="+rootId+".btn.previewFirstN]").attr("itemValue",e.target.innerHTML);
					$("[id="+rootId+".btn.previewFirstN]").val("预览前n行["+itemValue+"]");
				}
			}
		},
		dbSetting : {
			connTypeChange : function(e){
				jQuery.imeta.db.dbSetting.editFields(e.target.id,'connType');
			},
			accessChange : function(e){
				jQuery.imeta.db.dbSetting.editFields(e.target.id,'access');
			},
			
			editFields : function(elId,elType){
				var id = elId.split(".")[0];
				var dbId = id.split("_")[1];
				$.ajax({
	                type: "POST",
	                url: "ImetaAction!settingDatabaseFields.action",
	                data: jQuery.cutil.objectToUrl({
	                	databaseId : dbId,
	                	connType : $("[id="+id+".connectionType]").val(),
	                	access : $("[id="+id+".access]").val()
	                }),
	                dataType: "json",
	                success : function(json){
											var pr = $("#"+id).parent();
											$("#"+id).remove();
											$.createObjFromJson(json,$(pr),"$.iformwindow.extendContentFn");
	                }
	          	});
			},
			listeners : {
				poolParameter : {
					rowClick : function(c){
						var rootId = c.getAttribute("rootId");
						var description = c.getAttribute("description");
						$("[id="+rootId+".description]").val(description);
					}
				},
				useClusterClick : function(e,v){
					var elId = e.target.id;
					var id = elId.split(".")[0];
					if(e.target.checked){
						$("[id="+id+"_clusterParameters.btn.add]").attr("disabled",false);
						$("[id="+id+"_clusterParameters.btn.delete]").attr("disabled",false);
						$("[id="+id+"_clusterParameters.btn.add.root]").removeClass("x-item-disabled");
						$("[id="+id+"_clusterParameters.btn.delete.root]").removeClass("x-item-disabled");
						$("#"+id+"_clusterParameters_gRoot input").attr("disabled",false);
					}else{
						$("[id="+id+"_clusterParameters.btn.add]").attr("disabled",true);
						$("[id="+id+"_clusterParameters.btn.delete]").attr("disabled",true);
						$("[id="+id+"_clusterParameters.btn.add.root]").addClass("x-item-disabled");
						$("[id="+id+"_clusterParameters.btn.delete.root]").addClass("x-item-disabled");
						$("#"+id+"_clusterParameters_gRoot input").attr("disabled",true);
					}
				},
				usePoolClick : function(e,v){
					var elId = e.target.id;
					var id = elId.split(".")[0];
					if(e.target.checked){
						$("[id="+id+".initPool]").attr("disabled",false);
						$("[id="+id+".maxPool]").attr("disabled",false);
						$("#"+id+"_poolParameters_gRoot input").attr("disabled",false);
					}else{
						$("[id="+id+".initPool]").attr("disabled",true);
						$("[id="+id+".maxPool]").attr("disabled",true);
						$("#"+id+"_poolParameters_gRoot input").attr("disabled",true);
					}
				}
			},
			btn : {
				clusterAdd : function(c){
					var rootId = c.getAttribute("rootId");
					var listId = rootId + "_gList";
					var listDiv = $("<div class='x-grid-row' onmouseover='jQuery.imetabar.gridRowMouseOver(this);' onmouseout='jQuery.imetabar.gridRowMouseOut(this);' onclick='jQuery.imetabar.gridRowClick(this);'></div>");
					
					var r = [
						{ id : rootId+'.partitionId', type : 'input' , text : '', width : 50 },
						{ id : rootId+'.hostname', type : 'input' , text : '', width : 80 },
						{ id : rootId+'.port', type : 'input' , text : '', width : 60 },
						{ id : rootId+'.dbName', type : 'input' , text : '', width : 80 },
						{ id : rootId+'.username', type : 'input' , text : '', width : 80 },
						{ id : rootId+'.password', type : 'password' , text : '', width : 80 }
					];
					
					jQuery.imetabar.createRow(r,null,listDiv);
					$("#"+listId).append(listDiv);
				},
				clusterDelete : function(c){
					var rootId = c.getAttribute("rootId");
					var listId = rootId + "_gList";
					$("#"+listId+" .x-grid-row-selected").remove();
				},
				resetPoolParameters : function(e,v){
					var oldvalue;
					$(".x-grid-dirty-cell").removeClass("x-grid-dirty-cell");
					$("#database_1_poolParameters_gRoot input").each(function(i,o){
						oldvalue = o.getAttribute("oldvalue");
						if(oldvalue&&oldvalue!=null){
							o.value=oldvalue;
						}
					});
				},
				dbTest : function(e,v){
					var elId = e.target.id;
					var id = elId.split(".")[0];
					var dbId = 0;
					try{
						dbId = id.split("_")[1];
					}catch(e){}
					
					$("#"+id).ajaxSubmit({
						type: "POST",
						url:"ImetaAction!testDatabase.action",
						dataType:"json",
						data : {
							id : id,
							databaseId : dbId
						},
						success : function(json){
							$.imessagebox("#ibody",json);
						}
					});
					
					e.stopPropagation();
				},
				dbExplore : function(e,v){
					var elId = e.target.id;
					var id = elId.split(".")[0];
					var dbId = 0;
					try{
						dbId = id.split("_")[1];
					}catch(e){}
					
					if($.iformwindow.activeWindow(id+"_explorer")){
						return;
					}
					
					var win = $.iformwindow('#ibody',{
	                    id: id+"_explorer",
	                    title: "浏览数据库",
	                    showLoading : true
	                });
					
					$("#"+id).ajaxSubmit({
						url:"ImetaAction!exploreDatabase.action",
						dataType:"json",
						data : {
							id : id,
							databaseId : dbId
						},
						success : function(json){
							win.appendContent(json);
						},
						error : function(){
							$.imessagebox("#ibody",{
								title : "错误",
								marded : true,
								icon : "error",
								type : "alert",
								message : "无法浏览数据库！",
								fn : function(m){
									$("#window-"+id+"_explorer").remove();
								}
								
							});
						}
					});
					
					e.stopPropagation();
				},
				ok : function(e,v){
					var elId = e.target.id;
					var id = elId.split(".")[0];
					var dbId = 0;
					try{
						dbId = id.split("_")[1];
					}catch(e){}
					
					$("#"+id).ajaxSubmit({
						type: "POST",
						url:"ImetaAction!createOrUpdateDatabase.action",
						dataType:"json",
						data : {
							id : id,
							databaseId : dbId
						},
						success : function(json){
						    if(json.success){
						    	if(dbId == 0){
						    		$("#window-new_database").remove();
						    	}else{
						    		$("#window-"+id).remove();
						    	}
						    }
							$.imessagebox("#ibody",json);
						}
					});
					
					e.stopPropagation();
				},
				cancel : function(e,v){
					var elId = e.target.id;
					var id = elId.split(".")[0];
					var dbId = 0;
					try{
						dbId = id.split("_")[1];
					}catch(e){}
					
					if(dbId == 0){
			    		$("#window-new_database").remove();
			    	}else{
			    		$("#window-"+id).remove();
			    	}
				}
			}
		}
	},
	steps : {
		addxml : {
			btn : {
				getfields : function(e,v){
					 var r = [
                         { id : 'fieldId', type : 'number' , text : '' },
						 { id : 'fieldName', type : 'input' , text : 'fieldName'},
						 { id : 'fieldElement', type : 'input' , text : 'fieldName'},
						 { id : 'fieldType', type : 'select' , text : 'typeDesc'},
						 { id : 'fieldFormat', type : 'input' , text : ''},
						 { id : 'fieldLength', type : 'input' , text : 'length'},
						 { id : 'fieldPrecision', type : 'input' , text : 'precision'},
						 { id : 'fieldCurrency', type : 'input' , text : ''},
						 { id : 'fieldDecimal', type : 'input' , text : 'decimal' },
						 { id : 'fieldGroup', type : 'input' , text : 'group' },
						 { id : 'fieldNullif', type : 'input' , text : '' },
						 { id : 'fieldAttribute', type : 'select' , text : '' },
						 { id : 'fieldAttributeParentName', type : 'input' , text : '' }
                         ];
                       jQuery.imeta.parameter.getfields(e,v,r);	
				},
				fieldsAdd : function(c){
						var rootId = c.getAttribute("rootId");
						var r = [
							{ id : rootId+'.fieldId', type : 'number' , text : '' },
							{ id : rootId+'.fieldName', type : 'input' , text : ''},
							{ id : rootId+'.fieldElement', type : 'input' , text : ''},
							{ id : rootId+'.fieldType', type : 'select' , text : ''},
							{ id : rootId+'.fieldFormat', type : 'input' , text : ''},
							{ id : rootId+'.fieldLength', type : 'input' , text : ''},
							{ id : rootId+'.fieldPrecision', type : 'input' , text : ''},
							{ id : rootId+'.fieldCurrency', type : 'input' , text : ''},
							{ id : rootId+'.fieldDecimal', type : 'input' , text : '' },
							{ id : rootId+'.fieldGroup', type : 'input' , text : '' },
							{ id : rootId+'.fieldNullif', type : 'input' , text : '' },
							{ id : rootId+'.fieldAttribute', type : 'select' , text : '' },
							{ id : rootId+'.fieldAttributeParentName', type : 'input' , text : '' }
						];
						
						jQuery.imetabar.createRowByHeader(r,rootId);
					}
			}
		},
		accessinput : {
			btn : {
					getfields : function(e,v){
					var r = [	
						{ id : 'fieldId', type : 'number' , text : '' },
						{ id : 'fieldName', type : 'input' , text : 'fieldName' },
						{ id : 'fieldColumn', type : 'input' , text : '' },
						{ id : 'fieldType', type : 'select' , text : 'type' },
						{ id : 'fieldFormat', type : 'input' , text : ''},
						{ id : 'fieldLength', type : 'input' , text : 'length'},
						{ id : 'fieldPrecision', type : 'input' , text : 'precision' },
						{ id : 'fieldCurrency', type : 'input' , text : ''},
						{ id : 'fieldDecimal', type : 'input' , text : 'decimal' },
						{ id : 'fieldGroup', type : 'input' , text : 'group' },
						{ id : 'fieldTrimType', type : 'select' , text : '' },
						{ id : 'fieldRepeat', type : 'select' , text : '' }
					];
					jQuery.imeta.parameter.getfields(e,v,r);	
				}
			},
			selectedfiles : {
				btn : {
					fieldAdd : function(c){
						var rootId = c.getAttribute("rootId");
						var r = [
							{ id : rootId+'.fileId', type : 'number' , text : '' },
							{ id : rootId+'.fileName', type : 'input' , text : '' },
							{ id : rootId+'.fileMask', type : 'input' , text : '' }
						];
						jQuery.imetabar.createRowByHeader(r,rootId);
					}
				}
			},
			fields : {
				btn : {
					fieldAdd : function(c){
						var rootId = c.getAttribute("rootId");
						var r = [
							{ id : rootId+'.fieldId', type : 'number' , text : '' },
							{ id : rootId+'.fieldName', type : 'input' , text : '' },
							{ id : rootId+'.fieldColumn', type : 'input' , text : '' },
							{ id : rootId+'.fieldType', type : 'select' , text : '' },
							{ id : rootId+'.fieldFormat', type : 'input' , text : '' },
							{ id : rootId+'.fieldLength', type : 'input' , text : '' },
							{ id : rootId+'.fieldPrecision', type : 'input' , text : '' },
							{ id : rootId+'.fieldCurrency', type : 'input' , text : '' },
							{ id : rootId+'.fieldDecimal', type : 'input' , text : '' },
							{ id : rootId+'.fieldGroup', type : 'input' , text : '' },
							{ id : rootId+'.fieldTrimType', type : 'select' , text : '' },
							{ id : rootId+'.fieldRepeat', type : 'select' , text : '' }
						];
						jQuery.imetabar.createRowByHeader(r,rootId);
					}
				}
			},
			listeners : {
				isFileFieldListeners : function(e,v){
					var elId = e.target.id;
					var id = elId.split(".")[0];
					if(e.target.checked){
						$("[id="+id+".dynamicFilenameField]").attr("disabled",false);
						$("[id="+id+".fileOrpath]").attr("disabled",true);
						$("[id="+id+".regularExpression]").attr("disabled",true);
						$("[id="+id+".includeFilename]").attr("disabled",true);
						$("[id="+id+".rowLimit]").attr("disabled",true);
						$("[id="+id+".filenameField]").attr("disabled",true);
						$("[id="+id+".includeFilename]").attr("checked",false);
						
						$("[id="+id+"_selectedFiles.btn.add]").attr("disabled",true);
						$("[id="+id+"_selectedFiles.btn.delete]").attr("disabled",true);
						$("[id="+id+"_selectedFiles.btn.add.root]").addClass("x-item-disabled");
						$("[id="+id+"_selectedFiles.btn.delete.root]").addClass("x-item-disabled");
						$("#"+id+"_selectedFiles_gRoot input").attr("disabled",true);
					}else{
						$("[id="+id+".dynamicFilenameField]").attr("disabled",true);
						$("[id="+id+".fileOrpath]").attr("disabled",false);
						$("[id="+id+".regularExpression]").attr("disabled",false);
						$("[id="+id+".includeFilename]").attr("disabled",false);
						$("[id="+id+".rowLimit]").attr("disabled",false);
//						$("[id="+id+".filenameField]").attr("disabled",false);
						
						$("[id="+id+"_selectedFiles.btn.add]").attr("disabled",false);
						$("[id="+id+"_selectedFiles.btn.delete]").attr("disabled",false);
						$("[id="+id+"_selectedFiles.btn.add.root]").removeClass("x-item-disabled");
						$("[id="+id+"_selectedFiles.btn.delete.root]").removeClass("x-item-disabled");
						$("#"+id+"_selectedFiles_gRoot input").attr("disabled",false);
					}
				},
				includefilenameListeners : function(e,v){
					var elId = e.target.id;
					var id = elId.split(".")[0];
					if(e.target.checked){
						$("[id="+id+".filenameField]").attr("disabled",false);
					}else{
						$("[id="+id+".filenameField]").attr("disabled",true);
					}
				},
				includeTablenameListeners : function(e,v){
					var elId = e.target.id;
					var id = elId.split(".")[0];
					if(e.target.checked){
						$("[id="+id+".tablenameField]").attr("disabled",false);
					}else{
						$("[id="+id+".tablenameField]").attr("disabled",true);
					}
				},
				includeRowNumberListeners : function(e,v){
					var elId = e.target.id;
					var id = elId.split(".")[0];
					if(e.target.checked){
						$("[id="+id+".rowNumberField]").attr("disabled",false);
						$("[id="+id+".resetRowNumber]").attr("disabled",false);
					}else{
						$("[id="+id+".rowNumberField]").attr("disabled",true);
						$("[id="+id+".resetRowNumber]").attr("disabled",true);
					}
				}
			}
		},
		csvinput : {
			btn : {
					fieldAdd : function(c){
						var rootId = c.getAttribute("rootId");
						var r = [
							{ id : rootId+'.fieldId', type : 'number' , text : '' },
							{ id : rootId+'.fieldName', type : 'input' , text : '' },
							{ id : rootId+'.fieldType', type : 'select' , text : '' },
							{ id : rootId+'.fieldFormat', type : 'input' , text : '' },
							{ id : rootId+'.fieldLength', type : 'input' , text : ''},
							{ id : rootId+'.fieldPrecision', type : 'input' , text : '' },
							{ id : rootId+'.fieldCurrency', type : 'input' , text : '' },
							{ id : rootId+'.fieldDecimal', type : 'input' , text : '' },
							{ id : rootId+'.fieldGroup', type : 'input' , text : '' },
							{ id : rootId+'.fieldTrimType', type : 'select' , text : '' }
						];
						
						jQuery.imetabar.createRowByHeader(r,rootId);
					},
					getfields : function(e,v){
						var r = [
							{ id : 'fieldId', type : 'number' , text : '' },
							{ id : 'fieldName', type : 'input' , text : 'fieldName' },
							{ id : 'fieldType', type : 'select' , text : 'type' },
							{ id : 'fieldFormat', type : 'input' , text : '' },
							{ id : 'fieldLength', type : 'input' , text : 'length' },
							{ id : 'fieldPrecision', type : 'input' , text : 'precision' },
							{ id : 'fieldCurrency', type : 'input' , text : '' },
							{ id : 'fieldDecimal', type : 'input' , text : '' },
							{ id : 'fieldGroup', type : 'input' , text : 'group' },
							{ id : 'fieldTrimType', type : 'select' , text : '' }
						];
						jQuery.imeta.parameter.getfields(e,v,r);
					}
			}
		},
		excelinput : {
			btn : {
				getsheetsname : function(e,v){
					var elId = e.target.id;
					var id = elId.split(".")[0];
					if($.iformwindow.activeWindow(id+"_explorer")){
						return;
					}
					
					var win = $.iformwindow('#ibody',{
	                    id: id+"_explorer",
	                    title: "输入列表",
	                    showLoading : true
	                });
				},
				getfields : function(e,v){		
					var r = [	
						{ id : 'fieldId', type : 'number' , text : '' },
						{ id : 'fieldName', type : 'input' , text : 'fieldName' },
						{ id : 'fieldType', type : 'select' , text : 'type' },
						{ id : 'fieldLength', type : 'input' , text : 'length' },
						{ id : 'fieldPrecision', type : 'input' , text : 'precision' },
						{ id : 'fieldTrimType', type : 'select' , text : '' },
						{ id : 'fieldRepeat', type : 'select' , text : '' },
						{ id : 'fieldFormat', type : 'input' , text : '' },
						{ id : 'fieldCurrency', type : 'input' , text : '' },
						{ id : 'fieldDecimal', type : 'input' , text : 'decimal' },
						{ id : 'fieldGroup', type : 'input' , text : 'group' }
					];
					jQuery.imeta.parameter.getfields(e,v,r);	
				}
			},
			selectedfiles :{
				btn : {
					fieldAdd : function(c){
						var rootId = c.getAttribute("rootId");
						var r = [
							{ id : rootId+'.fileId', type : 'number' , text : '' },
							{ id : rootId+'.fileName', type : 'input' , text : '' },
							{ id : rootId+'.fileMask', type : 'input' , text : '' },
							{ id : rootId+'.fileRequired', type : 'input' , text : '' }
						];
						jQuery.imetabar.createRowByHeader(r,rootId);
					}
				}
			},
			selectedsheets : {
				btn : {
					fieldAdd : function(c){
						var rootId = c.getAttribute("rootId");
						var r = [
							{ id : rootId+'.sheetId', type : 'number' , text : '' },
							{ id : rootId+'.sheetName', type : 'input' , text : '' },
							{ id : rootId+'.startRow', type : 'input' , text : '' },
							{ id : rootId+'.startColumn', type : 'input' , text : '' }
						];
						
						jQuery.imetabar.createRowByHeader(r,rootId);
					}
				}
			},
			fields : {
				btn : {
					fieldAdd : function(c){
						var rootId = c.getAttribute("rootId");
						var r = [
							{ id : rootId+'.fieldId', type : 'number' , text : '' },
							{ id : rootId+'.fieldName', type : 'input' , text : '' },
							{ id : rootId+'.fieldType', type : 'select' , text : '' },
							{ id : rootId+'.fieldLength', type : 'input' , text : '' },
							{ id : rootId+'.fieldPrecision', type : 'input' , text : '' },
							{ id : rootId+'.fieldTrimType', type : 'select' , text : '' },
							{ id : rootId+'.fieldRepeat', type : 'select' , text : '' },
							{ id : rootId+'.fieldFormat', type : 'input' , text : '' },
							{ id : rootId+'.fieldCurrency', type : 'input' , text : '' },
							{ id : rootId+'.fieldDecimal', type : 'input' , text : '' },
							{ id : rootId+'.fieldGroup', type : 'input' , text : '' }
						];
						
						jQuery.imetabar.createRowByHeader(r,rootId);
					}
				}
			},	
			listeners : {
				getFilesNameListeners : function(e,v){
					var elId = e.target.id;
					var id = elId.split(".")[0];
					if(e.target.checked){
						$("[id="+id+".stepFilesName]").attr("disabled",false);
						$("[id="+id+".stepFieldFilesName]").attr("disabled",false);
						$("[id="+id+".fileOrDir]").attr("disabled",true);
						$("[id="+id+".ruleExpression]").attr("disabled",true);
						//点击后，表格屏蔽选中文件的按钮和包含文件的字段名
						$("[id="+id+"_selectedFiles.btn.add]").attr("disabled",true);
						$("[id="+id+"_selectedFiles.btn.delete]").attr("disabled",true);
						$("[id="+id+"_selectedFiles.btn.add.root]").addClass("x-item-disabled");
						$("[id="+id+"_selectedFiles.btn.delete.root]").addClass("x-item-disabled");
						$("#"+id+"_selectedFiles_gRoot input").attr("disabled",true);
					}else{
						$("[id="+id+".stepFilesName]").attr("disabled",true);
						$("[id="+id+".stepFieldFilesName]").attr("disabled",true);
						$("[id="+id+".fileOrDir]").attr("disabled",false);
						$("[id="+id+".ruleExpression]").attr("disabled",false);
						//点击不选中后，表格显示选中文件的按钮和包含文件的字段名
						$("[id="+id+"_selectedFiles.btn.add]").attr("disabled",false);
						$("[id="+id+"_selectedFiles.btn.delete]").attr("disabled",false);
						$("[id="+id+"_selectedFiles.btn.add.root]").removeClass("x-item-disabled");
						$("[id="+id+"_selectedFiles.btn.delete.root]").removeClass("x-item-disabled");
						$("#"+id+"_selectedFiles_gRoot input").attr("disabled",false);
					}
				},
				errorIgnoredListeners : function(e,v){
					var elId = e.target.id;
					var id = elId.split(".")[0];
					if(e.target.checked){
						$("[id="+id+".errorLineSkipped]").attr("disabled",false);
						$("[id="+id+".warningFilesDestinationDirectory]").attr("disabled",false);
						$("[id="+id+".warningFilesExtension]").attr("disabled",false);
						$("[id="+id+".errorFilesDestinationDirectory]").attr("disabled",false);
						$("[id="+id+".errorFilesExtension]").attr("disabled",false);
						$("[id="+id+".lineNumberFilesDestinationDirectory]").attr("disabled",false);
						$("[id="+id+".lineNumberFilesExtension]").attr("disabled",false);
					}else{
						$("[id="+id+".errorLineSkipped]").attr("disabled",true);
						$("[id="+id+".warningFilesDestinationDirectory]").attr("disabled",true);
						$("[id="+id+".warningFilesExtension]").attr("disabled",true);
						$("[id="+id+".errorFilesDestinationDirectory]").attr("disabled",true);
						$("[id="+id+".errorFilesExtension]").attr("disabled",true);
						$("[id="+id+".lineNumberFilesDestinationDirectory]").attr("disabled",true);
						$("[id="+id+".lineNumberFilesExtension]").attr("disabled",true);
					}
				}
			}
		},
		fixedinput : {
			btn : {
				fieldAdd : function(c){
					var rootId = c.getAttribute("rootId");
					var r = [
						{ id : rootId+'.fieldId', type : 'number' , text : '' },
						{ id : rootId+'.fieldName', type : 'input' , text : '' },
						{ id : rootId+'.fieldType', type : 'select' , text : '' },
						{ id : rootId+'.fieldFormat', type : 'input' , text : '' },
						{ id : rootId+'.fieldWidth', type : 'input' , text : '' },
						{ id : rootId+'.fieldLength', type : 'input' , text : '' },
						{ id : rootId+'.fieldPrecision', type : 'input' , text : '' },
						{ id : rootId+'.fieldCurrency', type : 'input' , text : '' },
						{ id : rootId+'.fieldDecimal', type : 'input' , text : '' },
						{ id : rootId+'.fieldGroup', type : 'input' , text : '' },
						{ id : rootId+'.fieldTrimType', type : 'select' , text : '' }
					];
					
					jQuery.imetabar.createRowByHeader(r,rootId);
				},
				getfields : function(e,v){
					var r = [
						{ id : 'fieldId', type : 'number' , text : '' },
						{ id : 'fieldName', type : 'input' , text : 'fieldName' },
						{ id : 'fieldType', type : 'select' , text : 'type' },
						{ id : 'fieldFormat', type : 'input' , text : '' },
						{ id : 'fieldWidth', type : 'input' , text : '' },
						{ id : 'fieldLength', type : 'input' , text : 'length' },
						{ id : 'fieldPrecision', type : 'input' , text : 'precision' },
						{ id : 'fieldCurrency', type : 'input' , text : '' },
						{ id : 'fieldDecimal', type : 'input' , text : '' },
						{ id : 'fieldGroup', type : 'input' , text : 'group' },
						{ id : 'fieldTrimType', type : 'select' , text : '' }
					];
					jQuery.imeta.parameter.getfields(e,v,r);
				}
			},
			listeners : {
				runningListeners : function(e,v){
					var elId = e.target.id;
					var id = elId.split(".")[0];
					if(e.target.checked){
						$("[id="+id+".fileType]").attr("disabled",false);
					}else{
						$("[id="+id+".fileType]").attr("disabled",true);
					}
				}
			}
		},
		randomvalue : {
			btn : {
					fieldAdd : function(c){
						var rootId = c.getAttribute("rootId");
						var r = [
							{ id : rootId+'.fieldId', type : 'number' , text : '' },
							{ id : rootId+'.fieldName', type : 'input' , text : '' },
							{ id : rootId+'.fieldType', type : 'select' , text : '' }
						];
						jQuery.imetabar.createRowByHeader(r,rootId);
					}
			}
		},
		GetFileNames : {
			btn : {
				fieldAdd : function(c){
					var rootId = c.getAttribute("rootId");
					var r = [
						{ id : rootId+'.fileId', type : 'number' , text : '' },
						{ id : rootId+'.fileName', type : 'input' , text : '' },
						{ id : rootId+'.fileMask', type : 'input' , text : '' },
						{ id : rootId+'.fileRequired', type : 'input' , text : '' }
					];
					jQuery.imetabar.createRowByHeader(r,rootId);
				}
			},
			listeners : {
				filenameDefinedListeners : function(e,v){
					var elId = e.target.id;
					var id = elId.split(".")[0];
					if(e.target.checked){
						$("[id="+id+".dynamicFilenameField]").attr("disabled",false);
						$("[id="+id+".dynamicWildcardField]").attr("disabled",false);
						$("[id="+id+".fileOrcontents]").attr("disabled",true);
// 						$("[id="+id+".btn.fileOrcontentsAdd]").attr("disabled",true);
// 						$("[id="+id+".btn.fileOrcontentsSelecter]").attr("disabled",true);
						$("[id="+id+".regularExpression]").attr("disabled",true);
//						$("[id="+id+".showFilesName]").attr("disabled",true);
// 						$("[id="+id+".deleteBtn]").attr("disabled",true);
//						$("[id="+id+".editBtn]").attr("disabled",true);
						$("[id="+id+".rowLimit]").attr("disabled",true);
						
						//点击后，表格屏蔽选中文件的按钮和包含文件的字段名
						$("[id="+id+"_selectedFiles.btn.add]").attr("disabled",true);
						$("[id="+id+"_selectedFiles.btn.delete]").attr("disabled",true);
						$("[id="+id+"_selectedFiles.btn.add.root]").addClass("x-item-disabled");
						$("[id="+id+"_selectedFiles.btn.delete.root]").addClass("x-item-disabled");
						$("#"+id+"_selectedFiles_gRoot input").attr("disabled",true);
					}else{
						$("[id="+id+".dynamicFilenameField]").attr("disabled",true);
						$("[id="+id+".dynamicWildcardField]").attr("disabled",true);
						$("[id="+id+".fileOrcontents]").attr("disabled",false);
// 						$("[id="+id+".btn.fileOrcontentsAdd]").attr("disabled",false);
// 						$("[id="+id+".btn.fileOrcontentsSelecter]").attr("disabled",false);
						$("[id="+id+".regularExpression]").attr("disabled",false);
// 						$("[id="+id+".showFilesName]").attr("disabled",false);
// 						$("[id="+id+".deleteBtn]").attr("disabled",false);
// 						$("[id="+id+".editBtn]").attr("disabled",false);
						$("[id="+id+".rowLimit]").attr("disabled",false);
						
						//点击不选中后，表格显示选中文件的按钮和包含文件的字段名
						$("[id="+id+"_selectedFiles.btn.add]").attr("disabled",false);
						$("[id="+id+"_selectedFiles.btn.delete]").attr("disabled",false);
						$("[id="+id+"_selectedFiles.btn.add.root]").removeClass("x-item-disabled");
						$("[id="+id+"_selectedFiles.btn.delete.root]").removeClass("x-item-disabled");
						$("#"+id+"_selectedFiles_gRoot input").attr("disabled",false);
					}
				}
			}
		},
		getfilesrowscount : {
			btn : {
					fieldAdd : function(c){
						var rootId = c.getAttribute("rootId");
						var r = [
							{ id : rootId+'.selectedFilesId', type : 'number' , text : '' },
							{ id : rootId+'.fileName', type : 'input' , text : '' },
							{ id : rootId+'.fileMask', type : 'input' , text : '' }
						];
						jQuery.imetabar.createRowByHeader(r,rootId);
					}
			},
			listeners : {
				RowSeparatorChange : function(e){
					var elId = e.target.id;
					var id = elId.split(".")[0];
					if(this.value == 'CUSTOM'){
						$("[id="+id+".RowSeparator]").attr("disabled",false);
					}else{
						$("[id="+id+".RowSeparator]").attr("disabled",true);
					}
				},
				getFilenamefieldListeners : function(e,v){
					var elId = e.target.id;
					var id = elId.split(".")[0];
					if(e.target.checked){
						$("[id="+id+".filenameFromfield]").attr("disabled",false);
						$("[id="+id+".fileOrdirectory]").attr("disabled",true);
//						$("[id="+id+".btn.fileOrdirectoryAdd]").attr("disabled",true);
//						$("[id="+id+".btn.fileOrdirectorySelecter]").attr("disabled",true);
						$("[id="+id+".regularExpression]").attr("disabled",true);
						$("[id="+id+".showFilesName]").attr("disabled",true);
//					 	$("[id="+id+".deleteBtn]").attr("disabled",true);
//					 	$("[id="+id+".editBtn]").attr("disabled",true);
						
						//点击后，表格屏蔽选中文件的按钮和包含文件的字段名
						$("[id="+id+"_selectedFiles.btn.add]").attr("disabled",true);
						$("[id="+id+"_selectedFiles.btn.delete]").attr("disabled",true);
						$("[id="+id+"_selectedFiles.btn.add.root]").addClass("x-item-disabled");
						$("[id="+id+"_selectedFiles.btn.delete.root]").addClass("x-item-disabled");
						$("#"+id+"_selectedFiles_gRoot input").attr("disabled",true);
					}else{
						$("[id="+id+".filenameFromfield]").attr("disabled",true);
						$("[id="+id+".fileOrdirectory]").attr("disabled",false);
// 						$("[id="+id+".btn.fileOrdirectoryAdd]").attr("disabled",false);
// 						$("[id="+id+".btn.fileOrdirectorySelecter]").attr("disabled",false);
						$("[id="+id+".regularExpression]").attr("disabled",false);
						$("[id="+id+".showFilesName]").attr("disabled",false);
// 						$("[id="+id+".deleteBtn]").attr("disabled",false);
// 						$("[id="+id+".editBtn]").attr("disabled",false);
						
						//点击不选中后，表格显示选中文件的按钮和包含文件的字段名
						$("[id="+id+"_selectedFiles.btn.add]").attr("disabled",false);
						$("[id="+id+"_selectedFiles.btn.delete]").attr("disabled",false);
						$("[id="+id+"_selectedFiles.btn.add.root]").removeClass("x-item-disabled");
						$("[id="+id+"_selectedFiles.btn.delete.root]").removeClass("x-item-disabled");
						$("#"+id+"_selectedFiles_gRoot input").attr("disabled",false);
					}
				},
				includeFilesCountListeners : function(e,v){
					var elId = e.target.id;
					var id = elId.split(".")[0];
					if(e.target.checked){
						$("[id="+id+".filesCountFieldName]").attr("disabled",false);
					}else{
						$("[id="+id+".filesCountFieldName]").attr("disabled",true);
					}
				}
			}
		},
		getxmldata : {
			btn : {
				getfields : function(e,v){
					var r = [
						{ id : 'fieldId', type : 'number' , text : '' },
						{ id : 'fieldName', type : 'input' , text : 'fieldName' },
						{ id : 'fieldXpath', type : 'input' , text : '' },
						{ id : 'fieldElement', type : 'select' , text : 'element' },
						{ id : 'fieldType', type : 'select' , text : 'type' },
						{ id : 'fieldFormat', type : 'input' , text : '' },
						{ id : 'fieldLength', type : 'input' , text : 'length' },
						{ id : 'fieldPrecision', type : 'input' , text : 'precision' },
						{ id : 'fieldCurrency', type : 'input' , text : '' },
						{ id : 'fieldDecimal', type : 'input' , text : 'decimal' },
						{ id : 'fieldGroup', type : 'input' , text : 'group' },
						{ id : 'fieldTrimType', type : 'select' , text : '' },
						{ id : 'fieldRepeat', type : 'select' , text : 'repeat' }
					];
					jQuery.imeta.parameter.getfields(e,v,r);	
				}
			},
			selectedfiles : {
				btn : {
					fieldAdd : function(c){
						var rootId = c.getAttribute("rootId");
						var r = [
							{ id : rootId+'.fileId', type : 'number' , text : '' },
							{ id : rootId+'.fileName', type : 'input' , text : '' },
							{ id : rootId+'.fileMask', type : 'input' , text : '' },
							{ id : rootId+'.fileRequired', type : 'input' , text : '' }
						];
						jQuery.imetabar.createRowByHeader(r,rootId);
					}
				}
			},
			fields : {
				btn : {
					fieldAdd : function(c){
						var rootId = c.getAttribute("rootId");
						var r = [
							{ id : rootId+'.fieldId', type : 'number' , text : '' },
							{ id : rootId+'.fieldName', type : 'input' , text : '' },
							{ id : rootId+'.fieldXpath', type : 'input' , text : '' },
							{ id : rootId+'.fieldElement', type : 'select' , text : '' },
							{ id : rootId+'.fieldType', type : 'select' , text : '' },
							{ id : rootId+'.fieldFormat', type : 'input' , text : '' },
							{ id : rootId+'.fieldLength', type : 'input' , text : '' },
							{ id : rootId+'.fieldPrecision', type : 'input' , text : '' },
							{ id : rootId+'.fieldCurrency', type : 'input' , text : '' },
							{ id : rootId+'.fieldDecimal', type : 'input' , text : '' },
							{ id : rootId+'.fieldGroup', type : 'input' , text : '' },
							{ id : rootId+'.fieldTrimType', type : 'select' , text : '' },
							{ id : rootId+'.fieldRepeat', type : 'select' , text : '' }
						];
						jQuery.imetabar.createRowByHeader(r,rootId);
					}
				}
			},
			listeners : {
				IsInFieldsListeners : function(e,v){
					var elId = e.target.id;
					var id = elId.split(".")[0];
					if(e.target.checked){
						$("[id="+id+".IsAFile]").attr("disabled",false);
						$("[id="+id+".readurl]").attr("disabled",false);
						$("[id="+id+".XmlField]").attr("disabled",false);	
						$("[id="+id+".fileOrdirectory]").attr("disabled",true);
// 						$("[id="+id+".btn.fileOrdirectoryAdd]").attr("disabled",true);
//	 					$("[id="+id+".btn.fileOrdirectorySelecter]").attr("disabled",true);
						$("[id="+id+".regularExpression]").attr("disabled",true);
// 						$("[id="+id+".delete]").attr("disabled",true);
// 						$("[id="+id+".edit]").attr("disabled",true);
// 						$("[id="+id+".showFilename]").attr("disabled",true);
						$("[id="+id+".encoding]").attr("disabled",true);
						$("[id="+id+".rowLimit]").attr("disabled",true);
						$("[id="+id+".prunePath]").attr("disabled",true);
						$("[id="+id+".includeFilename]").attr("disabled",true);
						$("[id="+id+".filenameField]").attr("disabled",true);
						//选中后includeFilename被置为未选中
						$("[id="+id+".includeFilename]").attr("checked",false);
						//点击后，表格屏蔽选中文件的按钮和包含文件的字段名
						$("[id="+id+"_selectedFiles.btn.add]").attr("disabled",true);
						$("[id="+id+"_selectedFiles.btn.delete]").attr("disabled",true);
						$("[id="+id+"_selectedFiles.btn.add.root]").addClass("x-item-disabled");
						$("[id="+id+"_selectedFiles.btn.delete.root]").addClass("x-item-disabled");
						$("#"+id+"_selectedFiles_gRoot input").attr("disabled",true);
					}else{
						$("[id="+id+".IsAFile]").attr("disabled",true);
						$("[id="+id+".readurl]").attr("disabled",true);
						$("[id="+id+".XmlField]").attr("disabled",true);	
						$("[id="+id+".fileOrdirectory]").attr("disabled",false);
// 						$("[id="+id+".btn.fileOrdirectoryAdd]").attr("disabled",false);
//						$("[id="+id+".btn.fileOrdirectorySelecter]").attr("disabled",false);
						$("[id="+id+".regularExpression]").attr("disabled",false);
//						$("[id="+id+".delete]").attr("disabled",false);
//						$("[id="+id+".edit]").attr("disabled",false);
//						$("[id="+id+".showFilename]").attr("disabled",false);
						$("[id="+id+".encoding]").attr("disabled",false);
						$("[id="+id+".rowLimit]").attr("disabled",false);
						$("[id="+id+".prunePath]").attr("disabled",false);
						$("[id="+id+".includeFilename]").attr("disabled",false);
						
						//点击不选中后，表格显示选中文件的按钮和包含文件的字段名
						$("[id="+id+"_selectedFiles.btn.add]").attr("disabled",false);
						$("[id="+id+"_selectedFiles.btn.delete]").attr("disabled",false);
						$("[id="+id+"_selectedFiles.btn.add.root]").removeClass("x-item-disabled");
						$("[id="+id+"_selectedFiles.btn.delete.root]").removeClass("x-item-disabled");
						$("#"+id+"_selectedFiles_gRoot input").attr("disabled",false);
					}
				},
				includeFilenameListeners : function(e,v){
					var elId = e.target.id;
					var id = elId.split(".")[0];
					if(e.target.checked){
						$("[id="+id+".filenameField]").attr("disabled",false);
					}else{
						$("[id="+id+".filenameField]").attr("disabled",true);
					}
				},
				includeRowNumberListeners : function(e,v){
					var elId = e.target.id;
					var id = elId.split(".")[0];
					if(e.target.checked){
						$("[id="+id+".rowNumberField]").attr("disabled",false);
					}else{
						$("[id="+id+".rowNumberField]").attr("disabled",true);
					}
				}
			}
		},
		ldapinput : {
			btn : {
			    fieldAdd : function(c){
			        var rootId = c.getAttribute("rootId");
					var r = [
    					{ id : rootId+'.fieldId', type : 'number' , text : '' },
    					{ id : rootId+'.fieldName', type : 'input' , text : '' },
    					{ id : rootId+'.fieldType', type : 'select' , text : '' },
    					{ id : rootId+'.fieldAttribute', type : 'input' , text : '' },
    					{ id : rootId+'.fieldFormat', type : 'input' , text : '' },
    					{ id : rootId+'.fieldLength', type : 'input' , text : '' },
    					{ id : rootId+'.fieldPrecision', type : 'input' , text : ''},
    					{ id : rootId+'.fieldCurrency', type : 'input' , text : '' },
    					{ id : rootId+'.fieldDecimal', type : 'input' , text : '' },
    					{ id : rootId+'.fieldGroup', type : 'input' , text : '' },
    					{ id : rootId+'.fieldTrimType', type : 'select' , text : '' },
    					{ id : rootId+'.fieldRepeat', type : 'select' , text : '' }
    				];
					jQuery.imetabar.createRowByHeader(r,rootId);
			    },
				getfields : function(e,v){	
					var r = [
						{ id : 'fieldId', type : 'number' , text : '' },
						{ id : 'fieldName', type : 'input' , text : 'fieldName '},
						{ id : 'fieldType', type : 'select' , text : 'type' },
						{ id : 'fieldAttribute', type : 'input' , text : '' },
						{ id : 'fieldFormat', type : 'input' , text : '' },
						{ id : 'fieldLength', type : 'input' , text : 'length' },
						{ id : 'fieldPrecision', type : 'input' , text : 'precision' },
						{ id : 'fieldCurrency', type : 'input' , text : '' },
						{ id : 'fieldDecimal', type : 'input' , text : 'decimal' },
						{ id : 'fieldGroup', type : 'input' , text : 'group' },
						{ id : 'fieldTrimType', type : 'select' , text : '' },
						{ id : 'fieldRepeat', type : 'select' , text : '' }
					];
					jQuery.imeta.parameter.getfields(e,v,r);	
				}
			},
			listeners : {
				useAuthenticationListeners : function(e,v){
					var elId = e.target.id;
					var id = elId.split(".")[0];
					if(e.target.checked){
						$("[id="+id+".userName]").attr("disabled",false);
						$("[id="+id+".password]").attr("disabled",false);
					}else{
						$("[id="+id+".userName]").attr("disabled",true);
						$("[id="+id+".password]").attr("disabled",true);
					}
				},
				includeRowNumberListeners : function(e,v){
					var elId = e.target.id;
					var id = elId.split(".")[0];
					if(e.target.checked){
						$("[id="+id+".rowNumberField]").attr("disabled",false);
					}else{
						$("[id="+id+".rowNumberField]").attr("disabled",true);					}
				}
			}
		},
		ldifinput : {
			btn : {
				getfields : function(e,v){
					var r = [
						{ id :'fieldId', type : 'number' , text : '' },
						{ id :'fieldName', type : 'input' , text : 'fieldName' },
						{ id : 'fieldType', type : 'select' , text : 'type' },
						{ id : 'fieldAttribute', type : 'input' , text : '' },
						{ id : 'fieldFormat', type : 'input' , text : '' },
						{ id : 'fieldLength', type : 'input' , text : 'length' },
						{ id : 'fieldPrecision', type : 'input' , text : 'precision' },
						{ id : 'fieldCurrency', type : 'input' , text : '' },
						{ id : 'fieldDecimal', type : 'input' , text : 'decimal' },
						{ id : 'fieldGroup', type : 'input' , text : 'group' },
						{ id : 'fieldTrimType', type : 'select' , text : '' },
						{ id : 'fieldRepeat', type : 'select' , text : '' }
					];
					jQuery.imeta.parameter.getfields(e,v,r);
				}
			},
			selectedfiles : {
				btn : {
					fieldAdd : function(c){
						var rootId = c.getAttribute("rootId");
						var r = [
							{ id : rootId+'.fileId', type : 'number' , text : '' },
							{ id : rootId+'.fileName', type : 'input' , text : '' },
							{ id : rootId+'.fileMask', type : 'input' , text : '' }
						];
						jQuery.imetabar.createRowByHeader(r,rootId);
					}
				}
			},
			fields : {
				btn : {
					fieldAdd : function(c){
						var rootId = c.getAttribute("rootId");
						var r = [
							{ id : rootId+'.fieldId', type : 'number' , text : '' },
							{ id : rootId+'.fieldName', type : 'input' , text : '' },
							{ id : rootId+'.fieldType', type : 'select' , text : '' },
							{ id : rootId+'.fieldAttribute', type : 'input' , text : '' },
							{ id : rootId+'.fieldFormat', type : 'input' , text : '' },
							{ id : rootId+'.fieldLength', type : 'input' , text : '' },
							{ id : rootId+'.fieldPrecision', type : 'input' , text : '' },
							{ id : rootId+'.fieldCurrency', type : 'input' , text : '' },
							{ id : rootId+'.fieldDecimal', type : 'input' , text : ''},
							{ id : rootId+'.fieldGroup', type : 'input' , text : '' },
							{ id : rootId+'.fieldTrimType', type : 'select' , text : '' },
							{ id : rootId+'.fieldRepeat', type : 'select' , text : '' }
						];
						jQuery.imetabar.createRowByHeader(r,rootId);
					}
				}
			},
			listeners : {
				filefieldListeners : function(e,v){
					var elId = e.target.id;
					var id = elId.split(".")[0];
					if(e.target.checked){
						$("[id="+id+".dynamicFilenameField]").attr("disabled",false);
						$("[id="+id+".fileOrdirectory]").attr("disabled",true);
// $("[id="+id+".btn.fileOrdirectoryAdd]").attr("disabled",true);
// $("[id="+id+".btn.fileOrdirectorySelecter]").attr("disabled",true);
						$("[id="+id+".regularExpression]").attr("disabled",true);
// $("[id="+id+".deletebtn]").attr("disabled",true);
// $("[id="+id+".editbtn]").attr("disabled",true);
// $("[id="+id+".showFilesName]").attr("disabled",true);
						$("[id="+id+".includeFilename]").attr("disabled",true);
						$("[id="+id+".filenameField]").attr("disabled",true);
						$("[id="+id+".rowLimit]").attr("disabled",true);
						$("[id="+id+".includeFilename]").attr("checked",false);
						
						//点击后，表格屏蔽选中文件的按钮和包含文件的字段名
						$("[id="+id+"_selectedFiles.btn.add]").attr("disabled",true);
						$("[id="+id+"_selectedFiles.btn.delete]").attr("disabled",true);
						$("[id="+id+"_selectedFiles.btn.add.root]").addClass("x-item-disabled");
						$("[id="+id+"_selectedFiles.btn.delete.root]").addClass("x-item-disabled");
						$("#"+id+"_selectedFiles_gRoot input").attr("disabled",true);
					}else{
						$("[id="+id+".dynamicFilenameField]").attr("disabled",true);
						$("[id="+id+".fileOrdirectory]").attr("disabled",false);
// $("[id="+id+".btn.fileOrdirectoryAdd]").attr("disabled",false);
// $("[id="+id+".btn.fileOrdirectorySelecter]").attr("disabled",false);
						$("[id="+id+".regularExpression]").attr("disabled",false);
// $("[id="+id+".deletebtn]").attr("disabled",false);
// $("[id="+id+".editbtn]").attr("disabled",false);
// $("[id="+id+".showFilesName]").attr("disabled",false);
						$("[id="+id+".includeFilename]").attr("disabled",false);
						$("[id="+id+".rowLimit]").attr("disabled",false);
						
						//点击不选中后，表格显示选中文件的按钮和包含文件的字段名
						$("[id="+id+"_selectedFiles.btn.add]").attr("disabled",false);
						$("[id="+id+"_selectedFiles.btn.delete]").attr("disabled",false);
						$("[id="+id+"_selectedFiles.btn.add.root]").removeClass("x-item-disabled");
						$("[id="+id+"_selectedFiles.btn.delete.root]").removeClass("x-item-disabled");
						$("#"+id+"_selectedFiles_gRoot input").attr("disabled",false);
					}
				},
				includeFilenameListeners : function(e,v){
					var elId = e.target.id;
					var id = elId.split(".")[0];
					if(e.target.checked){
						$("[id="+id+".filenameField]").attr("disabled",false);
					}else{
						$("[id="+id+".filenameField]").attr("disabled",true);
					}
				},
				includeRowNumberListeners : function(e,v){
					var elId = e.target.id;
					var id = elId.split(".")[0];
					if(e.target.checked){
						$("[id="+id+".rowNumberField]").attr("disabled",false);
					}else{
						$("[id="+id+".rowNumberField]").attr("disabled",true);
					}
				}
			}
		},
		propertyinput : {
			btn : {
				getfields : function(e,v){
					var r = [
						{ id : 'fieldId', type : 'number' , text : '' },
						{ id : 'fieldName', type : 'input' , text : 'fieldName' },
						{ id : 'fieldColumn', type : 'select' , text : '' },
						{ id : 'fieldType', type : 'select' , text : 'type' },
						{ id : 'fieldFormat', type : 'input' , text : '' },
						{ id : 'fieldLength', type : 'input' , text : 'length' },
						{ id : 'fieldPrecision', type : 'input' , text : 'precision' },
						{ id : 'fieldCurrency', type : 'input' , text : '' },
						{ id : 'fieldDecimal', type : 'input' , text : 'decimal' },
						{ id : 'fieldGroup', type : 'input' , text : 'group' },
						{ id : 'fieldTrimType', type : 'select' , text : '' },
						{ id : 'fieldRepeat', type : 'select' , text : '' }
					];
					jQuery.imeta.parameter.getfields(e,v,r);
				}
			},
			listeners : {
				filefieldListeners : function(e,v){
					var elId = e.target.id;
					var id = elId.split(".")[0];
					if(e.target.checked){
						$("[id="+id+".dynamicFilenameField]").attr("disabled",false);
						$("[id="+id+".fileOrdirectory]").attr("disabled",true);
// 						$("[id="+id+".btn.fileOrdirectoryAdd]").attr("disabled",true);
// 						$("[id="+id+".btn.fileOrdirectorySelecter]").attr("disabled",true);
						$("[id="+id+".regularExpression]").attr("disabled",true);
// 						$("[id="+id+".delete]").attr("disabled",true);
// 						$("[id="+id+".edit]").attr("disabled",true);
// 						$("[id="+id+".showFilename]").attr("disabled",true);
						$("[id="+id+".includeFilename]").attr("disabled",true);
						$("[id="+id+".filenameField]").attr("disabled",true);
						$("[id="+id+".rowLimit]").attr("disabled",true);
						//includeFilename置为未选中
						$("[id="+id+".includeFilename]").attr("checked",false);
						
						//点击后，表格屏蔽选中文件的按钮和包含文件的字段名
						$("[id="+id+"_selectedFiles.btn.add]").attr("disabled",true);
						$("[id="+id+"_selectedFiles.btn.delete]").attr("disabled",true);
						$("[id="+id+"_selectedFiles.btn.add.root]").addClass("x-item-disabled");
						$("[id="+id+"_selectedFiles.btn.delete.root]").addClass("x-item-disabled");
						$("#"+id+"_selectedFiles_gRoot input").attr("disabled",true);
					}else{
						$("[id="+id+".dynamicFilenameField]").attr("disabled",true);
						$("[id="+id+".fileOrdirectory]").attr("disabled",false);
// 						$("[id="+id+".btn.fileOrdirectoryAdd]").attr("disabled",false);
// 						$("[id="+id+".btn.fileOrdirectorySelecter]").attr("disabled",false);
						$("[id="+id+".regularExpression]").attr("disabled",false);
// 						$("[id="+id+".delete]").attr("disabled",false);
// 						$("[id="+id+".edit]").attr("disabled",false);
// 						$("[id="+id+".showFilename]").attr("disabled",false);
						$("[id="+id+".includeFilename]").attr("disabled",false);
						$("[id="+id+".rowLimit]").attr("disabled",false);
						
						//点击不选中后，表格显示选中文件的按钮和包含文件的字段名
						$("[id="+id+"_selectedFiles.btn.add]").attr("disabled",false);
						$("[id="+id+"_selectedFiles.btn.delete]").attr("disabled",false);
						$("[id="+id+"_selectedFiles.btn.add.root]").removeClass("x-item-disabled");
						$("[id="+id+"_selectedFiles.btn.delete.root]").removeClass("x-item-disabled");
						$("#"+id+"_selectedFiles_gRoot input").attr("disabled",false);
					}
				},
				includeFilenametListeners : function(e,v){
					var elId = e.target.id;
					var id = elId.split(".")[0];
					if(e.target.checked){
						$("[id="+id+".filenameField]").attr("disabled",false);
					}else{
						$("[id="+id+".filenameField]").attr("disabled",true);
					}
				},
				includeRowNumberListeners : function(e,v){
					var elId = e.target.id;
					var id = elId.split(".")[0];
					if(e.target.checked){
						$("[id="+id+".rowNumberField]").attr("disabled",false);
						$("[id="+id+".resetRowNumber]").attr("disabled",false);
					}else{
						$("[id="+id+".rowNumberField]").attr("disabled",true);
						$("[id="+id+".resetRowNumber]").attr("disabled",true);
					}
				}
			},
			selectedfiles : {
				btn : {
					fieldAdd : function(c){
						var rootId = c.getAttribute("rootId");
						var r = [
							{ id : rootId+'.fileId', type : 'number' , text : '' },
							{ id : rootId+'.fileName', type : 'input' , text : '' },
							{ id : rootId+'.fileMask', type : 'input' , text : '' }
						];
						jQuery.imetabar.createRowByHeader(r,rootId);
					}
				}
			},
			fields : {
				btn : {
					fieldAdd : function(c){
						var rootId = c.getAttribute("rootId");
						var r = [
							{ id : rootId+'.fieldId', type : 'number' , text : '' },
							{ id : rootId+'.fieldName', type : 'input' , text : '' },
							{ id : rootId+'.fieldColumn', type : 'select' , text : '' },
							{ id : rootId+'.fieldType', type : 'select' , text : '' },
							{ id : rootId+'.fieldFormat', type : 'input' , text : '' },
							{ id : rootId+'.fieldLength', type : 'input' , text : '' },
							{ id : rootId+'.fieldPrecision', type : 'input' , text : '' },
							{ id : rootId+'.fieldCurrency', type : 'input' , text : '' },
							{ id : rootId+'.fieldDecimal', type : 'input' , text : '' },
							{ id : rootId+'.fieldGroup', type : 'input' , text : '' },
							{ id : rootId+'.fieldTrimType', type : 'select' , text : '' },
							{ id : rootId+'.fieldRepeat', type : 'select' , text : '' }
						];
						jQuery.imetabar.createRowByHeader(r,rootId);
					}
				}
			}
		},
		xmlinputsax : {
		   	btn : {
				getfields : function(e,v){
						var r = [
							{ id : 'fieldId', type : 'number' , text : '' },
							{ id : 'fieldName', type : 'input' , text : 'fieldName' },
							{ id : 'fieldType', type : 'select' , text : 'type' },
							{ id : 'fieldFormat', type : 'input' , text : '' },
							{ id : 'fieldLength', type : 'input' , text : 'length' },
							{ id : 'fieldPrecision', type : 'input' , text : 'precision' },
							{ id : 'fieldCurrency', type : 'input' , text : '' },
							{ id : 'fieldDecimal', type : 'input' , text : 'decimal' },
							{ id : 'fieldGroup', type : 'input' , text : 'group' },
							{ id : 'fieldTrimType', type : 'select' , text : '' },
							{ id : 'fieldRepeat', type : 'select' , text : 'repeat' },
							{ id : 'fieldPostion', type : 'input' , text : '' }
						];
						jQuery.imeta.parameter.getfields(e,v,r);
				}
			},
			listeners : {
				includeFilenameListeners : function(e,v){
					var elId = e.target.id;
					var id = elId.split(".")[0];
					if(e.target.checked){
						$("[id="+id+".filenameField]").attr("disabled",false);
					}else{
						$("[id="+id+".filenameField]").attr("disabled",true);
					}
				},
				includeRowNumberListeners : function(e,v){
					var elId = e.target.id;
					var id = elId.split(".")[0];
					if(e.target.checked){
						$("[id="+id+".rowNumberField]").attr("disabled",false);
					}else{
						$("[id="+id+".rowNumberField]").attr("disabled",true);
					}
				}
			},
			selectedfiles : {
				btn : {
					fieldAdd : function(c){
						var rootId = c.getAttribute("rootId");
						var r = [
							{ id : rootId+'.fileId', type : 'number' , text : '' },
							{ id : rootId+'.fileName', type : 'input' , text : '' },
							{ id : rootId+'.fileMask', type : 'input' , text : '' }
						];
						
						jQuery.imetabar.createRowByHeader(r,rootId);
					}
				}
			},
			location : {
				btn : {
					fieldAdd : function(c){
						var rootId = c.getAttribute("rootId");
						var r = [
							{ id : rootId+'.elementId', type : 'number' , text :'' },
							{ id : rootId+'.element', type : 'input' , text : '' }
						];
						jQuery.imetabar.createRowByHeader(r,rootId);
					}
				}
			},
			elementfields : {
				btn : {
					fieldAdd : function(c){
						var rootId = c.getAttribute("rootId");
						var r = [
							{ id : rootId+'.elementId', type : 'number' , text : '' },
							{ id : rootId+'.element', type : 'input' , text : '' },
							{ id : rootId+'.attribute', type : 'input' , text : '' }
						];
						jQuery.imetabar.createRowByHeader(r,rootId);
					}
				}
			},
			fields : {
					btn : {
					fieldAdd : function(c){
						var rootId = c.getAttribute("rootId");
						var r = [
							{ id : rootId+'.fieldId', type : 'number' , text : '' },
							{ id : rootId+'.fieldName', type : 'input' , text : '' },
							{ id : rootId+'.fieldType', type : 'select' , text : '' },
							{ id : rootId+'.fieldFormat', type : 'input' , text : '' },
							{ id : rootId+'.fieldLength', type : 'input' , text : '' },
							{ id : rootId+'.fieldPrecision', type : 'input' , text : '' },
							{ id : rootId+'.fieldCurrency', type : 'input' , text : '' },
							{ id : rootId+'.fieldDecimal', type : 'input' , text : '' },
							{ id : rootId+'.fieldGroup', type : 'input' , text : '' },
							{ id : rootId+'.fieldTrimType', type : 'select' , text : '' },
							{ id : rootId+'.fieldRepeat', type : 'select' , text : '' },
							{ id : rootId+'.fieldPostion', type : 'input' , text : '' }
						];
						jQuery.imetabar.createRowByHeader(r,rootId);
					}
				}
			}
		},
		xbaseinput : {
			listeners : {
				acceptingFilenamesListeners : function(e,v){
					var elId = e.target.id;
					var id = elId.split(".")[0];
					if(e.target.checked){
						$("[id="+id+".dbfFileName]").attr("disabled",true);
					}else{
						$("[id="+id+".dbfFileName]").attr("disabled",false);
					}
				},
				rowNrAddedListeners : function(e,v){
					var elId = e.target.id;
					var id = elId.split(".")[0];
					if(e.target.checked){
						$("[id="+id+".rowNrField]").attr("disabled",false);
					}else{
						$("[id="+id+".rowNrField]").attr("disabled",true);
					}
				},
				IncludeFilenameListeners : function(e,v){
					var elId = e.target.id;
					var id = elId.split(".")[0];
					if(e.target.checked){
						$("[id="+id+".filenameField]").attr("disabled",false);
					}else{
						$("[id="+id+".filenameField]").attr("disabled",true);
					}
				}
			}
		},
		textfileinput : {
			btn : {
				getfields : function(e,v){	
					var r = [													
						{ id : 'fieldId', type : 'number' , text : '' },
						{ id : 'fieldName', type : 'input' , text : 'fieldName' },
						{ id : 'fieldType', type : 'select' , text : 'type'},
						{ id : 'fieldFormat', type : 'input' , text : '' },
						{ id : 'fieldPosition', type : 'input' , text : '' },
						{ id : 'fieldLength', type : 'input' , text : 'length' },
						{ id : 'fieldPrecision', type : 'input' , text : 'precision' },
						{ id : 'fieldCurrency', type : 'input' , text : '' },
						{ id : 'fieldDecimal', type : 'input' , text : 'decimal' },
						{ id : 'fieldGroup', type : 'input' , text : 'group' },
						{ id : 'nullIf', type : 'input' , text : '' },
						{ id : 'ifNull', type : 'input' , text : '' },
						{ id : 'fieldTrimType', type : 'select' , text : 'trimType' },
						{ id : 'fieldRepeat', type : 'select' , text : '' }
						];
						jQuery.imeta.parameter.getfields(e,v,r);	
				}
			},
			pitch : {
				btn : {
					fieldAdd : function(c){
						var rootId = c.getAttribute("rootId");
						var r = [
							{ id : rootId+'.fileId', type : 'number' , text : '' },
							{ id : rootId+'.fileName', type : 'input' , text : '' },
							{ id : rootId+'.fileMask', type : 'input' , text : '' },
							{ id : rootId+'.fileRequired', type : 'input' , text : '' }
						];
						
						jQuery.imetabar.createRowByHeader(r,rootId);
					}
				}
			},
			sift : {
				btn : {
					fieldAdd : function(c){
						var rootId = c.getAttribute("rootId");
						var r = [
							{ id : rootId+'.siftId', type : 'number' , text : '' },
							{ id : rootId+'.filterString', type : 'input' , text : '' },
							{ id : rootId+'.locationFilter', type : 'input' , text : '' },
							{ id : rootId+'.stopFilter', type : 'select' , text : '' }
						];
						
						jQuery.imetabar.createRowByHeader(r,rootId);
					}
				}
			},
			fields : {
				btn : {
					fieldAdd : function(c){
						var rootId = c.getAttribute("rootId");
						var r = [
							{ id : rootId+'.fieldId', type : 'number' , text :'' },
							{ id : rootId+'.fieldName', type : 'input' , text : '' },
							{ id : rootId+'.fieldType', type : 'select' , text : '' },
							{ id : rootId+'.fieldFormat', type : 'input' , text : '' },
							{ id : rootId+'.fieldPosition', type : 'input' , text : '' },
							{ id : rootId+'.fieldLength', type : 'input' , text : '' },
							{ id : rootId+'.fieldPrecision', type : 'input' , text : '' },
							{ id : rootId+'.fieldCurrency', type : 'input' , text : '' },
							{ id : rootId+'.fieldDecimal', type : 'input' , text : '' },
							{ id : rootId+'.fieldGroup', type : 'input' , text : '' },
							{ id : rootId+'.nullIf', type : 'input' , text : '' },
							{ id : rootId+'.ifNull', type : 'input' , text : '' },
							{ id : rootId+'.fieldTrimType', type : 'select' , text : '' },
							{ id : rootId+'.fieldRepeat', type : 'select' , text : '' }
						];
						jQuery.imetabar.createRowByHeader(r,rootId);
					}
				}
			},
			listeners : {
				acceptingFilenamesListeners : function(e,v){
					var elId = e.target.id;
					var id = elId.split(".")[0];
					if(e.target.checked){
						$("[id="+id+".fieldsorcatalog]").attr("disabled",true);
// 						$("[id="+id+".btn.add]").attr("disabled",true);
// 						$("[id="+id+".btn.browse]").attr("disabled",true);
						$("[id="+id+".codexexpression]").attr("disabled",true);
// 						$("[id="+id+".btn.delete]").attr("disabled",true);
// 						$("[id="+id+".btn.edit]").attr("disabled",true);
						$("[id="+id+".passingThruFields]").attr("disabled",false);
						$("[id="+id+".acceptingStepName]").attr("disabled",false);
						$("[id="+id+".acceptingField]").attr("disabled",false);
// 						$("[id="+id+".btn.showname]").attr("disabled",true);
// 						$("[id="+id+".btn.showcontent]").attr("disabled",true);
// 						$("[id="+id+".btn.showfirstlinecontent]").attr("disabled",true);
						
						//点击后，表格屏蔽选中文件的按钮和包含文件的字段名
						$("[id="+id+"_pitch.btn.add]").attr("disabled",true);
						$("[id="+id+"_pitch.btn.delete]").attr("disabled",true);
						$("[id="+id+"_pitch.btn.add.root]").addClass("x-item-disabled");
						$("[id="+id+"_pitch.btn.delete.root]").addClass("x-item-disabled");
						$("#"+id+"_pitch_gRoot input").attr("disabled",true);
					}else{
						$("[id="+id+".fieldsorcatalog]").attr("disabled",false);
//						$("[id="+id+".btn.add]").attr("disabled",false);
//						$("[id="+id+".btn.browse]").attr("disabled",false);
						$("[id="+id+".codexexpression]").attr("disabled",false);
// 						$("[id="+id+".btn.delete]").attr("disabled",false);
// 						$("[id="+id+".btn.edit]").attr("disabled",false);
						$("[id="+id+".passingThruFields]").attr("disabled",true);
						$("[id="+id+".acceptingStepName]").attr("disabled",true);
						$("[id="+id+".acceptingField]").attr("disabled",true);
//						$("[id="+id+".btn.showname]").attr("disabled",false);
//						$("[id="+id+".btn.showcontent]").attr("disabled",false);
// 						$("[id="+id+".btn.showfirstlinecontent]").attr("disabled",false);
						
						//点击不选中后，表格显示选中文件的按钮和包含文件的字段名
						$("[id="+id+"_pitch.btn.add]").attr("disabled",false);
						$("[id="+id+"_pitch.btn.delete]").attr("disabled",false);
						$("[id="+id+"_pitch.btn.add.root]").removeClass("x-item-disabled");
						$("[id="+id+"_pitch.btn.delete.root]").removeClass("x-item-disabled");
						$("#"+id+"_pitch_gRoot input").attr("disabled",false);
					}
				},
				header : function(e,v){
					var elId = e.target.id;
					var id = elId.split(".")[0];
					if(e.target.checked){
						$("[id="+id+".nrHeaderLines]").attr("disabled",false);
					}else{
						$("[id="+id+".nrHeaderLines]").attr("disabled",true);
					}
				},
				footer : function(e,v){
					var elId = e.target.id;
					var id = elId.split(".")[0];
					if(e.target.checked){
						$("[id="+id+".nrFooterLines]").attr("disabled",false);
					}else{
						$("[id="+id+".nrFooterLines]").attr("disabled",true);
					}
				},
				lineWrapped : function(e,v){
					var elId = e.target.id;
					var id = elId.split(".")[0];
					if(e.target.checked){
						$("[id="+id+".nrWraps]").attr("disabled",false);
					}else{
						$("[id="+id+".nrWraps]").attr("disabled",true);
					}
				},
				layoutPaged : function(e,v){
					var elId = e.target.id;
					var id = elId.split(".")[0];
					if(e.target.checked){
						$("[id="+id+".nrLinesPerPage]").attr("disabled",false);
						$("[id="+id+".nrLinesDocHeader]").attr("disabled",false);
					}else{
						$("[id="+id+".nrLinesPerPage]").attr("disabled",true);
						$("[id="+id+".nrLinesDocHeader]").attr("disabled",true);
					}
				},
				includeFilename : function(e,v){
					var elId = e.target.id;
					var id = elId.split(".")[0];
					if(e.target.checked){
						$("[id="+id+".filenameField]").attr("disabled",false);
					}else{
						$("[id="+id+".filenameField]").attr("disabled",true);
					}
				},
				includeRowNumber : function(e,v){
					var elId = e.target.id;
					var id = elId.split(".")[0];
					if(e.target.checked){
						$("[id="+id+".rowNumberField]").attr("disabled",false);
						$("[id="+id+".rowNumberByFile]").attr("disabled",false);
					}else{
						$("[id="+id+".rowNumberField]").attr("disabled",true);
						$("[id="+id+".rowNumberByFile]").attr("disabled",true);
					}
				},
				errorIgnored : function(e,v){
					var elId = e.target.id;
					var id = elId.split(".")[0];
					if(e.target.checked){
						$("[id="+id+".errorLineSkipped]").attr("disabled",false);
						$("[id="+id+".errorCountField]").attr("disabled",false);
						$("[id="+id+".errorFieldsField]").attr("disabled",false);
						$("[id="+id+".errorTextField]").attr("disabled",false);
						$("[id="+id+".warningFilesDestinationDirectory]").attr("disabled",false);
						$("[id="+id+".warningFilesExtension]").attr("disabled",false);
						$("[id="+id+".variable]").attr("disabled",false);
						$("[id="+id+".browse]").attr("disabled",false);
						$("[id="+id+".warningFilesExtension]").attr("disabled",false);
						$("[id="+id+".errorFilesDestinationDirectory]").attr("disabled",false);
						$("[id="+id+".errorFilesExtension]").attr("disabled",false);
						$("[id="+id+".lineNumberFilesDestinationDirectory]").attr("disabled",false);
						$("[id="+id+".lineNumberFilesExtension]").attr("disabled",false);
					}else{
						$("[id="+id+".errorLineSkipped]").attr("disabled",true);
						$("[id="+id+".errorCountField]").attr("disabled",true);
						$("[id="+id+".errorFieldsField]").attr("disabled",true);
						$("[id="+id+".errorTextField]").attr("disabled",true);
						$("[id="+id+".warningFilesDestinationDirectory]").attr("disabled",true);
						$("[id="+id+".warningFilesExtension]").attr("disabled",true);
						$("[id="+id+".variable]").attr("disabled",true);
						$("[id="+id+".browse]").attr("disabled",true);
						$("[id="+id+".warningFilesExtension]").attr("disabled",true);
						$("[id="+id+".errorFilesDestinationDirectory]").attr("disabled",true);
						$("[id="+id+".errorFilesExtension]").attr("disabled",true);
						$("[id="+id+".lineNumberFilesDestinationDirectory]").attr("disabled",true);
						$("[id="+id+".lineNumberFilesExtension]").attr("disabled",true);
					}
				}
			}
		},
		xmlinput : {
			btn : {
				getfields : function(e,v){
					var r = [
						{ id : 'fieldId', type : 'number' , text :'' },
						{ id : 'fieldName', type : 'input' , text : 'fieldName' },
						{ id : 'fieldType', type : 'select' , text : 'type' },
						{ id : 'fieldFormat', type : 'select' , text : ''},
						{ id : 'fieldLength', type : 'input' , text : 'length'},
						{ id : 'fieldPrecision', type : 'input' , text : 'precision'},
						{ id : 'fieldCurrency', type : 'input' , text : ''},
						{ id : 'fieldDecimal', type : 'input' , text : 'decimal'},
						{ id : 'fieldGroup', type : 'input' , text : 'group'},
						{ id : 'fieldTrimType', type : 'select' , text : ''},
						{ id : 'fieldRepeat', type : 'select' , text : '' },
						{ id : 'fieldPosition', type : 'input' , text : ''}
					];
					jQuery.imeta.parameter.getfields(e,v,r);							
				}
			},
			listeners : {
				includeFilenameListeners : function(e,v){
					var elId = e.target.id;
					var id = elId.split(".")[0];
					if(e.target.checked){
						$("[id="+id+".filenameField]").attr("disabled",false);
					}else{
						$("[id="+id+".filenameField]").attr("disabled",true);
					}
				},
				includeRowNumberListeners : function(e,v){
					var elId = e.target.id;
					var id = elId.split(".")[0];
					if(e.target.checked){
						$("[id="+id+".rowNumberField]").attr("disabled",false);
					}else{
						$("[id="+id+".rowNumberField]").attr("disabled",true);
					}
				}
			},
			selectedfiles : {
				btn : {
					fieldAdd : function(c){
						var rootId = c.getAttribute("rootId");
						var r = [
							{ id : rootId+'.fieldId', type : 'number' , text : ''},
							{ id : rootId+'.fileName', type : 'input' , text : '' },
							{ id : rootId+'.fileMask', type : 'input' , text : ''}
						];
						jQuery.imetabar.createRowByHeader(r,rootId);
					}
				}
			},
			location : {
				btn : {
					fieldAdd : function(c){
						var rootId = c.getAttribute("rootId");
						var r = [
							{ id : rootId+'.fieldId', type : 'number' , text : '' },
							{ id : rootId+'.element', type : 'input' , text : '' }
						];
						jQuery.imetabar.createRowByHeader(r,rootId);
					}
				}
			},
			fields : {
				btn : {
					fieldAdd : function(c){
						var rootId = c.getAttribute("rootId");
						var r = [
							{ id : rootId+'.fieldId', type : 'number' , text : '' },
							{ id : rootId+'.fieldName', type : 'input' , text : ''},
							{ id : rootId+'.fieldType', type : 'select' , text : ''},
							{ id : rootId+'.fieldFormat', type : 'select' , text : ''},
							{ id : rootId+'.fieldLength', type : 'input' , text : ''},
							{ id : rootId+'.fieldPrecision', type : 'input' , text : ''},
							{ id : rootId+'.fieldCurrency', type : 'input' , text : ''},
							{ id : rootId+'.fieldDecimal', type : 'input' , text : ''},
							{ id : rootId+'.fieldGroup', type : 'input' , text : ''},
							{ id : rootId+'.fieldTrimType', type : 'select' , text : ''},
							{ id : rootId+'.fieldRepeat', type : 'select' , text : ''},
							{ id : rootId+'.fieldPosition', type : 'input' , text : ''}
						];
						jQuery.imetabar.createRowByHeader(r,rootId);
					}
				}
			}
		},
		rssinput : {
			btn : {
				getfields : function(e,v){
					var r = [												
						{ id : 'fieldId', type : 'number' , text : '' },
						{ id : 'fieldName', type : 'input' , text : 'fieldName' },
						{ id : 'fieldColumn', type : 'select' , text : '' },
						{ id : 'fieldType', type : 'select' , text : 'type' },
						{ id : 'fieldFormat', type : 'input' , text : '' },
						{ id : 'fieldLength', type : 'input' , text : 'length' },
						{ id : 'fieldPrecision', type : 'input' , text : 'precision' },
						{ id : 'fieldCurrency', type : 'input' , text : '' },
						{ id : 'fieldDecimal', type : 'input' , text : 'decimal' },
						{ id : 'fieldGroup', type : 'input' , text : 'group' },
						{ id : 'fieldTrimType', type : 'select' , text : '' },
						{ id : 'fieldRepeat', type : 'select' , text : '' }
					];
					jQuery.imeta.parameter.getfields(e,v,r);	
				}
			},
			urlList : {
				btn : {
					fieldAdd : function(c){
						var rootId = c.getAttribute("rootId");
						var r = [
							{ id : rootId+'.urlId', type : 'number' , text : '' },
							{ id : rootId+'.url', type : 'input' , text : '' }
						];
						jQuery.imetabar.createRowByHeader(r,rootId);
					}
				}
			},
			fields : {
				btn : {
					fieldAdd : function(c){
						var rootId = c.getAttribute("rootId");
						var r = [
							{ id : rootId+'.fieldId', type : 'number' , text : '' },
							{ id : rootId+'.fieldName', type : 'input' , text : '' },
							{ id : rootId+'.fieldColumn', type : 'select' , text : '' },
							{ id : rootId+'.fieldType', type : 'select' , text : '' },
							{ id : rootId+'.fieldFormat', type : 'input' , text : '' },
							{ id : rootId+'.fieldLength', type : 'input' , text : '' },
							{ id : rootId+'.fieldPrecision', type : 'input' , text : '' },
							{ id : rootId+'.fieldCurrency', type : 'input' , text : '' },
							{ id : rootId+'.fieldDecimal', type : 'input' , text : '' },
							{ id : rootId+'.fieldGroup', type : 'input' , text : '' },
							{ id : rootId+'.fieldTrimType', type : 'select' , text : '' },
							{ id : rootId+'.fieldRepeat', type : 'select' , text : '' }
						];
						jQuery.imetabar.createRowByHeader(r,rootId);
					}
				}
			},
			listeners : {
				urlInField : function(e,v){
					var elId = e.target.id;
					var id = elId.split(".")[0];
					if(e.target.checked){
						$("[id="+id+".urlField]").attr("disabled",false);
						$("[id="+id+".includeUrl]").attr("disabled",true);
						$("[id="+id+".urlFieldname]").attr("disabled",true);
						$("[id="+id+".includeUrl]").attr("checked",false);
						
						//点击后，表格屏蔽选中文件的按钮和包含文件的字段名
						$("[id="+id+"_urlList.btn.add]").attr("disabled",true);
						$("[id="+id+"_urlList.btn.delete]").attr("disabled",true);
						$("[id="+id+"_urlList.btn.add.root]").addClass("x-item-disabled");
						$("[id="+id+"_urlList.btn.delete.root]").addClass("x-item-disabled");
						$("#"+id+"_urlList_gRoot input").attr("disabled",true);
					}else{
						$("[id="+id+".urlField]").attr("disabled",true);
						$("[id="+id+".includeUrl]").attr("disabled",false);
//						$("[id="+id+".urlFieldname]").attr("disabled",false);
						
						//点击不选中后，表格显示选中文件的按钮和包含文件的字段名
						$("[id="+id+"_urlList.btn.add]").attr("disabled",false);
						$("[id="+id+"_urlList.btn.delete]").attr("disabled",false);
						$("[id="+id+"_urlList.btn.add.root]").removeClass("x-item-disabled");
						$("[id="+id+"_urlList.btn.delete.root]").removeClass("x-item-disabled");
						$("#"+id+"_urlList_gRoot input").attr("disabled",false);
					}
				},
				includeUrlInOutput : function(e,v){
					var elId = e.target.id;
					var id = elId.split(".")[0];
					if(e.target.checked){
						$("[id="+id+".urlFieldname]").attr("disabled",false);
					}else{
						$("[id="+id+".urlFieldname]").attr("disabled",true);
					}
				},
				includeRownumInOutput : function(e,v){
					var elId = e.target.id;
					var id = elId.split(".")[0];
					if(e.target.checked){
						$("[id="+id+".rownumFieldname]").attr("disabled",false);
					}else{
						$("[id="+id+".rownumFieldname]").attr("disabled",true);
					}
				}
			}
		},
		salesforceinput : {
			btn : {
				getfields : function(e,v){
					var r = [												
						{ id : 'fieldId', type : 'number' , text : '' },
						{ id : 'name', type : 'input' , text : '' },
						{ id : 'field', type : 'input' , text : 'fieldName' },
						{ id : 'type', type : 'select' , text : 'type' },
						{ id : 'format', type : 'input' , text : '' },
						{ id : 'length', type : 'input' , text : 'length' },
						{ id : 'precision', type : 'input' , text : 'precision' },
						{ id : 'currencySymbol', type : 'input' , text : '' },
						{ id : 'decimalSymbol', type : 'input' , text : 'decimal' },
						{ id : 'groupSymbol', type : 'input' , text : 'group' },
						{ id : 'trimType', type : 'select' , text : '' },
						{ id : 'repeated', type : 'select' , text : '' }
					];
					jQuery.imeta.parameter.getfields(e,v,r);
				},				
				fieldAdd : function(c){
					var rootId = c.getAttribute("rootId");
					var r = [												
						{ id : rootId+'.fieldId', type : 'number' , text : '' },
						{ id : rootId+'.name', type : 'input' , text : '' },
						{ id : rootId+'.field', type : 'input' , text : '' },
						{ id : rootId+'.type', type : 'select' , text : '' },
						{ id : rootId+'.format', type : 'input' , text : '' },
						{ id : rootId+'.length', type : 'input' , text : '' },
						{ id : rootId+'.precision', type : 'input' , text : '' },
						{ id : rootId+'.currencySymbol', type : 'input' , text : '' },
						{ id : rootId+'.decimalSymbol', type : 'input' , text : '' },
						{ id : rootId+'.groupSymbol', type : 'input' , text : '' },
						{ id : rootId+'.trimType', type : 'select' , text : '' },
						{ id : rootId+'.repeated', type : 'select' , text : '' }
					];
					jQuery.imetabar.createRowByHeader(r,rootId);
				}
			},
			listeners : {
				includeTargetURLListeners : function(e,v){
					var elId = e.target.id;
					var id = elId.split(".")[0];
					if(e.target.checked){
						$("[id="+id+".targetURLField]").attr("disabled",false);
					}else{
						$("[id="+id+".targetURLField]").attr("disabled",true);
					}
				},
				includeModuleListeners : function(e,v){
					var elId = e.target.id;
					var id = elId.split(".")[0];
					if(e.target.checked){
						$("[id="+id+".moduleField]").attr("disabled",false);
					}else{
						$("[id="+id+".moduleField]").attr("disabled",true);
					}
				},
				includeSQLListeners : function(e,v){
					var elId = e.target.id;
					var id = elId.split(".")[0];
					if(e.target.checked){
						$("[id="+id+".sqlField]").attr("disabled",false);
					}else{
						$("[id="+id+".sqlField]").attr("disabled",true);
					}
				},
				includeTimestampListeners : function(e,v){
					var elId = e.target.id;
					var id = elId.split(".")[0];
					if(e.target.checked){
						$("[id="+id+".timestampField]").attr("disabled",false);
					}else{
						$("[id="+id+".timestampField]").attr("disabled",true);
					}
				},
				includeRowNumberListeners : function(e,v){
					var elId = e.target.id;
					var id = elId.split(".")[0];
					if(e.target.checked){
						$("[id="+id+".rowNumberField]").attr("disabled",false);
					}else{
						$("[id="+id+".rowNumberField]").attr("disabled",true);
					}
				}
			}
		},
		sqlfileoutput : {
			listeners : {
				addCreattable : function(e,v){
				var elId = e.target.id;
				var id = elId.split(".")[0];
					if(e.target.checked){
						$("[id="+id+".addTruncatetable]").attr("disabled",true);
					}else{
						$("[id="+id+".addTruncatetable]").attr("disabled",false);
					}
				}
			}
		},
		tableoutput: {
		     btn : {
				   fieldAdd : function(c){
					   var rootId = c.getAttribute("rootId");
					   var r = [
						   { id : rootId+'.fieldId', type : 'number' , text : '' },
						   { id : rootId+'.fieldDatabase', type : 'input' , text : '' },
						   { id : rootId+'.fieldStream', type : 'input' , text : '' }
						   
					   ];
					
					  jQuery.imetabar.createRowByHeader(r,rootId);
				   }
			   },
		  listeners : {
				specifyFieldsListener : function(e,v){
					var elId = e.target.id;
					var id = elId.split(".")[0];
					if(e.target.checked){
						$("[id="+id+".tableNameInTable]").attr("disabled",true);
					 
					}else{
						$("[id="+id+".tableNameInTable]").attr("disabled",false);
					}
				},
				tableZoningListener : function(e,v){
					var elId = e.target.id;
					var id = elId.split(".")[0];
					if(e.target.checked){
						$("[id="+id+".truncateTable]").attr("disabled",true);
						$("[id="+id+".partitioningField]").attr("disabled",false);
						$("[id="+id+".partitioningMonthly]").attr("disabled",false);
						$("[id="+id+".partitioningDaily]").attr("disabled",false);
					}else{
						$("[id="+id+".truncateTable]").attr("disabled",false);
						$("[id="+id+".partitioningField]").attr("disabled",true);
						$("[id="+id+".partitioningMonthly]").attr("disabled",true);
						$("[id="+id+".partitioningDaily]").attr("disabled",true);
					}
				},
				bulkInsertListener : function(e,v){
					var elId = e.target.id;
					var id = elId.split(".")[0];
					if(e.target.checked){
						$("[id="+id+".ignoreErrors]").attr("disabled",true);
				
					}else{
						$("[id="+id+".ignoreErrors]").attr("disabled",false);
					
					}
				},
				inOneFieldListener : function(e,v){
					var elId = e.target.id;
					var id = elId.split(".")[0];
					if(e.target.checked){
						$("[id="+id+".tablename]").attr("disabled",true);
						$("[id="+id+".truncateTable]").attr("disabled",true);
						$("[id="+id+".tableNameField]").attr("disabled",false);
				
					}else{
						$("[id="+id+".tablename]").attr("disabled",false);
						$("[id="+id+".truncateTable]").attr("disabled",false);
						$("[id="+id+".tableNameField]").attr("disabled",true);
					
					}
				},
			   returnKeyListener : function(e,v){
					var elId = e.target.id;
					var id = elId.split(".")[0];
					if(e.target.checked){
						$("[id="+id+".useBatchUpdate]").attr("disabled",true);
						$("[id="+id+".generatedKeyField]").attr("disabled",false);
				
					}else{
						$("[id="+id+".useBatchUpdate]").attr("disabled",false);
						$("[id="+id+".generatedKeyField]").attr("disabled",true);
					
					}
				}
				
			}
		},
		rssoutput : {
			btn : {
				channelFields1Add : function(c){
					var rootId = c.getAttribute("rootId");
					
					var r = [
						{ id : rootId+'.fieldId', type : 'number' , text :'' },
						{ id : rootId+'.ChannelCustomTags', type : 'input' , text : ''},
						{ id : rootId+'.ChannelCustomFields', type : 'input' , text : ''}
					];
					
					  jQuery.imetabar.createRowByHeader(r,rootId);
				},
				itemFields1Add : function(c){
					var rootId = c.getAttribute("rootId");
				
					var r = [
						{ id : rootId+'.fieldId', type : 'number' , text :'' },
						{ id : rootId+'.ItemCustomTags', type : 'input' , text : '' },
						{ id : rootId+'.ItemCustomFields', type : 'input' , text :'' }
					]

				   jQuery.imetabar.createRowByHeader(r,rootId);

				},
				namespacesAdd : function(c){
					var rootId = c.getAttribute("rootId");
				
					var r = [
						{ id : rootId+'.fieldId', type : 'number' , text :'' },
						{ id : rootId+'.NameSpacesTitle', type : 'input' , text :'' },
					   	{ id : rootId+'.NameSpaces', type : 'input' , text :'' }
					];
					
				
					  jQuery.imetabar.createRowByHeader(r,rootId);
				}
			}
		},
		tableinput : {
		    listeners : {
		        
		    },
		    btn : {
			    parametersAdd : function(c){
					var rootId = c.getAttribute("rootId");
					
					var r = [
						{ id : rootId+'.parameterId', type : 'number' , text : '' },
						{ id : rootId+'.parameterName', type : 'input' , text : '' }
					];
					
					jQuery.imetabar.createRowByHeader(r,rootId);
			   }
			}
		},
		textfileoutput : {
			listeners : {
				fileNameInFieldListener : function(e,v){
					var elId = e.target.id;
					var id = elId.split(".")[0];
					if(e.target.checked){
						$("[id="+id+".fileName]").attr("disabled",true);
						$("[id="+id+".btn.showFieldName]").attr("disabled",true);
						$("[id="+id+".fileAsCommand]").attr("disabled",true);
						$("[id="+id+".fileNameField]").attr("disabled",false);
					}else{
						$("[id="+id+".fileName]").attr("disabled",false);
						$("[id="+id+".btn.showFieldName]").attr("disabled",false);
						$("[id="+id+".fileAsCommand]").attr("disabled",false);
						$("[id="+id+".fileNameField]").attr("disabled",true);
					}
				},
				specifyingFormatListener : function(e,v){
					var elId = e.target.id;
					var id = elId.split(".")[0];
					if(e.target.checked){
						$("[id="+id+".dateInFilename]").attr("disabled",true);
						$("[id="+id+".timeInFilename]").attr("disabled",true);
						$("[id="+id+".dateTimeFormat]").attr("disabled",false);
					}else{
						$("[id="+id+".dateInFilename]").attr("disabled",false);
						$("[id="+id+".timeInFilename]").attr("disabled",false);
						$("[id="+id+".dateTimeFormat]").attr("disabled",true);
					}
				}
			},
			btn : {
			    fieldsAdd : function(c){
					var rootId = c.getAttribute("rootId");
					
					var r = [
						{ id : rootId+'.fieldId', type : 'number' , text : '' },
						{ id : rootId+'.fieldName', type : 'input' , text : '' },
						{ id : rootId+'.fieldType', type : 'select' , text : '' },
						{ id : rootId+'.fieldFormat', type : 'input' , text : '' },
						{ id : rootId+'.fieldLength', type : 'input' , text : '' },
						{ id : rootId+'.fieldPrecision', type : 'input' , text : '' },
						{ id : rootId+'.fieldCurrency', type : 'input' , text : '' },
						{ id : rootId+'.fieldDecimal', type : 'input' , text : '' },
						{ id : rootId+'.fieldGroup', type : 'input' , text : '' },
						{ id : rootId+'.fieldTrimType', type : 'select' , text : '' },
						{ id : rootId+'.fieldNullif', type : 'input' , text : '' }
					];
					
					jQuery.imetabar.createRowByHeader(r,rootId);
			   },
			   getfields : function(e,v){
                    var r = [
						{ id : 'fieldId', type : 'number' , text : '' },
						{ id : 'fieldName', type : 'input' , text : 'fieldName' },
						{ id : 'fieldType', type : 'select' , text : 'type' },
						{ id : 'fieldFormat', type : 'input' , text : '' },
						{ id : 'fieldLength', type : 'input' , text : 'length' },
						{ id : 'fieldPrecision', type : 'input' , text : 'precision' },
						{ id : 'fieldCurrency', type : 'input' , text : '' },
						{ id : 'fieldDecimal', type : 'input' , text : 'decimal' },
						{ id : 'fieldGroup', type : 'input' , text : 'group' },
						{ id : 'fieldTrimType', type : 'select' , text : 'trimType' },
						{ id : 'fieldNullif', type : 'input' , text : '' }
					];
					jQuery.imeta.parameter.getfields(e,v,r);
				}
			}
		},
		fieldsplitter: {
		     btn : {
				   fieldsAdd : function(c){
				       var rootId = c.getAttribute("rootId");
					   var r = [
						   { id : rootId+'.fieldId', type : 'number' , text : ''},
						   { id : rootId+'.fieldName', type : 'input' , text : ''},
						   { id : rootId+'.fieldID', type : 'input' , text : '' },
						   { id : rootId+'.fieldRemoveID', type : 'select' , text : '' },
						   { id : rootId+'.fieldType', type : 'select' , text : '' },
						   { id : rootId+'.fieldLength', type : 'input' , text : '' },
						   { id : rootId+'.fieldPrecision', type : 'input' , text : '' },
						   { id : rootId+'.fieldFormat', type : 'input' , text : '' },
						   { id : rootId+'.fieldGroup', type : 'input' , text : '' },
						   { id : rootId+'.fieldDecimal', type : 'input' , text : '' },
						   { id : rootId+'.fieldCurrency', type : 'input' , text : '' },
						   { id : rootId+'.fieldNullIf', type : 'input' , text : '' },
						   { id : rootId+'.fieldIfNull', type : 'input' , text : '' },
						   { id : rootId+'.fieldTrimType', type : 'select' , text : '' }
					   ];
				
					   jQuery.imetabar.createRowByHeader(r,rootId);	 
				   }
			   }
		},
		valuemapper: {
		     btn : {
				   fieldAdd : function(c){
					   var rootId = c.getAttribute("rootId");
					   var r = [
						   { id : rootId+'.field', type : 'number' , text : '' },
						   { id : rootId+'.sourceValue', type : 'input' , text : '' },
						   { id : rootId+'.targetValue', type : 'input' , text : '' }
						   
					   ];
					
					   jQuery.imetabar.createRowByHeader(r,rootId);	 
				   }
			   }
		},
		selectvalues: {
		     btn : {
		     getchoosefields : function(e,v){
				 var r = [
                     { id : 'fieldId', type : 'number' , text : '' },
					 { id : 'selectName', type : 'input' , text : 'fieldName' },
					 { id : 'selectRename', type : 'input' , text : '' },
					 { id : 'selectLength', type : 'input' , text : 'length' },
					 { id : 'selectPrecision', type : 'input' , text : 'precision' }
                  ];
                 jQuery.imeta.parameter.getfields(e,v,r,'fields');
				},
				 getremovefields : function(e,v){
					var r = [
						{ id : 'fieldId', type : 'number' , text : '' },
						{ id : 'deleteName', type : 'input' , text : 'fieldName' }
					];
					jQuery.imeta.parameter.getfields(e,v,r,'removefields');
				},
				getmetadata : function(e,v){
					var r = [
                        { id : 'fieldId', type : 'number' , text : '' },
						{ id : 'name', type : 'input' , text : 'fieldName' },
						{ id : 'rename', type : 'input' , text : '' },
						{ id : 'type', type : 'select' , text : 'type' },
						{ id : 'length', type : 'input' , text : 'length' },
						{ id : 'precision', type : 'input' , text : 'precision' },
					    { id : 'storageType', type : 'select' , text : '' },
						{ id : 'conversionMask', type : 'input' , text : '' },
						{ id : 'decimalSymbol', type : 'input' , text : 'decimal' },
						{ id : 'groupingSymbol', type : 'input' , text : 'group' },
						{ id : 'currencySymbol', type : 'input' , text : '' }
                     ];
                       jQuery.imeta.parameter.getfields(e,v,r,'metadata');
				},
				   fieldAdd : function(c){
					   var rootId = c.getAttribute("rootId");
					   var r = [
						   { id : rootId+'.fieldId', type : 'number' , text : '' },
						   { id : rootId+'.selectName', type : 'input' , text : '' },
						   { id : rootId+'.selectRename', type : 'input' , text : '' },
						   { id : rootId+'.selectLength', type : 'input' , text : '' },
						   { id : rootId+'.selectPrecision', type : 'input' , text : '' }
						   
					   ];
					
					   jQuery.imetabar.createRowByHeader(r,rootId);
				   },
				   removeAdd : function(c){
					   var rootId = c.getAttribute("rootId");
					   var r = [
						   { id : rootId+'.fieldId', type : 'number' , text : '' },
						   { id : rootId+'.deleteName', type : 'input' , text : '' }
					   ];
					
					   jQuery.imetabar.createRowByHeader(r,rootId);
				   },
				   metaAdd : function(c){
					   var rootId = c.getAttribute("rootId");
					   var r = [
						   { id : rootId+'.fieldId', type : 'number' , text : '' },
						   { id : rootId+'.name', type : 'input' , text : '' },
						   { id : rootId+'.rename', type : 'input' , text : '' },
						   { id : rootId+'.type', type : 'select' , text : '' },
						   { id : rootId+'.length', type : 'input' , text : '' },
						   { id : rootId+'.precision', type : 'input' , text : '' },
						   { id : rootId+'.storageType', type : 'select' , text : '' },
						   { id : rootId+'.conversionMask', type : 'input' , text : '' },
						   { id : rootId+'.decimalSymbol', type : 'input' , text : '' },
						   { id : rootId+'.groupingSymbol', type : 'input' , text : '' },
						   { id : rootId+'.currencySymbol', type : 'input' , text : '' }
						   
					   ];
					
					  jQuery.imetabar.createRowByHeader(r,rootId);
				   }
			   }
		},
		rowgenerator : {
			btn : {
				fieldAdd : function(c){
					var rootId = c.getAttribute("rootId");
					var r = [
						{ id : rootId+'.fieldId', type : 'number' , text : '' },
						{ id : rootId+'.fieldName', type : 'input' , text : '' },
						{ id : rootId+'.fieldType', type : 'select' , text : '' },
						{ id : rootId+'.fieldFormat', type : 'input' , text : '' },
						{ id : rootId+'.fieldLength', type : 'input' , text : '' },
						{ id : rootId+'.fieldPrecision', type : 'input' , text : '' },
						{ id : rootId+'.fieldCurrency', type : 'input' , text : '' },
						{ id : rootId+'.fieldDecimal', type : 'input' , text : '' },
						{ id : rootId+'.fieldGroup', type : 'input' , text : '' },
						{ id : rootId+'.fieldNullif', type : 'input' , text : '' }
					];
					jQuery.imetabar.createRowByHeader(r,rootId);
				}
			}
		},
		calculator :{
			btn : {
				calculatorAdd : function(c){
					var rootId = c.getAttribute("rootId");
					var r = [
						{ id : rootId+'.fieldId', type : 'number' , text : '' },
						{ id : rootId+'.fieldName', type : 'input' , text : '' },
						{ id : rootId+'.calcType', type : 'select' , text : '' },
						{ id : rootId+'.fieldA', type : 'input' , text : '' },
						{ id : rootId+'.fieldB', type : 'input' , text : '' },
						{ id : rootId+'.fieldC', type : 'input' , text : '' },
						{ id : rootId+'.valueType', type : 'select' , text : '' },
						{ id : rootId+'.valueLength', type : 'input' , text : '' },
						{ id : rootId+'.valuePrecision', type : 'input' , text : '' },
						{ id : rootId+'.removedFromResult', type : 'select' , text : '' },
						{ id : rootId+'.conversionMask', type : 'input' , text : '' },
						{ id : rootId+'.decimalSymbol', type : 'input' , text : '' },
						{ id : rootId+'.groupingSymbol', type : 'input' , text : '' },
						{ id : rootId+'.currencySymbol', type : 'input' , text : '' }
					];
					
					jQuery.imetabar.createRowByHeader(r,rootId);
				}
			}
		},
		encrypt : {
		    btn : {
		        encryptAdd : function(c){
					var rootId = c.getAttribute("rootId");
					var r = [
						{ id : rootId+'.fieldId', type : 'number' , text : '' },
						{ id : rootId+'.fieldName', type : 'input' , text : '' },
						{ id : rootId+'.newFieldName', type : 'input' , text : '' },
						{ id : rootId+'.optionType', type : 'select' , text : '' }
					];
					
					jQuery.imetabar.createRowByHeader(r,rootId);
				},
				getfields : function(e,v){
					var r = [
						{ id : 'fieldId', type : 'number' , text : '' },
						{ id : 'fieldName', type : 'input' , text : 'fieldName' },
						{ id : 'newFieldName', type : 'input' , text : '' },
						{ id : 'optionType', type : 'select' , text : '' }
					];
					jQuery.imeta.parameter.getfields(e,v,r);
				}
		    }
		},
		constant : {
			btn : {
				constantAdd : function(c){
					var rootId = c.getAttribute("rootId");
					var r = [
						{ id : rootId+'.fieldId', type : 'number' , text : '' },
						{ id : rootId+'.fieldName', type : 'input' , text : '' },
						{ id : rootId+'.fieldType', type : 'select' , text : '' },
						{ id : rootId+'.fieldFormat', type : 'input' , text : '' },
						{ id : rootId+'.fieldLength', type : 'input' , text : '' },
						{ id : rootId+'.fieldPrecision', type : 'input' , text : '' },
						{ id : rootId+'.currency', type : 'input' , text : '' },
						{ id : rootId+'.decimal', type : 'input' , text : '' },
						{ id : rootId+'.group', type : 'input' , text : '' },
						{ id : rootId+'.value', type : 'input' , text : '' }
					];
					
					jQuery.imetabar.createRowByHeader(r,rootId);
				}
			}
		},
		sort : {
			btn : {
			
				getwords : function(e,v){
					var r = [
						{ id : 'fieldId', type : 'number' , text : '' },
						{ id : 'fieldName', type : 'input' , text : 'fieldName' },
						{ id : 'ascending', type : 'select' , text : 'ascending' },
						{ id : 'caseSensitive', type : 'select' , text : '' }
					];
					jQuery.imeta.parameter.getfields(e,v,r);
				},
				sortAdd : function(c){
					var rootId = c.getAttribute("rootId");
					var r = [
						{ id : rootId+'.fieldId', type : 'number' , text : '' },
						{ id : rootId+'.fieldName', type : 'input' , text : '' },
						{ id : rootId+'.ascending', type : 'select' , text : '' },
						{ id : rootId+'.caseSensitive', type : 'select' , text : '' }
					];
					
					jQuery.imetabar.createRowByHeader(r,rootId);
				}
			},
			listeners : {
			}
		},
		uniquerows : {
			btn : {
				getfields : function(e,v){
					var r = [
						{ id : 'fieldId', type : 'number' , text : '' },
						{ id : 'compareFields', type : 'input' , text : 'fieldName' },
						{ id : 'caseInsensitive', type : 'select' , text : '' }
					];
					jQuery.imeta.parameter.getfields(e,v,r);
				},
				uniquerowsAdd : function(c){
					var rootId = c.getAttribute("rootId");;
					var r = [
						{ id : rootId+'.fieldId', type : 'number' , text : '' },
						{ id : rootId+'.compareFields', type : 'input' , text : '' },
						{ id : rootId+'.caseInsensitive', type : 'select' , text : '' }
					];
					
					jQuery.imetabar.createRowByHeader(r,rootId);
				}
			},
			listeners : {
				countRows : function(e,v){
					var elId = e.target.id;
					var id = elId.split(".")[0];
					if(e.target.checked){
						$("[id="+id+".countField]").attr("disabled",false);
					}else{
						$("[id="+id+".countField]").attr("disabled",true);
					}
				}
			}
		},
		addsequence : {
			btn : {
			},
			listeners : {
				useDatabase : function(e,v){
					var elId = e.target.id;
					var id = elId.split(".")[0];
					if(e.target.checked){
						$("[id="+id+".connectionId]").attr("disabled",false);
						$("[id="+id+".schemaName]").attr("disabled",false);
						$("[id="+id+".sequenceName]").attr("disabled",false);
						$("[id="+id+".counterName]").attr("disabled",true);
						$("[id="+id+".startAt]").attr("disabled",true);
						$("[id="+id+".incrementBy]").attr("disabled",true);
						$("[id="+id+".maxValue]").attr("disabled",true);
						$("[id="+id+".useCounter]").attr("checked",false);
						
						
					}else{
						$("[id="+id+".connectionId]").attr("disabled",true);
						$("[id="+id+".schemaName]").attr("disabled",true);
						$("[id="+id+".sequenceName]").attr("disabled",true);
						$("[id="+id+".counterName]").attr("disabled",false);
						$("[id="+id+".startAt]").attr("disabled",false);
						$("[id="+id+".incrementBy]").attr("disabled",false);
						$("[id="+id+".maxValue]").attr("disabled",false);
						$("[id="+id+".useCounter]").attr("checked",true);
					}
				},
				useCounter : function(e,v){
					var elId = e.target.id;
					var id = elId.split(".")[0];
					if(e.target.checked){
						$("[id="+id+".connectionId]").attr("disabled",true);
						$("[id="+id+".schemaName]").attr("disabled",true);
						$("[id="+id+".sequenceName]").attr("disabled",true);
						$("[id="+id+".counterName]").attr("disabled",false);
						$("[id="+id+".startAt]").attr("disabled",false);
						$("[id="+id+".incrementBy]").attr("disabled",false);
						$("[id="+id+".maxValue]").attr("disabled",false);
						$("[id="+id+".useCounter]").attr("checked",true);
						$("[id="+id+".useDatabase]").attr("checked",false);
						
					}else{
						$("[id="+id+".connectionId]").attr("disabled",false);
						$("[id="+id+".schemaName]").attr("disabled",false);
						$("[id="+id+".sequenceName]").attr("disabled",false);
						$("[id="+id+".counterName]").attr("disabled",true);
						$("[id="+id+".startAt]").attr("disabled",true);
						$("[id="+id+".incrementBy]").attr("disabled",true);
						$("[id="+id+".maxValue]").attr("disabled",true);
						$("[id="+id+".useCounter]").attr("checked",false);
						$("[id="+id+".useDatabase]").attr("checked",true);
					}
				}
			}
		},
		xslt : {
			btn : {
			},
			listeners : {
				xslFileFieldUse : function(e,v){
					var elId = e.target.id;
					var id = elId.split(".")[0];
					if(e.target.checked){
						$("[id="+id+".xslFileField]").attr("disabled",false);
						$("[id="+id+".xslFilename]").attr("disabled",true);
					}else{
						$("[id="+id+".xslFileField]").attr("disabled",true);
						$("[id="+id+".xslFilename]").attr("disabled",false);
					}
				}
			}
		},
		xsd : {
			btn : {
				
			},
			listeners : {
				outStringFieldClick : function(e,v){
					var elId = e.target.id;
					var id = elId.split(".")[0];
					if(e.target.checked){
						$("[id="+id+".ifXmlValid]").attr("disabled",false);
						$("[id="+id+".ifXmlInvalid]").attr("disabled",false);
						
						
					}else{
						$("[id="+id+".ifXmlValid]").attr("disabled",true);
						$("[id="+id+".ifXmlInvalid]").attr("disabled",true);
					}
				},
				addMsgValidationClick : function(e,v){
					var elId = e.target.id;
					var id = elId.split(".")[0];
					if(e.target.checked){
						$("[id="+id+".validationMessageField]").attr("disabled",false);
					}else{
						$("[id="+id+".validationMessageField]").attr("disabled",true);
					}
				},
				xsdSourceChange : function(e){
					// alert(this.value);
					var elId = e.target.id;
					var id = elId.split(".")[0];
					if(this.value == 'filename'){
						$("[id="+id+".xsdFileName]").attr("disabled",false);
						$("[id="+id+".xsdDefinedField]").attr("disabled",true);
					}else if(this.value == 'fieldname'){
						$("[id="+id+".xsdFileName]").attr("disabled",true);
						$("[id="+id+".xsdDefinedField]").attr("disabled",false);
					}else if(this.value == 'noneed'){
						$("[id="+id+".xsdFileName]").attr("disabled",true);
						$("[id="+id+".xsdDefinedField]").attr("disabled",true);
					}
				}
			}
		},
		mailValidate : {
			btn : {
				
			},
			listeners : {
				smtpCheckClick : function(e,v){
					var elId = e.target.id;
					var id = elId.split(".")[0];
					if(e.target.checked){
						$("[id="+id+".timeout]").attr("disabled",false);
						$("[id="+id+".emailSender]").attr("disabled",false);
						$("[id="+id+".defaultSMTP]").attr("disabled",false);
						$("[id="+id+".isdynamicDefaultSMTP]").attr("disabled",false);
					}else{
						$("[id="+id+".timeout]").attr("disabled",true);
						$("[id="+id+".emailSender]").attr("disabled",true);
						$("[id="+id+".defaultSMTP]").attr("disabled",true);
						$("[id="+id+".isdynamicDefaultSMTP]").attr("disabled",true);
					}
				},
				isDynamicSMTPClick : function(e,v){
					var elId = e.target.id;
					var id = elId.split(".")[0];
					if(e.target.checked){
						$("[id="+id+".defaultSMTPField]").attr("disabled",false);
					}else{
						$("[id="+id+".defaultSMTPField]").attr("disabled",true);
					}
				},
				isResultAsStringClick : function(e,v){
					var elId = e.target.id;
					var id = elId.split(".")[0];
					if(e.target.checked){
						$("[id="+id+".emailValideMsg]").attr("disabled",false);
						$("[id="+id+".emailNotValideMsg]").attr("disabled",false);
					}else{
						$("[id="+id+".emailValideMsg]").attr("disabled",true);
						$("[id="+id+".emailNotValideMsg]").attr("disabled",true);
					}
				}
			}
		},
		aggregateRows : {
			btn : {
				fieldAdd : function(c){
					var rootId = c.getAttribute("rootId");
					var r = [
						{ id : rootId+'.fieldId', type : 'number' , text : '' },
						{ id : rootId+'.fieldName', type : 'input' , text : ''},
						{ id : rootId+'.fieldNewName', type : 'input' , text : ''},
						{ id : rootId+'.aggregateType', type : 'select' , text : ''}
					];
					jQuery.imetabar.createRowByHeader(r,rootId);
				},
				
				getfields : function(e,v){
					
												var r = [
                        							{ id : 'fieldId', type : 'number' , text : '' },
                        							{ id : 'fieldName', type : 'input' , text : 'fieldName' },
                        							{ id : 'fieldNewName', type : 'input' , text : 'fieldName' },
                        							{ id : 'aggregateType', type : 'select' , text : 'aggregateType' }
                        						];
                        						
												jQuery.imeta.parameter.getfields(e,v,r);
										
				}
			}
		},
		getPreviousRowField : {
			btn : {
				fieldAdd : function(c){
					var rootId = c.getAttribute("rootId");
					var r = [
						{ id : rootId+'.fieldId', type : 'number' , text : ''},
						{ id : rootId+'.input', type : 'input' , text : ''},
						{ id : rootId+'.output', type : 'input' , text : ''}
					];
					jQuery.imetabar.createRowByHeader(r,rootId);
				},
				
				getfields : function(e,v){
					
												var r = [
                        							{ id : 'fieldId', type : 'number' , text : '' },
                        							{ id : 'input', type : 'input' , text : 'fieldName' },
                        							{ id : 'output', type : 'input' , text : '' }
                        							
                        						];
												jQuery.imeta.parameter.getfields(e,v,r);
					
				}
			}
		},
		lucidDBBulkLoader : {
			btn : {
				fieldAdd : function(c){
					var rootId = c.getAttribute("rootId");
					var r = [
						{ id : rootId+'.fieldId', type : 'number' , text : '' },
						{ id : rootId+'.fieldTable', type : 'input' , text : '' },
						{ id : rootId+'.fieldStream', type : 'input' , text : '' },
						{ id : rootId+'.fieldFormatOk', type : 'select' , text : '' }
					];
					jQuery.imetabar.createRowByHeader(r,rootId);
				},
				getfields : function(e,v){
					var r = [
						{ id : 'fieldId', type : 'number' , text : '' },
						{ id : 'fieldTable', type : 'input' , text : 'fieldName' },
						{ id : 'fieldStream', type : 'input' , text : 'fieldName' },
						{ id : 'fieldFormatOk', type : 'select' , text : '' }
					];
					jQuery.imeta.parameter.getfields(e,v,r,'fieldsToLoad');
				}
			}
		},
		OraBulkLoader : {
			btn : {
				fieldAdd : function(c){
					var rootId = c.getAttribute("rootId");
					var r = [
						{ id : rootId+'.fieldId', type : 'number' , text : '' },
						{ id : rootId+'.fieldTable', type : 'input' , text : '' },
						{ id : rootId+'.fieldStream', type : 'select' , text : '' },
						{ id : rootId+'.dateMask', type : 'select' , text : '' }
					];
					jQuery.imetabar.createRowByHeader(r,rootId);
				},
				getfields : function(e,v){
					var r = [
						{ id : 'fieldId', type : 'number' , text : '' },
						{ id : 'fieldTable', type : 'input' , text : 'fieldName' },
						{ id : 'fieldStream', type : 'select' , text : 'fieldName' },
						{ id : 'dateMask', type : 'select' , text : '' }
					];
					jQuery.imeta.parameter.getfields(e,v,r);
				}
			}
		},
		
		group : {
			btn : {
				fieldAdd : function(c){
					var rootId = c.getAttribute("rootId");
					var r = [
						{ id : rootId+'.fieldId', type : 'number' , text : '' },
						{ id : rootId+'.fieldName', type : 'input' , text : '' }
					];
					jQuery.imetabar.createRowByHeader(r,rootId);
				},
				getfields : function(e,v){
					
												var r = [
                        							{ id : 'fieldId', type : 'number' , text : '' },
                        							{ id : 'fieldName', type : 'input' , text : 'fieldName' }
                        						];
												jQuery.imeta.parameter.getfields(e,v,r,'fields');
										
				},// end getFields
				
				aggregationAdd : function(c){
					var rootId = c.getAttribute("rootId");
					var r = [
						{ id : rootId+'.fieldId', type : 'number' , text : '' },
						{ id : rootId+'.aggregateField', type : 'input' , text : '' },
						{ id : rootId+'.subjectField', type : 'input' , text : '' },
						{ id : rootId+'.aggregateType', type : 'select' , text : '' },
						{ id : rootId+'.valueField', type : 'input' , text : '' }
					];
					jQuery.imetabar.createRowByHeader(r,rootId);
				},
				
				getAggregationfields : function(e,v){
					
												var r = [
                        							{ id : 'fieldId', type : 'number' , text : '' },
                        							{ id : 'aggregateField', type : 'input' , text : 'fieldName' },
                        							{ id : 'subjectField', type : 'input' , text : 'fieldName' },
                        							{ id : 'aggregateType', type : 'select' , text : '' },
                        							{ id : 'valueField', type : 'input' , text : '' }
                        						];
												jQuery.imeta.parameter.getfields(e,v,r,'aggregation');
											
				},// end getFields
				analyticFieldAdd : function(c){
					var rootId = c.getAttribute("rootId");
					var r = [
						{ id : rootId+'.fieldId', type : 'number' , text : '' },
						{ id : rootId+'.groupField', type : 'input' , text : '' }
					];
					jQuery.imetabar.createRowByHeader(r,rootId);
				},
				getAnalyticFields : function(e,v){
					
												var r = [
                        							{ id : 'fieldId', type : 'number' , text : '' },
                        							{ id : 'groupField', type : 'input' , text : 'fieldName' }
                        						];
												jQuery.imeta.parameter.getfields(e,v,r,'group');
					
						
					
				},// end getFields
				analyticFunctionsAdd : function(c){
					var rootId = c.getAttribute("rootId");
					var r = [
						{ id : rootId+'.fieldId', type : 'number' , text : '' },
						{ id : rootId+'.name', type : 'input' , text : '' },
						{ id : rootId+'.subject', type : 'select' , text : '' },
						{ id : rootId+'.type', type : 'select' , text : '' },
						{ id : rootId+'.value', type : 'select' , text : '' }
					];
					jQuery.imetabar.createRowByHeader(r,rootId);
				},
				getAnalyticFunctions : function(e,v){
					
												var r = [
                        							{ id : 'fieldId', type : 'number' , text : '' },
                        							{ id : 'name', type : 'input' , text : 'fieldName' },
                        							{ id : 'subject', type : 'select' , text : 'fieldName' },
                        							{ id : 'type', type : 'select' , text : '' },
                        							{ id : 'value', type : 'input' , text : '' }
                        						];
												jQuery.imeta.parameter.getfields(e,v,r,'analyticFunctions');
						
				},// end getFields
				statsAdd : function(c){
					var rootId = c.getAttribute("rootId");
					var r = [
						{ id : rootId+'.fieldId', type : 'number' , text : '' },
						{ id : rootId+'.fieldName', type : 'input' , text : '' },
						{ id : rootId+'.n', type : 'select' , text : '' },
						{ id : rootId+'.mean', type : 'select' , text : '' },
						{ id : rootId+'.stdDev', type : 'select' , text : '' },
						{ id : rootId+'.min', type : 'select' , text : '' },
						{ id : rootId+'.max', type : 'select' , text : '' },
						{ id : rootId+'.median', type : 'select' , text : '' },
						{ id : rootId+'.arbitraryPercentile', type : 'input' , text : '' },
						{ id : rootId+'.interpolatePercentile', type : 'select' , text : ''}
					];
					jQuery.imetabar.createRowByHeader(r,rootId);
				}
			},
			listeners : {
				isPassAllRowsClick : function(e,v){
					var elId = e.target.id;
					var id = elId.split(".")[0];
					if(e.target.checked){
						$("[id="+id+".directory]").attr("disabled",false);
						$("[id="+id+".prefix]").attr("disabled",false);
						$("[id="+id+".addingLineNrInGroup]").attr("disabled",false);
					}else{
						$("[id="+id+".directory]").attr("disabled",true);
						$("[id="+id+".prefix]").attr("disabled",true);
						$("[id="+id+".addingLineNrInGroup]").attr("disabled",true);
					}
				},
				isAddingLineNrInGroupClick : function(e,v){
					var elId = e.target.id;
					var id = elId.split(".")[0];
					if(e.target.checked){
						$("[id="+id+".lineNrInGroupField]").attr("disabled",false);
					}else{
						$("[id="+id+".lineNrInGroupField]").attr("disabled",true);
					}
				}
			}
		},
		
		validator : {
			btn : {
				newValidation : function(e,v){
					$.imessagebox("#ibody",{
						title : "输入一个新的验证名称",
						type : "prompt",
						marded : true,
						message : "输入一个唯一的验证规则的名称",
						fn : function(m,val){
						// 等待完成
						
						if(m == 'ok'){
							var elId = e.target.id;
							var id = elId.split(".")[0];
							var result = 'notexist';
							$("#" + id + "_select option").each(
								function(){
									if(this.text == val){
										$.imessagebox("#ibody",{
											title : "该验证名称已存在",
											type : "alert",
											marded : true,
											message : "该验证名称已存在",
											fn : function(m){}
										});
										result = 'exist';
										return false;
									}
								}
							);
							if(result == 'notexist'){
								var jsonObject = {
									valName : val,
									fieldName : "",
									errorCode : "",
									errorDescription : "",
									dataType : 0,
									dataTypeVerified : false,
									allowedValues : new Array(),
									conversionMask : "",
									decimalSymbol : "",
									groupingSymbol : "",
									nullAllowed : true,
									onlyNullAllowed : false,
									onlyNumericAllowed : false,
									maximumLength : "",
									minimumLength : "",
									maximumValue : "",
									minimumValue : "",
									startString : "",
									endString : "",
									startStringNotAllowed : "",
									endStringNotAllowed : "",
									regularExpression : "",
									regularExpressionNotAllowed : "",
									sourcingValues : false,
									sourcingStepName : 0,
									sourcingField : 0
								};
								var jsonStr = $.toJSON(jsonObject);
								$("[id=" + id + "_select]").append("<option value='" + jsonStr + "'>" + val + "</option>");
								$("#" + id + "_edit").removeClass("notDisplay");
								$("[id=" + id + "_select]").attr("value",jsonStr);
								var o = eval('(' + $("#" + id + "_select option:selected").val() + ')'); // 将option的json字符串转换成对象
								$("[id=" + id + ".valName]").val(o.valName);
								$("[id=" + id +".fieldName]").val(o.fieldName);
								$("[id=" + id +".errorCode]").val(o.errorCode);
								$("[id=" + id +".errorDescription]").val(o.errorDescription);
								$("[id=" + id +".dataTypeVerified]").attr("checked",o.dataTypeVerified);
								$("[id=" + id +".dataType]")[0].selectedIndex = o.dataType; 
								$("[id=" + id + ".conversionMask]").val(o.conversionMask);
								$("[id=" + id +".decimalSymbol]").val(o.decimalSymbol);
								$("[id=" + id +".groupingSymbol]").val(o.groupingSymbol);
								$("[id=" + id + ".nullAllowed]").attr("checked",o.nullAllowed);
								$("[id=" + id + ".onlyNullAllowed]").attr("checked",o.onlyNullAllowed);
								$("[id=" + id + ".onlyNumericAllowed]").attr("checked",o.onlyNumericAllowed);
								$("[id=" + id +".maximumLength]").val(o.maximumLength=='-1'?'':o.maximumLength);
								$("[id=" + id +".minimumLength]").val(o.minimumLength);
								$("[id=" + id + ".maximumValue]").val(o.maximumValue);
								$("[id=" + id +".minimumValue]").val(o.minimumValue);
								$("[id=" + id +".startString]").val(o.startString);
								$("[id=" + id + ".endString]").val(o.endString);
								$("[id=" + id +".startStringNotAllowed]").val(o.startStringNotAllowed);
								$("[id=" + id +".endStringNotAllowed]").val(o.endStringNotAllowed);
								$("[id=" + id +".regularExpression]").val(o.regularExpression);
								$("[id=" + id +".regularExpressionNotAllowed]").val(o.regularExpressionNotAllowed);
								$("[id=" + id + ".sourcingStepName]").val(o.sourcingStepName);
								$("[id=" + id +".sourcingValues]").attr("checked",o.sourcingValues);
								$("[id=" + id +".sourcingStepName]")[0].selectedIndex = o.sourcingStepName; 
								$("[id=" + id +".sourcingField]")[0].selectedIndex = o.sourcingField; 
								
								if(o.sourcingValues == true){
									$("[id="+id+"_allowedValues.btn.add]").attr("disabled",true);
									$("[id="+id+"_allowedValues.btn.delete]").attr("disabled",true);
									$("[id="+id+"_allowedValues.btn.add.root]").addClass("x-item-disabled");
									$("[id="+id+"_allowedValues.btn.delete.root]").addClass("x-item-disabled");
									$("#"+id+"_allowedValues_gRoot input").attr("disabled",true);
									$("[id="+id+".sourcingStepName]").attr("disabled",false);
									$("[id="+id+".sourcingField]").attr("disabled",false);
								}else{
									$("[id="+id+"_allowedValues.btn.add]").attr("disabled",false);
									$("[id="+id+"_allowedValues.btn.delete]").attr("disabled",false);
									$("[id="+id+"_allowedValues.btn.add.root]").removeClass("x-item-disabled");
									$("[id="+id+"_allowedValues.btn.delete.root]").removeClass("x-item-disabled");
									$("#"+id+"_allowedValues_gRoot input").attr("disabled",false);
									$("[id="+id+".sourcingStepName]").attr("disabled",true);
									$("[id="+id+".sourcingField]").attr("disabled",true);
								}
								if(o.allowedValues != null && o.allowedValues.length > 0){
									$("[id=" + id + "_allowedValues_gList]").empty();
									$("[id=" + id + "_allowedValues_gList]").attr("rowNr","0");
									var arr = o.allowedValues;
									for(var i = 0; i < arr.length; i++){
										var rowNr = $("[id=" + id + "_allowedValues_gList]").attr("rowNr");
										rowNr = (rowNr)?rowNr:0;
										var listDiv = $("<div class='x-grid-row' onmouseover='jQuery.imetabar.gridRowMouseOver(this);' onmouseout='jQuery.imetabar.gridRowMouseOut(this);' onclick='jQuery.imetabar.gridRowClick(this);'></div>");
										var r = [
										         { id : id +'_allowedValues.num', type : 'number' , text : ++rowNr, width : 50 },
										         { id : id +'_allowedValues.value', type : 'input' , text : arr[i], width : 100 }
												];
										$("[id=" + id + "_allowedValues_gList]").attr("rowNr",rowNr);
										jQuery.imetabar.createRow(r,null,listDiv);
										$("[id=" + id + "_allowedValues_gList]").append(listDiv);
									}
								}else{
									$("[id=" + id + "_allowedValues_gList]").empty();
									$("[id=" + id + "_allowedValues_gList]").attr("rowNr","0");
								}
								
								
							} // end if
						} // end if(m == 'ok')
					} // end function
				}); // end imessagebox
			},
				deleteValidation : function(e,v){
					var elId = e.target.id;
					var id = elId.split(".")[0];
					if($("#" + id + "_select option:selected").val() != undefined){
						$("#" + id + "_select option:selected").remove();
						$("#" + id + "_edit").addClass("notDisplay");
					}
					
				},
				valueAdd : function(c){
					var rootId = c.getAttribute("rootId");
					var r = [
						{ id : rootId+'.fieldId', type : 'number' , text : '' },
						{ id : rootId+'.value', type : 'input' , text : ''}
					];
					jQuery.imetabar.createRowByHeader(r,rootId);
				},
				ok : function(e,v){
					var elId = e.target.id;
					var id = elId.split(".")[0];
					// 点击确定后将当前编辑的内容赋值给当前选中的下拉项
					var valName = $("[id=" + id + ".valName]").val();
					var fieldName = $("[id=" + id +".fieldName]").val();
					var errorCode = $("[id=" + id +".errorCode]").val();
					var errorDescription = $("[id=" + id +".errorDescription]").val();
					var dataTypeVerified = $("[id=" + id +".dataTypeVerified]").attr("checked");
					var dataType = $("[id=" + id + ".dataType]").val() == '-1'?0:$("[id=" + id + ".dataType]").val();
					var conversionMask = $("[id=" + id + ".conversionMask]").val();
					var decimalSymbol = $("[id=" + id +".decimalSymbol]").val();
					var groupingSymbol = $("[id=" + id +".groupingSymbol]").val();
					var nullAllowed = $("[id=" + id +".nullAllowed]").attr("checked");
					var onlyNullAllowed = $("[id=" + id +".onlyNullAllowed]").attr("checked");
					var onlyNumericAllowed = $("[id=" + id +".onlyNumericAllowed]").attr("checked");
					var maximumLength = $("[id=" + id + ".maximumLength]").val();
					var minimumLength = $("[id=" + id +".minimumLength]").val();
					var maximumValue = $("[id=" + id +".maximumValue]").val();
					var minimumValue = $("[id=" + id + ".minimumValue]").val();
					var startString = $("[id=" + id +".startString]").val();
					var endString = $("[id=" + id +".endString]").val();
					var startStringNotAllowed = $("[id=" + id + ".startStringNotAllowed]").val();
					var endStringNotAllowed = $("[id=" + id +".endStringNotAllowed]").val();
					var regularExpression = $("[id=" + id +".regularExpression]").val();
					var regularExpressionNotAllowed = $("[id=" + id + ".regularExpressionNotAllowed]").val();
					var sourcingStepName = $("[id=" + id +".sourcingStepName]").val() == '-1'?0:$("[id=" + id + ".sourcingStepName]").val();
					var sourcingField = $("[id=" + id +".sourcingField]").val() == '-1'?0:$("[id=" + id + ".sourcingField]").val();
					var sourcingValues = $("[id=" + id +".sourcingValues]").attr("checked");
					var vals = document.getElementsByName(id + "_allowedValues.value");
					var allowedValue = new Array();
					if(vals  && vals.length > 0){
						for(var i = 0; i < vals.length; i++){
							allowedValue[i] = vals[i].value;
						}
					}
					var jsonObject = {
										valName : valName,
										fieldName : fieldName,
										errorCode : errorCode,
										errorDescription : errorDescription,
										dataTypeVerified : dataTypeVerified,
										dataType : dataType,
										allowedValues : allowedValue,
										conversionMask : conversionMask,
										decimalSymbol : decimalSymbol,
										groupingSymbol : groupingSymbol,
										nullAllowed : nullAllowed,
										onlyNullAllowed : onlyNullAllowed,
										onlyNumericAllowed : onlyNumericAllowed,
										maximumLength : maximumLength,
										minimumLength : minimumLength,
										maximumValue : maximumValue,
										minimumValue : minimumValue,
										startString : startString,
										endString : endString,
										startStringNotAllowed : startStringNotAllowed,
										endStringNotAllowed : endStringNotAllowed,
										regularExpression : regularExpression,
										regularExpressionNotAllowed : regularExpressionNotAllowed,
										sourcingValues : sourcingValues,
										sourcingStepName : sourcingStepName,
										sourcingField : sourcingField
									};
					var jsonStr = $.toJSON(jsonObject);
					if($("#" + id + "_select option:selected").val()!= undefined){
						$("#" + id + "_select option:selected").val(jsonStr);
					}
					else{
						$("#" + id + "_select :first").val(jsonStr);
					}
					var optionValue = "";
					$("#" + id + "_select option").each(
							function(){
									optionValue += this.value;
									optionValue += "---";
							}
						);
					
					if(optionValue.substr(optionValue.length-3) == '---')
						optionValue = optionValue.substr(0,optionValue.length-3); // 去掉最后一个，
					$("#"+id).ajaxSubmit({
						type:"POST",
						url:"ImetaAction!validatorElementSubmit.action",
						dataType:"json",
						data : {
							id : id,
							roType : jQuery.iPortalTab.OBJECT_TYPE_TRANS,
							roName : e.target.getAttribute("transName"),
							elementName : e.target.getAttribute("stepName"),
							directoryId : e.target.getAttribute("directoryId"),
							optionValue : optionValue
						},
						success : function(json){
							if(json.oldName != json.newName){
								var el = jQuery.imenu.iContent.getCanvasElByText(json.oldName,'step');
								jQuery.imenu.iContent.updateEl(el,{
									bText : [json.newName]
								});
								jQuery.imenu.iContent.redraw();
							}
							jQuery.iPortalTab.updateActiveTab(jQuery.iPortalTab.TYPE_ACTION_CHANGE_STEP);
							$("#window-"+id).remove();
							$.imessagebox("#ibody",json);
						}
					});
				}
				
			},
			listeners : {
				validationChange : function(e){
					var id = e.target.id.substr(0,e.target.id.lastIndexOf("_"));
					$("#" + id + "_edit").removeClass("notDisplay");
					var valName = $("[id=" + id + ".valName]").val();
					var fieldName = $("[id=" + id +".fieldName]").val();
					var errorCode = $("[id=" + id +".errorCode]").val();
					var errorDescription = $("[id=" + id +".errorDescription]").val();
					var dataTypeVerified = $("[id=" + id +".dataTypeVerified]").attr("checked");
					var dataType = $("[id=" + id + ".dataType]").val() == '-1'?0:$("[id=" + id + ".dataType]").val();
					var conversionMask = $("[id=" + id + ".conversionMask]").val();
					var decimalSymbol = $("[id=" + id +".decimalSymbol]").val();
					var groupingSymbol = $("[id=" + id +".groupingSymbol]").val();
					var nullAllowed = $("[id=" + id +".nullAllowed]").attr("checked");
					var onlyNullAllowed = $("[id=" + id +".onlyNullAllowed]").attr("checked");
					var onlyNumericAllowed = $("[id=" + id +".onlyNumericAllowed]").attr("checked");
					var maximumLength = $("[id=" + id + ".maximumLength]").val();
					var minimumLength = $("[id=" + id +".minimumLength]").val();
					var maximumValue = $("[id=" + id +".maximumValue]").val();
					var minimumValue = $("[id=" + id + ".minimumValue]").val();
					var startString = $("[id=" + id +".startString]").val();
					var endString = $("[id=" + id +".endString]").val();
					var startStringNotAllowed = $("[id=" + id + ".startStringNotAllowed]").val();
					var endStringNotAllowed = $("[id=" + id +".endStringNotAllowed]").val();
					var regularExpression = $("[id=" + id +".regularExpression]").val();
					var regularExpressionNotAllowed = $("[id=" + id + ".regularExpressionNotAllowed]").val();
					var sourcingStepName = $("[id=" + id +".sourcingStepName]").val() == '-1'?0:$("[id=" + id + ".sourcingStepName]").val();
					var sourcingField = $("[id=" + id +".sourcingField]").val() == '-1'?0:$("[id=" + id + ".sourcingField]").val();
					var sourcingValues = $("[id=" + id +".sourcingValues]").attr("checked");
					
					
					// 得到表格的值
					var vals = document.getElementsByName(id + "_allowedValues.value");
					var allowedValue = new Array();
					if(vals != null  && vals.length > 0){
						for(var i = 0; i < vals.length; i++){
							allowedValue[i] = vals[i].value;
						}
					}
					var jsonObject = {
										valName : valName,
										fieldName : fieldName,
										errorCode : errorCode,
										errorDescription : errorDescription,
										dataTypeVerified : dataTypeVerified,
										dataType : dataType,
										allowedValues : allowedValue,
										conversionMask : conversionMask,
										decimalSymbol : decimalSymbol,
										groupingSymbol : groupingSymbol,
										nullAllowed : nullAllowed,
										onlyNullAllowed : onlyNullAllowed,
										onlyNumericAllowed : onlyNumericAllowed,
										maximumLength : maximumLength,
										minimumLength : minimumLength,
										maximumValue : maximumValue,
										minimumValue : minimumValue,
										startString : startString,
										endString : endString,
										startStringNotAllowed : startStringNotAllowed,
										endStringNotAllowed : endStringNotAllowed,
										regularExpression : regularExpression,
										regularExpressionNotAllowed : regularExpressionNotAllowed,
										sourcingValues : sourcingValues,
										sourcingStepName : sourcingStepName,
										sourcingField : sourcingField
									};
					var jsonStr = $.toJSON(jsonObject);
					// 将当前输入的内容保存到该option的value中
					$("#" + id + "_select option").each(
							function(){
								if(this.text == valName){
									this.value = jsonStr;
									return false;
								}
							}
						);
					// 将选中的option中的value内容填充到相应的文本框中
					var o = eval('(' + $("#" + id + "_select option:selected").val() + ')'); // 将option的json字符串转换成对象
					$("[id=" + id + ".valName]").val(o.valName);
					$("[id=" + id +".fieldName]").val(o.fieldName);
					$("[id=" + id +".errorCode]").val(o.errorCode);
					$("[id=" + id +".errorDescription]").val(o.errorDescription);
					$("[id=" + id +".dataTypeVerified]").attr("checked",o.dataTypeVerified);
					$("[id=" + id +".dataType]")[0].selectedIndex = o.dataType; 
					$("[id=" + id + ".conversionMask]").val(o.conversionMask);
					$("[id=" + id +".decimalSymbol]").val(o.decimalSymbol);
					$("[id=" + id +".groupingSymbol]").val(o.groupingSymbol);
					$("[id=" + id + ".nullAllowed]").attr("checked",o.nullAllowed);
					$("[id=" + id + ".onlyNullAllowed]").attr("checked",o.onlyNullAllowed);
					$("[id=" + id + ".onlyNumericAllowed]").attr("checked",o.onlyNumericAllowed);
					$("[id=" + id +".maximumLength]").val(o.maximumLength=='-1'?'':o.maximumLength);
					$("[id=" + id +".minimumLength]").val(o.minimumLength);
					$("[id=" + id + ".maximumValue]").val(o.maximumValue);
					$("[id=" + id +".minimumValue]").val(o.minimumValue);
					$("[id=" + id +".startString]").val(o.startString);
					$("[id=" + id + ".endString]").val(o.endString);
					$("[id=" + id +".startStringNotAllowed]").val(o.startStringNotAllowed);
					$("[id=" + id +".endStringNotAllowed]").val(o.endStringNotAllowed);
					$("[id=" + id +".regularExpression]").val(o.regularExpression);
					$("[id=" + id +".regularExpressionNotAllowed]").val(o.regularExpressionNotAllowed);
					$("[id=" + id + ".sourcingStepName]").val(o.sourcingStepName);
					$("[id=" + id +".sourcingValues]").attr("checked",o.sourcingValues);
					$("[id=" + id +".sourcingStepName]")[0].selectedIndex = o.sourcingStepName; 
					$("[id=" + id +".sourcingField]")[0].selectedIndex = o.sourcingField; 
					
					if(o.sourcingValues == true){
						$("[id="+id+"_allowedValues.btn.add]").attr("disabled",true);
						$("[id="+id+"_allowedValues.btn.delete]").attr("disabled",true);
						$("[id="+id+"_allowedValues.btn.add.root]").addClass("x-item-disabled");
						$("[id="+id+"_allowedValues.btn.delete.root]").addClass("x-item-disabled");
						$("#"+id+"_allowedValues_gRoot input").attr("disabled",true);
						$("[id="+id+".sourcingStepName]").attr("disabled",false);
						$("[id="+id+".sourcingField]").attr("disabled",false);
					}else{
						$("[id="+id+"_allowedValues.btn.add]").attr("disabled",false);
						$("[id="+id+"_allowedValues.btn.delete]").attr("disabled",false);
						$("[id="+id+"_allowedValues.btn.add.root]").removeClass("x-item-disabled");
						$("[id="+id+"_allowedValues.btn.delete.root]").removeClass("x-item-disabled");
						$("#"+id+"_allowedValues_gRoot input").attr("disabled",false);
						$("[id="+id+".sourcingStepName]").attr("disabled",true);
						$("[id="+id+".sourcingField]").attr("disabled",true);
					}
					if(o.allowedValues != null && o.allowedValues.length > 0){
						$("[id=" + id + "_allowedValues_gList]").empty();
						$("[id=" + id + "_allowedValues_gList]").attr("rowNr","0");
						var arr = o.allowedValues;
						for(var i = 0; i < arr.length; i++){
							var rowNr = $("[id=" + id + "_allowedValues_gList]").attr("rowNr");
							rowNr = (rowNr)?rowNr:0;
							var listDiv = $("<div class='x-grid-row' onmouseover='jQuery.imetabar.gridRowMouseOver(this);' onmouseout='jQuery.imetabar.gridRowMouseOut(this);' onclick='jQuery.imetabar.gridRowClick(this);'></div>");
							var r = [
							         { id : id +'_allowedValues.num', type : 'number' , text : ++rowNr, width : 50 },
							         { id : id +'_allowedValues.value', type : 'input' , text : arr[i], width : 100 }
									];
							$("[id=" + id + "_allowedValues_gList]").attr("rowNr",rowNr);
							jQuery.imetabar.createRow(r,null,listDiv);
							$("[id=" + id + "_allowedValues_gList]").append(listDiv);
						}
					}else{
						$("[id=" + id + "_allowedValues_gList]").empty();
						$("[id=" + id + "_allowedValues_gList]").attr("rowNr","0");
					}
				},
				validationClick : function(e){
					//alert(e.target.id);
					var id = e.target.id.substr(0,e.target.id.lastIndexOf("_"));
					$("#" + id + "_edit").removeClass("notDisplay");
				},
				valNameChange : function(e,v){
					var elId = e.target.id;
					var id = elId.split(".")[0];
					$("#" + id + "_select option:selected").text($("[id=" + id + ".valName]").val());
				},
				isSourcingValuesClick : function(e,v){
					var elId = e.target.id;
					var id = elId.split(".")[0];
					if(!e.target.checked){
						$("[id="+id+"_allowedValues.btn.add]").attr("disabled",false);
						$("[id="+id+"_allowedValues.btn.delete]").attr("disabled",false);
						$("[id="+id+"_allowedValues.btn.add.root]").removeClass("x-item-disabled");
						$("[id="+id+"_allowedValues.btn.delete.root]").removeClass("x-item-disabled");
						$("#"+id+"_allowedValues_gRoot input").attr("disabled",false);
						$("[id="+id+".sourcingStepName]").attr("disabled",true);
						$("[id="+id+".sourcingField]").attr("disabled",true);
					}else{
						$("[id="+id+"_allowedValues.btn.add]").attr("disabled",true);
						$("[id="+id+"_allowedValues.btn.delete]").attr("disabled",true);
						$("[id="+id+"_allowedValues.btn.add.root]").addClass("x-item-disabled");
						$("[id="+id+"_allowedValues.btn.delete.root]").addClass("x-item-disabled");
						$("#"+id+"_allowedValues_gRoot input").attr("disabled",true);
						$("[id="+id+".sourcingStepName]").attr("disabled",false);
						$("[id="+id+".sourcingField]").attr("disabled",false);
					}
				}
			}
		},
		splitfieldtorows : {
			btn : {
			},
			listeners : {
				includeRowNumber : function(e,v){
					var elId = e.target.id;
					var id = elId.split(".")[0];
					if(e.target.checked){
						$("[id="+id+".rowNumberField]").attr("disabled",false);
						$("[id="+id+".resetRowNumber]").attr("disabled",false);
					}else{
						$("[id="+id+".rowNumberField]").attr("disabled",true);
						$("[id="+id+".resetRowNumber]").attr("disabled",true);
					}
				}
			}
		},
		denormaliser : {
			btn : {
			    getwords : function(e,v){
			       var r = [
						{ id : 'fieldId', type : 'number' , text : '' },
						{ id : 'groupField', type : 'input' , text :'fieldName' }
					];
					jQuery.imeta.parameter.getfields(e,v,r,'consistwords');
				},
				getdemandwords : function(e,v){
					var r = [
						{ id : 'fieldId', type : 'number' , text : '' },
						{ id : 'targetName', type : 'input' , text : '' },
						{ id : 'fieldName', type : 'input' , text : 'fieldName' },
						{ id : 'keyValue', type : 'input' , text : '' },
						{ id : 'targetType', type : 'select' , text : 'type' },
						{ id : 'targetFormat', type : 'input' , text : '' },
						{ id : 'targetLength', type : 'input' , text : 'length' },
						{ id : 'targetPrecision', type : 'input' , text : 'precision' },
						{ id : 'targetCurrencySymbol', type : 'input' , text : '' },
						{ id : 'targetDecimalSymbol', type : 'input' , text : 'decimal' },
						{ id : 'targetGroupingSymbol', type : 'input' , text : 'group' },
						{ id : 'targetNullString', type : 'input' , text : '' },
						{ id : 'targetAggregationType', type : 'select' , text : '' }
                      	];
                       						
                        jQuery.imeta.parameter.getfields(e,v,r,'obwords');
											
				},
				consistwordsAdd : function(c){
					var rootId = c.getAttribute("rootId");
					var r = [
						{ id : rootId+'.fieldId', type : 'number' , text : '' },
						{ id : rootId+'.groupField', type : 'input' , text : '' }
					];
					
					jQuery.imetabar.createRowByHeader(r,rootId);
				},
				obwordsAdd : function(c){
					var rootId = c.getAttribute("rootId");
					
					var r = [
						{ id : rootId+'.fieldId', type : 'number' , text : '' },
						{ id : rootId+'.targetName', type : 'input' , text : '' },
						{ id : rootId+'.fieldName', type : 'input' , text : '' },
						{ id : rootId+'.keyValue', type : 'input' , text : '' },
						{ id : rootId+'.targetType', type : 'select' , text : '' },
						{ id : rootId+'.targetFormat', type : 'input' , text : '' },
						{ id : rootId+'.targetLength', type : 'input' , text : '' },
						{ id : rootId+'.targetPrecision', type : 'input' , text : '' },
						{ id : rootId+'.targetCurrencySymbol', type : 'input' , text : '' },
						{ id : rootId+'.targetDecimalSymbol', type : 'input' , text : '' },
						{ id : rootId+'.targetGroupingSymbol', type : 'input' , text : '' },
						{ id : rootId+'.targetNullString', type : 'input' , text : '' },
						{ id : rootId+'.targetAggregationType', type : 'select' , text : '' }
					];
					
					jQuery.imetabar.createRowByHeader(r,rootId);
				}
			}
		},
		normaliser : {
			btn : {
			    getfields : function(e,v){
				   var r = [
                       { id : 'fieldId', type : 'number' , text : '' },
					   { id : 'fieldName', type : 'input' , text : 'fieldName'},
					   { id : 'fieldValue', type : 'input' , text : 'fieldName' },
					   { id : 'fieldNorm', type : 'input' , text : ''}
                    ];
                    jQuery.imeta.parameter.getfields(e,v,r);
				},
				normaliserAdd : function(c){
					var rootId = c.getAttribute("rootId");
					var r = [
						{ id : rootId+'.fieldId', type : 'number' , text : '' },
						{ id : rootId+'.fieldName', type : 'input' , text : ''},
						{ id : rootId+'.fieldValue', type : 'input' , text : '' },
						{ id : rootId+'.fieldNorm', type : 'input' , text : ''}
					];
					
					jQuery.imetabar.createRowByHeader(r,rootId);
				}
			}
		},
		flattener : {
			btn : {
				flattenerAdd : function(c){
					var rootId = c.getAttribute("rootId");
					var r = [
						{ id : rootId+'.fieldId', type : 'number' , text : '' },
						{ id : rootId+'.targetField', type : 'input' , text : '' }
					];
					
					jQuery.imetabar.createRowByHeader(r,rootId);
				}
			}
		},
		checksum : {
			btn : {
			   getwords : function(e,v){
				  var r = [
                     { id : 'fieldId', type : 'number' , text : ''},
				     { id : 'fieldName', type : 'input' , text : 'fieldName' }
                  ];
                  jQuery.imeta.parameter.getfields(e,v,r);
				},
				checksumAdd : function(c){
					var rootId = c.getAttribute("rootId");
					var r = [
						{ id : rootId+'.fieldId', type : 'number' , text : ''},
						{ id : rootId+'.fieldName', type : 'select' , text : '' }
					];
					
					jQuery.imetabar.createRowByHeader(r,rootId);
				}
			}
		},
		numberrange : {
			btn : {
				numberrangeAdd : function(c){
					var rootId = c.getAttribute("rootId");
					var r = [
						{ id : rootId+'.fieldId', type : 'number' , text : '' },
						{ id : rootId+'.lowerBoundStr', type : 'input' , text : '' },
						{ id : rootId+'.upperBoundStr', type : 'input' , text : '' },
						{ id : rootId+'.value', type : 'input' , text : '' }
					];
					
					jQuery.imetabar.createRowByHeader(r,rootId);
				}
			}
		},
		replacestring : {
			btn : {
			    getfields : function(e,v){
				   var r = [
                      { id : 'fieldId', type : 'number' , text : '' },
					  { id : 'fieldInStream', type : 'input' , text : 'fieldName' },
					  { id : 'fieldOutStream', type : 'input' , text : '' },
					  { id : 'useRegEx', type : 'select' , text : '' },
					  { id : 'replaceString', type : 'input' , text : '' },
					  { id : 'replaceByString', type : 'input' , text : '' },
					  { id : 'wholeWord', type : 'select' , text : '' },
					  { id : 'caseSensitive', type : 'select' , text : '' }
                      ];
                       jQuery.imeta.parameter.getfields(e,v,r);
				},
				replacestringAdd : function(c){
						var rootId = c.getAttribute("rootId");
						var r = [
						{ id : rootId+'.fieldId', type : 'number' , text : '' },
						{ id : rootId+'.fieldInStream', type : 'input' , text : '' },
						{ id : rootId+'.fieldOutStream', type : 'input' , text : '' },
						{ id : rootId+'.useRegEx', type : 'select' , text : '' },
						{ id : rootId+'.replaceString', type : 'input' , text : '' },
						{ id : rootId+'.replaceByString', type : 'input' , text : '' },
						{ id : rootId+'.wholeWord', type : 'select' , text : '' },
						{ id : rootId+'.caseSensitive', type : 'select' , text : '' }
						];
						
						jQuery.imetabar.createRowByHeader(r,rootId);
					}
			  }
		},
		stringcut : {
			btn : {
			getfields : function(e,v){
				   var r = [
                        { id : 'fieldId', type : 'number' , text : '' },
						{ id : 'fieldInStream', type : 'input' , text : 'fieldName' },
						{ id : 'fieldOutStream', type : 'input' , text : '' },
						{ id : 'cutFrom', type : 'input' , text : '' },
						{ id : 'cutTo', type : 'input' , text : '' }
                      ];
                       jQuery.imeta.parameter.getfields(e,v,r);
				},
				stringcutAdd : function(c){
					var rootId = c.getAttribute("rootId");
					var r = [
						{ id : rootId+'.fieldId', type : 'number' , text : '' },
						{ id : rootId+'.fieldInStream', type : 'input' , text : '' },
						{ id : rootId+'.fieldOutStream', type : 'input' , text : '' },
						{ id : rootId+'.cutFrom', type : 'input' , text : '' },
						{ id : rootId+'.cutTo', type : 'input' , text : '' }
					];
					
					jQuery.imetabar.createRowByHeader(r,rootId);
				}
			}
		},
		systemdata : {
			btn : {
				systemdataAdd : function(c){
					var rootId = c.getAttribute("rootId");
					var r = [
						{ id : rootId+'.fieldId', type : 'number' , text : '' },
						{ id : rootId+'.fieldName', type : 'input' , text : '' },
						{ id : rootId+'.fieldType', type : 'select' , text : '' }
					];
					
					jQuery.imetabar.createRowByHeader(r,rootId);
				}
			}
		},
		nullif : {
			btn : {
			getfields : function(e,v){
			   var r = [
                   { id : 'fieldId', type : 'number' , text : '' },
				   { id : 'fieldName', type : 'input' , text : 'fieldName' },
				   { id : 'fieldValue', type : 'input' , text : '' }
                 ];
                  jQuery.imeta.parameter.getfields(e,v,r);
				},
				nullifAdd : function(c){
					var rootId = c.getAttribute("rootId");
					var r = [
						{ id : rootId+'.fieldId', type : 'number' , text : '' },
						{ id : rootId+'.fieldName', type : 'input' , text : '' },
						{ id : rootId+'.fieldValue', type : 'input' , text : '' }
					];
					
					jQuery.imetabar.createRowByHeader(r,rootId);
				}
			}
		},
		writetolog : {
			btn : {
			getwords : function(e,v){
				 var r = [
                     { id : 'fieldId', type : 'number' , text : '' },
					 { id : 'fieldName', type : 'input' , text : 'fieldName' }
                     ];
                    jQuery.imeta.parameter.getfields(e,v,r);
				},
				writetologAdd : function(c){
					var rootId = c.getAttribute("rootId");
					var r = [
						{ id : rootId+'.fieldId', type : 'number' , text : '' },
						{ id : rootId+'.fieldName', type : 'input' , text : '' }
					];
					
					jQuery.imetabar.createRowByHeader(r,rootId);
				}
			}
		},
		ifnull : {
			btn : {
			    getfields : function(e,v){
					 var r = [
                         { id : 'fieldId', type : 'number' , text : '' },
						 { id : 'fieldName', type : 'input' , text : 'fieldName' },
						 { id : 'replaceValue', type : 'input' , text : '' },
						 { id : 'replaceMask', type : 'input' , text : '' }
                        ];
                    jQuery.imeta.parameter.getfields(e,v,r);
				},
				ifnullTypeAdd : function(c){
					var rootId = c.getAttribute("rootId");
					var r = [
						{ id : rootId+'.typeId', type : 'number' , text : '' },
						{ id : rootId+'.typeName', type : 'input' , text : '' },
						{ id : rootId+'.typereplaceValue', type : 'input' , text : '' },
						{ id : rootId+'.typereplaceMask', type : 'input' , text : '' }
					];
					
					jQuery.imetabar.createRowByHeader(r,rootId);
				},
				ifnullFieldsAdd : function(c){
					var rootId = c.getAttribute("rootId");
					var r = [
						{ id : rootId+'.fieldId', type : 'number' , text : '' },
						{ id : rootId+'.fieldName', type : 'input' , text : '' },
						{ id : rootId+'.replaceValue', type : 'input' , text : '' },
						{ id : rootId+'.replaceMask', type : 'input' , text : '' }
					];
					
					jQuery.imetabar.createRowByHeader(r,rootId);
				}
			},
			listeners : {
				selectFields : function(e,v){
					var elId = e.target.id;
					var id = elId.split(".")[0];
					if(e.target.checked){
					    
						
                        $("[id="+id+"_fields.btn.add]").attr("disabled",false);
						$("[id="+id+"_fields.btn.delete]").attr("disabled",false);
						$("[id="+id+"_fields.btn.add.root]").removeClass("x-item-disabled");
						$("[id="+id+"_fields.btn.delete.root]").removeClass("x-item-disabled");
						$("#"+id+"_fields_gRoot input").attr("disabled",false);
						
						$("[id="+id+"_types.btn.add]").attr("disabled",true);
						$("[id="+id+"_types.btn.delete]").attr("disabled",true);
						$("[id="+id+"_types.btn.add.root]").addClass("x-item-disabled");
						$("[id="+id+"_types.btn.delete.root]").addClass("x-item-disabled");
						$("#"+id+"_types_gRoot input").attr("disabled",true);
						
						$("[id="+id+".replaceAllByValue]").attr("disabled",true);
						$("[id="+id+".replaceAllMask]").attr("disabled",true);
						$("[id="+id+".getfields]").attr("disabled",false);
					    $("[id="+id+".selectValuesType]").attr("checked",false);
					    
						
					}else{
					    
                        $("[id="+id+"_fields.btn.add]").attr("disabled",true);
						$("[id="+id+"_fields.btn.delete]").attr("disabled",true);
						$("[id="+id+"_fields.btn.add.root]").addClass("x-item-disabled");
						$("[id="+id+"_fields.btn.delete.root]").addClass("x-item-disabled");
						$("#"+id+"_fields_gRoot input").attr("disabled",true);
						
						$("[id="+id+"_types.btn.add]").attr("disabled",false);
						$("[id="+id+"_types.btn.delete]").attr("disabled",false);
						$("[id="+id+"_types.btn.add.root]").addClass("x-item-disabled");
						$("[id="+id+"_types.btn.delete.root]").addClass("x-item-disabled");
						$("#"+id+"_types_gRoot input").attr("disabled",false);
						
					    $("[id="+id+".replaceAllByValue]").attr("disabled",false);
						$("[id="+id+".replaceAllMask]").attr("disabled",false);
						$("[id="+id+".getfields]").attr("disabled",true);
						
						
					
					}
				},
				selectValuesType : function(e,v){
					var elId = e.target.id;
					var id = elId.split(".")[0];
					if(e.target.checked){
						
						$("[id="+id+"_fields.btn.add]").attr("disabled",true);
						$("[id="+id+"_fields.btn.delete]").attr("disabled",true);
						$("[id="+id+"_fields.btn.add.root]").addClass("x-item-disabled");
						$("[id="+id+"_fields.btn.delete.root]").addClass("x-item-disabled");
						$("#"+id+"_fields_gRoot input").attr("disabled",true);
						
						$("[id="+id+"_types.btn.add]").attr("disabled",false);
						$("[id="+id+"_types.btn.delete]").attr("disabled",false);
						$("[id="+id+"_types.btn.add.root]").removeClass("x-item-disabled");
						$("[id="+id+"_types.btn.delete.root]").removeClass("x-item-disabled");
						$("#"+id+"_types_gRoot input").attr("disabled",false);
						$("[id="+id+".replaceAllByValue]").attr("disabled",true);
						$("[id="+id+".replaceAllMask]").attr("disabled",true);
					    $("[id="+id+".selectFields]").attr("checked",false);
					    
					    $("[id="+id+".getfields]").attr("disabled",true);
					}else{
					
					    $("[id="+id+"_fields.btn.add]").attr("disabled",false);
						$("[id="+id+"_fields.btn.delete]").attr("disabled",false);
						$("[id="+id+"_fields.btn.add.root]").addClass("x-item-disabled");
						$("[id="+id+"_fields.btn.delete.root]").addClass("x-item-disabled");
						$("#"+id+"_fields_gRoot input").attr("disabled",false);
						
                        $("[id="+id+"_types.btn.add]").attr("disabled",true);
						$("[id="+id+"_types.btn.delete]").attr("disabled",true);
						$("[id="+id+"_types.btn.add.root]").addClass("x-item-disabled");
						$("[id="+id+"_types.btn.delete.root]").addClass("x-item-disabled");
						$("#"+id+"_types_gRoot input").attr("disabled",true);
						
					    $("[id="+id+".replaceAllByValue]").attr("disabled",false);
						$("[id="+id+".replaceAllMask]").attr("disabled",false);
						
						$("[id="+id+".getfields]").attr("disabled",true);
					
					}
				}
			}
		},
		switchcase : {
			btn : {
				caseValuesAdd : function(c){
					var rootId = c.getAttribute("rootId");
					var r = [
						{ id : rootId+'.caseId', type : 'number' , text :'' },
						{ id : rootId+'.caseValues', type : 'input' , text : '' },
						{ id : rootId+'.caseTargetStepnames', type : 'select' , text : '' }
					];
					
					jQuery.imetabar.createRowByHeader(r,rootId);
				}
			}
		},
		mappinginput : {
			btn : {
				fieldAdd : function(c){
					var rootId = c.getAttribute("rootId");
					var r = [
						{ id : rootId+'.fieldId', type : 'number' , text :'' },
						{ id : rootId+'.fieldName', type : 'input' , text : '' },
						{ id : rootId+'.fieldType', type : 'select' , text : '' },
						{ id : rootId+'.fieldLength', type : 'input' , text : '' },
						{ id : rootId+'.fieldPrecision', type : 'input' , text : '' }
					];
					
					jQuery.imetabar.createRowByHeader(r,rootId);
				}
			}
		},
		mapping : {
			btn : {
				fieldAdd : function(c){
					var rootId = c.getAttribute("rootId");
					var r = [
						{ id : rootId+'.fieldId', type : 'number' , text :'' },
						{ id : rootId+'.fieldName', type : 'input' , text : '' },
						{ id : rootId+'.fieldType', type : 'select' , text : '' }
					];
					
					jQuery.imetabar.createRowByHeader(r,rootId);
				}
			}
		},
		mergerows : {
			
			btn : {
				matchkeywordsAdd : function(c){
					var rootId = c.getAttribute("rootId");
					var r = [
						{ id : rootId+'.fieldId', type : 'number' , text : '' },
						{ id : rootId+'.fieldName', type : 'input' , text : ''}
					];
					jQuery.imetabar.createRowByHeader(r,rootId);
				},
				dateFieldAdd : function(c){
					var rootId = c.getAttribute("rootId");
					var r = [
						{ id : rootId+'.fieldId', type : 'number' , text : '' },
						{ id : rootId+'.valueFields', type : 'input' , text : ''}
					];
					jQuery.imetabar.createRowByHeader(r,rootId);
				},
				getKeywords : function(e,v){
					
					var r = [
			         { id :'fieldId', type : 'number' , text : '' },
			         { id : 'fieldName', type : 'input' , text : 'fieldName' }
			        ];
            						
					jQuery.imeta.parameter.getfields(e,v,r,'matchkeywords','info');
			
				},
				getValue : function(e,v){
					
													var r = [
													         { id :'fieldId', type : 'number' , text : '' },
													         { id : 'valueFields', type : 'input' , text : 'fieldName' }
													         ];
                        						
													jQuery.imeta.parameter.getfields(e,v,r,'dateField','info');
					
				}// end getField
			}
		},
		mergejoin : {
			btn : {
				stepAGridAdd : function(c){
					var rootId = c.getAttribute("rootId");
					var r = [
						        { id : rootId+'.fieldId', type : 'number' , text : '' },
						        { id : rootId+'.value', type : 'input' , text : ''}
						    ];
					jQuery.imetabar.createRowByHeader(r,rootId);
				},
				stepBGridAdd : function(c){
					var rootId = c.getAttribute("rootId");
					var r = [
						        { id : rootId+'.fieldId', type : 'number' , text : '' },
						        { id : rootId+'.value', type : 'input' , text : ''}
						    ];
					jQuery.imetabar.createRowByHeader(r,rootId);
				},
				getStep1Field : function(e,v){
					
													var r = [
													         { id : 'fieldId', type : 'number' , text : '' },
													         { id : 'value', type : 'input' , text : 'fieldName' }
													         ];
                        						
													jQuery.imeta.parameter.getfields(e,v,r,'stepAGrid','info');
						
				},// end getField
				getStep2Field : function(e,v){
					
													var r = [
													         { id : 'fieldId', type : 'number' , text : '' },
													         { id : 'value', type : 'input' , text : 'fieldName'}
													         ];
                        						
													jQuery.imeta.parameter.getfields(e,v,r,'stepBGrid','info');
											
				}// end getField
			}
		},
		sortedmerge : {
			btn : {
				fieldsAdd : function(c){
					var rootId = c.getAttribute("rootId");
					var r = [
						{ id : rootId+'.fieldId', type : 'number' , text : '' },
						{ id : rootId+'.fieldName', type : 'input' , text : ''},
						{ id : rootId+'.ascending', type : 'select' , text : ''}
					];
					jQuery.imetabar.createRowByHeader(r,rootId);
				},
				getfields : function(e,v){
					
												var r = [
                        							{ id : 'fieldId', type : 'number' , text : '' },
                        							{ id : 'fieldName', type : 'input' , text : 'fieldName' },
                        							{ id : 'ascending', type : 'select' , text : 'ascending' }
                        							
                        						];
												jQuery.imeta.parameter.getfields(e,v,r);
					
				}// end getFields
				
			}
		},
		xmljoin : {
			btn : {
				
			},
			listeners : {
				isComplexJoinClick : function(e,v){
					var elId = e.target.id;
					var id = elId.split(".")[0];
					if(e.target.checked){
						$("[id="+id+".joinCompareField]").attr("disabled",false);
					}else{
						$("[id="+id+".joinCompareField]").attr("disabled",true);
					}
				}
			}
		},
		dimensionlookup : {
			btn : {
				getfields : function(e,v){
					 var r = [
                        	{ id : rootId+'.keysId', type : 'number' , text : '' },
							{ id : rootId+'.keyStream', type : 'select' , text :''},
							{ id : rootId+'.keyLookup', type : 'select' , text :''}
                      ];
                   jQuery.imetabar.createRowByHeader(e,v,r);
			   },
			    keyWordsAdd : function(c){
					var rootId = c.getAttribute("rootId");
					var r = [
						{ id : rootId+'.keysId', type : 'number' , text : '' },
						{ id : rootId+'.keyStream', type : 'select' , text : ''},
						{ id : rootId+'.keyLookup', type : 'select' , text : ''}
					];
					
					jQuery.imetabar.createRowByHeader(r,rootId);
				},
				queryUpdateFieldAdd : function(c){
					var rootId = c.getAttribute("rootId");
					var r = [
						{ id : rootId+'.fieldsId', type : 'number' , text :'' },
						{ id : rootId+'.fieldStream', type : 'input' , text : ''},
						{ id : rootId+'.fieldLookup', type : 'input' , text : ''},
						{ id : rootId+'.fieldUpdate', type : 'select' , text : ''}
					];
					
					jQuery.imetabar.createRowByHeader(r,rootId);
				}
			}
		},
		update : {
			btn : {
				keywordsAdd : function(c){
				    var rootId = c.getAttribute("rootId");
					var r = [
						{ id : rootId+'.fieldId', type : 'number' , text :''},
						{ id : rootId+'.keyLookup', type : 'input' , text : ''},		
						{ id : rootId+'.keyCondition', type : 'input' , text : '' },
						{ id : rootId+'.keyStream', type : 'input' , text : '' },
						{ id : rootId+'.keyStream2', type : 'input' , text : '' }
					];
				
					jQuery.imetabar.createRowByHeader(r,rootId);
				},
				refreshwordsAdd : function(c){
					var rootId = c.getAttribute("rootId");
					var r = [
						{ id : rootId+'.fieldId', type : 'number' , text :'' },
						{ id : rootId+'.updateLookup', type : 'input' , text : '' },
						{ id : rootId+'.updateStream', type : 'input' , text : '' }
					];
				
					jQuery.imetabar.createRowByHeader(r,rootId);
				},
				getfields : function(e,v){
				  var r = [
					 { id : 'fieldId', type : 'number' , text : '' },
					 { id : 'keyLookup', type : 'input' , text : '' },
					 { id : 'keyCondition', type : 'input' , text :'' },
					 { id : 'keyStream', type : 'input' , text : '' },
					 { id : 'keyStream2', type : 'input' , text : ''}
         				 ];
     				  jQuery.imeta.parameter.getfields(e,v,r,'keywords');

				},
				getRefreshFields : function(e,v){
					var r = [
                      { id :'fieldId', type : 'number' , text : '' },
                      { id :'updateLookup', type : 'input' , text : ''},
                      { id :'updateStream', type : 'input' , text :'' }
                            ];
                  jQuery.imeta.parameter.getfields(e,v,r,'refreshwords');				
				}
			},
			listeners : {
				skipLookup : function(e,v){
					var elId = e.target.id;
					var id = elId.split(".")[0];
					if(e.target.checked){
						$("[id="+id+".errorIgnored]").attr("disabled",true);
					}else{
						$("[id="+id+".errorIgnored]").attr("disabled",false);
					
					}
				},
				errorIgnored : function(e,v){
					var elId = e.target.id;
					var id = elId.split(".")[0];
					if(e.target.checked){
						$("[id="+id+".ignoreFlagField]").attr("disabled",false);
					}else{
						$("[id="+id+".ignoreFlagField]").attr("disabled",true);
					
					}
				}
			}
		},
		xmloutput : {
			btn : {
				wordsAdd : function(c){
					var rootId = c.getAttribute("rootId");
					var r = [
						{ id : rootId+'.fieldId', type : 'number' , text : '' },
						{ id : rootId+'.fieldName', type : 'input' , text : ''},
						{ id : rootId+'.fieldType', type : 'select' , text : ''},
						{ id : rootId+'.fieldElement', type : 'input' , text : ''},
						{ id : rootId+'.fieldFormat', type : 'input' , text : ''},
						{ id : rootId+'.fieldLength', type : 'input' , text : ''},
						{ id : rootId+'.fieldPrecision', type : 'input' , text : ''},
						{ id : rootId+'.fieldCurrency', type : 'input' , text : ''},
						{ id : rootId+'.fieldDecimal', type : 'input' , text : ''},
						{ id : rootId+'.fieldGroup', type : 'input' , text : ''},
						{ id : rootId+'.fieldNullif', type : 'input' , text : ''}
					];
					
					jQuery.imetabar.createRowByHeader(r,rootId);
				},
				getfields : function(e,v){	
			       var r=[
                   		{id :'fieldId', type: 'number', text:''},
                   		{id :'fieldName', type: 'input', text:'fieldName'},
                   		{id :'fieldType', type: 'select', text:'type'},
                  	 	{id :'fieldElement', type: 'input', text:'element'},
                 	 	{id :'fieldFormat', type: 'input', text:''},
        				{id :'fieldLength', type: 'input', text:'length'},
        				{id :'fieldPrecision', type: 'input', text:'precision'},
        				{id :'fieldCurrency', type: 'input', text:''},
        				{id :'fieldDecimal', type:'input', text:'decimal'},
        				{id :'fieldGroup', type:'input', text:'group'},
        				{id :'fieldNullif', type:'input', text:''}
                          ];
                        						
             jQuery.imeta.parameter.getfields(e,v,r);
				}
			},
			listeners : {
				SpecifyFormat : function(e,v){
					var elId = e.target.id;
					var id = elId.split(".")[0];
					if(e.target.checked){
						$("[id="+id+".dateInFilename]").attr("disabled",true);
						$("[id="+id+".timeInFilename]").attr("disabled",true);
						$("[id="+id+".date_time_format]").attr("disabled",false);
					}else{
						$("[id="+id+".dateInFilename]").attr("disabled",false);
						$("[id="+id+".timeInFilename]").attr("disabled",false);
						$("[id="+id+".date_time_format]").attr("disabled",true);
					}
				}
			}
		},
		combinationlookup : {
			btn : {
				keyWordsAdd : function(c){
					var rootId = c.getAttribute("rootId");
					var r = [
						{ id : rootId+'.keysId', type : 'number' , text : '' },
						{ id : rootId+'.keyField', type : 'select' , text : ''},
						{ id : rootId+'.keyLookup', type : 'select' , text : ''}
					];
					
					jQuery.imetabar.createRowByHeader(r,rootId);
				},
				getfields : function(e,v){
					var elId = e.target.id;
					var roType = e.target.getAttribute("roType");
					var roName = e.target.getAttribute("roName");
					var elementName = e.target.getAttribute("elementName");
					var id = elId.split(".")[0];
					var rowNr = $("#"+id+"_keyWords_gList").attr("rowNr");
					$.imessagebox("#ibody",{
						title : "问题",
						marded : true,
						icon : "question",
						type : "custom",
						message : "表中已经有"+rowNr+"行记录，如何处理这"+rowNr+"行数据？",
						btns : [{
								key : "addNew",
								text : "增加新的",
								btnWidth : 80
							},{
								key : "addAll",
								text : "增加所有",
								btnWidth : 80
							},{
								key : "clearAndAddNew",
								text : "清除并增加新的",
								btnWidth : 80
							}],
						fn : function(m){
							switch(m){
								case "addNew" :
									$("#"+id).ajaxSubmit({
										type:"POST",
										url:"ImetaAction!addAndUpdateParameters.action",
										dataType:"json",
										data : {
											roType : roType,
										 	roName : roName,
										 	elementName : elementName
										},
										success : function(json){
											
										},
						                error : globalError
									});
									break;
								case "clearAndAddNew" : 
									var listId = id + "_keyWords_gList";
									$("#"+listId+" .x-grid-row").remove();
									var index=0;
									$("#"+listId).attr("rowNr",index);
								case "addAll" :
									$.ajax({
										type : "POST",
										url: "ImetaAction!addAndUpdateParameters.action",
						                data: jQuery.cutil.objectToUrl({
										 	roType : roType,
										 	roName : roName,
										 	elementName : elementName
										}),
						                dataType: "json",
						                success : function(json){
						                    
						                    var rootId = id+"_keyWords";
						                    
											$.each(json.fields,function(e,v){
												var r = [
                        							{ id : rootId+'.keysId', type : 'number' , text : '' },
													{ id : rootId+'.keyField', type : 'select' , text : v.fieldName},
													{ id : rootId+'.keyLookup', type : 'select' , text : v.fieldName}
                        						];
                        						
                        						jQuery.imetabar.createRowByHeader(r,rootId);
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
				useHash : function(e,v){
					var elId = e.target.id;
					var id = elId.split(".")[0];
					if(e.target.checked){
						$("[id="+id+".hashField]").attr("disabled",false);
					}else{
						$("[id="+id+".hashField]").attr("disabled",true);
					}
				}
			}
		},
		exceloutput : {
			btn : {
				outputFieldsAdd : function(c){
					var rootId = c.getAttribute("rootId");
				
					var r = [
						{ id : rootId+'.fieldId', type : 'number' , text :''},
						{ id : rootId+'.fieldName', type : 'input' , text : ''},
						{ id : rootId+'.fieldType', type : 'select' , text : ''},
						{ id : rootId+'.fieldFormat', type : 'input' , text : ''}
					];
					
					jQuery.imetabar.createRowByHeader(r,rootId);

				},
				
				getfields : function(e,v){
					var r = [
					{ id : 'fieldId', type : 'number' , text : '' },
                    { id : 'fieldName', type : 'input' , text : 'fieldName' },
                    { id : 'fieldType', type : 'select' , text : 'type '},
                    { id : 'fieldFormat', type : 'input' , text : '' }	
                            ];       						
                jQuery.imeta.parameter.getfields(e,v,r);

				}
			},
			listeners : {
				specifyFormat : function(e,v){
					var elId = e.target.id;
					var id = elId.split(".")[0];
					if(e.target.checked){
						$("[id="+id+".dateInFilename]").attr("disabled",true);
						$("[id="+id+".timeInFilename]").attr("disabled",true);
						$("[id="+id+".dateTimeFormat]").attr("disabled",false);
					}else{
						$("[id="+id+".dateInFilename]").attr("disabled",false);
						$("[id="+id+".timeInFilename]").attr("disabled",false);
						$("[id="+id+".dateTimeFormat]").attr("disabled",true);
					}
				},
				protectsheet : function(e,v){
					var elId = e.target.id;
					var id = elId.split(".")[0];
					if(e.target.checked){
						$("[id="+id+".password]").attr("disabled",false);
					}else{
						$("[id="+id+".password]").attr("disabled",true);
					}
				},
				templateEnabled : function(e,v){
					var elId = e.target.id;
					var id = elId.split(".")[0];
					if(e.target.checked){
						$("[id="+id+".templateFileName]").attr("disabled",false);
					}else{
						$("[id="+id+".templateFileName]").attr("disabled",true);
					}
				}
			}
		},
     	columnexists : {
		  	listeners : {
				istablenameInfield : function(e,v){
					var elId = e.target.id;
					var id = elId.split(".")[0];
					if(e.target.checked){
						$("[id="+id+".tablename]").attr("disabled",true);
						$("[id="+id+".tablenamefield]").attr("disabled",false);
					
					}else{
						$("[id="+id+".tablename]").attr("disabled",false);
						$("[id="+id+".tablenamefield]").attr("disabled",true);
						
					}
				}
			}
		
		},		
		dbproc : {
			btn : {
			    getfields : function(e,v){
					var r = [
						{ id : 'keywordsId', type : 'number' , text : ''},
						{ id : 'argument', type : 'input' , text : 'fieldName' },
						{ id : 'argumentDirection', type : 'select' , text : '' },
						{ id : 'argumentType', type : 'select' , text : 'type' }
					];
					jQuery.imeta.parameter.getfields(e,v,r,'keywords');
				},
				dbprocAdd : function(c){
					var rootId = c.getAttribute("rootId");
						var r = [
							{ id : rootId+'.keywordsId', type : 'number' , text : '' },
							{ id : rootId+'.argument', type : 'input' , text : '' },
							{ id : rootId+'.argumentDirection', type : 'select' , text : '' },
							{ id : rootId+'.argumentType', type : 'select' , text : '' }
						];
						jQuery.imetabar.createRowByHeader(r,rootId);
				}
			}
		},
		fileexists : {
		  	listeners : {
				includefiletype : function(e,v){
					var elId = e.target.id;
					var id = elId.split(".")[0];
					if(!e.target.checked){
						$("[id="+id+".filetypefieldname]").attr("disabled",true);
					}else{
						$("[id="+id+".filetypefieldname]").attr("disabled",false);
					}
				}
			}
		},
		
		janino : {
			btn : {
				fieldAdd : function(c){
					var rootId = c.getAttribute("rootId");
					var r = [
						{ id : rootId+'.fieldId', type : 'number' , text : '' },
						{ id : rootId+'.fieldName', type : 'input' , text : '' },
						{ id : rootId+'.formula', type : 'input' , text : '' },
						{ id : rootId+'.valueType', type : 'select' , text : '' },
						{ id : rootId+'.valueLength', type : 'input' , text : '' },
						{ id : rootId+'.valuePrecision', type : 'input' , text : '' },
						{ id : rootId+'.replaceField', type : 'input' , text : '' }
					];
					
					jQuery.imetabar.createRowByHeader(r,rootId);
				}
			}
		},
			
		injector : {
			btn : {
				injectorAdd : function(c){
					var rootId = c.getAttribute("rootId");
					var r = [
						{ id : rootId+'.fieldId', type : 'number' , text : '' },
						{ id : rootId+'.name', type : 'input' , text : '' },
						{ id : rootId+'.type', type : 'select' , text : '' },
						{ id : rootId+'.length', type : 'input' , text : '' },
						{ id : rootId+'.precision', type : 'input' , text : '' }
					];
					
					jQuery.imetabar.createRowByHeader(r,rootId);
				}
			}
		},
			
		formula : {
			btn : {
				fieldAdd : function(c){
					var rootId = c.getAttribute("rootId");
					var r = [
						{ id : rootId+'.fieldId', type : 'number' , text : '' },
						{ id : rootId+'.fieldName', type : 'input' , text : '' },
						{ id : rootId+'.formula', type : 'input' , text : '' },
						{ id : rootId+'.valueType', type : 'select' , text : '' },
						{ id : rootId+'.valueLength', type : 'input' , text : '' },
						{ id : rootId+'.valuePrecision', type : 'input' , text : '' },
						{ id : rootId+'.replaceField', type : 'input' , text : '' }
					];
					
					jQuery.imetabar.createRowByHeader(r,rootId);
				}
			}
		},		
	 	databasejoin: {
			btn : {
				getparam : function(e,v){
					var r = [									
							{ id : 'paramId', type : 'number' , text : '' },
							{ id : 'parameterField', type : 'input' , text : 'fieldName' },
							{ id : 'parameterType', type : 'select' , text : 'type' }
                     ];
                     jQuery.imeta.parameter.getfields(e,v,r,'param');
				},
				fieldAdd : function(c){
					var rootId = c.getAttribute("rootId");
					var r = [
						{ id : rootId+'.paramId', type : 'number' , text : '' },
						{ id : rootId+'.parameterField', type : 'input' , text : '' },
						{ id : rootId+'.parameterType', type : 'select' , text : '' }
					];
					
					jQuery.imetabar.createRowByHeader(r,rootId);
				}
			}
		},
		
		userdefinedjavaclass : {
			fields : {
				btn : {
					fieldAdd : function(c){
						var rootId = c.getAttribute("rootId");
						var r = [
							{ id : rootId+'.fieldId', type : 'number' , text : '' },
							{ id : rootId+'.fieldName', type : 'input' , text : '' },
							{ id : rootId+'.fieldType', type : 'select' , text : '' },
							{ id : rootId+'.fieldLength', type : 'input' , text : '' },
							{ id : rootId+'.fieldPrecision', type : 'input' , text : '' }
						];
						
						jQuery.imetabar.createRowByHeader(r,rootId);
					}
				}
			},
			parameters : {
				btn : {
					fieldAdd : function(c){
						var rootId = c.getAttribute("rootId");
						var r = [
							{ id : rootId+'.parameterId', type : 'number' , text : '' },
							{ id : rootId+'.parameterTag', type : 'input' , text : '' },
							{ id : rootId+'.parameterValue', type : 'input' , text : '' },
							{ id : rootId+'.parameterDesc', type : 'input' , text : '' }
						];
						
						jQuery.imetabar.createRowByHeader(r,rootId);
					}
				}
			},
			infoSteps : {
				btn : {
					fieldAdd : function(c){
						var rootId = c.getAttribute("rootId");
						var r = [
							{ id : rootId+'.infoStepId', type : 'number' , text : '' },
							{ id : rootId+'.infoStepTag', type : 'input' , text : '' },
							{ id : rootId+'.infoStepStep', type : 'input' , text : '' },
							{ id : rootId+'.infoStepDesc', type : 'input' , text : '' }
						];
						
						jQuery.imetabar.createRowByHeader(r,rootId);
					}
				}
			},
			targetSteps : {
				btn : {
					fieldAdd : function(c){
						var rootId = c.getAttribute("rootId");
						var r = [
							{ id : rootId+'.targetStepId', type : 'number' , text : '' },
							{ id : rootId+'.targetStepTag', type : 'input' , text : '' },
							{ id : rootId+'.targetStepStep', type : 'input' , text : '' },
							{ id : rootId+'.targetStepDesc', type : 'input' , text : '' }
						];
						
						jQuery.imetabar.createRowByHeader(r,rootId);
					}
				}
			}
		},
	 		
	  	scriptvalues_mod: {
			btn : {
				fieldAdd : function(c){
					var rootId = c.getAttribute("rootId");
					var r = [
						{ id : rootId+'.fieldId', type : 'number' , text : '' },
						{ id : rootId+'.name', type : 'input' , text : '' },
						{ id : rootId+'.rename', type : 'input' , text : '' },
						{ id : rootId+'.type', type : 'select' , text : '' },
						{ id : rootId+'.length', type : 'input' , text : '' },
						{ id : rootId+'.precision', type : 'input' , text : '' }
					];
					
					jQuery.imetabar.createRowByHeader(r,rootId);
				}
			},
			listeners : {
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
					var rootId = e.target.getAttribute("rootId");
					var js = $("[id="+rootId+".js]");
					js.val(js.val()+" "+e.target.innerHTML);
				}
			}
      	},
	  	regexeval: {
		  listeners : {
				createFileListener : function(e,v){
						var elId = e.target.id;
					var id = elId.split(".")[0];
					if(e.target.checked){
						$("[id="+id+"_fields.btn.add]").attr("disabled",false);
						$("[id="+id+"_fields.btn.delete]").attr("disabled",false);
						$("[id="+id+"_fields.btn.add.root]").removeClass("x-item-disabled");
						$("[id="+id+"_fields.btn.delete.root]").removeClass("x-item-disabled");
						$("#"+id+"_fields_gRoot input").attr("disabled",false);
					}else{
						$("[id="+id+"_fields.btn.add]").attr("disabled",true);
						$("[id="+id+"_fields.btn.delete]").attr("disabled",true);
						$("[id="+id+"_fields.btn.add.root]").addClass("x-item-disabled");
						$("[id="+id+"_fields.btn.delete.root]").addClass("x-item-disabled");
						$("#"+id+"_fields_gRoot input").attr("disabled",true);
					}
				}
			},
		    btn:{
				fieldAdd : function(c){
					var rootId = c.getAttribute("rootId");
					var r = [
						{ id : rootId+'.fieldId', type : 'number' , text : '' },
						{ id : rootId+'.fieldName', type : 'input' , text : '' },
						{ id : rootId+'.fieldType', type : 'select' , text : '' },
						{ id : rootId+'.fieldLength', type : 'input' , text : '' },
						{ id : rootId+'.fieldPrecision', type : 'input' , text : '' },
						{ id : rootId+'.fieldFormat', type : 'input' , text : '' },
						{ id : rootId+'.fieldGroup', type : 'input' , text : '' },
						{ id : rootId+'.fieldDecimal', type : 'input' , text : '' },
						{ id : rootId+'.fieldNullIf', type : 'input' , text : '' },
						{ id : rootId+'.fieldIfNull', type : 'input' , text : '' },
						{ id : rootId+'.fieldTrimType', type : 'select' , text : '' }
					];
					
					jQuery.imetabar.createRowByHeader(r,rootId);
				}
			
			}
		},
	 	http: {
		  	listeners : {
				urlInField : function(e,v){
					var elId = e.target.id;
					var id = elId.split(".")[0];
					if(e.target.checked){
						$("[id="+id+".url]").attr("disabled",true);
						$("[id="+id+".urlField]").attr("disabled",false);
					
					}else{
						$("[id="+id+".url]").attr("disabled",false);
						$("[id="+id+".urlField]").attr("disabled",true);
						
					}
				}
			},
          	btn:{
               getfields : function(e,v){
					var r = [
						{ id : 'fieldId', type : 'number' , text : '' },
						{ id : 'argumentField', type : 'input' , text : 'fieldName' },
						{ id : 'argumentParameter', type : 'input' , text : 'fieldName' }
					];
					jQuery.imeta.parameter.getfields(e,v,r);
				} ,
				fieldAdd : function(c){
    				var rootId = c.getAttribute("rootId");
    				var r = [
    					{ id : rootId+'.fieldId', type : 'number' , text : '' },
    					{ id : rootId+'.argumentField', type : 'input' , text : '' },
    					{ id : rootId+'.argumentParameter', type : 'input' , text : '' }
    				];
    				jQuery.imetabar.createRowByHeader(r,rootId);
    			}
			}		
		},
	 	streamlookup : {
			btn : {
			    getfields : function(e,v){
					var r = [
                        	{ id : 'keysId', type : 'number' , text : ''},
							{ id : 'keystream', type : 'input' , text : 'fieldName' },
							{ id : 'keylookup', type : 'input' , text : 'fieldName' }
                     ];
                     jQuery.imeta.parameter.getfields(e,v,r,"keys");
				},
			    getkeywords : function(e,v){
					var r = [
						{ id : 'valuesId', type : 'number' , text : '' },
						{ id : 'value', type : 'input' , text : 'fieldName' },
						{ id : 'valueName', type : 'input' , text : '' },
						{ id : 'valueDefault', type : 'input' , text : '' },
						{ id : 'valueDefaultType', type : 'select' , text : 'type' }
					];
					jQuery.imeta.parameter.getfields(e,v,r,"values");
				} ,
				keysAdd : function(c){
					var rootId = c.getAttribute("rootId");
					var r = [
						{ id : rootId+'.keysId', type : 'number' , text : ''},
						{ id : rootId+'.keystream', type : 'input' , text : '' },
						{ id : rootId+'.keylookup', type : 'input' , text : '' }
					];
					
					jQuery.imetabar.createRowByHeader(r,rootId);
				},
				valuesAdd : function(c){
					var rootId = c.getAttribute("rootId");
					var r = [
						{ id : rootId+'.valuesId', type : 'number' , text : '' },
						{ id : rootId+'.value', type : 'input' , text : '' },
						{ id : rootId+'.valueName', type : 'input' , text : '' },
						{ id : rootId+'.valueDefault', type : 'input' , text : '' },
						{ id : rootId+'.valueDefaultType', type : 'select' , text : '' }
					];
					
					jQuery.imetabar.createRowByHeader(r,rootId);
				}
			}
		},

		databaselookup : {
			btn : {
				getkeywords : function(e,v){
					var r = [		
						{ id : 'keywordsId', type : 'number' , text : '' },
						{ id : 'tableKeyField', type : 'input' , text : 'fieldName' },
						{ id : 'keyCondition', type : 'select' , text : '' },
						{ id : 'streamKeyField1', type : 'input' , text : 'fieldName' },
						{ id : 'streamKeyField2', type : 'input' , text : '' }
					];
					jQuery.imeta.parameter.getfields(e,v,r,"keywords");
				},
				getqueryvalue : function(e,v){
					var r = [		
						{ id : 'queryvalueId', type : 'number' , text : '' },
						{ id : 'returnValueField', type : 'input' , text : 'fieldName' },
						{ id : 'returnValueNewName', type : 'input' , text : '' },
						{ id : 'returnValueDefault', type : 'input' , text : '' },
						{ id : 'returnValueDefaultType', type : 'select' , text : 'type' }
					];
					jQuery.imeta.parameter.getfields(e,v,r,"queryvalue");
				}
			},
         	listeners : {
				cachedListeners : function(e,v){
					var elId = e.target.id;
					var id = elId.split(".")[0];
					if(e.target.checked){
						$("[id="+id+".cacheSize]").attr("disabled",false);
						$("[id="+id+".loadingAllDataInCache]").attr("disabled",false);
					    $("[id="+id+".failingOnMultipleResults]").attr("disabled",true);
					}else{
						$("[id="+id+".cacheSize]").attr("disabled",true);
						$("[id="+id+".loadingAllDataInCache]").attr("disabled",true);
						$("[id="+id+".failingOnMultipleResults]").attr("disabled",false);
					}
				},
				resultsListeners : function(e,v){
					var elId = e.target.id;
					var id = elId.split(".")[0];
					if(e.target.checked){
						$("[id="+id+".orderByClause]").attr("disabled",true);
						
					}else{
						$("[id="+id+".orderByClause]").attr("disabled",false);
					}
			    }
			},
			keywords : {
				btn : {
					fieldAdd : function(c){
						var rootId = c.getAttribute("rootId");
						var r = [
							{ id : rootId+'.keywordsId', type : 'number' , text : '' },
							{ id : rootId+'.tableKeyField', type : 'input' , text : '' },
							{ id : rootId+'.keyCondition', type : 'select' , text : '' },
							{ id : rootId+'.streamKeyField1', type : 'input' , text : '' },
							{ id : rootId+'.streamKeyField2', type : 'input' , text : '' }
						];
						jQuery.imetabar.createRowByHeader(r,rootId);
					}
				}
			},
			queryvalue : {
				btn : {
					fieldAdd : function(c){
						var rootId = c.getAttribute("rootId");
						var r = [
							{ id : rootId+'.queryvalueId', type : 'number' , text : '' },
							{ id : rootId+'.returnValueField', type : 'input' , text : '' },
							{ id : rootId+'.returnValueNewName', type : 'input' , text : '' },
							{ id : rootId+'.returnValueDefault', type : 'input' , text : '' },
							{ id : rootId+'.returnValueDefaultType', type : 'select' , text : '' }
						];
						jQuery.imetabar.createRowByHeader(r,rootId);
					}
				}
			}
		},

        deleteDialog : {
			btn : {
				deleteAdd : function(c){
					var rootId = c.getAttribute("rootId");
					var r = [
						{ id : rootId+'.fieldId', type : 'number' , text :'' },
						{ id : rootId+'.keyLookup', type : 'input' , text : ''},
						{ id : rootId+'.keyCondition', type : 'input' , text : ''},
						{ id : rootId+'.keyStream', type : 'input' , text : ''},
						{ id : rootId+'.keyStream2', type : 'input' , text : ''}
					];
					
					jQuery.imetabar.createRowByHeader(r,rootId);
				},
				getfields : function(e,v){
					var r = [
						{ id :'fieldId', type : 'number' , text : '' },
						{ id :'keyLookup', type : 'input' , text : 'fieldName' },
						{ id :'keyCondition', type : 'input' , text : '' },
						{ id :'keyStream', type : 'input' , text :'' },
						{ id :'keyStream2', type : 'input' , text : '' }
						];
						jQuery.imeta.parameter.getfields(e,v,r);  					
				}
			},
			listeners : {
				
			}
		},
		insertupdate : {
			btn : {
				keywordsAdd : function(c){
					var rootId = c.getAttribute("rootId");
					
					var r = [
						{ id : rootId+'.fieldId', type : 'number' , text : '' },
						{ id : rootId+'.keyLookup', type : 'input' , text : '' },
						{ id : rootId+'.keyCondition', type : 'select' , text : ''},
						{ id : rootId+'.keyStream', type : 'input' , text : '' },
						{ id : rootId+'.keyStream2', type : 'input' , text : '' }
					];
					
					jQuery.imetabar.createRowByHeader(r,rootId);
	
				},
				refreshwordsAdd : function(c){
					var rootId = c.getAttribute("rootId");
			
					var r = [
						{ id : rootId+'.fieldId', type : 'number' , text :''},
						{ id : rootId+'.updateLookup', type : 'input' , text : '' },
						{ id : rootId+'.updateStream', type : 'input' , text : '' },
						{ id : rootId+'.update', type : 'select' , text : '' }
					];
				
					jQuery.imetabar.createRowByHeader(r,rootId);
			
				},
				getfields : function(e,v){
					var r = [
						{ id :'fieldId', type : 'number' , text : '' },
						{ id :'keyLookup', type : 'input' , text : 'fieldName' },
						{ id :'keyStream', type : 'input' , text : '' },
						{ id :'keyCondition', type : 'input' , text :'' },
						{ id :'keyStream2', type : 'input' , text : '' }
						];
						jQuery.imeta.parameter.getfields(e,v,r,'keywords');  					
				},
				getRefreshFields : function(e,v){
				   var r = [
                     { id : 'fieldId', type : 'number' , text : '' },
                     { id : 'updateLookup', type : 'input' , text :'fieldName' },
                     { id : 'updateStream', type : 'input' , text : '' },
                     { id : 'update', type : 'select' , text :'' }
                    ];
                    jQuery.imeta.parameter.getfields(e,v,r,'refreshwords');			
				  }
			},
			listeners : {
				
			}
		},
		stepmeta : {
			btn : {
			},
			listeners : {
				outputRowcount : function(e,v){
					var elId = e.target.id;
					var id = elId.split(".")[0];
					if(e.target.checked){
						$("[id="+id+".rowcountField]").attr("disabled",false);
					}else{
						$("[id="+id+".rowcountField]").attr("disabled",true);
					}
				}
			}
		},
		clonerow : {
			btn : {
			},
			listeners : {
				nrcloneinfield : function(e,v){
					var elId = e.target.id;
					var id = elId.split(".")[0];
					if(!e.target.checked){
						$("[id="+id+".nrclones]").attr("disabled",false);
						$("[id="+id+".nrclonefield]").attr("disabled",true);
					}else{
						$("[id="+id+".nrclones]").attr("disabled",true);
						$("[id="+id+".nrclonefield]").attr("disabled",false);
					}
				},
				addcloneflag : function(e,v){
					var elId = e.target.id;
					var id = elId.split(".")[0];
					if(e.target.checked){
						$("[id="+id+".cloneflagfield]").attr("disabled",false);
					}else{
						$("[id="+id+".cloneflagfield]").attr("disabled",true);
					}
				}
			}
		},
		blockUntilStepsFinish : {
			btn : {
				fieldsAdd  : function(c){
					var rootId = c.getAttribute("rootId");
					
					var r = [
						{ id : rootId+'.fieldId', type : 'number' , text : '' },
						{ id : rootId+'.stepName', type : 'input' , text : '' },
						{ id : rootId+'.stepCopyNr', type : 'input' , text : '0'}
					];
					
					jQuery.imetabar.createRowByHeader(r,rootId);
				}
			}
		},
		blockingstep : {
			btn : {
			},
			listeners : {
				passAllRows : function(e,v){
					var elId = e.target.id;
					var id = elId.split(".")[0];
					if(e.target.checked){
						$("[id="+id+".directory]").attr("disabled",false);
						$("[id="+id+".prefix]").attr("disabled",false);
						$("[id="+id+".cacheSize]").attr("disabled",false);
						$("[id="+id+".compressFiles]").attr("disabled",false);
					}else{
						$("[id="+id+".directory]").attr("disabled",true);
						$("[id="+id+".prefix]").attr("disabled",true);
						$("[id="+id+".cacheSize]").attr("disabled",true);
						$("[id="+id+".compressFiles]").attr("disabled",true);
					}
				}
			}
		},
		sql : {
			btn : {
			    getfields : function(e,v){
				   var r = [
                      { id : 'fieldId', type : 'number' , text : '' },
					  { id : 'arguments', type : 'input' , text : 'fieldName'}
                      ];
                       jQuery.imeta.parameter.getfields(e,v,r);
				},
			    argsAdd : function(c){
					var rootId = c.getAttribute("rootId");
					var r = [
						{ id : rootId+'.fieldId', type : 'number' , text : '' },
						{ id : rootId+'.arguments', type : 'input' , text : ''}
					];
					
					jQuery.imetabar.createRowByHeader(r,rootId);
				}
			},
			listeners : {
				
			}
		},
		setvariable : {
			btn : {
			    getfields : function(e,v){
					 var r = [
                         { id : 'fieldId', type : 'number' , text : '' },
						 { id : 'fieldName', type : 'input' , text : 'fieldName'},
						 { id : 'variableName', type : 'input' , text : ''},
						 { id : 'variableType', type : 'select' , text : ''},
						 { id : 'defaultValue', type : 'input' , text : ''}
                      ];
                     jQuery.imeta.parameter.getfields(e,v,r);
				},
			    fieldsAdd : function(c){
					var rootId = c.getAttribute("rootId");
					var r = [
						{ id : rootId+'.fieldId', type : 'number' , text : '' },
						{ id : rootId+'.fieldName', type : 'input' , text : ''},
						{ id : rootId+'.variableName', type : 'input' , text : ''},
						{ id : rootId+'.variableType', type : 'select' , text : ''},
						{ id : rootId+'.defaultValue', type : 'input' , text : ''}
					];
					
					jQuery.imetabar.createRowByHeader(r,rootId);
				}
			},
			listeners : {
			}
		},
		getvariable : {
			btn : {
			    fieldsAdd : function(c){
					var rootId = c.getAttribute("rootId");
					var r = [
						{ id : rootId+'.fieldId', type : 'number' , text : '' },
						{ id : rootId+'.fieldName', type : 'input' , text : ''},
						{ id : rootId+'.variableString', type : 'input' , text : ''}
					];
					
					jQuery.imetabar.createRowByHeader(r,rootId);
				}
			},
			listeners : {
			}
		},
		rowsfromresult : {
			btn : {
			    fieldsAdd : function(c){
					var rootId = c.getAttribute("rootId");
					var r = [
						{ id : rootId+'.fieldId', type : 'number' , text : '' },
						{ id : rootId+'.name', type : 'input' , text : '' },
						{ id : rootId+'.type', type : 'select' , text : '' },
						{ id : rootId+'.length', type : 'input' , text : '' },
						{ id : rootId+'.precision', type : 'input' , text : '' }
					];
					
					jQuery.imetabar.createRowByHeader(r,rootId);
				}
			},
			listeners : {
			}
		},
		mail : {
			btn : {
			},
			listeners : {
				usingAuthentication : function(e,v){
					var elId = e.target.id;
					var id = elId.split(".")[0];
					if(e.target.checked){
						$("[id="+id+".authenticationUser]").attr("disabled",false);
						$("[id="+id+".authenticationPassword]").attr("disabled",false);
						$("[id="+id+".usingSecureAuthentication]").attr("disabled",false);
						//$("[id="+id+".secureconnectiontype]").attr("disabled",false);
					}else{
						$("[id="+id+".authenticationUser]").attr("disabled",true);
						$("[id="+id+".authenticationPassword]").attr("disabled",true);
						$("[id="+id+".usingSecureAuthentication]").attr("disabled",true);
						//$("[id="+id+".secureconnectiontype]").attr("disabled",true);
					}
				},
				usingSecureAuthentication : function(e,v){
					var elId = e.target.id;
					var id = elId.split(".")[0];
					if(e.target.checked){
						$("[id="+id+".secureconnectiontype]").attr("disabled",false);
					}else{
						$("[id="+id+".secureconnectiontype]").attr("disabled",true);
					}
				},
				useHTML : function(e,v){
					var elId = e.target.id;
					var id = elId.split(".")[0];
					if(e.target.checked){
						$("[id="+id+".encoding]").attr("disabled",false);
					}else{
						$("[id="+id+".encoding]").attr("disabled",true);
					}
				},
				manage : function(e,v){
					var elId = e.target.id;
					var id = elId.split(".")[0];
					if(e.target.checked){
						$("[id="+id+".priority]").attr("disabled",false);
						$("[id="+id+".importance]").attr("disabled",false);
					}else{
						$("[id="+id+".priority]").attr("disabled",true);
						$("[id="+id+".importance]").attr("disabled",true);
					}
				},
				isFilenameDynamic : function(e,v){
					var elId = e.target.id;
					var id = elId.split(".")[0];
					if(e.target.checked){
						$("[id="+id+".dynamicFieldname]").attr("disabled",false);
						$("[id="+id+".dynamicWildcard]").attr("disabled",false);
						$("[id="+id+".sourcefilefoldername]").attr("disabled",true);
						$("[id="+id+".sourcewildcard]").attr("disabled",true);
					}else{
						$("[id="+id+".dynamicFieldname]").attr("disabled",true);
						$("[id="+id+".dynamicWildcard]").attr("disabled",true);
						$("[id="+id+".sourcefilefoldername]").attr("disabled",false);
						$("[id="+id+".sourcewildcard]").attr("disabled",false);
					}
				},
				zipFiles : function(e,v){
					var elId = e.target.id;
					var id = elId.split(".")[0];
					if(e.target.checked){
						$("[id="+id+".zipFilenameDynamic]").attr("disabled",false);
						//$("[id="+id+".dynamicZipFilename]").attr("disabled",false);
						$("[id="+id+".zipFilename]").attr("disabled",false);
						$("[id="+id+".ziplimitsize]").attr("disabled",false);
					}else{
						$("[id="+id+".zipFilenameDynamic]").attr("disabled",true);
						//$("[id="+id+".dynamicZipFilename]").attr("disabled",true);
						$("[id="+id+".zipFilename]").attr("disabled",true);
						$("[id="+id+".ziplimitsize]").attr("disabled",true);
					}
				},
				zipFilenameDynamic : function(e,v){
					var elId = e.target.id;
					var id = elId.split(".")[0];
					if(e.target.checked){
						$("[id="+id+".dynamicZipFilename]").attr("disabled",false);
					}else{
						$("[id="+id+".dynamicZipFilename]").attr("disabled",true);
					}
				}
			}
		},
		GPbulkloader : {
			btn : {
				getfields : function(e,v){
					var r = [
						{ id : 'fieldId', type : 'number' , text : '' },
						{ id : 'fieldTable', type : 'input' , text : 'fieldName' },
						{ id : 'fieldStream', type : 'input' , text : 'fieldName' },
						{ id : 'dateMask', type : 'select' , text : '' }
					];
					jQuery.imeta.parameter.getfields(e,v,r,'fieldsToLoad');
				},
				fieldAdd : function(c){
					var rootId = c.getAttribute("rootId");
					var r = [
						{ id : rootId+'.fieldId', type : 'number' , text : '' },
						{ id : rootId+'.fieldTable', type : 'input' , text : '' },
						{ id : rootId+'.fieldStream', type : 'input' , text : '' },
						{ id : rootId+'.dateMask', type : 'select' , text : '' }
					];
					jQuery.imetabar.createRowByHeader(r,rootId);
				}
			}
		},
		monetDBbulkloader : {
			btn : {
			    getfields : function(e,v){
					var r = [
						{ id : 'fieldId', type : 'number' , text : '' },
						{ id : 'fieldTable', type : 'input' , text : 'fieldName' },
						{ id : 'fieldStream', type : 'input' , text : 'fieldName' },
						{ id : 'fieldFormatOk', type : 'select' , text : '' }
					];
					jQuery.imeta.parameter.getfields(e,v,r,'fieldsToLoad');
				},
				fieldAdd : function(c){
					var rootId = c.getAttribute("rootId");
					var r = [
						{ id : rootId+'.fieldId', type : 'number' , text : '' },
						{ id : rootId+'.fieldTable', type : 'input' , text : '' },
						{ id : rootId+'.fieldStream', type : 'input' , text : '' },
						{ id : rootId+'.fieldFormatOk', type : 'select' , text : '' }
					];
					
					jQuery.imetabar.createRowByHeader(r,rootId);
				}
			}
		},
		uniquerowsbyhashset : {
			btn : {
				getfields : function(e,v){
					var r = [
						{ id : 'fieldId', type : 'number' , text : '' },
						{ id : 'compareFields', type : 'input' , text : 'fieldName' }
					];
					jQuery.imeta.parameter.getfields(e,v,r);
				},
				fieldsAdd : function(c){
					var rootId = c.getAttribute("rootId");
					var r = [
						{ id : rootId+'.fieldId', type : 'number' , text : '' },
						{ id : rootId+'.compareFields', type : 'input' , text : '' }
					];
					
					jQuery.imetabar.createRowByHeader(r,rootId);
				}
			}
		},
		synchronizeaftermerge : {
			btn : {
				getFields : function(e,v){
					var r = [
						{ id : 'keyId', type : 'number' , text : '' },
						{ id : 'keyLookup', type : 'input' , text : 'fieldName' },
						{ id : 'keyCondition', type : 'select' , text : '' },
						{ id : 'keyStream', type : 'input' , text : 'fieldName' },
						{ id : 'keyStream2', type : 'input' , text : '' }
					];
					jQuery.imeta.parameter.getfields(e,v,r,"keys");
				},
				getUpdateFields : function(e,v){
					var r = [
						{ id : 'valueId', type : 'number' , text : '' },
						{ id : 'updateLookup', type : 'input' , text : 'fieldName' },
						{ id : 'updateStream', type : 'input' , text : 'fieldName' },
						{ id : 'update', type : 'select' , text : '' }
					];
					jQuery.imeta.parameter.getfields(e,v,r,"values");
				},
				keysAdd : function(c){
					var rootId = c.getAttribute("rootId");
					var r = [
						{ id : rootId+'.keyId', type : 'number' , text : '' },
						{ id : rootId+'.keyLookup', type : 'input' , text : '' },
						{ id : rootId+'.keyCondition', type : 'select' , text : '' },
						{ id : rootId+'.keyStream', type : 'input' , text : '' },
						{ id : rootId+'.keyStream2', type : 'input' , text : '' }
					];
					
					jQuery.imetabar.createRowByHeader(r,rootId);
				},
				valuesAdd : function(c){
					var rootId = c.getAttribute("rootId");
					var r = [
						{ id : rootId+'.valueId', type : 'number' , text : '' },
						{ id : rootId+'.updateLookup', type : 'input' , text : '' },
						{ id : rootId+'.updateStream', type : 'input' , text : '' },
						{ id : rootId+'.update', type : 'select' , text : '' }
					];
					
					jQuery.imetabar.createRowByHeader(r,rootId);
				}
			},
			listeners : {
				tablenameInFieldListeners : function(e,v){
					var elId = e.target.id;
					var id = elId.split(".")[0];
					if(e.target.checked){
						$("[id="+id+".tablenameField]").attr("disabled",false);
						$("[id="+id+".tableName]").attr("disabled",true);
					}else{
						$("[id="+id+".tablenameField]").attr("disabled",true);
						$("[id="+id+".tableName]").attr("disabled",false);
					}
				}
			}
		},
		httppost : {
			btn : {
				getbodyfields : function(e,v){
					var r = [
						{ id : 'fieldId', type : 'number' , text : '' },
						{ id : 'argumentField', type : 'input' , text : 'fieldName' },
						{ id : 'argumentParameter', type : 'input' , text : 'fieldName' }
					];
					jQuery.imeta.parameter.getfields(e,v,r,'bodyParameters');
				},
				getqueryfields : function(e,v){
					var r = [
						{ id : 'fieldId', type : 'number' , text : '' },
						{ id : 'queryField', type : 'input' , text : 'fieldName' },
						{ id : 'queryParameter', type : 'input' , text : 'fieldName' }
					];
					jQuery.imeta.parameter.getfields(e,v,r,'queryParameters');
				}
			},
			bodyfield : {
				btn : {
					fieldAdd : function(c){
						var rootId = c.getAttribute("rootId");
    					var r = [
    						{ id : rootId+'.fieldId', type : 'number' , text : '' },
    						{ id : rootId+'.argumentField', type : 'input' , text : '' },
    						{ id : rootId+'.argumentParameter', type : 'input' , text : '' }
    					];
    					jQuery.imetabar.createRowByHeader(r,rootId);
					}
				}
			},
			queryfield : {
				btn : {
					fieldAdd : function(c){
					    var rootId = c.getAttribute("rootId");
    					var r = [
    						{ id : rootId+'.fieldId', type : 'number' , text : '' },
    						{ id : rootId+'.queryField', type : 'input' , text : '' },
    						{ id : rootId+'.queryParameter', type : 'input' , text : '' }
    					];
    					jQuery.imetabar.createRowByHeader(r,rootId);
					}
				}
			},
			listeners : {
				urlInFieldListeners : function(e,v){
					var elId = e.target.id;
					var id = elId.split(".")[0];
					if(e.target.checked){
						$("[id="+id+".url]").attr("disabled",true);
						$("[id="+id+".urlField]").attr("disabled",false);
					}else{
						$("[id="+id+".url]").attr("disabled",false);
						$("[id="+id+".urlField]").attr("disabled",true);
					}
				}
			}
		},
		getsubfolders : {
		    btn : {
		        filesAdd : function(c){
		            var rootId = c.getAttribute("rootId");
					var r = [
						{ id : rootId+'.folderId', type : 'number' , text : '' },
						{ id : rootId+'.folderName', type : 'input' , text : '' },
						{ id : rootId+'.folderRequired', type : 'input' , text : '' }
					];
					
					jQuery.imetabar.createRowByHeader(r,rootId);
		        }
		    },
		    listeners : {
		        isFoldernameDynamic : function(e,v){
		           	var elId = e.target.id;
					var id = elId.split(".")[0];
					if(e.target.checked){
						$("[id="+id+".dynamicFoldernameField]").attr("disabled",false);
//						$("[id="+id+".directory]").attr("disabled",true);
						
						//点击后，表格屏蔽选中文件的按钮和包含文件的字段名
						$("[id="+id+"_files.btn.add]").attr("disabled",true);
						$("[id="+id+"_files.btn.delete]").attr("disabled",true);
						$("[id="+id+"_files.btn.add.root]").addClass("x-item-disabled");
						$("[id="+id+"_files.btn.delete.root]").addClass("x-item-disabled");
						$("#"+id+"_files_gRoot input").attr("disabled",true);
					}else{
						$("[id="+id+".dynamicFoldernameField]").attr("disabled",true);
//						$("[id="+id+".directory]").attr("disabled",false);
						
						//点击不选中后，表格显示选中文件的按钮和包含文件的字段名
						$("[id="+id+"_files.btn.add]").attr("disabled",false);
						$("[id="+id+"_files.btn.delete]").attr("disabled",false);
						$("[id="+id+"_files.btn.add.root]").removeClass("x-item-disabled");
						$("[id="+id+"_files.btn.delete.root]").removeClass("x-item-disabled");
						$("#"+id+"_files_gRoot input").attr("disabled",false);
					}
		        },
		        includeRowNumber : function(e,v){
		            var elId = e.target.id;
					var id = elId.split(".")[0];
					if(e.target.checked){
						$("[id="+id+".rowNumberField]").attr("disabled",false);
					}else{
						$("[id="+id+".rowNumberField]").attr("disabled",true);
					}
		        }
		    }
		},
		pgbulkloader : {
			btn : {
				getfields : function(e,v){
					var r = [
						{ id : 'fieldId', type : 'number' , text : '' },
						{ id : 'fieldTable', type : 'input' , text : 'fieldName' },
						{ id : 'fieldStream', type : 'input' , text : 'fieldName' },
						{ id : 'dateMask', type : 'select' , text : '' }
					];
					jQuery.imeta.parameter.getfields(e,v,r,'fieldsToLoad');
				},
				fieldAdd : function(c){
					var rootId = c.getAttribute("rootId");
					var r = [
						{ id : rootId+'.fieldId', type : 'number' , text : '' },
						{ id : rootId+'.fieldTable', type : 'input' , text : '' },
						{ id : rootId+'.fieldStream', type : 'input' , text : '' },
						{ id : rootId+'.dateMask', type : 'select' , text : '' }
					];
					jQuery.imetabar.createRowByHeader(r,rootId);
				}
			}
		},
		btn : {
			ok : function(e,v){
				var elId = e.target.id;
				var id = elId.split(".")[0];
				$("#"+id).ajaxSubmit({
					type:"POST",
					url:"ImetaAction!editElementSubmit.action",
					dataType:"json",
					data : {
						id : id,
						roType : jQuery.iPortalTab.OBJECT_TYPE_TRANS,
						roName : e.target.getAttribute("transName"),
						directoryId : e.target.getAttribute("directoryId"),
						elementName : e.target.getAttribute("stepName")
					},
					success : function(json){
						if(json.oldName != json.newName){
							var el = jQuery.imenu.iContent.getCanvasElByText(json.oldName,'step');
							jQuery.imenu.iContent.updateEl(el,{
							    stepName : json.newName,
								bText : [json.newName]
							});
							jQuery.imenu.iContent.redraw();
						}
						jQuery.iPortalTab.updateActiveTab(jQuery.iPortalTab.TYPE_ACTION_CHANGE_STEP);
						$("#window-"+id).remove();
						$.imessagebox("#ibody",json);
					}
				});
			},
			cancel : function(e,v){
				var elId = e.target.id;
				var id = elId.split(".")[0];
				$("#window-"+id).remove();
			}
		}
	},
	jobEntries : {
		shell : {
			btn : {
				wordsAdd : function(c){
					var rootId = c.getAttribute("rootId");
					
					var r = [
						{ id : rootId+'.fieldId', type : 'number' , text :''},
						{ id : rootId+'.arguments', type : 'input' , text : ''  }
					];
					
					jQuery.imetabar.createRowByHeader(r,rootId);

				}
			},
			listeners : {
				designatedFile : function(e,v){
					var elId = e.target.id;
					var id = elId.split(".")[0];
					if(e.target.checked){	
						$("[id="+id+".addFile]").attr("disabled",true);
						$("[id="+id+".fileName]").attr("disabled",true);
						$("[id="+id+".extension]").attr("disabled",true);
						$("[id="+id+".containsData]").attr("disabled",true);
						$("[id="+id+".containsTime]").attr("disabled",true);
						$("[id="+id+".logLevel]").attr("disabled",true);
						
					}else{
						$("[id="+id+".addFile]").attr("disabled",false);
						$("[id="+id+".fileName]").attr("disabled",false);
						$("[id="+id+".extension]").attr("disabled",false);
						$("[id="+id+".containsData]").attr("disabled",false);
						$("[id="+id+".containsTime]").attr("disabled",false);
						$("[id="+id+".logLevel]").attr("disabled",false);
					
					}
				},
				clusterMode : function(e,v){
					var elId = e.target.id;
					var id = elId.split(".")[0];
					if(e.target.checked){
						$("[id="+id+".slaveServer]").attr("disabled",true);
						$("[id="+id+".waitingToFinish]").attr("disabled",true);
						$("[id="+id+".followingAbortRemotely]").attr("disabled",true);
					}else{
						$("[id="+id+".slaveServer]").attr("disabled",false);
						$("[id="+id+".waitingToFinish]").attr("disabled",false);
						$("[id="+id+".followingAbortRemotely]").attr("disabled",false);
					}
				}
			}
		},
		sql : {
			btn : {
			    getfields : function(e,v){
				   var r = [
                      { id : 'fieldId', type : 'number' , text : '' },
					  { id : 'arguments', type : 'input' , text : 'fieldName'}
                      ];
                       jQuery.imeta.parameter.getfields(e,v,r);
				},
			    argsAdd : function(c){
					var rootId = c.getAttribute("rootId");
					var r = [
						{ id : rootId+'.fieldId', type : 'number' , text : '' },
						{ id : rootId+'.arguments', type : 'input' , text : ''}
					];
					
					jQuery.imetabar.createRowByHeader(r,rootId);
				}
			},
			listeners : {
				sqlfromfile : function(e,v){
					var elId = e.target.id;
					var id = elId.split(".")[0];
					if(e.target.checked){
						$("[id="+id+".sqlfilename]").attr("disabled",false);
						$("[id="+id+".useVariableSubstitution]").attr("disabled",true);
						$("[id="+id+".sql]").attr("disabled",true);
					}else{
						$("[id="+id+".sqlfilename]").attr("disabled",true);
						$("[id="+id+".useVariableSubstitution]").attr("disabled",false);
						$("[id="+id+".sql]").attr("disabled",false);
					}
				}
			}
		},
		exportrepository:{
			listeners : {
					SpecifyFormat : function(e,v){
					var elId = e.target.id;
					var id = elId.split(".")[0];
					if(e.target.checked){
						$("[id="+id+".add_date]").attr("disabled",false);
						$("[id="+id+".add_time]").attr("disabled",true);
						$("[id="+id+".date_time_format]").attr("disabled",true);
					}else{
						$("[id="+id+".add_date]").attr("disabled",true);
						$("[id="+id+".add_time]").attr("disabled",false);
						$("[id="+id+".date_time_format]").attr("disabled",false);
					}
				}
			}	
		},
		connectedtorepository:{
			listeners : {
				isspecificrep : function(e,v){
					var elId = e.target.id;
					var id = elId.split(".")[0];
					if(e.target.checked){
						$("[id="+id+".repname]").attr("disabled",false);
				
					}else{
						$("[id="+id+".repname]").attr("disabled",true);
			
					}
				},
				isspecificuser : function(e,v){
					var elId = e.target.id;
					var id = elId.split(".")[0];
					if(e.target.checked){
						
						$("[id="+id+".username]").attr("disabled",false);
				
					}else{
						$("[id="+id+".username]").attr("disabled",true);
					
					}
				}
			}	
		},
		ping : {
			pingChange : function(e){
					var elId = e.target.id;
					var id = elId.split(".")[0];
					if(this.value == 'classicPing'){ 
						$("[id="+id+".timeout]").attr("disabled",true);
						$("[id="+id+".nbrPackets]").attr("disabled",false);
					}else if(this.value == 'systemPing'){
						$("[id="+id+".timeout]").attr("disabled",false);
						$("[id="+id+".nbrPackets]").attr("disabled",true);
					}else if(this.value == 'bothPings'){
					    $("[id="+id+".timeout]").attr("disabled",false);
						$("[id="+id+".nbrPackets]").attr("disabled",false);
					}
				}
		},
		special : {
			schedulerTypeChange : function(e,v){
				var elId = e.target.id;
				var id = elId.split(".")[0];
				var value = e.target.value;
				switch(value){
					case "1" :
						changeField(false,false,true,true,true);
						break;
					case "2" :
						changeField(true,true,false,true,true);
						break;
					case "3" :
						changeField(true,true,true,false,true);
						break;
					case "4" :
						changeField(true,true,true,true,false);
						break;
					default : 
						changeField(true,true,true,true,true);
						break;
				}
				function changeField(intervalSecond,intervalMinute,timeOfDay,dayOfWeek,dayOfMonth){
					$("[id="+id+".intervalSecond]").attr("disabled",intervalSecond);
					$("[id="+id+".intervalMinute]").attr("disabled",intervalMinute);
					$("[id="+id+".timeOfDay]").attr("disabled",timeOfDay);
					$("[id="+id+".dayOfWeek]").attr("disabled",dayOfWeek);
					$("[id="+id+".dayOfMonth]").attr("disabled",dayOfMonth);
				}
			}
		},
		trans : {
			btn : {
				fieldAdd : function(c){
					var rootId = c.getAttribute("rootId");
					var r = [
						{ id : rootId+'.fieldId', type : 'number' , text : '' },
						{ id : rootId+'.arguments', type : 'input' , text : '' }
					];
					
					jQuery.imetabar.createRowByHeader(r,rootId);
				},
				parameterAdd : function(c){
					var rootId = c.getAttribute("rootId");
					var r = [
						{ id : rootId+'.parameterId', type : 'number' , text : '' },
						{ id : rootId+'.parameterNames', type : 'input' , text : '' },
						{ id : rootId+'.valuesFromResult', type : 'input' , text : '' },
						{ id : rootId+'.values', type : 'input' , text : '' }
					];
					
					jQuery.imetabar.createRowByHeader(r,rootId);
				},
				transnameBtn : function(e,v){
					var elId = e.target.id;
					var id = elId.split(".")[0];
					var win = $.iformwindow('#ibody',{
                    	id: id + "_transnameBtn",
                   		title: '选择转换',
	                    showLoading : true
	                });
					
					$.ajax({
		                type: "POST",
		                url: "ImetaAction!detectRep.action",
		                dataType: "json",
		                data : jQuery.cutil.objectToUrl({
		                	id : id + "_transnameBtn",
		                	parentId : id,
		                	// 显示的元素类型
		                	showTypes:"trans",
		                	// 点击确定后调用的方法，有两个参数输入，1 类型，2 ID
		                	customOkFn : "jQuery.imeta.jobEntries.trans.btn.transnameBtnRtn"
		                }),
		                success : function(json){
			               	win.appendContent(json);
		                }
		            });
				},
				transnameBtnRtn : function(t,st){
					var transname = t.attr("elName")
					if(transname && transname !=""){
						$("[id="+st+".transname]").val(transname);
						$("[id="+st+".directory]").val(t.attr("directoryPath"));
					}else{
						$("[id="+st+".transname]").val("");
						$("[id="+st+".directory]").val("");
					}
				}
			},
			listeners : {
				setLogfile : function(e,v){
					var elId = e.target.id;
					var id = elId.split(".")[0];
					if(e.target.checked){
						$("[id="+id+".setAppendLogfile]").attr("disabled",false);
						$("[id="+id+".logfile]").attr("disabled",false);
						$("[id="+id+".logext]").attr("disabled",false);
						$("[id="+id+".addDate]").attr("disabled",false);
						$("[id="+id+".addTime]").attr("disabled",false);
						$("[id="+id+".loglevel]").attr("disabled",false);
					}else{
						$("[id="+id+".setAppendLogfile]").attr("disabled",true);
						$("[id="+id+".logfile]").attr("disabled",true);
						$("[id="+id+".logext]").attr("disabled",true);
						$("[id="+id+".addDate]").attr("disabled",true);
						$("[id="+id+".addTime]").attr("disabled",true);
						$("[id="+id+".loglevel]").attr("disabled",true);
					}
				},
				clusterMode : function(e,v){
					var elId = e.target.id;
					var id = elId.split(".")[0];
					if(e.target.checked){
						$("[id="+id+".slaveServer]").attr("disabled",true);
						$("[id="+id+".waitingToFinish]").attr("disabled",true);
						$("[id="+id+".followingAbortRemotely]").attr("disabled",true);
					}else{
						$("[id="+id+".slaveServer]").attr("disabled",false);
						$("[id="+id+".waitingToFinish]").attr("disabled",false);
						$("[id="+id+".followingAbortRemotely]").attr("disabled",false);
					}
				}
			}
		},
		job : {
			btn : {
				fieldAdd : function(c){
					var rootId = c.getAttribute("rootId");
					var r = [
						{ id : rootId+'.fieldId', type : 'number' , text : '' },
						{ id : rootId+'.arguments', type : 'input' , text : '' }
					];
					
					jQuery.imetabar.createRowByHeader(r,rootId);
				},
				jobnameBtn : function(e,v){
					var elId = e.target.id;
					var id = elId.split(".")[0];
					var win = $.iformwindow('#ibody',{
                    	id: id + "_jobnameBtn",
                   		title: '选择任务',
	                    showLoading : true
	                });
					
					$.ajax({
		                type: "POST",
		                url: "ImetaAction!detectRep.action",
		                dataType: "json",
		                data : jQuery.cutil.objectToUrl({
		                	id : id + "_jobnameBtn",
		                	parentId : id,
		                	// 显示的元素类型
		                	showTypes:"job",
		                	// 点击确定后调用的方法，有两个参数输入，1 类型，2 ID
		                	customOkFn : "jQuery.imeta.jobEntries.job.btn.jobnameBtnRtn"
		                }),
		                success : function(json){
			               	win.appendContent(json);
		                }
		            });
				},
				jobnameBtnRtn : function(t,st){
					var jobname = t.attr("elName")
					if(jobname && jobname !=""){
						$("[id="+st+".jobname]").val(jobname);
						$("[id="+st+".directory]").val(t.attr("directoryPath"));
					}else{
						$("[id="+st+".jobname]").val("");
						$("[id="+st+".directory]").val("");
					}
				}
			},
			listeners : {
				setLogfile : function(e,v){
					var elId = e.target.id;
					var id = elId.split(".")[0];
					if(e.target.checked){
						$("[id="+id+".setAppendLogfile]").attr("disabled",false);
						$("[id="+id+".logfile]").attr("disabled",false);
						$("[id="+id+".logext]").attr("disabled",false);
						$("[id="+id+".addDate]").attr("disabled",false);
						$("[id="+id+".addTime]").attr("disabled",false);
						$("[id="+id+".loglevel]").attr("disabled",false);
					}else{
						$("[id="+id+".setAppendLogfile]").attr("disabled",true);
						$("[id="+id+".logfile]").attr("disabled",true);
						$("[id="+id+".logext]").attr("disabled",true);
						$("[id="+id+".addDate]").attr("disabled",true);
						$("[id="+id+".addTime]").attr("disabled",true);
						$("[id="+id+".loglevel]").attr("disabled",true);
					}
				}
			}
			
		},
		http : {
				listeners : {
					isRunForEveryRowClick : function(e,v){
						var elId = e.target.id;
						var id = elId.split(".")[0];
						if(e.target.checked){
							$("[id="+id+".url]").attr("disabled",true);
							$("[id="+id+".urlFieldname]").attr("disabled",false);
						}else{
							$("[id="+id+".url]").attr("disabled",false);
							$("[id="+id+".urlFieldname]").attr("disabled",true);
						}
					},
					isDateTimeAddedClick : function(e,v){
						var elId = e.target.id;
						var id = elId.split(".")[0];
						if(e.target.checked){
							$("[id="+id+".fileAppended]").attr("disabled",true);
							$("[id="+id+".targetFilenameExtention]").attr("disabled",false);
						}else{
							$("[id="+id+".fileAppended]").attr("disabled",false);
							$("[id="+id+".targetFilenameExtention]").attr("disabled",true);
						}
					}
				},
			btn:{
			   
			}
		},
		deleteFiles : {
			btn:{
				fieldAdd : function(c){
					var rootId = c.getAttribute("rootId");
					var r = [
						{ id : rootId+'.fieldId', type : 'number' , text : '' },
						{ id : rootId+'.fileFolder', type : 'input' , text : '' },
						{ id : rootId+'.wildcard', type : 'input' , text : '' }
					];
					jQuery.imetabar.createRowByHeader(r,rootId);
				}
			},
			listeners : {
				isArgFromPreviousClick : function(e,v){
					var elId = e.target.id;
					var id = elId.split(".")[0];
					if(!e.target.checked){
						$("[id="+id+"_fileFolderTable.btn.add]").attr("disabled",false);
						$("[id="+id+"_fileFolderTable.btn.delete]").attr("disabled",false);
						$("[id="+id+"_fileFolderTable.btn.add.root]").removeClass("x-item-disabled");
						$("[id="+id+"_fileFolderTable.btn.delete.root]").removeClass("x-item-disabled");
						$("#"+id+"_fileFolderTable_gRoot input").attr("disabled",false);
					}else{
						$("[id="+id+"_fileFolderTable.btn.add]").attr("disabled",true);
						$("[id="+id+"_fileFolderTable.btn.delete]").attr("disabled",true);
						$("[id="+id+"_fileFolderTable.btn.add.root]").addClass("x-item-disabled");
						$("[id="+id+"_fileFolderTable.btn.delete.root]").addClass("x-item-disabled");
						$("#"+id+"_fileFolderTable_gRoot input").attr("disabled",true);
					}
				}
				
			}
		},
		copyFiles : {
			btn:{
				fieldAdd : function(c){
					var rootId = c.getAttribute("rootId");
					var r = [
						{ id : rootId+'.fieldId', type : 'number' , text : '' },
						{ id : rootId+'.fileFolderSource', type : 'input' , text : '' },
						{ id : rootId+'.fileFolderDest', type : 'input' , text : '' },
						{ id : rootId+'.wildcard', type : 'input' , text : '' }
					];
					jQuery.imetabar.createRowByHeader(r,rootId);
				}
			},
			listeners : {
				isIncludeSubfoldersClick : function(e,v){
						var elId = e.target.id;
						var id = elId.split(".")[0];
						if(e.target.checked){
							$("[id="+id+".copy_empty_folders]").attr("disabled",false);
						}else{
							$("[id="+id+".copy_empty_folders]").attr("disabled",true);
						}
				},
				is_arg_from_previous_click : function(e,v){
					var elId = e.target.id;
					var id = elId.split(".")[0];
					if(!e.target.checked){
						$("[id="+id+"_fileFolderTable.btn.add]").attr("disabled",false);
						$("[id="+id+"_fileFolderTable.btn.delete]").attr("disabled",false);
						$("[id="+id+"_fileFolderTable.btn.add.root]").removeClass("x-item-disabled");
						$("[id="+id+"_fileFolderTable.btn.delete.root]").removeClass("x-item-disabled");
						$("#"+id+"_fileFolderTable_gRoot input").attr("disabled",false);
					}else{
						$("[id="+id+"_fileFolderTable.btn.add]").attr("disabled",true);
						$("[id="+id+"_fileFolderTable.btn.delete]").attr("disabled",true);
						$("[id="+id+"_fileFolderTable.btn.add.root]").addClass("x-item-disabled");
						$("[id="+id+"_fileFolderTable.btn.delete.root]").addClass("x-item-disabled");
						$("#"+id+"_fileFolderTable_gRoot input").attr("disabled",true);
					}
				}
			}
		},
		zipFile : {
			btn:{
				
			},
			listeners : {
				isfrompreviousClick : function(e,v){
						var elId = e.target.id;
						var id = elId.split(".")[0];
						if(e.target.checked){
							$("[id="+id+".sourcedirectory]").attr("disabled",true);
							$("[id="+id+".wildcard]").attr("disabled",true);
							$("[id="+id+".wildcardexclude]").attr("disabled",true);
							$("[id="+id+".zipFilename]").attr("disabled",true);
							$("[id="+id+".adddate]").attr("disabled",true);
							$("[id="+id+".addtime]").attr("disabled",true);
						}else{
							$("[id="+id+".sourcedirectory]").attr("disabled",false);
							$("[id="+id+".wildcard]").attr("disabled",false);
							$("[id="+id+".wildcardexclude]").attr("disabled",false);
							$("[id="+id+".zipFilename]").attr("disabled",false);
							$("[id="+id+".adddate]").attr("disabled",false);
							$("[id="+id+".addtime]").attr("disabled",false);
						}
				},
				isSpecifyFormatClick : function(e,v){
						var elId = e.target.id;
						var id = elId.split(".")[0];
						if(e.target.checked){
							$("[id="+id+".adddate]").attr("disabled",true);
							$("[id="+id+".addtime]").attr("disabled",true);
							$("[id="+id+".date_time_format]").attr("disabled",false);
						}else{
							$("[id="+id+".adddate]").attr("disabled",false);
							$("[id="+id+".addtime]").attr("disabled",false);
							$("[id="+id+".date_time_format]").attr("disabled",true);
						}
				},
				afterzipChange : function(e){
					// alert(this.value);
					var elId = e.target.id;
					var id = elId.split(".")[0];
					if(this.value == '2'){ // 移动文件
						$("[id="+id+".movetodirectory]").attr("disabled",false);
					}else{
						$("[id="+id+".movetodirectory]").attr("disabled",true);
					}
				}
			}
		},
		unzip : {
			btn:{
				
			},
			listeners : {
				isfrompreviousClick : function(e,v){
						var elId = e.target.id;
						var id = elId.split(".")[0];
						if(e.target.checked){
							$("[id="+id+".zipFilename]").attr("disabled",true);
							$("[id="+id+".wildcardSource]").attr("disabled",true);
						}else{
							$("[id="+id+".zipFilename]").attr("disabled",false);
							$("[id="+id+".wildcardSource]").attr("disabled",false);
						}
				},
				isSpecifyFormatClick : function(e,v){
						var elId = e.target.id;
						var id = elId.split(".")[0];
						if(e.target.checked){
							$("[id="+id+".adddate]").attr("disabled",true);
							$("[id="+id+".addtime]").attr("disabled",true);
							$("[id="+id+".date_time_format]").attr("disabled",false);
						}else{
							$("[id="+id+".adddate]").attr("disabled",false);
							$("[id="+id+".addtime]").attr("disabled",false);
							$("[id="+id+".date_time_format]").attr("disabled",true);
						}
				},
				afterunzipChange : function(e){
					// alert(this.value);
					var elId = e.target.id;
					var id = elId.split(".")[0];
					if(this.value == '2'){ // 移动文件
						$("[id="+id+".movetodirectory]").attr("disabled",false);
						$("[id="+id+".createMoveToDirectory]").attr("disabled",false);
					}else{
						$("[id="+id+".movetodirectory]").attr("disabled",true);
						$("[id="+id+".createMoveToDirectory]").attr("disabled",true);
					}
				},
				successOnChange : function(e){
					// alert(this.value);
					var elId = e.target.id;
					var id = elId.split(".")[0];
					if(this.value == '0'){ // 移动文件
						$("[id="+id+".nr_limit]").attr("disabled",true);
					}else{
						$("[id="+id+".nr_limit]").attr("disabled",false);
					}
				}
			}
		},
		foldersCompare : {
			btn:{
				
			},
			listeners : {
				compareonlyChange : function(e){
					// alert(this.value);
					var elId = e.target.id;
					var id = elId.split(".")[0];
					if(this.value == '0' || this.value == '1'){ 
						$("[id="+id+".wildcard]").attr("disabled",true);
						$("[id="+id+".comparefilesize]").attr("disabled",false);
						$("[id="+id+".comparefilecontent]").attr("disabled",false);
					}else if(this.value == '2'){
						$("[id="+id+".wildcard]").attr("disabled",true);
						$("[id="+id+".comparefilesize]").attr("disabled",true);
						$("[id="+id+".comparefilecontent]").attr("disabled",true);
					}else if(this.value == '3'){
						$("[id="+id+".wildcard]").attr("disabled",false);
						$("[id="+id+".comparefilesize]").attr("disabled",false);
						$("[id="+id+".comparefilecontent]").attr("disabled",false);
					}
				}
			}
		},
		addResultFileNames : {
			btn:{
				fieldAdd : function(c){
					var rootId = c.getAttribute("rootId");
					var r = [
						{ id : rootId+'.fieldId', type : 'number' , text : '' },
						{ id : rootId+'.fileFolder', type : 'input' , text : '' },
						{ id : rootId+'.wildcard', type : 'input' , text : '' }
					];
					jQuery.imetabar.createRowByHeader(r,rootId);
				}
			},
			listeners : {
				isArgFromPreviousClick : function(e,v){
						var elId = e.target.id;
					var id = elId.split(".")[0];
					if(!e.target.checked){
						$("[id="+id+"_multiFileFolderTable.btn.add]").attr("disabled",false);
						$("[id="+id+"_multiFileFolderTable.btn.delete]").attr("disabled",false);
						$("[id="+id+"_multiFileFolderTable.btn.add.root]").removeClass("x-item-disabled");
						$("[id="+id+"_multiFileFolderTable.btn.delete.root]").removeClass("x-item-disabled");
						$("#"+id+"_multiFileFolderTable_gRoot input").attr("disabled",false);
					}else{
						$("[id="+id+"_multiFileFolderTable.btn.add]").attr("disabled",true);
						$("[id="+id+"_multiFileFolderTable.btn.delete]").attr("disabled",true);
						$("[id="+id+"_multiFileFolderTable.btn.add.root]").addClass("x-item-disabled");
						$("[id="+id+"_multiFileFolderTable.btn.delete.root]").addClass("x-item-disabled");
						$("#"+id+"_multiFileFolderTable_gRoot input").attr("disabled",true);
					}
				}
			}
		},
		deleteResultFileNames : {
			btn:{
				
			},
			listeners : {
				isSpecifywildcardClick : function(e,v){
						var elId = e.target.id;
						var id = elId.split(".")[0];
						if(e.target.checked){
							$("[id="+id+".wildcard]").attr("disabled",false);
							$("[id="+id+".wildcardexclude]").attr("disabled",false);
						}else{
							$("[id="+id+".wildcard]").attr("disabled",true);
							$("[id="+id+".wildcardexclude]").attr("disabled",true);
						}
				}
			}
		},
		moveFiles : {
			btn:{
				fieldAdd : function(c){
					var rootId = c.getAttribute("rootId");
					var r = [
						{ id : rootId+'.fieldId', type : 'number' , text : '' },
						{ id : rootId+'.fileFolderSource', type : 'input' , text : '' },
						{ id : rootId+'.fileFolderDest', type : 'input' , text : '' },
						{ id : rootId+'.wildcard', type : 'input' , text : '' }
					];
					jQuery.imetabar.createRowByHeader(r,rootId);
				}
			},
			listeners : {
				is_include_subfolders_click : function(e,v){
						var elId = e.target.id;
						var id = elId.split(".")[0];
						if(e.target.checked){
							$("[id="+id+".move_empty_folders]").attr("disabled",false);
						}else{
							$("[id="+id+".move_empty_folders]").attr("disabled",true);
						}
				},
				is_arg_from_previous_click : function(e,v){
						var elId = e.target.id;
					var id = elId.split(".")[0];
					if(!e.target.checked){
						$("[id="+id+"_fileFolderTable.btn.add]").attr("disabled",false);
						$("[id="+id+"_fileFolderTable.btn.delete]").attr("disabled",false);
						$("[id="+id+"_fileFolderTable.btn.add.root]").removeClass("x-item-disabled");
						$("[id="+id+"_fileFolderTable.btn.delete.root]").removeClass("x-item-disabled");
						$("#"+id+"_fileFolderTable_gRoot input").attr("disabled",false);
					}else{
						$("[id="+id+"_fileFolderTable.btn.add]").attr("disabled",true);
						$("[id="+id+"_fileFolderTable.btn.delete]").attr("disabled",true);
						$("[id="+id+"_fileFolderTable.btn.add.root]").addClass("x-item-disabled");
						$("[id="+id+"_fileFolderTable.btn.delete.root]").addClass("x-item-disabled");
						$("#"+id+"_fileFolderTable_gRoot input").attr("disabled",true);
					}
				},
				isAddDateTimeClick : function(e,v){
						var elId = e.target.id;
						var id = elId.split(".")[0];
						if(e.target.checked){
							$("[id="+id+".AddDateBeforeExtension]").attr("disabled",false);
						}else{
							if(!$("[id="+id+".add_date]").attr("checked") && !$("[id="+id+".add_time]").attr("checked"))
								$("[id="+id+".AddDateBeforeExtension]").attr("disabled",true);
						}
				},
				isSpecifyFormatClick : function(e,v){
						var elId = e.target.id;
						var id = elId.split(".")[0];
						if(e.target.checked){
							$("[id="+id+".add_date]").attr("disabled",true);
							$("[id="+id+".add_date]").attr("checked",false);
							$("[id="+id+".add_time]").attr("disabled",true);
							$("[id="+id+".add_time]").attr("checked",false);
							$("[id="+id+".date_time_format]").attr("disabled",false);
							$("[id="+id+".AddDateBeforeExtension]").attr("disabled",false);
						}else{
							$("[id="+id+".add_date]").attr("disabled",false);
							$("[id="+id+".add_time]").attr("disabled",false);
							$("[id="+id+".date_time_format]").attr("disabled",true);
							$("[id="+id+".AddDateBeforeExtension]").attr("disabled",true);
						}
				},
				iffileexistsChange : function(e){
					// alert(this.value);
					var elId = e.target.id;
					var id = elId.split(".")[0];
					if(this.value == '4'){ // move to folder
						$("[id="+id+".destinationFolder]").attr("disabled",false);
						$("[id="+id+".create_move_to_folder]").attr("disabled",false);
						$("[id="+id+".add_moved_date]").attr("disabled",false);
						$("[id="+id+".add_moved_time]").attr("disabled",false);
						$("[id="+id+".SpecifyMoveFormat]").attr("disabled",false);
						// $("[id="+id+".moved_date_time_format]").attr("disabled",false);
						// $("[id="+id+".AddMovedDateBeforeExtension]").attr("disabled",false);
						$("[id="+id+".ifmovedfileexists]").attr("disabled",false);
					}else{
						$("[id="+id+".destinationFolder]").attr("disabled",true);
						$("[id="+id+".create_move_to_folder]").attr("disabled",true);
						$("[id="+id+".add_moved_date]").attr("disabled",true);
						$("[id="+id+".add_moved_time]").attr("disabled",true);
						$("[id="+id+".SpecifyMoveFormat]").attr("disabled",true);
						// $("[id="+id+".moved_date_time_format]").attr("disabled",true);
						// $("[id="+id+".AddMovedDateBeforeExtension]").attr("disabled",true);
						$("[id="+id+".ifmovedfileexists]").attr("disabled",true);
					}
				},
				isAddMovedDateTimeClick : function(e,v){
						var elId = e.target.id;
						var id = elId.split(".")[0];
						if(e.target.checked){
							$("[id="+id+".AddMovedDateBeforeExtension]").attr("disabled",false);
						}else{
							// alert("add_moved_date:"+$("[id="+id+".add_moved_date]").attr("checked"));
							// alert("add_moved_time:"+$("[id="+id+".add_moved_time]").attr("checked"));
							// alert("SpecifyMoveFormat:"+$("[id="+id+".SpecifyMoveFormat]").attr("checked"));
							if(!$("[id="+id+".add_moved_date]").attr("checked") && !$("[id="+id+".add_moved_time]").attr("checked") && !$("[id="+id+".SpecifyMoveFormat]").attr("checked"))
								$("[id="+id+".AddMovedDateBeforeExtension]").attr("disabled",true);
						}
				},
				isSpecifyMoveFormatClick : function(e,v){
					var elId = e.target.id;
					var id = elId.split(".")[0];
					if(e.target.checked){
						$("[id="+id+".add_moved_date]").attr("checked",false);
						$("[id="+id+".add_moved_time]").attr("checked",false);
						$("[id="+id+".moved_date_time_format]").attr("disabled",false);
						$("[id="+id+".AddMovedDateBeforeExtension]").attr("disabled",false);
					}else{
						$("[id="+id+".moved_date_time_format]").attr("disabled",true);
						if(!$("[id="+id+".add_moved_date]").attr("checked") && !$("[id="+id+".add_moved_time]").attr("checked") && !$("[id="+id+".SpecifyMoveFormat]").attr("checked"))
							$("[id="+id+".AddMovedDateBeforeExtension]").attr("disabled",true);
					}
				},
				success_conditionChange : function(e){
					// alert(this.value);
					var elId = e.target.id;
					var id = elId.split(".")[0];
					if(this.value == 'success_if_no_errors'){ // move to folder
						$("[id="+id+".nr_errors_less_than]").attr("disabled",true);
					}else{
						$("[id="+id+".nr_errors_less_than]").attr("disabled",false);
					}
				}
			}
		},
		copyMoveResultFilename : {
			btn:{
				
			},
			listeners : {
				isAddDateTimeClick : function(e,v){
						var elId = e.target.id;
						var id = elId.split(".")[0];
						if(e.target.checked){
							$("[id="+id+".AddDateBeforeExtension]").attr("disabled",false);
						}else{
							if(!$("[id="+id+".add_date]").attr("checked") && !$("[id="+id+".add_time]").attr("checked"))
								$("[id="+id+".AddDateBeforeExtension]").attr("disabled",true);
						}
				},
				isSpecifyFormatClick : function(e,v){
					var elId = e.target.id;
					var id = elId.split(".")[0];
					if(e.target.checked){
						$("[id="+id+".add_date]").attr("disabled",true);
						$("[id="+id+".add_date]").attr("checked",false);
						$("[id="+id+".add_time]").attr("disabled",true);
						$("[id="+id+".add_time]").attr("checked",false);
						$("[id="+id+".date_time_format]").attr("disabled",false);
						$("[id="+id+".AddDateBeforeExtension]").attr("disabled",false);
					}else{
						$("[id="+id+".add_date]").attr("disabled",false);
						$("[id="+id+".add_time]").attr("disabled",false);
						$("[id="+id+".date_time_format]").attr("disabled",true);
						$("[id="+id+".AddDateBeforeExtension]").attr("disabled",true);
					}
				},
				isSpecifywildcardClick : function(e,v){
					var elId = e.target.id;
					var id = elId.split(".")[0];
					if(e.target.checked){
						$("[id="+id+".wildcard]").attr("disabled",false);
						$("[id="+id+".wildcardexclude]").attr("disabled",false);
					}else{
						$("[id="+id+".wildcard]").attr("disabled",true);
						$("[id="+id+".wildcardexclude]").attr("disabled",true);
					}
				},
				success_conditionChange : function(e){
					// alert(this.value);
					var elId = e.target.id;
					var id = elId.split(".")[0];
					if(this.value == 'success_if_no_errors'){
						$("[id="+id+".nr_errors_less_than]").attr("disabled",true);
					}else{
						$("[id="+id+".nr_errors_less_than]").attr("disabled",false);
					}
				}
			}
		},
		deleteFolders : {
			btn:{
    			fieldAdd : function(c){
        			var rootId = c.getAttribute("rootId");
        			var r = [
        				{ id : rootId+'.fieldId', type : 'number' , text : '' },
        				{ id : rootId+'.folderName', type : 'input' , text : '' }
        			];
        			jQuery.imetabar.createRowByHeader(r,rootId);
        		}
			},
			listeners : {
				success_conditionChange : function(e){
					// alert(this.value);
					var elId = e.target.id;
					var id = elId.split(".")[0];
					if(this.value == 'success_if_no_errors'){
						$("[id="+id+".limit_folders]").attr("disabled",true);
					}else{
						$("[id="+id+".limit_folders]").attr("disabled",false);
					}
				},
				isArgFromPreviousClick : function(e,v){
					var elId = e.target.id;
					var id = elId.split(".")[0];
					if(!e.target.checked){
						$("[id="+id+"_foldersToDeleteTable.btn.add]").attr("disabled",false);
						$("[id="+id+"_foldersToDeleteTable.btn.delete]").attr("disabled",false);
						$("[id="+id+"_foldersToDeleteTable.btn.add.root]").removeClass("x-item-disabled");
						$("[id="+id+"_foldersToDeleteTable.btn.delete.root]").removeClass("x-item-disabled");
						$("#"+id+"_foldersToDeleteTable_gRoot input").attr("disabled",false);
					}else{
						$("[id="+id+"_foldersToDeleteTable.btn.add]").attr("disabled",true);
						$("[id="+id+"_foldersToDeleteTable.btn.delete]").attr("disabled",true);
						$("[id="+id+"_foldersToDeleteTable.btn.add.root]").addClass("x-item-disabled");
						$("[id="+id+"_foldersToDeleteTable.btn.delete.root]").addClass("x-item-disabled");
						$("#"+id+"_foldersToDeleteTable_gRoot input").attr("disabled",true);
					}
				}
			}
		},
		filesexist : {
			btn : {
				filesexistAdd : function(c){
					var rootId = c.getAttribute("rootId");
					var r = [
						{ id : rootId+'.fileId', type : 'number' , text : '' },
						{ id : rootId+'.arguments', type : 'input' , text : '' }
					];
					
					jQuery.imetabar.createRowByHeader(r,rootId);
				}
			}
		},
		
		columnsexist : {
			btn : {
				lineAdd : function(c){
					var rootId = c.getAttribute("rootId");
					var r = [
						{ id : rootId+'.lineId', type : 'number' , text : '' },
						{ id : rootId+'.arguments', type : 'input' , text : '' }
					];
					
					jQuery.imetabar.createRowByHeader(r,rootId);
				}
			}
		},
		mail : {
			btn : {
			},
			listeners : {
				usingAuthentication : function(e,v){
					var elId = e.target.id;
					var id = elId.split(".")[0];
					if(e.target.checked){
						$("[id="+id+".authenticationUser]").attr("disabled",false);
						$("[id="+id+".authenticationPassword]").attr("disabled",false);
						$("[id="+id+".usingSecureAuthentication]").attr("disabled",false);
						//$("[id="+id+".secureConnectionType]").attr("disabled",false);
					}else{
						$("[id="+id+".authenticationUser]").attr("disabled",true);
						$("[id="+id+".authenticationPassword]").attr("disabled",true);
						$("[id="+id+".usingSecureAuthentication]").attr("disabled",true);
						//$("[id="+id+".secureConnectionType]").attr("disabled",true);
					}
				},
				usingSecureAuthentication : function(e,v){
					var elId = e.target.id;
					var id = elId.split(".")[0];
					if(e.target.checked){
						$("[id="+id+".secureConnectionType]").attr("disabled",false);
					}else{
						$("[id="+id+".secureConnectionType]").attr("disabled", true);
					}
				},
				useHTML : function(e,v){
					var elId = e.target.id;
					var id = elId.split(".")[0];
					if(e.target.checked){
						$("[id="+id+".encoding]").attr("disabled",false);
					}else{
						$("[id="+id+".encoding]").attr("disabled",true);
					}
				},
				usePriority : function(e,v){
					var elId = e.target.id;
					var id = elId.split(".")[0];
					if(e.target.checked){
						$("[id="+id+".priority]").attr("disabled",false);
						$("[id="+id+".importance]").attr("disabled",false);
					}else{
						$("[id="+id+".priority]").attr("disabled",true);
						$("[id="+id+".importance]").attr("disabled",true);
					}
				},
				includingFiles : function(e,v){
					var elId = e.target.id;
					var id = elId.split(".")[0];
					if(e.target.checked){
						$("[id="+id+".fileType]").attr("disabled",false);
						$("[id="+id+".zipFiles]").attr("disabled",false);
					    //$("[id="+id+".zipFilename]").attr("disabled",false);
					}else{
						$("[id="+id+".fileType]").attr("disabled",true);
						$("[id="+id+".zipFiles]").attr("disabled",true);
						//$("[id="+id+".zipFilename]").attr("disabled",true);
					}
				},
				zipFiles : function(e,v){
					var elId = e.target.id;
					var id = elId.split(".")[0];
					if(e.target.checked){
						$("[id="+id+".zipFilename]").attr("disabled",false);
						
					
					}else{
						$("[id="+id+".zipFilename]").attr("disabled",true);
	
						
					}
				}
		    }
		},
		getpop : {
			btn : {	
			},
			listeners : {
			    getpopChange : function(e){
					var elId = e.target.id;
					var id = elId.split(".")[0];
					if(this.value == '0'){ 
						$("[id="+id+".firstmails]").attr("disabled",true);
					}else if(this.value == '1'){
						$("[id="+id+".firstmails]").attr("disabled",true);
					}else if(this.value == '2'){
					    $("[id="+id+".firstmails]").attr("disabled",false);
					}
				},
				usessl : function(e,v){
					var elId = e.target.id;
					var id = elId.split(".")[0];
					if(e.target.checked){
						$("[id="+id+".sslport]").attr("disabled",false);
					}else{
						$("[id="+id+".sslport]").attr("disabled",true);
					}
				}
			}		
		},
		folderisempty : {
			btn : {	
			},
			listeners : {
				specifywildcard : function(e,v){
					var elId = e.target.id;
					var id = elId.split(".")[0];
					if(e.target.checked){
						$("[id="+id+".wildcard]").attr("disabled",false);
					}else{
						$("[id="+id+".wildcard]").attr("disabled",true);
					}
				}
			}		
		},
		ftp : {
			btn : {	
			dbTest : function(e,v){
					var elId = e.target.id;
					var id = elId.split(".")[0];
					var dbId = 0;
					try{
						dbId = id.split("_")[1];
					}catch(e){}
					
					$("#"+id).ajaxSubmit({
						type: "POST",
						url:"ImetaAction!testDatabase.action",
						dataType:"json",
						data : {
							id : id,
							databaseId : dbId
						},
						success : function(json){
							$.imessagebox("#ibody",json);
						}
					});
					
					e.stopPropagation();
				}
				
			},
			listeners : {
			    ftpChange : function(e){
					var elId = e.target.id;
					var id = elId.split(".")[0];
					if(this.value == '0'){ 
						$("[id="+id+".nr_limit]").attr("disabled",true);
					}else{
						$("[id="+id+".nr_limit]").attr("disabled",false);
					}
				},
				movefiles : function(e,v){
					var elId = e.target.id;
					var id = elId.split(".")[0];
					if(e.target.checked){
						$("[id="+id+".movetodirectory]").attr("disabled",false);
						$("[id="+id+".createmovefolder]").attr("disabled",false);
					}else{
						$("[id="+id+".movetodirectory]").attr("disabled",true);
						$("[id="+id+".createmovefolder]").attr("disabled",true);
					}
				},
				SpecifyFormat : function(e,v){
					var elId = e.target.id;
					var id = elId.split(".")[0];
					if(e.target.checked){
						$("[id="+id+".adddate]").attr("disabled",true);
						$("[id="+id+".addtime]").attr("disabled",true);
						$("[id="+id+".date_time_format]").attr("disabled",false);
						$("[id="+id+".AddDateBeforeExtension]").attr("disabled",false);
					}else{
						$("[id="+id+".adddate]").attr("disabled",false);
						$("[id="+id+".addtime]").attr("disabled",false);
						$("[id="+id+".date_time_format]").attr("disabled",true);
						$("[id="+id+".AddDateBeforeExtension]").attr("disabled",true);
					}
				},
				onlyGettingNewFiles : function(e,v){
					var elId = e.target.id;
					var id = elId.split(".")[0];
					if(e.target.checked){
						$("[id="+id+".SifFileExists]").attr("disabled",false);
					}else{
						$("[id="+id+".SifFileExists]").attr("disabled",true);
					}
				}
			}		
		},
		sftp : {
			btn : {	
			},
			listeners : {
				copyprevious : function(e,v){
					var elId = e.target.id;
					var id = elId.split(".")[0];
					if(e.target.checked){
						$("[id="+id+".wildcard]").attr("disabled",false);
					}else{
						$("[id="+id+".wildcard]").attr("disabled",true);
					}
				}
			}		
		},
		sftpput : {
			btn : {	
			},
			listeners : {
				copyprevious : function(e,v){
					var elId = e.target.id;
					var id = elId.split(".")[0];
					if(e.target.checked){
						$("[id="+id+".localDirectory]").attr("disabled",false);
						$("[id="+id+".wildcard]").attr("disabled",false);
					}else{
						$("[id="+id+".localDirectory]").attr("disabled",true);
						$("[id="+id+".wildcard]").attr("disabled",true);
					}
				}
			}		
		},
		ssh2get : {
			btn : {	
			},
			listeners : {
			    ssh2getChange : function(e){
					var elId = e.target.id;
					var id = elId.split(".")[0];
					if(this.value == '2'){ 
						$("[id="+id+".destinationfolder]").attr("disabled",false);
						$("[id="+id+".createdestinationfolder]").attr("disabled",false);
					}else{
						$("[id="+id+".destinationfolder]").attr("disabled",true);
						$("[id="+id+".createdestinationfolder]").attr("disabled",true);
					}
				},
				usehttpproxy : function(e,v){
					var elId = e.target.id;
					var id = elId.split(".")[0];
					if(e.target.checked){
						$("[id="+id+".httpProxyHost]").attr("disabled",false);
						$("[id="+id+".httpproxyport]").attr("disabled",false);
						$("[id="+id+".useBasicAuthentication]").attr("disabled",false);
					}else{
						$("[id="+id+".httpProxyHost]").attr("disabled",true);
						$("[id="+id+".httpproxyport]").attr("disabled",true);
						$("[id="+id+".useBasicAuthentication]").attr("disabled",true);
						$("[id="+id+".httpproxyusername]").attr("disabled",true);
						$("[id="+id+".httpProxyPassword]").attr("disabled",true);
					}
				},
				useBasicAuthentication : function(e,v){
					var elId = e.target.id;
					var id = elId.split(".")[0];
					if(e.target.checked){
						$("[id="+id+".httpproxyusername]").attr("disabled",false);
						$("[id="+id+".httpProxyPassword]").attr("disabled",false);
					}else{
						$("[id="+id+".httpproxyusername]").attr("disabled",true);
						$("[id="+id+".httpProxyPassword]").attr("disabled",true);
					}
				},
				publicpublickey : function(e,v){
					var elId = e.target.id;
					var id = elId.split(".")[0];
					if(e.target.checked){
						$("[id="+id+".keyFilename]").attr("disabled",false);
						$("[id="+id+".keyFilePass]").attr("disabled",false);
					}else{
						$("[id="+id+".keyFilename]").attr("disabled",true);
						$("[id="+id+".keyFilePass]").attr("disabled",true);
					}
				}
			}		
		},
		ssh2put : {
			btn : {	
			},
			listeners : {
			    ssh2putChange : function(e){
					var elId = e.target.id;
					var id = elId.split(".")[0];
					if(this.value == '2'){ 
						$("[id="+id+".destinationfolder]").attr("disabled",false);
						$("[id="+id+".createDestinationFolder]").attr("disabled",false);
					}else{
						$("[id="+id+".destinationfolder]").attr("disabled",true);
						$("[id="+id+".createDestinationFolder]").attr("disabled",true);
					}
				},
				usehttpproxy : function(e,v){
					var elId = e.target.id;
					var id = elId.split(".")[0];
					if(e.target.checked){
						$("[id="+id+".httpproxyhost]").attr("disabled",false);
						$("[id="+id+".httpproxyport]").attr("disabled",false);
						$("[id="+id+".useBasicAuthentication]").attr("disabled",false);
					}else{
						$("[id="+id+".httpproxyhost]").attr("disabled",true);
						$("[id="+id+".httpproxyport]").attr("disabled",true);
						$("[id="+id+".useBasicAuthentication]").attr("disabled",true);
						$("[id="+id+".httpproxyusername]").attr("disabled",true);
						$("[id="+id+".httpProxyPassword]").attr("disabled",true);
					}
				},
				useBasicAuthentication : function(e,v){
					var elId = e.target.id;
					var id = elId.split(".")[0];
					if(e.target.checked){
						$("[id="+id+".httpproxyusername]").attr("disabled",false);
						$("[id="+id+".httpProxyPassword]").attr("disabled",false);
					}else{
						$("[id="+id+".httpproxyusername]").attr("disabled",true);
						$("[id="+id+".httpProxyPassword]").attr("disabled",true);
					}
				},
				publicpublickey : function(e,v){
					var elId = e.target.id;
					var id = elId.split(".")[0];
					if(e.target.checked){
						$("[id="+id+".keyFilename]").attr("disabled",false);
						$("[id="+id+".keyFilePass]").attr("disabled",false);
					}else{
						$("[id="+id+".keyFilename]").attr("disabled",true);
						$("[id="+id+".keyFilePass]").attr("disabled",true);
					}
				}
			}		
		},
		ftpdelete : {
			btn : {	
			},
			listeners : {
			    ftpdeleteChange : function(e){
					var elId = e.target.id;
					var id = elId.split(".")[0];
					if(this.value == '0'){ 
						$("[id="+id+".nr_limit_success]").attr("disabled",true);
					}else{
						$("[id="+id+".nr_limit_success]").attr("disabled",false);
					}
				},
				protocolChange : function(e){
					var elId = e.target.id;
					var id = elId.split(".")[0];
					if(this.value == '2'){ 
					    $("[id="+id+".publicpublickey]").attr("disabled",true);
						$("[id="+id+".keyFilename]").attr("disabled",true);
						$("[id="+id+".keyFilePass]").attr("disabled",true);
					}else{
					    $("[id="+id+".publicpublickey]").attr("disabled",false);
						$("[id="+id+".keyFilename]").attr("disabled",false);
						$("[id="+id+".keyFilePass]").attr("disabled",false);
					}
				},
				useproxy : function(e,v){
					var elId = e.target.id;
					var id = elId.split(".")[0];
					if(e.target.checked){
						$("[id="+id+".proxyHost]").attr("disabled",false);
						$("[id="+id+".proxyPort]").attr("disabled",false);
						$("[id="+id+".proxyUsername]").attr("disabled",false);
						$("[id="+id+".proxyPassword]").attr("disabled",false);
					}else{
						$("[id="+id+".proxyHost]").attr("disabled",true);
						$("[id="+id+".proxyPort]").attr("disabled",true);
						$("[id="+id+".proxyUsername]").attr("disabled",true);
						$("[id="+id+".proxyPassword]").attr("disabled",true);
					}
				},
				publicpublickey : function(e,v){
					var elId = e.target.id;
					var id = elId.split(".")[0];
					if(e.target.checked){
						$("[id="+id+".keyFilename]").attr("disabled",false);
						$("[id="+id+".keyFilePass]").attr("disabled",false);
					}else{
						$("[id="+id+".keyFilename]").attr("disabled",true);
						$("[id="+id+".keyFilePass]").attr("disabled",true);
					}
				},
				copyprevious : function(e,v){
					var elId = e.target.id;
					var id = elId.split(".")[0];
					if(e.target.checked){
						$("[id="+id+".ftpDirectory]").attr("disabled",false);
						$("[id="+id+".wildcard]").attr("disabled",false);
					}else{
						$("[id="+id+".ftpDirectory]").attr("disabled",true);
						$("[id="+id+".wildcard]").attr("disabled",true);
					}
				}
			}		
		},
		jobentryXMLwellformed : {
			listeners : {
				successOnChange : function(e){
					//alert(this.value);
					var elId = e.target.id;
					var id = elId.split(".")[0];
					if(this.value == 'success_if_no_errors'){ //移动文件
						$("[id="+id+".nr_errors_less_than]").attr("disabled",true);
					}else{
						$("[id="+id+".nr_errors_less_than]").attr("disabled",false);
					}
				}
			},
			btn : {
				fieldAdd : function(c){
        			var rootId = c.getAttribute("rootId");
        			var r = [
        				{ id : rootId+'.fieldId', type : 'number' , text : '' },
        				{ id : rootId+'.source_filefolder', type : 'input' , text : '' },
        				{ id : rootId+'.wildcard', type : 'input' , text : '' }
        			];
        			jQuery.imetabar.createRowByHeader(r,rootId);
        		}
			}
		},
		jobentrytruncatetables : {
			btn : {
				fieldAdd : function(c){
        			var rootId = c.getAttribute("rootId");
        			var r = [
        				{ id : rootId+'.fieldId', type : 'number' , text : '' },
        				{ id : rootId+'.arguments', type : 'input' , text : '' },
        				{ id : rootId+'.schemaname', type : 'input' , text : '' }
        			];
        			jQuery.imetabar.createRowByHeader(r,rootId);
        		}
			}
		},
		jobentrysetvariables : {
			btn : {
				fieldAdd : function(c){
        			var rootId = c.getAttribute("rootId");
        			var r = [
        				{ id : rootId+'.fieldId', type : 'number' , text : '' },
        				{ id : rootId+'.name', type : 'input' , text : '' },
        				{ id : rootId+'.value', type : 'input' , text : '' },
        				{ id : rootId+'.type', type : 'select' , text : '' }
        			];
        			jQuery.imetabar.createRowByHeader(r,rootId);
        		}
			}
		},
		jobentryMSaccessbulkload : {
			btn : {
				fieldAdd : function(c){
        			var rootId = c.getAttribute("rootId");
        			var r = [
        				{ id : rootId+'.fieldId', type : 'number' , text : '' },
        				{ id : rootId+'.source_filefolder', type : 'input' , text : '' },
        				{ id : rootId+'.source_wildcard', type : 'input' , text : '' },
        				{ id : rootId+'.delimiter', type : 'input' , text : '' },
        				{ id : rootId+'.target_Db', type : 'input' , text : '' },
        				{ id : rootId+'.target_table', type : 'input' , text : '' }
        			];
        			jQuery.imetabar.createRowByHeader(r,rootId);
        		}
			},
			listeners : {
				successOnChange : function(e){
					//alert(this.value);
					var elId = e.target.id;
					var id = elId.split(".")[0];
					if(this.value == 'success_if_no_errors'){ //移动文件
						$("[id="+id+".limit]").attr("disabled",true);
					}else{
						$("[id="+id+".limit]").attr("disabled",false);
					}
				}
			}
		},
		jobentrywaitforSQL : {
			listeners : {
				iscustomSQLListeners : function(e,v){
					var elId = e.target.id;
					var id = elId.split(".")[0];
					if(e.target.checked){
						$("[id="+id+".schemaname]").attr("disabled",true);
						$("[id="+id+".tablename]").attr("disabled",true);
						$("[id="+id+".isUseVars]").attr("disabled",false);
						$("[id="+id+".isClearResultList]").attr("disabled",false);
						$("[id="+id+".isAddRowsResult]").attr("disabled",false);
//						$("[id="+id+".getSqlSelect]").attr("disabled",false);
						$("[id="+id+".customSQL]").attr("disabled",false);
						
					}else{
						$("[id="+id+".schemaname]").attr("disabled",false);
						$("[id="+id+".tablename]").attr("disabled",false);
						$("[id="+id+".isUseVars]").attr("disabled",true);
						$("[id="+id+".isClearResultList]").attr("disabled",true);
						$("[id="+id+".isAddRowsResult]").attr("disabled",true);
//						$("[id="+id+".getSqlSelect]").attr("disabled",true);
						$("[id="+id+".customSQL]").attr("disabled",true);
					}
				}
			}
		},
		jobentryevaltablecontent : {
			listeners : {
				iscustomSQLListeners : function(e,v){
					var elId = e.target.id;
					var id = elId.split(".")[0];
					if(e.target.checked){
						$("[id="+id+".schemaname]").attr("disabled",true);
						$("[id="+id+".tablename]").attr("disabled",true);
						$("[id="+id+".isUseVars]").attr("disabled",false);
						$("[id="+id+".isClearResultList]").attr("disabled",false);
						$("[id="+id+".isAddRowsResult]").attr("disabled",false);
//						$("[id="+id+".getSqlSelect]").attr("disabled",false);
						$("[id="+id+".customSQL]").attr("disabled",false);
						
					}else{
						$("[id="+id+".schemaname]").attr("disabled",false);
						$("[id="+id+".tablename]").attr("disabled",false);
						$("[id="+id+".isUseVars]").attr("disabled",true);
						$("[id="+id+".isClearResultList]").attr("disabled",true);
						$("[id="+id+".isAddRowsResult]").attr("disabled",true);
//						$("[id="+id+".getSqlSelect]").attr("disabled",true);
						$("[id="+id+".customSQL]").attr("disabled",true);
					}
				}
			}
		},
		jobentrySNMPtrap : {
			listeners : {
				targettypeChange : function(e){
					//alert(this.value);
					var elId = e.target.id;
					var id = elId.split(".")[0];
					if(this.value == 'user'){ 
						$("[id="+id+".comString]").attr("disabled",false);
						$("[id="+id+".user]").attr("disabled",true);
						$("[id="+id+".passphrase]").attr("disabled",true);
						$("[id="+id+".engineid]").attr("disabled",true);
					}else{
						$("[id="+id+".comString]").attr("disabled",true);
						$("[id="+id+".user]").attr("disabled",false);
						$("[id="+id+".passphrase]").attr("disabled",false);
						$("[id="+id+".engineid]").attr("disabled",false);
					}
				}
			}
		},
		btn : {
			ok : function(e,v){
				var elId = e.target.id;
				var id = elId.split(".")[0];
				$("#"+id).ajaxSubmit({
					type:"POST",
					url:"ImetaAction!editElementSubmit.action",
					dataType:"json",
					data : {
						id : id,
						roType : jQuery.iPortalTab.OBJECT_TYPE_JOB,
						roName : e.target.getAttribute("jobsName"),
						directoryId : e.target.getAttribute("directoryId"),
						elementName : e.target.getAttribute("jobEntryName")
					},
					success : function(json){
						if(json.oldName != json.newName){
							var el = jQuery.imenu.iContent.getCanvasElByText(json.oldName,'step');
							jQuery.imenu.iContent.updateEl(el,{
							    stepName : json.newName,
								bText : [json.newName]
							});
							jQuery.imenu.iContent.redraw();
						}
						jQuery.iPortalTab.updateActiveTab(jQuery.iPortalTab.TYPE_ACTION_CHANGE_JOB_ENTRY);
						$("#window-"+id).remove();
						$.imessagebox("#ibody",json);
					}
				});
			},
			cancel : function(e,v){
				var elId = e.target.id;
				var id = elId.split(".")[0];
				$("#window-"+id).remove();
			}
		}
	}
};