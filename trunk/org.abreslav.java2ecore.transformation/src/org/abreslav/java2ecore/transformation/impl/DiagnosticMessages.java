package org.abreslav.java2ecore.transformation.impl;

import org.abreslav.java2ecore.transformation.diagnostics.IDiagnosticMessage;

public enum DiagnosticMessages implements IDiagnosticMessage {
	OPPOSITE_FEATURE_NOT_FOUND("Opposite feature not found"),
	OPPOSITE_FEATURE_HAS_WRONG_TYPE("Opposite feature has wrong type"),
	OPPOSITE_ALREADY_HAS_ANOTHER_OPPOSITE("Opposite reference already has another opposite"),
	NOT_ALLOWED_FOR_ITEM("This annotation is not allowed for %s"),
	ENUMS_DO_NOT_IMPLEMENT_INTERFACES("Ecore enums do not implement interfaces"),
	NOTHING_BUT_LITERALS_ALLOWED_IN_ENUMS("Nothing but literals is allowed in enums"),
	NO_CONTENT_ALLOWED_FOR_EDATATYPES("No content is allowed for EDataTypes"),
	ONLY_ECLASS_MIGHT_BE_A_SUPERTYPE("Only EClass might be a supertype"),
	NESTED_TYPES_ARE_NOT_SUPPORTED_BY_ECORE("Nested types are not supported by Ecore"),
	ONLY_ONE_BODY_CLASS_IS_ALLOWED("Only one body class is allowed"),
	ALL_THE_FEATURES_MUST_BE_SPECIFIED_IN_A_BODY_CLASS("If a body class is present, all the features must be specified in this type"),
	ONLY_ONE_FEATURE_PER_DECLARATION("Only one feature per declaration is allowed"),
	SPECIFY_DIMENSIONS_AT_THE_TYPE("Specify array dimensions at the field type"),
	CONFLICT_WITH_NO_DEFAULT_VALUE("This conflicts with @NoDefaultValue"),
	DEFAULT_VALUE_IS_SPECIFIED_BY_AN_INITIALIZER("Default value is specified by an initializer"),
	NON_CONSTANT_VALUES_ARE_NOT_ALLOWED("Non-constant values are not allowed"),
	ATTRIBUTE_SPECIFIED_BY_FEATURE_TYPE("%s is specified by feature type"),
	WRONG_BOUNDS_ARRAY_CONTENT("Bounds array must contain two integer values"),
	LOWERBOUND_CANNOT_BE_INFINITE("Lower bound cannot be infinite or unspecified"),
	LOWERBOUND_GREATER_THAN_UPPERBOUND("Lower bound %d is greater than upper bound %d"),
	MULTIDIM_ARRAYS_ARE_NOT_SUPPORTED("Multidimensional arrays are not supported"),
	RAW_COLLECTION_TYPE_WILL_BE_WRAPPED("Raw collection type will be wrapped into a simple EDatatType"),
	WRONG_NUMBER_FORMAT("Wrong number format. %s"),
	NO_PACKAGE_CLASS("No package class found"),
	ONE_TOPLEVEL_CLASS_PER_PACKAGE("All the types must be contained in a package. Only one top-level package might be declared in a compilation unit"),
	ROOT_PACKAGE_TYPE_CANNOT_BE_NON_MODEL("Root package type cannot be non-model"),
	TOP_LEVEL_TYPE_MUST_BE_EPACKAGE("This type must declare @EPackage annotation"),
	;
	
	private final String myTemplate;

	private DiagnosticMessages(String messageTemplate) {
		myTemplate = messageTemplate;
	}

	@Override
	public String toString() {
		return myTemplate;
	}
	
	public String format(Object... args) {
		return String.format(myTemplate, args);
	}
}
