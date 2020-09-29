package com.taobao.xdemo;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

public class Test {


    public static Map<String, Integer> params = new TreeMap<>();  //扩展参数

    private static List<String> dataList = new ArrayList<>();

    public static Map<String, ArrayList<String>> paramsAdv = new TreeMap<>();  //扩展参数


    private static List<Integer> arrayData = new ArrayList<>();

    public static Map<Integer, Integer> paramsadddddd = new TreeMap<>();  //扩展参数

    private static ArrayList<String> temp = new ArrayList<>();


    public static void main(String[] args) {


        try {
            HashMap<Object, Object> objectObjectHashMap = new HashMap<>();

            Object taobao = objectObjectHashMap.get("taobao");

            Class<?> aClass = taobao.getClass();
        } catch (Exception e) {
            e.printStackTrace();

            System.out.println("渠道id：" + e.getMessage());

        }

        System.out.println("渠道id：ccccccc");


//        getAdvid();


        // System.out.println("dataListAdv=" + stringIntegerEntry);

//        fileList("/Users/bill/Downloads/files_10.10");
//        fileList222222("/Users/bill/Downloads/files_10.10");
    }

    private static void getAdvid() {
        dataList.add("2200558954535");
        dataList.add("2200698026644");
        dataList.add("4027831123");
        dataList.add("2200803433959");
        dataList.add("3342106326");
        dataList.add("3843640202");
        dataList.add("3234525723");
        dataList.add("3907731441");
        dataList.add("3915747229");
        dataList.add("2200606446343");

        dataList.add("2200803433965");
        dataList.add("2200803433957");


        try {
            FileReader fr = new FileReader("/Users/bill/Downloads/spmList.txt");
            BufferedReader bf = new BufferedReader(fr);
            String str;
            // 按行读取字符串
            while ((str = bf.readLine()) != null) {

                for (String s : dataList) {
                    if (str.contains(s)) {
                        // 得到spm
                        String[] split = str.split("\\.");

                        if (split.length > 3) {
                            String channelId = split[2];
                            String ids = split[3];

                            String[] split1 = ids.split("-");
                            String advId = split1[1];

                            temp.add(advId);
                            paramsAdv.put(channelId, temp);
                        }
                    }
                }
            }
            bf.close();
            fr.close();
        } catch (IOException e) {
            e.printStackTrace();
        }


        for (int i = 0; i < 10000; i++) {
            arrayData.add(0);
        }


        for (String channel : dataList) {
            ArrayList<String> advIdlidt = paramsAdv.get(channel);

            // 某个渠道  所有的广告位id
            for (int i = 0; i < advIdlidt.size(); i++) {

                if (!advIdlidt.get(i).contains("bc_fl_src")) {
                    int key = Integer.valueOf(advIdlidt.get(i));
                    int value = arrayData.get(Integer.valueOf(advIdlidt.get(i)));
                    arrayData.set(key, value + 1);
                }
            }
        }

        for (int i = 0; i < arrayData.size(); i++) {
            if (arrayData.get(i) != 0) {
                paramsadddddd.put(i, arrayData.get(i));
            }
        }

        ArrayList<Map.Entry<String, Integer>> entries = sortMap(paramsadddddd);
        ArrayList<String> listData10 = new ArrayList<>();
        for (int i = 0; i < 5; i++) {
            String key = String.valueOf(entries.get(i).getKey());
            listData10.add(key);
        }


        for (String channel : dataList) {
            ArrayList<String> advIdlidt = paramsAdv.get(channel);

            for (int i = 0; i < listData10.size(); i++) {
                String key = String.valueOf(entries.get(i).getKey());

                if (advIdlidt.contains(key)) {
                    System.out.println("渠道id：" + channel + "  广告位id: " + key + " 次数:" + entries.get(i).getValue());
                }
            }
        }
    }

    private static void fileList222222(String filePath) {
        File srcFile = new File(filePath);
        boolean bFile = srcFile.exists();

        if (!bFile || !srcFile.isDirectory() || !srcFile.canRead()) {
            try {
                srcFile.createNewFile();
            } catch (IOException e) {
                e.printStackTrace();
            }
        } else {
            try {
                File[] file = srcFile.listFiles();
                for (int i = 0; i < file.length; i++) {
                    String absolutePath = file[i].getAbsolutePath();
                    String finalStr = readString(absolutePath);

                    int i2 = finalStr.indexOf("spm");
                    if (i2 != -1) {
                        finalStr = finalStr.substring(i2, i2 + 55);

                        finalStr = finalStr.substring(4, finalStr.length() - 1);

                        String[] split = finalStr.split("\\.");

                        if (split.length > 3) {
                            String channelId = split[2];
                            String ids = split[3];

                           /* String[] split1 = ids.split("-");
                            String advId = split1[1];*/

                            String srcText = readString("/Users/bill/Downloads/spmList.txt");
                            int number = appearNumber(srcText, channelId);

                            Integer integer = params.get(channelId);
                            if (integer != null && integer > 0) {
                                params.put(channelId, integer + number);
                            } else {
                                params.put(channelId, number);
                            }

                            // System.out.println("channelId=" + channelId + "出现次数：" + number);

                            saveAsFileWriter2222("channelId=" + channelId + "出现次数：" + number, "/Users/bill/Downloads/channelList.txt");
                        }
                    }
                }

                ArrayList<Map.Entry<String, Integer>> entries = sortMap(params);
                for (int i = 0; i < 30; i++) {
                    System.out.println(entries.get(i).getKey() + ":" + entries.get(i).getValue());
                }


            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    public static ArrayList<Map.Entry<String, Integer>> sortMap(Map map) {
        List<Map.Entry<String, Integer>> entries = new ArrayList<Map.Entry<String, Integer>>(map.entrySet());
        Collections.sort(entries, new Comparator<Map.Entry<String, Integer>>() {
            public int compare(Map.Entry<String, Integer> obj1, Map.Entry<String, Integer> obj2) {
                return obj2.getValue() - obj1.getValue();
            }
        });
        return (ArrayList<Map.Entry<String, Integer>>) entries;
    }


    public static int appearNumber(String srcText, String findText) {
        int count = 0;
        int index = 0;
        while ((index = srcText.indexOf(findText, index)) != -1) {
            index = index + findText.length();
            count++;
        }
        return count;
    }

    public static void saveAsFileWriter2222(String content, String filePath) {
        FileWriter fwriter = null;
        try {
            // true表示不覆盖原来的内容，而是加到文件的后面。若要覆盖原来的内容，直接省略这个参数就好
            fwriter = new FileWriter(filePath, true);
            fwriter.write(content + "\r\n");
        } catch (IOException ex) {
            ex.printStackTrace();
        } finally {
            try {
                fwriter.flush();
                fwriter.close();
            } catch (IOException ex) {
                ex.printStackTrace();
            }
        }
    }


    //////////////////////////////////////////////////////////////////////////

    public static void fileList(String filePath) {
        File srcFile = new File(filePath);
        boolean bFile = srcFile.exists();

        if (!bFile || !srcFile.isDirectory() || !srcFile.canRead()) {
            try {
                srcFile.createNewFile();
            } catch (IOException e) {
                e.printStackTrace();
            }
        } else {
            File[] file = srcFile.listFiles();
            for (int i = 0; i < file.length; i++) {
                String absolutePath = file[i].getAbsolutePath();
                System.out.println(absolutePath);

                // 读取文件  转换成字符串  截取
                String finalStr = readString(absolutePath);

                int i2 = finalStr.indexOf("spm");

                if (i2 != -1) {
                    finalStr = finalStr.substring(i2, i2 + 55);
                    System.out.println("finalStr: " + finalStr);

                    // 写入新文件
                    saveAsFileWriter("序号:" + i + " ===  " + finalStr);
                } else {
                    System.out.println("没有spm");
                }
            }
        }
    }

    private static String filePath = "/Users/bill/Downloads/spmList.txt";

    public static void saveAsFileWriter(String content) {
        FileWriter fwriter = null;
        try {
            // true表示不覆盖原来的内容，而是加到文件的后面。若要覆盖原来的内容，直接省略这个参数就好
            fwriter = new FileWriter(filePath, true);
            fwriter.write(content + "\r\n");
        } catch (IOException ex) {
            ex.printStackTrace();
        } finally {
            try {
                fwriter.flush();
                fwriter.close();
            } catch (IOException ex) {
                ex.printStackTrace();
            }
        }
    }

    public static String readString(String path) {
        String str = "";
        File file = new File(path);
        try {
            FileInputStream in = new FileInputStream(file);
            // size 为字串的长度 ，这里一次性读完
            int size = in.available();
            byte[] buffer = new byte[size];
            in.read(buffer);
            in.close();
            str = new String(buffer, "GB2312");
        } catch (IOException e) {
            return null;
        }
        return str;
    }

}
