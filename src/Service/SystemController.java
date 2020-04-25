package Service;

import Domain.Asset.Coach;
import Domain.Asset.Manager;
import Domain.Asset.Player;
import Domain.League.ASchedulingPolicy;
import Domain.League.IScorePolicy;
import Domain.League.League;
import Domain.League.Season;
import Domain.Users.*;
import Domain.Game.Game;
import Domain.Game.Team;
import Domain.Game.EventInGame;
import Exception.*;
import javafx.util.Pair;
import DataBase.DBController;

import java.util.*;

public class SystemController {
    private String name;
    private Role connectedUser;
    private DBController dbController;
    //  private HashMap<Member,String> passwordValidation;


    /**
     * constructor
     *
     * @param name
     */
    public SystemController(String name) {
        this.name = name;
        //this.initSystem("", "", ""); // change it
        try {
            initSystem();
        }catch (IncorrectInputException e) {

        } catch (DontHavePermissionException e) {
        }
//        password verifications
//        passwordValidation=new HashMap<>();
//        for(Role r:roles){
//            if(r instanceof Member){
//                
//            }
//        }
        //member = user; (argument in the constructor-Member user- Fan, Owner, AD, Referee...)
    }

    public void initSystem() throws IncorrectInputException, DontHavePermissionException {
        //check if the user name and the password are connect
        dbController = new DBController();
        try{
            Fan f = (Fan)signIn("admin", "admin@gmail.com" , "123",new Date(1995,2,15));
            SystemManager systemManager = new SystemManager("admin", "admin@gmail.com", f.getPassword(), this.dbController ,new Date(1995,2,15) );
            Role role=connectedUser;
            connectedUser=systemManager;
            this.addSystemManager(f.getUserMail());
            connectedUser=role;

        }catch (AlreadyExistException e){

        } catch (MemberNotExist memberNotExist) {
            memberNotExist.printStackTrace();
        }
        connectedUser = new Guest(dbController ,null);
        System.out.println("Init System:");
        System.out.println("Connect to Security System");
        //todo
    }

    /*************************************** function for guest******************************************/
    /**
     * this function makes a Guest into a member
     * if the member's mail doesnt exist -
     * we will remove the Guest from the roles map and add create a Fan member by default and return true
     * if the member's mail exist in the system - prints a error message and return false.
     *
     * @return true = success or false = failed to sign
     */
    public Member signIn(String userName, String userMail, String password , Date birthDate) throws IncorrectInputException, AlreadyExistException, DontHavePermissionException {
        if(connectedUser==null)
        {
            Guest guest=new Guest(dbController,new Date(1,1,1));
            return guest.signIn(userMail, userName, password , birthDate);
        }
        return ((Guest) connectedUser).signIn(userMail, userName, password , birthDate);

    }
    public Role logOut(){
        //todo
        this.connectedUser = new Guest(this.dbController , null);
        return this.connectedUser;
    }
    /**
     * this function makes a guest into an existing member.
     * if the member doesnt exist - return null
     * if the member exist - return the member
     *
     * @return
     */
    public Member logIn(String userMail, String userPassword) throws MemberNotExist, PasswordDontMatchException, DontHavePermissionException {
        if(connectedUser==null)
        {
            Guest guest=new Guest(dbController,new Date(1995,2,1));
            this.connectedUser=guest.logIn(userMail,userPassword);
            return (Member) this.connectedUser;
        }
        this.connectedUser =((Guest)this.connectedUser).logIn(userMail, userPassword);
        return (Member) this.connectedUser;
    }
    /*************************************** function for system manager******************************************/

    public boolean removeAssociationDelegate(String id) throws DontHavePermissionException {
        if (connectedUser instanceof SystemManager) {
            SystemManager systemManager = (SystemManager) connectedUser;
            return systemManager.removeAssociationDelegate(id);
        } else {
            throw new DontHavePermissionException();
        }
    }

    public boolean removeOwner(String ownerId) throws DontHavePermissionException, IncorrectInputException, NotReadyToDelete, MemberNotExist {
        if (connectedUser instanceof SystemManager) {
            SystemManager systemManager = (SystemManager) connectedUser;
            return systemManager.removeOwner(ownerId);
        } else {
            throw new DontHavePermissionException();
        }
    }

    public boolean removeSystemManager(String id) throws DontHavePermissionException, MemberNotExist, IncorrectInputException, NotReadyToDelete, AlreadyExistException {
        if (connectedUser instanceof SystemManager) {
            SystemManager systemManager = (SystemManager) connectedUser;
            return systemManager.removeSystemManager(id);
        } else {
            throw new DontHavePermissionException();
        }
    }

    public Referee getReferee(String s) throws DontHavePermissionException, ObjectNotExist {
//        if (connectedUser instanceof SystemManager) {
//            SystemManager systemManager = (SystemManager) connectedUser;
           return dbController.getReferee(connectedUser,s);
//        } else {
//            throw new DontHavePermissionException();
//        }
    }

    public void addSystemManager(String id) throws DontHavePermissionException, MemberNotExist, AlreadyExistException {
        if (connectedUser instanceof SystemManager) {
            SystemManager systemManager = (SystemManager) connectedUser;
            systemManager.addSystemManager(id);
        } else {
            throw new DontHavePermissionException();
        }
    }

    /***
     * this function get id of the refree to remove and the id of the system manager that take care of this function
     * if the referee didnt exist - return false
     * if the referee exist - delete it and return true
     */
    public boolean removeReferee(String refereeId) throws DontHavePermissionException, IncorrectInputException, MemberNotExist, AlreadyExistException {
            if (connectedUser instanceof SystemManager) {
                SystemManager systemManager = (SystemManager) connectedUser;
                return systemManager.removeReferee(refereeId);
            } else {
                throw new DontHavePermissionException();
            }
        }
    /***
     * this function get id of the member to make referee and the id of the system manager that take care of this function
     * if the referee already exist - return false
     * if the referee not exist and success of adding it - add it and return true
     */
    public boolean addReferee(String refereeId, boolean ifMainReferee) throws DontHavePermissionException, MemberAlreadyExistException, AlreadyExistException, MemberNotExist, IncorrectInputException {
        if (connectedUser instanceof SystemManager) {
            SystemManager systemManager = (SystemManager) connectedUser;
            return systemManager.addReferee(refereeId, ifMainReferee);
        } else {
            throw new DontHavePermissionException();
        }
    }
    /**
     * this function get the team name and the id of the system manager that take care of this function
     * if the team name already exist - close the team and return true
     * if the team dont exist return false
     *
     * @param teamName
     * @return
     * @throws DontHavePermissionException
     */
    public boolean closeTeam(String teamName) throws DontHavePermissionException, ObjectNotExist, MemberNotExist, AlreadyExistException, IncorrectInputException {
        if (connectedUser instanceof SystemManager) {
            SystemManager systemManager = (SystemManager) connectedUser;
            return systemManager.closeTeam(teamName);
        } else {
            throw new DontHavePermissionException();
        }
    }

    /**
     * this function get the id of the member we want to delete and the id of the system manager that take care of this function
     *
     * @param id
     * @return
     * @throws DontHavePermissionException
     */
    public boolean removeMember(String id) throws DontHavePermissionException, MemberNotExist, IncorrectInputException, AlreadyExistException, NotReadyToDelete {
        if (connectedUser instanceof SystemManager) {
            SystemManager systemManager = (SystemManager) connectedUser;
            return systemManager.removeMember(id);
        } else {
            throw new DontHavePermissionException();
        }
    }

    /**
     * this function get the path to the complaint file and the id of the system manager that take care of this function
     *
     * @param path
     * @throws DontHavePermissionException
     */
    public void watchComplaint(String path) throws DontHavePermissionException {
        if (connectedUser instanceof SystemManager) {
            SystemManager systemManager = (SystemManager) connectedUser;
            LinkedList<String> complaint = systemManager.watchComplaint(path);
        } else {
            throw new DontHavePermissionException();
        }
    }

    /**
     * this function get the path to the complaint file , the response for the complaint and the id of the system manager that take care of this function
     *
     * @param path
     * @param responseForComplaint
     * @return
     * @throws DontHavePermissionException
     */
    public boolean responseComplaint(String path, LinkedList<Pair<String, String>> responseForComplaint) throws DontHavePermissionException {
        if (connectedUser instanceof SystemManager) {
            SystemManager systemManager = (SystemManager) connectedUser;
            return systemManager.ResponseComplaint(path, responseForComplaint);
        } else {
            throw new DontHavePermissionException();
        }
    }

    public void schedulingGames(String seasonId, String leagueId) throws DontHavePermissionException, ObjectNotExist, IncorrectInputException {

           if (connectedUser instanceof SystemManager) {
               SystemManager systemManager = (SystemManager) connectedUser;
               systemManager.schedulingGames(seasonId, leagueId);
           } else {
               throw new DontHavePermissionException();
           }


    }

    public void viewSystemInformation(String path) throws DontHavePermissionException {
        if (connectedUser instanceof SystemManager) {
            SystemManager systemManager = (SystemManager) connectedUser;
            systemManager.viewSystemInformation(path);
        } else {
            throw new DontHavePermissionException();
        }
    }

//    /**
//     * this function get the team name and all the team member and the id of the system manager that take care of this function
//     * if the team name not exist - open the team and return true
//     * if the team exist return false
//     *
//     * @param players
//     * @param coachs
//     * @param managers
//     * @param owners
//     * @param teamName
//     * @return
//     * @throws DontHavePermissionException
//     */
    /*
    public boolean addTeam(LinkedList<String> players, LinkedList<String> coachs, LinkedList<String> managers, LinkedList<String> owners, String teamName) throws DontHavePermissionException {
        if (connectedUser instanceof SystemManager) {
            SystemManager systemManager = (SystemManager) connectedUser;
            return systemManager.addNewTeam(players, coachs, managers, owners, teamName);
        } else {
            throw new DontHavePermissionException();
        }
    }
*/
    public boolean addTeam(String teamName , String ownerId) throws DontHavePermissionException, ObjectNotExist, MemberNotExist, ObjectAlreadyExist, AlreadyExistException, IncorrectInputException {
        if (connectedUser instanceof SystemManager) {
            SystemManager systemManager = (SystemManager) connectedUser;
            return systemManager.addNewTeam(teamName , ownerId);
        } else {
            throw new DontHavePermissionException();
        }
    }

    public void addAssociationDelegate(String id) throws DontHavePermissionException, AlreadyExistException, MemberNotExist {
        if(connectedUser instanceof SystemManager){
            ((SystemManager)connectedUser).addAssociationDelegate(id);
            return;
        }
        throw new DontHavePermissionException();
    }

    public void addOwner(String id) throws DontHavePermissionException, AlreadyExistException, MemberNotExist {
        if(connectedUser instanceof SystemManager){
            ((SystemManager)connectedUser).addOwner(id);
            return;
        }
        throw new DontHavePermissionException();
    }

    /*************************************** function for owner******************************************/

    /**
     * owner:
     * add a coach
     *
     * @param teamName
     * @param mailId
     * @throws NoEnoughMoney
     */

    public void addCoach(String teamName, String mailId) throws ObjectNotExist, NoEnoughMoney, MemberNotExist, AlreadyExistException, DontHavePermissionException {
        if(!dbController.getTeams(this.connectedUser).containsKey(teamName))
            throw new ObjectNotExist("team not exist");
        if (dbController.getTeams(this.connectedUser).get(teamName).getAccount().getAmountOfTeam()-50 < 0)
            throw new NoEnoughMoney();
        if (!dbController.getRoles(this.connectedUser).containsKey(mailId))
            throw new MemberNotExist();
        if (dbController.getRoles(this.connectedUser).containsKey(mailId))
            if(dbController.getCoaches(this.connectedUser).containsKey(mailId))
               throw new AlreadyExistException();


        ((Owner) connectedUser).addCoach(teamName, mailId);
    }

    /**
     * owner:
     * add a player
     *
     * @param mailId
     * @param teamName
     * @param year
     * @param month
     * @param day
     * @param rolePlayer
     */
    public void addPlayer(String mailId, String teamName, int year, int month, int day, String rolePlayer) throws ObjectNotExist, IncorrectInputException, MemberNotExist, AlreadyExistException, DontHavePermissionException, NoEnoughMoney {
        if(!dbController.getTeams(this.connectedUser).containsKey(teamName))
            throw new ObjectNotExist("team not exist");
        if (dbController.getTeams(this.connectedUser).get(teamName).getAccount().getAmountOfTeam()-50 < 0)
            throw new NoEnoughMoney();
        if (!dbController.getRoles(this.connectedUser).containsKey(mailId))
            throw new MemberNotExist();
        if (dbController.getRoles(this.connectedUser).containsKey(mailId))
            if(dbController.getPlayers(this.connectedUser).containsKey(mailId))
                 throw new AlreadyExistException();
        if(dbController.getTeams(this.connectedUser).get(teamName).getPlayers().contains(mailId))
            throw new AlreadyExistException();

        if (year < 0 || year > 2020 || month > 12 || month < 1 || day < 1 || day > 32 || rolePlayer == null || rolePlayer=="")
            throw new IncorrectInputException();

        ((Owner) connectedUser).addPlayer(teamName, mailId, year, month, day, rolePlayer);
    }

    /**
     * owner:
     * add a field to list of training field of his team
     *
     * @param teamName
     * @param fieldName
     */
    public void addField(String teamName, String fieldName) throws DontHavePermissionException, IncorrectInputException, AlreadyExistException, ObjectAlreadyExist, NoEnoughMoney, ObjectNotExist {
        if(!dbController.getTeams(this.connectedUser).containsKey(teamName))
            throw new ObjectNotExist("team not exist");
        if (dbController.getTeams(this.connectedUser).get(teamName).getAccount().getAmountOfTeam()-50 < 0)
            throw new NoEnoughMoney();
        if (dbController.getTeams(this.connectedUser).get(teamName).getFieldFromTrainingFields(fieldName)!=null)
            throw new ObjectAlreadyExist();
        ((Owner) connectedUser).addField(teamName, fieldName);
    }

    /**
     * owner:
     * add new manager to one of his groups
     *
     * @param teamName
     * @param mailId
     * @throws NoEnoughMoney
     * @throws ObjectNotExist
     */
    public void addManager(String teamName, String mailId) throws NoEnoughMoney, ObjectNotExist, MemberNotExist, AlreadyExistException, DontHavePermissionException {
        if (!dbController.getRoles(this.connectedUser).containsKey(mailId))
            throw new MemberNotExist();
        if (dbController.getRoles(this.connectedUser).containsKey(mailId))
            if(dbController.getManagers(this.connectedUser).containsKey(mailId))
                     throw new AlreadyExistException();
        if (!dbController.getTeams(this.connectedUser).containsKey(teamName))
            throw new ObjectNotExist("team not exist");
        if (dbController.getTeams(this.connectedUser).get(teamName).getAccount().getAmountOfTeam()-50 < 0)
            throw new NoEnoughMoney();

        ((Owner) connectedUser).addManager(teamName, mailId);
    }

    /**
     * owner:
     * removes a manager
     *
     * @param teamName
     * @param mailToRemove
     */
    public void removeManager(String teamName, String mailToRemove) throws ObjectNotExist, MemberNotExist, AlreadyExistException, DontHavePermissionException {
        if(! (this.connectedUser instanceof Owner))
            throw new DontHavePermissionException();
        if (!dbController.getTeams(this.connectedUser).containsKey(teamName))
            throw new ObjectNotExist("team not exist ");
        if (!dbController.getRoles(this.connectedUser).containsKey(mailToRemove))
            throw new MemberNotExist();
        if (!dbController.getRoles(this.connectedUser).containsKey(mailToRemove))
            throw new ObjectNotExist("member not exist");

        ((Owner) connectedUser).removeManager(teamName, mailToRemove);

    }

    /**
     * owner:
     * remove coach
     *
     * @param teamName
     * @param mailToRemove
     * @throws MemberNotExist
     */
    public void removeCoach(String teamName, String mailToRemove) throws ObjectNotExist, MemberNotExist, AlreadyExistException, DontHavePermissionException {
        if(! (this.connectedUser instanceof Owner))
            throw new DontHavePermissionException();
        if (!dbController.getTeams(this.connectedUser).containsKey(teamName))
            throw new ObjectNotExist("team not exist");
        if (dbController.getRoles(this.connectedUser).containsKey(connectedUser.getName()))
            if(dbController.getCoaches(this.connectedUser).containsKey(mailToRemove))
                 throw new DontHavePermissionException();
        if (!dbController.getRoles(this.connectedUser).containsKey(mailToRemove))
            throw new MemberNotExist();

        ((Owner) connectedUser).removeCoach(teamName, mailToRemove);
    }

    /**
     * owner:
     * remove player from team
     *
     * @param teamName
     * @param mailToRemove
     */
    public void removePlayer(String teamName, String mailToRemove) throws DontHavePermissionException, ObjectNotExist, MemberNotExist, AlreadyExistException {
        if(! (this.connectedUser instanceof Owner))
            throw new DontHavePermissionException();
        if (!dbController.getTeams(this.connectedUser).containsKey(teamName))
            throw new ObjectNotExist("team not exist");
        if ((dbController.getRoles(this.connectedUser).containsKey(mailToRemove)))
            if(!dbController.getPlayers(this.connectedUser).containsKey(mailToRemove))
                  throw new MemberNotExist();
        if (!dbController.getRoles(this.connectedUser).containsKey(mailToRemove))
            throw new MemberNotExist();

        ((Owner) connectedUser).removePlayer(teamName, mailToRemove);
    }

    /**
     * owner:
     * removes a field
     *
     * @param teamName
     * @param fieldName
     */
    public void removeField(String teamName, String fieldName) throws ObjectNotExist, DontHavePermissionException, MemberNotExist, IncorrectInputException {
        if(! (this.connectedUser instanceof Owner))
            throw new DontHavePermissionException();
        if (!dbController.getTeams(this.connectedUser).containsKey(teamName))
            throw new ObjectNotExist("team not exist");
        if (!dbController.getTeams(this.connectedUser).containsKey(teamName))
            throw new ObjectNotExist("");
        if(dbController.getTeams(this.connectedUser).get(teamName).getField(fieldName)==null)
           throw new ObjectNotExist("field not exist");
        ((Owner) connectedUser).removeField(teamName, fieldName);
    }

    /**
     * owner:
     * add new owner to one of his groups
     *
     * @param teamName
     * @param mailId
     * @throws NoEnoughMoney
     * @throws ObjectNotExist
     * @throws MemberNotExist
     */
    public void addNewOwner(String teamName, String mailId) throws NoEnoughMoney, ObjectNotExist, MemberNotExist, AlreadyExistException, DontHavePermissionException {
        if (!dbController.getRoles(this.connectedUser).containsKey(mailId))
            throw new MemberNotExist();
        if (dbController.getRoles(this.connectedUser).containsKey(mailId))
            if (dbController.getOwners(this.connectedUser).containsKey(mailId))
                 throw new AlreadyExistException();
        if (!dbController.getTeams(this.connectedUser).containsKey(teamName))
            throw new ObjectNotExist("");
        if (dbController.getTeams(this.connectedUser).get(teamName).getAccount().getAmountOfTeam()-50 < 0)
            throw new NoEnoughMoney();

        ((Owner) connectedUser).addNewOwner(teamName, mailId);
    }

    /**
     * owner:
     * close team temporary
     *
     * @param teamName
     * @throws ObjectNotExist
     */
    public void temporaryTeamClosing(String teamName) throws ObjectNotExist, DontHavePermissionException {
        if(! (this.connectedUser instanceof Owner))
            throw new DontHavePermissionException();
        if (!dbController.getTeams(this.connectedUser).containsKey(teamName))
            throw new ObjectNotExist("team not exist");
        if (!((Owner) connectedUser).getTeams().containsKey(teamName))
            throw new ObjectNotExist("team not exist");
        ((Owner) connectedUser).temporaryTeamClosing(teamName);
    }

    /**
     * owner:
     * reopen team
     *
     * @param teamName
     * @throws ObjectNotExist
     */
    public void reopenClosedTeam(String teamName) throws ObjectNotExist, DontHavePermissionException {
        if(! (this.connectedUser instanceof Owner))
            throw new DontHavePermissionException();
        if (!dbController.getTeams(this.connectedUser).containsKey(teamName))
            throw new ObjectNotExist("");
        if (!((Owner) connectedUser).getTeams().containsKey(teamName))
            throw new ObjectNotExist("");
        ((Owner) connectedUser).reopenClosedTeam(teamName);
    }

    /**
     * owner:
     * add outcome of team
     *
     * @param teamName
     * @throws NoEnoughMoney
     * @throws ObjectNotExist
     */
    public void addOutCome(String teamName, String description, double amount) throws NoEnoughMoney, ObjectNotExist, AccountNotExist, IncorrectInputException, DontHavePermissionException {
       if(!dbController.getTeams(this.connectedUser).containsKey(teamName))
           throw new ObjectNotExist("team not exist");
        if (dbController.getTeams(this.connectedUser).get(teamName).getAccount().getAmountOfTeam() - amount < 0)
            throw new NoEnoughMoney();
        if (description == null || description.equals("") || amount < 0)
            throw new IncorrectInputException();
        if (dbController.getTeams(this.connectedUser).get(teamName).getAccount().getAmountOfTeam() < 0)
            throw new NoEnoughMoney();
        if (dbController.getTeams(this.connectedUser).get(teamName).getAccount() == null)
            throw new AccountNotExist();

        ((Owner) connectedUser).addOutCome(teamName, description, amount);
    }

    /**
     * owner:
     * add outcome of team
     *
     * @param teamName
     * @throws NoEnoughMoney
     * @throws ObjectNotExist
     */
    public void addInCome(String teamName, String description, double amount) throws NoEnoughMoney, ObjectNotExist, AccountNotExist, IncorrectInputException, DontHavePermissionException {
        if (description == null || description.equals("") || amount < 0)
            throw new IncorrectInputException();
        if (!dbController.getTeams(this.connectedUser).containsKey(teamName))
            throw new ObjectNotExist("");
        if (dbController.getTeams(this.connectedUser).get(teamName).getAccount().getAmountOfTeam()-amount < 0)
            throw new NoEnoughMoney();
        if (dbController.getTeams(this.connectedUser).get(teamName).getAccount() == null)
            throw new AccountNotExist();

        ((Owner) connectedUser).addInCome(teamName, description, amount);
    }


    /**
     * owner:
     * update role details
     * @param teamName
     * @param mailId
     * @param role
     * @throws NoEnoughMoney
     * @throws ObjectNotExist
     * @throws AccountNotExist
     * @throws IncorrectInputException
     * @throws DontHavePermissionException
     * @throws MemberNotExist
     * @throws AlreadyExistException
     */
    public void updatePlayerRole(String teamName, String mailId,String role) throws NoEnoughMoney, ObjectNotExist, AccountNotExist, IncorrectInputException, DontHavePermissionException, MemberNotExist, AlreadyExistException {
       if(role==null || role.equals(""))
           throw new IncorrectInputException();
        if (dbController.getRoles(this.connectedUser).containsKey(mailId))
            if (dbController.getPlayers(this.connectedUser).containsKey(mailId))
                if(dbController.getPlayers(this.connectedUser).get(mailId).getRole().equals(role))
                  throw new AlreadyExistException();
        if (!dbController.getTeams(this.connectedUser).containsKey(teamName))
            throw new ObjectNotExist("");

        ((Owner) connectedUser).updatePlayerRole(teamName, mailId,role);
    }

    /**
     * owner:
     * update home field
     * @param teamName
     * @param makeHomeField
     * @throws NoEnoughMoney
     * @throws ObjectNotExist
     * @throws AccountNotExist
     * @throws IncorrectInputException
     * @throws DontHavePermissionException
     * @throws MemberNotExist
     * @throws AlreadyExistException
     */
    public void updateHomeField(String teamName,String makeHomeField) throws ObjectNotExist, DontHavePermissionException, AlreadyExistException {
        if (!dbController.getTeams(this.connectedUser).containsKey(teamName))
            throw new ObjectNotExist("");
        if(dbController.getTeams(this.connectedUser).get(teamName).getTrainingFields().contains(makeHomeField))
            throw new AlreadyExistException();
        if(dbController.getTeams(this.connectedUser).get(teamName).getHomeField().getNameOfField().equals(makeHomeField))
            throw new AlreadyExistException();
        ((Owner) connectedUser).updateHomeField(teamName, makeHomeField);
    }



    /********Getters for Owner & System manager********/
    public HashMap<String, Role> getRoles() throws DontHavePermissionException {
        if (connectedUser instanceof Owner) {
            return ((Owner) connectedUser).getRoles();
        }
        if (connectedUser instanceof SystemManager) {
            return ((SystemManager) connectedUser).getRoles();
        }
        throw new DontHavePermissionException();
    }

    public HashMap<String, Team> getTeams() {
        try {
            if (connectedUser instanceof Owner) {
                return ((Owner) connectedUser).getTeams();
            }
            if (connectedUser instanceof SystemManager) {
                return ((SystemManager) connectedUser).getTeams();
            }
            throw new DontHavePermissionException();
        }
        catch (Exception e)
        {

        }
        return new HashMap<>();
    }

    public void setMoneyToAccount(String teamName , double amount) throws ObjectNotExist, DontHavePermissionException {
        if(! (connectedUser instanceof Owner) )
            throw new DontHavePermissionException();
        ((Owner)connectedUser).setMoneyToAccount(teamName,amount);
    }

    public double getAccountBalance(String teamName) throws DontHavePermissionException, ObjectNotExist {
        if(! (connectedUser instanceof Owner) )
            throw new DontHavePermissionException();
        return ((Owner)connectedUser).getAccountBalance(teamName);
    }
    /*************************************** function for associationDelegate******************************************/

    /**
     * this
     *
     * @param
     * @param leagueName
     * @throws AlreadyExistException
     */
    public void setLeague(String leagueName) throws AlreadyExistException, IncorrectInputException, DontHavePermissionException {
        try {
            ((AssociationDelegate) connectedUser).setLeague(leagueName);
        } catch (IncorrectInputException incorrectInput) {
            throw new IncorrectInputException(leagueName);
        } catch (AlreadyExistException alreadyExist) {
            throw new AlreadyExistException();
        } catch (Exception e) {
            throw new DontHavePermissionException();
        }
    }

    public void setLeagueByYear(String specificLeague, String year) throws ObjectNotExist, AlreadyExistException, DontHavePermissionException {
        try {
            ((AssociationDelegate) connectedUser).setLeagueByYear(specificLeague, year);
        } catch (ObjectNotExist incorrectInput) {
            throw new ObjectNotExist(incorrectInput.getMessage());
        } catch (AlreadyExistException alreadyExist) {
            throw new AlreadyExistException();
        }
        catch (Exception e){
            throw new DontHavePermissionException();
        }
    }

    public HashMap<String, League> getLeagues() throws DontHavePermissionException {

        try{
            HashMap<String, League> leagues = dbController.getLeagues(this.connectedUser);
            return leagues;
        }
        catch(Exception e){
            throw new DontHavePermissionException();

        }
    }

    public League getLeague(String league) throws DontHavePermissionException, ObjectNotExist {
        return dbController.getLeague(this.connectedUser, league);
    }

    public HashMap<String, Referee> getRefereesDoesntExistInTheLeagueAndSeason(String league, String season) throws DontHavePermissionException, ObjectNotExist {
        HashMap<String, Referee> referees = new HashMap<>();
        try {
            referees = ((AssociationDelegate) connectedUser).getRefereesDoesntExistInTheLeagueAndSeason(league, season);

        }catch(ObjectNotExist objectNotExist){
            throw new ObjectNotExist("");
        } catch (Exception e) {
            throw new DontHavePermissionException();
        }
        return referees;
    }

    public void addRefereeToLeagueInSeason(String league, String season, String refereeToAdd) throws DontHavePermissionException, ObjectNotExist {
        try{
            ((AssociationDelegate)connectedUser).addRefereeToLeagueInSeason(league, season, refereeToAdd);
        }
        catch(ObjectNotExist objectNotExist){
            throw new ObjectNotExist("");
        }
        catch(Exception e){
            throw new DontHavePermissionException();
        }

    }
    public HashMap<String, Referee> getRefereesInLeagueInSeason(String league , String season) throws DontHavePermissionException, ObjectNotExist {
        if( this.connectedUser instanceof AssociationDelegate){
            return ((AssociationDelegate)connectedUser).getRefereesInLeagueInSeason(league,season);
        }
        throw new DontHavePermissionException();
    }

    public HashMap<String, Season> getSeasons() {
        HashMap<String, Season> seasons=new HashMap<String, Season>();
        try{
            seasons = dbController.getSeasons(this.connectedUser);
            throw new DontHavePermissionException();
        }catch(Exception e){

        }
        return seasons;
    }

    public void changeScorePolicy(String league, String season, String sWinning, String sDraw, String sLosing) throws ObjectNotExist, IncorrectInputException, DontHavePermissionException {
       try {
           ((AssociationDelegate) connectedUser).changeScorePolicy(league, season, sWinning, sDraw, sLosing);
       }

       catch(ObjectNotExist objectNotExist){
           throw new ObjectNotExist("");
       }
       catch(IncorrectInputException incorrectInput){
           throw new IncorrectInputException();
       }
       catch(Exception e){
           throw new DontHavePermissionException();
       }
    }

    public IScorePolicy getScorePolicy(String league, String season) throws DontHavePermissionException, ObjectNotExist {
        if(this.connectedUser instanceof AssociationDelegate)
            return ((AssociationDelegate)connectedUser).getScorePolicy(league, season);
        else
            throw new DontHavePermissionException();
    }

    public HashMap<String, ASchedulingPolicy> getSchedulingPolicies() throws DontHavePermissionException{
       return ((AssociationDelegate)connectedUser).getSchedulingPolicies();
    }

    public void addSchedulingPolicy(String policyName) throws IncorrectInputException, DontHavePermissionException {
        ((AssociationDelegate)connectedUser).addSchedulingPolicy(policyName);
    }
    public HashSet<Game> getGames(String league , String season) throws ObjectNotExist, DontHavePermissionException {
        if (connectedUser instanceof SystemManager) {
            return ((SystemManager) this.connectedUser).getGames(league, season);
        }
        else if( connectedUser instanceof Fan){
            //return ((Fan)this.connectedUser).getGames(league,season);
            return null;
        }
        else {
            throw new DontHavePermissionException();
        }
    }
    /*************************************** function for Referee ******************************************/

    /** secondary and main referee **/

    public void updateDetails(String newName, String newMail,String newPassword, String newTraining) throws IncorrectInputException, DontHavePermissionException, MemberNotExist, AlreadyExistException {
        if (connectedUser instanceof MainReferee) {
            MainReferee referee = (MainReferee) connectedUser;
            referee.updateDetails(newName, newMail, newPassword, newTraining);
        }
        else if (connectedUser instanceof SecondaryReferee) {
            SecondaryReferee referee = (SecondaryReferee) connectedUser;
            referee.updateDetails(newName, newMail, newPassword, newTraining);
        }
        else {
            throw new DontHavePermissionException();
        }
    }

    public HashSet<Game> getGameSchedule() throws DontHavePermissionException {
        if (connectedUser instanceof Referee) {
            Referee referee = (Referee) connectedUser;
            return referee.getGameSchedule();
        } else {
            throw new DontHavePermissionException();
        }
    }

    /** main referee only **/

    public LinkedList<Game> getEditableGames () throws DontHavePermissionException {
        if (connectedUser instanceof MainReferee) {
            MainReferee mainReferee = (MainReferee) connectedUser;
            return mainReferee.getEditableGames ();
        } else {
            throw new DontHavePermissionException();
        }
    }

    public void updateGameEvent(Game game, int timeInGame, EventInGame event, Date date, String description, ArrayList<Player> players){
        if (connectedUser instanceof MainReferee) {
            MainReferee mainReferee = (MainReferee) connectedUser;
            mainReferee.updateGameEvent(game, timeInGame, event, date, description, players);
        } else {

        }
    }

    public void getGameReport(){
        //todo
    }

    /*************************************** function for Fan ******************************************/

    public void updatePersonalDetails(String newName, String newPassword, String newMail) throws DontHavePermissionException, IncorrectInputException, MemberNotExist, AlreadyExistException {
        if (connectedUser instanceof Fan) {
            Fan fan = (Fan) connectedUser;
            fan.updatePersonalDetails(newName, newPassword, newMail);
        } else {
            throw new DontHavePermissionException();
        }
    }

    public void sendComplaint (String path, String complaint) throws DontHavePermissionException {
        if (connectedUser instanceof Fan) {
            Fan fan = (Fan) connectedUser;
            fan.sendComplaint(path, complaint);
        } else {
            throw new DontHavePermissionException();
        }
    }

    public Team getTeamByName (String teamName) throws DontHavePermissionException, ObjectNotExist {
            Team team = dbController.getTeam(connectedUser, teamName);
            return team;
    }

    public void addFollowerToTeam(Team team) throws DontHavePermissionException {
        if (connectedUser instanceof Fan) {
            Fan fan = (Fan) connectedUser;
            team.addNewFollower(fan);
        } else {
            throw new DontHavePermissionException();
        }
    }

    public void addFollowerToGame(Game game) throws DontHavePermissionException {
        if (connectedUser instanceof Fan) {
            Fan fan = (Fan) connectedUser;
            game.addFollower(fan);
        } else {
            throw new DontHavePermissionException();
        }
    }

    /*************************************************************************************************************/

    public HashMap<String, Fan> getFans(Role role) throws DontHavePermissionException {
        return dbController.getFans(role);
    }

    public HashMap<String, Referee> getReferees() {
        return dbController.getReferees();
    }

    public HashMap<String, Player> getPlayers(Role role) throws DontHavePermissionException {
        return dbController.getPlayers(role);
    }

    public HashMap<String, Owner> getOwners(Role role) throws DontHavePermissionException {
        return dbController.getOwners(role);
    }

    public HashMap<String, Manager> getManagers(Role role) throws DontHavePermissionException {
        return dbController.getManagers(role);
    }

    public HashMap<String, Coach> getCoach(Role role) throws DontHavePermissionException {
        return dbController.getCoaches(role);
    }

    public HashMap<String,Member> getMembers(Role role) throws DontHavePermissionException {
        return dbController.getMembers(role);
    }

    public void setSchedulingPolicyToLeagueInSeason(String specificLeague, String year, String policy) throws IncorrectInputException, ObjectNotExist, DontHavePermissionException {
        try{
            ((AssociationDelegate)connectedUser).setSchedulingPolicyToLeagueInSeason(specificLeague, year, policy);
        }
        catch(IncorrectInputException incorrectInput){
            throw new IncorrectInputException();
        }
        catch(ObjectNotExist objectNotExist){
            throw new ObjectNotExist("");
        }
        catch(Exception e){
            throw new DontHavePermissionException();
        }

    }
    public ASchedulingPolicy getSchedulingPolicyInLeagueInSeason(String league , String seson) throws DontHavePermissionException, ObjectNotExist {
        if(this.connectedUser instanceof AssociationDelegate){
            return ((AssociationDelegate)connectedUser).getSchedulingPolicyInLeagueInSeason(league,seson);
        }
        else {
            throw new DontHavePermissionException();
        }
    }

    public HashMap<String, SystemManager> getSystemManager() throws DontHavePermissionException {
        return dbController.getSystemManagers(this.connectedUser);
    }

    public HashMap<String, AssociationDelegate> getAssociationDelegates(Role role) throws DontHavePermissionException {
        return dbController.getAssociationDelegate(role);
    }


    public HashMap<String, Role> getOwnersAndFans(Role role) throws DontHavePermissionException {
        return dbController.getOwnersAndFans(role);
    }

    /**
     * Associate Dellegite
     * this function add team to league in season
     * @param league
     * @param season
     * @param teamName
     * @throws DontHavePermissionException
     * @throws ObjectNotExist
     * @throws AlreadyExistException
     */
    public void addTeamToLeagueInSeason(String league,String season,String teamName) throws DontHavePermissionException, ObjectNotExist, AlreadyExistException, IncorrectInputException {
        if(this.connectedUser instanceof AssociationDelegate){
            ((AssociationDelegate)connectedUser).addTeamToLeagueInSeason(league,season,teamName);
        }
        else {
            throw new DontHavePermissionException();
        }
    }



    /**********shachar test*************/
    /*
    public void addPlayer(Player player1) throws AlreadyExistException {
        dbController.addPlayer(player1);
    }

    public void addCoach(Coach coach1) throws AlreadyExistException {
        dbController.addCoach(coach1);
    }

    public void addManager(Manager manager1) throws AlreadyExistException {
        dbController.addManager(manager1);
    }

    public void addOwner(Owner owner1) throws AlreadyExistException, DontHavePermissionException {
        dbController.addOwner(connectedUser,owner1);
    }

    public void addSystemManager(SystemManager systemManager) throws AlreadyExistException {
        dbController.addSystemManager(systemManager);
    }

    public void addFan(Fan fan1) {
        dbController.addFan(fan1);
    }
    */

}