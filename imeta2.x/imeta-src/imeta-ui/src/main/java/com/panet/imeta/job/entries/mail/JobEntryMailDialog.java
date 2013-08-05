package com.panet.imeta.job.entries.mail;

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
import com.panet.iform.forms.labelTextarea.LabelTextareaMeta;
import com.panet.iform.forms.menuTab.MenuTabMeta;
import com.panet.imeta.job.JobMeta;
import com.panet.imeta.job.entry.JobEntryCopy;
import com.panet.imeta.job.entry.JobEntryDialog;
import com.panet.imeta.job.entry.JobEntryDialogInterface;
import com.panet.imeta.ui.exception.ImetaException;

public class JobEntryMailDialog extends JobEntryDialog implements
JobEntryDialogInterface {
	/**
	 * 列式表单
	 */
	private ColumnFormMeta columnFormMeta;
	private ColumnFormDataMeta columnFormDataMeta;
	
	/**
	 * 邮件作业名称
	 */
	private LabelInputMeta name;

	/**
	 * 页签
	 */
	private MenuTabMeta meta;
	
	/***************************************
	 *  地址
	 ***************************************/
	
	/**
	 * 目的地址
	 */
	private ColumnFieldsetMeta destinationAddress;
	
	/**
	 * 目的地址
	 */
	private LabelInputMeta destination;
	
	/**
	 * 抄送
	 */
	private LabelInputMeta destinationCc;
	
	/**
	 * 暗送
	 */
	private LabelInputMeta destinationBCc;
	
	/**
	 * 回复
	 */
	private ColumnFieldsetMeta reply;
	
	/**
	 * 回复名称
	 */
	private LabelInputMeta replyName;
	
	/**
	 * 回复地址
	 */
	private LabelInputMeta replyAddress;
	
	/**
	 * Reply to
	 */
	private LabelInputMeta replyToAddresses;
	
	/**
	 * 联系人
	 */
	private LabelInputMeta contactPerson;
	
	/**
	 * 联系电话
	 */
	private LabelInputMeta contactPhone;
	
	/********************************
	 * 服务器
	 ********************************/
	
	/**
	 * 邮件服务器
	 */
	private ColumnFieldsetMeta emalServer;
	
	/**
	 * SMTP服务器
	 */
	private LabelInputMeta server;
	
	/**
	 * 端口号
	 */
	private LabelInputMeta port;
	
	/**
	 * 验证
	 */
	private ColumnFieldsetMeta authentication;
	
	/**
	 * 用户验证
	 */
	private LabelInputMeta usingAuthentication;
	
	/**
	 * 用户名
	 */
	private LabelInputMeta authenticationUser;
	
	/**
	 * 密码
	 */
	private LabelInputMeta authenticationPassword;
	
	/**
	 * 使用安全验证
	 */
	private LabelInputMeta usingSecureAuthentication;
	
	/**
	 * 安全验证类型
	 */
	private LabelSelectMeta secureConnectionType;
	
	/*******************************************
	 * 邮件消息
	 *******************************************/
	
	/**
	 * 消息设置
	 */
	private ColumnFieldsetMeta messageSetting;
	
	/**
	 * 消息里带日期
	 */
	private LabelInputMeta includeDate;
	
	/**
	 * 只发送邮件注释
	 */
	private LabelInputMeta onlySendComment;
	
	/**
	 * 使用HTML邮件格式
	 */
	private LabelInputMeta useHTML;
	
	/**
	 * 编码
	 */
	private LabelSelectMeta encoding;
	
	/**
	 * 管理优先级
	 */
	private LabelInputMeta usePriority;
	
	/**
	 * 优先级
	 */
	private LabelSelectMeta priority;
	
	/**
	 * 重要
	 */
	private LabelSelectMeta importance;
	
	/**
	 * 消息
	 */
	private ColumnFieldsetMeta news;
	
	/**
	 * 主题
	 */
	private LabelInputMeta subject;
	
	/**
	 * 注释
	 */
	private LabelTextareaMeta comment;
	
	/*******************************************
	 * 附件
	 *******************************************/
	
	/**
	 * 预览文件名
	 */
	private ColumnFieldsetMeta browseName;
	
	/**
	 * 带附件
	 */
	private LabelInputMeta includingFiles;
	
	/**
	 * 文件类型
	 */
	private LabelSelectMeta fileType;
	
	/**
	 * 压缩成统一文件格式
	 */
	private LabelInputMeta zipFiles;
	
	/**
	 * 压缩成文件名称
	 */
	private LabelInputMeta zipFilename;
	

	public JobEntryMailDialog(JobMeta jobMeta, JobEntryCopy jobEntryMeta) {
		super(jobMeta, jobEntryMeta);
	}

	public JSONObject open() throws ImetaException {
		try {
			JSONObject rtn = new JSONObject();
			JSONArray cArr = new JSONArray();
			String id = this.getId();

			JobEntryMail job = (JobEntryMail)super.getJobEntry();
			// 得到form
			this.columnFormDataMeta = new ColumnFormDataMeta(id, null);
			this.columnFormMeta = new ColumnFormMeta(columnFormDataMeta);
			
			// 邮件作业名称
			this.name = new LabelInputMeta(id + ".name", "邮件作业名称", null, null,
					"邮件作业名称必须填写", super.getJobEntryName(), null, ValidateForm
							.getInstance().setRequired(true));
			this.name.setSingle(true);
           
			// 得到页签
			this.meta = new MenuTabMeta(id, new String[] { "地址", "服务器", "邮件消息", "附件"});
			this.meta.setSingle(true);
			/*******************************************************************
			 * 地址
			 ******************************************************************/
			
			// 目的地址
			this.destinationAddress = new ColumnFieldsetMeta(null, "目的地址");
			this.destinationAddress.setSingle(true);
			
			// 目的地址：
			this.destination = new LabelInputMeta(id + ".destination", "目的地址：", null, null,
					"目的地址必须填写", job.getDestination(), null, ValidateForm
					.getInstance().setRequired(true));
			this.destination.setSingle(true);
			
			// 抄送：
			this.destinationCc = new LabelInputMeta(id + ".destinationCc", "抄送：", null, null,
					null, job.getDestinationCc(), null, ValidateForm
					.getInstance().setRequired(false));
			this.destinationCc.setSingle(true);
			
			// 暗送：
			this.destinationBCc = new LabelInputMeta(id + ".destinationBCc", "暗送：", null, null,
					null, job.getDestinationBCc(), null, ValidateForm
					.getInstance().setRequired(false));
			this.destinationBCc.setSingle(true);
			
			this.destinationAddress.putFieldsetsContent(new BaseFormMeta[] {
					this.destination, this.destinationCc, this.destinationBCc });
			
			// 回复
			this.reply = new ColumnFieldsetMeta(null, "回复");
			this.reply.setSingle(true);
			
			// 回复名称：
			this.replyName = new LabelInputMeta(id + ".replyName", "回复名称：", null, null,
					null, job.getReplyName(), null, ValidateForm
					.getInstance().setRequired(false));
			this.replyName.setSingle(true);
			
			// 回复地址
			this.replyAddress = new LabelInputMeta(id + ".replyAddress", "回复地址：", null, null,
					"回复地址必须填写", job.getReplyAddress(), null, ValidateForm
					.getInstance().setRequired(true));
			this.replyAddress.setSingle(true);
			
			this.reply.putFieldsetsContent(new BaseFormMeta[] {
					this.replyName, this.replyAddress});
			
			// Reply to
			this.replyToAddresses = new LabelInputMeta ( id + ".replyToAddresses", "回复到：", null, null,
					null, job.getReplyToAddresses(), null, ValidateForm.getInstance().setRequired(false));
			this.replyToAddresses.setSingle(true);
			
			// 联系人
			this.contactPerson = new LabelInputMeta(id + ".contactPerson", "联系人：", null, null,
					null, job.getContactPerson(), null, ValidateForm
					.getInstance().setRequired(false));
			this.contactPerson.setSingle(true);
			
			// 联系电话
			this.contactPhone = new LabelInputMeta(id + ".contactPhone", "联系电话：", null, null,
					null, job.getContactPhone(), null, ValidateForm
					.getInstance().setRequired(false));
			this.contactPhone.setSingle(true);
			
			this.meta.putTabContent(0, new BaseFormMeta[] { this.destinationAddress, this.reply, this.replyToAddresses, this.contactPerson, this.contactPhone });
			
			/*********************************************************************
			 * 服务器
			 *********************************************************************/
			// 邮件服务器
			this.emalServer = new ColumnFieldsetMeta(null, "邮件服务器");
			this.emalServer.setSingle(true);
			
			// SMTP服务器：
			this.server = new LabelInputMeta(id + ".server", "SMTP服务器：", null, null,
					"SMTP服务器必须填写", job.getServer(), null, ValidateForm
					.getInstance().setRequired(true));
			this.server.setSingle(true);
			
			// 端口号
			this.port = new LabelInputMeta(id + ".port", "端口号：", null, null,
					"端口号必须填写", job.getPort(), null, ValidateForm
					.getInstance().setRequired(true));
			this.port.setSingle(true);
			
			this.emalServer.putFieldsetsContent(new BaseFormMeta[] {
					this.server, this.port
					});
			
			// 验证
			this.authentication = new ColumnFieldsetMeta(null, "验证");
			this.authentication.setSingle(true);
			
			// 用户验证
			this.usingAuthentication = new LabelInputMeta(id + ".usingAuthentication", "用户验证？", null,
					null, null, String.valueOf(job.isUsingAuthentication()), InputDataMeta.INPUT_TYPE_CHECKBOX,
					ValidateForm.getInstance().setRequired(false));
			this.usingAuthentication.setSingle(true);
			this.usingAuthentication.addClick("jQuery.imeta.jobEntries.mail.listeners.usingAuthentication");
			
			// 用户名：
			this.authenticationUser = new LabelInputMeta(id + ".authenticationUser", "用户名：", null, null,
					"用户名必须填写", job.getAuthenticationUser(), null, ValidateForm
					.getInstance().setRequired(true));
			this.authenticationUser.setSingle(true);
			this.authenticationUser.setDisabled(!job.isUsingAuthentication());
			
			// 密码：
			this.authenticationPassword = new LabelInputMeta(id + ".authenticationPassword", "密码：", null, null,
					"密码必须填写", job.getAuthenticationPassword(), InputDataMeta.INPUT_TYPE_PASSWORD, ValidateForm
					.getInstance().setRequired(true));
			this.authenticationPassword.setSingle(true);
			this.authenticationPassword.setDisabled(!job.isUsingAuthentication());
			
			// 使用安全验证
			this.usingSecureAuthentication = new LabelInputMeta(id + ".usingSecureAuthentication", "使用安全验证？", null,
					null, null, String.valueOf(job.isUsingSecureAuthentication()), InputDataMeta.INPUT_TYPE_CHECKBOX,
					ValidateForm.getInstance().setRequired(false));
			this.usingSecureAuthentication.setSingle(true);
		    this.usingSecureAuthentication.addClick("jQuery.imeta.jobEntries.mail.listeners.usingSecureAuthentication");
			this.usingSecureAuthentication.setDisabled(!job.isUsingAuthentication());
			
			// 安全连接类型
			List<OptionDataMeta> option = new ArrayList<OptionDataMeta>();
			option.add(new OptionDataMeta("0", "SSL"));
			option.add(new OptionDataMeta("1", "TLS"));
			this.secureConnectionType= new LabelSelectMeta(id + ".secureConnectionType","安全连接类型",
					null,null,null,job.getSecureConnectionType(),null,option);	
			this.secureConnectionType.setSingle(true);
			this.secureConnectionType.setDisabled(!job.isUsingSecureAuthentication());
			
			this.authentication.putFieldsetsContent(new BaseFormMeta[] {
					this.usingAuthentication, this.authenticationUser,
					this.authenticationPassword, this.usingSecureAuthentication, 
					this.secureConnectionType
					});
			
			this.meta.putTabContent(1, new BaseFormMeta[] { this.emalServer, this.authentication });
			
			/*************************************************8
			 * 邮件消息
			 ****************************************************/
			
			// 消息设置
			this.messageSetting = new ColumnFieldsetMeta(null, "消息验证");
			this.messageSetting.setSingle(true);
			
			// 信息里带日期
			this.includeDate = new LabelInputMeta(id + ".includeDate", "信息里带日期?", null,
					null, null, String.valueOf(job.getIncludeDate()), InputDataMeta.INPUT_TYPE_CHECKBOX,
					ValidateForm.getInstance().setRequired(false));
			this.includeDate.setSingle(true);
			
			// 只发送邮件注释
			this.onlySendComment = new LabelInputMeta(id + ".onlySendComment", "只发送邮件注释?", null,
					null, null, String.valueOf(job.isOnlySendComment()), InputDataMeta.INPUT_TYPE_CHECKBOX,
					ValidateForm.getInstance().setRequired(false));
			this.onlySendComment.setSingle(true);
			
			// 使用HTML邮件格式
			this.useHTML = new LabelInputMeta(id + ".useHTML", "使用HTML邮件格式?", null,
					null, null, String.valueOf(job.isUseHTML()), InputDataMeta.INPUT_TYPE_CHECKBOX,
					ValidateForm.getInstance().setRequired(false));
			this.useHTML.setSingle(true);
			this.useHTML.addClick("jQuery.imeta.jobEntries.mail.listeners.useHTML");
			
			// 编码
			this.encoding= new LabelSelectMeta(id + ".encoding","编码",
					null,null,null,job.getEncoding(),null,super.getEncoding());	
			this.encoding.setSingle(true);
			this.encoding.setDisabled(!job.isUseHTML());
			
			// 管理优先级
			this.usePriority = new LabelInputMeta(id + ".usePriority", "管理优先级", null,
					null, null, String.valueOf(job.isUsePriority()), InputDataMeta.INPUT_TYPE_CHECKBOX,
					ValidateForm.getInstance().setRequired(false));
			this.usePriority.setSingle(true);
			this.usePriority.addClick("jQuery.imeta.jobEntries.mail.listeners.usePriority");
			
			// 优先级
			List<OptionDataMeta> optionsa = new ArrayList<OptionDataMeta>();
			optionsa.add(new OptionDataMeta("0", Messages.getString("JobMail.Priority.Low.Label")));
			optionsa.add(new OptionDataMeta("1", Messages.getString("JobMail.Priority.Normal.Label")));
			optionsa.add(new OptionDataMeta("2", Messages.getString("JobMail.Priority.High.Label")));
			this.priority= new LabelSelectMeta(id + ".priority","优先级",
					null,null,null,job.getPriority(),null,optionsa);	
			this.priority.setSingle(true);
			this.priority.setDisabled(!job.isUsePriority());
			
			// 重要
			List<OptionDataMeta> optionsb = new ArrayList<OptionDataMeta>();
			optionsb.add(new OptionDataMeta("0", Messages.getString("JobMail.Priority.Low.Label")));
			optionsb.add(new OptionDataMeta("1", Messages.getString("JobMail.Priority.Normal.Label")));
			optionsb.add(new OptionDataMeta("2", Messages.getString("JobMail.Priority.High.Label")));
			this.importance= new LabelSelectMeta(id + ".importance","重要",
					null,null,null,job.getImportance(),null,optionsb);
			this.importance.setSingle(true);
			this.importance.setDisabled(!job.isUsePriority());
			
			this.messageSetting.putFieldsetsContent(new BaseFormMeta[] {
					this.includeDate, this.onlySendComment,
					this.useHTML, this.encoding, 
					this.usePriority, this.priority,
					this.importance
					});
			
			// 消息
			this.news = new ColumnFieldsetMeta(null, "主题");
			this.news.setSingle(true);
			
			// 主题：
			this.subject = new LabelInputMeta(id + ".subject", "主题：", null, null,
					null, job.getSubject(), null, ValidateForm
					.getInstance().setRequired(false));
			this.subject.setSingle(true);
			
			// 注释
			this.comment = new LabelTextareaMeta(id + ".comment", "注释：", null, null,
					null, String.valueOf(job.getComment()), 3, null);
			
			this.comment.setSingle(true);
			
			this.news.putFieldsetsContent(new BaseFormMeta[] {
					this.subject, this.comment
					});
			
			this.meta.putTabContent(2, new BaseFormMeta[] { this.messageSetting, this.news });
			
			
			/*****************************************
			 * 附件
			 ******************************************/
			
			// 预览文件名
			this.browseName = new ColumnFieldsetMeta(null, "预览文件名称");
			this.browseName .setSingle(true);
			
			// 带附件？
			this.includingFiles = new LabelInputMeta(id + ".includingFiles", "带附件？", null,
					null, null, String.valueOf(job.isIncludingFiles()), InputDataMeta.INPUT_TYPE_CHECKBOX,
					ValidateForm.getInstance().setRequired(false));
			this.includingFiles.setSingle(true);
			this.includingFiles.addClick("jQuery.imeta.jobEntries.mail.listeners.includingFiles");

			// 文件类型
			int a = 0;
			int[] values = job.getFileType();
			if( values != null){
				for (int i = 0; i < values.length; i++) {
                    a = values[i];
		        }
			}else{
				values = new int[0];
			}
			
			List<OptionDataMeta> optionsDate = new ArrayList<OptionDataMeta>();
			optionsDate.add(new OptionDataMeta("0", "一般"));
			optionsDate.add(new OptionDataMeta("1", "日志"));
			optionsDate.add(new OptionDataMeta("2", "错误行"));
			optionsDate.add(new OptionDataMeta("3", "错误"));
			optionsDate.add(new OptionDataMeta("4", "警告"));
			this.fileType = new LabelSelectMeta(id + ".fileType", "文件类型:", null, null, null, 
					String.valueOf(a), null, optionsDate);
			    
//			System.out.println("Dialog = " + a);
			this.fileType.setSingle(true);
//			this.fileType.setLayout(LabelSelectMeta.LAYOUT_COLUMN);
//			this.fileType.setSize(10);
//			this.fileType.setSingle(true);
			this.fileType.setDisabled(!job.isIncludingFiles());
			
			// 压缩成统一文件格式
			this.zipFiles = new LabelInputMeta(id + ".zipFiles", "压缩成统一文件格式？", null,
					null, null, String.valueOf(job.isZipFiles()), InputDataMeta.INPUT_TYPE_CHECKBOX,
					ValidateForm.getInstance().setRequired(false));
			this.zipFiles.setSingle(true);
			this.zipFiles.setDisabled(!job.isIncludingFiles());
			this.zipFiles.addClick("jQuery.imeta.jobEntries.mail.listeners.zipFiles");
			
			
			// 压缩文件名称
			this.zipFilename = new LabelInputMeta(id + ".zipFilename", "压缩文件名称：", null, null,
					"压缩文件名称必须填写", job.getZipFilename(), null, ValidateForm
					.getInstance().setRequired(true));
			this.zipFilename.setSingle(true);
			this.zipFilename.setDisabled(!job.isZipFiles());
			
			this.browseName.putFieldsetsContent(new BaseFormMeta[] {
					this.includingFiles,
					this.fileType,
					this.zipFiles, this.zipFilename
					});
			
			this.meta.putTabContent(3, new BaseFormMeta[] { this.browseName});
			
			// 装载到form
			columnFormMeta.putFieldsetsContent(new BaseFormMeta[] { this.name,this.meta});

	        columnFormMeta.putFieldsetsFootButtons(new ButtonMeta[] {super.getOkBtn(), super.getCancelBtn() });

	        cArr.add(columnFormMeta.getFormJo());

	        rtn.put("items", cArr);
	        rtn.put("title", super.getJobMeta().getName());

	        return rtn;
          } catch (Exception ex) {
	   throw new ImetaException(ex.getMessage(), ex);
      }
   }
}
