package com.back.domain.quote.service;
import com.back.domain.quote.repository.FileManager;
import com.back.domain.quote.entity.QuoteData;

import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class QuoteService {
    private final ArrayList<QuoteData> dic = new ArrayList<QuoteData>(); //[quote, author, id]
    private int nextId = 1; // 다음 id

    private FileManager fm = new FileManager(); // 파일 관리 객체

    // -----------------------------------------------------
    // constructor
    // -----------------------------------------------------

    public QuoteService() {
    }

    // -----------------------------------------------------
    // getter, setter
    // -----------------------------------------------------


    public int getNextId() {
        return nextId;
    }

    public void setNextId(int nextId) {
        this.nextId = nextId;
    }

    public ArrayList<QuoteData> getDic() {
        return dic;
    }

    // -----------------------------------------------------
    // CRUD method
    // -----------------------------------------------------

    /**
     * 명언 등록
     */
    public void register(String quote, String author) throws Exception {
        // 예외 처리
        if (filterString(quote) || filterString(author)) {
            return;
        }

        // 명언 등록 (파일 생성 에러시 Exception 발생)
        QuoteData qd = new QuoteData(nextId, quote, author);
        dic.add(qd);
        fm.createQuoteFile(qd); // 파일 생성
        nextId++;
    }

    /**
     * 명언 삭제
     * @param targetId 삭제할 명언의 id
     * @param idx 삭제할 명언의 인덱스
     */
    public void delete(int targetId, int idx) throws Exception {
        fm.deleteQuoteFile(targetId); // 파일 삭제
        dic.remove(idx); // 리스트에서 삭제
    }


    /**
     * 명언 수정
     * @param targetId 수정할 명언의 id
     * @param idx 수정할 명언의 인덱스
     * @param newQuote 새 명언
     * @param newAuthor 새 작가
     */
    public void update(int targetId, int idx, String newQuote, String newAuthor) throws Exception {
        QuoteData qd = new QuoteData(targetId, newQuote, newAuthor);
        fm.updateQuoteFile(qd); // 파일 수정
        dic.set(idx, qd);
    }



    // ---------------------------------------------------------
    // 기타 method
    // ---------------------------------------------------------

    /**
     * 명언 목록 불러오기
     */
    public void loadQuoteList() throws Exception {
        dic.addAll(fm.readAllQuoteFiles());
    }

    /**
     * 다음 id를 가져온다.
     * @return 다음 id
     */
    public void loadNextId() throws Exception {
        nextId = fm.getLastId() + 1;
    }

    /**
     * 마지막 ID를 저장한다
     */
    public void saveLastId() throws Exception {
        fm.saveLastId(nextId-1);
    }

    /**
     * targetId과 일치하는 dic의 요소 인덱스를 반환
     * @param targetId 찾으려하는 id 값
     * @return 일치하는 요소의 인덱스 반환 (없으면 -1 반환)
     */
    public int searchIndex(int targetId){
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
    public boolean filterString(String s){
        String regex = "[^a-zA-Z0-9가-힣ㄱ-ㅎㅏ-ㅣ\\s]"; // 특수문자 필터링
        Pattern pattern = Pattern.compile(regex);
        Matcher matcher = pattern.matcher(s);

        return matcher.find();
    }

    public void build() throws Exception{
        fm.buildDataJson(this.dic);
    }

    public void reset() throws Exception{
        fm.resetDatas();
        dic.clear();
        nextId = 1;
    }




}
