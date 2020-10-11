package cucumber;

import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import io.restassured.RestAssured;
import io.restassured.response.Response;

import static org.hamcrest.Matchers.equalTo;

public class CardIntegrationTests {

    private final String BASE_ENDPOINT = "https://ygoapi-dev.cfapps.io/api/v1/card";

    private Response response;


    @Given("I request info for card with ID: {string}")
    public void i_request_info_for_card_with_id(String cardId)
    {

        final String requestEndpoint = BASE_ENDPOINT + "/" + cardId;
        response = RestAssured.get(requestEndpoint);

    }


    @Then("cardName should be {string}")
    public void check_card_name_is_correct(final String cardName)
    {
        response.then().body("cardName", equalTo(cardName));
    }


    @Then("cardColor should be {string}")
    public void check_card_color_is_correct(final String cardColor)
    {
        response.then().body("cardColor", equalTo(cardColor));
    }


    @Then("cardAttribute should be {string}")
    public void check_card_attribute_is_correct(final String cardAttribute)
    {
        response.then().body("cardAttribute", equalTo(cardAttribute));
    }


    @Then("monsterType should be {string}")
    public void check_monster_type_is_correct(final String monsterType)
    {
        response.then().body("monsterType", equalTo(monsterType));
    }

}
