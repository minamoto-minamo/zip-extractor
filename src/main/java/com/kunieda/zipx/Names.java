package com.kunieda.zipx;

final class Names {
	private Names() {
	}

	static String fileNameOnly(String path) {
		int idx = path.lastIndexOf('/');
		if (idx < 0) idx = path.lastIndexOf('\\');
		return (idx >= 0) ? path.substring(idx + 1) : path;
	}

	static String stem(String filename) {
		int dot = filename.lastIndexOf('.');
		return (dot > 0) ? filename.substring(0, dot) : filename;
	}
}
