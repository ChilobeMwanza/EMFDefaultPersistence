package change;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.UUID;

import org.eclipse.emf.common.notify.Notification;
import org.eclipse.emf.ecore.EObject;

import com.google.common.collect.BiMap;
import com.google.common.collect.HashBiMap;

import impl.DeltaResourceImpl;


public class ChangeLog 
{
	private final List<Notification> notificationsList;
	private final BiMap<EObject,Double> map; //change to normal map
	private final String classname = this.getClass().getSimpleName();
	
	private double current_id;
	
	public ChangeLog()
	{
		notificationsList = new ArrayList<Notification>();
		map = HashBiMap.create();
		current_id = 0.0;
	}
	
	public BiMap<EObject,Double> getObjectToIdMap()
	{
		return map;
	}
	
	public boolean addObjectToMap(EObject obj)
	{
		//System.out.println(obj.eClass().getName());
		if(map.get(obj)== null)
		{
			map.put(obj, current_id);
			current_id = current_id + 1; //update
			return true;
		}	
		return false;
	}
	
	// cid = 5, id =5
	public boolean addObjectToMap(EObject obj, double id)
	{
		if(id >= current_id)
			current_id = id + 1;
		 
		if(getEObject(id) == null)
		{
			map.put(obj, id);
			return true;
		}	
		return false;
	}
	
	public double getObjectId(EObject obj)
	{
		double id;
		if(map.get(obj)!= null)
			id = map.get(obj);
		else
			id = -1;
		
		return id;
	}
	
	public EObject getEObject(double id)
	{
		return  map.inverse().get(id);
	}
	
	public void addNotification(Notification n)
	{
		notificationsList.add(n);
	}
	
	public List<Notification> getEventsList()
	{
		return notificationsList;
	}
	
}
