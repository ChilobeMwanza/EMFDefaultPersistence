/**
 */
package library;

import org.eclipse.emf.common.util.EList;

/**
 * <!-- begin-user-doc -->
 * A representation of the model object '<em><b>Module</b></em>'.
 * <!-- end-user-doc -->
 *
 * <p>
 * The following features are supported:
 * </p>
 * <ul>
 *   <li>{@link library.Module#getRegisteredStudents <em>Registered Students</em>}</li>
 * </ul>
 *
 * @see library.LibraryPackage#getModule()
 * @model
 * @generated
 */
public interface Module extends NamedElement {
	/**
	 * Returns the value of the '<em><b>Registered Students</b></em>' reference list.
	 * The list contents are of type {@link library.Student}.
	 * It is bidirectional and its opposite is '{@link library.Student#getRegisteredModules <em>Registered Modules</em>}'.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Registered Students</em>' reference list isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Registered Students</em>' reference list.
	 * @see library.LibraryPackage#getModule_RegisteredStudents()
	 * @see library.Student#getRegisteredModules
	 * @model opposite="registeredModules"
	 * @generated
	 */
	EList<Student> getRegisteredStudents();

} // Module
