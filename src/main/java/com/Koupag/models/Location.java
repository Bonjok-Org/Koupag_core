package com.Koupag.models;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class Location {
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private Long Id;
	
	@JsonProperty("Latitude")
	private String Latitude;
	@JsonProperty("Longitude")
	private String Longitude;
	
	@JsonBackReference
	@OneToOne(targetEntity = Address.class,fetch = FetchType.LAZY,cascade = CascadeType.ALL)
	@JoinColumn(name = "user_address")
	private Address home_address;
	
	@OneToOne(targetEntity = DonationRequest.class, mappedBy = "location",fetch = FetchType.LAZY,cascade = CascadeType.ALL)
	private DonationRequest donationRequest;
	
	public Location(String latitude, String longitude, Address address) {
		Latitude = latitude;
		Longitude = longitude;
		this.home_address = address;
	}
	
	public Location(String latitude, String longitude) {
		Latitude = latitude;
		Longitude = longitude;
	}
}
