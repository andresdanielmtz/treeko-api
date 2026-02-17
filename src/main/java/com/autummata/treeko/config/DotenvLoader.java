package com.autummata.treeko.config;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;

public final class DotenvLoader {
	private DotenvLoader() {
	}

	public static void loadIfPresent(Path dotenvPath) {
		if (dotenvPath == null || !Files.exists(dotenvPath)) {
			return;
		}

		List<String> lines;
		try {
			lines = Files.readAllLines(dotenvPath, StandardCharsets.UTF_8);
		} catch (IOException e) {
			return;
		}

		for (String rawLine : lines) {
			if (rawLine == null) {
				continue;
			}

			String line = rawLine.trim();
			if (line.isEmpty() || line.startsWith("#")) {
				continue;
			}
			if (line.startsWith("export ")) {
				line = line.substring("export ".length()).trim();
			}

			int equals = line.indexOf('=');
			if (equals <= 0) {
				continue;
			}

			String key = line.substring(0, equals).trim();
			String value = line.substring(equals + 1).trim();
			if (key.isEmpty()) {
				continue;
			}

			value = stripOptionalQuotes(value);

			// Prefer real environment variables or already-set JVM properties.
			if (System.getenv(key) != null) {
				continue;
			}
			if (System.getProperty(key) != null) {
				continue;
			}

			System.setProperty(key, value);
		}
	}

	private static String stripOptionalQuotes(String value) {
		if (value == null) {
			return "";
		}
		if ((value.startsWith("\"") && value.endsWith("\"")) || (value.startsWith("'") && value.endsWith("'"))) {
			return value.substring(1, value.length() - 1);
		}
		return value;
	}
}
