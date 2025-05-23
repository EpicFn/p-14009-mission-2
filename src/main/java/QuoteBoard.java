import java.util.ArrayList;
import java.util.Scanner;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

enum CommandType {
    종료, 등록, 삭제, 수정, 목록, 시작
}

public class QuoteBoard {

    // -----------------------------------------------------
    // attribute
    // -----------------------------------------------------

    private CommandType cmd;
    private final ArrayList<String[]> dic; //[quote, author, id]
    private int nextId = 1;

    // -----------------------------------------------------
    // constructor
    // -----------------------------------------------------

    QuoteBoard(){
        cmd = CommandType.시작;
        dic = new ArrayList<String[]>();
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


        System.out.printf("%d번 명언이 등록되었습니다\n", nextId);

        dic.add(new String[]{quote, author, String.valueOf(nextId++)});
    }

    public void listUp(){

        System.out.println("번호 / 작가 / 명언");
        System.out.println("--------------------");

        String[] buf;
        for(int i=dic.size()-1; i>= 0; i--){
            buf = dic.get(i);
            System.out.printf("%s / %s / %s\n", buf[2], buf[1], buf[0]);
        }
    }

    public void delete(String buf){
        //올바른 입력 확인
        int targetId;
        try{
            targetId = Integer.parseInt(buf.substring(6));
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

        dic.remove(idx);
        System.out.printf("%d번 명언이 삭제되었습니다.\n", targetId);
    }

    public void update(String buf){
        //올바른 입력 확인
        int targetId;
        try{
            targetId = Integer.parseInt(buf.substring(6));
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


        System.out.printf("명언(기존) : %s\n", dic.get(idx)[0]);

        do{
            System.out.print("명언 : ");
            newQuote = sc.nextLine();

            flag = filterString(newQuote);

            if(flag)
                System.out.println("특수문자는 입력할 수 없습니다");
        }while(flag);

        System.out.printf("작가(기존) : %s\n", dic.get(idx)[1]);

        do{
            System.out.print("작가 : ");
            newAuthor = sc.nextLine();

            flag = filterString(newAuthor);

            if(flag)
                System.out.println("특수문자는 입력할 수 없습니다");
        }while(flag);


        dic.set(idx, new String[]{newQuote, newAuthor, String.valueOf(targetId)});

    }

    // -----------------------------------------------------
    // 기타 method
    // -----------------------------------------------------


    // 종료 조건 확인
    public boolean checkEndCondition(){
        return cmd.equals(CommandType.종료);
    }

    /**
     * targetId과 일치하는 dic의 요소 인덱스를 반환
     * @param targetId 찾으려하는 id 값
     * @return 일치하는 요소의 인덱스 반환 (없으면 -1 반환)
     */
    private int searchIndex(int targetId){
        for(int i=0; i<dic.size(); i++){
            if(Integer.parseInt(dic.get(i)[2]) == targetId)
                return i;

            else if(Integer.parseInt(dic.get(i)[2]) > targetId)
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




}
