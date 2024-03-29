package org.baljinder;

/**
 * XML utils, including formatting.
 */
public class XmlUtils {
	private static XmlFormatter formatter = new XmlFormatter(2, 80);

	public static String formatXml(String s) {
		return formatter.format(s, 0);
	}

	public static String formatXml(String s, int initialIndent) {
		return formatter.format(s, initialIndent);
	}

	private static class XmlFormatter {
		private int indentNumChars;
		private int lineLength;
		private boolean singleLine;

		public XmlFormatter(int indentNumChars, int lineLength) {
			this.indentNumChars = indentNumChars;
			this.lineLength = lineLength;
		}

		public synchronized String format(String s, int initialIndent) {
			int indent = initialIndent;
			StringBuilder sb = new StringBuilder();
			for (int i = 0; i < s.length(); i++) {
				char currentChar = s.charAt(i);
				if (currentChar == '<') {
					char nextChar = s.charAt(i + 1);
					if (nextChar == '/')
						indent -= indentNumChars;
					if (!singleLine) // Don't indent before closing element if
										// we're creating opening and closing
										// elements on a single line.
						sb.append(buildWhitespace(indent));
					if (nextChar != '?' && nextChar != '!' && nextChar != '/')
						indent += indentNumChars;
					singleLine = false; // Reset flag.
				}
				sb.append(currentChar);
				if (currentChar == '>') {
					if (s.charAt(i - 1) == '/') {
						indent -= indentNumChars;
						sb.append("\n");
					} else {
						int nextStartElementPos = s.indexOf('<', i);
						if (nextStartElementPos > i + 1) {
							String textBetweenElements = s.substring(i + 1,
									nextStartElementPos);

							// If the space between elements is solely newlines,
							// let them through to preserve additional newlines
							// in source document.
							if (textBetweenElements.replaceAll("\n", "")
									.length() == 0) {
								sb.append(textBetweenElements + "\n");
							}
							// Put tags and text on a single line if the text is
							// short.
							else if (textBetweenElements.length() <= lineLength * 0.5) {
								sb.append(textBetweenElements);
								singleLine = true;
							}
							// For larger amounts of text, wrap lines to a
							// maximum line length.
							else {
								sb.append("\n"
										+ lineWrap(textBetweenElements,
												lineLength, indent, null)
										+ "\n");
							}
							i = nextStartElementPos - 1;
						} else {
							sb.append("\n");
						}
					}
				}
			}
			return sb.toString();
		}
	}

	private static String buildWhitespace(int numChars) {
		StringBuilder sb = new StringBuilder();
		for (int i = 0; i < numChars; i++)
			sb.append(" ");
		return sb.toString();
	}

	/**
	 * Wraps the supplied text to the specified line length.
	 * 
	 * @lineLength the maximum length of each line in the returned string (not
	 *             including indent if specified).
	 * @indent optional number of whitespace characters to prepend to each line
	 *         before the text.
	 * @linePrefix optional string to append to the indent (before the text).
	 * @returns the supplied text wrapped so that no line exceeds the specified
	 *          line length + indent, optionally with indent and prefix applied
	 *          to each line.
	 */
	private static String lineWrap(String s, int lineLength, int indent,
			String linePrefix) {
		if (s == null)
			return null;

		StringBuilder sb = new StringBuilder();
		int lineStartPos = 0;
		int lineEndPos;
		boolean firstLine = true;
		while (lineStartPos < s.length()) {
			if (!firstLine)
				sb.append("\n");
			else
				firstLine = false;

			if (lineStartPos + lineLength > s.length())
				lineEndPos = s.length() - 1;
			else {
				lineEndPos = lineStartPos + lineLength - 1;
				while (lineEndPos > lineStartPos
						&& (s.charAt(lineEndPos) != ' ' && s.charAt(lineEndPos) != '\t'))
					lineEndPos--;
			}
			sb.append(buildWhitespace(indent));
			if (linePrefix != null)
				sb.append(linePrefix);

			sb.append(s.substring(lineStartPos, lineEndPos + 1));
			lineStartPos = lineEndPos + 1;
		}
		return sb.toString();
	}

	// other utils removed for brevity
}