package Domain.Game;

import Domain.Asset.Field;
import Domain.League.LeagueInSeason;
import Domain.Users.Referee;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.HashSet;
import java.util.Observable;
import java.util.Observer;

public class Game extends Observable {
    private String id;
    private Calendar dateAndTime; //
    private Team hostTeam;
    private Team visitorTeam;
    private Field field;
    private String result;
    private EventLog eventLog;
    private LeagueInSeason leagueInSeason;
    private HashSet<Referee> referees;

    public Game(String id , Calendar dateAndTime,Team hostTeam, Team visitorTeam, Field field, Referee mainReferee, Referee secondaryReferee, LeagueInSeason leagueInSeason) {
        this.id=id;
        this.dateAndTime = dateAndTime;
        this.hostTeam = hostTeam;
        this.visitorTeam = visitorTeam;
        this.field = field;
        this.result = "";
        this.eventLog = new EventLog(this);
        this.leagueInSeason=leagueInSeason;
        referees = new HashSet<>();
        referees.add(mainReferee);
        referees.add(secondaryReferee);
    }
    public String getId(){
        return id;
    }
    public void removeReferee(Referee referee) {
        referees.remove(referee);
        //put new referee after the delete
    }

    public boolean isRefereeInTheGame(Referee referee){
        return referees.contains(referee);
    }

    public void addEvent (Event event){
        this.eventLog.addEvent(event);
        notifyFollowers("new event in the game:" + event.toString());
    }

    public String getDateString() {
        String ans = "" + dateAndTime.get(Calendar.YEAR) + "-" +  dateAndTime.get(Calendar.MONTH) + "-" + dateAndTime.get(Calendar.DAY_OF_MONTH);
        return ans;
    }
    public Calendar getDateCalendar() {
        return dateAndTime;
    }

    public Team getHostTeam() {
        return hostTeam;
    }

    public Team getVisitorTeam() {
        return visitorTeam;
    }


    public String getDateAndTimeString(){
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy MMM dd HH:mm:ss");
        String ans = sdf.format(dateAndTime.getTime());
        return ans;
    }

    public Field getField() {
        return field;
    }
    public HashSet<Referee> getReferees(){ return this.referees;}

    public void addNewFollower(Observer follower){
        addObserver(follower);
    }

    public void notifyFollowers (String message){
        setChanged();
        notifyObservers(message);
    }

    public int getFollowersNumber(){
        return countObservers();
    }

}
