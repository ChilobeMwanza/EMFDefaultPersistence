/**
 * BUGS/ISSUES
 * 1) Passing file save location to resource via uri creates a new file,this
 * 	  breaks the mechanism which allows us to append to files (As opposed to 
 *    creating a new file each time). Same applies if you do resource.save(new FileOutputStream(new File("empty.txt")),null);
 */

package main;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import org.eclipse.emf.common.util.URI;
//import org.eclipse.emf.common.notify.AdapterFactory;
import org.eclipse.emf.ecore.resource.Resource;
import org.eclipse.emf.ecore.xmi.impl.XMIResourceImpl;

import io.emf.cbp.resource.DeltaResourceImpl;
import library.Book;
import library.Library;
import library.LibraryFactory;

public class App 
{
	//private static String fileSaveLocation ="library.txt";
	
	public static void main(String[] args) throws Exception
	{
		// TODO Auto-generated method stub
		App app = new App();
		//app.loadResource();
		app.createResource();
	}
	
	public void loadResource() throws IOException
	{
		Resource resource = new DeltaResourceImpl(URI.createURI("library.txt"));
		resource.load(null);
		//Resource resource = new DeltaResourceImpl();
		
		//Map<String, String> loadOptions = new HashMap<String, String>();
		//loadOptions.put("FILE_LOCATION", fileSaveLocation);
		
		//resource.load(loadOptions);
	}
	
	public void createResource() throws Exception
	{
		//Resource resource = new XMIResourceImpl();
		
		
		Resource resource = new DeltaResourceImpl(URI.createURI("library.txt"));
		

		
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
		
		resource.save(null);
	    
	}

}
