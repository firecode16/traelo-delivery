package com.traelo.delivery.service.impl;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.springframework.stereotype.Service;

import com.traelo.delivery.model.DeliveryZone;
import com.traelo.delivery.model.dto.CenterDTO;
import com.traelo.delivery.model.dto.GeometryDTO;
import com.traelo.delivery.model.dto.ZoneDTO;
import com.traelo.delivery.service.ZoneService;

@Service
public class ZoneServiceImpl implements ZoneService {

	// Radius of the Earth in kilometers
	private static final double EARTH_RADIUS_KM = 6371.0;

	/**
	 * Calculate the distance between two points using the Haversine formula
	 */
	@Override
	public double calculateDistance(double lat1, double lng1, double lat2, double lng2) {
		double dLat = Math.toRadians(lat2 - lat1);
		double dLng = Math.toRadians(lng2 - lng1);

		double a = Math.sin(dLat / 2) * Math.sin(dLat / 2) + Math.cos(Math.toRadians(lat1)) * Math.cos(Math.toRadians(lat2)) * Math.sin(dLng / 2) * Math.sin(dLng / 2);

		double c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1 - a));

		return EARTH_RADIUS_KM * c;
	}

	/**
	 * Find all areas near the user within a radius
	 */
	@Override
	public List<String> findNearbyZoneIds(List<DeliveryZone> allDeliveryZones, double userLat, double userLng, double maxDistanceKm) {
	    Set<String> nearbyZoneIds = new HashSet<>();

	    System.out.println("🎯 Searching zones near: (" + userLat + ", " + userLng + ") within " + maxDistanceKm + "Km");

	    for (DeliveryZone deliveryZone : allDeliveryZones) {
	        if (deliveryZone.getZones() == null)
	            continue;

	        for (Object zoneObj : deliveryZone.getZones()) {
	            try {
	                ZoneDTO zone = convertLinkedHashMapToZoneDTO(zoneObj);

	                if (zone != null && isValidZone(zone)) {
	                    // CHECK COORDINATES BEFORE CALCULATION
	                    Double zoneLat = zone.getCenter().getLatitude();
	                    Double zoneLng = zone.getCenter().getLongitude();
	                    
	                    System.out.println("📍 Zone: " + zone.getName());
	                    System.out.println("📍 Center coordinates: (" + zoneLat + ", " + zoneLng + ")");

	                    // Verify that the coordinates are reasonable for Mexico
	                    if (!isValidMexicoCoordinates(zoneLat, zoneLng)) {
	                        System.out.println("⚠️  Suspicious coordinates for Mexico, checking order...");
	                        
	                        // Try with inverted coordinates
	                        double distanceInverted = calculateDistance(userLat, userLng, zoneLng, zoneLat);
	                        double distanceNormal = calculateDistance(userLat, userLng, zoneLat, zoneLng);
	                        
	                        System.out.println("🔄 Distance (normal): " + String.format("%.2f", distanceNormal) + " Km");
	                        System.out.println("🔄 Distance (inverted): " + String.format("%.2f", distanceInverted) + " Km");
	                        
	                        // Use the smallest (most likely) distance
	                        double distance = Math.min(distanceNormal, distanceInverted);
	                        
	                        if (distance <= maxDistanceKm) {
	                            nearbyZoneIds.add(zone.getId());
	                            System.out.println("✅ ADDED to nearby zones (inverted coordinates)");
	                        }
	                    } else {
	                        // Coordinates seem correct
	                        double distance = calculateDistance(userLat, userLng, zoneLat, zoneLng);
	                        System.out.println("📏 Distance: " + String.format("%.2f", distance) + " Km");

	                        if (distance <= maxDistanceKm) {
	                            nearbyZoneIds.add(zone.getId());
	                            System.out.println("✅ ADDED to nearby zones");
	                        }
	                    }
	                }
	            } catch (Exception e) {
	                System.err.println("❌ Error processing zone: " + e.getMessage());
	                continue;
	            }
	        }
	    }

	    System.out.println("🎯 Total nearby zones found: " + nearbyZoneIds.size() + " - " + nearbyZoneIds);
	    return new ArrayList<>(nearbyZoneIds);
	}

	/**
	 * Check if the coordinates are reasonable for Mexico
	 */
	private boolean isValidMexicoCoordinates(Double lat, Double lng) {
	    if (lat == null || lng == null) return false;
	    
	    // Mexico is approximately between 14° and 33° N, and 86° and 118° W
	    boolean validLat = lat >= 14.0 && lat <= 33.0;
	    boolean validLng = lng >= -118.0 && lng <= -86.0;
	    
	    return validLat && validLng;
	}

	private ZoneDTO convertLinkedHashMapToZoneDTO(Object zoneObj) {
		try {
			if (zoneObj instanceof ZoneDTO) {
				return (ZoneDTO) zoneObj;
			}

			if (zoneObj instanceof Map) {
				@SuppressWarnings("unchecked")
				Map<String, Object> zoneMap = (Map<String, Object>) zoneObj;

				String id = (String) zoneMap.get("id");
				String name = (String) zoneMap.get("name");
				String address = (String) zoneMap.get("address");
				String place_name = (String) zoneMap.get("place_name");
				CenterDTO center = extractCenter(zoneMap.get("center"));
				GeometryDTO geometry = extractGeometry(zoneMap.get("geometry"));

				return new ZoneDTO(id, name, place_name, center, address, geometry);
			}

			System.err.println("❌ Unsupported type: " + zoneObj.getClass().getName());
			return null;
		} catch (Exception e) {
			System.err.println("❌ Error in convertLinkedHashMapToZoneDTO: " + e.getMessage());
			return null;
		}
	}

	private CenterDTO extractCenter(Object centerObj) {
	    try {
	        if (centerObj instanceof Map) {
	            @SuppressWarnings("unchecked")
	            Map<String, Object> centerMap = (Map<String, Object>) centerObj;
	            
	            // CHECK THE CURRENT ORDER OF THE COORDINATES
	            Object latitudeObj = centerMap.get("latitude");
	            Object longitudeObj = centerMap.get("longitude");
	            
	            System.out.println("🔍 DEBUG extractCenter - raw latitude: " + latitudeObj + ", raw longitude: " + longitudeObj);
	            
	            // TRY DIFFERENT ORDERS
	            Double latitude = extractDouble(latitudeObj);
	            Double longitude = extractDouble(longitudeObj);
	            
	            // If the coordinates seem to be reversed (lng, lat), correct them
	            if (latitude != null && longitude != null) {
	                // Check if they are reversed (typical coordinates of Mexico)
	                if (Math.abs(latitude) > 90 && Math.abs(longitude) <= 90) {
	                    // latitude has a longitude value (>90), they are reversed
	                    System.out.println("🔄 Coordinates appear inverted, swapping...");
	                    
	                    Double temp = latitude;
	                    latitude = longitude;
	                    longitude = temp;
	                } else if (Math.abs(longitude) > 90 && Math.abs(latitude) <= 90) {
	                    // Longitude has a longitude value (>90), they are reversed 
	                    System.out.println("🔄 Coordinates appear inverted, swapping...");
	                    
	                    Double temp = longitude;
	                    longitude = latitude;
	                    latitude = temp;
	                }
	            }
	            
	            System.out.println("✅ Final coordinates - lat: " + latitude + ", lng: " + longitude);
	            return new CenterDTO(latitude, longitude);
	        }
	        
	        return null;
	    } catch (Exception e) {
	        System.err.println("❌ Error extracting center: " + e.getMessage());
	        return null;
	    }
	}

	private GeometryDTO extractGeometry(Object geometryObj) {
		try {
			if (geometryObj instanceof Map) {
				@SuppressWarnings("unchecked")
				Map<String, Object> geometryMap = (Map<String, Object>) geometryObj;
				Object coordinates = geometryMap.get("coordinates");

				if (coordinates instanceof List) {
					@SuppressWarnings("unchecked")
					List<Number> coordsList = (List<Number>) coordinates;
					
					if (coordsList.size() >= 2) {
						Double[] coordsArray = new Double[] { coordsList.get(0).doubleValue(), coordsList.get(1).doubleValue() };
						return new GeometryDTO(coordsArray);
					}
				}
			}
			return null;
		} catch (Exception e) {
			System.err.println("❌ Error extracting geometry: " + e.getMessage());
			return null;
		}
	}

	private Double extractDouble(Object value) {
		if (value instanceof Number) {
			return ((Number) value).doubleValue();
		}
		return null;
	}

	private boolean isValidZone(ZoneDTO zone) {
		return zone != null && zone.getCenter() != null && zone.getCenter().getLatitude() != null && zone.getCenter().getLongitude() != null;
	}

}
