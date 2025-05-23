import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)
public class QuoteData {
    private String quote;
    private String author;
    private int id;

    // 생성자
    public QuoteData() {}

    public String getQuote() {
        return quote;
    }
    public void setQuote(String quote) {
        this.quote = quote;
    }
    public String getAuthor() {
        return author;
    }
    public void setAuthor(String author) {
        this.author = author;
    }
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public void printQuote() {
        System.out.println("명언 : " + quote);
        System.out.println("작가 : " + author);
        System.out.println("id : " + id);
    }

}
