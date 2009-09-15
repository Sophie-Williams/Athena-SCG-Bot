package scg.game;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.Writer;
import java.util.Date;

import scg.Util;
import scg.game.Game.PlayerStore;
import scg.gen.Event;
import scg.gen.PlayerSpec;
import scg.gen.Round;
import edu.neu.ccs.demeterf.lib.Entry;
import edu.neu.ccs.demeterf.lib.List;

public class HistoryFile {

    private final String historyFile;
    private final Writer history;
    private boolean headerPrinted = false;

    public HistoryFile(String prefix, Date compTime, String suffix) throws Exception {
        this.historyFile = prefix + Util.getFileNameSafeDate(compTime) + suffix;
        File f = new File(historyFile);
        if (!f.exists()) {
            f.createNewFile();
        }
        this.history = new OutputStreamWriter(new FileOutputStream(f));
    }

    public void header(List<Game.Player> players) throws IOException{
        if (headerPrinted) {
            throw new RuntimeException("Header can be printed only once");
        }
        history.append(players.toString("\n", "  "));
        history.append("\n\n");
        history.flush();
        headerPrinted = true;
    }

    private int round = -1;
    private List<Event> roundEvents = List.create();

    public void startRound(int numRound){
        this.round = numRound;
        this.roundEvents = List.create();
    }

    public void flushRound() throws IOException{
        Round r = new Round(round, roundEvents.reverse());
        history.append(r.toString() + "\n");
        history.flush();
        round = -1;
    }

    public void recordEvent(int playerID, Event event){
        if (round == -1) {
            throw new RuntimeException("Can record events in the context of a round only");
        }
        this.roundEvents = this.roundEvents.push(event);
    }

    public void footer(List<Entry<PlayerSpec, PlayerStore>> playersState) throws IOException{
        history.append("**** Final Results *****\n");
        List<Entry<PlayerSpec, PlayerStore>> sortedState = playersState
                .sort(new List.Comp<Entry<PlayerSpec, PlayerStore>>() {

                    @Override
                    public boolean comp(Entry<PlayerSpec, PlayerStore> a, Entry<PlayerSpec, PlayerStore> b){
                        if (a.getVal().kicked && !b.getVal().kicked) {
                            return false;
                        }
                        if (!a.getVal().kicked && b.getVal().kicked) {
                            return true;
                        }
                        return a.getVal().account > b.getVal().account;
                    }
                });

        history.append(sortedState.fold(new List.Fold<Entry<PlayerSpec, PlayerStore>, String>() {

            @Override
            public String fold(Entry<PlayerSpec, PlayerStore> f, String r){
                String k = f.getVal().kicked ? " * " : "  ";
                return r + "\n" + f.getKey().getName() + " : " + k + f.getVal().account;
            }
        }, ""));

        history.flush();
        history.close();
        // TODO: Should we close the File too?

    }
}
