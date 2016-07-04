package io.emf.cbp.text.change;

import org.eclipse.emf.ecore.EObject;

/*
 * Items can be added to other items via containment relationships e.g. Add library to book. Or add library to resource e.t.c
 */
public class AddObjectEntry extends AbstractEntry  
{
	String objectClassName;
	String destination;
	
	public AddObjectEntry(EObject eObject,String destination)
	{
		super(eObject);
	}
	
	public String[] getOutputStringsArray()
	{
		String[] outputStrings = new String[1];
		
		outputStrings[0] = "ADD "+objectClassName +" TO "+destination;
		return null;
	}
}