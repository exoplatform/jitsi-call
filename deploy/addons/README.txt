Scripts to automate work with git repos and branches while development

Find repos and define paths in the script to work correctly. For example, /h/Work/eXo/Real/Jitsi/jitsi-addon/jitsi

chmod + x $ PWD/releaseAcceptance - makes the script file executable

PWD/releaseAcceptance.sh to run the script (on windows you can run through Git Bash)

Scripts:
cloneRepos.sh - allows to clone repos with feature branches
deployAcceptance.sh - allows to deploy the acceptance
resetFBsToDevelop.sh - resets feature branches to develop
resetTrunkBranchesToFBs.sh - resets trunk branches to feature branches