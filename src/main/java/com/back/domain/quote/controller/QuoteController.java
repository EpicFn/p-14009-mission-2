package com.back.domain.quote.controller;

import com.back.domain.quote.component.CommandType;
import com.back.domain.quote.entity.QuoteData;
import com.back.domain.quote.service.QuoteService;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Scanner;


public class QuoteController {

    // -----------------------------------------------------
    // attribute
    // -----------------------------------------------------

    private QuoteService service = new QuoteService(); // 서비스 객체
    private Rq rq = new Rq(); // 요청 객체

    Scanner sc = new Scanner(System.in);


    // -----------------------------------------------------
    // constructor
    // -----------------------------------------------------

    public QuoteController(){
    }

    // -----------------------------------------------------
    // getter, setter
    // -----------------------------------------------------

    public Rq getRq() {
        return rq;
    }

    public void setRq(Rq rq) {
        this.rq = rq;
    }

    // -----------------------------------------------------
    // COMMAND method
    // -----------------------------------------------------

    /**
     * 명언 등록
     */
    public void register() {
        String quote;
        String author;
        boolean flag;

        do{
            System.out.print("명언 : ");
            quote = sc.nextLine();

            flag = service.filterString(quote);

            if(flag)
                System.out.println("특수문자는 입력할 수 없습니다");
        }while(flag);

        do{
            System.out.print("작가 : ");
            author = sc.nextLine();

            flag = service.filterString(author);

            if(flag)
                System.out.println("특수문자는 입력할 수 없습니다");
        }while(flag);


        // 명언 등록
        try {
            service.register(quote, author);
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

        ArrayList<QuoteData> dic = service.getDic(); // 명언 목록 가져오기
        QuoteData buf;
        for(int i=dic.size()-1; i>= 0; i--){
            buf = dic.get(i);
            System.out.printf("%s / %s / %s\n", buf.getId(), buf.getAuthor(), buf.getContent());
        }
    }

    /**
     * 명언 삭제
     */
    public void delete(){
        // id 파라미터가 있는지 확인
        if(!rq.hasParam("id")){
            System.out.println("잘못된 입력 입니다");
            return;
        }

        //올바른 입력 확인
        int targetId;
        try{
            targetId = Integer.parseInt(rq.getParam("id"));
        }catch(Exception e){
            System.out.println("잘못된 입력 입니다");
            return;
        }

        // 존재 여부 확인
        int idx = service.searchIndex(targetId);
        if(idx == -1){
            System.out.printf("%d번 명언은 존재하지 않습니다.\n", targetId);
            return;
        }

        // 명언 삭제
        try {
            service.delete(targetId, idx);
            System.out.printf("%d번 명언이 삭제되었습니다.\n", targetId);
        } catch (Exception e) {
            System.out.println("삭제 실패");
            e.printStackTrace();
        }
    }


    /**
     * 명언 수정
     */
    public void update(){
        // id 파라미터가 있는지 확인
        if(!rq.hasParam("id")){
            System.out.println("잘못된 입력 입니다");
            return;
        }

        //올바른 입력 확인
        int targetId;
        try{
            targetId = Integer.parseInt(rq.getParam("id"));
        }catch(Exception e){
            System.out.println("잘못된 입력 입니다");
            return;
        }

        // 존재 여부 확인
        int idx = service.searchIndex(targetId);
        if(idx == -1){
            System.out.printf("%d번 명언은 존재하지 않습니다.\n", targetId);
            return;
        }


        // 새 값 입력받기
        boolean flag;
        String newQuote;
        String newAuthor;

        System.out.printf("명언(기존) : %s\n", service.getDic().get(idx).getContent());

        do{
            System.out.print("명언 : ");
            newQuote = sc.nextLine();

            flag = service.filterString(newQuote);

            if(flag)
                System.out.println("특수문자는 입력할 수 없습니다");
        }while(flag);

        System.out.printf("작가(기존) : %s\n", service.getDic().get(idx).getAuthor());

        do{
            System.out.print("작가 : ");
            newAuthor = sc.nextLine();

            flag = service.filterString(newAuthor);

            if(flag)
                System.out.println("특수문자는 입력할 수 없습니다");
        }while(flag);

        // 명언 수정
        try {
            service.update(targetId, idx, newQuote, newAuthor);
        } catch (Exception e) {
            System.out.println("수정 실패");
            e.printStackTrace();
        }
    }

    public void build(){
        try {
            service.build();
            System.out.println("data.json 파일의 내용이 갱신되었습니다.");
        } catch (Exception e) {
            System.out.println("빌드 실패");
            e.printStackTrace();
        }
    }

    public void reset() {
        try {
            service.reset();
            System.out.println("초기화 완료");
        } catch (Exception e) {
            System.out.println("초기화 실패");
            e.printStackTrace();
        }
    }

    // -----------------------------------------------------
    // 기타 method
    // -----------------------------------------------------

    public boolean getUserInput(){
        System.out.print("명령) ");
        String buf = sc.nextLine().trim();

        try {
            rq.parse(buf); // 명령어 파싱
        } catch (IllegalArgumentException e) {
            System.out.println("잘못된 명령어입니다.");
            return false;
        }

        return true;
    }



    /**
     * 명언 목록, id 불러오기
     */
    private void loadData() {
        try {
            service.loadQuoteList(); // 명언 목록 불러오기
            service.loadNextId(); // 다음 id 불러오기
        } catch (Exception e) {
            System.out.println("데이터 불러오기 실패");
            e.printStackTrace();
        }
    }


    /**
     * 종료 조건 확인
     * @return 종료 조건이 맞으면 true, 아니면 false
     */
    public boolean checkEndCondition(){
        return rq.getCommand().equals(CommandType.종료);
    }



    public void startSequence() {

        loadData(); // 데이터 불러오기
        rq.setCommand(CommandType.시작); // 초기 명령어 설정

        System.out.println("== 명언 앱 시작 ==");
    }

    /**
     * 종료 시퀸스
     */
    public void endSequence() {
        try{
            service.saveLastId(); // 마지막 id 저장
        }catch (Exception e){
            System.out.println("id 저장 실패");
            e.printStackTrace();
        }
    }






}
