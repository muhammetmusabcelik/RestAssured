package GoRest;

import com.github.javafaker.Faker;
import io.restassured.builder.RequestSpecBuilder;
import io.restassured.http.ContentType;
import io.restassured.specification.RequestSpecification;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import java.util.HashMap;
import java.util.Map;

import static io.restassured.RestAssured.*;
import static io.restassured.RestAssured.*;
import static org.hamcrest.Matchers.equalTo;

public class _04_GoRestComment {

    Faker faker = new Faker();
    int commentID = 0;
    RequestSpecification reqSpec;

    @BeforeClass
    public void setup() {
        baseURI = "https://gorest.co.in/public/v2/comments";

        reqSpec = new RequestSpecBuilder()
                .addHeader("Authorization", "Bearer ea456749c622405eb573bcc13ee559e927c89d3f51ca505ced43890eb07bc143")
                .setContentType(ContentType.JSON)
                .build()
        ;
    }

    @Test
    public void createComment() {
        String randomFullName = faker.name().fullName();
        String randomEmail = faker.internet().emailAddress();
        String randomBody = faker.job().title();

        Map<String, String> newComment = new HashMap<>();
        newComment.put("post_id", "82469");
        newComment.put("name", randomFullName);
        newComment.put("email", randomEmail);
        newComment.put("body", randomBody);

        commentID =
                given()
                        .spec(reqSpec)
                        .body(newComment)

                        .when()
                        .post("https://gorest.co.in/public/v2/comments")

                        .then()
                        .log().body()
                        .statusCode(201)
                        .contentType(ContentType.JSON)
                        .extract().path("id")
        ;

    }

    @Test(dependsOnMethods = "createComment")
    public void getCommentById() {

        given()
                .spec(reqSpec)

                .when()
                .get("" + commentID)

                .then()
                .log().body()
                .statusCode(200)
                .contentType(ContentType.JSON)
                .body("id", equalTo(commentID))
        ;
    }

    @Test(dependsOnMethods = "getCommentById")
    public void updateComment() {

        Map<String, String> newComment = new HashMap<>();
        newComment.put("body", "Update DONE");

        given()
                .spec(reqSpec)
                .body(newComment)

                .when()
                .put("" + commentID)

                .then()
                .log().body()
                .statusCode(200)
                .contentType(ContentType.JSON)
                .body("body", equalTo("Update DONE"))
        ;

    }

    @Test(dependsOnMethods = "updateComment")
    public void deleteComment() {

        given()
                .spec(reqSpec)

                .when()
                .delete("" + commentID)

                .then()
                .log().body()
                .statusCode(204)

        ;

    }

    @Test(dependsOnMethods = "deleteComment")
    public void deleteCommentNegative() {

        given()
                .spec(reqSpec)

                .when()
                .delete("" + commentID)

                .then()
                .log().body()
                .statusCode(404)
        ;

    }
}
