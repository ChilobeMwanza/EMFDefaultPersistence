package dynamic_emf_tests;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.io.File;
import java.io.IOException;
import java.util.List;

import org.eclipse.emf.common.util.URI;
import org.eclipse.emf.ecore.EAttribute;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.resource.Resource;
import org.eclipse.emf.ecore.resource.impl.ResourceSetImpl;
import org.eclipse.emf.ecore.util.EcoreUtil;
import org.junit.After;
import org.junit.Test;

import impl.CBPTextResourceImpl;


public class GeneralSaveTests extends TestBase 
{
	 private List<EObject> savedContentsList = null;
	 private List<EObject> loadedContentsList = null;
	
	 @After
	 public void runAfterTestMethod()
	 {
		 savedContentsList = null;
		 loadedContentsList = null;
	 }
	/*Calling save() serialises all monitored changes (notifications) and clears these
	 * Notifications from the event list. Calling save repeatedly without having made
	 * any changes to the model should not result in subsequent modifications of the save
	 * file.
	 */
	@Test
	public void testRepeatedSaveNoModification() throws IOException
	{
		Resource res = new CBPTextResourceImpl(URI.createURI(FILE_SAVE_LOCATION),ePackage);
		
		EObject uni = createEObject("University");
		
		res.getContents().add(uni);
		
		//perform first save
		res.save(null);
		
		//save last modified time
		File file = new File(FILE_SAVE_LOCATION);
		Long timeStamp = file.lastModified();
		
		//perform further saves without modifiying the resource
		res.save(null);
		res.save(null);
		
		//check that the file has not been modified
		assertTrue(timeStamp == file.lastModified());
	}
	
	/*
	 * Test that making initial modifications, calling save, making more modifications,
	 *  results in the save file being modified accordingly.
	 */
	@Test
	public void testMultipleSaveWithModifications() throws IOException, InterruptedException
	{
		Resource res = new CBPTextResourceImpl(URI.createURI(FILE_SAVE_LOCATION),ePackage);
		
		EObject uni = createEObject("University");
		res.getContents().add(uni);
		
		//perform first save
		res.save(null);
		
		//save last modified time
		File file = new File(FILE_SAVE_LOCATION);

		Long timeStamp = file.lastModified();
		
		//make further modifications
		EAttribute uniNameAttr = (EAttribute)uni.eClass().getEStructuralFeature("name");
		uni.eSet(uniNameAttr, "York University");
		
		//wait a little before save to ensure modification does not happen too quickly
		Thread.sleep(10);
		
		res.save(null);
		
		//check that the file has been modified
		assertFalse(timeStamp == file.lastModified());
	}
	
	/*
	 * Test that saved object and loaded object are equal
	 */
	@Test
	public void testBasicSaveAndLoad() throws IOException
	{
		Resource res = new CBPTextResourceImpl(URI.createURI(FILE_SAVE_LOCATION),ePackage);
		
		EObject savedUni = createEObject("University");
		
		res.getContents().add(savedUni);
		
		res.save(null);
		
		
		savedContentsList = getResourceContentsList(res);
		
		res.getContents().clear();
		
		Resource loadedRes = loadResource();
		
		loadedContentsList = getResourceContentsList(loadedRes);
		
		assertTrue(EcoreUtil.equals(savedContentsList, loadedContentsList));
	}
	
	/*
	 * Tests that redundant modifications, after load, do not result in changes to the output file
	 */
	@Test
	public void testRedundantModification() throws IOException
	{
		Resource res = new CBPTextResourceImpl(URI.createURI(FILE_SAVE_LOCATION),ePackage);
		
		EObject savedUni = createEObject("University");
		
		res.getContents().add(savedUni);
		
		EAttribute uniNameAttr = (EAttribute)savedUni.eClass().getEStructuralFeature("name");
		savedUni.eSet(uniNameAttr, "York Uni");
		
		res.save(null);
		
		File file = new File(FILE_SAVE_LOCATION);
		Long timeStamp = file.lastModified();
		
		
		//Load in the resource
	
		
		Resource loadedRes = loadResource();
		
		loadedRes.load(null);
		
		EObject loadedUni = res.getContents().get(0);
		loadedUni.eSet(uniNameAttr, "York Uni");
		
		//check that the file has not been modified
		assertTrue(timeStamp == file.lastModified());
	}
}
