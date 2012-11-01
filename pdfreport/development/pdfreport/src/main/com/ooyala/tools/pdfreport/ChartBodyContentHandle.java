package com.ooyala.tools.pdfreport;

import java.util.ArrayList;
import java.util.TreeMap;

import org.eclipse.birt.report.model.api.CellHandle;
import org.eclipse.birt.report.model.api.ElementFactory;
import org.eclipse.birt.report.model.api.GridHandle;
import org.eclipse.birt.report.model.api.ReportDesignHandle;
import org.eclipse.birt.report.model.api.RowHandle;
import org.eclipse.birt.report.model.api.TextItemHandle;

public class ChartBodyContentHandle 
{
	TreeMap<String,Double> tmapUpperCase = new TreeMap<String,Double>();
	TreeMap<String,Double> tmapLowerCase = new TreeMap<String,Double>();
	TreeMap<String,Double> tmapNumbers = new TreeMap<String,Double>();
	TreeMap<String,Double> tmapSplChars = new TreeMap<String,Double>();
	
	public void initTmapUpperCase()
	{
		//below measurements are took by running the rptdesign.
		//given one character(A or B) and found the width for each character in xml layout
		tmapUpperCase.put("A",new Double("0.150"));
		tmapUpperCase.put("B",new Double("0.150"));
		tmapUpperCase.put("C",new Double("0.150"));
		tmapUpperCase.put("D",new Double("0.160"));
		tmapUpperCase.put("E",new Double("0.150"));
		tmapUpperCase.put("F",new Double("0.150"));
		tmapUpperCase.put("G",new Double("0.155"));
		tmapUpperCase.put("H",new Double("0.155"));
		tmapUpperCase.put("I",new Double("0.075"));
		tmapUpperCase.put("J",new Double("0.110"));
		tmapUpperCase.put("K",new Double("0.162"));
		tmapUpperCase.put("L",new Double("0.160"));
		tmapUpperCase.put("M",new Double("0.180"));
		tmapUpperCase.put("N",new Double("0.162"));
		tmapUpperCase.put("O",new Double("0.155"));
		tmapUpperCase.put("P",new Double("0.145"));
		tmapUpperCase.put("Q",new Double("0.170"));
		tmapUpperCase.put("R",new Double("0.162"));
		tmapUpperCase.put("S",new Double("0.150"));
		tmapUpperCase.put("T",new Double("0.150"));
		tmapUpperCase.put("U",new Double("0.160"));
		tmapUpperCase.put("V",new Double("0.165"));
		tmapUpperCase.put("W",new Double("0.200"));
		tmapUpperCase.put("X",new Double("0.165"));
		tmapUpperCase.put("Y",new Double("0.165"));
		tmapUpperCase.put("Z",new Double("0.150"));		
	}
	
	public void initTmapLowerCase()
	{
		//below measurements are took by running the rptdesign.
		//given one character(A or B) and found the width for each character in xml layout		
		tmapLowerCase.put("a",new Double("0.110"));
		tmapLowerCase.put("b",new Double("0.111"));
		tmapLowerCase.put("c",new Double("0.100"));
		tmapLowerCase.put("d",new Double("0.111"));
		tmapLowerCase.put("e",new Double("0.100"));
		tmapLowerCase.put("f",new Double("0.110"));
		tmapLowerCase.put("g",new Double("0.111"));
		tmapLowerCase.put("h",new Double("0.118"));
		tmapLowerCase.put("i",new Double("0.070"));
		tmapLowerCase.put("j",new Double("0.070"));
		tmapLowerCase.put("k",new Double("0.118"));
		tmapLowerCase.put("l",new Double("0.070"));
		tmapLowerCase.put("m",new Double("0.145"));
		tmapLowerCase.put("n",new Double("0.118"));
		tmapLowerCase.put("o",new Double("0.111"));
		tmapLowerCase.put("p",new Double("0.111"));
		tmapLowerCase.put("q",new Double("0.111"));
		tmapLowerCase.put("r",new Double("0.081"));
		tmapLowerCase.put("s",new Double("0.081"));
		tmapLowerCase.put("t",new Double("0.069"));
		tmapLowerCase.put("u",new Double("0.117"));
		tmapLowerCase.put("v",new Double("0.117"));
		tmapLowerCase.put("w",new Double("0.147"));
		tmapLowerCase.put("x",new Double("0.117"));
		tmapLowerCase.put("y",new Double("0.117"));
		tmapLowerCase.put("z",new Double("0.111"));			
	}
	
	public void initTmapNumbers()
	{
		//below measurements are took by running the rptdesign.
		//given one character(A or B) and found the width for each character in xml layout			
		tmapNumbers.put("1",new Double("0.165"));
	}
	
	public void initTmapSplChars()
	{
		//below measurements are took by running the rptdesign.
		//given one character(A or B) and found the width for each character in xml layout			
		tmapSplChars.put("-",new Double("0.085"));
		tmapSplChars.put(" ",new Double("0.008"));	
	}
	
	public ChartBodyContentHandle()
	{
		initTmapUpperCase();
		initTmapLowerCase();
		initTmapNumbers();
		initTmapSplChars();
	}
	
	public CellHandle getBodyHeading(CellHandle cell,ElementFactory efactory,String header,String style)
	{
		TextItemHandle paraHeading = efactory.newTextItem(null);
		if (header == null) 
		{
			header = "";
		}
		
		try
		{
			paraHeading.setContent(header);
			paraHeading.setProperty("style", style);
			paraHeading.setProperty("contentType", "html");
			cell.getContent().add(paraHeading);
		}
		catch(Exception e)
		{
			
		}		
		
		return cell;
			
	}

	public GridHandle getBodyContent(ReportDesignHandle design,ElementFactory efactory,String contentType,String style,ArrayList<String> aListContent)
	{
		GridHandle grid = null;
		
		ArrayList<String> aListContents = new ArrayList<String>();
		for (int j=0;j<aListContent.size();j++)
		{
			String tempDesc = (String)aListContent.get(j);
//			moving back to old code - starts - sep 26 2012
//			put bulk data into text item handle ignoring the length of texts
//			will not handle large set of datas that exceeds more than one page
//			it was assumed that comment will not exceed more than one page
			if (contentType.equalsIgnoreCase("block"))
			{
				tempDesc = tempDesc + "</BR>";
			}
			else if (contentType.equalsIgnoreCase("item"))
			{
				tempDesc = "<LI>" + tempDesc + "</LI>";
			}			
			aListContents.add(tempDesc);
//			put bulk data into text item handle ignoring the length of texts
//			moving back to old code - ends - sep 26 2012

			
//			please do not remove the commented code - will wrap content to specified length - starts - sep 25 2012
//			will handle large set of datas that exceeds more than one page
//			if (contentType.equalsIgnoreCase("item"))
//			{
//				tempDesc = "<LI>" + tempDesc;
//			}			
//			aListContents.addAll(getAListContents(tempDesc));
//			String strLast = (String)aListContents.get(aListContents.size()-1);
//			if (contentType.equalsIgnoreCase("block"))
//			{
//				strLast = strLast + "<BR><BR>";
//			}
//			else if(contentType.equalsIgnoreCase("item"))
//			{
//				strLast = strLast + "</LI><BR>";
//			}
//			
//			int contSize = aListContents.size();
//			aListContents.remove(contSize-1);
//			aListContents.add(contSize-1, strLast);		
//			please do not remove the commented code - will wrap content to specified length - ends - sep 25 2012
		}

		grid = design.getElementFactory().newGridItem(null/* name */, 1 /* cols */, aListContents.size() /* row */);
		for (int i=0;i<aListContents.size();i++)
		{
			String str2 = (String)aListContents.get(i);
			RowHandle rowCont = (RowHandle) grid.getRows().get(i);
			CellHandle cellCont = (CellHandle) rowCont.getCells().get(0);
			try
			{
				TextItemHandle cont = efactory.newTextItem(null);
				cont.setContent(str2);
				cont.setProperty("style", style);
				cont.setProperty("contentType", "html");
				cont.setProperty("paddingRight","15pt");		
				
				cellCont.getContent().add(cont);
			}
			catch(Exception e)
			{
				
			}
		}		
		
		return grid;
	}
	
	public ArrayList<String> getAListContents(String str)
	{
		char[] charResultant = new char[str.length()];
		double dCount = 0.0;
		String strTemp = "";
		ArrayList<String> aListContents = new ArrayList<String>();
		for (int i=0;i<str.length();i++)
		{
			charResultant[i] = str.charAt(i);
			double charWidth = 0.0;
			String strSearch = str.charAt(i)+"";
			if (tmapUpperCase.containsKey(strSearch))
			{
				charWidth = tmapUpperCase.get(strSearch);
			}
			else if (tmapLowerCase.containsKey(strSearch))
			{
				charWidth = tmapLowerCase.get(strSearch);
			}
			else if (tmapNumbers.containsKey(strSearch))
			{
				charWidth = tmapNumbers.get(strSearch);
			}
			else if (tmapSplChars.containsKey(strSearch))
			{
				charWidth = tmapSplChars.get(strSearch);
			}			
			else
			{
				charWidth = 0.165;
			}
			
			dCount = dCount+charWidth;
			
			//a row in block or item can take string of width "3.2 in" max
			if (dCount <= 3.2)
			{
				strTemp = strTemp + strSearch;
			}
			else
			{
				aListContents.add(strTemp);
				
				dCount = charWidth;
				strTemp = strSearch;
			}
			
		}
		
		aListContents.add(strTemp);
		
		return aListContents;
	}		
	
}
