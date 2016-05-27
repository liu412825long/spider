package cn.com.test1;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;


public class DownLoadPic {
	
	private static Map<String,String> tagsMap=new HashMap<String,String>();
	private static String pathRoot="http://www.5442.com";

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		//http://gb.wallpapersking.com/images/down9999.gif
		initTagsMap();
		// TODO Auto-generated method stub
			String pageContent=pageContent("http://www.5442.com/tag/fengguang.html");
			System.out.println(pageContent);
			writeFile(pageContent);
			pageContent=pageContent.toLowerCase();
			List<String> tags=getTags(pageContent);
			System.out.println(tags);
			for(String s:tags){
				String url=getURL(s);
				downPic(url);
				System.out.println(url);
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
				int start=tags.indexOf(tagValue)+tagValue.length()+1;
				String str=tags.substring(start);
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
	
	
	

	/**
	 * 检索网页中的某个标签
	 * @param str 网页中的内容
	 * @return List<String> 标签的集合对象
	 */
	public static List<String> getTags(String str){
		List<String> tagList=new ArrayList<String>();
		String tag="<img";
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

	/**
	 * 得到网页的内容
	 * @param pageUrl 网页的URL
	 * @return String 网页的内容
	 */
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
			//通过某种编码解析网页内容
			br = new BufferedReader(new InputStreamReader(inputStream, "utf-8"));
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
			// 关闭输入输出流
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

	/**
	 * @param picUrl  图片的URL地址
	 * @return String  本地图片的文件名
	 */
	public static String  downPic(String picUrl) {
		if(picUrl==null||picUrl.equals("")){
			return null;
		}
		//获得文件的类型
		String fileType = picUrl.substring(picUrl.lastIndexOf("."));
		String fileName=null;
		//创建本地下载文件夹
		File folder = new File("D:/downPic");
		if (!folder.exists()) {
			folder.mkdir();
		}
		InputStream inputStream = null;
		OutputStream outputStream = null;
		try {
			//获取资源对象
			URL url = new URL(picUrl);
			//生成资源连接对象
			URLConnection conn = url.openConnection();
			//将使用URL连接进行输出设置为true
			conn.setDoOutput(true);
			//设定超时时间，以毫秒为单位
			conn.setConnectTimeout(5000);
			//开启输入流
			inputStream = url.openStream();
			 fileName = new Date().getTime() + fileType;
			File outFile = new File(folder, fileName);
			//创建本地输出流对象
			outputStream = new FileOutputStream(outFile);
			int len = 0;
			//下载文件
			while ((len = inputStream.read()) != -1) {
				outputStream.write(len);
			}
			//刷新输出流 对象
			outputStream.flush();
		} catch (MalformedURLException e) {
			System.out.println("URL地址解析错误!!!");
			e.printStackTrace();
		} catch (IOException e) {
			System.out.println("打开连接或者IO流错误!!!");
			e.printStackTrace();
		} finally {
			//关闭输入输出流
			try {
				if (inputStream != null) {
					inputStream.close();
				}
				if (outputStream != null) {
					outputStream.close();
				}
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		return fileName;
	}
	

	public  static void writeFile(String str){
		File folder=new File("D:/page");
		if(!folder.exists()){
			folder.mkdir();
		}
		File file=new File(folder,"picTest.html");
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
