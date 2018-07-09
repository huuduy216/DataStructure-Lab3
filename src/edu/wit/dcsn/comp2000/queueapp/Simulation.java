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

	// Main loop
	for (int i = 0; i < tick; i++) {
	    ArrayList<ArrayList<Passenger>> newPassengers = new ArrayList<ArrayList<Passenger>>(stations.size());

	    if (i == 0) {
		newPassengers = generatePassenger(stationLocation, route, i, limConfig[0], rand);
	    } else {
		newPassengers = generatePassenger(stationLocation, route, i, limConfig[1], rand);
	    }

	    for (int pos = 0; pos < stations.size(); pos++) {
		stations.get(pos).enter(newPassengers.get(pos));
		System.out.printf("Station %n: %s/n", pos, stations.get(pos));
	    }
	    System.out.println("");
	}

    } // end test driver main()

    private static int getNextRandom(PairedLimit pl, Random rand) {
	return rand.nextInt(pl.maximum - pl.minimum + 1) + pl.minimum;
    }

    private static ArrayList<ArrayList<Passenger>> generatePassenger(int[] stations, TrainRoute route, int time,
	    PairedLimit pl, Random rand) {
	ArrayList<ArrayList<Passenger>> newPassengers = new ArrayList<ArrayList<Passenger>>(stations.length);
	for (int i = 0; i < getNextRandom(pl, rand); i++) {
	    int station = rand.nextInt(stations.length);
	    Location from = new Location(route, station, Direction.NOT_APPLICABLE);
	    station = rand.nextInt(stations.length);
	    Location to = new Location(route, station, Direction.NOT_APPLICABLE);

	    newPassengers.get(from.getPosition()).add(new Passenger(from, to, time));
	}
	return newPassengers;
    }
} // end class TrainRoute
