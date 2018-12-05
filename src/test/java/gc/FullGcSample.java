package gc;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Random;

/**
 * FullGC示例:
 *     集合处理必分页：集合持有太多对象时，可将对象分批处理，因为集合持有的对象不会被回收，这样的对象过多会造成年轻代占满而部分年龄
 *                  为达到指定年龄的对象晋升到老年代引发full gc。可以使用以下方式分批处理:
 *
 *                  if(userAppMongoList.size()>threadHold){
 *                      save2MongoDB(userAppMongoList);
 *                      userAppMongoList.clear();
 *                  }
 *
 * fixme：综上，一句话：集合持有的x对象不能被young-gc晋升到old区导致full—gc，因此可以设置阈值在年轻代占满之前释放掉x对象——可被young-gc掉
 *
 * @author 杜艮魁
 * @date 2018/12/3
 */

class AppMongo {
    private String appName;
    private String packageName;
    private int versionCode;
    private Date installTime;
    private String iconUrl;
    private String downLoadUrl;
    private String remark;
    private Long size;
    private String developer;
}

class UserAppMongo {
    private String id;
    private Long userId;
    private List<AppMongo> appMongoList;

    public void setId(String id) {
        this.id = id;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public void setAppMongoList(List<AppMongo> appMongoList) {
        this.appMongoList = appMongoList;
    }
}

class AppFromMySQL {
    private int id;
    private Long userId;
    private String packageName;
    private int versionCode;
    private Date installTime;
    private String appName;
    private String iconUrl;
    private String downLoadUrl;
    private String remark;
    private Long size;
    private String developer;

    public AppFromMySQL(int id, Long userId, String packageName, int versionCode, Date installTime, String appName) {
        this.id = id;
        this.userId = userId;
        this.packageName = packageName;
        this.versionCode = versionCode;
        this.installTime = installTime;
        this.appName = appName;
    }

    public void setIconUrl(String iconUrl) {
        this.iconUrl = iconUrl;
    }

    public void setDownLoadUrl(String downLoadUrl) {
        this.downLoadUrl = downLoadUrl;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    public void setSize(Long size) {
        this.size = size;
    }

    public void setDeveloper(String developer) {
        this.developer = developer;
    }
}


public class FullGcSample {
    public static int threadHold = 500;

    public static void main(String[] args) throws InterruptedException {
        for (int pageNo = 0; pageNo < 10000; pageNo++) {
            /**
             * 获取某页的用户id*2000
             */
            List<Long> userList = getUserIdByPage(pageNo);
            /**
             * 最终需要保存到数据库中的数据
             */
            List<UserAppMongo> userAppMongoList = new ArrayList<>(userList.size());

            for (Long userId : userList) {
                /**
                 * 获取某个用户安装的app列表
                 * fixme:appFromMySQLList这个对象需要被回收，包括列表中的每个对象
                 */
                UserAppMongo userAppMongo = new UserAppMongo();
                userAppMongo.setId(System.nanoTime() + "");
                userAppMongo.setUserId(userId);
                List<AppFromMySQL> appFromMySQLList = getUserInstalledAppList(userId);
                userAppMongo.setAppMongoList(appFromMysql2AppMongo(appFromMySQLList));

                //将用户使用的app数据放进对象userAppMongo中
                userAppMongoList.add(userAppMongo);

                /**
                 * fixme:重要改进：
                 *      fixme:在年轻带占满之前将数据保存到数据库，并释放引用，让对象可被gc掉(而不用再晋升到老年代)
                 */
//                if(userAppMongoList.size()>threadHold){
//                    save2MongoDB(userAppMongoList);
//                    userAppMongoList.clear();
//                }
            }
            save2MongoDB(userAppMongoList);
        }
    }

    private static void save2MongoDB(List<UserAppMongo> userAppMongos) throws InterruptedException {
        Thread.sleep(3);
    }

    private static List<AppMongo> appFromMysql2AppMongo(List<AppFromMySQL> appFromMySQLList) {
        List<AppMongo> appMongoList = new ArrayList<>();
        for (AppFromMySQL app : appFromMySQLList) {
            AppMongo appMongo = new AppMongo();
            //todo： tranform app to appMongo:不拷贝也没关系，旧的对象被丢弃、新的对象会生成
            appMongoList.add(appMongo);
        }
        return appMongoList;
    }

    /**
     * 构造app对象并放进结果链表：
     * 获取某个用户安装的app列表
     */
    private static List<AppFromMySQL> getUserInstalledAppList(Long userId) {
        List<AppFromMySQL> appFromMySQLList = new ArrayList<>();
        int size = 50 + new Random().nextInt(150);
        for (int i = 0; i < size; i++) {
            /**
             * fixme 构造的每个对象最后都需要被回收
             */
            AppFromMySQL appFromMySQL = new AppFromMySQL(i, (long) i, "com.afei.android" + i, i, new Date(), "appName" + i);
            appFromMySQL.setIconUrl(String.valueOf(i));
            appFromMySQL.setDownLoadUrl(String.valueOf(i));
            appFromMySQL.setRemark(String.valueOf(i));
            appFromMySQL.setSize((long) i);
            appFromMySQL.setDeveloper(String.valueOf(i));
            //放进链表
            appFromMySQLList.add(appFromMySQL);
        }
        return appFromMySQLList;
    }

    private static List<Long> getUserIdByPage(int pageNo) {
        List<Long> userList = new ArrayList();
        for (int i = 0; i < 2000; i++) {
            userList.add((long) (i + 1) * pageNo);
        }
        return userList;
    }
}
