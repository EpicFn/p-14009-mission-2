package com.back;
import java.util.Map;

public class Rq {
    private String command;
    private Map<String, String> params;

    public Rq(String userInput){
        String[] pars = userInput.split("\\?");

        this.command = pars[0].trim();

        // 파라미터가 없는 경우
        if(pars.length == 1){
            this.params = Map.of();
            return;
        }

        // 파라미터가 있는 경우
        this.params = Map.of();
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


    public String getCommand() {
        return command;
    }

    public Map<String, String> getParams() {
        return params;
    }

    public String getParam(String key) {
        return params.getOrDefault(key, null);
    }

}
