package com.bigdata2017.mysite.service;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Calendar;

import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

@Service
public class FileUploadService {
	/* 경로를 '/'로 해 주면 root를 찾아가게하는데, window(c:\)와 linux(/)의 root경로가 다르다. -> '/'로 해 주면 JVM이 OS마다 변환해줌 */
	/* 현재는 '/'로 할 때, 프로젝트가 D:/에 있으니 D드라이브로 연결됨 */
	/* 줄 때, 물리적인 위치를 주지 말아라! 물리적인 위치와 서비스로 접근하는 url을 반드시 매핑시켜라! */
	/* 실제 파일이 저장되는 위치(tomcat/webapp/....)랑 url(webapp/....)이랑 위치가 완전히 다르니, 가상 URL로 매칭시켜주는 작업이 반드시 필요함 */
	private static String SAVE_PATH = "/uploads/";
	private static String PREFIX_URL= "/gallery/images/";
	public String restore(MultipartFile file) {
		String url = "";

		try {
			String originalName = file.getOriginalFilename();
			String extName = originalName.substring(originalName.lastIndexOf("."), originalName.length());

			String saveFileName = genSaveFileName(extName);

			writeFile(file, saveFileName);
			url = PREFIX_URL + saveFileName;
		} catch (IOException e) {
			new RuntimeException(e);
		}

		return url;
	}

	private void writeFile(MultipartFile file, String saveFileName) throws IOException {
		byte[] fileData = file.getBytes();
		FileOutputStream fos = new FileOutputStream(SAVE_PATH + saveFileName);

		fos.write(fileData);
		fos.close();
	}

	private String genSaveFileName(String extName) {
		String fileName = "";

		Calendar calendar = Calendar.getInstance();
		fileName += calendar.get(Calendar.YEAR);
		fileName += calendar.get(Calendar.MONTH);
		fileName += calendar.get(Calendar.DATE);
		fileName += calendar.get(Calendar.HOUR);
		fileName += calendar.get(Calendar.MINUTE);
		fileName += calendar.get(Calendar.SECOND);
		fileName += calendar.get(Calendar.MILLISECOND);

		fileName += extName;

		return fileName;
	}

	
	/**
	 * file에서 image 삭제
	 */
	public boolean delete(String imgURL) {
		String url = "";
		url = SAVE_PATH + imgURL.replace(PREFIX_URL, "");
		
		File file = new File(url);
		
		return file.delete();
	}


}
