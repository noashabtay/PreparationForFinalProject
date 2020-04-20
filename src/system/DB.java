package system;

import Asset.Coach;
import Asset.Manager;
import Asset.Player;
import Game.Team;
import League.*;
import Users.*;
import Exception.*;

import java.util.HashMap;

public class DB {

    private HashMap<String, League> leagues; //key-name of league, value-league
    private HashMap<String, Season> seasons; //key-year, value-season
    private HashMap<String, SystemManager> systemManagers;
    private HashMap<String, Role> roles; // hash map <mail,role> - maybe users instead roles??
    private HashMap<String, Team> teams;
    private HashMap<String, Referee> referees;
    private HashMap<String, ASchedulingPolicy> schedulingPolicies;
    //  private HashMap<Member,String> passwordValidation;


    public DB() {
        this.leagues = new HashMap<>();
        this.seasons = new HashMap<>();
        this.systemManagers = new HashMap<>();
        this.roles = new HashMap<>();
        this.teams = new HashMap<>();
        this.referees = new HashMap<>();
        schedulingPolicies = new HashMap<>();
    }

    /********************************Association Delegate Functions********************************/
    public void removeLeague(String leagueName) {
        leagues.remove(leagueName);
    }
    public void addLeague(League league) {
        leagues.put(league.getName(), league);
    }
    public void removeSeason(String year) {
        seasons.remove(year);
    }
    public void addSeason(Season season) {
        seasons.put(season.getYear(), season);
    }
    /***************************************Getters******************************************/

    public HashMap<String, League> getLeagues() { return leagues; }

    public HashMap<String, Role> getRoles() { return roles; }

    public HashMap<String, Team> getTeams() { return teams; }

    public League getLeague(String leagueId) throws ObjectNotExist {
        if(leagues.containsKey(leagueId))
            return leagues.get(leagueId);
        else
            throw new ObjectNotExist(leagueId);
    }

    public Season getSeason(String seasonId) throws ObjectNotExist {
        if(seasons.containsKey(seasonId))
            return seasons.get(seasonId);
        else
            throw new ObjectNotExist(seasonId);
    }


    public Team getTeam(String teamName) { return teams.get(teamName); }

    public Referee getReferee(String id) { return (Referee) roles.get(id); }

    public Owner getOwner(String id) { return (Owner) roles.get(id); }

    public Player getPlayer(String id) { return (Player) roles.get(id); }

    public Manager getManager(String id) { return (Manager) roles.get(id); }

    public Coach getCoach(String id) { return (Coach) roles.get(id); }

    public Fan getFan(String id){ return (Fan)roles.get(id); }

    public SystemManager getSystemManager(String id){ return systemManagers.get(id); }

    public HashMap<String, Referee> getReferees() { return referees; }

    public HashMap<String, Fan> getFans() {
        HashMap<String,Fan> toReturn=new HashMap<>();
        for(String role:roles.keySet())
        {
            if(roles.get(role) instanceof  Fan)
            {
                toReturn.put(role,(Fan)roles.get(role));
            }
        }
        return toReturn;
    }

    public HashMap<String, Player> getPlayers() {
        HashMap<String,Player> toReturn=new HashMap<>();
        for(String role:roles.keySet())
        {
            if(roles.get(role) instanceof  Player)
            {
                toReturn.put(role,(Player) roles.get(role));
            }
        }
        return toReturn;
    }

    public HashMap<String, Owner> getOwners() {
        HashMap<String,Owner> toReturn=new HashMap<>();
        for(String role:roles.keySet())
        {
            if(roles.get(role) instanceof  Owner)
            {
                toReturn.put(role,(Owner) roles.get(role));
            }
        }
        return toReturn;

    }

    public HashMap<String, Manager> getManagers() {
        HashMap<String,Manager> toReturn=new HashMap<>();
        for(String role:roles.keySet())
        {
            if(roles.get(role) instanceof  Manager)
            {
                toReturn.put(role,(Manager) roles.get(role));
            }
        }
        return toReturn;
    }

    public HashMap<String, Coach> getCoaches() {
        HashMap<String,Coach> toReturn=new HashMap<>();
        for(String role:roles.keySet())
        {
            if(roles.get(role) instanceof  Coach)
            {
                toReturn.put(role,(Coach)roles.get(role));
            }
        }
        return toReturn;
    }

    public HashMap<String , Member> getMembers() {
        HashMap<String,Member> toReturn=new HashMap<>();
        for(String role:roles.keySet())
        {
            if(roles.get(role) instanceof  Member)
            {
                toReturn.put(role,(Member)roles.get(role));
            }
        }
        return toReturn;
    }
    /***************************************delete function******************************************/

    public void removeRole(String id) { roles.remove(id); }

    public void removeTeam(String name) { teams.remove(name); }


    /***************************************add function******************************************/

    /***
     * this function add player to the roles list
     * @param player
     */
    public void addPlayer(Player player) { roles.put(player.getUserMail(), player); }

    /***
     * this function add coach to the roles list
     * @param coach
     */
    public void addCoach(Coach coach) { roles.put(coach.getUserMail(), coach); }

    /**
     * this function add manager to the roles list
     *
     * @param manager
     */
    public void addManager(Manager manager) { roles.put(manager.getUserMail(), manager); }

    /**
     * this function add owner to the roles list
     *
     * @param owner
     */
    public void addOwner(Owner owner) { roles.put(owner.getUserMail(), owner); }

    /**
     * this function add team to the teams list
     *
     * @param team
     */
    public void addTeam(Team team) {
        teams.put(team.getName(), team);
    }

    /**
     * this function add system manager to the system manager list
     *
     * @param systemManager
     */
    public void addSystemManager(SystemManager systemManager) { systemManagers.put(systemManager.getUserMail(), systemManager); }


    /**
     * this function add AssociationDelegate to roles list
     * @param associationDelegate
     */
    public void addAssociationDelegate(AssociationDelegate associationDelegate) {
        if(!this.roles.containsKey(associationDelegate.getUserMail())){
            this.roles.put(associationDelegate.getUserMail(),associationDelegate);
        }
    }

    public void addReferee(Referee referee) {
        referees.put(referee.getUserMail(),referee);
    }
    /*****************************************exist function****************************************/
    public boolean existSeason(String year){
        return this.seasons.containsKey(year);
    }
    public boolean existLeague(String name){
        return this.seasons.containsKey(name);
    }
    public boolean existTeam(String name){
        return this.teams.containsKey(name);
    }
    /***************************************Guest function******************************************/

    /**
     * this function check if this member exist
     * @param id
     * @return
     */
    public boolean existMember(String id) {
        return (this.roles.containsKey(id) || this.systemManagers.containsKey(id));
    }
    /**
     * this function add fan to the roles list
     *
     * @param fan
     */
    public void addFan(Fan fan) {
        roles.put(fan.getUserMail(), fan);
    }

    public Member getMember(String id){
        if(roles.containsKey(id))
            return (Member)this.roles.get(id);
        return (Member)this.systemManagers.get(id);
    }

    /***************************************Exist function*****************************/
    public boolean existRefree(String refreeId) {
        if(referees.containsKey(refreeId)==true)
        {
            return true;
        }
        return false;
    }

    public boolean existFan(String fanId) {
        if(roles.containsKey(fanId)==true && roles.get(fanId) instanceof  Fan)
        {
            return true;
        }
        return false;
    }


    public HashMap<String,Season> getSeasons() {
        return seasons;
    }

    public HashMap<String, ASchedulingPolicy> getSchedulingPolicies(){
        return schedulingPolicies;
    }

    public void addSchedulingPolicies(ASchedulingPolicy policy) {
        schedulingPolicies.put(policy.getNameOfPolicy(), policy);
    }

    public boolean existSystemManager(String id) {
        if(this.systemManagers.containsKey(id))
            return true;
        return false;
    }
}
