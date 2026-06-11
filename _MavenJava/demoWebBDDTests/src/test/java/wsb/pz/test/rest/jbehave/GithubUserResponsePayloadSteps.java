package wsb.pz.test.rest.jbehave;

import org.apache.http.HttpResponse;
import org.hamcrest.Matchers;
import org.jbehave.core.annotations.Given;
import org.jbehave.core.annotations.Then;
import org.jbehave.core.annotations.When;
import wsb.pz.test.rest.GitHubUser;
import wsb.pz.test.rest.RetrieveUtil;

import java.io.IOException;


import static org.hamcrest.MatcherAssert.assertThat;
import static wsb.pz.test.rest.jbehave.GithubUserNotFoundSteps.getGithubUserProfile;

public class GithubUserResponsePayloadSteps {

    private String api;
    private GitHubUser resource;

    @Given("github user profile api")
    public void givenGithubUserProfileApi() {
        api = "https://api.github.com/users/%s";
    }

    @When("I look for $user via the api")
    public void whenILookForEugenpViaTheApi(String user) throws IOException {
        HttpResponse httpResponse = getGithubUserProfile(api, user);
        resource = RetrieveUtil.retrieveResourceFromResponse(httpResponse, GitHubUser.class);
    }

    @Then("github's response contains a 'login' payload same as $username")
    public void thenGithubsResponseContainsAloginPayloadSameAsEugenp(String username) {
        assertThat(username, Matchers.is(resource.getLogin()));
    }

}
