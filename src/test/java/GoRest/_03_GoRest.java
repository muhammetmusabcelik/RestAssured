package GoRest;

import Model.GoRestUser;
import com.github.javafaker.Faker;
import io.restassured.builder.RequestSpecBuilder;
import io.restassured.http.ContentType;
import io.restassured.specification.RequestSpecification;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import java.util.HashMap;
import java.util.Map;

import static io.restassured.RestAssured.*;
import static org.hamcrest.Matchers.*;

public class _03_GoRest {
    Faker faker = new Faker();
    int userID = 0;
    RequestSpecification reqSpec;

    @BeforeClass
    public void setup() {
        baseURI = "https://gorest.co.in/public/v2/users";

        reqSpec = new RequestSpecBuilder()
                .addHeader("Authorization", "Bearer 38fdcbe2e2296006c947378d1ee4ec91310a66d10d3fec7e038f23770998cad1")
                .setContentType(ContentType.JSON)
                .build()
        ;
    }

//    @Test
//    public void createUser() {
//        // 280586d073058f5f9b8f7d77cd656bf307c1ac5090d3d93b631514d499b52336
//        String randomFullName = faker.name().fullName();
//        String randomEmail = faker.internet().emailAddress();
//
//        userID =
//                given()
//                        .spec(reqSpec)
//                        .body("{\"name\":\"" + randomFullName + "\", \"gender\":\"male\", \"email\":\"" + randomEmail + "\", \"status\":\"active\"}") // giden body
//
//
//                        .when()
//                        .post(baseURI)
//
//                        .then()
//                        .log().body()
//                        .statusCode(201)
//                        .extract().path("id")
//        ;
//    }

    @Test
    public void createUserMap() {
        // 280586d073058f5f9b8f7d77cd656bf307c1ac5090d3d93b631514d499b52336
        String randomFullName = faker.name().fullName();
        String randomEmail = faker.internet().emailAddress();

        Map<String, String> newUser = new HashMap<>();
        newUser.put("name", randomFullName);
        newUser.put("gender", "male");
        newUser.put("email", randomEmail);
        newUser.put("status", "active");
        userID =
                given()
                        .header("Authorization", "Bearer ea456749c622405eb573bcc13ee559e927c89d3f51ca505ced43890eb07bc143")
                        .body(newUser) // giden body
                        .contentType(ContentType.JSON)

                        .when()
                        .post(baseURI)

                        .then()
                        .log().body()
                        .statusCode(201)
                        .extract().path("id")
        ;
    }

//    @Test
//    public void createUserClass() {
//        // 280586d073058f5f9b8f7d77cd656bf307c1ac5090d3d93b631514d499b52336
//        String randomFullName = faker.name().fullName();
//        String randomEmail = faker.internet().emailAddress();
//        GoRestUser user = new GoRestUser();
//        user.name = randomFullName;
//        user.email = randomEmail;
//        user.gender = "male";
//        user.status = "active";
//
//        userID =
//                given()
//                        .header("Authorization", "Bearer 38fdcbe2e2296006c947378d1ee4ec91310a66d10d3fec7e038f23770998cad1")
//                        .body(user) // giden body
//                        .contentType(ContentType.JSON)
//
//                        .when()
//                        .post("https://gorest.co.in/public/v2/users")
//
//                        .then()
//                        .log().body()
//                        .statusCode(201)
//                        .extract().path("id")
//        ;
//    }

    @Test(dependsOnMethods = "createUserMap")
    public void getUserById() {

        given()
                .spec(reqSpec)

                .when()
                .get("" + userID)

                .then()
                .log().body()
                .statusCode(200)
                .contentType(ContentType.JSON)
                .body("id", equalTo(userID))
        ;
    }

    @Test(dependsOnMethods = "getUserById")
    public void updateUser() {
        Map<String, String> updateUser = new HashMap<>();
        updateUser.put("name", "Ahmet Demir");

        given()

                .spec(reqSpec)
                .body(updateUser)

                .when()
                .put("" + userID)

                .then()
                .log().body()
                .statusCode(200)
                .body("id", equalTo(userID))
                .body("name", equalTo("Ahmet Demir"))
        ;
    }

    @Test(dependsOnMethods = "updateUser")
    public void deleteUser() {
        given()
                .spec(reqSpec)
                .when()
                .delete("" + userID)

                .then()
                .statusCode(204)
        ;
    }
    @Test(dependsOnMethods = "deleteUser")
    public void deleteUserNegative() {
        given()
                .spec(reqSpec)
                .when()
                .delete("" + userID)

                .then()
                .statusCode(404)
        ;
    }

}

