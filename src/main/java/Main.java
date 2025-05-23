import java.io.*;
import java.util.List;

import com.fasterxml.jackson.databind.ObjectMapper;

public class Main {

    public static void main(String[] args) throws IOException  {
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        String buf;

        QuoteBoard qb = new QuoteBoard();



        // -----------------------------------------------------
        System.out.println("== 명언 앱 ==");

        while(!qb.checkEndCondition()){
            // command 입력 받기
            System.out.print("명령) ");
            buf = br.readLine();

            try{
                qb.setCmd(CommandType.valueOf(buf.split("#")[0]));
            } catch (IllegalArgumentException e){
                System.out.println("잘못된 명령어입니다.");
                continue;
            }

            // command 에 따른 처리
            switch(qb.getCmd()){
                case 등록 :
                    qb.register();
                    break;
                case 삭제 :
                    qb.delete(buf);
                    break;
                case 수정 :
                    qb.update(buf);
                    break;
                case 목록 :
                    qb.listUp();
                    break;
                case 빌드 :
                    qb.build();
                    break;
                case 초기화 :
                    qb.reset();
                    break;
            }
        }

        // 종료 시퀸스
        qb.endSequence();

    }



}