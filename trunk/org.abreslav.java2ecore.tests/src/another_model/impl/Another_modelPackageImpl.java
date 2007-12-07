/**
 * <copyright>
 * </copyright>
 *
 * $Id$
 */
package another_model.impl;

import another_model.Another_modelFactory;
import another_model.Another_modelPackage;
import another_model.SampleClass;
import another_model.SampleEnum;
import another_model.SampleInterface;

import org.eclipse.emf.ecore.EAttribute;
import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.EEnum;
import org.eclipse.emf.ecore.EPackage;

import org.eclipse.emf.ecore.impl.EPackageImpl;

/**
 * <!-- begin-user-doc -->
 * An implementation of the model <b>Package</b>.
 * <!-- end-user-doc -->
 * @generated
 */
public class Another_modelPackageImpl extends EPackageImpl implements Another_modelPackage {
	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	private EClass sampleClassEClass = null;

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	private EClass sampleInterfaceEClass = null;

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	private EEnum sampleEnumEEnum = null;

	/**
	 * Creates an instance of the model <b>Package</b>, registered with
	 * {@link org.eclipse.emf.ecore.EPackage.Registry EPackage.Registry} by the package
	 * package URI value.
	 * <p>Note: the correct way to create the package is via the static
	 * factory method {@link #init init()}, which also performs
	 * initialization of the package, or returns the registered package,
	 * if one already exists.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see org.eclipse.emf.ecore.EPackage.Registry
	 * @see another_model.Another_modelPackage#eNS_URI
	 * @see #init()
	 * @generated
	 */
	private Another_modelPackageImpl() {
		super(eNS_URI, Another_modelFactory.eINSTANCE);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	private static boolean isInited = false;

	/**
	 * Creates, registers, and initializes the <b>Package</b> for this
	 * model, and for any others upon which it depends.  Simple
	 * dependencies are satisfied by calling this method on all
	 * dependent packages before doing anything else.  This method drives
	 * initialization for interdependent packages directly, in parallel
	 * with this package, itself.
	 * <p>Of this package and its interdependencies, all packages which
	 * have not yet been registered by their URI values are first created
	 * and registered.  The packages are then initialized in two steps:
	 * meta-model objects for all of the packages are created before any
	 * are initialized, since one package's meta-model objects may refer to
	 * those of another.
	 * <p>Invocation of this method will not affect any packages that have
	 * already been initialized.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #eNS_URI
	 * @see #createPackageContents()
	 * @see #initializePackageContents()
	 * @generated
	 */
	public static Another_modelPackage init() {
		if (isInited) return (Another_modelPackage)EPackage.Registry.INSTANCE.getEPackage(Another_modelPackage.eNS_URI);

		// Obtain or create and register package
		Another_modelPackageImpl theAnother_modelPackage = (Another_modelPackageImpl)(EPackage.Registry.INSTANCE.getEPackage(eNS_URI) instanceof Another_modelPackageImpl ? EPackage.Registry.INSTANCE.getEPackage(eNS_URI) : new Another_modelPackageImpl());

		isInited = true;

		// Create package meta-data objects
		theAnother_modelPackage.createPackageContents();

		// Initialize created meta-data
		theAnother_modelPackage.initializePackageContents();

		// Mark meta-data to indicate it can't be changed
		theAnother_modelPackage.freeze();

		return theAnother_modelPackage;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EClass getSampleClass() {
		return sampleClassEClass;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EAttribute getSampleClass_X() {
		return (EAttribute)sampleClassEClass.getEStructuralFeatures().get(0);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EClass getSampleInterface() {
		return sampleInterfaceEClass;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EEnum getSampleEnum() {
		return sampleEnumEEnum;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public Another_modelFactory getAnother_modelFactory() {
		return (Another_modelFactory)getEFactoryInstance();
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	private boolean isCreated = false;

	/**
	 * Creates the meta-model objects for the package.  This method is
	 * guarded to have no affect on any invocation but its first.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public void createPackageContents() {
		if (isCreated) return;
		isCreated = true;

		// Create classes and their features
		sampleClassEClass = createEClass(SAMPLE_CLASS);
		createEAttribute(sampleClassEClass, SAMPLE_CLASS__X);

		sampleInterfaceEClass = createEClass(SAMPLE_INTERFACE);

		// Create enums
		sampleEnumEEnum = createEEnum(SAMPLE_ENUM);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	private boolean isInitialized = false;

	/**
	 * Complete the initialization of the package and its meta-model.  This
	 * method is guarded to have no affect on any invocation but its first.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public void initializePackageContents() {
		if (isInitialized) return;
		isInitialized = true;

		// Initialize package
		setName(eNAME);
		setNsPrefix(eNS_PREFIX);
		setNsURI(eNS_URI);

		// Create type parameters

		// Set bounds for type parameters

		// Add supertypes to classes

		// Initialize classes and features; add operations and parameters
		initEClass(sampleClassEClass, SampleClass.class, "SampleClass", !IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS);
		initEAttribute(getSampleClass_X(), ecorePackage.getEInt(), "x", null, 0, 1, SampleClass.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);

		initEClass(sampleInterfaceEClass, SampleInterface.class, "SampleInterface", IS_ABSTRACT, IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS);

		// Initialize enums and add enum literals
		initEEnum(sampleEnumEEnum, SampleEnum.class, "SampleEnum");
		addEEnumLiteral(sampleEnumEEnum, SampleEnum.E1);
		addEEnumLiteral(sampleEnumEEnum, SampleEnum.E2);
		addEEnumLiteral(sampleEnumEEnum, SampleEnum.E3);

		// Create resource
		createResource(eNS_URI);
	}

} //Another_modelPackageImpl
