package com.messners.ajf.util;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.Writer;


/**
 * This class provides static methods for Base64 encoding/decoding of data
 * and IO streams.
 *
 * @author  Greg Messner <greg@messners.com>
 */
public class Base64Codec {

	/**
	 * Maps a byte to a valid Base64 byte.
	 */
	protected static byte[] encodeMap = 
	{
		65, 66, 67, 68, 69, 70, 71,
		72, 73, 74, 75, 76, 77, 78,
		79, 80, 81, 82, 83, 84, 85,
		86, 87, 88, 89, 90, 
	
		 97,  98,  99, 100, 101, 102, 103,
		104, 105, 106, 107, 108, 109, 110,
		111, 112, 113, 114, 115, 116, 117,
		118, 119, 120, 121, 122,

		48, 49, 50, 51, 52, 53, 54, 55, 56, 57,

		43, 47
	};
	
	/**
	 * Used to unmap a Base64 byte.
	 */
	protected static byte decodeMap[] = new byte[128];
	static 
	{
		/*
		 * Fill in the decode map
		 */
		for (int i = 0; i < encodeMap.length; i++) {
			decodeMap[encodeMap[i]] = (byte)i;
		}
	}


	/**
	 * This class isn't meant to be instantiated.
	 */
	private Base64Codec () 
	{
	}


	/**
	 * This method encodes the given byte[] using the Base64 encoding
	 * specified in RFC-2045.
	 *
	 * @param  data the data to encode
	 * @return the Base64 encoded <var>data</var>
	 */
	public final static byte[] encode (byte[] data) {

		if (data == null) {
			return (null);
		}

		if (data.length < 1)
			return (null);

		return (encode(data, data.length));
	}


	/**
	 * This method encodes the given byte[] using the Base64 encoding
	 * specified in RFC-2045.
	 *
	 * @param  data  the data to encode
	 * @param  data_len  the length of the data to encode
	 * @return the Base64 encoded <var>data</var>
	 */
	public final static byte[] encode (byte[] data, int data_len) {

		if (data == null) {
			return (null);
		}

		/*
		 * Create a buffer to hold the results
		 */
		byte dest[] = new byte[((data_len + 2) / 3) * 4];


		/*
		 * 3-byte to 4-byte conversion and 
		 * 0-63 to ascii printable conversion
		 */
		int i, j;
		int saved_data_len = data_len;
		data_len = data_len - 2;
		for (i = 0, j = 0; i < data_len; i += 3) {

			dest[j++] = encodeMap[(data[i] >>> 2) & 077];
	    		dest[j++] = encodeMap[(data[i + 1] >>> 4) & 017 |
				(data[i] << 4) & 077];
	    		dest[j++] = encodeMap[(data[i + 2] >>> 6) & 003 |
				(data[i + 1] << 2) & 077];
	    		dest[j++] = encodeMap[data[i + 2] & 077];
		}
	
		if (i < saved_data_len) {
			dest[j++] = encodeMap[(data[i] >>> 2) & 077];

			if (i < saved_data_len - 1) {
			    dest[j++] = encodeMap[(data[i + 1] >>> 4) & 017 |
				    (data[i] << 4) & 077];
			    dest[j++] = encodeMap[(data[i + 1] << 2) & 077];
	    		} else {
			    dest[j++] = encodeMap[(data[i] << 4) & 077];
			}
		}


		/*
		 * Pad with "=" characters
		 */
		for ( ; j < dest.length; j++) {
			dest[j] = (byte)'=';
		}

		return (dest);
	}


	/**
	 * This method decodes the given byte[] using the Base64 encoding
	 * specified in RFC-2045.
	 *
	 * @param  data the Base64 encoded data to decode
	 * @return the decoded <var>data</var>
	 */
	public final static byte[] decode (byte[] data) {

		if (data == null)
			return (null);

		if (data.length < 1)
			return (null);

		return (decode(data, data.length));
	}


	/**
	 * This method decodes the given byte[] using the Base64 encoding
	 * specified in RFC-2045.
	 *
	 * @param  data the Base64 encoded data to decode
	 * @param  data_len  the length of the data to decode
	 * @return the decoded <var>data</var>
	 */
	public final static byte[] decode (byte[] data, int data_len) {

		if (data == null)
			return (null);

		/*
		 * Remove the padding on the end
		 */
		if (data_len < 1) {
			return (null);
		}

		while (Character.isWhitespace((char)data[data_len - 1]))
			data_len--;

		while (data[data_len - 1] == '=')
			data_len--;

		/*
		 * ASCII printable to 0-63 conversion
		 */
		int new_data_len = 0;
		for (int i = 0; i < data_len; i++) {
			char c = (char)data[i];
			if (c != '\n' && c != '\t' && c != '\r' && c != ' ') {
				data[new_data_len] = decodeMap[c];
				new_data_len++;;
			}
		}


		/*
		 * Create a buffer to hold the results
		 */
		byte dest[] = new byte[(new_data_len * 3) / 4];

	
		/*
		 * 4-byte to 3-byte conversion
		 */
		int i, j;
		int dest_len = dest.length - 2;
		for (i = 0, j = 0; j < dest_len; i += 4, j += 3) {
			dest[j] = (byte) (((data[i] << 2) & 255) |
			 	((data[i + 1] >>> 4) & 003));
			dest[j + 1] = (byte) (((data[i + 1] << 4) & 255) |
				((data[i + 2] >>> 2) & 017));
	    		dest[j + 2] = (byte) (((data[i + 2] << 6) & 255) |
			    (data[i + 3] & 077));
		}

		if (j < dest.length) {
			dest[j] = (byte) (((data[i] << 2) & 255) |
				((data[i + 1] >>> 4) & 003));
		}

		j++;
		if (j < dest.length) {
			dest[j] = (byte) (((data[i + 1] << 4) & 255) |
				((data[i + 2] >>> 2) & 017));
		}

		return (dest);
	}


	/**
	 * This method reads from an input stream encoding the read data
	 * into Base64 and returns a byte array containing the encoded data.
	 *
	 * @param in input stream to encode
	 * @return byte[] containing the base64 encoded data
	 */
	public final static byte [] encode (InputStream in) throws IOException {

		ByteArrayOutputStream out = new ByteArrayOutputStream();
		encode(in, out);
		return (out.toByteArray());
	}


	/**
	 * This method reads from one stream encoding the read data into
	 * Base64 and writes the output to the output stream.
	 *
	 * @param  in   input stream to encode
	 * @param  out  output stream to write encoded data
	 */
	public final static void encode (InputStream in, OutputStream out) 
		throws IOException {

		/*
		 * Now read and encode a chunk at a time.
		 * NOTE: The size of inpbuf must be divisible by 3 and should
		 *       be <= 54
		 */
		byte inpbuf[] = new byte[48];
		int nread;
		boolean first_line = true;
		while ((nread = in.read(inpbuf)) != -1) {

			byte outbuf[] = encode(inpbuf, nread);
			if (outbuf == null) {
				return;
			} else if (outbuf.length > 0) {
				if (first_line) {
					first_line = false;
				} else {
					out.write('\r');
					out.write('\n');
				}
				out.write(outbuf);
			}
		}
	}


	/**
	 * This method reads from one stream encoding the read data into
	 * Base64 and writes the output to the Writer.
	 *
	 * @param  in   input stream to encode
	 * @param  out  output stream to write encoded data
	 */
	public final static void encode (InputStream in, Writer out) 
		throws IOException {

		/*
		 * Now read and encode a chunk at a time.
		 * NOTE: The size of inpbuf must be divisible by 3 and should
		 *       be <= 54
		 */
		byte inpbuf[] = new byte[48];
		char tmpbuf[] = new char[64];
		int nread;
		boolean first_line = true;
		while ((nread = in.read(inpbuf)) != -1) {

			byte outbuf[] = encode(inpbuf, nread);
			if (outbuf == null) {

				return;

			} else if (outbuf.length > 0) {

				if (first_line) {
					first_line = false;
				} else {
					out.write('\r');
					out.write('\n');
				}

				for (int i = 0; i < nread; i++) {
					tmpbuf[i] = (char)outbuf[i];
				}

				out.write(tmpbuf, 0, nread);
			}
		}
	}


	/**
	 * This method reads from a stream containing Base64 encoded data
	 * decodes the data and returns a byte array containing the decoded
	 * data.
	 *
	 * @param in input stream to decode
	 * @return byte[] containing the decoded base64 data
	 */
	public final static byte [] decode (InputStream in) throws IOException {

		ByteArrayOutputStream out = new ByteArrayOutputStream();
		decode(in, out);
		return (out.toByteArray());
	}


	/**
	 * This method reads from a stream containing Base64 encoded data
	 * decodes the data and writes it on the output stream.
	 *
	 * @param  inpbuf  input buffer to decode
	 * @param  out  OutputStream to write decoded data
	 */
	public final static void decode (byte inpbuf[], OutputStream out) 
		throws IOException {

		byte outbuf[] = decode(inpbuf);
		if (outbuf == null) {
			return;
		}

		out.write(outbuf);
	}


	/**
	 * This method reads from a stream containing Base64 encoded data
	 * decodes the data and writes it on the output stream.
	 *
	 * @param  in   input stream to decode
	 * @param  out  output stream to write decoded data
	 */
	public final static void decode (InputStream in, OutputStream out) 
		throws IOException {

		/*
		 * Now read and decode a chunk at a time
		 */
		int buf_len = 4096;
		byte line[] = new byte[buf_len];
		int buf_ptr = 0;
		int c;
		while ((c = in.read()) != -1) {

			if (c == '\n' || c == '\r' || c == '\t' || c == ' ') {
				continue;
			} 

			line[buf_ptr] = (byte)c;
			buf_ptr++;

			if (buf_ptr == buf_len) {

				byte outbuf[] = decode(line, buf_ptr);
				if (outbuf == null) {
					return;
				} else if (outbuf.length > 0) {
					out.write(outbuf);
				}

				buf_ptr = 0;
			}
		}

		/*
		 * Output any remaining
		 */
		if (buf_ptr > 0) {
			byte outbuf[] = decode(line, buf_ptr);
			if (outbuf != null && outbuf.length > 0) {
				out.write(outbuf);
			}
		}
	}
}

