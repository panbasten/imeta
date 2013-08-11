<%@ page contentType="text/html; charset=UTF-8"%>
<%@ page import="com.panet.imeta.ui.Messages" %>

<%
	String path = request.getContextPath();
%>
<html>
	<head>
		<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
		<title>iMeta(ETTL)操作平台</title>
        
        <link rel="shortcut icon" href="<%=path%>/framework/images/logo.ico" />
		<link rel="icon" href="<%=path%>/framework/images/logo.ico" />
        
        
        <link rel="stylesheet" href="<%=path%>/framework/js/_treeview/jquery.treeview.css" />
		<link href="<%=path%>/imeta/css/portal.css" rel="stylesheet"
			type="text/css" />
		<link href="<%=path%>/imeta/css/jquery.imeta.css" rel="stylesheet"
			type="text/css" />
		<link href="<%=path%>/imeta/css/jquery.contextMenu.css" rel="stylesheet"
			type="text/css"/>
		<link href="<%=path%>/imeta/css/jquery.iform.css" rel="stylesheet"
			type="text/css"/>
		<!--[if IE]><link href="<%=path%>/imeta/css/jquery.ie.css" rel="stylesheet"
			type="text/css" /><![endif]-->

		<script type="text/javascript">
			var $path = "<%=path%>";
			function globalError(xhr,etype,e){
				window.location = $path;
			}
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
			src="<%=path%>/imeta/js/jquery.cutil.js"></script>
		<script type="text/javascript"
			src="<%=path%>/imeta/js/jquery.iaccordion.js"></script>
		<script type="text/javascript"
			src="<%=path%>/imeta/js/jquery.imetabar.js"></script>
		<script type="text/javascript"
			src="<%=path%>/imeta/js/jquery.imenutab.js"></script>
		<script type="text/javascript"
			src="<%=path%>/imeta/js/jquery.icanvasObj.js"></script>
		<script type="text/javascript"
			src="<%=path%>/imeta/js/jquery.icontent.js"></script>
		<script type="text/javascript"
			src="<%=path%>/imeta/js/jquery.iportaltab.js"></script>
		<script type="text/javascript"
			src="<%=path%>/imeta/js/jquery.iform.js"></script>
		<script type="text/javascript"
			src="<%=path%>/imeta/js/jquery.imeta.pack.js"></script>
		<script type="text/javascript"
			src="<%=path%>/imeta/js/jquery.imeta.steps.pack.js"></script>
		<script type="text/javascript"
			src="<%=path%>/imeta/js/jquery.imeta.jobEntries.pack.js"></script>
		
	</head>
	<body id="ibody" style="overflow:hidden">
		<div id="portalLoading-mask"></div>
		<div id="programLoading-mask" style="display:none;"></div>
		<div id="portalLoading">
			<div class="portalLoading-indicator">
				<img src="<%=path%>/imeta/images/loading.gif" />
				<span id="portalLoading-msg">初始化框架...</span>
			</div>
		</div>
	
		<div id="portal">

		</div>
		
		<div id="log" style="z-index:200100;">
			<div id="logMain"></div>
		
		</div>
		
		<div id="drag_box" class="drag_box" style="display:none;z-index:200200;"></div>
		
		<ul id="portalContextMenu" class="contextMenu">
			<li class="edit element"><a href="#edit"><%=Messages.getString("IMeta.Portal.Context.Menu.Edit") %></a></li>
			<li class="cut element separator"><a href="#cut"><%=Messages.getString("IMeta.Portal.Context.Menu.Cut") %></a></li>
			<li class="copy element"><a href="#copy"><%=Messages.getString("IMeta.Portal.Context.Menu.Copy") %></a></li>
			<li class="paste element"><a href="#paste"><%=Messages.getString("IMeta.Portal.Context.Menu.Paste") %></a></li>
			<li class="delete_el element"><a href="#delete_el"><%=Messages.getString("IMeta.Portal.Context.Menu.Delete") %></a></li>
			<li class="data_distribute element separator"><a href="#data_distribute"><%=Messages.getString("IMeta.Portal.Context.Menu.DataDistribute") %></a></li>
			<li class="data_copy element"><a href="#data_copy"><%=Messages.getString("IMeta.Portal.Context.Menu.DataCopy") %></a></li>
			<li class="data_copies element"><a href="#data_copies"><%=Messages.getString("IMeta.Portal.Context.Menu.DataCopies") %></a></li>
			<li class="exchange jobhop"><a href="#exchange"><%=Messages.getString("IMeta.Portal.Context.Menu.Exchange") %></a></li>
			<li class="evaluation_unconditional jobhop separator"><a href="#evaluation_unconditional"><%=Messages.getString("IMeta.Portal.Context.Menu.EvaluationUnconditional") %></a></li>
			<li class="evaluation_success jobhop"><a href="#evaluation_success"><%=Messages.getString("IMeta.Portal.Context.Menu.EvaluationSuccess") %></a></li>
			<li class="evaluation_failure jobhop"><a href="#evaluation_failure"><%=Messages.getString("IMeta.Portal.Context.Menu.EvaluationFailure") %></a></li>
			<li class="delete_hop jobhop separator"><a href="#delete_hop"><%=Messages.getString("IMeta.Portal.Context.Menu.DeleteJobhop") %></a></li>
		</ul>
		
		<iframe id="downloadFile" name="downloadFile" src="" frameborder=0 scrolling=no marginheight=0 marginwidth=0 height=0 width=0> 
		</iframe>
		

<script type="text/javascript">
$(document).ready(
	function(){
	    
	    
	  	// 产生可拖拽的工具箱
		jQuery.imetabar.createDraggableToolbar("<%=path%>/imeta/js/jquery.imetabar.json","#ibody");

		// 产生可拖拽的对象视图
		jQuery.imetabar.createDraggableObjectView("<%=path%>/imeta/js/jquery.iobjectview.json","#ibody");
		
		// 创建portal
		jQuery.imenu.createPortal("<%=path%>/imeta/js/jquery.imenutab.json");
		
		// 创建log
		jQuery.imenu.createLog("<%=path%>/imeta/js/jquery.ilog.json");
		
		$(window).bind('resize', function() {
			jQuery.imenu.changePortalSize();
		});
		
		$(window).unload(jQuery.imetabar.logout);
	}
);
</script>


	</body>
</html>