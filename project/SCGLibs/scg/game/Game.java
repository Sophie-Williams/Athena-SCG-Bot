package scg.game;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import scg.Util;
import scg.gen.*;

/**
 * 
 * 
 * Representation of AdminState
 * 
 * Constructed with: a set of playerSpecs, a configuration object holding all
 * configuration parameters such as: initial account balance, min decrement (for
 * reoffering)
 * 
 * Can be turned to a string but not necessarily parsed.
 * 
 * Can be queried for: the context for a specific player. spec of a specific
 * player. All players.
 * 
 * Provide methods for installing different player transactions.
 * 
 * 
 */
public class Game implements GameI {

    /** Associates player specs to player id's */
    public static class Player {
        /**  */
        private final int id;
        /**  */
        private final PlayerProxyI proxy;

        /**  */
        public Player(int id, PlayerProxyI proxy) {
            this.id = id;
            this.proxy = proxy;
        }

        /**  */
        public int getId(){ return id; }
        /**  */
        public PlayerSpec getSpec(){ return proxy.getSpec(); }
        /**  */
        public PlayerTrans takeTurn(PlayerContext currentPlayerContext) throws Exception{
            return proxy.takeTurn(currentPlayerContext);
        }
        /**  */
        public String toString(){ return id+" : "+getSpec().toString(); }
    }

    /** Associates player id's to their state in the game */
    class PlayerStore {

        double account;
        boolean kicked = false;

        /** Challenges offered by the player The player shouldn't offer/reoffer these. */
        Map<Integer, OfferedChallenge> offeredChallenges = new HashMap<Integer, OfferedChallenge>();
        /** Challenges offered by the player and accepted by other player The player has
         *    to provide these. */
        Map<Integer, AcceptedChallenge> acceptedChallenges = new HashMap<Integer, AcceptedChallenge>();
        /** Challenges provided for the player The player should solve these */
        Map<Integer, ProvidedChallenge> providedChallenges = new HashMap<Integer, ProvidedChallenge>();

        /**  */
        public PlayerStore(double account) {
            this.account = account;
        }
    }

    private edu.neu.ccs.demeterf.lib.List<Player> players = edu.neu.ccs.demeterf.lib.List.<Player> create();
    private final Map<Integer, PlayerStore> playersStores = new HashMap<Integer, PlayerStore>();
    private final Map<Integer, Integer> challengeOfferer = new HashMap<Integer, Integer>();
    private final Config config;

    /**  */
    public Game(Config config, PlayerProxyI... playerProxies) {
        this(config, Arrays.asList(playerProxies));
    }

    /**  */
    public Game(Config config, edu.neu.ccs.demeterf.lib.List<PlayerProxyI> proxies) {
        this(config, proxies.toJavaList());
    }
    
    /**  */
    public Game(Config config, List<? extends PlayerProxyI> playerProxies) {
        this.config = config;
        for (PlayerProxyI playerProxy : playerProxies) {
            int pid = config.createPlayerID();
            this.playersStores.put(pid, new PlayerStore(config.getInitacc()));
            this.players = this.players.push(new Player(pid, playerProxy));
        }
    }

    /**  */
    public void start(HistoryFile history) throws IOException{
        int numPlayers = players.length();
        history.header(players);
        for (int round = 1; round <= config.getNumrounds() + config.getOtrounds() && numPlayers > 1; round++) {
            history.startRound(round);
            for (Player currentPlayer : players) {
                PlayerStore currentPlayerStore = playersStores.get(currentPlayer.id);
                if (currentPlayerStore.kicked) {
                    continue;
                }
                int currentPlayerID = currentPlayer.getId();
                PlayerSpec currentPlayerSpec = currentPlayer.getSpec();
                PlayerContext currentPlayerContext = getPlayerContext(currentPlayerID, round);
                try {
                    PlayerTrans trans = currentPlayer.takeTurn(currentPlayerContext);
                    currentPlayerContext.isLegal(trans);
                    trans.applyTransactions(this, config.isOverTime(round));
                    history.recordEvent(currentPlayerID, trans);
                } catch (Exception ex) {
                    kick(currentPlayerID);
                    ex.printStackTrace();
                    history.recordEvent(currentPlayerID, new PlayerKickedEvent(currentPlayerSpec, "" + ex.getMessage(),
                            Util.printDate(Util.now())));
                    if (--numPlayers <= 1) {
                        break;
                    }
                }
            }
            history.flushRound();
        }
        history.footer(getPlayersState());
    }

    // All challenges where the kicked player is the challenger must be
    // refunded.
    // All challenges where the kicked player is the challengee are just dropped
    // without refund.
    // player is removed from current players list.
    private void kick(int playerID){
        for (Entry<Integer, PlayerStore> player : playersStores.entrySet()) {
            // challenges kicked player accepted from other players
            PlayerStore playerStore = player.getValue();
            for (AcceptedChallenge challenge : playerStore.acceptedChallenges.values()) {
                if (challenge.getChallengee().getId() == playerID) {
                    playerStore.acceptedChallenges.remove(challenge);
                    // no refund
                }
            }
            // challenges kicked player provided to other players
            for (Challenge challenge : playerStore.providedChallenges.values()) {
                if (challenge.getChallenger().getId() == playerID) {
                    playerStore.providedChallenges.remove(challenge);
                    // refund
                    transferMoney(playerID, player.getKey(), challenge.getPrice());
                }
            }
            if (player.getKey() == playerID) {
                // challenges other players accepted from the kicked player
                for (AcceptedChallenge challenge : player.getValue().acceptedChallenges.values()) {
                    // refund
                    transferMoney(playerID, challenge.getChallenger().getId(), challenge.getPrice());
                }
                player.getValue().acceptedChallenges.clear();
                // challenges other players provided for the kicked player
                player.getValue().providedChallenges.clear();
                // All challenges offered by the kicked player but never
                // accepted
                player.getValue().offeredChallenges.clear();
                // player is added to the kicked players list.
                player.getValue().kicked = true;
            }
        }
    }

    /**  */
    public edu.neu.ccs.demeterf.lib.List<edu.neu.ccs.demeterf.lib.Entry<PlayerSpec, PlayerStore>> getPlayersState(){
        edu.neu.ccs.demeterf.lib.List<edu.neu.ccs.demeterf.lib.Entry<PlayerSpec, PlayerStore>> playersState = edu.neu.ccs.demeterf.lib.List
                .<edu.neu.ccs.demeterf.lib.Entry<PlayerSpec, PlayerStore>> create();
        for (Player player : players) {
            PlayerStore store = playersStores.get(player.id);
            playersState = playersState.push(new edu.neu.ccs.demeterf.lib.Entry<PlayerSpec, PlayerStore>(player
                    .getSpec(), store));
        }
        return playersState;
    }

    /**  */
    public edu.neu.ccs.demeterf.lib.List<Player> getPlayersTable(){
        return players;
    }

    /** Retrieve the player context for the current game */
    public PlayerContext getPlayerContext(int playerID, int currentRound){
        PlayerStore store = playersStores.get(playerID);
        List<OfferedChallenge> otherOffered = getOtherOffers(playerID);

        PlayerContext playerContext = new PlayerContext(config, new PlayerID(playerID), store.account, currentRound,
                Util.toDemF(store.offeredChallenges.values()), Util.toDemF(otherOffered), Util
                        .toDemF(store.acceptedChallenges.values()), Util.toDemF(store.providedChallenges.values()));

        return playerContext;

    }

    /** Collect offers made by other players */
    private List<OfferedChallenge> getOtherOffers(int playerID){
        List<OfferedChallenge> otherOffered = new ArrayList<OfferedChallenge>();
        for (Entry<Integer, PlayerStore> player : playersStores.entrySet()) {
            if (player.getKey() != playerID) {
                PlayerStore playerStore = player.getValue();
                otherOffered.addAll(playerStore.offeredChallenges.values());
            }
        }
        return otherOffered;
    }

    /** Transfer money between the accounts of two players */
    private void transferMoney(int from, int to, double amt){
        playersStores.get(from).account -= amt;
        playersStores.get(to).account += amt;
    }

    // Install transaction methods for all transaction types */

    /** Handle an offered challenge transaction */
    public void installTransaction(int challengerID, OfferTrans ot){
        OfferedChallenge challenge = new OfferedChallenge(config.createChallengeID(), new PlayerID(challengerID), ot
                .getPred(), ot.getPrice());
        challengeOfferer.put(challenge.getKey(), challengerID);
        playersStores.get(challengerID).offeredChallenges.put(challenge.getKey(), challenge);
    }

    /** Handle an accept challenge transaction */
    public void installTransaction(int challengeeID, AcceptTrans at){
        int challengeID = at.getChallengeid();
        int challengerID = challengeOfferer.get(challengeID);
        PlayerStore challengerStore = playersStores.get(challengerID);
        OfferedChallenge toBeAcceptedChallenge = challengerStore.offeredChallenges.get(challengeID);

        AcceptedChallenge acceptedChallenge = toBeAcceptedChallenge.accept(new PlayerID(challengeeID));

        transferMoney(challengeeID, challengerID, acceptedChallenge.getPrice());

        challengerStore.offeredChallenges.remove(challengeID);
        challengerStore.acceptedChallenges.put(challengeID, acceptedChallenge);

    }

    /** Handle Provide Transactions */
    public void installTransaction(int challengerID, ProvideTrans pt){

        int challengeID = pt.getChallengeid();
        PlayerStore challengerStore = playersStores.get(challengerID);
        AcceptedChallenge toBeProvidedChallenge = challengerStore.acceptedChallenges.get(challengeID);
        int challengee = toBeProvidedChallenge.getChallengee().getId();

        Problem problem = pt.getInst();
        ProvidedChallenge providedChallenge = toBeProvidedChallenge.provide(problem);

        PlayerStore challengeeStore = playersStores.get(challengee);
        challengerStore.acceptedChallenges.remove(challengeID);
        challengeeStore.providedChallenges.put(challengeID, providedChallenge);
    }

    /** Handle Solve Transactions */
    public void installTransaction(int challengeeID, SolveTrans st){
        int challengeID = st.getChallengeid();
        PlayerStore challengeeStore = playersStores.get(challengeeID);
        ProvidedChallenge toBeSolevedChallenge = challengeeStore.providedChallenges.get(challengeID);
        int challengerID = toBeSolevedChallenge.getChallenger().getId();

        Problem inst = toBeSolevedChallenge.getInstance();
        Solution sol = st.getSol();

        double quality = config.getObjective().value(inst, sol);
        double secretQuality = config.getObjective().getDefaultQuality(inst);
        double price = toBeSolevedChallenge.getPrice();
        double profitFactor = config.getProfitFactor();
        double profit = config.getObjective().calculatePayoff(quality, secretQuality, price, profitFactor);

        transferMoney(challengerID, challengeeID, profit);

        challengeeStore.providedChallenges.remove(challengeID);
    }

    /** Handle Reoffer Transactions */
    public void installTransaction(int newChallenger, ReofferTrans rt){
        int challengeID = rt.getChallengeid();

        int challengerID = challengeOfferer.get(challengeID);

        challengeOfferer.remove(challengeID);
        challengeOfferer.put(challengeID, newChallenger);

        PlayerStore challengerStore = playersStores.get(challengerID);
        PlayerStore newChallengerStore = playersStores.get(newChallenger);

        OfferedChallenge toBeReofferedChallenge = challengerStore.offeredChallenges.get(challengeID);
        OfferedChallenge reOfferedChallenge = toBeReofferedChallenge
                .reoffer(new PlayerID(newChallenger), rt.getPrice());

        challengerStore.offeredChallenges.remove(challengeID);
        newChallengerStore.offeredChallenges.put(challengeID, reOfferedChallenge);
    }

    /** DGP method from Class PrintHeapToString */
    @Override
    public String toString(){
        return scg.gen.PrintHeapToString.PrintHeapToStringM(this);
    }
}
