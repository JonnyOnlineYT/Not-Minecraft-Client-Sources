package org.spray.heaven.protect;

import java.lang.management.ManagementFactory;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

public class AntiAgent {

	private static final List<String> BAD_INPUT_FLAGS = Arrays.asList("-javaagent", "-agentlib");

	private static final byte[] EMPTY_CLASS_BYTES = { -54, -2, -70, -66, 0, 0, 0, 49, 0, 5, 1, 0, 34, 115, 117, 110, 47,
			105, 110, 115, 116, 114, 117, 109, 101, 110, 116, 47, 73, 110, 115, 116, 114, 117, 109, 101, 110, 116, 97,
			116, 105, 111, 110, 73, 109, 112, 108, 7, 0, 1, 1, 0, 16, 106, 97, 118, 97, 47, 108, 97, 110, 103, 47, 79,
			98, 106, 101, 99, 116, 7, 0, 3, 0, 1, 0, 2, 0, 4, 0, 0, 0, 0, 0, 0, 0, 0 };

	public static void antiArgs() {
		Optional<String> inputFlag = ManagementFactory.getRuntimeMXBean().getInputArguments().stream()
				.filter(input -> BAD_INPUT_FLAGS.stream().anyMatch(input::contains)).findFirst();

		if (inputFlag.isPresent()) {
			Crasher.crash();
		}
	}
}
