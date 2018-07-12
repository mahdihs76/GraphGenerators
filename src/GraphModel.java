/**
 * Created by MahdiHS on 7/12/2018.
 */
public enum GraphModel {
    BARABASI_ALBERT("Barabasi-Albert Model"),
    WATTS_STROGATZ("Watts-Strogatz Model"),
    MEDIATION_DRIVEN("Mediation-Driven Attachment Model");

    private final String text;

    GraphModel(String text) {
        this.text = text;
    }

    public String getText() {
        return text;
    }
}
