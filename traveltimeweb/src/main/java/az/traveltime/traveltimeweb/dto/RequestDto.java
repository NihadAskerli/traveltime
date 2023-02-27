package az.traveltime.traveltimeweb.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class RequestDto {
    private Long requestId;
    private String language;
    private String category;
    private String offer;
    private String countryType;
    private String travelType;
    private String destination;
    private String startingPoint;
    private String startDate;
    private String endDate;
    private String withSomeone;
    private String budget;
}
