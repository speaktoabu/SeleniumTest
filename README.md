# Selenium Testing using Jenkins with docker images (different versions of Chrome & firefox browser)



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
