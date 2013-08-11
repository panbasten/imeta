jQuery.imeta.trans = {
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
	                url: "ImetaBaseAction!getDirectory.action",
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
};