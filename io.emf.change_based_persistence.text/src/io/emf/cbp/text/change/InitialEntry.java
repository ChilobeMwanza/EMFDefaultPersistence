package io.emf.cbp.text.change;

import org.eclipse.emf.ecore.EObject;

public class InitialEntry extends AbstractEntry 
{
	//private String namespacePrefix;
	private String namespaceURI;
	//private String className;
	
	public InitialEntry(EObject eObject) 
	{
		super(eObject);
		
		//namespacePrefix = eObject.eClass().getEPackage().getNsPrefix();
		//System.out.println("Namespace prefix: "+namespacePrefix);
		
		namespaceURI = eObject.eClass().getEPackage().getNsURI();
		//System.out.println("NamespaceURI:"+namespaceURI);
		
	//	className = eObject.eClass().getName();
		//System.out.println("Class Name: "+className);
	}
	
	public String[] getOutputStringsArray()
	{
		String[] outputStrings = new String[1];
		
		//outputStrings[0] = "namespace prefix = "+namespacePrefix;
		outputStrings[0] = "NAMESPACEURI = "+namespaceURI;
		//outputStrings[2] ="classname = "+className;
		
		return outputStrings;
	}
	
}
