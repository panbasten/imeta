(function($) {
    	/**
		 * Panel对象
		 */
    	function PanelObj(_target, _options){
    		this.changePosition = changePosition;
    		var opt = $.extend({},$.ipanel.defaults,_options);
    		
    		var proot = $("<div class='x-panel x-form-label-right'></div>");
    		// 头部
	    	proot.append($("<div class='x-panel-tl'></div>")
	    	.append($("<div class='x-panel-tr'></div>")
	    	.append($("<div class='x-panel-tc'></div>")
	    	.append($("<div class='x-panel-header x-unselectable'></div>")
	    	.append((opt.titleCls)?($("<span class='x-panel-header-text "+opt.titleCls+"' style='padding-left:22px;'>"+opt.title+"</span>"))
	    		:($("<span class='x-panel-header-text'>"+opt.title+"</span>")))))));
	    	
	    	// 主体
	    	var bodyObj = $("<div class='x-panel-mc'></div>");
	    	// 底部
	    	var bottomObj = $("<div class='x-panel-footer'></div>");
	    	
	    	proot.append($("<div class='x-panel-bwrap'></div>")
	    	.append($("<div class='x-panel-ml'></div>")
	    	.append($("<div class='x-panel-mr'></div>")
	    	.append(bodyObj)))
	    	.append($("<div class='x-panel-bl'></div>")
	    	.append($("<div class='x-panel-br'></div>")
	    	.append($("<div class='x-panel-bc'></div>")
	    	.append(bottomObj)))));
	    	
	    	if(opt.bodyObj!=null){
	    		bodyObj.append(opt.bodyObj);
	    	}
	    	
	    	if(opt.bottomObj!=null){
	    		bottomObj.append(opt.bottomObj);
	    	}
	    	
	    	proot.appendTo(_target);
	    	
	    	function changePosition(p){
	    		proot.css({
	    			top : p.top,
	    			left : p.left
	    		});
	    	};
    	};
    	
    	/**
    	 * 调用返回Panel对象
    	 */
    	$.ipanel = function(target, options) {
    		var panel = new PanelObj(target, options);
    		return panel;
    	};
    	
    	/**
    	 * 按钮对象
    	 */
    	function ButtonObj(_target, _options){
    		this.btnClick = btnClick;
    		this.btnUnbind = btnUnbind;
    		this.btnEnable = btnEnable;
    		var opt = $.extend({},$.ibutton.defaults,_options);
    		
    		var btnTable = $("<table class='x-btn x-btn-icon' cellspacing='0'></table>");
    		if(opt.btnWidth!=''){
    			btnTable.css("width",opt.btnWidth);
    		}
    		if(!opt.enabled){
    			btnTable.addClass("x-item-disabled");
    		}
    		var btnTbody = $("<tbody></tbody>").appendTo(btnTable);
    		if(opt.btnType=='small'){
    			btnTbody.addClass("x-btn-small x-btn-icon-small-left");
    		}
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
    		
    		var btn = $("<button class='x-btn-text' type='button'>"+opt.text+"</button>");
    		btn.attr("id", opt.id);
    		btn.addClass(opt.clazz);
    		btnMain.append($("<em unselectable='on'></em>").append(btn));
    		
    		btnTable.appendTo(_target);
    		
    		function btnClick(fn){
    			btnEnable(true);
    			btnMain.click(fn);
    		};
    		
    		function btnUnbind(type,fn){
    			btnEnable(false);
    			btn.unbind(type,fn);
    		};
    		
    		function btnEnable(b){
    			if(b){
    				btnTable.removeClass("x-item-disabled");
    			}else{
    				btnTable.addClass("x-item-disabled");
    			}
    		};
    	};
    	
    	$.ibutton = function(target, options) {
    		var button = new ButtonObj(target, options);
    		return button;
    	};
    	
    	/**
    	 * 窗口
    	 */
    	function WindowObj(_target, _options){
    		this.changePosition = changePosition;
    		this.getBodyObj = getBodyObj;
    		
    		var opt = $.extend({},$.iwindow.defaults,_options);
    		
    		var windowShadow;
    		// 阴影
    		if(opt.shadow){
    			windowShadow = $("<div class='x-shadow'></div>");
    			windowShadow.width(opt.shadowWidth);
    			windowShadow.height(opt.shadowHeight);
    			
    			var cWidth = opt.shadowWidth-12;
    			var cHeight = opt.shadowHeight-12;
    			
    			windowShadow.append($("<div class='xst'></div>")
    			.append($("<div class='xstl'></div>"))
    			.append($("<div class='xstc'></div>").width(cWidth))
    			.append($("<div class='xstr'></div>")));
    			
    			windowShadow.append($("<div class='xsc'></div>").height(cHeight)
    			.append($("<div class='xsml'></div>"))
    			.append($("<div class='xsmc'></div>").width(cWidth))
    			.append($("<div class='xsmr'></div>")));
    			
    			windowShadow.append($("<div class='xsb'></div>")
    			.append($("<div class='xsbl'></div>"))
    			.append($("<div class='xsbc'></div>").width(cWidth))
    			.append($("<div class='xsbr'></div>")));
    			
    			windowShadow.appendTo(_target);
    		}
    		
    		var windowDialog = $("<div class='x-window x-window-plain' style='position: absolute;'></div>");
    		if(opt.winWidth!=''){
    			windowDialog.width(opt.winWidth);
    		}
	    	// 头部
	    	windowDialog.append($("<div class='x-window-tl'></div>")
	    	.append($("<div class='x-window-tr'></div>")
	    	.append($("<div class='x-window-tc'></div>"))));
	    	
	    	var bwrap = $("<div class='x-window-bwrap'></div>").appendTo(windowDialog);
	    	
	    	// 主体
	    	var bodyObj = $("<div class='x-window-body'></div>");
	    	
	    	bwrap.append($("<div class='x-window-ml'></div>")
	    	.append($("<div class='x-window-mr'></div>")
	    	.append($("<div class='x-window-mc'></div>")
	    	.append(bodyObj))));
	    	
	    	// 尾部
	    	bwrap.append($("<div class='x-window-bl x-panel-nofooter'></div>")
	    	.append($("<div class='x-window-br'></div>")
	    	.append($("<div class='x-window-bc'></div>"))));
	    	
	    	if(opt.bodyObj!=null){
	    		bodyObj.append(opt.bodyObj);
	    	}
	    	
	    	windowDialog.appendTo(_target);
	    	
	    	function changePosition(p){
	    		windowDialog.css({
	    			top : p.top,
	    			left : p.left
	    		});
	    		if(opt.shadow){
	    			windowShadow.css({
		    			top : (p.top+10),
		    			left : (p.left+10)
		    		});
	    		}
	    		
	    	};
	    	
	    	function getBodyObj(){
	    		return bodyObj;
	    	};
    	
    	};
    	
    	$.iwindow = function(target, options) {
    		var iwindow = new WindowObj(target, options);
    		return iwindow;
    	};
    	
    	function FormWindow(_target, _options){
    		this.resize = resize;
    		this.active = active;
    		this.appendContent = appendContent;
    		this.removeWin = removeWin;
    		
    		
    		var opt = $.extend({},$.iformwindow.defaults,_options);
    		
    		var id = opt.id;
    		
    		if ($.isNull(id)) {
    			id = "";
    		}
    		
			var iformwindow = $('<div></div>').attr({
				'id' : 'window-' + id,
				'class' : 'window',
				'title' : opt.title
			}).appendTo(_target);
			iformwindow.bind("click", function(e) {
						active();
					});

			var windowTop = $('<div></div>').attr({
						'id' : 'windowTop-' + id,
						'class' : 'windowTop'
					}).appendTo(iformwindow);
			var windowTopContent = $('<div></div>').attr({
						'class' : 'windowTopContent'
					}).html(!(opt.title) ? 'NULL' : opt.title).appendTo(windowTop);

			var windowBottom = $('<div></div>').attr({
						'class' : 'windowBottom'
					}).appendTo(iformwindow);
			var windowBottomContent = $('<div></div>').attr({
						'id' : 'windowBottomContent-' + id,
						'class' : 'windowBottomContent'
					}).appendTo(windowBottom);
			var windowContent = $('<div></div>').attr({
						'id' : 'windowContent-' + id,
						'class' : 'windowContent'
					}).appendTo(iformwindow);

			// loading
			if(opt.showLoading){
				appendContent($.iformwindow.loading);
			}

			// 关闭按钮
			var windowClose = $('<div></div>').attr({
						'id' : 'windowClose-' + id,
						'class' : 'windowClose'
					}).appendTo(windowTop);
			windowClose.bind("click", function(e) {
						iformwindow.remove();
					});

			// 最小化按钮
			var windowMin = $('<div></div>').attr({
						'id' : 'windowMin-' + id,
						'class' : 'windowMin'
					}).appendTo(windowTop);
			windowMin.bind("click", function(e) {
						resize(opt.oraWidth,opt.oraHeight,opt.oraTop,opt.oraLeft);
						windowMin.hide();
						windowMax.show();
					});
			windowMin.hide();

			// 最大化按钮
			var windowMax = $('<div></div>').attr({
						'id' : 'windowMax-' + id,
						'class' : 'windowMax'
					}).appendTo(windowTop);
			windowMax.bind("click", function(e) {
						opt.oraWidth = windowContent.width()+25;
						opt.oraHeight = windowContent.height()+45;
						opt.oraTop = iformwindow.css("top").split("px")[0];
						opt.oraLeft = iformwindow.css("left").split("px")[0];
						var wsize = $.getWindowScroll();
						var maxW = wsize.width-10;
						var maxH = wsize.height-10;
						resize(maxW,maxH,5,5);
						windowMax.hide();
						windowMin.show();
					});
			
			// 设置位置
			var wsize = $.getWindowScroll();
			this.resize(opt.oraWidth,
				opt.oraHeight,
				Math.floor((wsize.height-opt.oraHeight-45)/2),
				Math.floor((wsize.width-opt.oraWidth-25)/2)
			);

			// 增加拖拽功能
			iformwindow.draggable({
				containment : _target,
				scroll : false,
				handle : "#windowTop-" + id
			});
			
			iformwindow.toggle("puff", {
	                percent: 100
	            }, 500);
    		
    		function resize(w,h,t,l){
				t=(t>0)?t:0;
				l=(l>0)?l:0;
				iformwindow.width(w + "px");
				iformwindow.height(h + "px");
				iformwindow.css("top",t + "px");
				iformwindow.css("left",l + "px");
				windowBottomContent.height( (h-30) + "px" );
				windowContent.width( (w - 25) + "px" );
				windowContent.height( (h - 45) + "px" );
			};
			
			function active(){
			    $(".window").css('z-index',200000);
			    if(opt.zindex){
    				iformwindow.css('z-index',opt.zindex);
			    }else{
    				iformwindow.css('z-index',200010);
			    }
			};
			
			function appendContent(c,cfn){
				windowContent.empty();
				$.createObjFromJson(c, windowContent, (cfn)?cfn:"$.iformwindow.extendContentFn");
				active();
			};
			
			function removeWin(){
			    iformwindow.remove();
			};
    	};
    	
    	$.iformwindow =  function(target, options) {
    		var iformwindow = new FormWindow(target, options);
    		return iformwindow;
    	};
    	
    	$.iformwindow.activeWindow = function(id){
		    $(".window").css('z-index',200000);
	        if($("#window-" + id).length>0){
    			$("#window-" + id).css('z-index',200010);
				return true;
			}
			return false;
		};
    	
    	function MessageBoxj(_target, _options){
    		this.resize = resize;
    		
    		this.MESSAGEBOX_CONFIRM="confirm";
    		this.MESSAGEBOX_PROMPT="prompt";
    		this.MESSAGEBOX_MULTIPROMPT="multi-prompt";
    		this.MESSAGEBOX_YNCDIALOG="ync";
    		this.MESSAGEBOX_ALERT="alert";
    		this.MESSAGEBOX_CUSTOM="custom";
    		
    		this.MESSAGEBOX_ICON_ERROR="error";
    		this.MESSAGEBOX_ICON_INFO="info";
    		this.MESSAGEBOX_ICON_QUESTION="question";
    		this.MESSAGEBOX_ICON_WARNING="warning";
    		
    		this.MESSAGEBOX_TITLE = {
    			"custom" : "自定义",
    			"confirm" : "确认",
    			"prompt" : "输入",
    			"multi-prompt" : "输入",
    			"ync" : "确认",
    			"alert" : "警告"
    		};
    		
    		var opt = $.extend({},$.imessagebox.defaults,_options);
    		var id = $.trimEmpty(opt.id);
    		
    		if(opt.marked){
    			jQuery.imenu.programLoading(true,null);
    		}
    		
    		if($.isNull(opt.title)){
    			opt.title = this.MESSAGEBOX_TITLE[opt.type];
    		}
    		
    		if($.isNull(opt.icon)){
    			opt.icon = this.MESSAGEBOX_ICON_INFO;
    		}
    		
    		var imessage = $('<div></div>').attr({
				'id' : 'message-' + id,
				'class' : 'messagebox',
				'title' : opt.title
			}).appendTo(_target);

			var imessageTop = $('<div></div>').attr({
						'id' : 'messageTop-' + id,
						'class' : 'windowTop'
					}).appendTo(imessage);
			var imessageTopContent = $('<div></div>').attr({
						'class' : 'windowTopContent'
					}).html(!(opt.title) ? 'NULL' : opt.title).appendTo(imessageTop);

			var imessageBottom = $('<div></div>').attr({
						'class' : 'windowBottom'
					}).appendTo(imessage);
			var imessageBottomContent = $('<div></div>').attr({
						'id' : 'messageBottomContent-' + id,
						'class' : 'windowBottomContent'
					}).appendTo(imessageBottom);
					
			var imessageContent = $('<div></div>').attr({
						'id' : 'messageContent-' + id,
						'class' : 'messageboxContent'
					}).appendTo(imessage);
					
			// 生成内容
			var imgClass = "messagebox-icon-"+opt.icon;
			var msText = $.replaceEnterToBr(opt.message);
			var imessageIcon =  $('<div></div>').attr({
						'id' : 'messageIcon-' + id,
						'class' : 'messagebox-icon '+imgClass
					}).appendTo(imessageContent);
					
			var imessageText,imessageInput;
			if(opt.type == this.MESSAGEBOX_PROMPT){
				imessageText = $('<div></div>').attr({
						'id' : 'messageText-' + id,
						'class' : 'messagebox-text',
						'style' : 'background-color:transparent;border:none;'
					}).appendTo(imessageContent);
				
				
				$("<div class='messagebox-prompttext'></div>").html(msText).appendTo(imessageText);
					
				imessageInput = $('<input></input>').attr({
						'id' : 'messageInput-' + id
					}).val(opt.defaultValue).appendTo(imessageText);
			}else if(opt.type == this.MESSAGEBOX_MULTIPROMPT){
				imessageText = $('<div></div>').attr({
						'id' : 'messageText-' + id,
						'class' : 'messagebox-text',
						'style' : 'background-color:transparent;border:none;'
					}).appendTo(imessageContent);
				
				
				$("<div class='messagebox-prompttext'></div>").html(msText).appendTo(imessageText);
					
				imessageInput = $('<textarea></textarea>').attr({
						'id' : 'messageInput-' + id
					}).html(opt.defaultValue).appendTo(imessageText);
			}else if(opt.type == this.MESSAGEBOX_CUSTOM){
			    imessageText = $('<div></div>').attr({
						'id' : 'messageText-' + id,
						'class' : 'messagebox-text',
						'style' : 'background-color:transparent;border:none;'
					}).appendTo(imessageContent);
				
				
				$("<div class='messagebox-prompttext'></div>").html(msText).appendTo(imessageText);
				
				if(opt.content){
				    try{
    				    imessageInput = opt.content;
    				    imessageInput.attr({
    				        'id' : 'messageInput-' + id
    				    });
    				    imessageInput.appendTo(imessageText);
				    }catch(ex){
				        console.log(ex);
				    }
				}
			}else{
				imessageText = $('<div></div>').attr({
						'id' : 'messageText-' + id,
						'class' : 'messagebox-text'
					}).html(msText).appendTo(imessageContent);
			}
			
			var imessageBtn = $("<div style='margin: 5px auto auto;'></div>").appendTo(imessageContent);;
			var imessageBtnTable = $("<table class='messagebox-btn'></table>").appendTo(imessageBtn);
			var imessageBtnTbody = $("<tbody></tbody>").appendTo(imessageBtnTable);
			var imessageBtnTr = $("<tr></tr>").appendTo(imessageBtnTbody);
			
			var imessageYesBtn,imessageNoBtn,imessageOkBtn,imessageCancelBtn;
			if(opt.type == this.MESSAGEBOX_CONFIRM){
			    if(opt.btns){
    			    $.each(opt.btns,function(e,v){
    					var btn = $.ibutton($("<td></td>").appendTo(imessageBtnTr),{
    						text: v.text,
    						btnWidth : (v.btnWidth)?v.btnWidth:50
    					});
    					btn.btnClick(function(e){
    						btnClick((v.key)?v.key:e,(v.removeMessage!=undefined)?v.removeMessage:true,imessageInput);
    					});
    				});
			    }
				
				imessageYesBtn = $.ibutton($("<td></td>").appendTo(imessageBtnTr),{
					text: "是",
					btnWidth : 50
				});
				imessageYesBtn.btnClick(function(e){
					btnClick("yes",true);
				});
				
				imessageNoBtn = $.ibutton($("<td></td>").appendTo(imessageBtnTr),{
					text: '否',
					btnWidth : 50
				});
				imessageNoBtn.btnClick(function(e){
					btnClick("no",true);
				});
			}
			else if(opt.type == this.MESSAGEBOX_PROMPT || opt.type == this.MESSAGEBOX_MULTIPROMPT){
			    if(opt.btns){
    			    $.each(opt.btns,function(e,v){
    					var btn = $.ibutton($("<td></td>").appendTo(imessageBtnTr),{
    						text: v.text,
    						btnWidth : (v.btnWidth)?v.btnWidth:50
    					});
    					btn.btnClick(function(e){
    						btnClick((v.key)?v.key:e,(v.removeMessage!=undefined)?v.removeMessage:true,imessageInput);
    					});
    				});
			    }
				
				imessageOkBtn = $.ibutton($("<td></td>").appendTo(imessageBtnTr),{
					text: '确定',
					btnWidth : 50
				});
				imessageOkBtn.btnClick(function(e){
					btnClick("ok",true,imessageInput.val());
				});
				
				imessageCancelBtn = $.ibutton($("<td></td>").appendTo(imessageBtnTr),{
					text: '取消',
					btnWidth : 50
				});
				imessageCancelBtn.btnClick(function(e){
					btnClick("cancel",true,imessageInput.val());
				});
			}
			else if(opt.type == this.MESSAGEBOX_YNCDIALOG){
			    if(opt.btns){
    			    $.each(opt.btns,function(e,v){
    					var btn = $.ibutton($("<td></td>").appendTo(imessageBtnTr),{
    						text: v.text,
    						btnWidth : (v.btnWidth)?v.btnWidth:50
    					});
    					btn.btnClick(function(e){
    						btnClick((v.key)?v.key:e,(v.removeMessage!=undefined)?v.removeMessage:true,imessageInput);
    					});
    				});
			    }
				
				imessageYesBtn = $.ibutton($("<td></td>").appendTo(imessageBtnTr),{
					text: '是',
					btnWidth : 50
				});
				imessageYesBtn.btnClick(function(e){
					btnClick("yes",true);
				});
				
				imessageNoBtn = $.ibutton($("<td></td>").appendTo(imessageBtnTr),{
					text: '否',
					btnWidth : 50
				});
				imessageNoBtn.btnClick(function(e){
					btnClick("no",true);
				});
				
				imessageCancelBtn = $.ibutton($("<td></td>").appendTo(imessageBtnTr),{
					text: '取消',
					btnWidth : 50
				});
				imessageCancelBtn.btnClick(function(e){
					btnClick("cancel",true);
				});
			}
			else if(opt.type == this.MESSAGEBOX_ALERT){
			    if(opt.btns){
    			    $.each(opt.btns,function(e,v){
    					var btn = $.ibutton($("<td></td>").appendTo(imessageBtnTr),{
    						text: v.text,
    						btnWidth : (v.btnWidth)?v.btnWidth:50
    					});
    					btn.btnClick(function(e){
    						btnClick((v.key)?v.key:e,(v.removeMessage!=undefined)?v.removeMessage:true,imessageInput);
    					});
    				});
			    }
				
				imessageOkBtn = $.ibutton($("<td></td>").appendTo(imessageBtnTr),{
					text: '确定',
					btnWidth : 50
				});
				
				imessageOkBtn.btnClick(function(e){
					btnClick("ok",true);
				});
			}
			else if(opt.type == this.MESSAGEBOX_CUSTOM){
			    if(opt.btns){
    				$.each(opt.btns,function(e,v){
    					var btn = $.ibutton($("<td></td>").appendTo(imessageBtnTr),{
    						text: v.text,
    						btnWidth : (v.btnWidth)?v.btnWidth:50
    					});
    					btn.btnClick(function(e){
    						btnClick((v.key)?v.key:e,(v.removeMessage!=undefined)?v.removeMessage:true,imessageInput);
    					});
    				});
			    }
				
				imessageCancelBtn = $.ibutton($("<td></td>").appendTo(imessageBtnTr),{
					text: '取消',
					btnWidth : 50
				});
				imessageCancelBtn.btnClick(function(e){
					btnClick("cancel",true);
				});
			}
					
			// 设置位置
			var wsize = $.getWindowScroll();
			this.resize(opt.oraWidth,
				opt.oraHeight,
				Math.floor((wsize.height-opt.oraHeight-45)/2),
				Math.floor((wsize.width-opt.oraWidth-25)/2)
			);

			// 增加拖拽功能
			imessage.draggable({
				containment : _target,
				scroll : false,
				handle : "#messageTop-" + id
			});
			
			imessage.toggle("puff", {
	                percent: 100
	            }, 500);
	            
	        imessage.css('z-index',400000);
	        
	        function btnClick(rtn,isRemove,val){
	            if(isRemove){
	                imessage.remove();
	        	    jQuery.imenu.programLoading(false);
	            }
	        	
	        	if(!$.isObjNull(opt.fn)){
	        		if(typeof(opt.fn)=='string'){
	        			eval(opt.fn+"(rtn,val);");
	        		}else{
	        			opt.fn(rtn,val);
	        		}
	        	}
	        };
	        
	       	function resize(w,h,t,l){
				t=(t>0)?t:0;
				l=(l>0)?l:0;
				imessage.width(w + "px");
				imessage.height(h + "px");
				imessage.css("top",t + "px");
				imessage.css("left",l + "px");
				imessageBottomContent.height( (h-30) + "px" );
				imessageContent.width( (w - 25) + "px" );
				imessageContent.height( (h - 45) + "px" );
				imessageText.height( (h - 82) + "px" );
			};
    	};
    	
    	$.imessagebox = function(target, options) {
    		var imessagebox = new MessageBoxj(target, options);
    		return imessagebox;
    	};
    	
})(jQuery);

$.iformwindow.extendContentFn = function(item, theObj){
	if(item.grid){
		jQuery.imetabar.createGrid(item.grid,theObj);
	}
};

$.imessagebox.showAlert = function(ms){
	$.imessagebox("#ibody",{
		title : "提示",
		type : "alert",
		marded : true,
		message : ms,
		oraWidth : 300,
		oraHeight : 150
	});
};

$.imessagebox.defaults = {
	marked : true,
	type : 'alert',
	icon : "",
	title : "",
	defaultValue : "",
	oraTop : "",
	oraLeft : "",
	oraWidth : "400",
	oraHeight : "200"
};

$.iformwindow.defaults = {
	showLoading : true,
	oraTop : "",
	oraLeft : "",
	oraWidth : "650",
	oraHeight : "500"
};

$.iformwindow.loading = {
	items : [{
		createTag : "<div></div>",
		attr : {
			clazz : "windowLoading"
		},
		items : [{
			createTag : "<div></div>",
			attr : {
				clazz : "windowLoading-indicator"
			},
			items : [{
				createTag : "<img src='"+$path+"/imeta/images/loading.gif'/>"
			},{
				createTag : "<span></span>",
				attr : {
					clazz : "windowLoading-msg"
				},
				html : "初始化..."
			}]
		}]
	}]
	
	
};

$.iwindow.defaults = {
	bodyObj : null,
	winWidth : '',
	shadow : false,
	shadowWidth : 300,
	shadowHeight : 300
};

$.ipanel.defaults = {
	title : '',
	titleCls : '',
	bodyObj : null,
	bottomObj : null
};

$.ibutton.defaults = {
	id : '',
	clazz : '',
	text : '',
	btnWidth : '',
	btnType : 'normal',
	enabled : true
};
