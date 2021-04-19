package ru.sfedu.HospitalityNetwork.repo;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.sfedu.HospitalityNetwork.models.OfferHost;

public interface OfferHostRepo extends JpaRepository<OfferHost, Long> {
}
