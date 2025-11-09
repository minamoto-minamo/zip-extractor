package com.kunieda.zipx;

import java.io.IOException;
import java.nio.file.*;
import java.util.List;
import java.util.regex.Pattern;
import java.util.stream.Collectors;
import java.util.stream.Stream;

final class ZipFinder {

	private ZipFinder() {}

	static List<Path> find(AppConfig cfg) throws IOException {
		Path root = cfg.targetDir();
		Pattern zipPattern = cfg.zipPattern(); // ← プロパティから取得

		int depth = cfg.includeSubdirs() ? Integer.MAX_VALUE : 1;

		try (Stream<Path> stream = Files.walk(root, depth)) {
			return stream
					.filter(Files::isRegularFile)
					.filter(p -> zipPattern.matcher(p.getFileName().toString()).matches())
					.collect(Collectors.toList());
		}
	}
}
