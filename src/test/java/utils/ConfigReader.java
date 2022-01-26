package utils;


import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.util.Properties;
import java.io.FileReader;
import java.io.IOException;


//Implemetation of singleton design pattern
public class ConfigReader {
	
	private Properties properties;
	// The Static member holds a single instance of the ConfigReader class
	
	private static ConfigReader configReader;
	
	//A Private constructor  : ConfigReader prevents any other class from instantiating 	
	private ConfigReader() 
	{
		BufferedReader reader;
		 //String currentDirectory = System.getProperty("user.dir");
	      //System.out.println("The current working directory is " + currentDirectory);
		String configFilePath = System.getProperty("user.dir")+"/src/test/resources/configs/config.properties";
		
		//String propertiesFilePath = "C:\\Prasad\\eclipse-workspace\\RestassuredWithCucumber\\src\\test\\resources\\configs\\config.properties";
		
			try {
				reader = new BufferedReader(new FileReader(configFilePath));
				 properties = new Properties();
				 try
				 {
					 properties.load(reader);
				 } catch (IOException e) 
				 {
					e.printStackTrace();
				 }
			} 
			catch (FileNotFoundException e) 
			{
				
				e.printStackTrace();
				throw new RuntimeException("Configuration.properties not found at " + configFilePath);
			}
	}
	
	// A public static method returning an instance of the class , which will provide the global access
	public static ConfigReader getInstance()
	{
		if(configReader == null)
		{
			configReader = new ConfigReader();
		}
		return configReader;
	}

	public String getBaseUrl()
	{
		String baseUrl = properties.getProperty("base_url");
		if(baseUrl != null)
			return baseUrl;
		else
			throw new RuntimeException("base_Url not specified in the config.properties file.");
	}
	
	public String getUserName()
	{
		String userName = properties.getProperty("user_name");
		if(userName != null)
			return userName;
		else
			throw new RuntimeException("user_name not specified in the config.properties file.");
	}
	
	public String getpassword()
	{
		String passWord =  properties.getProperty("pass_word");
		if(passWord != null)
			return passWord;
		else
			throw new RuntimeException("pass_word not specified in the config.properties file.");
	}
}
