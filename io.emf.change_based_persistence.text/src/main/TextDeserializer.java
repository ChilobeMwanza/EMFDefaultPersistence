package main;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;

import java.util.Map;
import java.util.StringTokenizer;

import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.EPackage;
import org.eclipse.emf.ecore.EcoreFactory;
import org.eclipse.emf.ecore.EcorePackage;

public class TextDeserializer 
{
	private enum EventType
	{
		NAMESPACEURI,
		ADD,
		NULL;
	}
	private final String RESOURCE_NAME = "DeltaResourceImpl";
	private PersistenceManager persistenceManager;
	//private String fileLocation;
	
	public TextDeserializer(PersistenceManager persistenceManager)
	{
		this.persistenceManager = persistenceManager;
	}
	
	public void load(Map<?,?> options) throws IOException
	{
		//fileLocation = (String)options.get("FILE_LOCATION");
		
		BufferedReader br = new BufferedReader(
				new InputStreamReader(new FileInputStream(persistenceManager.getURI().path()), persistenceManager.TEXT_ENCODING));
		
		String line;
		EPackage ePackage = null;
		
		
		while((line = br.readLine()) != null)
		{
			
			
			System.out.println(line);
			StringTokenizer st = new StringTokenizer(line);
			
			EventType eventType = EventType.NULL;
			
			if(st.hasMoreElements())
			{ 
				  eventType = EventType.valueOf
						 (st.nextElement().toString());
			} 
			
			/* Switches over various event types, calls appropriate handler method*/
			switch(eventType)
			{
			case NULL:
				break;
			case NAMESPACEURI : 
				ePackage = loadMetamodel(getNthWord(line,3));
				break;
			case ADD:
				handleAddEvent(ePackage,line);
				break;
			default:
				break;
			}	
		}
		br.close();
		
		loadMetamodel("dfd");
	}
	
	private EPackage loadMetamodel(String metamodelURI)
	{
		//EPackage ePackage = EPackage.Registry.INSTANCE.getEPackage("http://io.emf.change_based_persistence.text");
		EPackage ePackage = null;
		if(EPackage.Registry.INSTANCE.containsKey(metamodelURI))
			ePackage = EPackage.Registry.INSTANCE.getEPackage(metamodelURI);
		return ePackage;
	}
	
	private void handleAddEvent(EPackage p,String line)
	{
		if(getNthWord(line,4).equals(RESOURCE_NAME)) //add object directly to resource
		{
			EObject eObject = p.getEFactoryInstance().create((EClass) p.getEClassifier(getNthWord(line,2)));
			persistenceManager.addObjectToContents(eObject);
		}
	}
	
	private String getNthWord(String input, int n)
	{
		String [] stringArray = input.split(" ");
		if(n-1 < stringArray.length)
			return stringArray[n-1];
		return null;
	}
}
