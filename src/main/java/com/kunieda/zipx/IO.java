package com.kunieda.zipx;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

final class IO {
	private IO() {
	}

	static void copy(InputStream in, OutputStream out) throws IOException {
		byte[] buf = new byte[8192];
		int r;
		while ((r = in.read(buf)) != -1) out.write(buf, 0, r);
	}
}
