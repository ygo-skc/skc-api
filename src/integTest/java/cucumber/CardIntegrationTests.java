package cucumber;

import io.cucumber.java.en.And;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.restassured.path.json.JsonPath;
import io.restassured.response.ValidatableResponse;
import static io.restassured.RestAssured.*;
import static org.hamcrest.Matchers.*;

public class CardIntegrationTests {

//    private final String BASE_ENDPOINT = "http://localhost:9999/api/v1/card";
    private final String BASE_ENDPOINT = "https://skc-ygo-api.com/api/v1/card";
//    private final String BASE_ENDPOINT = "https://skc-ygo-api.com/api/v1/card";

    private JsonPath jsonPath;
    private ValidatableResponse validatableResponse;

    @Given("I request all info for card with ID: {string}")
    public void i_request_all_info_for_card_with_id(String cardId)
    {

        final String requestEndpoint = BASE_ENDPOINT + "/" + cardId + "?allInfo=true";
        jsonPath = get(requestEndpoint).jsonPath();
        validatableResponse = get(requestEndpoint).then();

    }

    @Given("I request info without all info flag for card with ID: {string}")
    public void i_request_some_info_for_card_with_id(String cardId)
    {

        final String requestEndpoint = BASE_ENDPOINT + "/" + cardId;
        validatableResponse = get(requestEndpoint).then();

    }


    @Then("http status of response should be {int}")
    public void http_status_should_be(final int httpStatus)
    {
        validatableResponse.statusCode(httpStatus);
    }


    @And("cardID should be {string}")
    public void check_card_id_is_correct(final String cardID)
    {
        validatableResponse.body("cardID", equalTo(cardID));
    }


    @And("cardName should be {string}")
    public void check_card_name_is_correct(final String cardName)
    {
        validatableResponse.body("cardName", equalTo(cardName));
    }


    @And("cardColor should be {string}")
    public void check_card_color_is_correct(final String cardColor)
    {
        validatableResponse.body("cardColor", equalTo(cardColor));
    }


    @And("cardAttribute should be {string}")
    public void check_card_attribute_is_correct(final String cardAttribute)
    {
        validatableResponse.body("cardAttribute", equalTo(cardAttribute));
    }


    @And("monsterType should be {string}")
    public void check_monster_type_is_correct(String monsterType)
    {
        if (monsterType.equals("")) monsterType = null;
        validatableResponse.body("monsterType", equalTo(monsterType));
    }


    @And("monsterAtk should be {string}")
    public void check_monster_atk(String monsterAtk)
    {
        if (monsterAtk.equals(""))
            validatableResponse.body("monsterAttack", equalTo(null));
        else
        {
            validatableResponse.body("monsterAttack", equalTo(Integer.parseInt(monsterAtk)));
        }
    }


    @And("monsterDef should be {string}")
    public void check_monster_def(String monsterDef)
    {
        if (monsterDef.equals(""))
            validatableResponse.body("monsterDefense", equalTo(null));
        else
        {
            validatableResponse.body("monsterDefense", equalTo(Integer.parseInt(monsterDef)));
        }
    }


    @And("level should be {string}")
    public void check_level(final String level)
    {
        final Integer levelInt = (level.equals(""))? null : Integer.parseInt(level);
        if (levelInt != null)
        {
            validatableResponse.body("monsterAssociation.level", equalTo(levelInt));
        } else
        {
            validatableResponse.body("monsterAssociation.level", equalTo(null));
        }
    }


    @And("rank should be {string}")
    public void check_rank(final String rank)
    {
        final Integer rankInt = (rank.equals(""))? null : Integer.parseInt(rank);
        if (rankInt != null)
        {
            validatableResponse.body("monsterAssociation.rank", equalTo(rankInt));
        } else
        {
            validatableResponse.body("monsterAssociation.rank", equalTo(null));
        }
    }


    @And("card effect should not be empty or null")
    public void check_card_effect_is_not_empty()
    {
        validatableResponse.body("cardEffect", is(notNullValue()));
        validatableResponse.body("cardEffect", is(not(emptyString())));
    }


    @And("products card is found in should be greater than or equal to {int}")
    public void check_products_found_in_array_has_a_minimum_of_products(final int productsFoundIn)
    {
        validatableResponse.body("foundIn.size()", is(greaterThanOrEqualTo(productsFoundIn)));
    }


    @And("products card is found in should not be included")
    public void check_products_found_in_array_is_not_included()
    {
        validatableResponse.body("foundIn", nullValue());
    }


    @And("ban lists card is found in should be greater than or equal to {int}")
    public void check_restricted_in_array_has_a_minimum_of(final int restrictedIn)
    {
        if (jsonPath.get("restrictedIn") == null)  check_restricted_in_array_is_not_included();
        else validatableResponse.body("restrictedIn.size()", is(greaterThanOrEqualTo(restrictedIn)));
    }


    @And("ban lists card is found in should not be included")
    public void check_restricted_in_array_is_not_included()
    {
        validatableResponse.body("restrictedIn", nullValue());
    }


}
