//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by Fernflower decompiler)
//

package League;

import Game.Game;
import Game.Team;

import java.util.*;

public abstract class ASchedulingPolicy {
    public ASchedulingPolicy() {
    }

    protected Set<Game> setGamesOfTeams(List<Team> teams, LeagueInSeason leagueInSeason, boolean isTwice) {
        int year = Integer.parseInt(leagueInSeason.getSeason().getYear());
        Set<Game> games = new HashSet();
        FixtureGenerator fixtureGenerator = new FixtureGenerator();
        List<List<Fixture>> rounds = fixtureGenerator.getFixtures(teams, isTwice);
        Calendar dateAndTime = new GregorianCalendar(year, 0, 1, 20, 30, 0);

        for(int i = 0; i < rounds.size(); ++i) {
            List<Fixture> round = rounds.get(i);
            for(Fixture fixture: round) {
                Game game = new Game(dateAndTime, fixture.getHomeTeam(), fixture.getAwayTeam(), fixture.getHomeTeam().getHomeField(), leagueInSeason);
                //System.out.println("Date1- " + game.getDateAndTimeString());
                games.add(game);
            }
            dateAndTime = new GregorianCalendar(year, 0, 1, 20, 30, 0);
            dateAndTime.add(Calendar.DATE, (i+1)*7);
        }

//        for(Game gameTest : games) {
//            System.out.println("Team1: " + gameTest.getHostTeam().getName() + " Team2: " + gameTest.getVisitorTeam().getName() + " Date: " + gameTest.getDateAndTimeString() + " Field: " + gameTest.getField().getNameOfField());
//        }
        return games;
    }

    public abstract String getNameOfPolicy();

    public abstract Set<Game> setGamesOfTeams(List<Team> var1, LeagueInSeason var2);
}
