package com.kunieda.zipx;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

final class Log {
	private static final DateTimeFormatter TS = DateTimeFormatter.ofPattern("uuuu-MM-dd HH:mm:ss");

	private Log() {
	}

	static void i(String fmt, Object... args) {
		System.out.println(prefix("INFO ") + String.format(fmt, args));
	}

	static void x(String fmt, Object... args) {
		System.out.println(prefix("XTRCT") + String.format(fmt, args));
	}

	static void w(Throwable t, String fmt, Object... args) {
		System.err.println(prefix("WARN ") + String.format(fmt, args));
		if (t != null) t.printStackTrace(System.err);
	}

	static void e(String fmt, Object... args) {
		System.err.println(prefix("ERROR") + String.format(fmt, args));
	}

	static void e(Throwable t, String fmt, Object... args) {
		e(fmt, args);
		if (t != null) t.printStackTrace(System.err);
	}

	private static String prefix(String level) {
		return "[" + TS.format(LocalDateTime.now()) + "][" + level + "] ";
	}
}
