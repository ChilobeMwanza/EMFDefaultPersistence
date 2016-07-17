
//todo: specify encoding ?
//http://www.javapractices.com/topic/TopicAction.do?Id=31
package drivers;

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
import org.eclipse.emf.ecore.EObject;

import change.AbstractEntry;
import change.ChangeLog;
import change.Entry;
import impl.DeltaResourceImpl;

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
		TextSerializer serializer = new TextSerializer(this);
		serializer.save(options);
	}

	
	public void load(Map<?,?> options) throws IOException
	{	
		
		TextDeserializer textDeserializer = new TextDeserializer(this);
		textDeserializer.load(options);
	}
	
	public DeltaResourceImpl getResource()
	{
		return this.resource;
	}
}