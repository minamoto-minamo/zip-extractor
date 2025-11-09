package com.kunieda.zipx;

import java.nio.charset.Charset;
import java.nio.file.Path;
import java.util.Objects;
import java.util.regex.Pattern;

public final class AppConfig {
	private final Path targetDir;
	private final Pattern namePattern;
	private final Pattern zipPattern;
	private final Charset zipCharset;
	private final boolean includeSubdirs;
	private final String zipGlob;
	private final boolean createZipNameFolder;

	// コンストラクタに追加
	public AppConfig(Path targetDir,
	                 Pattern namePattern,
	                 Pattern zipPattern,        // ← 追加
	                 Charset zipCharset,
	                 boolean includeSubdirs,
	                 String zipGlob,
	                 boolean createZipNameFolder) {
		this.targetDir = Objects.requireNonNull(targetDir);
		this.namePattern = Objects.requireNonNull(namePattern);
		this.zipPattern = Objects.requireNonNull(zipPattern);
		this.zipCharset = Objects.requireNonNull(zipCharset);
		this.includeSubdirs = includeSubdirs;
		this.zipGlob = Objects.requireNonNull(zipGlob);
		this.createZipNameFolder = createZipNameFolder;
	}

	public Path targetDir() {
		return targetDir;
	}

	public Pattern namePattern() {
		return namePattern;
	}

	public Charset zipCharset() {
		return zipCharset;
	}

	public boolean includeSubdirs() {
		return includeSubdirs;
	}

	public String zipGlob() {
		return zipGlob;
	}


	// getter 追加
	public Pattern zipPattern() {
		return zipPattern;
	}

	public boolean createZipNameFolder() {
		return createZipNameFolder;
	}
}
