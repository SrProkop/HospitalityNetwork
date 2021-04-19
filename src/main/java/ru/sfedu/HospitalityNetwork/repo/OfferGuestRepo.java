package ru.sfedu.HospitalityNetwork.repo;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.sfedu.HospitalityNetwork.models.OfferGuest;

public interface OfferGuestRepo extends JpaRepository<OfferGuest, Long> {
}
