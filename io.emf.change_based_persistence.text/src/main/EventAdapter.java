package main;

import org.eclipse.emf.common.notify.Notification;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.util.EContentAdapter;

import io.emf.cbp.text.change.AddObjectEntry;

public class EventAdapter extends EContentAdapter
{
	@Override
	public void notifyChanged(Notification notification)
	{
		super.notifyChanged(notification);
		
		if(notification.isTouch())
			return; 
		
		//Get the class of the object affected by the change.
	    Class<? extends Object> affectedClass = notification.getNotifier().getClass(); //if we just want a name, get notifer works.
	    int eventType = notification.getEventType();
	   // Object object = notification.getNotifier();
	    
	   // Object object = notification.getNewValue();
	    
		System.out.println("Change made to: "+affectedClass
		.getSimpleName());
		
		System.out.println("Event type :"+eventType);
		
		
	  // System.out.println("debugging object: "+object.getClass().);
		
		
		switch(notification.getEventType())
		{
		case 3:
			EObject eObject = (EObject)notification.getNewValue();
			AddObjectEntry entry = new AddObjectEntry(eObject,notification.getNotifier().getClass().getSimpleName());
			break;
		default:
			System.out.println("default");
			break;
		}
	}
	
	public void handleAddEvent()
	{
		
	}
}
