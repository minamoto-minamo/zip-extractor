package com.kunieda.zipx;

import java.nio.file.Path;

final class SafePaths {
	private SafePaths() {
	}

	static Path safeRelativize(Path base, Path child) {
		if (base == null || child == null) return null;
		Path a = child.toAbsolutePath().normalize();
		Path b = base.toAbsolutePath().normalize();
		if (a.startsWith(b)) return b.relativize(a);
		return null;
	}
}
