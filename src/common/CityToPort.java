package common;

import java.util.HashMap;
import java.util.Map;

public class CityToPort {

  static public Map<String, Integer> map = new HashMap<>();
  static {
    map.put("TOR", 6666);
    map.put("MTL", 7777);
    map.put("OTW", 8888);
  }

}
