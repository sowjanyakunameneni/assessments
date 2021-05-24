package com.java.assessment.clientmemberapp.controller;

import java.util.Arrays;
import java.util.List;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.java.assessment.clientmemberapp.model.Client;

public class TestUtils {

	@SuppressWarnings("rawtypes")
	public static List jsonToList(String json, TypeToken token) {
		return new Gson().fromJson(json, token.getType());
	}

	public static String objectToJson(Object obj) {
		return new Gson().toJson(obj);
	}

	public static <T> T jsonToObject(String json, Class<T> classOf) {
		return new Gson().fromJson(json, classOf);
	}

	public static List<Client> buildClients() {
		Client clientOne = new Client(1L, "Sachin", "Tendulkar", "9897928989", "5609132562089", "Mumbai");
		Client clientTwo = new Client(2L, "Rohith", "Sharma", "5656478454", "3204152365148", "Pune");
		List<Client> clientList = Arrays.asList(clientOne, clientTwo);
		return clientList;
	}

}
