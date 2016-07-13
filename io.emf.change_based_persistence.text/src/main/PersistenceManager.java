
//todo: specify encoding ?
//http://www.javapractices.com/topic/TopicAction.do?Id=31
package main;

import java.io.BufferedWriter;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.nio.charset.Charset;
import java.util.List;
import java.util.Map;

import org.eclipse.emf.common.util.URI;

import io.emf.cbp.resource.DeltaResourceImpl;
import io.emf.cbp.text.change.AbstractEntry;
import io.emf.cbp.text.change.ChangeLog;
import io.emf.cbp.text.change.Entry;
import org.eclipse.emf.ecore.EObject;

public class PersistenceManager 
{
	private ChangeLog changeLog; //change this, change log is singleton, can just get it directly.

	private DeltaResourceImpl resource;
	public final String TEXT_ENCODING = "Ascii";
	
	
	public PersistenceManager(ChangeLog aChangeLog, DeltaResourceImpl resource)
	{
		changeLog = aChangeLog;
		this.resource = resource;
		
	}
	
	public void addObjectsToContents(List<EObject> objects)
	{
		resource.getContents().addAll(objects);
	}
	
	
	public void addObjectToContents(EObject object)
	{
		resource.getContents().add(object);
	}
	
	public URI getURI()
	{
		return resource.getURI();
	}

	
	//when the logic gets more complex, this is moving to its own serializer class.
	public void save(Map<?,?> options)
	{
		//String fileSaveLocation = (String) options.get("FILE_SAVE_LOCATION");
		try
		{
			
			BufferedWriter bw = new BufferedWriter
				    (new OutputStreamWriter(new FileOutputStream(resource.getURI().path(),true),Charset.forName(TEXT_ENCODING).newEncoder()));
			
			PrintWriter out = new PrintWriter(bw);
			
			for(AbstractEntry entry: changeLog.getEventsList())
			{
				for(String string: entry.getOutputStringsArray())
				{
					System.out.println("PersistenceManager.java "+string);
					out.println(string);
				}
			}
			out.close();
		}
		catch(IOException e)
		{
			e.printStackTrace();
		}
		
		//System.out.println("Size of events list: "+changeLog.getEventsList().size());
		
		
	//	out.close();
	}

	
	public void load(Map<?,?> options) throws IOException
	{	
		
		TextDeserializer textDeserializer = new TextDeserializer(this);
		textDeserializer.load(options);
	}
}
