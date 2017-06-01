package cheweibao.jason.com.carPlace.service;

import android.util.Log;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import cheweibao.jason.com.carPlace.Bean.carPlace;
import cheweibao.jason.com.carPlace.Bean.user;
import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.FindListener;
import cn.bmob.v3.listener.QueryListener;
import cn.bmob.v3.listener.SaveListener;
import cn.bmob.v3.listener.UpdateListener;

import static cn.bmob.v3.Bmob.getApplicationContext;

/**
 * Created by JasonSong on 2017/4/19.
 */

public class userService {
    static List<String> applyedList = new ArrayList();
    static List<String> publishedList = new ArrayList();
    static String registerResult = "0";
    static String loginResult = "0";
    static Boolean updateResult = false;
    static String userId;
    private static boolean isExsitResult = true;

    /**
     * 查看已申请的车位
     *
     * @param Id
     * @return
     */
    public static List applyedPlaces(String Id) {

        BmobQuery<user> query = new BmobQuery<user>();
        query.addQueryKeys("usePlaces");
        query.addQueryKeys("usePlace");
        query.getObject(Id, new QueryListener<user>() {
            @Override
            public void done(user object, BmobException e) {
                if (e == null) {
                    if (object.getUsePlace()) {
                        applyedList = object.getUsePlaces();
//                        Toast.makeText(getApplicationContext(), "查看成功！",
//                                Toast.LENGTH_SHORT).show();
                    }
//                    else {
//                        Toast.makeText(getApplicationContext(), "没有申请车位！",
//                                Toast.LENGTH_LONG).show();
//                    }
                } else {
                    Toast.makeText(getApplicationContext(), "查看失败！",
                            Toast.LENGTH_LONG).show();
                    Log.i("bmob", "查看失败：" + e.getMessage() + "," + e.getErrorCode());
                }
            }
        });

        return applyedList;
    }

    /**
     * 查看已发布的车位
     *
     * @param Id
     * @return
     */
    public static List publishedPlaces(String Id) {
        BmobQuery<user> query = new BmobQuery<user>();
        query.addQueryKeys("havePlaces");
        query.addQueryKeys("havePlace");
        query.getObject(Id, new QueryListener<user>() {
            @Override
            public void done(user object, BmobException e) {
                if (e == null) {
                    if (object.getHavePlace()) {
                        publishedList = object.getHavePlaces();
//                        Toast.makeText(getApplicationContext(), "查看成功！",
//                                Toast.LENGTH_SHORT).show();
                    }
//                    else {
//                        Toast.makeText(getApplicationContext(), "没有发布车位！",
//                                Toast.LENGTH_LONG).show();
//                    }
                } else {
                    Toast.makeText(getApplicationContext(), "查看失败！",
                            Toast.LENGTH_LONG).show();
                    Log.i("bmob", "查看失败：" + e.getMessage() + "," + e.getErrorCode());
                }
            }
        });
        return publishedList;
    }


    public static Map login(String name, final String password) {
        BmobQuery<user> query = new BmobQuery<user>();
        query.addWhereEqualTo("name", name);
        query.findObjects(new FindListener<user>() {
            @Override
            public void done(List<user> object, BmobException e) {
                if (e == null) {
                    if (object.size() != 0 && password.equals(object.get(0).getPassword())) {
                        userId = object.get(0).getObjectId();
                        Toast.makeText(getApplicationContext(), "登录成功！",
                                Toast.LENGTH_SHORT).show();
                        loginResult = "1";
                    } else if (!password.equals(object.get(0).getPassword())) {
//                        Toast.makeText(getApplicationContext(), "password:"+password+"object.get(0)"+object.get(0),
//                                Toast.LENGTH_LONG).show();
                        Toast.makeText(getApplicationContext(), "登录失败：密码不正确！",
                                Toast.LENGTH_LONG).show();
                    } else {
                        Toast.makeText(getApplicationContext(), "登录失败：用户不存在！",
                                Toast.LENGTH_LONG).show();
                    }

                } else {
                    Toast.makeText(getApplicationContext(), "登录失败" + e.getMessage() + e.toString(),
                            Toast.LENGTH_LONG).show();
                    Log.i("bmob", "失败：" + e.getMessage() + "," + e.getErrorCode());
                }
            }
        });
        Map<String, String> resultMap = new HashMap<String, String>();
        resultMap.put("loginResult", loginResult);
        resultMap.put("userId", userId);
        return resultMap;
    }

    public static String register(final String name, final String password) {
//        BmobQuery<user> query = new BmobQuery<user>();
//        query.addQueryKeys(name);
//        query.addWhereEqualTo("name", name);
//        query.findObjects(new FindListener<user>() {
//            @Override
//            public void done(List<user> object, BmobException e) {
//                if (e == null) {
//                    if (object.size() >= 1) {
//                        Toast.makeText(getApplicationContext(), "该用户已存在！",
//                                Toast.LENGTH_LONG).show();
//                        registerResult = "2";
//                    } else {
        user user = new user();
        user.setName(name);
        user.setPassword(password);
        user.setHavePlace(false);
        user.setUsePlace(false);
        user.save(new SaveListener<String>() {
            @Override
            public void done(String objectId, BmobException e) {
                if (e == null) {
                    Toast.makeText(getApplicationContext(), "注册成功！",
                            Toast.LENGTH_SHORT).show();
                    registerResult = "1";
                    Log.i("bmob", "注册成功：" + objectId);
                } else {
                    Toast.makeText(getApplicationContext(), "注册失败！：" + e.getMessage() + "," + e.getErrorCode(),
                            Toast.LENGTH_LONG).show();
                    Log.i("bmob", "失败：" + e.getMessage() + "," + e.getErrorCode());
                }
            }
        });
//                    }
//                } else {
//                    Toast.makeText(getApplicationContext(), "查询失败：" + e.getMessage() + e.toString(),
//                            Toast.LENGTH_LONG).show();
//                    Log.i("bmob", "失败：" + e.getMessage() + "," + e.getErrorCode());
//                }
//            }
//        });
//        try {
//            Thread.sleep(500);
//            return registerResult;
//        } catch (InterruptedException e) {
//            e.printStackTrace();
//        }
        return registerResult;
    }

    public static void update(String userId, String name,  String password, String city, String phone, String carNum, String sex, String nickname) {
        user user = new user();
        user.setName(name);
        user.setPassword(password);
        user.setCity(city);
        user.setPhone(phone);
        user.setCarNum(carNum);
        user.setSex(sex);
        user.setNickname(nickname);
        user.update(userId, new UpdateListener() {
            @Override
            public void done(BmobException e) {
                if (e == null) {
                    Toast.makeText(getApplicationContext(), "修改成功！",
                            Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(getApplicationContext(), "修改失败" + e.getMessage() + e.toString(),
                            Toast.LENGTH_LONG).show();
                    Log.i("bmob", "失败：" + e.getMessage() + "," + e.getErrorCode());
                }
            }

        });

    }

//    public static Map registerAndLogin(String name, final String password) {
//        String userId = "";
//        String result = "0";
//        Map<String, String> resultMap = new HashMap<String, String>();
//        String registerResult = register(name, password);
//        if ("1".equals(registerResult) || "2".equals(registerResult)) {
//            Map<String, String> registerMap = login(name, password);
//            if ("1".equals(registerMap.get("loginResult"))) {
//                userId = registerMap.get("userId");
//                result = "1";
//                resultMap.put("loginResult", result);
//                resultMap.put("userId", userId);
//            }
//        }
//
//        return resultMap;
//    }

    public static Boolean isExsit(String name) {

        BmobQuery<user> query = new BmobQuery<user>();
        query.addQueryKeys(name);
        query.addWhereEqualTo("name", name);
        query.findObjects(new FindListener<user>() {

            @Override
            public void done(List<user> object, BmobException e) {
                if (e == null) {
                    if (object.size() >= 1) {
//                        Toast.makeText(getApplicationContext(), "该用户已存在！",
//                                Toast.LENGTH_SHORT).show();
                        isExsitResult = true;
//                        handler1.sendEmptyMessage(1);
                    } else {
                        isExsitResult = false;
//                        handler1.sendEmptyMessage(2);
                    }
                } else {
                    isExsitResult = true;
//                    handler1.sendEmptyMessage(1);
                    Toast.makeText(getApplicationContext(), "查询失败：" + e.getMessage() + e.toString(),
                            Toast.LENGTH_LONG).show();
                    Log.i("bmob", "失败：" + e.getMessage() + "," + e.getErrorCode());
                }
            }
        });
        return isExsitResult;

    }
}
