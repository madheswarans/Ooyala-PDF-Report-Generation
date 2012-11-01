package com.ooyala.tools.pdfreport;

import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.image.BufferedImage;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.TreeMap;

import javax.imageio.ImageIO;
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
import org.eclipse.birt.report.model.api.RowOperationParameters;
import org.eclipse.birt.report.model.api.SessionHandle;
import org.eclipse.birt.report.model.api.StyleHandle;
import org.eclipse.birt.report.model.api.TableHandle;
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

public class DashBoard {

	public String inputPath = null;
	public String chartSrc = null;
	private static String inputFileName = System
			.getProperty("pdf.report.config.file");
	File fXmlFile = new File(inputFileName);

	DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
	DocumentBuilder dBuilder = null;
	Document doc = null;
	MergeReports mergeReports = null;
	boolean isImageGenerate = false;

	public void createReport(String graphType, String chartSrc,
			String dashBoardTitle, String inputPath, boolean isImageGeneration, String color)
			throws IOException, SemanticException {
		// Configure the Engine and start the Platform
		this.inputPath = inputPath;
		this.chartSrc = chartSrc;
		setImageGenerate(isImageGeneration);

		DesignConfig config = new DesignConfig();
		config.setProperty("BIRT_HOME",
				"./lib/birt-runtime-3_7_2/birt-runtime-3_7_2/ReportEngine");
		IDesignEngine engine = null;

		try {
			Platform.startup(config);
			IDesignEngineFactory factory = (IDesignEngineFactory) Platform
					.createFactoryObject(IDesignEngineFactory.EXTENSION_DESIGN_ENGINE_FACTORY);
			engine = factory.createDesignEngine(config);

			dBuilder = dbFactory.newDocumentBuilder();
			doc = dBuilder.parse(fXmlFile);
			doc.getDocumentElement().normalize();
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
	
	
		GridHandle grid = efactory
				.newGridItem(null, 1 /* cols */, 15 /* row */);
		if(!isImageGenerate){
				element.setProperty("headerHeight", "1.0in");
					element.setProperty("footerHeight", "0.6in");
		}
		design.getMasterPages().add(element);
		
		if(!isImageGenerate){
		design.getBody().add(grid);
		}
		
	

		// Header and footer

		ApplicationConstants.APP_LOGGER.info("ooyal 111");
		// font for TEXT - starts - css styles declarations
		StyleHandle textStyle1 = efactory.newStyle("textStyle");
		textStyle1.setProperty(StyleHandle.FONT_FAMILY_PROP, "Helvetica");
		
		textStyle1.setProperty(StyleHandle.COLOR_PROP, "#636466");
		textStyle1.setProperty(StyleHandle.FONT_SIZE_PROP, "11");
		design.getStyles().add(textStyle1);

		StyleHandle textAlignRightStyle1 = efactory
				.newStyle("textAlignRightStyle1");
		textAlignRightStyle1.setProperty(StyleHandle.FONT_FAMILY_PROP,
				"Helvetica");
		textAlignRightStyle1.setProperty(StyleHandle.COLOR_PROP, "#636466");
		textAlignRightStyle1.setProperty(StyleHandle.MARGIN_TOP_PROP,"14");
		textAlignRightStyle1.setProperty(StyleHandle.FONT_WEIGHT_PROP,"bold");
		textAlignRightStyle1.setProperty(StyleHandle.FONT_SIZE_PROP, "11");
		

		textAlignRightStyle1.setProperty(StyleHandle.TEXT_ALIGN_PROP,
				DesignChoiceConstants.TEXT_ALIGN_RIGHT);
		design.getStyles().add(textAlignRightStyle1);

		// Create a grid and add it to the "body" slot of the report design.

		// style - starts

		StyleHandle cellBorder = efactory.newStyle("cellBorder");
		cellBorder.setProperty(StyleHandle.BORDER_BOTTOM_COLOR_PROP, "#FFFFFF");
		cellBorder.setProperty(StyleHandle.BORDER_TOP_COLOR_PROP, "#FFFFFF");
		cellBorder.setProperty(StyleHandle.BORDER_LEFT_COLOR_PROP, "#FFFFFF");
		cellBorder.setProperty(StyleHandle.BORDER_RIGHT_COLOR_PROP, "#FFFFFF");
		cellBorder.setProperty(StyleHandle.BORDER_BOTTOM_WIDTH_PROP, "2");
		cellBorder.setProperty(StyleHandle.BORDER_TOP_WIDTH_PROP, "2");
		cellBorder.setProperty(StyleHandle.BORDER_LEFT_WIDTH_PROP, "10");
		cellBorder.setProperty(StyleHandle.BORDER_RIGHT_WIDTH_PROP, "10");
		cellBorder.setProperty(StyleHandle.FONT_FAMILY_PROP,
				"Helvetica");
		cellBorder.setBorderBottomStyle("solid");
		cellBorder.setBorderLeftStyle("solid");
		cellBorder.setBorderRightStyle("solid");
		cellBorder.setBorderTopStyle("solid");
		design.getStyles().add(cellBorder);

		StyleHandle cellSectionBorder = efactory.newStyle("cellSectionBorder");
		cellSectionBorder.setProperty(StyleHandle.BORDER_BOTTOM_COLOR_PROP,
				"#FFFFFF");
		cellSectionBorder.setProperty(StyleHandle.BORDER_TOP_COLOR_PROP,
				"#FFFFFF");
		cellSectionBorder.setProperty(StyleHandle.BORDER_LEFT_COLOR_PROP,
				"#FFFFFF");
		cellSectionBorder.setProperty(StyleHandle.BORDER_RIGHT_COLOR_PROP,
				"#FFFFFF");
		cellSectionBorder
				.setProperty(StyleHandle.BORDER_BOTTOM_WIDTH_PROP, "2");
		cellSectionBorder.setProperty(StyleHandle.BORDER_TOP_WIDTH_PROP, "10");
		cellSectionBorder.setProperty(StyleHandle.BORDER_LEFT_WIDTH_PROP, "10");
		cellSectionBorder
				.setProperty(StyleHandle.BORDER_RIGHT_WIDTH_PROP, "10");
		cellSectionBorder.setProperty(StyleHandle.FONT_FAMILY_PROP,
				"Helvetica");
		cellSectionBorder.setBorderBottomStyle("solid");
		cellSectionBorder.setBorderLeftStyle("solid");
		cellSectionBorder.setBorderRightStyle("solid");
		cellSectionBorder.setBorderTopStyle("solid");
		design.getStyles().add(cellSectionBorder);

		StyleHandle cellHeaderBorder = efactory.newStyle("cellHeaderBorder");
		cellHeaderBorder.setProperty(StyleHandle.BORDER_BOTTOM_COLOR_PROP,
				"#FFFFFF");
		cellHeaderBorder.setProperty(StyleHandle.BORDER_TOP_COLOR_PROP,
				"#FFFFFF");
		cellHeaderBorder.setProperty(StyleHandle.BORDER_LEFT_COLOR_PROP,
				"#FFFFFF");
		cellHeaderBorder.setProperty(StyleHandle.BORDER_RIGHT_COLOR_PROP,
				"#FFFFFF");
		cellHeaderBorder.setProperty(StyleHandle.FONT_FAMILY_PROP,
				"Helvetica");
		cellHeaderBorder.setProperty(StyleHandle.BORDER_BOTTOM_WIDTH_PROP, "2");
		cellHeaderBorder.setProperty(StyleHandle.BORDER_TOP_WIDTH_PROP, "10");
		cellHeaderBorder.setProperty(StyleHandle.BORDER_LEFT_WIDTH_PROP, "10");
		cellHeaderBorder.setProperty(StyleHandle.BORDER_RIGHT_WIDTH_PROP, "10");
		cellHeaderBorder.setBorderBottomStyle("solid");
		cellHeaderBorder.setBorderLeftStyle("solid");
		cellHeaderBorder.setBorderRightStyle("solid");
		cellHeaderBorder.setBorderTopStyle("solid");
		design.getStyles().add(cellHeaderBorder);

		StyleHandle textStyle = efactory.newStyle("textStyle");
		textStyle.setProperty(StyleHandle.FONT_FAMILY_PROP, "Helvetica");
		textStyle.setProperty(StyleHandle.COLOR_PROP, "#636466");
		textStyle.setProperty(StyleHandle.FONT_SIZE_PROP, "15");
		design.getStyles().add(textStyle);

		StyleHandle textHeaderStyle = efactory.newStyle("textHeadeStyle");
		textHeaderStyle.setProperty(StyleHandle.FONT_FAMILY_PROP,
				"Helvetica");
		textHeaderStyle.setProperty(StyleHandle.COLOR_PROP, "#636466");
		textHeaderStyle.setProperty(StyleHandle.FONT_SIZE_PROP, "12");
		design.getStyles().add(textHeaderStyle);

		StyleHandle textAlignRightStyle = efactory
				.newStyle("textAlignRightStyle");
		textAlignRightStyle.setProperty(StyleHandle.FONT_FAMILY_PROP,
				"Helvetica");
		textAlignRightStyle.setProperty(StyleHandle.COLOR_PROP, "#00ced1");
		textAlignRightStyle.setProperty(StyleHandle.FONT_SIZE_PROP, "7");

		textAlignRightStyle.setProperty(StyleHandle.TEXT_ALIGN_PROP,
				DesignChoiceConstants.TEXT_ALIGN_RIGHT);
		design.getStyles().add(textAlignRightStyle);

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

		StyleHandle newStyle = efactory.newStyle("NewStyle");
		newStyle.setProperty(StyleHandle.FONT_FAMILY_PROP, "Helvetica");
		newStyle.setProperty(StyleHandle.BACKGROUND_COLOR_PROP, "#00FF00");
		design.getStyles().add(newStyle);

		StyleHandle newStyle3 = efactory.newStyle("NewStyle3");
		newStyle3.setProperty(StyleHandle.FONT_FAMILY_PROP,
				"Helvetica");
		newStyle3.setProperty(StyleHandle.BACKGROUND_COLOR_PROP, "#00FF00");
		design.getStyles().add(newStyle3);

		StyleHandle newStyle2 = efactory.newStyle("NewStyle2");
		newStyle2.setProperty(StyleHandle.FONT_FAMILY_PROP,
				"Helvetica");
		newStyle2.setProperty(StyleHandle.BACKGROUND_COLOR_PROP, "#FF8040");
		design.getStyles().add(newStyle2);

		StyleHandle textPublisherAlignRightStyle = efactory
				.newStyle("textPublisherAlignRightStyle");
		textPublisherAlignRightStyle.setProperty(StyleHandle.FONT_FAMILY_PROP,
				"Helvetica");
		textPublisherAlignRightStyle.setProperty(StyleHandle.COLOR_PROP,
				color);
		textPublisherAlignRightStyle.setProperty(StyleHandle.FONT_SIZE_PROP,
				"48");

		textPublisherAlignRightStyle.setProperty(StyleHandle.TEXT_ALIGN_PROP,
				DesignChoiceConstants.TEXT_ALIGN_LEFT);
		design.getStyles().add(textPublisherAlignRightStyle);

		StyleHandle textDateRightStyle = efactory
				.newStyle("textDateRightStyle");
		textDateRightStyle.setProperty(StyleHandle.FONT_FAMILY_PROP,
				"Helvetica");
		textDateRightStyle.setProperty(StyleHandle.FONT_FAMILY_PROP,
				"Helvetica");
		textDateRightStyle.setProperty(StyleHandle.COLOR_PROP, "#636466");
		textDateRightStyle.setProperty(StyleHandle.FONT_SIZE_PROP, "16");

		textDateRightStyle.setProperty(StyleHandle.TEXT_ALIGN_PROP,
				DesignChoiceConstants.TEXT_ALIGN_LEFT);
		design.getStyles().add(textDateRightStyle);

		StyleHandle cellText = efactory.newStyle("cellText");
		cellText.setProperty(StyleHandle.FONT_FAMILY_PROP, "Helvetica");
		cellText.setProperty(StyleHandle.FONT_SIZE_PROP, "16");
		cellText.setProperty(StyleHandle.FONT_WEIGHT_PROP, "bold");
		cellText.setProperty(StyleHandle.TEXT_ALIGN_PROP,
				DesignChoiceConstants.TEXT_ALIGN_LEFT);
		design.getStyles().add(cellText);

		// style - ends

		// header - starts
		GridHandle headerGrid = design.getElementFactory().newGridItem(
				null/* name */, 1 /* cols */, 2 /* row */);
		RowHandle rowTitle33 = (RowHandle) headerGrid.getRows().get(0);

		CellHandle cellTitle = (CellHandle) rowTitle33.getCells().get(0);
		GridHandle header_inner_grid = design.getElementFactory().newGridItem(
				null, 2 /* cols */, 1 /* row */);
		if (!isImageGenerate) {
			cellTitle.getContent().add(header_inner_grid);
		}

		RowHandle rowHeaderOoyalaImg = (RowHandle) header_inner_grid.getRows()
				.get(0);
		CellHandle cellTitle_header0 = (CellHandle) rowHeaderOoyalaImg
				.getCells().get(0);
		ImageHandle imageTitle_header0 = efactory.newImage(null);
		

		//imageTitle_header0.setProperty("height", "0.25875in");
	//	imageTitle_header0.setProperty("width", "2.00000in");
		imageTitle_header0.setProperty("height", "0.40625in");
		imageTitle_header0.setProperty("width", "2.0729166666666665in");
		
		imageTitle_header0.setFile("" + inputPath + Path.SEPARATOR
				+ ApplicationConstants.IMAGE_OOYALALOGO);
		if (!isImageGenerate) {
			cellTitle_header0.getContent().add(imageTitle_header0);
		}

		TreeMap<String, String> tmapMiscValues = getMiscValues();

		CellHandle cellTitle_header1 = (CellHandle) rowHeaderOoyalaImg
				.getCells().get(1);
		TextItemHandle reportTitle_header = efactory.newTextItem(null);
		reportTitle_header.setContent(ApplicationConstants.FIRST_PAGE_REPORT_NAME);
		reportTitle_header.setProperty("style", "textAlignRightStyle1");
		if (!isImageGenerate) {
			cellTitle_header1.getContent().add(reportTitle_header);
		}

		RowHandle rowHeaderLine = (RowHandle) headerGrid.getRows().get(1);
		ChartTitleHandle chartTitleHandle = new ChartTitleHandle();
		rowHeaderLine = chartTitleHandle.getThinCustomLine(design,rowHeaderLine,color,"header");		
//		CellHandle cellLine_header = (CellHandle) rowHeaderLine.getCells().get(
//				0);
//
//		ImageHandle imageHeaderLine = efactory.newImage("image12");
//		if (!isImageGenerate) {
//			cellLine_header.getContent().add(imageHeaderLine);
//		}
//		imageHeaderLine.setProperty("width", "8.522916666666667in");
//		imageHeaderLine.setFile(ApplicationConstants.CONFIG_FOLDER
//				+ Path.SEPARATOR + ApplicationConstants.OOYALA_IMAGE_LINE);
		if(!isImageGenerate){
			element.getSlot(ISimpleMasterPageModel.PAGE_HEADER_SLOT)
				.add(headerGrid);
		}
		// header - ends

		RowHandle row1_CustomImage = (RowHandle) grid.getRows().get(0);
		CellHandle cell_CustomImage = (CellHandle) row1_CustomImage.getCells()
				.get(0);
		ImageHandle customImage = efactory.newImage(null);
	
		
		BufferedImage bimg = ImageIO.read(new File(getInputPath()+Path.SEPARATOR+(String) tmapMiscValues.get("image")));
        int imageWidth=bimg.getWidth();
        int imageHeight=bimg.getHeight();
        if(imageHeight>=130)
        {
        	
        	customImage.setProperty("height", "1.73in");
        	customImage.setProperty("width", "1.1865in");
        }
		if (!isImageGenerate) {
			customImage.setFile("" + inputPath + Path.SEPARATOR
					+ (String) tmapMiscValues.get("image"));
			cell_CustomImage.getContent().add(customImage);
		}
		RowHandle row2_ReportHeading = (RowHandle) grid.getRows().get(2);
		CellHandle cell_ReportHeading = (CellHandle) row2_ReportHeading
				.getCells().get(0);
		TextItemHandle textItem_ReportHeading = efactory.newTextItem(null);
		textItem_ReportHeading.setContent((String) tmapMiscValues
				.get("title"));

		textItem_ReportHeading.setProperty("style",
				"textPublisherAlignRightStyle");
		if (!isImageGenerate) {
			cell_ReportHeading.getContent().add(textItem_ReportHeading);
		}

		RowHandle row3_ReportDate = (RowHandle) grid.getRows().get(3);
		CellHandle cell_ReportDate = (CellHandle) row3_ReportDate.getCells()
				.get(0);
		TextItemHandle textItem_ReportDate = efactory.newTextItem(null);
		textItem_ReportDate.setContent((String) tmapMiscValues.get("date"));

		textItem_ReportDate.setProperty("style", "textDateRightStyle");
		if (!isImageGenerate) {
			cell_ReportDate.getContent().add(textItem_ReportDate);
		}

		ArrayList<String> aList = getValues(inputPath, chartSrc);

		// Creating a table and mapping the values
		// TableHandle dashTable = null;
		TableHandle dashTable = efactory.newTableItem("table", 7);
		dashTable.setProperty(StyleHandle.TEXT_ALIGN_PROP,
				DesignChoiceConstants.TEXT_ALIGN_LEFT);

		ColumnHandle ch = (ColumnHandle) dashTable.getColumns().get(0);
		ch.getWidth().setStringValue("2.5in");

		if (aList != null) {
			Iterator<String> iterator = aList.iterator();

			boolean isFirstCell = true;
			boolean isFirstRow = true;
			int rowIndex = 0;

			while (iterator.hasNext()) {
				String row = (String) iterator.next();

				RowHandle tableRow = (RowHandle) dashTable.getDetail().get(
						rowIndex);
				if (isFirstRow) {
					tableRow.setProperty("height", "0.74444in");
				}

				rowIndex++;

				String[] values = row.split(",");
				int cellIndex = 0;
				boolean isContains = false;

				for (String cell : values) {
					if (!isFirstCell && cell.length() < 1) {
						cell = "0";
					}
					CellHandle tcell = null;
					if (isFirstCell) {
						tcell = (CellHandle) tableRow.getCells().get(cellIndex);
						tcell.setProperty(StyleHandle.VERTICAL_ALIGN_PROP,
								DesignChoiceConstants.VERTICAL_ALIGN_MIDDLE);
						tcell.setStyleName("cellHeaderBorder");
						LabelHandle label = efactory.newLabel(null);
						label.setText(dashBoardTitle);
						label.setStyleName("cellText");
						tcell.getContent().add(label);
						isFirstCell = false;
						cellIndex++;
						continue;
					}

					tcell = (CellHandle) tableRow.getCells().get(cellIndex);
					tcell.setProperty(StyleHandle.VERTICAL_ALIGN_PROP,
							DesignChoiceConstants.VERTICAL_ALIGN_MIDDLE);

					if (!isFirstRow && cell.contains("-")) {
						tcell.setStyleName("cellBorder");
						tableRow.setProperty("height", "0.2444in");
						tcell.setProperty("height", "0.2444in");
						isContains = true;
					}
					if (!isContains) {
						tableRow.setProperty("height", "0.64444in");
						tcell.setProperty("height", "0.64444in");
						tcell.setStyleName("cellSectionBorder");// RR
					} else {
						tcell.setStyleName("cellBorder");
						tableRow.setProperty("height", "0.2444in");
						tcell.setProperty("height", "0.2444in");
					}

					LabelHandle label = efactory.newLabel(null);
					label.setText(cell);
					if (!isFirstRow && cellIndex > 0) {
						label.setText("");
						double value = Double.parseDouble(cell);
						if (value > 0.5 && value <= 1) {
							tcell.setProperty("backgroundColor", "#06AA2A");
						} else if (value > 0 && value <= 0.5) {
							tcell.setProperty("backgroundColor", "#FD7900");
						} else if (value == 0) {
							tcell.setProperty("backgroundColor", "#FF1D00");
						}
					}

					label.setStyleName("");
					tcell.getContent().add(label);
					cellIndex++;
				}
				dashTable
						.insertRow(new RowOperationParameters(2, -1, rowIndex));
				isFirstRow = false;
			}
		}

		design.getBody().add(dashTable);

		String strTimeStamp = System.currentTimeMillis()+"";
		mergeReports = new MergeReports();
		if (isImageGeneration) {
			design.saveAs(ApplicationConstants.REPORT_FOLDER + Path.SEPARATOR
					+ strTimeStamp + graphType
					+ "imagedashboard1.rptdesign");
			mergeReports.setReportDesignPath(ApplicationConstants.REPORT_FOLDER
					+ Path.SEPARATOR + strTimeStamp + graphType
					+ "imagedashboard1.rptdesign");
		} else {
			design.saveAs(ApplicationConstants.REPORT_FOLDER + Path.SEPARATOR
					+ strTimeStamp + graphType + "dashboard1.rptdesign");
			mergeReports
					.getDashBoardFilePath(ApplicationConstants.REPORT_FOLDER
							+ Path.SEPARATOR + strTimeStamp + graphType
							+ "dashboard1.rptdesign");
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

	public static ArrayList<String> getValues(String inputPath, String csvFile) {
		ArrayList<String> aList = new ArrayList<String>();
		try {
			BufferedReader br = new BufferedReader(new FileReader(inputPath
					+ Path.SEPARATOR + csvFile));
			String line = "";

			// read comma separated file line by line
			while ((line = br.readLine()) != null) {
				aList.add(line);
			}
		} catch (IOException e) {
			System.out.println("exception  " + e.toString());
		}
		return aList;
	}

	/**
	 * @return Returns the value of inputPath
	 */
	public String getInputPath() {
		return inputPath;
	}

	/**
	 * @param inputPath
	 *            : Sets the value to inputPath
	 */
	public void setInputPath(String inputPath) {
		this.inputPath = inputPath;
	}

	public TreeMap<String, String> getMiscValues() {
		TreeMap<String, String> tmap = new TreeMap<String, String>();

		try {
			NodeList nList = doc.getElementsByTagName("head");

			for (int temp = 0; temp < nList.getLength(); temp++) {
				Node nNode = nList.item(temp);
				Element eElement = (Element) nNode;

				NodeList refNodes = nNode.getChildNodes();

				String title = getTagValue("title", eElement);
				String publisher = getTagValue("publisher", eElement);
				String color = getTagValue("color", eElement);
				String image = getTagValue("image", eElement);
				String date = getTagValue("date", eElement);
				String ooyalalogo = getTagValue("ooyalalogo", eElement);

				tmap.put("title", title);
				tmap.put("publisher", publisher);
				tmap.put("color", color);
				tmap.put("image", image);
				tmap.put("date", date);
				tmap.put("ooyalalogo", ooyalalogo);
			}
		} catch (Exception e) {

		}

		return tmap;
	}

	public static String getTagValue(String sTag, Element eElement) {
		NodeList nlList = eElement.getElementsByTagName(sTag).item(0)
				.getChildNodes();

		Node nValue = (Node) nlList.item(0);

		return nValue.getNodeValue();
	}

	public boolean isImageGenerate() {
		return isImageGenerate;
	}

	public void setImageGenerate(boolean isImageGenerate) {
		this.isImageGenerate = isImageGenerate;
	}
}
