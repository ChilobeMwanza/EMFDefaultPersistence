package change;

import org.eclipse.emf.common.notify.Notification;

public class RemoveEObjectsFromResourceEvent extends ResourceEvent 
{
	public RemoveEObjectsFromResourceEvent(Notification n)
	{
		super(Event.REMOVE_EOBJECTS_FROM_RESOURCE_EVENT,n);
	}
	
	public RemoveEObjectsFromResourceEvent(Object removedEObjects)
	{
		super(Event.REMOVE_EOBJECTS_FROM_RESOURCE_EVENT,removedEObjects);
	}
}
