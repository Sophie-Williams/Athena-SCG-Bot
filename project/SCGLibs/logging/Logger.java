package logging;

import java.io.*;
import scg.Util;

/** Superclass of various Logging Objects */
public abstract class Logger{
    /** Simple notifications, no Date/Stamp needed; returns the msg given. */
    public abstract String notify(String msg);
    /** Log an Error occurence; returns the msg given. */
    public abstract String error(String msg);
    /** Capture an event, by logging the Date and message; returns the msg given. */
    public String event(String msg){
        return notify(stamp()+msg);
    }
    /** Returns a Time/Date Stamp */
    protected String stamp(){ return Util.printDate(Util.now()); }
    /** Shutdown this Logger */
    public abstract void shutdown();
    
    /** Create a PlainText Logger out to the given File */
    public static Logger text(String file)throws IOException{
        return text(new FileOutputStream(file));
    }
    /** Create a PlainText Logger out to the given OutputStream */
    public static Logger text(OutputStream strm){
        return text(new PrintStream(strm));
    }
    /** Create a PlainText Logger out to the given PrintStream */
    public static Logger text(PrintStream strm){
        return new PlainText(strm);
    }
    /** Create a PlainText Logger out to the given File */
    public static Logger text(OutputStream o, String file)throws IOException{
        return text(SplitStream.create(o, new FileOutputStream(file)));
    }
    
    /** Create a HTML Logger out to the given File */
    public static Logger html(String file)throws IOException{
        return html(new FileOutputStream(file));
    }
    /** Create a HTML Logger out to the given OutputStream */
    public static Logger html(OutputStream strm){
        return html(new PrintStream(strm));
    }
    /** Create a HTML Logger out to the given PrintStream */
    public static Logger html(PrintStream strm){
        return new HTMLText(strm);
    }
    /** Create a HTML Logger out to the given File */
    public static Logger html(OutputStream o, String file)throws IOException{
        return html(SplitStream.create(o, new FileOutputStream(file)));
    }
    
    /** General Output Logging to a Stream/File */
    private static abstract class OutputLogger extends Logger{
        PrintStream out;
        OutputLogger(PrintStream strm){ out = strm; }
        
        void log(String s){ out.print(s); }
        void logln(String s){ out.println(s); }
        public void shutdown(){ out.close(); }
    }
    
    /** Plain Text Output Logging */
    private static class PlainText extends OutputLogger{
        PlainText(PrintStream strm){
            super(strm);
            logln("      ******* Log At: "+stamp()+" *******");
        }
        public String error(String msg){ logln(" !! "+msg); return msg; }
        public String notify(String msg){ logln(" -- "+msg); return msg; }
        public String event(String msg){ logln(" ** "+stamp()+" "+msg); return msg; }
        protected String stamp(){ return super.stamp()+":"; }
    }
    /** HTML Output Logging */
    private static class HTMLText extends OutputLogger{
        HTMLText(PrintStream strm){
            super(strm);
            logln("<html><head><title>Log At: "+stamp()+"</title></head>");
        }
        public String error(String msg){ logln(" <span color='red'>!! "+msg+"</span><br/>"); return msg; }
        public String notify(String msg){ logln(" -- "+msg+"<br/>"); return msg; }
        public String event(String msg){ logln(" <span color='blue'>** "+stamp()+": "+msg+"</span><br/>"); return msg; }        
        protected String stamp(){
            String s = super.stamp();
            log(" ** <span color='green'>"+s+"<span>");
            return s;
        }
        public void shutdown(){ out.println("\n</body><html>"); super.shutdown(); }
    }
}
