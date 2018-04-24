package lib;

import java.io.File;
import java.io.FileInputStream;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
/**
 * @author Dev Shah
 */
public class ExcelConfig {
	XSSFWorkbook wb;
	XSSFSheet sh;
	
	public ExcelConfig(String path) {
		
		try{
			File src=new File(path);
			FileInputStream fis=new FileInputStream(src);
			wb=new XSSFWorkbook(fis);		
			sh=wb.getSheetAt(0);	
		}
		catch (Exception e) {
			// TODO Auto-generated catch block
			e.getMessage();
		}
	}
	
	public String getData(String sheetname,int row,int column)
	{		
		sh=wb.getSheet(sheetname);
		
		String data=sh.getRow(row).getCell(column).getStringCellValue();

		return data;	
	}
	
	public String getrawData(String sheetname,int row,int column)
	{		
		sh=wb.getSheet(sheetname);
		
		String data=sh.getRow(row).getCell(column).getRawValue();

		return data;	
	}
}




