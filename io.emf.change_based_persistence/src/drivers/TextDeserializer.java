/*
 * TO DO:
 * Throw proper exceptions
 */
package drivers;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;

import java.util.Map;
import java.util.StringTokenizer;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.eclipse.emf.common.util.EList;
import org.eclipse.emf.ecore.EAttribute;
import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.EPackage;
import org.eclipse.emf.ecore.EReference;

import org.eclipse.emf.ecore.util.EcoreUtil;
import change.ChangeLog;

public class TextDeserializer 
{
	private final String classname = this.getClass().getSimpleName();
	private EPackage ePackage = null;
	private final ChangeLog changelog;
	
	
	private enum EventType
	{
		SET_A,
		UNSET_A,
		ADD_R,
	    CREATE,
		SET_R,
		DEL_R,
		UNSET_R,
		NULL;
	}
	
	private PersistenceManager manager;
	//private String fileLocation;
	
	public TextDeserializer(PersistenceManager manager, ChangeLog aChangeLog)
	{
		this.manager = manager;
		this.changelog = aChangeLog;
	}
	
	public void load(Map<?,?> options) throws IOException
	{	
		BufferedReader br = new BufferedReader(
				new InputStreamReader(new FileInputStream(manager.getURI().path()), manager.TEXT_ENCODING));
		
		String line;
		
		
		if((line = br.readLine()) != null)
		{
			ePackage = loadMetamodel(getNthWord(line,2));
		}
		else
		{
			System.out.println(classname+" Error, file empty");
			System.exit(0);
		}
		
		while((line = br.readLine()) != null)
		{
			//System.out.println(line);
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
			case CREATE:
				handleCreateEvent(line);
				break;
			case SET_A:
				handleSetEAttributeEvent(line);
				break;
			case SET_R:
				handleSetEReferenceEvent(line);
				break;
			case UNSET_A: 
				handleUnsetEAttributeEvent(line);
				break;
			case UNSET_R:
				handleUnsetEReferenceEvent(line);
				break;
			case ADD_R:
				handleAddToResourceEvent(line);
				break;
			case DEL_R:
				handleRemoveFromResourceEvent(line);
				break;
			case NULL:
				break;
			default:
				break;
			}	
		}
		br.close();
		manager.setResume(true);
	}
	
	private void handleSetEReferenceEvent(String line)
	{
		EObject focus_obj = changelog.getEObject(Long.valueOf(getNthWord(line,4)));
		EReference ref = (EReference) focus_obj.eClass().getEStructuralFeature(getNthWord(line,2));
		
		String[] feature_values_array = tokeniseString(getValueInSquareBrackets(line));
		
		if(ref.isMany())
		{
			@SuppressWarnings("unchecked")
			EList<EObject> feature_value_list = (EList<EObject>) focus_obj.eGet(ref);
			
			for(String str : feature_values_array)
			{
				feature_value_list.add(changelog.getEObject(Long.valueOf(getNthWord(str,2))));
			}
		}
		else
		{
			focus_obj.eSet(ref, changelog.getEObject(Long.valueOf(getNthWord(feature_values_array [0],2))));
		}
	}
	
	private void handleUnsetEReferenceEvent(String line)
	{
		EObject focus_obj = changelog.getEObject(Long.valueOf(getNthWord(line,4)));
		
		EReference ref = (EReference) focus_obj.eClass().getEStructuralFeature(getNthWord(line,2));
		
		if(ref.isMany())
		{
			String[] feature_values_array = tokeniseString(getValueInSquareBrackets(line));
			
			@SuppressWarnings("unchecked")
			EList<EObject> feature_value_list = (EList<EObject>) focus_obj.eGet(ref);
			
			for(String str : feature_values_array)
			{
				feature_value_list.remove(changelog.getEObject(Long.valueOf(getNthWord(str,2))));				
			}
		}
		else
		{
			focus_obj.eUnset(ref);
		}
	}
	
	private void handleSetEAttributeEvent(String line) 
	{
		EObject focus_obj = changelog.getEObject(Long.valueOf(getNthWord(line,4)));
		EAttribute attr = (EAttribute)focus_obj.eClass().getEStructuralFeature(getNthWord(line,2));
		
		String[] feature_values_array = tokeniseString(getValueInSquareBrackets(line));
		
		if(attr.isMany())
		{
			@SuppressWarnings("unchecked")
			EList<Object> feature_value_list = (EList<Object>) focus_obj.eGet(attr);  //change nam of var!
			
			for(String str : feature_values_array)
			{
				if(str.equals("null"))
					feature_value_list.add(null);
				else
					feature_value_list.add(EcoreUtil.createFromString(attr.getEAttributeType(),str));
			}
		}
		else
		{
			System.out.println(classname+" "+feature_values_array [0]);
			if(feature_values_array [0].equals("null"))
				focus_obj.eSet(attr, null);
			else
				focus_obj.eSet(attr, EcoreUtil.createFromString(attr.getEAttributeType(),feature_values_array [0]));
		}
	}
	
	private void handleAddToResourceEvent(String line)
	{
		String[] obj_str_array = tokeniseString(getValueInSquareBrackets(line));
		
		for(String str : obj_str_array)
		{
			EObject obj = changelog.getEObject(Long.valueOf(getNthWord(str,2)));
			manager.addEObjectToContents(obj);
		}
	}
	
	private void handleRemoveFromResourceEvent(String line)
	{
		String[] obj_str_array = tokeniseString(getValueInSquareBrackets(line));
		
		for(String str : obj_str_array)
		{
			EObject obj = changelog.getEObject(Long.valueOf(getNthWord(str,2)));
			manager.removeEObjectFromContents(obj);
		}
	}
	
	private void handleCreateEvent(String line)
	{
		String[] obj_str_array = tokeniseString(getValueInSquareBrackets(line));
		for(String str : obj_str_array)
		{
			Long id = Long.valueOf(getNthWord(str,2));
			EObject obj = createEObject(getNthWord(str,1));
			changelog.addObjectToMap(obj, id);	
		}
	}
	
	private void handleUnsetEAttributeEvent(String line)
	{
		long obj_id = Long.valueOf(getNthWord(line,4));
		
		EObject obj = changelog.getEObject(obj_id);
		
		EAttribute attr = (EAttribute) obj.eClass().getEStructuralFeature(getNthWord(line,2));
		
		if(attr.isMany())
		{
			@SuppressWarnings("unchecked")
			EList<Object> attrValueList = (EList<Object>) obj.eGet(attr);  
			String[] obj_attr_str_array = tokeniseString(getValueInSquareBrackets(line));
			
			for(String str : obj_attr_str_array)
			{
				attrValueList.remove(EcoreUtil.createFromString(attr.getEAttributeType(),str));
			}
		}
		else
		{
			obj.eUnset(attr);
		}
	}
	
	private EPackage loadMetamodel(String metamodelURI)
	{
		//EPackage ePackage = EPackage.Registry.INSTANCE.getEPackage("http://io.emf.change_based_persistence.text");
		EPackage ePackage = null;
		if(EPackage.Registry.INSTANCE.containsKey(metamodelURI))
			ePackage = EPackage.Registry.INSTANCE.getEPackage(metamodelURI);
		else
		{
			System.out.println(classname+" could not load metamodel!");
			System.exit(0);
		}
		return ePackage;
	}
	
	
	//should check for errors here
	private EObject createEObject(String eClassName) //does this need to be a method?
	{
		return ePackage.getEFactoryInstance().create((EClass)
				ePackage.getEClassifier(eClassName));
	}
	
	private String getNthWord(String input, int n)
	{
		String [] stringArray = input.split(" ");
		if(n-1 < stringArray.length)
			return stringArray[n-1];
		return null;
	}
	
	/*Tokenises a string seperated by a specified delimiter
	 *http://stackoverflow.com/questions/18677762/handling-delimiter-with-escape-
	  -in-java-string-split-method
	 * */
	private String[] tokeniseString(String input)
	{
		String regex = "(?<!" + Pattern.quote(PersistenceManager.ESCAPE_CHAR) + ")" + Pattern.quote(PersistenceManager.DELIMITER);

		String[] output = input.split(regex);
		
		for(int i = 0; i < output.length; i++)
		{
			output[i] = output[i].replace(PersistenceManager.ESCAPE_CHAR+PersistenceManager.DELIMITER, PersistenceManager.DELIMITER);
		}
		
		return output;
	}
	
	//returns everything inbetween []
	private String getValueInSquareBrackets(String str)
	{
		Pattern p = Pattern.compile("\\[(.*?)\\]");
		Matcher m = p.matcher(str);
		
		String result = "";
		
		if(m.find())
			result = m.group(1);
		return result;
	}
}
