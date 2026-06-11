package wsb.pz.test.rest.cucumber;

import io.cucumber.junit.Cucumber;
import io.cucumber.junit.CucumberOptions;
import org.junit.runner.RunWith;

@RunWith(Cucumber.class)
@CucumberOptions(features = "classpath:CucumberFeature/cucumber.feature")
public class CucumberIntegrationTest {
}