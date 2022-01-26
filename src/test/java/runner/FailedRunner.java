package runner;
import org.junit.runner.RunWith;

import io.cucumber.junit.Cucumber;
import io.cucumber.junit.CucumberOptions;

@RunWith(Cucumber.class)
@CucumberOptions(
        features = "@target/rerunfailed.txt", 
        glue={"stepDefinition.skill"},
        plugin = {"pretty",
        			"rerun:target/rerunfailed.txt",
        		},
       // strict=true, // true: will fail execution if any undefined or pending steps. Default is false -- which is no longer supported
       // dryRun=true, // true , checks if all the steps have the step definition. Default value is :false
        monochrome = true,// true: displays the console output in much readable way. default is flase
        publish = true // to view the cucumber report
)
public class FailedRunner {

}