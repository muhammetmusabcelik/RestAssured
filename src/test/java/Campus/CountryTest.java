package Campus;

import io.restassured.builder.RequestSpecBuilder;
import io.restassured.http.ContentType;
import io.restassured.http.Cookies;
import io.restassured.specification.RequestSpecification;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import java.util.HashMap;
import java.util.Map;

import static io.restassured.RestAssured.*;
import static org.hamcrest.Matchers.*;

import com.github.javafaker.Faker;

public class CountryTest {

    RequestSpecification recSpec;
    Faker faker = new Faker();
    String countryID;
    String rndCountryName;
    String rndCountryCode;

    @BeforeClass
    public void Setup() {

        baseURI = "https://test.mersys.io/";

        Map<String, String> userCredential = new HashMap<>();

        userCredential.put("username", "turkeyts");
        userCredential.put("password", "TechnoStudy123");
        userCredential.put("rememberMe", "true");
        Cookies cookies =
                given()
                        .body(userCredential)
                        .contentType(ContentType.JSON)
                        .when()
                        .post("/auth/login")

                        .then()
                        .log().all()
                        .statusCode(200)
                        .extract().response().getDetailedCookies();
        recSpec = new RequestSpecBuilder()
                .addCookies(cookies)
                .setContentType(ContentType.JSON)
                .build();
    }

    @Test
    public void createCountry() {
        rndCountryName = faker.country().name() + faker.name().fullName();
        rndCountryCode = faker.country().countryCode2() + faker.name().fullName();

        Map<String, String> newCountry = new HashMap<>();
        newCountry.put("name", rndCountryName);
        newCountry.put("code", rndCountryCode);

        countryID =
                given()
                        .spec(recSpec)
                        .body(newCountry)

                        .when()
                        .post("school-service/api/countries")

                        .then()
                        .log().body()
                        .statusCode(201)
                        .extract().path("id")
        ;

    }

    @Test(dependsOnMethods = "createCountry")
    public void createCountryNegative() {

        Map<String, String> newCountry = new HashMap<>();
        newCountry.put("name", rndCountryName);
        newCountry.put("code", rndCountryCode);

        given()
                .spec(recSpec)
                .body(newCountry)

                .when()
                .post("school-service/api/countries")

                .then()
                .log().body()
                .statusCode(400)
                .body("message", containsString("already"))
        ;

    }

    @Test(dependsOnMethods = "createCountry")
    public void getCountry() {

        Map<String, String> countryInfo = new HashMap<>();
        countryInfo.put("name", rndCountryName);

        given()
                .spec(recSpec)
                .body(countryInfo)

                .when()
                .post("school-service/api/countries/search")

                .then()
                .log().body()
                .statusCode(200)
        ;

    }

    @Test(dependsOnMethods = "createCountry")
    public void updateCountry() {
        String newCountryName = "UPDATE DONE" + faker.number().digits(5);

        Map<String, String> updateCountry = new HashMap<>();
        updateCountry.put("id", countryID);
        updateCountry.put("name", newCountryName);
        updateCountry.put("code", "UPDATE DONE");

        given()
                .spec(recSpec)
                .body(updateCountry)

                .when()
                .put("school-service/api/countries")

                .then()
                .log().body()
                .statusCode(200)
                .body("name", equalTo(newCountryName))
        ;
    }

    @Test(dependsOnMethods = "updateCountry")
    public void deleteCountry() {
        given()
                .spec(recSpec)

                .when()
                .delete("school-service/api/countries/" + countryID)

                .then()
                .statusCode(200)
        ;

    }

    @Test(dependsOnMethods = "deleteCountry")
    public void deleteCountryNegative() {
        given()
                .spec(recSpec)

                .when()
                .delete("school-service/api/countries/" + countryID)

                .then()
                .log().body()
                .statusCode(400)
                .body("message", equalTo("Country not found"))
        ;
    }

}
