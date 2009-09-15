package logging;

import java.io.IOException;
import java.io.OutputStream;

/** Split a Stream into two, so output can go to the screen and to
 *    a File, similar to the Unix utility "tee". */
public class SplitStream extends OutputStream{
    OutputStream one;
    OutputStream two;
    
    private SplitStream(OutputStream o, OutputStream t){ one = o; two = t; }
    
    /** Create a new SplitStream */
    public static SplitStream create(OutputStream o, OutputStream t){
        return new SplitStream(o,t);
    }

    /** Write to both of the output Streams. */
    public void write(int c) throws IOException{
        one.write(c); two.write(c);
    }
    
    /** Close Both Streams */
    public void close() throws IOException { one.close(); two.close(); }
    
    /** Fluch Both Streams */
    public void flush() throws IOException { one.flush(); two.flush(); }
}
