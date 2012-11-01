/**
 * Copyright (c) Ooyala Inc, 2012
 * Author     : Madheswaran
 * Version    : $Revision: 1.54 $
 * Updated-On : $Date: 2012/10/31 08:37:51 $
 *
 * Purpose:
 * A program to hold various constants required for the tool. 
 * Change History:
 * 2012-08-07, Madheswaran: Initial program.
 */

package com.ooyala.tools.pdfreport;

import java.io.IOException;
import java.util.ArrayList;

import org.apache.log4j.Logger;
import org.eclipse.birt.chart.model.ChartWithAxes;
import org.eclipse.birt.chart.model.attribute.Anchor;
import org.eclipse.birt.chart.model.attribute.AxisType;
import org.eclipse.birt.chart.model.attribute.ChartDimension;
import org.eclipse.birt.chart.model.attribute.Direction;
import org.eclipse.birt.chart.model.attribute.FontDefinition;
import org.eclipse.birt.chart.model.attribute.HorizontalAlignment;
import org.eclipse.birt.chart.model.attribute.IntersectionType;
import org.eclipse.birt.chart.model.attribute.Orientation;
import org.eclipse.birt.chart.model.attribute.Position;
import org.eclipse.birt.chart.model.attribute.TextAlignment;
import org.eclipse.birt.chart.model.attribute.TickStyle;
import org.eclipse.birt.chart.model.attribute.VerticalAlignment;
import org.eclipse.birt.chart.model.attribute.impl.BoundsImpl;
import org.eclipse.birt.chart.model.attribute.impl.ColorDefinitionImpl;
import org.eclipse.birt.chart.model.attribute.impl.FontDefinitionImpl;
import org.eclipse.birt.chart.model.attribute.impl.TextAlignmentImpl;
import org.eclipse.birt.chart.model.component.Axis;
import org.eclipse.birt.chart.model.component.Series;
import org.eclipse.birt.chart.model.component.impl.SeriesImpl;
import org.eclipse.birt.chart.model.data.BaseSampleData;
import org.eclipse.birt.chart.model.data.DataFactory;
import org.eclipse.birt.chart.model.data.OrthogonalSampleData;
import org.eclipse.birt.chart.model.data.Query;
import org.eclipse.birt.chart.model.data.SampleData;
import org.eclipse.birt.chart.model.data.SeriesDefinition;
import org.eclipse.birt.chart.model.data.impl.QueryImpl;
import org.eclipse.birt.chart.model.data.impl.SeriesDefinitionImpl;
import org.eclipse.birt.chart.model.impl.ChartWithAxesImpl;
import org.eclipse.birt.chart.model.layout.Legend;
import org.eclipse.birt.chart.model.type.BarSeries;
import org.eclipse.birt.chart.model.type.impl.BarSeriesImpl;
import org.eclipse.birt.report.model.api.CellHandle;
import org.eclipse.birt.report.model.api.DesignEngine;
import org.eclipse.birt.report.model.api.ElementFactory;
import org.eclipse.birt.report.model.api.ExtendedItemHandle;
import org.eclipse.birt.report.model.api.GridHandle;
import org.eclipse.birt.report.model.api.ImageHandle;
import org.eclipse.birt.report.model.api.OdaDataSetHandle;
import org.eclipse.birt.report.model.api.OdaDataSourceHandle;
import org.eclipse.birt.report.model.api.PropertyHandle;
import org.eclipse.birt.report.model.api.ReportDesignHandle;
import org.eclipse.birt.report.model.api.RowHandle;
import org.eclipse.birt.report.model.api.SessionHandle;
import org.eclipse.birt.report.model.api.SimpleMasterPageHandle;
import org.eclipse.birt.report.model.api.StructureFactory;
import org.eclipse.birt.report.model.api.StyleHandle;
import org.eclipse.birt.report.model.api.TableHandle;
import org.eclipse.birt.report.model.api.TextItemHandle;
import org.eclipse.birt.report.model.api.activity.SemanticException;
import org.eclipse.birt.report.model.api.command.ContentException;
import org.eclipse.birt.report.model.api.command.NameException;
import org.eclipse.birt.report.model.api.command.StyleException;
import org.eclipse.birt.report.model.api.elements.structures.ComputedColumn;
import org.eclipse.birt.report.model.api.extension.ExtendedElementException;
import org.eclipse.birt.report.model.elements.interfaces.ISimpleMasterPageModel;
import org.eclipse.core.runtime.Path;

public class StackedBarChart {

	static Logger log = Logger.getLogger(StackedBarChart.class.getName());
	private ReportDesignHandle reportDesignHandle = null;
	private ElementFactory elementFactory = null;
	private String dataSetName = "datasetstackedbarchart"+System.currentTimeMillis();
	MergeReports mergeReports = null;
	String rptdesign = "";
	String inputPath = null;
	String footerTitle = null;
	String footerPublisher = null;
	int pageNumber = 0;
	String strPageNumber = "";
	boolean isImageGenerate = false;

	public boolean isImageGenerate() {
		return isImageGenerate;
	}

	public void setImageGenerate(boolean isImageGenerate) {
		this.isImageGenerate = isImageGenerate;
	}

	/**
	 * 
	 * @param graphType
	 * @param chartSrc
	 * @param headerTitle
	 * @param chartTitle
	 * @param chartXaxis
	 * @param chartYaxis
	 * @throws SemanticException
	 * @throws IOException
	 */

	public void createReport(String graphType, String chartSrc,
			String headerTitle, String chartTitle, String chartXaxis,
			String chartYaxis, String chartYaxis2, String chartYaxis3, String chartYaxis4,
			String leftHeader, String leftBlock, String rightHeader,
			String rightItem, String inputPath, String title, String publisher,
			int pageNumber, String strPageNumber,
			ArrayList<String> aListBlocks, ArrayList<String> aListItems,
			boolean isImageGeneration, String strLHS, String strRHS, String color)
			throws SemanticException, IOException {

		try {
			this.inputPath = inputPath;
			setImageGenerate(isImageGeneration);
			setInputPath(inputPath);
			this.footerTitle = title;
			setFooterPublisher(publisher);
			setFooterTitle(footerTitle);
			this.pageNumber = pageNumber;
			setStrPageNumber(strPageNumber);
			init();
			ApplicationConstants.APP_LOGGER.info(" init method finished ");
			createMasterPages(headerTitle,color);
			ApplicationConstants.APP_LOGGER.info("master page finished ");
			createDataSources();
			ApplicationConstants.APP_LOGGER.info(" Data source finished ");
			createDataSets(chartSrc);
			ApplicationConstants.APP_LOGGER.info(" Data set finished ");
			createBody(graphType, chartTitle, chartXaxis, chartYaxis,
					chartYaxis2, chartYaxis3,chartYaxis4, leftHeader, leftBlock,
					rightHeader, rightItem, aListBlocks, aListItems, strLHS,
					strRHS, color);
			ApplicationConstants.APP_LOGGER.info("end");

		} catch (Exception e) {
			ApplicationConstants.APP_LOGGER.info(" Error occur "
					+ e.getMessage());

		}

		// save the report page
		try {
			String strTimeStamp = System.currentTimeMillis() + "";
			mergeReports = new MergeReports();
			if (isImageGeneration) {
				reportDesignHandle.saveAs(ApplicationConstants.REPORT_FOLDER
						+ Path.SEPARATOR + strTimeStamp + graphType
						+ "stackedBarImage.rptdesign");
				rptdesign = ApplicationConstants.REPORT_FOLDER + Path.SEPARATOR
						+ strTimeStamp + graphType
						+ "stackedBarImage.rptdesign";
				mergeReports.setReportDesignPath(rptdesign);
			} else {
				reportDesignHandle.saveAs(ApplicationConstants.REPORT_FOLDER
						+ Path.SEPARATOR + strTimeStamp + graphType
						+ "StackedBar.rptdesign");
				rptdesign = ApplicationConstants.REPORT_FOLDER + Path.SEPARATOR
						+ strTimeStamp + graphType + "StackedBar.rptdesign";
				mergeReports.getBarChartFilePath(rptdesign);
			}

			/*
			 * reportDesignHandle.saveAs(ApplicationConstants.REPORT_FOLDER+Path.
			 * SEPARATOR+chartTitle.replace(" ", "_")+"StackedBar.rptdesign");
			 * 
			 * mergeReports = new MergeReports(); rptdesign
			 * =ApplicationConstants
			 * .REPORT_FOLDER+Path.SEPARATOR+chartTitle.replace(" ",
			 * "_")+"StackedBar.rptdesign";
			 * mergeReports.getStackedBarChartFilePath(rptdesign);
			 */
		} catch (Exception e) {

			log.error(e.getMessage());

		}
	}

	/**
	 * 
	 * @throws ContentException
	 * @throws NameException
	 */
	private void createMasterPages(String headerTitle,String color)
			throws SemanticException, ContentException, NameException,
			StyleException {
		SimpleMasterPageHandle simpleMasterPage = elementFactory
				.newSimpleMasterPage("Master Page");//$NON-NLS-1$
		
		if(isImageGenerate){
			simpleMasterPage.setProperty("type","custom");
			simpleMasterPage.setProperty("height", "6.5in");
			simpleMasterPage.setProperty("width", "8.5in");
		}
		
		if(!isImageGenerate){
				if (getPageNumber() == 0) {
					simpleMasterPage.setProperty("headerHeight", "1.0in");
		
				} else {
					simpleMasterPage.setProperty("headerHeight", "0.5in");
				}
				simpleMasterPage.setProperty("footerHeight", "0.5in");
		}		

		reportDesignHandle.getMasterPages().add(simpleMasterPage);
		try {

			if (!isImageGenerate)
			{
			GridHandle headerGrid = reportDesignHandle.getElementFactory()
					.newGridItem(null/* name */, 1 /* cols */, 2 /* row */);

			RowHandle rowTitle33 = (RowHandle) headerGrid.getRows().get(0);
			if (getPageNumber()==0)
			{
				rowTitle33.setProperty("height", "0.724in");
			}				
			// rowTitle33.setProperty("height","1.7729166666666666in");

			CellHandle cellTitle03 = (CellHandle) rowTitle33.getCells().get(0);
			ChartTitleHandle chartTitleHandle = new ChartTitleHandle();
			if (getPageNumber() == 0) {
				TextItemHandle textItem3 = reportDesignHandle
						.getElementFactory().newTextItem("test5");
				textItem3.setContentType("auto");
				textItem3.setContent(headerTitle);
				textItem3.setContent(headerTitle.toUpperCase());
				textItem3.setProperty("fontSize", "30pt");
				textItem3.setProperty("color", color);
				textItem3.setProperty("fontFamily", "Helvetica");

				textItem3.setProperty("textAlign", "left");
				// textItem.setProperty("style", "textAlignRightStyle");
				/*
				 * if the isImageGenerate is true, then dont add the Header and
				 * footer.
				 */
					cellTitle03.getContent().add(textItem3);

			} else {
					GridHandle header_inner_grid = chartTitleHandle.getChartTitle(
							reportDesignHandle, headerTitle);					
					cellTitle03.getContent().add(header_inner_grid);
			}
			
			RowHandle rowLine = (RowHandle) headerGrid.getRows().get(1);
			rowLine = chartTitleHandle.getThinCustomLine(reportDesignHandle,rowLine,color,"header");			
//			// rowLine.setProperty("height","0.9729166666666666in");
//			CellHandle cellLine0 = (CellHandle) rowLine.getCells().get(0);
//			ImageHandle imageLine = elementFactory.newImage("image1");
//			/*
//			 * if the isImageGenerate is true, then dont add the Header and
//			 * footer.
//			 */
//			if (!isImageGenerate) {
//				cellLine0.getContent().add(imageLine);
//			}
//			imageLine.setProperty("width", "8.522916666666667in");
//			// rptdesign ="./report/"+chartTitle+"barchart.rptdesign";
//			imageLine.setFile(ApplicationConstants.CONFIG_FOLDER
//					+ Path.SEPARATOR + ApplicationConstants.OOYALA_IMAGE_LINE);
			// imageLine.setFile(""+getInputPath()+Path.SEPARATOR+Path.SEPARATOR+ApplicationConstants.OOYALA_IMAGE_LINE);
			/*
			 * if the isImageGenerate is true, then dont add the Header and
			 * footer.
			 */
				simpleMasterPage.getSlot(
						ISimpleMasterPageModel.PAGE_HEADER_SLOT)
						.add(headerGrid);
			
			
			// footer
			GridHandle footerGrid = reportDesignHandle.getElementFactory()
					.newGridItem(null/* name */, 1 /* cols */, 2 /* row */);
			RowHandle rowFooterLine = (RowHandle) footerGrid.getRows().get(0);
			rowFooterLine = chartTitleHandle.getThinCustomLine(reportDesignHandle,rowFooterLine,color,"footer");
			// rowLine.setProperty("height","0.9729166666666666in");
//			CellHandle cellFooterLine0 = (CellHandle) rowFooterLine.getCells()
//					.get(0);
//			ImageHandle imageFooterLine = elementFactory
//					.newImage("imagefooter1");
//			/*
//			 * if the isImageGenerate is true, then dont add the Header and
//			 * footer.
//			 */
//			cellFooterLine0.getContent().add(imageFooterLine);
//			imageFooterLine.setProperty("width", "8.522916666666667in");
//			imageFooterLine.setProperty("paddingTop", "0pt");
//			imageFooterLine.setProperty("paddingLeft", "0pt");
//			imageFooterLine.setProperty("paddingBottom", "0pt");
//			imageFooterLine.setProperty("paddingRight", "0pt");
//			// imageFooterLine.setFile(""+getInputPath()+Path.SEPARATOR+Path.SEPARATOR+ApplicationConstants.OOYALA_IMAGE_LINE);
//			imageFooterLine.setFile(ApplicationConstants.CONFIG_FOLDER
//					+ Path.SEPARATOR + ApplicationConstants.OOYALA_IMAGE_LINE);

			RowHandle rowTitle = (RowHandle) footerGrid.getRows().get(1);
			// rowTitle.setProperty("height","0.5729166666666666in");

			CellHandle cellTitle0 = (CellHandle) rowTitle.getCells().get(0);
			GridHandle footer_inner_grid = reportDesignHandle
					.getElementFactory()
					.newGridItem(null, 2 /* cols */, 1 /* row */);
			/*
			 * if the isImageGenerate is true, then dont add the Header and
			 * footer.
			 */
			cellTitle0.getContent().add(footer_inner_grid);
			RowHandle rowFooterOoyalaImg = (RowHandle) footer_inner_grid
					.getRows().get(0);

			CellHandle cellInnerTitle0 = (CellHandle) rowFooterOoyalaImg
					.getCells().get(0);
			ImageHandle imageTitle0 = elementFactory.newImage(null);
			//imageTitle0.setProperty("height", "0.25875in");
			//imageTitle0.setProperty("width", "2.00000in");
			imageTitle0.setProperty("height", "0.40625in");
			imageTitle0.setProperty("width", "2.0729166666666665in");
			imageTitle0.setFile("" + getInputPath() + Path.SEPARATOR
					+ ApplicationConstants.IMAGE_OOYALALOGO);
			cellInnerTitle0.getContent().add(imageTitle0);

			CellHandle cellTitle1 = (CellHandle) rowFooterOoyalaImg.getCells()
					.get(1);
			TextItemHandle textItem = reportDesignHandle.getElementFactory()
					.newTextItem("test");
			textItem.setProperty("contentType", "html");

			// textItem.setProperty("<FONT size="7pt" color="blue" face="Helvetica">QoS Health</FONT><FONT size="7pt" color="gray" face="Helvetica">INSIGHT REPORT:</FONT> <FONT size="7pt" color="gray" face="Helvetica">PAC-12</FONT>");

			textItem.setProperty("fontSize", "7pt");
			textItem.setProperty("marginTop","8");
			if (getFooterTitle() != null) {
				textItem.setContent("<BR><B><FONT size=\"\" color=\""+color+"\" face=\"Helvetica\">"
						+ getFooterTitle().toUpperCase()
						+ "</FONT></B>"
						+

						"&nbsp;&nbsp;"
						+

						"<B><FONT size=\"\" color=\"#6A6468\" face=\"Helvetica\">"+ApplicationConstants.REPORT_NAME+"</FONT></B>"
						+

						"&nbsp;&nbsp;"
						+

						"<FONT size=\"\" color=\"#717174\" face=\"Helvetica\">"
						+ getFooterPublisher()
						+ "&nbsp;&nbsp;&nbsp;&nbsp;"
						+ getStrPageNumber() + "</FONT>"

				);
			}
			textItem.setProperty("textAlign", "right");
			// textItem.setProperty("style", "textAlignRightStyle");

			// textItem.setProperty("color","#7177AD");
			/*
			 * if the isImageGenerate is true, then dont add the Header and
			 * footer.
			 */
				cellTitle1.getContent().add(textItem);
				simpleMasterPage.getSlot(
						ISimpleMasterPageModel.PAGE_FOOTER_SLOT)
						.add(footerGrid);
		}
		} catch (Exception e) {

			ApplicationConstants.APP_LOGGER
					.error("error in Stacked Bar  chart " + e.getMessage());

		}

	}

	/**
	 * 
	 * @ return void
	 */

	private void init() {
		// we need a handle of session of design engine
		 @SuppressWarnings(value={"deprecation"})
		SessionHandle sessionHandle = DesignEngine.newSession(null);
		reportDesignHandle = sessionHandle.createDesign();
		elementFactory = reportDesignHandle.getElementFactory();

	}

	/**
	 * 
	 * Create data set for the report and read the value from csv file
	 * 
	 */
	private void createDataSets(String chartSrc) throws SemanticException,
			IOException {

		try {
			OdaDataSetHandle dataSetHandle = ((ElementFactory) elementFactory)
					.newOdaDataSet(dataSetName,
							"org.eclipse.datatools.connectivity.oda.flatfile.dataSet");
			dataSetHandle.setDataSource("Data Source");
			// import csv file
			dataSetHandle.setQueryText("select * from " + chartSrc + "");
			reportDesignHandle.getDataSets().add(dataSetHandle);
		} catch (Exception ex) {
			ApplicationConstants.APP_LOGGER.info(" ex" + ex.getMessage());
		}
	}

	/**
	 * 
	 * 
	 * @throws SemanticException
	 *             @ return void
	 */

	private void createDataSources() throws SemanticException {

		OdaDataSourceHandle dsHandle = elementFactory.newOdaDataSource(
				"Data Source",
				"org.eclipse.datatools.connectivity.oda.flatfile");
		// Input location

		dsHandle.setProperty("HOME", "" + getInputPath() + Path.SEPARATOR + "");
		dsHandle.setProperty("CHARSET", "UTF-8");
		dsHandle.setProperty("INCLTYPELINE", "YES");
		dsHandle.setProperty("DELIMTYPE", "COMMA");
		dsHandle.setProperty("INCLCOLUMNNAME", "YES");
		dsHandle.setProperty("TRAILNULLCOLS", "NO");
		reportDesignHandle.getDataSources().add(dsHandle);
	}

	/**
	 * 
	 * @param graphType
	 * @param chartTitle
	 * @param chartXaxis
	 * @param chartYaxis
	 * @throws SemanticException
	 *             @ return void
	 */

	private void createBody(String graphType, String chartTitle,
			String chartXaxis, String chartYaxis, String chartYaxis2,
			String chartYaxis3, String chartYaxis4,String leftHeader, String leftBlock,
			String rightHeader, String rightItem,
			ArrayList<String> aListBlocks, ArrayList<String> aListItems,
			String strLHS, String strRHS, String color) throws SemanticException {

		TableHandle table = elementFactory.newTableItem(null, 1, 1, 1, 1);
		table.setWidth("100%");
		reportDesignHandle.getBody().add(
				createMulitYSeriesChart(graphType, chartTitle, chartXaxis,
						chartYaxis, chartYaxis2, chartYaxis3,chartYaxis4));

		if (!isImageGenerate) 
		{	
			StyleHandle textHeadingStyle = elementFactory.newStyle("textHeadingStyle");
			textHeadingStyle.setProperty(StyleHandle.FONT_FAMILY_PROP,"Helvetica");
			textHeadingStyle.setProperty(StyleHandle.COLOR_PROP, "#636466");
			textHeadingStyle.setProperty(StyleHandle.FONT_SIZE_PROP, "20");
			reportDesignHandle.getStyles().add(textHeadingStyle);
			
			StyleHandle textStyle = elementFactory.newStyle("textStyle");
			textStyle.setProperty(StyleHandle.FONT_FAMILY_PROP, "Helvetica");
			textStyle.setProperty(StyleHandle.COLOR_PROP, "#636466");
			textStyle.setProperty(StyleHandle.FONT_SIZE_PROP, "13");
			reportDesignHandle.getStyles().add(textStyle);
			
			//line image - starts
			GridHandle lineGrid = reportDesignHandle.getElementFactory()
					.newGridItem(null/* name */, 1 /* cols */, 1 /* row */);
			RowHandle rowLine = (RowHandle) lineGrid.getRows().get(0);
			ChartTitleHandle chartTitleHandle = new ChartTitleHandle();
			rowLine = chartTitleHandle.getThinCustomLine(reportDesignHandle,rowLine,color,"image");
			
			reportDesignHandle.getBody().add(lineGrid);
			//line image - ends
			
			//content - starts
			GridHandle contentGrid = reportDesignHandle.getElementFactory()
					.newGridItem(null/* name */, 2 /* cols */, 2 /* row */);

			ChartBodyContentHandle chartBodyContentHandle = new ChartBodyContentHandle();
			
			// left hand side heading - starts
			RowHandle rowItemsAndBlocksTitle = (RowHandle) contentGrid.getRows().get(0);
			CellHandle rowItemsAndBlocksTitle_cell0 = (CellHandle) rowItemsAndBlocksTitle.getCells().get(0);
			rowItemsAndBlocksTitle_cell0 = chartBodyContentHandle.getBodyHeading(rowItemsAndBlocksTitle_cell0,elementFactory,leftHeader,"textHeadingStyle");
			// left hand side heading - ends			

			// left hand side - starts
			RowHandle rowItemsAndBlocks = (RowHandle) contentGrid.getRows().get(1);
			CellHandle row2_cell1 = (CellHandle) rowItemsAndBlocks.getCells().get(0);
				
			GridHandle innergrid_LHS = chartBodyContentHandle.getBodyContent(reportDesignHandle,elementFactory,strLHS,"textStyle",aListBlocks);
			row2_cell1.getContent().add(innergrid_LHS);
			// left hand side - ends
			
			// right hand side heading - starts
			CellHandle rowItemsAndBlocksTitle_cell1 = (CellHandle) rowItemsAndBlocksTitle.getCells().get(1);
			rowItemsAndBlocksTitle_cell1 = chartBodyContentHandle.getBodyHeading(rowItemsAndBlocksTitle_cell1,elementFactory,rightHeader,"textHeadingStyle");
			// right hand side heading - ends			

			// right hand side - starts
			CellHandle row2_cell2 = (CellHandle) rowItemsAndBlocks.getCells().get(1);
			GridHandle innergrid_RHS = chartBodyContentHandle.getBodyContent(reportDesignHandle,elementFactory,strRHS,"textStyle",aListItems);
			row2_cell2.getContent().add(innergrid_RHS);
			// right hand side - ends
			
			reportDesignHandle.getBody().add(contentGrid);
			//content - ends
		}
	}

	/**
	 * 
	 * @param graphType
	 * @param chartTitle
	 * @param chartXaxis
	 * @param chartYaxis
	 * @return ExtendedItemHandle
	 */
	protected ExtendedItemHandle createMulitYSeriesChart(String graphType,
			String chartTitle, String chartXaxis, String chartYaxis,
			String chartYaxis2, String chartYaxis3,String chartYaxis4) {
		BarSeries bs1,bs2,bs3,bs4=null;
		ExtendedItemHandle eih = elementFactory.newExtendedItem(null, "Chart");//$NON-NLS-1$
		try {
			// set height and width for chart
			eih.setHeight("4.50in");//$NON-NLS-1$
			eih.setWidth("7.50in");//$NON-NLS-1$
			eih.setProperty(ExtendedItemHandle.DATA_SET_PROP, dataSetName);//$NON-NLS-1$
		} catch (SemanticException e) {
			e.printStackTrace();
		}

		System.setProperty("STANDALONE", "true");
		ChartWithAxes cwaBar = ChartWithAxesImpl.create();
		cwaBar.setType("Bar Chart"); //$NON-NLS-1$
		cwaBar.setSubType("Stacked");
		// cwaBar.setUnitSpacing(30.0);
		cwaBar.setOrientation(Orientation.VERTICAL_LITERAL);

		cwaBar.getBlock().setBounds(BoundsImpl.create(0, 0, 450, 175));

		SampleData sd = DataFactory.eINSTANCE.createSampleData();
		BaseSampleData sdBase = DataFactory.eINSTANCE.createBaseSampleData();
		sdBase.setDataSetRepresentation("");//$NON-NLS-1$
		sd.getBaseSampleData().add(sdBase);
		OrthogonalSampleData sdOrthogonal = DataFactory.eINSTANCE
				.createOrthogonalSampleData();
		sdOrthogonal.setDataSetRepresentation("");//$NON-NLS-1$
		sdOrthogonal.setSeriesDefinitionIndex(0);
		sd.getOrthogonalSampleData().add(sdOrthogonal);

		OrthogonalSampleData sdOrthogonal1 = DataFactory.eINSTANCE
				.createOrthogonalSampleData();
		sdOrthogonal1.setDataSetRepresentation("");//$NON-NLS-1$
		sdOrthogonal1.setSeriesDefinitionIndex(0);
		sd.getOrthogonalSampleData().add(sdOrthogonal);
		sd.getOrthogonalSampleData().add(sdOrthogonal1);

		cwaBar.setSampleData(sd);
		cwaBar.getBlock().setBackground(ColorDefinitionImpl.WHITE());
		cwaBar.getBlock().getOutline().setVisible(false);
		cwaBar.setDimension(ChartDimension.TWO_DIMENSIONAL_LITERAL);

		// CUSTOMIZE THE PLOT

		TextAlignment ta = TextAlignmentImpl.create();
		ta.setHorizontalAlignment(HorizontalAlignment.LEFT_LITERAL);
		ta.setVerticalAlignment(VerticalAlignment.TOP_LITERAL);
		FontDefinition font = FontDefinitionImpl.create("Helvetica", 14.0f,
				true, false, false, false, false, 0.0, ta);

		// Title font
		cwaBar.getTitle().getLabel().getCaption()
				.setValue(chartTitle.toUpperCase());
		cwaBar.getTitle().getLabel().getCaption().setFont(font);
		cwaBar.getTitle().getLabel().getCaption().getFont().setAlignment(ta);
		cwaBar.getTitle().getLabel().getCaption()
				.setColor(ColorDefinitionImpl.create(211, 211, 211));

		// CUSTOMIZE THE LEGEND
		TextAlignment taLegend = TextAlignmentImpl.create();
		taLegend.setHorizontalAlignment(HorizontalAlignment.CENTER_LITERAL);
		taLegend.setVerticalAlignment(VerticalAlignment.CENTER_LITERAL);
		FontDefinition legendFont = FontDefinitionImpl.create(
				"Helvetica", 16.0f, true, false, false, false, false,
				0.0, taLegend);

		
		
		Legend lg = cwaBar.getLegend();
		lg.getSeparator().setVisible(false);
		lg.getText().setColor(ColorDefinitionImpl.create(190, 190, 190));
		lg.getText().setFont(legendFont);
		lg.getInsets().set(10, 5, 0, 0);
		lg.setAnchor(Anchor.NORTH_LITERAL);
		lg.setVisible(true);
		lg.setDirection(Direction.TOP_BOTTOM_LITERAL);

		// CUSTOMIZE THE X-AXIS

		Axis xAxisPrimary = cwaBar.getPrimaryBaseAxes()[0];
		xAxisPrimary.setType(AxisType.TEXT_LITERAL);
		xAxisPrimary.getOrigin().setType(IntersectionType.VALUE_LITERAL);
		TextAlignment taXaxis = TextAlignmentImpl.create();
		taXaxis.setHorizontalAlignment(HorizontalAlignment.CENTER_LITERAL);
		taXaxis.setVerticalAlignment(VerticalAlignment.CENTER_LITERAL);
		FontDefinition fontXaxis = FontDefinitionImpl.create("Helvetica",
				12.0f, true, false, false, false, false, 0.0, taXaxis);
		xAxisPrimary.getTitle().getCaption().setValue(chartXaxis);
		xAxisPrimary.getTitle().getCaption().setFont(fontXaxis);
		// xaxis color
		xAxisPrimary.getTitle().getCaption()
				.setColor(ColorDefinitionImpl.create(190, 190, 190));
		xAxisPrimary.getTitle().setVisible(true);
		xAxisPrimary.getMajorGrid().getTickAttributes().setVisible(false);
		FontDefinition labelFont = FontDefinitionImpl.create(
				"Helvetica", 10.0f, true, false, false, false, false,
				0.0, taXaxis);

		xAxisPrimary.getLabel().getCaption().setColor(ColorDefinitionImpl.create(190, 190, 190));
		xAxisPrimary.getLabel().getCaption().setFont(labelFont);

		// CUSTOMIZE THE Y-AXIS
		Axis yAxisPrimary = cwaBar.getPrimaryOrthogonalAxis(xAxisPrimary);
		yAxisPrimary.getMajorGrid().setTickStyle(TickStyle.LEFT_LITERAL);
		yAxisPrimary.getMajorGrid().getLineAttributes()
				.setColor(ColorDefinitionImpl.create(128, 128, 64));
		yAxisPrimary.getMajorGrid().getLineAttributes().setVisible(true);
		yAxisPrimary.setType(AxisType.LINEAR_LITERAL);
		yAxisPrimary.getLabel().getCaption().getFont().setRotation(0);
		yAxisPrimary.getMajorGrid().getTickAttributes().setVisible(false);

		// y axis font
		TextAlignment ta1 = TextAlignmentImpl.create();
		ta1.setHorizontalAlignment(HorizontalAlignment.CENTER_LITERAL);
		ta1.setVerticalAlignment(VerticalAlignment.CENTER_LITERAL);
		FontDefinition fontYaxis = FontDefinitionImpl.create("Helvetica",
				12.0f, true, false, false, false, false, 90.0, ta1);
		// y Axis title
		yAxisPrimary.getTitle().getCaption().setValue(chartYaxis);
		yAxisPrimary.getTitle().getCaption().setFont(fontYaxis);
		yAxisPrimary.getTitle().getCaption()
				.setColor(ColorDefinitionImpl.create(190, 190, 190));
		yAxisPrimary.getTitle().setVisible(false);
		yAxisPrimary.getLineAttributes().setVisible(false);
		
		FontDefinition labelYAxisFont = FontDefinitionImpl.create(
				"Helvetica", 10.0f, true, false, false, false, false,
				0.0, ta1);
		yAxisPrimary.getLabel().getCaption().setColor(ColorDefinitionImpl.create(190, 190, 190));
		yAxisPrimary.getLabel().getCaption().setFont(labelYAxisFont);
		cwaBar.setOrientation(Orientation.VERTICAL_LITERAL);

		// Plot
		cwaBar.getBlock().setBackground(ColorDefinitionImpl.WHITE());

		// format Specifier
		/*
		 * NumberFormatSpecifier nf=NumberFormatSpecifierImpl.create();
		 * nf.setSuffix("%"); nf.setFractionDigits(0);
		 * yAxisPrimary.setFormatSpecifier(nf);
		 */

		yAxisPrimary = cwaBar.getOrthogonalAxes(xAxisPrimary, true)[0];

		// Min and Max value
		/*
		 * sc.setMin(NumberDataElementImpl.create(0.0d));
		 * sc.setMax(NumberDataElementImpl.create(500.0d)); sc.setStep(25.0);
		 * sc.setShowOutside(false); sc.setAutoExpand(true);
		 */

		// CREATE THE CATEGORY BASE SERIES

		try {
			
			ComputedColumn cs1 = null, cs2 = null, cs3 = null, cs4 =null,cs5= null;
			PropertyHandle cs = eih.getColumnBindings();
			cs1 = StructureFactory.createComputedColumn();
			cs2 = StructureFactory.createComputedColumn();
			cs3 = StructureFactory.createComputedColumn();
			
			cs1.setName(chartXaxis);
			cs2.setName(chartYaxis);
			cs1.setExpression("dataSetRow[\"" + chartXaxis + "\"]");
			cs2.setExpression("dataSetRow[\"" + chartYaxis + "\"]");
			cs.addItem(cs1);
			cs.addItem(cs2);
			if(chartYaxis2!=null){
				cs3.setName(chartYaxis2);
				cs3.setExpression("dataSetRow[\"" + chartYaxis2 + "\"]");
				cs.addItem(cs3);
			}
			
			if(chartYaxis3!=null){
				cs4 = StructureFactory.createComputedColumn();
				cs4.setName(chartYaxis3);
				cs4.setExpression("dataSetRow[\"" + chartYaxis3 + "\"]");
				cs.addItem(cs4);
			}
			if(chartYaxis4!=null){
				cs5 = StructureFactory.createComputedColumn();
				cs5.setName(chartYaxis4);
				cs5.setExpression("dataSetRow[\"" + chartYaxis4 + "\"]");
				cs.addItem(cs5);
			}
			

		} catch (SemanticException e) {
			e.printStackTrace();
		}

		// CREATE THE VALUE ORTHOGONAL SERIES

		Series seCategory = SeriesImpl.createDefault();
		Query xQ = QueryImpl.create("row[\"" + chartXaxis + "\"]");
		seCategory.getDataDefinition().add(xQ);
		SeriesDefinition sdX = SeriesDefinitionImpl.create();
		sdX.getSeriesPalette().update(ColorDefinitionImpl.create(0, 162, 210)); // SET
																				// THE
																				// COLORS
																				// IN
																				// THE
																				// PALETTE
		xAxisPrimary.getSeriesDefinitions().add(sdX);
		sdX.getSeries().add(seCategory);

		// Y serires
		
			bs1 = (BarSeries) BarSeriesImpl.create();
			bs1.setSeriesIdentifier("yaxis");
			Query yQ = QueryImpl.create("row[\"" + chartYaxis + "\"]");
			seCategory.getDataDefinition().add(yQ);
			bs1.getDataDefinition().add(yQ);
			bs1.setStacked(true);
			bs1.getLabel().setVisible(true);
			bs1.setRiserOutline(null);
			bs1.getLabel().setVisible(false);
			bs1.setLabelPosition(Position.INSIDE_LITERAL);
			
			SeriesDefinition sdY = SeriesDefinitionImpl.create();
			sdY.getSeriesPalette().shift(0);
			sdY.getSeriesPalette().update(ColorDefinitionImpl.create(124, 107, 85));
			yAxisPrimary.getSeriesDefinitions().add(sdY);
			sdY.getSeries().add(bs1);
			

		if(chartYaxis2!=null){
		 bs2 = (BarSeries) BarSeriesImpl.create();
		bs2.setSeriesIdentifier("yaxis2");
		Query yQ1 = QueryImpl.create("row[\"" + chartYaxis2 + "\"]");
		seCategory.getDataDefinition().add(yQ1);
		bs2.getDataDefinition().add(yQ1);
		bs2.setStacked(true);
		bs2.getLabel().setVisible(true);
		bs2.setRiserOutline(null);
		bs2.getLabel().setVisible(false);
		bs2.setLabelPosition(Position.INSIDE_LITERAL);
		
		SeriesDefinition sdY1 = SeriesDefinitionImpl.create();
		sdY1.getSeriesPalette().shift(0);
		sdY1.getSeriesPalette().update(
				ColorDefinitionImpl.create(253, 206, 158));
		yAxisPrimary.getSeriesDefinitions().add(sdY1);
		sdY1.getSeries().add(bs2);
		
		}
		

		if(chartYaxis3!=null){
		
				 bs3 = (BarSeries) BarSeriesImpl.create();
				bs3.setSeriesIdentifier("yaxis3");
				Query yQ2 = QueryImpl.create("row[\"" + chartYaxis3 + "\"]");
				seCategory.getDataDefinition().add(yQ2);
				bs3.getDataDefinition().add(yQ2);
				bs3.setStacked(true);
				bs3.getLabel().setVisible(true);
				bs3.setRiserOutline(null);
				bs3.getLabel().setVisible(false);
				bs3.setLabelPosition(Position.INSIDE_LITERAL);
				
				SeriesDefinition sdY2 = SeriesDefinitionImpl.create();

				sdY2.getSeriesPalette().shift(0);
				sdY2.getSeriesPalette()
						.update(ColorDefinitionImpl.create(22, 181, 204));
				yAxisPrimary.getSeriesDefinitions().add(sdY2);
				sdY2.getSeries().add(bs3);
				
		}
		
		
		if(chartYaxis4!=null){
		
				 bs4 = (BarSeries) BarSeriesImpl.create();
				bs4.setSeriesIdentifier("yaxis4");
				Query yQ3 = QueryImpl.create("row[\"" + chartYaxis4 + "\"]");
				seCategory.getDataDefinition().add(yQ3);
				bs4.getDataDefinition().add(yQ3);
				bs4.setStacked(true);
				bs4.getLabel().setVisible(true);
				bs4.setRiserOutline(null);
				bs4.getLabel().setVisible(false);
				bs4.setLabelPosition(Position.INSIDE_LITERAL);
				
				SeriesDefinition sdY3 = SeriesDefinitionImpl.create();
				sdY3.getSeriesPalette().shift(0);
				sdY3.getSeriesPalette()
						.update(ColorDefinitionImpl.create(113, 112, 115));
				yAxisPrimary.getSeriesDefinitions().add(sdY3);
				sdY3.getSeries().add(bs4);
		
		}
		

		try {
			// Add ChartReportItemImpl to ExtendedItemHandle
			eih.getReportItem().setProperty("chart.instance", cwaBar);
		} catch (ExtendedElementException e) {
			e.printStackTrace();
		}

		return eih;

	}

	public String getInputPath() {
		return inputPath;
	}

	public void setInputPath(String inputPath) {
		this.inputPath = inputPath;
	}

	/**
	 * @return the footerTitle
	 */
	public String getFooterTitle() {
		return footerTitle;
	}

	/**
	 * @param footerTitle
	 *            the footerTitle to set
	 */
	public void setFooterTitle(String footerTitle) {
		this.footerTitle = footerTitle;
	}

	/**
	 * @return the footerPublisher
	 */
	public String getFooterPublisher() {
		return footerPublisher;
	}

	/**
	 * @param footerPublisher
	 *            the footerPublisher to set
	 */
	public void setFooterPublisher(String footerPublisher) {
		this.footerPublisher = footerPublisher;
	}

	/**
	 * @return the pageNumber
	 */
	public int getPageNumber() {
		return pageNumber;
	}

	/**
	 * @param pageNumber
	 *            the pageNumber to set
	 */
	public void setPageNumber(int pageNumber) {
		this.pageNumber = pageNumber;
	}

	public String getStrPageNumber() {
		return strPageNumber;
	}

	public void setStrPageNumber(String strPageNumber) {
		this.strPageNumber = strPageNumber;
	}

}
