package com.ooyala.tools.pdfreport;

import java.util.TreeMap;

import org.eclipse.birt.report.model.api.CellHandle;
import org.eclipse.birt.report.model.api.ColumnHandle;
import org.eclipse.birt.report.model.api.GridHandle;
import org.eclipse.birt.report.model.api.ReportDesignHandle;
import org.eclipse.birt.report.model.api.RowHandle;
import org.eclipse.birt.report.model.api.TextItemHandle;

public class ChartTitleHandle {

	public GridHandle getChartTitle(ReportDesignHandle design,String chartTitle)
	{
		chartTitle = chartTitle.toUpperCase();
		GridHandle header_inner_grid = design.getElementFactory().newGridItem( null, 2 /* cols */, 1 /* row */ );
		
		try
		{
			RowHandle rowHeaderTitle = (RowHandle) header_inner_grid.getRows( ).get( 0 );
			CellHandle cellTitle_header0 = (CellHandle) rowHeaderTitle.getCells( ).get( 0 );
			TextItemHandle textItem3 = design.getElementFactory().newTextItem("test5");
			textItem3.setProperty("contentType", "html");
			textItem3.setContent(
					"<div style=\"background-color:#6e6f71;\">" + 
					"<FONT size=\"\" color=\"#FFFFFF\" face=\"Helvetica\">"+chartTitle+"</FONT>" + 
					"</div>"
					);		
			cellTitle_header0.getContent( ).add( textItem3 );
		   	
		   	ColumnHandle colHeaderTitle = (ColumnHandle)header_inner_grid.getColumns().get(0);
		   	colHeaderTitle.setProperty("width", getLength(chartTitle));
		}
		catch(Exception e)
		{
			
		}
		
		return header_inner_grid;

	}
	
	
	public RowHandle getThinCustomLine(ReportDesignHandle design,RowHandle rowCustomLine,String lineColor,String rowPosition)
	{
		try
		{
			if (rowPosition.equals("image"))
			{
			//	rowCustomLine.setProperty("height", "0.021083333333333337in");
				rowCustomLine.setProperty("height", "0.0266in");
			}
			else
			{
				//rowCustomLine.setProperty("height", "0.022083333333333336in");
				rowCustomLine.setProperty("height", "0.0276in");
			}
			
			CellHandle cellCustomLine = (CellHandle) rowCustomLine.getCells( ).get( 0 );
			TextItemHandle textLine = design.getElementFactory().newTextItem("textLine");
			textLine.setProperty("contentType", "html");
			textLine.setContent(
					"<div style=\"background-color:"+lineColor+"\">" + 
					"<FONT size=\"\" color=\""+lineColor+"\" face=\"\">&nbsp;</FONT>" + 
					"</div>"
					);		
			cellCustomLine.getContent( ).add( textLine );
			
			
		}
		catch(Exception e)
		{
			
		}
	
		
		return rowCustomLine;
	}
	
	public String getLength(String str)
	{
		TreeMap<String,Double> tmap = new TreeMap<String,Double>();

		//below measurements are took by running the rptdesign.
		//given one character(A or B) and found the width for each character in xml layout
		tmap.put("A",new Double("0.150"));
		tmap.put("B",new Double("0.150"));
		tmap.put("C",new Double("0.150"));
		tmap.put("D",new Double("0.160"));
		tmap.put("E",new Double("0.150"));
		tmap.put("F",new Double("0.150"));
		tmap.put("G",new Double("0.155"));
		tmap.put("H",new Double("0.155"));
		tmap.put("I",new Double("0.075"));
		tmap.put("J",new Double("0.110"));
		tmap.put("K",new Double("0.162"));
		tmap.put("L",new Double("0.160"));
		tmap.put("M",new Double("0.180"));
		tmap.put("N",new Double("0.162"));
		tmap.put("O",new Double("0.155"));
		tmap.put("P",new Double("0.145"));
		tmap.put("Q",new Double("0.180"));
		tmap.put("R",new Double("0.162"));
		tmap.put("S",new Double("0.150"));
		tmap.put("T",new Double("0.150"));
		tmap.put("U",new Double("0.160"));
		tmap.put("V",new Double("0.165"));
		tmap.put("W",new Double("0.200"));
		tmap.put("X",new Double("0.165"));
		tmap.put("Y",new Double("0.165"));
		tmap.put("Z",new Double("0.150"));
		tmap.put("-",new Double("0.085"));
		tmap.put("/",new Double("0.085"));
		tmap.put("\\",new Double("0.085"));
		tmap.put("1",new Double("0.150"));
		tmap.put("2",new Double("0.150"));
		tmap.put("3",new Double("0.150"));
		tmap.put("4",new Double("0.150"));
		tmap.put("5",new Double("0.150"));
		tmap.put("6",new Double("0.150"));
		tmap.put("7",new Double("0.150"));
		tmap.put("8",new Double("0.150"));
		tmap.put("9",new Double("0.150"));
		tmap.put(" ",new Double("0.008"));
		
		double result = 0;
		//handle texts contains more spaces
		//handled if more than two space is present word get wrapped
		int spaceCount = 0;
		for (int i=0;i<str.length();i++)
		{
			char c = str.charAt(i);
			Object obj = tmap.get(c+"");
			Double doubleObj = null;
			if (obj == null)
			{
				doubleObj = new Double("0.165");
			}
			else
			{
				doubleObj = (Double)obj;
				if (c != ' ')
				{
					doubleObj = doubleObj - new Double("0.052");
				}
				else
				{
					spaceCount++;
				}
				
				//if more than two space is present word get wrapped
				//increase the width of space so that text will be contained in one line
				if (spaceCount > 2 && c == ' ')
				{
					//from 0.008 to 0.030
					doubleObj = new Double("0.030");
				}
			}
			double d = doubleObj.doubleValue();
			result = result + d;
		}
		
		return result+"in";
	}	
}
