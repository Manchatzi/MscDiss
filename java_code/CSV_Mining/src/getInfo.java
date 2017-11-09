import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.Writer;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

import org.w3c.dom.Document;
import org.w3c.dom.Element;

import com.opencsv.CSVReader;

public class getInfo {

	public static void main(String[] args) throws Exception {
		// TODO Auto-generated method stub
		
		List<geo> finalData = new ArrayList<geo>();
		List<List<String>> finalInfo = new ArrayList<List<String>>();
//--------------------------------------		
		CSVReader reader = new CSVReader(new FileReader("collection.csv"));
		List my = reader.readAll();
		
		String[] titles = (String[]) my.get(0);
		List<Integer> indexes = new ArrayList<Integer>();
		List<Integer> info = new ArrayList<Integer>();
		int i = 0;
		for (String s:titles){
			if ((s.equals("Title#1"))||(s.equals("Alternate Title#1"))||(s.equals("Description#1"))||(s.equals("Source#1"))||(s.equals("Subject Place#1"))||(s.equals("Subject Place#1$1"))||(s.equals("Subject Place#1$2"))||(s.equals("Subject Event#1"))||(s.equals("Subject Event#1$1"))||(s.equals("Subject Event#1$2"))){
				indexes.add(i);
			}
			if ((s.equals("Title#1"))){
				info.add(i);
			}
			if ((s.equals("Alternate Title#1"))){
				info.add(i);
			}
			if ((s.equals("Creator#1"))){
				info.add(i);
			}
			if ((s.equals("Description#1"))){
				info.add(i);
			}
			if ((s.equals("URL TO JPG"))){
				info.add(i);
			}
			i++;
		}
		my.remove(0); //remove first line because it is the headers

//----------------------------------------	
//Create the files to be inputted in the geoparser
//----------
		int cn = 0;
		for (Object entry : my) {
		    try {
		    String filename = "outs/output" + cn + ".txt";
		    FileWriter f = new FileWriter(filename);
		    for (int j : indexes){
				f.write(((String[])entry)[j] + " ");
			}
		    f.write(System.getProperty("line.separator"));
		    f.close();
		    cn++;
		} catch (IOException e) {
		    e.printStackTrace();
		}}

		
//----------------------------------------	
//BREAK IN THE CODE. THE OUTPUT FILES FROM ABOVE NEED TO BE PARSED WITH THE GEOPARSER.
//THE TSV OUTPUT OF THE GEOPRASER IS THEN PROCESSED BY THE FOLLOWING CODE
//----------
		
		for (int k=0;k<1071;k++){
			String t1 = ((String[])my.get(k))[info.get(1)];
			String t2 = ((String[])my.get(k))[info.get(2)];
			String cr = ((String[])my.get(k))[info.get(3)];
			String ds = ((String[])my.get(k))[info.get(4)];
			String url = ((String[])my.get(k))[info.get(0)];
			
			List<String> l = new ArrayList<String>();
			l.add(0,t1); l.add(1,t2); l.add(2,cr); l.add(3,ds); l.add(4,url);
			finalInfo.add(k,l);

			String tsv = "tsvs/output" + k + ".out.tsv";

			File file = new File(tsv);
			if (file.length() == 0) {
			    geo g = new geo(k,"Empty",10.0f,10.0f);
			    finalData.add(g);
			}
			else{
				String[][] resultArray;
				List<String> lines = Files.readAllLines(Paths.get(tsv), StandardCharsets.UTF_8);
				resultArray = new String[lines.size()][]; 

				for(int ii =0; ii<lines.size(); ii++){
				  resultArray[ii] = lines.get(ii).split("\t"); //tab-separated
				}
				for (String[] s : resultArray){
					geo g = new geo(k);
					if (s.length == 1){
						g.location = s[0];
						g.lat = 0f;
						g.lon = 0f;
					}
					else{
						g.location = s[0];
						g.lat = Float.parseFloat(s[3]);
						g.lon = Float.parseFloat(s[4]);
					}
					if (!finalData.contains(g))
						finalData.add(g);
				}
			}
		}
		System.out.println(finalData.size());

//----------------------------------------	
//Create the XML files for the web interface
//----------
// INFO XML
		
		String output = "info.xml";
		Writer writer = null;
		try{
			writer = new FileWriter(output);
			writer.write("<?xml version="+"\"1.0\""+" encoding="+"\"UTF-8\""+" standalone="+"\"no"+"\"?>");writer.write(System.getProperty("line.separator"));
			writer.write("<info>");writer.write(System.getProperty("line.separator"));
			int counter = 0;
			for(List<String> l : finalInfo){
				writer.write("\t"+"<image>");writer.write(System.getProperty("line.separator"));
				writer.write("\t\t"+"<id>");writer.write(Integer.toString(counter));writer.write("</id>");writer.write(System.getProperty("line.separator"));
				writer.write("\t\t"+"<title1>");writer.write(l.get(0));writer.write("</title1>");writer.write(System.getProperty("line.separator"));
				writer.write("\t\t"+"<title2>");writer.write(l.get(1));writer.write("</title2>");writer.write(System.getProperty("line.separator"));
				writer.write("\t\t"+"<creator>");writer.write(l.get(2));writer.write("</creator>");writer.write(System.getProperty("line.separator"));
				writer.write("\t\t"+"<description>");writer.write(l.get(3));writer.write("</description>");writer.write(System.getProperty("line.separator"));
				writer.write("\t\t"+"<url>");writer.write(l.get(4));writer.write("</url>");writer.write(System.getProperty("line.separator"));
				writer.write("\t"+"</image>");writer.write(System.getProperty("line.separator"));
				counter++;
			}
			writer.write("</info>");writer.write(System.getProperty("line.separator"));
		}catch (IOException e){
			e.getMessage();
		}finally{
			try {writer.close();} catch (Exception ex) {}
		}
//--------------------------------------------
//LOCATION XML
		
		String output1 = "fileman.xml";
		try{
			writer = new FileWriter(output1);
			writer.write("<?xml version="+"\"1.0\""+" encoding="+"\"UTF-8\""+" standalone="+"\"no"+"\"?>");writer.write(System.getProperty("line.separator"));
			writer.write("<collection>");writer.write(System.getProperty("line.separator"));		
			for(geo g : finalData){
				writer.write("\t"+"<entry>");writer.write(System.getProperty("line.separator"));
				writer.write("\t\t"+"<id>");writer.write(Integer.toString(g.getID()));writer.write("</id>");writer.write(System.getProperty("line.separator"));
				writer.write("\t\t"+"<location>");writer.write(g.getLocation());writer.write("</location>");writer.write(System.getProperty("line.separator"));
				writer.write("\t\t"+"<latitude>");writer.write(Float.toString(g.getLat()));writer.write("</latitude>");writer.write(System.getProperty("line.separator"));
				writer.write("\t\t"+"<longitude>");writer.write(Float.toString(g.getLon()));writer.write("</longitude>");writer.write(System.getProperty("line.separator"));
				writer.write("\t"+"</entry>");writer.write(System.getProperty("line.separator"));
			}
			writer.write("</collection>");writer.write(System.getProperty("line.separator"));
		}catch (IOException e){
			e.getMessage();
		}finally{
			try {writer.close();} catch (Exception ex) {}
		}

	}

}
