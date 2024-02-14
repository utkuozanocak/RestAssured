package tests;

import io.qameta.allure.restassured.AllureRestAssured;
import io.restassured.RestAssured;
import io.restassured.path.json.JsonPath;
import io.restassured.response.Response;
import org.junit.Assert;
import org.junit.Test;

import utils.Configuration;
import java.util.HashMap;
import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.equalTo;

import io.qameta.allure.*;

@Epic("REST API Regression Testing using JUnit4")
@Feature("Verify CRUID Operations on Employee module")
public class ApiTests {
    protected static Configuration config = Configuration.getInstance();
    static String url = config.getUrl();
    private static final String API_URL = url;
    private String respBookingId;

    @Test
    public void newBookingFirstNameControlApiTest() {
        System.out.println("newBookingFirstNameControlApiTest Başladı...");
        RestAssured.baseURI = API_URL;
        HashMap<String, Object> requestMap = getNewBookRequestMap();
        Response response = given()
                .filter(new AllureRestAssured())
                .contentType("application/json")
                .body(requestMap)
                .expect()
                .body("booking.firstname",equalTo("test"))
                .when()
                .post("/booking")
                .then()
                .statusCode(200)
                .extract()
                .response();
        String responseBody = response.getBody().asString();
        JsonPath jsonPath = new JsonPath(responseBody);
        setBookingId(jsonPath.getString("bookingid"));
        Assert.assertEquals("test", jsonPath.getString("booking.firstname"));
        System.out.println("newBookingFirstNameControlApiTest Tamamlandı...");
    }

    @Test
    public void deleteBookingWithBookingId() {
        newBookingFirstNameControlApiTest();
        System.out.println("deleteBookingWithBookingId Başladı...");
        RestAssured.baseURI = API_URL;
        HashMap<String, Object> requestMap = getAuthRequestMap();
        Response responseCookie = given()
                .filter(new AllureRestAssured())
                .contentType("application/json")
                .body(requestMap)
                .expect()
                .when()
                .post("/auth")
                .then()
                .statusCode(200)
                .extract()
                .response();
        String responseBodyCookie = responseCookie.getBody().asString();
        JsonPath jsonPathCookie = new JsonPath(responseBodyCookie);
        String token = jsonPathCookie.getString("token");

        Response responseDeleteBooking = given()
                .filter(new AllureRestAssured())
                .contentType("application/json")
                .cookie("token", token)
                .when()
                .delete("/booking/" + getBookingId())
                .then()
                .statusCode(201)
                .extract()
                .response();
        Assert.assertEquals("Created", responseDeleteBooking.getBody().asString());

        Response responseGetBooking = given()
                .contentType("application/json")
                .filter(new AllureRestAssured())
                .when()
                .get("/booking/" + getBookingId())
                .then()
                .statusCode(404)
                .extract()
                .response();
        Assert.assertEquals("Not Found",responseGetBooking.getBody().asString());
        System.out.println("deleteBookingWithBookingId Tamamlandı...");
    }

    private static HashMap<String, Object> getNewBookRequestMap() {
        HashMap<String, Object> requestMap = new HashMap<>();
        requestMap.put("firstname", "test");
        requestMap.put("lastname", "test");
        requestMap.put("totalprice", 111);
        requestMap.put("depositpaid", true);
        HashMap<String, String> bookingDatesMap = new HashMap<>();
        bookingDatesMap.put("checkin", "2018-01-01");
        bookingDatesMap.put("checkout", "2019-01-01");
        requestMap.put("bookingdates", bookingDatesMap);
        requestMap.put("additionalneeds", "Breakfast");
        return requestMap;
    }

    private static HashMap<String, Object> getAuthRequestMap() {
        HashMap<String, Object> requestMap = new HashMap<>();
        requestMap.put("username", "admin");
        requestMap.put("password", "password123");
        return requestMap;
    }

    public String getBookingId() {
        return respBookingId;
    }

    public void setBookingId(String bookingid) {
        this.respBookingId = bookingid;
    }
}
