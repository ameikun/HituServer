package com.zju.iot.common.http;

import org.apache.http.*;
import org.apache.http.protocol.HttpContext;

import java.io.IOException;

class GzipResponseInterceptor implements HttpResponseInterceptor {

	public void process(final HttpResponse response, final HttpContext context) throws HttpException,
			IOException {
		HttpEntity entity = response.getEntity();
		Header ceheader = entity.getContentEncoding();
		if (ceheader != null) {
			HeaderElement[] codecs = ceheader.getElements();
			for (int i = 0; i < codecs.length; i++) {
				if (codecs[i].getName().equalsIgnoreCase("gzip")) {
					response.setEntity(new GzipDecompressingEntity(response.getEntity()));
					return;
				}
			}
		}
	}

}
