package EncryptionModule;

import java.awt.Dimension;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.util.Iterator;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPasswordField;
import javax.swing.JTextField;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

@SuppressWarnings("serial")
public class Password_Encryption extends JFrame implements ActionListener{
	
	  public Encryption encrpt = new Encryption();
	  String Tera_Username,Tera_Pass,Informatica_User,Informatica_Pass,Remote_User,Remote_Pass;
	  String filePath="../BatchExecution_BI/src/ConfigFiles/Config.xlsx";
	  JLabel TuserName=new JLabel ("Enter Teradata Username ");
	  JLabel Tpassword=new JLabel("Enter Teradata Password ");
	  JLabel IuserName=new JLabel("Enter Informatica Username ");
	  JLabel Ipassword=new JLabel("Enter Informatica Password ");
	  JLabel HuserName=new JLabel("Enter Remote Hostname Username ");
	  JLabel Hpassword=new JLabel("Enter Remote Hostname Password ");
	  
	  JLabel l10=new JLabel(" ");
	  JTextField TuserNametxt=new JTextField();
	  JPasswordField Tpasswordtxt=new JPasswordField();
	  JTextField IuserNametxt=new JTextField();
	  JPasswordField Ipasswordtxt=new JPasswordField();
	  JTextField HuserNametxt=new JTextField();
	  JPasswordField Hpasswordtxt=new JPasswordField();
	  JButton b= new JButton("SUBMIT");
	  JButton cancel= new JButton("CLEAR");
	  public Password_Encryption()
	  
	  { add(TuserName);
	    add(TuserNametxt);
	    add(Tpassword);
	    add(Tpasswordtxt);
	    add(IuserName);
	    add(IuserNametxt);
	    add(Ipassword);
	    add(Ipasswordtxt);
	    add(HuserName);
	    add(HuserNametxt);
	    add(Hpassword);
	    add(Hpasswordtxt);
	    add(b);
	    add(cancel);
	    add(l10);
	    TuserName.setBounds(20,45,260,30);
	    TuserName.setFont(new Font(Font.DIALOG, Font.BOLD, 15));
	    TuserNametxt.setBounds(285,45,450,30);
	    
	    Tpassword.setBounds(20,95,260,30);
	    Tpassword.setFont(new Font(Font.DIALOG, Font.BOLD, 15));
	    Tpasswordtxt.setBounds(285,95,450,30);
	    
	    IuserName.setBounds(20,145,260,30);
	    IuserName.setFont(new Font(Font.DIALOG, Font.BOLD, 15));
	    IuserNametxt.setBounds(285,145,450,30);
	    
	    Ipassword.setBounds(20,195,260,30);
	    Ipassword.setFont(new Font(Font.DIALOG, Font.BOLD, 15));
	    Ipasswordtxt.setBounds(285,195,450,30);
	    
	    HuserName.setBounds(20,245,260,30);
	    HuserName.setFont(new Font(Font.DIALOG, Font.BOLD, 15));
	    HuserNametxt.setBounds(285,245,450,30);
	    
	    Hpassword.setBounds(20,295,260,30);
	    Hpassword.setFont(new Font(Font.DIALOG, Font.BOLD, 15));
	    Hpasswordtxt.setBounds(285,295,450,30);
	    
	    b.setBounds(310,345,90,35);
	    cancel.setBounds(480, 345, 90, 35);
	    b.addActionListener(this);
	    cancel.addActionListener(this);
	    addWindowListener(new mwa());
	  }
	  @SuppressWarnings("deprecation")
	public void actionPerformed(ActionEvent e)
	  { 	  
		  if(e.getSource()==cancel) {
			  TuserNametxt.setText(null);
				Tpasswordtxt.setText(null);
				IuserNametxt.setText(null);
				Ipasswordtxt.setText(null);
				HuserNametxt.setText(null);
				Hpasswordtxt.setText(null);
		  }else {
			  
		  if (TuserNametxt.getText() == null || TuserNametxt.getText().trim().isEmpty() 
				  || Tpasswordtxt.getPassword() == null || Tpasswordtxt.getPassword().length==0
				  ||IuserNametxt.getText() == null || IuserNametxt.getText().trim().isEmpty() 
				  ||Ipasswordtxt.getPassword() == null || Ipasswordtxt.getPassword().length==0
				  ||HuserNametxt.getText() == null || HuserNametxt.getText().trim().isEmpty()
				  ||Hpasswordtxt.getPassword() == null || Hpasswordtxt.getPassword().length==0) {
			  JOptionPane.showMessageDialog(null, 
                      "TEXTFIELD ARE EMPTY, PLEASE TRY AGAIN..!!", 
                      "Failure", 
                      JOptionPane.WARNING_MESSAGE);
			  
			}
		  else {
			  
		  Tera_Username = TuserNametxt.getText();
		  writeExcel_XLSX(filePath,"Teradata_Username",Tera_Username);
		  
		  Tera_Pass = encrpt.encrypt(Tpasswordtxt.getText());
		  writeExcel_XLSX(filePath,"Teradata_Password",Tera_Pass);
		  
		  Informatica_User = IuserNametxt.getText();
		  writeExcel_XLSX(filePath,"Informatica_Username",Informatica_User);
		  
		  Informatica_Pass = encrpt.encrypt(Ipasswordtxt.getText());
		  writeExcel_XLSX(filePath,"Informatica_Password",Informatica_Pass);
		  
		  Remote_User = HuserNametxt.getText();
		  writeExcel_XLSX(filePath,"Remote_Host_Username",Remote_User);
		  
		  Remote_Pass = encrpt.encrypt(Hpasswordtxt.getText());
		  writeExcel_XLSX(filePath,"Remote_Host_Password",Remote_Pass);
		  
		  JOptionPane.showMessageDialog(null, 
                  "ALL PASSWORD ARE SUCCESSFULLY ENCRYPTED..!", 
                  "Success", 
                  JOptionPane.INFORMATION_MESSAGE); 
		  
		  l10.setText("All Password are successfully Encrypted in file ");
		  }
		  }
	  }
	  @SuppressWarnings("rawtypes")
	private void writeExcel_XLSX(String XLSX_FILE_PATH ,String Col_Name, String newValue) {

			XSSFRow mxssfrowRow = null;
			Iterator mitrRows,mitrCells;
			int noOfRows=0;
			try{
				FileInputStream file = new FileInputStream(new File(XLSX_FILE_PATH));
				XSSFWorkbook workbook = new XSSFWorkbook(file);
				mxssfrowRow = null;
				XSSFSheet sheet = workbook.getSheet("Config File"); 
				mitrRows = sheet.rowIterator();
				noOfRows = sheet.getPhysicalNumberOfRows();
				for (int i = 1; (i <noOfRows) && (mitrRows.hasNext()); i++)
				{
					mxssfrowRow = (XSSFRow) mitrRows.next();
					mitrCells = mxssfrowRow.cellIterator();							
										
					for( int j=0 ;mitrCells.hasNext() ; j++) 
					{
						mitrCells.next();	
						if(sheet.getRow(i).getCell(j)!=null){	
								int cellIndex = getColumnIndex(sheet, Col_Name);
								 Cell Cell=sheet.getRow(i).getCell(cellIndex);
								if (Cell == null) {
									Cell = sheet.getRow(i).createCell(cellIndex);
								}
								try {
									Cell.setCellValue(newValue);
									break;
								} catch (Exception e) {
									System.out.println(e);
								}
						}
					}
				}
				file.close();
				FileOutputStream out = new FileOutputStream(new File(XLSX_FILE_PATH));
				workbook.write(out);
				out.close();
				workbook.close();
			}catch(Exception e){
				e.printStackTrace();
			}
		
	}
	private int getColumnIndex(Sheet sheet, String columnName) {
		// TODO Auto-generated method stub
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
	public static void main(String s[])
	  { 
		Password_Encryption l=new Password_Encryption();
	    l.setSize(new Dimension(780,780));
	    l.setTitle("PASSWORD ENCRPTION");
	    l.setLocationRelativeTo(null);
	    l.setVisible(true);
	    l.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        l.setResizable(true);
	  }
	}
	class mwa extends WindowAdapter
	{ 
		public mwa(){
			
		}
		public void windowClosing(WindowEvent e)
		{ 
			System.exit(0);
		}

}