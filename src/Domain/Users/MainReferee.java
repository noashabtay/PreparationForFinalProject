package Domain.Users;

import Domain.Asset.Player;
import Domain.Game.EventInGame;
import Domain.Game.Game;
import Domain.Game.Event;
import Exception.IncorrectInputException;

import java.util.*;

public class MainReferee extends Referee {

    public MainReferee(String name, String userMail, String password, String training , Date birthDate) {
        super(name, userMail, password, training , birthDate);
    }
    public MainReferee(Fan fan)
    {
        super(fan.getName(),fan.getUserMail(), fan.getPassword() , "" , fan.getBirthDate());
    }
   // public HashMap<Game,String> getGameReport()
    //todo

   public void updateGameEvent(Game game, int timeInGame, EventInGame event, Date time, String description, ArrayList<Player> players){
        game.addEvent(new Event(time, description, event, timeInGame, players));
   }


    // the details which didn't update will be recieved as ""
    public void updateDetails(String newName, String newMail,String newPassword, String newTraining) throws IncorrectInputException {
        if (newName == null || newMail ==null || newPassword == null || newTraining == null){
            throw new IncorrectInputException("");
        }
        if (newName != ""){
            super.setName(newName);
        }
        if (newMail != ""){
            super.setUserMail(newMail);
        }
        if (newPassword != ""){
            super.setPassword(newPassword);
        }
        if (newTraining != ""){
            super.training = newTraining;
        }
        //todo - delete and add again to the DB
    }





    public LinkedList<Game> getEditableGames (){
        LinkedList<Game> editableGames = new LinkedList();
        for (Game game : super.games){
            long difference = game.getDateCalendar().getTimeInMillis() - Calendar.getInstance().getTimeInMillis();
            float differenceInHours= difference/(60*60*100);
            if(differenceInHours < 5){
                editableGames.add(game);
            }
        }
        return editableGames;
    }


}