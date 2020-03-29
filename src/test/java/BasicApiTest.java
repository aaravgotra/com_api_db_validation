import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;
import utils.ApiUtils;

import java.sql.ResultSet;
import java.util.Map;

public class BasicApiTest extends BaseTest {

    public ApiUtils session ;



    @BeforeMethod
    public void setupmethod(){
        session = new ApiUtils();
    }

    @AfterMethod
    public void teardownmethod(){
        session = null;
    }

    @Test
    public void T01_To_Check_db_api_data() {
        try{
            String query = "select * from countries";
            // Get the contents of userinfo table from DB
            ResultSet db_data = stmt.executeQuery(query);
            // Print the result untill all the records are printed
            // res.next() returns true if there is any next record else returns false
            while (db_data.next())
            {
                System.out.print(db_data.getString(1));
                System.out.print("\t" + db_data.getString(2));
                System.out.print("\t" + db_data.getString(3));
                System.out.println("\t" + db_data.getString(4));
            }
        }
        catch(Exception e)
        {
            e.printStackTrace();
        }

        res=session.createSession("api.countryapi")
                .hit();
        System.out.println(res.asString());
       // session.hasValidStatusCode();       to check valid status code
       // session.hasValidResponseSchema();    to check response schema
// res used for API data and db_data used for db data
    }


    }


