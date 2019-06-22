package it.polito.tdp.extflightdelays.model;

public class Route {
	private Airport departure;
	private Airport destination;
	private int numRoute;

	public Route(Airport departure, Airport destination, int numRoute) {
		this.departure = departure;
		this.destination = destination;
		this.numRoute = numRoute;
	}

	public Airport getDeparture() {
		return departure;
	}

	public Airport getDestination() {
		return destination;
	}

	public int getNumRoute() {
		return numRoute;
	}

	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder();
		builder.append("Route [departure=");
		builder.append(departure);
		builder.append(", destination=");
		builder.append(destination);
		builder.append(", numRoute=");
		builder.append(numRoute);
		builder.append("]");
		return builder.toString();
	}

}
