package Modules.Diesel.BI;

import java.io.FileWriter;
import java.util.List;
import java.util.Map;

import FunctionLibrary.ExcelDataPoolManager;
/*
 * @Author : VIKAS YADAV	
 */
public class Create_Bash_Script {
	
	private static FileWriter mObjFW0;
	public static String filePath;
	public String HeaderText;
	public String setPath="\n export PATH=$PATH:/pwc/Informatica/10.2/server/bin"+"\n export LD_LIBRARY_PATH=$LD_LIBRARY_PATH:/pwc/Informatica/10.2/server/bin"+"\n echo Our current directory is $PWD"+"\n echo Executing Workflow";	
	private static ExcelDataPoolManager objEDPM=new ExcelDataPoolManager();
	
	public static void main(String[] args) throws Exception {
		
		Create_Bash_Script script=new Create_Bash_Script();
		script.init_detail_Bash_Script();
		script.details_append();

	}

	public void init_detail_Bash_Script()throws Exception {
    	filePath="../BatchExecution_BI/SampleBash.sh";
		try{
			mObjFW0=new FileWriter(filePath);
		}catch(Exception e)
		{
			e.printStackTrace();
		}
		HeaderText="#! /bin/bash"+"\n echo \"Hello World\" "+"\n echo Our shell name is $BASH"+"\n echo Our shell version is @BASH_VERSION"+"\n echo Our current directory is $PWD"+"\n name=Mark \n number=10 \n echo The name is $name echo value is $number"+"\n cd /pwc/Informatica/10.2/server/bin "+"\n echo Our current directory is $PWD";
		mObjFW0.write(HeaderText);
		mObjFW0.write(setPath);
		mObjFW0.close();
    }
	 public void details_append() throws Exception {
		 
		 
		 try {
				@SuppressWarnings("rawtypes")
				List <Map> modulesList = objEDPM.readExcel_XLSX("../BatchExecution_BI/src/SuitFile/WorkflowSheet.xlsx", "Development");
				for (@SuppressWarnings("rawtypes") Map aModulesList : modulesList) {
					mObjFW0=new FileWriter(filePath,true);
					String userName = objEDPM.getValueByKey (aModulesList.entrySet (), "UserName");
					String password = objEDPM.getValueByKey (aModulesList.entrySet (), "Password");
					String integrationServiceName = objEDPM.getValueByKey (aModulesList.entrySet (), "IntegrationServiceName");
					String domainName = objEDPM.getValueByKey (aModulesList.entrySet (), "DomainName");
					String folderName = objEDPM.getValueByKey (aModulesList.entrySet (), "FolderName");
					String workFlowName = objEDPM.getValueByKey (aModulesList.entrySet (), "WorkFlowName");
					String strCMD="\n pmcmd startworkflow -sv "+integrationServiceName+" -d "+domainName+" -usd TMNL -u "+userName+" -p "+password+" -f "+folderName+" -wait "+workFlowName;
					mObjFW0.append(strCMD);
			    	mObjFW0.close();
				}
			} catch (Exception e) {
				e.printStackTrace();
			}finally{
				mObjFW0.close();
			}
	    }
}
