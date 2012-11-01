/**
 * Copyright (c) Ooyala Inc, 2012
 * Author     : Ramesh.Ramamoorthy
 * Version    : $Revision: 1.12 $
 * Updated-On : $Date: 2012/09/25 08:10:05 $
 *
 * Purpose:
 * A program to hold various constants required for the tool. 
 *
 * Change History:
 * 2012-08-07, Ramesh.R: Initial program.
 * 2012-08-07, Ramesh.R: Included a Application Logger Constant for logging the error/warning messages.
 * 2012-08-31, Madheswaran.S: Constant added to hold line image filename.
 * 2012-08-31, Anand.S: Contants added for TOC and Array graph report.
 * 2012-09-03, Ramesh.R: Added useful tool wide constants and util methods.
 */
package com.ooyala.tools.pdfreport;

import java.io.File;

import org.apache.log4j.Logger;

/**
 * Holds various constants used throughout the tool.
 */
public class ApplicationConstants
{
	/**
	 * Application Version
	 */
	public static final String OOYALA_PDFREPORT_TOOL_VERSION = "1.0";
	
	/**
	 * Application Logger
	 */
	public static final Logger APP_LOGGER = Logger.getLogger("com.ooyala.tools.pdfreport");
	
	/**
	 * Current working directory when this tool is executed 
	 */
	public static final String BASE_LOCATION = System.getProperty("user.dir");
	
	/**
	 * OS specific file path seperator
	 */
	public static final String OS_FILE_SEPERATOR = System.getProperty("file.separator");
	
	/**
	 * config folder
	 */
	public static final String CONFIG_LOCATION = BASE_LOCATION + OS_FILE_SEPERATOR + "config";
	
	/**
	 * report folder
	 */
	public static final String REPORT_LOCATION = BASE_LOCATION + OS_FILE_SEPERATOR + "report";
	
	/**
	 * Report Name
	 */
	public static final String REPORT_NAME = "INSIGHT REPORT:";	
	public static final String FIRST_PAGE_REPORT_NAME = "INSIGHT REPORT";
	
	/**
	 * Table of Contents Page Number
	 */
	public static final String TBL_CONTS_PG_NUMBER = "2";	
	
	/**
	 * Image ooyalalogo
	 */
	public static String IMAGE_OOYALALOGO = "ooyalalogo.png";		
	
	/**
	 * Image one
	 */
	public static final String OOYALA_IMAGE_LINE = "line.png";	
	
	/**
	 * Image arrow
	 */
	public static final String IMAGE_ARROW = "arrow.png";
	
	/**
	 * modified arrow image one
	 */
	public static final String IMAGE_ARROW_MODIFIED_ONE = "arrow1.png";	
	
	/**
	 * modified arrow image two
	 */
	public static final String IMAGE_ARROW_MODIFIED_TWO = "arrow2.png";		
	
	/**
	 * config folder
	 */
	public static final String CONFIG_FOLDER = "config";
	
	/**
	 * report folder
	 */
	public static final String REPORT_FOLDER = "report";
	
	/**
	 * HEADING LABEL IMAGE
	 */
	public static final String HEADING_LABEL_IMG = "headingLabelImage.png";
	
	/**
	 * Log the given message to Apache Log4j Logger 
	 */
	public static void log(String message, String type)
	{
		System.out.println(message);
		if (type.equalsIgnoreCase("debug"))
		{
			APP_LOGGER.debug(message);
		}
		else if (type.equalsIgnoreCase("fatal"))
		{
			APP_LOGGER.fatal(message);
		}
		else if (type.equalsIgnoreCase("error"))
		{
			APP_LOGGER.error(message);
		}
		else if (type.equalsIgnoreCase("warn"))
		{
			APP_LOGGER.warn(message);
		}
		else if (type.equalsIgnoreCase("info"))
		{
			APP_LOGGER.info(message);
		}
	}
	
	/**
	 * Log the given message to STDOUT 
	 */
	public static void log(String message)
	{
		System.out.println(message);
	}
	
	/**
	 * Get the FILE object for the given filename and return the same
	 * 
	 * @param filename
	 * @return FILE object
	 */
	public static File getFile(String filename)
	{
		String fileURI = BASE_LOCATION + OS_FILE_SEPERATOR + filename;
		return (new File(fileURI));
	}
}
