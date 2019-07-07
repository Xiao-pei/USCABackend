package xiaopei.bigdata.Service;

import org.apache.spark.SparkConf;
import org.apache.spark.api.java.JavaPairRDD;
import org.apache.spark.api.java.JavaSparkContext;
import org.apache.spark.api.java.function.Function;
import org.apache.spark.broadcast.Broadcast;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

@Service
public class SparkService {
    public static Map<String, Double> job_recommend(String str, JavaSparkContext sc) {


        SparkConf conf = new SparkConf().setMaster("local").setAppName("Recommend");

        //JavaSparkContext sc = new JavaSparkContext(conf);

        final Broadcast<HashMap<String, Double>> skills = sc.broadcast(get_skills(
                str));

        System.out.println(skills.getValue());


        JavaPairRDD<String, String> unprocessed_weight = sc.wholeTextFiles("./Data/");

        JavaPairRDD<String, HashMap<String, Double>> weight = unprocessed_weight.mapValues(new Function<String, HashMap<String, Double>>() {
            @Override
            public HashMap<String, Double> call(String s) throws Exception {

                HashMap<String, Double> map = new HashMap<>();

                String[] tmp1 = s.split("\n");
                for (String str : tmp1) {

                    String[] tmp2 = str.split(",");
                    map.put(tmp2[0], Double.valueOf(tmp2[1]));

                }
                return map;
            }
        });

        JavaPairRDD<String, Double> score = weight.mapValues(new Function<HashMap<String, Double>, Double>() {
            @Override
            public Double call(HashMap<String, Double> stringDoubleHashMap) throws Exception {

                double skill_score = 0;

                Iterator iter2 = stringDoubleHashMap.entrySet().iterator();
                while (iter2.hasNext()) {

                    Map.Entry entry = (Map.Entry) iter2.next();
                    Object val = entry.getValue();
                    Object key = entry.getKey();

                    if (skills.getValue().containsKey(key.toString())) {

                        double level = skills.getValue().get(key.toString());

                        skill_score += (level * level * (double) val);

                    }

                }

                //System.out.println(skill_score);
                return skill_score;
            }
        });

        Map<String, Double> job_score = score.collectAsMap();

        return job_score;


    }

    public static HashMap<String, Double> get_skills(String str) {

        HashMap<String, Double> map = new HashMap<>();

        String[] tmp = str.split("\n");

        for (String tmp2 : tmp) {

            String[] tmp3 = tmp2.split(",");

            map.put(tmp3[0], Double.valueOf(tmp3[1]));

        }

        return map;

    }

   /* public static String get_job(Map<String,Double> map){

        double max = 0;
        String max_job = "";
        Iterator iter2 = map.entrySet().iterator();
        while (iter2.hasNext()) {

            Map.Entry entry = (Map.Entry) iter2.next();
            Object val = entry.getValue();
            Object key = entry.getKey();

            if((double)val>max){
                max = (double)val;
                max_job = (String)key;
            }

        }

        return max_job;
    }*/

    public static HashMap<String, Double> get_scores(Map<String, Double> map) {

        HashMap<String, Double> new_map = new HashMap<>();


        Iterator iter2 = map.entrySet().iterator();
        while (iter2.hasNext()) {

            Map.Entry entry = (Map.Entry) iter2.next();
            Object val = entry.getValue();
            Object key = entry.getKey();

            String str = converse((String) key);

            new_map.put(str, (double) val);

        }

        return new_map;
    }

    public static String converse(String str) {

        String[] tmp1 = str.split("/");

        String[] tmp2 = tmp1[tmp1.length - 1].split("_");

        String result = "";

        System.out.println(tmp2[0]);

        switch (tmp2[0]) {

            case "ALGORITHM.csv":

                result = "算法工程师";
                break;

            case "BACKEND.csv":

                result = "后端工程师";
                break;

            case "BIGDATA.csv":

                result = "大数据工程师";
                break;

            case "BLOCKCHAIN.csv":
                result = "区块链工程师";
                break;

            case "CPP.csv":
                result = "C++开发工程师";
                break;

            case "DATAMINING.csv":
                result = "数据挖掘工程师";
                break;

            case "FRONTEND.csv":
                result = "前端工程师";
                break;

            case "GAME.csv":
                result = "游戏开发工程师";
                break;

            case "IOT.csv":
                result = "物联网工程师";
                break;

            case "JAVA.csv":
                result = "Java开发工程师";
                break;

            case "MOBILE.csv":
                result = "移动开发工程师";
                break;

            case "NLP.csv":
                result = "自然语言处理工程师";
                break;

            case "OPERATION.csv":
                result = "运维工程师";
                break;

            case "PHP.csv":
                result = "PHP开发工程师";
                break;

            case "PM.csv":
                result = "项目经理";
                break;

            case "SECURITY.csv":
                result = "信息安全工程师";
                break;

            case "TEST.csv":
                result = "软件测试工程师";
                break;

            case "UI.csv":
                result = "UI设计师";
                break;


        }

        return result;
    }

}
