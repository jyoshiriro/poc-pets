package prosper.pets.exception;

import feign.FeignException;
import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;

public class ChamadaApiException extends ResponseStatusException
{
    public static ChamadaApiException criar(String nomeApi, FeignException causa) {
        HttpStatus status;
        String mensagem;

        if (causa.status() == -1) {
            status = HttpStatus.SERVICE_UNAVAILABLE;
            mensagem = "API de %s indisponível: %s";
        } else {
            status = HttpStatus.INTERNAL_SERVER_ERROR;
            mensagem = "Erro na chamada à API de %s: %s";
        }

        return new ChamadaApiException(
            status,
            String.format(mensagem, nomeApi, causa.getMessage())
        );
    }

    private ChamadaApiException(HttpStatus status, String reason) {
        super(status, reason);
    }
}
