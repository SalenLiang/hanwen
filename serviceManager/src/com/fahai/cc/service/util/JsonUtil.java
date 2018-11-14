package com.fahai.cc.service.util;

import java.io.IOException;
import java.lang.reflect.Type;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;

import com.fahai.cc.service.customer.entity.Customer;
import com.fahai.cc.service.domain.entity.Domain;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonParseException;
import com.google.gson.JsonPrimitive;
import com.google.gson.JsonSerializationContext;
import com.google.gson.JsonSerializer;
import com.google.gson.TypeAdapter;
import com.google.gson.internal.LinkedTreeMap;
import com.google.gson.reflect.TypeToken;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonToken;
import com.google.gson.stream.JsonWriter;


public class JsonUtil {


    /**
     * 对象转为JSON
     * @param   bean 对象
     * @return  json对象
     */
    public static String beanToJson(Object bean){
        if(bean == null){
            return null;
        }
        Gson gson = new GsonBuilder().setDateFormat(DateUtil.DEFAULT_PATTERN_DATE_TIME).create();
        return  gson.toJson(bean);
    }

    /**
     * 对象转为美化后的JSON
     * @param bean  对象
     * @return  json对象
     */
    public static String beanToPrettyJson(Object bean){
        if(bean == null){
            return null;
        }
        Gson gson = new GsonBuilder().setDateFormat(DateUtil.DEFAULT_PATTERN_DATE_TIME).setPrettyPrinting().create();
        return  gson.toJson(bean);
    }

    /**
     * 使用默认的gson对象进行反序列化,获取单个对象
     *
     * @param json          对象
     * @param typeToken     typeToken
     * @return      clazz实例
     */
    public static <T> T fromJsonDefault(String json, TypeToken<T> typeToken) {
        if(StringUtils.isBlank(json)){
            return null;
        }
        Gson gson = new Gson();
        return gson.fromJson(json, typeToken.getType());
    }

    /**
     * 使用默认的gson对象进行反序列化,获取单个对象
     *
     * @param json          对象
     * @param clazz     clazz
     * @return      clazz实例
     */
    public static <T> T fromJsonDefault(String json, Class<T> clazz) {
        if(StringUtils.isBlank(json)){
            return null;
        }
        return fromJsonDefault(json,new TypeToken<T>(){});
    }

    /**
     *
     * @param json      json
     * @param clazz     class
     * @param <T>       反省
     * @return          bean的list集合
     */
    @Deprecated
    public static <T>List<T> fromJsonToList(String json,Class<T> clazz){
        Type type = new TypeToken<List<T>>() {}.getType();
        Gson gson = new GsonBuilder().registerTypeAdapter(Double.class, new JsonSerializer<Double>() {
            @Override
            public JsonElement serialize(Double src, Type typeOfSrc, JsonSerializationContext context) {
                if (src == src.longValue())
                    return new JsonPrimitive(src.longValue());
                return new JsonPrimitive(src);
            }
        }).create();
        return gson.fromJson(json, type);
    }

    /**
     * json转对象
     * @param json  json字符串
     * @param clazz clazz
     * @param <T>   clazz类型
     * @return      clazz类型实例
     */
    public static <T>T jsonToBean(String json,Class<T> clazz){
        if(StringUtils.isBlank(json)){
            return null;
        }
        Gson gson = new GsonBuilder().registerTypeAdapter(Date.class, new JsonDeserializer<Date>() {
            public Date deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {
                return new Date(json.getAsJsonPrimitive().getAsLong());
            }
        }).create();
        return gson.fromJson(json, clazz);
    }


    /**
     * json字符串转对象，解决int类型转换为double的问题
     * @param json          json
     * @return  转换后的对象
     */
    public static List<Map<String, String>> fromJsonToListMap(String json){
        if(StringUtils.isBlank(json)){
            return null;
        }
        //注册自己的处理Adapter
        Gson gson = new GsonBuilder()
                .registerTypeAdapter(new TypeToken<Map<String, Object>>() {}.getType(),
                        new CustomTypeAdapter())
                .create();
        return gson.fromJson(json,new TypeToken<List<Map<String, String>>>() {}.getType());
    }

    private static class CustomTypeAdapter<T> extends TypeAdapter<Object> {

        @Override
        public Object read(JsonReader in) throws IOException {
            JsonToken token = in.peek();
            System.out.println(token);
            switch (token) {
                case BEGIN_ARRAY:
                    List<Object> list = new ArrayList<Object>();
                    in.beginArray();
                    while (in.hasNext()) {
                        list.add(read(in));
                    }
                    in.endArray();
                    return list;

                case BEGIN_OBJECT:
                    Map<String, Object> map = new LinkedTreeMap<String, Object>();
                    in.beginObject();
                    while (in.hasNext()) {
                        map.put(in.nextName(), read(in));
                    }
                    in.endObject();
                    return map;

                case STRING:
                    return in.nextString();

                case NUMBER:
                    /**
                     * 改写数字的处理逻辑，将数字值分为整型与浮点型。
                     */
                    double dbNum = in.nextDouble();

                    // 数字超过long的最大值，返回浮点类型
                    if (dbNum > Long.MAX_VALUE) {
                        return dbNum;
                    }

                    // 判断数字是否为整数值
                    long lngNum = (long) dbNum;
                    if (dbNum == lngNum) {
                        return lngNum;
                    } else {
                        return dbNum;
                    }

                case BOOLEAN:
                    return in.nextBoolean();

                case NULL:
                    in.nextNull();
                    return null;

                default:
                    throw new IllegalStateException();
            }
        }
        @Override
        public void write(JsonWriter out, Object value) throws IOException {
            // 序列化无需实现
        }

    }
    
    //json串中存在时间字符串反序列化成date类型
    private class DateAdapter implements JsonDeserializer<Date>{
    	private final DateFormat format = new SimpleDateFormat("yyyy-MM-dd");
		@Override
		public Date deserialize(JsonElement jsonelement, Type type,
				JsonDeserializationContext jsondeserializationcontext) throws JsonParseException {
			try {
				return format.parse(jsonelement.getAsString());
			} catch (ParseException e) {
				e.printStackTrace();
			}
			return null;
		}
    	
    }
    //将json中的时间转为date类型
    public static <T>T toBean(String json,Class<T> clazz){
        if(StringUtils.isBlank(json)){
            return null;
        }
        Gson gson = new GsonBuilder().registerTypeAdapter(Date.class, new JsonDeserializer<Date>() {
        	private final DateFormat format = new SimpleDateFormat("yyyy-MM-dd");
            public Date deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {
            	try {
    				return format.parse(json.getAsString());
    			} catch (ParseException e) {
    				e.printStackTrace();
    			}
    				return null;
    			}
            }).create();
        return gson.fromJson(json, clazz);
    }
    
    public static void main(String[] args) {
        /*String json = "[{\"domainId\":9,\"domainCode\":null,\"domainName\":\"领域001\",\"status\":null,\"description\":null,\"lastModifyDate\":null,\"selectYN\":true,\"dimensionList\":[{\"dimensionId\":27,\"domainId\":9,\"domainName\":null,\"dimensionCode\":null,\"dimensionName\":\"维度001\",\"status\":null,\"description\":null,\"lastModifyDate\":null,\"fieldList\":[{\"fieldId\":11,\"dimensionId\":27,\"fieldCode\":\"FIELD_001_001\",\"fieldName\":\"字段001_001\",\"listShowYN\":null,\"defaultYN\":0,\"status\":null,\"description\":\"字段001属于维度001\",\"lastModifyDate\":null,\"selectYN\":false,\"$$hashKey\":\"object:760\"},{\"fieldId\":12,\"dimensionId\":27,\"fieldCode\":\"FIELD_001_002\",\"fieldName\":\"字段001_002\",\"listShowYN\":null,\"defaultYN\":1,\"status\":null,\"description\":\"该字段列出，默认不选中\",\"lastModifyDate\":null,\"selectYN\":true,\"$$hashKey\":\"object:761\"},{\"fieldId\":13,\"dimensionId\":27,\"fieldCode\":\"FIELD_001_003\",\"fieldName\":\"字段001_003\",\"listShowYN\":null,\"defaultYN\":0,\"status\":null,\"description\":\"FIELD_001_001\",\"lastModifyDate\":null,\"selectYN\":false,\"$$hashKey\":\"object:762\"}],\"selectYN\":true,\"$$hashKey\":\"object:357\"},{\"dimensionId\":28,\"domainId\":9,\"domainName\":null,\"dimensionCode\":null,\"dimensionName\":\"维度001_002\",\"status\":null,\"description\":null,\"lastModifyDate\":null,\"fieldList\":[{\"fieldId\":14,\"dimensionId\":28,\"fieldCode\":\"FIELD_001_002_001\",\"fieldName\":\"字段001_002_001\",\"listShowYN\":null,\"defaultYN\":0,\"status\":null,\"description\":\"001_002_001\",\"lastModifyDate\":null,\"selectYN\":false}],\"selectYN\":false,\"$$hashKey\":\"object:358\"}],\"$$hashKey\":\"object:331\"},{\"domainId\":10,\"domainCode\":null,\"domainName\":\"领域002\",\"status\":null,\"description\":null,\"lastModifyDate\":null,\"selectYN\":false,\"dimensionList\":[{\"dimensionId\":29,\"domainId\":10,\"domainName\":null,\"dimensionCode\":null,\"dimensionName\":\"维度002_001\",\"status\":null,\"description\":null,\"lastModifyDate\":null,\"fieldList\":[{\"fieldId\":15,\"dimensionId\":29,\"fieldCode\":\"FIELD_002_001\",\"fieldName\":\"字段_002_001\",\"listShowYN\":null,\"defaultYN\":0,\"status\":null,\"description\":\"_002_001\",\"lastModifyDate\":null,\"selectYN\":false}],\"selectYN\":false,\"$$hashKey\":\"object:376\"},{\"dimensionId\":30,\"domainId\":10,\"domainName\":null,\"dimensionCode\":null,\"dimensionName\":\"维度_002_002\",\"status\":null,\"description\":null,\"lastModifyDate\":null,\"fieldList\":[{\"fieldId\":null,\"dimensionId\":30,\"fieldCode\":null,\"fieldName\":null,\"listShowYN\":null,\"defaultYN\":null,\"status\":null,\"description\":null,\"lastModifyDate\":null,\"selectYN\":false}],\"selectYN\":false,\"$$hashKey\":\"object:377\"},{\"dimensionId\":31,\"domainId\":10,\"domainName\":null,\"dimensionCode\":null,\"dimensionName\":\"维度002_003\",\"status\":null,\"description\":null,\"lastModifyDate\":null,\"fieldList\":[{\"fieldId\":null,\"dimensionId\":31,\"fieldCode\":null,\"fieldName\":null,\"listShowYN\":null,\"defaultYN\":null,\"status\":null,\"description\":null,\"lastModifyDate\":null,\"selectYN\":false}],\"selectYN\":false,\"$$hashKey\":\"object:378\"}],\"$$hashKey\":\"object:332\"},{\"domainId\":11,\"domainCode\":null,\"domainName\":\"领域003\",\"status\":null,\"description\":null,\"lastModifyDate\":null,\"selectYN\":false,\"dimensionList\":[{\"dimensionId\":null,\"domainId\":11,\"domainName\":null,\"dimensionCode\":null,\"dimensionName\":null,\"status\":null,\"description\":null,\"lastModifyDate\":null,\"fieldList\":[],\"selectYN\":false,\"$$hashKey\":\"object:401\"}],\"$$hashKey\":\"object:333\"},{\"domainId\":12,\"domainCode\":null,\"domainName\":\"领域004\",\"status\":null,\"description\":null,\"lastModifyDate\":null,\"selectYN\":false,\"dimensionList\":[{\"dimensionId\":null,\"domainId\":12,\"domainName\":null,\"dimensionCode\":null,\"dimensionName\":null,\"status\":null,\"description\":null,\"lastModifyDate\":null,\"fieldList\":[],\"selectYN\":false,\"$$hashKey\":\"object:411\"}],\"$$hashKey\":\"object:334\"},{\"domainId\":13,\"domainCode\":null,\"domainName\":\"领域005\",\"status\":null,\"description\":null,\"lastModifyDate\":null,\"selectYN\":false,\"dimensionList\":[{\"dimensionId\":null,\"domainId\":13,\"domainName\":null,\"dimensionCode\":null,\"dimensionName\":null,\"status\":null,\"description\":null,\"lastModifyDate\":null,\"fieldList\":[],\"selectYN\":false,\"$$hashKey\":\"object:421\"}],\"$$hashKey\":\"object:335\"},{\"domainId\":14,\"domainCode\":null,\"domainName\":\"领域006\",\"status\":null,\"description\":null,\"lastModifyDate\":null,\"selectYN\":false,\"dimensionList\":[{\"dimensionId\":null,\"domainId\":14,\"domainName\":null,\"dimensionCode\":null,\"dimensionName\":null,\"status\":null,\"description\":null,\"lastModifyDate\":null,\"fieldList\":[],\"selectYN\":false,\"$$hashKey\":\"object:431\"}],\"$$hashKey\":\"object:336\"}]";
        Type type = new TypeToken<List<Domain>>() {}.getType();
        Gson gson = new GsonBuilder().registerTypeAdapter(Double.class, new JsonSerializer<Double>() {
            @Override
            public JsonElement serialize(Double src, Type typeOfSrc, JsonSerializationContext context) {
                if (src == src.longValue())
                    return new JsonPrimitive(src.longValue());
                return new JsonPrimitive(src);
            }
        }).create();
        List<Domain> domains= gson.fromJson(json, type);
        domains.forEach(domain -> System.out.println(domain.getDomainId()));*/
    	
    	
    	String json = "{\"companyName\":\"华联\",\"contactName\":\"小雪花\",\"contactTel\":\"6456456456\",\"contactEmail\":\"gfvadrd@grdasg\",\"userName\":\"fdsgvfsd\",\"contactPhone\":18945656562,\"customerArea\":\"0004\",\"trialBeginDate\":\"2017-06-28\",\"trialEndDate\":\"2017-07-01\",\"trialQuantity\":5555,\"contractBeginDate\":\"2017-06-27\",\"contractEndDate\":\"2017-06-28\",\"totalSearchQuantity\":4556555}";
    	
    	Customer bean = toBean(json, Customer.class);
    	
    	Customer jsonToBean = jsonToBean(json, Customer.class);
    	System.out.println(bean.getLastModifyDate());
    }

}
