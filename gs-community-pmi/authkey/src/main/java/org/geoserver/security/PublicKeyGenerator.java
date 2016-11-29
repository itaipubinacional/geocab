package org.geoserver.security;

import org.apache.commons.codec.digest.DigestUtils;

public class PublicKeyGenerator {
	/**
	 * 
	 * @param username
	 * @return
	 */
	public static String generateKey( String username ){
		return DigestUtils.md5Hex(username + "320bbf6f702da59b1af3ee7b3a5359f1");
	}
}
