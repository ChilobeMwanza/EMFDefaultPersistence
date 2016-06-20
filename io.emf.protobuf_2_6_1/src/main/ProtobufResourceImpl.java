package main;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Map;

import org.eclipse.emf.common.util.URI;
import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.EPackage;
import org.eclipse.emf.ecore.resource.impl.ResourceImpl;

import library.Library;

public class ProtobufResourceImpl extends ResourceImpl
{
	public ProtobufResourceImpl(URI uri)
	{
		super(uri);
	}
	
	@Override
	protected void doSave(OutputStream outputStream, Map<?, ?> options) throws IOException
	{
		for(EObject eObject : getContents())
		{
			EClass Eclass = eObject.eClass();
		}
	}
	
	@Override 
	protected void doLoad(InputStream inputStream, Map<?, ?> options) throws IOException
	{
		
	}
}
