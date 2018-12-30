package Modules.Diesel.BI;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import com.jcraft.jsch.ChannelExec;
import com.jcraft.jsch.JSch;
import com.jcraft.jsch.Session;

import EncryptionModule.Encryption;
import FunctionLibrary.ExcelDataPoolManager;
/*
 * @Author : VIKAS YADAV	
 */
public class ShellExecuter {
	
	private static ExcelDataPoolManager objEDPM=new ExcelDataPoolManager();
	public static Encryption encrpt = new Encryption();
	 
	private static String USERNAME; // username for remote host
	private static String PASSWORD; // password of the remote host
	private static String host = "remotehostaddress"; // remote host address
	private static int port=22;
	private static String InformaticaUserName;// Informatica Username read from config file
	private static String InformaticaPassword;// Informatica Password read from config file
	 @SuppressWarnings("rawtypes")
	public static void executeFile()
	 {
	     List<String> result = new ArrayList<String>();
	     try
	     {
	    	 System.out.println(System.getenv().get("USERNAME"));
	    	 List <Map> configList = objEDPM.readExcel_XLSX("../BatchExecution_BI/src/ConfigFiles/Config.xlsx", "Config File");

	    	 for (Map aConfigList : configList) {
	    		 USERNAME = objEDPM.getValueByKey (aConfigList.entrySet (), "Remote_Host_Username");
	    		 PASSWORD = objEDPM.getValueByKey ( aConfigList.entrySet (), "Remote_Host_Password");
	    		 InformaticaUserName = objEDPM.getValueByKey (aConfigList.entrySet (), "Informatica_Username");
				 InformaticaPassword = objEDPM.getValueByKey (aConfigList.entrySet (), "Informatica_Password");
	    	 }
	         JSch jsch = new JSch();
	         System.out.println(" Looking for the Informatica Server...\n ");
	         Session session = jsch.getSession(USERNAME, host, port);
	         session.setConfig("StrictHostKeyChecking", "no");
	         session.setPassword(encrpt.decrypt(PASSWORD));
	         session.connect();
	         System.out.println("\n User " + USERNAME + " connected to " + host +" Server Successfully..!!");
	         ChannelExec channelExec = (ChannelExec)session.openChannel("exec");
	         InputStream in = channelExec.getInputStream();
	         ExcelDataPoolManager objEDPM=new ExcelDataPoolManager();
	         String syr="";
	         String stry="-----------------------------------------------------------------------------------------------------------";
	         
	         List <Map> modulesList = objEDPM.readExcel_XLSX("../BatchExecution_BI/src/SuitFile/WorkflowSheet.xlsx", "Development");
				for (Map aModulesList : modulesList) {
					String integrationServiceName = objEDPM.getValueByKey (aModulesList.entrySet (), "IntegrationServiceName");
					String domainName = objEDPM.getValueByKey (aModulesList.entrySet (), "DomainName");
					String folderName = objEDPM.getValueByKey (aModulesList.entrySet (), "FolderName");
					String workFlowName = objEDPM.getValueByKey (aModulesList.entrySet (), "WorkFlowName");
					String sessionName = objEDPM.getValueByKey (aModulesList.entrySet (), "SessionName");
					if(!"".equals(syr)) {
						syr=syr+"\n";	
					}else {
						syr=" cd /pwc/Informatica/10.2/server/bin  \n"+
				        		 " echo Our current directory is $PWD \n"+
				        		 " export PATH=$PATH:/pwc/Informatica/10.2/server/bin \n"+
				        		 " export LD_LIBRARY_PATH=$LD_LIBRARY_PATH:/pwc/Informatica/10.2/server/bin \n";
						syr=syr+" echo "+stry+" \n";
					}
					syr=syr+" pmcmd startworkflow -sv "+integrationServiceName+" -d "+domainName+" -usd TMNL -u "+InformaticaUserName+" -p "+encrpt.decrypt(InformaticaPassword)+" -f "+folderName+" -wait "+workFlowName+" \n";
					syr=syr+" echo "+stry+" \n";
					syr=syr+" pmcmd getsessionstatistics -sv "+integrationServiceName+" -d "+domainName+" -usd TMNL -u "+InformaticaUserName+" -p "+encrpt.decrypt(InformaticaPassword)+" -f "+folderName+" -w "+workFlowName+" "+ sessionName+"\n";
					
					syr=syr+" echo "+stry;
				}
				syr=syr+"\n";

	         channelExec.setCommand(syr);

	         channelExec.connect();


	         BufferedReader reader = new BufferedReader(new InputStreamReader(in));
	         String line;
	         
	         while ((line = reader.readLine()) != null)
	         {
	             result.add(line);
	             System.out.println(line);
	         }
	         int exitStatus = channelExec.getExitStatus();

	         channelExec.disconnect();
	         session.disconnect();

	         if(exitStatus < 0){
	        	 System.out.println("Done, but exit status not set!");
	         }
	         else if(exitStatus > 0){
	        	 System.out.println("Done, but with error!");
	         }
	         else{
	             System.out.println("Done!");
	         }
	     }
	     
	     catch(Exception e)
	     {
	         System.err.println("Error: " + e);
	     }
	 }
	 
	 public static void main(String[] args) throws Exception, SQLException {

		 executeFile();
		 ReadTestCase.executeTestCase();
	 }
}
