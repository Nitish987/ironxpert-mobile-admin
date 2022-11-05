package com.ironxpert.admin.common.db;

import android.content.Context;

import com.android.volley.Request;
import com.google.firebase.firestore.FirebaseFirestore;
import com.ironxpert.admin.common.auth.Auth;
import com.ironxpert.admin.common.settings.ApiKey;
import com.ironxpert.admin.utils.Promise;
import com.ironxpert.admin.utils.Server;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class Database {
    public static FirebaseFirestore getInstance() {
        return FirebaseFirestore.getInstance();
    }

    public static class OthersAuth {
        public static void createDeliveryAgent(Context context, String name, String phone, String email, Promise<String> promise) {
            Map<String, String> headers = new HashMap<>();
            headers.put("RAK", ApiKey.REQUEST_API_KEY);
            headers.put("AT", Auth.AUTH_TOKEN);
            headers.put("UID", Auth.getAuthUserUid());

            JSONObject agent = new JSONObject();
            try {
                agent.put("name", name);
                agent.put("phone", phone);
                agent.put("email", email);
            } catch (JSONException e) {
                e.printStackTrace();
                return;
            }

            Server.request(context, Request.Method.POST, ApiKey.REQUEST_API_URL + "admin/create-delivery-agent/", headers, agent, new Promise<JSONObject>() {
                        @Override
                        public void resolving(int progress, String msg) {
                            promise.resolving(progress, msg);
                        }

                        @Override
                        public void resolved(JSONObject data) {
                            try {
                                promise.resolved(data.getString("message"));
                            } catch (JSONException e) {
                                e.printStackTrace();
                                promise.reject("Something went wrong.");
                            }
                        }

                        @Override
                        public void reject(String err) {
                            promise.reject(err);
                        }
                    }
            );
        }

        public static void deleteDeliveryAgent(Context context, String uid, Promise<String> promise) {
            Map<String, String> headers = new HashMap<>();
            headers.put("RAK", ApiKey.REQUEST_API_KEY);
            headers.put("AT", Auth.AUTH_TOKEN);
            headers.put("UID", Auth.getAuthUserUid());

            Server.request(context, Request.Method.DELETE, ApiKey.REQUEST_API_URL + "admin/delete-delivery-agent/"  + uid + "/", headers, null, new Promise<JSONObject>() {
                        @Override
                        public void resolving(int progress, String msg) {
                            promise.resolving(progress, msg);
                        }

                        @Override
                        public void resolved(JSONObject data) {
                            try {
                                promise.resolved(data.getString("message"));
                            } catch (JSONException e) {
                                e.printStackTrace();
                                promise.reject("Something went wrong.");
                            }
                        }

                        @Override
                        public void reject(String err) {
                            promise.reject(err);
                        }
                    }
            );
        }
    }

    public static void addFood(Context context, String name, String photo, String description, String foodIncludes, String ingredients, String foodType, boolean available, int price, int discount, Promise<String> promise) {
        Map<String, String> headers = new HashMap<>();
        headers.put("RAK", ApiKey.REQUEST_API_KEY);
        headers.put("AT", Auth.AUTH_TOKEN);
        headers.put("UID", Auth.getAuthUserUid());

        JSONObject food = new JSONObject();
        try {
            food.put("name", name);
            food.put("photo", photo);
            food.put("description", description);
            food.put("food_includes", foodIncludes);
            food.put("ingredients", ingredients);
            food.put("food_type", foodType);
            food.put("available", available);
            food.put("price", price);
            food.put("discount", discount);
        } catch (JSONException e) {
            e.printStackTrace();
            return;
        }

        Server.request(context, Request.Method.POST, ApiKey.REQUEST_API_URL + "admin/add-food/", headers, food, new Promise<JSONObject>() {
                    @Override
                    public void resolving(int progress, String msg) {
                        promise.resolving(progress, msg);
                    }

                    @Override
                    public void resolved(JSONObject data) {
                        try {
                            promise.resolved(data.getString("message"));
                        } catch (JSONException e) {
                            e.printStackTrace();
                            promise.reject("Something went wrong.");
                        }
                    }

                    @Override
                    public void reject(String err) {
                        promise.reject(err);
                    }
                }
        );
    }

    public static void editFood(Context context, String foodId, String name, String photo, String description, String foodIncludes, String ingredients, String foodType, boolean available, int price, int discount, Promise<String> promise) {
        Map<String, String> headers = new HashMap<>();
        headers.put("RAK", ApiKey.REQUEST_API_KEY);
        headers.put("AT", Auth.AUTH_TOKEN);
        headers.put("UID", Auth.getAuthUserUid());

        JSONObject food = new JSONObject();
        try {
            food.put("name", name);
            food.put("photo", photo);
            food.put("description", description);
            food.put("food_includes", foodIncludes);
            food.put("ingredients", ingredients);
            food.put("food_type", foodType);
            food.put("available", available);
            food.put("price", price);
            food.put("discount", discount);
        } catch (JSONException e) {
            e.printStackTrace();
            return;
        }

        Server.request(context, Request.Method.PUT, ApiKey.REQUEST_API_URL + "admin/edit-food/" + foodId + "/", headers, food, new Promise<JSONObject>() {
                    @Override
                    public void resolving(int progress, String msg) {
                        promise.resolving(progress, msg);
                    }

                    @Override
                    public void resolved(JSONObject data) {
                        try {
                            promise.resolved(data.getString("message"));
                        } catch (JSONException e) {
                            e.printStackTrace();
                            promise.reject("Something went wrong.");
                        }
                    }

                    @Override
                    public void reject(String err) {
                        promise.reject(err);
                    }
                }
        );
    }

    public static void deleteFood(Context context, String foodId, Promise<String> promise) {
        Map<String, String> headers = new HashMap<>();
        headers.put("RAK", ApiKey.REQUEST_API_KEY);
        headers.put("AT", Auth.AUTH_TOKEN);
        headers.put("UID", Auth.getAuthUserUid());

        Server.request(context, Request.Method.DELETE, ApiKey.REQUEST_API_URL + "admin/delete-food/"  + foodId + "/", headers, null, new Promise<JSONObject>() {
                    @Override
                    public void resolving(int progress, String msg) {
                        promise.resolving(progress, msg);
                    }

                    @Override
                    public void resolved(JSONObject data) {
                        try {
                            promise.resolved(data.getString("message"));
                        } catch (JSONException e) {
                            e.printStackTrace();
                            promise.reject("Something went wrong.");
                        }
                    }

                    @Override
                    public void reject(String err) {
                        promise.reject(err);
                    }
                }
        );
    }

    public static void addTopping(Context context, String name, String photo, String foodIncludes, boolean available, int price, Promise<String> promise) {
        Map<String, String> headers = new HashMap<>();
        headers.put("RAK", ApiKey.REQUEST_API_KEY);
        headers.put("AT", Auth.AUTH_TOKEN);
        headers.put("UID", Auth.getAuthUserUid());

        JSONObject topping = new JSONObject();
        try {
            topping.put("name", name);
            topping.put("photo", photo);
            topping.put("food_includes", foodIncludes);
            topping.put("available", available);
            topping.put("price", price);
        } catch (JSONException e) {
            e.printStackTrace();
            return;
        }

        Server.request(context, Request.Method.POST, ApiKey.REQUEST_API_URL + "admin/add-topping/", headers, topping, new Promise<JSONObject>() {
                    @Override
                    public void resolving(int progress, String msg) {
                        promise.resolving(progress, msg);
                    }

                    @Override
                    public void resolved(JSONObject data) {
                        try {
                            promise.resolved(data.getString("message"));
                        } catch (JSONException e) {
                            e.printStackTrace();
                            promise.reject("Something went wrong.");
                        }
                    }

                    @Override
                    public void reject(String err) {
                        promise.reject(err);
                    }
                }
        );
    }

    public static void editTopping(Context context, String toppingId, String name, String photo, String foodIncludes, boolean available, int price, Promise<String> promise) {
        Map<String, String> headers = new HashMap<>();
        headers.put("RAK", ApiKey.REQUEST_API_KEY);
        headers.put("AT", Auth.AUTH_TOKEN);
        headers.put("UID", Auth.getAuthUserUid());

        JSONObject topping = new JSONObject();
        try {
            topping.put("name", name);
            topping.put("photo", photo);
            topping.put("food_includes", foodIncludes);
            topping.put("available", available);
            topping.put("price", price);
        } catch (JSONException e) {
            e.printStackTrace();
            return;
        }

        Server.request(context, Request.Method.PUT, ApiKey.REQUEST_API_URL + "admin/edit-topping/" + toppingId + "/", headers, topping, new Promise<JSONObject>() {
                    @Override
                    public void resolving(int progress, String msg) {
                        promise.resolving(progress, msg);
                    }

                    @Override
                    public void resolved(JSONObject data) {
                        try {
                            promise.resolved(data.getString("message"));
                        } catch (JSONException e) {
                            e.printStackTrace();
                            promise.reject("Something went wrong.");
                        }
                    }

                    @Override
                    public void reject(String err) {
                        promise.reject(err);
                    }
                }
        );
    }

    public static void deleteTopping(Context context, String toppingId, Promise<String> promise) {
        Map<String, String> headers = new HashMap<>();
        headers.put("RAK", ApiKey.REQUEST_API_KEY);
        headers.put("AT", Auth.AUTH_TOKEN);
        headers.put("UID", Auth.getAuthUserUid());

        Server.request(context, Request.Method.DELETE, ApiKey.REQUEST_API_URL + "admin/delete-topping/"  + toppingId + "/", headers, null, new Promise<JSONObject>() {
                    @Override
                    public void resolving(int progress, String msg) {
                        promise.resolving(progress, msg);
                    }

                    @Override
                    public void resolved(JSONObject data) {
                        try {
                            promise.resolved(data.getString("message"));
                        } catch (JSONException e) {
                            e.printStackTrace();
                            promise.reject("Something went wrong.");
                        }
                    }

                    @Override
                    public void reject(String err) {
                        promise.reject(err);
                    }
                }
        );
    }
}
