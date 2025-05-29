package com.back;

import com.back.domain.quote.controller.QuoteController;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

public class App {
    public void run() throws IOException {
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        String buf;


        QuoteController qb = new QuoteController();


        qb.startSequence();

        while(!qb.checkEndCondition()){
            // 사용자 입력 받기
            if(!qb.getUserInput())
                continue; // 입력이 잘못되면 다시 입력 받기

            // command 에 따른 처리
            switch(qb.getRq().getCommand()) {
                case 등록 :
                    qb.register();
                    break;
                case 삭제 :
                    qb.delete();
                    break;
                case 수정 :
                    qb.update();
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
