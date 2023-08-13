package com.Koupag.controllers;

import com.Koupag.dtos.donation.EngagedDonationDTO;
import com.Koupag.dtos.donation.CompleteDonationDTO;
import com.Koupag.services.DonationRequestService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.NoSuchElementException;

@RestController
@RequestMapping("api/volunteer/")
public class VolunteerController {
	private final DonationRequestService donationRequestService;
	
	public VolunteerController(DonationRequestService donationRequestService) {
		this.donationRequestService = donationRequestService;
	}
	
	@PostMapping("engage-donation")
	public ResponseEntity<String> addVolunteerIdToRequest(@RequestBody EngagedDonationDTO engagedDonationDTO){
		try{
			donationRequestService.updateVolunteerIdByDonationRequest(engagedDonationDTO);
			return new ResponseEntity<>("Donation Request have been engaged by: "+engagedDonationDTO.getVolunteerId(), HttpStatus.OK);
		} catch (NoSuchElementException e){
			return new ResponseEntity<String>(e.getMessage(),HttpStatus.BAD_REQUEST);
		}
	}

	@PostMapping("complete-donation")
	public ResponseEntity<String> addVolunteerIdToRequest(@RequestBody CompleteDonationDTO completeDonationDTO){
		try{
			donationRequestService.updateRecipientIdByDonationRequest(completeDonationDTO);
			return new ResponseEntity<>("Successful Donation", HttpStatus.OK);
		} catch (NoSuchElementException e){
			return new ResponseEntity<String>(e.getMessage(), HttpStatus.BAD_REQUEST);
		} catch (Exception e) {
            return new ResponseEntity<String>(e.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }
}
