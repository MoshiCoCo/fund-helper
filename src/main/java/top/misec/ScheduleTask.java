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

import java.util.*;

/**
 * @author junzhou
 */

@Log4j2
@Component
@EnableScheduling
public class ScheduleTask {

    private final static int BATCH_SIZE = 4;

    @Scheduled(cron = "0 40,10 10,14 * * 1,2,3,4,5")
    public static void getFundValueInfo() {
        List<String> pushContentList = new ArrayList<>();
        LinkedList<List<String>> pushContentBatchList = new LinkedList<>();
        String json = loadFile.loadConfigJsonFromFile("config.json");
        JSONObject jsonObject = JSON.parseObject(json);
        JSONArray fundList = jsonObject.getJSONArray("fundList");
        List<String> arrayList = fundList.toJavaList(String.class);
        int count = 0;
        for (String code : arrayList) {
            long timestamp = System.currentTimeMillis();
            String url = "https://fundgz.1234567.com.cn/js/";
            String param = code + ".js?rt=" + timestamp;
            String result = HttpUtil.get(url + param).replace("jsonpgz(", "").replace(");", "");
            JSONObject jsonResult = JSON.parseObject(result);
            log.info(jsonResult);
            pushContentList.add(String.format("%s. %s ：%s%%\n", ++count, jsonResult.getString("name"), jsonResult.getString("gszzl")));
            // 手机上消息推送，先到的会展示在下方，后到的展示在上方，为了保持顺序，故而需要倒序推送
            if (count % BATCH_SIZE == 0) {
                pushContentBatchList.addFirst(pushContentList);
                pushContentList = new ArrayList<>();
            }
        }
        if (!pushContentList.isEmpty()) {
            pushContentBatchList.addFirst(pushContentList);
        }
        for (List<String> contentList : pushContentBatchList) {
            sendMsg(contentList, jsonObject);
        }

    }

    public static void sendMsg(List<String> pushContentList, JSONObject jsonObject) {

        //获取推送服务器地址
        String pushUrl = jsonObject.getString("server");

        //获取推送设备列表
        JSONArray pushArray = jsonObject.getJSONArray("key");
        List<String> pushList = JSON.parseArray(pushArray.toString(), String.class);
        //推送内容
        StringBuilder pushContent = new StringBuilder();

        pushContentList.forEach(pushContent::append);

        for (String deviceKey : pushList) {
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
