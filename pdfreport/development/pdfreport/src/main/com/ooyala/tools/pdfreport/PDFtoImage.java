package com.ooyala.tools.pdfreport;

import java.awt.HeadlessException;
import java.awt.Toolkit;
import java.awt.image.BufferedImage;
import java.io.File;

import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.util.PDFImageWriter;

/**
 * Convert a PDF document to an image.
 * 
 * @author <a href="ben@benlitchfield.com">Ben Litchfield</a>
 * @version $Revision: 1.5 $
 */
public class PDFtoImage {

	/**
	 * Infamous main method.
	 * 
	 * @param args
	 *            Command line arguments, should be one and a reference to a
	 *            file.
	 * 
	 * @throws Exception
	 *             If there is an error parsing the document.
	 */
	public static boolean generateImage(String pdfFile) throws Exception {
		String password = "";
		String outputPrefix = "";
		String imageFormat = "png";
		int startPage = 1;
		int endPage = Integer.MAX_VALUE;
		String color = "rgb";
		boolean success = false;
		int resolution;
		try {
			resolution = Toolkit.getDefaultToolkit().getScreenResolution();
		} catch (HeadlessException e) {
			resolution = 96;
		}
		
		File file = new File(pdfFile);
		if(!file.exists())
		{
			return false;
		}
		outputPrefix = pdfFile.substring(0, pdfFile.lastIndexOf("."));
		
		PDDocument document = null;
		try {
			document = PDDocument.load(pdfFile);
			int imageType = 24;
			if ("bilevel".equalsIgnoreCase(color)) {
				imageType = BufferedImage.TYPE_BYTE_BINARY;
			} else if ("indexed".equalsIgnoreCase(color)) {
				imageType = BufferedImage.TYPE_BYTE_INDEXED;
			} else if ("gray".equalsIgnoreCase(color)) {
				imageType = BufferedImage.TYPE_BYTE_GRAY;
			} else if ("rgb".equalsIgnoreCase(color)) {
				imageType = BufferedImage.TYPE_INT_RGB;
			} else if ("rgba".equalsIgnoreCase(color)) {
				imageType = BufferedImage.TYPE_INT_ARGB;
			} else {
				ApplicationConstants.APP_LOGGER.error("Error: the number of bits per pixel must be 1, 8 or 24.");
			}

			// Make the call
			PDFImageWriter imageWriter = new PDFImageWriter();
			success = imageWriter.writeImage(document, imageFormat,
					password, startPage, endPage, outputPrefix, imageType,
					resolution);
			if (!success) {
				ApplicationConstants.APP_LOGGER.error("Error: no writer found for image format '"
						+ imageFormat + "'");
			}
			
		boolean isDeleted = file.delete();
			if(isDeleted)
			{
				ApplicationConstants.APP_LOGGER.info("Source PDF for image generation has been deleted successfully");
			}
			else
			{
				ApplicationConstants.APP_LOGGER.info("Source PDF for image generation is not deleted");
			}
			
		} catch (Exception e) {
			ApplicationConstants.APP_LOGGER.error(e.getMessage());
		} finally {
			if (document != null) {
				document.close();
			}
		}
		return success;
	}
}