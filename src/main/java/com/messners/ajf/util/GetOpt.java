package com.messners.ajf.util;

/**
 * This class encapsulates the command line passed to
 * the main() function of an executable. It processes
 * the arguments in the same way as Unixes getopt().
 *
 * @author  Greg Messner <greg@messners.com>
 */
public class GetOpt {

	public static final char NUM_CHR  = '#';
	public static final char ARG_XPTD = ':';
	public static final int EOF   = -1;
	public static final int ERROR = -2;

	protected String optarg;
	protected int optind;
	protected int charpos;

	protected char optopt;
	protected char optsw;
	protected String optstr;
	protected char switches[];

	protected String msgbuf;

	/**
	 * No args constructor.
	 */
	public GetOpt () {
		this(null);
	}


	/**
	 * Construct a CmdLine object with the specified option switches.
	 *
	 * @param  optstr  the option string (see Unix getopt(3))
	 */
	public GetOpt (String optstr) {
		this.optstr   = optstr;
		optind   = 0;
		charpos  = 0;
		optopt   = '\0';
		optsw    = '\0';
		optarg   = null;
		msgbuf   = null;

		switches = new char[1];
		switches[0] = '-';
	}


	/**
	 * Process cmd line arguments (getopt substitute).<p>
	 *
	 * Notes:  This routine is a replacement for the UNIX routine getopt
	 * (which is not universally available).
	 *
	 * @param  argv	 argument vector
	 * @return  option found, ERROR if error or EOF if at end
	 */
	public int getOpt (String argv[]) {
		return (getOpt(argv, null));
	}


	/**
	 * Process cmd line arguments (getopt substitute).<p>
	 *
	 * Notes:  This routine is a replacement for the UNIX routine getopt
	 * (which is not universally available).
	 *
	 * @param  argv	 argument vector
	 * @param  optstr  the option string (see Unix getopt(3))  
	 * @return  option found, ERROR if error or EOF if at end
	 */
	public int getOpt (String argv[], String optstr) {

		int argc = argv.length;
		if (optstr == null) {
			optstr = this.optstr;
		}

		optarg = null;

		/* check if we should start over */
		if (optind == 0) {
			charpos = 0;
		}

		/* if past all arguments, done */
		if (optind >= argc) {
			charpos = 0;
			return (EOF);
		}

		/* check if new argument */
		if (charpos == 0) {
			/* check if option & longer than 1 char */
			char ch0 = argv[optind].charAt(0); 
			for (int i = 0; i < switches.length; i++) {
				if (switches[i] == ch0) {
					charpos = 1;
					optsw = ch0;
					break;
				}
			}

			if (charpos == 0) {
				return (EOF);
			}
		}

		/* get next option & bump ptr */
		optopt = argv[optind].charAt(charpos++); 

		/* check for double option chars */
		if (optopt == optsw) {
			charpos = 0;
			optind++;
			return (EOF);
		}

		/* check for number */
		if (Character.isDigit(optopt)) {
			for (int i = 0; i < optstr.length(); i++) {
				char c = optstr.charAt(i);
				if (c == NUM_CHR) {
					optopt = NUM_CHR;
					optarg = argv[optind].substring(
							charpos - 1);
					charpos = 0;
					optind++;
					return ((int)optopt);
				}
			}
		}

		/* check if at end of string of options */
		if (argv[optind].length() == charpos) {
			charpos = 0;
			optind++;
		}

		/* search for next option */
		int optstr_len = optstr.length();
		for (int i = 0; i < optstr_len; i++) {
			char ch = optstr.charAt(i);
			if (ch == ARG_XPTD) {
				continue;
			}

			if (optopt != ch) {
				continue;
			}

			/* got a matching letter in string */

			/* check if no arg expected */
			if (i + 1 >= optstr_len ||
				optstr.charAt(i + 1) != ARG_XPTD) {
					return ((int)optopt);
			}

			/* process option argument */

			if (optind == argc) {
				/* at end of list */

				msgbuf = "option requires an argument -- " + optopt;
				return (ERROR);
			}

			optarg  = argv[optind].substring(charpos);
			charpos = 0;
			optind++;
			return ((int)optopt);
		}

		msgbuf = "invalid option -- " + optopt;
		return (ERROR);
	}


	/**
	 * Gets the argument for the last parsed option.
	 */
	public String getOptArg () {
		return (optarg);
	}


	/**
	 * Gets the current option index.
	 */
	public int getOptIndex () {
		return (optind);
	}


	/**
	 * Checks if an arg is present.
	 *
	 * @param   argv  args from cmd line.
	 * @param   arg	  arg to look for.
	 * @return true if found, false if not.
	 */
	public boolean containsArg (String argv[], char arg) {

		int argc = argv.length;

		char opts[] = new char[1];;
		opts[0] = arg;
		String optstr = new String(opts);

		boolean found = false;
		while (!found) {
			if (optind >= argc) {
				break;
			}

			int c = getOpt(argv, optstr);
			if (c == arg) {
				found = true;
			} else if (c == EOF) {
				optind++;
			}
		}

		/* reset for real pass */
		optind = 0;		
		return (found);
	}


	/**
	 * Checks if an arg is present.
	 *
	 * @param  argv	 args from cmd line.
	 * @param  arg   arg to look for.
	 * @return  A pointer to arg if present, null if not.
	 */
	public String getStringArg (String argv[], char arg) {

		int argc = argv.length;

		char opts[] = new char[2];
		opts[0] = arg;
		opts[1] = ':';
		String optstr = new String(opts);

		while (true) {
			if (optind >= argc)
				break;

			int c = getOpt(argv, optstr);
			if (c == arg) {
				optind = 0;
				return (optarg);
			} else if (c == EOF) {
				optind++;
			}
		}

		optind = 0;		/* reset for real pass */
		return (null);
	}


	/**
	 * Gets the last reported error message.
	 *
	 * @return  A pointer to error message
	 */
	public String getErrorMsg () {
		return (msgbuf);
	}


	/**
	 * Sets option switches to look for.
	 *
	 * @param switches char array of switch characters.
	 */
	public void setSwitches (char switches[]) {
		this.switches = switches;
	}
}
