package cheweibao.jason.com.carPlace.service;

import android.util.Log;
import android.widget.Toast;

import com.alibaba.fastjson.JSON;


import java.util.ArrayList;
import java.util.List;

import cheweibao.jason.com.carPlace.Bean.carPlace;
import cheweibao.jason.com.carPlace.Bean.user;
import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.datatype.BmobGeoPoint;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.FindListener;
import cn.bmob.v3.listener.QueryListener;
import cn.bmob.v3.listener.SaveListener;
import cn.bmob.v3.listener.UpdateListener;

import static cn.bmob.v3.Bmob.getApplicationContext;

/**
 * Created by JasonSong on 2017/4/18.
 */

public class placeService {
    static List<String> resultlist = new ArrayList();
    static Boolean available = false;
    static String pubObjectId;
    static String appObjectId;

    public static void publish(String ownerName, String name, String address, String price, BmobGeoPoint geoPoint, final String userId) {
        carPlace carplace = new carPlace();
        carplace.setOwner(ownerName);
        carplace.setAddress(address);
        carplace.setAvailable(true);
        carplace.setName(name);
        carplace.setGeoPoint(geoPoint);
        carplace.setPrice(price);
        carplace.save(new SaveListener<String>() {
            @Override
            public void done(String objectId, BmobException e) {
                if (e == null) {
                    pubObjectId = objectId;
                    Toast.makeText(getApplicationContext(), "车位发布成功!",
                            Toast.LENGTH_SHORT).show();
                    BmobQuery<user> query = new BmobQuery<user>();
                    query.addQueryKeys("havePlaces");
                    query.addQueryKeys("havePlace");
                    query.getObject(userId, new QueryListener<user>() {
                        @Override
                        public void done(user object, BmobException e) {
                            if (e == null) {
                                if (object.getHavePlace()) {
                                    List publishedList = object.getHavePlaces();
//                                    Toast.makeText(getApplicationContext(), "已申请车位查看成功！",
//                                            Toast.LENGTH_SHORT).show();
                                    publishedList.add(pubObjectId);
                                    user user = new user();
                                    user.setHavePlaces(publishedList);
                                    user.setHavePlace(true);
                                    user.update(userId, new UpdateListener() {
                                        @Override
                                        public void done(BmobException e) {
                                            if (e == null) {
//                                    Toast.makeText(getApplicationContext(), "已申请车位更新成功！",
//                                            Toast.LENGTH_SHORT).show();

                                            } else {
                                                Toast.makeText(getApplicationContext(), "已申请车位修改失败" + e.getMessage() + e.toString(),
                                                        Toast.LENGTH_SHORT).show();
                                                Log.i("bmob", "已申请车位更新失败：" + e.getMessage() + "," + e.getErrorCode());
                                            }
                                        }
                                    });
                                } else {
                                    Toast.makeText(getApplicationContext(), "没有发布车位！",
                                            Toast.LENGTH_SHORT).show();
                                }
                            } else {
                                Toast.makeText(getApplicationContext(), "查看失败！",
                                        Toast.LENGTH_LONG).show();
                                Log.i("bmob", "查看失败：" + e.getMessage() + "," + e.getErrorCode());
                            }
                        }
                    });

                    Log.i("bmob", "车位发布成功：" + objectId);
                } else {
//                    Toast.makeText(getApplicationContext(), "车位发布失败！" + e.getMessage() + "," + e.getErrorCode(),
//                            Toast.LENGTH_LONG).show();
                    Log.i("bmob", "车位发布失败：" + e.getMessage() + "," + e.getErrorCode());
                }
            }
        });
    }

    /**
     * 申请车位
     *
     * @param myUser  申请车位的用户名
     * @param placeId 申请车位的车位ID
     * @param userId  申请车位的用户ID
     */
    public static void apply(final String myUser, final String placeId, final String userId) {
        BmobQuery<carPlace> query = new BmobQuery<carPlace>();
        query.getObject(placeId, new QueryListener<carPlace>() {

            @Override
            public void done(carPlace object, BmobException e) {
                if (e == null) {
                    //获得available的信息
                    available = object.getAvailable();
                    if (!available) {
                        Toast.makeText(getApplicationContext(), "申请失败！车位已被占用！",
                                Toast.LENGTH_LONG).show();
                        return;
                    } else {
                        carPlace carplace = new carPlace();
                        carplace.setUser(myUser);
                        carplace.setAvailable(false);
                        carplace.update(placeId, new UpdateListener() {
                            @Override
                            public void done(BmobException e) {
                                if (e == null) {
                                    Toast.makeText(getApplicationContext(), "申请成功!",
                                            Toast.LENGTH_SHORT).show();
                                } else {
                                    Toast.makeText(getApplicationContext(), "申请失败！" + e.getMessage() + "," + e.getErrorCode(),
                                            Toast.LENGTH_LONG).show();
                                    Log.i("bmob", "失败：" + e.getMessage() + "," + e.getErrorCode());
                                }
                            }
                        });
                        BmobQuery<user> query = new BmobQuery<user>();
                        query.addQueryKeys("usePlaces");
                        query.addQueryKeys("usePlace");
                        query.getObject(userId, new QueryListener<user>() {
                            @Override
                            public void done(user object, BmobException e) {
                                if (e == null) {
                                    if (object.getUsePlace()) {
                                        List applyedList = object.getUsePlaces();
                                        applyedList.add(placeId);
                                        user user = new user();
                                        user.setUsePlaces(applyedList);
                                        user.setUsePlace(true);
                                        user.update(userId, new UpdateListener() {
                                            @Override
                                            public void done(BmobException e) {
                                                if (e == null) {
//                                    Toast.makeText(getApplicationContext(), "已申请车位更新成功！",
//                                            Toast.LENGTH_SHORT).show();

                                                } else {
                                                    Toast.makeText(getApplicationContext(), "已申请车位修改失败" + e.getMessage() + e.toString(),
                                                            Toast.LENGTH_SHORT).show();
                                                    Log.i("bmob", "已申请车位更新失败：" + e.getMessage() + "," + e.getErrorCode());
                                                }
                                            }

                                        });
//                        Toast.makeText(getApplicationContext(), "查看成功！",
//                                Toast.LENGTH_SHORT).show();
                                    } else {
                                        Toast.makeText(getApplicationContext(), "没有申请车位！",
                                                Toast.LENGTH_SHORT).show();
                                    }
                                } else {
                                    Toast.makeText(getApplicationContext(), "查看失败！",
                                            Toast.LENGTH_LONG).show();
                                    Log.i("bmob", "查看失败：" + e.getMessage() + "," + e.getErrorCode());
                                }
                            }
                        });


                    }
                } else {
                    Log.i("bmob", "查询失败：" + e.getMessage() + "," + e.getErrorCode());
                }
            }
        });

    }


    public static List getNearbyPlaceList(BmobGeoPoint geoPoint, int limit, double maxDistance) {
//        List<carPlace> resultlist = new ArrayList();
        BmobQuery<carPlace> bmobQuery = new BmobQuery<carPlace>();
        bmobQuery.addWhereWithinKilometers("geoPoint", geoPoint, maxDistance);
        bmobQuery.setLimit(limit);    //获取最接近用户地点的limit 条数据
        bmobQuery.findObjects(new FindListener<carPlace>() {
            @Override
            public void done(List<carPlace> object, BmobException e) {
                if (e == null) {
                    Toast.makeText(getApplicationContext(), "查询成功,共" + object.size() + "条数据。",
                            Toast.LENGTH_LONG).show();
                    for (carPlace carPlaces : object) {
                        resultlist.add(JSON.toJSONString(carPlaces));
                    }
                } else {
                    Toast.makeText(getApplicationContext(), "查询失败:" + e.getMessage(),
                            Toast.LENGTH_LONG).show();
                }
            }
        });
        return resultlist;
    }


    public static void cancelApply(final String placeId, final String userId) {
        BmobQuery<user> query = new BmobQuery<user>();
        query.addQueryKeys("usePlaces");
        query.addQueryKeys("usePlace");
        query.getObject(userId, new QueryListener<user>() {
            @Override
            public void done(user object, BmobException e) {
                if (e == null) {
                    if (object.getUsePlace()) {
                        List applyedList = object.getUsePlaces();
//                        Toast.makeText(getApplicationContext(), "查看成功！",
//                                Toast.LENGTH_SHORT).show();
                        applyedList.remove(placeId);
                        user user = new user();
                        user.setUsePlaces(applyedList);
                        if (applyedList.isEmpty()) {
                            user.setUsePlace(false);
                        }
                        user.update(userId, new UpdateListener() {
                            @Override
                            public void done(BmobException e) {
                                if (e == null) {
//                                    Toast.makeText(getApplicationContext(), "已申请车位更新成功！",
//                                            Toast.LENGTH_SHORT).show();

                                } else {
//                                    Toast.makeText(getApplicationContext(), "修改失败" + e.getMessage() + e.toString(),
//                                            Toast.LENGTH_LONG).show();
                                    Log.i("bmob", "已申请车位更新失败：" + e.getMessage() + "," + e.getErrorCode());
                                }
                            }

                        });
                    }
//                    else {
//                        Toast.makeText(getApplicationContext(), "没有申请车位！",
//                                Toast.LENGTH_SHORT).show();
//                    }
                } else {
                    Toast.makeText(getApplicationContext(), "查看失败！",
                            Toast.LENGTH_SHORT).show();
                    Log.i("bmob", "查看失败：" + e.getMessage() + "," + e.getErrorCode());
                }
            }
        });

        carPlace carplace = new carPlace();
        carplace.setUser("");
        carplace.setAvailable(true);
        carplace.update(placeId, new UpdateListener() {
            @Override
            public void done(BmobException e) {
                if (e == null) {
                    Toast.makeText(getApplicationContext(), "车位取消申请成功：",
                            Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(getApplicationContext(), "车位取消申请失败！" + e.getMessage() + "," + e.getErrorCode(),
                            Toast.LENGTH_LONG).show();
                    Log.i("bmob", "失败：" + e.getMessage() + "," + e.getErrorCode());
                }
            }
        });
    }

    public static void cancelPublish(final String placeId, final String userId) {
        BmobQuery<carPlace> query = new BmobQuery<carPlace>();
        query.getObject(placeId, new QueryListener<carPlace>() {

            @Override
            public void done(carPlace object, BmobException e) {
                if (e == null) {
                    //获得available的信息
                    String userName = object.getUser();
                    if (userName!=null&&userName.equals("")) {
                        Toast.makeText(getApplicationContext(), "车位取消发布失败！车位已被占用！",
                                Toast.LENGTH_SHORT).show();
                        return;
                    } else if (userName==null||userName.equals("")){
                        BmobQuery<user> query = new BmobQuery<user>();
                        query.addQueryKeys("havePlaces");
                        query.addQueryKeys("havePlace");
                        query.getObject(userId, new QueryListener<user>() {
                            @Override
                            public void done(user object, BmobException e) {
                                if (e == null) {
                                    if (object.getHavePlace()) {
                                        List publishedList = object.getHavePlaces();
                                        publishedList.remove(placeId);
                                        user user = new user();
                                        user.setHavePlaces(publishedList);
                                        if (publishedList.isEmpty()) {
                                            user.setHavePlace(false);
                                        }
                                        user.update(userId, new UpdateListener() {
                                            @Override
                                            public void done(BmobException e) {
                                                if (e == null) {
//                                                    Toast.makeText(getApplicationContext(), "车位取消发布更新成功！",
//                                                            Toast.LENGTH_SHORT).show();

                                                } else {
//                                    Toast.makeText(getApplicationContext(), "修改失败" + e.getMessage() + e.toString(),
//                                            Toast.LENGTH_LONG).show();
                                                    Log.i("bmob", "车位取消发布更新失败：" + e.getMessage() + "," + e.getErrorCode());
                                                }
                                            }
                                        });
//                        Toast.makeText(getApplicationContext(), "查看成功！",
//                                Toast.LENGTH_SHORT).show();
                                    }
//                                    else {
//                                        Toast.makeText(getApplicationContext(), "没有发布车位！",
//                                                Toast.LENGTH_LONG).show();
//                                    }
                                } else {
                                    Toast.makeText(getApplicationContext(), "查看失败！",
                                            Toast.LENGTH_LONG).show();
                                    Log.i("bmob", "查看失败：" + e.getMessage() + "," + e.getErrorCode());
                                }
                            }
                        });


                        carPlace carplace = new carPlace();
                        carplace.setUser("");
                        carplace.setOwner("");
                        carplace.setName("canceledPlace"+placeId);
                        carplace.setAvailable(false);
                        carplace.update(placeId, new UpdateListener() {
                            @Override
                            public void done(BmobException e) {
                                if (e == null) {
                                    Toast.makeText(getApplicationContext(), "车位取消发布成功。",
                                            Toast.LENGTH_SHORT).show();
                                } else {
                                    Toast.makeText(getApplicationContext(), "车位取消发布失败！" + e.getMessage() + "," + e.getErrorCode(),
                                            Toast.LENGTH_LONG).show();
                                    Log.i("bmob", "车位取消发布失败：" + e.getMessage() + "," + e.getErrorCode());
                                }
                            }
                        });
                    }
                } else {
                    Log.i("bmob", "查询失败：" + e.getMessage() + "," + e.getErrorCode());
                }
            }
        });

    }


}
