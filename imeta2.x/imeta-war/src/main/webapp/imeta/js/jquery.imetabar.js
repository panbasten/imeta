jQuery.imetabar = {
    /**
     * 图片根目录
     *
     * @type
     */
    canvasImgPath: $path + "/imeta/images/imetabar/",
    
    windowImgMap: {},
    
    allBars : ['imb','iow','iet','ica'],
    
    createPanel : function(){
    	if(c&&c.id){
    		$("<div class='x-panel x-form-label-right'></div>")
	    	.append($("<div class='x-panel-tl'></div>")
	    	.append($("<div class='x-panel-tr'></div>")
	    	.append($("<div class='x-panel-tc'></div>")
	    	.append($("<div class='x-panel-header x-unselectable'></div>")
	    	.append($("<span class='x-panel-header-text'>登录系统</span>"))))));
    	}
    },
    
    /**
     * 创建按钮
     * @param c 按钮信息
     * 		{id,clazz,enabled}
     * @param p 父对象
     */
    createButton : function(c,p){
    	if(c&&c.id&&c.clazz){
    		var btnTable = $("<table class='x-btn x-btn-icon' cellspacing='0'></table>");
    		btnTable.attr("id", c.id+".root");
    		if(!c.enabled){
    			btnTable.addClass("x-item-disabled");
    		}
    		var btnTbody = $("<tbody class='x-btn-small x-btn-icon-small-left'></tbody>").appendTo(btnTable);
    		var btnTrTop = $("<tr></tr>").appendTo(btnTbody);
    		$("<td class='x-btn-tl'><i></i></td>").appendTo(btnTrTop);
    		$("<td class='x-btn-tc'></td>").appendTo(btnTrTop);
    		$("<td class='x-btn-tr'><i></i></td>").appendTo(btnTrTop);
    		
    		var btnTrMiddle = $("<tr></tr>").appendTo(btnTbody);
    		$("<td class='x-btn-ml'><i></i></td>").appendTo(btnTrMiddle);
    		var btnMain = $("<td class='x-btn-mc'></td>").appendTo(btnTrMiddle);
    		$("<td class='x-btn-mr'><i></i></td>").appendTo(btnTrMiddle);
    		
    		var btnTrBottom = $("<tr></tr>").appendTo(btnTbody);
    		$("<td class='x-btn-bl'><i></i></td>").appendTo(btnTrBottom);
    		$("<td class='x-btn-bc'></td>").appendTo(btnTrBottom);
    		$("<td class='x-btn-br'><i></i></td>").appendTo(btnTrBottom);
    		
    		var ex = "";
    		if(c.fn){
    			ex +=  (" onclick='"+c.fn+"(this);' ");
    		}
    		if(c.rootId){
    			ex +=  (" rootId='"+c.rootId+"' ");
    		}
    		if(!c.enabled){
    			ex +=  " disabled ";
    		}
    		if(c.title){
    			ex += " title='"+c.title+"' ";
    		}
    		var btn = $("<button class='x-btn-text' type='button' "+ex+" ></button>");
    		btn.attr("id", c.id);
    		btn.addClass(c.clazz);
    		btnMain.append($("<em unselectable='on'></em>").append(btn));
    		
    		if(p){
    			btnTable.appendTo(p);
    		}
    		return btnTable;
    	}
    },
    
    /**
     * 创建分隔符
     */
    createSeparator : function(p){
    	var s = $("<span class='xtb-sep'></span>");
    	if(p){
    		s.appendTo(p);
    	}
    	return s;
    },
    
    /**
     * 通过头部删除行
     */
    deleteRowByHeader : function(rootId){
		var listId = rootId + "_gList";
		$("#"+listId+" .x-grid-row-selected").remove();
		var index=0;
		$.each($("#"+listId+" .x-grid-td-numberer div.x-grid-cell-inner"),function(e,v){
			$(v).html(++index);
		});
		$("[id="+listId+"]").attr("rowNr",index);
		$("[id="+rootId+"_recordNum]").html("共 "+index+" 条记录");
    },
    
    /**
     * 通过头部生成行，并且进行验证重复的行
     */
    createRowByHeaderWithCheck : function(c,rootId){
    	// 验证重复的行
    	if(!jQuery.imetabar.checkRowByHeader(c,rootId)){
	    	// 添加行
	    	jQuery.imetabar.createRowByHeader(c,rootId);
    	}
    },
    
    checkRowByHeader : function(c,rootId){
    	if(c){
    		var listId = rootId + "_gList";
    		var rowNr = $("[id="+listId+"]").attr("rowNr");
			rowNr = (rowNr)?rowNr:0;
    		// 循环行
    		for(var index=0;index<rowNr;index++){
    			var isSame = true;
    			$.each(c,function(i,item){
        			var id = item.id;
        			var h = $("[id="+id+".header]");
        		    var cellInfo = {
        		        type : h.attr("cellType"),
        		        width : h.attr("cellWidth"),
        		        options : h.attr("options")
        		        
        		    };
        		    $.extend(cellInfo,item);
        		    if(item.type=='header'){
        				$.extend(cellInfo,{
        				    type : h.attr("cellType")
        				});
        			}
        		    if( !cellInfo.disabled && !(cellInfo.type=='number') && !(cellInfo.type=='checkbox') && !(cellInfo.type=='password') && !(cellInfo.type=='select') ){
        		    	if($("[id="+id+"]").get(index).value != cellInfo.text){
        		    		isSame = false;
        		    	}
        		    }
        		});
    			
    			if(isSame){
    				return true;
    			}
    		}
    		
    		return false;
    	}
    },
    
    /**
     * 通过头部生成行
     */
    createRowByHeader : function(c,rootId){
        if(c){
            var listId = rootId + "_gList";
			var listDiv = $("<div class='x-grid-row x-grid-row-show' onmouseover='jQuery.imetabar.gridRowMouseOver(this);' onmouseout='jQuery.imetabar.gridRowMouseOut(this);' onclick='jQuery.imetabar.gridRowClick(this);'></div>");
			var rowNr = $("[id="+listId+"]").attr("rowNr");
			rowNr = (rowNr)?rowNr:0;
            
            var rowTr = $("<tr></tr>");
    		var rowTable = $("<table cellspacing='0' cellpadding='0' border='0'></table>")
    			.append($("<tbody></tbody>")
    			.append(rowTr));
    			
    		$.each(c,function(i,item){
    		    var cellCanClick = "";
    		    var columnType = "";
    		    var id = item.id;
    		    var h = $("[id="+id+".header]");
    		    var cellInfo = {
    		        type : h.attr("cellType"),
    		        width : h.attr("cellWidth"),
    		        options : h.attr("options")
    		        
    		    };
    		    $.extend(cellInfo,item);
    		    if(item.type=='header'){
    				$.extend(cellInfo,{
    				    type : h.attr("cellType")
    				});
    			}
    			
    			// 创建行
    			var exAttr = (cellInfo.disabled)?"disabled":" ";
    			var text = cellInfo.text;
    			if(text && text != '' && typeof text == 'string'){
    				text = text.replace(new RegExp("'","gm"),"&apos;");
    			}
    			if(cellInfo.type=='number'){
    				columnType = "class='x-grid-td-numberer'";
    				text = (text&&text!=null&&text!='')?text:(++rowNr);
    			}else if(cellInfo.type=='checkbox'){
    				columnType = "class='x-grid-td-center'";
    				if("true" == text){
    					exAttr += "checked";
    				}
    				text = "<input id='"+id+"' name='"+id+"' type='checkbox' "+exAttr+" />";
    				cellCanClick = "onclick='jQuery.imetabar.gridCellClick(this);'";
    			}else if(cellInfo.type=='input'){
    				text = "<input  id='"+id+"' name='"+id+"' type='text' value='"+text+"' oldvalue='"+text+"' "+exAttr+" onchange='jQuery.imetabar.gridCellModify(this);' />";
    				cellCanClick = "onclick='jQuery.imetabar.gridCellClick(this);'";
    			}else if(cellInfo.type=='password'){
    				text = "<input  id='"+id+"' name='"+id+"' type='password' value='"+text+"' "+exAttr+" />";
    				cellCanClick = "onclick='jQuery.imetabar.gridCellClick(this);'";
    			}else if(cellInfo.type=='select'){
    				text = "<select  id='"+id+"' name='"+id+"' oldvalue='"+text+"' "+exAttr+" onchange='jQuery.imetabar.gridCellModify(this);' >";
    				text = text + jQuery.imetabar.getOptions(cellInfo.options,text);
    				text = text + "</select>";
    				cellCanClick = "onclick='jQuery.imetabar.gridCellClick(this);'";
    			}else if(cellInfo.type=='button'){
    				text = "<img id='"+id+"' name='"+id+"' src='"+cellInfo.src+"' onclick='"+cellInfo.cellClickFn+"' />";
    			}else{
    				text = "<input  id='"+id+"' name='"+id+"' type='text' title='"+text+"' value='"+text+"' readonly />";
    			}
    			
    			var w = parseInt(cellInfo.width)+2;
    			rowTr.append($("<td "+columnType+" style='width:"+w+"px;'></td>")
    				.append($("<div class='x-grid-cell-inner' "+cellCanClick+">"+text+"</div>")));
    		});
    		
    		$("[id="+listId+"]").attr("rowNr",rowNr);
			rowTable.appendTo(listDiv);
			$("[id="+listId+"]").append(listDiv);
			
			$("[id="+rootId+"_recordNum]").html("共 "+rowNr+" 条记录");
        }
    },
    
    getOptions : function(options,text){
       var newtext = "";
       if(options){
		    var ops = options.split(";");
		    for(var i=0;i<ops.length;i++){
		        if(ops[i] == ':'){
		            newtext = newtext + "<option value=''></option>";
		        }else{
			        var kv = ops[i].split(":");
			        if(kv.length==2){
			           newtext = newtext + "<option value='"+kv[0]+"' ";
			           if(text == kv[0]){
			               newtext = newtext + " selected ";
			           }
			           newtext = newtext + ">"+kv[1]+"</option>";
			        }else if(kv.length==1&&kv[0]!=''){
			            newtext = newtext + "<option value='"+kv[0]+"'></option>";
			        }
		        }
		    }
		}
		return newtext;
    },
    
    /**
     * c-行值
     * h-头值
     */
    createRow : function(c,h,p){
    	if(c){
    		var rowTr = $("<tr></tr>");
    		var rowTable = $("<table cellspacing='0' cellpadding='0' border='0'></table>")
    			.append($("<tbody></tbody>")
    			.append(rowTr));
    		var hasH = true;
    		if(!h || h==null){
    			h = c;
    			hasH = false;
    		}
    		
    		$.each(h,function(i,item){
    			var text = c[i].text;
    			if(text && text != '' && typeof text == 'string'){
    				text = text.replace(new RegExp("'","gm"),"&apos;");
    			}
    			var id = item.id;
    			var type = item.type;
    			var sMap = item.extendsMap;
    			
    			var columnType = "";
    			var cellCanClick = "";
    			
    			if(c[i].id&&hasH){
    				id = id + "." + c[i].id;
    			}
    			if(c[i].type!='header'){
    				type = c[i].type;
    			}
    			if(c[i].extendsMap){
    				extendsMap = c[i].extendsMap;
    			}
    			
    			var exAttr = (c[i].disabled)?"disabled":" ";
    			
    			if(type=='number'){
    				columnType = "class='x-grid-td-numberer'";
    			}else if(type=='checkbox'){
    				columnType = "class='x-grid-td-center'";
    				if("true" == text){
    					exAttr += "checked";
    				}
    				text = "<input id='"+id+"' name='"+id+"' type='checkbox' "+exAttr+" />";
    				cellCanClick = "onclick='jQuery.imetabar.gridCellClick(this);'";
    			}else if(type=='input'){
    				text = "<input  id='"+id+"' name='"+id+"' type='text' value='"+text+"' oldvalue='"+text+"' "+exAttr+" onchange='jQuery.imetabar.gridCellModify(this);' />";
    				cellCanClick = "onclick='jQuery.imetabar.gridCellClick(this);'";
    			}else if(type=='password'){
    				text = "<input  id='"+id+"' name='"+id+"' type='password' value='"+text+"' "+exAttr+" />";
    				cellCanClick = "onclick='jQuery.imetabar.gridCellClick(this);'";
    			}else if(type=='select'){
    				var newtext = "<select  id='"+id+"' name='"+id+"' oldvalue='"+text+"' "+exAttr+" onchange='jQuery.imetabar.gridCellModify(this);' >";
    				newtext = newtext + jQuery.imetabar.getOptions(item.options,text);
    				newtext = newtext + "</select>";
    				text = newtext;
    				cellCanClick = "onclick='jQuery.imetabar.gridCellClick(this);'";
    			}else if(type=='button'){
    				text = "<img id='"+id+"' name='"+id+"' src='"+c[i].src+"' onclick='"+c[i].cellClickFn+"' "+exAttr+" />";
    			}else{
    				text = "<input  id='"+id+"' name='"+id+"' type='text' title='"+text+"' value='"+text+"' readonly />";
    			}
    			
    			var w = item.width+2;
    			rowTr.append($("<td "+columnType+" style='width:"+w+"px;'></td>")
    				.append($("<div class='x-grid-cell-inner' "+cellCanClick+">"+text+"</div>")));
    		});

			if(p){
    			rowTable.appendTo(p);
    		}
    		return rowTable;
    	}
    },
    
    /**
     * 创建文字对象
     */
    createText : function(text,p,id){
    	var s = $("<div class='xtb-text' id='"+id+"'></div>");
    	s.html(text);
    	if(p){
    		s.appendTo(p);
    	}
    	return s;
    },
    
    /**
     * grid的cell修改
     */
    gridCellModify : function(c){
    	if(c.getAttribute('oldvalue') == c.value){
    		c.parentNode.className="x-grid-cell-inner";
    	}else{
    		c.parentNode.className="x-grid-cell-inner x-grid-dirty-cell";
    	}
    },
    
    /**
     * grid的cell点击
     */
     gridCellClick : function(c){
     	$(".x-grid-cell-selected").removeClass("x-grid-cell-selected");
     	c.className=c.className + " x-grid-cell-selected";
     },
     
     gridRowMouseOver : function(c){
     	$(".x-grid-row-over").removeClass("x-grid-row-over");
     	c.className=c.className + " x-grid-row-over";
     },
     
     gridRowMouseOut : function(c){
     	c.className=c.className.replace(" x-grid-row-over","");
     },
     
     gridRowClick: function(c){
     	$(".x-grid-row-selected").removeClass("x-grid-row-selected");
     	c.className=c.className + " x-grid-row-selected";
     	if(c.getAttribute('exRowClick')){
     		eval(c.getAttribute('exRowClick')+"(c);");
     	}
     },
     
     gridScrollFn : function(o,id){
     	$("[id="+id+"_gHeaderDiv]").css("left","-"+o.scrollLeft+"px");
     },
    
    /**
     * 创建列表
     * @param c 按钮信息
     * 		{
     * 			id : string,
     * 			widthPercent : number,
     * 			height : number,
     * 			header : [
     * 				{
     * 					id : string,
     * 					text : string,
     * 					type : string,     注：normal 一般格式, number 排序, checkbox 选择框
     * 					enabled : boolean,
     * 					width : number
     * 				}
     * 			],
     * 		}
     * @param p 父对象
     */
    createGrid : function(c,p){
    	if(c&&c.id){
    		var id = c.id;
    		var h = (c.height)?c.height:200;
    		var allWidth = 0;
    		var w = (c.widthPercent)?c.widthPercent:50;
    		w = (c.single)?(w-2):w;
    		var s = (c.single)?'position:absolute;':'float:left;';
    		p.height(h+24);
    		var gRoot = $("<div id='"+id+"_gRoot' class='x-panel-body' style='width:"+w+"%;"+s+"'></div>");
    		gRoot.height(h);
    		// 头部
    		var gHeader = $("<div id='"+id+"_gHeader' class='x-grid-header' style='width:auto;'></div>").appendTo(gRoot);
    		var gHeaderDiv = $("<div id='"+id+"_gHeaderDiv' style='padding:0px;position:relative;'></div>").appendTo(gHeader);
    		var gHeaderTable = $("<table cellspacing='0' cellpadding='0' border='0' ></table>").appendTo(gHeaderDiv);
    		var gHeaderThead = $("<thead></thead>").appendTo(gHeaderTable);
    		var gHeaderTr = $("<tr></tr>").appendTo(gHeaderThead);
    		if(c.header){
    			$.each(c.header,function(i,item){
    				var theHeader = $.extend({}, jQuery.imetabar.defaultGridHeader, item);
    				
    				var gHeaderInnerDiv = $("<div class='x-grid-hd-inner"+((theHeader.enabled)?"":" x-grid-hd-enabled")+"' unselectable='on'></div>");
    				if(theHeader.id){
    				    gHeaderInnerDiv.attr("id",theHeader.id+".header");
    				}
    				gHeaderInnerDiv.attr("cellWidth",theHeader.width);
    				gHeaderInnerDiv.attr("cellType",theHeader.type);
    				if(theHeader.options){
    				    gHeaderInnerDiv.attr("options",theHeader.options);
    				}
    				allWidth += (theHeader.width+2);
    				gHeaderTr.append($("<td style='width:"+theHeader.width+"px;'></td>")
    						.append(gHeaderInnerDiv.html(theHeader.text)));
    				
    			});
    		}
    		
    		// 列表
    		var gListScroll = $("<div id='"+id+"_gListScroll' class='x-grid-list-scroll' onscroll='jQuery.imetabar.gridScrollFn(this,\""+id+"\");'></div>").appendTo(gRoot);
    		
    		if(c.hasBottomBar){
    			gListScroll.height(h-46);
    		}else{
    			gListScroll.height(h-23);
    		}
    		
    		var gList = $("<div id='"+id+"_gList' class='x-grid-list'>").appendTo(gListScroll);
    		gHeaderDiv.width(allWidth);
    		gList.width(allWidth);
    		
    		// 加入列
    		var exRow = "";
    		var exDisplayClass = "";
    		var style = "";
    		$.each(c.rows,function(i,item){
    			exRow="";
    			if(item.rowClickFn){
	    			exRow="exRowClick='"+item.rowClickFn+"'";
	    		}
	    		if(item.extendsMap){
	    			$.each(item.extendsMap,function(k,v){
	    				exRow += ' ';
	    				exRow += (k+"='"+v+"'");
	    			});
	    		}
    			gList.append($("<div "+((item.style)?("style='"+item.style+"'"):" " )+" class='x-grid-row "+( (item.clazz)?item.clazz:" " )+( (item.rowDisplay)?" x-grid-row-show":" x-grid-row-hidden" )+"' onmouseover='jQuery.imetabar.gridRowMouseOver(this);' onmouseout='jQuery.imetabar.gridRowMouseOut(this);' onclick='jQuery.imetabar.gridRowClick(this);' "+exRow+"></div>").append(jQuery.imetabar.createRow(item.cell,c.header)));
    		});
    		
    		gList.attr("rowNr",c.recordNum);
    		
    		// grid 底部功能栏
    		if(c.hasBottomBar){
    			var gBbar = $("<div id='"+id+"_gBbar' class='x-panel-bbar'></div>").appendTo(gRoot);
	    		var gBbarMain = $("<div class='x-toolbar'></div>").appendTo(gBbar);
	    		var gBbarTable = $("<table class='x-toolbar-ct' cellspacing='0'></table>").appendTo(gBbarMain);
	    		var gBbarThead = $("<tbody></tbody>").appendTo(gBbarTable);
	    		var gBbarTr = $("<tr></tr>").appendTo(gBbarThead);
	    		var gBbarLeftRow = $("<tr class='x-toolbar-left-row'></tr>");
	    		var gBbarRightRow = $("<tr></tr>");
	    		
	    		gBbarTr.append($("<td class='x-toolbar-left' align='left'></td>")
	    				.append($("<table cellspacing='0'></table>")
	    				.append($("<tbody></tbody>")
	    				.append(gBbarLeftRow))));
	    		gBbarTr.append($("<td class='x-toolbar-right' align='right'></td>")
	    				.append($("<table class='x-toolbar-right-ct' cellspacing='0'></table>")
	    				.append($("<tbody></tbody>")
	    				.append(gBbarRightRow))));
	    				
	    		if(c.hasPage){
	    			gBbarLeftRow.append($("<td class='x-toolbar-cell'>").append(jQuery.imetabar.createButton({
		    			id : id+'.btn.pageFirst',
		    			rootId : id,
		    			clazz : 'x-tbar-page-first',
		    			enabled : c.pageFirst,
		    			title : '第一页'
		    		})));
		    		gBbarLeftRow.append($("<td class='x-toolbar-cell'>").append(jQuery.imetabar.createButton({
		    			id : id+'.btn.pagePrev',
		    			rootId : id,
		    			clazz : 'x-tbar-page-prev',
		    			enabled : c.pagePrev,
		    			title : '上一页'
		    		})));
		    		gBbarLeftRow.append($("<td class='x-toolbar-cell'>").append(jQuery.imetabar.createSeparator()));
		    		gBbarLeftRow.append($("<td class='x-toolbar-cell'>").append(jQuery.imetabar.createText("页")));
		    		gBbarLeftRow.append($("<td class='x-toolbar-cell'>")
		    			.append($("<input id='"+id+".pageIndex' class='x-tbar-page-number' type='text' value='"+c.pageIndex+"' size='3'/>")));
		    		gBbarLeftRow.append($("<td class='x-toolbar-cell'>").append(jQuery.imetabar.createText("共 "+c.pageNum)));
		    		gBbarLeftRow.append($("<td class='x-toolbar-cell'>").append(jQuery.imetabar.createSeparator()));
		    		gBbarLeftRow.append($("<td class='x-toolbar-cell'>").append(jQuery.imetabar.createButton({
		    			id : id+'.btn.pageNext',
		    			rootId : id,
		    			clazz : 'x-tbar-page-next',
		    			enabled : c.pageNext,
		    			title : '下一页'
		    		})));
		    		gBbarLeftRow.append($("<td class='x-toolbar-cell'>").append(jQuery.imetabar.createButton({
		    			id : id+'.btn.pageLast',
		    			rootId : id,
		    			clazz : 'x-tbar-page-last',
		    			enabled : c.pageLast,
		    			title : '最后一页'
		    		})));
		    		gBbarLeftRow.append($("<td class='x-toolbar-cell'>").append(jQuery.imetabar.createSeparator()));
		    		gBbarLeftRow.append($("<td class='x-toolbar-cell'>").append(jQuery.imetabar.createButton({
		    			id : id+'.btn.loading',
		    			rootId : id,
		    			clazz : 'x-tbar-loading',
		    			enabled : true,
		    			title : '刷新'
		    		})));
	    		}
	    		
	    		if(c.hasAdd){
	    			gBbarLeftRow.append($("<td class='x-toolbar-cell'>").append(jQuery.imetabar.createButton({
		    			id : id+'.btn.add',
		    			rootId : id,
		    			clazz : 'x-tbar-add',
		    			fn : c.addFn,
		    			enabled : c.add,
		    			title : '添加'
		    		})));
	    		}
	    		
	    		if(c.hasDelete){
	    			gBbarLeftRow.append($("<td class='x-toolbar-cell'>").append(jQuery.imetabar.createButton({
		    			id : id+'.btn.delete',
		    			rootId : id,
		    			clazz : 'x-tbar-delete',
		    			fn : c.deleteFn,
		    			enabled :c.del,
		    			title : '删除'
		    		})));
	    		}
	    		var msg = "共 "+c.recordNum+" 条记录";
	    		gBbarRightRow.append($("<td class='x-toolbar-cell'>").append(jQuery.imetabar.createText(msg,null,id+"_recordNum")));
    		}
    		
    		gRoot.appendTo(p);
    		
    	}
    },
    
    defaultGridHeader : {
    	id : '',
    	text : '',
    	type : 'normal',
    	enabled : false,
    	width : 50
    },
    
    changeBarView : function(id,is){
    	if(is){
    		if($("#"+id).css("display") == "none"){
    			if($.browser.msie){
    				$("#"+id).css('display','block');
    			}else{
				  	$("#"+id).toggle("blind", {
						direction : "vertical"
						}, 500);
    			}
			}
    	}else{
    		if($("#"+id).css("display") != "none"){
    			if($.browser.msie){
    				$("#"+id).css('display','none');
    			}else{
				  	$("#"+id).toggle("blind", {
						direction : "vertical"
						}, 500);
    			}
			}
    	}
    	jQuery.imenu.selectedMenu(id+"Menu",is);
    },
    
    
    /**
     * 创建可拖拽的对象视图
     */
    createDraggableObjectView: function(url, parentDiv){
    	jQuery.imenu.pushPortalLoading("objectView", "创建对象视图...");
        $.getJSON(url, function(d){
            var ov = $.createObjFromJson(d, parentDiv, "jQuery.imetabar.appendContentForObjectView");

            // 工具箱最小化
            $("#iov-min").bind("click", function(e){
            	jQuery.imenu.selectedMenu("iovMenu",false);
            	jQuery.imenu.customState.iovCollapse=true;
				var l = $("#iov").width();
				$("#iov").animate({left:l*(-1)}, "slow");
				jQuery.imenu.changePortalSize();
            });
            
            // 工具箱刷新
            $("#iov-flush").bind("click", jQuery.imetabar.createDraggableObjectViewFlush);
            
            jQuery.imetabar.createDraggableObjectViewEvents();
            
            jQuery.imenu.popPortalLoading("objectView");
        });
    },
    
    /**
     * 创建可拖拽的对象视图
     */
    createDraggableObjectViewFlush: function(e){
    	$("#iov-by").html("重新加载中...");
    	$.ajax({
            type: "GET",
            url: "ImetaDBAction!loadRepositoryObjects.action",
            dataType: "json",
            success : function(json){
                $("#iov-by").empty();
                var trees = {
                   items: json
	            };
	            $.createObjFromJson(trees, $("#iov-by"), "jQuery.imetabar.appendContentForObjectView");
                jQuery.imetabar.createDraggableObjectViewEvents();
                
                var iovsl = $('#iov-sl').val();
                jQuery.imetabar.objectValueSelectByValue(iovsl,"fast");
            },
            error : globalError
        });
    },
    
    createDraggableObjectViewEvents : function(){
        $("[id=treeRoot.Transformation]").treeview({
            animated: "fast",
            collapsed: false
        });
        $("[id=treeRoot.Job]").treeview({
            animated: "fast",
            collapsed: false
        });
        
        $("span.file").click(function(e){
            jQuery.imetabar.loadObjectViewFileChildren(e.target);
        });
        
        $("div.hitarea").click(function(e){
            if ($(this).parent().is(":has(>span.file)")) {
                var file = $(this).parent().find("span.file");
                if (file[0].getAttribute("load") != 'y') {
                    jQuery.imetabar.loadObjectViewFileChildren(file[0]);
                };
        	}
        });
        
        $(".file-open").click(function(e){
            e.stopPropagation();
            var t = e.target;
            // 判断是否打开，如果打开提示先关闭
            var id = t.getAttribute("treeId");
            if(jQuery.iPortalTab.hasCreateTab(id)){
            	var tabId = jQuery.iPortalTab.I_PORTAL_TAB_ID_PRE+id;
            	jQuery.iPortalTab.activeTab(tabId,t.getAttribute("objectType"));
            	return;
            }
            jQuery.imenu.programLoading(true,"program");
            // 读取页面信息
            $.ajax({
                type: "POST",
                url: "ImetaAction!openFile.action",
                data: jQuery.cutil.objectToUrl({
                	roName : t.getAttribute("objectName"),
                	roType : t.getAttribute("objectType"),
                	directoryId : t.getAttribute("directoryId")
                }),
                dataType: "json",
                success : function(json){
	                var frameConfig = {
	                    id: t.getAttribute("treeId"),
	                    objectType: t.getAttribute("objectType"),
	                    objectName: t.getAttribute("objectName"),
	                    directoryId : t.getAttribute("directoryId"),
	                    directoryName : t.getAttribute("directoryName"),
	                    directoryPath : t.getAttribute("directoryPath"),
	                    title: t.getAttribute("objectName"),
	                    editable: json.editable,
	                    ori: json
	                };
	                jQuery.iPortalTab.createTab(frameConfig);
	               
	                jQuery.imenu.programLoading(false);
                },
                error : globalError
            });
        });
        
        
        $(".file-setting").click(function(e){
            e.stopPropagation();
            var t = e.target;
            // 判断是否打开，如果打开提示先关闭
            var id = t.getAttribute("treeId");
            if(jQuery.iPortalTab.hasCreateTab(id)){
            	var tabId = jQuery.iPortalTab.I_PORTAL_TAB_ID_PRE+id;
            	jQuery.iPortalTab.activeTab(tabId,t.getAttribute("objectType"));
            	$.imessagebox.showAlert("请先关闭编辑窗口，然后再进行设置！");
            	return;
            }
			if($.iformwindow.activeWindow(t.getAttribute("treeId"))){
				return;
			}
			
			var win = $.iformwindow('#ibody',{
                id: id,
                title: t.getAttribute("objectName"),
                showLoading : true
            });
			
            $.ajax({
                type: "POST",
                url: "ImetaAction!settingFile.action",
                data: jQuery.cutil.objectToUrl({
                	id: id,
                	roName : t.getAttribute("objectName"),
                	roType : t.getAttribute("objectType"),
                	directoryId : t.getAttribute("directoryId")
                }),
                dataType: "json",
                success : function(json){
                    if(json.success){
                        win.appendContent(json);
                    }else{
                        win.removeWin();
                        $.imessagebox.showAlert(json.message);
                    }
                },
                error : globalError
            });
            
        });
    },
    
    closeObjectViewFileChildren : function(id){
    	var ov = $("#"+id);
    	ov.removeClass("collapsable").addClass("expandable");
    	ov.find("div").removeClass("collapsable-hitarea").addClass("expandable-hitarea");
    	ov.find("span").attr("load","n");
    	ov.find("ul").css("display","none").empty();
    },
    
    loadObjectViewFileChildren: function(e){
        if (e.getAttribute("load") != 'y') {
            var oldClass = e.getAttribute("class");
            e.setAttribute("class", "file-load");
            
            var objectType = e.getAttribute("objectType");
            var treeId = e.getAttribute("treeId");
            $.ajax({
                type: "POST",
                url: "ImetaDBAction!loadRepositoryObjectByName.action",
                data: jQuery.cutil.objectToUrl({
	                	roName : e.getAttribute("objectName"),
	                	directoryId : e.getAttribute("directoryId"),
	                	roType : objectType,
	                	roId : treeId
	                }),
                // dataType: "json",
                success : function(sub){
                	var branches = $(sub).appendTo( $("[id=loadingNode."+treeId+"]") );
		            $("#" + objectType).treeview({
		                add: branches
		            });
		            e.setAttribute("load", 'y');
		            e.setAttribute("class", oldClass);
		            
		            $("." + treeId + "-setting").click(function(e){
		            	var t = e.target;
		            	
		                var elementType = t.getAttribute("elementType");
		                var elementId = t.getAttribute("elementId");
		                var dbName = t.getAttribute("dbName");
		                var roName = t.getAttribute("roName");
		                var roType = t.getAttribute("roType");
		                var stepName = t.getAttribute("stepName");
		                var stepType = t.getAttribute("stepType");
		                var directoryId = t.getAttribute("directoryId");
		                
		                if($.iformwindow.activeWindow(elementType+"_"+elementId)){
							return;
						}
						
						var win = $.iformwindow('#ibody',{
				                    id: elementType+"_"+elementId,
				                    title: (dbName)?dbName:(stepName)?stepName:"",
				                    showLoading : true
				                });
						if(elementType == "database"){
							$.ajax({
				                type: "POST",
				                url: "ImetaDBAction!settingDatabase.action",
				                data: jQuery.cutil.objectToUrl({
				                	elementId : elementId
				                }),
				                dataType: "json",
				                success : function(json){
				                	win.appendContent(json);
				                },
				                error : globalError
				          	});
						} else if(elementType == "step"){
							$.ajax({
								type: "POST",
				                url: "ImetaAction!editElement.action",
				                data: jQuery.cutil.objectToUrl({
				                	elementId : elementId,
				                	roName : roName,
				                	roType : roType,
				                	stepName : stepName,
				                	stepType : stepType,
				                	directoryId : directoryId
				                }),
				                dataType: "json",
				                success : function(json){
				                	win.appendContent(json);
				                },
				                error : globalError
				          	});
						}
		               
			          	
		            });
		            $("." + treeId + "-setting").mouseover(function(e){
		                e.target.style.cursor = "pointer";
		                e.target.style.color = "red";
		            });
		            $("." + treeId + "-setting").mouseout(function(e){
		                e.target.style.cursor = "auto";
		                e.target.style.color = "black";
		            });
                }
            });
            

        }
    },
    
    /**
     * 对象视图的扩展
     */
    appendContentForObjectView: function(item, theObj){
        if (item.loadUrl) {
            $.ajax({
                url: item.loadUrl,
                async: false,
                dataType : "json",
                success : function(json){
                	var trees = {
		                items: json
		            };
		            $.createObjFromJson(trees, theObj, "jQuery.imetabar.appendContentForObjectView");
                },
                error : globalError
            });
        }
        
    },
    
    /**
     * 通过url创建可拖拽的iMeta工具箱
     *
     * @param {}
     *            url json对象的路径
     * @param {}
     *            parentDiv 添加到的父对象名称
     */
    createDraggableToolbar: function(url, parentDiv){
    	jQuery.imenu.pushPortalLoading("iMetaToolbar", "创建iMeta工具箱...");
        $.getJSON(url, function(d){
            // 创建工具箱
            var toolbar = $.createObjFromJson(d, parentDiv, "jQuery.imetabar.appendContentForToolbar");
            $("#imb").accordion({
                event: "mouseover"
            });
            $("#imb").draggable({
                containment: parentDiv,
                scroll: false,
                handle: "#imb-tl"
            });
            // 工具箱最小化
            $("#imb-min").bind("click", function(e){
                var is = ($("#imb").css("display") != "none");
            	jQuery.imetabar.changeBarView("imb",!is);
            });
            
            $("#imb").addClass("toolbar-null");
            
            jQuery.imenu.popPortalLoading("iMetaToolbar");
        });
    },
    
    /**
     * 添加一个图形框体
     *
     * @param {}
     *            frame
     * @param {}
     *            theObj
     * @param {}
     *            canvasConfig
     */
    createCanvasFrame: function(frame, theObj, canvasConfig, canvasImgPath){
    
    	if ($.isObjNull(canvasImgPath)) {
    		canvasImgPath = jQuery.imetabar.canvasImgPath;
    	}
        if ($.isObjNull(canvasConfig)) {
            canvasConfig = {};
        }
        if ($.isObjNull(canvasConfig.width)) {
            canvasConfig.width = 20;
        }
        if ($.isObjNull(canvasConfig.height)) {
            canvasConfig.height = 20;
        }
        theObj.html("<em><img id='img_" + frame.id + "' src='" + canvasImgPath + frame.img + "'><br>" + frame.name + "</em>");
        
    },
    
    /**
     * 处理工具箱的内容
     *
     * @param {}
     *            item
     * @param {}
     *            theObj
     */
    appendContentForToolbar: function(item, theObj){
    	if (item.loadUrl) {
            $.ajax({
                url: item.loadUrl,
                async: false,
                dataType : "json",
                success : function(json){
		           	$.createObjFromJson({items: json}, theObj, "jQuery.imetabar.appendContentForToolbar");
                },
                error : globalError
            });
        }
        // 如果是框体
        if (item.frame) {
            // 复制frame到框体
            theObj.attr(item.frame);
            jQuery.imetabar.createCanvasFrame(item.frame, theObj, {
                "width": 54,
                "height": 54
            });
            
            theObj.bind('dragstart',function( event ){
                $("#drag_box").empty().append($(this).find("img").clone());
    			return $("#drag_box").css({
    				top: event.offsetY,
    				left: event.offsetX,
    				display: "block"
    			});
			}).bind('drag',function( event ){
    			$("#drag_box").css({
    				top: event.offsetY,
    				left: event.offsetX
    			});
    		}).bind('dragend',function( event ){
    		     $("#drag_box").css({
    				display: "none"
    			});
    			
    			var activeTab = jQuery.iPortalTab.getActiveTab();
    			
    			var eventX = event.offsetX;
    			var eventY = event.offsetY;
    			
    			if(!jQuery.imenu.customState.iovCollapse){
        		    eventX = eventX - 200;
        		}
        		if(!jQuery.imenu.customState.menuCollapse){
        		    eventY = eventY - 133;
        		}else{
        		    eventY = eventY - 28;
        		}
        		
        		if(eventX<0 || eventY<0){
        		    return;
        		}
                
                $.ajax({
                	 type: "POST",
                	 url: "ImetaAction!newElement.action",
					 data: jQuery.cutil.objectToUrl({
					 	roName : activeTab.objectName,
	                	roType : activeTab.objectType,
	                	directoryId : activeTab.directoryId,
					 	elType: item.frame.id,
					 	element : "step",
					 	x : eventX,
					 	y : eventY
					 }),
					 dataType: "json",
					 success: function(json){
		                jQuery.imenu.iContent.appendEl(json, null);
	                },
	                error : globalError
                });
    		});
            
            // 建立拖拽属性
            theObj.bind("dblclick", function(e){
                // 判断是否有选定的对象
				if(jQuery.iPortalTab.activeTabId==null){
					$.imessagebox.showAlert("请先选定一个编辑对象！");
					return;
				}
				if(!jQuery.imenu.iMenuBtns['editObject'].selected){
				    $.imessagebox.showAlert("该对象正处于不可编辑状态，请先修改编辑状态");
					return;
				}
                
                var activeTab = jQuery.iPortalTab.getActiveTab();
                
                $.ajax({
                	 type: "POST",
                	 url: "ImetaAction!newElement.action",
					 data: jQuery.cutil.objectToUrl({
					 	roName : activeTab.objectName,
	                	roType : activeTab.objectType,
	                	directoryId : activeTab.directoryId,
					 	elType: item.frame.id,
					 	element : "step"
					 }),
					 dataType: "json",
					 success: function(json){
		                jQuery.imenu.iContent.appendEl(json, null);
	                },
	                error : globalError
                });

            });
        }
    },
    
    /**
     * 关闭页面，登出系统
     */
    logout : function(){
        $.ajax({
        	 type: "GET",
        	 url: "ImetaAction!logout.action"
        })
    },
    
    /**
     * 对象视图的筛选
     */
    objectValueSelect : function(c){
    	var obj = $(c).val();
    	jQuery.imetabar.objectValueSelectByValue(obj,"normal");
    },
    
    objectValueSelectByValue : function(obj,m){
    	if(obj == ''){
    		$.each(jQuery.iPortalTab.OBJECT_TYPES,function(e,v){
	    		$("[id=treeRoot."+v+"]").show(m);
	    	});
    	}else{
	    	$.each(jQuery.iPortalTab.OBJECT_TYPES,function(e,v){
	    		$("[id=treeRoot."+v+"]").hide(m);
	    	});
	    	$("[id=treeRoot."+obj+"]").show(m);
    	}
    }
};
