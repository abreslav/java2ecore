/**
 * <copyright>
 * </copyright>
 *
 * $Id$
 */
package another_model;

import org.eclipse.emf.ecore.EFactory;

/**
 * <!-- begin-user-doc -->
 * The <b>Factory</b> for the model.
 * It provides a create method for each non-abstract class of the model.
 * <!-- end-user-doc -->
 * @see another_model.Another_modelPackage
 * @generated
 */
public interface Another_modelFactory extends EFactory {
	/**
	 * The singleton instance of the factory.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	Another_modelFactory eINSTANCE = another_model.impl.Another_modelFactoryImpl.init();

	/**
	 * Returns a new object of class '<em>Sample Class</em>'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return a new object of class '<em>Sample Class</em>'.
	 * @generated
	 */
	SampleClass createSampleClass();

	/**
	 * Returns the package supported by this factory.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the package supported by this factory.
	 * @generated
	 */
	Another_modelPackage getAnother_modelPackage();

} //Another_modelFactory
