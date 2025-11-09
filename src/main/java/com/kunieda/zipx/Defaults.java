package com.kunieda.zipx;

final class Defaults {
	private Defaults() {
	}

	static String defaultZipCharset() {
		// Windows環境で日本語ZIPを想定（UTF-8運用なら "UTF-8" に変更）
		return "MS932";
	}
}
