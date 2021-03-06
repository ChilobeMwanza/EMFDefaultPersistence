package change;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.eclipse.emf.ecore.EAttribute;
import org.eclipse.emf.ecore.EObject;

import gnu.trove.map.TObjectIntMap;

import gnu.trove.map.hash.TObjectIntHashMap;


public class Changelog 
{
	private final List<Event> eventList;
	
	private final TObjectIntMap<EObject> eObjectToIDMap = new TObjectIntHashMap<EObject>();

	private final String classname = this.getClass().getSimpleName();
	
	private static int current_id = 0; 
	
	public Changelog()
	{
		eventList = new ArrayList<Event>();
	}
	
	public void clear()
	{
		eventList.clear();
		eObjectToIDMap.clear();
		current_id = 0;
	}
	
	public boolean addObjectToMap(EObject obj)
	{
		if(!eObjectToIDMap.containsKey(obj))
		{
			eObjectToIDMap.put(obj, current_id);
			
			current_id = current_id +1;
			return true;
		}
		return false;
	}
	
	public boolean addObjectToMap(EObject obj, int id)
	{
		if(!eObjectToIDMap.containsKey(obj))
		{
			eObjectToIDMap.put(obj, id);
			
			if(id >= current_id)
			{
				current_id = id + 1;
			}
			return true;
		}
		return false;
	}
	
	public int getObjectId(EObject obj)
	{
		if(!eObjectToIDMap.containsKey(obj)) //tbr
		{
			System.out.println(classname+" search returned false");
			System.exit(0);
		}
		return eObjectToIDMap.get(obj);
	}

	public void addEvent(Event e)
	{
		eventList.add(e);
	}
	
	public void removeEvent(Event e)
	{
		eventList.remove(e);
	}
	
	public void clearEvents()
	{
		eventList.clear();
	}
	
	public List<Event> getEventsList()
	{
		return eventList;
	}
	
	public void debug()
	{
		System.out.println(classname+" DEBUG!");
		for(Event e : eventList)
		{
			System.out.println(e.getEventType());
		}
	}
	
	private boolean redundantEntryCheck() //tbr
	{
		//check for duplicate eobject occurances, tbr
		Map<EObject,Event> duplicateCheckMap = new HashMap<EObject, Event>();
		
		for(Event e : eventList)
		{
			for(EObject obj : e.getEObjectList())
			{
				if(!duplicateCheckMap.containsKey(obj))
					duplicateCheckMap.put(obj, e);
				
				else
					return true;
			}
		}
		return false;
	}
	
	
	public void removeRedundantEvents()
	{   
		if(eventList.isEmpty())
			return;
		
		Map<EObject,Event> eObjectToEventMap = new HashMap<EObject,Event>();
		
		Map<EObject, EAttributeHolder >eObjectToEAttributeHolderMap = 
				new HashMap<EObject,EAttributeHolder>();
		
		/* Pass 1: For EAttributeEvents, populate EAttributeHolders.
		 * For all other events, map each EObject to its most recent event.
		 */
		for(Iterator<Event> eventListIterator = eventList.iterator(); eventListIterator.hasNext();)
		{
			Event e =  eventListIterator.next();
			
			if(e instanceof EAttributeEvent)
			{
				// Make EAttribute Holder for currennt object, if not exists
				if(!eObjectToEAttributeHolderMap.containsKey(((EAttributeEvent) e).getFocusObject()))
				{
					eObjectToEAttributeHolderMap.put(((EAttributeEvent) e).
							getFocusObject(), new EAttributeHolder());
				}
				
				//handle add event
				if(e instanceof AddObjectsToEAttributeEvent)
				{
					eObjectToEAttributeHolderMap.get(((EAttributeEvent) e).getFocusObject()).
					addObjects(((EAttributeEvent) e).getEAttribute(),
							((EAttributeEvent) e).getEAttributeValuesList());
					
					eventListIterator.remove();
				}
				else //e instanceof RemoveObjectsFromEAttributeEvent
				{
					for(Iterator<Object> it = ((EAttributeEvent) e).getEAttributeValuesList().iterator(); it.hasNext();)
					{
						Object obj = it.next();
						
						if(eObjectToEAttributeHolderMap.get(((EAttributeEvent) e).getFocusObject()).
								removeObject(((EAttributeEvent) e).getEAttribute(), obj))
						{
							it.remove(); 
						}
					}
					
					if(((EAttributeEvent) e).getEAttributeValuesList().isEmpty())
						eventListIterator.remove();
				}
						
			}
			else //EObject events
			{
				for(EObject obj : e.getEObjectList())
				{
					eObjectToEventMap.put(obj, e);
				}
			}
		}
		
		/*Pass 2: Remove redundant eobj entries, 
		 * add set attribute events for eattributes, if any
		 * */
		List<EObject> removedEObjects = new ArrayList<EObject>();
		
		for(Iterator <Event> eventListIterator = eventList.iterator(); eventListIterator.hasNext();)
		{
			Event e = eventListIterator.next();
			
			if(e instanceof EAttributeEvent)
				continue;
			
			//For all EOBjects
			for(Iterator<EObject> eObjectListIterator = e.getEObjectList().iterator(); eObjectListIterator.hasNext();)
			{
				EObject obj = eObjectListIterator.next();
				
				Event objMostRecentEvent = eObjectToEventMap.get(obj) ; //get the 'most recent' event for this object
				
				if(objMostRecentEvent instanceof AddEObjectsToResourceEvent || 
						objMostRecentEvent instanceof AddEObjectsToEReferenceEvent)
				{
					if(e != objMostRecentEvent) //if this event is not the objects 'most recent event'
					{
						eObjectListIterator.remove(); //remove obj from this event
					}
				}
				/*if (objMostRecentEvent instanceof  RemoveEObjectsFromResourceEvent || 
				 * objMostRecentEvent instanceof RemoveEObjectsFromEReferenceEvent)
				 */
				else 
				{
					if( e != objMostRecentEvent)
					{
						
						removedEObjects.add(obj); //note that we prevented this obj from being added.
						
						eObjectListIterator.remove();//remove obj from this event
					}
					else
					{
						if(removedEObjects.contains(obj)) //no need to remove objects that we prevented from being added
						{
							eObjectListIterator.remove();
						}
					}
				}
			}
			
			if(e.getEObjectList().isEmpty()) //discard empty events
				eventListIterator.remove();
		}
		
		/*Reinsert optimised add events, if any*/
		for(Iterator<Entry<EObject, EAttributeHolder>> it = eObjectToEAttributeHolderMap.entrySet().iterator(); it.hasNext();)
		{
			Entry<EObject, EAttributeHolder>  pair = (Map.Entry<EObject, EAttributeHolder>) it.next();
			
			EAttributeHolder ah = pair.getValue();
			
			Map<EAttribute,List<Object>> eAttributeToObjectValuesMap = ah.getEAttributeToObjectValuesMap();
			
			for(Iterator<Entry<EAttribute, List<Object>>> iter = 
					eAttributeToObjectValuesMap.entrySet().iterator(); iter.hasNext();)
			{
				Entry <EAttribute, List<Object>> pair2  = (Map.Entry<EAttribute, List<Object>>) iter.next();
				
				if(!pair2.getValue().isEmpty())
				{
					AddObjectsToEAttributeEvent event = 
							new AddObjectsToEAttributeEvent(pair.getKey(),pair2.getKey(),pair2.getValue());
					eventList.add(event);
				}
				iter.remove(); //clean up.
			}
			
			it.remove(); //clean up
		}
		
		if(redundantEntryCheck()) //tbr
		{
			System.out.println(classname+"redundant entries found");
			System.exit(0);
		}
	}
	
	class EAttributeHolder //wrapper, holds eattributes and their values
	{
		Map<EAttribute,List<Object>> eAttributeToObjectValuesMap = new HashMap<EAttribute,List<Object>>();
		
		public boolean removeObject(EAttribute attr, Object obj)
		{
			if(!eAttributeToObjectValuesMap.containsKey(attr)) //tbr
			{
				return false;
			}
			return eAttributeToObjectValuesMap.get(attr).remove(obj);
		}
		
		public void addObjects(EAttribute attr, List<Object> objList)
		{
			if(!eAttributeToObjectValuesMap.containsKey(attr))
			{
				eAttributeToObjectValuesMap.put(attr, new ArrayList<Object>());
			}
			
			if(!attr.isMany())
			{
				eAttributeToObjectValuesMap.get(attr).add(0,objList.get(0));
			}
			else
			{
				eAttributeToObjectValuesMap.get(attr).addAll(objList);
			}	
		}
		
		public Map<EAttribute,List<Object>> getEAttributeToObjectValuesMap()
		{
			return this.eAttributeToObjectValuesMap;
		}	
	}
}
