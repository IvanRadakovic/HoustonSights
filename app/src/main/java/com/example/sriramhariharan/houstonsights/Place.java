package com.example.sriramhariharan.houstonsights;
public class Place {
	private String name;
	private String tags;
	private String address;
	private double latitude;
	private double longitude;
	String description;

	public Place(String name, String tags, String address, double latitude, double longitude, String description) {
		this.name = name;
		this.tags = tags;
		this.address = address;
		this.latitude = latitude;
		this.longitude = longitude;
		this.description = description;
	}

	public String getName() {
		return name;
	}

	public String getTags() {
		return tags;
	}

	public String getAddress() {
		return address;
	}

	public double getLatitude() {
		return latitude;
	}

	public double getLongitude() {
		return longitude;
	}

	public String getDescription() {
		return description;
	}

	public String toString() {
		return getName();
	}

}
