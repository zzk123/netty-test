package javanoio.netty.privateAggreement.utils;

import com.google.gson.ExclusionStrategy;
import com.google.gson.FieldAttributes;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import lombok.extern.slf4j.Slf4j;

/**
 * @ClassName GsonUtil
 * @Description TODO
 * @Author zzk
 * @Date 2023/10/1 13:42
 **/
public class GsonUtil {
    private static final Gson GSON =
            new GsonBuilder().disableHtmlEscaping().setDateFormat("yyyy-MM-dd HH:mm:ss").serializeNulls().addSerializationExclusionStrategy(new ExclusionStrategy() {
                @Override
                public boolean shouldSkipField(FieldAttributes fieldAttributes) {
                    return false;
                }

                @Override
                public boolean shouldSkipClass(Class<?> aClass) {
                    return false;
                }
            }).create();

    public static String toJson(Object src) {
        return GSON.toJson(src);
    }

    public static <T> T parse(String str, Class<T> classOfT) {
        return GSON.fromJson(str, classOfT);
    }

    /**
     * 解析泛型
     *
     * @param json
     * @param typeToken
     * @param <T>
     * @return
     */
    public static <T> T parse(String json, TypeToken typeToken) {
        return GSON.fromJson(json, typeToken.getType());
    }
}