jQuery.imetalogin = {
	
	loginDialog : null,
	login : null,
    
    /**
     * 创建按钮
     */
    createLoginDialog : function(){
    	jQuery.imetalogin.loginDialog = $.iwindow($("#loginDialog"),{
    		winWidth : 300,
    		shadow : true,
			shadowWidth : 308,
			shadowHeight : 174
    	});

    	// form
    	var usernameInput = $("<input id='username' class='loginUsername' size='20' />");
    	var loginForm = $("<form class='x-panel-body x-form' style='margin-top:10px;' method='post'></form>")
    	.append($("<div class='x-form-item'></div>")
    	.append($("<label class='x-form-item-label' style='width:80px;' for='username'>登录账户：</label>"))
    	.append($("<div class='x-form-element' style='padding-left: 80px;'></div>")
    	.append(usernameInput)
    	.append($("<div class='x-form-clear-left'></div>"))));
    	
    	var passwordInput = $("<input id='password' class='loginPassword' size='20' type='password' />");
    	loginForm.append($("<div class='x-form-item'></div>")
    	.append($("<label class='x-form-item-label' style='width:80px;' for='password'>登录密码：</label>"))
    	.append($("<div class='x-form-element' style='padding-left: 80px;'></div>")
    	.append(passwordInput)
    	.append($("<div class='x-form-clear-left'></div>"))));
    	
    	var repSelect = $("<select id='repository' class='loginRepository'></select>");
    	loginForm.append($("<div class='x-form-item'></div>")
    	.append($("<label class='x-form-item-label' style='width:80px;' for='repository'>　资源库：</label>"))
    	.append($("<div class='x-form-element' style='padding-left: 80px;'></div>")
    	.append(repSelect)
    	.append($("<div class='x-form-clear-left'></div>"))));
    	
    	// btn
    	var loginBtn = $("<div class='x-panel-btns-ct' style='height:20px;'></div>");
    	var loginBtnDiv = $("<div class='x-panel-btns x-panel-btns-center'></div>").appendTo(loginBtn);
    	var okBtn = $.ibutton(loginBtnDiv,{
    		id : 'loginOk',
    		text : '确认',
    		btnWidth : 75,
    		enabled : false
    	});
    	
    	
    	jQuery.imetalogin.login = $.ipanel(jQuery.imetalogin.loginDialog.getBodyObj(),{
    		title : "iMeta平台登录",
    		titleCls : "loginLogo",
    		bodyObj : loginForm,
    		bottomObj : loginBtn
    	});
    	
    	jQuery.imetalogin.changePortalSize();
    	
    	var okBtnClick = function(e,v){
	    	$.ajax({
                type: "POST",
                url: "ImetaAction!login.action",
                data: jQuery.cutil.objectToUrl({
                	username : usernameInput.val(),
                	password : passwordInput.val(),
                	repository : repSelect.val()
                }),
                dataType: "json",
                success : function(json){
	                if(json.success){
	                	window.location = json.redirect;
	                }else{
	                	usernameInput.val("");
	                	passwordInput.val("");
	                	alert(json.msg);
	                }
                }
            });
	    };
    	
    	var changesFn = function(){
    		if(usernameInput.val()!=''&&passwordInput.val()!=''){
    			// 清除error
    			usernameInput.attr("title","");
    			usernameInput.removeClass("x-form-invalid");
    			passwordInput.attr("title","");
    			passwordInput.removeClass("x-form-invalid");
    			okBtn.btnClick(okBtnClick);
    		}else{
    			// 添加error
    			if(usernameInput.val()==''){
    				usernameInput.attr("title","“登录账户”不能为空！");
    				usernameInput.addClass("x-form-invalid");
    			}else{
    				usernameInput.attr("title","");
    				usernameInput.removeClass("x-form-invalid");
    			}
    			if(passwordInput.val()==''){
    				passwordInput.attr("title","“登录密码”不能为空！");
    				passwordInput.addClass("x-form-invalid");
    			}else{
    				passwordInput.attr("title","");
    				passwordInput.removeClass("x-form-invalid");
    			}
    			okBtn.btnUnbind("click");
    		}
    	};
    	
    	usernameInput.change(changesFn);
    	
    	passwordInput.change(changesFn);
    	
    	//okBtn.btnClick(jQuery.imetalogin.okBtnClick);
    	
    	// 读取perpareLogin
    	$.ajax({
            type: "POST",
            url: "ImetaAction!perpareLogin.action",
            dataType: "json",
            success : function(json){
            	if(json.reps){
            		$.each(json.reps,function(k,v){
            			repSelect.append($("<option id='"+v+"'>"+v+"</option>"));
            		});
            	}
            }
        });
    },
    
    /**
     * 修改Portal高度
     */
    changePortalSize : function(){
    	var top = Math.floor( ($.getWindowScroll().height-150)/2 +20 );
    	var left = Math.floor( ($.getWindowScroll().width-300)/2 );
    	
    	top = (top<0)?0:top;
    	left = (left<0)?0:left;
    	
    	jQuery.imetalogin.loginDialog.changePosition({
    		top : top,
    		left : left
    	});
    },
    
    /**
     * 登录
     */
    login : function(){
    	var username = "";
    	var password = "";
    	$.ajax({
            type: "POST",
            url: "ImetaAction!login.action",
            data: "username=" + username + "&password=" + password,
            //dataType: "json",
            success : function(json){
                
            }
        });
    }
};
