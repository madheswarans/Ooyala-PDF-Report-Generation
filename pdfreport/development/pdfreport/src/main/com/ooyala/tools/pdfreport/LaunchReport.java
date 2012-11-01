/**
 * Copyright (c) Ooyala Inc, 2012
 * Author     : Ramesh.Ramamoorthy
 * Version    : $Revision: 1.75 $
 * Updated-On : $Date: 2012/10/31 08:36:50 $
 *
 * Purpose:
 * A program which will launch the report generation tool. 
 *
 * Change History:
 * 2012-08-07, Ramesh.R: Initial program.
 */
package com.ooyala.tools.pdfreport;

import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.DocumentBuilder;
import org.w3c.dom.Document;
import org.w3c.dom.NodeList;
import org.w3c.dom.Node;
import org.w3c.dom.Element;
import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.TreeMap;

/**
 * Launch the report generation tool.
 */
public class LaunchReport
{
	/**
	 * Method for testing, will be removed once the launcher started working.
	 * 
	 * @param args
	 */
	
	public static void main(String[] args)
	{
		
		String headerTitle = null;
		String chartTitle = null;
		String chartXaxis = null;
		String chartYaxis = null;
		String chartYaxis2 = null;
		
		String graphType = null;
		//String leftHeader = null;
		String leftBlock = null;
		//String rightHeader = null;
		String rightItem = null;
		String chartSrc = null;
		BarChart barChart = null;
		LineChart lineChart = null;
		StackedBarChart stackedBarChart = null;
		MergeReports mergeReports = null;
		String leftTilteHeader = null;
		String rightTilteHeader = null;
		String inputPath = null;
		String outputPath = null;
		String thickness=null;
		// File fXmlFile = null;
		String inputFileName = null;
		String title = null;
		String publisher = null;
		String color = null;
		TreeMap<String, String> tmapPagesAndNumber = null;
		
		OoyalaPage2 ooyalaPage2 = null;
		TableOfContents tableOfContents = null;
		OoyalaArrowPage ooyalaArrowPage = null;
		String dashBoardTitle = null;
		int lineThickness=3;
		//text change
		
		
		
		
		try
		{
			
			inputFileName = System.getProperty("pdf.report.config.file");
			
			File fXmlFile = new File(inputFileName);
			DocumentBuilderFactory dbFactory = DocumentBuilderFactory
					.newInstance();
			DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
			
			// parse xml file
			Document doc = dBuilder.parse(fXmlFile);
			doc.getDocumentElement().normalize();
			ApplicationConstants.APP_LOGGER.info("Root element :"
					+ doc.getDocumentElement().getNodeName());
			// Get publisher
			NodeList nPublisherList = doc.getElementsByTagName("head");
			for (int temp = 0; temp < nPublisherList.getLength(); temp++)
			{
				Node nNode = nPublisherList.item(temp);
				Element eElement = (Element)nNode;
				
				title = getTagValue("title", eElement);
				publisher = getTagValue("publisher", eElement);
				color = getTagValue("color", eElement);
				ApplicationConstants.IMAGE_OOYALALOGO=getTagValue("ooyalalogo", eElement);
				
			}
			
			//
			
			NodeList nodeConList = doc.getElementsByTagName("configuration");
			for (int i = 0; i < nodeConList.getLength(); i++)
			{
				
				Node nConTextNode = nodeConList.item(i);
				Element eConElement = (Element)nConTextNode;
		    		  if(getTagValue("input-location", eConElement)!=null){
		    			  	inputPath=getTagValue("input-location", eConElement);
							ApplicationConstants.APP_LOGGER.info("inputPath=="+ inputPath);
						}
		    		  if(getTagValue("output-location", eConElement)!=null){
		    			  	outputPath=getTagValue("output-location", eConElement);
							ApplicationConstants.APP_LOGGER.info("output-location=="+ outputPath);
						}
				
			}
			mergeReports = new MergeReports();
			mergeReports.setOutputPath(outputPath);
			ApplicationConstants.APP_LOGGER.info("Root element :"
					+ doc.getDocumentElement().getNodeName());
			
			NodeList nodeList = doc.getElementsByTagName("summary");
			
			for (int n = 0; n < nodeList.getLength(); n++)
			{
				Node nNode0 = nodeList.item(n);
				Element eElement0 = (Element)nNode0;
				
				NodeList summaryList = eElement0.getElementsByTagName("graphs");
				
				for (int temp1 = 0; temp1 < summaryList.getLength(); temp1++)
				{
					Node nNode1 = summaryList.item(temp1);
					Element eElement1 = (Element)nNode1;
					NodeList nList2 = eElement1.getElementsByTagName("graph");
					
					for (int i = 0; i < nList2.getLength(); i++)
					{
						Node nNode2 = nList2.item(i);
						Element summaryGraphsElement = (Element)nNode2;
						if (summaryGraphsElement.getAttribute("type") != null)
						{
							graphType = summaryGraphsElement
									.getAttribute("type");
							ApplicationConstants.APP_LOGGER
									.info("graph type====>>>" + graphType);
							
						}
						if (summaryGraphsElement.getAttribute("src") != null)
						{
							chartSrc = summaryGraphsElement.getAttribute("src");
							ApplicationConstants.APP_LOGGER.info("chartSrc=="
									+ chartSrc);
							
						}
						
						if (getTagValue("title", summaryGraphsElement) != null)
						{
							dashBoardTitle = getTagValue("title",
									summaryGraphsElement);
							// System.out.println("dashBoardTitle---------> :" +
							// dashBoardTitle);
						}
						
						if (graphType != null
								&& graphType.equalsIgnoreCase("dashboard"))
						{
							DashBoard board = new DashBoard();
							board.createReport(graphType, chartSrc,
									dashBoardTitle, inputPath, false, color);
							board.createReport(graphType, chartSrc,
									dashBoardTitle, inputPath, true, color);
							
						}
						
						//
						
					}
					
				}
				
				NodeList nListText = eElement0.getElementsByTagName("text");
				ApplicationConstants.APP_LOGGER.info("== ");
				//
				for (int t = 0; t < nListText.getLength(); t++)
				{
					Node nTextNode3 = nListText.item(t);
					Element etitleElement3 = (Element)nTextNode3;
					
					if (getTagValue("left", etitleElement3) != null)
					{
						leftTilteHeader = getTagValue("left", etitleElement3);
						ApplicationConstants.APP_LOGGER.info("text header == "
								+ leftTilteHeader);
					}
					
					if (getTagValue("right", etitleElement3) != null)
					{
						rightTilteHeader = getTagValue("right", etitleElement3);
						
					}
					
					//
					
					//starts
					NodeList nList_left = etitleElement3
							.getElementsByTagName("left");
					//System.out.println("nList4.getLength()==>"+nList_left.getLength());
					ArrayList aListTempLHS = new ArrayList();
					String strLHS = "";
					for (int j = 0; j < nList_left.getLength(); j++)
					{
						String leftHeader = "";
						ArrayList aListBlocks = new ArrayList();
						Node nNode4 = nList_left.item(j);
						Element eElement4 = (Element)nNode4;
						if (getTagValue("header", eElement4) != null)
						{
							leftHeader = getTagValue("header",
									eElement4);
						}
						aListBlocks  = getTagValues("block",eElement4,eElement4.getElementsByTagName("block").getLength());
						if (aListBlocks == null || aListBlocks.isEmpty())
						{
						  aListBlocks = getTagValues("item",eElement4,eElement4.getElementsByTagName("item").getLength());
							  if (aListBlocks != null && !aListBlocks.isEmpty())
							  {
							   	  strLHS = "item";
								  }
						}
						else
						{
						  strLHS = "block";
						}
						aListTempLHS.add(leftHeader);
						aListTempLHS.add(strLHS);
						aListTempLHS.add(aListBlocks);
					}
					if (nList_left.getLength() == 0)
					{
						aListTempLHS.add("");
						aListTempLHS.add("");
						aListTempLHS.add(new ArrayList());
					}
					
					
					NodeList nList5 = etitleElement3
							.getElementsByTagName("right");
					ArrayList aListTempRHS = new ArrayList();
					String strRHS = "";						
					for (int k = 0; k < nList5.getLength(); k++)
					{
						String rightHeader = "";
						ArrayList aListItems = new ArrayList();								
						Node nNode5 = nList5.item(k);
						Element eElement5 = (Element)nNode5;
						if (getTagValue("header", eElement5) != null)
						{
							rightHeader = getTagValue("header",
									eElement5);
						}
						
	    			   aListItems = getTagValues("item",eElement5,eElement5.getElementsByTagName("item").getLength());
	    			   if (aListItems == null || aListItems.isEmpty())
	    			   {
	    				   aListItems  = getTagValues("block",eElement5,eElement5.getElementsByTagName("block").getLength());
	    				   if (aListItems != null && !aListItems.isEmpty())
	    				   {
	    					   strRHS = "block";
	    				   }
	    			   }
	    			   else
	    			   {
	    				   strRHS = "item";
	    			   }
	    			   
	    			   aListTempRHS.add(rightHeader);
	    			   aListTempRHS.add(strRHS);
	    			   aListTempRHS.add(aListItems);			    			   
						
					}
					
					if (nList5.getLength() == 0)
					{
						aListTempRHS.add("");
						aListTempRHS.add("");
						aListTempRHS.add(new ArrayList());
					}
					//ends
					ooyalaPage2 = new OoyalaPage2();
					ooyalaPage2.buildReport(aListTempLHS, aListTempRHS,
							inputPath);
					
				}
				
			}
			
			tableOfContents = new TableOfContents();
			tableOfContents.buildReport(inputPath);
			tmapPagesAndNumber = tableOfContents.getPagesAndNumber();
			
			ArrayList<Object> aListResultant = new ArrayList<Object>();
			//ArrayList<String> aListItems = null;
			//ArrayList<String> aListBlocks = null;
			Node rootElement = doc.getDocumentElement();
			
			NodeList nList = doc.getElementsByTagName("drilldown");
			TreeMap tmapTemp = new TreeMap();
			for (int temp = 0; temp < nList.getLength(); temp++)
			{
				
				Node nNode = nList.item(temp);
				Element eElement = (Element)nNode;
				// chart headerTitle
				if (getTagValue("title", eElement) != null)
				{
					headerTitle = getTagValue("title", eElement).trim();
					ApplicationConstants.APP_LOGGER.info("headetTitle == "
							+ headerTitle);
				}
				
				NodeList nList1 = eElement.getElementsByTagName("graphs");
				for (int temp1 = 0; temp1 < nList1.getLength(); temp1++)
				{
					Node nNode1 = nList1.item(temp1);
					Element eElement1 = (Element)nNode1;
					NodeList nList2 = eElement1.getElementsByTagName("graph");
					
					for (int graphIndex = 0; graphIndex < nList2.getLength(); graphIndex++)
					{
						Node nNode2 = nList2.item(graphIndex);
						Element graphsElement = (Element)nNode2;
						
						
						// Chart Title
						if (getTagValue("title", graphsElement) != null)
						{
							chartTitle = getTagValue("title", graphsElement);
							ApplicationConstants.APP_LOGGER
									.info("chartTitle ==>>" + chartTitle);
						}
						
						// xaxis
						if (getTagValue("xaxis", graphsElement) != null)
						{
							chartXaxis = getTagValue("xaxis", graphsElement);
							ApplicationConstants.APP_LOGGER.info("chartXaxis=="
									+ chartXaxis);
						}
						// yaxis
						if (getTagValue("yaxis", graphsElement) != null)
						{
							chartYaxis = getTagValue("yaxis", graphsElement);
							ApplicationConstants.APP_LOGGER.info("chartYaxis=="
									+ chartYaxis);
						}
						
						// yaxis
						if (getTagValue("yaxis2", graphsElement) != null)
						{
							chartYaxis2 = getTagValue("yaxis2", graphsElement);
							ApplicationConstants.APP_LOGGER.info("chartYaxis2=="
									+ chartYaxis2);
						}
						// yaxis
					
						
						// graph type
						if (graphsElement.getAttribute("type") != null)
						{
							graphType = graphsElement.getAttribute("type");
							ApplicationConstants.APP_LOGGER.info("graphType=="
									+ graphType);
							
						}
						// src
						if (graphsElement.getAttribute("src") != null)
						{
							chartSrc = graphsElement.getAttribute("src");
							ApplicationConstants.APP_LOGGER.info("chartSrc=="
									+ chartSrc);
							
						}
						NodeList nList3 = graphsElement
								.getElementsByTagName("text");
						
						String strLHS = "";
						String strRHS = "";
						if(nList3!=null){
						for (int l = 0; l < nList3.getLength(); l++)
						{
							Node nNode3 = nList3.item(l);
							Element eElement3 = (Element)nNode3;
							
							ArrayList aListTemp = new ArrayList();
							NodeList nList4 = eElement3
									.getElementsByTagName("left");
							for (int j = 0; j < nList4.getLength(); j++)
							{
								String leftHeader = "";
								ArrayList aListBlocks = new ArrayList();
								Node nNode4 = nList4.item(j);
								Element eElement4 = (Element)nNode4;
								
								if (getTagValue("header", eElement4) != null)
								{
									leftHeader = getTagValue("header",
											eElement4);
								}
								if (getTagValue("header", eElement4) != null)
								{
									leftBlock = getTagValue("block", eElement4);
								}
								
								aListBlocks  = getTagValues("block",eElement4,eElement4.getElementsByTagName("block").getLength());
								if (aListBlocks == null || aListBlocks.isEmpty())
								{
								  aListBlocks = getTagValues("item",eElement4,eElement4.getElementsByTagName("item").getLength());
									  if (aListBlocks != null && !aListBlocks.isEmpty())
									  {
									   	  strLHS = "item";
  									  }
								}
								else
								{
								  strLHS = "block";
								}
								
								aListTemp.add(leftHeader);
								aListTemp.add(strLHS);
								aListTemp.add(aListBlocks);
							}
							
							if (nList4.getLength() == 0)
							{
								aListTemp.add("");
								aListTemp.add("");
								aListTemp.add(new ArrayList());
							}
							NodeList nList5 = eElement3
									.getElementsByTagName("right");
							for (int k = 0; k < nList5.getLength(); k++)
							{
								String rightHeader = "";
								ArrayList aListItems = new ArrayList();								
								Node nNode5 = nList5.item(k);
								Element eElement5 = (Element)nNode5;
								if (getTagValue("header", eElement5) != null)
								{
									rightHeader = getTagValue("header",
											eElement5);
								}
								if (getTagValue("item", eElement5) != null)
								{
									rightItem = getTagValue("item", eElement5);
								}
								
			    			   aListItems = getTagValues("item",eElement5,eElement5.getElementsByTagName("item").getLength());
			    			   if (aListItems == null || aListItems.isEmpty())
			    			   {
			    				   aListItems  = getTagValues("block",eElement5,eElement5.getElementsByTagName("block").getLength());
			    				   if (aListItems != null && !aListItems.isEmpty())
			    				   {
			    					   strRHS = "block";
			    				   }
			    			   }
			    			   else
			    			   {
			    				   strRHS = "item";
			    			   }
								aListTemp.add(rightHeader);
								aListTemp.add(strRHS);
								aListTemp.add(aListItems);			    			   
								
							}
							if (nList5.getLength() == 0)
							{
								aListTemp.add("");
								aListTemp.add("");
								aListTemp.add(new ArrayList());
							}
							tmapTemp.put(headerTitle + "#"
									+ chartTitle + "#" + graphType + "#"
									+ chartSrc,aListTemp);
							
						}
						if (nList3.getLength() == 0)
						{
							ArrayList aListTemp = new ArrayList();
							
							//LHS
							aListTemp.add("");
							aListTemp.add("");
							aListTemp.add(new ArrayList());
							
							//RHS
							aListTemp.add("");
							aListTemp.add("");
							aListTemp.add(new ArrayList());							
							
							tmapTemp.put(headerTitle + "#"
									+ chartTitle + "#" + graphType + "#"
									+ chartSrc,aListTemp);							
						}
						}	
						try
						{
							if (graphType != null
									&& graphType.equalsIgnoreCase("line") && graphsElement.getAttribute("multiple").equalsIgnoreCase("yes"))
							{
								String xaxis=null;
								String yaxis=null;
								String xaxisTitle=null;
								String yaxisTitle=null;
								String yaxis2=null;
								String chartYaxis3 = null;
								String chartYaxis4 = null;
								String chartYaxis5 = null;
								String chartYaxis6 = null;
								String chartYaxis7 = null;
								String chartYaxis8 = null;
								String chartYaxis9 = null;
								String chartYaxis10 = null;
								HashMap<String, String> hashMap=new HashMap<String, String>();
								MultipleLineChart multipleLineChart=null;
								multipleLineChart=new MultipleLineChart();
								try
								{
									String tempLeftHeader = null;
									String tempStrLHS = null;
									ArrayList tempaListBlocks = null;									
									String tempRightHeader = null;
									String tempStrRHS = null;
									ArrayList tempaListItems =null;
									ArrayList aList = (ArrayList) tmapTemp.get(headerTitle + "#"
											+ chartTitle + "#" + graphType + "#"
											+ chartSrc);
									
									if(aList!=null){
										 tempLeftHeader = (String)aList.get(0);
										 tempStrLHS = (String)aList.get(1);
										 tempaListBlocks = (ArrayList)aList.get(2);
										
										 tempRightHeader = (String)aList.get(3);
										 tempStrRHS = (String)aList.get(4);
										 tempaListItems = (ArrayList)aList.get(5);	
										}							
									
									
									
									String strPageNumber = tmapPagesAndNumber
											.get(headerTitle + "#" + chartTitle
													+ "#" + graphType + "#"
													+ chartSrc);
									if (strPageNumber == null)
									{
										strPageNumber = "0";
									}
									
									if (getTagValue("xaxis", graphsElement) != null)
									{
										
										xaxis = getTagValue("xaxis", graphsElement);
										ApplicationConstants.APP_LOGGER.info("chartXaxis=="
												+ xaxis);
										
										NodeList nTitleList = graphsElement
												.getElementsByTagName("xaxis");
										
										for (int x = 0; x < nTitleList.getLength(); x++)
										{
											Node nNode6 = nTitleList.item(x);
											Element graphsXaxisElement = (Element)nNode6;
											
											
										if(graphsXaxisElement.getAttribute("title")!=null)
										{
											xaxisTitle=graphsXaxisElement.getAttribute("title");
											
										}
										}
									}
									// yaxis
									if (getTagValue("yaxis", graphsElement) != null)
									{
										
										yaxis = getTagValue("yaxis", graphsElement);
										ApplicationConstants.APP_LOGGER.info("chartYaxis=="
												+ yaxis);
										NodeList nYTitleList = graphsElement
												.getElementsByTagName("yaxis");
										
										for (int y = 0; y < nYTitleList.getLength(); y++)
										{
											Node nNode7 = nYTitleList.item(y);
											Element graphsYaxisElement = (Element)nNode7;
										if(graphsYaxisElement.getAttribute("title")!=null)
										{
											yaxisTitle=graphsYaxisElement.getAttribute("title");
											
										}
										}
										
									}
									
									
									if (getTagValue("yaxis2", graphsElement) != null)
									{
										
										yaxis2 = getTagValue("yaxis2", graphsElement);
										hashMap.put("yaxis2", yaxis2);
										ApplicationConstants.APP_LOGGER.info("yaxis2=="
												+ yaxis2);
										
									}
									if (getTagValue("yaxis3", graphsElement) != null)
									{
										chartYaxis3 = getTagValue("yaxis3", graphsElement);
										hashMap.put("yaxis3", chartYaxis3);
										ApplicationConstants.APP_LOGGER.info("chartYaxis3=="
												+ chartYaxis3);
										
									}
									
									if (getTagValue("yaxis4", graphsElement) != null)
									{
										chartYaxis4 = getTagValue("yaxis4", graphsElement);
										hashMap.put("yaxis4", chartYaxis4);
										ApplicationConstants.APP_LOGGER.info("chartYaxis4=="
												+ chartYaxis4);
										
									}
									if (getTagValue("yaxis5", graphsElement) != null)
									{
										chartYaxis5 = getTagValue("yaxis5", graphsElement);
										hashMap.put("yaxis5", chartYaxis5);
										ApplicationConstants.APP_LOGGER.info("chartYaxis5=="
												+ chartYaxis5);
										
									}
									
									if (getTagValue("yaxis6", graphsElement) != null)
									{
										chartYaxis6 = getTagValue("yaxis6", graphsElement);
										hashMap.put("yaxis6", chartYaxis6);
										ApplicationConstants.APP_LOGGER.info("chartYaxis6=="
												+ chartYaxis6);
										
									}
									if (getTagValue("yaxis7", graphsElement) != null)
									{
										chartYaxis7 = getTagValue("yaxis7", graphsElement);
										hashMap.put("yaxis7", chartYaxis7);
										ApplicationConstants.APP_LOGGER.info("chartYaxis7=="
												+ chartYaxis7);
									}
									
									if (getTagValue("yaxis8", graphsElement) != null)
									{
										chartYaxis8 = getTagValue("yaxis8", graphsElement);
										hashMap.put("yaxis8", chartYaxis8);
										ApplicationConstants.APP_LOGGER.info("chartYaxis8=="
												+ chartYaxis8);
									}
									if (getTagValue("yaxis9", graphsElement) != null)
									{
										chartYaxis9 = getTagValue("yaxis9", graphsElement);
										hashMap.put("yaxis9", chartYaxis9);
										ApplicationConstants.APP_LOGGER.info("chartYaxis9=="
												+ chartYaxis9);
									}
									if (getTagValue("yaxis10", graphsElement) != null)
									{
										chartYaxis10 = getTagValue("yaxis10", graphsElement);
										hashMap.put("yaxis10", chartYaxis10);
										ApplicationConstants.APP_LOGGER.info("chartYaxis10=="
												+ chartYaxis10);
									}
								
									if(hashMap.size()<=8){
									multipleLineChart.createReport(graphType,
											chartSrc, headerTitle, chartTitle,
											xaxis, yaxis,hashMap,											
											tempLeftHeader, leftBlock,
											tempRightHeader, rightItem, inputPath,
											title, publisher, strPageNumber,
											tempaListBlocks, tempaListItems, false,graphIndex, tempStrLHS, tempStrRHS, color,xaxisTitle,yaxisTitle);
									multipleLineChart.createReport(graphType,
											chartSrc, headerTitle, chartTitle,
											xaxis, yaxis,
											hashMap,
											tempLeftHeader, leftBlock,
											tempRightHeader, rightItem, inputPath,
											title, publisher, strPageNumber,
											tempaListBlocks, tempaListItems, true,graphIndex, tempStrLHS, tempStrRHS, color,xaxisTitle,yaxisTitle);
									}else{
										
										throw new Exception(" Error Occured in multiline chart") ;
									}
									
								}
								catch (Exception e)
								{
									ApplicationConstants.APP_LOGGER
											.info(" error occured in multiline  chart"
													+ e.getMessage());
									
								}
							}
							else if (graphType != null
									&& graphType.equalsIgnoreCase("line"))
							{
								
								try
								{
									String tempLeftHeader = null;
									String tempStrLHS = null;
									ArrayList tempaListBlocks = null;
									
									String tempRightHeader = null;
									String tempStrRHS = null;
									ArrayList tempaListItems =null;
									if (getTagValue("thickness", graphsElement) != null)
									{
										thickness = getTagValue("thickness", graphsElement);
										ApplicationConstants.APP_LOGGER.info("thickness=="
												+ thickness);
										
										 lineThickness=Integer.parseInt(thickness);
									}
									
									lineChart = new LineChart();
									
									String strPageNumber = tmapPagesAndNumber
											.get(headerTitle + "#" + chartTitle
													+ "#" + graphType + "#"
													+ chartSrc);
									if (strPageNumber == null)
									{
										strPageNumber = "0";
									}
									ApplicationConstants.APP_LOGGER
											.info(graphType + "==>" + leftBlock);
									
									ArrayList aList = (ArrayList) tmapTemp.get(headerTitle + "#"
											+ chartTitle + "#" + graphType + "#"
											+ chartSrc);
									
									if(aList!=null){
										 tempLeftHeader = (String)aList.get(0);
										 tempStrLHS = (String)aList.get(1);
										 tempaListBlocks = (ArrayList)aList.get(2);
										
										 tempRightHeader = (String)aList.get(3);
										 tempStrRHS = (String)aList.get(4);
										 tempaListItems = (ArrayList)aList.get(5);	
										}
									lineChart.createReport(graphType, chartSrc,
											headerTitle, chartTitle,
											chartXaxis, chartYaxis, tempLeftHeader,
											leftBlock, tempRightHeader, rightItem,
											inputPath, title, publisher,
											graphIndex, strPageNumber,
											tempaListBlocks, tempaListItems, false, tempStrLHS, tempStrRHS, color,lineThickness);
									lineChart.createReport(graphType, chartSrc,
											headerTitle, chartTitle,
											chartXaxis, chartYaxis, tempLeftHeader,
											leftBlock, tempRightHeader, rightItem,
											inputPath, title, publisher,
											graphIndex, strPageNumber,
											tempaListBlocks, tempaListItems, true, tempStrLHS, tempStrRHS, color,lineThickness);
								}
								catch (Exception e)
								{
									
									ApplicationConstants.APP_LOGGER
											.info(" error occured in line chart"
													+ e.getMessage());
									
								}
								
							}
							if (graphType != null
									&& graphType.equalsIgnoreCase("bar") && graphsElement.getAttribute("multiple").equalsIgnoreCase("yes"))
							{
								String xaxis=null;
								String yaxis=null;
								String xaxisTitle=null;
								String yaxisTitle=null;
								String yaxis2=null;
								String chartYaxis3 = null;
								String chartYaxis4 = null;
								String chartYaxis5 = null;
								String chartYaxis6 = null;
								String chartYaxis7 = null;
								String chartYaxis8 = null;
								String chartYaxis9 = null;
								String chartYaxis10=null;
								
								HashMap<String, String> hashMap=new HashMap<String, String>();
								MultipleBarChart multipleBarChart=null;
								try
								{
									String tempLeftHeader = null;
									String tempStrLHS = null;
									ArrayList tempaListBlocks = null;									
									String tempRightHeader = null;
									String tempStrRHS = null;
									ArrayList tempaListItems =null;
									ArrayList aList = (ArrayList) tmapTemp.get(headerTitle + "#"
											+ chartTitle + "#" + graphType + "#"
											+ chartSrc);
									
									if(aList!=null){
										 tempLeftHeader = (String)aList.get(0);
										 tempStrLHS = (String)aList.get(1);
										 tempaListBlocks = (ArrayList)aList.get(2);
										
										 tempRightHeader = (String)aList.get(3);
										 tempStrRHS = (String)aList.get(4);
										 tempaListItems = (ArrayList)aList.get(5);	
										}							
									
									
									
									String strPageNumber = tmapPagesAndNumber
											.get(headerTitle + "#" + chartTitle
													+ "#" + graphType + "#"
													+ chartSrc);
									
									if (strPageNumber == null)
									{
										strPageNumber = "0";
									}
									
									if (getTagValue("xaxis", graphsElement) != null)
									{
										
										xaxis = getTagValue("xaxis", graphsElement);
										ApplicationConstants.APP_LOGGER.info("chartXaxis=="
												+ xaxis);
										
										NodeList nodeBarList = graphsElement
												.getElementsByTagName("xaxis");
										
										for (int x = 0; x < nodeBarList.getLength(); x++)
										{
											Node nNode8 = nodeBarList.item(x);
											Element element = (Element)nNode8;
											
										if(element.getAttribute("title")!=null)
										{
											
											xaxisTitle=element.getAttribute("title");
											
										}
										}
									}
									// yaxis
									if (getTagValue("yaxis", graphsElement) != null)
									{
										
										yaxis = getTagValue("yaxis", graphsElement);
										ApplicationConstants.APP_LOGGER.info("chartYaxis=="
												+ yaxis);
										NodeList barYTitleList = graphsElement
												.getElementsByTagName("yaxis");
										
										for (int y = 0; y < barYTitleList.getLength(); y++)
										{
											Node nNode9 = barYTitleList.item(y);
											Element yElement = (Element)nNode9;
										if(yElement.getAttribute("title")!=null)
										{
											yaxisTitle=yElement.getAttribute("title");
											
										}
										}
										
									}
									if (getTagValue("yaxis2", graphsElement) != null)
									{
										
										yaxis2 = getTagValue("yaxis2", graphsElement);
										hashMap.put("yaxis2", yaxis2);
										ApplicationConstants.APP_LOGGER.info("yaxis2=="
												+ yaxis2);
										
									}
									if (getTagValue("yaxis3", graphsElement) != null)
									{
										chartYaxis3 = getTagValue("yaxis3", graphsElement);
										hashMap.put("yaxis3", chartYaxis3);
										ApplicationConstants.APP_LOGGER.info("chartYaxis3=="
												+ chartYaxis3);
										
									}
									
									if (getTagValue("yaxis4", graphsElement) != null)
									{
										chartYaxis4 = getTagValue("yaxis4", graphsElement);
										hashMap.put("yaxis4", chartYaxis4);
										ApplicationConstants.APP_LOGGER.info("chartYaxis4=="
												+ chartYaxis4);
										
									}
									if (getTagValue("yaxis5", graphsElement) != null)
									{
										chartYaxis5 = getTagValue("yaxis5", graphsElement);
										hashMap.put("yaxis5", chartYaxis5);
										ApplicationConstants.APP_LOGGER.info("chartYaxis5=="
												+ chartYaxis5);
										
									}
									
									if (getTagValue("yaxis6", graphsElement) != null)
									{
										chartYaxis6 = getTagValue("yaxis6", graphsElement);
										hashMap.put("yaxis6", chartYaxis6);
										ApplicationConstants.APP_LOGGER.info("chartYaxis6=="
												+ chartYaxis6);
										
									}
									if (getTagValue("yaxis7", graphsElement) != null)
									{
										chartYaxis7 = getTagValue("yaxis7", graphsElement);
										hashMap.put("yaxis7", chartYaxis7);
										ApplicationConstants.APP_LOGGER.info("chartYaxis7=="
												+ chartYaxis7);
									}
									
									if (getTagValue("yaxis8", graphsElement) != null)
									{
										chartYaxis8 = getTagValue("yaxis8", graphsElement);
										hashMap.put("yaxis8", chartYaxis8);
										ApplicationConstants.APP_LOGGER.info("chartYaxis8=="
												+ chartYaxis8);
									}
									if (getTagValue("yaxis9", graphsElement) != null)
									{
										chartYaxis9 = getTagValue("yaxis9", graphsElement);
										hashMap.put("yaxis9", chartYaxis9);
										ApplicationConstants.APP_LOGGER.info("chartYaxis9=="
												+ chartYaxis9);
									}
									if (getTagValue("yaxis10", graphsElement) != null)
									{
										chartYaxis10 = getTagValue("yaxis10", graphsElement);
										hashMap.put("yaxis10", chartYaxis10);
										ApplicationConstants.APP_LOGGER.info("chartYaxis10=="
												+ chartYaxis10);
									}
									multipleBarChart=new MultipleBarChart();
									if(hashMap.size()<=8){
										
									
									multipleBarChart.createReport(graphType,
											chartSrc, headerTitle, chartTitle,
											chartXaxis, chartYaxis,hashMap,											
											tempLeftHeader, leftBlock,
											tempRightHeader, rightItem, inputPath,
											title, publisher, strPageNumber,
											tempaListBlocks, tempaListItems, false,graphIndex, tempStrLHS, tempStrRHS, color,xaxisTitle,yaxisTitle);
									multipleBarChart.createReport(graphType,
											chartSrc, headerTitle, chartTitle,
											chartXaxis, chartYaxis,
											hashMap,
											tempLeftHeader, leftBlock,
											tempRightHeader, rightItem, inputPath,
											title, publisher, strPageNumber,
											tempaListBlocks, tempaListItems, true,graphIndex, tempStrLHS, tempStrRHS, color,xaxisTitle,yaxisTitle);
									}else{
										
										throw new Exception(" Error Occured in multibar  chart") ;
									}
									
									
								}
								catch (Exception e)
								{
									ApplicationConstants.APP_LOGGER
											.info(" error occured in arrow  chart"
													+ e.getMessage());
									
								}
							}
							else if (graphType != null
									&& graphType.equalsIgnoreCase("bar"))
							{
								
								try
								{
									String tempLeftHeader = null;
									String tempStrLHS = null;
									ArrayList tempaListBlocks = null;
									
									String tempRightHeader = null;
									String tempStrRHS = null;
									ArrayList tempaListItems =null;
									barChart = new BarChart();
									String strPageNumber = tmapPagesAndNumber
											.get(headerTitle + "#" + chartTitle
													+ "#" + graphType + "#"
													+ chartSrc);
									if (strPageNumber == null)
									{
										strPageNumber = "0";
									}
									// System.out
									// .println(chartXaxis+""+chartYaxis);
									ArrayList aList = (ArrayList) tmapTemp.get(headerTitle + "#"
											+ chartTitle + "#" + graphType + "#"
											+ chartSrc);
									
									if(aList!=null){
										 tempLeftHeader = (String)aList.get(0);
										 tempStrLHS = (String)aList.get(1);
										 tempaListBlocks = (ArrayList)aList.get(2);
										
										 tempRightHeader = (String)aList.get(3);
										 tempStrRHS = (String)aList.get(4);
										 tempaListItems = (ArrayList)aList.get(5);	
										}
									barChart.createReport(graphType, chartSrc,
											headerTitle, chartTitle,
											chartXaxis, chartYaxis, tempLeftHeader,
											leftBlock, tempRightHeader, rightItem,
											inputPath, title, publisher,
											graphIndex, strPageNumber,
											tempaListBlocks, tempaListItems, false, tempStrLHS, tempStrRHS, color);
									barChart.createReport(graphType, chartSrc,
											headerTitle, chartTitle,
											chartXaxis, chartYaxis, tempLeftHeader,
											leftBlock, tempRightHeader, rightItem,
											inputPath, title, publisher,
											graphIndex, strPageNumber,
											tempaListBlocks, tempaListItems, true, tempStrLHS, tempStrRHS, color);
									
								}
								catch (Exception e)
								{
									
									ApplicationConstants.APP_LOGGER
											.error("Exception Occured in bar chart "
													+ e.getMessage());
								}
								
							}
							
							if (graphType != null
									&& graphType.equalsIgnoreCase("bar-line"))
							{
								try
								{
									String xaxis=null;
									String yaxis=null;
									String xaxisTitle=null;
									String yaxisTitle=null;
									String yaxis2=null;
									String chartYaxis3 = null;
									String chartYaxis4 = null;
									String chartYaxis5 = null;
									String chartYaxis6 = null;
									String chartYaxis7 = null;
									String chartYaxis8 = null;
									String chartYaxis9 = null;
									String chartYaxis10 = null;
									HashMap<String, String> hashMap=new HashMap<String, String>();
									BarLineChart barLineChart=null;
									
									String tempLeftHeader = null;
									String tempStrLHS = null;
									ArrayList tempaListBlocks = null;
									String tempRightHeader = null;
									String tempStrRHS = null;
									ArrayList tempaListItems =null;
									
									String strPageNumber = tmapPagesAndNumber
											.get(headerTitle + "#" + chartTitle
													+ "#" + graphType + "#"
													+ chartSrc);
									if (strPageNumber == null)
									{
										strPageNumber = "0";
									}
									
									ArrayList aList = (ArrayList) tmapTemp.get(headerTitle + "#"
											+ chartTitle + "#" + graphType + "#"
											+ chartSrc);
									if(aList!=null){
									 tempLeftHeader = (String)aList.get(0);
									 tempStrLHS = (String)aList.get(1);
									 tempaListBlocks = (ArrayList)aList.get(2);
									
									 tempRightHeader = (String)aList.get(3);
									 tempStrRHS = (String)aList.get(4);
									 tempaListItems = (ArrayList)aList.get(5);	
									}
									if (getTagValue("xaxis", graphsElement) != null)
									{
										
										xaxis = getTagValue("xaxis", graphsElement);
										ApplicationConstants.APP_LOGGER.info("chartXaxis=="
												+ xaxis);
										
										NodeList nodeBarLineList = graphsElement
												.getElementsByTagName("xaxis");
										
										for (int x = 0; x < nodeBarLineList.getLength(); x++)
										{
											Node nNode10 = nodeBarLineList.item(x);
											Element element = (Element)nNode10;
											
										if(element.getAttribute("title")!=null)
										{
											
											xaxisTitle=element.getAttribute("title");
											
										}
										}
									}
									// yaxis
									if (getTagValue("yaxis", graphsElement) != null)
									{
										
										yaxis = getTagValue("yaxis", graphsElement);
										ApplicationConstants.APP_LOGGER.info("chartYaxis=="
												+ yaxis);
										NodeList barLineYTitleList = graphsElement
												.getElementsByTagName("yaxis");
										
										for (int y = 0; y < barLineYTitleList.getLength(); y++)
										{
											Node nNode11 = barLineYTitleList.item(y);
											Element yElement = (Element)nNode11;
										if(yElement.getAttribute("title")!=null)
										{
											yaxisTitle=yElement.getAttribute("title");
											
										}
										}
										
									}
								
									if (getTagValue("yaxis2", graphsElement) != null)
									{
										
										yaxis2 = getTagValue("yaxis2", graphsElement);
										hashMap.put("yaxis2", yaxis2);
										ApplicationConstants.APP_LOGGER.info("yaxis2=="
												+ yaxis2);
										
									}
									if (getTagValue("yaxis3", graphsElement) != null)
									{
										chartYaxis3 = getTagValue("yaxis3", graphsElement);
										hashMap.put("yaxis3", chartYaxis3);
										ApplicationConstants.APP_LOGGER.info("chartYaxis3=="
												+ chartYaxis3);
										
									}
									
									if (getTagValue("yaxis4", graphsElement) != null)
									{
										chartYaxis4 = getTagValue("yaxis4", graphsElement);
										hashMap.put("yaxis4", chartYaxis4);
										ApplicationConstants.APP_LOGGER.info("chartYaxis4=="
												+ chartYaxis4);
										
									}
									if (getTagValue("yaxis5", graphsElement) != null)
									{
										chartYaxis5 = getTagValue("yaxis5", graphsElement);
										hashMap.put("yaxis5", chartYaxis5);
										ApplicationConstants.APP_LOGGER.info("chartYaxis5=="
												+ chartYaxis5);
										
									}
									
									if (getTagValue("yaxis6", graphsElement) != null)
									{
										chartYaxis6 = getTagValue("yaxis6", graphsElement);
										hashMap.put("yaxis6", chartYaxis6);
										ApplicationConstants.APP_LOGGER.info("chartYaxis6=="
												+ chartYaxis6);
										
									}
									if (getTagValue("yaxis7", graphsElement) != null)
									{
										chartYaxis7 = getTagValue("yaxis7", graphsElement);
										hashMap.put("yaxis7", chartYaxis7);
										ApplicationConstants.APP_LOGGER.info("chartYaxis7=="
												+ chartYaxis7);
									}
									
									if (getTagValue("yaxis8", graphsElement) != null)
									{
										chartYaxis8 = getTagValue("yaxis8", graphsElement);
										hashMap.put("yaxis8", chartYaxis8);
										ApplicationConstants.APP_LOGGER.info("chartYaxis8=="
												+ chartYaxis8);
									}
									if (getTagValue("yaxis9", graphsElement) != null)
									{
										chartYaxis9 = getTagValue("yaxis9", graphsElement);
										hashMap.put("yaxis9", chartYaxis9);
										ApplicationConstants.APP_LOGGER.info("chartYaxis9=="
												+ chartYaxis9);
									}if (getTagValue("yaxis10", graphsElement) != null)
									{
										chartYaxis10 = getTagValue("yaxis10", graphsElement);
										hashMap.put("yaxis10", chartYaxis10);
										ApplicationConstants.APP_LOGGER.info("chartYaxis9=="
												+ chartYaxis10);
									}
									
									barLineChart=new BarLineChart();
									if(hashMap.size()<=8){
									barLineChart.createReport(graphType,
											chartSrc, headerTitle, chartTitle,
											chartXaxis, chartYaxis,
											hashMap, tempLeftHeader, leftBlock,
											tempRightHeader, rightItem, inputPath,
											title, publisher, strPageNumber,
											tempaListBlocks, tempaListItems, false,graphIndex, tempStrLHS, tempStrRHS, color,xaxisTitle,yaxisTitle);
									barLineChart.createReport(graphType,
											chartSrc, headerTitle, chartTitle,
											chartXaxis, chartYaxis,
											hashMap, tempLeftHeader, leftBlock,
											tempRightHeader, rightItem, inputPath,
											title, publisher, strPageNumber,
											tempaListBlocks, tempaListItems, true,graphIndex, tempStrLHS, tempStrRHS, color,xaxisTitle,yaxisTitle);
									}else{
										throw new Exception(" More than 9 line in bar - line chart");
									}
								}
								catch (Exception e)
								{
									
									ApplicationConstants.APP_LOGGER
											.info(" error occured in Bar line chart"
													+ e.getMessage());
									
								}
							}
							
							if (graphType != null
									&& graphType.equalsIgnoreCase("stacked"))
							{
								
								try
								{
									String tempLeftHeader = null;
									String tempStrLHS = null;
									ArrayList tempaListBlocks = null;
									
									String tempRightHeader = null;
									String tempStrRHS = null;
									ArrayList tempaListItems =null;
									stackedBarChart = new StackedBarChart();
									String strPageNumber = tmapPagesAndNumber
											.get(headerTitle + "#" + chartTitle
													+ "#" + graphType + "#"
													+ chartSrc);
									if (strPageNumber == null)
									{
										strPageNumber = "0";
									}
									ArrayList aList = (ArrayList) tmapTemp.get(headerTitle + "#"
											+ chartTitle + "#" + graphType + "#"
											+ chartSrc);
									
									if(aList!=null){
										 tempLeftHeader = (String)aList.get(0);
										 tempStrLHS = (String)aList.get(1);
										 tempaListBlocks = (ArrayList)aList.get(2);
										
										 tempRightHeader = (String)aList.get(3);
										 tempStrRHS = (String)aList.get(4);
										 tempaListItems = (ArrayList)aList.get(5);	
										}
									String chartYaxis3 = null;
									String chartYaxis4 = null;
									if (getTagValue("yaxis3", graphsElement) != null)
									{
										chartYaxis3 = getTagValue("yaxis3", graphsElement);
										ApplicationConstants.APP_LOGGER.info("chartYaxis3=="
												+ chartYaxis3);
									}
									
									if (getTagValue("yaxis4", graphsElement) != null)
									{
										chartYaxis4 = getTagValue("yaxis4", graphsElement);
										ApplicationConstants.APP_LOGGER.info("chartYaxis4=="
												+ chartYaxis4);
									}
									

									stackedBarChart.createReport(graphType,
											chartSrc, headerTitle, chartTitle,
											chartXaxis, chartYaxis,
											chartYaxis2, chartYaxis3,chartYaxis4,
											tempLeftHeader, leftBlock, tempRightHeader,
											rightItem, inputPath, title,
											publisher, graphIndex,
											strPageNumber, tempaListBlocks,
											tempaListItems,false, tempStrLHS, tempStrRHS, color);
									stackedBarChart.createReport(graphType,
											chartSrc, headerTitle, chartTitle,
											chartXaxis, chartYaxis,
											chartYaxis2, chartYaxis3,chartYaxis4,
											tempLeftHeader, leftBlock, tempRightHeader,
											rightItem, inputPath, title,
											publisher, graphIndex,
											strPageNumber, tempaListBlocks,
											tempaListItems,true, tempStrLHS, tempStrRHS, color);
									
									
								}
								catch (Exception e)
								{
									ApplicationConstants.APP_LOGGER
											.info(" error occured in Stacked  chart"
													+ e.getMessage());
									
								}
							}
							
						
							
							if (graphType != null
									&& graphType.equalsIgnoreCase("arrows"))
							{
								
								try
								{
									String tempLeftHeader = null;
									String tempStrLHS = null;
									ArrayList tempaListBlocks = null;
									
									String tempRightHeader = null;
									String tempStrRHS = null;
									ArrayList tempaListItems =null;
									ArrayList aList = (ArrayList) tmapTemp.get(headerTitle + "#"
											+ chartTitle + "#" + graphType + "#"
											+ chartSrc);
									
									if(aList!=null){
										 tempLeftHeader = (String)aList.get(0);
										 tempStrLHS = (String)aList.get(1);
										 tempaListBlocks = (ArrayList)aList.get(2);
										
										 tempRightHeader = (String)aList.get(3);
										 tempStrRHS = (String)aList.get(4);
										 tempaListItems = (ArrayList)aList.get(5);	
										}							
									
									
									ooyalaArrowPage = new OoyalaArrowPage();
									String strPageNumber = tmapPagesAndNumber
											.get(headerTitle + "#" + chartTitle
													+ "#" + graphType + "#"
													+ chartSrc);
									if (strPageNumber == null)
									{
										strPageNumber = "0";
									}
									
									ooyalaArrowPage.createReport(graphType,
											chartSrc, headerTitle, chartTitle,
											tempLeftHeader, tempaListBlocks,
											tempRightHeader, tempaListItems, inputPath,
											title, publisher, strPageNumber, false,
											tempStrLHS, tempStrRHS,graphIndex,color);
									
									ooyalaArrowPage.createReport(graphType,
											chartSrc, headerTitle, chartTitle,
											tempLeftHeader, tempaListBlocks,
											tempRightHeader, tempaListItems, inputPath,
											title, publisher, strPageNumber, true,
											tempStrLHS, tempStrRHS,graphIndex,color);
									
								}
								catch (Exception e)
								{
									ApplicationConstants.APP_LOGGER
											.info(" error occured in arrow  chart"
													+ e.getMessage());
									
								}
							}
							
						}
						catch (Exception exe)
						{
							ApplicationConstants.APP_LOGGER
									.info(" Error occured read xm file" + exe);
						}
						
					}// graph for loop end
					
				}// graphs for loop end
				
			}// drilldown for loop end
			
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}
		// report generate
		
		try
		{
			
			mergeReports.getReportDesign();
			mergeReports.generateImageReport();
		}
		catch (Exception e)
		{
			
			ApplicationConstants.APP_LOGGER.info(" error in lauch report page "
					+ e.getMessage());
			
		}
		
	}
	
	// get Tag value
	private static String getTagValue(String sTag, Element eElement)
	{
		Node nValue = null;
		if (eElement.getElementsByTagName(sTag).item(0) != null)
		{
			NodeList nlList = eElement.getElementsByTagName(sTag).item(0)
					.getChildNodes();
			
			nValue = (Node)nlList.item(0);
			return nValue.getNodeValue();
		}
		
		return null;
	}
	
	/**
	 * @param sTag
	 * @param eElement
	 * @param count
	 * @return
	 */
	public static ArrayList<String> getTagValues(String sTag, Element eElement,
			int count)
	{
		ArrayList<String> aListValues = new ArrayList<String>();
		// ApplicationConstants.APP_LOGGER.info("count==>"+count);
		
		for (int i = 0; i < count; i++)
		{
			NodeList nlList = eElement.getElementsByTagName(sTag).item(i)
					.getChildNodes();
			Node nValue = (Node)nlList.item(0);
			String str = nValue.getNodeValue();
			aListValues.add(str);
		}
		
		return aListValues;
	}
	
}
