package io.emf.cbp.text.change;

import org.eclipse.emf.ecore.EObject;

public class CreateObjectEntry 
{
	String className ; //The class of the object
	
	
	public CreateObjectEntry(EObject eObject)
	{
		//super(eObject);
		className = eObject.eClass().getName();
	}
	
	
}