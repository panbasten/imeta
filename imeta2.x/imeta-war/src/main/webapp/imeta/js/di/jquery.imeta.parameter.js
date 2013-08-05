jQuery.imeta = {};
jQuery.imeta.steps = {};

jQuery.imeta.parameter = {
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
};