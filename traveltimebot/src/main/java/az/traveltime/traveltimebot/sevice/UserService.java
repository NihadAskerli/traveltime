package az.traveltime.traveltimebot.sevice;

import az.traveltime.traveltimebot.models.User;
import az.traveltime.traveltimebot.repo.UserRepo;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserService {
    public final UserRepo userRepo;
    public void saveUser(User user){
        userRepo.save(user);
    }
    public User getUserByChatId(Long id){
        return userRepo.getByChatId(id);
    }
}
