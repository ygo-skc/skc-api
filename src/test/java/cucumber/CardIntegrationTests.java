package cucumber;

import io.cucumber.java.en.And;
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


    @Then("http status of response should be {int}")
    public void http_status_should_be(final int httpStatus)
    {
        response.then().statusCode(httpStatus);
    }


    @And("cardName should be {string}")
    public void check_card_name_is_correct(final String cardName)
    {
        response.then().body("cardName", equalTo(cardName));
    }


    @And("cardColor should be {string}")
    public void check_card_color_is_correct(final String cardColor)
    {
        response.then().body("cardColor", equalTo(cardColor));
    }


    @And("cardAttribute should be {string}")
    public void check_card_attribute_is_correct(final String cardAttribute)
    {
        response.then().body("cardAttribute", equalTo(cardAttribute));
    }


    @And("monsterType should be {string}")
    public void check_monster_type_is_correct(String monsterType)
    {
        if (monsterType.equals("")) monsterType = null;
        response.then().body("monsterType", equalTo(monsterType));
    }


    @And("monsterAtk should be {string}")
    public void check_monster_atk(String monsterAtk)
    {
        if (monsterAtk.equals(""))
            response.then().body("monsterAttack", equalTo(null));
        else
        {
            response.then().body("monsterAttack", equalTo(Integer.parseInt(monsterAtk)));
        }
    }


    @And("monsterDef should be {string}")
    public void check_monster_def(String monsterDef)
    {
        if (monsterDef.equals(""))
            response.then().body("monsterDefense", equalTo(null));
        else
        {
            response.then().body("monsterDefense", equalTo(Integer.parseInt(monsterDef)));
        }
    }

}
