package io.emf.cbp.text.change;

import org.eclipse.emf.ecore.EObject;

public interface Entry 
{
	public EObject getEObject();
	public String[] getOutputStringsArray();
}