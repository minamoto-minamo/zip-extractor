package com.kunieda.zipx;

import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Properties;
import java.util.regex.Pattern;

final class PropertyLoader {
	private PropertyLoader() {
	}

	static AppConfig load(Path propPath) throws IOException {
		Properties p = new Properties();
		try (InputStream in = Files.newInputStream(propPath)) {
			p.load(in);
		}

		String targetDirStr = trimToNull(p.getProperty("targetDir"));
		String patternStr = trimToNull(p.getProperty("namePattern"));
		String zipPatternStr = trimToNull(p.getProperty("zipPattern"));
		if (targetDirStr == null || patternStr == null) {
			throw new IllegalArgumentException("targetDir and namePattern are required");
		}

		Path targetDir = Paths.get(targetDirStr).toAbsolutePath().normalize();
		if (!Files.isDirectory(targetDir)) {
			throw new IllegalArgumentException("targetDir is not a directory: " + targetDir);
		}

		Pattern namePattern;
		try {
			namePattern = Pattern.compile(patternStr);
		} catch (Exception e) {
			throw new IllegalArgumentException("Invalid regex: namePattern=" + patternStr, e);
		}

		Pattern zipPattern;
		try {
			zipPattern = Pattern.compile(zipPatternStr);
		} catch (Exception e) {
			throw new IllegalArgumentException("Invalid regex: zipPatternStr=" + zipPatternStr, e);
		}

		String charsetName = trimToNull(p.getProperty("zipCharset"));
		Charset cs = Charset.forName(charsetName != null ? charsetName : Defaults.defaultZipCharset());

		boolean includeSubdirs = parseBool(p.getProperty("includeSubdirs"), true);
		String zipGlob = trimToNull(p.getProperty("zipGlob"));
		if (zipGlob == null) zipGlob = "**/*.zip";

		boolean zipNameFolder = parseBool(p.getProperty("createZipNameFolder"), true);

		return new AppConfig(targetDir, namePattern, zipPattern, cs, includeSubdirs, zipGlob, zipNameFolder);
	}

	private static boolean parseBool(String s, boolean def) {
		if (s == null) return def;
		return "true".equalsIgnoreCase(s.trim());
	}

	private static String trimToNull(String s) {
		if (s == null) return null;
		String t = s.trim();
		return t.isEmpty() ? null : t;
	}
}
