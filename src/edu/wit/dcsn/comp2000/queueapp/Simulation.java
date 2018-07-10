package edu.wit.dcsn.comp2000.queueapp;

import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Random;

import edu.wit.dcsn.comp2000.queueapp.Configuration.PairedLimit;
import edu.wit.dcsn.comp2000.queueapp.Configuration.TrainSpec;

public class Simulation {
    public static void main(String[] args) throws FileNotFoundException {

	Configuration config = new Configuration();
	TrainRoute route = new TrainRoute(config.getRoute());
	TrainSpec[] trainSpecs = config.getTrains();
	PairedLimit[] limConfig = config.getPassengers();

	int seed = config.getSeed();
	Random rand = new Random(seed);

	int tick = config.getTicks();
	int[] stationLocation = config.getStations();
	ArrayList<Station> stations = new ArrayList<Station>();

	// Create stations
	for (int i : stationLocation) {
	    stations.add(new Station(route, i));
	}

	// Create trains
	ArrayList<Train> trains = new ArrayList<Train>();
	for (TrainSpec spec : trainSpecs) {
	    trains.add(new Train(route, spec));
	}

	// Main loop
	Logger.create();
	for (int i = 0; i < tick; i++) {
	    Logger.write("TICK: " + (i + 1));
	    Logger.write("");
	    ArrayList<ArrayList<Passenger>> newPassengers = new ArrayList<ArrayList<Passenger>>(stations.size());

	    // Generate new passenger
	    if (i == 0) {
		newPassengers = generatePassenger(stationLocation, route, i, limConfig[0], rand);
	    } else {
		newPassengers = generatePassenger(stationLocation, route, i, limConfig[1], rand);
	    }

	    // Put passenger into approiate station
	    for (int pos = 0; pos < stations.size(); pos++) {
		Station s = stations.get(pos);
		s.enter(newPassengers.get(pos));
		int numPas[] = s.getNumPassenger();
		Logger.write("Station " + pos + " has " + numPas[0] + " passenger(s) inbound and " + numPas[1]
			+ " passenger(s) outbound");
	    }
	    Logger.write("");
	    // Move train
	    for (Train t : trains) {
		Logger.write(t + " is " + t.getLocation() + " with capacity " + t.getCapacity());
		// Check if train arrive at a station
		for (Station s : stations) {
		    // If train arrive at a station
		    if (t.getLocation().getPosition() == s.getLocation().getPosition()) {
			Logger.write(t + " ARRIVED AT " + s.toString());
			// Deboard Passenger
			ArrayList<Passenger> deboarded = t.deBoard();
			for (Passenger e : deboarded) {
			    Logger.write(e + " got off at " + s);
			}
			// Board passenger
			Passenger p = s.leave(t.getLocation().getDirection());
			while (p != null && t.board(p)) {
			    Logger.write(p + " boarded " + t);
			    p = s.leave(t.getLocation().getDirection());
			}
			break;
		    }
		}
		t.getLocation().move();
	    }
	    Logger.write("");
	}
	Logger.close();
    } // end test driver main()

    private static int getNextRandom(PairedLimit pl, Random rand) {
	return rand.nextInt(pl.maximum - pl.minimum + 1) + pl.minimum;
    }

    private static ArrayList<ArrayList<Passenger>> generatePassenger(int[] stations, TrainRoute route, int time,
	    PairedLimit pl, Random rand) {
	ArrayList<ArrayList<Passenger>> newPassengers = new ArrayList<ArrayList<Passenger>>();
	for (int i = 0; i < stations.length; i++) {
	    newPassengers.add(new ArrayList<Passenger>());
	}

	for (int i = 0; i < getNextRandom(pl, rand); i++) {
	    int station = rand.nextInt(stations.length);
	    Location from = new Location(route, station, Direction.NOT_APPLICABLE);
	    int station2 = rand.nextInt(stations.length);
	    while (station2 == station) {
		station2 = rand.nextInt(stations.length);
	    }
	    Location to = new Location(route, station2, Direction.NOT_APPLICABLE);

	    newPassengers.get(from.getPosition()).add(new Passenger(from, to, time));
	}
	return newPassengers;
    }
} // end class TrainRoute
