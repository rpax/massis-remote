package com.massisframework.jsoninvoker.services;

import com.massisframework.jsoninvoker.reflect.JsonServiceResponseHandler;

public interface JsonMethod<R> {
	
	@FunctionalInterface
	public static interface JsonMethod1<M0, R> extends JsonMethod<R> {
		public void invoke(M0 p0, JsonServiceResponseHandler<R> handler);
	}
	@FunctionalInterface
	public static interface JsonMethod2<M0, M1, R> extends JsonMethod<R> {
		public void invoke(M0 p0, M1 p1,
				JsonServiceResponseHandler<R> handler);
	}
	@FunctionalInterface
	public static interface JsonMethod3<M0, M1, M2, R> extends JsonMethod<R> {
		public void invoke(M0 p0, M1 p1, M2 p2,
				JsonServiceResponseHandler<R> handler);
	}
	@FunctionalInterface
	public static interface JsonMethod4<M0, M1, M2, M3, R>
			extends JsonMethod<R> {
		public void invoke(M0 p0, M1 p1, M2 p2, M3 p3,
				JsonServiceResponseHandler<R> handler);
	}
	@FunctionalInterface
	public static interface JsonMethod5<M0, M1, M2, M3, M4, R>
			extends JsonMethod<R> {
		public void invoke(M0 p0, M1 p1, M2 p2, M3 p3, M4 p4,
				JsonServiceResponseHandler<R> handler);
	}
	

	

}