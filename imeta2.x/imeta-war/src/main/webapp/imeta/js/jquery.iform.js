(function($){
	$.fn.extend({
		
		/**
		 * 表单
		 */
		iformtab : function(options){
			// Clean up peoples goddam crappy code
			// Get the user extensions and defaults
			var opts = $.extend({}, $.fn.iformtab.defaults, options);
			// Content location
			if (opts.contloc == "none") {
				contloc = $(this).parent();
			} else {
				contloc = opts.contloc;
			}
			// Content location
			if (opts.tabloc == "none") {
				tabloc = $(this).parent();
			} else {
				tabloc = opts.tabloc;
			}
			// better define some stuff for safety
			var newli = "", newdiv = "";
	
			// Start Building Tabs
			return this.each(function(i) {
					// start developing basis
					var now = $(this);
					var nowid = now.attr("id");
					now.addClass("iformtab_head");

					// tab maker function
					$("#" + nowid + " li").each(function(i) {
						taba = $('#' + nowid + " li q");
						$(this).addClass("removeme");
						tabcont = taba.html();
						$(".removeme q").remove();
						tabli = $('#' + nowid + " li");
						licont = tabli.html();
						$(this).remove();

						newli += "<li rel='" + nowid + "-" + i
								+ "'><a>" + licont + "</a></li>";
						newdiv += "<div id='" + nowid + "-" + i
								+  "' class='iformtab_space_tab'>" + tabcont + "</div>";

					});

					// Something weird to gain the location
					now.remove();
					$(tabloc).append("<ul id='" + nowid
							+ "' class='" + opts.color + "'>"
							+ newli + "</ul>");
					// Fix the ul
					// $("#"+nowid).append(newli);
					// Find the Parent then append the divs
					// var parent = $("#"+nowid).parent();

					newdiv = "<div id='"
							+ nowid
							+ "content' class='iformtab_cont'><div class='"
							+ opts.area + "'>" + newdiv
							+ "</div>"
							+ "</div>";
					newdiv = newdiv.replace(/\/>/, ">");
					$(contloc).append(newdiv);

					// Find the default
					var ndef = nowid + "-" + opts.dopen;
					var ncon = nowid + "content ." + opts.area + " div";
					$('#' + ncon).css('display','none');
					$('#' + ndef).css('display','block');
					$('#' + ndef + " div").css('display','block');

					var deftab = $('li[rel*=' + ndef + "]");

					deftab.addClass(opts.tabactive);
					deftab.children("a").addClass(opts.tabactive);
					
					// Seperate function to start the tabbing
					$("#" + nowid + " li").click(function() {
						var here = $(this);
						var nbound = here.attr("rel");

						// Make the active class / remove it from
						// others
						$("#" + nowid + " li")
								.removeClass(opts.tabactive);
						$("#" + nowid + " li a")
								.removeClass(opts.tabactive);
						here.addClass(opts.tabactive);
						here.children("a").addClass(opts.tabactive);

						// The real action! Also detirmine
						$('#' + ncon).css('display','none');
						$('#' + nbound).css('display','block');
						$('#' + nbound + ' div').css('display','block');

					});
				});// end return each (i)
		}// end iformtab
	});// end $.fn.extend
	
	// Defaults
	$.fn.iformtab.defaults = {
		mislpace : 'none',
		color : 'iformtab_head',
		area : 'iformtab_space',
		tabloc : 'none',
		contloc : 'none',
		dopen : 0,
		transition : 'fade',
		tabactive : 'tabactive'
	}; // end defaults
})(jQuery);// end function($)