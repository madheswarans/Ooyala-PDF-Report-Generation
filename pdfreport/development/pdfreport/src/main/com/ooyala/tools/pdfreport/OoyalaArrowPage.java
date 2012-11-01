package com.ooyala.tools.pdfreport;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.image.BufferedImage;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.StringTokenizer;
import javax.imageio.ImageIO;
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

import com.ibm.icu.util.ULocale;

/**
 * Simple BIRT Design Engine API (DEAPI) demo.
 */
public class OoyalaArrowPage {
	MergeReports mergeReports = null;
	boolean isImageGenerate = false;
	int pageNumber=0;

	public void createReport(String graphType, String chartSrc,
			String headerTitle, String chartTitle, String leftHeader,
			ArrayList<String> aListBlocks, String rightHeader,
			ArrayList<String> aListItems, String inputPath, String title,
			String publisher, String strPageNumber, boolean isImageGeneration,
			String strLHS, String strRHS, int pageNumber, String color)
			throws IOException, SemanticException {
		// Configure the Engine and start the Platform
		setImageGenerate(isImageGeneration);
		this.pageNumber = pageNumber;
		setPageNumber(pageNumber);
		DesignConfig config = new DesignConfig();
		config.setProperty("BIRT_HOME",
				"./lib/birt-runtime-3_7_2/birt-runtime-3_7_2/ReportEngine");
		IDesignEngine engine = null;

		try {
			Platform.startup(config);
			IDesignEngineFactory factory = (IDesignEngineFactory) Platform
					.createFactoryObject(IDesignEngineFactory.EXTENSION_DESIGN_ENGINE_FACTORY);
			engine = factory.createDesignEngine(config);
		} catch (Exception ex) {
			ex.printStackTrace();
		}

		SessionHandle session = engine.newSessionHandle(ULocale.ENGLISH);
		// Create a new report design.
		ReportDesignHandle design = session.createDesign();

		// The element factory creates instances of the various BIRT elements.
		ElementFactory efactory = design.getElementFactory();

		// Create a simple master page that describes how the report will appear
		// when printed.
		// Note: The report will fail to load in the BIRT designer unless you
		// create a master page.
		DesignElementHandle element = efactory
				.newSimpleMasterPage("Page Master");
		if(isImageGenerate){
			element.setProperty("type","custom");
			element.setProperty("height", "6.5in");
			element.setProperty("width", "8.5in");
		}
		
		if(!isImageGenerate){
			if(getPageNumber()==0){
				element.setProperty("headerHeight", "1.0in");
			}else{
				element.setProperty("headerHeight", "0.5in");
			}
			element.setProperty("footerHeight", "0.5in");
			}
		
		design.getMasterPages().add(element);
		
		
		
		
	
		// style - starts
		StyleHandle textStyle = efactory.newStyle("textStyle");
		textStyle.setProperty(StyleHandle.FONT_FAMILY_PROP, "Helvetica");
		textStyle.setProperty(StyleHandle.COLOR_PROP, "#636466");
		textStyle.setProperty(StyleHandle.FONT_SIZE_PROP, "16");
		textStyle.setProperty(StyleHandle.TEXT_ALIGN_PROP,
				DesignChoiceConstants.TEXT_ALIGN_CENTER);		
		design.getStyles().add(textStyle);
		StyleHandle textCommentStyle = efactory.newStyle("textCommentStyle");
		textCommentStyle.setProperty(StyleHandle.FONT_FAMILY_PROP, "Helvetica");
		textCommentStyle.setProperty(StyleHandle.COLOR_PROP, "#636466");
		textCommentStyle.setProperty(StyleHandle.FONT_SIZE_PROP, "13");	
		design.getStyles().add(textCommentStyle);		
		StyleHandle textHeadingStyle = efactory.newStyle("textHeadingStyle");
		textHeadingStyle.setProperty(StyleHandle.FONT_FAMILY_PROP,
				"Helvetica");
		textHeadingStyle.setProperty(StyleHandle.COLOR_PROP, "#636466");
		textHeadingStyle.setProperty(StyleHandle.FONT_SIZE_PROP, "20");
		design.getStyles().add(textHeadingStyle);

		StyleHandle textAlignRightStyle = efactory
				.newStyle("textAlignRightStyle");
		textAlignRightStyle.setProperty(StyleHandle.FONT_FAMILY_PROP,
				"Helvetica");
		textAlignRightStyle.setProperty(StyleHandle.COLOR_PROP, "#00ced1");
		textAlignRightStyle.setProperty(StyleHandle.FONT_SIZE_PROP, "7");

		textAlignRightStyle.setProperty(StyleHandle.TEXT_ALIGN_PROP,
				DesignChoiceConstants.TEXT_ALIGN_RIGHT);
		textAlignRightStyle.setProperty(StyleHandle.MARGIN_TOP_PROP,"8");
		design.getStyles().add(textAlignRightStyle);

		StyleHandle textTitleStyle = efactory.newStyle("textTitleStyle");
		textTitleStyle.setProperty(StyleHandle.FONT_FAMILY_PROP,
				"Helvetica");
		textTitleStyle.setProperty(StyleHandle.COLOR_PROP, "#D3D3D3");
		textTitleStyle.setProperty(StyleHandle.FONT_SIZE_PROP, "14");
		design.getStyles().add(textTitleStyle);

		StyleHandle textInsightReportAlignRightStyle = efactory
				.newStyle("textInsightReportAlignRightStyle");
		textInsightReportAlignRightStyle.setProperty(
				StyleHandle.FONT_FAMILY_PROP, "Helvetica");
		textInsightReportAlignRightStyle.setProperty(StyleHandle.COLOR_PROP,
				"#6A6468");
		textInsightReportAlignRightStyle.setProperty(
				StyleHandle.FONT_SIZE_PROP, "7");

		textInsightReportAlignRightStyle.setProperty(
				StyleHandle.TEXT_ALIGN_PROP,
				DesignChoiceConstants.TEXT_ALIGN_RIGHT);
		design.getStyles().add(textInsightReportAlignRightStyle);

		StyleHandle textPublisherAlignRightStyle = efactory
				.newStyle("textPublisherAlignRightStyle");
		textPublisherAlignRightStyle.setProperty(StyleHandle.FONT_FAMILY_PROP,
				"Helvetica");
		textPublisherAlignRightStyle.setProperty(StyleHandle.COLOR_PROP,
				"#717174");
		textPublisherAlignRightStyle.setProperty(StyleHandle.FONT_SIZE_PROP,
				"7");

		textPublisherAlignRightStyle.setProperty(StyleHandle.TEXT_ALIGN_PROP,
				DesignChoiceConstants.TEXT_ALIGN_RIGHT);
		design.getStyles().add(textPublisherAlignRightStyle);
		// style - ends

		// header - starts
		GridHandle headerGrid = design.getElementFactory().newGridItem(
				null/* name */, 1 /* cols */, 2 /* row */);
		RowHandle rowTitle33 = (RowHandle) headerGrid.getRows().get(0);

		CellHandle cellTitle03 = (CellHandle) rowTitle33.getCells().get(0);

		if (getPageNumber() == 0) {
			TextItemHandle textItem3 = design.getElementFactory().newTextItem("test5");
			textItem3.setContentType("auto");		
			textItem3.setContent(headerTitle.toUpperCase());
			textItem3.setProperty("fontSize", "30pt");
			textItem3.setProperty("color", color);
			textItem3.setProperty("textAlign", "left");
			textItem3.setProperty("fontFamily", "Helvetica");
			if (!isImageGenerate) {
			cellTitle03.getContent().add(textItem3);
			}

		} else {
			ChartTitleHandle chartTitleHandle = new ChartTitleHandle();
			GridHandle header_inner_grid = chartTitleHandle.getChartTitle(design,headerTitle.toUpperCase());
			if (!isImageGenerate) {
			cellTitle03.getContent().add( header_inner_grid);
			}
		}
		
		if (getPageNumber()==0)
		{
			rowTitle33.setProperty("height", "0.724in");
		}
		RowHandle rowLine = (RowHandle) headerGrid.getRows().get(1);
		// rowLine.setProperty("height","0.9729166666666666in");
//		CellHandle cellLine0 = (CellHandle) rowLine.getCells().get(0);
//		ImageHandle imageLine = efactory.newImage("image1");
//		if (!isImageGenerate) {
//			cellLine0.getContent().add(imageLine);
//		}
//		imageLine.setProperty("width", "8.522916666666667in");
//		imageLine.setFile(ApplicationConstants.CONFIG_FOLDER + Path.SEPARATOR
//				+ ApplicationConstants.OOYALA_IMAGE_LINE);
		
		ChartTitleHandle chartTitleHandle = new ChartTitleHandle();
		rowLine = chartTitleHandle.getThinCustomLine(design,rowLine,color,"header");
		if (!isImageGenerate) {
		element.getSlot(ISimpleMasterPageModel.PAGE_HEADER_SLOT)
				.add(headerGrid);
		}
		// header - ends

		// footer - starts
		GridHandle footerGrid = design.getElementFactory().newGridItem(
				null/* name */, 1 /* cols */, 2 /* row */);
		RowHandle rowFooter0 = (RowHandle) footerGrid.getRows().get(0);
//		CellHandle cellFooterLine = (CellHandle) rowFooter0.getCells().get(0);
//		ImageHandle imageFooterLine = efactory.newImage("image1");
//		if (!isImageGenerate) {
//			cellFooterLine.getContent().add(imageFooterLine);
//		}
//		imageFooterLine.setProperty("width", "8.522916666666667in");
//
//		imageFooterLine.setFile(ApplicationConstants.CONFIG_FOLDER
//				+ Path.SEPARATOR + ApplicationConstants.OOYALA_IMAGE_LINE);
		
		rowFooter0 = chartTitleHandle.getThinCustomLine(design,rowFooter0,color,"footer");		
		RowHandle rowFooter1 = (RowHandle) footerGrid.getRows().get(1);
		CellHandle cellFooterImage = (CellHandle) rowFooter1.getCells().get(0);
		GridHandle footer_inner_grid = design.getElementFactory().newGridItem(
				null, 2 /* cols */, 1 /* row */);
		if (!isImageGenerate) {
			cellFooterImage.getContent().add(footer_inner_grid);
		}

		RowHandle rowFooterOoyalaImg = (RowHandle) footer_inner_grid.getRows()
				.get(0);
		CellHandle cellTitle_footer0 = (CellHandle) rowFooterOoyalaImg
				.getCells().get(0);
		ImageHandle imageTitle_footer0 = efactory.newImage(null);
		

		//imageTitle_footer0.setProperty("height", "0.25875in");
	//	imageTitle_footer0.setProperty("width", "2.00000in");
		imageTitle_footer0.setProperty("height", "0.40625in");
		imageTitle_footer0.setProperty("width", "2.0729166666666665in");
		
		imageTitle_footer0.setFile("" + inputPath + Path.SEPARATOR
				+ ApplicationConstants.IMAGE_OOYALALOGO);

		if (!isImageGenerate) {
			cellTitle_footer0.getContent().add(imageTitle_footer0);
		}
		CellHandle cellTitle1_footer = (CellHandle) rowFooterOoyalaImg
				.getCells().get(1);
		TextItemHandle reportTitle_footer = efactory.newTextItem(null);
//		reportTitle_footer
//				.setContent("<BR><FONT size=\"\" color=\"#00ced1\" face=\"\">"
//						+ title.toUpperCase() + "</FONT>" + "&nbsp;&nbsp;"
//						+ "<FONT size=\"\" color=\"#6A6468\" face=\"\">"
//						+ ApplicationConstants.REPORT_NAME + ":" + "</FONT>"
//						+ "&nbsp;&nbsp;"
//						+ "<FONT size=\"\" color=\"#717174\" face=\"\">"
//						+ publisher + "&nbsp;&nbsp;" + strPageNumber
//						+ "</FONT>");
		reportTitle_footer.setContent(
				"<BR><B><FONT size=\"\" color=\""+color+"\" face=\"Helvetica\">"+title.toUpperCase()+"</FONT></B>" +
						"&nbsp;&nbsp;"+
						"<B><FONT size=\"\" color=\"#6A6468\" face=\"Helvetica\">"+ApplicationConstants.REPORT_NAME+"</FONT></B>" + 
						"&nbsp;&nbsp;"+
						"<FONT size=\"\" color=\"#717174\" face=\"Helvetica\">"+publisher+"&nbsp;&nbsp;&nbsp;&nbsp;"+strPageNumber+"</FONT>"
						);
		reportTitle_footer.setProperty("style", "textAlignRightStyle");
		reportTitle_footer.setProperty("contentType", "html");
		if (!isImageGenerate) {
			cellTitle1_footer.getContent().add(reportTitle_footer);
		}
		if (!isImageGenerate) {
		element.getSlot(ISimpleMasterPageModel.PAGE_FOOTER_SLOT)
				.add(footerGrid);
		}
		// footer - ends

		// title
		GridHandle grid = efactory
				.newGridItem(null, 1 /* cols */, 5 /* row */);
		
	
	
		design.getBody().add(grid);
	
		
		RowHandle row0 = (RowHandle) grid.getRows().get(0);
		CellHandle cell_title = (CellHandle) row0.getCells().get(0);
		row0.setProperty("height", "1.40625in");
		TextItemHandle text_title = efactory.newTextItem(null);
		text_title.setContent(chartTitle.toUpperCase());
		text_title.setProperty("style", "textTitleStyle");
		cell_title.getContent().add(text_title);

		// Get the first row - starts.
		RowHandle row = (RowHandle) grid.getRows().get(1);
		row.setProperty("height", "2.8229166666666665in");

		CellHandle cellArrowAndText = (CellHandle) row.getCells().get(0);
		GridHandle cellArrowAndText_inner_grid = design.getElementFactory()
				.newGridItem(null, 5 /* cols */, 1 /* row */);
		cellArrowAndText.getContent().add(cellArrowAndText_inner_grid);

		RowHandle ArrowAndText_row = (RowHandle) cellArrowAndText_inner_grid
				.getRows().get(0);
		ArrayList<String> aList = getValues(chartSrc, inputPath);

		// cell 0
		CellHandle cell = (CellHandle) ArrowAndText_row.getCells().get(0);

		TextItemHandle para_cell0 = efactory.newTextItem(null);
		para_cell0.setContent((String) aList.get(0));
		para_cell0.setProperty("style", "textStyle");
		cell.getContent().add(para_cell0);

		// cell 1
		// text on image - starts
		File source = new File(ApplicationConstants.CONFIG_FOLDER
				+ Path.SEPARATOR + ApplicationConstants.IMAGE_ARROW);
		Image SourceImage = ImageIO.read(source);

		BufferedImage cpimg = bufferImage(SourceImage);
		Graphics g = cpimg.createGraphics();
		Font fnt = new Font("Helvetica", 1, 14);
		Color fntC = new Color(0, 0, 0);
		g.setColor(fntC);
		g.setFont(fnt);
		g.drawString((String) aList.get(1), 50, 50);
		String strPageName = graphType + chartSrc + headerTitle + chartTitle;
		strPageName = strPageName.replace(" ", "_");
		String strTimeStamp = System.currentTimeMillis()+"";
//		String strFile1 = ApplicationConstants.IMAGE_ARROW_MODIFIED_ONE
//				+ strPageName + ".png";
		String strFile1 = ApplicationConstants.IMAGE_ARROW_MODIFIED_ONE
				+ strTimeStamp + ".png";
		File f1 = new File(ApplicationConstants.CONFIG_FOLDER + Path.SEPARATOR
				+ strFile1);
		ImageIO.write(cpimg, "png", f1);
		// text on image - ends
		// Create an image and add it to the second cell.
		ImageHandle image1 = efactory.newImage(null);
		cell = (CellHandle) ArrowAndText_row.getCells().get(1);
		cell.getContent().add(image1);
		image1.setFile(ApplicationConstants.CONFIG_FOLDER + Path.SEPARATOR
				+ strFile1);

		// cell 2
		cell = (CellHandle) ArrowAndText_row.getCells().get(2);
		TextItemHandle para_cell2 = efactory.newTextItem(null);
		para_cell2.setContent((String) aList.get(2));
		para_cell2.setProperty("style", "textStyle");
		cell.getContent().add(para_cell2);

		// cell 3
		// Create an image and add it to the fourth cell.
		ImageHandle image2 = efactory.newImage(null);
		cell = (CellHandle) ArrowAndText_row.getCells().get(3);
		cell.getContent().add(image2);

		BufferedImage cpimg1 = bufferImage(SourceImage);

		Graphics g1 = cpimg1.createGraphics();
		Font fnt1 = new Font("Helvetica", 1, 14);
		Color fntC1 = new Color(0, 0, 0);
		g1.setColor(fntC1);
		g1.setFont(fnt1);
		g1.drawString((String) aList.get(3), 50, 50);
//		String strFile2 = ApplicationConstants.IMAGE_ARROW_MODIFIED_TWO
//				+ strPageName + ".png";
		String strFile2 = ApplicationConstants.IMAGE_ARROW_MODIFIED_TWO
				+ strTimeStamp + ".png";				
		f1 = new File(ApplicationConstants.CONFIG_FOLDER + Path.SEPARATOR
				+ strFile2);
		ImageIO.write(cpimg1, "png", f1);
		image2.setFile(ApplicationConstants.CONFIG_FOLDER + Path.SEPARATOR
				+ strFile2);

		// cell 4
		cell = (CellHandle) ArrowAndText_row.getCells().get(4);
		TextItemHandle para_cell4 = efactory.newTextItem(null);
		para_cell4.setContent((String) aList.get(4));
		para_cell4.setProperty("style", "textStyle");
		cell.getContent().add(para_cell4);
		// Get the first row - ends.

		if (!isImageGenerate) 
		{	
			RowHandle rowLine2 = (RowHandle) grid.getRows().get(2);
			rowLine2 = chartTitleHandle.getThinCustomLine(design,rowLine2,color,"image");
			
			// Get second row - starts
			RowHandle row2 = (RowHandle) grid.getRows().get(3);
			CellHandle cellItemsAndBlocks = (CellHandle) row2.getCells().get(0);
			GridHandle cellItemsAndBlocks_inner_grid = design.getElementFactory()
					.newGridItem(null, 2 /* cols */, 2 /* row */);
			cellItemsAndBlocks.getContent().add(cellItemsAndBlocks_inner_grid);
			
			ChartBodyContentHandle chartBodyContentHandle = new ChartBodyContentHandle();
	
			// left hand side heading - starts
			RowHandle rowItemsAndBlocksTitle = (RowHandle) cellItemsAndBlocks_inner_grid
					.getRows().get(0);
			CellHandle rowItemsAndBlocksTitle_cell0 = (CellHandle) rowItemsAndBlocksTitle.getCells().get(0);
			rowItemsAndBlocksTitle_cell0 = chartBodyContentHandle.getBodyHeading(rowItemsAndBlocksTitle_cell0,efactory,leftHeader,"textHeadingStyle");
			// left hand side heading - ends
			
			// left hand side - starts
			RowHandle rowItemsAndBlocks = (RowHandle) cellItemsAndBlocks_inner_grid.getRows().get(1);
			CellHandle row2_cell1 = (CellHandle) rowItemsAndBlocks.getCells().get(0);
				
			GridHandle innergrid_LHS = chartBodyContentHandle.getBodyContent(design,efactory,strLHS,"textCommentStyle",aListBlocks);
			row2_cell1.getContent().add(innergrid_LHS);
			// left hand side - ends
			
			// right hand side heading - starts
			CellHandle rowItemsAndBlocksTitle_cell1 = (CellHandle) rowItemsAndBlocksTitle.getCells().get(1);
			rowItemsAndBlocksTitle_cell1 = chartBodyContentHandle.getBodyHeading(rowItemsAndBlocksTitle_cell1,efactory,rightHeader,"textHeadingStyle");
			// right hand side heading - ends

			// right hand side - starts
			CellHandle row2_cell2 = (CellHandle) rowItemsAndBlocks.getCells().get(1);
			GridHandle innergrid_RHS = chartBodyContentHandle.getBodyContent(design,efactory,strRHS,"textCommentStyle",aListItems);
			row2_cell2.getContent().add(innergrid_RHS);
			// right hand side - ends
			// Get second row - ends
		}
		// right hand side - ends

		// Save the design and close it.

		mergeReports = new MergeReports();
		if (isImageGeneration) {
			design.saveAs(ApplicationConstants.REPORT_FOLDER + Path.SEPARATOR
					+ strTimeStamp + graphType + "Image.rptdesign");
			mergeReports.setReportDesignPath(ApplicationConstants.REPORT_FOLDER
					+ Path.SEPARATOR + strTimeStamp + graphType
					+ "Image.rptdesign");
		} else {
			design.saveAs(ApplicationConstants.REPORT_FOLDER + Path.SEPARATOR
					+ strTimeStamp + graphType + ".rptdesign");
			mergeReports
					.getArrowPageContentsPath(ApplicationConstants.REPORT_FOLDER
							+ Path.SEPARATOR
							+ strTimeStamp + graphType
							+ ".rptdesign");
		}
		design.close();
	}

	public static BufferedImage bufferImage(Image image) {
		return bufferImage(image, BufferedImage.TYPE_INT_RGB);
	}

	public static BufferedImage bufferImage(Image image, int type) {
		BufferedImage bufferedImage = new BufferedImage(image.getWidth(null),
				image.getHeight(null), type);
		Graphics2D g = bufferedImage.createGraphics();
		g.drawImage(image, null, null);
		return bufferedImage;
	}

	public static ArrayList<String> getValues(String csvFile, String inputPath) {
		ArrayList<String> aList = new ArrayList<String>();
		ArrayList<String> aListFinal = new ArrayList<String>();
		try {
			// create BufferedReader to read csv file
			BufferedReader br = new BufferedReader(new FileReader(inputPath
					+ Path.SEPARATOR + csvFile));
			String line = "";

			// read comma separated file line by line
			while ((line = br.readLine()) != null) {
				aList.add(line);
			}

			for (int i = 0; i < aList.size(); i++) {
				String str = (String) aList.get(i);
				StringTokenizer st1 = new StringTokenizer(str, ",");
				int countTokens = st1.countTokens();
				while (st1.hasMoreTokens()) {
					if (countTokens == 2) {
						String str1 = st1.nextToken();
						//if (str1.length() > 20) {
							if (str1.indexOf(" ") == -1) {
								String temp1 = str1.substring(0, 10);
								String temp2 = str1
										.substring(11, str1.length());
								str1 = temp1 + "\n" + temp2;
							} else {
								String temp1 = str1.substring(0,
										str1.indexOf(" "));
								String temp2 = str1.substring(
										str1.indexOf(" ") + 1, str1.length());
								str1 = temp1 + "\n" + temp2;
							}
						//}
						//str1 = str1 + "\n";

						String str2 = st1.nextToken();
						//aListFinal.add(str1 + "  " + str2);
						aListFinal.add(test(str1 + "  " + str2));
					} else if (countTokens == 1) {
						String str1 = st1.nextToken();
						aListFinal.add(str1);
					}
				}
			}
		} catch (Exception e) {
			System.err.println("CSV file cannot be read : " + e);
		}

		return aListFinal;
	}

	public static String test(String str)
	{
		//String str = "Total Impressions 2.1M";
		//String str = "Recommended plays 1.4M";
		//String str = "Cumulative recommended plays 6.8";
		

		StringTokenizer st = new StringTokenizer(str);
		ArrayList<String> aList = new ArrayList<String>();
//		int strLen = 0;
		while (st.hasMoreTokens()) 
		{
			String str1 = st.nextToken();
//			if (str1.length()>strLen)
//			{
//				strLen = str1.length();
//			}
			
			aList.add(str1);
		}
		
		String strResultant = "";
		for (int i=0;i<aList.size();i++)
		{
			String str1 = (String)aList.get(i);
//			int tempSpaceAppend = (strLen)-(str1.length());
			
			String strTempSpace = "";
//			for (int j=0;j<tempSpaceAppend;j++)
//			{
//				strTempSpace = strTempSpace + " ";
//			}
			
			str1 = strTempSpace + str1 + strTempSpace + "\n";
				
			strResultant = strResultant + str1;
			
		}
		
		return strResultant;
	}
	
	public boolean isImageGenerate() {
		return isImageGenerate;
	}

	public void setImageGenerate(boolean isImageGenerate) {
		this.isImageGenerate = isImageGenerate;
	}

	/**
	 * @return the pageNumber
	 */
	public int getPageNumber() {
		return pageNumber;
	}

	/**
	 * @param pageNumber the pageNumber to set
	 */
	public void setPageNumber(int pageNumber) {
		this.pageNumber = pageNumber;
	}
	
}
