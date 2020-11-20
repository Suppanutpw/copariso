import org.json.simple.JSONObject;

public class Main {

    public static void main(String[] args) {
        // new CoparisoClient();

        JSONObject obj = new JSONObject();

        obj.put("name", "Pankaj Kumar");
        obj.put("age", 32);

        System.out.print(obj.toJSONString());
    }

}
