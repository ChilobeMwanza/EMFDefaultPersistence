package change;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import org.eclipse.emf.common.notify.Notification;
import org.eclipse.emf.ecore.EAttribute;
import org.eclipse.emf.ecore.EObject;

public class UnsetEAttributeEvent extends Event 
{
	private EObject focus_obj;
	
	private EAttribute attr;
	
	private List<Object> attr_values_list = new ArrayList<Object>();
	
	@SuppressWarnings("unchecked")
	public UnsetEAttributeEvent(EObject focus_obj, EAttribute attr, Object oldValue)
	{
		super(Event.UNSET_EATTRIBUTE_EVENT);
		
		if(oldValue instanceof Collection)
			this.attr_values_list = (List<Object>) oldValue;
		
		else
			this.attr_values_list.add(oldValue);
		
		this.focus_obj = focus_obj;
		
		this.attr = attr;
	}
	
	public UnsetEAttributeEvent(Notification n)
	{
		this((EObject) n.getNotifier(),(EAttribute) n.getFeature(),n.getOldValue());
	}
	
	public EObject getFocusObj()
	{
		return this.focus_obj;
	}
	
	public EAttribute getEAttribute()
	{
		return this.attr;
	}
	
	public List<Object> getAttributeValuesList()
	{
		return this.attr_values_list;
	}
}