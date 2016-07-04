
package io.emf.cbp.resource;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.util.Map;

import org.eclipse.emf.common.util.URI;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.resource.impl.ResourceImpl;

import io.emf.cbp.text.change.ChangeLog;
import io.emf.cbp.text.change.CreateObjectEntry;
import io.emf.cbp.text.change.InitialEntry;
import main.PersistenceManager;


public class DeltaResourceImpl extends ResourceImpl
{
	private final ChangeLog changeLog = ChangeLog.INSTANCE;
    private PersistenceManager persistenceManager;
	
	private boolean initEntryAdded = false;
	
	public DeltaResourceImpl()
	{
		//tbr
	}
	
	
	@Override
	public void save(Map<?, ?> options)
	{
		persistenceManager = new PersistenceManager(options,changeLog);
		persistenceManager.save();
	}
	
	@Override
	public void load(Map<?, ?> options)
	{
		System.out.println("Load called!");
	}
	


	//called when object is added to resource, directly or indirectly.
	@Override
	public void attached(EObject eObject) 
	{
		super.attached(eObject);
		
		if(!initEntryAdded)
			addChangeLogInitialEntry(eObject);
		
		//System.out.println(eObject.getClass().getSimpleName()+" Object attached to le resource");
	}
	
	private void addChangeLogInitialEntry(EObject eObject)
	{
		changeLog.addEvent(new InitialEntry(eObject));
		initEntryAdded = true;
	}
	
	private void addChangeLogCreateObjectEntry(EObject eObject)
	{
		//changeLog.addEvent(new CreateObjectEntry(eObject));
	}
}