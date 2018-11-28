package Modules.Diesel.BI;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Map;
import EncryptionModule.Encryption;
import FunctionLibrary.ExcelDataPoolManager;



public class ReadTestCase {

	private static ExcelDataPoolManager objEDPM=new ExcelDataPoolManager();
	 public static Encryption encrpt = new Encryption();
	
	@SuppressWarnings("rawtypes")
	public static void  executeTestCase() throws ClassNotFoundException, SQLException {
		
	String teraUser = null , teraPassword = null;
	int TotalTC = 0,TotalPass=0,TotalFail=0;
	Connection conn;
		try {
			
			List <Map> modulesList = objEDPM.readExcel_XLSX("../BatchExecution_BI/src/SuitFile/SuitFile.xlsx", "Config File");
			
			List <Map> configList = objEDPM.readExcel_XLSX("../BatchExecution_BI/src/ConfigFiles/Config.xlsx", "Config File");
			modulesList.forEach(System.out::println);
			
			for (Map aModulesList : modulesList) {
				String connurl;	
				System.out.println(" JDBC driver loaded. \n");
				
				Class.forName("com.teradata.jdbc.TeraDriver");
				System.out.println(" Looking for the Teradata JDBC driver...\n ");
				
				for (Map aConfigList : configList) {	
					teraUser = objEDPM.getValueByKey (aConfigList.entrySet (), "Teradata_Username");
				 	teraPassword = objEDPM.getValueByKey ( aConfigList.entrySet (), "Teradata_Password");
				}
				String Evnr = objEDPM.getValueByKey (aModulesList.entrySet (), "Environment");
				if(Evnr.equalsIgnoreCase("Development")) {
					connurl="jdbc:teradata://acctera.ux.nl.tmo";
				}else if(Evnr.equalsIgnoreCase("Acceptance")) {
					connurl="jdbc:teradata://acctera.ux.nl.tmo";
				}else {
					connurl="jdbc:teradata://acctera.ux.nl.tmo";
				}
				conn=DriverManager.getConnection(connurl, teraUser,encrpt.decrypt(teraPassword) );	
				System.out.println("\n User " + teraUser + " connected.");
				
				String TestCaseFilePath = objEDPM.getValueByKey (aModulesList.entrySet (), "TestCasePath");
				String TableName = objEDPM.getValueByKey (aModulesList.entrySet (), "TablesName");
				
				System.out.println(TableName);
				List <Map> testCaseList = objEDPM.readExcel_XLSX (TestCaseFilePath, Evnr);
				ArrayList<String> headers = new ArrayList<String>();
				headers.add("TESTCASE NAME");
				headers.add("TESTCASE_ACTUAL_RESULT");
				headers.add("TESTCASE STATUS");
				headers.add("TESTCASE_EXECUTION_DATE");
				ArrayList<ArrayList<String>> content = new ArrayList<ArrayList<String>>();
				for (Map aTestCaseList : testCaseList) {

					String TestQuery = objEDPM.getValueByKey (aTestCaseList.entrySet (), "TC_QUERY");			
					String TestCondition=objEDPM.getValueByKey (aTestCaseList.entrySet (), "TC_SIGN");		
					String TestCase_Name=objEDPM.getValueByKey (aTestCaseList.entrySet (), "TC_NAME").trim();		
					try {	
						String query=TestQuery;
						PreparedStatement stmt=conn.prepareStatement(query);
						ResultSet rs=stmt.executeQuery();	
						while(rs.next()) {
							String result=rs.getString(1);
							String timeStamp = new SimpleDateFormat("dd-MM-YYYY HH :mm : ss").format(Calendar.getInstance().getTime());
							if(TestCondition.equalsIgnoreCase(">")) {
								int iresult=rs.getInt(1);
								if(iresult > 0) {
									TotalPass++;
									ArrayList<String> resultList = new ArrayList<String>();
									resultList.add(TestCase_Name);
									resultList.add(result);
									resultList.add("PASS");
									resultList.add(timeStamp);
									content.add(resultList);
									objEDPM.writeExcel_XLSX (TestCaseFilePath, Evnr,"STATUS",query,"PASS");
									objEDPM.writeExcel_XLSX (TestCaseFilePath, Evnr, "TC_ACTUAL_RESULT",query,result);
									objEDPM.writeExcel_XLSX (TestCaseFilePath, Evnr, "TC_EXECUTION_DATE",query,timeStamp);							
								}else {
									TotalFail++;
									ArrayList<String> resultList = new ArrayList<String>();
									resultList.add(TestCase_Name);
									resultList.add(result);
									resultList.add("FAIL");
									resultList.add(timeStamp);
									content.add(resultList);
									objEDPM.writeExcel_XLSX (TestCaseFilePath, Evnr, "STATUS",query,"FAIL");
									objEDPM.writeExcel_XLSX (TestCaseFilePath, Evnr, "TC_ACTUAL_RESULT",query,result);
									objEDPM.writeExcel_XLSX (TestCaseFilePath, Evnr,"TC_EXECUTION_DATE",query,timeStamp);
								}	
							}
							if(TestCondition.equalsIgnoreCase("=")) {
								int iresult=rs.getInt(1);
									if(iresult == 0) {
										TotalPass++;
										ArrayList<String> resultList = new ArrayList<String>();
										resultList.add(TestCase_Name);
										resultList.add(result);
										resultList.add("PASS");
										resultList.add(timeStamp);
										content.add(resultList);
											objEDPM.writeExcel_XLSX (TestCaseFilePath, Evnr,"STATUS",query,"PASS");
											objEDPM.writeExcel_XLSX (TestCaseFilePath, Evnr, "TC_ACTUAL_RESULT",query,result);
											objEDPM.writeExcel_XLSX (TestCaseFilePath, Evnr, "TC_EXECUTION_DATE",query,timeStamp);							
									}else {
										TotalFail++;
										ArrayList<String> resultList = new ArrayList<String>();
										resultList.add(TestCase_Name);
										resultList.add(result);
										resultList.add("FAIL");
										resultList.add(timeStamp);
										content.add(resultList);
											objEDPM.writeExcel_XLSX (TestCaseFilePath, Evnr, "STATUS",query,"FAIL");
											objEDPM.writeExcel_XLSX (TestCaseFilePath, Evnr, "TC_ACTUAL_RESULT",query,result);
											objEDPM.writeExcel_XLSX (TestCaseFilePath, Evnr,"TC_EXECUTION_DATE",query,timeStamp);
									}	
							}
						}
					} catch (Exception e) {
						e.printStackTrace();
					}
				}
				ConsoleTable ct = new ConsoleTable(headers,content);
				ct.printTable();
				conn.close();
				TotalTC=TotalPass+TotalFail;
				System.out.println("[ Total TestCase : "+TotalTC +"] "+"[ TestCase Pass : "+TotalPass+"] "+"[ TestCase Fail : "+TotalFail +"]");
				System.out.println(" Connection to Teradata closed. \n");
				System.out.println("-----------------------------------------------------------------------------------------------------------");
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		
	}
	

}
