/**
 * BUGS/ISSUES
 */

package main;


import java.io.IOException;
import java.io.PrintStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import javax.swing.text.html.HTMLDocument.Iterator;

import org.eclipse.emf.common.notify.Adapter;
import org.eclipse.emf.common.util.EList;
import org.eclipse.emf.common.util.TreeIterator;
import org.eclipse.emf.common.util.URI;
import org.eclipse.emf.ecore.EAttribute;
import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.EReference;
import org.eclipse.emf.ecore.EStructuralFeature;
//import org.eclipse.emf.common.notify.AdapterFactory;
import org.eclipse.emf.ecore.resource.Resource;
import org.eclipse.emf.ecore.resource.ResourceSet;
import org.eclipse.emf.ecore.resource.impl.ResourceSetImpl;
import org.eclipse.emf.ecore.util.EContentAdapter;
import org.eclipse.emf.ecore.util.EcoreUtil;
import org.eclipse.emf.ecore.xmi.impl.XMIResourceFactoryImpl;
import org.eclipse.emf.ecore.xmi.impl.XMIResourceImpl;

import impl.DeltaResourceImpl;
import university.University;
import university.UniversityFactory;
import university.UniversityPackage;


public class App 
{
	private static String fileSaveLocation ="university.txt";
	private  final String classname = this.getClass().getSimpleName();
	
	public static void main(String[] args) throws Exception
	{
		// TODO Auto-generated method stub
		App app = new App();
		
		app.createResource();
	
		//app.loadResource() ;
	
	}
	
	public void loadXMIResource() throws IOException  
	{
		
		ResourceSet rs = new ResourceSetImpl();
		rs.getResourceFactoryRegistry().getExtensionToFactoryMap().put("txt", new XMIResourceFactoryImpl());
		rs.getPackageRegistry().put(UniversityPackage.eINSTANCE.getNsURI(), UniversityPackage.eINSTANCE);
		Resource resource = rs.createResource(URI.createFileURI("library.txt"));
		resource.load(null);

	}
	
	public void createResource() throws Exception
	{
		
		Resource resource = new DeltaResourceImpl(URI.createURI(fileSaveLocation));
	
		University uni = UniversityFactory.eINSTANCE.createUniversity();
		resource.getContents().add(uni);
		uni.setName("Uni 1");
	    
		/*SAVE STARTS HERE*/
		resource.save(null); 
	
	}
}
