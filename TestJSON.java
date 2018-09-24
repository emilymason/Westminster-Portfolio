
import org.json.simple.JSONObject;
import org.json.simple.JSONArray;
import org.json.simple.parser.ParseException;
import org.json.simple.parser.JSONParser;

public class TestJSON{

    public static void main(String[] args){
        JSONObject obj = new JSONObject();

          obj.put("name", "Emily");
        var par = JSON.parse(obj);
        if(obj.name.equals("Emily")){
            System.out.print("Name check");
        }
    }

}