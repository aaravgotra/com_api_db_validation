package utils;

import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import io.restassured.matcher.RestAssuredMatchers.*;
import io.restassured.path.json.JsonPath;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;
import org.hamcrest.Matchers.*;
import org.json.JSONObject;
import org.testng.Assert;

import java.io.File;
import java.util.Map;

import static io.restassured.module.jsv.JsonSchemaValidator.matchesJsonSchemaInClasspath;

public class ApiUtils {
    //Global Setup Variables


    public  RequestSpecification Request =RestAssured.given();
    public  String path;
    public  String method;
    public  String responseSchema;
    public  String body;
    public  int successStatusCode;
    public Response res;
    public  YamlReader apiconfig;
    public YamlReader testdataconfig;
    public static String jsonPathTerm;
    public String filepath=System.getProperty("user.dir") + File.separator + "src" + File.separator + "test" + File.separator + "resources" + File.separator + "CriticalApi.yaml";
    public String testdata=System.getProperty("user.dir") + File.separator + "src" + File.separator + "test" + File.separator + "resources" + File.separator+"testData" +File.separator + "testdata.yaml";
    public static String configFile = System.getProperty("user.dir") + File.separator + "configuration.properties";

    public ApiUtils createSession(String apiId){
        apiconfig = new YamlReader(filepath);
        this.method=apiconfig.get(apiId+".method");
        this.responseSchema=apiconfig.get(apiId+".responseSchema");
        this.path=apiconfig.get(apiId+".path");
        this.body=apiconfig.get(apiId+".body");
        try {
            this.successStatusCode = Integer.parseInt(apiconfig.get(apiId + ".successStatusCode"));
        }
        catch (Exception e){
            //success reponse code not found
            System.out.println("---");
        }
        System.out.println(this.path);
        System.out.println(this.method);
        return this ;

    }

    public   Response hit(){
        switch(method.toLowerCase()){
            case "get":
                this.res = getDefaultHttpRequestHeader().get(this.path);
                break;
            case "post":
                this.res = getDefaultHttpRequestHeader().post(this.path);
                break;
            case "put":
                this.res = getDefaultHttpRequestHeader().put() ;
                break;
            case "delete":
                this.res = getDefaultHttpRequestHeader().delete();
                break;

        }

        return this.res;
    }

    public  Map<String, Object> testdataprovider(String nodepath){
        testdataconfig = new YamlReader(testdata);
        return testdataconfig.getMap(nodepath);
    }
    //Sets query parameter
    public ApiUtils setQueryParam(String key, String value){
        Request.queryParam(key,value);
        return this;
    }
    public ApiUtils setBody(Map<String,Object> body){

        Request.body(new JSONObject(body).toString());
        return this;
    }


    public ApiUtils hasValidResponseSchema(){
        System.out.println(this.responseSchema);
       this.res.then().assertThat().body(matchesJsonSchemaInClasspath("response_schema/"+this.responseSchema+".json"));
       return this;
    }



    public ApiUtils hasValidStatusCode(){
        Assert.assertEquals(this.res.getStatusCode(),this.successStatusCode);
        return this;

    }
    public ApiUtils update_url(String key, String value) {
        this.path = this.path.replace("{" + key + "}", value);
        return this;
    }



   /// default request header
    private  RequestSpecification getDefaultHttpRequestHeader() {

        return Request.contentType("application/json\r\n").header("Accept","application/json")
                .header("Cache-Control", "no-cache").log().all();
    }



    public ApiUtils appendHeader(String key, String value) {
         Request.header(key,value);
         return this;
    }


    //Sets ContentType
    public  void setContentType (ContentType Type){

        Request.contentType(Type);
    }


    //Returns response
    public static Response getResponse() {
        return RestAssured.get();
    }

    //Returns JsonPath object
    public static JsonPath getJsonPath (Response res) {
        String json = res.asString();
        //System.out.print("returned json: " + json +"\n");
        return new JsonPath(json);
    }
}
