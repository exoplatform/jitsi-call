var xmlHttp = new XMLHttpRequest();
xmlHttp.open("GET", "../portal/rest/jitsi/resources/version", false); // false for synchronous request
xmlHttp.send(null);
var resorcesVersion = xmlHttp.responseText;

/* RequireJS config */
var require = {
  baseUrl : "/portal/scripts/" + resorcesVersion,
  waitSeconds : 60,
  paths : {
    "call" : "../../../jitsi/js/call"
  }
};


