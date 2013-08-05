Ext.namespace("panet");
Ext.namespace("panet.graph");

/**
 * circle构造
 * @param {} id
 */
panet.graph.circle = function (id) {
	var elm = document.getElementById(id);
	if(! elm) { return; }
	if(elm.nodeName != "CANVAS") { return; }
	if(elm.parentNode.nodeName != "DIV") { return; };
	this.canvas = elm;
	// CANVAS对象
	if ( ! this.canvas ){ return; }
	if ( ! this.canvas.getContext ){ return; }
	// 生成2D背景
	this.ctx = this.canvas.getContext('2d');
	this.canvas.style.margin = "0";
	this.canvas.parentNode.style.position = "relative";
	this.canvas.parentNode.style.padding = "0";
};

/**
 * circle绘图
 * @param {} items
 * @param {} inparams
 */
panet.graph.circle.prototype.draw = function(items, inparams) {
	if( ! this.ctx ) {return;}
	// 初始化参数
	var params = {
		backgroundColor: null,
		shadow: true,
		border: false,
		caption: false,
		captionNum: true,
		captionRate: true,
		fontSize: "12px",
		fontFamily: "Arial,sans-serif",
		textShadow: true,
		captionColor: "#ffffff",
		startAngle: -90,
		legend: true,
		legendFontSize: "12px",
		legendFontFamily: "Arial,sans-serif",
		legendColor: "#000000",
		otherCaption: "other"
	};
	if( inparams && typeof(inparams) == 'object' ) {
		for( var key in inparams ) {
			params[key] = inparams[key];
		}
	}
	this.params = params;
	// CANVAS的背景填充
	if( params.backgroundColor ) {
		this.ctx.beginPath();
		this.ctx.fillStyle = params.backgroundColor;
		this.ctx.fillRect(0, 0, this.canvas.width, this.canvas.height);
	}
	// 如果CANVAS对象的“宽/高<1.5”，或者“高<200”，legend（示例）参数强制为false
	if(this.canvas.width / this.canvas.height < 1.5 || this.canvas.height < 200) {
		params.legend == false;
	}
	// CANVAS对象的坐标
	var canvas_pos = this._getElementAbsPos(this.canvas);
	// 饼图的中心坐标和半径
	var cpos = {
		x: this.canvas.width / 2,
		y: this.canvas.height / 2,
		r: Math.min(this.canvas.width, this.canvas.height) * 0.8 / 2
	};
	// 如果超过10个分类，10个分类以后的分类将综合成“其他”
	items = this._fold_items(items);
	var item_num = items.length;
	// 如果legend（示例）显示，则计算出来每个示例的坐标
	if(params.legend == true) {
		// 饼图中心x坐标
		cpos.x = this.canvas.height * 0.1 + cpos.r;
		// 插入一个测试的DIV（1行的高度）
		var tmpdiv = document.createElement('DIV');
		tmpdiv.appendChild( document.createTextNode('TEST') );
		tmpdiv.style.fontSize = params.legendFontSize;
		tmpdiv.style.fontFamily = params.legendFontFamily;
		tmpdiv.style.color = params.legendColor;
		tmpdiv.style.margin = "0";
		tmpdiv.style.padding = "0";
		tmpdiv.style.visible = "hidden";
		tmpdiv.style.position = "absolute";
		tmpdiv.style.left = canvas_pos.x.toString() + "px";
		tmpdiv.style.top = canvas_pos.y.toString() + "px";
		this.canvas.parentNode.appendChild(tmpdiv);
		// 计算示例的各种坐标属性
		var lpos = {
			x: this.canvas.height * 0.2 + cpos.r * 2,
			y: ( this.canvas.height - ( tmpdiv.offsetHeight * item_num + tmpdiv.offsetHeight * 0.2 * (item_num - 1) ) ) / 2,
			h: tmpdiv.offsetHeight
		};
		lpos.cx = lpos.x + lpos.h * 1.4; // 文字起始位置（x坐标）
		lpos.cw = this.canvas.width - lpos.cx;       // 文字的宽度
		// 删除测试DIV
		tmpdiv.parentNode.removeChild(tmpdiv);
	}
	// 图像的外阴影
	if( params.shadow == true ) {
		this._make_shadow(cpos);
	}
	// 外阴影的黑色填充
	this.ctx.beginPath();
	this.ctx.arc(cpos.x, cpos.y, cpos.r, 0, Math.PI*2, false)
	this.ctx.closePath();
	this.ctx.fillStyle = "black";
	this.ctx.fill();
	// 计算全部分类数值的合计
	var sum = 0;
	for(var i=0; i<item_num; i++) {
		var n = items[i][1];
		if( isNaN(n) || n <=0 ) {
			throw new Error('invalid graph item data.' + n);
		}
		sum += n;
	}
	if(sum <= 0) {
		throw new Error('invalid graph item data.');
	}
	// 定义每个分类的默认颜色（10个）
	var colors = ["24,41,206", "198,0,148", "214,0,0", "255,156,0", "33,156,0", "33,41,107", "115,0,90", "132,0,0", "165,99,0", "24,123,0"];
	// 绘制一个饼图
	var rates = new Array();
	var startAngle = this._degree2radian(params.startAngle);
	for(var i=0; i<item_num; i++) {
		// 分类的名称
		var cap = items[i][0];
		// 分类的数值
		var n = items[i][1];
		// 分类的比例
		var r = n / sum;
		// 分类的百分比
		var p = Math.round(r * 1000) / 10;
		// 分类绘图所占的角度值
		var rad = this._degree2radian(360*r);
		// 分类绘图结束的角度值
		endAngle = startAngle + rad;
		// 绘制分类的边界线
		this.ctx.beginPath();
		this.ctx.moveTo(cpos.x,cpos.y);
		this.ctx.lineTo(
			cpos.x + cpos.r * Math.cos(startAngle),
			cpos.y + cpos.r * Math.sin(startAngle)
		);
		this.ctx.arc(cpos.x, cpos.y, cpos.r, startAngle, endAngle, false);
		this.ctx.closePath();
		// 设定分类的颜色
		var rgb = colors[i];
		var rgbo = this._csscolor2rgb(items[i][2]);
		if(rgbo) {
			rgb = rgbo.r + "," + rgbo.g + "," + rgbo.b;
		}
		// 设置饼图的梯度
		var radgrad = this.ctx.createRadialGradient(cpos.x,cpos.y,0,cpos.x,cpos.y,cpos.r);
		radgrad.addColorStop(0, "rgba(" + rgb + ",1)");
		radgrad.addColorStop(0.7, "rgba(" + rgb + ",0.85)");
		radgrad.addColorStop(1, "rgba(" + rgb + ",0.75)");
		this.ctx.fillStyle = radgrad;
		this.ctx.fill();
		// 绘制饼图的弧线
		if(params.border == true) {
			this.ctx.stroke();
		}
		// 饼图分类的标题
		var drate;
		var fontSize;
		if(r <= 0.03) {
			drate = 1.1;
		} else if(r <= 0.05) {
			drate = 0.9;
		} else if(r <= 0.1) {
			drate = 0.8;
		} else if(r <= 0.15) {
			drate = 0.7;
		} else {
			drate = 0.6;
		}
		var cp = {
			x: cpos.x + (cpos.r * drate) * Math.cos( (startAngle + endAngle) / 2 ),
			y: cpos.y + (cpos.r * drate) * Math.sin( (startAngle + endAngle) / 2 )
		};
		var div = document.createElement('DIV');
		if(r <= 0.05) {
			if(params.captionRate == true) {
				div.appendChild( document.createTextNode( p + "%") );
			}
		} else {
			if(params.caption == true) {
				div.appendChild( document.createTextNode(cap) );
				if(params.captionNum == true || params.captionRate == true) {
					div.appendChild( document.createElement('BR') );
				}
			}
			if(params.captionRate == true) {
				div.appendChild( document.createTextNode( p + "%" ) );
			}
			if(params.captionNum == true) {
				var numCap = n;
				if(params.caption == true || params.captionRate == true) {
					numCap = "(" + numCap + ")";
				}
				div.appendChild( document.createTextNode( numCap ) );
			}
		}
		div.style.position = 'absolute';
		div.style.textAlign = 'center';
		div.style.color = params.captionColor;
		div.style.fontSize = params.fontSize;
		div.style.fontFamily = params.fontFamily;
		div.style.margin = "0";
		div.style.padding = "0";
		div.style.visible = "hidden";
		if( params.textShadow == true ) {
			var dif = [ [ 0,  1], [ 0, -1], [ 1,  0], [ 1,  1], [ 1, -1], [-1,  0], [-1,  1], [-1, -1] ];
			for(var j=0; j<8; j++) {
				var s = div.cloneNode(true);
				this.canvas.parentNode.appendChild(s);
				s.style.color = "black";
				s.style.left = (parseFloat(cp.x - s.offsetWidth / 2 + dif[j][0])).toString() + "px";
				s.style.top = (cp.y - s.offsetHeight / 2 + dif[j][1]).toString() + "px";
			}
		}
		this.canvas.parentNode.appendChild(div);
		div.style.left = (cp.x - div.offsetWidth / 2).toString() + "px";
		div.style.top = (cp.y - div.offsetHeight / 2).toString() + "px";
		// 示例
		if(params.legend == true) {
			// 示例文字
			var ltxt = document.createElement('DIV');
			ltxt.appendChild( document.createTextNode(cap) );
			ltxt.style.position = "absolute";
			ltxt.style.fontSize = params.legendFontSize;
			ltxt.style.fontFamily = params.legendFontFamily;
			ltxt.style.color = params.legendColor;
			ltxt.style.margin = "0";
			ltxt.style.padding = "0";
			ltxt.style.left = lpos.cx.toString() + "px";
			ltxt.style.top = lpos.y.toString() + "px";
			ltxt.style.width = (lpos.cw).toString() + "px";
			ltxt.style.whiteSpace = "nowrap";
			ltxt.style.overflow = "hidden";
			this.canvas.parentNode.appendChild(ltxt);
			// 示例图形
			if( params.shadow == true ) {
				this.ctx.beginPath();
				this.ctx.rect(lpos.x+1, lpos.y+1, lpos.h, lpos.h);
				this.ctx.fillStyle = "#222222";
				this.ctx.fill();
			}
			// 符号
			this.ctx.beginPath();
			this.ctx.rect(lpos.x, lpos.y, lpos.h, lpos.h);
			this.ctx.fillStyle = "black";
			this.ctx.fill();
			this.ctx.beginPath();
			this.ctx.rect(lpos.x, lpos.y, lpos.h, lpos.h);
			var symbolr = {
				x: lpos.x + lpos.h / 2,
				y: lpos.y + lpos.h / 2,
				r: Math.sqrt(lpos.h * lpos.h * 2) / 2
			};
			var legend_radgrad = this.ctx.createRadialGradient(symbolr.x,symbolr.y,0,symbolr.x,symbolr.y,symbolr.r);
			legend_radgrad.addColorStop(0, "rgba(" + rgb + ",1)");
			legend_radgrad.addColorStop(0.7, "rgba(" + rgb + ",0.85)");
			legend_radgrad.addColorStop(1, "rgba(" + rgb + ",0.75)");
			this.ctx.fillStyle = legend_radgrad;
			this.ctx.fill();
			//
			lpos.y = lpos.y + lpos.h * 1.2;
		}
		//
		startAngle = endAngle;
	}
};

/* ━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━
* 以下的是内部方法
* ──────────────────────────────── */

/**
 * 将分类归在10个以内
 * @param {} items
 * @return {}
 */
panet.graph.circle.prototype._fold_items = function(items) {
	var len = items.length;
	if(len <= 10) { return items; }
	var n = 0;
	for( var i=9; i<len; i++ ) {
		n += items[i][1];
	}
	var newitems = items.slice(0, 10);
	newitems[9] = [this.params.otherCaption, n];
	return newitems;
};

/**
 * 根据elm的左上角坐标，得到在浏览器显示区域左上角的绝对坐标
 * @param {} elm
 * @return {}
 */
panet.graph.circle.prototype._getElementAbsPos = function(elm) {
	var obj = new Object();
	obj.x = elm.offsetLeft;
	obj.y = elm.offsetTop;
	while(elm.offsetParent) {
		elm = elm.offsetParent;
		obj.x += elm.offsetLeft;
		obj.y += elm.offsetTop;
	}
	return obj;
};

/**
 * 生成阴影
 * @param {} cpos
 */
panet.graph.circle.prototype._make_shadow = function (cpos) {
	this.ctx.beginPath();
	this.ctx.arc(cpos.x+5, cpos.y+5, cpos.r, 0, Math.PI*2, false)
	this.ctx.closePath();
	var radgrad = this.ctx.createRadialGradient(cpos.x+5,cpos.y+5,0,cpos.x+5,cpos.y+5,cpos.r);
	if(document.uniqueID) {
		radgrad.addColorStop(0, '#555555');
	} else {
		radgrad.addColorStop(0, 'rgba(0,0,0,1)');
		radgrad.addColorStop(0.93, 'rgba(0,0,0,1)');
		radgrad.addColorStop(1, 'rgba(0,0,0,0)');
	}
	this.ctx.fillStyle = radgrad;
	this.ctx.fill();
};

/**
 * 角度转弧度
 * @param {} degree
 * @return {}
 */
panet.graph.circle.prototype._degree2radian = function (degree) {
	return (Math.PI/180) * degree;
};

/**
 * CSS颜色文字转颜色RGB值
 * @param {} c
 * @return {}
 */
panet.graph.circle.prototype._csscolor2rgb = function (c) {
	if( ! c ) { return null; }
	var color_map = {
		black: "#000000",
		gray: "#808080",
		silver: "#c0c0c0",
		white: "#ffffff",
		maroon: "#800000",
		red: "#ff0000",
		purple: "#800080",
		fuchsia: "#ff00ff",
		green: "#008000",
		lime: "#00FF00",
		olive: "#808000",
		yellow: "#FFFF00",
		navy: "#000080",
		blue: "#0000FF",
		teal: "#008080",
		aqua: "#00FFFF"
	};
	c = c.toLowerCase();
	var o = new Object();
	if( c.match(/^[a-zA-Z]+$/) && color_map[c] ) {
		c = color_map[c];
	}
	var m = null;
	if( m = c.match(/^\#([a-f\d]{2})([a-f\d]{2})([a-f\d]{2})$/i) ) {
		o.r = parseInt(m[1], 16);
		o.g = parseInt(m[2], 16);
		o.b = parseInt(m[3], 16);
	} else if( m = c.match(/^\#([a-f\d]{1})([a-f\d]{1})([a-f\d]{1})$/i) ) {
		o.r = parseInt(m[1]+"0", 16);
		o.g = parseInt(m[2]+"0", 16);
		o.b = parseInt(m[3]+"0", 16);
	} else if( m = c.match(/^rgba*\(\s*(\d+),\s*(\d+),\s*(\d+)/i) ) {
		o.r = m[1];
		o.g = m[2];
		o.b = m[3];
	} else if( m = c.match(/^rgba*\(\s*(\d+)\%,\s*(\d+)\%,\s*(\d+)\%/i) ) {
		o.r = Math.round(m[1] * 255 / 100);
		o.g = Math.round(m[2] * 255 / 100);
		o.b = Math.round(m[3] * 255 / 100);
	} else {
		return null;
	}
	return o;
};

