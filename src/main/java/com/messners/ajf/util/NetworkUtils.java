package com.messners.ajf.util;

import java.net.InetAddress;
import java.net.UnknownHostException;


/**
 * Provides common network utilities.
 */
public class NetworkUtils {
	
   /** Masks used to get bit values in a byte. */
   private static final byte[] BYTE_MASK = { 0x7f, 0x3f, 0x1f, 0x0f,
         0x07, 0x03, 0x01, 0x00 };

   /**
    * This class cannot be instantiated, hide the the no-args constructor
    * by declaring it private.
    */
   private NetworkUtils () { }

   /**
    * Given an InetAddress instance that specifies a network mask and an 
    * InetAddress instance for a specific host figure out whether the host is 
    * included in the network mask.
    *
    * @param host Host address.
    * @param network Network address (hint: use getNetwork() method).
    * @param mask Raw subnet mask (hint: use getSubnetMask() method).
    */
   public static boolean isOnNetwork (InetAddress host, InetAddress network, byte[] mask) {

      boolean result = true;
      byte[] hostAddr = host.getAddress();
      byte[] networkAddr = network.getAddress();

      for (int i = 0; i < networkAddr.length; i++) {

         if ((hostAddr[i] & mask[i]) != networkAddr[i]) {
            result = false;
            break;
         }
      }
      return result;
   }


   /**
    * Given a stating InetAddress and an ending InetAddress compute an 
    * InetAddress instance that will be used as the network for the 
    * isOnNetwork() method.
    *
    * @param start Starting address.
    * @param end Ending address.
    * @return Address of the network containing start and end, null if
    * there was an error.
    */
   public static InetAddress getNetwork (InetAddress start, InetAddress end) {

      byte[] mask = getSubnetMask(start, end);
      byte[] startAddr = start.getAddress();
      byte[] networkAddr = new byte[mask.length];
      
      for (int i = 0; i < networkAddr.length; i++) {
         
         networkAddr[i] = (byte) (startAddr[i] & mask[i]);
      }
      
      InetAddress result = null;
      try {
         result = InetAddress.getByAddress(networkAddr);
      } catch (Exception e) { 
         result = null;
      }
      return result;
   }


   /**
    * Given a starting InetAddress and an ending InetAddress compute a 
    * raw subnet mask that will be used as the mask for the isOnNetwork()
    * method.
    * 
    * We use the following heuristic approach to calculating the subnet mask:
    * If the bits of the addresses are the same (start[0] = 0 and end[0] = 0; 
    * likewise start[0] = 1 and end[0] = 1), then the corresponding bit
    * of the subnet mask is 1; otherwise the corresponding bit of the subnet
    * mask is 0.  We evaluate each bit of the start and end addresses until
    * we find one that is different.  After this point, the rest of the 
    * subnet mask is 0 (represents the host part of the subnet mask).
    *
    * @param start Starting address.
    * @param end Ending address.
    * @return Raw subnet address.
    */
   public static byte[] getSubnetMask (InetAddress start, InetAddress end) {

      byte[] startAddr = start.getAddress();
      byte[] endAddr = end.getAddress();
      byte[] maskAddr = new byte[startAddr.length]; 

      for (int i = 0; i < maskAddr.length; i++) {
        
         for (int j = 0; j < BYTE_MASK.length; j++) {

            /*
             * Flip the bits of the mask (so instead of 0111, we have 1000)
             * to make the logic easier to follow.
             */
            byte byteMask = (byte) ~BYTE_MASK[j];

            byte startAddrMask = (byte) (startAddr[i] & byteMask);
            byte endAddrMask = (byte) (endAddr[i] & byteMask);
            if (startAddrMask != endAddrMask) {
               // End of network part of subnet, rest should already be zeros.
               return maskAddr;
            }
            maskAddr[i] = (byte) (maskAddr[i] | byteMask);
         }
      }
      return maskAddr;
   }


	/**
	 * Computes a long value for the given hostname (or dot notated IP
	 * address). If <code>host</code> == null the address for the
	 * local host will be returned.
	 *
	 * @param  host  the hostname or dot notated IP address to get
	 * the long IP address for
	 * @return the long IP address for the host or -1 on error
	 */
	public static final long getInetAddress (String host) {

		java.net.InetAddress inaddr;
		try {

			if (host == null) {
				inaddr = java.net.InetAddress.getLocalHost();
			} else {
				inaddr = java.net.InetAddress.getByName(host);
			}

		} catch (java.net.UnknownHostException une) {
			return (-1);
		}


		byte ip[] = inaddr.getAddress();
		int numBytes = ip.length;
		long addr = 0;
		for (int i = 0; i < numBytes; i++) {
			long b = (ip[i] < 0 ? ip[i] + 256 : ip[i]);
			addr += (b << (8 * (numBytes - i - 1)));
		}

		return (addr);
	}


   /**
    * Checks if a string is an IP address instead of a host name.
    *
    * @param host String to check.
    * @return True if <I>host</I> is an IP address.
    */
   public static boolean isIpAddress (String host) {

      /* 
       * Conditions for raw IP address:
       *     1.  All characters in host are numeric or period.
       *     2.  There must be 3 periods in host.
       */
      int periodCount = 0;
      boolean addrIsIp = isMaybeIpAddress(host, periodCount);
      return (addrIsIp && (periodCount == 3));
   }


   /**
    * Converts a string in IP address format to a raw ip address.
    *
    * @param host String to convert.
    * @return Raw IP address.  <code>null</code> if host is invalid.
    */
   public static byte[] toRawIpAddress (String host) {

      try {
         String[] nums = host.split("\\.", 4);
         // TBD:  Byte.parseByte throws exception when value > 127.
         byte[] rawAddr = { Byte.parseByte(nums[0]), Byte.parseByte(nums[1]), 
                            Byte.parseByte(nums[2]), Byte.parseByte(nums[3]) };
         return rawAddr;
      } catch(NumberFormatException e) {
         e.printStackTrace();
      }
      return null;
   }


   /**
    * Converts a hostname or IP address into a InetAddress.
    *
    * @param host Hostname or IP address.
    * @return InetAddress object.  <code>null</code> if host is invalid.
    */
   public static InetAddress toInetAddress (String host) {

      InetAddress addr = null;
      if (host.length() > 0) {
         try {
            addr = InetAddress.getByName(host);
            /*
             * If the address is numeric, check if it's canonical host
             * name is equal to it's host name.  This prevents the
             * case where Java resolves 192.168.1 as 192.168.0.1, 
             * hence creating a valid InetAddress when it shouldn't.
             */
            int periodCount = 0;
            if (isMaybeIpAddress(host, periodCount)) {
               String canHostNm = addr.getCanonicalHostName();
               String hostNm = addr.getHostName();
               if (!canHostNm.equals(hostNm)) {
                  addr = null; // Invalid address.
               }
            }
         } catch (UnknownHostException e) {
            addr = null;
         } catch (SecurityException e) {
            addr = null;
         }
      }
      return addr;
   }


   /**
    * Gets the host name for the local machine.
    *
    * @return A string with the local hostname or null on error.
    */
   public static String getLocalHostName () {

      java.net.InetAddress addr = null;
      try {
         addr = java.net.InetAddress.getLocalHost();
      } catch (Exception ignore) {
         return (null);
      }

      return (addr.getHostName());
   }
   

   /**
    * Checks whether a string might be an IP address instead of a host name.
    *
    * @param host String to check.
    * @param periodCount Populated with the number of periods encountered.
    * @return True if <I>host</I> is well formed (e.g. contains only
    * numbers and periods.
    */
   private static boolean isMaybeIpAddress (String host, int periodCount) {

      char[] hostChars = host.toCharArray();
      periodCount = 0;
      boolean addrIsIp = true;
      for (int i=0; i < host.length(); i++) {
         if (!Character.isDigit(hostChars[i])) {
            if ((periodCount < 3) && (hostChars[i] == '.')) {
               periodCount++;
            } else {
               addrIsIp = false;
               break;
            }
         }
      }
      return addrIsIp;
   }
}
