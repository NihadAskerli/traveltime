package az.traveltime.traveltimeweb.service;

import az.traveltime.traveltimeweb.dto.RequestDto;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class AnswerDtoService {
 public static final List<RequestDto>list=new ArrayList<>();

    @RabbitListener(queues = "bot_queue")
    public void getAnswer(RequestDto requestDto) {
        System.out.println(requestDto);
        list.add(requestDto);
    }
    @RabbitListener(queues = "remove_queue")
    public void getId(Long requestId) {
        System.out.println(requestId);
        RequestDto request=null;
        for (RequestDto requestDto:list
             ) {
            if(requestDto.getRequestId()==requestId){
                request=requestDto;
            }
        }
        list.remove(request);
    }

}
