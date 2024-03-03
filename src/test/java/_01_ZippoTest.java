import io.restassured.builder.RequestSpecBuilder;
import io.restassured.builder.ResponseSpecBuilder;
import io.restassured.filter.log.LogDetail;
import io.restassured.http.ContentType;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;
import io.restassured.specification.ResponseSpecification;
import org.testng.Assert;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import java.util.List;

import static io.restassured.RestAssured.*;
import static org.hamcrest.Matchers.*;

public class _01_ZippoTest {

    @Test
    public void test1() {

        given()
                // Hazırlık işlemleri kodları
                .when()
                // endpoint(URL), metod u verip istek gönderiliyor

                .then()
                // assertion, test, data işlemleri
        ;

    }

    @Test
    public void statusCodeTest() {

        given()
                // Hazırlık kısmı boş
                .when()
                .get("http://api.zippopotam.us/us/90210")

                .then()
                .log().body()
                .statusCode(200)
                .contentType(ContentType.JSON) // dönen datanın tipi JSON mı
        ;

    }

    @Test
    public void test3() {
        given()

                .when()
                .get("http://api.zippopotam.us/us/90210")

                .then()
                .log().body()
                .statusCode(200)
                .body("country", equalTo("United States")) // assertion
        ;

    }

    // Soru : "http://api.zippopotam.us/us/90210"  endpoint indne dönen
    // place dizisinin ilk elemanının state değerinin  "California"
    // olduğunu doğrulayınız
    @Test
    public void test4() {
        given()

                .when()
                .get("http://api.zippopotam.us/us/90210")

                .then()
                .log().body()
                .statusCode(200)
                .body("places[0].state", equalTo("California"))
        ;

    }

    @Test
    public void test5() {
        given()

                .when()
                .get("http://api.zippopotam.us/tr/01000")

                .then()
                .log().body()
                .statusCode(200)
                .body("places.'place name'", hasItem("Dörtağaç Köyü"))
        ;

    }

    @Test
    public void test6() {
        // Soru : "http://api.zippopotam.us/us/90210"  endpoint in dönen
        // place dizisinin dizi uzunluğunun 1 olduğunu doğrulayınız.
        given()

                .when()
                .get("http://api.zippopotam.us/us/90210")

                .then()
                .log().body()
                .statusCode(200)
                .body("places", hasSize(1))
        ;

    }

    @Test
    public void combiningTest() {
        given()

                .when()
                .get("http://api.zippopotam.us/us/90210")

                .then()
                .statusCode(200)
                .body("places", hasSize(1))
                .body("places[0].state", equalTo("California"))
                .body("places.'place name'", hasItem("Beverly Hills"))
        ;
    }

    @Test
    public void pathParamTest() {
        given()
                .pathParam("ulke", "us")
                .pathParam("postaKodu", 90210)
                .log().uri() // request link çalışmadan önceki hali
                .when()
                .get("http://api.zippopotam.us/{ulke}/{postaKodu}")

                .then()
        ;

    }

    @Test
    public void queryParamTest() {
        // https://gorest.co.in/public/v1/users?page=3
        given()
                .param("page", 1)
                .log().uri()
                .when()
                .get("https://gorest.co.in/public/v1/users")

                .then()
                .statusCode(200)
                .log().body()
        ;
    }


    @Test
    public void requestResponseSpecification() {

        for (int i = 1; i <= 10; i++) {
            given()
                    .param("page", i)
                    .log().uri()
                    .when()
                    .get("https://gorest.co.in/public/v1/users")

                    .then()
                    .statusCode(200)
                    .body("meta.pagination.page", equalTo(i))
            ;
        }
    }

    RequestSpecification requestSpec;
    ResponseSpecification responseSpec;

    @BeforeClass
    public void setup() {
        baseURI = "https://gorest.co.in/public/v1";
        requestSpec = new RequestSpecBuilder()
                .setContentType(ContentType.JSON)
                .log(LogDetail.URI) // log().uri()
                .build();

        responseSpec = new ResponseSpecBuilder()
                .expectStatusCode(200)
                .log(LogDetail.BODY)
                .expectContentType(ContentType.JSON)
                .build();
    }

    @Test
    public void queryParamTest1() {
        // https://gorest.co.in/public/v1/users?page=3

        given()
                .param("page", 1)
                .spec(requestSpec)

                .when()
                .get("/users") // http yok ise baseURI baş tarafına gelir

                .then()
                .spec(responseSpec)
        ;
    }

    @Test
    public void queryParamTest2() {
        //    SORGU İÇERİSİNDEN BİR VERİ ÇIKARTMA
        String countryName =
                given()
                        .when()
                        .get("http://api.zippopotam.us/us/90210")

                        .then()
                        .extract().path("country") // PATH i country olan değeri EXTRACT yap.
                ;
        System.out.println("countryName = " + countryName);
        Assert.assertEquals(countryName, "United States");
    }

    @Test
    public void soru1() {
        //    SORGU İÇERİSİNDEN BİR VERİ ÇIKARTMA
        // Soru : "http://api.zippopotam.us/us/90210"  endpoint indne dönen
        // place dizisinin ilk elemanının state değerinin  "California"
        // olduğunu testNG Assertion ile doğrulayınız
        String stateName =
                given()
                        .when()
                        .get("http://api.zippopotam.us/us/90210")

                        .then()
                        .extract().path("places[0].state");
        System.out.println("stateName = " + stateName);
        Assert.assertEquals(stateName, "California");
    }

    @Test
    public void soru2() {
        // Soru : "http://api.zippopotam.us/us/90210"  endpoint indne dönen
        // place dizisinin ilk elemanının place name değerinin  "Beverly Hills"
        // olduğunu testNG Assertion ile doğrulayınız
        String placeName =
                given()
                        .when()
                        .get("http://api.zippopotam.us/us/90210")

                        .then()
                        .extract().path("places[0].'place name'");
        System.out.println("placeName = " + placeName);
        Assert.assertEquals(placeName, "Beverly Hills");
    }


    @Test
    public void soru3() {
        // Soru : "https://gorest.co.in/public/v1/users"  endpoint in den dönen
        // limit bilgisinin 10 olduğunu testNG ile doğrulayınız.
        int limit =
                given()
                        .when()
                        .get("/users")

                        .then()
                        .extract().path("meta.pagination.limit");
        System.out.println("limit = " + limit);
        Assert.assertEquals(limit, 10);
    }

    @Test
    public void extractionJsonPath1() {
        List<Integer> idler =
                given()
                        .when()
                        .get("https://gorest.co.in/public/v1/users")

                        .then()
                        .extract().path("data.id");

        System.out.println("idler = " + idler);
    }

    @Test
    public void extractionJsonPath2() {
        List<String> names =
                given()
                        .when()
                        .get("https://gorest.co.in/public/v1/users")

                        .then()
                        .extract().path("data.name");

        System.out.println("names = " + names);
    }

    @Test
    public void extractingJsonPathResponsAll() {
        Response donenData =
                given()
                        .when()
                        .get("https://gorest.co.in/public/v1/users")

                        .then()
                        .extract().response();

        List<Integer> idler = donenData.path("data.id");
        List<String> isimler = donenData.path("data.name");
        int limit = donenData.path("meta.pagination.limit");

        System.out.println("idler = " + idler);
        System.out.println("isimler = " + isimler);
        System.out.println("limit = " + limit);

        Assert.assertTrue(isimler.contains("Mahesh Menon"));
        Assert.assertTrue(idler.contains(5599126));
        Assert.assertTrue(limit==10);

    }

}
