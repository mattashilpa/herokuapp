package com.intuit.rest.herokuapp.common;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;

import org.apache.commons.io.IOUtils;

public class HerokuappUtil {
	
	public static String getJsonInputFile(String fileName){
		String requestJson = "";
		if(fileName == null || fileName.equals(""))
		{
			throw new IllegalArgumentException( "Input Json File name to HerokuappUtil was null" );
		}else{
//			File requestFile = new File(fileName);
//			if(requestFile.exists()){
				//Get file from resources folder
				try{
					ClassLoader classLoader = HerokuappUtil.class.getClassLoader();
					requestJson = IOUtils.toString(classLoader.getResourceAsStream(fileName));
				}catch(FileNotFoundException fe){
					fe.printStackTrace();
				}catch(IOException e){
					e.printStackTrace();
				}
//			}else{
//				System.out.println("Request JSON file does not exist." + fileName);
//			}
		}
		return requestJson;
	}

	public static boolean isNotEmpty(String str){
		 return(str != null && !str.isEmpty());
	}
}
