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
package tests;
import static org.junit.Assert.*;


import java.util.ArrayList;
import java.util.List;


import org.eclipse.emf.ecore.EObject;

import org.eclipse.emf.ecore.util.EcoreUtil;
import org.junit.*;

import university.Department;
import university.Library;
import university.Student;
import university.University;
import university.UniversityFactory;
import university.Vehicle;

public class AddToResourceTests extends TestBase
{
	
	/*
	 * Test if a single object added to resource can be serialised 
	 */
	@Test
    public void testAddSingleToResource() throws Exception 
	{
		/*Create some objects */
		University uni = UniversityFactory.eINSTANCE.createUniversity();
		resource.getContents().add(uni);
		
		saveAndLoadResource();
		
		assertTrue(EcoreUtil.equals(savedContentsList, loadedContentsList));
    }
	
	/*
	 * Test if multiple single objects added to resource can be serialised
	 */
	@Test
	public void testAddSingleRepeatedToResource()
	{
		University uni1 = UniversityFactory.eINSTANCE.createUniversity();
		University uni2 = UniversityFactory.eINSTANCE.createUniversity();
		Library lib1 = UniversityFactory.eINSTANCE.createLibrary();
		Department dep1 = UniversityFactory.eINSTANCE.createDepartment();
		
		resource.getContents().add(uni1);
		resource.getContents().add(uni2);
		resource.getContents().add(lib1);
		resource.getContents().add(dep1);
		
		saveAndLoadResource();
		
		assertTrue(EcoreUtil.equals(savedContentsList, loadedContentsList));
	}
	
	/*
	 * Test if a list of eObjects can be added to the resource
	 */
	@Test
	public void testAddListToResource()
	{
		University uni1 = UniversityFactory.eINSTANCE.createUniversity();
		University uni2 = UniversityFactory.eINSTANCE.createUniversity();
		Library lib1 = UniversityFactory.eINSTANCE.createLibrary();
		Department dep1 = UniversityFactory.eINSTANCE.createDepartment();
		
		List <EObject> list = new ArrayList<EObject>();
		list.add(uni1);
		list.add(uni2);
		list.add(lib1);
		list.add(dep1);
		
		resource.getContents().addAll(list);
		
		saveAndLoadResource();
		
		assertTrue(EcoreUtil.equals(savedContentsList, loadedContentsList));
	}
	
	/*
	 * Tests all the different ways of adding items to the resource
	 */
	@Test
	public void testAllAddToResource()
	{
		University uni1 = UniversityFactory.eINSTANCE.createUniversity();
		University uni2 = UniversityFactory.eINSTANCE.createUniversity();
		Vehicle v1 = UniversityFactory.eINSTANCE.createVehicle();
		
		resource.getContents().add(uni1);
		resource.getContents().add(uni2);
		resource.getContents().add(v1);
		
		List<EObject> list = new ArrayList<EObject>();
		Library lib1 = UniversityFactory.eINSTANCE.createLibrary();
		Library lib2 = UniversityFactory.eINSTANCE.createLibrary();
		Department d1 = UniversityFactory.eINSTANCE.createDepartment();
		Student s1 = UniversityFactory.eINSTANCE.createStudent();
		
		list.add(lib1);
		list.add(lib2);
		list.add(d1);
		list.add(s1);
		
		resource.getContents().addAll(list);
		
		saveAndLoadResource();
		
		assertTrue(EcoreUtil.equals(savedContentsList, loadedContentsList));
	}
	
	@Test
	public void testAddManyToResource()
	{
		for(int i = 0; i < 10000; i++)
		{
			EObject obj = UniversityFactory.eINSTANCE.createUniversity();
			resource.getContents().add(obj);
			
			obj = UniversityFactory.eINSTANCE.createBook();
			resource.getContents().add(obj);
			
			obj = UniversityFactory.eINSTANCE.createDepartment();
			resource.getContents().add(obj);
		}
		
		List<EObject> list = new ArrayList<EObject>();
		
		for(int i = 0; i <1000; i++)
		{
			EObject obj = UniversityFactory.eINSTANCE.createLibrary();
			list.add(obj);
			
			obj = UniversityFactory.eINSTANCE.createModule();
			list.add(obj);
			
			obj = UniversityFactory.eINSTANCE.createStaffMember();
			list.add(obj);
		}
		
		resource.getContents().addAll(list);
		
		saveAndLoadResource();
		
		assertTrue(EcoreUtil.equals(savedContentsList, loadedContentsList));
	}
}
