package com.Koupag.controllers;

import com.Koupag.dtos.donation.CreateDonationDTO;
import com.Koupag.execptions.NoSuchElementExceptionWrapper;
import com.Koupag.execptions.NullPointerExceptionWrapper;
import com.Koupag.execptions.UnknownError;
import com.Koupag.mappers.DonationMapper;
import com.Koupag.models.DonationRequest;
import com.Koupag.services.DonationRequestService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.*;

@RestController
@RequestMapping(value = "api/donor/",consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
//@PreAuthorize("hasRole('ROLE_DONOR')")
public class DonorController {
    
    
    final private DonationRequestService donationRequestService;

    @Autowired
    public DonorController(DonationRequestService donationRequestService  ) {
        this.donationRequestService = donationRequestService;
    }
    

    @PostMapping("create-donation")
    public ResponseEntity<Void> donationRequest(@RequestBody CreateDonationDTO request){
        try {
            donationRequestService.createNewDonationRequest(request);
            return new ResponseEntity<>(HttpStatus.OK);
        }
        catch (NullPointerException e){
            throw new NullPointerExceptionWrapper("Requested Content is Null");
        } catch ( NoSuchElementException e){
            throw new NoSuchElementExceptionWrapper(" Requested Content is nonexistent ",e.getCause());
        }
        catch(Exception e){
            throw new UnknownError("Unknown Error : " + e.getMessage(),e.getCause(),e.getStackTrace());
        }
    }

    // Previous Donations
    @GetMapping("donations/{id}")
    public ResponseEntity<List<DonationMapper>>getAllDonations(@PathVariable(name = "id") UUID id){
        final List<DonationRequest> donationList = donationRequestService.getAllSuccessfulDonationRequestByDonorId(id);
        final List<DonationMapper> data = donationList.stream().map(DonationMapper::new).toList();
        return new ResponseEntity<>( data,HttpStatus.OK);
    }
    // closes an opened donation request
    @PostMapping("close_donation/{id}")
    public ResponseEntity<Void> closeActiveDonation(@PathVariable(name = "id") UUID id){
        try {
            donationRequestService.closeActiveDonationById(id);
            return new ResponseEntity<>(HttpStatus.OK);
        } catch (Exception e) {
            throw new UnknownError("Unknown Error : " + e.getMessage(),e.getCause(),e.getStackTrace());
        }
    }

    // Get the opened donation requests
    @GetMapping("active_donation/{id}")
    public ResponseEntity<DonationMapper> activeDonation(@PathVariable(name = "id") UUID id){
        try{
            DonationMapper donationRequest = new DonationMapper(donationRequestService.getActiveDonationRequestByDonorId(id));
            return new ResponseEntity<>(donationRequest,HttpStatus.OK);
        } catch (NullPointerException e) {

            throw new NullPointerExceptionWrapper("Requested Content is Null");
        } catch (Exception e){
            throw new UnknownError("Unknown Error : " + e.getMessage(),e.getCause(),e.getStackTrace());
        }
    }
}

