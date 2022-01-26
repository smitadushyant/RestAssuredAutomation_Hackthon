package runner;

import org.junit.runner.RunWith;

import io.cucumber.junit.Cucumber;
import io.cucumber.junit.CucumberOptions;

@RunWith(Cucumber.class)
@CucumberOptions(
        features = "src/test/resources/FeatureFiles/skill", //set the path of feature files {}
       glue={"stepDefinition.skill"}, //set the path of step defiition files {}
     tags = "@GET or @POST and @Smoke",
      // tags = "@GET and @Negative", //Instructs what tags in the feature file should be executed
        //tags = "@Negative",
       //tags = "not @Negative" //operators "and","or", "not" are lower case
       //tags = "@Smoke or @Negative"
        plugin = {"pretty","io.qameta.allure.cucumber6jvm.AllureCucumber6Jvm",
        			"rerun:target/rerunfailed.txt",
        		"html:target/HtmlReports1/report.html",
        		  "json:target/JSONReports/report.json",
        		  "junit:target/JUnitReports/report.xml"},
       // strict=true, // true: will fail execution if any undefined or pending steps. Default is false -- which is no longer supported
       // dryRun=true, // true , checks if all the steps have the step definition. Default value is :false
        monochrome = true,// true: displays the console output in much readable way. default is flase
        publish = true // to view the cucumber report
)
public class TestRunner {

}