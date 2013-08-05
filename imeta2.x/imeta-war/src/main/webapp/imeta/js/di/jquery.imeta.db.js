jQuery.imeta.db = {
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
};