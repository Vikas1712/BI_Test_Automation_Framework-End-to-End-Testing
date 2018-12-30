/**
 * @Author : VIKAS YADAV
 */
package FunctionLibrary;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.IndexedColors;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.xssf.usermodel.XSSFCell;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

/*
 * @Author : VIKAS YADAV	
 */
@SuppressWarnings("rawtypes")
public class ExcelDataPoolManager {

	InputStream minputStream = null ;
	InputStream minputStreamReadRow = null ;
	HSSFWorkbook mhssfwrkbokWorkbook;
	HSSFRow mhssfrowRow = null;
	HSSFCell mhssfcellCell = null;
	HSSFSheet msheetSheet ; 
	
	XSSFWorkbook mxssfwrkbokWorkbook;
	XSSFRow mxssfrowRow = null;
	XSSFCell mxssfcellCell = null;
	XSSFSheet mxsheetSheet ;
	
	Iterator mitrRows,mitrCells;	
	Set set = null ;
	List<Map> mlistData;
	int noOfRows=0;
	
	public ExcelDataPoolManager() {
		super();
	}
	
	public List<Map> readExcel(String XLS_FILE_PATH , String sSheetName) throws Exception 
	{
		try{
			mlistData = new ArrayList<Map>();
			minputStream = new FileInputStream(XLS_FILE_PATH);
			mhssfwrkbokWorkbook = new HSSFWorkbook(minputStream);
			mhssfrowRow = null;
			mhssfcellCell = null;
			msheetSheet = mhssfwrkbokWorkbook.getSheet(sSheetName); 
			mitrRows = msheetSheet.rowIterator();
			noOfRows = msheetSheet.getPhysicalNumberOfRows();
			set = null ;
			Map<String,String> hm = null;
			int intFlag=0;
			int intRunFlagIndex=0;
			for (int i = 1; (i < noOfRows) && (mitrRows.hasNext()); i++)
			{
				hm = new HashMap<String,String>(); 
				mhssfrowRow = (HSSFRow) mitrRows.next();
				mitrCells = mhssfrowRow.cellIterator();
								
				for(int k=0;mitrCells.hasNext();k++){
					if(msheetSheet.getRow(0).getCell(k).toString().equalsIgnoreCase("RUNFLAG")){
						intRunFlagIndex=k;
						break;
					}			
				}
				
				intFlag=0;
				for( int j=0 ;mitrCells.hasNext() ; j++) 
				{
					mitrCells.next();	
					if(msheetSheet.getRow(i).getCell(j)!=null){
						if(msheetSheet.getRow(i).getCell(intRunFlagIndex).toString().equalsIgnoreCase("run")){
							hm.put(getValue(msheetSheet.getRow(0).getCell(j)), getValue(msheetSheet.getRow(i).getCell(j)));
							intFlag=1;
						}
					}

				}
				if(intFlag==1){
					mlistData.add(hm); 
					if (minputStream != null) 
					{
						minputStream.close();
						minputStream = null;
					}
					minputStream = null;
				}
			}
		}catch(Exception e){
			e.printStackTrace();
		}
		return(mlistData);
	}

	//@SuppressWarnings({ "rawtypes", "deprecation" })
	public Map<String,String> readExcelByRow(String XLS_FILE_PATH , String sSheetName , int iRow) throws IOException {

		@SuppressWarnings("unused")
		List<Map> data = new ArrayList<Map>();
		Map<String,String> hm = null;


		//------------Declare Excel Sheet Variables----------------// 
		minputStreamReadRow = new FileInputStream(XLS_FILE_PATH);
		mhssfwrkbokWorkbook = new HSSFWorkbook(minputStreamReadRow);
		mhssfrowRow = null;
		mhssfcellCell = null;
		msheetSheet = mhssfwrkbokWorkbook.getSheet(sSheetName);
		mitrRows = msheetSheet.rowIterator();
		noOfRows = msheetSheet.getPhysicalNumberOfRows();
		set = null ;
		int i = 1;

		//------------Count total no of scripts in a Module----------------// 
		for (; (i < noOfRows) && (mitrRows.hasNext()); i++) {
			if( i == iRow ) {
				break;
			}
		}

		hm = new HashMap<String,String>(); 
		mhssfrowRow = (HSSFRow) mitrRows.next();
		mitrCells = mhssfrowRow.cellIterator();
		int j=0 ;
		for( ;mitrCells.hasNext() ; j++) {
			mitrCells.next();
			hm.put( getValue(msheetSheet.getRow(0).getCell(j)) , getValue(msheetSheet.getRow(iRow).getCell(j , Row.CREATE_NULL_AS_BLANK)));
		}

		if (minputStreamReadRow != null) 
		{
			minputStreamReadRow.close();
			minputStreamReadRow = null;
		}
		minputStreamReadRow = null;

		return(hm);
	}

	private String getValue( Cell cell){
		String value = "";
		switch (cell.getCellType()) {
		case Cell.CELL_TYPE_NUMERIC:
			value = BigDecimal.valueOf(cell.getNumericCellValue()).toPlainString();
			break;

		case Cell.CELL_TYPE_STRING:
			value = cell.getStringCellValue();
			break;

		case Cell.CELL_TYPE_BLANK:
			value = "";
			break;

		case Cell.CELL_TYPE_FORMULA:
			value = cell.getCellFormula();
			break;

		default:
			break;
		}
		return value;
	}

	public int rowCount(String XLS_FILE_PATH , String sSheetName) throws IOException 
	{
		int noOfRows = 0;
		minputStream = new FileInputStream(XLS_FILE_PATH);
		mhssfwrkbokWorkbook = new HSSFWorkbook(minputStream);
		msheetSheet = mhssfwrkbokWorkbook.getSheet(sSheetName); 
		noOfRows = msheetSheet.getPhysicalNumberOfRows();
		//------------Return rowCount from function----------------//
		return noOfRows;
	} 	

	
	public List<Map> readExcel_XLSX(String XLSX_FILE_PATH , String sSheetName) throws Exception 
	{
		try{
			mlistData = new ArrayList<Map>();
			minputStream = new FileInputStream(XLSX_FILE_PATH);
			mxssfwrkbokWorkbook = new XSSFWorkbook(minputStream);
			mxssfrowRow = null;
			mxssfcellCell = null;
			mxsheetSheet = mxssfwrkbokWorkbook.getSheet(sSheetName); 
			mitrRows = mxsheetSheet.rowIterator();
			noOfRows = mxsheetSheet.getPhysicalNumberOfRows();
			set = null ;
			Map<String,String> hm = null;
			int intFlag=0;
			int intRunFlagIndex=0;
			for (int i = 1; (i < noOfRows) && (mitrRows.hasNext()); i++)
			{
				hm = new HashMap<String,String>(); 
				mxssfrowRow = (XSSFRow) mitrRows.next();
				mitrCells = mxssfrowRow.cellIterator();
								
				for(int k=0;mitrCells.hasNext();k++){
					if(mxsheetSheet.getRow(0).getCell(k).toString().equalsIgnoreCase("RUNFLAG")){
						intRunFlagIndex=k;
						break;
					}				
				}
				
				intFlag=0;
				for( int j=0 ;mitrCells.hasNext() ; j++) 
				{
					mitrCells.next();	
					if(mxsheetSheet.getRow(i).getCell(j)!=null){
						if(mxsheetSheet.getRow(i).getCell(intRunFlagIndex).toString().equalsIgnoreCase("run")){
							hm.put(getValue(mxsheetSheet.getRow(0).getCell(j)), getValue(mxsheetSheet.getRow(i).getCell(j)));
							intFlag=1;
						}
					}

				}
				if(intFlag==1){
					mlistData.add(hm); 
					if (minputStream != null) 
					{
						minputStream.close();
						minputStream = null;
					}
					minputStream = null;
				}
			}
		}catch(Exception e){
			e.printStackTrace();
		}
		return(mlistData);
	}
	
	public void writeExcel_XLSX(String XLSX_FILE_PATH , String sSheetName,String Col_Name, String strQry, String newValue) throws Exception 
	{
		try{
			FileInputStream file = new FileInputStream(new File(XLSX_FILE_PATH));
			XSSFWorkbook workbook = new XSSFWorkbook(file);
			mxssfrowRow = null;
			mxssfcellCell = null; 
			XSSFSheet sheet = workbook.getSheet(sSheetName); 
			mitrRows = sheet.rowIterator();
			noOfRows = sheet.getPhysicalNumberOfRows();
			int intRunFlagIndex=0;
			for (int i = 1; (i < noOfRows) && (mitrRows.hasNext()); i++)
			{
				mxssfrowRow = (XSSFRow) mitrRows.next();
				mitrCells = mxssfrowRow.cellIterator();							
				for(int k=0;mitrCells.hasNext();k++){
					if(sheet.getRow(0).getCell(k).toString().equalsIgnoreCase("TC_QUERY")){
						intRunFlagIndex=k;
						break;
					}		
				}
				
				for( int j=0 ;mitrCells.hasNext() ; j++) 
				{
					mitrCells.next();	
					if(sheet.getRow(i).getCell(j)!=null){
						if(sheet.getRow(i).getCell(intRunFlagIndex).toString().equalsIgnoreCase(strQry)){	
							int cellIndex = getColumnIndex(sheet, Col_Name);
							 Cell Cell=sheet.getRow(i).getCell(cellIndex);

							 CellStyle style = workbook.createCellStyle();
							 if(newValue.equalsIgnoreCase("PASS")) {
								 style.setFillForegroundColor(IndexedColors.GREEN.getIndex());
								 style.setFillPattern(CellStyle.SOLID_FOREGROUND); 
								 Cell.setCellStyle(style);
							 }
							if(newValue.equalsIgnoreCase("FAIL")) {
								 style.setFillForegroundColor(IndexedColors.RED.getIndex());
								 style.setFillPattern(CellStyle.SOLID_FOREGROUND); 
								 Cell.setCellStyle(style);
							 }
							 
							if (Cell == null)
								Cell = sheet.getRow(i).createCell(cellIndex);
							try {
								Cell.setCellValue(newValue);
								
							} catch (Exception e) {
								System.out.println(e);
							}
						}
					}
				}
			}
			file.close();
			FileOutputStream out = new FileOutputStream(new File(XLSX_FILE_PATH));
			workbook.write(out);
			workbook.close();
			out.flush();
			out.close();
		}catch(Exception e){
			e.printStackTrace();
		}
	}
	
	public String getValueByKey(Set TempSet,String KeyName) throws Exception 
	{	
		Iterator CountRow = TempSet.iterator();
		while(CountRow.hasNext()) {
			Map.Entry me = (Map.Entry)CountRow.next();	
			if(me.getKey().toString().equalsIgnoreCase(KeyName)){	
				return me.getValue().toString();
			}
		}
		return null; 
	} 
	
	public static int getColumnIndex(Sheet sheet, String columnName) {
	    int columnIndex = -1;
	    try {
	        int colNum = sheet.getRow(0).getLastCellNum();
	        Row firstRow = sheet.getRow(0);
	        for (int colIndex = 0; colIndex < colNum; colIndex++) {
	            Cell cell = firstRow.getCell(colIndex);
	            if (cell.toString().equals(columnName)) {
	                columnIndex = colIndex;
	            }
	        }
	    } catch (Exception e) {
	        throw e;
	    }
	    return columnIndex;
	}
	
}
