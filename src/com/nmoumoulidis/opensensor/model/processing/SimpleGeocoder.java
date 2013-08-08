package com.nmoumoulidis.opensensor.model.processing;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import com.nmoumoulidis.opensensor.view.ServerActivity;

import android.location.Address;
import android.location.Geocoder;

public class SimpleGeocoder
{
	private Geocoder geocoder;
	private List<Address> addressList;
	private List<Address> coordsList;
	private String coords[];
	private double latitude;
	private double longitude;
	
	public SimpleGeocoder(ServerActivity serveractivity) {
		geocoder = new Geocoder(serveractivity);
	}
	
	public String reverseGeocode(String location){
		coords = location.split(";");
		latitude = Double.valueOf(coords[0]);
		longitude = Double.valueOf(coords[1]);
		String locString="";
		try {
			addressList = geocoder.getFromLocation(latitude, longitude, 5);
			for(int i=0 ; i<addressList.size() ; i++) {
				if(addressList.get(i).getPostalCode() != null) {
					if(addressList.get(i).getPostalCode().contains(" ")) {
						for(int j=0 ; j<=addressList.get(i).getMaxAddressLineIndex() ; j++) {
							locString += addressList.get(i).getAddressLine(j);
							if(j != addressList.get(i).getMaxAddressLineIndex()) {
								locString += ", ";
							}
							else {
								locString += ".";
							}
						}
						return locString;
					}
				}
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
		return locString;
	}
	
	public String forwardGeocode(String address) {
		try {
			coordsList = geocoder.getFromLocationName(address, 1);
			for(int i=0; i<coordsList.size();) {
				return coordsList.get(i).getLatitude()+";"+coordsList.get(i).getLongitude();
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
		return null;
	}
	
	public ArrayList<HashMap<String, String>> replaceCoordsWithAddress(ArrayList<HashMap<String, String>> data) {
		String tempLocation;
		for(int i=0 ; i<data.size() ; i++) {
			tempLocation = reverseGeocode(data.get(i).get("location"));
			data.get(i).put("location", tempLocation);
		}
		return data;
	}

	public ArrayList<HashMap<String, String>> filterDataByNearbyLocationsGivenByUser(ArrayList<HashMap<String, String>> data,
													String locationGivenByUser) {
		if(locationGivenByUser.equals("")) {
			return data;
		}
		
		String coordsAsPerUsersLocation = forwardGeocode(locationGivenByUser);
		if(coordsAsPerUsersLocation != null) {
			System.out.println("forward Geocoding found something!");
			coords = coordsAsPerUsersLocation.split(";");
			double userLatitude = Double.valueOf(coords[0]);
			double userLongitude = Double.valueOf(coords[1]);
			
			ArrayList<HashMap<String, String>> newDataList = new ArrayList<HashMap<String, String>>();
			for(int i=0 ; i<data.size() ; i++) {
				coords = data.get(i).get("location").split(";");
				latitude = Double.valueOf(coords[0]);
				longitude = Double.valueOf(coords[1]);
				double distance = calculateHaversineDistance(latitude,userLatitude, longitude, userLongitude);
				System.out.println("HAVERSINE DISTANCE: "+distance);
				if(distance < 500) {
					newDataList.add(data.get(i));
				}
			}
			return newDataList;
		}
		else {
			return null;
		}
	}
	
	public static double calculateHaversineDistance(double lat1,double lat2,double lon1,double lon2) {
		double earthRadius = 6371000;
        double dLat = Math.toRadians(lat1 - lat2);
        double dLong = Math.toRadians(lon1 - lon2);
        double lat1Rad = Math.toRadians(lat1);
        double lat2Rad = Math.toRadians(lat2);
        
        double angle = 2 * Math.asin(Math.sqrt(Math.pow(Math.sin(dLat / 2), 2) 
        		+ Math.cos(lat1Rad) * Math.cos(lat2Rad) * Math.pow(Math.sin(dLong / 2), 2)));
        
        return angle * earthRadius;
      }

}
