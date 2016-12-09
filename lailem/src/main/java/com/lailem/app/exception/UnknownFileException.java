package com.lailem.app.exception;

import java.io.IOException;

public class UnknownFileException extends IOException {

	public UnknownFileException() {
		super();
	}

	public UnknownFileException(String message, Throwable cause) {
		super(message, cause);
	}

	public UnknownFileException(String detailMessage) {
		super(detailMessage);
	}

	public UnknownFileException(Throwable cause) {
		super(cause);
	}

}
