jQuery.imeta.jobEntries.moveFiles = {
	btn : {
		fieldAdd : function(c) {
			var rootId = c.getAttribute("rootId");
			var r = [ {
				id : rootId + '.fieldId',
				type : 'number',
				text : ''
			}, {
				id : rootId + '.fileFolderSource',
				type : 'input',
				text : ''
			}, {
				id : rootId + '.fileFolderDest',
				type : 'input',
				text : ''
			}, {
				id : rootId + '.wildcard',
				type : 'input',
				text : ''
			} ];
			jQuery.imetabar.createRowByHeader(r, rootId);
		}
	},
	listeners : {
		is_include_subfolders_click : function(e, v) {
			var elId = e.target.id;
			var id = elId.split(".")[0];
			if (e.target.checked) {
				$("[id=" + id + ".move_empty_folders]").attr("disabled", false);
			} else {
				$("[id=" + id + ".move_empty_folders]").attr("disabled", true);
			}
		},
		is_arg_from_previous_click : function(e, v) {
			var elId = e.target.id;
			var id = elId.split(".")[0];
			if (!e.target.checked) {
				$("[id=" + id + "_fileFolderTable.btn.add]").attr("disabled",
						false);
				$("[id=" + id + "_fileFolderTable.btn.delete]").attr(
						"disabled", false);
				$("[id=" + id + "_fileFolderTable.btn.add.root]").removeClass(
						"x-item-disabled");
				$("[id=" + id + "_fileFolderTable.btn.delete.root]")
						.removeClass("x-item-disabled");
				$("#" + id + "_fileFolderTable_gRoot input").attr("disabled",
						false);
			} else {
				$("[id=" + id + "_fileFolderTable.btn.add]").attr("disabled",
						true);
				$("[id=" + id + "_fileFolderTable.btn.delete]").attr(
						"disabled", true);
				$("[id=" + id + "_fileFolderTable.btn.add.root]").addClass(
						"x-item-disabled");
				$("[id=" + id + "_fileFolderTable.btn.delete.root]").addClass(
						"x-item-disabled");
				$("#" + id + "_fileFolderTable_gRoot input").attr("disabled",
						true);
			}
		},
		isAddDateTimeClick : function(e, v) {
			var elId = e.target.id;
			var id = elId.split(".")[0];
			if (e.target.checked) {
				$("[id=" + id + ".AddDateBeforeExtension]").attr("disabled",
						false);
			} else {
				if (!$("[id=" + id + ".add_date]").attr("checked")
						&& !$("[id=" + id + ".add_time]").attr("checked"))
					$("[id=" + id + ".AddDateBeforeExtension]").attr(
							"disabled", true);
			}
		},
		isSpecifyFormatClick : function(e, v) {
			var elId = e.target.id;
			var id = elId.split(".")[0];
			if (e.target.checked) {
				$("[id=" + id + ".add_date]").attr("disabled", true);
				$("[id=" + id + ".add_date]").attr("checked", false);
				$("[id=" + id + ".add_time]").attr("disabled", true);
				$("[id=" + id + ".add_time]").attr("checked", false);
				$("[id=" + id + ".date_time_format]").attr("disabled", false);
				$("[id=" + id + ".AddDateBeforeExtension]").attr("disabled",
						false);
			} else {
				$("[id=" + id + ".add_date]").attr("disabled", false);
				$("[id=" + id + ".add_time]").attr("disabled", false);
				$("[id=" + id + ".date_time_format]").attr("disabled", true);
				$("[id=" + id + ".AddDateBeforeExtension]").attr("disabled",
						true);
			}
		},
		iffileexistsChange : function(e) {
			// alert(this.value);
			var elId = e.target.id;
			var id = elId.split(".")[0];
			if (this.value == '4') { // move to folder
				$("[id=" + id + ".destinationFolder]").attr("disabled", false);
				$("[id=" + id + ".create_move_to_folder]").attr("disabled",
						false);
				$("[id=" + id + ".add_moved_date]").attr("disabled", false);
				$("[id=" + id + ".add_moved_time]").attr("disabled", false);
				$("[id=" + id + ".SpecifyMoveFormat]").attr("disabled", false);
				// $("[id="+id+".moved_date_time_format]").attr("disabled",false);
				// $("[id="+id+".AddMovedDateBeforeExtension]").attr("disabled",false);
				$("[id=" + id + ".ifmovedfileexists]").attr("disabled", false);
			} else {
				$("[id=" + id + ".destinationFolder]").attr("disabled", true);
				$("[id=" + id + ".create_move_to_folder]").attr("disabled",
						true);
				$("[id=" + id + ".add_moved_date]").attr("disabled", true);
				$("[id=" + id + ".add_moved_time]").attr("disabled", true);
				$("[id=" + id + ".SpecifyMoveFormat]").attr("disabled", true);
				// $("[id="+id+".moved_date_time_format]").attr("disabled",true);
				// $("[id="+id+".AddMovedDateBeforeExtension]").attr("disabled",true);
				$("[id=" + id + ".ifmovedfileexists]").attr("disabled", true);
			}
		},
		isAddMovedDateTimeClick : function(e, v) {
			var elId = e.target.id;
			var id = elId.split(".")[0];
			if (e.target.checked) {
				$("[id=" + id + ".AddMovedDateBeforeExtension]").attr(
						"disabled", false);
			} else {
				// alert("add_moved_date:"+$("[id="+id+".add_moved_date]").attr("checked"));
				// alert("add_moved_time:"+$("[id="+id+".add_moved_time]").attr("checked"));
				// alert("SpecifyMoveFormat:"+$("[id="+id+".SpecifyMoveFormat]").attr("checked"));
				if (!$("[id=" + id + ".add_moved_date]").attr("checked")
						&& !$("[id=" + id + ".add_moved_time]").attr("checked")
						&& !$("[id=" + id + ".SpecifyMoveFormat]").attr(
								"checked"))
					$("[id=" + id + ".AddMovedDateBeforeExtension]").attr(
							"disabled", true);
			}
		},
		isSpecifyMoveFormatClick : function(e, v) {
			var elId = e.target.id;
			var id = elId.split(".")[0];
			if (e.target.checked) {
				$("[id=" + id + ".add_moved_date]").attr("checked", false);
				$("[id=" + id + ".add_moved_time]").attr("checked", false);
				$("[id=" + id + ".moved_date_time_format]").attr("disabled",
						false);
				$("[id=" + id + ".AddMovedDateBeforeExtension]").attr(
						"disabled", false);
			} else {
				$("[id=" + id + ".moved_date_time_format]").attr("disabled",
						true);
				if (!$("[id=" + id + ".add_moved_date]").attr("checked")
						&& !$("[id=" + id + ".add_moved_time]").attr("checked")
						&& !$("[id=" + id + ".SpecifyMoveFormat]").attr(
								"checked"))
					$("[id=" + id + ".AddMovedDateBeforeExtension]").attr(
							"disabled", true);
			}
		},
		success_conditionChange : function(e) {
			// alert(this.value);
			var elId = e.target.id;
			var id = elId.split(".")[0];
			if (this.value == 'success_if_no_errors') { // move to folder
				$("[id=" + id + ".nr_errors_less_than]").attr("disabled", true);
			} else {
				$("[id=" + id + ".nr_errors_less_than]").attr("disabled", false);
			}
		}
	}
};