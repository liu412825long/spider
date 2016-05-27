package cn.com.test1;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * @author HaiLong
 *
 */
public class DownPage {

	private static Map<String,String> tagsMap=new HashMap<String,String>();
	private static String pathRoot="http://www.5442.com";
	private static List<String> urlList=new ArrayList<String>();

	
	/**
	 * @param args
	 */
	public static void main(String[] args) {
		initTagsMap();
		// TODO Auto-generated method stub
		String sb=pageContent("http://www.5442.com/tag/fengguang.html");
		List<String> tagList=getTags(sb);
		//System.out.println(tagList);
		for(int i=0;i<tagList.size();i++){
			String str=tagList.get(i);
			String s=getURL(str);
			System.out.println(s);
			if(s!=null&&s.contains("http://")){
				if(s.contains("http://www.5442.com")){
					String page=pageContent(s);
					writeFile(page,s);
					if(!urlList.contains(s)){
						urlList.add(s);
					}
				}
			}
		}
	}
	
	
	public static void getUrlList(String url){
		String sb=pageContent(url);
		List<String> tagList=getTags(sb);
		//System.out.println(tagList);
		for(int i=0;i<tagList.size();i++){
			String str=tagList.get(i);
			String s=getURL(str);
			if(s!=null&&s.contains("http://")){
				if(s.contains("http://www.5442.com")){
					//String page=pageContent(s);
					//writeFile(page,s);
					if(!urlList.contains(s)){
						urlList.add(s);
					}
				}
			}
		}
		
	}
	
	 
	
	
	
	
	
	/**
	 * 初始化标签Map 
	 */
	public static void initTagsMap(){
		tagsMap.put("<a", "href");
		tagsMap.put("<img", "src");
		tagsMap.put("<link", "href");
		tagsMap.put("<script", "src");
		tagsMap.put("<form", "action");
	}
	
	
	/**
	 * 根据不同的标签得到相应的链接 
	 * @param tags 完整的标签
	 * @return String 一个标签所链接的地址
	 */
	public static String getURL(String tags){
		Set<String> set=tagsMap.keySet();
		Iterator<String> iter=set.iterator();
		String url=null;
		while(iter.hasNext()){
			String tagKey=iter.next();
			if(tags.startsWith(tagKey)){
				String tagValue=tagsMap.get(tagKey);
				int start=0;
				String str=null;
				if(tags.contains(tagValue)){
					  start=tags.indexOf(tagValue)+tagValue.length()+1;
					  str=tags.substring(start);
				}else{
					break;
				}
				int end=0;
				if(str.contains(" ")){
					if(str.startsWith("'")){
						start=start+1;
						 end=tags.indexOf("'",start);
					}else if(str.startsWith("\"")){
						start=start+1;
						end=tags.indexOf("\"",start);
					}else{
						end=tags.indexOf(" ",start);
					}
				}else{
					
					if(str.startsWith("'")){
						start=start+1;
						 end=tags.indexOf("'",start);
					}else if(str.startsWith("\"")){
						start=start+1;
						end=tags.indexOf("\"",start);
					}else{
						end=tags.indexOf(">",start);
					}
				}
				url=tags.substring(start, end);
				break;
			}
		}
		return combatUrl(url);
	}
	
	/**
	 * 拼装连接的绝对地址
	 * @param url
	 * @return
	 */
	public static String combatUrl(String url){
		if(url==null){
			return null;
		}else if(url.startsWith("//")){
			return ""+url;
		}else if(url.startsWith("/")){
			return pathRoot+url;
		}else{
			return url;
		}
	}
	
	public static List<String> getTags(String str){
		List<String> tagList=new ArrayList<String>();
		String tag="<a";
		int i=0;
		while(i<str.length()){
		if(str.contains(tag)){
			String s=str.substring(str.indexOf(tag),str.indexOf(">", str.indexOf(tag))+1);
			tagList.add(s);
		}else{
			break;
		}
		i=str.indexOf(">", str.indexOf(tag))+1;
		str=str.substring(i);
		i=0;
		}
		return tagList;
	}

	public static String pageContent(String pageUrl) {
		InputStream inputStream = null;
		BufferedReader br = null;
		StringBuffer sb =null;
		try {
			URL url = new URL(pageUrl);
			URLConnection conn = url.openConnection();
			conn.setDoOutput(true);
			conn.connect();
			inputStream = url.openStream();
			br = new BufferedReader(new InputStreamReader(inputStream, "gbk"));
			sb= new StringBuffer(); 
			String str = null;
			while ((str = br.readLine()) != null) {
				sb.append(str);
				sb.append("\n");
			}
			//System.out.println(sb.toString());
		} catch (MalformedURLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} finally {
			try {
				if (br != null) {
					br.close();
				}
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		return sb.toString();
	}

	

	public  static void writeFile(String str,String url){
		File folder=new File("D:/page");
		if(!folder.exists()){
			folder.mkdir();
		}
		String fileName=null;
		//http://www.5442.com/tag/
		if(url.contains(".")){
			try {
				fileName=url.substring(url.lastIndexOf("/"),url.lastIndexOf("."));
			} catch (Exception e) {
				// TODO Auto-generated catch block
				fileName="index";
				//e.printStackTrace();
			}
		}else{
			return;
		}
		File file=new File(folder,fileName+".html");
		try {
			BufferedWriter bw=new BufferedWriter(new OutputStreamWriter(new FileOutputStream(file),"utf-8"));
			bw.write(str);
			bw.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
