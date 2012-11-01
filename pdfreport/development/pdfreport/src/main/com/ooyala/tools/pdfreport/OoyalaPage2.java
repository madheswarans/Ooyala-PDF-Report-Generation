/**
 * Copyright (c) Ooyala Inc, 2012
 * Author     : Madheswaran
 * Version    : $Revision: 1.38 $
 * Updated-On : $Date: 2012/10/16 13:32:54 $
 *
 * Purpose:
 * A program to hold various constants required for the tool. 
 *
 * Change History:
 * 2012-08-07, Madheswaran: Initial program.
 */
package com.ooyala.tools.pdfreport;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.TreeMap;

import javax.imageio.ImageIO;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import org.eclipse.birt.core.framework.Platform;
import org.eclipse.birt.report.model.api.CellHandle;
import org.eclipse.birt.report.model.api.DesignConfig;
import org.eclipse.birt.report.model.api.DesignElementHandle;
import org.eclipse.birt.report.model.api.ElementFactory;
import org.eclipse.birt.report.model.api.GridHandle;
import org.eclipse.birt.report.model.api.IDesignEngine;
import org.eclipse.birt.report.model.api.IDesignEngineFactory;
import org.eclipse.birt.report.model.api.ImageHandle;
import org.eclipse.birt.report.model.api.ReportDesignHandle;
import org.eclipse.birt.report.model.api.RowHandle;
import org.eclipse.birt.report.model.api.SessionHandle;
import org.eclipse.birt.report.model.api.StyleHandle;
import org.eclipse.birt.report.model.api.TextItemHandle;
import org.eclipse.birt.report.model.api.activity.SemanticException;
import org.eclipse.birt.report.model.api.elements.DesignChoiceConstants;
import org.eclipse.birt.report.model.elements.interfaces.ISimpleMasterPageModel;
import org.eclipse.core.runtime.Path;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import com.ibm.icu.util.ULocale;

/**
 * Simple BIRT Design Engine API (DEAPI) demo.
 */
public class OoyalaPage2 
{
	private ElementFactory elementFactory = null;
	private ReportDesignHandle design = null;
	private static String inputFileName=System.getProperty("pdf.report.config.file");
	File fXmlFile = new File(inputFileName);
	//File fXmlFile = new File("input//QOS_report.xml");
	DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
	DocumentBuilder dBuilder = null;
	Document doc = null;
	MergeReports mergeReports = null;
	String inputPath																		 = null;
	
	
	
	public void init()
	{
       // Create a session handle. This is used to manage all open designs.
       // Your app need create the session only once.
 		
       //Configure the Engine and start the Platform 
       DesignConfig config = new DesignConfig( );
       config.setProperty("BIRT_HOME", "./lib/birt-runtime-3_7_2/birt-runtime-3_7_2/ReportEngine");
       IDesignEngine engine = null;
       
       try
       {
           Platform.startup( config );
           IDesignEngineFactory factory = (IDesignEngineFactory) Platform.createFactoryObject( IDesignEngineFactory.EXTENSION_DESIGN_ENGINE_FACTORY );
           engine = factory.createDesignEngine( config );
			dBuilder = dbFactory.newDocumentBuilder();
			doc = dBuilder.parse(fXmlFile);
			doc.getDocumentElement().normalize();           
       }
       catch( Exception ex )
       {
           ex.printStackTrace();
       }
	       
		SessionHandle session = engine.newSessionHandle( ULocale.ENGLISH ) ;
	
       // Create a new report design.
       design = session.createDesign( );
       
       // The element factory creates instances of the various BIRT elements.
       elementFactory = design.getElementFactory( );
	}
	
	   // This method shows how to build a very simple BIRT report with a
	   // minimal set of content: a simple grid with an image and a label.
	   public void buildReport(ArrayList aListTempLHS,ArrayList aListTempRHS,String inputPath ) throws IOException, SemanticException
	   {
//			inputPath = Path.SEPARATOR+inputPath;
			setInputPath(inputPath);
		   ApplicationConstants.APP_LOGGER.info("ooyal page2");
		   init();

	       // Create a simple master page that describes how the report will appear when printed.
	       // Note: The report will fail to load in the BIRT designer unless you create a master page.
		   try{
	       DesignElementHandle element = elementFactory.newSimpleMasterPage( "Page Master" ); 
	       
		element.setProperty("headerHeight", "0.7in");
		element.setProperty("footerHeight", "0.5in");
	       design.getMasterPages( ).add( element );
	       
	       TreeMap<String,String> tmapMiscValues = getMiscValues();
	       
	       // Create a grid and add it to the "body" slot of the report design.
	       GridHandle grid = elementFactory.newGridItem( null, 1 /* cols */, 25 /* row */ );
		   design.getBody( ).add( grid );
			
		   ApplicationConstants.APP_LOGGER.info("ooyal 111");
			//font for TEXT - starts - css styles declarations
			StyleHandle textStyle = elementFactory.newStyle( "textStyle" ); 
			textStyle.setProperty(StyleHandle.FONT_FAMILY_PROP, "Helvetica" );
			textStyle.setProperty(StyleHandle.COLOR_PROP, "#636466" );
			textStyle.setProperty(StyleHandle.FONT_SIZE_PROP,"15");
	        design.getStyles().add(textStyle);
	        
			StyleHandle textAlignRightStyle = elementFactory.newStyle( "textAlignRightStyle" ); 
			textAlignRightStyle.setProperty(StyleHandle.FONT_FAMILY_PROP, "Helvetica" );
			textAlignRightStyle.setProperty(StyleHandle.COLOR_PROP, "#636466" );
			textAlignRightStyle.setProperty(StyleHandle.MARGIN_TOP_PROP,"14");
			textAlignRightStyle.setProperty(StyleHandle.FONT_WEIGHT_PROP,"bold");
			textAlignRightStyle.setProperty(StyleHandle.FONT_SIZE_PROP,"11");
			
			textAlignRightStyle.setProperty(StyleHandle.TEXT_ALIGN_PROP,DesignChoiceConstants.TEXT_ALIGN_RIGHT);
	        design.getStyles().add(textAlignRightStyle);	       
	        
	        

				StyleHandle textPublisherAlignRightStyle = elementFactory

					.newStyle("textPublisherAlignRightStyle");

					textPublisherAlignRightStyle

					.setProperty(StyleHandle.FONT_FAMILY_PROP, "Helvetica");

					textPublisherAlignRightStyle.setProperty(StyleHandle.COLOR_PROP, tmapMiscValues.get("color"));

					textPublisherAlignRightStyle.setProperty(StyleHandle.FONT_SIZE_PROP, "48");

					textPublisherAlignRightStyle.setProperty(StyleHandle.TEXT_ALIGN_PROP,

					DesignChoiceConstants.TEXT_ALIGN_LEFT);

					design.getStyles().add(textPublisherAlignRightStyle);
					
					StyleHandle textDateRightStyle = elementFactory

							.newStyle("textDateRightStyle");

							textDateRightStyle

							.setProperty(StyleHandle.FONT_FAMILY_PROP, "Helvetica");

							textDateRightStyle.setProperty(StyleHandle.COLOR_PROP, "#636466");

							textDateRightStyle.setProperty(StyleHandle.FONT_SIZE_PROP, "16");

							textDateRightStyle.setProperty(StyleHandle.TEXT_ALIGN_PROP,

							DesignChoiceConstants.TEXT_ALIGN_LEFT);

							design.getStyles().add(textDateRightStyle);

			StyleHandle textHeadingStyle = elementFactory.newStyle("textHeadingStyle");
			textHeadingStyle.setProperty(StyleHandle.FONT_FAMILY_PROP,
					"Helvetica");
			textHeadingStyle.setProperty(StyleHandle.COLOR_PROP, "#636466");
			textHeadingStyle.setProperty(StyleHandle.FONT_SIZE_PROP, "20");
			design.getStyles().add(textHeadingStyle);			
			//font for TEXT - ends
			  				
		// header - starts
		GridHandle headerGrid = design.getElementFactory().newGridItem(
				null/* name */, 1 /* cols */, 2 /* row */);
		RowHandle rowHeader1 = (RowHandle) headerGrid.getRows().get(0);
		CellHandle cellFooterImage = (CellHandle) rowHeader1.getCells().get(0);
		GridHandle header_inner_grid = design.getElementFactory().newGridItem(
				null, 2 /* cols */, 1 /* row */);
		cellFooterImage.getContent().add(header_inner_grid);

		RowHandle rowFooterOoyalaImg = (RowHandle) header_inner_grid.getRows()
				.get(0);
		CellHandle cellTitle_header0 = (CellHandle) rowFooterOoyalaImg
				.getCells().get(0);
		ImageHandle imageTitle_header0 = elementFactory.newImage(null);
		imageTitle_header0.setProperty("height", "0.40625in");
		imageTitle_header0.setProperty("width", "2.0729166666666665in");
		
		imageTitle_header0.setFile("" + inputPath + Path.SEPARATOR
				+ ApplicationConstants.IMAGE_OOYALALOGO);
		
		cellTitle_header0.getContent().add(imageTitle_header0);

		CellHandle cellTitle1_header = (CellHandle) rowFooterOoyalaImg
				.getCells().get(1);
		TextItemHandle reportTitle_header = elementFactory.newTextItem(null);
		reportTitle_header
				.setContent(ApplicationConstants.FIRST_PAGE_REPORT_NAME);
		reportTitle_header.setProperty("style", "textAlignRightStyle");
		reportTitle_header.setProperty("contentType", "html");
		cellTitle1_header.getContent().add(reportTitle_header);


		RowHandle rowHeader0 = (RowHandle) headerGrid.getRows().get(1);
//		CellHandle cellHeaderLine = (CellHandle) rowHeader0.getCells().get(0);
//		ImageHandle imageHeaderLine = elementFactory.newImage("image1");
//		cellHeaderLine.getContent().add(imageHeaderLine);
//
//		imageHeaderLine.setProperty("width", "8.522916666666667in");
//
//		imageHeaderLine.setFile(ApplicationConstants.CONFIG_FOLDER
//				+ Path.SEPARATOR + ApplicationConstants.OOYALA_IMAGE_LINE);	
		
		ChartTitleHandle chartTitleHandle = new ChartTitleHandle();
		rowHeader0 = chartTitleHandle.getThinCustomLine(design,rowHeader0,tmapMiscValues.get("color"),"header");
		
		element.getSlot(ISimpleMasterPageModel.PAGE_HEADER_SLOT)
		.add(headerGrid);		
		// header - ends		
							
	       
//	       CellHandle cellHandleTitle = grid.getCell(1, 0);
//	       GridHandle gridHandleTitle = elementFactory.newGridItem( null, 2 /* cols */, 1 /* row */ );
//	       cellHandleTitle.getContent().add(gridHandleTitle);
//	       ApplicationConstants.APP_LOGGER.info("ooyal 333");
//	       RowHandle rowTitle = (RowHandle) gridHandleTitle.getRows( ).get( 0 );	
	      //  rowTitle.setProperty("height","0.5129166666666666in");
	       
//	       CellHandle cellTitle0 = (CellHandle) rowTitle.getCells( ).get( 0 );
//	       ImageHandle imageTitle0 = elementFactory.newImage( null );
//	       cellTitle0.getContent( ).add( imageTitle0 );	
//	      // imageTitle0.setProperty("height","0.46875in");
//	       //imageTitle0.setProperty("width","2.00000in");
//	    
//	       imageTitle0.setFile(""+getInputPath()+Path.SEPARATOR+(String) tmapMiscValues.get("ooyalalogo"));
////	       imageTitle0.setFile(""+"input//"+(String) tmapMiscValues.get("ooyalalogo"));
//	      
//	       CellHandle cellTitle1 = (CellHandle) rowTitle.getCells( ).get( 1 );
//	       TextItemHandle reportTitle = elementFactory.newTextItem(null);
//	       reportTitle.setContent("\n"+ApplicationConstants.REPORT_NAME);
//	       reportTitle.setProperty("style", "textAlignRightStyle");
//	       cellTitle1.getContent( ).add( reportTitle );	
//	      
//	       RowHandle rowLine = (RowHandle) grid.getRows( ).get( 1 );
//	       CellHandle cellLine0 = (CellHandle) rowLine.getCells( ).get( 0 );
//	       
//	       ImageHandle imageLine = elementFactory.newImage( null );
//	       cellLine0.getContent( ).add( imageLine );	
//	       imageLine.setProperty("width","8.522916666666667in");
//	       imageLine.setFile(""+ApplicationConstants.CONFIG_FOLDER+Path.SEPARATOR+ApplicationConstants.OOYALA_IMAGE_LINE);
//	       //imageLine.setFile(""+"config//"+ApplicationConstants.OOYALA_IMAGE_LINE);
//
//	       //imageLine.setFile("input//line.PNG");
//	       
	       RowHandle rowLogoImage = (RowHandle)grid.getRows().get(2);
	       ImageHandle image2 = elementFactory.newImage( null );
	       CellHandle cellLogoImage = (CellHandle)rowLogoImage.getCells( ).get( 0 );
	       image2.setFile(""+getInputPath()+Path.SEPARATOR+(String) tmapMiscValues.get("image"));
	      // image2.setFile(getInputPath()//"+(String) tmapMiscValues.get("image"));
	       BufferedImage bimg = ImageIO.read(new File(getInputPath()+Path.SEPARATOR+(String) tmapMiscValues.get("image")));
           int imageWidth=bimg.getWidth();
           int imageHeight=bimg.getHeight();
           if(imageHeight>=130)
           {
        	   image2.setProperty("height", "1.73in");
       	      
           }
           cellLogoImage.getContent( ).add( image2 );

           
	       ApplicationConstants.APP_LOGGER.info("ooyal ");
	      // image2.setProperty("height","0.55in");
	     //  image2.setProperty("width","1.04in");
	       
//	       RowHandle rowOoSLabel = (RowHandle)grid.getRows().get(3);
	      // rowOoSLabel.setProperty(propName, value)
	       //rowOoSLabel.setProperty("height", "0.1875in");
//	       File headingLabelImgsource = new File(ApplicationConstants.CONFIG_FOLDER+Path.SEPARATOR+ApplicationConstants.HEADING_LABEL_IMG);
//	       Image headingLabelImg = ImageIO.read(headingLabelImgsource);
//	       BufferedImage headingLabelBuffImg=bufferImage(headingLabelImg);
	       
//	       Graphics oOsLabelGraphics1 = headingLabelBuffImg.createGraphics();
//		   Font fnt1=new Font("Helvetica Neue Condensed Bold",1,48);//setting Font size and style
//		   Color fntC1 = new Color(0,181,204);//setting font color
//			
//		   oOsLabelGraphics1.setColor(fntC1);
//		   oOsLabelGraphics1.setFont(fnt1);
//		   oOsLabelGraphics1.drawString((String) tmapMiscValues.get("publisher"),1,38); 
//		   ApplicationConstants.APP_LOGGER.info("ooyal 666");
//		   //
//		   Graphics oOsLabelGraphics2 = headingLabelBuffImg.createGraphics();
//		   Font fnt2=new Font("Helvetica Neue Regular",1,16);//setting Font size and style
//		   
//		   Color fntC2 = new Color(65,65,65);//setting font color
//		   oOsLabelGraphics2.setColor(fntC2);
//		   oOsLabelGraphics2.setFont(fnt2);	   
		   
		 //  oOsLabelGraphics2.drawString((String) tmapMiscValues.get("date"),1,68);
		  //
		   
		   RowHandle row2_ReportHeading = (RowHandle)grid.getRows().get(2);
			CellHandle cell_ReportHeading = (CellHandle)row2_ReportHeading.getCells().get(0);
			TextItemHandle textItem_ReportHeading = elementFactory.newTextItem(null);
			textItem_ReportHeading.setContent((String) tmapMiscValues.get("title"));
//			textItem_ReportHeading.setProperty("fontSize", "48pt");
//			textItem_ReportHeading.setProperty("fontFamily", "48pt");
			
			textItem_ReportHeading.setProperty("style", "textPublisherAlignRightStyle");
			cell_ReportHeading.getContent().add(textItem_ReportHeading);

		       RowHandle row3_ReportDate = (RowHandle)grid.getRows().get(3);
				CellHandle cell_ReportDate = (CellHandle)row3_ReportDate.getCells().get(0);
				TextItemHandle textItem_ReportDate = elementFactory.newTextItem(null);
				textItem_ReportDate.setContent((String) tmapMiscValues.get("date"));

				textItem_ReportDate.setProperty("style", "textDateRightStyle");
				cell_ReportDate.getContent().add(textItem_ReportDate);
				


		   
		   
		   
		   //
		   
		   
//		   File oOsLabelFile = new File(ApplicationConstants.CONFIG_FOLDER+Path.SEPARATOR+ApplicationConstants.HEADING_LABEL_IMG);
//		   ImageIO.write(headingLabelBuffImg, "png", oOsLabelFile);		
//		   ApplicationConstants.APP_LOGGER.info("ooyal 9999");
//	       
//	       ImageHandle image3 = elementFactory.newImage( "iamge 1" );
//	       CellHandle cellHeadingImage = (CellHandle)rowOoSLabel.getCells( ).get( 0 );
//	       cellHeadingImage.getContent( ).add( image3 );
//	       image3.setFile(ApplicationConstants.CONFIG_FOLDER+Path.SEPARATOR+ApplicationConstants.HEADING_LABEL_IMG);
//	      
//	      
//	       GridHandle grid1 = elementFactory.newGridItem( null, 2 /* cols */, 1 /* row */ );
//	      
//	       ApplicationConstants.APP_LOGGER.info("ooyal 777");
//	       RowHandle row = (RowHandle) grid1.getRows( ).get( 0 );
//	       CellHandle cellLeftTitle = (CellHandle) row.getCells( ).get( 0 );
//	        TextItemHandle textItem = design.getElementFactory().newTextItem("test1");
//	        textItem.setContentType("auto");
//	        textItem.setContent(leftTilteHeader);
//	        textItem.setProperty("textAlign","left");
//	        textItem.setProperty("fontSize","15pt");
//	        textItem.setProperty("color","gray");
//	        //  textItem.setProperty("style", "textAlignRightStyle");
//	        cellLeftTitle.getContent().add(textItem);
//	        ApplicationConstants.APP_LOGGER.info("ooyal 8888");
//	       
//	        CellHandle cellRightTitle1 = (CellHandle) row.getCells( ).get( 1 );
//	        TextItemHandle textItem1 = design.getElementFactory().newTextItem("test2");
//	        textItem1.setContentType("auto");
//
//	        textItem1.setContent(rightTilteHeader);
//	        textItem1.setProperty("textAlign","left");
//	        textItem1.setProperty("fontSize","15pt");
//	        textItem1.setProperty("color","gray");
//	        textItem1.setProperty("textAlign","left");
//	        
//	        //  textItem.setProperty("style", "textAlignRightStyle");
//	        cellRightTitle1.getContent().add(textItem1);
	       	       
				
//				String leftHeader = (String)aListTempLHS.get(0);
//				String strLHS = (String)aListTempLHS.get(1);
//				ArrayList aListBlocks = (ArrayList)aListTempLHS.get(2);
				

				
		// Get second row - starts
		RowHandle row2 = (RowHandle) grid.getRows().get(3);
		CellHandle cellItemsAndBlocks = (CellHandle) row2.getCells().get(0);
		GridHandle cellItemsAndBlocks_inner_grid = design.getElementFactory()
				.newGridItem(null, 2 /* cols */, 2 /* row */);
		cellItemsAndBlocks.getContent().add(cellItemsAndBlocks_inner_grid);
		
		ChartBodyContentHandle chartBodyContentHandle = new ChartBodyContentHandle();

		String leftHeader = (String)aListTempLHS.get(0);
		String strLHS = (String)aListTempLHS.get(1);
		ArrayList aListBlocks = (ArrayList)aListTempLHS.get(2);
		// left hand side heading - starts
		RowHandle rowItemsAndBlocksTitle = (RowHandle) cellItemsAndBlocks_inner_grid
				.getRows().get(0);
		CellHandle rowItemsAndBlocksTitle_cell0 = (CellHandle) rowItemsAndBlocksTitle.getCells().get(0);
		rowItemsAndBlocksTitle_cell0 = chartBodyContentHandle.getBodyHeading(rowItemsAndBlocksTitle_cell0,elementFactory,leftHeader,"textHeadingStyle");
		// left hand side heading - ends
		
		// left hand side - starts
		RowHandle rowItemsAndBlocks = (RowHandle) cellItemsAndBlocks_inner_grid.getRows().get(1);
		CellHandle row2_cell1 = (CellHandle) rowItemsAndBlocks.getCells().get(0);
			
		GridHandle innergrid_LHS = chartBodyContentHandle.getBodyContent(design,elementFactory,strLHS,"textStyle",aListBlocks);
		row2_cell1.getContent().add(innergrid_LHS);
		// left hand side - ends
		
		// right hand side heading - starts
		String rightHeader = (String)aListTempRHS.get(0);
		String strRHS = (String)aListTempRHS.get(1);
		ArrayList aListItems = (ArrayList)aListTempRHS.get(2);						
		CellHandle rowItemsAndBlocksTitle_cell1 = (CellHandle) rowItemsAndBlocksTitle.getCells().get(1);
		rowItemsAndBlocksTitle_cell1 = chartBodyContentHandle.getBodyHeading(rowItemsAndBlocksTitle_cell1,elementFactory,rightHeader,"textHeadingStyle");
		// right hand side heading - ends

		// right hand side - starts
		CellHandle row2_cell2 = (CellHandle) rowItemsAndBlocks.getCells().get(1);
		GridHandle innergrid_RHS = chartBodyContentHandle.getBodyContent(design,elementFactory,strRHS,"textStyle",aListItems);
		row2_cell2.getContent().add(innergrid_RHS);
		// right hand side - ends
		// Get second row - ends
				
				
	        //design.getBody( ).add( grid1 );
	       

	       // Save the design and close it.
	       design.saveAs( ApplicationConstants.REPORT_FOLDER+Path.SEPARATOR+"OoyalaPage2.rptdesign" ); 
	       
			mergeReports = new MergeReports();
			mergeReports.getOoyalaPage2Path(ApplicationConstants.REPORT_FOLDER+Path.SEPARATOR+"OoyalaPage2.rptdesign" ); 
			
	       design.close( );
	       
	     
		   }catch(Exception e){
			   System.out.println("exception....");
			   e.printStackTrace();
			   ApplicationConstants.APP_LOGGER.info(" Error occured  page2 file"+e.getMessage());
		   }
	       // We're done!
	   }	
	   
   
	   

		
		public static BufferedImage bufferImage(Image image) 
		{
			return bufferImage(image,BufferedImage.TYPE_INT_RGB);
		}
		
		public static BufferedImage bufferImage(Image image, int type) 
		{
			BufferedImage bufferedImage = new BufferedImage(image.getWidth(null), image.getHeight(null), type);
			Graphics2D g = bufferedImage.createGraphics();
			g.drawImage(image, null, null);
			return bufferedImage;
		} 	   

		
		public TreeMap<String,String> getMiscValues()
		{
			ArrayList<Object> aListMisc = new ArrayList<Object>();
			TreeMap<String,String> tmap = new TreeMap<String,String>();
			
			
			try
			{
				NodeList nList = doc.getElementsByTagName("head");
				

				for (int temp = 0; temp < nList.getLength(); temp++) 
				{
			   	  Node nNode = nList.item(temp);
			      Element eElement = (Element) nNode;
			      
			      NodeList refNodes = nNode.getChildNodes();
			      //System.out.println("# of chidren are " + refNodes.item(0));

			      
			      String title = getTagValue("title",eElement);
			      String publisher = getTagValue("publisher",eElement);
			      String color = getTagValue("color",eElement);
			      String image = getTagValue("image",eElement);
			      String date = getTagValue("date",eElement);
			      String ooyalalogo = getTagValue("ooyalalogo",eElement);
			      
			      tmap.put("title", title);
			      tmap.put("publisher", publisher);
			      tmap.put("color", color);
			      tmap.put("image", image);
			      tmap.put("date", date);
			      tmap.put("ooyalalogo", ooyalalogo);
			      
			      aListMisc.add(title);
			      aListMisc.add(date);
			      
				}
			}
			catch(Exception e)
			{
				
			}
			
			return tmap;
		}
		
		
		
		
		public ArrayList<Object> getTableOfContentsValues()
		{
		  ArrayList<Object> aListResultant = new ArrayList<Object>();
		  try 
		  {	 
			NodeList nList = doc.getElementsByTagName("drilldown");
			for (int temp = 0; temp < nList.getLength(); temp++) 
			{
		   	  Node nNode = nList.item(temp);
		      Element eElement = (Element) nNode;
		      NodeList nList1 = eElement.getElementsByTagName("graph");
		      
		      for (int temp1 = 0; temp1 < nList1.getLength(); temp1++) 
		      {
		    	  Node nNode1 = nList1.item(temp1);
		    	  Element eElement1 = (Element) nNode1;
		    	  
		    	  aListResultant.add(eElement1.getAttribute("src"));
		    	  
		    	  NodeList nList2 = eElement1.getElementsByTagName("text");
		    	  for (int i=0;i<nList2.getLength();i++)
		    	  {
		    		  Node nNode2 = nList2.item(i);
		    		  Element eElement2 = (Element) nNode2;
		    		  
		    		  NodeList nList3 = eElement2.getElementsByTagName("left");
		    		  for (int j=0;j<nList3.getLength();j++)
		    		  {
		    			  Node nNode3 = nList3.item(j);
		    			  Element eElement3 = (Element) nNode3;
		    			  
		    			  String header = getTagValue("header",eElement3);
		    			  aListResultant.add(header);
		    			  
		    			  ArrayList aListBlocks  = getTagValues("block",eElement3,eElement3.getElementsByTagName("block").getLength());
		    			  aListResultant.add(aListBlocks);
		    		  }
		    		  
		    		  NodeList nList4 = eElement2.getElementsByTagName("right");
		    		  for (int k=0;k<nList4.getLength();k++)
		    		  {
		    			  Node nNode4 = nList4.item(k);
		    			  Element eElement4 = (Element) nNode4;
		    			  
		    			  String header = getTagValue("header",eElement4);
		    			  aListResultant.add(header);
		    			  
		    			  ArrayList<String> aListItems = getTagValues("item",eElement4,eElement4.getElementsByTagName("item").getLength());
		    			 aListResultant.add(aListItems);
		    			
		    		  }
		    		  
		    		  
		    	  }
		    	  
		      }
			}
		  } 
		  catch (Exception e) 
		  {
			e.printStackTrace();
		  }			
			  
			  
			  return aListResultant;
		}
		
		
		
		
		
		  public static String getTagValue(String sTag, Element eElement) {
				NodeList nlList = eElement.getElementsByTagName(sTag).item(0).getChildNodes();
			 
			        Node nValue = (Node) nlList.item(0);
			 
				return nValue.getNodeValue();
			  }
		  
		  public static ArrayList<String> getTagValues(String sTag, Element eElement,int count) {
			  ArrayList<String> aListValues = new ArrayList<String>();
			  
			  for (int i=0;i<count;i++)
			  {
				  NodeList nlList = eElement.getElementsByTagName(sTag).item(i).getChildNodes();
				  Node nValue = (Node) nlList.item(0);
				  String str = nValue.getNodeValue();
				  aListValues.add(str);
			  }

			 
				return aListValues;
			  }

		/**
		 * @return the inputPath
		 */
		public String getInputPath() {
			return inputPath;
		}

		/**
		 * @param inputPath the inputPath to set
		 */
		public void setInputPath(String inputPath) {
			this.inputPath = inputPath;
		}		
		  
		  
}

