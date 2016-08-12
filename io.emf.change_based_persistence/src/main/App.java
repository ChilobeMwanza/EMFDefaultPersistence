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


import org.eclipse.emf.ecore.resource.impl.ResourceSetImpl;
import org.eclipse.emf.ecore.util.EContentAdapter;
import org.eclipse.emf.ecore.util.EcoreUtil;
import org.eclipse.emf.ecore.xmi.impl.XMIResourceImpl;

import impl.DeltaResourceImpl;
import library.Author;
import library.Book;
import library.Library;
import library.LibraryFactory;
import library.LibraryPackage;
import library.LibraryType;
import library.Module;
import library.Student;
import library.Vehicle;


public class App 
{
	private static String fileSaveLocation ="library.txt";
	private  final String classname = this.getClass().getSimpleName();
	
	public static void main(String[] args) throws Exception
	{
		List<EObject> savedList = new ArrayList<EObject>();
		List<EObject> loadedList = new ArrayList<EObject>();
		
		// TODO Auto-generated method stub
		App app = new App();
		
		savedList = app.createResource();
		loadedList = app.loadResource() ;
	
		app.verify(savedList, loadedList);
	}
	
	public List<EObject> loadResource() throws IOException
	{
		ResourceSetImpl rs = new ResourceSetImpl();
		rs.getResourceFactoryRegistry().getExtensionToFactoryMap().put
		("txt", new Resource.Factory()
		{
			@Override
			public Resource createResource(URI uri)
			{
				return new DeltaResourceImpl(uri);
			}
		});
		
		rs.getPackageRegistry().put(LibraryPackage.eINSTANCE.getNsURI(), 
				LibraryPackage.eINSTANCE);
		
		Resource resource = rs.createResource(URI.createFileURI(fileSaveLocation));
		resource.load(null);
		
		//Map<String, String> loadOptions = new HashMap<String, String>();
		//loadOptions.put("FILE_LOCATION", fileSaveLocation);
		
		List<EObject> objectsList = new ArrayList<EObject>();
		
		for(TreeIterator<EObject> it = resource.getAllContents(); it.hasNext();)
		{
			objectsList.add(it.next());
		}
		
		resource.getContents().clear();
		
		return objectsList;
		
	}
	
	public List<EObject> createResource() throws Exception
	{
		
		Resource resource = new DeltaResourceImpl(URI.createURI("library.txt"));
		
		resource.getContents().addAll(loadResource());
		
	    Library lib1 = LibraryFactory.eINSTANCE.createLibrary();
	    resource.getContents().add(lib1);
	    resource.getContents().remove(lib1);
	    
	    List<String> names = new ArrayList<String>();
	   // names.add("peter white");
	    names.add("Mary, April Smith");
	    
	    //lib1.getEmployeeNames().addAll(names);
	    
	  
	   
	    //lib1.setName("York library");
	    
	   // Book b1  = LibraryFactory.eINSTANCE.createBook();
	   // lib1.setSuperBook(b1);
	    
	    
		/*SAVE STARTS HERE*/
		resource.save(null); 
		List<EObject> objectsList = new ArrayList<EObject>();
		
		for(TreeIterator<EObject> it = resource.getAllContents(); it.hasNext();)
		{
			objectsList.add(it.next());
		}
		resource.getContents().clear();
		
		return objectsList;
	}
	
	private void verify(List<EObject> savedList, List<EObject>loadedList)
	{
		/* Verify that saved and serialised data are the same*/
		if(EcoreUtil.equals(savedList, loadedList))
		{
			out("Serialsed data Verified!");
			return;
		}
		else
		{
			out("verification failed! attempting to find cause of failure...");
		}
		
		/* Check saved list and loaded list contain same number of elements*/
		if(savedList.size() != loadedList.size())
		{
			out("Error : mismatch between savedList size :"+savedList.size()+" and loadedList size:"+loadedList.size());
			return;
		}
	
		/* Compare attributes and their values */
		for(int i = 0; i < savedList.size(); i++)
		{
			EObject obj1 = savedList.get(i);
			EObject obj2 = loadedList.get(i);
			
			
			/*Comapare attributes*/
			for(EAttribute attr1 : obj1.eClass().getEAllAttributes()) //e vs eall
			{
				for(EAttribute attr2 : obj2.eClass().getEAllAttributes())
				{
					if(attr1 == attr2 && obj1.eIsSet(attr1))
					{
						
						Object value1 = obj1.eGet(attr1);
						Object value2 = obj2.eGet(attr2);
					
						
						if(value1.hashCode() != value2.hashCode())
						{
							out(" mismatched attributes :");
							out(obj1.eClass().getName()+"1 attribute : "+attr1.getName() + " value: "+obj1.eGet(attr1).toString());
							out(obj2.eClass().getName()+"2 attribute : "+attr2.getName() + " value: "+obj2.eGet(attr2).toString());
							System.exit(0);
						}
					}
				}
			}
			
			/*Compare structural features*/
			
			
			/*for(EStructuralFeature f1 : obj1.eClass().getEAllStructuralFeatures()) //combine two loops into one
			{
				for(EStructuralFeature f2 : obj2.eClass().getEAllStructuralFeatures())
				{
					if(f1==f2 && obj1.eIsSet(f1))
					{
						Object value1 = obj1.eGet(f1);
						Object value2 = obj2.eGet(f2);
						
						boolean null_value = false;
						
						if(value1 == null)
						{
							out("Warning: value of f1: \""+f1.getName()+"\" is null" );
							null_value = true;
						}
						else if(value2 == null)
						{
							out("Warning: value of EObject:\""+obj2.eClass().getName()+"\" feature:\""+f2.getName()+"\" is null");
							null_value = true;
						}
						if(!EcoreUtil.equals(f1, f2))
						{
							out("Error: mismatched structural features:");
							out(obj1.eClass().getName()+"1 reference : "+f1.getName()+" value: "+value1);
							out(obj2.eClass().getName()+"2 reference : "+f2.getName()+" value: "+value2);
							return;
						}
						
					}
				}
			}*/
			
		}
		
		out("Unable to determine cause of verification failure! Have you implemented the logic ?");
		
	}
	
	
	
	private void out(String str)
	{
		System.out.println(this.getClass().getSimpleName()+": "+str);
	}
	
	private void out(Boolean bool)
	{
		System.out.println(this.getClass().getSimpleName()+": "+bool);
	}

}