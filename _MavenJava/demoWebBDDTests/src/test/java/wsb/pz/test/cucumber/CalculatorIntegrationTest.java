package wsb.pz.test.cucumber;

import org.junit.runner.RunWith;

import io.cucumber.junit.Cucumber;
import io.cucumber.junit.CucumberOptions;

@RunWith(Cucumber.class)
@CucumberOptions(
  features = {"classpath:CucumberFeature/calculator.feature", "classpath:CucumberFeature/calculator-scenario-outline.feature"}
  , plugin = {"pretty", "json:target/reports/json/calculator.json"}
)
public class CalculatorIntegrationTest {
}
