package com.panet.imeta.trans.steps.rssoutput;

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
import com.panet.iform.forms.grid.GridCellDataMeta;
import com.panet.iform.forms.grid.GridHeaderDataMeta;
import com.panet.iform.forms.labelGrid.LabelGridMeta;
import com.panet.iform.forms.labelInput.LabelInputMeta;
import com.panet.iform.forms.labelSelect.LabelSelectMeta;
import com.panet.iform.forms.menuTab.MenuTabMeta;
import com.panet.imeta.trans.TransMeta;
import com.panet.imeta.trans.step.BaseStepDialog;
import com.panet.imeta.trans.step.StepDialogInterface;
import com.panet.imeta.trans.step.StepMeta;
import com.panet.imeta.ui.exception.ImetaException;

public class RssOutputDialog extends BaseStepDialog implements
StepDialogInterface{

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

	//General ----------------------------------------------------------------
	
	/**
	  * 单选框
	  */

	private LabelInputMeta customrss;
	
	/**
	  * 单选框
	  */

	 private LabelInputMeta displayitem;
	
	/**
	  * Channel Fields
	  */
	  private ColumnFieldsetMeta channelFields;
	  
	  /**
	    * Channel Title Field
	    */
	  private LabelSelectMeta channeltitle;
	  
	  /**
	    * Channel Description Field
	    */
	  private LabelSelectMeta channeldescription;
	  
	  /**
	    * Channel link Field
	    */
	  private LabelSelectMeta channellink;
	  
	  /**
	    *  Channel pubdate Field
	    */
	  private LabelSelectMeta channelpubdate;
	  
	  /**
	    * Channel language Field
	    */
	  private LabelSelectMeta channellanguage;
	  
	  /**
	    * Channel author Field
	    */
	  private LabelSelectMeta channelauthor;
	  
	  /**
	    * Channel copyright Field
	    */
	  private LabelSelectMeta channelcopyright;
	  
	  /**
		* add Image
		*/
		private LabelInputMeta addimage;
		
    /**
	  * Image Title Field
      */
        private LabelSelectMeta channelimagetitle;
		  
   /**
	 *  Image Link Field
	 */
	   private LabelSelectMeta channelimagelink;
		  
   /**
	 * Image Url Field
	 */
	    private LabelSelectMeta channelimageurl;
		  
	/**
	  * Image Description Field
	  */
	    private LabelSelectMeta channelimagedescription;
	    
	    
	 /**
	   * Encoding
	   */
		 private LabelSelectMeta encoding;
			  
	 /**
	   * Version
	   */
		 private LabelSelectMeta version;
			

	//Item ----------------------------------------------------------------
		 
    /**
      * Channel Item Fields
      */	 
    private ColumnFieldsetMeta itemFields;
			
   /**
     * Item Title Field
     */	
    private LabelSelectMeta itemtitle;
			
   /**
     * Item Description Field
     */	 
	private LabelSelectMeta itemdescription;
			
   /**
     * Item Link Field
     */	 
	private LabelSelectMeta itemlink;
			
  /**
    * Item Pubdate Field
   */	 
	private LabelSelectMeta itempubdate;
			
  /**
    * Item Author Field
    */	 
	private LabelSelectMeta itemauthor;
			
  /**
    * Add GeoRSS
   */	 
	private LabelInputMeta addgeorss;
			
  /**
    * Use GeoRSS GML version
    */	 
	private LabelInputMeta usegeorssgml;
			
  /**
    * Latitude
   */	 
	private LabelSelectMeta geopointlat;
			
   /**
     * Longitude
     */	 
	private LabelSelectMeta geopointlong;

	
	//custom output -----------------------------------------------------------------

	/**
	 * Channel Fields
	 */ 
	private LabelGridMeta channelFields1;
	
	/**
	 * 得到频道文件
	 */ 
	private ButtonMeta getFields;
	
	/**
	 * Item Fields
	 */ 
	private LabelGridMeta itemFields1;
	
	/**
	 *  得到项目文件
	 */ 
	private ButtonMeta getFieldsItem;
	
	
	//custom namespace -----------------------------------------------------------------
	
	/**
	 * Custom namespaces
	 */ 
	private LabelGridMeta namespaces;

	//output file -----------------------------------------------------------------
	
	/**
	 * Output File
	 */ 
	private ColumnFieldsetMeta outputFile;
	
	/**
	 * Filename
	 */ 
	private LabelInputMeta fileName;
		
	/**
	 * Create Parent Folder
	 */ 
	private LabelInputMeta createparentfolder;
	
	/**
	 * fileName Defined in a field
	 */ 
	private LabelInputMeta isfilenameinfield;
	
	/**
	 * fileName Field
	 */ 
	private LabelSelectMeta filenamefield;
	
	/**
	 * 扩展名
	 */ 
	private LabelInputMeta extension;
	
	/**
	 * Include Stepnr in fileName
	 */ 
	private LabelInputMeta stepNrInFilename;
	
	/**
	 * Include Date in fileName
	 */ 
	private LabelInputMeta dateInFilename;
	
	/**
	 * Include Time in fileName
	 */ 
	private LabelInputMeta timeInFilename;
	
	/**
	 * Result fileName
	 */ 
	private ColumnFieldsetMeta resultFilename;
	
	/**
	 * Add File
	 */ 
	private LabelInputMeta AddToResult;

	
	
	/**
	 * 初始化
	 * @param stepMeta
	 * @param transMeta
	 */
	public RssOutputDialog(StepMeta stepMeta, TransMeta transMeta) {
		super(stepMeta, transMeta);
	}
	

	@Override
	public JSONObject open() throws ImetaException {
		try {
			JSONObject rtn = new JSONObject();
			JSONArray cArr = new JSONArray();
			String id = this.getId();
			
			RssOutputMeta step = (RssOutputMeta) super.getStep();
			
			// 得到form
			this.columnFormDataMeta = new ColumnFormDataMeta(id, null);
			this.columnFormMeta = new ColumnFormMeta(columnFormDataMeta);
			
			// 得到步骤名称
			this.name = new LabelInputMeta(id + ".name", "步骤名称", null, null,
					"步骤名称", super.getStepName(), null, ValidateForm
					.getInstance().setRequired(false));
			this.name.setSingle(true);

			// 得到页签
			this.meta = new MenuTabMeta(id, new String[] {  "概括",
															"项目", 
															"自订输出",
															"自定义命名空间",
															"输出文件"});
			this.meta.setSingle(true);
			
			
			/*******************************************************************
			 * 标签0---------General
			 ******************************************************************/
			
			//Create custom RSS
			this.customrss = new LabelInputMeta(id + ".customrss", "创建自定义 RSS", null,
					 null, null,
					 String.valueOf(step.isCustomRss()),
					 InputDataMeta.INPUT_TYPE_CHECKBOX,
					 ValidateForm.getInstance().setRequired(false));
			
			this.customrss.setSingle(true);
	
			//Diaplay item tag
			this.displayitem = new LabelInputMeta(id + ".displayitem", "显示项目标记", null,
			 null, null,String.valueOf(step.isDisplayItem()),
			 InputDataMeta.INPUT_TYPE_CHECKBOX,
			 ValidateForm.getInstance().setRequired(false));
			this.displayitem.setSingle(true);
			
			/**
			* Channel Fields
			*/
			this.channelFields = new ColumnFieldsetMeta(null, "频道");
			this.channelFields.setSingle(true);
			
//1 Channel title field
			
				this.channeltitle = new LabelSelectMeta(id + ".channeltitle","频道名称字段：",
						null,null,null,
						step.getChannelTitle(),
						null,
						super.getPrevStepResultFields());

				this.channeltitle.setSingle(true);
				
//2	Channel description field			

				this.channeldescription = new LabelSelectMeta(id + ".channeldescription","频道描述字段：",
						null,null,null,
						step.getChannelDescription()
						,null,super.getPrevStepResultFields());

				this.channeldescription.setSingle(true);
				
//3	Channel link field			
				this.channellink = new LabelSelectMeta(id + ".channellink","频道链接栏：",
						null,null,null,
						step.getChannelLink(),
						null,
						super.getPrevStepResultFields());

				this.channellink.setSingle(true);
				
//4	Channel pubdate field				
				this.channelpubdate = new LabelSelectMeta(id + ".channelpubdate","频道公共数据字段：",
						null,null,null,
						step.getChannelPubDate(),
						null,
						super.getPrevStepResultFields());

				this.channelpubdate.setSingle(true);
				
//5 Channel language field
		
				this.channellanguage = new LabelSelectMeta(id + ".channellanguage","频道语言字段：",
							null,null,null,
							step.getChannelLanguage()
							,null,
							super.getPrevStepResultFields());

				this.channellanguage.setSingle(true);
					
//6  Channel author field		
				
				this.channelauthor = new LabelSelectMeta(id + ".channelauthor","频道作者字段：",
							null,null,null,
							step.getChannelAuthor(),
							null,
							super.getPrevStepResultFields());

				this.channelauthor.setSingle(true);
					
//7	Channel copyright field		
				

				this.channelcopyright = new LabelSelectMeta(id + ".channelcopyright","频道版权领域：",
							null,null,null,
							step.getChannelCopyright(),
							null,
							super.getPrevStepResultFields());

				this.channelcopyright.setSingle(true);
				
 // Add image
				this.addimage = new LabelInputMeta(id + ".addimage", 
						"添加图片", 
						null,
						null, 
						null,
						String.valueOf(step.AddImage()),
						InputDataMeta.INPUT_TYPE_CHECKBOX,
						ValidateForm.getInstance().setRequired(false));
				this.addimage.setSingle(true);

					
//8	Image title field			
			
				this.channelimagetitle = new LabelSelectMeta(id + ".channelimagetitle","图片名称字段：",
							null,null,null,
							step.getChannelImageTitle(),null,super.getPrevStepResultFields());

				this.channelimagetitle.setSingle(true);
										
//9 Image link field
			
				this.channelimagelink = new LabelSelectMeta(id + ".channelimagelink","图像链接栏：",
								null,null,null,
								step.getChannelImageLink(),null,super.getPrevStepResultFields());

				this.channelimagelink.setSingle(true);
						
//10 Image Url field			
			
				this.channelimageurl = new LabelSelectMeta(id + ".channelimageurl","图片URL领域：",
								null,null,null,
								step.getChannelImageUrl(),null,super.getPrevStepResultFields());

				this.channelimageurl.setSingle(true);
						
//11 Image Description field		
			
				this.channelimagedescription = new LabelSelectMeta(id + ".channelimagedescription","图片说明字段：",
								null,null,null,
								step.getChannelImageDescription(),null,super.getPrevStepResultFields());

				this.channelimagedescription.setSingle(true);
				
				
				this.channelFields.putFieldsetsContent(new BaseFormMeta[] {
						                    this.channeltitle, 
	                                        this.channeldescription,  
	                                        this.channellink, 
	                                        this.channelpubdate,  
	                                        this.channellanguage, 
	                                        this.channelauthor,  
	                                        this.channelcopyright, 
	                                        this.channelimagetitle,  
	                                        this.channelimagelink, 
	                                        this.channelimageurl,  
	                                        this.channelimagedescription
						});
				
				
//12 Encoding	
			
				this.encoding = new LabelSelectMeta(id + "encoding","编码：",
								null,null,null,
								step.getEncoding(),null,
								super.getEncoding());

				this.encoding.setSingle(true);
						
//13 Version			
				List<OptionDataMeta> optionsVersion = new ArrayList<OptionDataMeta>();
				optionsVersion
				.add(new OptionDataMeta("0", "1.0"));
				optionsVersion
				.add(new OptionDataMeta("1", "2.0"));
				optionsVersion
				.add(new OptionDataMeta("2", "3.0"));
				optionsVersion
				.add(new OptionDataMeta("3", "4.0"));
				this.version = new LabelSelectMeta(id + ".version","版本：",
								null,null,null,step.getVersion(),null,optionsVersion);

				this.version.setSingle(true);
				
 this.meta.putTabContent(0, new BaseFormMeta[] { 
		 					this.customrss,
		 					this.displayitem,
		                    this.channelFields,
							this.encoding, 
							this.version
		                     });
						
	
			/*******************************************************************
			 * 标签1---------Item
			 ******************************************************************/

           this.itemFields = new ColumnFieldsetMeta(null, "频道项目领域");
	       this.itemFields.setSingle(true);
	       
//1   Item title field    
	   
	   	this.itemtitle = new LabelSelectMeta(id + ".itemtitle","物品名称字段：",
	   			null,null,null,
	   			step.getItemTitle(),null,super.getPrevStepResultFields());

	   	this.itemtitle.setSingle(true);

//2 	Item description field 
	
		this.itemdescription = new LabelSelectMeta(id + ".itemdescription","物品描述字段：",
				null,null,null,
				step.getItemDescription(),null,super.getPrevStepResultFields());

		this.itemdescription.setSingle(true);
		
//3     Item link field   
		 
	   	this.itemlink = new LabelSelectMeta(id + ".itemlink","项目链接栏：",
	   			null,null,null,
	   			step.getItemLink(),null,super.getPrevStepResultFields());

	   	this.itemlink.setSingle(true);

//4 	Item pubdate field 
	   
		this.itempubdate = new LabelSelectMeta(id + ".itempubdate","项目公开数据字段：",
				null,null,null,
				step.getItemPubDate(),null,super.getPrevStepResultFields());

		this.itempubdate.setSingle(true);
		
//5      Item author field  


	   	this.itemauthor = new LabelSelectMeta(id + ".itemauthor","作者项目字段：",
	   			null,null,null,step.getItemAuthor(),null,super.getPrevStepResultFields());

	   	this.itemauthor.setSingle(true);
	   	
//Add GeoRSS
	   	this.addgeorss = new LabelInputMeta(id + ".addgeorss", "添加GeoRSS", null,
	   					 null, null,
	   					 String.valueOf(step.AddGeoRSS()),
	   					 InputDataMeta.INPUT_TYPE_CHECKBOX,
	   					ValidateForm.getInstance().setRequired(false));
	   	this.addgeorss.setSingle(true);
	   	
//Use GeoRSS GML version
	   	this.usegeorssgml = new LabelInputMeta(id + ".usegeorssgml", "使用GeoRSS GML的版本", null,
	   					 null, null,String.valueOf(step.useGeoRSSGML()),
	   					 InputDataMeta.INPUT_TYPE_CHECKBOX,
	   					ValidateForm.getInstance().setRequired(false));
	   	this.usegeorssgml.setSingle(true);

//6 	geopointlat
	   
		this.geopointlat = new LabelSelectMeta(id + ".geopointlat","纬度：",
				null,null,null,step.getGeoPointLat()
				,null,super.getPrevStepResultFields());

		this.geopointlat.setSingle(true);
		
//7    geopointlong  
		
	   	this.geopointlong = new LabelSelectMeta(id + ".geopointlong","经度：",
	   			null,null,null,step.getGeoPointLong(),
	   			null,super.getPrevStepResultFields());

	   	this.geopointlong.setSingle(true);

	   	this.itemFields.putFieldsetsContent(new BaseFormMeta[] {
				                    this.itemtitle, 
                                    this.itemdescription,  
                                    this.itemlink, 
                                    this.itempubdate,  
                                    this.itemauthor, 
                                    this.addgeorss,  
                                    this.usegeorssgml,  
                                    this.geopointlat,  
                                    this.geopointlong
				});

	    this.meta.putTabContent(1, new BaseFormMeta[] { this.itemFields });
	    
			/*******************************************************************
			 * 标签2---------Custom OutPut
			 ******************************************************************/
	    
	  // Channel 文件
		this.channelFields1 = new LabelGridMeta(id + "_channelFields1",null, 200);
		this.channelFields1.addHeaders(new GridHeaderDataMeta[] {
				new GridHeaderDataMeta(id + "_channelFields1.fieldId", "#", null, false, 50),
				new GridHeaderDataMeta(id + "_channelFields1.ChannelCustomTags", "标签", null, false, 100),
				new GridHeaderDataMeta(id + "_channelFields1.ChannelCustomFields", "字段", null, false, 100)
						});
		
		String[] fileName = step.getChannelCustomTags();
		if(fileName != null && fileName.length > 0){
			for(int i = 0; i < fileName.length; i++){
				this.channelFields1.addRow(new Object[] {
						String.valueOf(i+1),
						new GridCellDataMeta(null,step.getChannelCustomTags()[i],
								GridCellDataMeta.CELL_TYPE_INPUT),
						new GridCellDataMeta(null,step.getChannelCustomFields()[i],
								GridCellDataMeta.CELL_TYPE_INPUT)
					
				});	
			}
		}
		this.channelFields1.setSingle(true);
		this.channelFields1.setHasBottomBar(true);
		
		this.channelFields1.setHasAdd(true, true, 
				"jQuery.imeta.steps.rssoutput.btn.channelFields1Add");
		this.channelFields1.setHasDelete(true,true,"jQuery.imeta.parameter.fieldsDelete");
		
		this.getFields = new ButtonMeta(id + ".btn.getFields", id
				+ ".btn.getFields", "获取字段", "获取字段");
		this.getFields.setButtonWidthStyle(20);
	
	
		// Item fields
		this.itemFields1 = new LabelGridMeta(id + "_itemFields1",null, 200);
		
		this.itemFields1.addHeaders(new GridHeaderDataMeta[] {
				new GridHeaderDataMeta(id + "_itemFields1.fieldId", "#", null, false, 50),
				new GridHeaderDataMeta(id + "_itemFields1.ItemCustomTags", "标签", null, false, 100),
				new GridHeaderDataMeta(id + "_itemFields1.ItemCustomFields", "字段", null, false, 100)
						});

		String[] fileName1 = step.getItemCustomTags();
		if(fileName1 != null && fileName1.length > 0){
			for(int i = 0; i < fileName1.length; i++){
				this.itemFields1.addRow(new Object[] {
						String.valueOf(i+1),
						new GridCellDataMeta(null,step.getItemCustomTags()[i],
								GridCellDataMeta.CELL_TYPE_INPUT),
						new GridCellDataMeta(null,step.getItemCustomFields()[i],
								GridCellDataMeta.CELL_TYPE_INPUT)
					
				});	
			}
		}
		
		this.itemFields1.setSingle(true);			
		this.itemFields1.setHasBottomBar(true);
		
		this.itemFields1.setHasAdd(true, true, 
				"jQuery.imeta.steps.rssoutput.btn.itemFields1Add");
		this.itemFields1.setHasDelete(true,true,"jQuery.imeta.parameter.fieldsDelete");
		
		this.getFieldsItem = new ButtonMeta(id + ".btn.getFieldsItem", id
				+ ".btn.getFieldsItem", "获取字段", "获取字段");
		
		this.getFieldsItem.setButtonWidthStyle(20);

		this.meta.putTabContent(2, new BaseFormMeta[] { this.channelFields1,
														this.itemFields1
													});
			
			/*******************************************************************
			 * 标签3---------Custom Namespace
			 ******************************************************************/
	// Custom namespaces
		
		this.namespaces = new LabelGridMeta(id + "_namespaces",null, 200);
		
		this.namespaces.addHeaders(new GridHeaderDataMeta[] {
				new GridHeaderDataMeta(id + "_namespaces.fieldId", "#", null, false, 50),
				new GridHeaderDataMeta(id + "_namespaces.NameSpacesTitle", "字段", null, false, 100),
				new GridHeaderDataMeta(id + "_namespaces.NameSpaces", "标签", null, false, 100)
						});
		
		String[] fileName2 = step.getNameSpacesTitle();
		if(fileName2 != null && fileName2.length > 0){
			for(int i = 0; i < fileName2.length; i++){
				this.namespaces.addRow(new Object[] {
						String.valueOf(i+1),
						new GridCellDataMeta(null,step.getNameSpacesTitle()[i],
								GridCellDataMeta.CELL_TYPE_INPUT),
						new GridCellDataMeta(null,step.getNameSpaces()[i],
								GridCellDataMeta.CELL_TYPE_INPUT)
					
				});	
			}
		}
		
		this.namespaces.setSingle(true);
		this.namespaces.setHasBottomBar(true);
		
		this.namespaces.setHasAdd(true, true, 
				"jQuery.imeta.steps.rssoutput.btn.namespacesAdd");
		
		this.namespaces.setHasDelete(true,true,"jQuery.imeta.parameter.fieldsDelete");
			
		this.meta.putTabContent(3, new BaseFormMeta[] { this.namespaces });
			

			/*******************************************************************
			 * 标签4---------Output File
			 ******************************************************************/
		//	Output File	
		this.outputFile = new ColumnFieldsetMeta(null, "输出文件");
		this.outputFile.setSingle(true);
		
		//---fileName
		this.fileName = new LabelInputMeta(id + ".fileName", "文件名:", null, null,
				" ", step.getFileName(), 
				null, ValidateForm
				.getInstance().setRequired(false));

       this.fileName.setSingle(true);
       
		
		//---1  Create Parent folder
		this.createparentfolder = new LabelInputMeta(id + ".createparentfolder", "创建父文件夹", null,
						 null, null,
						 String.valueOf(step.isCreateParentFolder()),
						 InputDataMeta.INPUT_TYPE_CHECKBOX,
						ValidateForm.getInstance().setRequired(false));
		this.createparentfolder.setSingle(true);
		
		//---2  fileName defined in a field
		this.isfilenameinfield = new LabelInputMeta(id + ".isfilenameinfield", "默认文件名", null,
						 null, null,
						 String.valueOf(step.isFilenameInField()),
						 InputDataMeta.INPUT_TYPE_CHECKBOX,
						 ValidateForm.getInstance().setRequired(false));
		this.isfilenameinfield.setSingle(true);
		
		//---fileName field
		
			this.filenamefield = new LabelSelectMeta(id + ".filenamefield","其他文件名：",
					null,null,null,
					step.getFileNameField(),
					null,super.getPrevStepResultFields());

			this.filenamefield.setSingle(true);
			
		//---扩展名 Extension
		this.extension = new LabelInputMeta(id + ".extension", "扩展名:", null, null,"xml", 
				step.getExtension(), 
				null, ValidateForm.getInstance().setRequired(false));

		this.extension.setSingle(true);
		
		//---3  Include stepnr in fileName
		this.stepNrInFilename = new LabelInputMeta(id + ".stepNrInFilename", "在文件名里包含步骤 ", null,
						 null, null,
						 String.valueOf(step.isStepNrInFilename()),
						 InputDataMeta.INPUT_TYPE_CHECKBOX,
						ValidateForm.getInstance().setRequired(false));
        this.stepNrInFilename.setSingle(true);
		
		//---4  Include date in fileName
		this.dateInFilename = new LabelInputMeta(id + ".dateInFilename", "在文件名中包含日期", null,
						 null, null,
						 String.valueOf(step.isDateInFilename()),
						 InputDataMeta.INPUT_TYPE_CHECKBOX,
						ValidateForm.getInstance().setRequired(false));
		this.dateInFilename.setSingle(true);
		
		//---5  Include time in fileName
		this.timeInFilename = new LabelInputMeta(id + ".timeInFilename", "在文件名中包含当前时间", null,
						 null, null,
						 String.valueOf(step.isTimeInFilename())
						 ,InputDataMeta.INPUT_TYPE_CHECKBOX,
						ValidateForm.getInstance().setRequired(false));
		this.timeInFilename.setSingle(true);
				
		this.outputFile.putFieldsetsContent(new BaseFormMeta[] {
				                    this.fileName , 
                                    this.createparentfolder,  
                                    this.isfilenameinfield, 
                                    this.filenamefield,  
                                    this.extension, 
                                    this.stepNrInFilename,  
                                    this.dateInFilename, 
                                    this.timeInFilename
				});

		//		Result fileName	
		this.resultFilename = new ColumnFieldsetMeta(null, "结果文件名");
		this.resultFilename.setSingle(true);
		
		//---6 Add File to result
		this.AddToResult = new LabelInputMeta(id + ".AddToResult", "将文件添加到结果", null,
						 null, null,
						 String.valueOf(step.AddToResult())
						 ,InputDataMeta.INPUT_TYPE_CHECKBOX,
						ValidateForm.getInstance().setRequired(false));
		this.AddToResult.setSingle(true);
		
		this.resultFilename.putFieldsetsContent(new BaseFormMeta[] {
				this.AddToResult
				});
		
		this.meta.putTabContent(4, new BaseFormMeta[] { this.outputFile
														,this.resultFilename });	
			
		//-----------------------------------------页签完结-----------------------
			// 装载到form
			columnFormMeta.putFieldsetsContent(new BaseFormMeta[] { this.name,
					this.meta });

			// 确定，取消
			columnFormMeta.putFieldsetsFootButtons(new ButtonMeta[] {
					super.getOkBtn(), super.getCancelBtn() });
			
			cArr.add(columnFormMeta.getFormJo());

			rtn.put("items", cArr);
			rtn.put("title", super.getStepMeta().getName());
			return rtn;
		}catch(Exception ex) {
			throw new ImetaException(ex.getMessage(), ex);
		}
	}
}
