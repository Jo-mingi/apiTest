package co.mr;

import com.google.gson.*;

import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.io.BufferedReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class ApiTest {

    public static void main(String[] args) throws IOException {
        StringBuilder urlBuilder = new StringBuilder("http://apis.data.go.kr/1360000/TourStnInfoService/getTourStnVilageFcst"); /*URL*/
        urlBuilder.append("?" + URLEncoder.encode("serviceKey","UTF-8") + "=wILAtvV9ZsrSSMvJfpgApYwaywmj6N7juXb1Ka5q0G6wOHVa3IN9fn2sb6ubEAQNuqvQzAoNShdxtqiOCUWT7A%3D%3D"); /*Service Key*/
        urlBuilder.append("&" + URLEncoder.encode("pageNo","UTF-8") + "=" + URLEncoder.encode("1", "UTF-8")); /*페이지번호*/
        urlBuilder.append("&" + URLEncoder.encode("numOfRows","UTF-8") + "=" + URLEncoder.encode("3", "UTF-8")); /*한 페이지 결과 수*/
        urlBuilder.append("&" + URLEncoder.encode("dataType","UTF-8") + "=" + URLEncoder.encode("JSON", "UTF-8")); /*요청자료형식(XML/JSON)*/
        urlBuilder.append("&" + URLEncoder.encode("CURRENT_DATE","UTF-8") + "=" + URLEncoder.encode("2022011010", "UTF-8")); /*2016-12-01 01시부터 조회*/
        urlBuilder.append("&" + URLEncoder.encode("HOUR","UTF-8") + "=" + URLEncoder.encode("24", "UTF-8")); /*CURRENT_DATE부터 24시간 후까지의 자료 호출*/
        urlBuilder.append("&" + URLEncoder.encode("COURSE_ID","UTF-8") + "=" + URLEncoder.encode("341", "UTF-8")); /*관광 코스ID*/
        URL url = new URL(urlBuilder.toString());
        HttpURLConnection conn = (HttpURLConnection) url.openConnection();
        conn.setRequestMethod("GET");
        conn.setRequestProperty("Content-type", "application/json");
        System.out.println("Response code: " + conn.getResponseCode());
        BufferedReader rd;
        if(conn.getResponseCode() >= 200 && conn.getResponseCode() <= 300) {
            rd = new BufferedReader(new InputStreamReader(conn.getInputStream()));
        } else {
            rd = new BufferedReader(new InputStreamReader(conn.getErrorStream()));
        }
        StringBuilder sb = new StringBuilder();
        String line;
        while ((line = rd.readLine()) != null) {
            sb.append(line);
        }
        rd.close();
        conn.disconnect();

        String jsonData = sb.toString();
        // 파싱 처리
        JsonElement jsonElement = JsonParser.parseString(jsonData);

        // 자바 json 객체
        JsonObject jsonObject = jsonElement.getAsJsonObject();

        JsonObject responseObject = jsonObject.get("response").getAsJsonObject();
//        System.out.println(responseValue);

        JsonObject bodyObj = responseObject.get("body").getAsJsonObject();
//        System.out.println(bodyObj);

        JsonObject itemsObj = bodyObj.get("items").getAsJsonObject();
        JsonArray arrayData = itemsObj.get("item").getAsJsonArray();
        System.out.println(arrayData.size());

        Gson gson = new Gson();

//        https://www.jsonschema2pojo.org/
//        TourDto tourInfo = gson.fromJson(arrayData.get(0), TourDto.class);

        List<TourDto> tourInfos = new ArrayList<TourDto>();

//        이제 tourInfo. 으로 불러올 수 있다

        for(int i = 0; i < arrayData.size(); i++){
//            System.out.println(arrayData.get(i));
            TourDto tourInfo = gson.fromJson(arrayData.get(i), TourDto.class);
            tourInfos.add(tourInfo);

        }

        for(TourDto tInfo : tourInfos){
            System.out.println(tInfo.getCourseName());
            System.out.println(tInfo.getThema());
        }

//        System.out.println(sb.toString());
    }

}
