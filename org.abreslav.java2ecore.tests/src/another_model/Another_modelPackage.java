/**
 * <copyright>
 * </copyright>
 *
 * $Id$
 */
package another_model;

import org.eclipse.emf.ecore.EAttribute;
import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.EEnum;
import org.eclipse.emf.ecore.EPackage;

/**
 * <!-- begin-user-doc -->
 * The <b>Package</b> for the model.
 * It contains accessors for the meta objects to represent
 * <ul>
 *   <li>each class,</li>
 *   <li>each feature of each class,</li>
 *   <li>each enum,</li>
 *   <li>and each data type</li>
 * </ul>
 * <!-- end-user-doc -->
 * @see another_model.Another_modelFactory
 * @model kind="package"
 * @generated
 */
public interface Another_modelPackage extends EPackage {
	/**
	 * The package name.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	String eNAME = "another_model";

	/**
	 * The package namespace URI.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	String eNS_URI = "http://import.com";

	/**
	 * The package namespace name.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	String eNS_PREFIX = "imp";

	/**
	 * The singleton instance of the package.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	Another_modelPackage eINSTANCE = another_model.impl.Another_modelPackageImpl.init();

	/**
	 * The meta object id for the '{@link another_model.impl.SampleClassImpl <em>Sample Class</em>}' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see another_model.impl.SampleClassImpl
	 * @see another_model.impl.Another_modelPackageImpl#getSampleClass()
	 * @generated
	 */
	int SAMPLE_CLASS = 0;

	/**
	 * The feature id for the '<em><b>X</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int SAMPLE_CLASS__X = 0;

	/**
	 * The number of structural features of the '<em>Sample Class</em>' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int SAMPLE_CLASS_FEATURE_COUNT = 1;

	/**
	 * The meta object id for the '{@link another_model.SampleInterface <em>Sample Interface</em>}' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see another_model.SampleInterface
	 * @see another_model.impl.Another_modelPackageImpl#getSampleInterface()
	 * @generated
	 */
	int SAMPLE_INTERFACE = 1;

	/**
	 * The number of structural features of the '<em>Sample Interface</em>' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int SAMPLE_INTERFACE_FEATURE_COUNT = 0;

	/**
	 * The meta object id for the '{@link another_model.SampleEnum <em>Sample Enum</em>}' enum.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see another_model.SampleEnum
	 * @see another_model.impl.Another_modelPackageImpl#getSampleEnum()
	 * @generated
	 */
	int SAMPLE_ENUM = 2;


	/**
	 * Returns the meta object for class '{@link another_model.SampleClass <em>Sample Class</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for class '<em>Sample Class</em>'.
	 * @see another_model.SampleClass
	 * @generated
	 */
	EClass getSampleClass();

	/**
	 * Returns the meta object for the attribute '{@link another_model.SampleClass#getX <em>X</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the attribute '<em>X</em>'.
	 * @see another_model.SampleClass#getX()
	 * @see #getSampleClass()
	 * @generated
	 */
	EAttribute getSampleClass_X();

	/**
	 * Returns the meta object for class '{@link another_model.SampleInterface <em>Sample Interface</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for class '<em>Sample Interface</em>'.
	 * @see another_model.SampleInterface
	 * @generated
	 */
	EClass getSampleInterface();

	/**
	 * Returns the meta object for enum '{@link another_model.SampleEnum <em>Sample Enum</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for enum '<em>Sample Enum</em>'.
	 * @see another_model.SampleEnum
	 * @generated
	 */
	EEnum getSampleEnum();

	/**
	 * Returns the factory that creates the instances of the model.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the factory that creates the instances of the model.
	 * @generated
	 */
	Another_modelFactory getAnother_modelFactory();

	/**
	 * <!-- begin-user-doc -->
	 * Defines literals for the meta objects that represent
	 * <ul>
	 *   <li>each class,</li>
	 *   <li>each feature of each class,</li>
	 *   <li>each enum,</li>
	 *   <li>and each data type</li>
	 * </ul>
	 * <!-- end-user-doc -->
	 * @generated
	 */
	interface Literals {
		/**
		 * The meta object literal for the '{@link another_model.impl.SampleClassImpl <em>Sample Class</em>}' class.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @see another_model.impl.SampleClassImpl
		 * @see another_model.impl.Another_modelPackageImpl#getSampleClass()
		 * @generated
		 */
		EClass SAMPLE_CLASS = eINSTANCE.getSampleClass();

		/**
		 * The meta object literal for the '<em><b>X</b></em>' attribute feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EAttribute SAMPLE_CLASS__X = eINSTANCE.getSampleClass_X();

		/**
		 * The meta object literal for the '{@link another_model.SampleInterface <em>Sample Interface</em>}' class.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @see another_model.SampleInterface
		 * @see another_model.impl.Another_modelPackageImpl#getSampleInterface()
		 * @generated
		 */
		EClass SAMPLE_INTERFACE = eINSTANCE.getSampleInterface();

		/**
		 * The meta object literal for the '{@link another_model.SampleEnum <em>Sample Enum</em>}' enum.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @see another_model.SampleEnum
		 * @see another_model.impl.Another_modelPackageImpl#getSampleEnum()
		 * @generated
		 */
		EEnum SAMPLE_ENUM = eINSTANCE.getSampleEnum();

	}

} //Another_modelPackage
