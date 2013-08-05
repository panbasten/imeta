jQuery.cutil={
	
	/**
		 * 用来遍历指定对象所有的属性名称和值 obj 需要遍历的对象 <br>
		 * jQuery.cutil.$getAllPrpos(obj);
		 * 
		 * @param {}
		 *            obj
		 * @return {Boolean}
		 */
	$getAllPrpos:function (obj){
		
		// 用来保存所有的属性名称和值
		if(!obj){
			
			alert('null');			
			return false;			
			
		}
		var props="";		
		// 开始遍历
		for(var p in obj){
			
			// p 为属性名称，obj[p]为对应属性的值
			props+=p+"="+obj[p]+"\t";			
			
		}
		// 最后显示所有的属性
		alert(props);		
		
	},
	
	/**
		 * jQuery.cutil.$getAllPrposName(obj);
		 * 
		 * @param {}
		 *            obj
		 * @return {Boolean}
		 */
	$getAllPrposName:function (obj){
		
		// 用来保存所有的属性名称和值
		if(!obj){
			
			alert('null');			
			return false;			
			
		}
		var props="";		
		// 开始遍历
		for(var p in obj){
			
			// p 为属性名称，obj[p]为对应属性的值
			props+=p+"\t";			
			
		}
		// 最后显示所有的属性
		alert(props);		
		
	},
	
	getPosition:function (e){
		
		var x=0;		
		var y=0;		
		var es=e.style;		
		var restoreStyles=false;		
		if(jQuery(e).css('display')=='none'){
			
			var oldVisibility=es.visibility;			
			var oldPosition=es.position;			
			restoreStyles=true;			
			es.visibility='hidden';			
			es.display='block';			
			es.position='absolute';			
			
		}
		var el=e;		
		while(el){
			
			x+=el.offsetLeft
			+(el.currentStyle&&!jQuery.browser.opera
			?parseInt(el.currentStyle.borderLeftWidth)||0
			:0);			
			y+=el.offsetTop
			+(el.currentStyle&&!jQuery.browser.opera
			?parseInt(el.currentStyle.borderTopWidth)||0
			:0);			
			el=el.offsetParent;			
			
		}
		el=e;		
		while(el&&el.tagName&&el.tagName.toLowerCase()!='body'){
			
			x-=el.scrollLeft||0;			
			y-=el.scrollTop||0;			
			el=el.parentNode;			
			
		}
		if(restoreStyles==true){
			
			es.display='none';			
			es.position=oldPosition;			
			es.visibility=oldVisibility;			
			
		}
		return {
			
			x:x,
			y:y
			
		};		
		
	},
	getPositionLite:function (el){
		
		var x=0,y=0;		
		while(el){
			
			x+=el.offsetLeft||0;			
			y+=el.offsetTop||0;			
			el=el.offsetParent;			
			
		}
		return {
			
			x:x,
			y:y
			
		};		
		
	},
	getSize:function (e){
		
		var w=jQuery.css(e,'width');		
		var h=jQuery.css(e,'height');		
		var wb=0;		
		var hb=0;		
		var es=e.style;		
		if(jQuery(e).css('display')!='none'){
			
			wb=e.offsetWidth;			
			hb=e.offsetHeight;			
			
		}else {
			
			var oldVisibility=es.visibility;			
			var oldPosition=es.position;			
			es.visibility='hidden';			
			es.display='block';			
			es.position='absolute';			
			wb=e.offsetWidth;			
			hb=e.offsetHeight;			
			es.display='none';			
			es.position=oldPosition;			
			es.visibility=oldVisibility;			
			
		}
		return {
			
			w:w,
			h:h,
			wb:wb,
			hb:hb
			
		};		
		
	},
	getSizeLite:function (el){
		
		return {
			
			wb:el.offsetWidth||0,
			hb:el.offsetHeight||0
			
		};		
		
	},
	getClient:function (e){
		
		var h,w,de;		
		if(e){
			
			w=e.clientWidth;			
			h=e.clientHeight;			
			
		}else {
			
			de=document.documentElement;			
			w=window.innerWidth||self.innerWidth||(de&&de.clientWidth)
			||document.body.clientWidth;			
			h=window.innerHeight||self.innerHeight
			||(de&&de.clientHeight)||document.body.clientHeight;			
			
		}
		return {
			
			w:w,
			h:h
			
		};		
		
	},
	getScroll:function (e){
		
		var t=0,l=0,w=0,h=0,iw=0,ih=0;		
		if(e&&e.nodeName.toLowerCase()!='body'){
			
			t=e.scrollTop;			
			l=e.scrollLeft;			
			w=e.scrollWidth;			
			h=e.scrollHeight;			
			iw=0;			
			ih=0;			
			
		}else {
			
			if(document.documentElement){
				
				t=document.documentElement.scrollTop;				
				l=document.documentElement.scrollLeft;				
				w=document.documentElement.scrollWidth;				
				h=document.documentElement.scrollHeight;				
				
			}else if(document.body){
				
				t=document.body.scrollTop;				
				l=document.body.scrollLeft;				
				w=document.body.scrollWidth;				
				h=document.body.scrollHeight;				
				
			}
			iw=self.innerWidth||document.documentElement.clientWidth
			||document.body.clientWidth||0;			
			ih=self.innerHeight||document.documentElement.clientHeight
			||document.body.clientHeight||0;			
			
		}
		return {
			
			t:t,
			l:l,
			w:w,
			h:h,
			iw:iw,
			ih:ih
			
		};		
		
	},
	getMargins:function (e,toInteger){
		
		var el=jQuery(e);		
		var t=el.css('marginTop')||'';		
		var r=el.css('marginRight')||'';		
		var b=el.css('marginBottom')||'';		
		var l=el.css('marginLeft')||'';		
		if(toInteger)
		return {
			
			t:parseInt(t)||0,
			r:parseInt(r)||0,
			b:parseInt(b)||0,
			l:parseInt(l)
			
		};		
		else 
		return {
			
			t:t,
			r:r,
			b:b,
			l:l
			
		};		
		
	},
	getPadding:function (e,toInteger){
		
		var el=jQuery(e);		
		var t=el.css('paddingTop')||'';		
		var r=el.css('paddingRight')||'';		
		var b=el.css('paddingBottom')||'';		
		var l=el.css('paddingLeft')||'';		
		if(toInteger)
		return {
			
			t:parseInt(t)||0,
			r:parseInt(r)||0,
			b:parseInt(b)||0,
			l:parseInt(l)
			
		};		
		else 
		return {
			
			t:t,
			r:r,
			b:b,
			l:l
			
		};		
		
	},
	getBorder:function (e,toInteger){
		
		var el=jQuery(e);		
		var t=el.css('borderTopWidth')||'';		
		var r=el.css('borderRightWidth')||'';		
		var b=el.css('borderBottomWidth')||'';		
		var l=el.css('borderLeftWidth')||'';		
		if(toInteger)
		return {
			
			t:parseInt(t)||0,
			r:parseInt(r)||0,
			b:parseInt(b)||0,
			l:parseInt(l)||0
			
		};		
		else 
		return {
			
			t:t,
			r:r,
			b:b,
			l:l
			
		};		
		
	},
	getPointer:function (event){
		
		var x=event.pageX
		||(event.clientX+(document.documentElement.scrollLeft||document.body.scrollLeft))
		||0;		
		var y=event.pageY
		||(event.clientY+(document.documentElement.scrollTop||document.body.scrollTop))
		||0;		
		return {
			
			x:x,
			y:y
			
		};		
		
	},
	traverseDOM:function (nodeEl,func){
		
		func(nodeEl);		
		nodeEl=nodeEl.firstChild;		
		while(nodeEl){
			
			jQuery.cutil.traverseDOM(nodeEl,func);			
			nodeEl=nodeEl.nextSibling;			
			
		}
		
	},
	purgeEvents:function (nodeEl){
		
		jQuery.cutil.traverseDOM(nodeEl,function (el){
			
			for(var attr in el){
				
				if(typeofel[attr]==='function'){
					
					el[attr]=null;					
					
				}
				
			}
			
		});		
		
	},
	centerEl:function (el,axis){
		
		var clientScroll=jQuery.cutil.getScroll();		
		var windowSize=jQuery.cutil.getSize(el);		
		if(!axis||axis=='vertically')
		jQuery(el).css({
			
			top:clientScroll.t
			+((Math.max(clientScroll.h,clientScroll.ih)
			-clientScroll.t-windowSize.hb)/2)+'px'
			
		});		
		if(!axis||axis=='horizontally')
		jQuery(el).css({
			
			left:clientScroll.l
			+((Math.max(clientScroll.w,clientScroll.iw)
			-clientScroll.l-windowSize.wb)/2)+'px'
			
		});		
		
	},
	fixPNG:function (el,emptyGIF){
		
		var images=jQuery('img[@src*="png"]',el||document),png;		
		images.each(function (){
			
			png=this.src;			
			this.src=emptyGIF;			
			this.style.filter="progid:DXImageTransform.Microsoft.AlphaImageLoader(src='"
			+png+"')";			
			
		});		
		
	},
	objectToUrl:function (obj){
		
		var rtn="";		
		if(obj&&obj!=null){
			
			$.each(obj,function (e,v){
				
				rtn=rtn+"&"+e+"="+v;				
				
			});			
			return rtn.substring(1);			
			
		}
		return rtn;		
		
	}
	
};

// Helper function to support older browsers!
[].indexOf||(Array.prototype.indexOf=function (v,n){
	
	n=(n==null)?0:n;	
	var m=this.length;	
	for(var i=n;i<m;i++)
	if(this[i]==v)
	return i;
	return -1;
	
});

(function ($){
	
	/**
	 * Jquery对象方法扩展
	 */
	$.fn.extend({
		
		getDimensions:function (){
			
			var el=this[0];			
			var display=this.css('display');			
			if(display!='none'&&display!=null)// Safari bug
			return {
				width:el.offsetWidth,height:el.offsetHeight
			};			
			var els=el.style,oV=els.visibility,oP=els.position,oD=els.display;			
			els.visibility='hidden';			
			els.position='absolute';			
			els.display='block';			
			var oW=el.clientWidth,oH=el.clientHeight;			
			els.display=oD;			
			els.position=oP;			
			els.visibility=oV;			
			return {
				width:oW,height:oH
			};			
			
		}
		
	});	
	
	/**
	 * Jquery通用方法扩展
	 */
	$.extend({
		
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
		
	});	
	
	
})(jQuery);

