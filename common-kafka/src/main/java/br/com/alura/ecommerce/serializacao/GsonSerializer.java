package br.com.alura.ecommerce.serializacao;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import org.apache.kafka.common.serialization.Serializer;

import java.nio.charset.StandardCharsets;

public class GsonSerializer <T> implements Serializer<T> {
    private final Gson gson = new GsonBuilder().create();
    @Override
    public byte[] serialize(String s, T obj) {
        return gson.toJson(obj).getBytes(StandardCharsets.UTF_8);
    }
}
