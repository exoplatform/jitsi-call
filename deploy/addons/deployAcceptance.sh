#!/bin/bash

GIT=`which git`
MVN=`which mvn`

# Paths to repos for deployment
#repos[0]=/h/Work/eXo/Meeds/social                            # social
#repos[1]=/h/Work/eXo/eXo_sources/exo-addons/chat-application # chat
#repos[2]=/h/Work/eXo/Real/cloud-drive-in-ecms/final/ecms	  # ecms
#repos[3]=/h/Work/eXo/Meeds/gatein-portal	 				  # gatein-portal
repos[4]=/h/Work/eXo/eXo_sources/exo-addons/web-conferencing # webconf
repos[5]=/h/Work/eXo/Real/Jitsi/jitsi-addon/jitsi            # jitsi

# Branches (should exist)
trunkBranch="trunk/jitsi"
developBranch="develop"
featureBranch="feature/jitsi"

function goToRepo(){
	cd ${repos[$1]}
	return $?
}

function pullBranch(){
	${GIT} pull --rebase 
	return $?
}

function goToBranch(){
	${GIT} checkout $1
	return $?
}

function rebaseBranch(){
	${GIT} rebase $1
	return $?
}

function forceBranch(){
	${GIT} push -f
	return $?
}

for index in ${!repos[*]}
do
	printf  "\n\n****************************************************\n"
	printf  "****************************************************\n"
	printf "Deploy to acceptance for ${repos[$index]}\n\n"
	
	if ! goToRepo $index
	then
		echo "Error go to repo for ${repos[$index]}"
		continue
	fi
	
	if ! goToBranch $trunkBranch
	then
		echo "Error go to trunk branch ($trunkBranch) for ${repos[$index]}"
		continue
	fi
	
	if ! pullBranch
	then
		echo "Error pull trunk branch ($trunkBranch) for ${repos[$index]}"
		continue
	fi
	
	if ! goToBranch $developBranch
	then
		echo "Error go to develop branch ($developBranch) for ${repos[$index]}"
		continue
	fi
	
	if ! pullBranch
	then
		echo "Error pull develop branch ($developBranch) for ${repos[$index]}"
		continue
	fi
	
	if ! goToBranch $featureBranch
	then
		echo "Error go to feature branch ($featureBranch) for ${repos[$index]}"
		continue
	fi
	
	if ! pullBranch
	then
		echo "Error pull feature branch  ($featureBranch) for ${repos[$index]}"
		continue
	fi
	
	if ! rebaseBranch $trunkBranch
	then
		echo "Error rebase trunk branch ($trunkBranch) for ${repos[$index]}"
		continue
	fi
	
	if ! rebaseBranch $developBranch
	then
		echo "Error rebase develop branch ($developBranch) for ${repos[$index]}"
		continue
	fi
	
	if ! forceBranch
	then
		echo "Error force feature branch ($featureBranch) for ${repos[$index]}"
		continue
	fi
	
	if ! goToBranch $trunkBranch
	then
		echo "Error final go to trunk branch ($trunkBranch) for ${repos[$index]}"
		continue
	fi
	
	printf "\nDONE"
done

printf  "\n\n"
read -n 1 -s -r -p "Press any key to continue"