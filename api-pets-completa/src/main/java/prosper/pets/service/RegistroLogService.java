package prosper.pets.service;

import org.springframework.stereotype.Service;
import prosper.pets.domain.logs.RegistroLog;

public interface RegistroLogService {

    RegistroLog registrarLog(String usuario, String descricao);
}
