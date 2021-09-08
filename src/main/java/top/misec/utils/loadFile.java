package top.misec.utils;

import java.io.*;
import java.nio.charset.StandardCharsets;

import lombok.extern.log4j.Log4j2;

/**
 * @author junzhou
 */
@Log4j2
public class loadFile {
    /**
     * 从外部资源读取配置文件
     *
     * @return config
     */
    public static String loadConfigJsonFromFile(String fileName) {
        String config = null;
        try {
            String outPath = System.getProperty("user.dir") + File.separator + fileName + File.separator;
            InputStream is = new FileInputStream(outPath);
            int size = is.available();
            byte[] buffer = new byte[size];
            is.read(buffer);
            is.close();
            config = new String(buffer, StandardCharsets.UTF_8);
        } catch (FileNotFoundException e) {
            log.info("没有找到{}配置文件，请将其放置到jar包所在目录", fileName);
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
            log.debug("", e);
        } finally {
            log.info("读取{}文件成功", fileName);
        }
        return config;
    }

    public static void main(String[] args) {
        loadConfigJsonFromFile("config.json");
    }

}
