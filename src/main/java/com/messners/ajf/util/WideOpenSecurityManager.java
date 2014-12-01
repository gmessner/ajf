package com.messners.ajf.util;

import java.io.FileDescriptor;
import java.rmi.RMISecurityManager;
import java.security.Permission;


/**
 * This class provides a wide open SecurityManager for use with RMI.
 *
 * @author  Greg Messner <greg@messners.com>
 */

public class WideOpenSecurityManager extends RMISecurityManager {


	/**
	 * Installs an instance of this class as the System SecurityManager.
	 *
	 * @return  the old SecurityManager
	 */
	public static SecurityManager install () {

		SecurityManager current = System.getSecurityManager();
		System.setSecurityManager(new WideOpenSecurityManager());
		return (current);
	}
		

	public WideOpenSecurityManager () {
		super();
	}


	public synchronized void checkAccept (String host, int port) {}
	public synchronized void checkAwtEventQueueAccess (String host, int port) {}
	public synchronized void checkConnect (String host, int port) {}
	public synchronized void checkConnect (
			String host, int port, Object executionContext) {}
	public synchronized void checkListen (int port) {}
	public synchronized void checkAccess (Thread thread) {}
	public synchronized void checkAccess (ThreadGroup threadgroup) {}
	public synchronized void checkCreateClassLoader () {}
	public synchronized void checkDelete (String filename) {}
	public synchronized void checkExec (String command) {}
	public synchronized void checkExit (int status) {}
	public synchronized void checkLink (String library) {}
	public synchronized void checkMemberAccess (Class<?> clz, int which) {}
	public synchronized void checkPackageAccess (String packageName) {}
	public synchronized void checkPackageDefinition (String packageName) {}
	public synchronized void checkPermission (Permission perm) {}
	public synchronized void checkPermission (
			Permission perm, Object context) {}
	public synchronized void checkPropertiesAccess () {}
	public synchronized void checkPropertyAccess (String key) {}
	public synchronized void checkPropertyAccess (String key, String def) {}
	public synchronized void checkRead (FileDescriptor filedescriptor) {}
	public synchronized void checkRead (String filename) {}
	public synchronized void checkRead (
			String filename, Object executionContext) {}
	public synchronized void checkSecurityAccess (String target) {}
	public synchronized void checkSystemClipboardAccess () {}
	public synchronized void checkWrite (FileDescriptor filedescriptor) {}
	public synchronized void checkWrite (String filename) {}
	public synchronized void checkSetFactory () {}
	public synchronized boolean checkTopLevelWindow (Object window) {
		return (true);
	}
}

