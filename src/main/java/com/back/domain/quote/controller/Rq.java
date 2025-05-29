package com.back.domain.quote.controller;
import com.back.domain.quote.component.CommandType;

import java.util.HashMap;
import java.util.Map;

public class Rq {
    private CommandType command;
    private Map<String, String> params;

    public Rq() {}

    /**
     * 사용자 입력을 파싱하여 명령어와 파라미터를 설정합니다.
     * @param userInput 사용자 입력 문자열
     * @throws IllegalArgumentException 잘못된 명령어 형식일 경우
     */
    public void parse(String userInput) throws IllegalArgumentException {
        String[] pars = userInput.split("\\?");

        // 명령어 처리
        try{
            command = CommandType.valueOf(pars[0].trim());
        } catch (IllegalArgumentException e) {
            command = CommandType.시작;
            throw new IllegalArgumentException("잘못된 명령어입니다: " + pars[0].trim());
        }

        // 파라미터가 없는 경우
        if(pars.length == 1){
            this.params = Map.of();
            return;
        }

        // 파라미터가 있는 경우
        this.params = new HashMap<>();
        String[] paramPairs = pars[1].split("&");
        for(String pair : paramPairs){
            String[] keyValue = pair.split("=");
            if(keyValue.length == 2){
                String key = keyValue[0].trim();
                String value = keyValue[1].trim();
                this.params.put(key, value);
            }
        }
    }

    public void setCommand(CommandType command) {
        this.command = command;
    }

    public CommandType getCommand() {
        return command;
    }

    public Map<String, String> getParams() {
        return params;
    }

    public String getParam(String key) {
        return params.getOrDefault(key, null);
    }

    public String getParamOrDefault(String key, String defaultValue) {
        return params.getOrDefault(key, defaultValue);
    }

    public boolean hasParam(String key) {
        return params.containsKey(key);
    }



}
