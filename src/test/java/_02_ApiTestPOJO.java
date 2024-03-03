import Model.GoRestUser;
import Model.Location;
import Model.Place;
import Model.Task3POJO;
import io.restassured.http.ContentType;
import io.restassured.response.Response;
import org.testng.Assert;
import org.testng.annotations.Test;

import java.util.List;

import static io.restassured.RestAssured.*;
import static org.hamcrest.Matchers.equalTo;

public class _02_ApiTestPOJO {
    // PJO : JSON nesnesi : location nesnesi
    @Test
    public void extractJsonAll_POJO() {
        Location locationNesnesi =
                given()
                        .when()
                        .get("http://api.zippopotam.us/us/90210")

                        .then()
                        .extract().body().as(Location.class) // Location kalıbına göre
                ; // dönen body bilgisini Location Class kalıbıyla çevir.

        System.out.println(locationNesnesi.getCountry());
        System.out.println(locationNesnesi.getPlaces());
        System.out.println("locationNesnesi.getCountry() = " +
                locationNesnesi.getCountry());
        System.out.println("locationNesnesi.getPlaces() = " +
                locationNesnesi.getPlaces());

        for (Place p : locationNesnesi.getPlaces()) {
            System.out.println("p = " + p);
        }

        // JsonaDonustur(locationNesnesi); developer bu şekilde dönüştürmüştü
        // Json.Serialise(locationNesnesi); ben de tersine dSerialize yaptım.
        // yani NESNE yi elde ettim.

    }

    @Test
    public void soru() {
        // http://api.zippopotam.us/tr/01000
        // endpointinden dönen verilerden "Dörtağaç Köyü" ait bilgileri yazdırınız

        Location locationNesnesi =
                given()
                        .when()
                        .get("http://api.zippopotam.us/tr/01000")

                        .then()
                        .extract().body().as(Location.class);

        for (Place p : locationNesnesi.getPlaces()) {
            if (p.getPlacename().contains("Dörtağaç Köyü")) {
                System.out.println("p = " + p);
            }
        }
    }

    @Test
    public void task1() {
        /**
         Task 1
         create a request to https://jsonplaceholder.typicode.com/todos/2
         expect status 200
         expect content type JSON
         expect title in response body to be "quis ut nam facilis et officia qui"
         */

        given()
                .when()
                .get("https://jsonplaceholder.typicode.com/todos/2")

                .then()
                .statusCode(200)
                .contentType(ContentType.JSON)
                .body("title", equalTo("quis ut nam facilis et officia qui"))
        ;
    }

    @Test
    public void task2() {
        /**

         Task 2
         create a request to https://jsonplaceholder.typicode.com/todos/2
         expect status 200
         expect content type JSON
         expect response completed status to be false(hamcrest)
         extract completed field and testNG assertion(testNG)
         */
        given()
                .when()
                .get("https://jsonplaceholder.typicode.com/todos/2")

                .then()
                .statusCode(200)
                .contentType(ContentType.JSON)
                .body("completed", equalTo(false))
        ;

        boolean completedStatus =
                given()
                        .when()
                        .get("https://jsonplaceholder.typicode.com/todos/2")

                        .then()
                        .statusCode(200)
                        .contentType(ContentType.JSON)
                        .extract()
                        .path("completed");

        Assert.assertFalse(completedStatus, "False olmalı.");

    }

    @Test
    public void task3() {
        /** Task 3

         create a request to https://jsonplaceholder.typicode.com/todos/2
         expect status 200
         Converting Into POJO*/

        Task3POJO locObj =
                given()
                        .when()
                        .get("https://jsonplaceholder.typicode.com/todos/2")

                        .then()
                        .statusCode(200)
                        .extract().body().as(Task3POJO.class);

        System.out.println("locObj = " + locObj);

    }

    @Test
    public void extractingPath() {

        String postCode =
                given()
                        .when()
                        .get("https://jsonplaceholder.typicode.com/todos/2")

                        .then()
                        .extract().path("'post code'");

        System.out.println("postCode = " + postCode);
    }

    @Test
    public void extractingJSONPath() {

        int postCode =
                given()
                        .when()
                        .get("https://jsonplaceholder.typicode.com/todos/2")

                        .then()
                        .extract().jsonPath().getInt("'post code'");

        System.out.println("postCode = " + postCode);
    }

    @Test
    public void getZipCode() {

        Response response =
                given()
                        .when()
                        .get("http://api.zippopotam.us/tr/01000")

                        .then()
                        .extract().response();

        Location locationPathAs = response.as(Location.class);
        System.out.println("locationPathAs.getPlaces() = " + locationPathAs.getPlaces());

        List<Place> places = response.jsonPath().getList("places", Place.class);

        System.out.println("places = " + places); // nokta atışı istediğimiz nesneyi aldık

        // Daha önceki örneklerde (as) Class dönüşümleri için tüm yapıya karşılık gelen
        // gereken tüm classları yazarak dönüştürüp istediğimiz elemanlara ulaşıyorduk.

        // Burada ise(JsonPath) aradaki bir veriyi clasa dönüştürerek bir list olarak almamıza
        // imkan veren JSONPATH i kullandık.Böylece tek class ile veri alınmış oldu
        // diğer class lara gerek kalmadan

        // path : class veya tip dönüşümüne imkan veremeyen direk veriyi verir. List<String> gibi
        // jsonPath : class dönüşümüne ve tip dönüşümüne izin vererek , veriyi istediğimiz formatta verir.
    }



    @Test
    public void goRestPOJO() {
        // https://gorest.co.in/public/v1/users  endpointte dönen Sadece Data Kısmını POJO
        // dönüşümü ile alarak yazdırınız.

        List<GoRestUser> dataUsers =
                given()
                        .when()
                        .get("https://gorest.co.in/public/v1/users")

                        .then()
                        .extract().jsonPath().getList("data", GoRestUser.class);

        System.out.println("goRestPOJOList.get(0).getEmail() = " + dataUsers.get(0).getEmail());

        for (GoRestUser p : dataUsers){
            System.out.println("p = " + p);
        }
    }

}
