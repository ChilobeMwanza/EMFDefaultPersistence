package tests;

import org.junit.runner.RunWith;
import org.junit.runners.Suite;

@RunWith(Suite.class)
@Suite.SuiteClasses({
    AddToResourceTests.class, 
    DeleteFromResourceTests.class,
    GeneralSaveTests.class,
    SetUnsetReferenceTests.class,
    SetUnsetAttributeTests.class
    
})
public class TestSuite 
{

}
