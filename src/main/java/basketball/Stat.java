package basketball;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

public class Stat {
	public static void main(String[] args) {

		StringBuilder code = new StringBuilder("3pwo1d1pqoqrqoqrqp1i");

		//碰到进球，需要修改前一个传球为助攻
		boolean fix = false;
		for(int i = code.length() - 1; i > 0; i=i-2){
			if(code.charAt(i) == 'i'){
				fix = true;
				continue;
			}
			if(fix){
				code.setCharAt(i, 'a');
				fix = false;
			}
		}
		
		
		//编码转map
		Map<String, Integer> statMap = new HashMap<String, Integer>();
		for(int i = 0 ; i < code.length() - 1; i=i+2){
			String key = code.substring(i, i+2);
			System.out.println(key);
			
			Integer val = statMap.put(key, 1);
			
			if(val != null){
				statMap.put(key, val+1);
			}
			
		}
		
		Set<Entry<String, Integer>> entry = statMap.entrySet();
		Iterator<Entry<String, Integer>> ite = entry.iterator();
		while (ite.hasNext()) {
			Entry<String, Integer> e = ite.next();
			System.out.println(e.getKey() + ":" + e.getValue());
		}
		
		
		//输出到Excel
		
		
		
		
		
		
		System.out.println(code.toString());
		
	}
}
