if (!window.ImetaPortal)
	window.ImetaPortal = {};

ImetaPortal.BrowserDetect = {
	init: function () {
		this.browser = this.searchString(this.dataBrowser) || "unknown";
		this.version = $.browser.version || "unknown";
		this.OS = this.searchString(this.dataOS) || "unknown";
		this.browser_ver = this.browser;
		var vers = this.version.split(".");
		for(var i=0;i<vers.length && i<2;i++){
			if(i==0){
				this.browser_ver = this.browser_ver + vers[i];
			}else{
				this.browser_ver = this.browser_ver + "_" + vers[i];
			}
		}
	},
	searchString: function (data) {
		for (var i=0;i<data.length;i++)	{
			var dataString = data[i].string;
			var dataProp = data[i].prop;
			if (dataString) {
				if (dataString.indexOf(data[i].subString) != -1)
					return data[i].identity;
			}
			else if (dataProp)
				return data[i].identity;
		}
	},
	dataBrowser: [
		{
			string: navigator.userAgent,
			subString: "Chrome",
			identity: "chrome"
		},
		{ 	string: navigator.userAgent,
			subString: "OmniWeb",
			versionSearch: "OmniWeb/",
			identity: "omniWeb"
		},
		{
			string: navigator.vendor,
			subString: "Apple",
			identity: "safari",
			versionSearch: "Version"
		},
		{
			prop: window.opera,
			identity: "opera"
		},
		{
			string: navigator.vendor,
			subString: "iCab",
			identity: "iCab"
		},
		{
			string: navigator.vendor,
			subString: "KDE",
			identity: "konqueror"
		},
		{
			string: navigator.userAgent,
			subString: "Firefox",
			identity: "firefox"
		},
		{
			string: navigator.vendor,
			subString: "Camino",
			identity: "camino"
		},
		{		// for newer Netscapes (6+)
			string: navigator.userAgent,
			subString: "Netscape",
			identity: "netscape"
		},
		{
			string: navigator.userAgent,
			subString: "MSIE",
			identity: "ie",
			versionSearch: "MSIE"
		},
		{
			string: navigator.userAgent,
			subString: "Gecko",
			identity: "mozilla",
			versionSearch: "rv"
		},
		{ 		// for older Netscapes (4-)
			string: navigator.userAgent,
			subString: "Mozilla",
			identity: "netscape",
			versionSearch: "Mozilla"
		}
	],
	dataOS : [
		{
			string: navigator.platform,
			subString: "Win",
			identity: "win"
		},
		{
			string: navigator.platform,
			subString: "Mac",
			identity: "mac"
		},
		{
			string: navigator.userAgent,
			subString: "iPhone",
			identity: "iPhone"
	    },
		{
			string: navigator.platform,
			subString: "Linux",
			identity: "linux"
		}
	]
};

ImetaPortal.utils = {
	
	/**
	   * @see  将json字符串转换为对象
	   * @param   json字符串
	   * @return 返回object,array,string等对象
	   */
	evalJSON:function (strJson){
		
		return eval("("+strJson+")");			
		
	},
	
	/**
	   * @see  将javascript数据类型转换为json字符串
	   * @param 待转换对象,支持object,array,string,function,number,boolean,regexp
	   * @return 返回json字符串
	   */
	toJSON:function (object){
		
		var type=typeof object;			
		if('object'==type){
			
			if(Array==object.constructor )
			type='array';				
			else if(RegExp==object.constructor )
			type='regexp';				
			else 
			type='object';				
			
		}
		switch(type){
			
			case 'undefined':
			case 'unknown':
			return ;				
			break;				
			case 'function':
			case 'boolean':
			case 'regexp':
			return object.toString ();				
			break;				
			case 'number':
			return isFinite(object)?object.toString ():'null';				
			break;				
			case 'string':
			return '"'+object.replace(/(\\|\")/g,"\\$1").replace(/\n|\r|\t/g,
			function (){
				
				var a=arguments[0];
				
				return (a=='\n')?'\\n':
				(a=='\r')?'\\r':
				(a=='\t')?'\\t':""
				
			})+'"';				
			break;				
			case 'object':
			if(object===null)return 'null';				
			var results=[];				
			for(var property in object){
				
				var value=jQuery.toJSON(object[property]);					
				if(value!==undefined)
				results.push(jQuery.toJSON(property)+':'+value);					
				
			}
			return '{'+results.join(',')+'}';				
			break;				
			case 'array':
			var results=[];				
			for(var i=0;i<object.length;i++){
				
				var value=jQuery.toJSON(object[i]);					
				if(value!==undefined)results.push(value);					
				
			}
			return '['+results.join(',')+']';				
			break;				
			
		}
		
	},
	/**
	 * 得到鼠标的位置
	 */
	getMousePosition: function(e,target){
		var win = $(window),
	    left = e.pageX,
	    top = e.pageY;
	    width = $(target).outerWidth(),
	    height = $(target).outerHeight();

	    //collision detection for window boundaries
	    if((left + width) > (win.width())+ win.scrollLeft()) {
	        left = left - width;
	    }
	    if((top + height ) > (win.height() + win.scrollTop())) {
	        top = top - height;
	    }
	    return {"left":left,"top":top};
	},
	/**
	 * @see 得到窗体的
	 */
	getWindowScroll:function (){
		
		var T,L,W,H,win=window,dom=document.documentElement,doc=document.body;			
		T=dom&&dom.scrollTop||doc&&doc.scrollTop||0;			
		L=dom&&dom.scrollLeft||doc&&doc.scrollLeft||0;			
		if(win.innerWidth){
			
			W=win.innerWidth;				
			H=win.innerHeight;				
			
		}else {
			
			W=dom&&dom.clientWidth||doc&&doc.clientWidth;				
			H=dom&&dom.clientHeight||doc&&doc.clientHeight;				
			
		}
		return {
			top:T,left:L,width:W,height:H
		};			
		
	},
	getPageSize:function (){
		
		var windowWidth,windowHeight,
		xScroll,yScroll,
		win=window,dom=document.documentElement,doc=document.body;			
		if(win.innerHeight&&win.scrollMaxY){
			
			xScroll=doc.scrollWidth;				
			yScroll=win.innerHeight+win.scrollMaxY;				
			
		}else {
			
			xScroll=Math.max(dom?dom.scrollWidth:0,doc.scrollWidth,doc.offsetWidth);				
			yScroll=Math.max(dom?dom.scrollHeight:0,doc.scrollHeight,doc.offsetHeight);				
			
		}
		if(win.innerHeight){
			
			windowWidth=win.innerWidth;				
			windowHeight=win.innerHeight;				
			
		}else {
			
			windowWidth=dom&&dom.clientWidth||doc&&doc.clientWidth;				
			windowHeight=dom&&dom.clientHeight||doc&&doc.clientHeight;				
			
		}
		yScroll<windowHeight&&(yScroll=windowHeight);			
		xScroll<windowWidth&&(xScroll=windowWidth);			
		return {
			pageWidth:xScroll,pageHeight:yScroll,windowWidth:windowWidth,windowHeight:windowHeight
		};			
		
	},
	/**
	 * 替换/r/n为<br>
	 */
	replaceEnterToBr : function(str){
		return str.replace(new RegExp("\r\n","gm"),"<br>");
	},
	
	/**
	 * 字符串是否为空
	 * 
	 * @param {}
	 *            str
	 */
	isNull:function (str){
		
		if(typeof(str)=='undefined'||typeof(str)!='string'||str==null||str==''){
			
			return true;
			
		}
		return false;		
		
	},
	
	trimEmpty : function (str){
		if(typeof(str)=='undefined'||typeof(str)!='string'||str==null){
			return "";
		}
		return str;
	},
	
	/**
	 * 对象是否为空
	 * 
	 * @param {}
	 *            str
	 */
	isObjNull:function (str){
		
		if(typeof(str)=='undefined'||str==null){
			
			return true;			
			
		}
		return false;		
		
	},
	
	createObjFromJson:function (d,parentObj,fn){
		var inits = [];
		$.createObjFromJsonMain(d, parentObj, fn, inits);
		$.each(inits,function(e,v){
			try{
				eval(v);
				//console.log(v);
			}catch(e){
				console.debug("error:"+v);
			};
		});
	},
	
	/**
	 * 通过json对象创建对象
	 * 
	 * @param {}
	 *            d 当前json对象
	 * @param {}
	 *            parentDiv  当前创建对象的父对象
	 * @param {}
	 *            fn 扩展方法调用
	 * @param {}
	 * 			  inits 初始化方法
	 */
	createObjFromJsonMain:function (d,parentObj,fn,inits){
		$.each(d.items,function (i,item){
			if(!$.isNull(item.createTag)&&parentObj!=null){
				
				theObj=$(item.createTag);				
				if(item.attr!=null){
					if(item.attr.clazz!=null){
						
						if(typeof item.attr.clazz =='object'){
							$.extend(item.attr,{'class':$.toJSON(item.attr.clazz)});
						}else{
							$.extend(item.attr,{'class':item.attr.clazz});
						}
						item.attr.clazz=undefined;
					}
					if(item.attr.value!=null){
						$.extend(item.attr,{val:item.attr.value});
						item.attr.value=undefined;
					}
					theObj.attr(item.attr);					
					
				}
				if(!$.isNull(item.html)){
					theObj.html(item.html);
				}
				if(inits){
					if(!$.isNull(item.init)){
						inits.push(item.init);
					}
					if(item.listeners){
						$.each(item.listeners,function(e,v){
							$.each(v,function(se,sv){
								inits.push("$('[id="+item.attr.id+"]')."+e+"("+sv+");");
							});
						});
					}
				}
				theObj.appendTo(parentObj);				
				if(!$.isNull(fn)){
					
					eval(fn+"(item, theObj);");					
					
				}
				
				// 生成子对象
				if(item.items!=null){
					
					$.createObjFromJsonMain(item,theObj,fn,inits);					
					
				}
				
			} else {
				if(!$.isNull(fn)){
					
					eval(fn+"(item, parentObj);");					
					
				}
			}
		});		
	}
};

ImetaPortal.noOpera = function(){
	return;
};

ImetaPortal.navbarSwitch = function(i){
	var cur = $(".desktop_current");
	var ncur = $(cur.parent().children(".desktopContainer")[i]);
	var curIdx = cur.parent().children(".desktopContainer").index(cur);
	if(i != curIdx){
		$("#indicatorContainer").attr("class","indicator_container nav_current_"+(i+1));
	}
	if (!$.browser.msie){
		if(i < curIdx){
			cur.attr("class","desktopContainer desktop_current desktop_disappear_prepare2 desktop_disappear_animation2");
			setTimeout(function(){
				cur.removeClass("desktop_current");
				ncur.attr("class","desktopContainer desktop_current desktop_show_prepare2 desktop_show_animation2");
			},400);
		}else if(i > curIdx){
			cur.attr("class","desktopContainer desktop_current desktop_disappear_prepare1 desktop_disappear_animation1");
			setTimeout(function(){
				cur.removeClass("desktop_current");
				ncur.attr("class","desktopContainer desktop_current desktop_show_prepare1 desktop_show_animation1");
			},400);
		}
	}else{
		if(i != curIdx){
			cur.fadeOut(function(){
				cur.removeClass("desktop_current");
				ncur.fadeIn(function(){
					ncur.addClass("desktop_current");
				});
			});
		}
	}
}

/**
 * 改变桌面的尺寸
 */
ImetaPortal.changeDesktopSize = function (){
	var oraWin = ImetaPortal.utils.getWindowScroll();
	var oraHeight = oraWin.height - 60;
	$("#zoomWallpaperGrid").width(oraWin.width).height(oraWin.height);
	$("#zoomWallpaper").width(oraWin.width).height(oraWin.height);
	$("#desktopsContainer").width(oraWin.width).height(oraHeight);
	$(".desktopContainer").width(oraWin.width).height(oraHeight);
	oraHeight=oraHeight-46;
	$(".appListContainer").width(oraWin.width-28).height(oraHeight);
	
	// navbar
	var navbar = $("#navbar");
	navbar.css("left",(oraWin.width-navbar.width())/2+"px");
	
	// app
	var addBtn;
	var columnNum = Math.floor((oraHeight-12)/112);
	if(columnNum<1){
		columnNum=1;
	}
	
	$(".appListContainer").each(function(i){
		$(this).children(".appButton").each(function(j){
			if(!$(this).hasClass("addQuickLinkButton")){
				var c = Math.floor((j-1)/columnNum);
				var r = (j-1)%columnNum;
				$(this).css({
					top : (12+112*r)+"px",
					left : (27+142*c)+"px"
				});
			}
		});
	});
	
	$(".addQuickLinkButton").each(function(i){
		var len = $(this).parent().children(".appButton").length;
		var c = Math.floor((len-1)/columnNum);
		var r = (len-1)%columnNum;
		$(this).css({
			top : (12+112*r)+"px",
			left : (27+142*c)+"px"
		});
	});
	
};

/**
 * 设置浏览器环境变量
 */
ImetaPortal.initEnvironment = function(){
	ImetaPortal.BrowserDetect.init();
	$("html").attr("class", "javascriptEnabled " + ImetaPortal.BrowserDetect.OS + " " + ImetaPortal.BrowserDetect.browser + " " + ImetaPortal.BrowserDetect.browser_ver);
};

/**
 * 打开应用
 */
ImetaPortal.openApp = function(uid,attr,index){
	if(!index){
		index = cur.parent().children(".desktopContainer").index($(".desktop_current"));
	}
	
	if(window["dia_"+uid]){
		// 跳转到该desktop
		ImetaPortal.navbarSwitch(index);
		if(window["dia_"+uid].visible){
			window["dia_"+uid].toggleMinimize();
		}else{
			window["dia_"+uid].show();
			if(window["dia_"+uid].minimized){
				window["dia_"+uid].toggleMinimize();
			}
		}
	}else{
		var dia = $("<div id='"+uid+"' class='ui-dialog ui-widget ui-widget-content ui-corner-all ui-shadow'></div>");
		
		// title
		var diaTitle = $("<div class='ui-dialog-titlebar ui-widget-header ui-helper-clearfix ui-corner-top'></div>");
		diaTitle.append($("<span class='ui-dialog-title'>"+"tttttt"+"</span>"));
		if(attr.closable)diaTitle.append($("<a href='#' class='ui-dialog-titlebar-icon ui-dialog-titlebar-close ui-corner-all'><span class='ui-icon ui-icon-closethick'></span></a>"));
		if(attr.maximizable)diaTitle.append($("<a href='#' class='ui-dialog-titlebar-icon ui-dialog-titlebar-minimize ui-corner-all'><span class='ui-icon ui-icon-minus'></span></a>"));
		if(attr.minimizable)diaTitle.append($("<a href='#' class='ui-dialog-titlebar-icon ui-dialog-titlebar-maximize ui-corner-all'><span class='ui-icon ui-icon-extlink'></span></a>"));
		dia.append(diaTitle);
		
		// centent
		dia.append($("<div class='ui-dialog-content ui-widget-content'></div>"));
		
		dia.insertAfter($(".appListContainer").get(index));
		
		attr.id=uid;
		attr.autoOpen=true;
		if(attr.maximizable){
			attr.onMaximize=function(){ImetaPortal.navbarSwitch(index);};
		}
		if(attr.minimizable){
			attr.onMinimize=function(){ImetaPortal.navbarSwitch(index);};
		}
		window["dia_"+uid] = new PrimeFaces.widget.Dialog(attr);
	}
};

ImetaPortal.bindEvent = function(){
	$(".appListContainer").each(function(i){
		$(this).children(".appButton").each(function(j){
			if(!$(this).hasClass("addQuickLinkButton")){
				$(this).bind("contextmenu",function(e){
					$("#contextMenuForm").children('.context_menu:visible').hide();
					var cm = $(PrimeFaces.escapeClientId("contextMenuForm:context_menu_1"));
					var mp = ImetaPortal.utils.getMousePosition(e,cm);
					$(cm.find(".context_menu_desktop").find("a").removeClass("ui-state-disabled").get(i+1)).addClass("ui-state-disabled");
					cm.attr("uid",$(this).attr("uid")).css({
						"left": mp.left,
				        "top": mp.top,
				        "z-index": ++PrimeFaces.zindex
					}).show("fast");
					e.stopPropagation();
					return false;
				}).bind("click",function(e){
					var uid = $(this).attr("uid");
					var attr = ImetaPortal.utils.evalJSON($(this).attr("attr"));
					ImetaPortal.openApp(uid,attr,i);
				});
			}
		});
	});
	
	$(".addQuickLinkButton").each(function(i){
		$(this).bind("click",function(e){
			$("#contextMenuForm").children('.context_menu:visible').hide();
			var cm = $(PrimeFaces.escapeClientId("contextMenuForm:context_menu_0"));
			var mp = ImetaPortal.utils.getMousePosition(e,cm);
			cm.attr("screen",$(this).attr("screen")).css({
				"left": mp.left,
		        "top": mp.top,
		        "z-index": ++PrimeFaces.zindex
			}).show("fast");
			e.stopPropagation();
		});
	});
	
	$(document.body).bind('click', function (e) {
		$("#contextMenuForm").children('.context_menu:visible').hide();
    });
	
	$(window).bind('resize', function() {
		ImetaPortal.changeDesktopSize();
	});
};
