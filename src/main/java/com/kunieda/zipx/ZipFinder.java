package com.kunieda.zipx;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;
import java.util.regex.Pattern;
import java.util.stream.Collectors;
import java.util.stream.Stream;

final class ZipFinder {

	private ZipFinder() {
	}

	/**
	 * 対象フォルダ配下からzipファイルを収集してリストとして返却する
	 */
	static List<Path> find(AppConfig cfg) throws IOException {
		Path root = cfg.targetDir();
		Pattern zipPattern = cfg.zipPattern();

		int depth = cfg.includeSubdirs() ? Integer.MAX_VALUE : 1;

		try (Stream<Path> stream = Files.walk(root, depth)) {
			return stream
					.filter(Files::isRegularFile)
					.filter(p -> zipPattern.matcher(p.getFileName().toString()).matches())
					.collect(Collectors.toList());
		}
	}
}
