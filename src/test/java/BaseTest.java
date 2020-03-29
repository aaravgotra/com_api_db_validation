import io.restassured.RestAssured;
import io.restassured.path.json.JsonPath;
import io.restassured.response.Response;
import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import utils.ApiUtils;
import utils.ReadWritePropertyFile;
import utils.YamlReader;

import java.io.File;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.Statement;

public class BaseTest {
    public Response res = null; //Response
    public JsonPath jp = null; //JsonPath
    public String env= ReadWritePropertyFile.getProperty("env", ApiUtils.configFile).toString();
    public String testdata=System.getProperty("user.dir") + File.separator + "src" + File.separator + "test" + File.separator + "resources" + File.separator+"testData" +File.separator + "testdata.yaml";
    public YamlReader testdataconfig = new YamlReader(testdata);

    // Connection object
    static Connection con = null;
    // Statement object
    public static Statement stmt;
    // Constant for Database URL
    public static String DB_URL = "jdbc:mysql://localhost:3306/prod";  // your db name
    // Constant for Database Username
    public static String DB_USER = "root";  // your user name
    // Constant for Database Password
    public static String DB_PASSWORD = "aarav1234";  // your db password

    @BeforeClass
    public void setup (){


        //database setup
        try{
            // Make the database connection
            String dbClass = "com.mysql.jdbc.Driver";
            Class.forName(dbClass).newInstance();
            // Get connection to DB
            Connection con = DriverManager.getConnection(DB_URL, DB_USER, DB_PASSWORD);
            // Statement object to send the SQL statement to the Database
            stmt = con.createStatement();
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }



        if (env.equalsIgnoreCase("test") ){
            RestAssured.baseURI = "https://restcountries.eu";
        }
        else{
            RestAssured.baseURI ="https://restcountries.eu";
        }
    }

    @AfterClass
    public void afterTest (){
        RestAssured.baseURI=null;
        RestAssured.basePath=null;
    }
}
