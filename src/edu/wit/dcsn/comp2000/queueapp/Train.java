/**
 * Representation of a train on a train route. A Train has a fixed, limited
 * capacity to carry Passengers. Passengers board() and disembark().
 * 
 * <p>
 * NOTE: This class is incomplete - you may want to restructure it based on your
 * implementation's requirements.
 * 
 * <p>
 * <b>WARNING</b>: Some CIRCULAR route functionality is not yet implemented!
 * 
 * <p>
 * Note: You may use this class, with or without modification, in your Comp
 * 2000, Queue application/Train Simulation solution. You must retain all
 * authorship comments. If you modify this, add your authorship to mine.
 */

/*
 * Usage restrictions:
 * 
 * You may use this code for exploration, experimentation, and furthering your
 * learning for this course. You may not use this code for any other
 * assignments, in my course or elsewhere, without explicit permission, in
 * advance, from myself (and the instructor of any other course). Further, you
 * may not post or otherwise share this code with anyone other than current
 * students in my sections of this course. Violation of these usage restrictions
 * will be considered a violation of the Wentworth Institute of Technology
 * Academic Honesty Policy.
 */

package edu.wit.dcsn.comp2000.queueapp;

import java.io.FileNotFoundException ;
import java.util.ArrayList ;
import java.util.Arrays ;

import edu.wit.dcsn.comp2000.queueapp.Configuration.TrainSpec ;


/**
 * Representation of a train on a train route. A Train has a fixed, limited
 * capacity to carry Passengers. Passengers board() and disembark().
 * 
 * <p>
 * NOTE: This class is incomplete - you may want to restructure it based on your
 * implementation's requirements.
 * 
 * <p>
 * <b>WARNING</b>: Some CIRCULAR route functionality is not yet implemented!
 * 
 * <p>
 * Note: You may use this class, with or without modification, in your Comp
 * 2000, Queue application/Train Simulation solution. You must retain all
 * authorship comments. If you modify this, add your authorship to mine.
 * 
 * @author David M Rosenberg
 * @version 1.0.0	base version
 */
public final class Train
	{
	// class-wide/shared information
	private static int	nextId =		1 ;	// enables automatic id assignment
	
	// per-instance fields
	private final int	id ;				// unique id for this train route
	
	private final int	capacity ;
	private Location	currentLocation ;
	private ArrayList< Passenger >
						passengers ;

	
	/**
	 * @param onRoute the instance of the TrainRoute on which this Train
	 *            operates
	 * @param trainSpecification the specifications from the configuration file
	 */
	public Train( TrainRoute	onRoute,
	              TrainSpec		trainSpecification )
    	{
    	id =							Train.nextId++ ;	// assign the next unique id
    	
    	// create an empty collection to hold Passengers while they're on board
    	passengers =					new ArrayList<>() ;
    	
    	// save the configuration parameters
    	capacity =						trainSpecification.capacity ;
    	currentLocation =				new Location( onRoute,
    	             					              trainSpecification.location,
    	             					              trainSpecification.direction ) ;
    	
    	}	// end 2-arg constructor
	
	
	/**
	 * Retrieves the capacity (maximum number of Passengers simultaneously on
	 * this train) as specified in the configuration file
	 * 
	 * @return the capacity was set when the train was instantiated
	 */
	public int getCapacity()
    	{
    	return capacity ;
    	}	// end getCapacity()
	
	
	/**
	 * Retrieves the current location along a route
	 * 
	 * @return the current location object
	 */
	public Location getLocation()
    	{
    	return currentLocation ;
    	}	// end getLocation()
	
	
	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString()
    	{
    	return String.format( "%s %,d", 
    	                      getClass().getSimpleName(),
    	                      id ) ;
    	}	// end toString()
	
	
	
	/**
	 * Board passengers into the train
	 * @return true if successfully board the passenger
	 */
	public boolean board(ArrayList<Passenger> newPassengers) {
	    if(passengers.size() + newPassengers.size() <= capacity) {
		return passengers.addAll(newPassengers);
	    }
	    return false;
	}
	
	/**
	 * Deboard passengers from the train
	 * @return a list of passenger that need to get off
	 */
	public ArrayList<Passenger> deBoard(){
	    ArrayList<Passenger> deBoardList = new ArrayList<Passenger>();
	    for(Passenger p : passengers) {
		if (currentLocation.getId() == p.getTo().getId()) {
		    deBoardList.add(p);
		    passengers.remove(p);
		}
	    }
	    return deBoardList;
	}
	/**
	 * Unit test driver
	 * 
	 * @param args -unused-
	 * @throws FileNotFoundException see {@link Configuration#Configuration()}
	 */
	public static void main( String[] args ) throws FileNotFoundException
    	{
    	Configuration	theConfig =		new Configuration() ;
       	TrainRoute		theRoute =		new TrainRoute( theConfig.getRoute() ) ;
    	TrainSpec[]		theTrainSpecs =	theConfig.getTrains() ;
    	
    	System.out.printf( "Using configuration:%n\t%s%n",
    	                   Arrays.toString( theTrainSpecs )
    	                   ) ;
    	
    	System.out.println( "The result is:" ) ;
    	
    	for( TrainSpec aTrainSpecification : theTrainSpecs )
    		{
    		Train		aTrain =		new Train( theRoute, aTrainSpecification ) ;
    		System.out.printf( "\t%s is %s with capacity %,d%n",
    		                   aTrain,
    		                   aTrain.currentLocation,
    		                   aTrain.capacity
    						) ;
    		}	// end foreach()

    	}	// end test driver main()

	}	// end class TrainRoute
