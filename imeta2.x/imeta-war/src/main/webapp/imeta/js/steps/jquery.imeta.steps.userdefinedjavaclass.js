jQuery.imeta.steps.userdefinedjavaclass = {
	fields : {
		btn : {
			fieldAdd : function(c) {
				var rootId = c.getAttribute("rootId");
				var r = [ {
					id : rootId + '.fieldId',
					type : 'number',
					text : ''
				}, {
					id : rootId + '.fieldName',
					type : 'input',
					text : ''
				}, {
					id : rootId + '.fieldType',
					type : 'select',
					text : ''
				}, {
					id : rootId + '.fieldLength',
					type : 'input',
					text : ''
				}, {
					id : rootId + '.fieldPrecision',
					type : 'input',
					text : ''
				} ];

				jQuery.imetabar.createRowByHeader(r, rootId);
			}
		}
	},
	parameters : {
		btn : {
			fieldAdd : function(c) {
				var rootId = c.getAttribute("rootId");
				var r = [ {
					id : rootId + '.parameterId',
					type : 'number',
					text : ''
				}, {
					id : rootId + '.parameterTag',
					type : 'input',
					text : ''
				}, {
					id : rootId + '.parameterValue',
					type : 'input',
					text : ''
				}, {
					id : rootId + '.parameterDesc',
					type : 'input',
					text : ''
				} ];

				jQuery.imetabar.createRowByHeader(r, rootId);
			}
		}
	},
	infoSteps : {
		btn : {
			fieldAdd : function(c) {
				var rootId = c.getAttribute("rootId");
				var r = [ {
					id : rootId + '.infoStepId',
					type : 'number',
					text : ''
				}, {
					id : rootId + '.infoStepTag',
					type : 'input',
					text : ''
				}, {
					id : rootId + '.infoStepStep',
					type : 'input',
					text : ''
				}, {
					id : rootId + '.infoStepDesc',
					type : 'input',
					text : ''
				} ];

				jQuery.imetabar.createRowByHeader(r, rootId);
			}
		}
	},
	targetSteps : {
		btn : {
			fieldAdd : function(c) {
				var rootId = c.getAttribute("rootId");
				var r = [ {
					id : rootId + '.targetStepId',
					type : 'number',
					text : ''
				}, {
					id : rootId + '.targetStepTag',
					type : 'input',
					text : ''
				}, {
					id : rootId + '.targetStepStep',
					type : 'input',
					text : ''
				}, {
					id : rootId + '.targetStepDesc',
					type : 'input',
					text : ''
				} ];

				jQuery.imetabar.createRowByHeader(r, rootId);
			}
		}
	}
};