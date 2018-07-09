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
	int tick = config.getTicks();
	int[] stationLocation = config.getStations();
	ArrayList<Station> stations = new ArrayList<Station>();
	
	//Create stations
	for(int i : stationLocation) {
	    stations.add(new Station(route, i));
	}
	
	Random rand = new Random(seed);
	
	//Main loop
	for (int i = 0; i < tick; i++) {
	    ArrayList<Passenger> newPassengers = new ArrayList<Passenger>();
	    if (i == 0) {
		newPassengers = generatePassenger(stationLocation, route, i, limConfig[0], rand);
	    } else {
		newPassengers = generatePassenger(stationLocation, route, i, limConfig[1], rand);
	    }
	    
	    
	}
	System.out.printf("Using configuration:%n\t%s%n", Arrays.toString(trainSpecs));

	System.out.println("The result is:");

	for (TrainSpec aTrainSpecification : trainSpecs) {
	    Train aTrain = new Train(route, aTrainSpecification);
	    System.out.printf("\t%s is %s with capacity %,d%n", aTrain, aTrain.getLocation(), aTrain.getCapacity());
	} // end foreach()

    } // end test driver main()

    private static int getNextRandom(PairedLimit pl, Random rand) {
	return rand.nextInt(pl.maximum - pl.minimum + 1) + pl.minimum;
    }

    private static ArrayList<Passenger> generatePassenger(int[] stations, TrainRoute route, int time, PairedLimit pl,
	    Random rand) {
	ArrayList<Passenger> newPassengers = new ArrayList<Passenger>();
	for (int i = 0; i < getNextRandom(pl, rand); i++) {
	    int station = rand.nextInt(stations.length);
	    Location from = new Location(route, station, Direction.NOT_APPLICABLE);
	    station = rand.nextInt(stations.length);
	    Location to = new Location(route, station, route.whichDirection(from.getPosition(), station));

	    newPassengers.add(new Passenger(from, to, time));
	}
	return newPassengers;
    }
} // end class TrainRoute
