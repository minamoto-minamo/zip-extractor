package com.kunieda.zipx;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardOpenOption;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.regex.Matcher;
import java.util.zip.ZipFile;

final class ZipEntryExtractor {
	private ZipEntryExtractor() {
	}

	static void extractMatching(Path zipPath, AppConfig cfg, AtomicInteger extractedCounter) throws IOException {
		Path absZip = zipPath.toAbsolutePath().normalize();
		Path resultRoot = Layout.resultRoot(cfg);
		Path zipBaseOut = Layout.baseOutputForZip(absZip, cfg);

		try (ZipFile zf = new ZipFile(absZip.toFile(), cfg.zipCharset())) {
			zf.stream()
					.filter(e -> !e.isDirectory())
					.forEach(entry -> {
						String filenameOnly = Names.fileNameOnly(entry.getName());
						Matcher m = cfg.namePattern().matcher(filenameOnly);
						if (!m.matches()) return;

						try {
							Path outPath = zipBaseOut.resolve(entry.getName());
							Path normalized = outPath.toAbsolutePath().normalize();

							// Zip Slip 防止
							if (!normalized.startsWith(resultRoot.toAbsolutePath().normalize())) {
								throw new SecurityException("Zip Slip detected: " + entry.getName());
							}

							Files.createDirectories(normalized.getParent());
							try (InputStream in = zf.getInputStream(entry);
							     OutputStream out = Files.newOutputStream(normalized,
									     StandardOpenOption.CREATE, StandardOpenOption.TRUNCATE_EXISTING, StandardOpenOption.WRITE)) {
								IO.copy(in, out);
							}
							Log.x("EXTRACT %s :: %s -> %s", absZip, entry.getName(), normalized);
							extractedCounter.incrementAndGet();
						} catch (Exception ex) {
							Log.w(ex, "Failed entry: %s :: %s", absZip, entry.getName());
						}
					});
		}
	}
}
