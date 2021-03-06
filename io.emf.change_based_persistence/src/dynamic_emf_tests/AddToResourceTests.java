/*
 * Explicitly tests the ADD_R entry (which adds items directly to the resource), 
 * create entry is implicitly tested.
 * 
 * Resource contents are cleared before each test run. Each test run is independent.
 * 
 * The save file is deleted before each test run. 
 * 
 * This class does not test resume functionality (i.e make model 1, persist model 1, load model 1, make additional changes 
 * to loaded model, persist it, e.t.c)
 */
package dynamic_emf_tests;

import static org.junit.Assert.assertTrue;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.eclipse.emf.common.util.URI;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.resource.Resource;
import org.eclipse.emf.ecore.util.EcoreUtil;
import org.junit.After;
import org.junit.Test;

import impl.CBPTextResourceImpl;

public class AddToResourceTests extends TestBase
{
	private List<EObject> savedContentsList = null;
	private List<EObject> loadedContentsList = null;
	
	@After
	public void runAfterTestMethod()
	{
		savedContentsList = null;
		loadedContentsList = null;
	}
	
	/*
	 * Test if a single object added to resource can be serialised 
	 */
	@Test
    public void testAddSingleToResource() throws Exception 
	{
		Resource res = new CBPTextResourceImpl(URI.createURI(FILE_SAVE_LOCATION),ePackage);
		
		EObject uni = createEObject("University");
		res.getContents().add(uni);
		
		res.save(null);
	
		savedContentsList = getResourceContentsList(res);
		
		Resource loadedRes = loadResource();
		
		loadedContentsList = getResourceContentsList(loadedRes);
		
		assertTrue(EcoreUtil.equals(savedContentsList, loadedContentsList));
    }
	
	/*
	 * Test if multiple single objects added to resource can be serialised
	 */
	@Test
	public void testAddSingleRepeatedToResource() throws IOException
	{
		Resource res = new CBPTextResourceImpl(URI.createURI(FILE_SAVE_LOCATION),ePackage);
		
		EObject uni1 = createEObject("University");
		EObject uni2 = createEObject("University");
		EObject lib1 = createEObject("Library");
		EObject dep1 = createEObject("Library");
		
		res.getContents().add(uni1);
		res.getContents().add(uni2);
		
		res.getContents().add(lib1);
		res.getContents().add(dep1);
		
		res.save(null);
		
		savedContentsList = getResourceContentsList(res);
		
		Resource loadedRes = loadResource();
		
		loadedContentsList = getResourceContentsList(loadedRes);
		
		assertTrue(EcoreUtil.equals(savedContentsList, loadedContentsList));
	}
	
	/*
	 * Test if a list of eObjects can be added to the resource
	 */
	@Test
	public void testAddListToResource() throws IOException
	{
		Resource res = new CBPTextResourceImpl(URI.createURI(FILE_SAVE_LOCATION),ePackage);
		
		EObject uni1 = createEObject("University");
		EObject uni2 = createEObject("University");
		EObject lib1 = createEObject("Library");
		EObject dep1 = createEObject("Library");
		
		List <EObject> list = new ArrayList<EObject>();
		list.add(uni1);
		list.add(uni2);
		list.add(lib1);
		list.add(dep1);
		
		res.getContents().addAll(list);
		
		res.save(null);
		
		savedContentsList = getResourceContentsList(res);
		
		Resource loadedRes = loadResource();
		
		loadedContentsList = getResourceContentsList(loadedRes);
		
		assertTrue(EcoreUtil.equals(savedContentsList, loadedContentsList));
	}
	
	/*
	 * Tests all the different ways of adding items to the resource
	 */
	@Test
	public void testAllAddToResource() throws IOException
	{
		Resource res = new CBPTextResourceImpl(URI.createURI(FILE_SAVE_LOCATION),ePackage);
		
		EObject uni1 = createEObject("University");
		EObject uni2 = createEObject("University");
		EObject v1 = createEObject("Vehicle");
		
		res.getContents().add(uni1);
		res.getContents().add(uni2);
		res.getContents().add(v1);
		
		List<EObject> list = new ArrayList<EObject>();
		EObject lib1 = createEObject("Library");
		EObject lib2 = createEObject("Library");
		EObject d1 = createEObject("Department");
		EObject s1 = createEObject("Department");
		
		list.add(lib1);
		list.add(lib2);
		list.add(d1);
		list.add(s1);
		
		res.getContents().addAll(list);
		
		res.save(null);
		
		savedContentsList = getResourceContentsList(res);
		
		Resource loadedRes = loadResource();
		
		loadedContentsList = getResourceContentsList(loadedRes);
		
		assertTrue(EcoreUtil.equals(savedContentsList, loadedContentsList));
	}
	
	@Test
	public void testAddManyToResource() throws IOException
	{
		Resource res = new CBPTextResourceImpl(URI.createURI(FILE_SAVE_LOCATION),ePackage);
		
		for(int i = 0; i < 10000; i++)
		{
			EObject obj = createEObject("University");
			res.getContents().add(obj);
			
			obj = createEObject("Book");
			res.getContents().add(obj);
			
			obj = createEObject("Department");
			res.getContents().add(obj);
		}
		
		List<EObject> list = new ArrayList<EObject>();
		
		for(int i = 0; i <1000; i++)
		{
			EObject obj = createEObject("Library");
			list.add(obj);
			
			obj = createEObject("Department");
			list.add(obj);
			
			obj = createEObject("StaffMember");
			list.add(obj);
		}
		
		res.getContents().addAll(list);
		
		res.save(null);
		
		savedContentsList = getResourceContentsList(res);
		
		Resource loadedRes = loadResource();
		
		loadedContentsList = getResourceContentsList(loadedRes);
		
		assertTrue(EcoreUtil.equals(savedContentsList, loadedContentsList));
	}
	
	
}
