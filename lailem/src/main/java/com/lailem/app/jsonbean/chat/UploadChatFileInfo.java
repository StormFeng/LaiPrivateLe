package com.lailem.app.jsonbean.chat;

import com.google.gson.JsonSyntaxException;
import com.lailem.app.AppException;
import com.lailem.app.bean.Result;

public class UploadChatFileInfo extends Result {

	private FileInfo fileInfo;

	public static UploadChatFileInfo parse(String json) throws AppException {
		UploadChatFileInfo res = new UploadChatFileInfo();
		try {
			res = gson.fromJson(json, UploadChatFileInfo.class);
		} catch (JsonSyntaxException e) {
			e.printStackTrace();
			throw AppException.json(e);
		}
		return res;
	}

	public FileInfo getFileInfo() {
		return fileInfo;
	}

	public void setFileInfo(FileInfo fileInfo) {
		this.fileInfo = fileInfo;
	}

	public class FileInfo {
		private String fileName;
		private String fileType;
		private String duration;
		private String thumbnail;
        private String gThumbnail;

        public String getgThumbnail() {
            return gThumbnail;
        }

        public void setgThumbnail(String gThumbnail) {
            this.gThumbnail = gThumbnail;
        }

        public String getFileName() {
			return fileName;
		}

		public void setFileName(String fileName) {
			this.fileName = fileName;
		}

		public String getFileType() {
			return fileType;
		}

		public void setFileType(String fileType) {
			this.fileType = fileType;
		}

		public String getDuration() {
			return duration;
		}

		public void setDuration(String duration) {
			this.duration = duration;
		}

		public String getThumbnail() {
			return thumbnail;
		}

		public void setThumbnail(String thumbnail) {
			this.thumbnail = thumbnail;
		}

		@Override
		public String toString() {
			return "FileInfo [fileName=" + fileName + ", fileType=" + fileType + ", duration=" + duration + ", thumbnail=" + thumbnail + "]";
		}

	}

	@Override
	public String toString() {
		return "UploadChatFileInfo [fileInfo=" + fileInfo + "]";
	}

}
