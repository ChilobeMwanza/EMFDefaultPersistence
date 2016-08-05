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
import library.Vehicle;


public class App 
{
	private static String fileSaveLocation ="library.txt";
	
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
		//Resource resource = new XMIResourceImpl(URI.createURI("library.txt"));
		
		Resource resource = new DeltaResourceImpl(URI.createURI("library.txt"));
		
		Library lib1 = LibraryFactory.eINSTANCE.createLibrary();
		Library lib2 = LibraryFactory.eINSTANCE.createLibrary();
		
		Library lib3 = LibraryFactory.eINSTANCE.createLibrary();
		Library lib4 = LibraryFactory.eINSTANCE.createLibrary();
		
		List<EObject> list = new ArrayList<EObject>();
		list.add(lib3);
		list.add(lib4);
		
		resource.getContents().add(lib1);
		resource.getContents().add(lib2);
		
		resource.getContents().addAll(list);
		
		List<String> namesList = new ArrayList<String>();
		
		namesList.add("Peter Black");
		namesList.add("April May");
		namesList.add("Harry Potter");
		
		lib1.getEmployeeNames().addAll(namesList);
		lib1.getEmployeeNames().add("Jacob white");
	    lib1.getNumbersList().add(37);
	    lib1.setADouble(22.5);
	    
	   // lib1.getEmployeeNames().addAll(namesList);
		
	//	resource.getContents().addAll(list);
		
		Book book1 = LibraryFactory.eINSTANCE.createBook();
		lib1.getBadBooks().add(book1);
		
		Author author = LibraryFactory.eINSTANCE.createAuthor();
		book1.setAnAuthor(author);
		author.setName("hello!");
		
		
		Book book2 = LibraryFactory.eINSTANCE.createBook();
	
		Book book3 = LibraryFactory.eINSTANCE.createBook();
		
		Book book4 = LibraryFactory.eINSTANCE.createBook();
		
		Book book5 = LibraryFactory.eINSTANCE.createBook();
	
		List<Book> booklist = new ArrayList<Book>();
		booklist.add(book2);
		booklist.add(book3);
		booklist.add(book4);
		booklist.add(book5);
		
		lib1.getGoodBooks().addAll(booklist);
		

		Vehicle v1 = LibraryFactory.eINSTANCE.createVehicle();
		lib2.setMainLibraryCar(v1);
		
		Vehicle v2 = LibraryFactory.eINSTANCE.createVehicle();
		Vehicle v3 = LibraryFactory.eINSTANCE.createVehicle();
		Vehicle v4 = LibraryFactory.eINSTANCE.createVehicle();
		
		List<Vehicle> carsList = new ArrayList<Vehicle>();
		carsList.add(v2);
		carsList.add(v3);
		carsList.add(v4);
		
		lib3.getReserveLibraryCars().addAll(carsList);
		
		v4.setName("dfd");
		v2.setVehicleID(33.0f);

		out("checking for adapaters:");
		for(Adapter e: v2.eAdapters() )
		{
			out(e.toString());
			
		}
	
	//	v1.setVehicleID(40.0f);
		//v1.setName("super car");
		
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
			out("verificaiton failed! attempting to find cause of failure...");
		}
		
		/* Check saved list and loaded list contain same number of elements*/
		if(savedList.size() != loadedList.size())
		{
			out(" : mismatch between savedList size :"+savedList.size()+" and loadedList size:"+loadedList.size());
			System.exit(0);
		}
	
		/* Compare attributes and their values */
		for(int i = 0; i < savedList.size(); i++)
		{
			EObject obj1 = savedList.get(i);
			EObject obj2 = loadedList.get(i);
			
			
			/*Comapare attributes*/
			/*for(EAttribute attr1 : obj1.eClass().getEAllAttributes()) //e vs eall
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
			}*/
			
			/*Compare structural features*/
		
			for(EStructuralFeature f1 : obj1.eClass().getEAllStructuralFeatures()) //combine two loops into one
			{
				for(EStructuralFeature f2 : obj1.eClass().getEAllStructuralFeatures())
				{
					if(f1 == f2 && obj1.eIsSet(f1))
					{
						Object value1 = obj1.eGet(f1);
						Object value2 = obj2.eGet(f2);
						
						if(value1 == null)
						{
							out("mismatched structural features:");
							out("value of f1: \""+f1.getName()+"\" is null" );
							System.exit(0);
						}
						else if(value2 == null)
						{
							out("mismatched structural features:");
							out("value of f2: \""+f2.getName()+"\" is null" );
							System.exit(0);
						}
						else if(!EcoreUtil.equals(f1, f2))
						{
							out("mismatched structural features:");
							out(obj1.eClass().getName()+"1 reference : "+f1.getName()+" value: "+value1);
							out(obj2.eClass().getName()+"2 reference : "+f2.getName()+" value: "+value2);
							System.exit(0);
						}
						
					}
				}
			}
			
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
