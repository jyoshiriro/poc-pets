package prosper.pets.domain.racas;

public enum TipoRaca {

    CACHORRO("cachorros"),
    GATOS("gatos"),
    COELHO("coelhos");

    private String uri;

    TipoRaca(String uri) {
        this.uri = uri;
    }

    public String getUri() {
        return uri;
    }
}
