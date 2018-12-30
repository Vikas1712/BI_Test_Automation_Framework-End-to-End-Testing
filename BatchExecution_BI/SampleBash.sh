#! /bin/bash
 echo "Hello World" 
 echo Our shell name is $BASH
 echo Our shell version is @BASH_VERSION
 echo Our current directory is $PWD
 name=Mark 
 number=10 
 echo The name is $name echo value is $number
 cd /pwc/Informatica/10.2/server/bin 
 echo Our current directory is $PWD
 export PATH=$PATH:/pwc/Informatica/10.2/server/bin
 export LD_LIBRARY_PATH=$LD_LIBRARY_PATH:/pwc/Informatica/10.2/server/bin
 echo Our current directory is $PWD
 echo Executing Workflow
 pmcmd startworkflow -sv PWC_INT_DEV -d PWC_Domain_DEV -usd TMNL -u ID029640 -p Password -f KEY_CDM -wait wf_KEY_SELE_TICO001
 pmcmd startworkflow -sv PWC_INT_DEV -d PWC_Domain_DEV -usd TMNL -u ID029640 -p Password -f KEY_CDM -wait wf_KEY_SELE_TICO002
 pmcmd startworkflow -sv PWC_INT_DEV -d PWC_Domain_DEV -usd TMNL -u ID029640 -p Password -f KEY_CDM -wait wf_APPLY_WSLA_TICO001
 pmcmd startworkflow -sv PWC_INT_DEV -d PWC_Domain_DEV -usd TMNL -u ID029640 -p Password -f EDW -wait wf_DIM_CALL_CONSULT_CONS001
 pmcmd startworkflow -sv PWC_INT_DEV -d PWC_Domain_DEV -usd TMNL -u ID029640 -p Password -f EDW -wait wf_DIM_CALL_CENTER_WORKGROUP
 pmcmd startworkflow -sv PWC_INT_DEV -d PWC_Domain_DEV -usd TMNL -u ID029640 -p Password -f EDW -wait wf_FCT_WORKGROUP_CALL_CCC001