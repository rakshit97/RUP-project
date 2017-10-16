package example;

import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.LambdaLogger;
import com.amazonaws.services.lambda.runtime.RequestStreamHandler;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;

import java.io.*;
import java.util.logging.Logger;

public class Hello implements RequestStreamHandler
{
    JSONParser parser = new JSONParser();

    public void handleRequest(InputStream inputStream, OutputStream outputStream, Context context) throws IOException
    {

        LambdaLogger logger = context.getLogger();
        logger.log("Loading Java Lambda handler of ProxyWithStream");


        BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream));
        String name = "you";
        /*String city = "World";
        String time = "day";
        String day = null;*/
        String responseCode = "200";
        JSONObject responseJson = new JSONObject();

        try
        {
            JSONObject event = (JSONObject) parser.parse(reader);
            if (event.get("queryStringParameters") != null)
            {
                JSONObject qps = (JSONObject) event.get("queryStringParameters");
                logger.log("\nHas query params\n");
                if (qps.get("name") != null)
                {
                    name = (String) qps.get("name");
                }
            }

            /*if (event.get("pathParameters") != null)
            {
                JSONObject pps = (JSONObject) event.get("pathParameters");
                if (pps.get("proxy") != null)
                {
                    city = (String) pps.get("proxy");
                }
            }

            /*if (event.get("headers") != null)
            {
                JSONObject hps = (JSONObject) event.get("headers");
                if (hps.get("day") != null)
                {
                    day = (String) hps.get("day");
                }
            }

            if (event.get("body") != null)
            {
                JSONObject bod = (JSONObject) parser.parse((String) event.get("body"));
                if (bod.get("callerName") != null)
                {
                    name = (String) bod.get("callerName");
                }
            }

            String greeting = "Good " + time + ", " + name + " of " + city + ". ";
            if (day != null && !day.equals("")) greeting += "Happy " + day + '!';*/
            String greeting = "hello from " + name;


            JSONObject responseBody = new JSONObject();
            responseBody.put("message", greeting);

            JSONObject headerJson = new JSONObject();
            //headerJson.put("x-custom-response-header", "my custom response header value");

            responseJson.put("statusCode", responseCode);
            responseJson.put("headers", headerJson);
            responseJson.put("body", responseBody.toString());

        }
        catch (Exception pex)
        {
            responseJson.put("statusCode", "400");
            responseJson.put("exception", pex);
        }

        logger.log(responseJson.toJSONString());
        OutputStreamWriter writer = new OutputStreamWriter(outputStream, "UTF-8");
        writer.write(responseJson.toJSONString());
        writer.close();
    }
}
