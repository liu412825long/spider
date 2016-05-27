package cn.com.test1;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.URL;
import java.net.URLConnection;
import java.util.Date;
import java.util.ArrayList;
import java.util.List;

public class Test1 {

	/**
	 * @param args
	 */
	public static void main(String[] args) throws Exception{
		// TODO Auto-generated method stub
		URL url=new URL("http://image.so.com/z?ch=beauty");//取得资源对象
		/*URLConnection uc=url.openConnection();//生成连接对象
		uc.setDoOutput(true);
		uc.connect(); //发出连接
*/		String temp;
		final StringBuffer sb = new StringBuffer();
		final BufferedReader in = new BufferedReader(new InputStreamReader(
		url.openStream(),"gbk"));
		while ((temp = in.readLine()) != null) {
		sb.append(temp);
		sb.append("\n");
		}
		in.close();
		writeFile(sb.toString());
		List<String> list=getTags(jiequ(sb.toString()));
		for(String s:list){
			//getLink(s);
			System.out.println(s);
			System.out.println(".................................................");
		}
	}
	
	public static List<String> getTags(List<String> list){
		for(int i=list.size()-1;i>=0;i--){
			if(list.get(i).contains("</")||list.get(i).contains("<!")){
				list.remove(i);
			}
		}
		return list;
	}
	
	public static void getLink(String str){
		if(!str.contains("</")){
			String str1=str.substring(0, str.indexOf(" "));
			System.out.println("tags:"+str1);
		}
	}
	
	public  static void writeFile(String str){
		File file=new File("D://file.html");
		try {
			FileWriter fw=new FileWriter(file);
			fw.write(str);
			fw.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public static List<String> jiequ(String str){
		List<String> list=new ArrayList<String>();
		int tem=0;
		for(int i=0;i<str.length();i++){
			if(String.valueOf(str.charAt(i)).equals("<")){
					tem=i;
			}else if(String.valueOf(str.charAt(i)).equals(">")){
				list.add(str.substring(tem, i+1));
				//System.out.print(str.substring(tem, i+1));
				//System.out.println();
			}
		}
		return list;
	}
	
	public static void downPic(String picUrl){
		
		try {
			URL url=new URL(picUrl);//取得资源对象
			URLConnection uc=url.openConnection();//生成连接对象
			uc.setDoOutput(true);
			uc.connect(); //发出连接
			InputStream inputStream=url.openStream();
			File file=new File("D:/downPic");
			if(!file.exists()){
				file.mkdir();
			}
			String fileType=picUrl.substring(picUrl.lastIndexOf("."));
			Date date=new Date();
			String fileName=date.getTime()+fileType;
			File outFile=new File(file,fileName);
			OutputStream output=new FileOutputStream(outFile);
			int len=0;
			while((len=inputStream.read())!=-1){
				output.write(len);
			}
			output.flush();
			output.close();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

}
