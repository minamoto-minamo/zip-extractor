package com.kunieda.zipx;

import java.nio.file.Path;
import java.nio.file.Paths;

final class Layout {
	private Layout() {
	}

	static Path resultRoot(AppConfig cfg) {
		Path target = cfg.targetDir();
		String resultDirName = "result_" + target.getFileName().toString();
		Path parent = target.getParent();
		return (parent != null) ? parent.resolve(resultDirName) : Paths.get(resultDirName);
	}

	static Path baseOutputForZip(Path absZip, AppConfig cfg) {
		Path target = cfg.targetDir().toAbsolutePath().normalize();
		Path resultRoot = resultRoot(cfg).toAbsolutePath().normalize();
		Path relDir = SafePaths.safeRelativize(target, absZip.getParent());
		Path base = (relDir == null) ? resultRoot : resultRoot.resolve(relDir);

		if (cfg.createZipNameFolder()) {
			base = base.resolve(Names.stem(absZip.getFileName().toString()));
		}
		return base;
	}
}
