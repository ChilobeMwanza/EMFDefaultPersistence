package change;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import org.eclipse.emf.common.notify.Notification;
import org.eclipse.emf.ecore.EAttribute;
import org.eclipse.emf.ecore.EObject;

public class SetEAttributeEvent extends Event
{
	
	private EObject focus_obj;
	private EAttribute attr;
	private List<Object> attr_values_list = new ArrayList<Object>();
	 
    @SuppressWarnings("unchecked")
	public SetEAttributeEvent(EObject focus_obj, EAttribute attr, Object newValue)
    {
       super(Event.SET_EATTRIBUTE_EVENT);
       
       this.focus_obj = focus_obj;
       this.attr = attr;
       
       if(newValue instanceof Collection)
    	   this.attr_values_list = (List<Object>) newValue;
       
       else
    	   this.attr_values_list.add(newValue);
       
    }
    
    @SuppressWarnings("unchecked")
    public SetEAttributeEvent(Notification n)
    {
        this((EObject) n.getNotifier(),(EAttribute) n.getFeature(),(List<Object>) n.getNewValue());
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