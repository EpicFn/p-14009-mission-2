import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;

public class FileManager {
    static final private String dbPath = "db/wiseSaying"; // 폴더 경로
    static final private String lastIdPath = "db/wiseSaying/lastId.txt"; // 마지막 id 저장 경로
    private ObjectMapper mapper = new ObjectMapper(); // JSON 변환 객체

    /**
     * 명언.json 파일 생성
     * @param qd 명언 데이터
     * @throws Exception
     */
    public void createQuoteFile(QuoteData qd) throws Exception {
        mapper.writerWithDefaultPrettyPrinter()
                .writeValue(new File(dbPath + "/" + qd.getId() + ".json"), qd);
    }

    /**
     * 명언.json 파일 삭제
     * @param id 삭제할 명언의 id
     * @throws Exception
     */
    public void deleteQuoteFile(int id) throws Exception {
        File file = new File(dbPath + "/" + id + ".json");
        if (file.exists() && file.delete()) {
            System.out.println("파일이 삭제되었습니다.");
        } else {
            System.out.println("파일이 존재하지 않습니다.");
        }
    }

    /**
     * 명언.json 파일 읽기
     * @param id 읽을 명언의 id
     * @return 명언 데이터
     * @throws Exception
     */
    public QuoteData readQuoteFile(int id) throws Exception {
        return mapper.readValue(new File(dbPath + "/" + id + ".json"), QuoteData.class);
    }

    /**
     * 명언.json 파일 수정
     * @param qd 수정할 명언 데이터
     * @throws Exception
     */
    public void updateQuoteFile(QuoteData qd) throws Exception {
        mapper.writerWithDefaultPrettyPrinter()
                .writeValue(new File(dbPath + "/" + qd.getId() + ".json"), qd);
    }


    /**
     * 모든 명언.json 파일 읽기
     *
     * @return 명언 데이터 배열
     * @throws Exception
     */
    public Collection<? extends QuoteData> readAllQuoteFiles() throws Exception {
        File folder = new File(dbPath);
        File[] files = folder.listFiles((dir, name) -> name.endsWith(".json"));
        if (files == null) {
            return Arrays.asList(new QuoteData[0]);
        }
        QuoteData[] quotes = new QuoteData[files.length];
        for (int i = 0; i < files.length; i++) {
            quotes[i] = readQuoteFile(Integer.parseInt(files[i].getName().replace(".json", "")));
        }
        return Arrays.asList(quotes);
    }

    /**
     * 마지막 id 저장
     * @return 마지막 id
     * @throws Exception
     */
    public void saveLastId(int lastId) throws Exception {
        try(BufferedWriter bw = new BufferedWriter(new FileWriter(lastIdPath))) {
            bw.write(String.valueOf(lastId));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    /**
     * 마지막 id 읽기
     * @return 마지막 id
     * @throws Exception
     */
    public int getLastId() throws Exception {
        File file = new File(lastIdPath);
        if (!file.exists()) {
            return 0;
        }
        List<String> lines = java.nio.file.Files.readAllLines(file.toPath());
        return Integer.parseInt(lines.get(0));
    }


}
