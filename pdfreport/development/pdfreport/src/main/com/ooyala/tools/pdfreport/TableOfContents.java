/**
 * Copyright (c) Ooyala Inc, 2012
 * Author     : Anand
 * Version    : $Revision: 1.30 $
 * Updated-On : $Date: 2012/09/27 10:38:40 $
 *
 * Purpose:
 * A program to hold various constants required for the tool. 
 * Change History:
 * 2012-08-07, Anand: Initial program.
 */
package com.ooyala.tools.pdfreport;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.TreeMap;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import org.eclipse.birt.core.framework.Platform;
import org.eclipse.birt.report.model.api.CellHandle;
import org.eclipse.birt.report.model.api.ColumnHandle;
import org.eclipse.birt.report.model.api.DesignConfig;
import org.eclipse.birt.report.model.api.DesignElementHandle;
import org.eclipse.birt.report.model.api.ElementFactory;
import org.eclipse.birt.report.model.api.GridHandle;
import org.eclipse.birt.report.model.api.IDesignEngine;
import org.eclipse.birt.report.model.api.IDesignEngineFactory;
import org.eclipse.birt.report.model.api.ImageHandle;
import org.eclipse.birt.report.model.api.LabelHandle;
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
public class TableOfContents 
{
	private ElementFactory elementFactory = null;
	private ReportDesignHandle design = null;
	private static String inputFileName=System.getProperty("pdf.report.config.file");
	String inputPath																		 = null;
	
  	
  	File fXmlFile = new File(inputFileName);
//  	File fXmlFile = new File(".//input//QOS_report.xml");
  	DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
	DocumentBuilder dBuilder = null;
	Document doc = null;
	MergeReports mergeReports = null;
	private TreeMap<String,String> tmapPagesAndNumber = null;
	
	
	public void init()
	{
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
	
	public void buildReport(String inputPath) throws IOException, SemanticException
	{
		init();
		ArrayList<String> aListResultant = getTableOfContentsValues();
		TreeMap<String,String> tmapMiscValues = getMiscValues();
	   
		// Create a simple master page that describes how the report will appear when printed.
		// Note: The report will fail to load in the BIRT designer unless you create a master page.
		DesignElementHandle element = elementFactory.newSimpleMasterPage( "Page Master" );       
		design.getMasterPages( ).add( element );
	   
		// Create a grid and add it to the "body" slot of the report design.
		GridHandle grid = elementFactory.newGridItem( null, 1 /* cols */, aListResultant.size()+1 /* row */ );
		element.setProperty("footerHeight","0.5in");
		design.getBody( ).add( grid );
					
		StyleHandle tableOfContentStyle = elementFactory.newStyle( "tableOfContentStyle" ); 
		//labelStyle.setProperty( StyleHandle.FONT_WEIGHT_PROP,
		//DesignChoiceConstants.FONT_WEIGHT_BOLD );
		tableOfContentStyle.setProperty(StyleHandle.FONT_FAMILY_PROP, "Helvetica" );
		tableOfContentStyle.setProperty(StyleHandle.COLOR_PROP, "#636466" );
		tableOfContentStyle.setProperty(StyleHandle.FONT_SIZE_PROP,"15");
		design.getStyles().add(tableOfContentStyle);
	    
		StyleHandle chapterStyle = elementFactory.newStyle( "chapterStyle" ); 
		chapterStyle.setProperty(StyleHandle.FONT_FAMILY_PROP, "Helvetica" );
		chapterStyle.setProperty(StyleHandle.COLOR_PROP, tmapMiscValues.get("color") );//Dark Turquoise
		chapterStyle.setProperty(StyleHandle.FONT_SIZE_PROP,"12");
	    design.getStyles().add(chapterStyle);	     
	    
		StyleHandle dotDotPageNumberStyle = elementFactory.newStyle( "dotDotPageNumberStyle" ); 
		dotDotPageNumberStyle.setProperty(StyleHandle.FONT_FAMILY_PROP, "Helvetica" );
		dotDotPageNumberStyle.setProperty(StyleHandle.COLOR_PROP, "#636466" );//Dark Turquoise
		dotDotPageNumberStyle.setProperty(StyleHandle.FONT_SIZE_PROP,"12");
	    design.getStyles().add(dotDotPageNumberStyle);	        	        
		StyleHandle dotDotPageNumberRightStyle = elementFactory.newStyle( "dotDotPageNumberRightStyle" ); 
		dotDotPageNumberRightStyle.setProperty(StyleHandle.FONT_FAMILY_PROP, "Helvetica" );
		dotDotPageNumberRightStyle.setProperty(StyleHandle.COLOR_PROP, "#636466" );//Dark Turquoise
		dotDotPageNumberRightStyle.setProperty(StyleHandle.FONT_SIZE_PROP,"12");
		dotDotPageNumberRightStyle.setProperty(StyleHandle.TEXT_ALIGN_PROP,DesignChoiceConstants.TEXT_ALIGN_RIGHT);
	    design.getStyles().add(dotDotPageNumberRightStyle);	
		StyleHandle textAlignRightStyle = elementFactory.newStyle( "textAlignRightStyle" ); 
		textAlignRightStyle.setProperty(StyleHandle.FONT_FAMILY_PROP, "Helvetica" );
		textAlignRightStyle.setProperty(StyleHandle.COLOR_PROP, "#00ced1" );
		textAlignRightStyle.setProperty(StyleHandle.MARGIN_TOP_PROP,"7");
		textAlignRightStyle.setProperty(StyleHandle.FONT_SIZE_PROP,"7");
		
		textAlignRightStyle.setProperty(StyleHandle.TEXT_ALIGN_PROP,DesignChoiceConstants.TEXT_ALIGN_RIGHT);
	    design.getStyles().add(textAlignRightStyle);
	    
		StyleHandle textInsightReportAlignRightStyle = elementFactory.newStyle( "textInsightReportAlignRightStyle" ); 
		textInsightReportAlignRightStyle.setProperty(StyleHandle.FONT_FAMILY_PROP, "Helvetica" );
		textInsightReportAlignRightStyle.setProperty(StyleHandle.COLOR_PROP, "#6A6468" );
		textInsightReportAlignRightStyle.setProperty(StyleHandle.FONT_SIZE_PROP,"7");		
		
		textInsightReportAlignRightStyle.setProperty(StyleHandle.TEXT_ALIGN_PROP,DesignChoiceConstants.TEXT_ALIGN_RIGHT);
	    design.getStyles().add(textInsightReportAlignRightStyle);
	    
		StyleHandle textPublisherAlignRightStyle = elementFactory.newStyle( "textPublisherAlignRightStyle" ); 
		textPublisherAlignRightStyle.setProperty(StyleHandle.FONT_FAMILY_PROP, "Helvetica" );
		textPublisherAlignRightStyle.setProperty(StyleHandle.COLOR_PROP, "#717174" );
		textPublisherAlignRightStyle.setProperty(StyleHandle.FONT_SIZE_PROP,"7");		
		//textAlignRightStyle.setProperty(StyleHandle.MARGIN_TOP_PROP,"12");
		
		textPublisherAlignRightStyle.setProperty(StyleHandle.TEXT_ALIGN_PROP,DesignChoiceConstants.TEXT_ALIGN_RIGHT);
	    design.getStyles().add(textPublisherAlignRightStyle);	    
		//font for TEXT - ends
	    
		//footer -  starts
		GridHandle footerGrid = design.getElementFactory().newGridItem(null/* name */, 1 /* cols */, 2 /* row */);
		RowHandle rowFooter0 = (RowHandle) footerGrid.getRows( ).get( 0 );
//		CellHandle cellFooterLine = (CellHandle) rowFooter0.getCells( ).get( 0 );
//		ImageHandle imageFooterLine = elementFactory.newImage( "image1" );
//		cellFooterLine.getContent().add(imageFooterLine);
//		imageFooterLine.setProperty("width","8.522916666666667in");			
//		imageFooterLine.setFile(""+ApplicationConstants.CONFIG_FOLDER+Path.SEPARATOR+ApplicationConstants.OOYALA_IMAGE_LINE);
		ChartTitleHandle chartTitleHandle = new ChartTitleHandle();
		rowFooter0 = chartTitleHandle.getThinCustomLine(design,rowFooter0,tmapMiscValues.get("color"),"footer");
		RowHandle rowFooter1 = (RowHandle) footerGrid.getRows( ).get( 1 );
		CellHandle cellFooterImage = (CellHandle) rowFooter1.getCells( ).get( 0 );
		GridHandle footer_inner_grid = design.getElementFactory().newGridItem( null, 2 /* cols */, 1 /* row */ );
		cellFooterImage.getContent().add( footer_inner_grid);	
		
		RowHandle rowFooterOoyalaImg = (RowHandle) footer_inner_grid.getRows( ).get( 0 );
		CellHandle cellTitle_footer0 = (CellHandle) rowFooterOoyalaImg.getCells( ).get( 0 );
		ImageHandle imageTitle_footer0 = elementFactory.newImage( null );
		cellTitle_footer0.getContent( ).add( imageTitle_footer0 );	
		
		//imageTitle_footer0.setProperty("height","0.25875in");
		//imageTitle_footer0.setProperty("width","2.00000in");
		imageTitle_footer0.setProperty("height", "0.40625in");
		imageTitle_footer0.setProperty("width", "2.0729166666666665in");
	
		imageTitle_footer0.setFile(""+inputPath+Path.SEPARATOR+ApplicationConstants.IMAGE_OOYALALOGO);
		
		
		CellHandle cellTitle1_footer = (CellHandle) rowFooterOoyalaImg.getCells( ).get( 1 );
		TextItemHandle reportTitle_footer = elementFactory.newTextItem(null);
		//reportTitle_footer.setContent("\n"+tmapMiscValues.get("title"));
		reportTitle_footer.setContent(
				"<B><BR><FONT size=\"\" color=\""+tmapMiscValues.get("color")+"\" face=\"\">"+tmapMiscValues.get("title").toUpperCase()+"</FONT></B>" +
						"&nbsp;&nbsp;"+
						"<B><FONT size=\"\" color=\"#6A6468\" face=\"\">"+ApplicationConstants.REPORT_NAME+"</FONT></B>" + 
						"&nbsp;&nbsp;"+
						"<FONT size=\"\" color=\"#717174\" face=\"\">"+tmapMiscValues.get("publisher")+"&nbsp;&nbsp;&nbsp;&nbsp;"+ApplicationConstants.TBL_CONTS_PG_NUMBER+"</FONT>"
						);
		
		reportTitle_footer.setProperty("style", "textAlignRightStyle");
		reportTitle_footer.setProperty("contentType","html");
		cellTitle1_footer.getContent( ).add( reportTitle_footer );
				
		element.getSlot(ISimpleMasterPageModel.PAGE_FOOTER_SLOT).add(footerGrid);		    
		//footer - ends
		
					
		
	    // Get the first row.
	    RowHandle rowTblCont = (RowHandle) grid.getRows( ).get( 0 );			
	    TextItemHandle textTblCont = elementFactory.newTextItem(null);
	    //LabelHandle labelTblCont = elementFactory.newLabel( null );
	    CellHandle cellTblCont = (CellHandle) rowTblCont.getCells( ).get( 0 );
	    cellTblCont.getContent().add( textTblCont );
	    textTblCont.setContent( "TABLE OF CONTENTS");
	    textTblCont.setStyleName("tableOfContentStyle");
	    rowTblCont.setProperty("height", "0.3958333333333333in");
	          	       

	    int rowNumber = 1;
	    for (int i=0;i<aListResultant.size();i++)
	    {
	    	boolean isTopic = false;
	    	String topic = (String)aListResultant.get(i);
	    	//LabelHandle label0 = elementFactory.newLabel( null );
	    	TextItemHandle text0 = elementFactory.newTextItem(null);
	    	if (topic.startsWith("$"))
	    	{
	    		topic = topic.substring(1).toUpperCase();
	    		text0.setStyleName("chapterStyle");	

	    		isTopic = true;
	    	}
	    	else
	    	{
	    		text0.setStyleName("dotDotPageNumberStyle");	
	    	}
	    	RowHandle row = (RowHandle) grid.getRows( ).get( rowNumber );
	    	////skip topic 0.
	    	//leave spaces for each topic
	    	if (isTopic && i != 0)
	    	{
	    		RowHandle previousRow = (RowHandle) grid.getRows( ).get( rowNumber-1 );
	    		//setting height to the row which is one step before the topic's row
	    		//if topic's row is 4 means,then setting height to 3 rd row
	    		previousRow.setProperty("height", "0.4166666666666667in");
	    	}
	    	CellHandle cell = (CellHandle) row.getCells( ).get( 0 );
    	   
	    	GridHandle inner_grid = elementFactory.newGridItem( null, 2 /* cols */, 1 /* row */ );
	    	cell.getContent().add( inner_grid);	    	   
   	        
	    	RowHandle inner_grid_Row1 = (RowHandle) inner_grid.getRows( ).get( 0 );
	    	CellHandle inner_grid_Row1_Cell0 = (CellHandle) inner_grid_Row1.getCells( ).get( 0 );
	       
	    	inner_grid_Row1_Cell0.getContent( ).add( text0);
	    	String pageNumber = topic.substring(topic.lastIndexOf("#")+1);
	    	topic = topic.substring(0,topic.lastIndexOf("#"));
	    	text0.setContent(topic);
	       
	    	CellHandle inner_grid_Row1_Cell1 = (CellHandle) inner_grid_Row1.getCells( ).get( 1 );
	    	//LabelHandle label1 = elementFactory.newLabel( null );
	    	TextItemHandle text1 = elementFactory.newTextItem(null);
	    	inner_grid_Row1_Cell1.getContent( ).add( text1 );
	    	int dotCount = 65-topic.length();

	    	String str = "";
	    	for (int j=0;j<dotCount;j++)
	    	{
	    		if (isTopic)
	    		{
	    			//str = str +"..";
	    		}
	    		else
	    		{
	    			//str = str +"..";
	    		}
	    	}
	    	text1.setContent(str+pageNumber);
	    	text1.setStyleName("dotDotPageNumberRightStyle");
	       
//	    	float width1 = (float)((0.075) * topic.length());
//	    	if (isTopic)
//	    	{
//	    		width1 = (float)((0.11) * topic.length());
//	    	}

	    	float width1 = (float) 5.000;
	    	float width2 = (float) (7.875 - width1);
	    	ColumnHandle inner_grid_col0 = (ColumnHandle) inner_grid.getColumns().get(0);
	    	inner_grid_col0.setProperty("width", String.valueOf(width1));
	       
	    	ColumnHandle inner_grid_col1 = (ColumnHandle) inner_grid.getColumns().get(1);
	    	inner_grid_col1.setProperty("width", String.valueOf(width2));			       
	       
	    	rowNumber++;
	    }
       
	    // Save the design and close it.
//	    design.saveAs( "D:\\workspace2\\WPBirt\\Chart Types\\Output\\TableOfContents.rptdesign" ); 
	    design.saveAs( ".\\report\\TableOfContents.rptdesign" );
       
	    mergeReports = new MergeReports();
		mergeReports.getTableOfContentsPath(".\\report\\TableOfContents.rptdesign");
		
		design.close( );
       
		// We're done!
	}
	
	public TreeMap<String,String> getMiscValues()
	{
		TreeMap<String,String> tmap = new TreeMap<String,String>();
	
		try
		{
			NodeList nList = doc.getElementsByTagName("head");
			for (int temp = 0; temp < nList.getLength(); temp++) 
			{
		   	  Node nNode = nList.item(temp);
		      Element eElement = (Element) nNode;
		      
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
		      
			}
		}
		catch(Exception e)
		{
			
		}
	
		return tmap;
	}
	
	public ArrayList<String> getTableOfContentsValues()
	{
		ArrayList<String> aListResultant = new ArrayList<String>();
		TreeMap<String,String> tmapPagesAndNumber = new TreeMap<String,String>();
		int pageCount = 3;
		try 
		{
			NodeList nList = doc.getElementsByTagName("drilldown");
			for (int temp = 0; temp < nList.getLength(); temp++) 
			{
				Node nNode = nList.item(temp);
				Element eElement = (Element) nNode;
      
				String title = getTagValue("title", eElement).trim();
				aListResultant.add("$"+title + "#" + pageCount);
      
				NodeList nList1 = eElement.getElementsByTagName("graph");      
				for (int temp1 = 0; temp1 < nList1.getLength(); temp1++) 
				{
					Node nNode1 = nList1.item(temp1);
					Element eElement1 = (Element) nNode1;
					
					String type = eElement1.getAttribute("type");
					String src = eElement1.getAttribute("src");
					String subTitle = getTagValue("title", eElement1).trim();
					String strPageName = title + "#" + subTitle + "#" + type + "#" + src;
					
					if (!tmapPagesAndNumber.containsKey(strPageName))
					{
						if (temp1 != 0)
						{
							pageCount++;
						}
						
						tmapPagesAndNumber.put(strPageName, ""+pageCount);
					}
					else
					{
						continue;
					}
					
					aListResultant.add(subTitle + "#" + pageCount);
				}
				pageCount++;
			}
		} 
		catch (Exception e) 
		{
			e.printStackTrace();
		}
  
		this.tmapPagesAndNumber = tmapPagesAndNumber;
		return aListResultant;
	}
		
	public static String getTagValue(String sTag, Element eElement) 
	{
		NodeList nlList = eElement.getElementsByTagName(sTag).item(0).getChildNodes();
		Node nValue = (Node) nlList.item(0);
		
		return nValue.getNodeValue();
	}

	
	public TreeMap<String,String> getPagesAndNumber()
	{
		return this.tmapPagesAndNumber;
	}
	
	
}


