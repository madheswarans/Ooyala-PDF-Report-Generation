/**
 * Copyright (c) Ooyala Inc, 2012
 * Author     : Madheswaran
 * Version    : $Revision: 1.30 $
 * Updated-On : $Date: 2012/09/14 09:44:28 $
 *
 * Purpose:
 * A program to hold various constants required for the tool. 
 * Change History:
 * 2012-08-07, Madheswaran: Initial program.
 */

package com.ooyala.tools.pdfreport;

import java.io.ByteArrayOutputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import org.apache.log4j.Logger;
import org.eclipse.birt.core.exception.BirtException;
import org.eclipse.birt.core.framework.Platform;
import org.eclipse.birt.report.engine.api.EngineConfig;
import org.eclipse.birt.report.engine.api.EngineException;
import org.eclipse.birt.report.engine.api.IReportEngine;
import org.eclipse.birt.report.engine.api.IReportEngineFactory;
import org.eclipse.birt.report.engine.api.IReportRunnable;
import org.eclipse.birt.report.engine.api.IRunAndRenderTask;
import org.eclipse.birt.report.engine.api.PDFRenderOption;
import org.eclipse.core.runtime.Path;

import com.lowagie.text.DocumentException;
import com.lowagie.text.pdf.PdfCopyFields;
import com.lowagie.text.pdf.PdfReader;

public class MergeReports {

	static Logger log = Logger.getLogger(MergeReports.class.getName());
	// Output file
	public String DESTINATION_FILE_PATH = null;
	public String BIRT_HOME = "./lib/birt-runtime-3_7_2/birt-runtime-3_7_2/ReportEngine";
	public static List<String> reportGen = new ArrayList<String>();
	boolean isImageGenarate = false;
	
	public boolean isImageGenarate()
	{
		return isImageGenarate;
	}

	public void setImageGenarate(boolean isImageGenarate)
	{
		this.isImageGenarate = isImageGenarate;
	}



	/**
	 * List instance for storing list of report file paths
	 */
	public static List<String> reportDesigns = new ArrayList<String>();

	/**
	 * Method to add the report file paths into the list
	 * 
	 * @param path
	 */
	public void setReportDesignPath(String path) {
		reportDesigns.add(path);
	}

	/**
	 * @return void
	 * @param rptDesign
	 */
	public void getOoyalaPage2Path(String rptDesign) {
		try {

			reportGen.add(rptDesign);
		} catch (Exception e) {

			System.out.println(e.getMessage());
		}

	}

	public void getSummaryContentsPath() {
		try {

			reportGen.add("");

		} catch (Exception e) {

			System.out.println(e.getMessage());
		}

	}

	/**
	 * @return void
	 * @param rptDesign
	 */
	public void getTableOfContentsPath(String rptDesign) {
		try {

			reportGen.add(rptDesign);
		} catch (Exception e) {

			System.out.println(e.getMessage());
		}

	}

	/**
	 * @return void
	 * @param rptDesign
	 */
	public void getArrowPageContentsPath(String rptDesign) {
		try {

			reportGen.add(rptDesign);
		} catch (Exception e) {

			System.out.println(e.getMessage());
		}

	}

	/**
	 * 
	 * @param rptDesign
	 */
	public void getBarChartFilePath(String rptDesign) {
		try {

			reportGen.add(rptDesign);
		} catch (Exception e) {

			System.out.println(e.getMessage());
		}

	}

	/**
	 * @return void
	 * @param rptDesign
	 */

	public void getLineChartFilePath(String rptDesign) {
		try {

			reportGen.add(rptDesign);
		} catch (Exception ex) {
			System.out.println(ex.getMessage());
		}

	}

	/**
	 * @return void
	 * @param rptDesign
	 */
	public void getStackedBarChartFilePath(String rptDesign) {
		try {

			reportGen.add(rptDesign);
		} catch (Exception e) {

			System.out.println(e.getMessage());
		}

	}

	/**
	 * @return void
	 * @param rptDesign
	 */
	public void getBarLineChartFilePath(String rptDesign) {
		try {

			reportGen.add(rptDesign);
		} catch (Exception e) {

			System.out.println(e.getMessage());
		}

	}

	/**
	 * 
	 * 
	 */
	public void getDashBoardFilePath(String rptDesign) {
		try {

			reportGen.add(rptDesign);
		} catch (Exception e) {

			System.out.println(e.getMessage());
		}

	}

	public void getReportDesign() {
		try {

			// List<String> reportFilePathsToMerge =
			// getReportFilePathsToMerge();
			List<String> reportFilePathsToMerge = reportGen;
			runReport(reportFilePathsToMerge, false);
		} catch (Exception e) {

			System.out.println("error occured report design" + e.getMessage());

		}
	}

	/**
	 * This method will be called from the launch report to generate the PDF.
	 * It will verify the size of the design path list and pass that list to
	 * run method.
	 *   
	 * @return boolean
	 * @throws Exception
	 */
	public boolean generateImageReport() throws Exception {
		boolean isGenerated = false;
		
		/**
		 * Verify the list contains any design path value
		 */
		if (reportDesigns!=null && reportDesigns.size() > 0) {
			runReport(reportDesigns, true);
			isGenerated = true;
			Platform.shutdown();
		}
		
		/**
		 * Return the status of the pdf generation
		 */
		return isGenerated;
	}

	/**
	 * 
	 * @param reportFilePathsToMerge
	 * @throws Exception
	 */
	private void runReport(List<String> reportFilePathsToMerge, boolean isImageGenaration)
			throws Exception {
		setImageGenarate(isImageGenaration);
		List<ByteArrayOutputStream> byteArrayOutputStreams = new ArrayList<ByteArrayOutputStream>();
		IReportEngine engine = initBirtEngine();
		for (String filePath : reportFilePathsToMerge) {
			byteArrayOutputStreams.add(runTaskAndGetOutputStream(filePath,
					engine));
		}
		String generatedPDF = renderCombinedReportFromOutputStreams(byteArrayOutputStreams);
		engine.destroy();
		
		if(generatedPDF != null && isImageGenaration)
		{
			PDFtoImage.generateImage(generatedPDF);
		}
		
	}

	/**
	 * 
	 * 
	 * @return
	 * @throws BirtException
	 */
	private IReportEngine initBirtEngine() throws BirtException {
		EngineConfig config = new EngineConfig();
		config.setBIRTHome(BIRT_HOME);
		Platform.startup(config);
		IReportEngineFactory factory = (IReportEngineFactory) Platform
				.createFactoryObject(IReportEngineFactory.EXTENSION_REPORT_ENGINE_FACTORY);
		IReportEngine engine = factory.createReportEngine(config);
		return engine;
	}

	/**
	 * 
	 * @param filePath
	 * @param engine
	 * @return
	 * @throws EngineException
	 */
	private ByteArrayOutputStream runTaskAndGetOutputStream(String filePath,
			IReportEngine engine) throws EngineException {
		IReportRunnable design = engine.openReportDesign(filePath);
		IRunAndRenderTask task = engine.createRunAndRenderTask(design);

		PDFRenderOption options = new PDFRenderOption();
		ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
		options.setOutputStream(byteArrayOutputStream);
		options.setOutputFormat("pdf");
		task.setRenderOption(options);
		task.run();
		task.close();
		return byteArrayOutputStream;
	}

	/**
	 * @param byteArrayOutputStreams
	 * @throws DocumentException
	 * @throws IOException
	 */
	private String renderCombinedReportFromOutputStreams(
			List<ByteArrayOutputStream> byteArrayOutputStreams)
			throws DocumentException, IOException {

		SimpleDateFormat simpleDateFormat = new SimpleDateFormat(
				"yyyyMMddHHmmss");
		Date date = new Date();
		List<PdfReader> pdfReaders = new ArrayList<PdfReader>();
		String imageGenarateLocation ="";
		if(isImageGenarate){
			imageGenarateLocation = "_image";
		}
		String path = DESTINATION_FILE_PATH + Path.SEPARATOR + "Chart_"+imageGenarateLocation
				+ simpleDateFormat.format(date).toString() + ".pdf";
		
		PdfCopyFields pdfCopyFields = new PdfCopyFields(new FileOutputStream(path));
		for (ByteArrayOutputStream byteArrayOutputStream : byteArrayOutputStreams) {
			PdfReader pdfReader = new PdfReader(
					byteArrayOutputStream.toByteArray());
			pdfReaders.add(pdfReader);
		}
		for (PdfReader pdfReader : pdfReaders) {
			pdfCopyFields.addDocument(pdfReader);
		}
		pdfCopyFields.close();
		
		return path;
	}



	/**
	 * 
	 * 
	 * @return
	 */

	public void setOutputPath(String outputPath) {
		try {
			DESTINATION_FILE_PATH = outputPath;
			log.info("output " + DESTINATION_FILE_PATH);
		} catch (Exception e) {
			log.error("output path" + e.getMessage());
		}
	}
}
