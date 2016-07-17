package change;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import impl.DeltaResourceImpl;


public class ChangeLog 
{
	//public static final ChangeLog INSTANCE = new ChangeLog();
	
	public ChangeLog() {};
	private List<AbstractEntry> eventsList = new ArrayList<AbstractEntry>();
	private List<Entry>blah;

	public void addEvent(AbstractEntry entry)
	{
		eventsList.add(entry);
	}
	
	public List<AbstractEntry> getEventsList()
	{
		return eventsList;
	}
	
	public List<AbstractEntry> sortChangeLog()
	{
		return sortChangeLog(eventsList);
	}
	
	public List<AbstractEntry> sortChangeLog(List<AbstractEntry> list)
	{
		/* Create List for each type of event*/ //this could be built in to the class itself, would remove first for loop.
		List<AbstractEntry> newObjectEntryList = new ArrayList<AbstractEntry>();
		List<AbstractEntry> setAttrSingleList = new ArrayList<AbstractEntry>();
		List<AbstractEntry> setAttrManyList = new ArrayList<AbstractEntry>();
		
		/* Sort List entries into appropriate list*/
		for(AbstractEntry e: list)
		{
			if(e instanceof NewObjectEntry)
				newObjectEntryList.add(e);
			else if(e instanceof SetAttributeEntry)
			{
				SetAttributeEntry s = (SetAttributeEntry)e;
				if(s.geteAttribute().isMany())
					setAttrManyList.add(e);
				else
					setAttrSingleList.add(e);
			}	
		}
		
		/*Create a sorted list of entries*/
		List<AbstractEntry> sortedList = new ArrayList<AbstractEntry>();
		for(AbstractEntry a : newObjectEntryList)
		{
			sortedList.add(a);
			for(AbstractEntry b: setAttrSingleList) //get all set 'attribute single entries' for this item
			{
				if(b.getUUID() == a.getUUID())
					sortedList.add(b);
			}
			for(AbstractEntry c : setAttrManyList) //get all 'set attribute many' entries for this item
			{
				if(c.getUUID() == a.getUUID())
					sortedList.add(c);
			}
			
		}
		
		return sortedList;
	}
	
	public void showLogEntries(List<AbstractEntry> list)
	{
		for(AbstractEntry e : list)
		{
			if(e instanceof NewObjectEntry)
				System.out.println("CREATE "+e.getEObject().eClass().getName()+" "+e.getUUID().toString());
			
			if(e instanceof SetAttributeEntry)
			{
				SetAttributeEntry s = (SetAttributeEntry)e;
				System.out.println("SET " + e.getEObject().eClass().getName()+" "+e.getUUID()+" "+s.geteAttribute().getName());
			}
		}
	}
	
	public void showLogEntries()
	{
		showLogEntries(eventsList);
	}
}