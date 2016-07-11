package main;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;

import java.util.Map;

import org.eclipse.emf.ecore.EPackage;
import org.eclipse.emf.ecore.EcorePackage;

public class TextDeserializer 
{
	private PersistenceManager persistenceManager;
	private String fileLocation;
	
	public TextDeserializer(PersistenceManager persistenceManager)
	{
		this.persistenceManager = persistenceManager;
	}
	
	
	public void load(Map<?,?> options) throws IOException
	{
		fileLocation = (String)options.get("FILE_LOCATION");
		
		BufferedReader br = new BufferedReader(
				new InputStreamReader(new FileInputStream(fileLocation), persistenceManager.TEXT_ENCODING));
		
		String line;
		while((line = br.readLine()) != null)
		{
			//System.out.println(line);
		}
		br.close();
		
		loadMetamodel("dfd");
		
	}
	
	
	private EPackage loadMetamodel(String metamodelURI)
	{
		EPackage ePackage = null;
		System.out.println(EcorePackage.eNS_URI);
		
		return ePackage;
		
	}
}