jQuery.imeta.steps.scriptvalues_mod = {
	btn : {
		fieldAdd : function(c) {
			var rootId = c.getAttribute("rootId");
			var r = [ {
				id : rootId + '.fieldId',
				type : 'number',
				text : ''
			}, {
				id : rootId + '.name',
				type : 'input',
				text : ''
			}, {
				id : rootId + '.rename',
				type : 'input',
				text : ''
			}, {
				id : rootId + '.type',
				type : 'select',
				text : ''
			}, {
				id : rootId + '.length',
				type : 'input',
				text : ''
			}, {
				id : rootId + '.precision',
				type : 'input',
				text : ''
			} ];

			jQuery.imetabar.createRowByHeader(r, rootId);
		}
	},
	listeners : {
		itemMouseOver : function(e) {
			e.target.style.cursor = 'pointer';
			e.target.style.color = 'red';
		},
		itemMouseOut : function(e) {
			e.target.style.cursor = 'auto';
			e.target.style.color = 'black';
		},
		itemClick : function(e) {
			$(".tree-item-select").removeClass("tree-item-select");
			e.target.className = e.target.className + " tree-item-select";
			var rootId = e.target.getAttribute("rootId");
			var js = $("[id=" + rootId + ".js]");
			js.val(js.val() + " " + e.target.innerHTML);
		}
	}
};