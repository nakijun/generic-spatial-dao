package org.genericspatialdao.utils;

import org.apache.commons.lang.StringUtils;

public class ConstantsUtils {

	public static final String SPACE = " ";
	public static final String DOT = ".";
	public static final String DOT_SPACE = DOT + SPACE;
	public static final String DOUBLE_DOT = ":";
	public static final String COLON = ":";
	public static final String COLON_SPACE = COLON + SPACE;
	public static final String SEMI_COLON = ";";
	public static final String SEMI_COLON_SPACE = SEMI_COLON + SPACE;
	public static final String SLASH = "/";
	public static final String REVERSE_SLASH = "\\";
	public static final String LEFT_BRACE = "{";
	public static final String RIGHT_BRACE = "}";
	public static final String LEFT_PARENTHESES = "(";
	public static final String RIGHT_PARENTHESES = ")";

	// root default
	public static final String DEFAULT_PERSISTENCE_UNIT = "default";
	// default by user
	public static final String DEFAULT_APPLICATION_PERSISTENCE_UNIT;
	public static final boolean AUTO_BEGIN_TRANSACTION;
	public static final boolean AUTO_COMMIT;
	public static final boolean AUTO_ROLLBACK;

	// properties
	private static final String PERSISTENCEUNIT_DEFAULT_PROPERTY = "persistenceunit.default";
	private static final String BEGINTRANSACTION_AUTO_PROPERTY = "begintransaction.auto";
	private static final String COMMIT_AUTO_PROPERTY = "commit.auto";
	private static final String ROLLBACK_AUTO_PROPERTY = "rollback.auto";

	static {
		String defaultPersistenceUnit = PropertiesUtils
				.getString(PERSISTENCEUNIT_DEFAULT_PROPERTY);
		if (StringUtils.isBlank(defaultPersistenceUnit)) {
			defaultPersistenceUnit = DEFAULT_PERSISTENCE_UNIT;
		}
		DEFAULT_APPLICATION_PERSISTENCE_UNIT = defaultPersistenceUnit;

		String autoBeginTransactionString = PropertiesUtils
				.getString(BEGINTRANSACTION_AUTO_PROPERTY);
		if (StringUtils.isBlank(autoBeginTransactionString)
				|| StringUtils.equalsIgnoreCase(autoBeginTransactionString,
						Boolean.TRUE.toString())) {
			AUTO_BEGIN_TRANSACTION = true;
		} else {
			AUTO_BEGIN_TRANSACTION = false;
		}

		String autoCommitString = PropertiesUtils
				.getString(COMMIT_AUTO_PROPERTY);
		if (StringUtils.isBlank(autoCommitString)
				|| StringUtils.equalsIgnoreCase(autoCommitString,
						Boolean.TRUE.toString())) {
			AUTO_COMMIT = true;
		} else {
			AUTO_COMMIT = false;
		}

		String autoRollbackString = PropertiesUtils
				.getString(ROLLBACK_AUTO_PROPERTY);
		if (StringUtils.isBlank(autoRollbackString)
				|| StringUtils.equalsIgnoreCase(autoRollbackString,
						Boolean.TRUE.toString())) {
			AUTO_ROLLBACK = true;
		} else {
			AUTO_ROLLBACK = false;
		}
	}
}
