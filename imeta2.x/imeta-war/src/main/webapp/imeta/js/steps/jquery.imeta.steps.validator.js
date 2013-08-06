jQuery.imeta.steps.validator = {
	btn : {
		newValidation : function(e, v) {
			$.imessagebox(
				"#ibody",
				{
					title : "输入一个新的验证名称",
					type : "prompt",
					marded : true,
					message : "输入一个唯一的验证规则的名称",
					fn : function(m, val) {
						// 等待完成
					if (m == 'ok') {
						var elId = e.target.id;
						var id = elId.split(".")[0];
						var result = 'notexist';
						$("#" + id + "_select option").each(
								function() {
									if (this.text == val) {
										$.imessagebox("#ibody", {
											title : "该验证名称已存在",
											type : "alert",
											marded : true,
											message : "该验证名称已存在",
											fn : function(m) {
											}
										});
										result = 'exist';
										return false;
									}
								});
						if (result == 'notexist') {
							var jsonObject = {
								valName : val,
								fieldName : "",
								errorCode : "",
								errorDescription : "",
								dataType : 0,
								dataTypeVerified : false,
								allowedValues : new Array(),
								conversionMask : "",
								decimalSymbol : "",
								groupingSymbol : "",
								nullAllowed : true,
								onlyNullAllowed : false,
								onlyNumericAllowed : false,
								maximumLength : "",
								minimumLength : "",
								maximumValue : "",
								minimumValue : "",
								startString : "",
								endString : "",
								startStringNotAllowed : "",
								endStringNotAllowed : "",
								regularExpression : "",
								regularExpressionNotAllowed : "",
								sourcingValues : false,
								sourcingStepName : 0,
								sourcingField : 0
							};
							var jsonStr = $.toJSON(jsonObject);
							$("[id=" + id + "_select]").append(
									"<option value='" + jsonStr
											+ "'>" + val
											+ "</option>");
							$("#" + id + "_edit").removeClass(
									"notDisplay");
							$("[id=" + id + "_select]").attr(
									"value", jsonStr);
							var o = eval('(' + $(
									"#"
											+ id
											+ "_select option:selected")
									.val() + ')'); // 将option的json字符串转换成对象
							$("[id=" + id + ".valName]").val(
									o.valName);
							$("[id=" + id + ".fieldName]").val(
									o.fieldName);
							$("[id=" + id + ".errorCode]").val(
									o.errorCode);
							$("[id=" + id + ".errorDescription]")
									.val(o.errorDescription);
							$("[id=" + id + ".dataTypeVerified]")
									.attr("checked",
											o.dataTypeVerified);
							$("[id=" + id + ".dataType]")[0].selectedIndex = o.dataType;
							$("[id=" + id + ".conversionMask]")
									.val(o.conversionMask);
							$("[id=" + id + ".decimalSymbol]").val(
									o.decimalSymbol);
							$("[id=" + id + ".groupingSymbol]")
									.val(o.groupingSymbol);
							$("[id=" + id + ".nullAllowed]").attr(
									"checked", o.nullAllowed);
							$("[id=" + id + ".onlyNullAllowed]")
									.attr("checked",
											o.onlyNullAllowed);
							$("[id=" + id + ".onlyNumericAllowed]")
									.attr("checked",
											o.onlyNumericAllowed);
							$("[id=" + id + ".maximumLength]").val(
									o.maximumLength == '-1' ? ''
											: o.maximumLength);
							$("[id=" + id + ".minimumLength]").val(
									o.minimumLength);
							$("[id=" + id + ".maximumValue]").val(
									o.maximumValue);
							$("[id=" + id + ".minimumValue]").val(
									o.minimumValue);
							$("[id=" + id + ".startString]").val(
									o.startString);
							$("[id=" + id + ".endString]").val(
									o.endString);
							$(
									"[id="
											+ id
											+ ".startStringNotAllowed]")
									.val(o.startStringNotAllowed);
							$("[id=" + id + ".endStringNotAllowed]")
									.val(o.endStringNotAllowed);
							$("[id=" + id + ".regularExpression]")
									.val(o.regularExpression);
							$(
									"[id="
											+ id
											+ ".regularExpressionNotAllowed]")
									.val(
											o.regularExpressionNotAllowed);
							$("[id=" + id + ".sourcingStepName]")
									.val(o.sourcingStepName);
							$("[id=" + id + ".sourcingValues]")
									.attr("checked",
											o.sourcingValues);
							$("[id=" + id + ".sourcingStepName]")[0].selectedIndex = o.sourcingStepName;
							$("[id=" + id + ".sourcingField]")[0].selectedIndex = o.sourcingField;

							if (o.sourcingValues == true) {
								$(
										"[id="
												+ id
												+ "_allowedValues.btn.add]")
										.attr("disabled", true);
								$(
										"[id="
												+ id
												+ "_allowedValues.btn.delete]")
										.attr("disabled", true);
								$(
										"[id="
												+ id
												+ "_allowedValues.btn.add.root]")
										.addClass("x-item-disabled");
								$(
										"[id="
												+ id
												+ "_allowedValues.btn.delete.root]")
										.addClass("x-item-disabled");
								$(
										"#"
												+ id
												+ "_allowedValues_gRoot input")
										.attr("disabled", true);
								$(
										"[id="
												+ id
												+ ".sourcingStepName]")
										.attr("disabled", false);
								$("[id=" + id + ".sourcingField]")
										.attr("disabled", false);
							} else {
								$(
										"[id="
												+ id
												+ "_allowedValues.btn.add]")
										.attr("disabled", false);
								$(
										"[id="
												+ id
												+ "_allowedValues.btn.delete]")
										.attr("disabled", false);
								$(
										"[id="
												+ id
												+ "_allowedValues.btn.add.root]")
										.removeClass(
												"x-item-disabled");
								$(
										"[id="
												+ id
												+ "_allowedValues.btn.delete.root]")
										.removeClass(
												"x-item-disabled");
								$(
										"#"
												+ id
												+ "_allowedValues_gRoot input")
										.attr("disabled", false);
								$(
										"[id="
												+ id
												+ ".sourcingStepName]")
										.attr("disabled", true);
								$("[id=" + id + ".sourcingField]")
										.attr("disabled", true);
							}
							if (o.allowedValues != null
									&& o.allowedValues.length > 0) {
								$(
										"[id="
												+ id
												+ "_allowedValues_gList]")
										.empty();
								$(
										"[id="
												+ id
												+ "_allowedValues_gList]")
										.attr("rowNr", "0");
								var arr = o.allowedValues;
								for ( var i = 0; i < arr.length; i++) {
									var rowNr = $(
											"[id="
													+ id
													+ "_allowedValues_gList]")
											.attr("rowNr");
									rowNr = (rowNr) ? rowNr : 0;
									var listDiv = $("<div class='x-grid-row' onmouseover='jQuery.imetabar.gridRowMouseOver(this);' onmouseout='jQuery.imetabar.gridRowMouseOut(this);' onclick='jQuery.imetabar.gridRowClick(this);'></div>");
									var r = [
											{
												id : id + '_allowedValues.num',
												type : 'number',
												text : ++rowNr,
												width : 50
											},
											{
												id : id + '_allowedValues.value',
												type : 'input',
												text : arr[i],
												width : 100
											} ];
									$(
											"[id="
													+ id
													+ "_allowedValues_gList]")
											.attr("rowNr", rowNr);
									jQuery.imetabar.createRow(r,
											null, listDiv);
									$(
											"[id="
													+ id
													+ "_allowedValues_gList]")
											.append(listDiv);
								}
							} else {
								$(
										"[id="
												+ id
												+ "_allowedValues_gList]")
										.empty();
								$(
										"[id="
												+ id
												+ "_allowedValues_gList]")
										.attr("rowNr", "0");
							}

						} // end if
					} // end if(m == 'ok')
				} // end function
			}); // end imessagebox
		},
		deleteValidation : function(e, v) {
			var elId = e.target.id;
			var id = elId.split(".")[0];
			if ($("#" + id + "_select option:selected").val() != undefined) {
				$("#" + id + "_select option:selected").remove();
				$("#" + id + "_edit").addClass("notDisplay");
			}

		},
		valueAdd : function(c) {
			var rootId = c.getAttribute("rootId");
			var r = [ {
				id : rootId + '.fieldId',
				type : 'number',
				text : ''
			}, {
				id : rootId + '.value',
				type : 'input',
				text : ''
			} ];
			jQuery.imetabar.createRowByHeader(r, rootId);
		},
		ok : function(e, v) {
			var elId = e.target.id;
			var id = elId.split(".")[0];
			// 点击确定后将当前编辑的内容赋值给当前选中的下拉项
			var valName = $("[id=" + id + ".valName]").val();
			var fieldName = $("[id=" + id + ".fieldName]").val();
			var errorCode = $("[id=" + id + ".errorCode]").val();
			var errorDescription = $("[id=" + id + ".errorDescription]").val();
			var dataTypeVerified = $("[id=" + id + ".dataTypeVerified]").attr(
					"checked");
			var dataType = $("[id=" + id + ".dataType]").val() == '-1' ? 0 : $(
					"[id=" + id + ".dataType]").val();
			var conversionMask = $("[id=" + id + ".conversionMask]").val();
			var decimalSymbol = $("[id=" + id + ".decimalSymbol]").val();
			var groupingSymbol = $("[id=" + id + ".groupingSymbol]").val();
			var nullAllowed = $("[id=" + id + ".nullAllowed]").attr("checked");
			var onlyNullAllowed = $("[id=" + id + ".onlyNullAllowed]").attr(
					"checked");
			var onlyNumericAllowed = $("[id=" + id + ".onlyNumericAllowed]").attr(
					"checked");
			var maximumLength = $("[id=" + id + ".maximumLength]").val();
			var minimumLength = $("[id=" + id + ".minimumLength]").val();
			var maximumValue = $("[id=" + id + ".maximumValue]").val();
			var minimumValue = $("[id=" + id + ".minimumValue]").val();
			var startString = $("[id=" + id + ".startString]").val();
			var endString = $("[id=" + id + ".endString]").val();
			var startStringNotAllowed = $("[id=" + id + ".startStringNotAllowed]")
					.val();
			var endStringNotAllowed = $("[id=" + id + ".endStringNotAllowed]")
					.val();
			var regularExpression = $("[id=" + id + ".regularExpression]").val();
			var regularExpressionNotAllowed = $(
					"[id=" + id + ".regularExpressionNotAllowed]").val();
			var sourcingStepName = $("[id=" + id + ".sourcingStepName]").val() == '-1' ? 0
					: $("[id=" + id + ".sourcingStepName]").val();
			var sourcingField = $("[id=" + id + ".sourcingField]").val() == '-1' ? 0
					: $("[id=" + id + ".sourcingField]").val();
			var sourcingValues = $("[id=" + id + ".sourcingValues]")
					.attr("checked");
			var vals = document.getElementsByName(id + "_allowedValues.value");
			var allowedValue = new Array();
			if (vals && vals.length > 0) {
				for ( var i = 0; i < vals.length; i++) {
					allowedValue[i] = vals[i].value;
				}
			}
			var jsonObject = {
				valName : valName,
				fieldName : fieldName,
				errorCode : errorCode,
				errorDescription : errorDescription,
				dataTypeVerified : dataTypeVerified,
				dataType : dataType,
				allowedValues : allowedValue,
				conversionMask : conversionMask,
				decimalSymbol : decimalSymbol,
				groupingSymbol : groupingSymbol,
				nullAllowed : nullAllowed,
				onlyNullAllowed : onlyNullAllowed,
				onlyNumericAllowed : onlyNumericAllowed,
				maximumLength : maximumLength,
				minimumLength : minimumLength,
				maximumValue : maximumValue,
				minimumValue : minimumValue,
				startString : startString,
				endString : endString,
				startStringNotAllowed : startStringNotAllowed,
				endStringNotAllowed : endStringNotAllowed,
				regularExpression : regularExpression,
				regularExpressionNotAllowed : regularExpressionNotAllowed,
				sourcingValues : sourcingValues,
				sourcingStepName : sourcingStepName,
				sourcingField : sourcingField
			};
			var jsonStr = $.toJSON(jsonObject);
			if ($("#" + id + "_select option:selected").val() != undefined) {
				$("#" + id + "_select option:selected").val(jsonStr);
			} else {
				$("#" + id + "_select :first").val(jsonStr);
			}
			var optionValue = "";
			$("#" + id + "_select option").each(function() {
				optionValue += this.value;
				optionValue += "---";
			});
	
			if (optionValue.substr(optionValue.length - 3) == '---')
				optionValue = optionValue.substr(0, optionValue.length - 3); // 去掉最后一个，
			$("#" + id).ajaxSubmit(
				{
					type : "POST",
					url : "ImetaAction!validatorElementSubmit.action",
					dataType : "json",
					data : {
						id : id,
						roType : jQuery.iPortalTab.OBJECT_TYPE_TRANS,
						roName : e.target.getAttribute("transName"),
						elementName : e.target.getAttribute("stepName"),
						directoryId : e.target
								.getAttribute("directoryId"),
						optionValue : optionValue
					},
					success : function(json) {
						if (json.oldName != json.newName) {
							var el = jQuery.imenu.iContent
									.getCanvasElByText(json.oldName,
											'step');
							jQuery.imenu.iContent.updateEl(el, {
								bText : [ json.newName ]
							});
							jQuery.imenu.iContent.redraw();
						}
						jQuery.iPortalTab
								.updateActiveTab(jQuery.iPortalTab.TYPE_ACTION_CHANGE_STEP);
						$("#window-" + id).remove();
						$.imessagebox("#ibody", json);
					}
				});
		}
	},
	listeners : {
		validationChange : function(e) {
			var id = e.target.id.substr(0, e.target.id.lastIndexOf("_"));
			$("#" + id + "_edit").removeClass("notDisplay");
			var valName = $("[id=" + id + ".valName]").val();
			var fieldName = $("[id=" + id + ".fieldName]").val();
			var errorCode = $("[id=" + id + ".errorCode]").val();
			var errorDescription = $("[id=" + id + ".errorDescription]").val();
			var dataTypeVerified = $("[id=" + id + ".dataTypeVerified]").attr(
					"checked");
			var dataType = $("[id=" + id + ".dataType]").val() == '-1' ? 0 : $(
					"[id=" + id + ".dataType]").val();
			var conversionMask = $("[id=" + id + ".conversionMask]").val();
			var decimalSymbol = $("[id=" + id + ".decimalSymbol]").val();
			var groupingSymbol = $("[id=" + id + ".groupingSymbol]").val();
			var nullAllowed = $("[id=" + id + ".nullAllowed]").attr("checked");
			var onlyNullAllowed = $("[id=" + id + ".onlyNullAllowed]").attr(
					"checked");
			var onlyNumericAllowed = $("[id=" + id + ".onlyNumericAllowed]")
					.attr("checked");
			var maximumLength = $("[id=" + id + ".maximumLength]").val();
			var minimumLength = $("[id=" + id + ".minimumLength]").val();
			var maximumValue = $("[id=" + id + ".maximumValue]").val();
			var minimumValue = $("[id=" + id + ".minimumValue]").val();
			var startString = $("[id=" + id + ".startString]").val();
			var endString = $("[id=" + id + ".endString]").val();
			var startStringNotAllowed = $(
					"[id=" + id + ".startStringNotAllowed]").val();
			var endStringNotAllowed = $("[id=" + id + ".endStringNotAllowed]")
					.val();
			var regularExpression = $("[id=" + id + ".regularExpression]")
					.val();
			var regularExpressionNotAllowed = $(
					"[id=" + id + ".regularExpressionNotAllowed]").val();
			var sourcingStepName = $("[id=" + id + ".sourcingStepName]").val() == '-1' ? 0
					: $("[id=" + id + ".sourcingStepName]").val();
			var sourcingField = $("[id=" + id + ".sourcingField]").val() == '-1' ? 0
					: $("[id=" + id + ".sourcingField]").val();
			var sourcingValues = $("[id=" + id + ".sourcingValues]").attr(
					"checked");

			// 得到表格的值
			var vals = document.getElementsByName(id + "_allowedValues.value");
			var allowedValue = new Array();
			if (vals != null && vals.length > 0) {
				for ( var i = 0; i < vals.length; i++) {
					allowedValue[i] = vals[i].value;
				}
			}
			var jsonObject = {
				valName : valName,
				fieldName : fieldName,
				errorCode : errorCode,
				errorDescription : errorDescription,
				dataTypeVerified : dataTypeVerified,
				dataType : dataType,
				allowedValues : allowedValue,
				conversionMask : conversionMask,
				decimalSymbol : decimalSymbol,
				groupingSymbol : groupingSymbol,
				nullAllowed : nullAllowed,
				onlyNullAllowed : onlyNullAllowed,
				onlyNumericAllowed : onlyNumericAllowed,
				maximumLength : maximumLength,
				minimumLength : minimumLength,
				maximumValue : maximumValue,
				minimumValue : minimumValue,
				startString : startString,
				endString : endString,
				startStringNotAllowed : startStringNotAllowed,
				endStringNotAllowed : endStringNotAllowed,
				regularExpression : regularExpression,
				regularExpressionNotAllowed : regularExpressionNotAllowed,
				sourcingValues : sourcingValues,
				sourcingStepName : sourcingStepName,
				sourcingField : sourcingField
			};
			var jsonStr = $.toJSON(jsonObject);
			// 将当前输入的内容保存到该option的value中
			$("#" + id + "_select option").each(function() {
				if (this.text == valName) {
					this.value = jsonStr;
					return false;
				}
			});
			// 将选中的option中的value内容填充到相应的文本框中
			var o = eval('(' + $("#" + id + "_select option:selected").val() + ')'); // 将option的json字符串转换成对象
			$("[id=" + id + ".valName]").val(o.valName);
			$("[id=" + id + ".fieldName]").val(o.fieldName);
			$("[id=" + id + ".errorCode]").val(o.errorCode);
			$("[id=" + id + ".errorDescription]").val(o.errorDescription);
			$("[id=" + id + ".dataTypeVerified]").attr("checked",
					o.dataTypeVerified);
			$("[id=" + id + ".dataType]")[0].selectedIndex = o.dataType;
			$("[id=" + id + ".conversionMask]").val(o.conversionMask);
			$("[id=" + id + ".decimalSymbol]").val(o.decimalSymbol);
			$("[id=" + id + ".groupingSymbol]").val(o.groupingSymbol);
			$("[id=" + id + ".nullAllowed]").attr("checked", o.nullAllowed);
			$("[id=" + id + ".onlyNullAllowed]").attr("checked",
					o.onlyNullAllowed);
			$("[id=" + id + ".onlyNumericAllowed]").attr("checked",
					o.onlyNumericAllowed);
			$("[id=" + id + ".maximumLength]").val(
					o.maximumLength == '-1' ? '' : o.maximumLength);
			$("[id=" + id + ".minimumLength]").val(o.minimumLength);
			$("[id=" + id + ".maximumValue]").val(o.maximumValue);
			$("[id=" + id + ".minimumValue]").val(o.minimumValue);
			$("[id=" + id + ".startString]").val(o.startString);
			$("[id=" + id + ".endString]").val(o.endString);
			$("[id=" + id + ".startStringNotAllowed]").val(
					o.startStringNotAllowed);
			$("[id=" + id + ".endStringNotAllowed]").val(o.endStringNotAllowed);
			$("[id=" + id + ".regularExpression]").val(o.regularExpression);
			$("[id=" + id + ".regularExpressionNotAllowed]").val(
					o.regularExpressionNotAllowed);
			$("[id=" + id + ".sourcingStepName]").val(o.sourcingStepName);
			$("[id=" + id + ".sourcingValues]").attr("checked",
					o.sourcingValues);
			$("[id=" + id + ".sourcingStepName]")[0].selectedIndex = o.sourcingStepName;
			$("[id=" + id + ".sourcingField]")[0].selectedIndex = o.sourcingField;

			if (o.sourcingValues == true) {
				$("[id=" + id + "_allowedValues.btn.add]").attr("disabled",
						true);
				$("[id=" + id + "_allowedValues.btn.delete]").attr("disabled",
						true);
				$("[id=" + id + "_allowedValues.btn.add.root]").addClass(
						"x-item-disabled");
				$("[id=" + id + "_allowedValues.btn.delete.root]").addClass(
						"x-item-disabled");
				$("#" + id + "_allowedValues_gRoot input").attr("disabled",
						true);
				$("[id=" + id + ".sourcingStepName]").attr("disabled", false);
				$("[id=" + id + ".sourcingField]").attr("disabled", false);
			} else {
				$("[id=" + id + "_allowedValues.btn.add]").attr("disabled",
						false);
				$("[id=" + id + "_allowedValues.btn.delete]").attr("disabled",
						false);
				$("[id=" + id + "_allowedValues.btn.add.root]").removeClass(
						"x-item-disabled");
				$("[id=" + id + "_allowedValues.btn.delete.root]").removeClass(
						"x-item-disabled");
				$("#" + id + "_allowedValues_gRoot input").attr("disabled",
						false);
				$("[id=" + id + ".sourcingStepName]").attr("disabled", true);
				$("[id=" + id + ".sourcingField]").attr("disabled", true);
			}
			if (o.allowedValues != null && o.allowedValues.length > 0) {
				$("[id=" + id + "_allowedValues_gList]").empty();
				$("[id=" + id + "_allowedValues_gList]").attr("rowNr", "0");
				var arr = o.allowedValues;
				for ( var i = 0; i < arr.length; i++) {
					var rowNr = $("[id=" + id + "_allowedValues_gList]").attr(
							"rowNr");
					rowNr = (rowNr) ? rowNr : 0;
					var listDiv = $("<div class='x-grid-row' onmouseover='jQuery.imetabar.gridRowMouseOver(this);' onmouseout='jQuery.imetabar.gridRowMouseOut(this);' onclick='jQuery.imetabar.gridRowClick(this);'></div>");
					var r = [ {
						id : id + '_allowedValues.num',
						type : 'number',
						text : ++rowNr,
						width : 50
					}, {
						id : id + '_allowedValues.value',
						type : 'input',
						text : arr[i],
						width : 100
					} ];
					$("[id=" + id + "_allowedValues_gList]").attr("rowNr",
							rowNr);
					jQuery.imetabar.createRow(r, null, listDiv);
					$("[id=" + id + "_allowedValues_gList]").append(listDiv);
				}
			} else {
				$("[id=" + id + "_allowedValues_gList]").empty();
				$("[id=" + id + "_allowedValues_gList]").attr("rowNr", "0");
			}
		},
		validationClick : function(e) {
			// alert(e.target.id);
			var id = e.target.id.substr(0, e.target.id.lastIndexOf("_"));
			$("#" + id + "_edit").removeClass("notDisplay");
		},
		valNameChange : function(e, v) {
			var elId = e.target.id;
			var id = elId.split(".")[0];
			$("#" + id + "_select option:selected").text(
					$("[id=" + id + ".valName]").val());
		},
		isSourcingValuesClick : function(e, v) {
			var elId = e.target.id;
			var id = elId.split(".")[0];
			if (!e.target.checked) {
				$("[id=" + id + "_allowedValues.btn.add]").attr("disabled",
						false);
				$("[id=" + id + "_allowedValues.btn.delete]").attr("disabled",
						false);
				$("[id=" + id + "_allowedValues.btn.add.root]").removeClass(
						"x-item-disabled");
				$("[id=" + id + "_allowedValues.btn.delete.root]").removeClass(
						"x-item-disabled");
				$("#" + id + "_allowedValues_gRoot input").attr("disabled",
						false);
				$("[id=" + id + ".sourcingStepName]").attr("disabled", true);
				$("[id=" + id + ".sourcingField]").attr("disabled", true);
			} else {
				$("[id=" + id + "_allowedValues.btn.add]").attr("disabled",
						true);
				$("[id=" + id + "_allowedValues.btn.delete]").attr("disabled",
						true);
				$("[id=" + id + "_allowedValues.btn.add.root]").addClass(
						"x-item-disabled");
				$("[id=" + id + "_allowedValues.btn.delete.root]").addClass(
						"x-item-disabled");
				$("#" + id + "_allowedValues_gRoot input").attr("disabled",
						true);
				$("[id=" + id + ".sourcingStepName]").attr("disabled", false);
				$("[id=" + id + ".sourcingField]").attr("disabled", false);
			}
		}
	}
};