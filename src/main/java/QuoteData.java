import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)
public class QuoteData {
    private int id;
    private String content;
    private String author;

    // 생성자
    public QuoteData() {}

    public QuoteData(int id, String content, String author) {
        this.id = id;
        this.content = content;
        this.author = author;
    }

    public String getContent() {
        return content;
    }

    // getter, setter
    public void setContent(String content) {
        this.content = content;
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

    // 기타 메소드
    public void printQuote() {
        System.out.println("명언 : " + content);
        System.out.println("작가 : " + author);
        System.out.println("id : " + id);
    }
}
