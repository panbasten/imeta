jQuery.imeta.job = {
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
};