package cz.vutbr.ubmi;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;

import cz.vutbr.ubmi.ColonyModel.MaskCircle;



public class CSVMaskReaderWriter {
	
	public static ArrayList<MaskCircle> loadCsv( String filename ) {
		
		String DELIMITER = ",";
		
		ArrayList<MaskCircle> loadedCircles=new ArrayList<MaskCircle>();
		
		try (BufferedReader br = new BufferedReader( new FileReader( filename ) )) {
			String line;
			String headerLine = br.readLine();
			while ( ( line = br.readLine() ) != null ) {
				
				String[] values = line.split( DELIMITER );
				MaskCircle c = new MaskCircle( Double.parseDouble( values[ 0 ] ), Double.parseDouble( values[ 1 ] ), Double.parseDouble( values[ 2 ] ),Double.parseDouble( values[ 3 ] ) );
				loadedCircles.add( c );
			}
				
		} catch ( FileNotFoundException e ) {
			System.out.println( "File not found!" );
			e.printStackTrace();
		} catch ( IOException e ) {
			System.out.println( "File read problem/IO!" );
			e.printStackTrace();
		}
		
		return loadedCircles;
		
	}
	
	public static void writeCsv( String fileName, ArrayList<MaskCircle> circles) {

		String DELIMITER = ",";
		String NEW_LINE_SEPARATOR = "\n";
		String FILE_HEADER = "x,y,r,t";
		FileWriter fileWriter = null;

		try {
			fileWriter = new FileWriter( fileName );

			//Write the CSV file header
			fileWriter.append( FILE_HEADER.toString() );

			//Add a new line separator after the header
			fileWriter.append( NEW_LINE_SEPARATOR );

			//Write a new student object list to the CSV file
			for ( MaskCircle c : circles ) {
				fileWriter.append( String.valueOf( c.x ) );
				fileWriter.append( DELIMITER );
				fileWriter.append( String.valueOf( c.y ) );
				fileWriter.append( DELIMITER );
				fileWriter.append( String.valueOf( c.r ) );
				fileWriter.append( DELIMITER );
				fileWriter.append( String.valueOf( c.t ) );
				fileWriter.append( NEW_LINE_SEPARATOR );
			}
			
		

			System.out.println( "CSV file was created successfully !!!" );

		} catch ( Exception e ) {
			System.out.println( "Error in CsvFileWriter !!!" );
			e.printStackTrace();
		}
		finally {

			try {
				fileWriter.flush();
				fileWriter.close();
			} catch ( IOException e ) {
				System.out.println( "Error while flushing/closing fileWriter !!!" );
				e.printStackTrace();
			}

		}
		
		
	}

}
