
import Asset.*;
import Exception.*;
import Game.Account;
import Game.Team;
import Game.Transaction;
import League.League;
import Users.*;
import javafx.util.Pair;
import system.DBController;
import system.SystemController;

import javax.sound.midi.Soundbank;
import java.util.*;
import java.util.regex.Pattern;

public class Main {
    static Scanner scanInput = new Scanner(System.in);
    static SystemController controller = new SystemController("System Controller");
    static Member member;
    static String path;

    public static void main(String[] args) {
/****************************************************menu******************************************************/
        path = "";
        //SystemController controller =

//        /**********for my test - shacahr********/
//        Main main = new Main();
//        main.shacharFunctionForTesting();
//        /***************************************/
//
//        startMenu();


        /**** Hilla Peter Tests!!****/
      //  Main main = new Main();
       // main.hillaPeterFunctionForTesting();
        startMenu();
    }


    /***********************************************private function**************************************************/
    private static void startMenu() {
        String input = "";
        while (!input.equals("Exit")) {
            System.out.println("choose one of the following options:\n");
            System.out.println("write \"1\" for signIn.");
            System.out.println("write \"2\" for logIn.");
            System.out.println("\nwrite \"Exit\" if you want to finish. \n");
            input = "";
            while (input.equals("")) {
                input = scanInput.nextLine();
            }
            switch (input) {

                case "1": {
                    try {
                        String[] details = fillFormSignIn();
                        try {
                            member = controller.signIn(details[1], details[0], details[2]);
                            System.out.println("succeed to signIn!!");
                            showMenu(member);
                        } catch (AlreadyExistException e) {
                            System.out.println("this mail is already exist in the system.\ntry again with a different mai.");
                        }

                    } catch (IncorrectPasswordInputException e) {
                        System.out.println("you entered wrong password - please enter password that contains only numbers and letters.");
                    } catch (IncorrectInputException e) {
                        System.out.println("you entered invalid mail.");
                    }
                }
                break;

                case "2": {
                    String[] details = fillFormLogIn();
                    try {
                        member = controller.logIn(details[0], details[1]);
                        showMenu(member);
                    } catch (MemberNotExist e) {
                        System.out.println("This member mail is doesnt exist in the system.\nlog in with different mail.");
                    } catch (PasswordDontMatchException e) {
                        System.out.println("You entered incorrect password.\nlog in with the correct password.");
                    }
                }
                break;
                case "Exit": {
                }
            }
        }
    }

    private static void showMenu(Role member) {
        //just for testing
        //    member = new SystemManager("shachar", "shachar@gmail.com", "shachar", controller);


        // member = new Owner("hilla", "hilla@gmail.com", "h1", controller);


        if (member instanceof SystemManager) {
            SystemManagerMenu((Member) member);
        } else if (member instanceof MainReferee) {

        } else if (member instanceof SecondaryReferee) {

        } else if (member instanceof Fan) {

        } else if (member instanceof Owner) {
            OwnerMenu();
        } else if (member instanceof AssociationDelegate) {
            AssociationDelegateMenu((Member) member);
        }
    }

    /*********************************************System Manager**********************************************/
    private static void SystemManagerMenu(Member member) {
        String input = "";
        while (!input.equals("Exit")) {
            System.out.println("choose one of the following options:\n");
            System.out.println("write \"1\" for handle with refree");
            System.out.println("write \"2\" for handle with team");
            System.out.println("write \"3\" for remove member");
            System.out.println("write \"4\" for handle with complaints");
            System.out.println("write \"5\" for handle with games");
            System.out.println("write \"6\" for view System Information");
            System.out.println("\nwrite \"Exit\" if you want to finish. \n");
            input = "";
            while (input.equals("")) {
                input = scanInput.nextLine();
            }
            switch (input) {

                case "1": {

                    while (!input.equals("Exit")) {
                        System.out.println("choose one of the following options:\n");
                        System.out.println("write \"1\" to remove referee");
                        System.out.println("write \"2\" to add referee");
                        System.out.println("write \"3\" to update referee details");
                        System.out.println("\nwrite \"Exit\" if you want to finish. \n");
                        input = "";
                        while (input.equals("")) {
                            input = scanInput.nextLine();
                        }
                        switch (input) {
                            case "1": {
                                System.out.println("please enter the Id of the referee");
                                String id = scanInput.nextLine();
                                try {
                                    boolean success = controller.removeReferee(member.getUserMail(), id);
                                } catch (MemberNotSystemManager e) {
                                    System.out.println("you don't have the permission to remove referee");
                                }
                            }

                            case "2": {
                                System.out.println("please enter the Id of the refree");
                                String id = scanInput.nextLine();
                                System.out.println("would you like this refree will be main refree ? yes/no");
                                String bool = scanInput.nextLine();
                                boolean mainReferee;
                                if (bool.equals("yes"))
                                    mainReferee = true;
                                else {
                                    mainReferee = false;
                                }
                                try {
                                    boolean success = controller.addReferee(member.getUserMail(), id, mainReferee);
                                } catch (MemberNotSystemManager e) {
                                    System.out.println("you don't have the permission to remove referee");
                                }
                            }
                            case "3": {
                                //  controller.updateReferee();
                            }
                        }
                    }
                }
                case "2": {
                    while (!input.equals("Exit")) {
                        System.out.println("choose one of the following options:\n");
                        System.out.println("write \"1\" to add new team");
                        System.out.println("write \"2\" to remove exist team");
                        System.out.println("\nwrite \"Exit\" if you want to finish. \n");
                        input = "";
                        while (input.equals("")) {
                            input = scanInput.nextLine();
                        }
                        switch (input) {
                            case "1": {
                                LinkedList<String> players = new LinkedList<>();
                                LinkedList<String> coaches = new LinkedList<>();
                                LinkedList<String> managers = new LinkedList<>();
                                LinkedList<String> owners = new LinkedList<>();
                                String teamName = "";
                                System.out.println("please enter the name of the new team");
                                teamName = scanInput.nextLine();
                                String id = "";
                                while (!id.equals("0")) {
                                    System.out.println("please enter the id of the players in the team \n when you finish press 0");
                                    id = scanInput.nextLine();
                                    if (!id.equals(0))
                                        players.add(id);
                                }
                                while (!id.equals("0")) {
                                    System.out.println("please enter the id of the coaches in the team \n when you finish press 0");
                                    id = scanInput.nextLine();
                                    if (!id.equals(0))
                                        coaches.add(id);
                                }
                                while (!id.equals("0")) {
                                    System.out.println("please enter the id of the managers in the team \n when you finish press 0");
                                    id = scanInput.nextLine();
                                    if (!id.equals(0))
                                        managers.add(id);
                                }
                                while (!id.equals("0")) {
                                    System.out.println("please enter the id of the owners in the team \n when you finish press 0");
                                    id = scanInput.nextLine();
                                    if (!id.equals(0))
                                        owners.add(id);
                                }
                                try {
                                    boolean success = controller.addTeam(member.getUserMail(), players, coaches, managers, owners, teamName);
                                } catch (MemberNotSystemManager e) {
                                    System.out.println("you don't have the permission to remove referee");
                                }
                            }
                            case "2": {
                                System.out.println("please enter the team name you want to close");
                                String TeamName = scanInput.nextLine();
                                try {
                                    boolean success = controller.closeTeam(member.getUserMail(), TeamName);
                                } catch (MemberNotSystemManager e) {
                                    System.out.println("you don't have the permission to remove referee");
                                }
                            }
                        }
                    }
                }
                case "3": {
                    System.out.println("please enter the Id of the member you want to remove");
                    String id = scanInput.nextLine();
                    try {
                        boolean success = controller.removeMember(member.getUserMail(), id);
                    } catch (MemberNotSystemManager e) {
                        System.out.println("you don't have the permission to remove referee");
                    }
                }
                case "4": {

                    while (!input.equals("Exit")) {
                        System.out.println("choose one of the following options:\n");
                        System.out.println("write \"1\" to watch the complaints");
                        System.out.println("write \"2\" to response on the complaints");
                        System.out.println("\nwrite \"Exit\" if you want to finish. \n");
                        input = "";
                        while (input.equals("")) {
                            input = scanInput.nextLine();
                        }
                        switch (input) {
                            case "1": {
                                try {
                                    controller.watchComplaint(member.getUserMail(), path);
                                } catch (MemberNotSystemManager e) {
                                    System.out.println("you don't have the permission to remove referee");
                                }
                            }
                            case "2": {
                                try {
                                    LinkedList<Pair<String, String>> responseForComplaint = new LinkedList<>();
                                    boolean success = controller.responseComplaint(member.getUserMail(), path, responseForComplaint);
                                } catch (MemberNotSystemManager e) {
                                    System.out.println("you don't have the permission to remove referee");
                                }
                            }
                        }
                    }
                }
                case "5": {
                    try {
                        controller.schedulingGames(member.getUserMail());
                    } catch (MemberNotSystemManager e) {
                        System.out.println("you don't have the permission to remove referee");
                    }
                }
                case "6": {
                    try {
                        controller.viewSystemInformation(member.getUserMail());
                    } catch (MemberNotSystemManager e) {
                        System.out.println("you don't have the permission to remove referee");
                    }
                }
            }


        }
    }


    /*********************************************Owner**********************************************/
    private static void OwnerMenu() {
        try {
            Team team;
            String input = "";
            while (!input.equals("Exit")) {
                System.out.println("choose one of the following options:\n");
                System.out.println("write \"1\" Add Asset");
                System.out.println("write \"2\" Update Asset");
                System.out.println("write \"3\" Remove Asset");
                System.out.println("write \"4\" Add New Manager");
                System.out.println("write \"5\" Add New Owner");
                System.out.println("write \"6\" Remove Manager");
                System.out.println("write \"7\" Temporary Team Closing");
                System.out.println("write \"8\" Reopen Closed Team");
                System.out.println("write \"9\" Add Outcome");
                System.out.println("write \"10\" Add Income");
                System.out.println("\nwrite \"Exit\" if you want to finish. \n");
                input = "";
                while (input.equals("")) {
                    input = scanInput.nextLine();
                }
                switch (input) {
                    case "1": {
                        System.out.println("What asset do you want to add? choose by index");
                        System.out.println("Press \"1.\" for Team manager");
                        System.out.println("Press \"2.\" for Coach");
                        System.out.println("Press \"3.\" for Player");
                        System.out.println("Press \"4.\" for Field");
                        System.out.println("\nwrite \"Exit\" if you want to finish. \n");
                        input = scanInput.nextLine();
                        while (!input.equals("Exit")) {
                            switch (input) {
                                case "1": {
                                    addTeamManager();
                                }
                                case "2": {
                                    addTeamCoach();
                                }
                                case "3": {
                                    addTeamPlayer();
                                }
                                case "4": {
                                    addTeamField();
                                }
                                case "Exit": {
                                }
                            }
                        }
                    }//case 1- add asset
                    case "2": {
                        System.out.println("not implement!!!!!!!!!!!!!"); //todo!!!!!
                        break;
                    }
                    case "3": {
                        System.out.println("What asset do you want to remove? choose by index");
                        System.out.println("Press \"1.\" for Team manager");
                        System.out.println("Press \"2.\" for Coach");
                        System.out.println("Press \"3.\" for Player");
                        System.out.println("Press \"4.\" for Field");
                        System.out.println("\nwrite \"Exit\" if you want to finish. \n");
                        input = scanInput.nextLine();
                        while (!input.equals("Exit")) {
                            switch (input) {
                                case "1": {
                                    removeTeamManager();
                                }
                                case "2": {
                                    removeTeamCoach();
                                }
                                case "3": {
                                    removeTeamPlayer();
                                }
                                case "4": {
                                    removeTeamField();
                                }
                                case "Exit": {
                                }
                            }
                        }
                    }//removeAsset
                    case "4": {
                        addTeamManager();
                        break;
                    }
                    case "5": {
                        System.out.println("Choose role by mail to make him owner");
                        HashMap<String, Role> allRoles = controller.getRoles();
                        //moving on all the roles in system
                        for (String mailId : allRoles.keySet()) {
                            Role role = allRoles.get(mailId);
                            if (!(role instanceof Owner)) {
                                System.out.println(mailId);
                            }
                        }
                        String mailId = scanInput.nextLine();
                        Role role = allRoles.get(mailId);
                        HashMap<String, Team> teams = controller.getTeams();
                        System.out.println("Choose team name to add new owner");
                        for (String teamName : teams.keySet()) {
                            System.out.println(teamName);
                        }

                        String teamName = scanInput.nextLine();
                        try {
                            controller.addNewOwner(teamName, mailId);
                        } catch (TeamNotExist exception) {

                        } catch (OwnerNotExist ownerNotExist) {
                            ownerNotExist.printStackTrace();
                        }

                        break;

                    }
                    case "6": {
                        removeTeamManager();
                        break;
                    }
                    case "7": {
                        HashMap<String, Team> teams = controller.getTeams();
                        System.out.println("Choose a team you want to close temporary");
                        for (String teamName : teams.keySet()) {
                            team = teams.get(teamName);
                            //open
                            if (team.getStatus()) {
                                System.out.println(teamName);
                            }
                        }
                        String teamName = scanInput.next();
                        controller.temporaryTeamClosing(teamName);
                        break;
                    }
                    case "8": {
                        HashMap<String, Team> teams = controller.getTeams();
                        System.out.println("Choose a team you want to reopen temporary");
                        for (String teamName : teams.keySet()) {
                            team = teams.get(teamName);
                            //closed
                            if (!team.getStatus()) {
                                System.out.println(teamName);
                            }
                        }
                        String teamName = scanInput.next();
                        controller.reopenClosedTeam(teamName);
                        break;
                    }
                    case "9": {
                        HashMap<String, Team> teams = controller.getTeams();
                        System.out.println("Choose team name to update outcome");
                        for (String teamName : teams.keySet()) {
                            System.out.println(teamName);
                        }
                        String teamName = scanInput.nextLine();
                        System.out.println("What outcome do you want to add?");
                        String description = scanInput.nextLine();
                        System.out.println("What is the amount?");
                        double amount = scanInput.nextDouble();
                        controller.addOutCome(teamName, description, amount);
                        break;
                    }
                    case "10": {
                        HashMap<String, Team> teams = controller.getTeams();
                        System.out.println("Choose team name to update income");
                        for (String teamName : teams.keySet()) {
                            System.out.println(teamName);
                        }
                        String teamName = scanInput.nextLine();
                        System.out.println("What income do you want to add?");
                        String description = scanInput.nextLine();
                        System.out.println("What is the amount?");
                        double amount = scanInput.nextDouble();
                        controller.addInCome(teamName, description, amount);
                        break;
                    }
                    case "Exit": {
                        System.out.println("End of program");
                    }
                }
            }
        } catch (Exception ownerNotExist) {
        }

    }

    /**
     * Private functions for Owner main-don't delete!
     **/
    private static void addTeamManager() {
        System.out.println("Choose role by mail to make him manager");
        HashMap<String, Role> allRoles = controller.getRoles();
        //moving on all the roles in system
        for (String mailId : allRoles.keySet()) {
            Role role = allRoles.get(mailId);
            if (!(role instanceof Manager) && !(role instanceof Owner)) {
                System.out.println(mailId);
            }
        }
        String mailId = scanInput.nextLine();
        Role role = allRoles.get(mailId);
        HashMap<String, Team> teams = controller.getTeams();
        System.out.println("Choose team name to add new manager");
        for (String teamName : teams.keySet()) {
            System.out.println(teamName);
        }

        String teamName = scanInput.nextLine();
        try {
            controller.addManager(teamName, mailId);
        } catch (Exception ownerNotExist) {
        }

    }

    private static void addTeamCoach() {
        System.out.println("Choose role by mail to make him coach");
        HashMap<String, Role> allRoles = controller.getRoles();
        //moving on all the roles in system
        for (String mailId : allRoles.keySet()) {
            Role role = allRoles.get(mailId);
            if (!(role instanceof Manager) && !(role instanceof Owner)) {
                System.out.println(mailId);
            }
        }
        String mailId = scanInput.nextLine();
        Role role = allRoles.get(mailId);
        HashMap<String, Team> teams = controller.getTeams();
        System.out.println("Choose team name to add new coach");
        for (String teamName : teams.keySet()) {
            System.out.println(teamName);
        }

        String teamName = scanInput.nextLine();
        try {
            controller.addCoach(teamName, mailId);
        } catch (Exception ownerNotExist) {
        }

    }

    private static void addTeamPlayer() throws IncorrectInputException, TeamNotExist, ManagerNotExist, MemberNotExist, AlreadyExistException {
        System.out.println("Choose role by mail to make him coach");
        HashMap<String, Role> allRoles = controller.getRoles();
        //moving on all the roles in system
        for (String mailId : allRoles.keySet()) {
            Role role = allRoles.get(mailId);
            if (!(role instanceof Manager) && !(role instanceof Owner)) {
                System.out.println(mailId);
            }
        }
        String mailId = scanInput.nextLine();
        Role role = allRoles.get(mailId);

        HashMap<String, Team> teams = controller.getTeams();
        System.out.println("Choose team name to add new player");
        for (String teamName : teams.keySet()) {
            System.out.println(teamName);
        }
        String teamName = scanInput.nextLine();
        System.out.println("Enter year of birth of player");
        int year = scanInput.nextInt();
        System.out.println("Enter month of birth of player");
        int month = scanInput.nextInt();
        System.out.println("Enter day of birth of player");
        int day = scanInput.nextInt();
        System.out.println("Choose role for the player");
        String rolePlayer = scanInput.nextLine();

        controller.addPlayer(mailId, teamName, year, month, day, rolePlayer);

    }

    private static void addTeamField() throws IncorrectInputException, TeamNotExist, ManagerNotExist {
        HashMap<String, Team> teams = controller.getTeams();
        System.out.println("Choose team name to add new field");
        for (String teamName : teams.keySet()) {
            System.out.println(teamName);
        }
        String teamName = scanInput.nextLine();
        System.out.println("Enter Field name");
        String fieldName = scanInput.nextLine();

        controller.addField(teamName, fieldName);

    }

    private static void removeTeamManager() {
        HashMap<String, Team> teams = controller.getTeams();
        System.out.println("Choose team name to remove manager");
        for (String teamName : teams.keySet()) {
            System.out.println(teamName);
        }

        String teamName = scanInput.nextLine();
        Team team = teams.get(teamName);

        System.out.println("Choose role to delete him from being manager");
        HashSet<Manager> managers = team.getManagers();
        for (Manager m : managers) {
            System.out.println(m.getUserMail());
        }
        String mailToRemove = scanInput.nextLine();

        try {
            controller.removeManager(teamName, mailToRemove);
        } catch (Exception ownerNotExist) {
        }
    }

    private static void removeTeamCoach() {
        HashMap<String, Team> teams = controller.getTeams();
        System.out.println("Choose team name to remove coach");
        for (String teamName : teams.keySet()) {
            System.out.println(teamName);
        }

        String teamName = scanInput.nextLine();
        Team team = teams.get(teamName);

        System.out.println("Choose role to delete him from being coach");
        HashSet<Coach> coaches = team.getCoaches();
        for (Coach c : coaches) {
            System.out.println(c.getUserMail());
        }
        String mailToRemove = scanInput.nextLine();

        try {
            controller.removeCoach(teamName, mailToRemove);
        } catch (Exception ownerNotExist) {
        }
    }

    private static void removeTeamPlayer() {
        HashMap<String, Team> teams = controller.getTeams();
        System.out.println("Choose team name to remove coach");
        for (String teamName : teams.keySet()) {
            System.out.println(teamName);
        }

        String teamName = scanInput.nextLine();
        Team team = teams.get(teamName);

        System.out.println("Choose role to delete him from being coach");
        HashSet<Player> players = team.getPlayers();
        for (Player p : players) {
            System.out.println(p.getUserMail());
        }
        String mailToRemove = scanInput.nextLine();

        try {
            controller.removePlayer(teamName, mailToRemove);
        } catch (Exception ownerNotExist) {
        }
    }

    private static void removeTeamField() throws IncorrectInputException, TeamNotExist, OwnerNotExist {
        HashMap<String, Team> teams = controller.getTeams();
        System.out.println("Choose team name to add remove field");
        for (String teamName : teams.keySet()) {
            System.out.println(teamName);
        }
        String teamName = scanInput.nextLine();
        System.out.println("Enter Field name");
        String fieldName = scanInput.nextLine();

        controller.removeField(teamName, fieldName);
    }


    /***************************************************tests********************************************************/

//    private static void shacharFunctionForTesting() throws MemberNotSystemManager {
//
//        Fan fan1 = new Fan("adi", "adi@gmail.com", "adi");
//        Fan fan2 = new Fan("alisa", "alisa@gmail.com", "alisa");
//        Player player1 = new Player("yaara", "yaara@gmail.com", "yaara", new Date(1995, 1, 1), "player");
//        Player player2 = new Player("daniel", "daniel@gmail.com", "daniel", new Date(1995, 1, 1), "player");
//        Player player3 = new Player("hilla", "hilla@gmail.com", "hilla", new Date(1995, 1, 1), "player");
//        Player player4 = new Player("noa", "noa@gmail.com", "noa", new Date(1995, 1, 1), "player");
//        Player player5 = new Player("liat", "liat@gmail.com", "liat", new Date(1995, 1, 1), "player");
//        Player player6 = new Player("neta", "neta@gmail.com", "neta", new Date(1995, 1, 1), "player");
//        Player player7 = new Player("ziv", "ziv@gmail.com", "ziv", new Date(1995, 1, 1), "player");
//        Player player8 = new Player("neta", "neta@gmail.com", "neta", new Date(1995, 1, 1), "player");
//        Player player9 = new Player("or", "or@gmail.com", "or", new Date(1995, 1, 1), "player");
//        Player player10 = new Player("shoval", "shoval@gmail.com", "shoval", new Date(1995, 1, 1), "player");
//        Player player11 = new Player("gal", "gal@gmail.com", "gal", new Date(1995, 1, 1), "player");
//        Player player12 = new Player("michelle", "michelle@gmail.com", "michelle", new Date(1995, 1, 1), "player");
//        Player player13 = new Player("gabi", "gabi@gmail.com", "gabi", new Date(1995, 1, 1), "player");
//        Player player14 = new Player("almog", "almog@gmail.com", "almog", new Date(1995, 1, 1), "player");
//        Player player15 = new Player("shani", "shani@gmail.com", "shani", new Date(1995, 1, 1), "player");
//        Player player16 = new Player("ifat", "ifat@gmail.com", "ifat", new Date(1995, 1, 1), "player");
//        Player player17 = new Player("inbal", "inbal@gmail.com", "dor", new Date(1995, 1, 1), "player");
//        Player player18 = new Player("oscar", "oscar@gmail.com", "oscar", new Date(1995, 1, 1), "player");
//        Player player19 = new Player("roman", "roman@gmail.com", "roman", new Date(1995, 1, 1), "player");
//        Player player20 = new Player("omer", "omer@gmail.com", "omer", new Date(1995, 1, 1), "player");
//        Player player21 = new Player("asi", "asi@gmail.com", "asi", new Date(1995, 1, 1), "player");
//        Player player22 = new Player("peleg", "peleg@gmail.com", "peleg", new Date(1995, 1, 1), "player");
//
//        controller.addPlayer(player1);
//        controller.addPlayer(player2);
//        controller.addPlayer(player3);
//        controller.addPlayer(player4);
//        controller.addPlayer(player5);
//        controller.addPlayer(player6);
//        controller.addPlayer(player7);
//        controller.addPlayer(player8);
//        controller.addPlayer(player9);
//        controller.addPlayer(player10);
//        controller.addPlayer(player11);
//        controller.addPlayer(player12);
//        controller.addPlayer(player13);
//        controller.addPlayer(player14);
//        controller.addPlayer(player15);
//        controller.addPlayer(player16);
//        controller.addPlayer(player17);
//        controller.addPlayer(player18);
//        controller.addPlayer(player19);
//        controller.addPlayer(player20);
//        controller.addPlayer(player21);
//        controller.addPlayer(player22);
//
//
//        Coach coach1 = new Coach("yosi oren", "yosi@gmaill.com", "123", "Coach");
//        Coach coach2 = new Coach("arnold strum", "arnold@gmaill.com", "124", "Coach");
//        controller.addCoach(coach1);
//        controller.addCoach(coach2);
//        Manager manager1 = new Manager("oren tzur", "oren@gmaill.com", "125");
//        Manager manager2 = new Manager("guy shani", "guy@gmaill.com", "126");
//        controller.addManager(manager1);
//        controller.addManager(manager2);
//        Owner owner1 = new Owner("ariel pelner", "ariel@gmail.com", "127", controller);
//        Owner owner2 = new Owner("dor atzmon", "dor@gmail.com", "128", controller);
//        controller.addOwner(owner1);
//        controller.addOwner(owner2);
//        SystemManager systemManager = new SystemManager("shachar meretz ", "shachar@gmail.com", "shachar", controller);
//        controller.addSystemManager(systemManager);
//        controller.addFan(fan1);
//        //  controller.addReferee(systemManager.getUserMail(), fan1.getUserMail(), true);
//        //   controller.removeReferee(systemManager.getUserMail(), fan1.getUserMail());
//        // controller.removeMember(systemManager.getUserMail(), coach1.getUserMail());
//        //  controller.removeMember(systemManager.getUserMail(), manager1.getUserMail());
//        //   controller.removeMember(systemManager.getUserMail(), owner1.getUserMail());
//        //   controller.removeMember(systemManager.getUserMail(), player1.getUserMail());
//        //   controller.removeMember(systemManager.getUserMail(), fan2.getUserMail());
//
//        String name = "macabi";
//        LinkedList<String> players1 = new LinkedList<>();
//        players1.add(player1.getUserMail());
//        players1.add(player2.getUserMail());
//        players1.add(player3.getUserMail());
//        players1.add(player4.getUserMail());
//        players1.add(player5.getUserMail());
//        players1.add(player6.getUserMail());
//        players1.add(player7.getUserMail());
//        players1.add(player8.getUserMail());
//        players1.add(player9.getUserMail());
//        players1.add(player10.getUserMail());
//        players1.add(player11.getUserMail());
//
//        LinkedList<String> coaches1 = new LinkedList<>();
//        coaches1.add(coach1.getUserMail());
//
//        LinkedList<String> managers1 = new LinkedList<>();
//        managers1.add(manager1.getUserMail());
//
//        LinkedList<String> owners1 = new LinkedList<>();
//        owners1.add(owner1.getUserMail());
//
//        controller.addTeam(systemManager.getUserMail(), players1, coaches1, managers1, owners1, name);
//        systemManager.watchComplaint("C:\\Users\\shachar meretz\\Desktop\\semesterB\\arnold\\projectGit\\PreparationForFinalProject\\complaint.txt");
//        int x = 0;
//
//
//    }

  /*      SecurityMachine securityMachine = new SecurityMachine();
        String afterEncrtypt = securityMachine.encrypt("stamLibdok", "key");
        String afterDycrypt = securityMachine.decrypt(afterEncrtypt, "key");
        System.out.println(afterEncrtypt);
        System.out.println(afterDycrypt);


        SystemController SystemController = new SystemController("shachar");

        LinkedList<Player> players1 = new LinkedList<>();
        LinkedList<Player> players2 = new LinkedList<>();
        LinkedList<Integer> onlyTheIdPlayers1 = new LinkedList<>();
        LinkedList<Integer> onlyTheIdPlayers2 = new LinkedList<>();

        for (int i = 0; i < 11; i++) {
            players1.add(new Player("jordi" + i, "1", "" + i + i + i, new Date(i, i, i + i), "Player"));
            players2.add(new Player("static" + i, "2", "" + i + i + i, new Date(i, i, i + i), "Player"));
            onlyTheIdPlayers1.add(i);
            onlyTheIdPlayers2.add(i);
        }
        for (int i = 0; i < 11; i++) {
            SystemController.addPlayer(players1.get(i));
            SystemController.addPlayer(players2.get(i));
        }
        LinkedList<Integer> onlyTheIdCoach1 = new LinkedList<>();
        LinkedList<Integer> onlyTheIdManager21 = new LinkedList<>();
        LinkedList<Integer> onlyTheIdOwner1 = new LinkedList<>();


        Coach coach1 = new Coach("romi", "romi@gmaill.com", "123", "Coach");
        Coach coach2 = new Coach("yosi", "yosi@gmaill.com", "124", "Coach");
        SystemController.addCoach(coach1);
        onlyTheIdCoach1.add(123);
        SystemController.addCoach(coach1);
        Manager manager1 = new Manager("shlomi", "shlomi@gmaill.com", "125");
        Manager manager2 = new Manager("lior", "lior@gmaill.com", "126");
        SystemController.addManager(manager1);
        onlyTheIdManager21.add(125);
        SystemController.addManager(manager2);
        Owner owner1 = new Owner("daniel", "daniel@gmail.com", "127");
        Owner owner2 = new Owner("ben-el", "ben-el@gmail.com", "128");
        SystemController.addOwner(owner1);
        onlyTheIdOwner1.add(127);
        SystemController.addOwner(owner2);
        SystemManager systemManager = new SystemManager("shachar", "shachar@gmail.com", "208240275", SystemController);
        SystemController.addSystemManager(systemManager);
        systemManager.addNewTeam(onlyTheIdPlayers1, onlyTheIdCoach1, onlyTheIdManager21, onlyTheIdOwner1, "macabi");
        System.out.println("done");
        int x = 0;
        x++;*/

    // system.addCoach();
    //system.addManager();
    //system.addOwner();
    //system.addReferee();
    //system
    //system.addTeam();


    /***************************************************testsHillaPeter********************************************************/

    /**
     * checking owner- please don't delete it! (Hilla P)
     */

    private static void hillaPeterFunctionForTesting() {

        DBController dbController = new DBController();

        Owner ownerHilla = new Owner("hilla", "hilla@gmail.com", "h1", dbController);
        Owner ownerLiat = new Owner("liat", "liat@gmail.com", "l1", dbController);
        Manager manager1 = new Manager("oren tzur", "oren@gmail.com", "125");
        Manager manager2 = new Manager("guy shani", "guy@gmail.com", "126");


        Fan fan1 = new Fan("adi", "adi@gmail.com", "adi");
        Fan fan2 = new Fan("alisa", "alisa@gmail.com", "alisa");
        Player player1 = new Player("yaara", "yaara@gmail.com", "yaara", new Date(1995, 1, 1), "player");
        Player player2 = new Player("daniel", "daniel@gmail.com", "daniel", new Date(1995, 1, 1), "player");
        Player player3 = new Player("may", "may@gmail.com", "may", new Date(1995, 1, 1), "player");
        Player player4 = new Player("noa", "noa@gmail.com", "noa", new Date(1995, 1, 1), "player");
        Player player5 = new Player("inbar", "inbar@gmail.com", "inbar", new Date(1995, 1, 1), "player");
        Player player6 = new Player("neta", "neta@gmail.com", "neta", new Date(1995, 1, 1), "player");
        Player player7 = new Player("ziv", "ziv@gmail.com", "ziv", new Date(1995, 1, 1), "player");
        Player player8 = new Player("neta", "neta@gmail.com", "neta", new Date(1995, 1, 1), "player");
        Player player9 = new Player("or", "or@gmail.com", "or", new Date(1995, 1, 1), "player");
        Player player10 = new Player("shoval", "shoval@gmail.com", "shoval", new Date(1995, 1, 1), "player");
        Player player11 = new Player("gal", "gal@gmail.com", "gal", new Date(1995, 1, 1), "player");

        try {
            dbController.addPlayer(player1);
            dbController.addPlayer(player2);
            dbController.addPlayer(player3);
            dbController.addPlayer(player4);
            dbController.addPlayer(player5);
            dbController.addPlayer(player6);
            dbController.addPlayer(player7);
            dbController.addPlayer(player8);
            dbController.addPlayer(player9);
            dbController.addPlayer(player10);
            dbController.addPlayer(player11);
        } catch (Exception e) {

        }
        HashSet<Player> players = new HashSet<>();
        players.add(player1);
        players.add(player2);
        players.add(player3);
        players.add(player4);
        players.add(player5);
        players.add(player6);
        players.add(player7);
        players.add(player8);
        players.add(player9);
        players.add(player10);
        players.add(player11);


//        controller.addFan(fan1);
//        controller.addFan(fan2);

        Transaction transaction = new Transaction("Transaction", 12);
        Transaction transaction1 = new Transaction("Transaction1", 43);
        Transaction transaction2 = new Transaction("Transaction2", 445453);
        ArrayList<Transaction> listTransactions = new ArrayList<>();
        listTransactions.add(transaction);
        listTransactions.add(transaction1);
        listTransactions.add(transaction2);

        Account account0 = new Account("Hapoel", listTransactions, 123123);
        Field field0 = new Field("sami");
        Team team0 = new Team("hapoel", account0, field0);
        field0.setTeam(team0);
        HashSet<Owner> ownersTeam0 = team0.getOwners();
        ownersTeam0.add(ownerHilla);

        Account account1 = new Account("Maccabi", listTransactions, 12335435);
        //  Field field1= new Field("Sami offer");
        Team team1 = new Team("maccabi", account1, null);
        // field1.setTeam(team1);
        HashSet<Owner> ownersTeam1 = team1.getOwners();
        ownersTeam1.add(ownerHilla);

        HashMap<String, Team> hashMapTeams = new HashMap<>();
        hashMapTeams.put("Maccabi", team0);
        hashMapTeams.put("Hapoel", team1);

        //    ownerHilla.setTeams(hashMapTeams);


        team0.setPlayers(players);


        HashSet<Owner> owners = new HashSet<>();
        owners.add(ownerHilla);
        owners.add(ownerLiat);

        team0.setOwners(owners);

        //managers
        HashSet<Manager> managers = new HashSet<>();
        managers.add(manager1);
        managers.add(manager2);
        team0.setManagers(managers);
//
//        controller.addTeam(team0);
//        controller.addTeam(team1);
    }


    /******************************* public function for guest menu (noa) **********************************/

    /**
     * This function fill the signIn-form with user mail, user name and user password
     *
     * @return String array - details[mail,name,password]
     */

    public static String[] fillFormSignIn() throws IncorrectPasswordInputException, IncorrectInputException {
        String[] details = new String[3];
        Scanner scanInput = new Scanner(System.in);
        System.out.println("please enter your mail");
        String mailInput = scanInput.nextLine();
        if (!checkMailInput(mailInput)) {
            throw new IncorrectInputException("incorrect mail input");
        }
        System.out.println("please enter full name");
        String nameInput = scanInput.nextLine();
        System.out.println("please enter password - contains only numbers and letters");
        String password = scanInput.nextLine();
        if (!checkPasswordValue(password)) {
            throw new IncorrectPasswordInputException();
        }
        System.out.println("please verify your password");
        String password2 = scanInput.nextLine();
        while (password.compareTo(password2) != 0) {
            System.out.println("you entered two different password");
            System.out.println("please enter password");
            password = scanInput.nextLine();
            System.out.println("please verify your password");
            password2 = scanInput.nextLine();
        }

        System.out.println("your details entered successfully!\nplease wait for confirmation");
        details[0] = mailInput;
        details[1] = nameInput;
        details[2] = password;

        return details;
    }

    /**
     * this function fill the logIn-form : user name , user password
     *
     * @return String array  - details[mail,password]
     */
    public static String[] fillFormLogIn() {
        String[] details = {"", ""};
        Scanner scanInput = new Scanner(System.in);
        System.out.println("please enter your mail");
        String mailInput = scanInput.nextLine();
        System.out.println("please enter password");
        String password = scanInput.nextLine();
        details[0] = mailInput;
        details[1] = password;
        return details;
    }

    /**
     * this function check if an email address is valid using Regex.
     *
     * @param mailInput
     * @return
     */
    public static boolean checkMailInput(String mailInput) {
        String emailRegex = "^[a-zA-Z0-9_+&*-]+(?:\\." +
                "[a-zA-Z0-9_+&*-]+)*@" +
                "(?:[a-zA-Z0-9-]+\\.)+[a-z" +
                "A-Z]{2,7}$";

        Pattern pat = Pattern.compile(emailRegex);
        if (mailInput == null)
            return false;
        return pat.matcher(mailInput).matches();
    }

    /**
     * this function Use a regular expression (regex) to check for only letters and numbers
     * The regex will check for upper and lower case letters and digits
     *
     * @param password
     * @return
     */
    public static boolean checkPasswordValue(String password) {
        // todo - check input of password - only numbers and letters
        if (!password.matches("[a-zA-Z0-9]+")) {
            /* A non-alphanumeric character was found, return false */
            return false;
        }
        return true;
    }


    /***********************************************AssociationDelegate**************************************************/

    private static void AssociationDelegateMenu(Member member) {
        String input = "";
        while (!input.equals("Exit")) {
            System.out.println("choose one of the following options:\n");
            System.out.println("write \"1\" to add new league");
            System.out.println("write \"2\" to define season in a specific league");
            System.out.println("write \"3\" to set referee to a league in a specific season");
            System.out.println("write \"4\" to change the score policy in a specific league in season");
            System.out.println("write \"5\" to set scheduling policy in a specific league in season");
            System.out.println("\nwrite \"Exit\" if you want to finish. \n");
            input = "";
            while (input.equals("")) {
                input = scanInput.nextLine();
            }
            switch (input) {
                case "1":
                    System.out.println("Please write a name for new league");
                    String leagueName = scanInput.nextLine();
                    try {
                        controller.setLeague(leagueName);
                    } catch (IncorrectInputException incorrectInput) {
                        System.out.println("The name of league must contains only letters");
                    } catch (AlreadyExistException alreadyExist) {
                        System.out.println("This name is already exists");
                    } catch (Exception e) {
                        System.out.println("You can't define a league");
                    }
                    break;

                case "2":
                    System.out.println("Please choose a specific league (only letters)");
                    HashMap<String, League> leagues = controller.getLeagues();
                    for (String league : leagues.keySet()) {
                        System.out.println("League: " + league);
                    }
                    String specificLeague = scanInput.nextLine();
                    System.out.println("Please write the year of this league");
                    String year = scanInput.nextLine();
                    try {
                        controller.setLeagueByYear(specificLeague, year);
                    } catch (IncorrectInputException incorrectInput) {
                        System.out.println(incorrectInput.getMessage());

                    } catch (AlreadyExistException alreadyExist) {
                        System.out.println("This season is already exist in this league");
                    } catch (Exception e) {
                        System.out.println("You can't do this functionality");
                    }
                    break;

                case "3":


                case "4":

                case "5":

                case "Exit":

                default:

            }
        }
    }


}