package com.example.database;

import android.provider.BaseColumns;

/**
 * 
 * 2015-10-19
 */
public final class DBUser {

	public static final class User implements BaseColumns {
		public static final String USERNAME = "username";
		public static final String PASSWORD = "password";
		// public static final String USERTYPE = "usertype";
		public static final String ISSAVED = "issaved";

	}

}
