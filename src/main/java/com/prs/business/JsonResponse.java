package com.prs.business;

import java.util.ArrayList;

public class JsonResponse {

	private Object data = null;
	private Object errors = null;
	private Object meta = null;

	// Good response - single or multiple resource
	public JsonResponse(Object d) {
		data = d;
		meta = new ArrayList<>();
	}

	// 500 / error response
	public JsonResponse(Exception e) {
		errors = e;
	}

	public JsonResponse(String s) {
		errors = s;
	}

	public Object getData() {
		return data;
	}

	public void setData(Object data) {
		this.data = data;
	}

	public Object getErrors() {
		return errors;
	}

	public void setErrors(Object errors) {
		this.errors = errors;
	}

	public Object getMeta() {
		return meta;
	}

	public void setMeta(Object meta) {
		this.meta = meta;
	}

	// Good call - pass in the object to be returned
	public static JsonResponse getInstance(Object d) {
		return new JsonResponse(d);
	}

	// Unsuccessful call - pass in the exception thrown
	public static JsonResponse getInstance(Exception e) {
		return new JsonResponse(e);
	}

	// Used when we want to pass a message back to front end
	public static JsonResponse getInstance(String s) {
		return new JsonResponse(s);
	}
}