package com.panet.imeta.trans.steps.mail;

import java.util.ArrayList;
import java.util.List;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

import com.panet.iform.core.BaseFormMeta;
import com.panet.iform.core.ValidateForm;
import com.panet.iform.core.base.ButtonMeta;
import com.panet.iform.core.base.InputDataMeta;
import com.panet.iform.core.base.OptionDataMeta;
import com.panet.iform.forms.columnFieldset.ColumnFieldsetMeta;
import com.panet.iform.forms.columnForm.ColumnFormDataMeta;
import com.panet.iform.forms.columnForm.ColumnFormMeta;
import com.panet.iform.forms.labelInput.LabelInputMeta;
import com.panet.iform.forms.labelSelect.LabelSelectMeta;
import com.panet.iform.forms.menuTab.MenuTabMeta;
import com.panet.imeta.trans.TransMeta;
import com.panet.imeta.trans.step.BaseStepDialog;
import com.panet.imeta.trans.step.StepDialogInterface;
import com.panet.imeta.trans.step.StepMeta;
import com.panet.imeta.ui.exception.ImetaException;

public class MailDialog extends BaseStepDialog implements
		StepDialogInterface {
	/**
	 * 列式表单
	 */
	private ColumnFormMeta columnFormMeta;
	private ColumnFormDataMeta columnFormDataMeta;
	/**
	 * 步骤名称
	 */
	private LabelInputMeta name;

	/**
	 * 页签
	 */
	private MenuTabMeta meta;
	
	/*****************************8
	 * Addresses
	 *****************************/
	
	/**
	 * Destination
	 */
	private ColumnFieldsetMeta destinationCol;
	
	/**
	 * Destination
	 */
	private LabelSelectMeta destination;
	
	/**
	 * Cc
	 */
	private LabelSelectMeta destinationCc;
	
	/**
	 * BCc
	 */
	private LabelSelectMeta destinationBCc;
	
	/**
	 * sender
	 */
	private ColumnFieldsetMeta sender;
	
	/**
	 * Sender name
	 */
	private LabelSelectMeta replyName;
	
	/**
	 * sender address
	 */
	private LabelSelectMeta replyAddress;
	
	/**
	 * Reply to
	 */
	private LabelSelectMeta replyToAddresses;
	
    /**
     * Contact
     */
	private LabelSelectMeta contactPerson;
	
	/**
	 * Contact phone
	 */
	private LabelSelectMeta contactPhone;
	
	/**********************************8
	 * server
	 **********************************/
	
	private ColumnFieldsetMeta serverCol;
	
	/**
	 * SMTP server
	 */
	private LabelSelectMeta server;
	
	/**
	 * port
	 */
	private LabelSelectMeta port;
	
	/**
	 * Authentication
	 */
	private ColumnFieldsetMeta authenticationCol;
	
	/**
	 * use authentication
	 */
	private LabelInputMeta usingAuthentication;
	
	/**
	 * Authentication user
	 */
	private LabelSelectMeta authenticationUser;
	
	private LabelSelectMeta authenticationPassword;
	
	/**
	 * use secure
	 */
	private LabelInputMeta usingSecureAuthentication;
	
	/**
	 * Secure connection
	 */
	private LabelSelectMeta secureconnectiontype;
	
	/** ***************************************
	 * EMail Message
	 *****************************************/
	
	private ColumnFieldsetMeta messageSetting;
	/**
	 * Include date in
	 */
	private LabelInputMeta includeDate;
	
	/**
	 * Only send
	 */
	private LabelInputMeta onlySendComment;
	
	/**
	 * Use HTML
	 */
	private LabelInputMeta useHTML;
	
	/**
	 * Encoding
	 */
	private LabelSelectMeta encoding;
	
	/**
	 * Manage priority
	 */
	private LabelInputMeta usePriority;
	
	/**
	 * Priority
	 */
	private LabelSelectMeta priority;
	
	/**
	 * Importance
	 */
	private LabelSelectMeta importance;
	
	/**
	 * Message
	 */
    private ColumnFieldsetMeta message;

    /**
     * subject
     */
    private LabelSelectMeta subject;
    
    /**
     * Comment
     */
    private LabelSelectMeta comment;
    
    /*************************************
	 * Attached files
	 *************************************/
	/**
	 * Attached filenames
	 */
    private ColumnFieldsetMeta attached;
    
    /**
     * Dynamic
     */
    private LabelInputMeta isFilenameDynamic;
    
    /**
     * Filename field
     */
    private LabelSelectMeta dynamicFieldname;
    
    /**
     * Wildcard field
     */
    private LabelSelectMeta dynamicWildcard;
    
    /**
     * name/foldername
     */
    private LabelInputMeta sourcefilefoldername;
    
//    private ButtonMeta fileBtn;
//
//    private ButtonMeta folderBtn;
//    
    /**
     * Include Subfolders
     */
    private LabelInputMeta includeSubFolders;
    
    /**
     * wildcard
     */
    private LabelInputMeta sourcewildcard;
	
	/**
	 * Zip files
	 */
	private ColumnFieldsetMeta zipFilesCol;
	
	/**
	 * Zip file
	 */
	private LabelInputMeta zipFiles;
	
	/**
	 * Is zip filename
	 */
	private LabelInputMeta zipFilenameDynamic;
	
	/**
	 * Zipfilename field
	 */
	private LabelSelectMeta dynamicZipFilename;
	
	/**
	 * Zip filename
	 */
	private LabelInputMeta zipFilename;
	
	/**
	 * zip files if size
	 */
	private LabelInputMeta ziplimitsize;

	/**
	 * 初始化
	 * 
	 * @param stepMeta
	 * @param transMeta
	 */
	public MailDialog(StepMeta stepMeta, TransMeta transMeta) {
		super(stepMeta, transMeta);
	}

	@Override
	public JSONObject open() throws ImetaException {
		try {
			JSONObject rtn = new JSONObject();
			JSONArray cArr = new JSONArray();
			String id = this.getId();

			MailMeta step = (MailMeta)super.getStep();
			// 得到form
			this.columnFormDataMeta = new ColumnFormDataMeta(id, null);
			this.columnFormMeta = new ColumnFormMeta(columnFormDataMeta);
			
			// 得到步骤名称
			this.name = new LabelInputMeta(id + ".name", "步骤名称", null, null,
					"步骤名称必须填写", super.getStepMeta().getName(), null, ValidateForm
							.getInstance().setRequired(true));
			this.name.setSingle(true);
           
			// 得到页签a
			this.meta = new MenuTabMeta(id, new String[] { "收件人电子邮件", "服务器", "电子邮件", "附加档案"});
			this.meta.setSingle(true);
			/*******************************************************************
			 * 标签0  Addresses
			 ******************************************************************/
		    this.destinationCol = new ColumnFieldsetMeta ( null, "目标");
			this.destinationCol.setSingle(true);
			
			// Destination address
			this.destination = new LabelSelectMeta(id + ".destination","目标地址",
					null,null,null, step.getDestination(),null,super.getPrevStepResultFields());	
			this.destination.setSingle(true);
			
			// Cc
			this.destinationCc = new LabelSelectMeta(id + ".destinationCc","抄送",
					null,null,null, step.getDestinationCc(),null,super.getPrevStepResultFields());	
			this.destinationCc.setSingle(true);
			
			// BCc
			this.destinationBCc = new LabelSelectMeta(id + ".destinationBCc","暗送",
					null,null,null, step.getDestinationBCc(),null,super.getPrevStepResultFields());	
			this.destinationBCc.setSingle(true);
			
			this.destinationCol.putFieldsetsContent(new BaseFormMeta[] {
					this.destination, this.destinationCc, this.destinationBCc	
			});
			
			// Sender
			this.sender = new ColumnFieldsetMeta ( null, "寄信人");
			this.sender.setSingle(true);
			
			// Sender name
			this.replyName = new LabelSelectMeta(id + ".replyName","寄信人姓名",
					null,null,null, step.getReplyName(),null,super.getPrevStepResultFields());	
			this.replyName.setSingle(true);
			
			// Sender address
			this.replyAddress = new LabelSelectMeta(id + ".replyAddress","寄信人地址",
					null,null,null, step.getReplyAddress(),null,super.getPrevStepResultFields());	
			this.replyAddress.setSingle(true);
			
			this.sender.putFieldsetsContent(new BaseFormMeta[] {
					this.replyName, this.replyAddress
			});
			
			// reply to
			this.replyToAddresses = new LabelSelectMeta(id + ".replyToAddresses","回复",
					null,null,null, step.getReplyToAddresses(),null,super.getPrevStepResultFields());	
			this.replyToAddresses.setSingle(true);
			
			// Contact
			this.contactPerson = new LabelSelectMeta(id + ".contactPerson","联系人",
					null,null,null, step.getContactPerson(),null,super.getPrevStepResultFields());
			this.contactPerson.setSingle(true);
			
			// BContact phone
			this.contactPhone = new LabelSelectMeta(id + ".contactPhone","联系电话",
					null,null,null, step.getContactPhone(),null,super.getPrevStepResultFields());	
			this.contactPhone.setSingle(true);
			
			this.meta.putTabContent(0, new BaseFormMeta[] { this.destinationCol, this.sender,
					this.replyToAddresses, this.contactPerson, this.contactPhone
			});
			
			/******************************************88
			 * 1,Server
			 ********************************************/
			// SMTP
			this.serverCol = new ColumnFieldsetMeta ( null, "SMTP服务器");
			this.serverCol.setSingle(true);
			
			// SMTP server
			this.server = new LabelSelectMeta(id + ".server","SMTP服务器",
					null,null,null, step.getServer(),null,super.getPrevStepResultFields());	
			this.server.setSingle(true);
			
			// Port
			this.port = new LabelSelectMeta(id + ".port","端口",
					null,null,null, String.valueOf(step.getPort()),null,super.getPrevStepResultFields());		
			this.port.setSingle(true);
			
			this.serverCol.putFieldsetsContent(new BaseFormMeta[] {
					this.server, this.port
			});
			
			// Authentication
			this.authenticationCol = new ColumnFieldsetMeta ( null, "验证");
			this.authenticationCol.setSingle(true);
			
			// Use authentication
			this.usingAuthentication = new LabelInputMeta ( id + ".usingAuthentication", "使用验证", null, null, 
					null, String.valueOf(step.isUsingAuthentication()), InputDataMeta.INPUT_TYPE_CHECKBOX, ValidateForm.getInstance().setRequired(false));
			this.usingAuthentication.setSingle(true);
			this.usingAuthentication.addClick("jQuery.imeta.steps.mail.listeners.usingAuthentication");
			
			// Authentication user
			this.authenticationUser = new LabelSelectMeta(id + ".authenticationUser","验证用户",
					null,null,null, step.getAuthenticationUser(),null,super.getPrevStepResultFields());	
			this.authenticationUser.setSingle(true);
			this.authenticationUser.setDisabled(!step.isUsingAuthentication());
			
			// Authentication
			this.authenticationPassword = new LabelSelectMeta(id + ".authenticationPassword","验证",
					null,null,null, step.getAuthenticationPassword(),null,super.getPrevStepResultFields());	
			this.authenticationPassword.setSingle(true);
			this.authenticationPassword.setDisabled(!step.isUsingAuthentication());
			
			// Use secure
			this.usingSecureAuthentication = new LabelInputMeta ( id + ".usingSecureAuthentication", "使用安全协议", null, null,
					null, String.valueOf(step.isUsingSecureAuthentication()), InputDataMeta.INPUT_TYPE_CHECKBOX, ValidateForm.getInstance().setRequired(false));
			this.usingSecureAuthentication.setSingle(true);
			this.usingSecureAuthentication.addClick("jQuery.imeta.steps.mail.listeners.usingSecureAuthentication");
			this.usingSecureAuthentication.setDisabled(!step.isUsingAuthentication());
			
			// Secure connection
			List<OptionDataMeta> optionsSecureSonnection = new ArrayList<OptionDataMeta>();
			optionsSecureSonnection.add(new OptionDataMeta("secureConnection", "SSL"));
			optionsSecureSonnection.add(new OptionDataMeta("secureConnection","TLS"));
			this.secureconnectiontype = new LabelSelectMeta(id + ".secureconnectiontype","安全连接协议",
					null,null,null, step.getSecureConnectionType(),null,optionsSecureSonnection);	
			this.secureconnectiontype.setSingle(true);
			this.secureconnectiontype.setDisabled(!step.isUsingSecureAuthentication());
			this.authenticationCol.putFieldsetsContent(new BaseFormMeta[] {
					this.usingAuthentication, this.authenticationUser,
					this.authenticationPassword, this.usingSecureAuthentication, this.secureconnectiontype
			});
			
			this.meta.putTabContent(1, new BaseFormMeta[] { 
                    this.serverCol, this.authenticationCol
			});
			
			/*************************888888888
			 * Email message
			 **********************************/
			// message setting
			this.messageSetting = new ColumnFieldsetMeta ( null, "邮件设置");
			this.messageSetting.setSingle(true);
			
			// include date in
			this.includeDate = new LabelInputMeta ( id + ".includeDate", "包含邮件日期?", null, null,
					null, String.valueOf(step.getIncludeDate()), InputDataMeta.INPUT_TYPE_CHECKBOX, ValidateForm.getInstance().setRequired(false));
			this.includeDate.setSingle(true);
			
			// Only send comment
			this.onlySendComment = new LabelInputMeta ( id + ".onlySendComment", "只传输评论?", null, null, 
					null, String.valueOf(step.isOnlySendComment()), InputDataMeta.INPUT_TYPE_CHECKBOX, ValidateForm.getInstance().setRequired(false));
			this.onlySendComment.setSingle(true);
			
			// Use HTML format in
			this.useHTML = new LabelInputMeta ( id + ".useHTML", "使用HTML格式的Mail body?", null, null,
					null, String.valueOf(step.isUseHTML()), InputDataMeta.INPUT_TYPE_CHECKBOX, ValidateForm.getInstance().setRequired(false));
			this.useHTML.setSingle(true);
			this.useHTML.addClick("jQuery.imeta.steps.mail.listeners.useHTML");
			
			// Encoding
			this.encoding = new LabelSelectMeta(id + ".encoding","编码",
					null,null,null, step.getEncoding(),null,super.getEncoding());	
			this.encoding.setSingle(true);
			this.encoding.setDisabled(!step.isUseHTML());
			
			// Manage priority
			this.usePriority = new LabelInputMeta ( id + ".usePriority", "管理重点", null, null,
					null, String.valueOf(step.isUsePriority()), InputDataMeta.INPUT_TYPE_CHECKBOX, ValidateForm.getInstance().setRequired(false));
			this.usePriority.setSingle(true);
			this.usePriority.addClick("jQuery.imeta.steps.mail.listeners.manage");
			
			// Priority
			List<OptionDataMeta> optionsPriority = new ArrayList<OptionDataMeta>();
			optionsPriority.add(new OptionDataMeta("0", Messages.getString("Mail.Priority.Low.Label")));
			optionsPriority.add(new OptionDataMeta("1", Messages.getString("Mail.Priority.Normal.Label")));
			optionsPriority.add(new OptionDataMeta("2", Messages.getString("Mail.Priority.High.Label")));
			this.priority = new LabelSelectMeta(id + ".priority","优先级",
					null,null,null, step.getPriority(),null,optionsPriority);	
			this.priority.setSingle(true);
			this.priority.setDisabled(!step.isUsePriority());
			
			// Importance
			List<OptionDataMeta> optionsImportance = new ArrayList<OptionDataMeta>();
			optionsImportance.add(new OptionDataMeta("0", Messages.getString("Mail.Priority.Low.Label")));
			optionsImportance.add(new OptionDataMeta("1", Messages.getString("Mail.Priority.Normal.Label")));
			optionsImportance.add(new OptionDataMeta("2", Messages.getString("Mail.Priority.High.Label")));
			this.importance = new LabelSelectMeta(id + ".importance","重要性",
					null,null,null, step.getImportance(),null,optionsImportance);	
			this.importance.setSingle(true);
			this.importance.setDisabled(!step.isUsePriority());
			
			this.messageSetting.putFieldsetsContent(new BaseFormMeta[] {
					this.includeDate, this.onlySendComment, this.useHTML,
					this.encoding, this.usePriority, this.priority,
					this.importance
			});
			
			// Message
			this.message = new ColumnFieldsetMeta ( null, "邮件");
			this.message.setSingle(true);
			
			// Subject
			this.subject = new LabelSelectMeta(id + ".subject","主题",
					null, null, null, step.getSubject(), null, super.getPrevStepResultFields());	
			this.subject.setSingle(true);
			
			// Comment
			this.comment = new LabelSelectMeta(id + ".comment","意见",
					null, null, null, step.getComment(), null, super.getPrevStepResultFields());	
			this.comment.setSingle(true);
			
			this.message.putFieldsetsContent(new BaseFormMeta[] {
					this.subject, this.comment
			});
			
			this.meta.putTabContent(2, new BaseFormMeta[] { this.messageSetting, this.message
			});
			
			/*************************************
			 * Attached files
			 ***************************************/
			
			// Attached filenames
			this.attached = new ColumnFieldsetMeta ( null, "附加文件名");
			this.attached.setSingle(true);
			
			// Dynamic filenames
			this.isFilenameDynamic = new LabelInputMeta ( id + ".isFilenameDynamic", "动态文件名", null, null, 
					null, String.valueOf(step.isDynamicFilename()), InputDataMeta.INPUT_TYPE_CHECKBOX, ValidateForm.getInstance().setRequired(false));
			this.isFilenameDynamic.setSingle(true);
			this.isFilenameDynamic.addClick("jQuery.imeta.steps.mail.listeners.isFilenameDynamic");
			
			// Filename field
			this.dynamicFieldname = new LabelSelectMeta(id + ".dynamicFieldname","文件名字段",
					null, null, null, step.getDynamicFieldname(), null, super.getPrevStepResultFields());	
			this.dynamicFieldname.setSingle(true);
			this.dynamicFieldname.setDisabled(!step.isDynamicFilename());
			
			// Wildcard field
			this.dynamicWildcard = new LabelSelectMeta(id + ".dynamicWildcard","通配符字段",
					null, null, null, step.getDynamicWildcard(), null, super.getPrevStepResultFields());	
			this.dynamicWildcard.setSingle(true);
			this.dynamicWildcard.setDisabled(!step.isDynamicFilename());
			
			// Filename/foldername
			this.sourcefilefoldername = new LabelInputMeta ( id + ".sourcefilefoldername", "文件/文件夹", null, null,
					"文件/文件夹必须填写", step.getSourceFileFoldername(), null, ValidateForm.getInstance().setRequired(true));
			this.sourcefilefoldername.setSingle(true);
			this.sourcefilefoldername.setDisabled(step.isDynamicFilename());
//			this.fileBtn = new ButtonMeta ( id + ".btn.fileBtn", id + ".btn.fileBtn",
//					"文件...", "文件...");
//			this.folderBtn = new ButtonMeta ( id + ".btn.folderBtn", id + ".btn.folderBtn",
//					"文件夹...", "文件夹...");
//			this.foldername.addButton( new ButtonMeta [] { this.fileBtn, this.folderBtn });
			this.sourcefilefoldername.setSingle(true);
			
			// Include subfolders 
			this.includeSubFolders = new LabelInputMeta ( id + ".includeSubFolders", "包含子文件夹", null, null, 
					null, String.valueOf(step.isIncludeSubFolders()), InputDataMeta.INPUT_TYPE_CHECKBOX, ValidateForm.getInstance().setRequired(false));
			this.includeSubFolders.setSingle(true);
			
			// wildcard
			this.sourcewildcard = new LabelInputMeta ( id + ".sourcewildcard", "通配符", null, null, 
					"通配符必须填写", step.getSourceWildcard(), null, ValidateForm.getInstance().setRequired(true));
			this.sourcewildcard.setSingle(true);
			this.sourcewildcard.setDisabled(step.isDynamicFilename());
			
			this.attached.putFieldsetsContent(new BaseFormMeta[] {
					this.isFilenameDynamic, this.dynamicFieldname, this.dynamicWildcard,
					this.sourcefilefoldername, this.includeSubFolders, this.sourcewildcard
			});
			
			this.zipFilesCol = new ColumnFieldsetMeta ( null, "Zip文件");
			this.zipFilesCol.setSingle(true);
			
			// Zip files
			this.zipFiles = new LabelInputMeta ( id + ".zipFiles", "Zip文件", null, null, 
					null, String.valueOf(step.isZipFiles()), InputDataMeta.INPUT_TYPE_CHECKBOX, ValidateForm.getInstance().setRequired(false));
			this.zipFiles.setSingle(true);
			this.zipFiles.addClick("jQuery.imeta.steps.mail.listeners.zipFiles");
			
			// is zip filename
			this.zipFilenameDynamic = new LabelInputMeta ( id + ".zipFilenameDynamic", "是ZIP文件?", null, null,
					null, String.valueOf(step.isZipFilenameDynamic()), InputDataMeta.INPUT_TYPE_CHECKBOX, ValidateForm.getInstance().setRequired(false));
			this.zipFilenameDynamic.setSingle(true);
			this.zipFilenameDynamic.addClick("jQuery.imeta.steps.mail.listeners.zipFilenameDynamic");
			this.zipFilenameDynamic.setDisabled(!step.isZipFiles());
			
			// Zipfilename field
			this.dynamicZipFilename = new LabelSelectMeta(id + ".dynamicZipFilename","ZIP文件名字段",
					null,null,null,step.getDynamicZipFilenameField(),null,super.getPrevStepResultFields());	
			this.dynamicZipFilename.setSingle(true);
			this.dynamicZipFilename.setDisabled(!step.isZipFilenameDynamic());
			
			// zip filename
			this.zipFilename = new LabelInputMeta ( id + ".zipFilename", "ZIP文件名称", null, null, 
					"ZIP文件名称必须填写", step.getZipFilename(), null, ValidateForm.getInstance().setRequired(true));
			this.zipFilename.setSingle(true);
			this.zipFilename.setDisabled(!step.isZipFiles());
			
			// Zip files if zise greater
			this.ziplimitsize = new LabelInputMeta ( id + ".ziplimitsize", "使用Zip文件当文件大于", null, null,
					null, step.getZipLimitSize(), null, ValidateForm.getInstance().setRequired(true));
			this.ziplimitsize.setSingle(true);
			this.ziplimitsize.setDisabled(!step.isZipFiles());
			
			this.zipFilesCol.putFieldsetsContent(new BaseFormMeta[] {
					this.zipFiles, this.zipFilenameDynamic, this.dynamicZipFilename,
					this.zipFilename, this.ziplimitsize
			});
			
			this.meta.putTabContent(3, new BaseFormMeta[] { this.attached, this.zipFilesCol
			});
			// 装载到form
			columnFormMeta.putFieldsetsContent(new BaseFormMeta[] { this.name,
					this.meta
					});

			// 确定，取消
			columnFormMeta.putFieldsetsFootButtons(new ButtonMeta[] {
					super.getOkBtn(), super.getCancelBtn() });

			cArr.add(columnFormMeta.getFormJo());

			rtn.put("items", cArr);
			rtn.put("title", super.getStepMeta().getName());

			return rtn;
		} catch (Exception ex) {
			throw new ImetaException(ex.getMessage(), ex);
		}
	}
}
