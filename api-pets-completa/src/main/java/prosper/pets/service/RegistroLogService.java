package prosper.pets.service;

import feign.FeignException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;
import prosper.pets.config.apiclient.RegistroLogsApi;
import prosper.pets.domain.logs.RegistroLog;

@Service
public class RegistroLogService {

    @Autowired
    private RegistroLogsApi registroLogsApi;

    public RegistroLog registrarLog(String usuario, String descricao) {
        RegistroLog novoRegistroLog = new RegistroLog(usuario, descricao);
        try {
            return registroLogsApi.post(novoRegistroLog);
        } catch (FeignException ex) {
            if (ex.status() == -1) {
                throw new ResponseStatusException(
                        HttpStatus.SERVICE_UNAVAILABLE,
                        "API de Logs indisponível"
                );
            }
            throw new ResponseStatusException(
                    HttpStatus.INTERNAL_SERVER_ERROR,
                    String.format("Erro na chamada à API de Logs: %s", ex.getMessage()),
                    ex
            );
        }
    }
}
