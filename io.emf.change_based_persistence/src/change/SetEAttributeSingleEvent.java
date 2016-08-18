package change;

import org.eclipse.emf.common.notify.Notification;
import org.eclipse.emf.ecore.EAttribute;
import org.eclipse.emf.ecore.EObject;

public class SetEAttributeSingleEvent extends Event
{
	private EObject focus_obj;
	private EAttribute attr;
	private Object newValue;
	
	public SetEAttributeSingleEvent(EObject focus_obj, EAttribute attr, Object newValue)
	{
		super(Event.SET_EATTRIBUTE_SINGLE);
		
		this.focus_obj = focus_obj;
		this.attr = attr;
		this.newValue = newValue;
	}
	
	public SetEAttributeSingleEvent(Notification n)
	{
		this((EObject) n.getNotifier(),(EAttribute) n.getFeature(),n.getNewValue());
	}
	
	public EObject getFocusObj()
	{
		return this.focus_obj;
	}
	
	public EAttribute getEAttribte()
	{
		return this.attr;
	}
	
	public Object getNewValue()
	{
		return this.newValue;
	}
}