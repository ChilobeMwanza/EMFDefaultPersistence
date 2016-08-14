/*
 * todo:
 * remove 'smartness' in tests
 * what if exception occurs during switch, and thus notifications are not cleared?
 * 
 * more robustness, i.e invalid save file (first line is not namespace, invalid entries e.t.c)
 * add some unit tests
 * catch exeptions (i.e file not found, at load)
 * optimisation algorithm
 * check everything works with non generated emf (Reflective)
 * make txt format less verbose
 * binary
 * 
 * when unsetting single valued feature, name is not needed
 */
package drivers;


import java.io.BufferedWriter;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import org.eclipse.emf.common.notify.Notification;
import org.eclipse.emf.ecore.EAttribute;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.EReference;
import org.eclipse.emf.ecore.util.EcoreUtil;

import change.ChangeLog;
import impl.DeltaResourceImpl;


public class TextSerializer 
{
	private  final String classname = this.getClass().getSimpleName();
	private final PersistenceManager manager;
	private List<String> outputList;
    private List<Notification> notificationsList;
	private final ChangeLog changelog; 
	
	public TextSerializer(PersistenceManager manager, ChangeLog aChangeLog)
	{
		this.manager =  manager;
		this.changelog = aChangeLog;
		this.outputList = new ArrayList<String>();
		this.notificationsList = new ArrayList<Notification>();
		
		notificationsList = manager.getChangeLog().getEventsList();
	}
	
	public void save(Map<?,?> options)
	{
		if(notificationsList.isEmpty())
			return;
		
		//Check if output file contains initial 'namespace' entry
		
		
		
		/*if(!manager.isResume()) 
		{
			addInitialEntryToOutputList();
			System.out.println(classname+"not in resume mode!, adding init entry to output list!");
		}
		else if(!isInitialEntrySerialised()) 
		{
			System.out.println(classname+" init entry not serialised, working on it!");
			addInitialEntryToOutputList();
		}*/
		
		
		if(!manager.isResume()) 
		{
			addInitialEntryToOutputList();
			System.out.println(classname+"not in resume mode!, adding init entry to output list!");
		}
	
		
		
		for(Notification n : notificationsList)
		{
			switch(n.getEventType())
			{
				case Notification.ADD:
				{
					if(n.getNewValue() instanceof EObject) 
						handleSetEReferenceSingleEvent(n);
					
					else if(n.getFeature() instanceof EAttribute)
						handleSetEAttributeSingleEvent(n);
					
					break;
				}
				case Notification.SET:
				{
					if(n.getNewValue() instanceof EObject)
						handleSetEReferenceSingleEvent(n);
					
					else if(n.getFeature() instanceof EAttribute)
						handleSetEAttributeSingleEvent(n);
					
					else if(n.getNewValue() == null)
						handleUnsetEReferenceSingleEvent(n);
					break;
				}
				case Notification.ADD_MANY:
				{
					@SuppressWarnings("unchecked")
					List<Object> list =  (List<Object>) n.getNewValue();
					if(list.get(0) instanceof EObject)
						handleSetEReferenceManyEvent(n);
					
					else if(n.getFeature() instanceof EAttribute)
						handleSetEAttributeManyEvent(n);
					
					break;
				}
				case Notification.REMOVE:
				{
					if(n.getOldValue() instanceof EObject)
						handleUnsetEReferenceSingleEvent(n);
					
					else if(n.getFeature() instanceof EAttribute)
						 handleUnsetEAttributeSingleEvent(n);
					
					break;
				}
				case Notification.REMOVE_MANY:
				{
					@SuppressWarnings("unchecked")
					List<Object> list =  (List<Object>) n.getOldValue();
					if(list.get(0) instanceof EObject)
						handleUnsetEReferenceManyEvent(n);
					
					else if(n.getFeature() instanceof EAttribute)
						handleUnsetEAttributeManyEvent(n);
					
					break;
				}
				default:
				{
					System.out.println(classname+"Unhandled notification!" +n.toString());
					System.exit(0);
				}
			}
		}
		
		changelog.clearNotifications();
		
		/*finally append strings to file, if no previous successful load,don't 
		 * serialise in append mode(i.e create new file, e.t.c)
		 */
		System.out.println(classname+" appendmode: "+manager.isResume());
		
		/*Write contents of output list to file*/
		writeOutputListToFile(manager.isResume()); 
	}
	
	private void handleSetEAttributeSingleEvent(Notification n)
	{
		EObject focus_obj = (EObject) n.getNotifier();
		
		EAttribute attr = (EAttribute) n.getFeature();
		
		String newValue = EcoreUtil.convertToString(attr.getEAttributeType(),  n.getNewValue());
		
		if(newValue == null)
			newValue = "null";
		
		newValue = newValue.replace(PersistenceManager.DELIMITER, PersistenceManager.ESCAPE_CHAR+PersistenceManager.DELIMITER); //escape delimiter (if any)
		
		outputList.add("SET_A "+attr.getName()+" "+focus_obj.eClass().getName()+" "+changelog.getObjectId(focus_obj)+" ["+newValue+"]");
	}
	
	/*private boolean isInitialEntrySerialised() 
	{
		
		// if output file doesn't exits, initial entry doesn't exist
		File f = new File(manager.getURI().path());
		if(!f.exists() || f.isDirectory())
			return false;
		
		// output file exists, check it for 'namespace' entry
		try (BufferedReader br = new BufferedReader(
				new InputStreamReader(new FileInputStream(manager.getURI().path()), manager.TEXT_ENCODING));)
		{
			String line;
			
			if((line = br.readLine()) != null)
			{
				return line.contains("NAMESPACE_URI");
			}
		} 
		catch (Exception e) 
		{
			e.printStackTrace();
		}
		
		return false;
	}*/
	
	private void handleUnsetEAttributeSingleEvent(Notification n)
	{
		EObject focus_obj = (EObject) n.getNotifier();
		
		EAttribute attr = (EAttribute) n.getFeature();
		
		String oldValue = EcoreUtil.convertToString(attr.getEAttributeType(),  n.getOldValue());
		
		oldValue = oldValue.replace(PersistenceManager.DELIMITER, PersistenceManager.ESCAPE_CHAR+PersistenceManager.DELIMITER); //escape delimiter (if any)
		
		outputList.add("UNSET_A "+attr.getName()+" "+focus_obj.eClass().getName()+" "
					+changelog.getObjectId(focus_obj)+" ["+oldValue+"]");
	}
	
	private void handleSetEAttributeManyEvent(Notification n)
	{
		EObject focus_obj = (EObject) n.getNotifier();
		EAttribute attr = (EAttribute) n.getFeature();
		
		@SuppressWarnings("unchecked")
		List<Object> attr_values_list = (List<Object>) n.getNewValue();
		
		String obj_list_str = "[";
		
		for(Object object: attr_values_list)
		{
			String newValue = (EcoreUtil.convertToString(attr.getEAttributeType(), object));
			
			if(newValue == null)
				newValue = "null";
			
			newValue = newValue.replace(PersistenceManager.DELIMITER, PersistenceManager.ESCAPE_CHAR+PersistenceManager.DELIMITER); //escape delimiter
			
			obj_list_str = obj_list_str +newValue+PersistenceManager.DELIMITER;
		}
		obj_list_str = obj_list_str.substring(0,obj_list_str.length()-1)+"]"; // remove final delimiter  add "]"
		outputList.add("SET_A "+attr.getName()+" "+focus_obj.eClass().getName()+" "+changelog.getObjectId(focus_obj)+" "+obj_list_str);
	}
	
	private void handleUnsetEAttributeManyEvent(Notification n)
	{
		EObject focus_obj = (EObject) n.getNotifier();
		EAttribute attr = (EAttribute) n.getFeature();
		
		@SuppressWarnings("unchecked")
		List<Object> attr_values_list =(List<Object>) n.getOldValue();
		
		String obj_list_str = "[";
		
		for(Object object: attr_values_list)
		{
			String newValue = (EcoreUtil.convertToString(attr.getEAttributeType(), object));
			obj_list_str = obj_list_str + newValue+PersistenceManager.DELIMITER;
		}
		obj_list_str = obj_list_str.substring(0,obj_list_str.length()-1)+"]"; // remove final delimiter  add "]"
		outputList.add("UNSET_A "+attr.getName()+" "+focus_obj.eClass().getName()+" "+changelog.getObjectId(focus_obj)+" "+obj_list_str);
		
	}
	
	private void handleSetEReferenceSingleEvent(Notification n)
	{
		EObject added_obj = (EObject)n.getNewValue();
		
		if(changelog.addObjectToMap(added_obj))//make 'create' entries for obj which don't already exist
			outputList.add("CREATE ["+added_obj.eClass().getName()+ " "+changelog.getObjectId(added_obj)+"]");
			
		if(n.getNotifier() instanceof DeltaResourceImpl) //add eobject to resource
		{
			outputList.add("ADD_R ["+added_obj.eClass().getName()+" "+changelog.getObjectId(added_obj)+"]"); 
		}
		else if(n.getNotifier() instanceof EObject) //add eobject to eobject
		{
			EObject focus_obj = (EObject) n.getNotifier();
			
			outputList.add("SET_R "+((EReference)n.getFeature()).getName()+" "+focus_obj.eClass().getName()+" "
					+changelog.getObjectId(focus_obj)+" ["+added_obj.eClass().getName()+" "
					+changelog.getObjectId(added_obj)+"]");
		}
		
	
	}
	
	private void handleUnsetEReferenceSingleEvent(Notification n)
	{
		EObject removed_obj = (EObject)n.getOldValue();
		long removed_obj_id = changelog.getObjectId(removed_obj);
		if(n.getNotifier() instanceof DeltaResourceImpl) //delete eobject from resource
		{
			outputList.add("DEL_R ["+removed_obj.eClass().getName()+" "+
					removed_obj_id+"]"); 
			
			changelog.deleteEObjectFromMap(removed_obj_id);
		}
		else if(n.getNotifier() instanceof EObject)
		{
			EObject focus_obj = (EObject) n.getNotifier();
			
			outputList.add("UNSET_R "+((EReference)n.getFeature()).getName()+" "+focus_obj.eClass().getName()+" "
					+changelog.getObjectId(focus_obj)+" ["+removed_obj.eClass().getName()+" "
					+removed_obj_id+"]");
			
			changelog.deleteEObjectFromMap(removed_obj_id);
		}
		
	}
	
	private void handleSetEReferenceManyEvent(Notification n)
	{
		@SuppressWarnings("unchecked")
		List<EObject> obj_list = (List<EObject>) n.getNewValue();
		
		String added_obj_list_str = "["; //list of obj to add
		String obj_create_list_str = "["; //list of obj to create

		
		for(EObject obj : obj_list)
		{
			if(changelog.addObjectToMap(obj)) //if obj does not already exist
			{
				obj_create_list_str = obj_create_list_str + obj.eClass().getName()+" "
						+ ""+changelog.getObjectId(obj)+PersistenceManager.DELIMITER; 
			}
			
			added_obj_list_str = added_obj_list_str + obj.eClass().getName()+" "+changelog.getObjectId(obj)+PersistenceManager.DELIMITER; 
		}
		
		if(obj_create_list_str.length()>1) //if we have items to create...
		{
			 obj_create_list_str = obj_create_list_str.substring(0,added_obj_list_str.length()-1)+"]"; // remove final delimiter  add "]"
			 outputList.add("CREATE "+ obj_create_list_str);
		}
		
	    added_obj_list_str = added_obj_list_str.substring(0,added_obj_list_str.length()-1)+"]"; // remove final delimiter add "]"
	    
	    if(n.getNotifier() instanceof DeltaResourceImpl) //add eobject to resource
	    {
	    	outputList.add("ADD_R "+added_obj_list_str);
	    }	
	    else if(n.getNotifier() instanceof EObject) //add eobject to eobject
	    {
	    	EObject focus_obj = (EObject) n.getNotifier();
	    	
	    	outputList.add("SET_R "+((EReference)n.getFeature()).getName()+" "+focus_obj.eClass().getName()+" "
					+changelog.getObjectId(focus_obj)+" "+added_obj_list_str);
	    }
	    
	}
	
	private void handleUnsetEReferenceManyEvent(Notification n)
	{
		@SuppressWarnings("unchecked")
		List<EObject> removed_obj_list = (List<EObject>) n.getOldValue();
		
		String obj_delete_list_str = "["; //list of obj to delete
		
		for (EObject obj : removed_obj_list)
		{
			long removed_obj_id = changelog.getObjectId(obj);
			
			obj_delete_list_str = obj_delete_list_str +  obj.eClass().getName()+" "+removed_obj_id +PersistenceManager.DELIMITER; 
			
			changelog.deleteEObjectFromMap(removed_obj_id);	
		}
		
		obj_delete_list_str = obj_delete_list_str.substring(0,obj_delete_list_str.length()-1)+"]";
		
		if(n.getNotifier() instanceof DeltaResourceImpl) //DELETE OBJs FROM RESOURCE
		{
			outputList.add("DEL_R "+obj_delete_list_str);
		}
		else if(n.getNotifier() instanceof EObject)
		{
			EObject focus_obj = (EObject) n.getNotifier();
			
			outputList.add("UNSET_R "+((EReference)n.getFeature()).getName()+" "+focus_obj.eClass().getName()+" "
							+changelog.getObjectId(focus_obj)+" "+obj_delete_list_str);
		}	
	
	}
	
	private void addInitialEntryToOutputList()
	{
		EObject obj = null;
		for(Notification n : notificationsList)
		{
			if(n.getEventType() == Notification.ADD)
			{
				obj = (EObject) n.getNewValue();
				break;
			}
			else if(n.getEventType() == Notification.ADD_MANY)
			{
				@SuppressWarnings("unchecked")
				List<EObject> objectsList = (List<EObject>) n.getNewValue();
				obj = objectsList.get(0);
				break;
			}
			else
			{
				System.out.println("out of luck!");
			}
		}
		outputList.add("NAMESPACE_URI "+obj.eClass().getEPackage().getNsURI());
	}
	
	
	private void writeOutputListToFile(boolean appendMode)
	{
		try
		{
			BufferedWriter bw = new BufferedWriter
				    (new OutputStreamWriter(new FileOutputStream(manager.getURI().path(),appendMode),
				    		Charset.forName(manager.TEXT_ENCODING).newEncoder()));
			PrintWriter out = new PrintWriter(bw);
			for(String string: outputList)
			{
				//System.out.println("PersistenceManager.java "+string);
				out.println(string);
			}
			out.close();
		}
		catch(IOException e)
		{
			e.printStackTrace();
		}
		
		manager.setResume(true);
	}	
}
