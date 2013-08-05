<%@ page contentType="text/html; charset=UTF-8"%>

<%
	String path = request.getContextPath();
	
%>
<html>
	<head>
		<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
		<title>iMeta(ETTL)操作平台</title>
        
        <link rel="shortcut icon" href="<%=path%>/framework/images/logo.ico" />
		<link rel="icon" href="<%=path%>/framework/images/logo.ico" />
        
		<link href="<%=path%>/imeta/css/imeta.compressed.css" rel="stylesheet"
			type="text/css" />
		<!--[if IE]><link href="<%=path%>/imeta/css/jquery.ie.css" rel="stylesheet"
			type="text/css" /><![endif]-->

		<script type="text/javascript">
			var $path = "<%=path%>";
		</script>

		<script type="text/javascript"
			src="<%=path%>/framework/js/_jquery/jquery-1.3.min.js"></script>
		<!--[if IE]><script type="text/javascript" src="<%=path%>/framework/js/_excanvas/excanvas.pack.js"></script><![endif]-->
           
		<script type="text/javascript"
			src="<%=path%>/framework/js/_jquery/ui/ui.core.js"></script>
		<script type="text/javascript"
			src="<%=path%>/framework/js/_jquery/ui/minified/ui.draggable.min.js"></script>
		<script type="text/javascript"
			src="<%=path%>/framework/js/_jquery/ui/minified/ui.droppable.min.js"></script>
		<script type="text/javascript"
			src="<%=path%>/framework/js/_jquery/ui/minified/ui.resizable.min.js"></script>
		<script type="text/javascript"
			src="<%=path%>/framework/js/_jquery/ui/effects.core.js"></script>
		<script type="text/javascript"
			src="<%=path%>/framework/js/_jquery/ui/minified/effects.blind.min.js"></script>
		<script type="text/javascript"
			src="<%=path%>/framework/js/_jquery/ui/minified/effects.scale.min.js"></script>
		<script type="text/javascript"
			src="<%=path%>/framework/js/_jquery/ui/minified/effects.pulsate.min.js"></script>
		<script type="text/javascript"
			src="<%=path%>/framework/js/_hotkeys/jquery.hotkeys-0.7.8-packed.js"></script>
		<script type="text/javascript"
			src="<%=path%>/framework/js/_treeview/jquery.treeview.pack.js"></script>
		<script type="text/javascript"
			src="<%=path%>/framework/js/_validate/jquery.validate.js"></script>
		<script type="text/javascript"
			src="<%=path%>/framework/js/_validate/lib/jquery.metadata.js"></script>
		<script type="text/javascript"
			src="<%=path%>/framework/js/_form/jquery.form.pack.js"></script>
		<script type="text/javascript"
			src="<%=path%>/framework/js/_drag/jquery.event.drag-1.2.min.js"></script>
		
		<script type="text/javascript"
			src="<%=path%>/imeta/js/portal.pack.js"></script>

		<style type="text/css">
			.loginLogo{ background:url(<%=path %>/framework/images/logo.png) no-repeat 1px 1px; }
			.loginUsername{ background:url(<%=path %>/framework/images/icons/user.png) no-repeat 1px 2px; }
			.loginPassword{ background:url(<%=path %>/framework/images/icons/key.png) no-repeat 1px 2px; }
			.loginRepository{ background:url(<%=path %>/framework/images/icons/database_connect.png) no-repeat 1px 2px; }
			.loginUsername,.loginPassword,.loginRepository{
				background-color:#FFFFFF;
				padding:2px 0 2px 20px;
				font-weight:bold;
				color:#000033;
				border:1px solid #B5B8C8;
				width:80%;
			}
			
		</style>
	</head>
	<body id="login">

		<div id="loginDialog">

		</div>
		

<script type="text/javascript">
$(document).ready(
	function(){
	    
		jQuery.imetalogin.createLoginDialog();

		
		$(window).bind('resize', function() {
			jQuery.imetalogin.changePortalSize();
		});
	}
);
</script>


	</body>
</html>