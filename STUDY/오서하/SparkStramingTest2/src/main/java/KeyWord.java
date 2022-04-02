public class KeyWord {

    private String keyword;
    private int total; //
    private int positive;
    private int nagative;
    private int neutality;
    private int unclassified;


    public KeyWord() {

    }

    public KeyWord(String keyword, int total, int positive, int nagative, int neutality, int unclassified) {
        this.keyword = keyword;
        this.total = total;
        this.positive = positive;
        this.nagative = nagative;
        this.neutality = neutality;
        this.unclassified = unclassified;
    }

    public void setKeyword(String keyword) {
        this.keyword = keyword;
    }

    public void setTotal(int total) {
        this.total = total;
    }

    public void setPositive(int positive) {
        this.positive = positive;
    }

    public void setNagative(int nagative) {
        this.nagative = nagative;
    }

    public void setNeutality(int neutality) {
        this.neutality = neutality;
    }

    public void setUnclassified(int unclassified) {
        this.unclassified = unclassified;
    }
}
