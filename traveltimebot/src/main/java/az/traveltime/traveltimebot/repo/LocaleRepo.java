package az.traveltime.traveltimebot.repo;



import az.traveltime.traveltimebot.models.Locale;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface LocaleRepo extends JpaRepository<Locale, Long> {
    List<Locale>getAllByKeyAndLang(String key,String lang);
    Locale getLocaleByKeyAndLang(String key,String lang);

}
