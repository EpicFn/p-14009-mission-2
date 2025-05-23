import java.util.ArrayList;
import java.util.Scanner;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

enum CommandType {
    종료, 등록, 삭제, 수정, 목록, 빌드, 시작
}

public class QuoteBoard {

    // -----------------------------------------------------
    // attribute
    // -----------------------------------------------------

    private CommandType cmd;
    private final ArrayList<QuoteData> dic; //[quote, author, id]
    private int nextId = 1;

    private FileManager fm = new FileManager(); // 파일 관리 객체

    // -----------------------------------------------------
    // constructor
    // -----------------------------------------------------

    QuoteBoard(){
        cmd = CommandType.시작;
        dic = new ArrayList<QuoteData>();
        loadQuoteList(); // 명언 목록 불러오기
        loadNextId(); // 다음 id 불러오기
    }

    // -----------------------------------------------------
    // getter, setter
    // -----------------------------------------------------

    public CommandType getCmd() {
        return cmd;
    }

    public void setCmd(CommandType cmd) {
        this.cmd = cmd;
    }

    public int getNextId(){
        return nextId;
    }

    // -----------------------------------------------------
    // CRUD method
    // -----------------------------------------------------

    /**
     * 명언 등록
     */
    public void register() {
        Scanner sc = new Scanner(System.in);
        String quote;
        String author;
        boolean flag;

        do{
            System.out.print("명언 : ");
            quote = sc.nextLine();

            flag = filterString(quote);

            if(flag)
                System.out.println("특수문자는 입력할 수 없습니다");
        }while(flag);

        do{
            System.out.print("작가 : ");
            author = sc.nextLine();

            flag = filterString(author);

            if(flag)
                System.out.println("특수문자는 입력할 수 없습니다");
        }while(flag);


        // 명언 등록
        try {
            QuoteData qd = new QuoteData(nextId, quote, author);
            fm.createQuoteFile(qd);
            dic.add(qd);
        } catch (Exception e) {
            System.out.println("등록 실패");
            e.printStackTrace();
        }

    }

    /**
     * 명언 목록 출력
     */
    public void listUp(){

        System.out.println("번호 / 작가 / 명언");
        System.out.println("--------------------");

        QuoteData buf;
        for(int i=dic.size()-1; i>= 0; i--){
            buf = dic.get(i);
            System.out.printf("%s / %s / %s\n", buf.getId(), buf.getAuthor(), buf.getContent());
        }
    }

    /**
     * 명언 삭제
     * @param cmd 입력된 커멘드 ex) 삭제#id=1
     */
    public void delete(String cmd){
        //올바른 입력 확인
        int targetId;
        try{
            targetId = Integer.parseInt(cmd.substring(6));
        }catch(Exception e){
            System.out.println("잘못된 입력 입니다");
            return;
        }

        // 존재 여부 확인
        int idx = searchIndex(targetId);

        if(idx == -1){
            System.out.printf("%d번 명언은 존재하지 않습니다.\n", targetId);
            return;
        }

        // 명언 삭제
        try {
            fm.deleteQuoteFile(targetId);
            dic.remove(idx);
            System.out.printf("%d번 명언이 삭제되었습니다.\n", targetId);
        } catch (Exception e) {
            System.out.println("삭제 실패");
            e.printStackTrace();
        }
    }


    /**
     * 명언 수정
     * @param cmd 입력된 커멘드 ex) 수정#id=1
     */
    public void update(String cmd){
        //올바른 입력 확인
        int targetId;
        try{
            targetId = Integer.parseInt(cmd.substring(6));
        }catch(Exception e){
            System.out.println("잘못된 입력 입니다");
            return;
        }

        // 존재 여부 확인
        int idx = searchIndex(targetId);

        if(idx == -1){
            System.out.printf("%d번 명언은 존재하지 않습니다.\n", targetId);
            return;
        }

        Scanner sc = new Scanner(System.in);
        boolean flag;
        String newQuote;
        String newAuthor;

        System.out.printf("명언(기존) : %s\n", dic.get(idx).getContent());

        do{
            System.out.print("명언 : ");
            newQuote = sc.nextLine();

            flag = filterString(newQuote);

            if(flag)
                System.out.println("특수문자는 입력할 수 없습니다");
        }while(flag);

        System.out.printf("작가(기존) : %s\n", dic.get(idx).getAuthor());

        do{
            System.out.print("작가 : ");
            newAuthor = sc.nextLine();

            flag = filterString(newAuthor);

            if(flag)
                System.out.println("특수문자는 입력할 수 없습니다");
        }while(flag);

        // 명언 수정
        try {
            QuoteData qd = new QuoteData(targetId, newQuote, newAuthor);
            fm.updateQuoteFile(qd);
            dic.set(idx, qd);
        } catch (Exception e) {
            System.out.println("수정 실패");
            e.printStackTrace();
        }
    }

    // -----------------------------------------------------
    // 기타 method
    // -----------------------------------------------------

    /**
     * 명언 목록을 dic에 저장
     */
    private void loadQuoteList() {
        try {
            dic.addAll(fm.readAllQuoteFiles());
        } catch (Exception e) {
            System.out.println("명언 목록 불러오기 실패");
            e.printStackTrace();
        }
    }

    /**
     * 다음 id를 가져온다.
     * @return 다음 id
     */
    public int loadNextId() {
        try {
            nextId = fm.getLastId();
        } catch (Exception e) {
            System.out.println("id 불러오기 실패");
            e.printStackTrace();
        }

        return nextId++;
    }


    /**
     * 종료 조건 확인
     * @return 종료 조건이 맞으면 true, 아니면 false
     */
    public boolean checkEndCondition(){
        return cmd.equals(CommandType.종료);
    }

    /**
     * 종료 시퀸스
     */
    public void endSequence() {
        try{
            fm.saveLastId(nextId);
        }catch (Exception e){
            System.out.println("id 저장 실패");
            e.printStackTrace();
        }
    }

    /**
     * targetId과 일치하는 dic의 요소 인덱스를 반환
     * @param targetId 찾으려하는 id 값
     * @return 일치하는 요소의 인덱스 반환 (없으면 -1 반환)
     */
    private int searchIndex(int targetId){
        for(int i=0; i<dic.size(); i++){
            if(dic.get(i).getId() == targetId)
                return i;

            else if(dic.get(i).getId() > targetId)
                break;
        }

        return -1;
    }

    /**
     * 입력받은 문자열에 특수문자가 포함됐는지 여부를 반환한다.
     * @param s 확인하려는 문자열
     * @return 특수문자가 있을 경우 true, 없을 경우 false
     */
    private boolean filterString(String s){
        String regex = "[^a-zA-Z0-9가-힣ㄱ-ㅎㅏ-ㅣ\\s]"; // 특수문자 필터링
        Pattern pattern = Pattern.compile(regex);
        Matcher matcher = pattern.matcher(s);

        return matcher.find();
    }

    public void build(){
        try {
            fm.buildDataJson(this.dic);
        } catch (Exception e) {
            System.out.println("빌드 실패");
            e.printStackTrace();
        }
    }




}
