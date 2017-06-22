package basketball;

import java.io.FileOutputStream;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import java.util.TreeMap;

import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.CreationHelper;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;

public class Stat {
	
	//球员
	private final static Map<String, String> PLAYER = new HashMap<String, String>();
	static {
		PLAYER.put("1", "雨哥");
		PLAYER.put("2", "白哥");
		PLAYER.put("3", "何哥");
		PLAYER.put("q", "琳哥");
		PLAYER.put("w", "诺哥");
		PLAYER.put("e", "华哥");
	}
	
	//行为
	//OPTIMIZE: 定义新数据结构，3项关联
	private final static Map<Integer,String> MANI = new TreeMap<Integer,String>();
	static {
		MANI.put(1, "i");//得分
		MANI.put(2, "o");//投失
		MANI.put(3, "r");//进攻篮板
		MANI.put(4, "d");//防守篮板
		MANI.put(5, "a");//助攻
		MANI.put(6, "p");//传球
		MANI.put(7, "s");//抢断
		MANI.put(8, "b");//封盖
		MANI.put(9, "t");//失误
		MANI.put(10, "f");//犯规
	}
	
	//(人+行为)编码
	private final static StringBuilder CODE = new StringBuilder("3pwo1d1pqoqrqoqrqp1i");
	
	//对阵情况
	private final static String GROUP = "3we:12q"; //何哥诺哥华哥:雨哥白哥琳哥；必须有分号，因为可能有3对2的情况
	
	public static void main(String[] args) {

		//碰到进球，需要修改前一个传球为助攻
		boolean fix = false;
		for(int i = CODE.length() - 1; i > 0; i=i-2){
			if(CODE.charAt(i) == 'i'){
				fix = true;
				continue;
			}
			if(fix){
				CODE.setCharAt(i, 'a');
				fix = false;
			}
		}
		
		
		//编码转map
		Map<String, Integer> statMap = new TreeMap<String, Integer>(); //TreeMap有序，好做Excel
		for(int i = 0 ; i < CODE.length() - 1; i=i+2){
			String key = CODE.substring(i, i+2);
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
		initSheet(statMap);
		
		System.out.println(CODE.toString());
		
	}
	
	private static void initSheet(Map<String, Integer> statMap){
		Workbook wb = new HSSFWorkbook();
	    Sheet sheet = wb.createSheet("第一拨");
	    int offset_row = 2;//留空间，好看；做成变量，省得麻烦

	    //行头
	    Row row = sheet.createRow(0);
	    Cell cell = row.createCell(offset_row);
	    cell.setCellValue("姓名");
	    cell = row.createCell(offset_row+1);
	    cell.setCellValue("得分");
	    cell = row.createCell(offset_row+2);
	    cell.setCellValue("投失");
	    cell = row.createCell(offset_row+3);
	    cell.setCellValue("进攻篮板");
	    cell = row.createCell(offset_row+4);
	    cell.setCellValue("防守篮板");
	    cell = row.createCell(offset_row+5);
	    cell.setCellValue("助攻");
	    cell = row.createCell(offset_row+6);
	    cell.setCellValue("传球");
	    cell = row.createCell(offset_row+7);
	    cell.setCellValue("抢断");
	    cell = row.createCell(offset_row+8);
	    cell.setCellValue("封盖");
	    cell = row.createCell(offset_row+9);
	    cell.setCellValue("失误");
	    cell = row.createCell(offset_row+10);
	    cell.setCellValue("犯规");
	    cell = row.createCell(offset_row+11);
	    cell.setCellValue("失误原因");
	    
	    //数据
	    for(int i = 0; i < GROUP.length(); i++){
	    		Row playerRow = sheet.createRow(i+1);

	    		String player = String.valueOf((GROUP.charAt(i)));
	    		if(player.equals(":")){
	    			continue;
	    		}
	    		
	    	    cell = playerRow.createCell(offset_row);//姓名
	    	    cell.setCellValue(PLAYER.get(player));
	    	    
	    	    //*****此处有一点需要注意，MANI的顺序必须和列顺序一致，这也是定义三相关联的数据结构的必要性*****
	    	    Set<Entry<Integer, String>> entry = MANI.entrySet();
	    		Iterator<Entry<Integer, String>> ite = entry.iterator();
	    		while (ite.hasNext()) {
	    			Entry<Integer, String> e = ite.next();
	    			
	    			cell = playerRow.createCell(offset_row+e.getKey());
	    			cell.setCellValue(statMap.get(player+e.getValue()) == null ? 0 : statMap.get(player+e.getValue()));
	    		}
	    }


	    try {
	    		FileOutputStream fileOut = new FileOutputStream("workbook.xls");
		    wb.write(fileOut);
		    fileOut.close();
		    wb.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	
}
