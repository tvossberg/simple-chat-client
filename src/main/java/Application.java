import com.google.gson.Gson;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.util.*;
import java.util.stream.Collectors;

public class Application {

    public static void main(String[] args) throws FileNotFoundException {
        Gson gson = new Gson();

        Map<String, ArrayList<Double>> map = gson.fromJson(new FileReader(args[0]), Map.class);
        System.out.println(map);

        Map<Long, Set<Long>> realMap = new HashMap<>();
        for(Map.Entry<String,ArrayList<Double>> entry: map.entrySet()){
            Set<Long> valueSet= entry.getValue().stream().map(Double::longValue).collect(Collectors.toSet());
            realMap.put(Long.parseLong(entry.getKey()),valueSet);
        }
        System.out.println(realMap);
    }
}
