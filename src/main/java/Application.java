import com.google.gson.Gson;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.Map;

public class Application {

    public static void main(String[] args) throws FileNotFoundException {
        Gson gson = new Gson();

        Map<String, ArrayList<Double>> map = gson.fromJson(new FileReader(args[0]), Map.class);
        System.out.println(map);
    }
}
