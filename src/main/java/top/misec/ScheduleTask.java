package top.misec;


import cn.hutool.http.Header;
import cn.hutool.http.HttpRequest;
import cn.hutool.http.HttpStatus;
import cn.hutool.http.HttpUtil;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import lombok.extern.log4j.Log4j2;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import top.misec.utils.loadFile;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author junzhou
 */

@Log4j2
@Component
@EnableScheduling
public class ScheduleTask {
    @Scheduled(cron = "0 40,10 10,14 * * 1,2,3,4,5")
    public static void getFundValueInfo() {
        HashMap<String, String> hashMap = new HashMap<>(16);
        String json = loadFile.loadConfigJsonFromFile("config.json");
        JSONObject jsonObject = JSON.parseObject(json);
        JSONArray fundList = jsonObject.getJSONArray("fundList");
        List<Integer> arrayList = fundList.toJavaList(Integer.class);

        for (Integer integer : arrayList) {
            long timestamp = System.currentTimeMillis();
            String fundCode = integer.toString();
            String url = "https://fundgz.1234567.com.cn/js/";
            String param = fundCode + ".js?rt=" + timestamp;
            String result = HttpUtil.get(url + param).replace("jsonpgz(", "").replace(");", "");
            JSONObject jsonResult = JSON.parseObject(result);
            log.info(jsonResult);
            hashMap.put(jsonResult.getString("name"), jsonResult.getString("gszzl"));
        }
        log.info(hashMap);

        sendMsg(hashMap);

    }

    public static void sendMsg(HashMap<String, String> hashMap) {

        String json = loadFile.loadConfigJsonFromFile("pushconfig.json");
        JSONObject jsonObject = JSON.parseObject(json);

        String pushUrl = jsonObject.getString("server");

        JSONArray pushArray = jsonObject.getJSONArray("key");

        List<String> list = JSON.parseArray(pushArray.toString(), String.class);

        StringBuilder pushContent = new StringBuilder();

        for (Map.Entry<String, String> entry : (hashMap).entrySet()) {
            String key = entry.getKey();
            String value = entry.getValue();
            pushContent.append(key).append(" ：").append(value).append("%\n");
        }
        for (String deviceKey : list) {


            JSONObject postBody = new JSONObject();
            postBody.put("device_key", deviceKey);
            postBody.put("title", "基金涨跌提醒");
            postBody.put("body", pushContent.toString());
            String res = HttpRequest.post(pushUrl)
                    .header(Header.CONTENT_TYPE, "application/json; charset=utf-8")
                    .body(postBody.toString())
                    .execute()
                    .body();

            JSONObject result = JSON.parseObject(res);

            if (result.getInteger("code") == HttpStatus.HTTP_OK) {
                log.info("推送成功");
            }
        }

    }

    public static void main(String[] args) {
        getFundValueInfo();
    }
}
