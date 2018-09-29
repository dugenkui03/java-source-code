package data;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.List;

/**
 * @Description Builder模式构造对象——在构造参数越来越多并且可选情况很多时，Builder模式适用
 * @Date 2018/9/29 下午2:48
 * -
 * @Author dugenkui
 **/

public class DataHolder {
    public List<String> uuidList;
    private String tag;

    private DataHolder(DataHolderBuilder builder){
        uuidList=builder.uuidList;
        this.tag=builder.tag;
    }

    public static DataHolderBuilder newBuilder(String path){
        return new DataHolderBuilder(path);
    }

    public static class DataHolderBuilder{
        private List<String> uuidList;
        private String tag;
        private String path;

        public DataHolderBuilder(String path){
            this.path=path;
        }

        public DataHolderBuilder tag(String tag){
            this.tag=tag;
            return this;
        }

        public DataHolder build(){
            try {
                uuidList=new ArrayList();
                File file = new File(path);
                BufferedReader fileReader = new BufferedReader(new FileReader(file));
                String tmp;
                while ((tmp = fileReader.readLine()) != null) {
                    this.uuidList.add(tmp);
                }
            } catch (Exception e) {
                System.out.println("请在指定目录添加样本数据");
            }
            return new DataHolder(this);
        }
    }
}
