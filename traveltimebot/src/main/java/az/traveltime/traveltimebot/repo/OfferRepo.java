package az.traveltime.traveltimebot.repo;

import az.traveltime.traveltimebot.models.Offer;
import org.springframework.data.jpa.repository.JpaRepository;

public interface OfferRepo extends JpaRepository<Offer, Long> {
    Offer getByRequestId(Long id);
}
