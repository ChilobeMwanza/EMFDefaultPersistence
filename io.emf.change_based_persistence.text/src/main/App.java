/**
 * BUGS/ISSUES
 * 1) Registering of the epackage accross sessions.
 */

package main;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import org.eclipse.emf.common.util.URI;
import org.eclipse.emf.ecore.EPackage;
import org.eclipse.emf.ecore.EcorePackage;
//import org.eclipse.emf.common.notify.AdapterFactory;
import org.eclipse.emf.ecore.resource.Resource;


import org.eclipse.emf.ecore.resource.impl.ResourceSetImpl;



import io.emf.cbp.resource.DeltaResourceImpl;
import library.Book;
import library.Library;
import library.LibraryFactory;
import library.LibraryPackage;

public class App 
{
	private static String fileSaveLocation ="library.txt";
	
	public static void main(String[] args) throws Exception
	{
		// TODO Auto-generated method stub
		App app = new App();
		app.loadResource();
		//app.createResource();
	}
	
	public void loadResource() throws IOException
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
		
		
		
		Library library = (Library) resource.getContents().get(0);
		
		
		//Map<String, String> loadOptions = new HashMap<String, String>();
		//loadOptions.put("FILE_LOCATION", fileSaveLocation);
		
	}
	
	
	public void createResource() throws Exception
	{
		//Resource resource = new XMIResourceImpl();
		
		System.out.println(EPackage.Registry.INSTANCE.containsKey("http://io.emf.change_based_persistence.text"));
		Resource resource = new DeltaResourceImpl(URI.createURI("library.txt"));
		
		EPackage epackage = LibraryPackage.eINSTANCE; //1
		
		System.out.println(epackage.getNsURI());

		
		//Create root object, add it to resource.
		Library lib = LibraryFactory.eINSTANCE.createLibrary();
		resource.getContents().add(lib);
		
		
		//Library lib2 = LibraryFactory.eINSTANCE.createLibrary();
	//	resource.getContents().add(lib2);
		
		
		//create book, make some (untracked) changes to book
	 //   Book book = LibraryFactory.eINSTANCE.createBook();
	  //  book.setIdNumber(666);
	  //  book.setName("Harry Potter and the MODE");
	    
	    //Add book to library
	  //  lib.getBooks().add(book);
	    
	 
	    //add library to contents
	    //resource.getContents().add(lib);
	    
	    //changes made to lib or book past this point are tracked...
		
		//resource.save(new FileOutputStream(new File("library.xmi")),null);

		//Map<String, String> saveOptions = new HashMap<String, String>();
		//saveOptions.put("FILE_SAVE_LOCATION", fileSaveLocation);
		
		//resource.save(null);
	    
	}

}
