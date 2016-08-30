package drivers;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.eclipse.emf.ecore.EAttribute;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.EReference;

import org.eclipse.emf.ecore.resource.Resource;

import change.AddEObjectsToEReferenceEvent;
import change.AddEObjectsToResourceEvent;
import change.AddObjectsToEAttributeEvent;
import change.Changelog;

public class ResourceContentsToEventsConverter 
{
	Changelog changelog;
	Resource resource;
	
	@SuppressWarnings("unused")
	private  final String classname = this.getClass().getSimpleName();
	
	public ResourceContentsToEventsConverter(Changelog changelog, Resource resource)
	{
		this.changelog = changelog;
		this.resource = resource;
	}
	
	public void convert()
	{
		if(resource.getContents().isEmpty())
			return;
		
		changelog.clear();
		
		AddEObjectsToResourceEvent e = new AddEObjectsToResourceEvent (resource.getContents());
		changelog.addEvent(e);
		
		for(EObject obj : resource.getContents())
		{
			createSetAttributeEntries(obj);
			handleReferences(obj);
		}
	}
	
	private List<EReference> opositeReferences = new ArrayList<EReference>();
	
	private void handleReferences(EObject root) 
	{
		for(Iterator<EObject> it = root.eAllContents(); it.hasNext();) //containment refs
		{
			EObject obj = it.next();
			
			createAddEObjectsToEReferenceEvent(obj.eContainer(),obj,obj.eContainmentFeature());
		
			createSetAttributeEntries(obj);
		}
		
		for(EObject obj : root.eCrossReferences()) //non containment refs
		{
			for(EReference rf : root.eClass().getEAllReferences())
			{
				if(!rf.isContainment())
				{
					if(root.eIsSet(rf))
					{
						createAddEObjectsToEReferenceEvent(root,obj,rf);
						createSetAttributeEntries(obj);
						
						//handle any of this objects containments
						if(rf.getEOpposite() != null)
						{
							if(opositeReferences.contains(rf))
							{
								return;
							}
							opositeReferences.add(rf);
						}
						handleReferences(obj);
					}
				}
			}
		}
	}
	
	private void createAddEObjectsToEReferenceEvent
		(EObject focusObject,EObject addedObject, EReference eRef)
	{
		AddEObjectsToEReferenceEvent e = 
				new AddEObjectsToEReferenceEvent(focusObject,addedObject,eRef);
		
		changelog.addEvent(e);
	}
	
	private void createSetAttributeEntries(EObject focusObject)
	{
		for(EAttribute attr : focusObject.eClass().getEAllAttributes())
		{
			if(focusObject.eIsSet(attr))
			{
				AddObjectsToEAttributeEvent e =
						new AddObjectsToEAttributeEvent(focusObject,attr,focusObject.eGet(attr));
				changelog.addEvent(e); //add to entry
			}
		}
	}
}
