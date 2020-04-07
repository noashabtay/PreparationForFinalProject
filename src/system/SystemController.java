package system;

import Asset.Coach;
import Asset.Manager;
import Asset.Player;
import Asset.TeamMember;
import Game.Team;
import League.*;
import Users.*;
import Exception.*;
import javafx.util.Pair;

import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;

public class SystemController {
    private String name;
    private HashMap<String , League> leagues;
    private HashMap<String , Season> seasons;
    private HashMap<String, SystemManager> systemManagers;
    private HashMap<String, Role> roles; // hash map <mail,role>
    private HashMap<String, Team> teams;
    //  private HashMap<Member,String> passwordValidation;

    /**
     * constructor
     * @param name
     */
    public SystemController(String name) {
        this.name = name;
        leagues = new HashMap<>();
        seasons = new HashMap<>();
        systemManagers = new HashMap<>();
        roles = new HashMap<>();
        teams = new HashMap<>();
        //todo
//        password verifications
//        passwordValidation=new HashMap<>();
//        for(Role r:roles){
//            if(r instanceof Member){
//                
//            }
//        }
    }

    public void initSystem(String userName, String password) {
        //check if the user name and the password are connect
    }

    /*************************************** function for guest******************************************/

    /**
     * this function makes a Guest into a member
     * if the member's mail doesnt exist -
     * we will remove the Guest from the roles map and add create a Fan member by default and return true
     * if the member's mail exist in the system - prints a error message and return false.
     * @return true = success or false = failed to sign
     */
    public Member signIn(String userName, String userMail, String password) throws MemberAlreadyExistException {
        if (roles.containsKey(userMail)) {
            throw new MemberAlreadyExistException();
        } else {
            Fan newMember = new Fan(userName, userMail, password);
            roles.put(newMember.getUserMail(), newMember);
            return newMember;
        }
    }

    /**
     * this function makes a guest into an existing member.
     * if the member doesnt exist - return null
     * if the member exist - return the member
     *
     * @return
     */
    public Member logIn(String userMail, String userPassword) throws MemberDontExist {
        if (roles.containsKey(userMail)) {
            Member existingMember = (Member) roles.get(userMail);
            roles.remove("0");
            return existingMember;
        }
        else if(systemManagers.containsKey(userMail)) {
            Member existingMember = (Member) roles.get(userMail);
            roles.remove("0");
            return existingMember;
        }
        else {
            throw new MemberDontExist();
        }
    }


    /*************************************** function for system manager******************************************/

    /***
     * this function get id of the refree to remove and the id of the system manager that take care of this function
     * if the refree didnt exist - return false
     * if the refree exist - delete it and return true
     */
    public boolean removeReferee(String systemManagerId, String refreeId) throws MemberNotSystemManager {
        SystemManager systemManager = systemManagers.get(systemManagerId);
        if (null != systemManager) {
            return systemManager.removeReferee(refreeId);
        } else {
            throw new MemberNotSystemManager();
        }
    }

    /***
     * this function get id of the member to make refree and the id of the system manager that take care of this function
     * if the refree already exist - return false
     * if the refree not exist and success of adding it - add it and return true
     */
    public boolean addRefree(String systemManagerId, String refreeId, boolean ifMainRefree) throws MemberNotSystemManager {
        SystemManager systemManager = systemManagers.get(systemManagerId);
        if (null != systemManager) {
            return systemManager.addReferee(refreeId, ifMainRefree);
        } else {
            throw new MemberNotSystemManager();
        }
    }

    /**
     * this function get the team name and the id of the system manager that take care of this function
     * if the team name already exist - close the team and return true
     * if the team dont exist return false
     * @param systemManagerId
     * @param teamName
     * @return
     * @throws MemberNotSystemManager
     */
    public boolean closeTeam(String systemManagerId, String teamName) throws MemberNotSystemManager {
        SystemManager systemManager = systemManagers.get(systemManagerId);
        if (null != systemManager) {
            return systemManager.closeTeam(teamName);
        } else {
            throw new MemberNotSystemManager();
        }
    }

    /**
     * this function get the id of the member we want to delete and the id of the system manager that take care of this function
     * @param systemManagerId
     * @param id
     * @return
     * @throws MemberNotSystemManager
     */
    public boolean removeMember(String systemManagerId, String id) throws MemberNotSystemManager {
        SystemManager systemManager = systemManagers.get(systemManagerId);
        if (null != systemManager) {
            return systemManager.removeMember(id);
        } else {
            throw new MemberNotSystemManager();
        }
    }

    /**
     * this function get the path to the complaint file and the id of the system manager that take care of this function
     * @param systemManagerId
     * @param path
     * @throws MemberNotSystemManager
     */
    public void watchComplaint(String systemManagerId , String path) throws MemberNotSystemManager {
        SystemManager systemManager = systemManagers.get(systemManagerId);
        if (null != systemManager) {
            LinkedList<String> complaint= systemManager.watchComplaint(path);
        } else {
            throw new MemberNotSystemManager();
        }
    }

    /**
     * this function get the path to the complaint file , the response for the complaint and the id of the system manager that take care of this function
     * @param systemManagerId
     * @param path
     * @param responseForComplaint
     * @return
     * @throws MemberNotSystemManager
     */
    public boolean responseComplaint(String systemManagerId , String path , LinkedList<Pair<String , String>>responseForComplaint) throws MemberNotSystemManager {
        SystemManager systemManager = systemManagers.get(systemManagerId);
        if (null != systemManager) {
            return systemManager.ResponseComplaint(path , responseForComplaint);
        } else {
            throw new MemberNotSystemManager();
        }
    }

    public void schedulingGames(String systemManagerId) throws MemberNotSystemManager {
        SystemManager systemManager = systemManagers.get(systemManagerId);
        if (null != systemManager) {
         //   systemManager.schedulingGames();
        } else {
            throw new MemberNotSystemManager();
        }
    }

    public void viewSystemInformation(String systemManagerId) throws MemberNotSystemManager {
        SystemManager systemManager = systemManagers.get(systemManagerId);
        if (null != systemManager) {
            systemManager.viewSystemInformation();
        } else {
            throw new MemberNotSystemManager();
        }
    }

    /**
     *  this function get the team name and all the team member and the id of the system manager that take care of this function
     *   if the team name not exist - open the team and return true
     *   if the team exist return false
     * @param systemManagerId
     * @param players
     * @param coachs
     * @param managers
     * @param owners
     * @param teamName
     * @return
     * @throws MemberNotSystemManager
     */
    public boolean addTeam(String systemManagerId, LinkedList<String> players, LinkedList<String> coachs, LinkedList<String> managers, LinkedList<String> owners, String teamName) throws MemberNotSystemManager {
        SystemManager systemManager = systemManagers.get(systemManagerId);
        if (null != systemManager) {
            return systemManager.addNewTeam(players,coachs,managers,owners,teamName);
        } else {
            throw new MemberNotSystemManager();
        }
    }

    /***************************************help function for system manager******************************************/

    public boolean notAllTheIdAreMembers(LinkedList<String> idPlayers, LinkedList<String> idCoach, LinkedList<String> idManager, LinkedList<String> idOwner) {
        for (int i = 0; i < idPlayers.size(); i++) {
            if (roles.containsKey(idPlayers.get(i)) == false)
                return true;
        }
        for (int i = 0; i < idCoach.size(); i++) {
            if (roles.containsKey(idCoach.get(i)) == false)
                return true;
        }
        for (int i = 0; i < idManager.size(); i++) {
            if (roles.containsKey(idManager.get(i)) == false)
                return true;
        }
        for (int i = 0; i < idOwner.size(); i++) {
            if (roles.containsKey(idOwner.get(i)) == false)
                return true;
        }
        return false;
    }

    public boolean alreadyIncludeThisTeamName(String teamName) {
        if (teams == null) {
            return false;
        }
        for (String name : teams.keySet()
        ) {
            if (name.equals(teamName))
                return true;
        }
        return false;
    }

    public LinkedList<Member> returnFromSystemTheExactUsers(LinkedList<String> id) {
        LinkedList<Member> toReturn = new LinkedList<>();
        for (int i = 0; i < id.size(); i++) {
            toReturn.add((Member) roles.get(id.get(i)));
        }
        return toReturn;
    }

    public void makeTheRoleARefree(String id, boolean mainRefree) {
        //if the refree is main refree the boolean filed wil be true
        //change the role from fan to refree
        Fan fan = (Fan) roles.get(id);
        Referee referee;
        if (mainRefree)
            referee = new MainReferee(fan.getName(), fan.getUserMail(), fan.getPassword(), "");
        else
            referee = new SecondaryReferee(fan.getName(), fan.getUserMail(), fan.getPassword(), "");
        roles.put(fan.getUserMail(), referee);
    }

    /***************************************delete function function******************************************/

    public void deleteTeam(String teamName) {
        teams.remove(teamName);
    }

    public void deleteRefree(String id) {
        roles.remove(id);
    }

    public void deleteRole(String id) {
        roles.remove(id);
    }

    /***************************************exist function******************************************/

    public boolean existFan(String id) {
        return roles.get(id) instanceof Fan;
    }

    public boolean existTeamName(String teamName) {
        if (teams.containsKey(teamName))
            return true;
        else
            return false;
    }

    public boolean existRefree(String id) {
        if (roles.get(id) instanceof Referee)
            return true;
        else
            return false;
    }

    public boolean existRole(String id) {
        return roles.containsKey(id);
    }


    /***************************************get function******************************************/

    public League getLeague(String leagueId) {
        return leagues.get(leagueId);
    }

    public Season getSeason(String seasonId) {
        return seasons.get(seasonId);
    }

    public Team getTeam(String teamName) {
        return teams.get(teamName);
    }

    public Referee getRefree(String id) {
        return (Referee) roles.get(id);
    }
/***************************************add function******************************************/

    /***
     * this function add player to the roles list
     * @param player
     */
    public void addPlayer(Player player) {
        roles.put(player.getUserMail(), player);
    }

    /***
     * this function add coach to the roles list
     * @param coach
     */
    public void addCoach(Coach coach) {
        roles.put(coach.getUserMail(), coach);
    }

    /**
     * this function add manager to the roles list
     * @param manager
     */
    public void addManager(Manager manager) {
        roles.put(manager.getUserMail(), manager);
    }

    /**
     * this function add owner to the roles list
     * @param owner
     */
    public void addOwner(Owner owner) {
        roles.put(owner.getUserMail(), owner);
    }

    /**
     * this function add team to the teams list
     * @param team
     */
    public void addTeam(Team team) {
        teams.put(team.getName(), team);
    }

    /**
     * this function add system manager to the system manager list
     * @param systemManager
     */
    public void addSystemManager(SystemManager systemManager) {
        systemManagers.put(systemManager.getUserMail(), systemManager);
    }

    /**
     * this function add fan to the roles list
     * @param fan1
     */
    public void addFan(Fan fan1) {
        roles.put(fan1.getUserMail() ,fan1);
    }

    /**
     * this function is used in test - return if the member exist in the system
     * @param memberMail
     * @return
     */
    public boolean ifMemberExistTesting(String memberMail){
        if(memberMail!=null){
            return roles.containsKey(memberMail);
        }
        return false;
    }
    public int sizeOfMembersListTesting(){
        return this.roles.size();
    }
    public void addMemberTesting(Member member){
        this.roles.put(member.getUserMail(),member);
    }

}