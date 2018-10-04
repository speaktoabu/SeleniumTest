# Selenium Testing using Jenkins with docker (different versions of Chrome & firefox browser)



* Current issue we are facing is that for selenium testing we need to provision many virtual machines to test different capabilities.
* To solve this I've come with solution using Docker and Jenkins

![Selenium](https://github.com/speaktoabu/SeleniumTest/blob/master/Selenium_images/01.png)

## Prerequisite:

* Jenkins "Extended Choice Parameter" plugin
* Selenium sample test case 
* Jenkins agent with Docker installed. 

## Jenkins Configuration 
* Create Jenkins Pipeline 
* Select the checkbox as given in the below Image and udpate groovy script. 

![Pipeline](https://github.com/speaktoabu/SeleniumTest/blob/master/Selenium_images/pipeline-job.png)

## Groovy script

```
import groovy.json.JsonSlurper;
import java.util.regex.*;


String REGEXC= "Google Chrome:(.*?)\r";
String REGEXF = "Mozilla Firefox:(.*?)\r";
List<String> versions = new ArrayList<String>()
def versionsObjectRaw =  new URL("https://api.github.com/repos/SeleniumHQ/docker-selenium/releases").getText()
def jsonSlurper = new JsonSlurper()
def versionsJsonObject = jsonSlurper.parseText(versionsObjectRaw)

int i=0;

versionsJsonObject.each {
  def INPUT = it.body;
  Matcher matcherChrome = Pattern.compile(REGEXC).matcher(INPUT);
  Matcher matcherFF = Pattern.compile(REGEXF).matcher(INPUT);
  String chromeVersion ="";
  String ffVersion ="";
  if(matcherChrome.find())
    chromeVersion = matcherChrome.group(1).replace("*", "").trim();

  if(matcherFF.find())
    ffVersion = matcherFF.group(1).replace("*", "").trim();

  if(!chromeVersion.isEmpty() && !ffVersion.isEmpty())
    versions.add(it.tag_name+":( ChromeVersion: "+chromeVersion+" & FirefoxVersion: "+ffVersion+" )");
}
return versions

```

## Pipeline script

```
import groovy.json.JsonSlurper
def tagName = "${Versions}".substring(0,"${Versions}".indexOf(':'))
def nodePort = ""
def provisionNode(nodePortIs) {
    int nP = "${nodePortIs}"
    def status = sh returnStatus: true, script: "netstat -anp | grep ${nP}"
    if("${status}"=="0") {
        nP = nP + 1
        provisionNode("${nP}")
    } else {
        return "${nP}"
    }
}
node('builder1'){
    stage('Provision node') {
        nodePort = provisionNode(4444)
        sh "sudo docker run -d -p ${nodePort}:4444 --name selenium-hub-${nodePort} selenium/hub:3.12.0-cobalt"
        sh "sudo docker run -d --name node-chrome-${JOB_NAME}-${BUILD_NUMBER} --link selenium-hub-${nodePort}:hub -v /dev/shm:/dev/shm selenium/node-chrome:${tagName}"
        sh "sudo docker run -d --name node-firefox-${JOB_NAME}-${BUILD_NUMBER} --link selenium-hub-${nodePort}:hub -v /dev/shm:/dev/shm selenium/node-firefox:${tagName}"
    }
    stage('Checkout') {
        git credentialsId: 'c60e5790-5bb5-4175-aec6-b344f8238495', url: 'https://github.com/speaktoabu/SeleniumTest.git'
    }
    stage('Run test') {
        sh "mvn clean install -DGridUrl=http://10.0.16.10:${nodePort}"
    }
    stage('Delete node') {
        sh "sudo docker stop selenium-hub-${nodePort}"
        sh "sudo docker rm selenium-hub-${nodePort}"
        sh "sudo docker stop node-chrome-${JOB_NAME}-${BUILD_NUMBER}"
        sh "sudo docker rm node-chrome-${JOB_NAME}-${BUILD_NUMBER}"
        sh "sudo docker stop node-firefox-${JOB_NAME}-${BUILD_NUMBER}"
        sh "sudo docker rm node-firefox-${JOB_NAME}-${BUILD_NUMBER}"
    }
}
```

Setup over !!!

## Lets start the test 

* Click on the "build with Parameter" and select the chrome / firefox version, you need to test

![Parameter Image](https://github.com/speaktoabu/SeleniumTest/blob/master/Selenium_images/build-withparameter-3.PNG)
![Parameter2 Image](https://github.com/speaktoabu/SeleniumTest/blob/master/Selenium_images/build-withparameter-4.PNG)
