package change;

import java.util.UUID;

import org.eclipse.emf.ecore.EObject;

public abstract class AbstractEntry implements Entry
{
	private EObject eObject;
	private UUID id;
	
	public AbstractEntry(EObject eObject, UUID id)
	{
		this.eObject = eObject;
		this.id = id;
	}
	
	public EObject getEObject()
	{
		return eObject;
	}
	
	public UUID getUUID()
	{
		return id;
	}
	
}