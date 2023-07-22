package com.Koupag.Repository;

import com.Koupag.Model.DonationRequest;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface DonationRequestRepository extends JpaRepository<DonationRequest,Long> {
}
