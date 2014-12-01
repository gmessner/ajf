package com.messners.ajf.logging;

// import java.text.*;
import java.util.logging.Level;
import java.util.logging.Formatter;
import java.util.Date;
import java.util.logging.LogManager;
import java.util.logging.LogRecord;
import java.text.SimpleDateFormat;

/**
 * This formatter produces one line with time stamp and Level
 * for each log record.    The class and method names in the log record
 * are included in the output if the Level is "below" a threhold level.
 * The threhold classlevel may be set by the OneLineFormatter.classlevel
 * property, and defaults to FINE.  (Thus, all records at FINE, FINER, or
 * below will contain the class/method names.)
 */

public class OneLineFormatter extends Formatter {

   /**
    * message string buffer
    */
   private StringBuffer sb = new StringBuffer();

   /**
    * to hold the logrecord's date
    */
   private Date date = new Date();

   /**
    * system's idea of line end
    */
   private String endline = System.getProperty( "line.separator" );

   /**
    * how we want the date to appear
    */
   private SimpleDateFormat dateFmt =
      new SimpleDateFormat( "yyyyMMdd HHmmss.SSS " );

   /**
    * the static default level at which we also log classname/method fields
    */
   private static final Level DEFAULT_CLASS_LEVEL = Level.FINE;

   /**
    * the default level at which instances also log classname/method fields
    */
   private static Level defaultClassLevel;
   private Level classLevel;

   /**
    * configure this formatter from LogManager properties.
    * The only one we look for is the classnameLevel, which
    * is the place at or 'below' which we output class/method names.
    */
   private static void configure() {

      String cname = OneLineFormatter.class.getName();

      LogManager lm = LogManager.getLogManager();
      // esd.Logging initializer arranges to put our props in LogManager.
      String lmprop = lm.getProperty( cname + ".classlevel" );

      defaultClassLevel = DEFAULT_CLASS_LEVEL;
      if ( lmprop != null ) {
         try {
            defaultClassLevel = Level.parse( lmprop );
         }
         catch ( Exception ignore ) {
            // already set the default level of fine.
         }
      }
   }

   /**
    * set up statics - i.e.: defaultClassLevel threshold.
    */
   static {
      configure();
   }

   /**
    * setup instances from static properties
    */
   {
      classLevel = defaultClassLevel;
   }



   synchronized public String format( LogRecord logRecord ) {

      sb.setLength( 0 );
      date.setTime( logRecord.getMillis() );
      sb.append( dateFmt.format( date ) );
      sb.append( logRecord.getLevel().getName() ); // non-localized!
      sb.append( ' ' );

      // jdk levels are 'backwards', with lower numbers being more detailed.
      // (severe=1000,warn=900,info=800,config=700,fine=500,finer=400,finest=300)
      // we add class/method names on levels "below" the defaultClassLevel threshold.
      if ( classLevel.intValue() != Level.OFF.intValue() &&
         ( logRecord.getLevel().intValue() <= classLevel.intValue() ||
         classLevel.intValue() == Level.ALL.intValue() ) ) {
            String fullname = logRecord.getSourceClassName();
            sb.append( fullname.substring( fullname.lastIndexOf( '.' ) + 1 ) );
            sb.append( '.' );
            sb.append( logRecord.getSourceMethodName() );
            sb.append( "() " );
      }

      sb.append( logRecord.getMessage() ); // non-localized!?!
      sb.append( endline );

      return sb.toString();
   }

   /**
    * the level at which the instance also logs classname/method fields
    */
   public Level getClassLevel(){
      return classLevel;
   }


   /**
    * the level at which the instance also logs classname/method fields
    */
   public void setClassLevel(Level classLevel){
      this.classLevel = classLevel;
   }
}
