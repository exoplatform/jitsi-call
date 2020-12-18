#!/bin/bash

#This script allows to clone repos with feature branches

GIT=`which git`
MVN=`which mvn`

# Repos to clone
repos[0]=Meeds-io/ws.git
repos[1]=Meeds-io/social.git
repos[2]=Meeds-io/platform-ui.git
repos[3]=Meeds-io/meeds.git	 				  
repos[4]=Meeds-io/maven-depmgt-pom.git 
repos[5]=Meeds-io/kernel.git            
repos[6]=Meeds-io/gatein-wci.git            
repos[7]=Meeds-io/gatein-sso.git            
repos[8]=Meeds-io/gatein-portal.git            
repos[9]=Meeds-io/gatein-pc.git            
repos[10]=Meeds-io/gatein-dep.git            
repos[11]=Meeds-io/core.git            
repos[12]=Meeds-io/commons.git            
repos[13]=Meeds-io/addons-manager.git            
repos[14]=Meeds-io/wallet.git            
repos[15]=Meeds-io/push-notifications.git            
repos[16]=Meeds-io/perk-store.git           
repos[17]=Meeds-io/kudos.git           
repos[18]=Meeds-io/gamification.git             
repos[19]=Meeds-io/exo-es-embedded.git            
repos[20]=Meeds-io/app-center.git            
repos[21]=exoplatform/agenda.git           
repos[22]=exoplatform/wiki.git            
repos[23]=exoplatform/platform-private-distributions.git            
repos[24]=exoplatform/jcr.git            
repos[25]=exoplatform/forum.git            
repos[26]=exoplatform/ecms.git            
repos[27]=exoplatform/calendar.git            
repos[28]=exoplatform/web-conferencing.git            
repos[29]=exoplatform/task.git            
repos[30]=exoplatform/spnego-addon.git            
repos[31]=exoplatform/saml2-addon.git            
repos[32]=exoplatform/remote-edit.git            
repos[33]=exoplatform/openam-addon.git            
repos[34]=exoplatform/onlyoffice.git            
repos[35]=exoplatform/news.git            
repos[36]=exoplatform/legacy-intranet.git            
repos[37]=exoplatform/lecko.git            
repos[38]=exoplatform/layout-management.git            
repos[39]=exoplatform/jitsi.git            
repos[40]=exoplatform/digital-workplace.git            
repos[41]=exoplatform/data-upgrade.git            
repos[42]=exoplatform/cmis-addon.git            
repos[43]=exoplatform/chat-application.git           
repos[44]=exoplatform/cas-addon.git     

# can be "https://github.com/"
cloneType="git@github.com:"               

function cloneRepo(){
	${GIT} clone "${cloneType}${repos[$1]}"
	return $?
}

for index in ${!repos[*]}
do
	printf  "\n\n****************************************************\n"
	printf  "****************************************************\n"
	printf "Clone repo ${repos[$index]}\n\n"
	
	if ! cloneRepo $index
	then
		echo "Error to clone repo ${repos[$index]}"
		continue
	fi
	
	printf "\nDONE"
done

printf  "\n\n"
read -n 1 -s -r -p "Press any key to continue"