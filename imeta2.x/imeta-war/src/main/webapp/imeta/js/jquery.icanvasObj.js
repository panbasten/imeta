(function($){
	function CanvasObj(_target,_otarget,_obj){
		// 自动创建对象序号
		autoCreateElIndex = 1;
		// 每次缩放的比例
		PER_SCALE = 0.1;
		// 图形容器，缩微图容器
		canvas = null, overlay = null;
		// 图形内容，缩微图内容
		ctx = null, octx = null;
		canvasWidth = 0, canvasHeight = 0;
		oCanvasWidth = 0, oCanvasHeight = 0;
		overviewScale = 1;
		
		// 目标对象
		target = _target;
		otarget = _otarget;
		
		// 是否显示网格
		var showGrid = false;
		// 是否贴近网格
		closeGrid = false;
		gridPoints = {
			initDistance : 50,
			points : []
		};
		
		outerControl = {
			isControl : false,
			controlType : null
		};
		
		canvasEls = {};
		// 辅助对象
		canvasAllSteps = [];
		canvasAllHops = [];
		
		selectedSteps = [];
		selectedHops = [];
		ropeRect = null;
		oropeRect = null;
		pmRopeRect = null;
		opmRopeRect = null;
		moveMousedown = null;
		omoveMousedown = null;
		// 连接块选定
		joinBlock = null;
		// 增加选定
		shiftFlag = null;
		// 拿起的对象
		pickupStepEl = null;
		pickupHopEl = null;
		// 鼠标对象
		mousePress = false;
		mouseDownEl = null;
		mouseUpEl = null;
		elMove = null;
		
		this.getCanvas = function() { return canvas; };
		this.getOverlay = function() { return overlay; };
		/**
		 * @public 添加元素
		 */
		this.appendEl = appendEl;
		/**
		 * 通过文字得到对象
		 */
		this.getCanvasElByText = getCanvasElByText;
		/**
		 * @public 删除选定元素
		 */
		this.deleteSelectedEl = deleteSelectedEl;
		/**
		 * @public 重画
		 */
		this.redraw = redraw;
		
		/**
		 * 更新元素
		 */
		this.updateEl = updateEl;
		
		/**
		 * 初始化
		 */
		this.reinitCanvas = reinitCanvas;
		
		/**
		 * 重新绘制内容框
		 */
		this.reinitContent = reinitContent;
		
		/**
		 * 得到绘图对象
		 */
		this.getCanvasEls = getCanvasEls;
		
		/**
		 * 设置绘图对象
		 */
		this.setCanvasEls = setCanvasEls;
		
		/**
		 * 克隆canvasEls
		 */
		this.cloneCanvasEls = cloneCanvasEls;
		
		/**
		 * 设置是否显示网格
		 */
		this.setShowGrid = setShowGrid;
		
		/**
		 * 设置是否贴近网格
		 */
		this.setCloseGrid = setCloseGrid;
		
		this.setOuterControl = setOuterControl;
		
		/**
		 * 100%
		 */
		this.setHundredPercent = setHundredPercent;
		
		/**
		 * 合适的尺寸
		 */
		this.setFixScale = setFixScale;
		
        /**
		 * 得到剪切对象
		 */
		this.getCutSelectedEls = getCutSelectedEls;
		
		/**
		 * 得到复制对象
		 */
		this.getCopySelectedEls = getCopySelectedEls;
					
        /**
		 * 分组
		 */
		this.setGroup = setGroup;
		
		/**
		 * 删除元素
		 */
		this.disabledEl = disabledEl;
		
		/**
		 * 删除元素附带的线条
		 */
		this.deleteHopWithEl = deleteHopWithEl;
		
		/**
		 * 清除选定
		 */
		this.cleanSelectedEl = cleanSelectedEl;
		
		// 初始化过程组
		canvasEls = cloneCanvasElsObject($.canvasObj.defaults);
		$.extend(canvasEls,_obj);
		constructCanvas();
		constructTarget();
		initCanvasGrid();
		
		/**
		 * 设置缩放比例和起点坐标
		 */
		function setCanvasElScale(s,x,y){
			if(canvasEls.scale>10 && s>canvasEls.scale){return false;}
			if(canvasEls.scale<0.2 && s<canvasEls.scale){return false;}
			if(canvasEls.scale==s && canvasEls.x0==x && canvasEls.y0==y){return false;}
			canvasEls.scale=s;
			canvasEls.x0=x;
			canvasEls.y0=y;
			
			return true;
		}
		
		/**
		 * 100%
		 */
		function setHundredPercent(){
			if(setCanvasElScale(1,0,0)){
				redraw();
				// 更新tab--100%
				//alert("100%");
				if(jQuery.imenu.iMenuBtns['editObject'].selected){
				     jQuery.iPortalTab.updateActiveTab(jQuery.iPortalTab.TYPE_ACTION_HUNDRED_PERCENT);
				}
			}
		}
		
		function getDrawTextRect(text,x,y){
			if(text!=undefined && text.length>0){
				var textRect = {
					x : canvasWidth,
					y : canvasHeight,
					x2 : 0,
					y2 : 0
				};
				// 如果是IE
				if ($.browser.msie){
				}
				// 如果不是IE
				else{
					var nf = 10;
					
					for(var i=0;i<text.length;i++){
						ctx.save();
						ctx.font = nf+"px Mono";
						var len = ctx.measureText(text[i]).width;
						var sx = Math.floor(x-(len/2));
						var sy = y+(nf+4)*i;
						ctx.restore();
						
						textRect.x = (sx<textRect.x)?sx:textRect.x;
						textRect.y = ( (sy-nf)<textRect.y)?(sy-nf):textRect.y;
						textRect.x2 = ( (sx+len)>textRect.x2)?(sx+len):textRect.x2;
						textRect.y2 = (sy>textRect.y2)?sy:textRect.y2;
					}
					
					return textRect;
				}
			}
			
			return null;
		}
		
		/**
		 * 设置成合适的尺寸
		 */
		function setFixScale(){
			var elScope = {
				x : canvasWidth,
				y : canvasHeight,
				x2 : 0,
				y2 : 0
			};
			// 遍历steps
			var x2,y2,d;
			$.each(canvasEls.steps, function(e,v){
				if(v!=undefined){
					if(v.isEnable){
						elScope.x = (v.dx<elScope.x)?v.dx:elScope.x;
						elScope.y = (v.dy<elScope.y)?v.dy:elScope.y;
						x2 = v.dx+v.dWidth;
						y2 = v.dy+v.dHeight;
						elScope.x2 = (x2>elScope.x2)?x2:elScope.x2;
						elScope.y2 = (y2>elScope.y2)?y2:elScope.y2;
						
						d = getDrawTextRect(v.bText,v.dx+(v.dWidth/2),v.dy+v.dHeight+14);
						if(d!=null){
							elScope.x = (d.x<elScope.x)?d.x:elScope.x;
							elScope.y = (d.y<elScope.y)?d.y:elScope.y;
							elScope.x2 = (d.x2>elScope.x2)?d.x2:elScope.x2;
							elScope.y2 = (d.y2>elScope.y2)?d.y2:elScope.y2;
						}
					}
				}
			});
			// 遍历hops，只遍历中间节点
			var hopx,hopy,thex,they;
			$.each(canvasEls.hops, function(e,v){
				if(v!=undefined){
					if(v.isEnable){
						hopx = v.x;
						hopy = v.y;
						for(var i=1;i<hopx.length-1;i++){
							thex=hopx[i];
							they=hopy[i];
							elScope.x = (thex<elScope.x)?thex:elScope.x;
							elScope.y = (they<elScope.y)?they:elScope.y;
							elScope.x2 = (thex>elScope.x2)?thex:elScope.x2;
							elScope.y2 = (they>elScope.y2)?they:elScope.y2;
							
							d = getDrawTextRect(v.text, (v.x[0]+v.x[1])/2, (v.y[0]+v.y[1])/2);
							if(d!=null){
								elScope.x = (d.x<elScope.x)?d.x:elScope.x;
								elScope.y = (d.y<elScope.y)?d.y:elScope.y;
								elScope.x2 = (d.x2>elScope.x2)?d.x2:elScope.x2;
								elScope.y2 = (d.y2>elScope.y2)?d.y2:elScope.y2;
							}
						}
					}
				}
			});
			// 遍历groups
			// TODO
			
			if(setScaleByRect(elScope)){
				// 更新tab--合适比例
				//alert("合适比例");
				if(jQuery.imenu.iMenuBtns['editObject'].selected){
				    jQuery.iPortalTab.updateActiveTab(jQuery.iPortalTab.TYPE_ACTION_FIX_SCALE);
				}
					
				return true;
			}
			return false;
		}
		
		/**
		 * 局部放大
		 */
		function setPartScale(r){
			return setScaleByRect({
				x : (r.x<r.x2)?r.x:r.x2,
				y : (r.y<r.y2)?r.y:r.y2,
				x2 : (r.x<r.x2)?r.x2:r.x,
				y2 : (r.y<r.y2)?r.y2:r.y
			});
		}
		
		/**
		 * 通过矩形放大面板
		 */
		function setScaleByRect(elScope){
			// 所有元素的整体长宽
			var allWidth = elScope.x2-elScope.x;
			allWidth = (allWidth<5)?5:allWidth;
			var allHeight = elScope.y2-elScope.y;
			allHeight = (allHeight<5)?5:allHeight;
			var allWidthScale = canvasWidth/allWidth;
			var allHeightScale = canvasHeight/allHeight;
			var theScale = (allWidthScale<allHeightScale)?allWidthScale:allHeightScale;
			// 缩放
			var isScale = setCanvasElScale(theScale,0,0);
			// 如果超过最大值返回
			if(!isScale){return false;}
			
			// 原中点的缩放后的新坐标值
			var nsp = scalePoint({
				x : elScope.x,
				y : elScope.y
			});
			var nep = scalePoint({
				x : elScope.x2,
				y : elScope.y2
			});
			canvasEls.x0 = Math.floor( canvasWidth/2 - (nsp.x+nep.x)/2 );
			canvasEls.y0 = Math.floor( canvasHeight/2 - (nsp.y+nep.y)/2 );
			redraw();
			return true;
		}
		
		function setOuterControl(c){
			outerControl.isControl=c.isControl;
			outerControl.controlType=c.controlType;
			if(c.isControl&&c.cursorImg&&c.cursorImg!=null){
				setCanvasCursor(c.cursorImg);
			}else{
				setCanvasCursor(null);
			}
		}
		
		function cloneStep(o){
		    var cloneStep = {};
			$.extend(cloneStep,o);
			cloneStep.bText = [];
			if(o.bText){
    			for(var i=0;i<o.bText.length;i++){
    				cloneStep.bText[i] = o.bText[i];
    			}
			}
			return cloneStep;
		}
		
		function cloneHop(o){
			var cloneHop = {};
			$.extend(cloneHop,o);
			cloneHop.x = [];
			cloneHop.y = [];
			for(var i=0;i<o.x.length;i++){
				cloneHop.x[i] = o.x[i];
				cloneHop.y[i] = o.y[i];
			}
			return cloneHop;
		}
		
		/**
		 * 克隆group
		 */
		function cloneGroup(o){
			return cloneCanvasElsObject(o);
		}
		
		function cloneCanvasEls(){
			return cloneCanvasElsObject(canvasEls);
		}
		
		function cloneCanvasElsObject(el){
			var newCanvasEls = {};
			$.extend(newCanvasEls,el,{
			    steps : [],
				hops : [],
				groups : []
			})
			if(el.steps){
				$.each(el.steps,function(e,v){
					if(v!=undefined){
						var newStep = cloneStep(v);
						$.extend(newStep,{
							index:newCanvasEls.steps.length
						});
						newCanvasEls.steps.push(newStep);	
					}
				});
			}
			if(el.hops){
				$.each(el.hops,function(e,v){
					if(v!=undefined){
						var newHop = cloneHop(v);
						$.extend(newHop,{
							index:newCanvasEls.hops.length
						});
						newCanvasEls.hops.push(newHop);
					}
				});
			}
			if(el.groups){
				$.each(el.groups,function(e,v){
					if(v!=undefined){
						var newGroup = cloneGroup(v);
						$.extend(newGroup,{
							index:newCanvasEls.groups.length
						});
						newCanvasEls.groups.push(newGroup);
					}
				});
			}
			return newCanvasEls;
		}
		
		function getCanvasEls(){
			return canvasEls;
		}
		
		
		/**
		 * 设置当前页面元素
		 */
		function setCanvasEls(cels){
			var c = cloneCanvasElsObject(cels);
			$.extend(canvasEls,c);
			selectedSteps.length=0;
			selectedHops.length=0;
			redraw();
		}
		
		/**
		 * 新建一个组对象
		 * 并将选定元素复制到组对象
		 */
		function cloneGroupObject(){
			var newGroupObj = {
				steps: [],
				hops: [],
				groups: []
		    };
		 
			$.each(selectedSteps,function(e,v){
				if(v!=undefined){
					var newStep = {};
					$.extend(newStep,v);
					newStep.id = "group_"+newGroupObj.steps.length+"_"+v.id;
					newStep.index = newGroupObj.steps.length;
					newGroupObj.steps.push(newStep);
//						console.log(newStep);
				}
			});
			
			$.each(selectedHops,function(e,v){
				if(v!=undefined){
					var newHop = {};
					$.extend(newHop,v);
					newHop.index = newGroupObj.hops.length;
					newGroupObj.hops.push(newHop);
//						console.log(newHop);
				}
			});
			return newGroupObj;
		}
		
		/**
		 * 分组
		 */
		function setGroup(){
			//TODO
			var g = cloneGroupObject();
//				$.extend(canvasEls,g);
			canvasEls.groups.push(g);
			deleteSelectedEl();
//				console.log(canvasEls);
			
		}
		
		/**
		 * 初始化画布网格
		 */
		function initCanvasGrid(){
			// 绘制网格
			if(showGrid){
				var distance = Math.floor(gridPoints.initDistance*canvasEls.scale);
				gridPoints.points.length=0;
				
				var tx0=canvasEls.x0,ty0=canvasEls.y0;
				
				while(tx0>0)tx0-=distance;
				while(ty0>0)ty0-=distance;
				
				for(var i=tx0;i<=canvasWidth;i=i+distance){
					var c = [];
					for(var j=ty0;j<=canvasHeight;j=j+distance){
						drawCross({
							x : i,
							y : j
						});
						c.push({
							x : i,
							y : j
						});
					}
					gridPoints.points.push(c);
				}
			}
		}
		
		function setShowGrid(b){
			showGrid = b;
		}
		
		function setCloseGrid(b){
			closeGrid = b;
		}
		
		function reinitCanvas(){
			setCanvasEls($.canvasObj.defaults);
			redraw();
		}
		
		/**
		 * 画十字
		 */
		function drawCross(c){
			ctx.save();
		  	ctx.beginPath();
		  	ctx.strokeStyle = '#999';
			ctx.globalAlpha = 1;
		  	ctx.moveTo(c.x-5,c.y);
		  	ctx.lineTo(c.x+5,c.y);
		  	ctx.moveTo(c.x,c.y-5);
		  	ctx.lineTo(c.x,c.y+5);
		  	ctx.stroke();
		  	ctx.restore();
		}
		
		/**
		 * 绘制圆角框
		 */
		function roundedRect(o){
		  ctx.save();
		  ctx.beginPath();
		  ctx.moveTo(o.x,o.y+o.radius);
		  ctx.lineTo(o.x,o.y+o.height-o.radius);
		  ctx.quadraticCurveTo(o.x,o.y+o.height,o.x+o.radius,o.y+o.height);
		  ctx.lineTo(o.x+o.width-o.radius,o.y+o.height);
		  ctx.quadraticCurveTo(o.x+o.width,o.y+o.height,o.x+o.width,o.y+o.height-o.radius);
		  ctx.lineTo(o.x+o.width,o.y+o.radius);
		  ctx.quadraticCurveTo(o.x+o.width,o.y,o.x+o.width-o.radius,o.y);
		  ctx.lineTo(o.x+o.radius,o.y);
		  ctx.quadraticCurveTo(o.x,o.y,o.x,o.y+o.radius);
		  ctx.stroke();
		  ctx.restore();
		}
		
		/**
		 * 绘制选择框
		 */
		function drawSelectedRect(x,y,width,height){
			drawSelectedRectMeta(ctx,1,x,y,width,height);
			drawSelectedRectMeta(octx,overviewScale,x,y,width,height);
		}
		
		function drawSelectedRectMeta(o,s,x,y,width,height){
			o.save();
			o.fillStyle = '#09F';
			o.globalAlpha = 0.2;
			o.fillRect(x*s,y*s,width*s,height*s);
			o.restore();
		}
		
		/**
		 * 绘制选择的连线
		 */
		function drawSelectedLine(el){
			drawSelectedLineMeta(ctx,1,el);
			drawSelectedLineMeta(octx,overviewScale,el);
		}
		
		function drawSelectedLineMeta(o,s,el){
			var len = el.x.length;
			o.save();
			o.fillStyle = '#09F';
			o.globalAlpha = 0.2;
			o.lineWidth = 8*s;
			o.lineCap = 'round';
			o.lineJoin = 'round';
			o.beginPath();
			var fel,tel;
			var felc,telc;
			// 得到开始对象的中点
			if(el.fromEl!=null&&el.fromEl!=''){
				fel = getCanvasAllElById(el.fromEl,'step');
				felc = getElMidPoint(fel.el);
				
			}
			// 得到结束对象的中点
			if(el.toEl!=null&&el.toEl!=''){
				tel = getCanvasAllElById(el.toEl,'step');
				telc = getElMidPoint(tel.el);
			}
			
			var nc;
			if(len<=2 && el.fromEl == el.toEl){
				var r = scaleLength(20);
				o.arc(el.x[0]*s,(el.y[0]-r)*s,r*s,(Math.PI*0.25),(Math.PI*0.75),true);
			}else{
				// 开始框体对象
				if(len==2){
					nc = {
						x : el.x[1],
						y : el.y[1]
					};
				}else{
					nc = scalePoint({
						x : el.x[1],
						y : el.y[1]
					});
				}
				fc = getLineShortedCoords(scaleLength(el.fromElWidth),scaleLength(el.fromElHeight),{
					x : felc.x,
					y : felc.y,
					x2 : nc.x,
					y2 : nc.y
				});
				
				// 结束框体对象
				if(len==2){
					nc = {
						x : el.x[0],
						y : el.y[0]
					};
				}else{
					nc = scalePoint({
						x : el.x[len-2],
						y : el.y[len-2]
					});
				}
				tc = getLineShortedCoords(scaleLength(el.toElWidth),scaleLength(el.toElHeight),{
					x : telc.x,
					y : telc.y,
					x2 : nc.x,
					y2 : nc.y
				});
				o.moveTo(fc.x*s,fc.y*s);
				for(var i=1;i<len-1;i++){
					nc = scalePoint({
						x : el.x[i],
						y : el.y[i]
					});
					o.lineTo(nc.x*s,nc.y*s);
					
					drawSelectedRect(nc.x-5,nc.y-5,10,10);
				}
				o.lineTo(tc.x*s,tc.y*s);
			}	
			o.stroke();
			o.restore();
		}
		
		
		/**
		 * 绘制关联线
		 */
		function drawHop(obj,offset){
			drawHopMeta(ctx,1,obj,offset);
			drawHopMeta(octx,overviewScale,obj,offset);
		}
		function drawHopMeta(o,s,obj,offset){
			var hopOption = {
				id : '',
				x : [0,0],
				y : [0,0],
				fromEl : '',
				toEl : '',
				fromElType : '',
				toElType : '',
				fromElWidth:32,
				fromElHeight:32,
				toElWidth:32,
				toElHeight:32,
				elType : "hop",
				style : "blue",
				text:[]
			};
			// 用o覆盖imgOptions
			$.extend(hopOption, obj);
			// 设置偏移量
			if(offset!=null){
				var ox=null,oy=null;
				if(offset.x!=null && typeof offset.x == "number") {
					ox = $.map(hopOption.x, function(i){
							return i+offset.x;
						});
				}
				if(offset.y!=null && typeof offset.y == "number") {
					oy = $.map(hopOption.y, function(i){
							return i+offset.y;
						});
				}
				$.extend(hopOption, {
					x : (ox!=null)?ox:hopOption.x,
					y : (oy!=null)?oy:hopOption.y
				});
			}
			
			var len = hopOption.x.length;
			// 更新起点和终点
			var fel,tel;
			var felc,telc;
			// 得到开始对象,更新开始点
			if(hopOption.fromEl!=null&&hopOption.fromEl!=''){
				fel = getCanvasAllElById(hopOption.fromEl,'step');
				felc = getElMidPoint(fel.el);
				hopOption.x[0]=felc.x;
				hopOption.y[0]=felc.y;
				hopOption.fromElWidth = fel.el.dWidth;
				hopOption.fromElHeight = fel.el.dHeight;
			}
			// 得到结束对象,更新结束点
			if(hopOption.toEl!=null&&hopOption.toEl!=''){
				tel = getCanvasAllElById(hopOption.toEl,'step');
				telc = getElMidPoint(tel.el);
				hopOption.x[len-1]=telc.x;
				hopOption.y[len-1]=telc.y;
				hopOption.toElWidth = tel.el.dWidth;
				hopOption.toElHeight = tel.el.dHeight;
			}
			var fc=null,tc=null;
			var nc;
			// 缩短开始，结束长度

			// 开始对象
			// 两个节点
			// 1.直线连接两个节点
			// 2.拖动的画线且没有中间节点
			if(len==2 ){
				nc = {
					x : hopOption.x[1],
					y : hopOption.y[1]
				};
			}
			// 大于两个节点
			else{
				nc = scalePoint({
					x : hopOption.x[1],
					y : hopOption.y[1]
				});
			}
			fc = getLineShortedCoords(scaleLength(hopOption.fromElWidth),scaleLength(hopOption.fromElHeight),{
				x : felc.x,
				y : felc.y,
				x2 : nc.x,
				y2 : nc.y
			});
			
			// 结束对象
			if(hopOption.toEl!=null && hopOption.toEl!=''){
				if(len==2){
					nc = {
						x : hopOption.x[0],
						y : hopOption.y[0]
					};
				}else{
					nc = scalePoint({
						x : hopOption.x[len-2],
						y : hopOption.y[len-2]
					});
				}
				tc = getLineShortedCoords(scaleLength(hopOption.toElWidth),scaleLength(hopOption.toElHeight),{
					x : telc.x,
					y : telc.y,
					x2 : nc.x,
					y2 : nc.y
				});
			}
			
			o.save();
			ctx.strokeStyle = hopOption.style;
			ctx.fillStyle = hopOption.style;
			o.globalAlpha = 1;
			o.beginPath();
			if(len<=2 && hopOption.fromEl == hopOption.toEl){
				var r = scaleLength(20);
				o.arc( hopOption.x[0]*s,( hopOption.y[0]-r)*s,r*s,(Math.PI*0.25),(Math.PI*0.75),true);
				o.stroke();
				o.beginPath();
				var ot = Math.floor(Math.sqrt(Math.pow(r,2)/2));
				o.arc(( hopOption.x[0]-ot)*s,( hopOption.y[0]-r+ot)*s,3*s,0,Math.PI*2,true);
				o.fill();
			}else{
				o.moveTo(fc.x*s,fc.y*s);
				for(var i=1;i<len-1;i++){
					nc = scalePoint({
						x : hopOption.x[i],
						y : hopOption.y[i]
					});
					o.lineTo(nc.x*s,nc.y*s);
				}
				if(tc!=null){
					o.lineTo(tc.x*s,tc.y*s);
					o.stroke();
					o.beginPath();
					o.arc(tc.x*s,tc.y*s,3*s,0,Math.PI*2,true);
					o.fill();
				}else{
					o.lineTo(hopOption.x[len-1]*s,hopOption.y[len-1]*s);
				}
				
			}
			if(s==1){
				var nh,nh2;
				nh = {
					x : hopOption.x[0],
					y : hopOption.y[0]
				};
				if(hopOption.x.length==2){
					nh2 = {
						x : hopOption.x[1],
						y : hopOption.y[1]
					};
				}else{
					nh2 = scalePoint({
						x : hopOption.x[1],
						y : hopOption.y[1]
					});
				}
				drawText(hopOption.text, (nh.x+nh2.x)/2, (nh.y+nh2.y)/2,'Blue');
			}
			
			o.stroke();
			o.restore();
			
			// 加入all
			canvasAllHops.push(hopOption);
		}
		
		/**
		 * 缩放点
		 * 1.参照元素位置进行缩放
		 * 2.根据现在的起点进行修正
		 */
		function scalePoint(p){
			return {
				x : Math.floor(canvasEls.x0+p.x*canvasEls.scale),
				y : Math.floor(canvasEls.y0+p.y*canvasEls.scale)
			};
		}
		
		/**
		 * 反向缩放
		 */
		function arcScalePoint(p){
			return {
				x : Math.floor( (p.x-canvasEls.x0)/canvasEls.scale ),
				y : Math.floor( (p.y-canvasEls.y0)/canvasEls.scale )
			}
		}
		
		/**
		 * 缩放长度
		 */
		function scaleLength(l){
			return Math.floor(l*canvasEls.scale);
		}
		
		function arcScaleLength(l){
			return Math.floor(l/canvasEls.scale);
		}
		
		/**
		 * 画图
		 */
		function drawImg(o,offset){
			var imgOptions = {
				id : '',
				imgId : '',
				sx : 0, 
				sy : 0, 
				sWidth : 32, 
				sHeight : 32, 
				dx : Math.floor(canvasWidth/3), 
				dy : Math.floor(canvasHeight/3), 
				dWidth : 32, 
				dHeight : 32,
				elType:"step",
				index:canvasEls.steps.length,
				bText: [],
				tlText : []
			};
			// 用o覆盖imgOptions
			$.extend(imgOptions, o);
			// 设置偏移量
			if(offset!=null){
				if(offset.x!=null && typeof offset.x == "number"){ imgOptions.dx = imgOptions.dx+offset.x;}
				if(offset.y!=null && typeof offset.y == "number"){ imgOptions.dy = imgOptions.dy+offset.y;}
			}
			// 原图
			ctx.save();
			
			var sd = scalePoint({
				x : imgOptions.dx,
				y : imgOptions.dy
			});
			var sdWidth = scaleLength(imgOptions.dWidth);
			var sdHeight = scaleLength(imgOptions.sHeight);
			try{
				ctx.drawImage($('#'+imgOptions.imgId).get(0),
					imgOptions.sx, imgOptions.sy,
					imgOptions.sWidth,imgOptions.sHeight,
					sd.x,sd.y,sdWidth,sdHeight);
			}catch(ex){
				console.log("jquery.icontent.js-"+ex);
			}
			
			try{
				drawText(imgOptions.bText,sd.x+(sdWidth/2),sd.y+sdHeight+scaleLength(10)+4);
				drawText(imgOptions.tlText,sd.x,sd.y);
			}catch(ex){
				console.log("jquery.icontent.js-"+ex);
			}
			
			ctx.restore();
			
			// 缩略图
			octx.save();
			octx.strokeRect(sd.x*overviewScale,sd.y*overviewScale,
					sdWidth*overviewScale,sdHeight*overviewScale);
			octx.restore();
			
			
			// 加入all
			canvasAllSteps.push(imgOptions);
			
		}
		
		function drawText(text,x,y,style){
			if(text!=undefined && text.length>0){
				var textRect = {
					x : canvasWidth,
					y : canvasHeight,
					x2 : 0,
					y2 : 0
				};
				
				var nf = scaleLength(10);
				
				for(var i=0;i<text.length;i++){
					ctx.save();
					
					ctx.fillStyle = (style)?style:"Red";
					ctx.font = nf+"px Mono";
					var len = ctx.measureText(text[i]).width;
					var sx = Math.floor(x-(len/2));
					var sy = y+(nf+4)*i;
					ctx.fillText(text[i],sx,sy);
					ctx.restore();
					
					textRect.x = (sx<textRect.x)?sx:textRect.x;
					textRect.y = ( (sy-nf)<textRect.y)?(sy-nf):textRect.y;
					textRect.x2 = ( (sx-len)>textRect.x2)?(sx-len):textRect.x2;
					textRect.y2 = (sy>textRect.y2)?sy:textRect.y2;
				}
				
				return textRect;
				
			}
		}
		
		
		/**
		 * 画组
		 */
		function drawGroup(o,offset){
			var groupOptions = {
				id : '',
				x : 300,
				y : 200,
				width : 300,
				height : 150,
				radius : 5
			};
			roundedRect(groupOptions);
		}
		
		/**
		 * 重画页面
		 */
		function redrawCanvas(_els,_offset){
			initCanvasGrid();
			// 取得页面对象canvasEls
			var els = _els;
			// 设置偏移量
			var offset = null;
			if(_offset !=null ){
				offset = _offset;
			}else{
				offset = {
					x : 0,
					y : 0
				};
			}
			// 1绘制groups
			if(els.groups!=null){
				$.each(els.groups, function (k, v){
					if(v != undefined){
						//redrawCanvas(v, v.offset);
						drawGroup();
					}
				});
			}
			
			// 2绘制steps
			if(els.steps!=null){
				$.each(els.steps, function (k, v) {
					if(v != undefined){
						if(v.isEnable){
							drawImg(v,offset);
						}
					}
				});
			}
			
			// 3绘制hops
			if(els.hops!=null){
				$.each(els.hops, function (k, v) {
					if(v != undefined){
						if(v.isEnable){
							drawHop(v,offset);	
						}
					}
				});
			}
			
		}
		
		/**
		 * 重画选定的对象
		 */
		function redrawSelectedRect(){
			// 选定对象
			$.each(selectedSteps, function (k, v){
				if(v != undefined){
					// 更新选定框位置
					var cel = getCanvasAllElById(v.id,'step');
					$.extend(selectedSteps[k], cel.el);
					var nc = scalePoint({
						x : cel.el.dx,
						y : cel.el.dy
					});
					drawSelectedRect(nc.x-3,nc.y-3,scaleLength(cel.el.dWidth)+6,scaleLength(cel.el.dHeight)+6);
				}
			});
			// 选定连线
			$.each(selectedHops, function (k, v){
				if(v != undefined){
					// 更新选定框位置
					var cel = getCanvasAllElById(v.id,'hop');
					$.extend(selectedHops[k], cel.el);
					drawSelectedLine(cel.el);
				}
			});
		}
		
		/**
		 * 重画圈选框
		 */
		function redrawRopeRect(){
			if(oropeRect!=null){
				redrawRopeRectMeta(ctx,1/(overviewScale),oropeRect);
				redrawRopeRectMeta(octx,1,oropeRect);
			}else if(ropeRect!=null){
				redrawRopeRectMeta(ctx,1,ropeRect);
				redrawRopeRectMeta(octx,overviewScale,ropeRect);
			}
		}
		function redrawRopeRectMeta(o,s,r){
			if(r!=null){
				var x = (r.x<r.x2)?r.x:r.x2;
				var y = (r.y<r.y2)?r.y:r.y2;
				var w = Math.abs(r.x2 - r.x);
				var h = Math.abs(r.y2 - r.y);
				o.save();
				o.fillStyle = '#FD0';
				o.globalAlpha = 0.3;
				o.fillRect(x*s,y*s,w*s,h*s);
				o.restore();
			}
		}
		
		/**
		 * 重画局部扩大框
		 */
		function redrawPmRopeRect(){
			if(opmRopeRect!=null){
				redrawPmRopeRectMeta(ctx,1/overviewScale,opmRopeRect);
				redrawPmRopeRectMeta(octx,1,opmRopeRect);
			}else if(pmRopeRect!=null){
				redrawPmRopeRectMeta(ctx,1,pmRopeRect);
				redrawPmRopeRectMeta(octx,overviewScale,pmRopeRect);
			}
		}
		function redrawPmRopeRectMeta(o,s,r){
			if(r!=null){
				var x = (r.x<r.x2)?r.x:r.x2;
				var y = (r.y<r.y2)?r.y:r.y2;
				var w = Math.abs(r.x2 - r.x);
				var h = Math.abs(r.y2 - r.y);
				o.save();
				o.fillStyle = '#6495ED';
				o.globalAlpha = 0.3;
				o.fillRect(x*s,y*s,w*s,h*s);
				o.restore();
			}
		}
		
		function redraw() {
			// 清空图形
			ctx.clearRect(0,0,canvasWidth,canvasHeight);
			octx.clearRect(0,0,oCanvasWidth,oCanvasHeight);
			// 清空辅助对象
			canvasAllSteps.length = 0;
			canvasAllHops.length = 0;
			// 重画图形
			redrawCanvas(canvasEls);
			// 重画选定框
			redrawSelectedRect();
			// 重画圈选框
			redrawRopeRect();
			// 重画圈选框
			redrawPmRopeRect();
		}
		
		function appendEl(o, path){
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
			var objectType = activeTab.objectType;
			
			var newd;
			if(o.dx && o.dx>=0){
			    newd = arcScalePoint({
			 				x : o.dx,
			 				y : o.dy
			 			});
			}else{
				var newNum = activeTab.newNum;
				var newLength = scaleLength(50);
				var newElWidth = Math.floor(canvasWidth/3);
				var newElHeight =Math.floor(canvasHeight/3);
				var maxNewElWidthNum = Math.floor(newElWidth/newLength);
				var maxNewElHeightNum = Math.floor(newElHeight/newLength);
				newNum = newNum % (maxNewElWidthNum*maxNewElHeightNum);
				var newdxNum = Math.floor(newNum/maxNewElHeightNum);
				var newdyNum = newNum%maxNewElHeightNum;
				newd = arcScalePoint({
			 				x : (newElWidth + newdxNum*newLength),
			 				y : (newElHeight +newdyNum*newLength) 
			 			});
			}
		 			
			var theId = o.id;
			var theO = null;
			if(path != null) {
				// TODO加入到组中
			}else{
				if(o.elType=='step'){
					theId = (theId!=null)?theId:('new'+objectType+'_'+(autoCreateElIndex++));
					theO = {
						id : theId,
						imgId : '',
						sx : 0,
						sy : 0,
						sWidth : 32, 
						sHeight : 32,
						dWidth : 32, 
						dHeight : 32,
						elType:"step",
						dx : 0, 
						dy : 0 , 
						path : 'canvasEls.steps',
						index : canvasEls.steps.length,
						isNew : true,
						isEnable : true,
						bText:[]
					};
					$.extend(theO,o);
					$.extend(theO,{
						id : theId,
						dx : newd.x, 
						dy : newd.y , 
						path : 'canvasEls.steps',
						index : canvasEls.steps.length,
						isNew : true,
						isEnable : true
					});
					canvasEls.steps.push(theO);
					// 更新tab--添加
					//alert("添加");
					if(objectType == jQuery.iPortalTab.OBJECT_TYPE_TRANS){
						jQuery.iPortalTab.updateActiveTab(jQuery.iPortalTab.TYPE_ACTION_NEW_STEP);
					}else if(objectType == jQuery.iPortalTab.OBJECT_TYPE_JOB){
						jQuery.iPortalTab.updateActiveTab(jQuery.iPortalTab.TYPE_ACTION_NEW_JOB_ENTRY);
					}
					
				}else if(o.elType == 'hop') {
					theId = (theId!=null)?theId:('newhop_'+(autoCreateElIndex++));
					theO = {
						id : theId,
						path : 'canvasEls.hops',
						index : canvasEls.hops.length,
						isNew : true,
						isEnable : true,
						x : [0,0],
						y : [0,0],
						fromEl : '',
						toEl : '',
						fromElType : '',
						toElType : '',
						fromElWidth:32,
						fromElHeight:32,
						toElWidth:32,
						toElHeight:32,
						elType : "hop",
						text:[]
					};
					$.extend(theO,o);
					$.extend(theO,{
						id : theId,
						path : 'canvasEls.hops',
						index : canvasEls.hops.length,
						isNew : true,
						isEnable : true
					});
					canvasEls.hops.push(theO);
				}else if(o.elType == 'group') {
					theId = (theId!=null)?theId:('newgroup_'+(autoCreateElIndex++));
					// TODO 
				}
			}
			// 重画
			redraw();
			return theO;
		}
		
		/**
		 * 剪切 选定的对象
		 */
		function getCutSelectedEls(){
			
			//得到选中的对象
			var cutEls = null;
			
			cutEls = {
		    	steps: selectedSteps,
				hops: selectedHops
		     };
		    
		   // 克隆剪切对象
            var cutEls1=cloneCanvasElsObject(cutEls);
            
		    // 保存剪切对象   
		    var activeTabId = jQuery.iPortalTab.activeTabId;
		    
		    jQuery.imenu.iContentClipboard.originTabId = activeTabId;
		 //   alert("111---"+jQuery.imenu.iContentClipboard.originTabId);
		    jQuery.imenu.iContentClipboard.cutEls = cutEls1;
		//     alert("222---"+jQuery.imenu.iContentClipboard.cutEls);
		    
		     // 选中的剪切对象不显示
			$.each(selectedSteps, function (k, v){
				disabledEl(v);
				deleteHopWithEl(v);
			});
			
			// 选中的剪切关系不显示
			$.each(selectedHops, function (k, v){
				disabledEl(v);
			});
			
			// 重画
			redraw();
			        
           // 清空选中  
            selectedSteps.length=0;
			selectedHops.length=0;
			
			return cutEls;
		}
		
		/**
		 * 复制 选定的对象
		 */
		function getCopySelectedEls(){
			
			//得到选中的对象
			var copyEls =null;
			
			copyEls = {
		    	steps: selectedSteps,
				hops: selectedHops
		     };
		     	
			// 克隆选中的对象
	      var copyEls1=cloneCanvasElsObject(copyEls);
	        
	        // 保存选中的对象 
		    var activeTabId = jQuery.iPortalTab.activeTabId;
		    jQuery.imenu.iContentClipboard.originTabId = activeTabId;
		    jQuery.imenu.iContentClipboard.cutEls = copyEls1;
	        
	        // 重画
			redraw();
	        
	        // 清空选中
	        selectedSteps.length=0;
			selectedHops.length=0;
			
			return copyEls;
		}
		
		
		/**
		 * 删除选定的对象
		 */
		function deleteSelectedEl(){
			if(jQuery.iPortalTab.activeTabId==null){return;}
			if(!jQuery.imenu.iMenuBtns['editObject'].selected){return;}
			var activeTab = jQuery.iPortalTab.getActiveTab();
			var objectType = activeTab.objectType;
			
			var delStep = false, delHop = false;
			$.each(selectedSteps, function (k, v){
				disabledEl(v);
				deleteHopWithEl(v);
				delStep = true;
			});
			$.each(selectedHops, function (k, v){
				disabledEl(v);
				delHop = true;
			});
			cleanSelectedEl();
			// 重画
			redraw();
			// 更新tab--删除
//				alert("delete");
			var actionType = "";
			
			if(objectType == jQuery.iPortalTab.OBJECT_TYPE_TRANS){
				if(delStep){
					jQuery.iPortalTab.updateActiveTab(jQuery.iPortalTab.TYPE_ACTION_DELETE_STEP);
				}else if(delHop){
					jQuery.iPortalTab.updateActiveTab(jQuery.iPortalTab.TYPE_ACTION_DELETE_HOP);
				}
				
			}else if(objectType == jQuery.iPortalTab.OBJECT_TYPE_JOB){
				if(delStep){
					jQuery.iPortalTab.updateActiveTab(jQuery.iPortalTab.TYPE_ACTION_DELETE_JOB_ENTRY);
				}else if(delHop){
					jQuery.iPortalTab.updateActiveTab(jQuery.iPortalTab.TYPE_ACTION_DELETE_JOB_HOP);
				}
			}
		}
		
		/**
		 * 删除对象
		 */
		function deleteEl(el){
			eval('delete '+el.path+'['+el.index+'];');
		}
		
		function disabledEl(el){
			var ex = {
				isEnable : false
			};
			updateEl(el, ex);
		}
		
		
		/**
		 * 删除对象相关的联系
		 */
		function deleteHopWithEl(el){
			var elId = el.id;
			$.each(canvasAllHops, function(k, v){
				if(v.fromEl == elId || v.toEl == elId){
					disabledEl(v);
				}
			});
		}
		
		/**
		 * 更新对象
		 */
		function updateEl(el, ex){
			var e = eval(el.path+'['+el.index+']');
			$.extend(e,ex);
		}
		
		function updateElTextById(elId){
			var text = $("#"+elId+"-generalLabel").attr("value");
			text = text.split(",");
			var el = getSelectedElById(elId,'step');
			if(el!=null){
				updateEl(el.el,{
					bText : text
				});
			}
		}
		
		/**
		 * 更新与对象连接的关联线
		 */
		function updateHopWithEl(el, newCoords){
			var elId = el.id;
			$.each(canvasAllHops, function(k, v){
				var isUpdate = false;
				var theX = v.x;
				var theY = v.y;
				var len = v.x.length;
				if(v.fromEl==elId){
					theX[0] = newCoords.x;
					theY[0] = newCoords.y;
					isUpdate = true;
				}
				
				if(v.toEl==elId){
					theX[len-1] = newCoords.x;
					theY[len-1] = newCoords.y;
					isUpdate = true;
				}
				if(isUpdate){
					updateEl(v, {
						x : theX,
						y : theY
					});
				}
			});
		}
		
		
		function updateElAndHop(c,el,o){
			updateEl(el,{
				dx : c.x + o.x - el.dWidth / 2,
				dy : c.y + o.y - el.dHeight / 2
			});
			updateHopWithEl(el,{
				x : c.x + o.x,
				y : c.y + o.y
			});
		}
		
		/**
		 * 增加一个被选定的对象的ID
		 */
		function appendSelectedEl(el,type){
			if(type == 'step'){
				selectedSteps.push(el);
			} else if(type == 'hop'){
				selectedHops.push(el);
			}
		}
		
		function cleanSelectedEl(){
			selectedSteps.length=0;
			selectedHops.length=0;
		}
		
		/**
		 * 从选定对象中删除
		 */
		function deleteFromSelectedElByIndex(index,type){
			if(type == 'step'){
				delete selectedSteps[index];
			} else if(type == 'hop'){
				delete selectedHops[index];
			}
		}
		
		/**
		 * 设置鼠标样式
		 */
		function setCanvasCursor(d){
			if(d){
				if ($.browser.msie){
					target.css("cursor","url('"+d+"')");
					otarget.css("cursor","url('"+d+"')");
				}else{
					target.css("cursor",null);
					otarget.css("cursor",null);
					target.attr("style",target.attr("style")+"cursor:url("+d+"),pointer;");
					otarget.attr("style",otarget.attr("style")+"cursor:url("+d+"),pointer;");
				}
			}else{
				target.css("cursor","auto");
				otarget.css("cursor","auto");
			}
			
		}
		
		/**
		 * 重新初始化
		 */
		function reinitContent(h,w){
			constructCanvas(h,w);
			redraw();
		}
		
		/**
		 * 初始化内容框和缩略图框
		 */
		function constructCanvas(h,w){
			canvasWidth = (w)?w:target.width();
        	canvasHeight = (h)?h:target.height();
        	oCanvasWidth = otarget.width();
        	overviewScale = oCanvasWidth / canvasWidth;
        	oCanvasHeight = Math.floor(canvasHeight * overviewScale);
        	
        	// 清空目标对象的内容
       		target.html("");
       		//设置目标对象为相对坐标
       	 	target.css("position", "relative");
       	 	
       	 	if (canvasWidth <= 0 || canvasHeight <= 0){
            	throw "目标对象不合法 width = " + canvasWidth + ", height = " + canvasHeight;
       	 	}

            // canvas
            canvas = $('<canvas width="' + canvasWidth + '" height="' + canvasHeight + '"></canvas>').appendTo(target).get(0);
            if ($.browser.msie){ // excanvas hack
                canvas = window.G_vmlCanvasManager.initElement(canvas);
            }
            ctx = canvas.getContext("2d");
            
            // overlay canvas for interactive features
            otarget.html("");
            otarget.css("position", "relative");
            otarget.css("height", oCanvasHeight+"px");
            overlay = $('<canvas width="' + oCanvasWidth + '" height="' + oCanvasHeight + '"></canvas>').appendTo(otarget).get(0);
            if ($.browser.msie){ // excanvas hack
                overlay = window.G_vmlCanvasManager.initElement(overlay);
            }
            octx = overlay.getContext("2d");
		}
		
		/**
		 * 创建JS控制的事件
		 */
		function constructTarget() {
            target.mousedown(function(e){
                if(jQuery.iPortalTab.activeTabId==null){return;}
            	if(!jQuery.imenu.iMenuBtns['editObject'].selected && !outerControl.isControl){return;}
            	// 保证是鼠标左键
            	if (e.which != 1) {return;}
            	// 是否外部控制
            	if(outerControl.isControl) {
            		var mouseCoords = getMouseCoords(e);
            		if(outerControl.controlType=='partMagnify'){
	            		pmRopeRect = {
	        				x : mouseCoords.x,
	        				y : mouseCoords.y,
	        				x2 : mouseCoords.x,
	        				y2 : mouseCoords.y
	        			};
            		}else if(outerControl.controlType=='screenMove'){
            			moveMousedown = {
            				x0 : canvasEls.x0,
            				y0 : canvasEls.y0,
            				x : mouseCoords.x,
            				y : mouseCoords.y
            			};
            		}
            		redraw();
            		return;
            	}
            	// 鼠标按下初始化
            	mousePress = true;
            	elMove = null;
            	mouseDownEl = null;
            	mouseUpEl = null;
            	pickupStepEl = null;
            	// 1.在被选中的对象上面按下鼠标
            	// selectedEl表示当前选定
            	var mouseCoords = getMouseCoords(e);
            	var sel = getSelectedEl(mouseCoords,'step');
            	if(joinBlock!=null){
            		joinBlock.clicked = true;
            		return;
            	}
            	if(sel!=null){
            		// 拿起选定
            		pickupStepEl = sel.el;
            		mouseDownEl = sel.el;
            	}else{
            		pickupStepEl = null;
            		var cel = getCanvasEl(mouseCoords,'step');
            		// 2.在未选中的对象上面按下鼠标
            		if(cel != null){
            			mouseDownEl = cel.el;
            		}
            		// 3.点击线
            		else {
            			var hel = getCanvasEl(mouseCoords, 'hop');
            		 	if(hel!=null){
            				mouseDownEl = hel.el;
	            		}
	            		// 4.在空白位置点击鼠标
	            		else{
	            			// 清空选定
	            			if(shiftFlag==null){
		            			cleanSelectedEl();
	            			}
	            			ropeRect = {
	            				x : mouseCoords.x,
	            				y : mouseCoords.y,
	            				x2 : mouseCoords.x,
	            				y2 : mouseCoords.y
	            			};
	            		}
            		}
            	}
            });
            
            target.mouseup(function(e){
            	if(jQuery.iPortalTab.activeTabId==null){return;}
            	if(!jQuery.imenu.iMenuBtns['editObject'].selected && !outerControl.isControl){return;}
            	// 保证是鼠标左键
            	if (e.which != 1){return;}
            	
            	var activeTab = jQuery.iPortalTab.getActiveTab();
				var objectType = activeTab.objectType;
            	
            	var mouseCoords = getMouseCoords(e);
            	
            	// 是否外部控制
            	if(outerControl.isControl) {
            		var isScale = true;
            		var actionType = jQuery.iPortalTab.TYPE_ACTION_NONE;
            		// 局部放大
	        		if(outerControl.controlType=='partMagnify'){
	        			var ns = arcScalePoint({
	        				x : pmRopeRect.x,
	        				y : pmRopeRect.y
	        			});
	        			var ne = arcScalePoint({
	        				x : pmRopeRect.x2,
	        				y : pmRopeRect.y2
	        			});
	            		isScale = setPartScale({
	            			x : ns.x,
	            			y : ns.y,
	            			x2 : ne.x,
	            			y2 : ne.y
	            		});
	            		actionType = jQuery.iPortalTab.TYPE_ACTION_PART_SCALE;
	        			pmRopeRect = null;
	        		}
	        		// 放大
	        		else if(outerControl.controlType=='magnify'){
	        			// 起点坐标
    					// x0' = x0 - (xm - x0) * PER_SCALE
    					// y0' = y0 - (ym - y0) * PER_SCALE
    					var tscale = canvasEls.scale*(1+PER_SCALE);
    					var tx0 = canvasEls.x0 - (mouseCoords.x - canvasEls.x0) * PER_SCALE;
    					var ty0 = canvasEls.y0 - (mouseCoords.y - canvasEls.y0) * PER_SCALE;
    					
    					isScale = setCanvasElScale(tscale,tx0,ty0);
    					actionType = jQuery.iPortalTab.TYPE_ACTION_MAGNIFY_SCALE;
	        		}
	        		// 缩小
	        		else if(outerControl.controlType=='lessen'){
	        			var sscale = canvasEls.scale/(1+PER_SCALE);
	        			var sx0 = (mouseCoords.x *PER_SCALE + canvasEls.x0) / (1+PER_SCALE);
    					var sy0 = (mouseCoords.y *PER_SCALE + canvasEls.y0) / (1+PER_SCALE);
    					
    					isScale = setCanvasElScale(sscale,sx0,sy0);
    					actionType = jQuery.iPortalTab.TYPE_ACTION_LESSEN_SCALE;
	        		}
	        		// 移动
	        		else if(outerControl.controlType=='screenMove'){
	        			if(moveMousedown!=null){
            				canvasEls.x0=moveMousedown.x0+mouseCoords.x-moveMousedown.x;
	            			canvasEls.y0=moveMousedown.y0+mouseCoords.y-moveMousedown.y;
            			}
	        			moveMousedown = null;
	        			actionType = jQuery.iPortalTab.TYPE_ACTION_SCREEN_MOVE;
	        		}
	        		redraw();
					if(isScale){
						// 更新tab--缩放
						//alert("缩放");
						if(actionType != jQuery.iPortalTab.TYPE_ACTION_NONE && jQuery.imenu.iMenuBtns['editObject'].selected){
							jQuery.iPortalTab.updateActiveTab(actionType);
						}
					}
            		return;
            	}
            	
            	// 选定对象
            	var cel = getCanvasEl(mouseCoords,'step');
            	// 对象存在
            	var type = '';
            	if(cel!=null){
            		mouseUpEl=cel.el;
            		type = 'step';
            	}
            	
            	// 拖动连线
            	if(pickupHopEl!=null){
            		// 如果在对象上面释放鼠标，则释放连线
            		if(mouseUpEl!=null){
            			// 终点是结束对象的中点
            			var elMpCoords = getElMidPoint(mouseUpEl);
            			var theX = pickupHopEl.x;
	            		var theY = pickupHopEl.y;
	            		var len = pickupHopEl.x.length;
	            		theX[len-1] = elMpCoords.x;
	            		theY[len-1] = elMpCoords.y;
            			
        				// 更新tab--画线
        				// 创建新的连线
						// 更新服务器
						$.ajax({
            		    	 type: "POST",
            		    	 url: "ImetaAction!newElement.action",
            				 dataType:"json",
            				 data: jQuery.cutil.objectToUrl({
            				 	roName : activeTab.objectName,
            		        	roType : activeTab.objectType,
            		        	directoryId : activeTab.directoryId,
            		        	fromEl : pickupHopEl.fromEl,
            		        	toEl : mouseUpEl.id,
            		        	toElWidth : mouseUpEl.dWidth,
    	            			toElHeight : mouseUpEl.dHeight,
            		        	element : "hop"
            				 }),
            				 success : function(json){
            				     var xs = theX;
            				     var ys = theY;
            				     var theO = {
    	            				x : xs,
    	            				y : ys
                				};
                				$.extend(theO,json);
            				    ﻿jQuery.imenu.iContent.updateEl(pickupHopEl,theO);
            				    // 更新客户端
                				if(objectType == jQuery.iPortalTab.OBJECT_TYPE_TRANS){
    								jQuery.iPortalTab.updateActiveTab(jQuery.iPortalTab.TYPE_ACTION_NEW_HOP);
                				}else if(objectType == jQuery.iPortalTab.OBJECT_TYPE_JOB){
    								jQuery.iPortalTab.updateActiveTab(jQuery.iPortalTab.TYPE_ACTION_NEW_JOB_HOP);
    							}
                				// 释放画线
                				pickupHopEl = null;
                				﻿jQuery.imenu.iContent.redraw();
            				 },
            				 error : globalError
            		    });
            		    
            		   
						
            		}
            		// 否则添加联系的新起点
            		else {
            			var theX = pickupHopEl.x;
	            		var theY = pickupHopEl.y;
	            		var len = pickupHopEl.x.length;
	            		var closeMouseCoords = mouseCoords;
	            		if(showGrid && closeGrid){
	            			closeMouseCoords = getCloseGridPoint(mouseCoords);
	            		}
	            		var ncm = arcScalePoint({
	            			x : closeMouseCoords.x,
	            			y : closeMouseCoords.y
	            		});
	            		theX[len-1] = ncm.x;
	            		theY[len-1] = ncm.y;
	            		theX.push(closeMouseCoords.x);
	            		theY.push(closeMouseCoords.y);
            			updateEl(pickupHopEl,{
            				x : theX,
            				y : theY
        				});
            		}
            	}else{
            		if(cel==null){
	            		var hel = getCanvasEl(mouseCoords,'hop');
	            		if(hel!=null){
	            			mouseUpEl=hel.el;
	            			type = 'hop';
	            		}
	            	}

					// 判断选定
	            	if(mouseUpEl!=null && mouseDownEl!=null && mouseUpEl.id==mouseDownEl.id){
	            		if(shiftFlag==null){
	            			cleanSelectedEl();
	            		}
	            		var sel = getSelectedElById(mouseUpEl.id,type);
	            		if(sel!=null){
	            			deleteFromSelectedElByIndex(sel.index,type);
	            		}else{
	            			appendSelectedEl(mouseUpEl,type);
	            		}
	            	}else if(ropeRect!=null){
	            		// 圈定对象
		            	getSelectedElsByRope(ropeRect,1);
	            	}else if(joinBlock!=null){
	            	}else {
	            		cleanSelectedEl();
	            	}
            	}
            	
            	if(elMove!=null){
            		//对象移动
					if(objectType == jQuery.iPortalTab.OBJECT_TYPE_TRANS){
						if(elMove.actionType=="step"){
							jQuery.iPortalTab.updateActiveTab(jQuery.iPortalTab.TYPE_ACTION_POSITION_STEP);
						}else if(elMove.actionType=="hop"){
							jQuery.iPortalTab.updateActiveTab(jQuery.iPortalTab.TYPE_ACTION_CHANGE_HOP);
						}
					}else if(objectType == jQuery.iPortalTab.OBJECT_TYPE_JOB){
						if(elMove.actionType=="step"){
							jQuery.iPortalTab.updateActiveTab(jQuery.iPortalTab.TYPE_ACTION_POSITION_JOB_ENTRY);
						}else if(elMove.actionType=="hop"){
							jQuery.iPortalTab.updateActiveTab(jQuery.iPortalTab.TYPE_ACTION_CHANGE_JOB_HOP);
						}
					}
            	}
            	
            	// 鼠标松开释放对象
            	pickupStepEl = null;
            	mouseDownEl = null;
            	mouseUpEl = null;
            	mousePress = false;
            	elMove = null;
            	joinBlock = null;
            	ropeRect = null;
            	pmRopeRect = null;
            	redraw();
            });
            
            target.mousemove(function(e){
            	if(jQuery.iPortalTab.activeTabId==null){return;}
            	if(!jQuery.imenu.iMenuBtns['editObject'].selected && !outerControl.isControl){return;}
            	// 保证是鼠标左键
            	if (e.which != 1) {return;}
            	
            	// 是否外部控制
            	if(outerControl.isControl) {
            		var mouseCoords = getMouseCoords(e);
            		if(outerControl.controlType=='partMagnify'){
	            		if(pmRopeRect!=null){
	            			pmRopeRect.x2 = mouseCoords.x;
	            			pmRopeRect.y2 = mouseCoords.y;
	            		}
            		}else if(outerControl.controlType=='screenMove'){
            			if(moveMousedown!=null){
            				canvasEls.x0=moveMousedown.x0+mouseCoords.x-moveMousedown.x;
	            			canvasEls.y0=moveMousedown.y0+mouseCoords.y-moveMousedown.y;
            			}
	            	}
            		redraw();
            		return;
            	}
            	
            	// 鼠标指针
            	var mouseCoords0 = getMouseCoords(e);
            	var mouseCoords = mouseCoords0;
            	
            	var sel = getSelectedEl(mouseCoords0,'step');
            	var cel = getCanvasEl(mouseCoords0,'step');
            	if(cel==null){
            		cel = getCanvasEl(mouseCoords0,'hop');
            	}
            	
            	// 移动的鼠标指针
            	if(shiftFlag!=null){
            		target.css("cursor","crosshair");
            	}else if(sel!=null){
            		target.css("cursor","move");
            	}else if(cel!=null){
            		target.css("cursor","pointer");
            	}else{
            		target.css("cursor","auto");
            	}
            	
            	if(sel==null){
            		sel = getSelectedEl(mouseCoords0,'hop');
            	}
            	
            	
            	// 判断是否接近
            	if(showGrid && closeGrid){
	            	mouseCoords = getCloseGridPoint(mouseCoords0);
	            }
            	
            	// 在shift没有按下时才能拖动，否则不能
            	if(shiftFlag==null){
            		// 拖动滑块
            		if(joinBlock!=null&&joinBlock.clicked){
	            		var theX = joinBlock.el.x;
	            		var theY = joinBlock.el.y;
	            		var nj = arcScalePoint({
	            			x : mouseCoords.x,
	            			y : mouseCoords.y
	            		});
	            		theX[joinBlock.index] = nj.x;
	            		theY[joinBlock.index] = nj.y;
            			updateEl(joinBlock.el,{
            				x : theX,
            				y : theY
            			});
            			elMove = {
            				actionType : "hop",
            				actionEl : {
            					x : theX,
            					y : theY
            				}
            			};
		            }
	            	// 拖动对象：在拿起选定对象（pickupStepEl!=null）时触发
	            	else if(pickupStepEl!=null){
	            		// 得到Step的中点
	            		var mp = getElMidPoint(pickupStepEl);
	            		
	            		// 循环选定的hop对象的中间位置
            			 $.each(selectedHops,function(k, v){
            			 	if(v!=undefined){
            			 		var theX = v.x;
								var theY = v.y;
								var len = v.x.length;
								var np;
            			 		for(var i=1;i<len-1;i++){
            			 			np = scalePoint({
            			 				x : theX[i],
            			 				y : theY[i]
            			 			});
            			 			np = arcScalePoint({
            			 				x : np.x-mp.x+mouseCoords.x,
            			 				y : np.y-mp.y+mouseCoords.y
            			 			});
            			 			theX[i] = np.x;
            			 			theY[i] = np.y;
            			 		}
            			 		updateEl(v, {
									x : theX,
									y : theY
								});
            			 	}
            			});
	            		
            			// 循环选定的step对象
            			$.each(selectedSteps,function(k, v){
            				if(v!=undefined){
            					// 转换成正常比例下的鼠标
            					var om = arcScalePoint({
            						x : mouseCoords.x,
            						y : mouseCoords.y
            					});
            					// 其他选定对象到拖动对象的距离
            					var offset = {
            						x : v.dx-pickupStepEl.dx,
            						y : v.dy-pickupStepEl.dy
            					};
            					updateElAndHop(om,v,offset);
            				}
            			});
            			
            			elMove = {
            				actionType : "step",
            				actionEl : selectedSteps
            			}
	            	} 
	            	// 拖动关联线：在拿起关联线时触发(pickupHopEl!=null)，这时pickupHopEl==fromEl，然后创建连接线
	            	else {
	            		// 新建画线
		            	if(mouseDownEl!=null && mouseDownEl.elType=='step' && pickupHopEl==null && cel==null){
		            		// 起点是开始对象的中点
	            			var elMpCoords = getElMidPoint(mouseDownEl);
	            			var newhopEl = {
								elType : 'hop',
								x : [elMpCoords.x,mouseCoords.x],
								y : [elMpCoords.y,mouseCoords.y],
								fromEl : mouseDownEl.id,
								fromElWidth : mouseDownEl.dWidth,
								fromElHeight : mouseDownEl.dHeight
							};
							var activeTab = jQuery.iPortalTab.getActiveTab();
							if(activeTab.objectType == jQuery.iPortalTab.OBJECT_TYPE_JOB){
								$.extend(newhopEl,{
									customType : 'success',
									style : 'green'
								});
							}
	            			pickupHopEl = appendEl(newhopEl,null);
		            	}
		            	// 拖动连线
		            	else if(pickupHopEl!=null){
		            		// 修改关联线位置
		            		var theX = pickupHopEl.x;
		            		var theY = pickupHopEl.y;
		            		var len = pickupHopEl.x.length;
		            		theX[len-1] = mouseCoords.x;
		            		theY[len-1] = mouseCoords.y;
	            			updateEl(pickupHopEl,{
	            				x : theX,
	            				y : theY
	            			});
		            	}
		            	// 拖动联系中部
		            	else if(sel!=null && sel.el.elType=='hop'){
		            		if(joinBlock==null||(joinBlock!=null&&!joinBlock.clicked)){
								var len = sel.el.x.length;
								var ns;
			            		for(var i=1;i<len-1;i++){
			            			ns = scalePoint({
			            				x : sel.el.x[i],
			            				y : sel.el.y[i]
			            			});
			            			if( Math.abs(ns.x-mouseCoords0.x)<=5 && Math.abs(ns.y-mouseCoords0.y)<=5 ){
			            				target.css("cursor","move");
		            					joinBlock={
		            						el : sel.el,
		            						index : i,
		            						clicked : false
		            					};
			            				break;
			            			}
			            			target.css("cursor","auto");
			            			joinBlock = null;
			            		}
		            		}
		            	}
		            	// 拖动圈选框
		            	else {
		            		if(ropeRect!=null){
		            			ropeRect.x2 = mouseCoords.x;
		            			ropeRect.y2 = mouseCoords.y;
		            		}
		            	}
	            	}
            	}else{
            		if(ropeRect!=null){
            			ropeRect.x2 = mouseCoords.x;
            			ropeRect.y2 = mouseCoords.y;
		            }
            	}
            	
            	// 重画
        		redraw();
            });
            
            otarget.mouseup(function(e){
            	if(jQuery.iPortalTab.activeTabId==null){return;}
            	if(!jQuery.imenu.iMenuBtns['editObject'].selected && !outerControl.isControl){return;}
            	// 保证是鼠标左键
            	if (e.which != 1){return;}
            	
            	// 是否外部控制
            	if(outerControl.isControl) {
            		var mouseCoords = getMouseCoords(e);
            		var isScale = true;
            		var actionType = jQuery.iPortalTab.TYPE_ACTION_NONE;
            		if(outerControl.controlType=='partMagnify'){
            			// 改变比率
            			if(opmRopeRect!=null){
            				var ns = arcScalePoint({
		        				x : Math.floor(opmRopeRect.x/overviewScale),
		        				y : Math.floor(opmRopeRect.y/overviewScale)
		        			});
		        			var ne = arcScalePoint({
		        				x : Math.floor(opmRopeRect.x2/overviewScale),
		        				y : Math.floor(opmRopeRect.y2/overviewScale)
		        			});
		            		isScale = setPartScale({
		            			x : ns.x,
		            			y : ns.y,
		            			x2 : ne.x,
		            			y2 : ne.y
		            		});
		        			opmRopeRect = null;
		        			actionType = jQuery.iPortalTab.TYPE_ACTION_PART_SCALE;
            			}
            		}else if(outerControl.controlType=='screenMove'){
            			if(omoveMousedown!=null){
            				canvasEls.x0=omoveMousedown.x0+(mouseCoords.x-omoveMousedown.x)/overviewScale;
	            			canvasEls.y0=omoveMousedown.y0+(mouseCoords.y-omoveMousedown.y)/overviewScale;
	            			omoveMousedown=null;
	            			actionType = jQuery.iPortalTab.TYPE_ACTION_SCREEN_MOVE;
            			}
            		}
            		redraw();
            		if(isScale){
	            		// 更新tab--缩放
						//alert("缩放");
						if(actionType != jQuery.iPortalTab.TYPE_ACTION_NONE && jQuery.imenu.iMenuBtns['editObject'].selected){
							jQuery.iPortalTab.updateActiveTab(actionType);
						}
            		}
            		return;
            	}
            	
            	// 鼠标指针
            	var mouseCoords = getMouseCoords(e);
            	
            	if(oropeRect!=null){
            		// 圈定对象
	            	getSelectedElsByRope(oropeRect,1/(overviewScale));
            	}
            	
            	oropeRect = null;
            	opmRopeRect=null;
            	redraw();
            });
            
            otarget.mousedown(function(e){
            	if(jQuery.iPortalTab.activeTabId==null){return;}
            	if(!jQuery.imenu.iMenuBtns['editObject'].selected && !outerControl.isControl){return;}
            	// 保证是鼠标左键
            	if (e.which != 1){return;}
            	
            	// 是否外部控制
            	if(outerControl.isControl) {
            		var mouseCoords = getMouseCoords(e);
            		if(outerControl.controlType=='partMagnify'){
	            		opmRopeRect = {
	        				x : mouseCoords.x,
	        				y : mouseCoords.y,
	        				x2 : mouseCoords.x,
	        				y2 : mouseCoords.y
	        			};
            		}else if(outerControl.controlType=='screenMove'){
            			omoveMousedown = {
            				x0 : canvasEls.x0,
            				y0 : canvasEls.y0,
            				x : mouseCoords.x,
            				y : mouseCoords.y
            			};
            		}
            		redraw();
            		return;
            	}
            	
            	// 鼠标指针
            	var mouseCoords = getMouseCoords(e);
            	
            	if(shiftFlag==null){
        			cleanSelectedEl();
    			}
    			oropeRect = {
    				x : mouseCoords.x,
    				y : mouseCoords.y,
    				x2 : mouseCoords.x,
    				y2 : mouseCoords.y
    			};
    			redraw();
            });
            
            otarget.mousemove(function(e){
            	if(jQuery.iPortalTab.activeTabId==null){return;}
            	if(!jQuery.imenu.iMenuBtns['editObject'].selected && !outerControl.isControl){return;}
            	// 保证是鼠标左键
            	if (e.which != 1){return;}
            	
            	// 是否外部控制
            	if(outerControl.isControl) {
            		var mouseCoords = getMouseCoords(e);
            		if(outerControl.controlType=='partMagnify'){
	            		if(opmRopeRect!=null){
	            			opmRopeRect.x2 = mouseCoords.x;
	            			opmRopeRect.y2 = mouseCoords.y;
	            		}
            		}else if(outerControl.controlType=='screenMove'){
            			if(omoveMousedown!=null){
            				canvasEls.x0=omoveMousedown.x0+(mouseCoords.x-omoveMousedown.x)/overviewScale;
	            			canvasEls.y0=omoveMousedown.y0+(mouseCoords.y-omoveMousedown.y)/overviewScale;
            			}
	            	}
	            	redraw();
            		return;
            	}
            	
            	if(shiftFlag!=null){
            		otarget.css("cursor","crosshair");
            	}else{
            		otarget.css("cursor","auto");
            	}
            	
            	// 鼠标指针
            	var mouseCoords = getMouseCoords(e);
            	
            	if(oropeRect!=null){
        			oropeRect.x2 = mouseCoords.x;
        			oropeRect.y2 = mouseCoords.y;
        		}
        		redraw();
            });
            
            /**
             * 鼠标右键事件
             */
            target.mouseup(function(e){
            	if(jQuery.iPortalTab.activeTabId==null){return;}
            	if(!jQuery.imenu.iMenuBtns['editObject'].selected){return;}
            	// 保证是鼠标右键
            	if (e.which != 3){return;}
            	
            	// 鼠标指针
            	var mouseCoords = getMouseCoords(e);
            	var elType = null;
            	cleanSelectedEl();
            	var cel = getCanvasEl(mouseCoords,'step');
            	if(cel==null){
            		cel = getCanvasEl(mouseCoords,'hop');
            		if(cel!=null){
            			appendSelectedEl(cel.el,'hop');
            			elType = 'hop';
            		}
            	}else{
            		appendSelectedEl(cel.el,'step');
            		elType = 'step';
            	}
            	
            	if(cel!=null){
            		var menu = null;
            		var activeTab = jQuery.iPortalTab.getActiveTab();
            		if(elType == 'step'){
            		    if(activeTab.objectType == jQuery.iPortalTab.OBJECT_TYPE_TRANS){
            		        var dataMoveType = cel.el.dataMoveType;
		            		menu = $('#portalContextMenu');
		            		$(menu).find('LI:not(.jobhop)').css("display","block");
	            			$(menu).find('LI.jobhop').css("display","none");
	            			$(menu).find('LI').removeClass("contextMenuSelected");
	            			$(menu).find('LI.data_'+dataMoveType).addClass("contextMenuSelected");
	            			$(menu).css('width','180px');
            		    }
            		}else if(elType == 'hop'){
            			if(activeTab.objectType == jQuery.iPortalTab.OBJECT_TYPE_JOB){
            				var customType = cel.el.customType;
            				menu = $('#portalContextMenu');
            				$(menu).find('LI:not(.jobhop)').css("display","none");
            				$(menu).find('LI.jobhop').css("display","block");
            				$(menu).find('LI').removeClass("contextMenuSelected");
            				$(menu).find('LI.evaluation_'+customType).addClass("contextMenuSelected");
            				$(menu).css('width','240px');
            			}
            		}
            		
            		if(menu==null){return;}
            		// 判断页面收放后的坐标，判断页面的最大高度和宽度
            		var mx = mouseCoords.x+10;
            		var my = mouseCoords.y+10;
            		if(!jQuery.imenu.customState.iovCollapse){
            		    mx = mx + 200;
            		}
            		if(!jQuery.imenu.customState.menuCollapse){
            		    my = my + 105;
            		}
            		$(menu).css({ top: my, left: mx }).fadeIn(150);
		            
	            	// Hover events
	            	$(menu).find('A').unbind('mouseover');
					$(menu).find('A').mouseover( function() {
						$(menu).find('LI.hover').removeClass('hover');
						$(this).parent().addClass('hover');
					}).mouseout( function() {
						$(menu).find('LI.hover').removeClass('hover');
					});
					
					// When items are selected
					$(menu).find('A').unbind('click');
					$(menu).find('LI:not(.disabled) A').click( function(me) {
						$(menu).hide();
						try{
							eval("jQuery.imeta.contextMenu."+$(this).attr('href').substr(1)+"(cel.el,mouseCoords)");
						}catch(ex){
							
						}
						return false;
					});
					
					$(menu).find('LI:not(.disabled) A').bind('contextmenu', function() { return false; });
					
					redraw();
					
            	}
            });
          
            target.mouseup(function(e){
            	if(jQuery.iPortalTab.activeTabId==null){return;}
            	if(!jQuery.imenu.iMenuBtns['editObject'].selected){return;}
            	// 保证是鼠标左键
            	if (e.which != 1){return;}
            	
            	// 鼠标指针
            	var mouseCoords = getMouseCoords(e);
            	var menu = $('#portalContextMenu');
            	$(menu).fadeOut(100);
            });
            
            target.bind('contextmenu', function() { return false; });
            
            if(!$.browser.msie){
	            target.dblclick(function(e){
	            	if(jQuery.iPortalTab.activeTabId==null){return;}
	            	if(!jQuery.imenu.iMenuBtns['editObject'].selected){return;}
	            	// 保证是鼠标左键
	            	if (e.which != 1){return;}
	            	if (outerControl.isControl){return;}
	            	
	            	var activeTab = jQuery.iPortalTab.getActiveTab();
					var objectType = activeTab.objectType;
					
	            	// 鼠标指针
	            	var mouseCoords = getMouseCoords(e);
	            	
	            	if(shiftFlag==null){
	            		// 双击step对象
	            		var cel = getCanvasEl(mouseCoords,'step');
	            		if(cel!=null){
	            			stepOpen(cel.el,mouseCoords);
	            			return;
	            		}
	            		
	            		// 双击选择的连线
	            		var sel = getSelectedEl(mouseCoords,'hop');
	            		if(sel!=null && sel.el.elType=='hop'){
	            			// 双击连线的连接块
	            			var len = sel.el.x.length;
	            			var theX = sel.el.x;
				            var theY = sel.el.y;
				            
				            var ns;
		            		for(var i=1;i<len-1;i++){
		            			ns = scalePoint({
		            				x : sel.el.x[i],
		            				y : sel.el.y[i]
				           		});
		            			if( Math.abs(ns.x-mouseCoords.x)<=5 && Math.abs(ns.y-mouseCoords.y)<=5 ){
				            		theX.splice(i,1);
				            		theY.splice(i,1);
			            			updateEl(sel.el,{
			            				x : theX,
			            				y : theY
			            			});
			            			// 重画
            						redraw();
            						// 更新tab--双击连线连接块
            						//alert("双击连线连接块");
            						if(objectType == jQuery.iPortalTab.OBJECT_TYPE_TRANS){
										jQuery.iPortalTab.updateActiveTab(jQuery.iPortalTab.TYPE_ACTION_CHANGE_HOP);
									}else if(objectType == jQuery.iPortalTab.OBJECT_TYPE_JOB){
										jQuery.iPortalTab.updateActiveTab(jQuery.iPortalTab.TYPE_ACTION_CHANGE_JOB_HOP);
									}
		            				return;
		            			}
		            		}
		            		// 双击连线的非连接块位置,添加一个连接块
		            		// 最近线的开始坐标
		            		var nli = sel.nearLineIndex;
		            		var nm = arcScalePoint({
		            			x : mouseCoords.x,
		            			y : mouseCoords.y
		            		});
		            		theX = [].concat(theX.slice(0,nli),nm.x,theX.slice(nli));
		            		theY = [].concat(theY.slice(0,nli),nm.y,theY.slice(nli));
		            		updateEl(sel.el,{
	            				x : theX,
	            				y : theY
	            			});
	            			// 重画
            				redraw();
            				// 更新tab--双击空白连线
            				//alert("双击空白连线");
            				if(objectType == jQuery.iPortalTab.OBJECT_TYPE_TRANS){
								jQuery.iPortalTab.updateActiveTab(jQuery.iPortalTab.TYPE_ACTION_CHANGE_HOP);
							}else if(objectType == jQuery.iPortalTab.OBJECT_TYPE_JOB){
								jQuery.iPortalTab.updateActiveTab(jQuery.iPortalTab.TYPE_ACTION_CHANGE_JOB_HOP);
							}
            				return;
	            		}
	            	}
	            });
            }
            
            //快捷键删除
            $(document).bind('keydown', 'del',
            	function (e){
            		if(jQuery.iPortalTab.activeTabId==null){return;}
            		deleteSelectedEl();
            	});
            	
           	$(document).keydown(function(e){
           		if(jQuery.iPortalTab.activeTabId==null){return;}
           		if(!jQuery.imenu.iMenuBtns['editObject'].selected){return;}
           		if (outerControl.isControl){return;}
           		// 按下shift键
           		if (e.which == 16){
           			shiftFlag = target.attr("style");
            		target.css("cursor","crosshair");
            		otarget.css("cursor","crosshair");
           		}
           	});
           	
           	$(document).keyup(function(e){
           		if(jQuery.iPortalTab.activeTabId==null){return;}
           		if(!jQuery.imenu.iMenuBtns['editObject'].selected){return;}
           		if (outerControl.isControl){return;}
           		// 按下shift键
           		if (e.which == 16){
           			target.css("cursor","auto");
           			otarget.css("cursor","auto");
            		shiftFlag = null;
           		}
           	});
           	
		}
		
		/**
		 * 打开step
		 */
		function stepOpen(el,mc){
			jQuery.imeta.contextMenu.edit(el,mc);
		}
        
        /**
         * 得到鼠标的坐标
         */
        function getMouseCoords(e) {
			return {
				x : e.originalEvent.x||e.originalEvent.layerX||0, 
				y : e.originalEvent.y||e.originalEvent.layerY||0
			};
        }
        
        /**
         * 得到对象的中点坐标
         */
        function getElMidPoint(el) {
        	var nel = scalePoint({
        		x : el.dx,
        		y : el.dy
        	});
        	return {
        		x : nel.x + scaleLength(el.dWidth) / 2,
            	y : nel.y + scaleLength(el.dHeight) / 2
        	};
        }
        
        /**
         * 得到接近的网格
         */
        function getCloseGridPoint(el){
        	var distance = Math.floor(gridPoints.initDistance*canvasEls.scale);
        	
        	var tx0=canvasEls.x0,ty0=canvasEls.y0;
				
			while(tx0>0)tx0-=distance;
			while(ty0>0)ty0-=distance;
        	
        	var xIndex = Math.floor((el.x - tx0)/distance + 0.5);
        	var yIndex = Math.floor((el.y - ty0)/distance + 0.5);
        	xIndex=(xIndex<0)?0:xIndex;
        	yIndex=(yIndex<0)?0:yIndex;
        	
        	var rt = gridPoints.points[xIndex];
        	return rt[yIndex];
        }
        
        function getCanvasElByText(text,type){
        	var el;
        	if(type == 'step'){
        		for(var i=canvasAllSteps.length-1;i>=0;i--){
        			el = canvasAllSteps[i].bText;
        			if(el){
	        			for(var j=0;j<el.length;j++){
	        				if(el[j] == text){
	        					return canvasAllSteps[i];
	        				}
	        			}
        			}
        		}
        	}else if(type == 'hop'){
        		for(var i=canvasAllHops.length-1;i>=0;i--){
        			el = canvasAllHops[i].text;
        			if(el){
	        			for(var j=0;j<el.length;j++){
	        				if(el[j] == text){
	        					return canvasAllHops[i];
	        				}
	        			}
        			}
        		}
        	}
        	return null;
        }
        		
		/**
		 * 得到对象，通过坐标对象
		 */
		function getCanvasEl(c,type){
			if(type == 'step'){
				var x,y,x2,y2;
				for(var i=canvasAllSteps.length-1;i>=0;i--){
					var nel = scalePoint({
		        		x : canvasAllSteps[i].dx,
		        		y : canvasAllSteps[i].dy
		        	});
					x = nel.x;
					y = nel.y;
					x2 = x + scaleLength(canvasAllSteps[i].dWidth);
					y2 = y + scaleLength(canvasAllSteps[i].dHeight);
					if ( (x<=c.x) && (c.x<=x2) && (y<=c.y) && (c.y<=y2) ) {
						return {
							el : canvasAllSteps[i],
							index : i
						};
					}
				}
			}else if(type == 'hop'){
				for(var i=canvasAllHops.length-1;i>=0;i--){
					var hop = canvasAllHops[i];
					// 线条对象坐标集
					// 线条的起始和结束节点都是从实体中取得的，是实际值
					var x = hop.x;
					var y = hop.y;
					var len = y.length;
					var d;
					var np,np2;
					if(hop.fromEl==hop.toEl && len<=2){
						var r = scaleLength(20);
						d = getDistancePointToCircle(c,{
							x : x[0],
							y : y[0]-r
						},r);
						if(d<5){
							return {
								el : hop,
								index : i,
								nearLineIndex : 1
							};
						}
					}else {
						for(var j=1;j<len;j++){
							// 线条的起始和结束节点都是从实体中取得的，是实际值
							if((j-1)==0){
								np = {
									x : x[j-1],
									y : y[j-1]
								};
							}else{
								np = scalePoint({
									x : x[j-1],
									y : y[j-1]
								});
							}
							
							if(j==(len-1)){
								np2 = {
									x : x[j],
									y : y[j]
								};
							}else{
								np2 = scalePoint({
									x : x[j],
									y : y[j]
								});
							}
							if( 
								(((np.x-5<=c.x) && (c.x<=np2.x+5)) ||
								 ((np2.x-5<=c.x) && (c.x<=np.x+5))) &&
								(((np.y-5<=c.y) && (c.y<=np2.y+5)) ||
								 ((np2.y-5<=c.y) && (c.y<=np.y+5)))
							  ){
								d = getDistancePointToLine(c,{
									x : np.x,
									y : np.y,
									x2 : np2.x,
									y2 : np2.y
								});
								if(d<10){
									return {
										el : hop,
										index : i,
										nearLineIndex : j
									};
								}
							}
						}
					}
				}
			}
			return null;
		}
		
		/**
		 * 点到线的距离
		 * @param c : { x:x轴坐标, y:y轴坐标 }
		 * 		点
		 * @param line : { x:起点x轴坐标, y:起点y轴坐标, x2:终点x轴坐标, y2:终点y轴坐标 }
		 * 		线
		 * @return number
		 * 		点到线的距离
		 */
		function getDistancePointToLine(c, line){
			// 两个坐标直线公式
			// y = (y2-y1)*(x-x1)/(x2-x1)+y1
			// d = y - c.y
			var d;
			if(Math.abs(line.x-line.x2)<10){
				d=Math.abs(c.x-line.x);
			}else{
				d=Math.abs(Math.floor(
					(line.y2-line.y) * (c.x-line.x) / (line.x2-line.x) +line.y -c.y
				));
			}
			return d;
		}
		
		/**
		 * 点到圆的距离
		 * @param p : { x:x轴坐标, y:y轴坐标 }
		 * 		点
		 * @param c : { x:圆心x坐标, y:圆心y坐标 }
		 * 		圆心
		 * @param r : 半径
		 * @return number
		 * 		点到线的距离
		 */
		function getDistancePointToCircle(p,c,r){
			// 点到圆心距离
			var D;
			D = Math.floor(
				Math.sqrt( Math.pow((p.x-c.x),2) + Math.pow((p.y-c.y),2))
			);
			return Math.abs(D-r);
		}
		
		/**
		 * 通过ID得到Canvas对象
		 */
		function getCanvasAllElById(id,type){
			var rtnEl = null
			var rtnIndex = null;
			if(type=='step'){
				$.each(canvasAllSteps, function(k, v){
					if(v.id == id){
						rtnEl = v;
						rtnIndex = k;
						return;
					}
				});
			}else if(type == 'hop'){
				$.each(canvasAllHops, function(k, v){
					if(v.id == id){
						rtnEl = v;
						rtnIndex = k;
						return;
					}
				});
			}
			if(rtnEl!=null){
				return {
					el : rtnEl,
					index : rtnIndex
				};
			}
			return null;
		}
		
		/**
		 * 通过坐标得到选定的对象
		 */
		function getSelectedEl(c,type){
			if(type=='step'){
				var no;
				for(var i=selectedSteps.length-1;i>=0;i--){
					var o = selectedSteps[i];
					if (o!=undefined){
						no = scalePoint({
							x : o.dx,
							y : o.dy
						});
						if ( ( (no.x-3)<=c.x) && (c.x<=(no.x+scaleLength(o.dWidth)+3) ) 
								&& ( (no.y-3)<=c.y) && (c.y<=(no.y+scaleLength(o.dHeight)+3) ) ){
							return {
								el : o,
								index : i
							};
						}
					}
				}
			}else if(type == 'hop'){
				for(var i=selectedHops.length-1;i>=0;i--){
					var hop = selectedHops[i];
					if(hop!=undefined){
						var x = hop.x;
						var y = hop.y;
						var len = y.length;
						var d;
						var np,np2,r;
						if(hop.fromEl==hop.toEl && len<=2){
							r = scaleLength(20);
							d = getDistancePointToCircle(c,{
								x : x[0],
								y : y[0]-r
							},r);
							if(d<5){
								return {
									el : hop,
									index : i,
									nearLineIndex : 1
								};
							}
						}else {
							for(var j=1;j<len;j++){
								if((j-1)==0){
									np = {
										x : x[j-1],
										y : y[j-1]
									};
								}else{
									np = scalePoint({
										x : x[j-1],
										y : y[j-1]
									});
								}
								
								if(j==(len-1)){
									np2 = {
										x : x[j],
										y : y[j]
									};
								}else{
									np2 = scalePoint({
										x : x[j],
										y : y[j]
									});
								}
								if( 
									(((np.x-5<=c.x) && (c.x<=np2.x+5)) ||
									 ((np2.x-5<=c.x) && (c.x<=np.x+5))) &&
									(((np.y-5<=c.y) && (c.y<=np2.y+5)) ||
									 ((np2.y-5<=c.y) && (c.y<=np.y+5)))
								  ){
									
									d = getDistancePointToLine(c,{
										x : np.x,
										y : np.y,
										x2 : np2.x,
										y2 : np2.y
									});
									if(d<10){
										return {
											el : hop,
											index : i,
											nearLineIndex : j
										};
									}
								}
							}
						}
					}
				}
			}
			return null;
		}
		
		/**
		 * 通过ID得到Selected对象
		 */
		function getSelectedElById(id,type){
			var rtnEl = null
			var rtnIndex = null;
			var s = null;
			if(type=='step'){
				$.each(selectedSteps, function(k, v){
					if(v != undefined){
						if(v.id == id){
							rtnEl = v;
							rtnIndex = k;
							return;
						}
					}
				});
			}else if(type=='hop'){
				$.each(selectedHops, function(k, v){
					if(v != undefined){
						if(v.id == id){
							rtnEl = v;
							rtnIndex = k;
							return;
						}
					}
				});
			}
			if(rtnEl!=null){
				return {
					el : rtnEl,
					index : rtnIndex
				};
			}
			return null;
		}
		
		/**
		 * 得到圈选框中对象
		 */
		function getSelectedElsByRope(rope,s){
			if(rope!=null){
				// 得到圈选框起始点坐标
				var x = rope.x*s;
				var x2 = rope.x2*s;
				var y = rope.y*s;
				var y2 = rope.y2*s;
				if(x>x2){
					x=x+x2;
					x2=x-x2;
					x=x-x2;
				}
				if(y>y2){
					y=y+y2;
					y2=y-y2;
					y=y-y2;
				}
				// 遍历steps
				var nc,ndWidth,ndHeight;
				$.each(canvasAllSteps, function(k, v){
					nc = scalePoint({
						x : v.dx,
						y : v.dy
					});
					ndWidth = scaleLength(v.dWidth);
					ndHeight = scaleLength(v.dHeight);
					if( ( ((x<=nc.x) && ((nc.x+ndWidth/2)<=x2)) || ((x<=nc.x+ndWidth/2) && ((nc.x+ndWidth)<=x2)) )
						&& ( ((y<=nc.y) && ((nc.y+ndHeight/2)<=y2)) || ((y<=nc.y+ndHeight/2) && ((nc.y+ndHeight)<=y2)) ) ){
						// 未选择的对象
						if(getSelectedElById(v.id,'step')==null){
							appendSelectedEl(v,'step');
						}
					}
				});
				// 遍历hops
				$.each(canvasAllHops, function(k, v){
					var max = getLineMaxCoords(v);
					if( (x<=max.x) && (max.x2<=x2) && (y<=max.y) && (max.y2<=y2) ){
						// 未选择的线条
						if(getSelectedElById(v.id,'hop')==null){
							appendSelectedEl(v,'hop');
						}
					}
				});
			}
		}
		
		/**
		 * 得到线条的最大坐标
		 */
		function getLineMaxCoords(el){
			var np;
			var x=el.x[0],x2=el.x[0],y=el.y[0],y2=el.y[0];
			var len = el.x.length;
			for(var i=1;i<len-1;i++){
				np = scalePoint({
					x : el.x[i],
					y : el.y[i]
				});
				x=(x<np.x)?x:np.x;
				y=(y<np.y)?y:np.y;
				x2=(x2>np.x)?x2:np.x;
				y2=(y2>np.y)?y2:np.y;
			}
			x=(x<el.x[len-1])?x:el.x[len-1];
			y=(y<el.y[len-1])?y:el.y[len-1];
			x2=(x2>el.x[len-1])?x2:el.x[len-1];
			y2=(y2>el.y[len-1])?y2:el.y[len-1];
			return {
				x : x,
				y : y,
				x2 : x2,
				y2 : y2
			}
		}
		
		/**
		 * 得到线条的缩短坐标
		 */
		function getLineShortedCoords(dWidth,dHeight,line){
			var dx = line.x2-line.x;
			var dy = line.y2-line.y;
			var buffer = 5;
			var x,y;
			var X,Y;
			// 竖线
			if(Math.abs(dx)<5){
				X=line.x;
				Y=line.y;
				if(dy<0){
					x = X;
					y = Y - (dHeight/2 + buffer);
				}else{
					x = X;
					y = Y + (dHeight/2 + buffer);
				}
			}else{
				var slope = dy/dx;
			
				X=line.x;
				Y=line.y;
				if(-1<=slope && slope<=1){
					//1:(-PI/4~PI/4)
					if(dx>0){
						x = X + (dWidth/2 + buffer);
					}
					//3:(PI*3/4~PI*5/4)
					else{
						x = X - (dWidth/2 + buffer);
					}
					y = Math.floor( slope * (x-X) + Y );
					x = Math.floor(x);
				}
				
				else if(slope<-1 || slope>1){
					//2:(PI/4~PI*3/4)
					if(dy<0){
						y = line.y - (dHeight/2 + buffer);
					}
					//4:(PI*5/4~PI*7/4)
					else{
						y = line.y + (dHeight/2 + buffer);
					}
					x = Math.floor( (y-Y) / slope + X );
					y = Math.floor(y);
				}
			}
			return {
				x : x,
				y : y
			};
		}
		
	}

	/**
	 * 调用返回icontent对象
	 */
    $.canvasObj = function(target, options) {
    	var canvasObj = new CanvasObj(target, options);
   		return canvasObj;
	};
    
})(jQuery);

$.canvasObj.defaults = {
    steps : [],
    hops : [],
    groups : [],
    scale : 1,
    x0 : 0,
    y0 : 0,
    editable : true,
    textColor : 'Red',
    gridColor : "#dddddd"
};

$.canvasObj.stepObjDefaults = {
    id : '',
    imgId : '',
    dx : -1,
    dy : -1,
    sx : 0,
    sy : 0,
    sWidth : 32, 
	sHeight : 32,
	dWidth : 32, 
	dHeight : 32,
    elType : 'entry',
    isNew : false,
    isUpdate : false,
    isDisplay : true,
    ltText : [],
    rtText : [],
    lbText : [],
    rbText : [],
    tText : [],
    bText : [],
    lText : [],
    rText : []
};

$.canvasObj.hopObjDefaults = {
};