package az.traveltime.traveltimeweb.controller;

import az.traveltime.traveltimeweb.dto.OfferDto;
import az.traveltime.traveltimeweb.service.OfferDtoService;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;


@RestController
@RequestMapping(value = "/travelTime")
@RequiredArgsConstructor
@CrossOrigin("*")
public class OfferController {
    private final ObjectMapper objectMapper;
    @Autowired
    OfferDtoService offerDtoService;

    @PostMapping("/offer")
    public ResponseEntity<OfferDto> sendMessage(@RequestBody String offerDto) throws JsonProcessingException {
        OfferDto offerDto1 = objectMapper.readValue(offerDto, OfferDto.class);
        if (offerDto1.getId() == null && offerDto1.getSuggestion() == null) {
             return null;
        }
        offerDtoService.sendOffer(offerDto1);
        return ResponseEntity.ok(offerDto1);
    }


}
