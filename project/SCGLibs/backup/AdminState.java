// ** This class was generated with DemFGen (vers:06/04/2009)

package scg.gen;

import edu.neu.ccs.demeterf.demfgen.lib.Empty;
import edu.neu.ccs.demeterf.demfgen.lib.Entry;
import edu.neu.ccs.demeterf.demfgen.lib.List;
import edu.neu.ccs.demeterf.demfgen.lib.Map;

/** Representation of AdminState */
public class AdminState {

    public final Map<PlayerID, PlayerSpec> players;
    public final Map<PlayerID, PlayerStore> store;
    public final Map<PlayerID, Double> accounts;

    /** Construct a(n) AdminState Instance */
    public AdminState(Map<PlayerID, PlayerSpec> players,
            Map<PlayerID, PlayerStore> store, Map<PlayerID, Double> accounts) {
        this.players = players;
        this.store = store;
        this.accounts = accounts;
    }

    /** Is the given object Equal to this AdminState? */
    @Override
    public boolean equals(Object o){
        if (!(o instanceof AdminState)) {
            return false;
        }
        if (o == this) {
            return true;
        }
        AdminState oo = (AdminState) o;
        return ((Object) players).equals(oo.players)
                && ((Object) store).equals(oo.store)
                && ((Object) accounts).equals(oo.accounts);
    }

    /** Parse an instance of AdminState from the given String */
    public static AdminState parse(String inpt) throws ParseException{
        return new TheParser(new java.io.StringReader(inpt)).parse_AdminState();
    }

    /** Parse an instance of AdminState from the given Stream */
    public static AdminState parse(java.io.InputStream inpt)
            throws ParseException{
        return new TheParser(inpt).parse_AdminState();
    }

    /** Parse an instance of AdminState from the given Reader */
    public static AdminState parse(java.io.Reader inpt) throws ParseException{
        return new TheParser(inpt).parse_AdminState();
    }

    /** Field Class for AdminState.players */
    public static class players extends edu.neu.ccs.demeterf.control.Fields.any {
    }

    /** Field Class for AdminState.store */
    public static class store extends edu.neu.ccs.demeterf.control.Fields.any {
    }

    /** Field Class for AdminState.accounts */
    public static class accounts extends
            edu.neu.ccs.demeterf.control.Fields.any {
    }

    /** hate doing this */
    /** Mutable state begins here */
    private int currChallengeID = 500;

    private Map<Integer, OfferedChallenge> offeredChallenges = new Map<Integer, OfferedChallenge>(
            new Empty<Entry<Integer, OfferedChallenge>>());

    private Map<Integer, AcceptedChallenge> acceptedChallenges = new Map<Integer, AcceptedChallenge>(
            new Empty<Entry<Integer, AcceptedChallenge>>());

    private Map<Integer, ProvidedChallenge> providedChallenges = new Map<Integer, ProvidedChallenge>(
            new Empty<Entry<Integer, ProvidedChallenge>>());

    private void setCurrChallengeID(int currChallengeID){
        this.currChallengeID = currChallengeID;
    }

    private void setOfferedChallenges(
            Map<Integer, OfferedChallenge> offeredChallenges){
        this.offeredChallenges = offeredChallenges;
    }

    private void setAcceptedChallenges(
            Map<Integer, AcceptedChallenge> acceptedChallenges){
        this.acceptedChallenges = acceptedChallenges;
    }

    private void setProvidedChallenges(
            Map<Integer, ProvidedChallenge> providedChallenges){
        this.providedChallenges = providedChallenges;
    }

    /** Mutable state ends here */

    AdminState applyOfferedTransaction(PlayerID pid, OfferTrans ot){
        OfferedChallenge challenge = new OfferedChallenge(currChallengeID, pid,
                ot.pred, ot.price);
        Map<Integer, OfferedChallenge> offeredChallenges = this.offeredChallenges
                .put(challenge.key, challenge);
        PlayerStore ps = store.get(pid).addOfferedChallenge(challenge);
        AdminState ret = new AdminState(players, store.put(pid, ps), accounts);
        ret.setCurrChallengeID(currChallengeID + 1);
        ret.setOfferedChallenges(offeredChallenges);
        return ret;
    }

    AdminState applyAcceptedTransaction(PlayerID challengee, AcceptTrans at){

        int challengeId = at.challengeid;
        OfferedChallenge toBeAcceptedChallenge = offeredChallenges
                .get(challengeId);
        PlayerID challenger = toBeAcceptedChallenge.challenger;

        AcceptedChallenge acceptedChallenge = toBeAcceptedChallenge
                .accept(challengee);

        // remove from offered challenges
        Map<Integer, OfferedChallenge> offeredChallenges = this.offeredChallenges
                .remove(challengeId);
        // add to accepted challenges
        Map<Integer, AcceptedChallenge> acceptedChallenges = this.acceptedChallenges
                .put(challengeId, acceptedChallenge);

        // add to challengee.accepted
        PlayerStore challengeePS = store.get(challengee).addAcceptedChallenge(
                acceptedChallenge);
        // remove from challenger.offered
        PlayerStore challengerPS = store.get(challenger)
                .removeOfferedChallenge(toBeAcceptedChallenge);

        Map<PlayerID, PlayerStore> newStore = store.put(challengee,
                challengeePS).put(challenger, challengerPS);

        double challengeeBalance = accounts.get(challengee);
        double challengerBalance = accounts.get(challenger);
        double price = toBeAcceptedChallenge.price;

        Map<PlayerID, Double> newAccounts = accounts.put(challengee,
                challengeeBalance - price).put(challenger,
                challengerBalance + price);

        // create and return new admin state
        AdminState ret = new AdminState(players, newStore, newAccounts);
        ret.setOfferedChallenges(offeredChallenges);
        ret.setAcceptedChallenges(acceptedChallenges);
        return ret;
    }

    // AdminState applyProvideTransaction(ProvideTrans pt){
    //
    // int challengeId = pt.challengeid;
    // AcceptedChallenge toBeProvidedChallenge = acceptedChallenges
    // .get(challengeId);
    //
    // PlayerID challenger = toBeProvidedChallenge.challenger;
    // PlayerID challengee = toBeProvidedChallenge.challengee;
    // Problem problem = pt.inst;
    // ProvidedChallenge providedChallenge = toBeProvidedChallenge
    // .provide(problem);
    //
    // // remove from accepted challenges
    // Map<Integer, AcceptedChallenge> acceptedChallenges =
    // this.acceptedChallenges
    // .remove(challengeId);
    //
    // // add to provided challenges
    // Map<Integer, ProvidedChallenge> providedChallenges =
    // this.providedChallenges
    // .put(challengeId, providedChallenge);
    //
    // // remove from challengee.accepted
    // PlayerStore challengeePS = store.get(challengee)
    // .removeAcceptedChallenge(toBeProvidedChallenge);
    // // add to challengee.provided
    // challengeePS = challengeePS.addProvidedChallenge(providedChallenge);
    //
    // Map<PlayerID, PlayerStore> newStore = store.put(challengee,
    // challengeePS);
    //
    // // create and return new admin state
    // AdminState ret = new AdminState(players, newStore, accounts);
    // ret.setProvidedChallenges(providedChallenges);
    // ret.setAcceptedChallenges(acceptedChallenges);
    // return ret;
    // }
    //
    // AdminState applySolvedTrans(SolveTrans st){
    // int challengeId = st.challengeid;
    //
    // ProvidedChallenge toBeSolvedChallenge = providedChallenges
    // .get(challengeId);
    // PlayerID challenger = toBeSolvedChallenge.challenger;
    // PlayerID challengee = toBeSolvedChallenge.challengee;
    //
    // double quality =
    // // remove from offered challenges
    // Map<Integer, OfferedChallenge> offeredChallenges = this.offeredChallenges
    // .remove(challengeId);
    // // add to accepted challenges
    // Map<Integer, AcceptedChallenge> acceptedChallenges =
    // this.acceptedChallenges
    // .put(challengeId, acceptedChallenge);
    //
    // // add to challengee.accepted
    // PlayerStore challengeePS = store.get(challengee).addAcceptedChallenge(
    // acceptedChallenge);
    // // remove from challenger.offered
    // PlayerStore challengerPS = store.get(challenger)
    // .removeOfferedChallenge(toBeAcceptedChallenge);
    //
    // Map<PlayerID, PlayerStore> newStore = store.put(challengee,
    // challengeePS).put(challenger, challengerPS);
    //
    // double challengeeBalance = accounts.get(challengee);
    // double challengerBalance = accounts.get(challenger);
    // double price = toBeAcceptedChallenge.price;
    //
    // Map<PlayerID, Double> newAccounts = accounts.put(challengee,
    // challengeeBalance - price).put(challenger,
    // challengerBalance + price);
    //
    // // create and return new admin state
    // AdminState ret = new AdminState(players, newStore, newAccounts);
    // ret.setOfferedChallenges(offeredChallenges);
    // ret.setAcceptedChallenges(acceptedChallenges);
    // return ret;
    // }

    PlayerContext getPlayerContext(Config config, PlayerID pid){
        PlayerStore ps = store.get(pid);
        List<OfferedChallenge> offeredByOthers = new Empty<OfferedChallenge>();
        for (Entry<PlayerID, PlayerStore> entry : store) {
            if (!entry.key.equals(pid)) {
                offeredByOthers = offeredByOthers.push(entry.val.offered);
            }
        }
        return new PlayerContext(config, pid, accounts.get(pid), ps.offered,
                offeredByOthers, ps.accepted, ps.provided);
    }

    /** Create an AdminState from the given Maps... */
    public static AdminState create(Map<PlayerID, PlayerSpec> specs,
            Map<PlayerID, PlayerStore> strs, Map<PlayerID, Double> acc){
        return new AdminState(specs, strs, acc);
    }

    /** Create the initial (empty) AdminState */
    public static AdminState initial(){
        return initial(Map.<PlayerID, PlayerSpec> create());
    }

    /**
     * Create the initial (empty) AdminState With players drawn from the given
     * File
     */
    public static AdminState initial(String playerFile) throws ParseException,
            java.io.IOException{
        List<PlayerSpec> plyrs = PlayersFile.parse(
                new java.io.FileInputStream(playerFile)).getPlayerSpecs();
        return initial(plyrs);
    }

    /** Create the initial (empty) AdminState With the given players drawn */
    public static AdminState initial(List<PlayerSpec> plyrs){
        Map<PlayerID, PlayerSpec> specs = plyrs.fold(
                new List.Fold<PlayerSpec, Map<PlayerID, PlayerSpec>>() {

                    int start = (int) (Math.random() * 100);

                    @Override
                    public Map<PlayerID, PlayerSpec> fold(PlayerSpec s,
                            Map<PlayerID, PlayerSpec> m){
                        return m.put(new PlayerID(start++), s);
                    }
                }, Map.<PlayerID, PlayerSpec> create());
        return initial(specs);
    }

    /** Create the initial (empty) AdminState With the given players */
    public static AdminState initial(Map<PlayerID, PlayerSpec> plyrs){
        return create(plyrs, Map.<PlayerID, PlayerStore> create(), Map
                .<PlayerID, Double> create());
    }

    /** DGP method from Class PrintHeapToString */
    @Override
    public String toString(){
        return scg.gen.PrintHeapToString.PrintHeapToStringM(this);
    }

}
