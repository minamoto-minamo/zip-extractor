package com.kunieda.zipx;

import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.concurrent.atomic.AtomicInteger;

public final class App {
	public static void main(String[] args) {
		if (args.length != 1) {
			Log.e("Usage: java -jar zipx.jar <path/to/config.properties>");
			System.exit(2);
		}
		try {
			Path propPath = Paths.get(args[0]).toAbsolutePath().normalize();
			AppConfig config = PropertyLoader.load(propPath);
			Log.i("targetDir     : %s", config.targetDir());
			Log.i("resultRoot    : %s", Layout.resultRoot(config));
			Log.i("namePattern   : %s", config.namePattern());
			Log.i("zipCharset    : %s", config.zipCharset());
			Log.i("includeSubdirs: %s", config.includeSubdirs());
			Log.i("zipNameFolder : %s", config.createZipNameFolder());

			AtomicInteger zipCount = new AtomicInteger();
			AtomicInteger extractedCount = new AtomicInteger();

			ZipFinder.find(config).forEach(zip -> {
				zipCount.incrementAndGet();
				try {
					ZipEntryExtractor.extractMatching(zip, config, extractedCount);
				} catch (Exception ex) {
					Log.w(ex, "Extract failed: %s", zip);
				}
			});

			Log.i("DONE. zips=%d, extracted=%d", zipCount.get(), extractedCount.get());
		} catch (Exception e) {
			Log.e(e, "Fatal error");
			System.exit(1);
		}
	}
}
