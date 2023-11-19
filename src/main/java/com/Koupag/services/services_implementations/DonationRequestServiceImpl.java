package com.Koupag.services.services_implementations;

import com.Koupag.dtos.donation.CreateDonationDTO;
import com.Koupag.models.*;
import com.Koupag.dtos.donation.EngagedDonationDTO;
import com.Koupag.dtos.donation.CompleteDonationDTO;
import com.Koupag.repositories.*;
import com.Koupag.services.DonationRequestService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;

@Service
public class DonationRequestServiceImpl implements DonationRequestService {
    private final DonationRequestRepository donationRequestRepository;
    private final DonorRepository donorRepository;
    private final VolunteerRepository volunteerRepository;
    private final RecipientRepository recipientRepository;
    private final SurplusMaterialRepository surplusMaterialRepository;
    private final RequestItemRepository requestItemRepository;
    @Autowired
    public DonationRequestServiceImpl(DonationRequestRepository donationRequestRepository,
                                      DonorRepository donorRepository, VolunteerRepository volunteerRepository,
                                      RecipientRepository recipientRepository, SurplusMaterialRepository surplusMaterialRepository,
                                      RequestItemRepository requestItemRepository) {
        this.donationRequestRepository = donationRequestRepository;
        this.donorRepository = donorRepository;
        this.volunteerRepository = volunteerRepository;
        this.recipientRepository = recipientRepository;
        this.surplusMaterialRepository = surplusMaterialRepository;
        this.requestItemRepository = requestItemRepository;
    }

    @Override
    public DonationRequest createNewDonationRequest(CreateDonationDTO request) throws  Exception {
//        System.out.println(LocalDateTime.parse(request.getExpectedPickupTime() , DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss.SSSXXX")));
        if(donationRequestRepository.findByDonorIdAndIsDonationActiveTrue(request.getDonorId()) == null){
            DonationRequest dr = new DonationRequest();
            dr.setDonor(donorRepository.findById(request.getDonorId()).get());
            final RequestItem requestItemTemp = new RequestItem(
                    request.getCount(),
                    surplusMaterialRepository.findById(request.getSurplusMaterialId()).get()
            );
            requestItemRepository.save(requestItemTemp);
            dr.setDescription(request.getDescription());
            dr.setExpectedPickupTime(request.getExpectedPickupTime());
            dr.setLocation(request.getLocation());
            dr.setRequestItem(requestItemTemp);
            dr.setCreationDateAndTime(LocalDateTime.now());
            dr.setIsDonationActive(true);

            return donationRequestRepository.save(dr);
        }
        throw new Exception("Already exists an donation");
    }
    
    @Override
    public Optional<DonationRequest> getDonationRequestById(long id) {
        return donationRequestRepository.findById(id);
    }

    @Override
    public void updateVolunteerPickupByDonationRequest(EngagedDonationDTO engagedDonationDTO) throws NoSuchElementException, Exception  {
        Optional<DonationRequest> requestToBeUpdated = donationRequestRepository.findById(engagedDonationDTO.getRequestId());
        if(requestToBeUpdated.isPresent())      // Can throw self-made Exception here...
        {
            DonationRequest request = requestToBeUpdated.get();
            if (!request.getIsDonationActive()) return;    // The donation was closed by donor
            if (request.getVolunteerPickupTime() != null) return; // The donation isn't picked yet.

            request.setVolunteer(volunteerRepository.findById(engagedDonationDTO.getVolunteerId()).get());
            request.setVolunteerPickupTime(LocalDateTime.now());
            donationRequestRepository.save(request);
        } else {
            throw new Exception("Donation Not Found");
        }
    }

    @Override
    public void updateVolunteerEngagedTime(EngagedDonationDTO engagedDonationDTO) throws Exception  {
        Optional<DonationRequest> requestToBeUpdated = donationRequestRepository.findById(engagedDonationDTO.getRequestId());
        if(requestToBeUpdated.isPresent())      // Can throw self-made Exception here...
        {
            DonationRequest request = requestToBeUpdated.get();
            if(!request.getIsDonationActive()) return;    // The donation was closed by donor
            if(request.getVolunteerPickupTime() == null) return; // The donation isn't picked yet.
            if(request.getEngagedDateAndTime() != null) return;  // The donation already engaged by another one

            Donor donor = donorRepository.findById(request.getDonor().getId()).get();
            donor.setLastServed(LocalDate.now());
            donorRepository.save(donor);

            request.setEngagedDateAndTime(LocalDateTime.now());
            donationRequestRepository.save(request);
        } else {
            throw new Exception("Donation Not Found");
        }
    }
    
    @Override
    public void updateRecipientByDonationRequest(CompleteDonationDTO completeDonationDTO) throws Exception {

        Optional<DonationRequest> requestToBeUpdated = donationRequestRepository.findById(completeDonationDTO.getRequestId());
        if(requestToBeUpdated.isPresent())      // Can throw self-made Exception here...
        {
            DonationRequest request = requestToBeUpdated.get();     // Can throw self-made Exception here...
            if(!request.getIsDonationActive()) return;    // The donation was closed by donor
            if(request.getVolunteerPickupTime() == null) return; // The donation isn't picked yet.
            if(request.getEngagedDateAndTime() == null) return;  // The donation already engaged by another one
            if(request.getSuccessfulDonationDateAndTime() != null) return; // The donation had donated already
            if(request.getVolunteer().getId() != completeDonationDTO.getVolunteerId()) return; // The case another volunteer is trying to donate donation


            Volunteer volunteer = volunteerRepository.findById(completeDonationDTO.getVolunteerId()).get();
            volunteer.setLastServed(LocalDate.now());
            volunteerRepository.save(volunteer);
            Recipient recipient = recipientRepository.findById(completeDonationDTO.getRecipientId()).get();
            recipient.setLastServed(LocalDate.now());
            recipientRepository.save(recipient);



            request.setRecipient(recipientRepository.findById(completeDonationDTO.getRecipientId()).get());
            request.setSuccessfulDonationDateAndTime(LocalDateTime.now());
            request.setIsDonationActive(false);
            donationRequestRepository.save(request);
        } else {
            throw new Exception("Donation Not Found");
        }
    }
    
    @Override
    public List<DonationRequest> getAllSuccessfulDonationRequestByDonorId(Long donorId) {
	    return donationRequestRepository.findByDonorIdAndIsDonationActiveFalse(donorId);
    }
    // New Methods - Start
    @Override
    public DonationRequest getActiveDonationRequestByDonorId(Long donorId) throws Exception {
        return donationRequestRepository.findByDonorIdAndIsDonationActiveTrue(donorId);
    }

    @Override
    public List<DonationRequest> getAllSuccessfulDonationRequestByVolunteerId(Long volunteerId) {
        return donationRequestRepository.findByVolunteerIdAndIsDonationActiveFalse(volunteerId);
    }

    @Override
    public List<DonationRequest> getActiveDonationRequestByVolunteerId(Long volunteerId) {
        return null;
    }

    @Override
    public List<DonationRequest> getAllDonationRequestByRecipientId(Long recipientId) {
        return donationRequestRepository.findDonationRequestsByRecipientId(recipientId);
    }
    
    @Override
    public void closeActiveDonationById(Long id) throws Exception {
        DonationRequest activeDonation = getActiveDonationRequestByDonorId(id);
        activeDonation.setIsDonationActive(false);
        donationRequestRepository.save(activeDonation);
    }
    
    @Override
    public List<DonationRequest> getAllActiveDonation() {
        return donationRequestRepository.findByIsDonationActiveTrue();
    }
    // New Methods - End
}
