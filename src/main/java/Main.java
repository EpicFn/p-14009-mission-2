import java.io.*;
import com.fasterxml.jackson.databind.ObjectMapper;

public class Main {

    public static void main(String[] args) throws IOException  {
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        String buf;

        //QuoteBoard qb = new QuoteBoard();

        ObjectMapper om = new ObjectMapper();
        String json = "{\"quote\":\"필생즉사 필사즉생\", \"author\":\"이순신\", \"id\": 1}";
        QuoteData qd = om.readValue(json, QuoteData.class);

        qd.printQuote();

        // -----------------------------------------------------
//        System.out.println("== 명언 앱 ==");
//
//        while(!qb.checkEndCondition()){
//            // command 입력 받기
//            System.out.print("명령) ");
//            buf = br.readLine();
//
//            qb.setCmd(CommandType.valueOf(buf.substring(0,2)));
//
//            // command 에 따른 처리
//            switch(qb.getCmd()){
//                case 등록 :
//                    qb.register();
//                    break;
//                case 삭제 :
//                    qb.delete(buf);
//                    break;
//                case 수정 :
//                    qb.update(buf);
//                    break;
//                case 목록 :
//                    qb.listUp();
//                    break;
//            }
//        }

    }



}