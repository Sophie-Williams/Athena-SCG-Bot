package player;

import logging.Logger;
import player.tasks.AcceptTask;
import player.tasks.OfferTask;
import player.tasks.ProvideTask;
import player.tasks.ReofferTask;
import player.tasks.SolveTask;
import scg.gen.AcceptedChallenge;
import scg.gen.OfferedChallenge;
import scg.gen.PlayerContext;
import scg.gen.PlayerID;
import scg.gen.PlayerTrans;
import scg.gen.ProvidedChallenge;
import scg.gen.Transaction;
import edu.neu.ccs.demeterf.lib.List;

/**
 * Player class, responsible for taking a single turn. We deligate to separate
 * Tasks classes to make things a little easier to understand. See
 * {@link player.tasks Tasks}.
 */
public class Player {

    /** Team Name: change this to your team's name */
    private final String teamName = "GenericPlayer";
    /** The context for this current turn */
    private final PlayerContext context;
    /** Logger instance for output */
    private final Logger log;

    /** Get our assigned ID from the context */
    private PlayerID playerID(){ return context.getId(); }

    private Player(PlayerContext ctxt, Logger l) {
        context = ctxt;
        log = l;
    }

    /** Construct a new player with the given Context */
    public static Player create(PlayerContext ctxt, Logger l){
        return new Player(ctxt, l);
    }

    /** Get this Players Team Name */
    public String getName(){ return teamName; }

    /** Take a turn for this player */
    public PlayerTrans play(){
        log.event("Chance to Play...");
        // Try to accept some of the offered challenges
        List<Transaction> accs = acceptChallenges();
        // If not then we must Reoffer
        if (accs.isEmpty()) {
            accs = reofferChallenges();
        }
        // Create a player transaction from our ID and our other transactions 
        return new PlayerTrans(playerID(), offerChallenges().append(
                accs.append(provideChallenges().append(solveChallenges()))));
    }

    /** Offer new Challenges... that are not currently offered */
    public List<Transaction> offerChallenges(){
        log.event("Running Offer");
        return (List) new OfferTask(log).offer(context);
    }

    /** Accept Challenges that seem reasonable/profitable */
    public List<Transaction> acceptChallenges(){
        log.event("Running Accept");
        return (List) new AcceptTask(log).accept(context);
    }

    /** Reoffer other Player's Challenges */
    public List<Transaction> reofferChallenges(){
        log.event("Running Reoffer");
        final ReofferTask reoff = new ReofferTask(log);
        return context.getTheirOffered().map(new List.Map<OfferedChallenge, Transaction>() {

            @Override
            public Transaction map(OfferedChallenge ch){
                return reoff.reoffer(ch, context.getConfig());
            }
        });
    }

    /** Provide Problem instances for accepted Challenges */
    public List<Transaction> provideChallenges(){
        log.event("Running Provide");
        final ProvideTask prov = new ProvideTask(log);
        return context.getAccepted().map(new List.Map<AcceptedChallenge, Transaction>() {
            public Transaction map(AcceptedChallenge ch){
                return prov.provide(ch);
            }
        });
    }

    /** Solve provided Problem instances */
    public List<Transaction> solveChallenges(){
        log.event("Running Solve");
        final SolveTask solve = new SolveTask(log);
        return context.getProvided().map(new List.Map<ProvidedChallenge, Transaction>() {
            public Transaction map(ProvidedChallenge ch){
                return solve.solve(ch);
            }
        });
    }
}
